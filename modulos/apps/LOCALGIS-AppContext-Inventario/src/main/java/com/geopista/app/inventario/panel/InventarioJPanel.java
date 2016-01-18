/**
 * InventarioJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.BienJDialog;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.IMainInventario;
import com.geopista.app.inventario.InventarioInternalFrame;
import com.geopista.app.inventario.panel.bienesRevertibles.BienesRevertiblesJDialog;
import com.geopista.app.inventario.panel.lotes.LoteJDialog;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.Version;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.CampoFiltro;
import com.geopista.protocol.inventario.ConfigParameters;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.DerechoRealBean;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.protocol.inventario.SemovienteBean;
import com.geopista.protocol.inventario.ValorMobiliarioBean;
import com.geopista.protocol.inventario.VehiculoBean;
import com.geopista.protocol.inventario.ViaBean;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.util.exception.CancelException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 11-jul-2006
 * Time: 11:35:06
 * To change this template use File | Settings | File Templates.
 */
public class InventarioJPanel extends JPanel implements FeatureExtendedPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger= Logger.getLogger(InventarioJPanel.class);


    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;

    public TipoBienesJPanel tipoBienesJPanel;
    public BienesJPanel bienesJPanel;
    public BotoneraEditorJPanel botoneraJPanel;
    private Object[] listafeatures;
    GeopistaFeature gFeature;

    private InventarioClient inventarioClient= null;
    private Municipio municipio;
    private Object bienSeleccionado;

 
    private boolean fromGIS= false;
    private MapaJPanel mapaJPanel;
    private BotoneraAppJPanel botoneraAppJPanel;

    
    private ArrayList<CampoFiltro> filtro;

    /** la primera vez aparecera seleccionado en el dialogo de filtro */
    private boolean aplicarFiltro= true;
    private String cadenaBusqueda;

    /** Desde GIS. Si no se ha bloqueado la capa para hacer los cambios, lock es true (evitamos hacer cambios en la feature) */
    private boolean lock= false;
	private JDialogGeometryBienJPanel asociacionDialog;

    static final int INMUEBLE= 0;
    static final int MUEBLE= 1;
    static final int DERECHOS_REALES= 2;
    static final int VALOR_MOBILIARIO= 3;
    static final int CREDITO_DERECHO= 4;
    static final int SEMOVIENTE= 5;
    static final int VIAS= 6;
    static final int VEHICULOS= 7;

    boolean filtroEliminadosAplicado=false;
    boolean filtroBajasAplicado=false;
    
    /**
     * Método que genera el panel del inventario del patrimonio para las aplicaciones cliente
     * @param b true si la llamada se hace desde una aplicacion cliente, false si se hace desde el editor de cartografia.
     */
    public InventarioJPanel(boolean b) {
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        this.desktop= aplicacion.getMainFrame();
        this.locale= UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true);
        this.fromGIS= b;
        com.geopista.app.inventario.UtilidadesComponentes.inicializar();
        inventarioClient= new InventarioClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
        		ServletConstants.INVENTARIO_SERVLET_NAME);

        try{municipio= getMunicipio();}catch(Exception e){}
        initComponents();
        
        
        //Filtros para que no se muestren los bienes dados de baja y los borrados
        if (filtro==null){
            CampoFiltro campo= new CampoFiltro();
            campo.setNombre("borrado");
            campo.setTabla("bienes_inventario");
            campo.setVarchar();
            campo.setOperador("=");
            campo.setValorVarchar("0");

            CampoFiltro campo1= new CampoFiltro();
            campo1.setNombre("borrado");
            campo1.setTabla("bien_revertible");
            campo1.setVarchar();
            campo1.setOperador("=");
            campo1.setValorVarchar("0");

            filtro=new ArrayList<CampoFiltro>();
            
            
            filtro.add(campo);
            filtro.add(campo1);

        }
        
    }

    private void initComponents(){
        tipoBienesJPanel= new TipoBienesJPanel(desktop, locale);
        bienesJPanel= new BienesJPanel(locale);
        
        bienesJPanel.setPanelPadre(this);
        if (fromGIS){
            /** Para evitar que si no hay datos en bienesJPanel, el panel tipoBienesJPanel haga automaticamente un resize */
            tipoBienesJPanel.setPreferredSize(new Dimension(120, 400));
            tipoBienesJPanel.setMaximumSize(new Dimension(120, 400));
        }else{
            /** Para evitar que si no hay datos en bienesJPanel, el panel tipoBienesJPanel haga automaticamente un resize */
            tipoBienesJPanel.setPreferredSize(new Dimension(120, 580));
            tipoBienesJPanel.setMaximumSize(new Dimension(120, 580));
        }
        botoneraJPanel= new BotoneraEditorJPanel(desktop);
        botoneraJPanel.setEnabled(false);
        botoneraJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				botoneraJPanel_actionPerformed();
			}
		});

        tipoBienesJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				tipoBienesJPanel_actionPerformed();
			}
		});

        bienesJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
            	if (BienesJPanel.DOBLE_CLICK.equals(e.getActionCommand())){
            		bienesJPanel_dobleClick();
            	}else if (BienesJPanel.CHANGE_TAB.equals(e.getActionCommand())){
            		bienSeleccionado=null;
            		actualizaBotonera();
            	}else {
            		if (e.getActionCommand().equals(Const.SUPERPATRON_BIENES))
            			bienesJPanel_actionPerformed();
            		else if (e.getActionCommand().equals(Const.SUPERPATRON_REVERTIBLES))
            			bienesRevertibleJPanel_actionPerformed();
            		else if (e.getActionCommand().equals(Const.SUPERPATRON_LOTES))
            			lotesJPanel_actionPerformed();
            		else
            			bienesJPanel_actionPerformed();
            	}
			}
		});


        /* Cargamos la lista de features */
        Blackboard identificadores= aplicacion.getBlackboard();
        /* devuelve un array */
        listafeatures= (Object[])identificadores.get("feature");

        /** En caso de que estemos en el editor GIS solo se podrá seleccionar una feature cada vez */
        if((fromGIS) && ((listafeatures == null) || (listafeatures.length == 0) || (listafeatures.length > 1))) return;
        try{
            if (fromGIS) gFeature= (GeopistaFeature) listafeatures[0];
            this.setName( aplicacion.getI18nString("inventario.InventarioJPanel.name"));
            this.setLayout(new java.awt.BorderLayout());
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                                   tipoBienesJPanel, bienesJPanel);
            splitPane.setOneTouchExpandable(true);
            
            splitPane.setDividerLocation(150);
            this.add(splitPane, BorderLayout.CENTER);
            this.add(botoneraJPanel, BorderLayout.SOUTH);
            setPreferredSize(new Dimension(120, 700));
            setMaximumSize(new Dimension(200, 700));


        }catch(Exception e){
            logger.error("Error al mostrar el inventario de una feature ", e);
        }

    }
    private void bienesJPanel_dobleClick(){
    	//if (!botoneraJPanel.modificarJButtonActionPerformed())
    	//	if (botoneraAppJPanel != null) botoneraAppJPanel.consultarJButtonActionPerformed();
    	if (botoneraAppJPanel != null) botoneraAppJPanel.consultarJButtonActionPerformed();
    }
    private void bienesJPanel_actionPerformed(){
    	try{
    		
        /** Desde GIS. Si no se ha bloqueado la capa para hacer los cambios, lock es true (evitamos hacer cambios en la feature) */
        if (lock) return;
        
        boolean mismoBien=false;
        //Si el bien ya esta seleccionado, lo deseleccionamos
        if (bienSeleccionado!=null){
        	Object bienSeleccionadoTemp= bienesJPanel.getBienSeleccionado();
        	if (bienSeleccionadoTemp instanceof BienBean &&
        			bienSeleccionado instanceof BienBean){
	        	if (((BienBean)bienSeleccionadoTemp).getId()==
	        		((BienBean)bienSeleccionado).getId()){
	        		bienesJPanel.clearSelection();
	        		mapaJPanel.clear();
		            setFeaturesSelected(null);
		            bienSeleccionado=null;
		            mismoBien=true;

	        	}
        	}
        	else if (bienSeleccionadoTemp instanceof BienRevertible &&
        			bienSeleccionado instanceof BienRevertible){
	        	if (((BienRevertible)bienSeleccionadoTemp).getId()==
	        		((BienRevertible)bienSeleccionado).getId()){
	        		bienesJPanel.clearSelection();
	        		mapaJPanel.clear();
		            setFeaturesSelected(null);
		            mismoBien=true;

	        	}
        	}
        	else if (bienSeleccionadoTemp instanceof Lote &&
        			bienSeleccionado instanceof Lote){
	        	if (((Lote)bienSeleccionadoTemp).getId_lote()==
	        		((Lote)bienSeleccionado).getId_lote()){
	        		bienesJPanel.clearSelection();
	        		mapaJPanel.clear();
		            setFeaturesSelected(null);
		            mismoBien=true;

	        	}
        	}
        	actualizaBotonera();
        	if (mismoBien)
        		return;
        }


        
    	
    	bienSeleccionado= bienesJPanel.getBienSeleccionado();
    	bienSeleccionado=cargarBienInventario();
    

			if (asociacionDialog != null && asociacionDialog.isVisible()) {
				if (getMapaJPanel().getGeopistaEditor().getSelectionManager()
						.getFeatureSelection().getFeaturesWithSelectedItems()
						.size() != 0
						&& getBienesPanel().getBienesSeleccionados() != null
						&& getBienesPanel().getBienesSeleccionados().size() > 0) {
					asociacionDialog.setEnabledButtons(false);
				} else {
					asociacionDialog.setEnabledButtons(true);
				}
			} else if (mapaJPanel != null && bienSeleccionado != null) {
				/** marcamos la feature del bien seleccionado por el usuario */
				// if (!getTipoBienesPanel().isModoAsociacion()){
				mapaJPanel.refreshFeatureSelectionEIEL(
						((BienBean) bienSeleccionado).getIdFeatures(),
						((BienBean) bienSeleccionado).getIdLayers(),
						((BienBean) bienSeleccionado).getId());
				setFeaturesSelected(mapaJPanel.getGeopistaEditor()
						.getSelection());
				// } else if (asociacionDialog != null) {
				//
				// }

				// Insertamos en el Blackboard de la aplicacion las features
				// seleccionadas. Necesario para seleccionar los documentos de
				// BD
				Blackboard Identificadores = aplicacion.getBlackboard();
				try {
					Identificadores.put("feature", mapaJPanel
							.getGeopistaEditor().getSelection().toArray());
				} catch (Exception e) {
				}
			}
        
         
        actualizaBotonera();
    	}catch(Exception ex){
        	logger.error("Error al seleccionar un bien de la lista",ex);
        }
    	
    }
    
    private void actualizaBotonera(){

    	/*No se ha seleccionado un bien*/
    	if (bienSeleccionado==null){
    		if (Const.SUPERPATRON_BIENES.equals (tipoBienesJPanel.getTipoSeleccionado())||
    				Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO.equals (tipoBienesJPanel.getTipoSeleccionado()))
    		{
    			if(tipoBienesJPanel.getSubtipoSeleccionado()==null){
    				  botoneraJPanel.setEnabled(false);
    			      if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(false);
    			      return;
    			}else{
    				botoneraJPanel.setEnabled(false);
    				botoneraJPanel.annadirJButtonSetEnabled(true);
    			    if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(false);
    			    if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabledBuscarFiltrarInf();
    			    return;
    			}
    		}
    		if (Const.SUPERPATRON_REVERTIBLES.equals (tipoBienesJPanel.getTipoSeleccionado()))
    		{
    			botoneraJPanel.setEnabled(false);
    		    if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(false);
    		    if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabledBuscarFiltrarInf();
    			botoneraJPanel.annadirJButtonSetEnabled(true);
    			return;
    		}
    		if (Const.SUPERPATRON_LOTES.equals (tipoBienesJPanel.getTipoSeleccionado())){
    			botoneraJPanel.setEnabled(false);
    		    if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(false);
    		    if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabledBuscarFiltrarInf();
    			return;
    		}
    	}else {
	    	/*Se ha seleccionado un bien */
	    	botoneraJPanel.setEnabled(true);
	    	if (botoneraAppJPanel != null){
	    		botoneraAppJPanel.setEnabled(true);
	    		botoneraAppJPanel.setEnabledRecuperarJButton(false);
	    	}
	    	 /*El botón de recuperar solo se actualiza si tiene versión*/ 
	    	if (Const.SUPERPATRON_REVERTIBLES.equals (tipoBienesJPanel.getTipoSeleccionado())){
	    		if (((BienRevertible)bienSeleccionado).getRevisionExpirada()!= Long.parseLong("9999999999")){
		    		   botoneraJPanel.setEnabled(false);
		    		   botoneraJPanel.annadirJButtonSetEnabled(true);
		    		   if (botoneraAppJPanel != null)
	                   	botoneraAppJPanel.setEnabledRecuperarJButton(true);
	                   
		    		}
	    	}
			
            /*El botón de recuperar solo se actualiza si tiene versión*/ 
	    	if (Const.SUPERPATRON_BIENES.equals (tipoBienesJPanel.getTipoSeleccionado())||
				Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO.equals (tipoBienesJPanel.getTipoSeleccionado()))
	    	{
	    		if (bienSeleccionado instanceof BienBean){
	    			if (((BienBean)bienSeleccionado).getRevisionExpirada()!= Long.parseLong("9999999999")){
	    				botoneraJPanel.setEnabled(false);
	    				botoneraJPanel.annadirJButtonSetEnabled(true);
	    				botoneraJPanel.eliminarJButtonSetEnabled(true);
	    				if (botoneraAppJPanel != null)
	    					botoneraAppJPanel.setEnabledRecuperarJButton(true);
                   
	    			}
	    		}else{
	    			if (botoneraAppJPanel != null)
    					botoneraAppJPanel.setEnabled(false);
	    			botoneraJPanel.setEnabled(false);
    				botoneraJPanel.annadirJButtonSetEnabled(true);
	    		}	 	
    
	    	}
	    	if (Const.SUPERPATRON_LOTES.equals (tipoBienesJPanel.getTipoSeleccionado())){
	    		botoneraJPanel.annadirJButtonSetEnabled(false);
	    	}
	    		
      }
    }
    
    public void bienesRevertibleJPanel_actionPerformed(){
        Object obj= bienesJPanel.getBienRevertibleSeleccionado();
        bienSeleccionado= obj;
        actualizaBotonera();
    
    }
    
    public void lotesJPanel_actionPerformed(){
        Object obj= bienesJPanel.getLoteSeleccionado();
        bienSeleccionado= obj;
        actualizaBotonera();
    }
    public void tipoBienesJPanel_actionPerformed(){
        try{
        	bienesJPanel.setPanelesVisibles(
        			tipoBienesJPanel.getTipoSeleccionado()==null || tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_BIENES)
        			|| tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO),
        			tipoBienesJPanel.getTipoSeleccionado()!=null && tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES),
        			tipoBienesJPanel.getTipoSeleccionado()!=null && tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_LOTES));
        	
        	
        	//MEJORA_001. Aunque el tipo seleccionado sea null seguimos
        	//para adelante
        	/*if (tipoBienesJPanel.getTipoSeleccionado()==null) {
        		bienSeleccionado=null;
        		return; 
        	}*/
        	
        	//Reiniciamos el offset si se cambia entre bienes.
        	bienesJPanel.setActualOffset(0);
        	if (tipoBienesJPanel.getTipoSeleccionado()!=null && tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES)){
        		recargarTablaBienesRevertibles();
        		bienSeleccionado=bienesJPanel.getBienRevertibleSeleccionado();
        	}else if (tipoBienesJPanel.getTipoSeleccionado()!=null && tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_LOTES)){
        		recargarTablaLotes();
        		bienSeleccionado=bienesJPanel.getLoteSeleccionado();
        	}else {
        		
        		
	            /*Se ha seleccionado un tipo*/
        		
        		//MEJORA_001. Cuando el usuario no tiene seleccionado
        		//un epigrafe, buscamos a lo bestia el bien seleccionado
        		//en el mapa para ver si lo localizamos. No devolvemos 
        		//la solicitud como antes.
        		
        		
	        	if (tipoBienesJPanel.getSubtipoSeleccionado() == null){
	        		
	        			//if (listafeatures==null){
	        			if ((listafeatures==null) && (mapaJPanel!=null) && (mapaJPanel.getGeopistaEditor().getSelection().size()>0)){
	        				//do nothing
	        			}
	        			else if (!tipoBienesJPanel.isModoSeleccion()){
		            		bienesJPanel.loadListaBienes(null);
		            		if (mapaJPanel != null) mapaJPanel.clear();
		            		bienSeleccionado=null;
	        			}
	        			//}
	            		//return;
	            }
	            
	            
	            /** Cargamos las features seleccionadas en el mapa:
	             * - seleccionada directamente por el usuario.
	             * - seleccionada al seleccionar el usuario un tipo de bien resultado de la busqueda
	             */
	            if (mapaJPanel != null){
	                setFeaturesSelected(mapaJPanel.getGeopistaEditor().getSelection());
	                /** Insertamos en el Blackboard de la aplicacion las features seleccionadas. Necesario para seleccionar los documentos de BD */
	                Blackboard Identificadores= aplicacion.getBlackboard();
	                try{Identificadores.put("feature", mapaJPanel.getGeopistaEditor().getSelection().toArray());}catch (Exception e){}
	            }
	           
	            /** Desde el editor GIS, solo es posible seleccionar una unica feature. Desde la aplicacion pueden ser varias */
	            
	            /** La cadena de busqueda se pierde al cambiar de tipo de bien o seleccionar una feature.
	             *  Es necesario guardarlo en una variable global debido a que cada vez que abrimos y cerramos un dialogo (inmueble, nueble, vehiculos...)
	             * se recarga la lista de bienes para actualizar los bloqueos. Si no guardasemos la cadena de busqueda,
	             * al actualizar la lista de bienes se cargarían los de ese tipo, sin tener en cuenta la busqueda que ha hecho el usuario. */
	            cadenaBusqueda= null;
	            /** cargamos la lista de bienes en funcion del tipo seleccionado en el arbol */
	            
	            //MEJORA_001
	            if ((tipoBienesJPanel.getTipoSeleccionado()!=null) &&
	            		(tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))
	            		&&(listafeatures.length==0))
	            	recargarTablaBienesInventario(0);
	            //El modo de seleccion impide que estando en el epigrafe por ejemplo de inmuebles  y vayas al nodo
	            //raiz no se recargen todos los bienes.
	            else if (!tipoBienesJPanel.isModoSeleccion())
	            	return;
	            else if (((tipoBienesJPanel.getTipoSeleccionado()==null) ||
	            		(tipoBienesJPanel.getSubtipoSeleccionado()==null)) &&
	            		(listafeatures.length>0))
	            	recargarTablaBienesInventario(0,false);
	            
	            else if (((tipoBienesJPanel.getTipoSeleccionado()==null) ||
	            		(tipoBienesJPanel.getSubtipoSeleccionado()==null)) &&
	            		(listafeatures.length==0))
	            	return;
	            else	
	            	recargarTablaBienesInventario(0);
	            
	            bienSeleccionado=bienesJPanel.getBienSeleccionado();
	       
	        }
        }catch(Exception e){
            logger.error("Error al cargar la lista de bienes del tipo seleccionado", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
        }finally{
        	 actualizaBotonera();
        }


    }

    /**
     * Recoge las acciones realizadas sobre la botonera del editos GIS. Son BLOQUEANTES
     */
    public void botoneraJPanel_actionPerformed(){
        try{
            if (botoneraJPanel.getBotonPressed() == null) return;
            
            if(botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ASOCIAR_FEATURES)){
	           	
            	if(AppContext.getApplicationContext().getBlackboard().get(Const.ASSOCIATE_FEATURES_BIENES)==null)
            		callJDialogGeometryBien();
            	
                return;
            }            /** Comprobamos si hay algun subtipo seleccionado */
            if (tipoBienesJPanel.getSubtipoSeleccionado() == null &&
            (tipoBienesJPanel.getTipoSeleccionado()==null || 
             tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_BIENES) ||
             tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))) 
            				return;
            
            // Esta seleccionado muebles revertibles */
            if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES)){
            	if (botoneraJPanel.getBotonPressed().equals(Constantes.OPERACION_ANNADIR)){
            		BienRevertible auxRevertible=new BienRevertible();
                    if (municipio != null) {
            		    auxRevertible.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
                    }
                	
            		BienesRevertiblesJDialog aux= new BienesRevertiblesJDialog(desktop, 'e', locale,auxRevertible, Constantes.OPERACION_ANNADIR,tipoBienesJPanel.getSubtipoSeleccionado());
            		aux.setVisible(true);
            		recargarTablaBienesRevertibles();
            	}
            	if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            		bienSeleccionado=bienesJPanel.getBienRevertibleSeleccionado();
            		if (bienSeleccionado==null) return;
            		BienesRevertiblesJDialog aux= new BienesRevertiblesJDialog(desktop, 'e', locale,
            				(BienRevertible)bienSeleccionado, Constantes.OPERACION_MODIFICAR,tipoBienesJPanel.getSubtipoSeleccionado());
            		aux.setVisible(true);
            		recargarTablaBienesRevertibles();
            		bienSeleccionado=aux.getBienRevertible();
            		if (bienSeleccionado instanceof BienRevertible)
            			bienesJPanel.seleccionarBienRevertible((BienRevertible)bienSeleccionado);
    
            	}
            	if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_BORRAR)){
            		bienSeleccionado=bienesJPanel.getBienRevertibleSeleccionado();
            		if (bienSeleccionado==null) return;
            		if (confirm("inventario.bienes.tag2", "inventario.bienes.tag3")){
                        bienSeleccionado=inventarioClient.borrarInventario(bienSeleccionado);
                        recargarTablaBienesRevertibles();
                        bienesJPanel.seleccionarBienRevertible((BienRevertible)bienSeleccionado);
            		}
              		
            	}
            	if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ELIMINAR)){
            		bienSeleccionado=bienesJPanel.getBienRevertibleSeleccionado();
            		
            		if (confirm("inventario.bienes.tag2", "inventario.bienes.tag3")){
            			inventarioClient.eliminarInventario(null,bienesJPanel.getBienRevertibleSeleccionado());
            			//OpcionA no se borra fisicamente
            			 recargarTablaBienesRevertibles();
                         bienesJPanel.seleccionarBienRevertible((BienRevertible)bienSeleccionado);
                        //OpcionB se borra fisicamente 
                        //bienesJPanel.deleteBienRevertibleTabla(((BienRevertible)bienSeleccionado).getId().toString());
                    }
            		
            	}
            	if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
            		bienSeleccionado=bienesJPanel.getBienRevertibleSeleccionado();
            		abrirGestionDocumentalJDialog(Constantes.OPERACION_ANEXAR);
            	}
            	
            	  /** RECARGAMOS TABLA - refrescamos los bloqueos */
                if (mapaJPanel != null){
                	cargarMapa(); 
                }
                	
            	return;
            }
            
         // Esta seleccionado un lote */
            if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_LOTES)){
            	if (botoneraJPanel.getBotonPressed().equals(Constantes.OPERACION_ANNADIR)){
            		//No se puede añadir un lote, se añade cuando se crea el bien
            	}
            	if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) ||
            			botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            		bienSeleccionado=bienesJPanel.getLoteSeleccionado();
            		if (bienSeleccionado==null) return;
            		LoteJDialog aux= new LoteJDialog(desktop, 'e', locale,
            				(Lote)bienSeleccionado, botoneraJPanel.getBotonPressed());
            		aux.setVisible(true);
            		recargarTablaLotes();
                    bienesJPanel.seleccionarLote((Lote)bienSeleccionado);
             	}
            	
            	//Marcamos para borrado
            	if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_BORRAR)){
            		bienSeleccionado=bienesJPanel.getLoteSeleccionado();
            		if (confirm("inventario.bienes.tag2", "inventario.bienes.tag3")){
            			((Lote)bienSeleccionado).setFecha_baja(new Date());
            			inventarioClient.updateInventario(bienSeleccionado);
            			//Marcamos para borrado los bienes
            			if (confirm("inventario.bienes.tag9", "inventario.bienes.tag3")){
            				 for (Iterator<BienBean> it=((Lote)bienSeleccionado).getBienes().iterator();it.hasNext();)
            				 {
            					 BienBean bien=it.next();
            					 String bloqueado= inventarioClient.bloqueado((BienBean)bien);
            					 if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
            						 /** Mostramos mensaje de bloqueo del bien */
            						 JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.mensajes.tag1")+" "+bloqueado+"\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
            						 return;
            					 }else bloquearInventario(bien);
            					 desbloquearInventario((BienBean)inventarioClient.borrarInventario(bien));
            				 }
            			}	
            			recargarTablaLotes();
                        bienesJPanel.seleccionarLote((Lote)bienSeleccionado);
                      }
            	}
            	/*Operación eliminar lote*/
            	if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ELIMINAR)){
            		bienSeleccionado=bienesJPanel.getLoteSeleccionado();
            		if (confirm("inventario.bienes.tag8", "inventario.bienes.tag3")){
            			inventarioClient.eliminarInventario(null,bienSeleccionado);
            			//Eliminamos los bienes
            			if (confirm("inventario.bienes.tag10", "inventario.bienes.tag3")){
           				 for (Iterator<BienBean> it=((Lote)bienSeleccionado).getBienes().iterator();it.hasNext();)
           				 {
           					 BienBean bien=it.next();
           					 String bloqueado= inventarioClient.bloqueado((BienBean)bien);
           					 if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
           						 /** Mostramos mensaje de bloqueo del bien */
           						 JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.mensajes.tag1")+" "+bloqueado+"\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
           						 return;
           					 }else bloquearInventario(bien);
           					 desbloquearInventario((BienBean)inventarioClient.eliminarInventario(null,bien));
           				 }
            			}	
                        bienesJPanel.deleteLoteTabla(((Lote)bienSeleccionado).getId_lote().toString());
                      }
            	}
            	if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
            		bienSeleccionado=bienesJPanel.getLoteSeleccionado();
            		abrirGestionDocumentalJDialog(Constantes.OPERACION_ANEXAR);
            	}
            	
            	  /** RECARGAMOS TABLA - refrescamos los bloqueos */
                if (mapaJPanel != null){
                	cargarMapa();   
                }
            	return;
            }


            /** Operaciones que no necesitan abrir dialogo */
            if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_BORRAR) ||
                botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ELIMINAR)){
                String bloqueado= inventarioClient.bloqueado((BienBean)bienSeleccionado);
                 if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                    /** Mostramos mensaje de bloqueo del bien */
                    JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.mensajes.tag1")+" "+bloqueado+"\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
                    recargarTablaBienesInventario(bienesJPanel.getActualOffset());
                    if (bienSeleccionado instanceof BienBean){
                    	//bienSeleccionado=bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
                    	bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
                    }
                    return;
                }else bloquearInventario((BienBean)bienesJPanel.getBienSeleccionado());

                if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_BORRAR)){
                    if (confirm("inventario.bienes.tag2", "inventario.bienes.tag3")){
                    	bienSeleccionado=inventarioClient.borrarInventario(bienesJPanel.getBienSeleccionado());
                        bienesJPanel.updateBienTabla(desbloquearInventario((BienBean)bienSeleccionado));
                        bienesJPanel.clearSelection();
                        recargarTablaBienesInventario(bienesJPanel.getActualOffset());
                    }
                }else{
                	BienBean bien=(BienBean)bienesJPanel.getBienSeleccionado();
                	if (bien.isEliminado()){
	                    if (confirm("inventario.bienes.tag4.1", "inventario.bienes.tag5.1")){
	                        inventarioClient.eliminarInventarioNoRecover(bienesJPanel.getBienSeleccionado());	                        
	                        recargarTablaBienesInventario(bienesJPanel.getActualOffset());
	                    }            
                	}
                	else{
	                    if (confirm("inventario.bienes.tag4", "inventario.bienes.tag5")){
	                        bienesJPanel.deleteBienTabla((BienBean)/**por si el bien pertenece a varias features*/
	                        desbloquearInventario((BienBean)inventarioClient.eliminarInventario(listafeatures, bienesJPanel.getBienSeleccionado())));
	                        updateFeatures();
	                        //RECARGA CONDICIONAL. QUITAMOS LA SELECCION DEL ELEMENTO
	                        mapaJPanel.clear();
	    		            setFeaturesSelected(null);
	    		            
	                        recargarTablaBienesInventario(bienesJPanel.getActualOffset());
	                    }
                	}
                }
                /** RECARGAMOS TABLA - refrescamos los bloqueos */
                if (mapaJPanel != null){
                	cargarMapa();                
                }
                if (bienSeleccionado instanceof BienBean){
                	//bienSeleccionado=bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
                	bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
                }
                return;
            }

            /** Operaciones que abren el dialogo */
             
            if ((listafeatures==null || listafeatures.length==0) && botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
            	//if (!confirm("No ha seleccionado ninguna parcela. \n ¿Desea continuar?","AVISO"))
                if (!confirm("inventario.bienes.continuarseleccion","AVISO"))
            		return;
            	
            } 
            
            if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS) ||
                tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
                abrirDialogo(INMUEBLE,tipoBienesJPanel.getSubtipoSeleccionado());
            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART) ||
                      tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
                abrirDialogo(MUEBLE, tipoBienesJPanel.getSubtipoSeleccionado());
            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_DERECHOS_REALES)){
                abrirDialogo(DERECHOS_REALES);
            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_VALOR_MOBILIARIO)){
                abrirDialogo(VALOR_MOBILIARIO);
            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
                abrirDialogo(CREDITO_DERECHO);
            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_SEMOVIENTES)){
                abrirDialogo(SEMOVIENTE);
            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_VIAS_PUBLICAS_URBANAS) ||
                      tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
                abrirDialogo(VIAS, tipoBienesJPanel.getSubtipoSeleccionado());
            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_VEHICULOS)){
                abrirDialogo(VEHICULOS);
            }
            if (bienSeleccionado instanceof BienBean){
            	//bienSeleccionado=bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
            	bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
            }
            

        }catch (Exception e){
        	e.printStackTrace();
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
        }finally{
        	actualizaBotonera();
        }

        botoneraJPanel.setBotonPressed(null);
        if (botoneraAppJPanel!=null)  botoneraAppJPanel.setBotonPressed(null);

    }

    private void callJDialogGeometryBien(){
    	asociacionDialog = new JDialogGeometryBienJPanel(
    			AppContext.getApplicationContext().getMainFrame(),
    			aplicacion.getI18nString("inventario.nonmodal.associatebienesgeom.title"),this);
//    	this.getTipoBienesPanel().setModoAsociacion(true);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		asociacionDialog.setLocation(d.width / 7, d.height / 15);
		asociacionDialog.setVisible(true);
    }

	/**
     * Recoge las acciones realizadas sobre la botonera de la aplicacion. No son BLOQUEANTES
     */
    private void botoneraAppJPanel_actionPerformed(){
        try{
            /** Las operaciones con esta botonera no son BLOQUEANTES */
            if (botoneraAppJPanel==null) return;
            if (botoneraAppJPanel.getBotonPressed() == null) return;
            /** Comprobamos si hay algun subtipo seleccionado */
            if ((tipoBienesJPanel.getSubtipoSeleccionado() == null)  && 
                !(tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_LOTES)) &&
                !(tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES))) return;

            /* Se trata de un lote */
            if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_LOTES)){
            	//Operacion consultar
            	if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            		bienSeleccionado=bienesJPanel.getLoteSeleccionado();
            		if (bienSeleccionado==null) return;
            		LoteJDialog aux= new LoteJDialog(desktop, 'e', locale,
            				(Lote)bienSeleccionado, Constantes.OPERACION_CONSULTAR);
            		aux.setVisible(true);
            		recargarTablaLotes();
                    bienesJPanel.seleccionarLote((Lote)bienSeleccionado);
                    actualizaBotonera();
                    return;
             	}
             }
            /* Se trata de un bien Revertible */
            if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES)){
            	//Operacion consultar
            	if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            		bienSeleccionado=bienesJPanel.getBienRevertibleSeleccionado();
            		if (bienSeleccionado==null) return;
            	    BienesRevertiblesJDialog aux= new BienesRevertiblesJDialog(desktop, 'e', locale,
            				(BienRevertible)bienSeleccionado, Constantes.OPERACION_CONSULTAR,tipoBienesJPanel.getSubtipoSeleccionado());
            		aux.setVisible(true);
            		if (bienSeleccionado instanceof BienRevertible)
            			bienesJPanel.seleccionarBienRevertible((BienRevertible)bienSeleccionado);
                    actualizaBotonera();
                    return;
             	}
            	if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_RECUPERAR)){
            		 bienSeleccionado=bienesJPanel.getBienRevertibleSeleccionado();
            	     if (bienSeleccionado==null) return;
                     if (confirm("inventario.bienes.tag6", "inventario.bienes.tag7")){
                     	 bienSeleccionado=inventarioClient.recuperarInventario(bienSeleccionado);
                     	 updateVersion();
                         recargarTablaBienesRevertibles();
                     	if (bienSeleccionado instanceof BienRevertible)
                			bienesJPanel.seleccionarBienRevertible((BienRevertible)bienSeleccionado);
        
                         actualizaBotonera();
            		 }
                     return;
            	}
                	
             }
            	
            if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
                if (bienSeleccionado == null) return;
                bienSeleccionado=cargarBienInventario();
                if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS) ||
                    tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
                    abrirDialogoInmueble(Constantes.OPERACION_CONSULTAR,tipoBienesJPanel.getSubtipoSeleccionado());
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)){
                    abrirDialogoMueble(Constantes.OPERACION_CONSULTAR, Const.PATRON_MUEBLES_HISTORICOART);
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
                    abrirDialogoMueble(Constantes.OPERACION_CONSULTAR, Const.PATRON_BIENES_MUEBLES);
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_DERECHOS_REALES)){
                    abrirDialogoDerechosReales(Constantes.OPERACION_CONSULTAR);
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_VALOR_MOBILIARIO)){
                    abrirDialogoValorMobiliario(Constantes.OPERACION_CONSULTAR);
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
                    abrirDialogoCreditoDerecho(Constantes.OPERACION_CONSULTAR);
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_SEMOVIENTES)){
                    abrirDialogoSemoviente(Constantes.OPERACION_CONSULTAR);
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
                    abrirDialogoVias(Constantes.OPERACION_CONSULTAR, Const.PATRON_VIAS_PUBLICAS_RUSTICAS);
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_VIAS_PUBLICAS_URBANAS)){
                    abrirDialogoVias(Constantes.OPERACION_CONSULTAR, Const.PATRON_VIAS_PUBLICAS_URBANAS);
                }else if (tipoBienesJPanel.getSubtipoSeleccionado().equals(Const.PATRON_VEHICULOS)){
                    abrirDialogoVehiculo(Constantes.OPERACION_CONSULTAR);
                }
            }else if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_BUSCAR)){
                abrirDialogoBusqueda();
            }else if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_FILTRAR)){
                abrirDialogoFiltro();
            }else if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_INFORMES)){
                abrirDialogoInformes();
            }else if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_RECUPERAR)){
                String bloqueado= inventarioClient.bloqueado((BienBean)bienSeleccionado);
                if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                	/** Mostramos mensaje de bloqueo del bien */
                    JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.mensajes.tag1")+" "+bloqueado+"\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
                    recargarTablaBienesInventario(bienesJPanel.getActualOffset());
                    return;
                }else bloquearInventario((BienBean)bienesJPanel.getBienSeleccionado());

                if (confirm("inventario.bienes.tag6", "inventario.bienes.tag7")){
                	bienesJPanel.updateBienTabla(desbloquearInventario((BienBean)inventarioClient.recuperarInventario(bienesJPanel.getBienSeleccionado())));
                    bienesJPanel.clearSelection();
                }
                /** RECARGAMOS TABLA - refrescamos los bloqueos */
                if (mapaJPanel != null){
                	cargarMapa();                
                }
                recargarTablaBienesInventario(bienesJPanel.getActualOffset());
                actualizaBotonera();
                return;
            }

        }catch (Exception e){
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
        }

        botoneraJPanel.setBotonPressed(null);
        botoneraAppJPanel.setBotonPressed(null);

    }


    /**
     * Abre el dialogo para un bien inmueble
     * @param operacion que realiza el usuario
     * @throws Exception
     */
    private void abrirDialogoInmueble(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        	InmuebleJDialog inmuebleJDialog= new InmuebleJDialog(desktop, locale, operacion,tipo);

        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		
        		bienesJPanel.clearSelection();        		
        		try{
        			inmuebleJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		InmuebleBean inmueble= new InmuebleBean();
        		inmueble.setTipo(tipoBienesJPanel.getSubtipoSeleccionado());
        		if (municipio != null) {
        			inmueble.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
        		}
        		inmuebleJDialog.load(inmueble, true);
        		if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
        			inmuebleJDialog.pmsChecked();
        		}
        		else if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES)){
        			inmuebleJDialog.setRevertible();
        		}
        		
//        		addBienSicalwin();
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		
        		if (bienSeleccionado==null) return;
        		if (!(bienSeleccionado instanceof InmuebleBean)) return;
        		try{
        			inmuebleJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));
        		}catch (Exception e){}
            	inmuebleJDialog.load((InmuebleBean)bienSeleccionado, true);
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
        		
        		if (bienSeleccionado==null) return;
        		if (!(bienSeleccionado instanceof InmuebleBean)) return;
        		try{inmuebleJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
        		inmuebleJDialog.load((InmuebleBean)bienSeleccionado, false);
        	}

        	inmuebleJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			pantallaAction(operacion, (BienJDialog)e.getSource());
        		}
        	});

        	GUIUtil.centreOnWindow(inmuebleJDialog);
        	inmuebleJDialog.setVisible(true);
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Abre el dialogo de un bien mueble
     * @param operacion operacion que realiza el usuario
     * @param tipo de mueble (Hist. Artistico)
     * @throws Exception
     */
    private void abrirDialogoMueble(final String operacion, String tipo) throws Exception{
        if (tipo == null) return;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
            MuebleJDialog muebleJDialog= new MuebleJDialog(desktop, locale, tipo);
            muebleJDialog.setOperacion(operacion);

            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                bienesJPanel.clearSelection();
                try{muebleJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag1"));}catch (Exception e){}
                MuebleBean mueble= new MuebleBean();
                mueble.setTipo(tipoBienesJPanel.getSubtipoSeleccionado());
                if (municipio != null) mueble.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
                muebleJDialog.load(mueble, true);
                if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
        			muebleJDialog.pmsChecked();
        		}
                
            }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                if (!(bienSeleccionado instanceof MuebleBean)) return;
                try{muebleJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));}catch (Exception e){}
                muebleJDialog.load((MuebleBean)bienSeleccionado, true);
            }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
                if (!(bienSeleccionado instanceof MuebleBean)) return;
                try{muebleJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
                muebleJDialog.load((MuebleBean)bienSeleccionado, false);
            }

            muebleJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    pantallaAction(operacion,(BienJDialog) e.getSource());
                }
            });
            GUIUtil.centreOnWindow(muebleJDialog);
            muebleJDialog.setVisible(true);
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            actualizaBotonera();
        }
    }

     /**
      * Abre el dialogo de un bien de tipo Derechos Reales
      * @param operacion que realiza el usuario
      * @throws Exception
      */
     private void abrirDialogoDerechosReales(final String operacion) throws Exception{
         this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         try{
        	JDialogDerechosReales jDialogDerechosReales= new JDialogDerechosReales(desktop, locale);
            jDialogDerechosReales.setOperacion(operacion);

            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                bienesJPanel.clearSelection();
                try{jDialogDerechosReales.setTitle(aplicacion.getI18nString("inventario.dialogo.tag1"));}catch (Exception e){}
                DerechoRealBean bien= new DerechoRealBean();
                bien.setTipo(tipoBienesJPanel.getSubtipoSeleccionado());
                if (municipio != null) bien.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
                jDialogDerechosReales.load(bien, true);
                if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
        			jDialogDerechosReales.pmsChecked();
        		}
            }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                if (bienSeleccionado==null) return;
                if (!(bienSeleccionado instanceof DerechoRealBean)) return;
                try{jDialogDerechosReales.setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));}catch (Exception e){}
                jDialogDerechosReales.load((DerechoRealBean)bienSeleccionado, true);
            }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
                if (bienSeleccionado==null) return;
                if (!(bienSeleccionado instanceof DerechoRealBean)) return;
                try{jDialogDerechosReales.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
                jDialogDerechosReales.load((DerechoRealBean)bienSeleccionado, false);
            }

            jDialogDerechosReales.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                   pantallaAction(operacion, (BienJDialog)e.getSource());
                }
            });

            GUIUtil.centreOnWindow(jDialogDerechosReales);
            jDialogDerechosReales.setVisible(true);
         }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        	actualizaBotonera();
         }
    }

    /**
     * Abre el dialogo de un bien de tipo Valor Mobiliario
     * @param operacion que realiza el usuario
     * @throws Exception
     */
     private void abrirDialogoValorMobiliario(final String operacion) throws Exception{
         this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         try{
        	JDialogValorMobiliario valorMobiliarioJDialog= new JDialogValorMobiliario(desktop, locale);
            valorMobiliarioJDialog.setOperacion(operacion);

            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                bienesJPanel.clearSelection();
                try{valorMobiliarioJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag1"));}catch (Exception e){}
                ValorMobiliarioBean bien= new ValorMobiliarioBean();
                bien.setTipo(tipoBienesJPanel.getSubtipoSeleccionado());
                if (municipio != null) bien.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
                valorMobiliarioJDialog.load(bien, true);
                if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
                	valorMobiliarioJDialog.pmsChecked();
        		}
            }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                if (bienSeleccionado==null) return;
                if (!(bienSeleccionado instanceof ValorMobiliarioBean)) return;
                try{valorMobiliarioJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));}catch (Exception e){}
                valorMobiliarioJDialog.load((ValorMobiliarioBean)bienSeleccionado, true);
            }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
                if (bienSeleccionado==null) return;
                if (!(bienSeleccionado instanceof ValorMobiliarioBean)) return;
                try{valorMobiliarioJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
                valorMobiliarioJDialog.load((ValorMobiliarioBean)bienSeleccionado, false);
            }

            valorMobiliarioJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    pantallaAction(operacion,(BienJDialog)e.getSource());
                }
            });

            GUIUtil.centreOnWindow(valorMobiliarioJDialog);
            valorMobiliarioJDialog.setVisible(true);
         }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            actualizaBotonera();
         }
    }

    private void abrirDialogoCreditoDerecho(final String operacion) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
           CreditosDerechosJDialog creditosDerechosJDialog= new CreditosDerechosJDialog(desktop, locale);
           creditosDerechosJDialog.setOperacion(operacion);

           if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
               bienesJPanel.clearSelection();
               try{creditosDerechosJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag1"));}catch (Exception e){}
               CreditoDerechoBean credito= new CreditoDerechoBean();
               credito.setTipo(tipoBienesJPanel.getSubtipoSeleccionado());
               if (municipio != null) credito.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
               creditosDerechosJDialog.load(credito, true);
               if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
               		creditosDerechosJDialog.pmsChecked();
       		   }
           }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
               if (bienSeleccionado==null) return;
               if (!(bienSeleccionado instanceof CreditoDerechoBean)) return;
               try{creditosDerechosJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));}catch (Exception e){}
               creditosDerechosJDialog.load((CreditoDerechoBean)bienSeleccionado, true);
           }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
               if (bienSeleccionado==null) return;
               if (!(bienSeleccionado instanceof CreditoDerechoBean)) return;
               try{creditosDerechosJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
               creditosDerechosJDialog.load((CreditoDerechoBean)bienSeleccionado, false);
           }

           creditosDerechosJDialog.addActionListener(new java.awt.event.ActionListener(){
               public void actionPerformed(ActionEvent e){
                   pantallaAction(operacion,(BienJDialog)e.getSource());
               }
           });

           GUIUtil.centreOnWindow(creditosDerechosJDialog);
           creditosDerechosJDialog.setVisible(true);
        }finally{
           this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           actualizaBotonera();
        }
   }

    private void abrirDialogoSemoviente(final String operacion) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
           SemovienteJDialog semovienteJDialog= new SemovienteJDialog(desktop, locale);
           semovienteJDialog.setOperacion(operacion);

           if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
               bienesJPanel.clearSelection();
               try{semovienteJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag1"));}catch (Exception e){}
               SemovienteBean semoviente= new SemovienteBean();
               semoviente.setTipo(tipoBienesJPanel.getSubtipoSeleccionado());
               if (municipio != null) semoviente.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
               semovienteJDialog.load(semoviente, true);
               if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
              		semovienteJDialog.pmsChecked();
      		   }
           }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
               if (bienSeleccionado==null) return;
               if (!(bienSeleccionado instanceof SemovienteBean)) return;
               try{semovienteJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));}catch (Exception e){}
               semovienteJDialog.load((SemovienteBean)bienSeleccionado, true);
           }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
               if (bienSeleccionado==null) return;
               if (!(bienSeleccionado instanceof SemovienteBean)) return;
               try{semovienteJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
               semovienteJDialog.load((SemovienteBean)bienSeleccionado, false);
           }

           semovienteJDialog.addActionListener(new java.awt.event.ActionListener(){
               public void actionPerformed(ActionEvent e){
                   pantallaAction(operacion,(BienJDialog)e.getSource());
               }
           });

           GUIUtil.centreOnWindow(semovienteJDialog);
           semovienteJDialog.setVisible(true);
        }finally{
           this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           actualizaBotonera();
        }
   }

    private void abrirDialogoVias(final String operacion, String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
            ViasJDialog viasJDialog= new ViasJDialog(desktop, locale, tipo);
           viasJDialog.setOperacion(operacion);

           if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
               bienesJPanel.clearSelection();
               try{viasJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag1"));}catch (Exception e){}
               ViaBean via= new ViaBean();
               via.setTipo(tipoBienesJPanel.getSubtipoSeleccionado());
               if (municipio != null) via.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
               viasJDialog.load(via, true);
               if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
            	   viasJDialog.pmsChecked();
     		   }
           }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
               if (bienSeleccionado==null) return;
               if (!(bienSeleccionado instanceof ViaBean)) return;
               try{viasJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));}catch (Exception e){}
               viasJDialog.load((ViaBean)bienSeleccionado, true);
           }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
               if (bienSeleccionado==null) return;
               if (!(bienSeleccionado instanceof ViaBean)) return;
               try{viasJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
               viasJDialog.load((ViaBean)bienSeleccionado, false);
           }

           viasJDialog.addActionListener(new java.awt.event.ActionListener(){
               public void actionPerformed(ActionEvent e){
                   pantallaAction(operacion,(BienJDialog)e.getSource());
               }
           });

           GUIUtil.centreOnWindow(viasJDialog);
           viasJDialog.setVisible(true);
        }finally{
           this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           actualizaBotonera();
        }
   }


    private void abrirDialogoVehiculo(final String operacion) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
           VehiculoJDialog vehiculoJDialog= new VehiculoJDialog(desktop, locale);
           vehiculoJDialog.setOperacion(operacion);

           if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
               bienesJPanel.clearSelection();
               try{vehiculoJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag1"));}catch (Exception e){}
               VehiculoBean vehiculo= new VehiculoBean();
               vehiculo.setTipo(tipoBienesJPanel.getSubtipoSeleccionado());
               if (municipio != null) vehiculo.setOrganizacion(municipio.getNombre() + " - "+ municipio.getProvincia());
               vehiculoJDialog.load(vehiculo, true);
               if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
            	   vehiculoJDialog.pmsChecked();
     		   }
           }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
               if (bienSeleccionado==null) return;
               if (!(bienSeleccionado instanceof VehiculoBean)) return;
               try{vehiculoJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag2"));}catch (Exception e){}
               vehiculoJDialog.load((VehiculoBean)bienSeleccionado, true);
           }else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
               if (bienSeleccionado==null) return;
               if (!(bienSeleccionado instanceof VehiculoBean)) return;
               try{vehiculoJDialog.setTitle(aplicacion.getI18nString("inventario.dialogo.tag3"));}catch (Exception e){}
               vehiculoJDialog.load((VehiculoBean)bienSeleccionado, false);
           }

           vehiculoJDialog.addActionListener(new java.awt.event.ActionListener(){
               public void actionPerformed(ActionEvent e){
                   pantallaAction(operacion, (BienJDialog)e.getSource());
               }
           });

           GUIUtil.centreOnWindow(vehiculoJDialog);
           vehiculoJDialog.setVisible(true);
        }finally{
           this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           actualizaBotonera();
        }
   }

    private void abrirDialogoBusqueda() throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
            /** desmarcamos las features que esten seleccionadas en el mapa */
            if (mapaJPanel!=null) mapaJPanel.clear();

            BuscarJDialog buscarJDialog= new BuscarJDialog(desktop);
            buscarJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    buscarJDialog_actionPerformed((BuscarJDialog)e.getSource());
                }
            });

            buscarJDialog.setVisible(true);
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void abrirDialogoFiltro() throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
            /** desmarcamos las features que esten seleccionadas en el mapa */
            if (mapaJPanel!=null) mapaJPanel.clear();

            FiltrarJDialog filtrarJDialog= new FiltrarJDialog(desktop,tipoBienesJPanel.getTipoSeleccionado(),tipoBienesJPanel.getSubtipoSeleccionado(), locale);
            filtrarJDialog.load(filtro!=null?(Collection)filtro.clone():null, aplicarFiltro);
            filtrarJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    filtrarJDialog_actionPerformed((FiltrarJDialog)e.getSource());
                }
            });

            filtrarJDialog.setVisible(true);
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void abrirDialogoInformes() throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
            /** desmarcamos las features que esten seleccionadas en el mapa */
            
        	InformesJDialog informesJDialog= new InformesJDialog(desktop, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), locale);
            informesJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                	informesJDialog_actionPerformed((InformesJDialog)e.getSource());
                }
            });


            GUIUtil.centreOnWindow(informesJDialog);
            informesJDialog.setVisible(true);
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }


    /**
     * Abre el dialogo de la gestion documental de un tipo de bien de inventario
     * @param operacion (ANEXAR, CONSULTA_ANEXOS)
     * @throws Exception
     */
    private void abrirGestionDocumentalJDialog(final String operacion) throws Exception{
        if (bienSeleccionado==null) return;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
        	GestionDocumentalJDialog documentJDialog= new GestionDocumentalJDialog(desktop, bienSeleccionado, operacion);
            documentJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    gestionDocumentalJDialog_actionPerformed(operacion);
                }
            });

            GUIUtil.centreOnWindow(documentJDialog);
            documentJDialog.setVisible(true);
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }


    public void pantallaAction(String operacion, BienJDialog bienJDialog){
        try{
        	logger.info("Modificando el bien");
            if (bienJDialog.getBien() != null){
        		if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR) ||
        				operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            		BienBean bienNuevo=bienJDialog.getBien();
            		//ASO comprobamos que el numero de inventario es correcto
            		if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) && (bienNuevo.getNumInventario()==null
            			|| bienNuevo.getNumInventario().trim().length()==0))
            		{
            			 /** Mostramos mensaje de bloqueo del bien */
						 JOptionPane.showMessageDialog((JDialog)bienJDialog, aplicacion.getI18nString("inventario.mensajes.tag5"));
						 return;
            		}
            		BienBean auxBean=inventarioClient.getBienByNumInventario(bienNuevo.getNumInventario());
            		
            		if (auxBean !=null && auxBean.getId()!= bienNuevo.getId()){
           			 /** Mostramos mensaje de bloqueo del bien */
            			 JOptionPane.showMessageDialog((JDialog)bienJDialog, aplicacion.getI18nString("inventario.mensajes.tag6"));
						 return;
            		}
            		if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
            			Vector documentos= bienJDialog.getDocumentosJPanel().getFilesInUp();
            			if (bienJDialog instanceof MuebleJDialog){
            				if (((MuebleJDialog)bienJDialog).getLotePanel()!=null){
            					documentos.addAll(((MuebleJDialog)bienJDialog).getLotePanel().getDocumentosJPanel().getFilesInUp());
            				}
            			}
            			bienSeleccionado= inventarioClient.insertInventario(listafeatures, bienNuevo,documentos);
            			if (bienSeleccionado == null) return;
            			updateFeatures();
            		}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            			logger.info("Actualizando inventario");
            			bienSeleccionado= inventarioClient.updateInventario(bienNuevo, bienJDialog.getDocumentosJPanel().getFilesInUp());
            		}
            	}
                if (mapaJPanel != null){
                	cargarMapa();
                }
                recargarTablaBienesInventario(bienesJPanel.getActualOffset());
                
            }/** El usuario ha Cancelado la operacion */
            else{
            	
            	//Desbloqueamos el bien
            	if (bienSeleccionado!=null)
            		desbloquearInventario((BienBean)bienSeleccionado);
            	bienSeleccionado=null;
            }

            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                desbloquearInventario((BienBean)bienSeleccionado);
            }

            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y actualizamos bloqueos */
            
            bienesJPanel.clearSelection();
            
            //Seleccionamos el bien en la pantalla, pero no lo recuperamos porque sino
            //se queda con menos información de la introducida. Problema con la perdida de observacaciones
            if (bienSeleccionado instanceof BienBean){
            	//BienBean BienSeleccionadoTemporal=bienesJPanel.seleccionarBien((BienBean)bienSeleccionado); 
            	bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
            }
            
            actualizaBotonera();
            
        }catch(Exception e){
            logger.error("Error al operar con un vehiculo de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

         bienJDialog.setVisible(false);
    }

    public void buscarJDialog_actionPerformed(BuscarJDialog buscarJDialog){
        try{
            if (buscarJDialog.getValor() != null){
                cadenaBusqueda= buscarJDialog.getValor();
                cargarResultadoBusqueda();
            }/** El usuario ha Cancelado la operacion */

            bienesJPanel.clearSelection();
            actualizaBotonera();
        }catch(Exception e){
            logger.error("Error en la busqueda ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
        }
        buscarJDialog.setVisible(false);
    }

    public void filtrarJDialog_actionPerformed(FiltrarJDialog filtrarJDialog){
        try{
            if (filtrarJDialog.aceptar()){
                filtro= filtrarJDialog.getFiltro();
                aplicarFiltro= filtrarJDialog.getAplicarFiltro();
                if (botoneraAppJPanel != null)
                	botoneraAppJPanel.renombrarFiltrarJButton((filtro!=null && filtro.size()>0)?aplicarFiltro:false);
                cargarResultadoBusqueda();
            }/** El usuario ha Cancelado la operacion */

            bienesJPanel.clearSelection();
            actualizaBotonera();
        }catch(Exception e){
            logger.error("Error en el filtro ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
        }
        filtrarJDialog.setVisible(false);
    }

    public void informesJDialog_actionPerformed(InformesJDialog informesJDialog){
        informesJDialog.setVisible(false);
    }



    public void gestionDocumentalJDialog_actionPerformed(final String operacion){
        try{
            if ((bienSeleccionado instanceof BienBean)){
            	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR))
            		desbloquearInventario((BienBean)bienSeleccionado);
            	 /** Recargamos los bienes de inventario */
                recargarTablaBienesInventario(bienesJPanel.getActualOffset());
                //bienSeleccionado=bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
                bienesJPanel.seleccionarBien((BienBean)bienSeleccionado);
            }
            if ((bienSeleccionado instanceof Lote)){
            	/** Recargamos los bienes de inventario */
            	recargarTablaLotes();
            	bienesJPanel.seleccionarLote((Lote)bienSeleccionado);
            }

        }catch (Exception e){
            logger.error("Error al operar con los documentos de un bien de inventario ", e);
        }
    }



    public TipoBienesJPanel getTipoBienesPanel(){
        return tipoBienesJPanel;
    }

    public BienesJPanel getBienesPanel(){
        return bienesJPanel;
    }

    public BotoneraEditorJPanel getBotoneraPanel(){
        return botoneraJPanel;
    }

    public MapaJPanel getMapaJPanel() {
        return mapaJPanel;
    }

    public void setMapaJPanel(MapaJPanel mapaJPanel) {
        this.mapaJPanel = mapaJPanel;
    }

    public BotoneraAppJPanel getBotoneraAppJPanel() {
        return botoneraAppJPanel;
    }

    public void setBotoneraAppJPanel(BotoneraAppJPanel botoneraAppJPanel) {
        this.botoneraAppJPanel = botoneraAppJPanel;
        this.botoneraAppJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                botoneraAppJPanel_actionPerformed();
            }
        });

    }


    /**
     * Método que abre una ventana de confirmacion para realizar una operacion
     * @param tag1 tag del mensaje
     * @param tag2 tag del titulo de la ventana
     * @return false si la confirmacion es negativa, true en caso contrario
     */
    private boolean confirm(String tag1, String tag2){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString(tag1), aplicacion.getI18nString(tag2), JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }

    public void setEnabled(boolean b){
        botoneraJPanel.setEnabled(b);
        this.lock= !b;
    }

    public void setFeaturesSelected(Collection c){
    	if (c!=null)
    		listafeatures= c.toArray();
    	else
    		listafeatures=null;
    }

    public GeopistaFeature getSelectedFeature(){
        return gFeature;
    }

    public void renombrarComponentes(){
    	try{
    		tipoBienesJPanel.renombrarComponentes();
    	}
    	catch (CancelException e){
    		
    	}
        bienesJPanel.renombrarComponentes();
        botoneraJPanel.renombrarComponentes();
    }


    /* Cuando nos movemos por la ventana de atributos */
    public void enter(){
    }

    /* De momento no se usa */
    public void exit(){
    }

    /**
     * Metodo para obtener el municipio
     * @return Municipio
     */
    private Municipio getMunicipio()
    {
        Connection conn = null;
        try
        {
                conn = getDBConnection();
                PreparedStatement ps = conn.prepareStatement("getMunicipio");
                //CAST 8.4                
                //ps.setString(1,aplicacion.getString("localgis.DefaultCityId"));
                ps.setInt(1,Integer.parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID),10));
                ResultSet  rs =ps.executeQuery();
                Municipio municipio=null;
                if  (rs.next()) {
                    municipio= new Municipio(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID), rs.getString("idprovincia"),rs.getString("nombre"),rs.getString("provincia"));
                }
                rs.close();
                ps.close();
                conn.close();
                return municipio;
       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("ERROR al recoger los datos del municipio:"+sw.toString());
        }
        return null;
    }

    /**
     * Metodo que obtiene la conexion a BD
     * @return Connection
     */
    public Connection getDBConnection()
    {
        Connection conn = null;
        try
        {
            // Quitamos los drivers
            Enumeration e = DriverManager.getDrivers();
            while (e.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver) e.nextElement());
            }
            DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());
            String sConn = aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_URL);
            conn = DriverManager.getConnection(sConn);
            AppContext app = (AppContext) AppContext.getApplicationContext();
            conn = app.getConnection();
            conn.setAutoCommit(false);
        } catch (Exception e)
        {
            return null;
        }
        return conn;
   }

    /**
     * Bloquea un bien de inventario
     * @param bien a bloquear
     */
    private void bloquearInventario(final BienBean bien){
        if (bienSeleccionado==null) return;
        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("inventario.mensajes.tag3"));
        progressDialog.report(aplicacion.getI18nString("inventario.mensajes.tag4"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        try{
                            inventarioClient.bloquearInventario(bien, true);
                        }
                        catch(Exception e){
                            logger.error("Error al bloquear el bien de invenatrio ", e);
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                            return;
                        }
                        finally{
                            progressDialog.setVisible(false);
                        }
                    }
              }).start();
          }
       });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);

    }

    /**
     * Desbloquea un bien de inventario
     * @param bien a desbloquear
     * @return
     */
    private Object desbloquearInventario(BienBean bien){
        try{
            if (bien==null) return null;
            return inventarioClient.bloquearInventario(bien, false);
        }
        catch(Exception e){return null;}
    }

    private void abrirDialogo(int dialogo) throws Exception{
         abrirDialogo(dialogo, null);
    }
    /**
     * Abre el tipo de dialogo pasado como parametro
     * @param dialogo a abrir
     * @throws Exception
     */
    private void abrirDialogo(int dialogo, String tipo) throws Exception{
        if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
            switch (dialogo){
                case INMUEBLE:
                    abrirDialogoInmueble(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case MUEBLE:
                    abrirDialogoMueble(Constantes.OPERACION_ANNADIR, tipo);
                    break;
                case DERECHOS_REALES:
                    abrirDialogoDerechosReales(Constantes.OPERACION_ANNADIR);
                    break;
                case VALOR_MOBILIARIO:
                    abrirDialogoValorMobiliario(Constantes.OPERACION_ANNADIR);
                    break;
                case CREDITO_DERECHO:
                    abrirDialogoCreditoDerecho(Constantes.OPERACION_ANNADIR);
                    break;
                case SEMOVIENTE:
                    abrirDialogoSemoviente(Constantes.OPERACION_ANNADIR);
                    break;
                case VIAS:
                    abrirDialogoVias(Constantes.OPERACION_ANNADIR, tipo);
                    break;
                case VEHICULOS:
                    abrirDialogoVehiculo(Constantes.OPERACION_ANNADIR);
                    break;

            }
            
        }else if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) ||
                  botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
            if (bienSeleccionado == null) return;
            
            //Realmente hay que volver a cargarlo???
            //bienSeleccionado=cargarBienInventario();
            String bloqueado= inventarioClient.bloqueado((BienBean)bienSeleccionado);
            if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                /** Mostramos mensaje de bloqueo del bien */
                JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.mensajes.tag1")+" "+bloqueado+"\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
                /** lo abrimos en modo consulta */
                if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                    switch (dialogo){
                        case INMUEBLE:
                            abrirDialogoInmueble(Constantes.OPERACION_CONSULTAR,tipo);
                            break;
                        case MUEBLE:
                            abrirDialogoMueble(Constantes.OPERACION_CONSULTAR, tipo);
                            break;
                        case DERECHOS_REALES:
                            abrirDialogoDerechosReales(Constantes.OPERACION_CONSULTAR);
                            break;
                        case VALOR_MOBILIARIO:
                            abrirDialogoValorMobiliario(Constantes.OPERACION_CONSULTAR);
                            break;
                        case CREDITO_DERECHO:
                            abrirDialogoCreditoDerecho(Constantes.OPERACION_CONSULTAR);
                            break;
                        case SEMOVIENTE:
                            abrirDialogoSemoviente(Constantes.OPERACION_CONSULTAR);
                            break;
                        case VIAS:
                            abrirDialogoVias(Constantes.OPERACION_CONSULTAR, tipo);
                            break;
                        case VEHICULOS:
                            abrirDialogoVehiculo(Constantes.OPERACION_CONSULTAR);
                            break;

                    }
                }else abrirGestionDocumentalJDialog(Constantes.OPERACION_CONSULTA_ANEXOS);
                return;

            }else bloquearInventario((BienBean)bienSeleccionado);

            if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                switch (dialogo){
                    case INMUEBLE:
                        abrirDialogoInmueble(Constantes.OPERACION_MODIFICAR,tipo);
                        break;
                    case MUEBLE:
                        abrirDialogoMueble(Constantes.OPERACION_MODIFICAR, tipo);
                        break;
                    case DERECHOS_REALES:
                        abrirDialogoDerechosReales(Constantes.OPERACION_MODIFICAR);
                        break;
                    case VALOR_MOBILIARIO:
                        abrirDialogoValorMobiliario(Constantes.OPERACION_MODIFICAR);
                        break;
                    case CREDITO_DERECHO:
                        abrirDialogoCreditoDerecho(Constantes.OPERACION_MODIFICAR);
                        break;
                    case SEMOVIENTE:
                        abrirDialogoSemoviente(Constantes.OPERACION_MODIFICAR);
                        break;
                    case VIAS:
                        abrirDialogoVias(Constantes.OPERACION_MODIFICAR, tipo);
                        break;
                    case VEHICULOS:
                        abrirDialogoVehiculo(Constantes.OPERACION_MODIFICAR);
                        break;

                }

            }else abrirGestionDocumentalJDialog(Constantes.OPERACION_ANEXAR);
        }

    }

    private void cargarResultadoBusqueda() throws Exception{
        listafeatures= null;
        if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_BIENES)){
        	recargarTablaBienesInventario(0);
            return;
        }
        if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_LOTES)){
        	recargarTablaLotes();
        	return;
        }
        if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_REVERTIBLES)){
        	recargarTablaBienesRevertibles();
        	return;
        }
        if (tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO)){
        	recargarTablaBienesInventario(0);
        	return;
        }
        
    }

    private void cargarResultadoFiltro() throws Exception{
        listafeatures= null;
        recargarTablaBienesInventario(0);
    }


    /**
     * Recarga la tabla de bienes de inventario cuando el usuario selecciona un tipo de bien del arbol,
     * o finaliza algun tipo de operacion sobre algun bien de la tabla.
     * @throws Exception
     */
    public void recargarTablaBienesInventario() throws Exception{
    	recargarTablaBienesInventario(bienesJPanel.getActualOffset());
    }
    
    public void recargarTablaBienesInventario(final int offset) throws Exception{
    	recargarTablaBienesInventario(offset,true);
    }

    public void recargarTablaBienesInventario(final int offset,final boolean tipoSeleccionado) throws Exception{
        
        AppContext app =(AppContext) AppContext.getApplicationContext();
        final JFrame desktop= (JFrame)app.getMainFrame();
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
        progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e) 
            {
                new Thread(new Runnable()
                {
                    public void run()  //throws Exception
                    {
                        try{
                            progressDialog.report(aplicacion.getI18nString("inventario.app.tag3"));

                            Collection c= null;
                            boolean b= true;

                            //MEJORA. Cargamos independientemente de que el usuario no
                            //tenga seleccionado un epigrafe
                            //if (tipoBienesJPanel.getSubtipoSeleccionado()==null) return;

                            ConfigParameters configParameters=new ConfigParameters();
                            if (Constantes.MOSTRAR_PAGINACION)
                            	configParameters.setLimit(ConfigParameters.DEFAULT_LIMIT);
                            else
                            	configParameters.setLimit(-1);
                            
                            bienesJPanel.setActualOffset(offset);
                            configParameters.setOffset(offset);
                            
                            
                            inventarioClient.setTaskMonitor(progressDialog);
                            if ((tipoBienesJPanel.getSubtipoSeleccionado()==null) && (listafeatures.length>0)){
                            	//logger.info("Lista de features:"+listafeatures.length);
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_BIENES, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }
                            else if ((tipoBienesJPanel.getSubtipoSeleccionado()==null) &&
                            		(tipoBienesJPanel.getTipoSeleccionado()!=null) &&
                            		(tipoBienesJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_PATRIMONIO_MUNICIPAL_SUELO))){                            	
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_BIENES, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }
                            else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS) ||
                            		tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_INMUEBLES, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART) ||
                            		tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_MUEBLES, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DERECHOS_REALES)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_DERECHOS_REALES, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_VALOR_MOBILIARIO)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_VALORES_MOBILIARIOS, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_CREDITOS_DERECHOS, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_SEMOVIENTES)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_SEMOVIENTES, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS) ||
                            		tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_VIAS, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }else if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_VEHICULOS)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_VEHICULOS, tipoBienesJPanel.getTipoSeleccionado(), tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures,configParameters);
                            }else{
                            	/** No es ningun tipo reconocido. */
                            	b= false;
                            }

                            
                            bienesJPanel.clearTable();
                            /** Cargamos la coleccion */
                            bienesJPanel.loadListaBienes(c);
                            
                            if (!tipoSeleccionado){
                            	seleccionarEpigrafeAuto(c,1);                            	
                            }
                            
                            /** Desde GIS. Si no se ha bloqueado la capa para hacer los cambios, lock es true (evitamos hacer cambios en la feature) */
                            if (lock) return;
                           
                        }
                        catch(Exception e){
                        	
                        	if ((progressDialog!=null) && (progressDialog.isCancelRequested())){
				        		logger.warn("Carga de datos cancelada");		
    							JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de datos cancelada");
    							//cancelada=true;
				        	}
                        	else
                        		ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"),
                        			aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
                        }
                        finally
    					{
    						progressDialog.setVisible(false);
    					}
                    }

                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        cadenaBusqueda="";
        
    }
    
    
    public void recargarTablaBienesRevertibles() throws Exception{
    	recargarTablaBienesRevertibles(bienesJPanel.getActualOffset());
    }
    
    /**
     * Recarga la tabla de bienes revertibles
     * @throws Exception
     */
    public void recargarTablaBienesRevertibles(final int offset) throws Exception{
        
        AppContext app =(AppContext) AppContext.getApplicationContext();
        final JFrame desktop= (JFrame)app.getMainFrame();
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
        progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e) 
            {
                new Thread(new Runnable()
                {
                    public void run()  //throws Exception
                    {
                        try{
                            progressDialog.report(aplicacion.getI18nString("inventario.app.tag3"));

                            
                            ConfigParameters configParameters=new ConfigParameters();
                            if (Constantes.MOSTRAR_PAGINACION)
                            	configParameters.setLimit(ConfigParameters.DEFAULT_LIMIT);
                            else
                            	//configParameters.setLimit(ConfigParameters.MAX_LIMIT);
                        		configParameters.setLimit(-1);
                            
                            bienesJPanel.setActualOffset(offset);
                            configParameters.setOffset(offset);
                            
                            inventarioClient.setTaskMonitor(progressDialog);
                            Collection<BienRevertible> c= inventarioClient.getBienesRevertibles(tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null,configParameters);
                          
                           
  
                            /** Cargamos la coleccion */
                            bienesJPanel.clearTable();
                            bienesJPanel.loadListaBienesRevertibles(c);
     
                       }
                        catch(Exception e){
                        	if ((progressDialog!=null) && (progressDialog.isCancelRequested())){
				        		logger.warn("Carga de datos cancelada");		
    							JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de datos cancelada");
    							//cancelada=true;
				        	}
                        	else
                        		ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"),
                        			aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
                        }
                        finally
    					{
    						progressDialog.setVisible(false);
    					}
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        cadenaBusqueda="";
        
    }
    
    public void recargarTablaLotes() throws Exception{
    	recargarTablaLotes(bienesJPanel.getActualOffset());
    }
    /**
     * Recarga la tabla de bienes revertibles
     * @throws Exception
     */
    public void recargarTablaLotes(final int offset) throws Exception{
        
        AppContext app =(AppContext) AppContext.getApplicationContext();
        final JFrame desktop= (JFrame)app.getMainFrame();
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
        progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e) 
            {
                new Thread(new Runnable()
                {
                    public void run()  //throws Exception
                    {
                        try{
                            progressDialog.report(aplicacion.getI18nString("inventario.app.tag3"));

                            
                            ConfigParameters configParameters=new ConfigParameters();
                            if (Constantes.MOSTRAR_PAGINACION)
                            	configParameters.setLimit(ConfigParameters.DEFAULT_LIMIT);
                            else
                            	//configParameters.setLimit(ConfigParameters.MAX_LIMIT);
                            	configParameters.setLimit(-1);
                            
                            bienesJPanel.setActualOffset(offset);
                            configParameters.setOffset(offset);
                            
                            inventarioClient.setTaskMonitor(progressDialog);
                            Collection<Lote> c= inventarioClient.getLotes(tipoBienesJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null,configParameters);
                          
  
                            /** Cargamos la coleccion */
                            bienesJPanel.clearTable();
                            bienesJPanel.loadListaLotes(c);
                        
                    
                       }
                        catch(Exception e){
                        	if ((progressDialog!=null) && (progressDialog.isCancelRequested())){
				        		logger.warn("Carga de datos cancelada");		
    							JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de datos cancelada");
    							//cancelada=true;
				        	}
                        	else
                        		ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"),
                        			aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
                        }
                        finally
    					{
    						progressDialog.setVisible(false);
    					}
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        cadenaBusqueda="";
        
    }
    private BienBean cargarBienInventario() throws Exception{
    	
    	if (tipoBienesJPanel.getSubtipoSeleccionado()==null){
    		String tipo=((BienBean)bienSeleccionado).getTipo();
    		tipoBienesJPanel.setSubtipoSeleccionado(tipo);
    		
    	}
    	
    	if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS) ||
                    		tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
                    	return (BienBean)inventarioClient.getBienInventario(Const.ACTION_GET_INMUEBLE, tipoBienesJPanel.getSubtipoSeleccionado(), 
                    			((BienBean)bienSeleccionado).getId(),((BienBean)bienSeleccionado).getRevisionActual(),((BienBean)bienSeleccionado).getRevisionExpirada());
        }
    	if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART) ||
                    		tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
                    	return (BienBean)inventarioClient.getBienInventario(Const.ACTION_GET_MUEBLE, tipoBienesJPanel.getSubtipoSeleccionado(), 
                    			((BienBean)bienSeleccionado).getId(),((BienBean)bienSeleccionado).getRevisionActual(),((BienBean)bienSeleccionado).getRevisionExpirada() );
        }
    	
    	if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DERECHOS_REALES)){
                        return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_DERECHO_REAL, tipoBienesJPanel.getSubtipoSeleccionado(), 
                    			((BienBean)bienSeleccionado).getId(),((BienBean)bienSeleccionado).getRevisionActual(),((BienBean)bienSeleccionado).getRevisionExpirada() );
        }
    	if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_VALOR_MOBILIARIO)){
                    	return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_VALOR_MOBILIARIO, tipoBienesJPanel.getSubtipoSeleccionado(), 
                    			((BienBean)bienSeleccionado).getId(),((BienBean)bienSeleccionado).getRevisionActual(),((BienBean)bienSeleccionado).getRevisionExpirada());
        }
    	if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
                    	return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_CREDITO_DERECHO, tipoBienesJPanel.getSubtipoSeleccionado(),
                    			((BienBean)bienSeleccionado).getId(),((BienBean)bienSeleccionado).getRevisionActual(),((BienBean)bienSeleccionado).getRevisionExpirada());
        }
    	if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_SEMOVIENTES)){
                    	return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_SEMOVIENTE, tipoBienesJPanel.getSubtipoSeleccionado(), 
                    			((SemovienteBean)bienSeleccionado).getId(),((BienBean)bienSeleccionado).getRevisionActual(),((BienBean)bienSeleccionado).getRevisionExpirada());
        }
    	if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS) ||
                    		tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
                    	return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_VIA, tipoBienesJPanel.getSubtipoSeleccionado(), 
                    			((BienBean)bienSeleccionado).getId(),((BienBean)bienSeleccionado).getRevisionActual(),((BienBean)bienSeleccionado).getRevisionExpirada());
        }
    	if (tipoBienesJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_VEHICULOS)){
                    	return (BienBean) inventarioClient.getBienInventario(Const.ACTION_GET_VEHICULO, tipoBienesJPanel.getSubtipoSeleccionado(), 
                    			((BienBean)bienSeleccionado).getId(),((BienBean)bienSeleccionado).getRevisionActual(),((BienBean)bienSeleccionado).getRevisionExpirada());
        }
    	return null;
    }
    
    private void decrementarNumeroBienes(){
    	
    	if (listafeatures!= null && listafeatures.length>0){

    		if (mapaJPanel!=null){
	    		ArrayList lstLayers = (ArrayList) mapaJPanel.getGeopistaEditor().getLayerManager().getLayers();
	    		for (Iterator iterLayers = lstLayers.iterator(); iterLayers.hasNext();){
	    			Object obj = iterLayers.next();
	    			FeatureEvent event = null;
	    			if(obj instanceof Layer || obj instanceof GeopistaLayer){
	
	    				ArrayList lstFeatures = new ArrayList();
	    				for (int i=0; i<listafeatures.length; i++){
	    					Object feature = listafeatures[i];
	    					if (feature instanceof GeopistaFeature){
	    						GeopistaFeature geopistaFeature = (GeopistaFeature)feature;                    					
	    						if (geopistaFeature.getLayer().equals(obj)){
	    							
	    							Object valor=geopistaFeature.getAttribute("Numero de Bienes");
	    							if (valor instanceof Long){
	    								long bienes=(Long)valor;
	    								if (bienes>0){
	    									bienes--;	    							
	    									geopistaFeature.setAttribute("Numero de Bienes", bienes);
	    								}
	    							}
	    						}
	    					}
	    				}
	    			}
	    		}
    		}
    	}
    }	    
    
    private void updateFeatures(){
    	
    	if (listafeatures!= null && listafeatures.length>0){

    		if (mapaJPanel!=null){
	    		ArrayList lstLayers = (ArrayList) mapaJPanel.getGeopistaEditor().getLayerManager().getLayers();
	    		for (Iterator iterLayers = lstLayers.iterator(); iterLayers.hasNext();){
	    			Object obj = iterLayers.next();
	    			FeatureEvent event = null;
	    			if(obj instanceof Layer || obj instanceof GeopistaLayer){
	
	    				ArrayList lstFeatures = new ArrayList();
	    				for (int i=0; i<listafeatures.length; i++){
	    					Object feature = listafeatures[i];
	    					if (feature instanceof GeopistaFeature){
	    						GeopistaFeature geopistaFeature = (GeopistaFeature)feature;                    					
	    						if (geopistaFeature.getLayer().equals(obj)){
	    							lstFeatures.add(geopistaFeature);
	    						}
	    					}
	    				}
	    				event = new FeatureEvent(lstFeatures,FeatureEventType.GEOMETRY_MODIFIED,(Layer)obj,lstFeatures);                    			
	    			}
	    			mapaJPanel.getGeopistaEditor().featuresChanged(event);
	    		}
    		}
    	}
    }

    
    private void redrawNewFeature( Object[] listafeatures){
    	
    	if (listafeatures!= null && listafeatures.length>0){

    		if (mapaJPanel!=null){
	    		ArrayList lstLayers = (ArrayList) mapaJPanel.getGeopistaEditor().getLayerManager().getLayers();
	    		for (Iterator iterLayers = lstLayers.iterator(); iterLayers.hasNext();){
	    			Object obj = iterLayers.next();
	    			FeatureEvent event = null;
	    			if(obj instanceof Layer || obj instanceof GeopistaLayer){
	
	    				ArrayList lstFeatures = new ArrayList();
	    				for (int i=0; i<listafeatures.length; i++){
	    					Object feature = listafeatures[i];
	    					if (feature instanceof GeopistaFeature){
	    						GeopistaFeature geopistaFeature = (GeopistaFeature)feature;                    					
	    						if (geopistaFeature.getLayer().equals(obj)){
	    							
	    							/*Object valor=geopistaFeature.getAttribute("Numero de Bienes");
	    							if (valor instanceof Long){
	    								long bienes=(Long)valor;
    									bienes++;	    							
    									geopistaFeature.setAttribute("Numero de Bienes", bienes);
	    							}*/
	    							lstFeatures.add(geopistaFeature);
	    						}
	    					}
	    				}
	    				event = new FeatureEvent(lstFeatures,FeatureEventType.GEOMETRY_MODIFIED,(Layer)obj,lstFeatures);                    			
	    			}
	    			mapaJPanel.getGeopistaEditor().featuresChanged(event);
	    		}
    		}
    	}
    }

    
    /**
     * Este metodo solo se puede utilizar para la aplicacion de Inventario para el plugin
     * interno que se utiliza dentro del editor no funciona
     * @return
     */
    private InventarioInternalFrame getInventarioFrame(){
    	return ((InventarioInternalFrame)((IMainInventario)this.desktop).getIFrame());
    }

    /**
     * Vuelve a cargar el mapa
     */
    private void cargarMapa(){
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
    	progressDialog.setTitle(I18N.get("GeopistaLoadMapPlugIn.CargandoMapa"));
    	progressDialog.report(I18N.get("GeopistaLoadMapPlugIn.CargandoMapa"));
    	progressDialog.addComponentListener(new ComponentAdapter() {

    		public void componentShown(ComponentEvent e) {
    			new Thread(new Runnable() {

	                public void run() {
	                	try {
	                		updateVersion();
	                    	Iterator itLayers = getInventarioFrame().getJPanelMap().getGeopistaEditor().getLayerManager().getLayers().iterator();
	                    	boolean bCargarMapa = false;
	                    	while (itLayers.hasNext()){
	                    		GeopistaLayer layer = (GeopistaLayer)itLayers.next();
	                    		if (layer.isVersionable() && layer.getRevisionActual() != -1){
	                    			bCargarMapa = true;
	                    			break;
	                    		}
	                    	}
	                    	if (bCargarMapa){
		                        Version version = new Version();
		                        version.setFecha(com.geopista.protocol.inventario.Const.fechaVersion);
		                        version.setFeaturesActivas(true);
		                        AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.VERSION,version);
		                    	getInventarioFrame().getJPanelMap().getGeopistaEditor().loadMap("geopista:///"+Constantes.idMapaInventario);
	                    	}
	                    } catch (Exception e) {
	                    	ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("Version","SeleccionarRevision"), I18N.get("Version","SeleccionarRevision"), StringUtil
		                                .stackTrace(e));
	                    } finally {
	                          progressDialog.setVisible(false);
	                    }
	                }
	            }).start();
    		}
    	});
    	GUIUtil.centreOnWindow(progressDialog);
    	progressDialog.setVisible(true);
    	
    }
    
    public InventarioClient getInventarioClient(){
    	return this.inventarioClient;
    }
    /**
     * Actualiza la version
     */
    private void updateVersion(){
    	try{
    		String hora = (String)inventarioClient.getHora(Const.ACTION_GET_HORA);
    		String fechaVersion = (String) new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" "+hora;
    		Version version = new Version();
    		version.setFecha(fechaVersion);
    		version.setFeaturesActivas(true);
    		AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.VERSION,version);
    		com.geopista.protocol.inventario.Const.fechaVersion=fechaVersion;
    		if (getInventarioFrame()!=null)getInventarioFrame().setFecha((String) new SimpleDateFormat("dd-MM-yyyy").format(new Date())+" "+hora);
        }catch(Exception ex){
    		logger.error("Error al actualizar la version despues de la carga:", ex);
    	}
	}

    /**
     * Aplicamos un filtro. Por ahora solo uno.
     * @param indiceFiltro
     */
	public void setFilter(String nombreFiltro,boolean apply,Vector filtrosToRemove) {
			
		
		//TODO. Esto habria que mejorarlo por si acaso queremos
		//incluir filtro predefinidos. Por ahora solo los gestionamos
		//de forma individual
		if (filtro==null)
			filtro=new ArrayList<CampoFiltro>();

		//Primero lo intentamos buscamos y lo eliminamos por si estuviera
		//aplicado
		Iterator<CampoFiltro> itera = filtro.iterator();
		while (itera.hasNext()){
			CampoFiltro campoFiltro=(CampoFiltro)itera.next();
			if ((campoFiltro.getNombre()!=null && campoFiltro.getNombre().equals(nombreFiltro))){
				itera.remove();		
			}
			//Algunas veces al aplicar un filtro debemos borrar los que hubiera anteriormente relacionados
			if (filtrosToRemove!=null)
				for (int i=0;i<filtrosToRemove.size();i++){
					String filtroToRemove=(String)filtrosToRemove.elementAt(i);
					if ((campoFiltro.getNombre()!=null && campoFiltro.getNombre().equals(filtroToRemove))){
						itera.remove();		
					}
			}
		}
		
		ArrayList<CampoFiltro> listaFiltros=getFiltro(nombreFiltro,apply);
		if (listaFiltros!=null){
			filtro.addAll(listaFiltros);
		}
		
		
	}

	/**
	 * Devolvemos un filtro predefinido.
	 * @param nombreFiltro
	 * @return
	 */
	private ArrayList<CampoFiltro> getFiltro(String nombreFiltro,boolean apply) {
		ArrayList<CampoFiltro> listaFiltros=new ArrayList();
		if (nombreFiltro.equals("eliminados")){
			if (apply){				
				filtroEliminadosAplicado=true;
				aplicarFiltroEliminados(listaFiltros);

								
			}
			else{
				filtroEliminadosAplicado=false;
				//Si el filtro de bajas no esta aplicado, reseteamos
				if (!filtroBajasAplicado){
					resetFilter(listaFiltros);					
				}
				else{
					aplicarFiltroBajas(listaFiltros);
				}
			}
			
		}
		else if (nombreFiltro.equals("bajas")){
			if (apply){
				filtroBajasAplicado=true;
				aplicarFiltroBajas(listaFiltros); 				
			}
			else{
				filtroBajasAplicado=false;
				if (!filtroEliminadosAplicado){
					resetFilter(listaFiltros);					
				}
				else{
					aplicarFiltroEliminados(listaFiltros);
				}

			}
		}
		else
			return listaFiltros;
			
		return listaFiltros;
	}
	
	private void aplicarFiltroEliminados(ArrayList listaFiltros){
		CampoFiltro newFiltro= new CampoFiltro();
		newFiltro.setNombre("eliminados");
		newFiltro.setTabla("bienes_inventario");
		if (filtroBajasAplicado)
			newFiltro.setValorCompuesto(" (bienes_inventario.borrado='0' or bienes_inventario.borrado='2' or bienes_inventario.borrado='1') ");
		else
			newFiltro.setValorCompuesto(" (bienes_inventario.borrado='0' or bienes_inventario.borrado='2') ");
	 	newFiltro.setCompuesto();
	 	listaFiltros.add(newFiltro);
	 	
		CampoFiltro newFiltro1= new CampoFiltro();
		newFiltro1.setNombre("eliminados");
		newFiltro1.setTabla("bien_revertible");
		if (filtroBajasAplicado)
			newFiltro1.setValorCompuesto(" (bien_revertible.borrado='0' or bien_revertible.borrado='2' or bien_revertible.borrado='1') ");
		else
			newFiltro1.setValorCompuesto(" (bien_revertible.borrado='0' or bien_revertible.borrado='2') ");
	 	newFiltro1.setCompuesto();
	 	listaFiltros.add(newFiltro1);
	}
	
	private void aplicarFiltroBajas(ArrayList listaFiltros){
		CampoFiltro newFiltro= new CampoFiltro();
		newFiltro.setNombre("bajas");
		newFiltro.setTabla("bienes_inventario");
		if (filtroEliminadosAplicado)
			newFiltro.setValorCompuesto(" (bienes_inventario.borrado='0' or bienes_inventario.borrado='1' or bienes_inventario.borrado='2') ");
		else
			newFiltro.setValorCompuesto(" (bienes_inventario.borrado='0' or bienes_inventario.borrado='1') ");
	 	newFiltro.setCompuesto();
	 	listaFiltros.add(newFiltro);
	 	
		CampoFiltro newFiltro1= new CampoFiltro();
		newFiltro1.setNombre("bajas");
		newFiltro1.setTabla("bien_revertible");
		if (filtroEliminadosAplicado)
			newFiltro1.setValorCompuesto(" (bien_revertible.borrado='0' or bien_revertible.borrado='1' or bien_revertible.borrado='2') ");
		else
			newFiltro1.setValorCompuesto(" (bien_revertible.borrado='0' or bien_revertible.borrado='1') ");
	 	newFiltro1.setCompuesto();
	 	listaFiltros.add(newFiltro1);		
	}
	
	private void resetFilter(ArrayList listaFiltros){
		
		CampoFiltro newFiltro= new CampoFiltro();
	 	newFiltro.setNombre("borrado");
	 	newFiltro.setTabla("bienes_inventario");
	 	newFiltro.setVarchar();
	 	newFiltro.setOperador("=");
	 	newFiltro.setValorVarchar("0");		
	 	listaFiltros.add(newFiltro);
	 	
	 	CampoFiltro newFiltro1= new CampoFiltro();
	 	newFiltro1.setNombre("borrado");
	 	newFiltro1.setTabla("bien_revertible");
	 	newFiltro1.setVarchar();
	 	newFiltro1.setOperador("=");
	 	newFiltro1.setValorVarchar("0");		
	 	listaFiltros.add(newFiltro1);
	}
	
	private void seleccionarEpigrafeAuto(Collection c,int posicionEpigrafe){
		//tipoBienesJPanel.bienesJTree.expandRow(1);
		Iterator it=c.iterator();
		if (it.hasNext()){
			BienBean bien=(BienBean)it.next();
			tipoBienesJPanel.bienesJTree.expandRow(1);
			tipoBienesJPanel.disableEvents();
			if (bien.getTipo().equals(Const.PATRON_INMUEBLES_URBANOS))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_INMUEBLES_URBANOS+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_INMUEBLES_RUSTICOS))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_INMUEBLES_RUSTICOS+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_MUEBLES_HISTORICOART))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_MUEBLES_HISTORICOART+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_VIAS_PUBLICAS_URBANAS))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_VIAS_PUBLICAS_URBANAS+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_VIAS_PUBLICAS_URBANAS+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_DERECHOS_REALES))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_DERECHOS_REALES+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_VALOR_MOBILIARIO))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_PATRON_MOBILIARIO+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_BIENES_MUEBLES))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_BIENES_MUEBLES+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_SEMOVIENTES))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_SEMOVIENTES+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_VEHICULOS))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_VEHICULOS+posicionEpigrafe);
			else if (bien.getTipo().equals(Const.PATRON_CREDITOS_DERECHOS_PERSONALES))
				tipoBienesJPanel.bienesJTree.setSelectionRow(Const.POSICION_CREDITOS_DERECHOS_PERSONALES+posicionEpigrafe);
			
			tipoBienesJPanel.enableEvents();
		}
	}
    public static void main(String args[]){
		String fechaVersion = (String) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("Actualizando version fecha:"+fechaVersion);
    	
    }
	


    public void clearSelectedFeatures(){
    	this.listafeatures = null;
    }

	public void setSelectedFeatures(Object[] selectedItem) {
		this.listafeatures = selectedItem;
	}
	
	
	public void desassociatedata() {
//		this.getTipoBienesPanel().setModoAsociacion(false);
	}
	
    protected void associatedata(boolean replace) throws Exception {
    	logger.info("Asociando features");
    	// Obtener los elementos seleccionados de la tabla
    	String tipo=getTipoBienesPanel().getTipoSeleccionado();
    	BienBean[] bienesSeleccionados=null;
    	// Obtener las features seleccionadas en el mapa
    	Collection<GeopistaFeature> featureList = mapaJPanel.getGeopistaEditor().getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems();
    	Collection<String> idLayers = getLayersFromFeatureList(featureList);
    	Collection<String> idFeatures = getFeaturesFromFeatureList(featureList);
    	if(tipo.equalsIgnoreCase("1")){
    		//bienesSeleccionados = (BienBean[])this.bienesJPanel.getBienesSeleccionados().toArray();
        	// Enviar datos a escribir en base de datos
    		//TODO: Siempre es un bien el que se selecciona
//        	for(int i = 0;i<bienesSeleccionados.length;i++){
        		BienBean bienActual = (BienBean) bienSeleccionado;//bienesSeleccionados[i];
        		Collection<String>resultIdLayers = idLayers;
        		Collection<String>resultIdFeatures = idFeatures;
        		if(!replace){
            		Object[] originalIdLayers = bienActual.getIdLayers();
            		Object[] originalIdFeatures = bienActual.getIdFeatures();
            		resultIdLayers = getResultIdLayers(idLayers,idFeatures,originalIdLayers,originalIdFeatures);
            		resultIdFeatures = getResultIdFeatures(idLayers,idFeatures,originalIdLayers,originalIdFeatures);
            		
            	}
        		
        		bienActual.setIdLayers(resultIdLayers);
        		bienActual.setIdFeatures(resultIdFeatures);
        		bienesSeleccionados= new BienBean[1];
        		bienesSeleccionados[0]=bienActual;
//        	}
        	long start=System.currentTimeMillis();
        	inventarioClient.associateBienesInventario(bienesSeleccionados);
        	long finish=System.currentTimeMillis();
        	logger.info("Tiempo para associateBienesInventario: "+Long.toString(finish-start));

        	try {
        		Date date = null;
        		try{
    	        	date = (Date)getInventarioClient().getDate(Const.ACTION_GET_DATE);
    	        } catch (Exception ex) {
    	            StringWriter sw = new StringWriter();
    	            PrintWriter pw = new PrintWriter(sw);
    	            ex.printStackTrace(pw);
    	            logger.error("Exception: " + sw.toString());
    	        }
            	getInventarioFrame().setFecha(new SimpleDateFormat("dd-MM-yyyy kk:mm:ss").format(date));
            	Const.fechaVersion = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(date);
            	Version version = new Version();
                version.setFecha(Const.fechaVersion);
                version.setFeaturesActivas(true);
                AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.VERSION,version);
                //TODO: Hay problemas con las versiones
            	//for(int i = 0;i<bienesSeleccionados.length;i++){
            	//	bienesSeleccionados[i].setFechaVersion(date);
            	//}
                start=System.currentTimeMillis();
	   			 //Actualizamos la feature original
	   			 //decrementarNumeroBienes();
	   			 updateFeatures();

	   			 //Forzamos el repintado incrementando el numero de bienes. Con
    			 //esta sentencia recuperamos los datos de la nueva feature y la ponemos en
    			 //amarillo
    			 redrawNewFeature(featureList.toArray());
    			 
    			 finish=System.currentTimeMillis();
    			 
      			 //start=System.currentTimeMillis();
    			 setFeaturesSelected(featureList);
     			  recargarTablaBienesInventario();
     			  
     			  
                //logger.info("recargarTablaBienesInventario"+Long.toString(finish-start));
    			 //start=System.currentTimeMillis();
    			 //updateFeatures();
    			 //finish=System.currentTimeMillis();
    			 logger.info("updateFeatures"+Long.toString(finish-start));

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		desassociatedata();

    	}else if(tipo.equalsIgnoreCase("2")){
	    	//Obtener los bienes Revertibles seleccionados
    		BienRevertible[] bienesRevertiblesSeleccionados = (BienRevertible[]) this.bienesJPanel.getBienesReversiblesSeleccionados().toArray();

	    	for(int i = 0;i<bienesRevertiblesSeleccionados.length;i++){
	    		BienRevertible bienRevertibleActual = bienesRevertiblesSeleccionados[i];
	    	}
	    	//TODO:FERNANDO U obtenemos los bienes del revertible, o guardamos el revertible con una feature,pero existen campos que no tenemos en estos bienes.

	    }

	}
    
	private Collection<String> getLayersFromFeatureList(Collection<GeopistaFeature> featureList) {
    	Iterator<GeopistaFeature> it = featureList.iterator();
    	ArrayList<String> idLayers = new ArrayList<String>();
    	while(it.hasNext()){
    		GeopistaFeature feature = it.next();
    		idLayers.add(feature.getLayer().getId_LayerDataBase()+"");
    	}
		return idLayers;
	}
	
    private Collection<String> getFeaturesFromFeatureList(
			Collection<GeopistaFeature> featureList) {
    	Iterator<GeopistaFeature> it = featureList.iterator();
    	ArrayList<String> idFeatures = new ArrayList<String>();
    	while(it.hasNext()){
    		GeopistaFeature feature = it.next();
    		idFeatures.add(feature.getSystemId());
    	}
		return idFeatures;
	}
    
	private Collection<String> getResultIdLayers(Collection<String> idLayers,
			Collection<String> idFeatures, Object[] originalIdLayers,
			Object[] originalIdFeatures) {
		ArrayList<String> result = getResultArrayList(idLayers,idFeatures,originalIdLayers,originalIdFeatures);
		
		return getidLayers(result);
	}
	private Collection<String> getResultIdFeatures(Collection<String> idLayers,
			Collection<String> idFeatures, Object[] originalIdLayers,
			Object[] originalIdFeatures) {
		ArrayList<String> result = getResultArrayList(idLayers,idFeatures,originalIdLayers,originalIdFeatures);
		
		return getidFeatures(result);
	}
	
	private Collection<String> getidFeatures(ArrayList<String> result) {
		ArrayList<String> result2 = new ArrayList<String>();
		Iterator<String> it = result.iterator();
		while(it.hasNext()){
			String value = it.next();
			result2.add(value.split("-")[1]);
		}
		return result2;
	}

	
	private Collection<String> getidLayers(ArrayList<String> result) {
		ArrayList<String> result2 = new ArrayList<String>();
		Iterator<String> it = result.iterator();
		while(it.hasNext()){
			String value = it.next();
			result2.add(value.split("-")[0]);
		}
		return result2;
	}
	
	private ArrayList<String> getResultArrayList(
			Collection<String> idLayers, Collection<String> idFeatures,
			Object[] originalIdLayers, Object[] originalIdFeatures) {
		ArrayList layers = new ArrayList(idLayers);
		ArrayList features = new ArrayList(idFeatures);
		ArrayList<String> result = new ArrayList<String>();
		if (layers!=null && features!=null)
		for(int i = 0; i<layers.size();i++){
			String value = layers.get(i)+"-"+features.get(i);
			if(!result.contains(value))
				result.add(value);
		}
		if (originalIdLayers!=null && originalIdLayers.length>0 && originalIdFeatures!=null && originalIdFeatures.length>0)
		for(int j=0;j<originalIdLayers.length;j++){
			//comprueba la longitud de los vectores
				String value = originalIdLayers[j]+"-"+originalIdFeatures[j];
				if(!result.contains(value))
					result.add(value);
		}
		return result;
	}

	public JDialogGeometryBienJPanel getAsociacionDialog() {
		return asociacionDialog;
	}

	public void setAsociacionDialog(JDialogGeometryBienJPanel asociacionDialog) {
		this.asociacionDialog = asociacionDialog;
	}

}
