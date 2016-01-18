/**
 * GeorreferenciacionExternaPanel05.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georreferenciacionExterna.paneles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.deegree.graphics.sld.LabelPlacement;
import org.deegree.graphics.sld.PointPlacement;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.TextSymbolizer;
import org.deegree_impl.graphics.sld.FeatureTypeStyle_Impl;
import org.deegree_impl.graphics.sld.LineSymbolizer_Impl;
import org.deegree_impl.graphics.sld.Rule_Impl;
import org.deegree_impl.graphics.sld.StyleFactory;
import org.deegree_impl.graphics.sld.StyleFactory2;
import org.deegree_impl.graphics.sld.UserStyle_Impl;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.Column;
import com.geopista.feature.DateDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.NumberDomain;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.model.GeopistaLayer;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.ui.plugin.georreferenciacionExterna.ConsultaDatosBean;
import com.geopista.ui.plugin.georreferenciacionExterna.FiltroData;
import com.geopista.ui.plugin.georreferenciacionExterna.GeorreferenciacionExternaData;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.localgis.georreferenciaexterna.webservicesclient.GeorreferenciaExternaWSWrapper;
import com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.RowBean;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/** 
 *	Panel que genera las features a través de los datos almacenados anteriormente. 
 *	Presenta los errores encontrados por el validador de features o por la captura de errores.
 *
 */
public class GeorreferenciacionExternaPanel05 extends javax.swing.JPanel implements WizardPanel {

	private static final long serialVersionUID = 1L;
    private String localId = null;
    private String nextID = null;
    private PlugInContext   context;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();    
    private WizardContext wizardContext;  	
    private JTextArea jTextArea = null;
	private JLabel jLabel = null;    
    private AbstractValidator validator = null;
    private GeopistaLayer capa = null;  
    private JPanel jPanelDatos = null;
	private JPanel jPanelInfo = null;
	private JScrollPane jScrollPanel = null;
	
	private HashMap dataType;
	private HashMap dataTypeCamposBBDD;
	
	private ArrayList fieldNames=new ArrayList();
	private ArrayList fieldTypes=new ArrayList();	
	
	private ArrayList campoElegidosCoordeXY = new ArrayList();
	private ConsultaDatosBean consultaSelected = null;
	private GeorreferenciacionExternaData datos=null; 	
	
	ArrayList lstFiltros = new ArrayList();
	boolean accionEjecutar = false;
	private int numPasadasTotal=0, numPaquete=0,inicio=0, fin=0;	
	
	final TaskMonitorDialog progressDialog=new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(),null);
		    
	public GeorreferenciacionExternaPanel05(String id, String nextId, PlugInContext context2, 
			ConsultaDatosBean consultaSelected, boolean accionEjecutar ) {
		
		Locale loc=I18N.getLocaleAsObject();      
    	ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.georreferenciacionExterna.languages.GeorreferenciacionExternai18n",loc,this.getClass().getClassLoader());
    	I18N.plugInsResourceBundle.put("GeorreferenciacionExterna",bundle);
    	    
        this.context=context2;
        this.nextID = nextId;
        this.localId = id;
        this.consultaSelected  = consultaSelected;
        this.accionEjecutar = accionEjecutar;
        try
        {
            setName(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel05.titlePanel"));
            initialize();
        }catch(Exception e)
        {
            e.printStackTrace();
        }   
    }    
    private void initialize() {
    	
    	this.setLayout(new GridBagLayout());
    	this.setSize(new Dimension(600, 550));
    	this.setPreferredSize(new Dimension(600, 550));
    	
    	this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getJPanelDatos(), 
				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));		    	      
    }
    
    private JPanel getJPanelDatos(){
    	if (jPanelDatos == null){
    		
    		jPanelDatos  = new JPanel();
    		jPanelDatos.setLayout(new GridBagLayout());
    		
    		jPanelDatos.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.panel05.finalizacion"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));
    		
    		jPanelDatos.add(getJLabel(), 
    				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0)); 
    		
    		jPanelDatos.add(getJScrollPanel(), 
    				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    		
    	}
    	return jPanelDatos;
    }
    
    private JPanel getJPanelInfo(){

		if (jPanelInfo == null){

			jPanelInfo     = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("GeorreferenciacionExterna","georreferenciacionExterna.Info6"));
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
		}
		return jPanelInfo;
	}
    
	public void add(InputChangedListener listener) {
		// TODO Auto-generated method stub		
	}

	public void enteredFromLeft(Map dataMap) {

		Hashtable camposElegidosMostrar = new Hashtable();
		datos=new GeorreferenciacionExternaData();
		if(accionEjecutar){
			//la opcion seleccionada en el panel de la tabla de consultas ha sido ejecutar
			datos.setTabla(consultaSelected.getNombre_tabla_ext());
			datos.setBbdd(consultaSelected.getNombre_bbdd_ext());
			datos.setTipoDatos(consultaSelected.getMetodo_georeferencia());
			datos.setNombreConsultaCapa(consultaSelected.getNombreConsulta());	
			datos.setTipoGeometria(consultaSelected.getTipo_geometria());
			datos.setTablaGeometria(consultaSelected.getTabla_cruce());
			datos.setDescripcion(consultaSelected.getDescripcion());
			//datos.setEtiquetaSLD(consultaSelected.getCampo_etiqueta());
			datos.setPortal(consultaSelected.getPortal());
			
			// los filtros
	    	String [] nombre_campos_operadores = null;
	    	String [] nombre_campos_valores = null;
	    	String [] campos_operadores = null;
	    	String [] campos_valores = null;
	    	
	    	HashMap hashOperadores = new HashMap();
	    	HashMap hashValores = new HashMap();
	    	FiltroData filtroData = new FiltroData();
	    	
	    	if(!this.consultaSelected.getFiltro_operador().equals("") &&
					!this.consultaSelected.getFiltro_valor().equals("")){
				
				campos_operadores = this.consultaSelected.getFiltro_operador().split(":");
				for(int j=0; j<campos_operadores.length;j++){

					nombre_campos_operadores = campos_operadores[j].split("#");
					hashOperadores.put(nombre_campos_operadores[0], nombre_campos_operadores[1]);
				}
				
				campos_valores = this.consultaSelected.getFiltro_valor().split(":");
				for(int j=0; j<campos_valores.length;j++){
					nombre_campos_valores = campos_valores[j].split("#");
					hashValores.put(nombre_campos_valores[0], nombre_campos_valores[1]);
				}
				
			}
			
			
			
			
			
			
			Hashtable tipoDatos = new Hashtable();
			if(!consultaSelected.getCampos_mostrar().equals("")){
				String [] camposMostrar = consultaSelected.getCampos_mostrar().split(":");
				
				for(int i=0; i<camposMostrar.length; i++){
					
					String [] nomCampo_typeCampo = camposMostrar[i].split("=");
					if(consultaSelected.getMetodo_georeferencia().equals("Coordenadas X,Y")){
						
						String [] campoGeorreferencia = consultaSelected.getCampo_georeferencia().split(":");
						for(int h=0; h<campoGeorreferencia.length; h++){
							if(campoGeorreferencia[h].equals(nomCampo_typeCampo[0])){
								tipoDatos.put(campoGeorreferencia[h], nomCampo_typeCampo[1]);
								
								Hashtable tipoDatosXY = new Hashtable();
								tipoDatosXY.put(campoGeorreferencia[h], nomCampo_typeCampo[1]);
								campoElegidosCoordeXY.add(tipoDatosXY);
							}
							else{
								if(!nomCampo_typeCampo[0].equals("GEOMETRY")){
									camposElegidosMostrar.put(campoGeorreferencia[h], nomCampo_typeCampo[1]);
								
									filtroData = new FiltroData();
									if(hashOperadores.containsKey(nomCampo_typeCampo[0]) &&
											hashValores.containsKey(nomCampo_typeCampo[0])){
										filtroData.setCampo(nomCampo_typeCampo[0]);
										filtroData.setOperador((String)hashOperadores.get(nomCampo_typeCampo[0]));
										filtroData.setValor((String)hashValores.get(nomCampo_typeCampo[0]));
										filtroData.setTipoCampo(nomCampo_typeCampo[1]);
									}
									else{
										filtroData = null;
									}
									lstFiltros.add(filtroData);
								}
							}
						}
						
					
					}
					else{
						String campoGeorreferencia = consultaSelected.getCampo_georeferencia();
						if(campoGeorreferencia.equals(nomCampo_typeCampo[0])){
							tipoDatos.put(campoGeorreferencia, nomCampo_typeCampo[1]);
							campoElegidosCoordeXY = null;
						}
						else{
							if(!nomCampo_typeCampo[0].equals("GEOMETRY")){
								camposElegidosMostrar.put(nomCampo_typeCampo[0], nomCampo_typeCampo[1]);
							
								filtroData = new FiltroData();
								if(hashOperadores.containsKey(nomCampo_typeCampo[0]) &&
										hashValores.containsKey(nomCampo_typeCampo[0])){
									filtroData.setCampo(nomCampo_typeCampo[0]);
									filtroData.setOperador((String)hashOperadores.get(nomCampo_typeCampo[0]));
									filtroData.setValor((String)hashValores.get(nomCampo_typeCampo[0]));
									filtroData.setTipoCampo(nomCampo_typeCampo[1]);
								}
								else{
									filtroData = null;
								}
								lstFiltros.add(filtroData);
							}
						}

					}

				}
				datos.setTipoElegido(tipoDatos);

			}
		}
		
		else{
			// ya que la opcion incial fue modificar o crear una nueva consulta
			//Cargamos los datos de paneles anteriores.		
			datos.setTabla((String)wizardContext.getData("TB"));
	    	datos.setBbdd((String)wizardContext.getData("BD"));
	    	datos.setTipoDatos((String)wizardContext.getData("tipo"));
	    	datos.setTipoElegido((Hashtable)wizardContext.getData("cmp"));  
	    	datos.setNombreConsultaCapa((String)wizardContext.getData("NCC"));  
	    	datos.setTipoGeometria((String)wizardContext.getData("tipogeom"));  
	    	datos.setTablaGeometria((String)wizardContext.getData("tablatipogeom")); 
	    	datos.setDescripcion((String)wizardContext.getData("DES"));
	    	//datos.setEtiquetaSLD((String)wizardContext.getData("ETQSLD"));
	    	
	    	campoElegidosCoordeXY = (ArrayList)wizardContext.getData("cmpElegidosXY");
	    	//camposElegidosMostrar = (Hashtable)wizardContext.getData("cmps");
	    	//lstFiltros = (ArrayList)wizardContext.getData("FLT");
	    	

	    	datos.setPortal((String)wizardContext.getData("cmpDP"));

		}
		camposElegidosMostrar = (Hashtable)wizardContext.getData("cmps");
    	lstFiltros = (ArrayList)wizardContext.getData("FLT");
		datos.setEtiquetaSLD((String)wizardContext.getData("ETQSLD"));
    	
    	//Genero la nueva capa
		
    	Collection selectedCategories = context.getLayerNamePanel().getSelectedCategories();
		    context.addLayer(selectedCategories.isEmpty()? StandardCategoryNames.WORKING: selectedCategories.iterator().next().toString(), "Nueva Capa",
		            createBlankFeatureCollection()).setFeatureCollectionModified(false).setEditable(true);
		    ((EditingPlugIn) context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY)).getToolbox(context.getWorkbenchContext()).setVisible(true);
		
		    
	    capa = (GeopistaLayer)context.getLayerNamePanel().getLayerManager().getLayer("Nueva Capa");
	    
	    capa.setName(datos.getNombreConsultaCapa());
	    
	    setStylesSLD(capa, datos.getEtiquetaSLD());


	    //Mando al método setDataType todos los campos que hemos obtenido.
	   
		if (capa.isLocal()){
			//setDataType((Hashtable)wizardContext.getData("cmps"));
			setDataType(camposElegidosMostrar);
			setDataType(datos.getTipoElegido());
		}
		
		progressDialog.setTitle("TaskMonitorDialog.Wait");		
        progressDialog.addComponentListener(new ComponentAdapter()
        {
        	public void componentShown(ComponentEvent e)
            { 
        		new Thread(new Runnable()
        		{
        			public void run()
        			{
        				try{
        					progressDialog.report("Cargando datos");  
        					obtenerFilas();  

        					if(!accionEjecutar){
        						// Si la accion no es de ejecucion se hacen las operaciones necesarias con la BBDD 
        						// para guardar la consulta en caso de que sea una nueva o actualizar la consulta seleccionada        				
        						guardarConsulta();
        					}
        					
        				} catch (SecurityException e) {
        					e.printStackTrace();
        				} catch (IllegalArgumentException e) {
        					e.printStackTrace();
        				} catch (Exception e){
        					e.printStackTrace();
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

	public void exiting() {
		// TODO Auto-generated method stub
		
	}

	public void exitingToRight() throws Exception {
		// TODO Auto-generated method stub	    
		capa.getLayerManager().setFiringEvents(true);
	}

	public String getID() {
		// TODO Auto-generated method stub
		return localId;
	}

	public String getInstructions() {
		// TODO Auto-generated method stub
		return "Resultados";
	}

	public String getNextID() {
		// TODO Auto-generated method stub
		return nextID;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return this.getName();
	}

	public boolean isInputValid() {
		// TODO Auto-generated method stub
		wizardContext.previousEnabled(false);
		return true;
	}

	public void remove(InputChangedListener listener) {
		// TODO Auto-generated method stub
		dataType=null;					
	}

	public void setNextID(String nextID) {
		// TODO Auto-generated method stub
		
	}

	public void setWizardContext(WizardContext wd) {
		// TODO Auto-generated method stub
		wizardContext =wd;
	}
	
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setBounds(new Rectangle(46, 90, 492, 288));
		}
		return jTextArea;
	}
	
	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(46, 44, 246, 33));
			jLabel.setText(I18N.get("Georreferenciacion","georeference.panel06.errorLabel"));
		}
		return jLabel;
	}
	
	private JScrollPane getJScrollPanel()
    {
        if (jScrollPanel == null)
        {
            jScrollPanel = new JScrollPane(getJTextArea());
            jScrollPanel.setBounds(new Rectangle(46, 90, 492, 288));
            jScrollPanel.setAutoscrolls(true);
            jScrollPanel.createHorizontalScrollBar();            
        }
        return jScrollPanel;
    }
	
	private void setDataType(Hashtable localizado){
		
		//Como la geometría la añado al crear la capa, le indico que me añada al 'dataType' primero la geoemtría y luego el resto de campos
		//Al añadir la geometría el 'dataType' aun esta vacío, sin embargo al añadir el resto ya no (contiene a la geometría),
		//por eso distingo entre 'null' y '!=null'.
		
		ArrayList key_nuevo=new ArrayList();
		
		if (dataType == null){
			dataType = new HashMap();
			dataTypeCamposBBDD = new HashMap();
			Set valores = localizado.keySet();
			Iterator listaCampos = valores.iterator();

			String tipoDeCampo = "";
			while (listaCampos.hasNext()){				
				String key = listaCampos.next().toString();
				AttributeType atributo = null;	
				if(localizado.get(key)!=null){
					if (localizado.get(key).toString().equalsIgnoreCase("GEOMETRY")){
						atributo = AttributeType.GEOMETRY;
					}
					else{
						atributo = typeOfData(localizado.get(key).toString());
					}
				}				
				dataType.put(key, atributo);
				
				//solo para guardar o actualizar la consulta
				dataTypeCamposBBDD.put(key,localizado.get(key).toString());
			}
		}else if(dataType!=null){
			Set valores = localizado.keySet();
			Iterator listaCampos = valores.iterator();

			while (listaCampos.hasNext()){
				String key = listaCampos.next().toString();
				AttributeType atributo = null;	
				if(localizado.get(key)!=null){
					if (localizado.get(key).toString().equalsIgnoreCase("GEOMETRY")){
						atributo = AttributeType.GEOMETRY;
					}
					else{
						atributo = typeOfData(localizado.get(key).toString());						
					}
				}				
				dataType.put(key, atributo);
				
				//solo para guardar o actualizar la consulta
				dataTypeCamposBBDD.put(key,localizado.get(key));
			}		
		}
	}
	
	private void guardarConsulta(){
		String user = AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME);
		
		ConsultaDatosBean consultaDatosBean = new ConsultaDatosBean();
		consultaDatosBean.setNombreConsulta(datos.getNombreConsultaCapa());
		consultaDatosBean.setDescripcion(datos.getDescripcion());
		consultaDatosBean.setUsuario(user);
		consultaDatosBean.setNombre_bbdd_ext(datos.getBbdd());
		consultaDatosBean.setNombre_tabla_ext(datos.getTabla());

		consultaDatosBean.setMetodo_georeferencia(datos.getTipoDatos());
		consultaDatosBean.setTipo_geometria(datos.getTipoGeometria());
		consultaDatosBean.setTabla_cruce(datos.getTablaGeometria());
		consultaDatosBean.setCampo_etiqueta(datos.getEtiquetaSLD());
		
		
		if(datos.getTipoDatos().equals("Coordenadas X,Y")){
			// cuando se selecciona Coordenadas X,Y se tienen que almacenar dos campos
			// el puntox y el puntoy y se almacenan en la BB de la siguiente forma
			// campo1:campo2
			String camposEleg = "";
			for(int a=0; a<campoElegidosCoordeXY.size(); a++){
				Hashtable campoRefElegido = (Hashtable)campoElegidosCoordeXY.get(a);
				Set indices = campoRefElegido.keySet();
				Iterator listaCamposEleg = indices.iterator();		
				while (listaCamposEleg.hasNext()){
					String key = listaCamposEleg.next().toString();
					camposEleg += key +":";
				}	
			}
			
			if(camposEleg.endsWith(":")){
				camposEleg = camposEleg.substring(0, camposEleg.length()-1);
			}
			consultaDatosBean.setCampo_georeferencia(camposEleg);
			
						
			
		}
		else{
			Set indices = datos.getTipoElegido().keySet();
			Iterator listaCamposEleg = indices.iterator();		
			while (listaCamposEleg.hasNext()){
				String key = listaCamposEleg.next().toString();
				consultaDatosBean.setCampo_georeferencia(key);
			}	
			
		}
	
		//se guardan los datos en la BBDD de la siguiente forma
		// nombreCampo1=valor1:nombreCampo2=valor2: .....
		String camposMostrar = "";
		//Set indicesCamposTipo = datos.getCamposTipo().keySet();
		Set indicesCamposTipo = dataTypeCamposBBDD.keySet();
		
		Iterator listaCamposElegTipo = indicesCamposTipo.iterator();		
		while (listaCamposElegTipo.hasNext()){
			String key = listaCamposElegTipo.next().toString();
			String value = (String) dataTypeCamposBBDD.get(key);
			camposMostrar += key + "=" +value + ":";
		
		}
		if(camposMostrar.endsWith(":")){
			camposMostrar = camposMostrar.substring(0, camposMostrar.length()-1);
		}
		
		consultaDatosBean.setCampos_mostrar(camposMostrar);
		
		// se guardan los filtros en la BBDD de la forma
		// OPERADOR --> nombreCampo1;operador1:nombreCampo2;operador2
		// VALOR --> 	nombreCampo1;valor1:nombreCampo2;valor2:  ......
		
		String filtroOperador = "";
		String filtroValor = "";
		if(!lstFiltros.isEmpty()){
			for(int j=0; j<lstFiltros.size();j++){
				FiltroData filtroDataLocal= (FiltroData)lstFiltros.get(j);
				
				if(filtroDataLocal != null){
					filtroOperador += filtroDataLocal.getCampo() + "#" + filtroDataLocal.getOperador(); 
					filtroValor +=filtroDataLocal.getCampo() + "#" + filtroDataLocal.getValor();

					if(j<lstFiltros.size()-1){
						//hay mas elementos
						filtroOperador += ":";
						filtroValor += ":";
					}
				}	
			}
		}
		
		consultaDatosBean.setFiltro_operador(filtroOperador);
		consultaDatosBean.setFiltro_valor(filtroValor);
		consultaDatosBean.setPortal(datos.getPortal());
		
		if(consultaSelected!=null){
			//es una modificacion
			consultaDatosBean.setId(consultaSelected.getId());
			GeorreferenciaExternaWSWrapper.actualizarConsulta(consultaDatosBean);
		}
		else{
			//es un alta
			GeorreferenciaExternaWSWrapper.guardarConsulta(consultaDatosBean);
		}
	}
	
	public void obtenerFilas(){

		boolean firingEvents=false;	
		int var=0,contador=0;
		numPasadasTotal=0;		
		/*if(monitor.isCancelRequested()){
			progressDialog.report("Cancelando operación");
			return;
		}*/
		try{
			//Relleno los ArrayList, fieldNames y fieldTypes
			//Relleno los ArrayList, fieldNames y fieldTypes			
			
			fieldNames.clear();
			fieldTypes.clear();
			
			if(capa.isLocal() && dataType != null){
				Set valores = dataType.keySet();
				Iterator listaCampos = valores.iterator();

				while (listaCampos.hasNext()){
					String key = listaCampos.next().toString();
					fieldNames.add(key);
					if (dataType.get(key) == null)
						fieldTypes.add(AttributeType.STRING);
					else
						fieldTypes.add(dataType.get(key));
				}
			}
	
			//Relleno el hashtable para enviar datos al webservice a través del 'GeorreferenciaciónExternaData'
			
			Hashtable data=new Hashtable();
			
	    	for (int v=0;v<fieldNames.size();v++){	
	    		data.put(fieldNames.get(v).toString(),fieldTypes.get(v).toString());		    				    		
	    	}
	    	
	    	//Le envío los datos a 'GeorreferenciaciónExternaData'.
	    	
	    	datos.setCamposTipo(data);
	    	
	    	int valor=GeorreferenciaExternaWSWrapper.obtenerTotalFilasBbdd(datos.getBbdd(), datos.getTabla(), datos.getTipoDatos(), 
	    				datos.getTipoElegido());
	    	
	    	while(var<valor){
	    		if(numPasadasTotal!=0){
		    		inicio=var;
			    	fin=var+100;
		    	}else{
		    		inicio=0;
		    		fin=100;
		    	}
	    		
	    		RowBean[] datosBBDD = GeorreferenciaExternaWSWrapper.obtenerDatosBbdd(
	    				datos.getBbdd(), datos.getTabla(), datos.getCamposTipo(), datos.getTipoDatos(), 
	    				datos.getTipoElegido(),inicio,fin, datos.getTipoGeometria(), datos.getTablaGeometria(),
	    				context.getLayerManager().getCoordinateSystem().getEPSGCode(), campoElegidosCoordeXY, 
	    				lstFiltros, datos.getPortal());
	    		
			   	if(datosBBDD!=null && progressDialog.isCancelRequested()==false){
			   		
					progressDialog.report(var,valor," features");
				   	addLayerAttribute(datosBBDD);
				}else if(datosBBDD!=null && progressDialog.isCancelRequested()){	
				
			    	var=valor;
			    	contador++;
			    	this.jTextArea.append("Proceso cancelado\n\n");
			    }else if(datosBBDD==null){
			    
			    	this.jTextArea.append("No hay datos para procesar en el paquete "+ numPaquete +"\n\n");
			    }
			   	var+=100;
	    	}
	    	
	    	if(contador==0 && valor!=0){
	    		
	    		this.jTextArea.append("Proceso finalizado correctamente\n\n");
	    	}else if(contador==0 && valor==0){
	    		
	    		this.jTextArea.append("No hay datos para procesar\n\n");  		
	    	}
		}catch(Exception e){			
			this.jTextArea.append(e.toString()+"\n\n");
		}finally
		{
			if(!capa.isLocal()){
				capa.getLayerManager().setFiringEvents(firingEvents);
			}
		}
	}

	public void addLayerAttribute(com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.RowBean[] rowbeannuevo) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{		
		validator = new SchemaValidator(null);						
		GeopistaSchema newSchema=new GeopistaSchema();
		GeopistaFeature fNew=null;
		boolean firingEvents=false;	

		capa.setEditable(true);

		//Saco el esquema de la nueva capa aún por rellenar, solo con la geometría

		GeopistaSchema schema=(GeopistaSchema)capa.getFeatureCollectionWrapper().getFeatureSchema();

		if(capa.isLocal() && dataType != null){//SOLO CUANDO LA CAPA ES LOCAL
			newSchema=completSchema(schema,fieldNames,fieldTypes);				
			Collection featureCollection= new ArrayList();
			ILayerManager layerManager =capa.getLayerManager();
			boolean exitLoop=false;				

			numPaquete++;
			com.localgis.ws.georreferenciaexterna.client.protocol.ServicesStub.ColumnsBean[] colb=null;
			int cont=0;

			for (int h=0;h<rowbeannuevo.length;h++){
									
				if(rowbeannuevo[h]!=null){
					
					fNew=new GeopistaFeature(newSchema);	
					if(rowbeannuevo[h].getGeometryWkt()!=null && /*!rowbeannuevo[h].getGeometryWkt().equals("") &&*/ !rowbeannuevo[h].getGeometryWkt().toLowerCase().equals("null")){
						
							com.vividsolutions.jts.io.WKTReader a = new com.vividsolutions.jts.io.WKTReader();
							
							String wktGeometry = rowbeannuevo[h].getGeometryWkt();
							
							
							try {
								if(wktGeometry.endsWith("EMPTY")){

									fNew.setGeometryEmpty();
								}
								else{
									Geometry g = a.read(wktGeometry);
								
									CoordinateSystem inCoord = 
										PredefinedCoordinateSystems.getCoordinateSystem(Integer.valueOf(rowbeannuevo[h].getSrid()));
									
					                CoordinateSystem outCoord = 
					                	PredefinedCoordinateSystems.getCoordinateSystem(layerManager.getCoordinateSystem().getEPSGCode());
					               
					                Reprojector.instance().reproject(g,inCoord,outCoord);

					               
									fNew.setGeometry(g);
											
								}
								
							} catch (com.vividsolutions.jts.io.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}								
						
						colb=rowbeannuevo[h].getColumns();						
						for (int j=0;j<colb.length;j++){
							if(colb[j].getType().equals("INTEGER")){									
								fNew.setAttribute(colb[j].getName().toString(),new Integer(colb[j].getValue().toString()));									
							}else if(colb[j].getType().equals("DOUBLE")){
								if(datos.getTipoDatos().equals("Dirección Postal") && colb[j].getName().toString().equals(datos.getTipoElegido().toString().substring(1, datos.getTipoElegido().toString().indexOf("=")))){
									fNew.setAttribute(colb[j].getName().toString(),new Integer(colb[j].getValue().toString()));
								}else{
									fNew.setAttribute(colb[j].getName().toString(),Double.parseDouble(colb[j].getValue().toString()));	
								}																		
							}else if(colb[j].getType().equals("DATE")){
								if(!colb[j].getValue().equals("")){
									fNew.setAttribute(colb[j].getName().toString(),getDate(colb[j].getValue().toString()));
								}else{
									fNew.setAttribute(colb[j].getName().toString(), null);
								}																		
							}else if (colb[j].getType().equals("STRING")){
								fNew.setAttribute(colb[j].getName().toString(), colb[j].getValue().toString());
							}else if (colb[j].getType().equals("TIMESTAMP")){
								if(!colb[j].getValue().equals("")){
									fNew.setAttribute(colb[j].getName().toString(), Timestamp.valueOf(colb[j].getValue().toString()));
								}else{
									fNew.setAttribute(colb[j].getName().toString(), null);
								}									
							}else if (colb[j].getType().equals("TIME")){
								if(!colb[j].getValue().equals("")){
									fNew.setAttribute(colb[j].getName().toString(), Time.valueOf(colb[j].getValue().toString()));
								}else{
									fNew.setAttribute(colb[j].getName().toString(), null);
								}									
							}else if (colb[j].getType().equals("BOOLEAN")){
								if(!colb[j].getValue().equals("")){
									fNew.setAttribute(colb[j].getName().toString(), Boolean.parseBoolean(colb[j].getValue().toString()));
								}else{
									fNew.setAttribute(colb[j].getName().toString(), null);
								}									
							}else if (colb[j].getType().equals("FLOAT")){
								fNew.setAttribute(colb[j].getName().toString(), Float.parseFloat(colb[j].getValue().toString()));
							}else if (colb[j].getType().equals("LONG")){
								fNew.setAttribute(colb[j].getName().toString(), Long.parseLong(colb[j].getValue().toString()));
							}							
						}							
						fNew.setLayer(capa);
						featureCollection.add(fNew);
						numPasadasTotal++;
					}else{
						cont++;		
						numPasadasTotal++;
					}
				}
			}
			if(cont!=0 && cont==rowbeannuevo.length){
				this.jTextArea.append("En el paquete "+ numPaquete +" no hay geometrías asociadas\n\n");
			}else if(cont==0 || (cont!=0 && cont<rowbeannuevo.length)){
				capa.getFeatureCollectionWrapper().addAll(featureCollection);
				capa.setEditable(false);				
			}			
		}else{
			this.jTextArea.append("No se puede generar la feature\n\n");
		}	
	}	
	
    private AttributeType typeOfData(String s)
    {
        s=s.trim();
        AttributeType res = null;
        //Transformo tipo de datos.
        if (s.equals("STRING") || s.equals("CHAR") || s.equals("VARCHAR") || s.equals("LONGVARCHAR")){
        	res=AttributeType.STRING;
        }else if (s.equals("LONG") || s.equals("BIGINT")){
        	res=AttributeType.LONG;
        }else if (s.equals("INTEGER") || s.equals("NUMBER") || s.equals("INT2") || s.equals("INT4") || s.equals("INT8") || s.equals("SMALLINT")){
        	res=AttributeType.INTEGER;
        }else if (s.equals("FLOAT")){
        	res=AttributeType.FLOAT;
        }else if (s.equals("DOUBLE") || s.equals("DECIMAL") || s.equals("NUMERIC") || s.equals("REAL")){
        	res=AttributeType.DOUBLE;        	
        }else if (s.equals("DATE")){        	
        	res=AttributeType.DATE;
        }else if(s.equals("GEOMETRY")){
        	res=AttributeType.GEOMETRY;        
        }/*else if(s.equals("BOOLEAN")){
        	res=AttributeType.BOOLEAN;
        }else if(s.equals("TIME")){
            	res=AttributeType.TIME; 
        }else if(s.equals("TIMESTAMP")){
        	res=AttributeType.TIMESTAMP; 
        }else if(s.equals("BLOB")){
            	res=AttributeType.BLOB; 
        }else if(s.equals("CLOB")){
        	res=AttributeType.CLOB; 
        }else if (s.equals("OTHER")){
        	res=AttributeType.STRING;
        }*/
		return res; 
    }
    
    public Date getDate(String val){
        Date value=null;
        try{
        	SimpleDateFormat sdf=null;
        	if(val.contains("-")){
        		sdf = new SimpleDateFormat("yyyy-MM-dd");
        	}else{
        		sdf = new SimpleDateFormat("dd/MM/yy");
        	}
        	value=sdf.parse(val);
        }catch(Exception e){
            e.printStackTrace();
        }
        return value;
    }
    
    public GeopistaSchema completSchema(GeopistaSchema schema,ArrayList fieldNames,ArrayList fieldTypes){
        
        AttributeType attributeType,typeSchema= null;
        Table tableDummy = new Table();
        
        if(schema.getAttributeCount()!=0){
        	for(int i=0;i<fieldNames.size();i++){
        		for (int h=0;h<schema.getAttributeCount();h++){
        			attributeType =(AttributeType)fieldTypes.get(i); 
            		if(schema.getAttributeName(h).equals(fieldNames.get(i))){        			
            			if (schema.getAttributeByColumn(schema.getAttributeName(h))==null){
            				Domain domainDummy = null;
                            if(attributeType.equals(AttributeType.STRING)){
                                domainDummy= new StringDomain("?[.*]","");
                            }else if(attributeType.equals(AttributeType.INTEGER) || attributeType.equals(AttributeType.LONG )){
                                domainDummy= new NumberDomain("?[-INF:INF]","");
                            }else if(attributeType.equals(AttributeType.DOUBLE) || attributeType.equals(AttributeType.FLOAT)){
                                domainDummy= new NumberDomain("?[-INF:INF]","");
                            }else if(attributeType.equals(AttributeType.DATE)){
                                domainDummy= new DateDomain("?[*:*]","");
                            }else
                                domainDummy= new StringDomain("?[.*]","");
                            
                            Column columnDummy = new Column(fieldNames.get(i).toString(), "", tableDummy,domainDummy);
                            schema.setAttributeColumn(h, columnDummy);
                            schema.setAttributeAccess(fieldNames.get(i).toString(), "R/W");
            			}        		
        			} 
        		}
        	}
        }
        
        List atributo= schema.getAttributes();
        fieldNames.remove("");        
        tableDummy.setName(datos.getTabla());
        
        //recorremos el schema
        int numOldattr=schema.getAttributeCount();
        
        for(int i=0;i<fieldNames.size();i++){
        	for(int t=0;t<numOldattr;t++){
        		attributeType =(AttributeType)fieldTypes.get(i); 
        		if(!fieldNames.get(i).equals(atributo.get(t))){
        			if (schema.getAttributeByColumn(fieldNames.get(i).toString())==null){
                        Domain domainDummy = null;
                        if(attributeType.equals(AttributeType.STRING)){
                            domainDummy= new StringDomain("?[.*]","");
                        }else if(attributeType.equals(AttributeType.INTEGER) || attributeType.equals(AttributeType.LONG )){
                            domainDummy= new NumberDomain("?[-INF:INF]","");
                        }else if(attributeType.equals(AttributeType.DOUBLE) || attributeType.equals(AttributeType.FLOAT)){
                            domainDummy= new NumberDomain("?[-INF:INF]","");
                        }else if(attributeType.equals(AttributeType.DATE)){
                            domainDummy= new DateDomain("?[*:*]","");
                        }else
                            domainDummy= new StringDomain("?[.*]","");
                        
                        Column columnDummy = new Column(fieldNames.get(i).toString(), "", tableDummy,domainDummy);                  
                        schema.addAttribute(fieldNames.get(i).toString(), attributeType, columnDummy,
                                GeopistaSchema.READ_WRITE);
                    }
        		}
        	}
        }
        return schema;
    }
    
    //Este método me sirve para sacar el esquema básico de la capa.
	
	public FeatureCollection createBlankFeatureCollection() {
		Hashtable hhs = new Hashtable();
		GeopistaSchema gShema=new GeopistaSchema();
        gShema.addAttribute("GEOMETRY", this.typeOfData("GEOMETRY"));        
        hhs.put(gShema.getAttributeName(gShema.getAttributeIndex("GEOMETRY")), gShema.getAttributeType(gShema.getAttributeIndex("GEOMETRY")));
        setDataType(hhs);
        return new FeatureDataset(gShema);
    }	
	
	
	private void setStylesSLD(GeopistaLayer capa, String etiqueta){
		FeatureCollectionWrapper featureCollection =   capa.getFeatureCollectionWrapper();
		Symbolizer[] symbolizerArray = new Symbolizer[1];
		SLDStyleImpl style = (SLDStyleImpl)capa.getBasicStyle();
		createTextPoint(style, symbolizerArray, capa.getName());
    	Rule newRule = StyleFactory.createRule(symbolizerArray);
    	style.getUserStyle("default").getFeatureTypeStyles()[0].addRule(newRule);
    	capa.getLabelStyle().setEnabled(true);
    	capa.getLabelStyle().setAttribute(etiqueta);
    	capa.getLabelStyle().setColor(style.getLineColor());
    	capa.getVertexStyle().setEnabled(true);
    	

    	if (style!=null && style.getStyles().size()>0){
    		for (Iterator iteratorStyles = style.getStyles().iterator();iteratorStyles.hasNext();){
    			UserStyle_Impl userStyle = (UserStyle_Impl)iteratorStyles.next();
    			if(userStyle!=null && userStyle.getFeatureTypeStyles()!=null){
    				for (int indice=0;indice<userStyle.getFeatureTypeStyles().length;indice++){
    					FeatureTypeStyle_Impl featureStyle = (FeatureTypeStyle_Impl) userStyle.getFeatureTypeStyles()[indice];
    					if(featureStyle.getRules()!=null){
    						for (int indiceRules = 0; indiceRules < featureStyle.getRules().length; indiceRules++){
    							Rule_Impl rule = (Rule_Impl) featureStyle.getRules()[indiceRules];
    							if(rule.getSymbolizers()!=null){
    								for(int indiceSymbolicers = 0; indiceSymbolicers < rule.getSymbolizers().length; indiceSymbolicers++){
    									if (rule.getSymbolizers()[indiceSymbolicers] instanceof LineSymbolizer_Impl){
    										//if((featureCollection.getFeatureSchema().hasAttribute("TEXTO"))&&(capa.getName().indexOf("CO")==-1)){
    										if((featureCollection.getFeatureSchema().hasAttribute(datos.getEtiquetaSLD()))){
    											featureStyle.removeRule(rule);    											
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
		
	}
	
	 private void createTextPoint(SLDStyleImpl style,Symbolizer[] symbolizer, String layerName) {
			
			boolean italic = false;
			boolean bold = false;
			double fontSize = 10;
			org.deegree.graphics.sld.Font font = (org.deegree.graphics.sld.Font)StyleFactory.createFont("Arial",italic,bold,fontSize);
			Color colorFont = style.getLineColor();
			((org.deegree.graphics.sld.Font) font).setColor(colorFont);
			String atributeName = datos.getEtiquetaSLD();//"TEXTO";
			
			double anchorX = 0.5;
			double anchorY = 0.5;
			double displacementX = 0;
			double displacementY = 0;
			double rotation = 0.0;
			
			
			/*if(layerName!=null){
				
				if (layerName.indexOf("AS")!=-1){				
					anchorX = 0.5;
					anchorY = 0.0;				
				}
				else if (layerName.indexOf("TL")!=-1){
					anchorX = 0.5;
					anchorY = 0.5;		
				}
				else{
					anchorX = 0.5;
					anchorY = 1.0;	
				}
			}*/
					
			PointPlacement pointPlacement = StyleFactory.createPointPlacement(anchorX,
				anchorY,displacementX,displacementY,rotation);
			LabelPlacement labelPlacement = StyleFactory.createLabelPlacement(pointPlacement);
			TextSymbolizer textSymbolizer = StyleFactory.createTextSymbolizer( null, StyleFactory2.createLabel(atributeName), (org.deegree.graphics.sld.Font) font, labelPlacement, null, null, 0, Double.MAX_VALUE);		
			symbolizer[0] = textSymbolizer;	
		}
	
	
}