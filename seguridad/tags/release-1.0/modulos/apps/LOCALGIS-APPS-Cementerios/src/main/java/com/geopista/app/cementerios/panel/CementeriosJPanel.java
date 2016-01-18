package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.CementeriosInternalFrame;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.MainCementerios;
import com.geopista.app.cementerios.UtilidadesComponentes;
import com.geopista.app.cementerios.utils.Validation;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.Version;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.cementerios.BloqueBean;
import com.geopista.protocol.cementerios.CementerioBean;
import com.geopista.protocol.cementerios.CementerioClient;
import com.geopista.protocol.cementerios.ConcesionBean;
import com.geopista.protocol.cementerios.Const;
import com.geopista.protocol.cementerios.DifuntoBean;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.cementerios.ErrorBean;
import com.geopista.protocol.cementerios.ExhumacionBean;
import com.geopista.protocol.cementerios.HistoricoDifuntoBean;
import com.geopista.protocol.cementerios.HistoricoPropiedadBean;
import com.geopista.protocol.cementerios.InhumacionBean;
import com.geopista.protocol.cementerios.IntervencionBean;
import com.geopista.protocol.cementerios.PersonaBean;
import com.geopista.protocol.cementerios.PlazaBean;
import com.geopista.protocol.cementerios.TarifaBean;
import com.geopista.protocol.cementerios.UnidadEnterramientoBean;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class CementeriosJPanel extends JPanel implements FeatureExtendedPanel{
	
    Logger logger= Logger.getLogger(CementeriosJPanel.class);
	private TaskMonitorDialog progressDialog;

    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;

    public TipoElemCementeriosJPanel tipoElemCementeriosJPanel;
    public ElemCementeriosJPanel elemCementeriosJPanel;
    public BotoneraEditorJPanel botoneraJPanel;
    private Object[] listafeatures;
    GeopistaFeature gFeature;

    private CementerioClient cementeriosClient= null;
    private Municipio municipio;
    private Object elemSeleccionado;

    private boolean fromGIS= false;
    private MapaJPanel mapaJPanel;
    private BotoneraAppJPanel botoneraAppJPanel;

    private GestionDocumentalJDialog documentJDialog;

    private BuscarJDialog buscarJDialog;
    
    private FiltrarJDialog filtrarJDialog;
    private InformesJDialog informesJDialog;

    //Paneles
    private UnidadEnterramientoJDialog unidadEnterramientoJDialog;
    private IntervencionJDialog intervencionJDialog;
    private TitularJDialog titularJDialog;
    private ConcesionJDialog concesionJDialog;
    private BloqueJDialog bloqueJDialog;
    private DefuncionJDialog defuncionJDialog;
    private InhumacionJDialog inhumacionJDialog;
    private ExhumacionJDialog exhumacionJDialog;
    private DatosPersonalesJDialog datosPersonalesJDialog;
    private TarifasJDialog tarifaConcesionJDialog;
    private TarifasDifuntosJDialog tarifaDifuntosJDialog;
    private HistoricoDifuntoJDialog historicoDifuntoJDialog;
    private HistoricoPropiedadJDialog historicoPropiedadJDialog;
    
    private ArrayList filtro;
    private String tipoFiltro;
    private String subtipoFiltro;

    /** la primera vez aparecera seleccionado en el dialogo de filtro */
    private boolean aplicarFiltro= true;
    private String cadenaBusqueda;
    private Integer idCementerio;
    private static String nombreCementerio;

    /** Desde GIS. Si no se ha bloqueado la capa para hacer los cambios, lock es true (evitamos hacer cambios en la feature) */
    private boolean lock= false;

    static final int UnidadEnterramiento= 0;
    static final int Bloque=1 ;
    
    static final int Difunto=2 ;
  
    static final int Inhumacion=3 ;
    static final int Exhumacion=4 ;
    
    static final int Titular=6 ;
    static final int Concesion=7 ;
    static final int Intervencion=8 ;
    
    static final int TarifaGPropiedad=9;
    static final int TarifaGDifuntos=10;
    
    static final int HistoricoDifunto=11;
    static final int HistoricoPropiedad=12;
    
    
    /**
     * Método que genera el panel del cementerio para las aplicaciones cliente
     * @param b true si la llamada se hace desde una aplicacion cliente, false si se hace desde el editor de cartografia.
     */
    public CementeriosJPanel(boolean b) {
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        this.desktop= aplicacion.getMainFrame();
        this.locale= aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", true);
        this.fromGIS= b;
        UtilidadesComponentes.inicializar();
        
//        cementeriosClient= new CementerioClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
//        		ServletConstants.CEMENTERIO_SERVLET_NAME);
        
        cementeriosClient= new CementerioClient(aplicacion.getString(AppContext.HOST_ADMCAR) + "/" + WebAppConstants.CEMENTERIOS_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME +
        		ServletConstants.CEMENTERIO_SERVLET_NAME);

        try{municipio= getMunicipio();}catch(Exception e){}
        initComponents();
        
        try {
			nombreCementerio = ((CementerioBean) cementeriosClient.getCementerio(Const.ACTION_GET_CEMENTERIO, idCementerio)).getNombre();
			
		} catch (Exception e) {
			logger.error("Error obteniendo el nombre de cementerio" + e);
		}
        
    }

    private void initComponents(){
        tipoElemCementeriosJPanel= new TipoElemCementeriosJPanel(desktop, locale);
        
        idCementerio = (Integer) aplicacion.getBlackboard().get(Constantes.ID_CEMENTERIO);
        
        elemCementeriosJPanel= new ElemCementeriosJPanel(locale);
        if (fromGIS){
            /** Para evitar que si no hay datos en bienesJPanel, el panel tipoBienesJPanel haga automaticamente un resize */
            tipoElemCementeriosJPanel.setPreferredSize(new Dimension(120, 400));
            tipoElemCementeriosJPanel.setMaximumSize(new Dimension(120, 400));
        }else{
            /** Para evitar que si no hay datos en bienesJPanel, el panel tipoBienesJPanel haga automaticamente un resize */
            tipoElemCementeriosJPanel.setPreferredSize(new Dimension(120, 580));
            tipoElemCementeriosJPanel.setMaximumSize(new Dimension(120, 580));
        }
        botoneraJPanel= new BotoneraEditorJPanel(desktop);
        botoneraJPanel.setEnabled(false);
        botoneraJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				botoneraJPanel_actionPerformed();
			}
		});

        tipoElemCementeriosJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
            	tipoElemCementeriosJPanel_actionPerformed();
			}
		});

        elemCementeriosJPanel.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (ElemCementeriosJPanel.DOBLE_CLICK.equals(e.getActionCommand())){
            		elemJPanel_dobleClick();
            	}else {
            		elemJPanel_actionPerformed();
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
            this.setName( aplicacion.getI18nString("cementerio.InventarioJPanel.name"));
            this.setLayout(new java.awt.BorderLayout());
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                                   tipoElemCementeriosJPanel, elemCementeriosJPanel);
            splitPane.setOneTouchExpandable(true);
            
            splitPane.setDividerLocation(350);
            this.add(splitPane, BorderLayout.CENTER);
            this.add(botoneraJPanel, BorderLayout.SOUTH);
            setPreferredSize(new Dimension(120, 700));
            setMaximumSize(new Dimension(200, 700));


        }catch(Exception e){
            logger.error("Error al mostrar el inventario de una feature ", e);
        }

    }

    private void elemJPanel_dobleClick(){
    	if (!botoneraJPanel.modificarJButtonActionPerformed())
    		botoneraAppJPanel.consultarJButtonActionPerformed();
    }
    private void elemJPanel_actionPerformed(){
        /** Desde GIS. Si no se ha bloqueado la capa para hacer los cambios, lock es true (evitamos hacer cambios en la feature) */
        //if (lock) return;
        Object obj= elemCementeriosJPanel.getElemSeleccionado();
        
	        if ((obj == null) && !((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
	        		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR))))
	        {
	            botoneraJPanel.setEnabled(false);
	            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
	            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
	            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();
	            elemSeleccionado= null;
	            return;
	        }else{
	        	 if ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
	        	            		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASPROP))){
        	    	  botoneraJPanel.setEnabled(true);
        	    	  botoneraJPanel.annadirJButtonSetEnabled(false);
        	    	  if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(true);
        	    	  elemSeleccionado= obj;
	        	 }
	        	 else if ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)) &&
 	            		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASDEF))){
	        		 botoneraJPanel.setEnabled(true);
	        		 botoneraJPanel.annadirJButtonSetEnabled(false);
	        		 if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(true);
	        		 elemSeleccionado= obj;
	        	 }
	        	 else{	
	        	
		        /** marcamos la feature del bien seleccionado por el usuario */
		        if (mapaJPanel != null){
		            mapaJPanel.refreshFeatureSelection(((ElemCementerioBean)obj).getIdFeatures(),((ElemCementerioBean)obj).getIdLayers());
		            setFeaturesSelected(mapaJPanel.getGeopistaEditor().getSelection());
		            // Insertamos en el Blackboard de la aplicacion las features seleccionadas. Necesario para seleccionar los documentos de BD
		            Blackboard Identificadores= aplicacion.getBlackboard();
		            try{Identificadores.put("feature", mapaJPanel.getGeopistaEditor().getSelection().toArray());}catch (Exception e){}
		        }
	       }
	      if (((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
	    		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CONCESION))) || 
	    		
	    		((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)) &&
	    	    (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)))  || 
	    	    
	    	    ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
	            		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR))))
	    	{
	    	
	    	  botoneraJPanel.setEnabled(true);
	    	  botoneraJPanel.annadirJButtonSetEnabled(false);
	    	  if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(true);
	    	  elemSeleccionado= obj;
	    	  
	      }else
	    	  if ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)) &&
	        		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO))){
			      		botoneraJPanel.setEnabled(false);
			      		if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(true);
			      		elemSeleccionado= obj;
	      	}
	    	else if ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)) &&
	        		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_PROPIEDAD))){
			      		botoneraJPanel.setEnabled(false);
			      		if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(true);
			      		elemSeleccionado= obj;
	      	}
	    	  else if ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)) &&
       			   (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DEFUNCION))){
      	    	  botoneraJPanel.setEnabled(true);
	   	    	  botoneraJPanel.eliminarJButtonSetEnabled(false);
	   	    	  if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(true);
	   	    	  elemSeleccionado= obj;
	    	  }
	    	  
	      else{
	        botoneraJPanel.setEnabled(true);
	        if (listafeatures==null || listafeatures.length==0) botoneraJPanel.annadirJButtonSetEnabled(false);
	        if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(true);
	        elemSeleccionado= obj;
	      }
	     }
       
    }

    //de init components
    public void tipoElemCementeriosJPanel_actionPerformed(){
        try{
            if (tipoElemCementeriosJPanel.getSubtipoSeleccionado() == null){
            	
            	elemCementeriosJPanel.setSubtipoSeleccionado(null);
            	
                botoneraJPanel.setEnabled(false);
                if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
                
                elemCementeriosJPanel.loadListaElemCementerios(null);
                if (mapaJPanel != null) mapaJPanel.clear();
                return;
            }
            
            /** Cargamos las features seleccionadas en el mapa:
             * - seleccionada directamente por el usuario.
             * - seleccionada al seleccionar el usurio un tipo de bien resultado de la busqueda
             */
            if (mapaJPanel != null){
                setFeaturesSelected(mapaJPanel.getGeopistaEditor().getSelection());
                /** Insertamos en el Blackboard de la aplicacion las features seleccionadas. Necesario para seleccionar los documentos de BD */
                Blackboard Identificadores= aplicacion.getBlackboard();
                try{Identificadores.put("feature", mapaJPanel.getGeopistaEditor().getSelection().toArray());}catch (Exception e){}
            }
            
            if (((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
            		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CONCESION))) || 
            		
            		((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)) &&
            	    (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)))  || 
            	    
            	    ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
                    		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR))))
            	{
            	
            	  botoneraJPanel.setEnabled(true);
            	  botoneraJPanel.annadirJButtonSetEnabled(false);
            	  if (botoneraAppJPanel != null)botoneraAppJPanel.setEnabled(true);
            	  
          }else{
            
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

          }
            /** Desde el editor GIS, solo es posible seleccionar una unica feature. Desde la aplicacion pueden ser varias */
            
            /** La cadena de busqueda se pierde al cambiar de tipo de bien o seleccionar una feature.
             *  Es necesario guardarlo en una variable global debido a que cada vez que abrimos y cerramos un dialogo (inmueble, nueble, vehiculos...)
             * se recarga la lista de bienes para actualizar los bloqueos. Si no guardasemos la cadena de busqueda,
             * al actualizar la lista de bienes se cargarían los de ese tipo, sin tener en cuenta la busqueda que ha hecho el usuario. */
            cadenaBusqueda= null;
            /** cargamos la lista de bienes en funcion del tipo seleccionado en el arbol */
            recargarTablaElemCementerio();

        }catch(Exception e){
            logger.error("Error al cargar la lista de bienes del tipo seleccionado", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }


    }

    /**
     * Recoge las acciones realizadas sobre la botonera del editos GIS. Son BLOQUEANTES
     */
    public void botoneraJPanel_actionPerformed(){
        try{
            if (botoneraJPanel.getBotonPressed() == null) return;
            /** Comprobamos si hay algun subtipo seleccionado */
            if (tipoElemCementeriosJPanel.getSubtipoSeleccionado() == null) return;

            /** Operaciones que no necesitan abrir dialogo */
            if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_BORRAR) ||
                botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ELIMINAR)){
                String bloqueado= cementeriosClient.bloqueado((ElemCementerioBean)elemSeleccionado);
                 if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN,"",false))){
                    /** Mostramos mensaje de bloqueo del bien */
                    JOptionPane.showMessageDialog(this, aplicacion.getI18nString("cementerio.mensajes.tag1")+" "+bloqueado+"\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
                    recargarTablaElemCementerio();
                    return;
                }//else bloquearCementerio((CementerioBean)elemCementeriosJPanel.getElemSeleccionado());

                if (confirm("cementerio.bienes.tag4", "cementerio.bienes.tag5")){

                	if (elemCementeriosJPanel.getElemSeleccionado() instanceof ConcesionBean){
                		Collection c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                		  Object[] arrayElems = c.toArray();
         	             int n = arrayElems.length;
         	             if (n>0){ 
         	            	 UnidadEnterramientoBean unidad = (UnidadEnterramientoBean) arrayElems[0];
         	            	 Validation validation = Validation.getInstance();
         	            	  if ((unidad.getPlazas()!= null) && (unidad.getPlazas().size() > 0) && (!validation.todasPlazasLibres(unidad))){
         	            		 if (confirm("cementerio.eliminar.confirm1","cementerio.bienes.tag5")){
         	            			Object obj = cementeriosClient.delete(listafeatures, elemCementeriosJPanel.getElemSeleccionado(), null, idCementerio);
         	            		 	}
         	            		 }else{
         	            			Object obj = cementeriosClient.delete(listafeatures, elemCementeriosJPanel.getElemSeleccionado(), null, idCementerio);
         	            		 }
         	             	}
                	}
                	else{
                		Object obj = cementeriosClient.delete(listafeatures, elemCementeriosJPanel.getElemSeleccionado(), null, idCementerio);
                		if (obj instanceof ErrorBean){
                		JOptionPane.showMessageDialog(desktop,
                				((ErrorBean)obj).getMessage(),
                				((ErrorBean)obj).getTitle(),
                		        JOptionPane.WARNING_MESSAGE);
                		}else{
                			elemCementeriosJPanel.deleteElemCementerioTabla(obj);
                		}
                	}
	                    updateFeatures();
		                botoneraJPanel.setEnabled(false);
		                if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
		                if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
		                if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();
                }
                /** RECARGAMOS TABLA - refrescamos los bloqueos */
                if (mapaJPanel != null){
                        mapaJPanel.refreshFeatureSelection(null,null);
                        setFeaturesSelected(mapaJPanel.getGeopistaEditor().getSelection());
                        cargarMapa();    
                	
                }
                recargarTablaElemCementerio();
                return;
            }

            /** Operaciones que abren el dialogo */
            if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
                if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_BLOQUE)){
                    abrirDialogo(Bloque,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                }
                if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
                    abrirDialogo(UnidadEnterramiento,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                }
            }else 
            if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
            	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DEFUNCION)){
            		abrirDialogo(Difunto,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            	}
            }
            if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
            	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INHUMACION)){
            		abrirDialogo(Inhumacion,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            	}
            	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_EXHUMACION)){
            		abrirDialogo(Exhumacion,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            	}
            	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
            		abrirDialogo(TarifaGDifuntos,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            	}
            }
            if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
            	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR)){
            		abrirDialogo(Titular,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            	}
            	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CONCESION)){
            		abrirDialogo(Concesion,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            	}
            	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASPROP)){
            		abrirDialogo(TarifaGPropiedad,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            	}
            }	
            if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
                if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INTERVENCION)){
                	abrirDialogo(Intervencion,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                }
            }
            if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
                if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
                	abrirDialogo(HistoricoDifunto,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                }
            }

        }catch (Exception e){
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
        }

        botoneraJPanel.setBotonPressed(null);
        if (botoneraAppJPanel!=null)  botoneraAppJPanel.setBotonPressed(null);

    }

    /**
     * Recoge las acciones realizadas sobre la botonera de la aplicacion. No son BLOQUEANTES
     */
    private void botoneraAppJPanel_actionPerformed(){
    	
        try{
        	
            idCementerio = (Integer) aplicacion.getBlackboard().get(Constantes.ID_CEMENTERIO);
        	
            /** Las operaciones con esta botonera no son BLOQUEANTES */
            if (botoneraAppJPanel==null) return;
            if (botoneraAppJPanel.getBotonPressed() == null) return;
            /** Comprobamos si hay algun subtipo seleccionado */
            if (tipoElemCementeriosJPanel.getSubtipoSeleccionado() == null) return;

            if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
                if (elemSeleccionado == null) return;
                
                /** Operaciones que abren el dialogo */
                if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
                    if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_BLOQUE)){
                    	abrirDialogoBloque(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                    }
                    if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
                        abrirDialogoUnidadEnterramiento(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado(), false);
                    }
                }else 
                if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
                	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DEFUNCION)){
                		abrirDialogoDefuncionFromTree(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado(), false);
                	}
                	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INHUMACION)){
                		abrirDialogoInhumacion(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                	}
                	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_EXHUMACION)){
                		abrirDialogoExhumacion(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                	}
                	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
                		abrirDialogoTarifasGDifuntos(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                	}

                }
                if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
                	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR)){
                		abrirDialogoTitular(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                		                	}
                	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CONCESION)){
                		abrirDialogoConcesion(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                	}
                	if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASPROP)){
                		abrirDialogoTarifasGPropiedad(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                	}
                }
                if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
                    if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
                    	abrirDialogoHistoricoDifunto(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                    }
                    if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_PROPIEDAD)){
                    	abrirDialogoHistoricoPropiedad(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                    }
                }
                if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
                    if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INTERVENCION)){
                    	abrirDialogoIntervencion(Constantes.OPERACION_CONSULTAR,tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                    }
                }
            }else if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_BUSCAR)){
                abrirDialogoBusqueda();
            }else if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_FILTRAR)){
                abrirDialogoFiltro();
            }else if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_INFORMES)){
                abrirDialogoInformes();
            }else if (botoneraAppJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_RECUPERAR)){
                String bloqueado= cementeriosClient.bloqueado((ElemCementerioBean)elemSeleccionado);
                if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN,"",false))){
                	/** Mostramos mensaje de bloqueo del bien */
                    JOptionPane.showMessageDialog(this, aplicacion.getI18nString("cementerio.mensajes.tag1")+" "+bloqueado+"\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
                    recargarTablaElemCementerio();
                    return;
                }else bloquearCementerio((ElemCementerioBean)elemCementeriosJPanel.getElemSeleccionado());

                if (confirm("cementerio.bienes.tag6", "cementerio.bienes.tag7")){
                    elemCementeriosJPanel.clearSelection();
                    botoneraJPanel.setEnabled(false);
                    if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
                    if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
                    if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();
                }
                /** RECARGAMOS TABLA - refrescamos los bloqueos */
                if (mapaJPanel != null){
                	cargarMapa();                
                }
                recargarTablaElemCementerio();
                return;
            }
        }catch (Exception e){
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
        }

        botoneraJPanel.setBotonPressed(null);
        botoneraAppJPanel.setBotonPressed(null);

    }

    
    private void abrirDialogoTarifasGPropiedad(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        	tarifaConcesionJDialog= new TarifasJDialog(desktop, locale, operacion,tipo);
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			tarifaConcesionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		TarifaBean tarifa = new TarifaBean();
        		tarifa.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		tarifa.setTipo_tarifa(Constantes.TARIFA_GPROPIEDAD);
        		
        		if (municipio != null) {
        			tarifa.setIdMunicipio(municipio.getId());
        			tarifa.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			tarifa.setNombreCementerio(nombreCementerio);
        		}

        		tarifaConcesionJDialog.load(tarifa, true);

        	//MODIFICAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof TarifaBean)) return;
        		try{
        			tarifaConcesionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		tarifaConcesionJDialog.load((TarifaBean)elemSeleccionado, true);
        		
        	//CONSULTAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof TarifaBean)) return;
	    		try{
	    			tarifaConcesionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
	    		tarifaConcesionJDialog.load((TarifaBean)elemSeleccionado, false);
	    	}


        	tarifaConcesionJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			tarifaConcesionJDialog_actionPerformed(operacion);
        		}
        	});

        	GUIUtil.centreOnWindow(tarifaConcesionJDialog);
        	tarifaConcesionJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void abrirDialogoTarifasGDifuntos(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        	tarifaDifuntosJDialog= new TarifasDifuntosJDialog(desktop, locale, operacion,tipo);
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			tarifaDifuntosJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		TarifaBean tarifa = new TarifaBean();
        		tarifa.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		tarifa.setTipo_tarifa(Constantes.TARIFA_GDIFUNTOS);
        		
        		if (municipio != null) {
        			tarifa.setIdMunicipio(municipio.getId());
        			tarifa.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			tarifa.setNombreCementerio(nombreCementerio);
        		}

        		tarifaDifuntosJDialog.load(tarifa, true);

        	//MODIFICAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof TarifaBean)) return;
        		try{
        			tarifaDifuntosJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		tarifaDifuntosJDialog.load((TarifaBean)elemSeleccionado, true);
        		
        	//CONSULTAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof TarifaBean)) return;
	    		try{titularJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
	    		tarifaDifuntosJDialog.load((TarifaBean)elemSeleccionado, false);
	    	}


        	tarifaDifuntosJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			tarifaDifuntosJDialog_actionPerformed(operacion);
        		}
        	});

        	GUIUtil.centreOnWindow(tarifaDifuntosJDialog);
        	tarifaDifuntosJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    
    private void abrirDialogoHistoricoDifunto(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        DifuntoBean difunto = null;
    	if (elemCementeriosJPanel.getElemSeleccionado() instanceof DifuntoBean){
    		difunto = (DifuntoBean) elemCementeriosJPanel.getElemSeleccionado();
    	
	    /**Historico**/
		Collection c = cementeriosClient.getHistoricos(Const.ACTION_GET_HISTORICO, Const.SUPERPATRON_GHISTORICOS, Const.PATRON_HISTORICO_DIFUNTO,
				cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio, difunto);
		Object[] arrayElems = c.toArray();
	    int n = arrayElems.length;
	    ArrayList listaHistorico = new ArrayList();
	    HistoricoDifuntoBean historico;
	    for (int i = 0; i < arrayElems.length; i++) {
	    	historico = (HistoricoDifuntoBean) arrayElems[i];
	    	listaHistorico.add(historico);
		} 
        
        
        try{
        	historicoDifuntoJDialog= new HistoricoDifuntoJDialog(desktop, locale, operacion,tipo,difunto, listaHistorico );
        	// No hay ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof DifuntoBean)) return;
	    		try{
	    			historicoDifuntoJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
	    		historicoDifuntoJDialog.load((DifuntoBean)elemSeleccionado, false);
	    	}


        	historicoDifuntoJDialog.addActionListener(new java.awt.event.ActionListener(){
            	public void actionPerformed(ActionEvent e){
            		if (ElemCementeriosJPanel.DOBLE_CLICK.equals(e.getActionCommand())){
            			HistoricoDifuntoBean elem = (HistoricoDifuntoBean) historicoDifuntoJDialog.getHistoricoSeleccionado();
            			elemSeleccionado = elem.getElem();            			
            			if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            				historicoJTable_dobleClick(elem.getTipo(), elem);
                        	historicoDifuntosJDialog_actionPerformed(Constantes.OPERACION_CONSULTAR);                        	
            			}else{
	            				historicoJTable_dobleClick(elem.getTipo(), elem);
	            				historicoDifuntosJDialog_actionPerformed(Constantes.OPERACION_CONSULTAR);
            			}
            		}
            		else {
            			historicoDifuntosJDialog_actionPerformed(operacion);
                	}
    			}
    		});

        	GUIUtil.centreOnWindow(historicoDifuntoJDialog);
        	historicoDifuntoJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    	}else{
    		JOptionPane.showMessageDialog(desktop, "El difunto seleccionado, no está correctamente dado de alta");
	    }
    }

    /**
     * 
     * @param operacion
     * @param tipo
     * @throws Exception
     */
    private void abrirDialogoHistoricoPropiedad(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        PersonaBean titular = null;
    	if (elemCementeriosJPanel.getElemSeleccionado() instanceof PersonaBean){
    		titular = (PersonaBean) elemCementeriosJPanel.getElemSeleccionado();
    	
	    /**Historico**/
		Collection c = cementeriosClient.getHistoricos(Const.ACTION_GET_HISTORICO, Const.SUPERPATRON_GHISTORICOS, Const.PATRON_HISTORICO_PROPIEDAD,
				cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio, titular);
		Object[] arrayElems = c.toArray();
	    int n = arrayElems.length;
	    ArrayList listaHistorico = new ArrayList();
	    HistoricoPropiedadBean historico;
	    for (int i = 0; i < arrayElems.length; i++) {
	    	historico = (HistoricoPropiedadBean) arrayElems[i];
	    	listaHistorico.add(historico);
		} 
        
        
        try{
        	historicoPropiedadJDialog= new HistoricoPropiedadJDialog(desktop, locale, operacion,tipo,titular, listaHistorico );
        	// No hay ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof PersonaBean)) return;
	    		try{
	    			historicoPropiedadJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
	    		historicoPropiedadJDialog.load((PersonaBean)elemSeleccionado, false);
	    	}


        	historicoPropiedadJDialog.addActionListener(new java.awt.event.ActionListener(){
            	public void actionPerformed(ActionEvent e){
            		if (ElemCementeriosJPanel.DOBLE_CLICK.equals(e.getActionCommand())){
            			HistoricoPropiedadBean elem = (HistoricoPropiedadBean) historicoPropiedadJDialog.getHistoricoSeleccionado();
            			elemSeleccionado = elem.getElem();            			
            			if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            				historicoPropiedadJTable_dobleClick(elem.getTipo(), elem);
                        	historicoPropiedadJDialog_actionPerformed(Constantes.OPERACION_CONSULTAR);                        	
            			}else{
	            				historicoPropiedadJTable_dobleClick(elem.getTipo(), elem);
	            				historicoPropiedadJDialog_actionPerformed(Constantes.OPERACION_CONSULTAR);
            			}
            		}
            		else {
            			historicoPropiedadJDialog_actionPerformed(operacion);
                	}
    			}
    		});

        	GUIUtil.centreOnWindow(historicoPropiedadJDialog);
        	historicoPropiedadJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    	}else{
    		JOptionPane.showMessageDialog(desktop, "El difunto seleccionado, no está correctamente dado de alta");
	    }
    }
    
    
    @SuppressWarnings("rawtypes")
	private void abrirDialogoUnidadEnterramiento(final String operacion,String tipo, final boolean fromPlaza) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        	 ConcesionBean concesion = null;
    		/**Comprobamos si en la feature seleccionada hay un bloque dado de alta **/
    		Collection c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_BLOQUE, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
             Object[] arrayElems = c.toArray();
             int n = arrayElems.length;
             BloqueBean bloque = null;
             if (n>0){ 
            	 bloque = (BloqueBean) arrayElems[0];
             }
        	unidadEnterramientoJDialog= new UnidadEnterramientoJDialog(desktop, locale, operacion,tipo);
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		unidadEnterramientoJDialog= new UnidadEnterramientoJDialog(desktop, locale, operacion,tipo);
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			unidadEnterramientoJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		UnidadEnterramientoBean unidadEnterramiento = new UnidadEnterramientoBean();
        		unidadEnterramiento.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		
        		if (municipio != null) {
        			unidadEnterramiento.setIdMunicipio(municipio.getId());
        			unidadEnterramiento.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			unidadEnterramiento.setNombreCementerio(nombreCementerio);

        		}
        		
        		if (bloque != null){
               	 unidadEnterramiento.setFila(bloque.getNumFilas());
               	 unidadEnterramiento.setColumna(bloque.getNumColumnas());
               	 unidadEnterramiento.setTipo_unidad(bloque.getTipo_unidades());
                }

        		unidadEnterramientoJDialog.load(unidadEnterramiento, true, operacion, bloque, concesion);

        	//MODIFICAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof UnidadEnterramientoBean)) return;
        		try{
        			unidadEnterramientoJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		/**Comprobamos si la unidad de enterramiento tiene una concesion asociada**/
        		 c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GPROPIEDAD, Const.PATRON_CONCESION, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                 arrayElems = c.toArray();
                 n = arrayElems.length;
                 if (n>0){ 
                	 concesion = (ConcesionBean) arrayElems[0];
                 }
        		
        		unidadEnterramientoJDialog.load((UnidadEnterramientoBean)elemSeleccionado, true, operacion, bloque, concesion);
        		
        	//CONSULTAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof UnidadEnterramientoBean)) return;
	    		try{
	    			unidadEnterramientoJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
        		/**Comprobamos si la unidad de enterramiento tiene una concesion asociada**/
       		 	c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GPROPIEDAD, Const.PATRON_CONCESION, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                arrayElems = c.toArray();
                n = arrayElems.length;
                if (n>0){ 
               	 concesion = (ConcesionBean) arrayElems[0];
                }
	    		unidadEnterramientoJDialog.load((UnidadEnterramientoBean)elemSeleccionado, false, operacion, bloque, concesion);
	    	}

        	final ConcesionBean concesionfinal = concesion;
        	
        	unidadEnterramientoJDialog.addActionListener(new java.awt.event.ActionListener(){
            	public void actionPerformed(ActionEvent e){
            		if (ElemCementeriosJPanel.DOBLE_CLICK.equals(e.getActionCommand())){
            			if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            				plazasJTable_dobleClick(operacion);
                        	unidadEnterramientoJDialog_actionPerformed(Constantes.OPERACION_CONSULTAR, true);
            			}else{
	            			if (concesionfinal != null){
	            				plazasJTable_dobleClick(operacion);
	                        	unidadEnterramientoJDialog_actionPerformed(Constantes.OPERACION_CONSULTAR, true);
	            			}else{
	            				String message = "No hay ninguna concesion asociada";
	            				JOptionPane.showMessageDialog(desktop, message);
	            			}
            			}
                	}else {
                		unidadEnterramientoJDialog_actionPerformed(operacion, false);
                	}
    			}
    		});

        	GUIUtil.centreOnWindow(unidadEnterramientoJDialog);
        	unidadEnterramientoJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    
    private void abrirDialogoIntervencion(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        	intervencionJDialog= new IntervencionJDialog(desktop, locale, operacion,tipo);
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			intervencionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		IntervencionBean intervencion = new IntervencionBean();
        		intervencion.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		
        		if (municipio != null) {
        			intervencion.setIdMunicipio(municipio.getId());
        			intervencion.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			intervencion.setNombreCementerio(nombreCementerio);
        		}
        		intervencionJDialog.load(intervencion, true, operacion);

        	//MODIFICAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof IntervencionBean)) return;
        		try{
        			intervencionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		intervencionJDialog.load((IntervencionBean)elemSeleccionado, true, operacion);
        		
        	//CONSULTAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof IntervencionBean)) return;
	    		try{intervencionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
	    		intervencionJDialog.load((IntervencionBean)elemSeleccionado, false, operacion);
	    	}


        	intervencionJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			intervencionJDialog_actionPerformed(operacion);
        		}
        	});

        	GUIUtil.centreOnWindow(intervencionJDialog);
        	intervencionJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * AbrirDialogoTitular
     * @param operacion
     * @param tipo
     * @throws Exception
     */
    private void abrirDialogoTitular(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        	titularJDialog= new TitularJDialog(desktop, locale, operacion,tipo);
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			titularJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		PersonaBean titular = new PersonaBean();
        		titular.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		
        		if (municipio != null) {
        			titular.setIdMunicipio(municipio.getId());
        			titular.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			titular.setNombre(nombreCementerio);
        		}

        		titularJDialog.load(titular, true, operacion);

        	//MODIFICAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof PersonaBean)) return;
        		try{
        			titularJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		titularJDialog.load((PersonaBean)elemSeleccionado, true, operacion);
        		
        	//CONSULTAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof PersonaBean)) return;
	    		try{titularJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
	    		titularJDialog.load((PersonaBean)elemSeleccionado, false, operacion);
	    	}


        	titularJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			titularJDialog_actionPerformed(operacion);
        		}
        	});

        	GUIUtil.centreOnWindow(titularJDialog);
        	titularJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void abrirDialogoConcesion(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
    		Collection c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
  		  	Object[] arrayElems = c.toArray();
            int n = arrayElems.length;
            Vector listaSimple = new Vector();
            UnidadEnterramientoBean unidad;
            for (int i = 0; i < arrayElems.length; i++) {
            	unidad = (UnidadEnterramientoBean) arrayElems[i];
            	listaSimple.add(unidad);
			} 
            //Lista de tarifas que existen segun cementerio
    		c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GPROPIEDAD, Const.PATRON_TARIFASPROP, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
  		  	arrayElems = c.toArray();
            n = arrayElems.length;
            ArrayList listaTarifas = new ArrayList();
            TarifaBean tarifa;
            for (int i = 0; i < arrayElems.length; i++) {
            	tarifa = (TarifaBean) arrayElems[i];
            	listaTarifas.add(tarifa);
			} 
            
        	concesionJDialog= new ConcesionJDialog(desktop, locale, operacion,tipo, listaSimple, listaTarifas);
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			concesionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		ConcesionBean concesion = new ConcesionBean();
        		concesion.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		
        		if (municipio != null) {
        			concesion.setIdMunicipio(municipio.getId());
        			concesion.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			concesion.setNombreCementerio(nombreCementerio);
        		}
        		concesionJDialog.load(concesion, true, operacion);

        	//MODIFICAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof ConcesionBean)) return;
        		try{
        			concesionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		concesionJDialog.load((ConcesionBean)elemSeleccionado, true, operacion);
        		
        	//CONSULTAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof ConcesionBean)) return;
	    		try{concesionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
	    		concesionJDialog.load((ConcesionBean)elemSeleccionado, false, operacion);
	    	}


        	concesionJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			concesionJDialog_actionPerformed(operacion);
        		}
        	});

        	GUIUtil.centreOnWindow(concesionJDialog);
        	concesionJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
 
    /**
     * Abre el dialogo para un bien inmueble
     * @param operacion que realiza el usuario
     * @throws Exception
     */
    private void abrirDialogoDefuncionFromTree(final String operacion,String tipo, final boolean fromplaza) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        
    		Collection c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
  		  	Object[] arrayElems = c.toArray();
            int n = arrayElems.length;
            Vector listaSimple = new Vector();
            UnidadEnterramientoBean unidad;
            for (int i = 0; i < arrayElems.length; i++) {
            	unidad = (UnidadEnterramientoBean) arrayElems[i];
            	listaSimple.add(unidad);
			}
            
       	 
			 /**Comprobamos si la unidad de enterramiento tiene una concesion asociada**/
	    	 c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GPROPIEDAD, Const.PATRON_CONCESION, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
	    	 arrayElems = c.toArray();
	    	 n = arrayElems.length;
	    	 Vector listaConcesiones = new Vector();
	    	 ConcesionBean concesion;
	    	 for (int i = 0; i < arrayElems.length; i++) {
	    		 concesion = (ConcesionBean) arrayElems[i];
	    		 listaConcesiones.add(concesion);
			} 
//	    		 concesion = (ConcesionBean) arrayElems[0];
//	    		 if (concesion == null){
//	                 
//	 				String message = "No hay ninguna concesion asociada";
//	 				JOptionPane.showMessageDialog(desktop, message);
//	 				return;
//	    		 }
//	        } 
            
            
            
        	defuncionJDialog= new DefuncionJDialog(desktop, locale,operacion, tipo, listaSimple, listaConcesiones);
        	

        	 
//     		/**Comprobamos si en la feature seleccionada hay una unidad dado de alta y recuperamos las plazas **/
//     		 c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
//             arrayElems = c.toArray();
//             n = arrayElems.length;
//             unidad = null;
//             if (n>0){ 
//             	 unidad = (UnidadEnterramientoBean) arrayElems[0];
//            	 defuncionJDialog.loadListaPlazas(unidad.getPlazas());
//            	 defuncionJDialog.setUnidadEnterramiento(unidad);
//            	 defuncionJDialog.setListaPlazasTabla(unidad.getPlazas());
//            	 
//            	 if ((operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)) && ((unidad.getPlazas()== null) || (unidad.getPlazas().size()== 0))){
//            		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("cementerio.mensajes.plazas"),"Añadir Difunto",
//             				JOptionPane.WARNING_MESSAGE);
//            		 
//            		 return;
//            	 }
//              }
        	
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			defuncionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		DifuntoBean difunto = defuncionJDialog.getDifunto();
        		
        		if ((difunto == null ) &&  (defuncionJDialog.getPlazaSelected() != null)){
        			if (defuncionJDialog.getPlazaSelected().getDifunto() != null){
        				difunto = defuncionJDialog.getPlazaSelected().getDifunto();
        			}
        		}
        		if (difunto == null) difunto = new DifuntoBean();
        		if (fromplaza){
	        		if (defuncionJDialog.getPlazaSelected() != null){
	        			difunto.setId_plaza(defuncionJDialog.getPlazaSelected().getIdPlaza());
	        		}
        		}
        		difunto.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		if (municipio != null) {
        			difunto.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			difunto.setNombreCementerio(nombreCementerio);
        		}
        		defuncionJDialog.load(difunto, true);
        	} 
        	//MODIFICAR	
        	else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
           		elemCementeriosJPanel.clearSelection();        		
        		try{
        			defuncionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		DifuntoBean difunto = defuncionJDialog.getDifunto();
        		if ((difunto == null ) &&  (defuncionJDialog.getPlazaSelected() != null)){
        			if (defuncionJDialog.getPlazaSelected().getDifunto() != null){
        				difunto = defuncionJDialog.getPlazaSelected().getDifunto();
        			}
        		}
        		if (defuncionJDialog.getPlazaSelected() != null){
        			difunto.setId_plaza(defuncionJDialog.getPlazaSelected().getIdPlaza());
        		}
        	 	
        		if (difunto == null){
        			difunto = (DifuntoBean)elemSeleccionado;
        		}
        		
        		difunto.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		if (municipio != null) {
        			difunto.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			difunto.setNombreCementerio(nombreCementerio);
        		}
        		defuncionJDialog.load(difunto, true);
        	}
        	//CONSULTAR
        	else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
           		elemCementeriosJPanel.clearSelection();        		
        		try{
        			defuncionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
        		}catch (Exception e){}
        		
        		DifuntoBean difunto = defuncionJDialog.getDifunto();
        		if ((difunto == null ) &&  (defuncionJDialog.getPlazaSelected() != null)){
        			if (defuncionJDialog.getPlazaSelected().getDifunto() != null){
        				difunto = defuncionJDialog.getPlazaSelected().getDifunto();
        			}
        		}
        		if (defuncionJDialog.getPlazaSelected() != null){
        			difunto.setId_plaza(defuncionJDialog.getPlazaSelected().getIdPlaza());
        		}
        	 	
        		if (difunto == null){
        			difunto = (DifuntoBean)elemSeleccionado;
        		}
        		
        		difunto.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		if (municipio != null) {
        			difunto.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			difunto.setNombreCementerio(nombreCementerio);
        		}
        		defuncionJDialog.load(difunto, false);
        	}
        	
        	
        	defuncionJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			defuncionJDialog_actionPerformedFromTree(operacion, fromplaza);
        		}
        	});

        	GUIUtil.centreOnWindow(defuncionJDialog);
        	defuncionJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
  /**
  * Abre el dialogo para un bien inmueble
  * @param operacion que realiza el usuario
  * @throws Exception
  */
 private void abrirDialogoDefuncionFromPlaza(final String operacion,String tipo, final boolean fromplaza) throws Exception{
     this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
     try{
    	 
 		Collection c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
		  	Object[] arrayElems = c.toArray();
        int n = arrayElems.length;
        Vector listaSimple = new Vector();
        UnidadEnterramientoBean unidad;
        for (int i = 0; i < arrayElems.length; i++) {
        	unidad = (UnidadEnterramientoBean) arrayElems[i];
        	listaSimple.add(unidad);
		} 
    	
         /**Comprobamos si la unidad de enterramiento tiene una concesion asociada**/
	   	 c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GPROPIEDAD, Const.PATRON_CONCESION, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
	   	 arrayElems = c.toArray();
	   	 n = arrayElems.length;
	   	 Vector listaConcesiones = new Vector();
	   	 ConcesionBean concesion;
	   	 for (int i = 0; i < arrayElems.length; i++) {
	   		 concesion = (ConcesionBean) arrayElems[i];
	   		 listaConcesiones.add(concesion);
			} 
        
    	defuncionJDialog= new DefuncionJDialog(desktop, locale,operacion, tipo, listaSimple, listaConcesiones);
    	 
     	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
     		elemCementeriosJPanel.clearSelection();        		
     		try{
     			defuncionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
     		}catch (Exception e){}
     		
     		DifuntoBean difunto = defuncionJDialog.getDifunto();
     		
     		if (fromplaza){
	        		if ((difunto == null ) &&  (unidadEnterramientoJDialog.getPlazaSelected() != null)){
	        			if (unidadEnterramientoJDialog.getPlazaSelected().getDifunto() != null){
	        				difunto = unidadEnterramientoJDialog.getPlazaSelected().getDifunto();
	        			}
	        		}
     		}
     		if (difunto == null) difunto = new DifuntoBean();
     		if (fromplaza){
	        		if (unidadEnterramientoJDialog.getPlazaSelected() != null){
	        			difunto.setId_plaza(unidadEnterramientoJDialog.getPlazaSelected().getIdPlaza());
//	        			ArrayList<PlazaBean> listaPlazas = new ArrayList<PlazaBean>();
//	        			listaPlazas.add(unidadEnterramientoJDialog.getPlazaSelected());
	        			ArrayList<PlazaBean> listaPlazas = unidadEnterramientoJDialog.getListaPlazasTabla();
	        			if (listaPlazas.size()== 0){
		        			listaPlazas = new ArrayList<PlazaBean>();
		        			listaPlazas.add(unidadEnterramientoJDialog.getPlazaSelected());
	        			}
	        			defuncionJDialog.setListaPlazasTabla(listaPlazas);
	        			defuncionJDialog.loadListaPlazas(listaPlazas);
	        		}
     		}
     	 	
     		difunto.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
     		if (municipio != null) {
     			difunto.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
     			difunto.setNombreCementerio(nombreCementerio);
     		}
     		defuncionJDialog.load(difunto, true);
     	}
     	//CONSULTAR
     	else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
        		elemCementeriosJPanel.clearSelection();        		
     		try{
     			defuncionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
     		}catch (Exception e){}
     		
     		DifuntoBean difunto = defuncionJDialog.getDifunto();
     		if ((difunto == null ) &&  (unidadEnterramientoJDialog.getPlazaSelected() != null)){
     			if (unidadEnterramientoJDialog.getPlazaSelected().getDifunto() != null){
     				difunto = unidadEnterramientoJDialog.getPlazaSelected().getDifunto();
     			}
     		}
     		if (unidadEnterramientoJDialog.getPlazaSelected() != null){
     			difunto.setId_plaza(unidadEnterramientoJDialog.getPlazaSelected().getIdPlaza());
     		}
     	 	
     		difunto.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
     		if (municipio != null) {
     			difunto.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
     			difunto.setNombreCementerio(nombreCementerio);
     		}
     		defuncionJDialog.load(difunto, false);
     	}
     	
     	
     	defuncionJDialog.addActionListener(new java.awt.event.ActionListener(){
     		public void actionPerformed(ActionEvent e){
     			defuncionJDialog_actionPerformedFromPlaza(operacion, fromplaza);
     		}
     	});

     	GUIUtil.centreOnWindow(defuncionJDialog);
     	defuncionJDialog.show();
     }finally{
     	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
     }
 }
    
    
    /**
     * Abre el dialogo Para un bloque
     * @param operacion que realiza el usuario
     * @throws Exception
     */
    private void abrirDialogoBloque(final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        	bloqueJDialog= new BloqueJDialog(desktop, locale, operacion,tipo);
        	
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			bloqueJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag1"));
        		}catch (Exception e){}
        		
        		BloqueBean bloque = new BloqueBean();
        		bloque.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		
        		if (municipio != null) {
        			bloque.setIdMunicipio(municipio.getId());
        			bloque.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
//        			bloque.setNombre(municipio.getNombre() + " - " + aplicacion.getI18nString("cementerio.entidad.tag"));
        			bloque.setNombreCementerio(nombreCementerio);
        		}
        		bloqueJDialog.load(bloque, true);

        	//MODIFICAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof BloqueBean)) return;
        		try{
        			bloqueJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		bloqueJDialog.load((BloqueBean)elemSeleccionado, true);
        		
        	//CONSULTAR	
        	}else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		if (elemSeleccionado==null) return;
	    		if (!(elemSeleccionado instanceof BloqueBean)) return;
	    		try{bloqueJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
	    		}catch (Exception e){}
	    		
	    		bloqueJDialog.load((BloqueBean)elemSeleccionado, false);
	    	}


        	bloqueJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			bloqueJDialog_actionPerformed(operacion);
        		}
        	});

        	GUIUtil.centreOnWindow(bloqueJDialog);
        	bloqueJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    
    /**
     * Abre el dialogo Para una operacion de inhumacion
     * @param operacion que realiza el usuario
     * @throws Exception
     */
    private void   abrirDialogoInhumacion (final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
		Collection c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
		  	Object[] arrayElems = c.toArray();
        int n = arrayElems.length;
        Vector listaSimple = new Vector();
        UnidadEnterramientoBean unidad = null;
        if (arrayElems.length == 0) {
        	String message1 = "No hay ninguna unidad de enterramiento dada de alta en la selección.\n ";
        	String message2 = "Haga una selección válida.";
        	message2 = StringUtils.center(message2, message1.length());
        	String message = message1.concat(message2);
			JOptionPane.showMessageDialog(desktop, message);
        }else{
        if (arrayElems.length <=1){
        	unidad = (UnidadEnterramientoBean) arrayElems[0];
        }

        /**Tarifas y difuntos**/
		c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GDIFUNTOS, Const.PATRON_DEFUNCION, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
		arrayElems = c.toArray();
        n = arrayElems.length;
        ArrayList listaDifuntos = new ArrayList();
        DifuntoBean difunto;
        for (int i = 0; i < arrayElems.length; i++) {
        	difunto = (DifuntoBean) arrayElems[i];
        	listaDifuntos.add(difunto);
		} 
        //Lista de tarifas que existen segun cementerio
		c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GDIFUNTOS, Const.PATRON_TARIFASDEF, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
		  	arrayElems = c.toArray();
        n = arrayElems.length;
        ArrayList listaTarifas = new ArrayList();
        TarifaBean tarifa;
        for (int i = 0; i < arrayElems.length; i++) {
        	tarifa = (TarifaBean) arrayElems[i];
        	listaTarifas.add(tarifa);
		} 
        
        try{
        	inhumacionJDialog= new InhumacionJDialog(desktop, locale, operacion,tipo, unidad, listaDifuntos, listaTarifas);
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			inhumacionJDialog.setTitle(aplicacion.getI18nString("cementerio.InhumacionDialog.title"));
        		}catch (Exception e){}

        		InhumacionBean inhumacion= new InhumacionBean();
        		inhumacion.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());

        		if (municipio != null) {
        			inhumacion.setIdMunicipio(municipio.getId());
        			inhumacion.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			inhumacion.setNombreCementerio(municipio.getNombre() + " - " + nombreCementerio);
        		}
        		
        		inhumacionJDialog.load(inhumacion, true);
        	}
        	//MODIFICAR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof InhumacionBean)) return;
        		try{
        			inhumacionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		if (municipio != null) {
        			((InhumacionBean)elemSeleccionado).setIdMunicipio(municipio.getId());
        			((InhumacionBean)elemSeleccionado).setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			((InhumacionBean)elemSeleccionado).setNombreCementerio(nombreCementerio);
        		}
    		
        		((InhumacionBean)elemSeleccionado).setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		inhumacionJDialog.load((InhumacionBean)elemSeleccionado, true);
        		
        	}
        	//CONSULTAR	
        	else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof InhumacionBean)) return;
        		try{
        			inhumacionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
        		}catch (Exception e){}
    		
        		if (municipio != null) {
        			((InhumacionBean)elemSeleccionado).setIdMunicipio(municipio.getId());
        			((InhumacionBean)elemSeleccionado).setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			((InhumacionBean)elemSeleccionado).setNombreCementerio(nombreCementerio);
        		}
        		inhumacionJDialog.load((InhumacionBean)elemSeleccionado, false);
        	}
        
        	inhumacionJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			inhumacionJDialog_actionPerformed(operacion);
        		}
        	});
        	
        	GUIUtil.centreOnWindow(inhumacionJDialog);
        	inhumacionJDialog.show();
        
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Abre el dialogo Para una operacion de inhumacion
     * @param operacion que realiza el usuario
     * @throws Exception
     */
    private void   abrirDialogoExhumacion (final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
		Collection c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
		  	Object[] arrayElems = c.toArray();
        int n = arrayElems.length;
        Vector listaSimple = new Vector();
        UnidadEnterramientoBean unidad = null;
        if (arrayElems.length == 0) {
        	String message1 = "No hay ninguna unidad de enterramiento dada de alta en la selección.\n ";
        	String message2 = "Haga una selección válida.";
        	message2 = StringUtils.center(message2, message1.length());
        	String message = message1.concat(message2);
			JOptionPane.showMessageDialog(desktop, message);
        }else{
        if (arrayElems.length <=1){
        	unidad = (UnidadEnterramientoBean) arrayElems[0];
        }

        /**Tarifas y difuntos**/
		c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GDIFUNTOS, Const.PATRON_DEFUNCION, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
		arrayElems = c.toArray();
        n = arrayElems.length;
        ArrayList listaDifuntos = new ArrayList();
        DifuntoBean difunto;
        for (int i = 0; i < arrayElems.length; i++) {
        	difunto = (DifuntoBean) arrayElems[i];
        	listaDifuntos.add(difunto);
		} 
        //Lista de tarifas que existen segun cementerio
		c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GDIFUNTOS, Const.PATRON_TARIFASDEF, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
		  	arrayElems = c.toArray();
        n = arrayElems.length;
        ArrayList listaTarifas = new ArrayList();
        TarifaBean tarifa;
        for (int i = 0; i < arrayElems.length; i++) {
        	tarifa = (TarifaBean) arrayElems[i];
        	listaTarifas.add(tarifa);
		} 
        
        try{
        	exhumacionJDialog= new ExhumacionJDialog(desktop, locale, operacion,tipo, unidad, listaDifuntos, listaTarifas);
        	//ANNADIR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			exhumacionJDialog.setTitle(aplicacion.getI18nString("cementerio.ExhumacionDialog.title"));
        		}catch (Exception e){}

        		ExhumacionBean exhumacion= new ExhumacionBean();
        		exhumacion.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());

        		if (municipio != null) {
        			exhumacion.setIdMunicipio(municipio.getId());
        			exhumacion.setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			exhumacion.setNombreCementerio(nombreCementerio);
        		}
        		
        		exhumacionJDialog.load(exhumacion, true);
        	}
        	//MODIFICAR
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof ExhumacionBean)) return;
        		try{
        			exhumacionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag2"));
        		}catch (Exception e){}
        		
        		if (municipio != null) {
        			((ExhumacionBean)elemSeleccionado).setIdMunicipio(municipio.getId());
        			((ExhumacionBean)elemSeleccionado).setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			((ExhumacionBean)elemSeleccionado).setNombreCementerio(nombreCementerio);
        		}
    		
        		((ExhumacionBean)elemSeleccionado).setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		exhumacionJDialog.load((ExhumacionBean)elemSeleccionado, true);
        		
        	}
        	//CONSULTAR	
        	else if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
        		if (elemSeleccionado==null) return;
        		if (!(elemSeleccionado instanceof ExhumacionBean)) return;
        		try{
        			exhumacionJDialog.setTitle(aplicacion.getI18nString("cementerio.dialogo.tag3"));
        		}catch (Exception e){}
    		
        		if (municipio != null) {
        			((ExhumacionBean)elemSeleccionado).setIdMunicipio(municipio.getId());
        			((ExhumacionBean)elemSeleccionado).setEntidad(municipio.getNombre() + " - "+ municipio.getProvincia());
        			((ExhumacionBean)elemSeleccionado).setNombreCementerio(nombreCementerio);
        		}
        		exhumacionJDialog.load((ExhumacionBean)elemSeleccionado, false);
        	}
        
        	exhumacionJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			exhumacionJDialog_actionPerformed(operacion);
        		}
        	});
        	
        	GUIUtil.centreOnWindow(exhumacionJDialog);
        	exhumacionJDialog.show();
        
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    /**
     * Abre el dialogo Para una operacion de inhumacion
     * @param operacion que realiza el usuario
     * @throws Exception
     */
    private void   abrirDialogoDatosPersonales (final String operacion,String tipo) throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
        	datosPersonalesJDialog= new DatosPersonalesJDialog(desktop, locale, operacion,tipo);
        	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
        		elemCementeriosJPanel.clearSelection();        		
        		try{
        			datosPersonalesJDialog.setTitle(aplicacion.getI18nString("cementerio.DatosPersonalesDialog.title"));
        		}catch (Exception e){}

        		PersonaBean persona= new PersonaBean();
        		persona.setTipo(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        		if (municipio != null) {
        			persona.setNombre(municipio.getNombre() + " - "+ municipio.getProvincia());
        		}
        		datosPersonalesJDialog.load(persona, true);
        	}
        	datosPersonalesJDialog.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(ActionEvent e){
        			exhumacionJDialog_actionPerformed(operacion);
        		}
        	});

        	GUIUtil.centreOnWindow(datosPersonalesJDialog);
        	datosPersonalesJDialog.show();
        }finally{
        	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    private void abrirDialogoBusqueda() throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
            /** desmarcamos las features que esten seleccionadas en el mapa */
            if (mapaJPanel!=null) mapaJPanel.clear();
            
            Collection c = cementeriosClient.getCamposElem(Const.ACTION_GET_CAMPOS_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            ArrayList campos = new ArrayList();
            campos.addAll(c);
            
            buscarJDialog=  new BuscarJDialog(desktop, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(),campos, locale);
            buscarJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    buscarJDialog_actionPerformed();
                }
            });

            buscarJDialog.show();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void abrirDialogoFiltro() throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
            /** desmarcamos las features que esten seleccionadas en el mapa */
            if (mapaJPanel!=null) mapaJPanel.clear();

            Collection c = cementeriosClient.getCamposElem(Const.ACTION_GET_CAMPOS_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado());
            ArrayList campos = new ArrayList();
            campos.addAll(c);
            filtrarJDialog= new FiltrarJDialog(desktop, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(),campos,  locale);
            filtrarJDialog.load(filtro!=null?(Collection)filtro.clone():null, aplicarFiltro);
            filtrarJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    filtrarJDialog_actionPerformed();
                }
            });

            filtrarJDialog.show();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void abrirDialogoInformes() throws Exception{
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
            /** desmarcamos las features que esten seleccionadas en el mapa */
            
            informesJDialog= new InformesJDialog(desktop, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), locale);
            informesJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    informesJDialog_actionPerformed();
                }
            });


            GUIUtil.centreOnWindow(informesJDialog);
            informesJDialog.show();
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
        if (elemSeleccionado==null) return;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try{
        	if (elemSeleccionado instanceof ElemCementerioBean) {
        		((ElemCementerioBean)elemSeleccionado).setSuperPatron(tipoElemCementeriosJPanel.getTipoSeleccionado());
        		((ElemCementerioBean)elemSeleccionado).setPatron(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
        	}
            documentJDialog= new GestionDocumentalJDialog(desktop, elemSeleccionado, operacion);
            documentJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    gestionDocumentalJDialog_actionPerformed(operacion);
                }
            });

            GUIUtil.centreOnWindow(documentJDialog);
            documentJDialog.show();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }


    public void unidadEnterramientoJDialog_actionPerformed(String operacion, boolean fromplaza){
        if (fromplaza){
        	try {
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
             Collection c = null;
				c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
	             Object[] arrayElems = c.toArray();
	             int n = arrayElems.length;
	             if (n>0){ 
	            	 UnidadEnterramientoBean unidad = (UnidadEnterramientoBean) arrayElems[0];
	            	 unidadEnterramientoJDialog.loadListaPlazas(unidad.getPlazas());
	            	 unidadEnterramientoJDialog.setUnidadEnterramiento(unidad);
	             }
            }//Si es modificar y viene de plaza hay que actualizar en bbdd 
            else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            	  if (unidadEnterramientoJDialog.getUnidadEnterramiento() != null){
            		  elemSeleccionado= cementeriosClient.update(unidadEnterramientoJDialog.getUnidadEnterramiento(), null);
                	 unidadEnterramientoJDialog.setUnidadEnterramiento((UnidadEnterramientoBean)elemSeleccionado);
            	  }
            }
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error en la actualizacion de la unidad" + e);
           }
        }else{
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof UnidadEnterramientoBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
        else{
            if (unidadEnterramientoJDialog.getUnidadEnterramiento() != null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, unidadEnterramientoJDialog.getUnidadEnterramiento(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                 	unidadEnterramientoJDialog.setUnidadEnterramiento((UnidadEnterramientoBean)elemSeleccionado);
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	 elemSeleccionado= cementeriosClient.update(unidadEnterramientoJDialog.getUnidadEnterramiento(), null);
                	 unidadEnterramientoJDialog.setUnidadEnterramiento((UnidadEnterramientoBean)elemSeleccionado);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            }
        	}/** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        unidadEnterramientoJDialog.setVisible(false);
        }
    }

    public void plazasJTable_dobleClick(String operacion){
    	
    	try {
    		if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
	    		abrirDialogoDefuncionFromTree(Constantes.OPERACION_CONSULTAR, Const.PATRON_DEFUNCION, false);
    		}else{
    			abrirDialogoDefuncionFromPlaza(Constantes.OPERACION_ANNADIR, Const.PATRON_DEFUNCION, true);
    		}
		} catch (Exception e) {
			logger.error("Error al añadir un difunto", e);
			e.printStackTrace();
		}
    }
    
    
    public void titularJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
        	    	if (elemSeleccionado==null) return;
        	    		if (!(elemSeleccionado instanceof ConcesionBean)) return;
        	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
        	    		return;
        	    }
            else{            if (titularJDialog.getTitular()!= null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert (null, titularJDialog.getTitular(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	 elemSeleccionado= cementeriosClient.update(titularJDialog.getTitular(), null);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            	}
            }/** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
//            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        titularJDialog.setVisible(false);
    }
    
    
    
    public void concesionJDialog_actionPerformed(String operacion){
        try{
        if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof ConcesionBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
        else{
            if (concesionJDialog.getConcesion() != null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, concesionJDialog.getConcesion(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	 elemSeleccionado= cementeriosClient.update(concesionJDialog.getConcesion(), null);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            	}
        	}
            /** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        concesionJDialog.setVisible(false);
    }
    
    
    public void tarifaConcesionJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof TarifaBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
        else{
            if (tarifaConcesionJDialog.getTarifa() != null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, tarifaConcesionJDialog.getTarifa(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                 	tarifaConcesionJDialog.setTarifa((TarifaBean) elemSeleccionado);
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	  elemSeleccionado= cementeriosClient.update(tarifaConcesionJDialog.getTarifa(), null);
                	 tarifaConcesionJDialog.setTarifa((TarifaBean) elemSeleccionado);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            }
        	}/** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cemeneterio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        tarifaConcesionJDialog.setVisible(false);
    }

    public void tarifaDifuntosJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof TarifaBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
        else{
            if (tarifaDifuntosJDialog.getTarifa() != null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, tarifaDifuntosJDialog.getTarifa(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                 	tarifaDifuntosJDialog.setTarifa((TarifaBean) elemSeleccionado);
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	  elemSeleccionado= cementeriosClient.update(tarifaDifuntosJDialog.getTarifa(), null);
                	  tarifaDifuntosJDialog.setTarifa((TarifaBean) elemSeleccionado);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            }
        	}/** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cemeneterio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        tarifaDifuntosJDialog.setVisible(false);
    }
    
    public void bloqueJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof BloqueBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
        else{
            if (bloqueJDialog.getBloque() != null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, bloqueJDialog.getBloque(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                 	bloqueJDialog.setBloque((BloqueBean) elemSeleccionado);
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	 elemSeleccionado= cementeriosClient.update(bloqueJDialog.getBloque(), null);
                	 bloqueJDialog.setBloque((BloqueBean) elemSeleccionado);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            }
        	}/** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        bloqueJDialog.setVisible(false);
    }
    
    public void intervencionJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof IntervencionBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
        else{
            if (intervencionJDialog.getIntervencion() != null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, intervencionJDialog.getIntervencion(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                 	intervencionJDialog.setIntervencion((IntervencionBean) elemSeleccionado);
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	 elemSeleccionado= cementeriosClient.update(intervencionJDialog.getIntervencion(), null);
                	 intervencionJDialog.setIntervencion((IntervencionBean) elemSeleccionado);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            }
        	}/** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        intervencionJDialog.setVisible(false);
    }
    
    
    public void defuncionJDialog_actionPerformedFromPlaza(String operacion, boolean fromplaza){
        try{
        	PlazaBean plaza = null;
            if (defuncionJDialog.getDifunto() != null){
            
            	DifuntoBean dif = defuncionJDialog.getDifunto();
            	if (fromplaza){
	            	plaza = unidadEnterramientoJDialog.getPlazaSelected();
	            	plaza.setDifunto(dif);
	            	unidadEnterramientoJDialog.setPlazaSelected(plaza);
            	}
            	/**Compruebo si la plaza existe en bbdd si no la inserto tb junto al difunto**/ 
            	PlazaBean  plazabbdd = (PlazaBean) cementeriosClient.getPlaza(Const.ACTION_GET_PLAZA, plaza.getIdPlaza());
            	PlazaBean newPlaza = null;
            	if (plazabbdd == null){
            		newPlaza = (PlazaBean) cementeriosClient.insert(listafeatures, plaza, null, idCementerio);
            		dif.setId_plaza(newPlaza.getIdPlaza());
            		plaza.setIdPlaza(newPlaza.getIdPlaza());
            	}
            	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
	            	dif.setId_plaza(plaza.getIdPlaza());
	            	dif.setIdCementerio(idCementerio);
	                elemSeleccionado= cementeriosClient.insert(listafeatures, dif, null, idCementerio);
	                if (elemSeleccionado == null) return;
	
	                /**Actualizo la lista de plazas en el dialog**/
	                    newPlaza = (PlazaBean) cementeriosClient.getPlaza(Const.ACTION_GET_PLAZA, plaza.getIdPlaza());
	                    	
		            	ArrayList<PlazaBean> listaPlazas = defuncionJDialog.getListaPlazasTabla();
		            	for (int i = 0; i < listaPlazas.size(); i++) {
							PlazaBean plazaElem = listaPlazas.get(i);
							if (plazaElem.getDifunto().getId_plaza() == newPlaza.getIdPlaza()){
								listaPlazas.remove(i);
								listaPlazas.add(i, newPlaza);
								defuncionJDialog.setListaPlazasTabla(listaPlazas);
								//TODO prueba
								unidadEnterramientoJDialog.setListaPlazasTabla(listaPlazas);
							}
						}
	                updateFeatures();
               }
	            if (fromplaza){
	            	unidadEnterramientoJDialog_actionPerformed(Constantes.OPERACION_CONSULTAR, fromplaza);
	            	System.out.println("Recargo....");
            }
            if (mapaJPanel != null){
            	cargarMapa();        
            } 
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        defuncionJDialog.setVisible(false);
    }
    
    
    public void defuncionJDialog_actionPerformedFromTree(String operacion, boolean fromplaza){
        try{
        	
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof ConcesionBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
            else if ((!operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) || (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR))) {
        	
        	PlazaBean plaza = null;
            if (defuncionJDialog.getDifunto() != null){
            
            	DifuntoBean dif = defuncionJDialog.getDifunto();
	            plaza = defuncionJDialog.getPlazaSelected();
	            if ((plaza != null) && (plaza.getDifunto() == null)){
	            plaza.setDifunto(dif);
	            defuncionJDialog.setPlazaSelected(plaza);
            	/**Compruebo si la plaza existe en bbdd si no la inserto tb junto al difunto**/ 
            	PlazaBean  plazabbdd = (PlazaBean) cementeriosClient.getPlaza(Const.ACTION_GET_PLAZA, plaza.getIdPlaza());
            	PlazaBean newPlaza = null;
            	if (plazabbdd == null){
            		newPlaza = (PlazaBean) cementeriosClient.insert(listafeatures, plaza, null, idCementerio);
            		dif.setId_plaza(newPlaza.getIdPlaza());
            		plaza.setIdPlaza(newPlaza.getIdPlaza());
            	}

            	if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
	            	dif.setId_plaza(plaza.getIdPlaza());
	            	dif.setIdCementerio(idCementerio);
	                elemSeleccionado= cementeriosClient.insert(listafeatures, dif, null, idCementerio);
	                if (elemSeleccionado == null) return;
	
	                /**Actualizo la lista de plazas en el dialog**/
	                    newPlaza = (PlazaBean) cementeriosClient.getPlaza(Const.ACTION_GET_PLAZA, plaza.getIdPlaza());
	                    	
		            	ArrayList<PlazaBean> listaPlazas = defuncionJDialog.getListaPlazasTabla();
		            	for (int i = 0; i < listaPlazas.size(); i++) {
							PlazaBean plazaElem = listaPlazas.get(i);
							if ((plazaElem.getDifunto()!= null)&& (plazaElem.getDifunto().getId_plaza() == newPlaza.getIdPlaza())){
								listaPlazas.remove(i);
								listaPlazas.add(i, newPlaza);
								defuncionJDialog.setListaPlazasTabla(listaPlazas);
							}
						}
	                updateFeatures();
               }

            if (mapaJPanel != null){
            	cargarMapa();        
            } 
            }
//	        else if ((plaza!= null) &&  (plaza.getDifunto()!= null)){
//            	String message = "Plaza ya asignada.Debe seleccionar una plaza libre";
//				JOptionPane.showMessageDialog(desktop, message);
//            	
//            }else{
//				String message = "Debe seleccionar una plaza libre";
//				JOptionPane.showMessageDialog(desktop, message);
//            }
        	}
        }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }
            catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        defuncionJDialog.setVisible(false);
    }
    
    public void historicoDifuntosJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof InhumacionBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        historicoDifuntoJDialog.setVisible(false);
    }
    
    public void historicoJTable_dobleClick(String tipo, HistoricoDifuntoBean elem){
    	
    	try {
    		if (tipo.equalsIgnoreCase(Const.PATRON_INHUMACION)){
    			if (elemSeleccionado instanceof InhumacionBean){
    				((InhumacionBean) elemSeleccionado).setDifunto(elem.getDifunto());
    				abrirDialogoInhumacion(Constantes.OPERACION_CONSULTAR, Const.PATRON_INHUMACION);
    			}
    		}else {
    			if (elemSeleccionado instanceof ExhumacionBean){
    				((ExhumacionBean) elemSeleccionado).setDifunto(elem.getDifunto());
    				abrirDialogoExhumacion(Constantes.OPERACION_CONSULTAR, Const.PATRON_EXHUMACION);
    			}
    		}
		} catch (Exception e) {
			logger.error("Error al añadir un difunto", e);
			e.printStackTrace();
		}
    }
    
    public void historicoPropiedadJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof PersonaBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        historicoPropiedadJDialog.setVisible(false);
    }
    
    public void historicoPropiedadJTable_dobleClick(String tipo, HistoricoPropiedadBean elem){
    	
    	try {
    		if (elemSeleccionado instanceof ConcesionBean){
    				abrirDialogoConcesion(Constantes.OPERACION_CONSULTAR, Const.PATRON_CONCESION);
    			}
		} catch (Exception e) {
			logger.error("Error al añadir un difunto", e);
			e.printStackTrace();
		}
    }
    
    public void inhumacionJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof InhumacionBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
        else{
            if (inhumacionJDialog.getInhumacion() != null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, inhumacionJDialog.getInhumacion(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                 	inhumacionJDialog.setInhumacion((InhumacionBean) elemSeleccionado);
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	 elemSeleccionado= cementeriosClient.update(inhumacionJDialog.getInhumacion(), null);
                	 inhumacionJDialog.setInhumacion((InhumacionBean) elemSeleccionado);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            }
        	}/** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        inhumacionJDialog.setVisible(false);
    }
    
    
    /**
     * 
     * @param operacion
     */
    public void exhumacionJDialog_actionPerformed(String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
    	    	if (elemSeleccionado==null) return;
    	    		if (!(elemSeleccionado instanceof ExhumacionBean)) return;
    	    			elemSeleccionado= cementeriosClient.update(elemSeleccionado, documentJDialog.getDocumentosJPanel().getFilesInUp());
    	    		return;
    	    }
        else{
            if (exhumacionJDialog.getExhumacion() != null){
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, exhumacionJDialog.getExhumacion(), null, idCementerio);
                	if (elemSeleccionado == null) return;
                 	updateFeatures();
                 	exhumacionJDialog.setExhumacion((ExhumacionBean) elemSeleccionado);
                }
                else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                	 elemSeleccionado= cementeriosClient.update(exhumacionJDialog.getExhumacion(), null);
                	 exhumacionJDialog.setExhumacion((ExhumacionBean) elemSeleccionado);
                }
                if (mapaJPanel != null){
                	cargarMapa();}
            }
        	}/** else el usuario ha Cancelado la operacion */
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        exhumacionJDialog.setVisible(false);
    }   
    
    public void DatosPersonalesJDialog_actionPerformed(String operacion){
        try{

            if (datosPersonalesJDialog.getPersona() != null){
            	
                if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
                	elemSeleccionado= cementeriosClient.insert(listafeatures, datosPersonalesJDialog.getPersona(), null, idCementerio);
                }
                if (elemSeleccionado == null) return;
                updateFeatures();
                }
                if (mapaJPanel != null){
                	cargarMapa();        
                	}
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
            }
            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        datosPersonalesJDialog.setVisible(false);
    }
    
    
    public void buscarJDialog_actionPerformed(){
        try{
            if (buscarJDialog.getValor() != null){
                cadenaBusqueda= buscarJDialog.getValor();
                filtro = (ArrayList) buscarJDialog.getFiltro();
                
                cargarResultadoBusqueda();
                cadenaBusqueda = null;
                filtro = null; //new ArrayList();
            }/** El usuario ha Cancelado la operacion */
            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error en la busqueda ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
        }

        elemSeleccionado= null;
        buscarJDialog.setVisible(false);
    }

    public void filtrarJDialog_actionPerformed(){
        try{
            if (filtrarJDialog.aceptar()){
                filtro= filtrarJDialog.getFiltro();
                aplicarFiltro= filtrarJDialog.getAplicarFiltro();
                if (filtro!= null){
                	tipoFiltro = tipoElemCementeriosJPanel.getTipoSeleccionado();
                	subtipoFiltro = tipoElemCementeriosJPanel.getSubtipoSeleccionado();
                }
                if (botoneraAppJPanel != null) botoneraAppJPanel.renombrarFiltrarJButton((filtro!=null && filtro.size()>0)?aplicarFiltro:false);
            }/** El usuario ha Cancelado la operacion */

            /** Actualizamos bloqueos */
            cargarResultadoFiltro();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error en el filtro ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
        }

        elemSeleccionado= null;
        filtrarJDialog.setVisible(false);
    }

    public void informesJDialog_actionPerformed(){
        informesJDialog.setVisible(false);
    }



    public void gestionDocumentalJDialog_actionPerformed(final String operacion){
        try{
            if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
//                desbloquearCementerio((CementerioBean)elemSeleccionado);
            	guardarDocumentacion(Constantes.OPERACION_ANEXAR);
            }

            /** RECARGAMOS TABLA- soluciona el problema de la ListaPane y refrescamos los bloqueos */
            recargarTablaElemCementerio();

            elemCementeriosJPanel.clearSelection();
            botoneraJPanel.setEnabled(false);
            if (listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
            if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();

        }catch(Exception e){
            logger.error("Error al operar con un inmueble de una feature ", e);
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"), aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
         }

        elemSeleccionado= null;
        documentJDialog.setVisible(false);
      }


    private void guardarDocumentacion(String operacion) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    	try{
	    	Class clase = CementeriosJPanel.class;
	    	
			Class[] tiposParams = new Class[1];
			Object[] params = null;
			String method= "";
	
			tiposParams[0] = String.class;
			params = new Object[1];
			params[0] = (operacion);
	
			if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
				if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DEFUNCION)){
					tiposParams = new Class[2];
					tiposParams[0] = String.class;
					tiposParams[1] = Boolean.class;
					params = new Object[2];
					params[0] = (operacion);
					params[1] = false;
					
					method = method.concat("defuncionJDialog_actionPerformedFromTree");
				}
			}else{

				method = selectMethod();
			}
			Method metodoSave = clase.getMethod(method, tiposParams);
	
			metodoSave.invoke(this, params);
		
    	}catch (Exception e) {
    		logger.error("error guardando la documentacion" + e);
		}
		
	}
    
   
    
    private String selectMethod(){
    	
    	String method = "";
    	String finmethod= "actionPerformed";
    	
    	 if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
    		 if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
    			 method = method.concat("unidadEnterramientoJDialog_");
    		 }else{
    			 method = method.concat("bloqueJDialog_");
    		 }
    	 }else if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
    		 if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CONCESION)){
    			 method = method.concat("concesionJDialog_");
    		 }else if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR)){
    			 method = method.concat("titularJDialog_");
    		 }else{
    			 method = method.concat("tarifaConcesionJDialog_");
    		 }
    	 }else if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
    		 if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
    			 method = method.concat("historicoDifuntosJDialog_");
    		 }else{
    			 method = method.concat("historicoPropiedadJDialog_");
    		 }
    	 }else if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
    		 if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INHUMACION)){
    			 method = method.concat("inhumacionJDialog_");
    		 }else if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_EXHUMACION)){
    			 method = method.concat("exhumacionJDialog_");
    		 }else if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASDEF)){
    			 method = method.concat("tarifaDifuntosJDialog_");
    		 }
    	 }else if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
    		 if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INTERVENCION)){
    			 method = method.concat("intervencionJDialog_");
    		 }
    	 }
    	 
    	 return method.concat(finmethod);
    }
    
    
    public TipoElemCementeriosJPanel getTipoElemCementeriosJPanel() {
		return tipoElemCementeriosJPanel;
	}

	public void setTipoElemCementeriosJPanel(
			TipoElemCementeriosJPanel tipoElemCementeriosJPanel) {
		this.tipoElemCementeriosJPanel = tipoElemCementeriosJPanel;
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
        listafeatures= c.toArray();
    }

    public GeopistaFeature getSelectedFeature(){
        return gFeature;
    }

    public void renombrarComponentes(){
        tipoElemCementeriosJPanel.renombrarComponentes();
        elemCementeriosJPanel.renombrarComponentes();
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
                ps.setString(1,aplicacion.getString("geopista.DefaultCityId"));
                ResultSet  rs =ps.executeQuery();
                Municipio municipio=null;
                if  (rs.next()) {
                    municipio= new Municipio(aplicacion.getString("geopista.DefaultCityId"), rs.getString("idprovincia"),rs.getString("nombre"),rs.getString("provincia"));
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
            String sConn = aplicacion.getString("geopista.conexion.url");
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
    private void bloquearCementerio(final ElemCementerioBean elemCementerio){
        if (elemSeleccionado==null) return;
        /* ponemos una pantalla con el reloj */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("cementerio.mensajes.tag3"));
        progressDialog.report(aplicacion.getI18nString("cementerio.mensajes.tag4"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        try{
//                            cementeriosClient.bloquearCementerio(elemCementerio, true);
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
//     */
//    private Object desbloquearCementerio(CementerioBean elemCementerio){
//        try{
//            if (elemCementerio==null) return null;
////            return cementeriosClient.bloquearCementerio(elemCementerio, false);
//        }
//        catch(Exception e){return null;}
//    }

    private void abrirDialogo(int dialogo) throws Exception{
        abrirDialogo(dialogo, null);
    }
    /**
     * Abre el tipo de dialogo pasado como parametro
     * @param dialogo a abrir
     * @throws Exception
     */
    private void abrirDialogo(int dialogo, String tipo) throws Exception{
    	
        idCementerio = (Integer) aplicacion.getBlackboard().get(Constantes.ID_CEMENTERIO);
        
        if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ANNADIR)){
            switch (dialogo){
                case UnidadEnterramiento:
                	abrirDialogoUnidadEnterramiento(Constantes.OPERACION_ANNADIR,tipo, false);
                    break;
                case Bloque:
                	abrirDialogoBloque(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case Difunto:
                	abrirDialogoDefuncionFromTree(Constantes.OPERACION_ANNADIR,tipo, false);
                    break;
                case Inhumacion:
                	abrirDialogoInhumacion(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case Exhumacion:
                	abrirDialogoExhumacion(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case Titular:
                	abrirDialogoTitular(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case Concesion:
                	abrirDialogoConcesion(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case Intervencion:
                	abrirDialogoIntervencion(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case TarifaGPropiedad:
                	abrirDialogoTarifasGPropiedad(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case TarifaGDifuntos:
                	abrirDialogoTarifasGDifuntos(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case HistoricoDifunto:
                	abrirDialogoHistoricoDifunto(Constantes.OPERACION_ANNADIR,tipo);
                    break;
                case HistoricoPropiedad:
                	abrirDialogoHistoricoPropiedad(Constantes.OPERACION_ANNADIR,tipo);
                    break;     
                    
            }
        }else if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) ||
                  botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_ANEXAR)){
            if (elemSeleccionado == null) return;
//            String bloqueado= cementeriosClient.bloqueado((CementerioBean)elemSeleccionado);
//            if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN,"",false))){
//                /** Mostramos mensaje de bloqueo del bien */
//                JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.mensajes.tag1")+" "+bloqueado+"\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
//                /** lo abrimos en modo consulta */
                if (botoneraJPanel.getBotonPressed().equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)){
                    switch (dialogo){
                        case UnidadEnterramiento:
                        	abrirDialogoUnidadEnterramiento(Constantes.OPERACION_MODIFICAR,tipo, false);
                            break;
                        case Bloque:
                        	abrirDialogoBloque(Constantes.OPERACION_MODIFICAR,tipo);
                            break;
                        case Difunto:
                        	abrirDialogoDefuncionFromTree(Constantes.OPERACION_MODIFICAR,tipo, false);
                            break;
                        case Inhumacion:
                        	abrirDialogoInhumacion(Constantes.OPERACION_MODIFICAR,tipo);
                            break;
                        case Exhumacion:
                        	abrirDialogoExhumacion(Constantes.OPERACION_MODIFICAR,tipo);
                            break;
                        case Titular:
                        	abrirDialogoTitular(Constantes.OPERACION_MODIFICAR,tipo);
                            break;
                        case Concesion:
                        	abrirDialogoConcesion(Constantes.OPERACION_MODIFICAR,tipo);
                            break;
                        case Intervencion:
                        	abrirDialogoIntervencion(Constantes.OPERACION_MODIFICAR,tipo);
                            break;
                        case TarifaGPropiedad:
                        	abrirDialogoTarifasGPropiedad(Constantes.OPERACION_MODIFICAR,tipo);
                            break;
                        case TarifaGDifuntos:
                        	abrirDialogoTarifasGDifuntos(Constantes.OPERACION_MODIFICAR,tipo);
                            break;      
                    }
                    
                } else abrirGestionDocumentalJDialog(Constantes.OPERACION_ANEXAR);
                return;

            }
   }
    

    private void cargarResultadoBusqueda() throws Exception{
        listafeatures= null;
        recargarTablaElemCementerio();
    }

    private void cargarResultadoFiltro() throws Exception{
        listafeatures= null;
        recargarTablaElemCementerio();
    }


    /**
     * Recarga la tabla de bienes de inventario cuando el usuario selecciona un tipo de bien del arbol,
     * o finaliza algun tipo de operacion sobre algun bien de la tabla.
     * @throws Exception
     */
    private void recargarTablaElemCementerio() throws Exception{
        
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
                    @SuppressWarnings("unused")
					public void run()  
                    {
                        try{
                            progressDialog.report(aplicacion.getI18nString("cementerio.app.tag3"));

                            Collection c= null;
                            Collection subc = null;
                            boolean b= true;
                            
                            idCementerio = (Integer) aplicacion.getBlackboard().get(Constantes.ID_CEMENTERIO);
                            nombreCementerio = ((CementerioBean) cementeriosClient.getCementerio(Const.ACTION_GET_CEMENTERIO, idCementerio)).getNombre();
                            
                            elemCementeriosJPanel.clearTable();
                        
                        if (filtro != null) {
                        	if ((cadenaBusqueda!= null) && (cadenaBusqueda.length() > 0)){
                        		c= cementeriosClient.getAllElems(Const.ACTION_FIND_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                        	}else{
                        		if (tipoFiltro.equalsIgnoreCase(tipoElemCementeriosJPanel.getTipoSeleccionado()) && 
                        				subtipoFiltro.equalsIgnoreCase(tipoElemCementeriosJPanel.getSubtipoSeleccionado())){
                        			c= cementeriosClient.getAllElems(Const.ACTION_FILTER_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                        		}else{
                        			c= cementeriosClient.getAllElems(Const.ACTION_GET_ALL, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                        		}
                        	}
                        }
                        else if  ((filtro == null) && ((listafeatures == null) || (listafeatures.length == 0))){
                        	c= cementeriosClient.getAllElems(Const.ACTION_GET_ALL, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                        }else{

                        if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GELEMENTOS)){
                            if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_BLOQUE)){
                            	c= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            }
                            else if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_UENTERRAMIENTO)){
                            	c= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            }
                            
                        }else if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)){
                            if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CONCESION)){
                                subc= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, Const.SUPERPATRON_GELEMENTOS, Const.PATRON_UENTERRAMIENTO, cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                                c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            }else if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR)){
                            	c = cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            }
                            
                        }else if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GINTERVENCIONES)){
                            if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INTERVENCION)){
                                c= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            }    
                        }else if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)){
                            if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DEFUNCION)){
                                c= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            } else if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_INHUMACION)){
                                c= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            } else if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_EXHUMACION)){
                                c= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            }        
                        }else if (tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)){
                            if (tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO)){
                                c= cementeriosClient.getElementosCementerio(Const.ACTION_GET_ELEM, tipoElemCementeriosJPanel.getTipoSeleccionado(), tipoElemCementeriosJPanel.getSubtipoSeleccionado(), cadenaBusqueda, aplicarFiltro?filtro:null, listafeatures, idCementerio);
                            }        
                        }
                                    
                        else{
                        /** No es ningun tipo reconocido. */
                           b= false;
                        	}
                        }
                        	elemCementeriosJPanel.setTipoSeleccionado(tipoElemCementeriosJPanel.getTipoSeleccionado());
                        	elemCementeriosJPanel.setSubtipoSeleccionado(tipoElemCementeriosJPanel.getSubtipoSeleccionado());
                            /** Cargamos la coleccion */
                            elemCementeriosJPanel.loadListaElemCementerios(c);
                        	botoneraJPanel.setEnabled(false);
                        	if (botoneraAppJPanel != null){ botoneraAppJPanel.setEnabled(false);  botoneraAppJPanel.setEnabledBuscarFiltrarInf();}
                            if ((c == null) || (c.size() == 0)){
                            	botoneraJPanel.setEnabled(false);
                            	if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
                            }
                            if  ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GHISTORICOS)) &&
                               		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_HISTORICO_DIFUNTO))){
                            	botoneraJPanel.setEnabled(false);
                            	if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();
                            }
                            else if  ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)) &&
                               		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_DEFUNCION))){
                            	botoneraJPanel.setEnabled(false);
                            	if (b && listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
                            	botoneraJPanel.eliminarJButtonSetEnabled(false);
                            	if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();
                            }

                            /**Si se selecciona otro superpatron y patron diferente a unidad de enterramiento es necesario que haya una dada de alta 
                            en la feature seleccionada **/
                            else if  (((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
                               		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_CONCESION))) &&
                               		(subc != null) && (subc.size() == 0)){
                        			if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabled(false);
                        			JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.mensajes.concesion"));	
                            	}
                            else if ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
                               		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASPROP))  || 
                               		(tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GDIFUNTOS)) &&
                               		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TARIFASDEF)))  {
                            		
                            		botoneraJPanel.annadirJButtonSetEnabled(true);
                            		if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();
                            }
                           else if ((tipoElemCementeriosJPanel.getTipoSeleccionado().equalsIgnoreCase(Const.SUPERPATRON_GPROPIEDAD)) &&
                              		(tipoElemCementeriosJPanel.getSubtipoSeleccionado().equalsIgnoreCase(Const.PATRON_TITULAR))){
                           			
                        	   		if (b && listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(false);
                        	   		if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();
                           }
                            else{
	                            	if (b && listafeatures!=null && listafeatures.length>0) botoneraJPanel.annadirJButtonSetEnabled(true);
	                            	if (botoneraAppJPanel != null) botoneraAppJPanel.setEnabledBuscarFiltrarInf();
                                 }
                        }
                        catch(Exception e){
                        	ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("cementerio.SQLError.Titulo"),
                        			aplicacion.getI18nString("cementerio.SQLError.Aviso"), StringUtil.stackTrace(e));
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

    /**
     * Este metodo solo se puede utilizar para la aplicacion de Inventario para el plugin
     * interno que se utiliza dentro del editor no funciona
     * @return
     */
    private CementeriosInternalFrame getCementeriosFrame(){
    	return ((CementeriosInternalFrame)((MainCementerios)this.desktop).getIFrame());
    }

    
    /**
     * Vuelve a cargar el mapa
     */
    private void cargarMapa(){
    	progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
    	progressDialog.setTitle(I18N.get("GeopistaLoadMapPlugIn.CargandoMapa"));
    	progressDialog.report(I18N.get("GeopistaLoadMapPlugIn.CargandoMapa"));
    	progressDialog.addComponentListener(new ComponentAdapter() {

    		public void componentShown(ComponentEvent e) {
    			new Thread(new Runnable() {

	                public void run() {
	                	try {
	                		
	                        idCementerio = (Integer) aplicacion.getBlackboard().get(Constantes.ID_CEMENTERIO);
	                		
	                    	Date date = new Date();
//	                    	String hora = (String)getInventarioClient().getHora(Const.ACTION_GET_HORA);
//	                    	getCementeriosFrame().setFecha((String) new SimpleDateFormat("dd-MM-yyyy").format(date));
	                    	Const.fechaVersion = (String) new SimpleDateFormat("yyyy-MM-dd").format(date);
	                    	Iterator itLayers = getCementeriosFrame().getJPanelMap().getGeopistaEditor().getLayerManager().getLayers().iterator();
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
		                        version.setFecha(Const.fechaVersion);
		                        version.setFeaturesActivas(true);
		                        AppContext.getApplicationContext().getBlackboard().put(AppContext.VERSION,version);
		                    	getCementeriosFrame().getJPanelMap().getGeopistaEditor().loadMap("geopista:///"+Constantes.idMapaCementerios);
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
    
}
