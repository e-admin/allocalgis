/**
 * EditingInfoPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 package com.geopista.app.eiel.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.CabildoConsejoEIEL;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CasasConsistorialesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.CentrosAsistencialesEIEL;
import com.geopista.app.eiel.beans.CentrosCulturalesEIEL;
import com.geopista.app.eiel.beans.CentrosEnsenianzaEIEL;
import com.geopista.app.eiel.beans.CentrosSanitariosEIEL;
import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.beans.EdificiosSinUsoEIEL;
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.Encuestados1EIEL;
import com.geopista.app.eiel.beans.Encuestados2EIEL;
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.FeatureEIELSimple;
import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.beans.LonjasMercadosEIEL;
import com.geopista.app.eiel.beans.MataderosEIEL;
import com.geopista.app.eiel.beans.NucleoEncuestado7EIEL;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.OtrosServMunicipalesEIEL;
import com.geopista.app.eiel.beans.PadronMunicipiosEIEL;
import com.geopista.app.eiel.beans.PadronNucleosEIEL;
import com.geopista.app.eiel.beans.ParquesJardinesEIEL;
import com.geopista.app.eiel.beans.PlaneamientoUrbanoEIEL;
import com.geopista.app.eiel.beans.PoblamientoEIEL;
import com.geopista.app.eiel.beans.PuntosVertidoEIEL;
import com.geopista.app.eiel.beans.RecogidaBasurasEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.beans.ServiciosRecogidaBasuraEIEL;
import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.TanatoriosEIEL;
import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.eiel.beans.WorkflowEIEL;
import com.geopista.app.eiel.beans.filter.LCGMunicipioEIEL;
import com.geopista.app.eiel.beans.filter.LCGNodoEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.dialogs.AbastecimientoAutonomoDialog;
import com.geopista.app.eiel.dialogs.EntidadesAgrupadasDialog;
import com.geopista.app.eiel.dialogs.CabildoConsejoDialog;
import com.geopista.app.eiel.dialogs.CaptacionesDialog;
import com.geopista.app.eiel.dialogs.CarreterasDialog;
import com.geopista.app.eiel.dialogs.CasaConsistorialDialog;
import com.geopista.app.eiel.dialogs.CementeriosDialog;
import com.geopista.app.eiel.dialogs.CentroCulturalDialog;
import com.geopista.app.eiel.dialogs.CentroEnsenianzaDialog;
import com.geopista.app.eiel.dialogs.CentrosAsistencialesDialog;
import com.geopista.app.eiel.dialogs.CentrosSanitariosDialog;
import com.geopista.app.eiel.dialogs.ColectoresDialog;
import com.geopista.app.eiel.dialogs.DepositosDialog;
import com.geopista.app.eiel.dialogs.Depuradoras1Dialog;
import com.geopista.app.eiel.dialogs.Depuradoras2Dialog;
import com.geopista.app.eiel.dialogs.DiseminadosDialog;
import com.geopista.app.eiel.dialogs.EIELListaTablaDialog;
import com.geopista.app.eiel.dialogs.EIELListaTablaVersionDialog;
import com.geopista.app.eiel.dialogs.EdificiosSinUsoDialog;
import com.geopista.app.eiel.dialogs.EmisoresDialog;
import com.geopista.app.eiel.dialogs.Encuestados1Dialog;
import com.geopista.app.eiel.dialogs.Encuestados2Dialog;
import com.geopista.app.eiel.dialogs.EntidadesAgrupadasDialog;
import com.geopista.app.eiel.dialogs.EntidadesSingularesDialog;
import com.geopista.app.eiel.dialogs.FichasFilterDialog;
import com.geopista.app.eiel.dialogs.IncendiosProteccionDialog;
import com.geopista.app.eiel.dialogs.InfoTerminosMunicipalesDialog;
import com.geopista.app.eiel.dialogs.InstalacionDeportivaDialog;
import com.geopista.app.eiel.dialogs.LonjasMercadosDialog;
import com.geopista.app.eiel.dialogs.MataderosDialog;
import com.geopista.app.eiel.dialogs.NucleosAbandonadosDialog;
import com.geopista.app.eiel.dialogs.NucleosPoblacionDialog;
import com.geopista.app.eiel.dialogs.OtrosServMunicipalesDialog;
import com.geopista.app.eiel.dialogs.PadronMunicipiosDialog;
import com.geopista.app.eiel.dialogs.PadronNucleosDialog;
import com.geopista.app.eiel.dialogs.ParquesJardinesDialog;
import com.geopista.app.eiel.dialogs.PlaneamientoUrbanoDialog;
import com.geopista.app.eiel.dialogs.PoblamientoDialog;
import com.geopista.app.eiel.dialogs.PuntosVertidoDialog;
import com.geopista.app.eiel.dialogs.RecogidaBasurasDialog;
import com.geopista.app.eiel.dialogs.SaneamientoAutonomoDialog;
import com.geopista.app.eiel.dialogs.ServiciosAbastecimientosDialog;
import com.geopista.app.eiel.dialogs.ServiciosRecogidaBasurasDialog;
import com.geopista.app.eiel.dialogs.ServiciosSaneamientoDialog;
import com.geopista.app.eiel.dialogs.TanatoriosDialog;
import com.geopista.app.eiel.dialogs.TramosConduccionDialog;
import com.geopista.app.eiel.dialogs.TratamientosPotabilizacionDialog;
import com.geopista.app.eiel.dialogs.ValidateMPTDialog;
import com.geopista.app.eiel.dialogs.VertederosDialog;
import com.geopista.app.eiel.models.EIELTableModel;
import com.geopista.app.eiel.models.AbastecimientoAutonomoEIELTableModel;
import com.geopista.app.eiel.models.CabildoConsejoEIELTableModel;
import com.geopista.app.eiel.models.CaptacionesEIELTableModel;
import com.geopista.app.eiel.models.CarreterasEIELTableModel;
import com.geopista.app.eiel.models.CasasConsistorialesEIELTableModel;
import com.geopista.app.eiel.models.CementeriosEIELTableModel;
import com.geopista.app.eiel.models.CentrosAsistencialesEIELTableModel;
import com.geopista.app.eiel.models.CentrosCulturalesEIELTableModel;
import com.geopista.app.eiel.models.CentrosEnsenianzaEIELTableModel;
import com.geopista.app.eiel.models.CentrosSanitariosEIELTableModel;
import com.geopista.app.eiel.models.ColectoresEIELTableModel;
import com.geopista.app.eiel.models.DepositosEIELTableModel;
import com.geopista.app.eiel.models.Depuradora1EIELTableModel;
import com.geopista.app.eiel.models.Depuradora2EIELTableModel;
import com.geopista.app.eiel.models.DiseminadosEIELTableModel;
import com.geopista.app.eiel.models.EIELTableModel;
import com.geopista.app.eiel.models.EdificiosSinUsoEIELTableModel;
import com.geopista.app.eiel.models.EmisariosEIELTableModel;
import com.geopista.app.eiel.models.Encuestados1EIELTableModel;
import com.geopista.app.eiel.models.Encuestados2EIELTableModel;
import com.geopista.app.eiel.models.EntidadesSingularesEIELTableModel;
import com.geopista.app.eiel.models.IncendiosProteccionEIELTableModel;
import com.geopista.app.eiel.models.InfoTerminosMunicipalesEIELTableModel;
import com.geopista.app.eiel.models.InstalacionesDeportivasEIELTableModel;
import com.geopista.app.eiel.models.LonjasMercadosEIELTableModel;
import com.geopista.app.eiel.models.MataderosEIELTableModel;
import com.geopista.app.eiel.models.NucleosAbandonadosEIELTableModel;
import com.geopista.app.eiel.models.NucleosPoblacionEIELTableModel;
import com.geopista.app.eiel.models.OtrosServMunicipalesEIELTableModel;
import com.geopista.app.eiel.models.PadronMunicipiosEIELTableModel;
import com.geopista.app.eiel.models.PadronNucleosEIELTableModel;
import com.geopista.app.eiel.models.ParquesJardinesEIELTableModel;
import com.geopista.app.eiel.models.PlaneamientoUrbanoEIELTableModel;
import com.geopista.app.eiel.models.PoblamientoEIELTableModel;
import com.geopista.app.eiel.models.PuntosVertidoEIELTableModel;
import com.geopista.app.eiel.models.RecogidaBasurasEIELTableModel;
import com.geopista.app.eiel.models.SaneamientoAutonomoEIELTableModel;
import com.geopista.app.eiel.models.ServiciosAbastecimientosEIELTableModel;
import com.geopista.app.eiel.models.ServiciosRecogidaBasuraEIELTableModel;
import com.geopista.app.eiel.models.ServiciosSaneamientoEIELTableModel;
import com.geopista.app.eiel.models.TanatoriosEIELTableModel;
import com.geopista.app.eiel.models.TramosConduccionEIELTableModel;
import com.geopista.app.eiel.models.TanatoriosEIELTableModel;
import com.geopista.app.eiel.models.TratamientosPotabilizacionEIELTableModel;
import com.geopista.app.eiel.models.VertederosEIELTableModel;
import com.geopista.app.eiel.utils.ColorTableCellRenderer;
import com.geopista.app.eiel.utils.ExcelExporter;
import com.geopista.app.eiel.utils.FiltroByFeature;
import com.geopista.app.eiel.utils.HideableNode;
import com.geopista.app.eiel.utils.LayerNotFoundException;
import com.geopista.app.eiel.utils.LoadedLayers;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.layers.ReloadLayerPlugIn;
import com.geopista.util.EIELPanel;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


public class EditingInfoPanel extends JPanel implements FeatureExtendedPanel
{        
    /**
	 * 
	 */
	private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;
	
	private static final long serialVersionUID = 1L;
	private EIELInfoPanel jPanelTree = null;
	private JSplitPane jSplitPane = null;
	private ListaDatosEIELJPanel jPanelListaElementos = null;
	private BotoneraJPanel jPanelBotonera = null;
	private SelectorMunicipioJPanel jPanelSelectorMunicipio = null;
	private String nameTableModel = null;
	private boolean lock= false;
	private String patternSelected = null;
		
	private static EditingInfoPanel _instance = null;
	
	int oldSelectedRow;
	
	static boolean cancelado=false;
	
	private LocalGISEIELClient eielClient;
	
	 private org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(EditingInfoPanel.class);
        
	 
	JDialog currentDialog;
	 
	public static EditingInfoPanel getInstance(){
		return _instance;
	}
	
	private void setInstance(EditingInfoPanel instance){
		_instance = instance;
	}
	
    public EditingInfoPanel()
    {
        super();
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        this.desktop= aplicacion.getMainFrame();
        this.locale= UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true);
        eielClient = InitEIEL.clienteLocalGISEIEL;
        initialize();       
        setInstance(this);
    }           
    
    private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(848, 500));
        
        //Tamaño de la parte izquierda de la pantalla.
        this.setPreferredSize(new java.awt.Dimension(440,500));
        
        
        //Panel Principal. Arriba el arbol de elemento y abajo los elementos seleccionados
        jSplitPane  = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        		getJPanelTree(),getJPanelListaElementos());
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerSize(10);
        
        this.setBorder(BorderFactory.createTitledBorder
                (null, I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.title"), 
                		TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 13))); 
       
        this.add(jSplitPane, 
        		new GridBagConstraints(0,0,1,1,0.1, 0.90,GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
        
        //Panel de seleccion de municipios para elementos de tipo vertedero, captaciones, etc.
        this.add(getJPanelSelectorMunicipio(), 
        		new GridBagConstraints(0,1,1,1,0.1, 0.02,GridBagConstraints.CENTER,
                        GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));

        this.add(getJPanelBotonera(), 
        		new GridBagConstraints(0,2,1,1,0.1, 0.08,GridBagConstraints.CENTER,
                        GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
        
        
        
    }
    
    public EIELInfoPanel getJPanelTree(){
    	
    	if (jPanelTree == null){
    		
    		jPanelTree = new EIELInfoPanel(true);
    		jPanelTree.setPreferredSize(new Dimension(200,200));
    		jPanelTree.setMaximumSize(jPanelTree.getPreferredSize());
    		jPanelTree.setMinimumSize(jPanelTree.getPreferredSize());
    		jPanelTree.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
            	
            	if ((e.getActionCommand()!=null) && (e.getActionCommand().equals(ConstantesLocalGISEIEL.EVENT_DOUBLE_CLICK))){
            		dominiosJPanel_doubleclick();
            	}
            	else if ((e.getActionCommand()!=null) && (e.getActionCommand().equals(ConstantesLocalGISEIEL.EVENT_EIEL_MAP_SELECTION_CHANGED))){
            		dominiosJPanel_selectionChanged(e);
            	}
            	else if ((e.getActionCommand()!=null) && (e.getActionCommand().equals(ConstantesLocalGISEIEL.EVENT_EIEL_MAP_FEATURE_CHANGED))){
            		dominiosJPanel_featureChanged(e);
            	}
            	else
            		dominiosJPanel_actionPerformed();
			}
		});
    	}
    	return jPanelTree;
    }
    
    public void dominiosJPanel_doubleclick(){
    	
    	/*String sUrlPrefix = CConstantesComando.adminCartografiaUrl;
        AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(sUrlPrefix + "AdministradorCartografiaServlet");
		ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
        Integer srid = new Integer(acClient.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad())));
        */
    	
        if (GeopistaEditorPanel.getEditor()!= null){	       	        
	        	try {
	        		if (getJPanelTree().getPatronSelected()==null) return;
	        		
	        		 String layerName=LoadedLayers.getName(getJPanelTree().getPatronSelected());	
	        		 GeopistaLayer geopistaLayer=LoadedLayers.getLayerOrGlobal(layerName);	
	        		 if (layerName!=null){	 			
	        				TreeRendereEIELDomains renderer=(TreeRendereEIELDomains)getJPanelTree().getTree().getCellRenderer();
	        			 	if (InitEIEL.loadedLayers.containsKey(layerName)){
	        			 		if (geopistaLayer!=null){
		        			 		new ReloadLayerPlugIn().loadFeatures(null,geopistaLayer, GeopistaEditorPanel.getEditor().getLayerViewPanel(),true);
		        			 		renderer.setIconoLoaded(getJPanelTree().getPatronSelected(),false);
		        			 		InitEIEL.loadedLayers.remove(layerName);
	        			 		}
	        			 	}
	        			 	else{
	        			 		if (geopistaLayer!=null){
		        			 		new ReloadLayerPlugIn().loadFeatures(null,geopistaLayer, GeopistaEditorPanel.getEditor().getLayerViewPanel(),false);
		        			 		//Modificamos el icono para indicar que se ha cargado la capa correctamente
			        				renderer.setIconoLoaded(getJPanelTree().getPatronSelected(),true);
			        				InitEIEL.loadedLayers.put(layerName,true);
	        			 		}
	        			 	}
	        				
	        				
	        				
	        		 }
	 			} catch (Exception e) {
	 				logger.warn("No existe la capa definida en el editor");
	 				e.printStackTrace();
	 			}	        		        
        }
    	
    	
    }
    
    public void dominiosJPanel_selectionChanged(ActionEvent e){
	 
	 	//System.out.println("Selection Changed");
	 	if (e.getSource() instanceof AbstractSelection){
	 		AbstractSelection abtractSelection=(AbstractSelection)e.getSource();
	 		ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems();
            //System.out.println("Feature:"+featuresCollection.size());

            //Solo podemos gestionar una feature en la seleccion
            if (featuresCollection.size()==1){
            	Iterator it=featuresCollection.iterator();
                while (it.hasNext()){
                	Feature feature=(Feature)it.next();
                	if (feature instanceof GeopistaFeature){
                		
                		GeopistaFeature geopistaFeature=(GeopistaFeature)feature;
                		String layerName=geopistaFeature.getLayer().getSystemId();
                		
                        GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().setStatusMessage("Capa seleccionada:"+layerName);
                		
                		//Buscamos el nodo del arbol asociado;
                        String nodoSeleccionado=LoadedLayers.getName(layerName);
                		
                		if (nodoSeleccionado!=null){
                            GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().setStatusMessage("Capa seleccionada:"+layerName);
                			
            				try {
								HashMap<String,Object> listaNodosByName=eielClient.getNodosEIELByName(nodoSeleccionado, locale);
								
								
								int posicionNodo=getJPanelTree().findPositionNodeTree(nodoSeleccionado);
		                		if (posicionNodo!=-1){		
		                			FiltroByFeature filtroByFeature=new FiltroByFeature();
		                			String sql=filtroByFeature.getSql(listaNodosByName,nodoSeleccionado,geopistaFeature);
		                			
		                			if (sql!=null){
		                	        	aplicacion.getBlackboard().put("EIEL_FILTRO_SQL",sql);
		                			}
		                		
		                			//En este momento obtenemos la informacion alfanumerica del nodo para filtrar la información solicitada.
		                			getJPanelTree().getTree().setSelectionRow(0);
		                			getJPanelTree().getTree().setSelectionRow(posicionNodo);
		                			getJPanelTree().getTree().scrollRowToVisible(posicionNodo);
		                			getJPanelTree().getTree().setScrollsOnExpand(true);		
		                			
		                			
		                		}
		                		else
		                			System.out.println("No se encuentra el nodo para:"+nodoSeleccionado);
		                		
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
            				
            				
	                	
                		}
                		else
                			GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().setStatusMessage("Capa seleccionada:"+layerName+" sin referencia alfanumerica");

                
                		//feature.getID();
                	}
                }
            }
            else{
            	//Si hay mas de una feature es dificil seleccionar en el arbol todas
            }
            
	 	}
    	/*ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems();
        Iterator featuresCollectionIter = featuresCollection.iterator();
        Feature actualFeature = (Feature) featuresCollectionIter.next();*/
    	
    	
    }
    
    
    public void dominiosJPanel_featureChanged(ActionEvent e){
    	
    	
   	 	//Solo lo realizamos si es el usuario tiene perfil de publicador
    	
		//if (ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
			if ((ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)) ||
					(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_VALIDADOR_EIEL))){
	
				
		 	if (e.getSource() instanceof FeatureEvent){
			 	logger.info("Actualizando elemento alfanumerico al cambiar el elemento grafico....");		 		
			 	
				GeopistaLayer geopistaLayer= (GeopistaLayer)((FeatureEvent) e.getSource()).getLayer();
				GeopistaFeature geopistaFeature= (GeopistaFeature)((FeatureEvent) e.getSource()).getFeatures().toArray()[0];
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				
				String nodoSeleccionado=getJPanelTree().getPatronSelected();
				try {
					HashMap<String,Object> listaNodosByName=eielClient.getNodosEIELByName(nodoSeleccionado, locale);
	
	        		FiltroByFeature filtroByFeature=new FiltroByFeature();
	        		String sql=filtroByFeature.getSql(listaNodosByName,nodoSeleccionado,geopistaFeature);
	        			
	        		if (sql!=null){
	        	       	aplicacion.getBlackboard().put("EIEL_FILTRO_SQL",sql);
	        		}
	    			ArrayList lst = obtenerListaDatos(getJPanelTree().getPatronSelected(),0,null);
	    			
	    			//Un solo elemento indica que no tiene ni temporal ni publicable, dos significa que uno es valido
	    			//y otro temporal o publicable
	    			if ( (lst!=null) && (lst.size()==1 || (lst.size()==2))){
	    				WorkflowEIEL workflowEIEL=(WorkflowEIEL)lst.get(0);
	    				//logger.info("Existe un solo elemento alfanumerico");
	    				InitEIEL.clienteLocalGISEIEL.insertarElemento(workflowEIEL, idLayer, nodoSeleccionado);
	    				
	    				int posicionNodo=getJPanelTree().findPositionNodeTree(nodoSeleccionado);
	            		if (posicionNodo!=-1){		
	            			            		
	            			//En este momento obtenemos la informacion alfanumerica del nodo para filtrar la información solicitada.
	            			getJPanelTree().getTree().setSelectionRow(0);
	            			getJPanelTree().getTree().setSelectionRow(posicionNodo);
	            			getJPanelTree().getTree().scrollRowToVisible(posicionNodo);
	            			getJPanelTree().getTree().setScrollsOnExpand(true);		
	            			
	            			
	            		}
	            		
	    			}
	    			
	    			
	    			/*else{
	    				logger.info("Posible inconsistencia en el numero de elementos devueltos");
	        		}*/	
	        			
				}
				catch (Exception e1){
					e1.printStackTrace();
				}
		 	}
			
	
	 	}
    	
    }
    	
    
    public void dominiosJPanel_actionPerformed(){
        try{
        	
        	
        	//Ponemos el nodo seleccionado en memoria.
        	aplicacion.getBlackboard().put("EIEL_NODO_SELECCIONADO",getJPanelTree().getPatronSelected());
        	//System.out.println("EIEL_NODO_SELECCIONADO:"+getJPanelTree().getPatronSelected());
        	jPanelSelectorMunicipio.setEnabled(false);
        	//ConstantesLocalGISEIEL.idMunicipioSeleccionado=0;
        	getJPanelBotonera().getJButtonModificar().setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonmodify"));
        	
        	 
        	if (getJPanelTree().getPatronSelected()!=null){
        		 pintarPendientes();
        	}
        	
            if (getJPanelTree().getNodeSelected() == null){
            	getJPanelBotonera().setEnabled(false);
            	if ((getJPanelTree().getPatronSelected()!=null)){
            		getJPanelBotonera().getJButtonInformes().setEnabled(true);            		
        			borrarDatosTabla();        			
        			if (GeopistaEditorPanel.getEditor()!= null)
            			deselectFeatureSelection();        		
        				//getRelationLayer("NP",true);
            	}
            	else{
            		//Si el patron seleccionado es nulo activamos la ficha Municipal.
            		getJPanelBotonera().getJButtonFichaMunicipal().setEnabled(true);
            		borrarDatosTabla();
            		if (GeopistaEditorPanel.getEditor()!= null)
            			deselectFeatureSelection();
            	}
            	
                return;
            }
           
                        
            listarDatosTabla();
            reColorearBloqueo(getJPanelTree().getPatronSelected());
            
            if(getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
            //	getJPanelBotonera().setEnabled(true);
//            	getJPanelBotonera().getJButtonVersionado().setEnabled(true);
//            	getJPanelBotonera().getJButtonListarTabla().setEnabled(true);
            	getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
            }
            
            //Fijamos la capa en el editor.
            try {
				selectLayerInLayerManager(getJPanelTree().getPatronSelected(),true);
			} catch (Exception e) {
				logger.warn("No existe la capa definida en el editor");
			}
            aplicacion.getBlackboard().remove("EIEL_FILTRO_SQL");
        }catch(Exception e){
            e.printStackTrace();
         }

    }
    
    /**
     * Pintamos los elementos pendientes de validar o publicables en el arbol
     */
    private void pintarPendientes(){
    	String parametros=null;
    	

    		if((ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)) ||
    				(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_VALIDADOR_EIEL))){
	    		
    	    	try {
    	    		
					HashMap nodos=getNumElementosPendientes(getJPanelTree().getPatronSelected(),locale);	
		
					if (nodos==null) return;
					Enumeration enumera=getJPanelTree().getNodeArbol().children();
					while (enumera.hasMoreElements()){
						DomainNode domain=(DomainNode)((HideableNode)enumera.nextElement()).getUserObject();					
						String term=domain.getFirstTerm();
						
						LCGNodoEIEL nodoEIEL=(LCGNodoEIEL)nodos.get(domain.getPatron());
						if (nodoEIEL!=null){
							if (InitEIEL.modelosPatrones.get(nodoEIEL.getClave())!=null){
								if (nodoEIEL.getElementosTemporales()>0 || nodoEIEL.getElementosPublicables()>0||nodoEIEL.getElementosExternos()>0)
									//parametros=" ("+nodoEIEL.getClave()+") (T:"+nodoEIEL.getElementosTemporales()+")"+ " (P:"+nodoEIEL.getElementosPublicables()+")";
									parametros="  (T:"+nodoEIEL.getElementosTemporales()+")"+ " (P:"+nodoEIEL.getElementosPublicables()+") (E:"+nodoEIEL.getElementosExternos()+") (B:"+nodoEIEL.getElementosBorrables()+")";
									//parametros=" ("+nodoEIEL.getClave()+")  (T:"+nodoEIEL.getElementosTemporales()+")"+ " (P:"+nodoEIEL.getElementosPublicables()+") (E:"+nodoEIEL.getElementosExternos()+")";
								else
									//parametros=" ("+nodoEIEL.getClave()+")";
									parametros="";
								domain.setParametros(parametros);								
							}
							else{
								parametros=" ("+nodoEIEL.getClave()+")";
								domain.setParametros(null);								
							}
						}else{
							//logger.error("No existe asociacion para:"+domain.getPatron());
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
						
    }
    
    private void borrarDatosTabla(){
    	//TODO Mejora la limpieza.
		//Limpiamos la pantalla de elementos. Esto seguro que hay una forma mas sencilla de borrarlos.
		try {
			Object tableModel=(Object)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel();
			if (tableModel instanceof TableSorted){
    			TableModel valor = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();            		
				DefaultTableModel valor2=(DefaultTableModel)valor;            	
				Class[] argumentTypes = { ArrayList.class };
				Method method = valor2.getClass().getMethod("setData", argumentTypes);
				Object[] arguments = { new ArrayList()};
				method.invoke(valor2,arguments);
				OrdenarTabla(((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Pintamos la fila segun el tipo de elemento
     * @param selectedPattern
     */
    public void reColorearBloqueo(String selectedPattern){

    	Vector redRows= new Vector();
    	
    	ArrayList colorRows=new ArrayList();

    	if (selectedPattern==null)
    		return;
    	
    	for (int i=0; i < ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getRowCount(); i++){

    		
    		// METODO GENERICO. VALIDO PARA TODOS LOS NODOS QUE EXTIENDAN WORKFLOWEIEL.
    		//else{
    			Color color=Color.black;
    			TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
    	    		
    			WorkflowEIEL elemento = ((EIELTableModel)tableModel).getValueAt(getModelIndex(i));
    			//Verificamos si el elemento esta bloqueado y no soy yo el que lo bloquea.
    			if (elemento != null && elemento.getBloqueado()!=null &&
    						!elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
    				color=Color.red;    				
    			}
    			else if ((elemento!=null) && (elemento.isEstadoTemporal())){
    				color=Color.blue;				
    			}
       			else if ((elemento!=null) && (elemento.isEstadoPublicable())){
    				color=Color.green;    				
    			}
       			else if ((elemento!=null) && (elemento.isEstadoAutoPublicable())){
    				color=Color.green;    				
    			}
       			else if ((elemento!=null) && (elemento.isEstadoBorrable())){
    				color=Color.orange;    				
    			}

    			colorRows.add(color);    			
    			
    			//((ListaDatosEIELJPanel)getJPanelListaElementos()).setPreferredSize(new Dimension(100,100));
    			for ( int j=0; j < ((EIELTableModel)tableModel).getColumnCount(); j++){
    				TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
    				//La primera columna del estado la hacemos un poco mas pequeña que el resto.
    				if (j==0){
    					col.setPreferredWidth(3);
    		            //col.setMinWidth(3);
    		            col.setMaxWidth(3);
    				}
    				else{
    					if (col.getHeaderValue().equals("Clave")){
    						col.setPreferredWidth(0);
    						col.setMinWidth(0);
    						col.setMaxWidth(0);
    					}
    					if (((String)col.getHeaderValue()).contains("Cod. Nucl")){
    						col.setPreferredWidth(0);
    						col.setMinWidth(0);
    						col.setMaxWidth(0);
    					}
    					//col.setPreferredWidth(200);
    		            //col.setMinWidth(3);
    		            //col.setMaxWidth(200);
    				}
    				ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(colorRows);
    				col.setCellRenderer(colorTableCellRenderer);
    			}

    		//}
    	}
    }
    
    
    public void listarDatosTabla(){
		if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_CONSULTA_EIEL)){
			listarDatosTabla(false,0,null);			
		}			
		else
			JOptionPane.showMessageDialog(
                    this,
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
			
				
    }
    
    public void listarDatosTabla(boolean activarBoton,int idMunicipio,LCGNucleoEIEL nucleoEIEL){
    	
    	/*if (patternSelected != null){
    		getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
    	}*/
    	oldSelectedRow=-1;
    	if (getJPanelTree().getPatronSelected()==null){
    		return;
    	}
    	
    	getJPanelBotonera().getJButtonModificar().setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonmodify"));

    	Object selectedItem = null;
    	if (getJPanelTree().getPatronSelected().equals(this.patternSelected)){
    		selectedItem = this.getSelectedElement();
    	}
    	this.patternSelected  = getJPanelTree().getPatronSelected();
    	
    	
    	if (patternSelected != null && !patternSelected.equals("")){
    		
    		
    		//Aqui indicamos todos aquellos en los que queremos que no se haga nada.
    		if (patternSelected.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AGUA)){
    			((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().clearSelection();
    			return;
    		}
    		if (patternSelected.equals(ConstantesLocalGISEIEL.SANEAMIENTO)){
    			((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().clearSelection();
    			return;
    		}
    		if(!ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_CONSULTA_EIEL)){
        		JOptionPane.showMessageDialog(
                        this,
                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                        null,
                        JOptionPane.INFORMATION_MESSAGE);
        		return;
    		}
    		
    		//TRATAMIENTO ESPECIAL
    		//GENERICO
    		else{
    			//Tratamiento de elementos especiales que no tienen informacion alfanumerica
    			if (isSpecialPattern(patternSelected)){
                    GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().setStatusMessage("Capa sin informacion alfanumerica asociada");
    				return;
    			}
    			ArrayList lst=recargarElementos(patternSelected,activarBoton,idMunicipio,nucleoEIEL);
    			this.setBorder(BorderFactory.createTitledBorder
    	                (null, I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.title")+" ("+patternSelected+":"+lst.size()+")", 
    	                		TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 13))); 
    			String activarMunicipio=InitEIEL.modelosActivarSelectorMunicipio.get(patternSelected);
    			String activarNucleo=InitEIEL.modelosActivarSelectorNucleo.get(patternSelected);
    		
    			if ((activarMunicipio!=null) && (activarMunicipio.equals("S"))){
    				jPanelSelectorMunicipio.setEnabledMunicipio(true);
	    			if ((activarNucleo!=null) && (activarNucleo.equals("S")))
						jPanelSelectorMunicipio.setEnabledNucleo(true);
	    			else
	    				jPanelSelectorMunicipio.setEnabledNucleo(false);
    			}
    			else{    			
    				jPanelSelectorMunicipio.setEnabledMunicipio(false);
    				if ((activarNucleo!=null) && (activarNucleo.equals("S")))
    					jPanelSelectorMunicipio.setEnabledNucleo(true);
    				else
    					jPanelSelectorMunicipio.setEnabledNucleo(false);
    			}
    			if (lst!=null){
    				try {
    					//Cambiamos el nombres de las columnas para que aparezca lo que viene de la capa en lugar de Info1 e Info2.
    					EIELTableModel tableModel=((EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel());
    					if (tableModel instanceof com.geopista.app.eiel.models.GenericEIELTableModel){
    						
    						//com.geopista.app.eiel.models.GenericEIELTableModel genericEIELTableModel=(com.geopista.app.eiel.models.GenericEIELTableModel)tableModel;
    						if (lst.size()>0){
	    						String columna1=(String)((WorkflowEIEL)lst.get(0)).getDatosAdicionales().keySet().toArray()[0];
	    						((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(2).setHeaderValue(columna1);
	    						String columna2=(String)((WorkflowEIEL)lst.get(0)).getDatosAdicionales().keySet().toArray()[1];
	    						((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(3).setHeaderValue(columna2);
    						}
    					}
    				}
    				catch (Exception e){
    					
    				}
    			
    				((EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
    			//getJPanelBotonera().setEnabled(false);
    			}
    		}
    	
    		
    		//en cualquier caso, ordenamos todos los campos de la tabla
    		try{
    			OrdenarTabla(((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()));
    		}
    		catch (Exception e){    			
    		}
    		
    		
    		//Intentamos dejar de nuevo seleccionado el elemento que hemos obtenido al principio del método
    		if (selectedItem != null){
    			this.setSeletecdRowByItem(selectedItem);
    		}
    	}
    	
    }
    
    public boolean isSpecialPattern(String patternSelected){
    	if (patternSelected!=null && 
    			(patternSelected.equals("ALUM") || (patternSelected.equals("TU")))){

        		getJPanelBotonera().getJButtonAniadir().setEnabled(false);
    			getJPanelBotonera().getJButtonModificar().setEnabled(false);
    			getJPanelBotonera().getJButtonConsultar().setEnabled(false);
    			getJPanelBotonera().getJButtonVersionado().setEnabled(false);
    			getJPanelBotonera().getJButtonEliminar().setEnabled(false);
    			getJPanelBotonera().getJButtonListarTabla().setEnabled(false);
    			//if (patternSelected.equals("TU"))
    			//	getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
    			//else
    				getJPanelBotonera().getJButtonFiltroInformes().setEnabled(true);
    			getJPanelBotonera().getJButtonExportar().setEnabled(true);
    			getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
    			getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(false);
    			getJPanelBotonera().getJButtonValidar().setEnabled(false);
    			getJPanelBotonera().getJButtonPublicar().setEnabled(false);        	
			return true;
    	}
    	else
    		return false;
					
    }
    
    /**
     * Recargamos los elementos en el panel
     * @param patternSelected
     * @param activarBoton
     * @param idMunicipio 
     */
    private ArrayList recargarElementos(String patternSelected, boolean activarBoton, int idMunicipio,LCGNucleoEIEL nucleoEIEL){
    	
    	String idMunicipioSesion = ConstantesLocalGISEIEL.idMunicipio;
    	
    	boolean desactivar=true;
    	if (idMunicipio!=0){
    		
    		String codProvincia=String.valueOf(idMunicipio).substring(0,2);
    		String codMunicipio=String.valueOf(idMunicipio).substring(2,5);
    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			desactivar=false;	    		
    	}
    	else{
    		desactivar=false;
    	}
    		
    	if (desactivar){
    		getJPanelBotonera().getJButtonAniadir().setEnabled(false);
			getJPanelBotonera().getJButtonModificar().setEnabled(false);
			getJPanelBotonera().getJButtonConsultar().setEnabled(false);			
			getJPanelBotonera().getJButtonVersionado().setEnabled(false);
			getJPanelBotonera().getJButtonEliminar().setEnabled(false);
			getJPanelBotonera().getJButtonListarTabla().setEnabled(false);
			getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
			getJPanelBotonera().getJButtonExportar().setEnabled(false);
			getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
			getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(false);
			getJPanelBotonera().getJButtonModificar().setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonasignar"));
			getJPanelBotonera().getJButtonValidar().setEnabled(false);
			getJPanelBotonera().getJButtonPublicar().setEnabled(false);
    	}
    	else{
    		getJPanelBotonera().getJButtonAniadir().setEnabled(true);
			getJPanelBotonera().getJButtonModificar().setEnabled(activarBoton);
			getJPanelBotonera().getJButtonConsultar().setEnabled(activarBoton);
			getJPanelBotonera().getJButtonVersionado().setEnabled(activarBoton);
			getJPanelBotonera().getJButtonEliminar().setEnabled(activarBoton);
			getJPanelBotonera().getJButtonListarTabla().setEnabled(true);
			getJPanelBotonera().getJButtonFiltroInformes().setEnabled(true);
			getJPanelBotonera().getJButtonExportar().setEnabled(true);
			getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(true);
			getJPanelBotonera().getJButtonModificar().setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonmodify"));
			getJPanelBotonera().getJButtonValidar().setEnabled(false);
			getJPanelBotonera().getJButtonPublicar().setEnabled(false);
    	}
    	
    	
    	//Tratamiento especifico de capas solo con datos graficos.
    	if (ConstantesLocalGISEIEL.capasEspeciales.containsKey(patternSelected)){
    		getJPanelBotonera().getJButtonAniadir().setEnabled(false);
			getJPanelBotonera().getJButtonModificar().setEnabled(false);
			getJPanelBotonera().getJButtonConsultar().setEnabled(false);
			getJPanelBotonera().getJButtonVersionado().setEnabled(false);
			getJPanelBotonera().getJButtonEliminar().setEnabled(false);
			getJPanelBotonera().getJButtonListarTabla().setEnabled(false);
			getJPanelBotonera().getJButtonFiltroInformes().setEnabled(true);
			getJPanelBotonera().getJButtonExportar().setEnabled(false);
			getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(false);
			getJPanelBotonera().getJButtonValidar().setEnabled(false);
			getJPanelBotonera().getJButtonPublicar().setEnabled(false);
			getJPanelBotonera().getjButtonValidarMPT().setEnabled(false);
    	}
    	
    	

    	
    	
		String modelo=InitEIEL.modelosPatrones.get(patternSelected);
		
		setNameTableModel(modelo);    			
		jPanelListaElementos = null;
		jSplitPane.setRightComponent(getJPanelListaElementos());    		    			
		ArrayList lst = obtenerListaDatos(patternSelected,idMunicipio,nucleoEIEL);
		
		
		
		
		if (lst==null || lst.size()==0){
			if (desactivar){
				getJPanelBotonera().getJButtonModificar().setEnabled(false);
				getJPanelBotonera().getJButtonConsultar().setEnabled(false);
			}
		}
		return lst;
    }
    
    
    
    private void OrdenarTabla(TableSorted tableSorted) {
    	for(int i=0; i < tableSorted.getColumnCount(); i ++){
			tableSorted.setSortingStatus(i, 1);
		}
	}

	private ArrayList getSelectedElements(ArrayList lst, String patternSelected){
    	
    	if (GeopistaEditorPanel.getEditor() != null){

			Collection lstSelectedFeatures = GeopistaEditorPanel.getEditor().getSelection();
			if (lstSelectedFeatures.size()>0){

				ArrayList lstTemp = new ArrayList();
				
				for (Iterator iter = lst.iterator(); iter.hasNext();){

					Object object =  (Object) iter.next();

					Collection lstIdFeatures = getIdFeatures(object, patternSelected);
					
					Iterator allFeatures= lstSelectedFeatures.iterator();
					while (allFeatures.hasNext()) {
						Feature feature= (Feature)allFeatures.next();

						for (Iterator iterIdsFeatures = lstIdFeatures.iterator(); iterIdsFeatures.hasNext();){
							Integer featureID = (Integer) iterIdsFeatures.next();
							if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase(featureID.toString())){
								lstTemp.add(object);
								break;
							}
						}
					}
				}
				if (lstTemp.size()>0)
					lst = lstTemp;
			}
		}  
    	return lst;
    }
    
    private Collection getIdFeatures(Object object, String selectedPattern){
    	
    	Collection lstIdFeatures = null;
    	try {
    		
    		lstIdFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, selectedPattern);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lstIdFeatures;
    }
        
    private ArrayList obtenerListaDatos(String tipo){
    	return obtenerListaDatos(tipo,0,null);
    }
    
    private ArrayList obtenerListaDatos(String tipo,int idMunicipio,LCGNucleoEIEL nucleoEIEL){

    	ArrayList lstElements = new ArrayList();
    	try {
    		
    		//String filter = Const.REVISION_EXPIRADA+" =9999999999  ";
    		
    		String nombreTabla=InitEIEL.tablasAlfanumericas.get(tipo);    		
    		ArrayList camposEspecificos=InitEIEL.camposTablaEspecificos.get(tipo);
    		
    		String filter="";
    		if (nombreTabla!=null){
	    		
	    		filter = " ("+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_VALIDA;
	    		filter += " or "+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_TEMPORAL;				
	    		filter += " or "+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE;				
	    		filter += " or "+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE_MOVILIDAD;
	    		filter += " or "+nombreTabla+".revision_expirada="+ConstantesLocalGISEIEL.REVISION_BORRABLE+")";
    		}
    		else{
    			filter = " (revision_expirada="+ConstantesLocalGISEIEL.REVISION_VALIDA;
	    		filter += " or revision_expirada="+ConstantesLocalGISEIEL.REVISION_TEMPORAL;				
	    		filter += " or revision_expirada="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE;				
	    		filter += " or revision_expirada="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE_MOVILIDAD;
	    		filter += " or revision_expirada="+ConstantesLocalGISEIEL.REVISION_BORRABLE+")";
    		}
    		String codEntidadNucleo=null;
    		if ((nucleoEIEL!=null) && (!nucleoEIEL.getCodentidad().equals("0"))){
    			String filtroTablaTR=InitEIEL.filtroTablasAlfanumericasTR.get(tipo);
    			codEntidadNucleo=nucleoEIEL.getCodentidad();
	    		if (filtroTablaTR!=null){
	    			filtroTablaTR=filtroTablaTR.replaceAll("\\$COD_PROV\\$", nucleoEIEL.getCodProvincia());
	    			filtroTablaTR=filtroTablaTR.replaceAll("\\$COD_MUN\\$", nucleoEIEL.getCodMunicipio());
	    			filtroTablaTR=filtroTablaTR.replaceAll("\\$COD_ENTIDAD\\$", nucleoEIEL.getCodentidad());
	    			filtroTablaTR=filtroTablaTR.replaceAll("\\$COD_POBL\\$", nucleoEIEL.getCodpoblamiento());	    			
	    			filter += filtroTablaTR;
	    		}
    		}
	    		
    		if (aplicacion.getBlackboard().get("EIEL_FILTRO_SQL")!=null){
    			filter += " and "+aplicacion.getBlackboard().get("EIEL_FILTRO_SQL");
    		}
    		
    		lstElements = InitEIEL.clienteLocalGISEIEL.getLstElementos(filter, tipo,
    				getJPanelBotonera().getJCheckBoxFiltroGeometrias().isSelected(),idMunicipio,nombreTabla,camposEspecificos,null,codEntidadNucleo);
    		
    		//Si se aplica un filtro en principio debería de salir unicamente un elemento
    		//si salen mas de uno puede existir algun problema
    		if (lstElements!=null && lstElements.size()==1){
    			
    		}
    	} catch (Exception e) {
    		logger.error("Excepcion al recuperar los elementos",e);
    		JOptionPane.showMessageDialog(
                    this,
                    "Se ha producido un error al recuperar los elementos",
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    	}

    	return lstElements;
    }
     
    private void setNameTableModel(String nameTableModel){
    	this.nameTableModel = nameTableModel;
    }
    
    private JPanel getJPanelListaElementos(){
    	  
    	if (jPanelListaElementos == null){
    		
    		jPanelListaElementos  = new ListaDatosEIELJPanel(nameTableModel);  
    		jPanelListaElementos.setPreferredSize(new Dimension(100,100));
    		jPanelListaElementos.setSize(new Dimension(100,100));

    		jPanelListaElementos.addActionListener(new java.awt.event.ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				
    				//Gestion del Double Click en un elemento
    				if (ConstantesLocalGISEIEL.EVENT_DOUBLE_CLICK.equals(e.getActionCommand())){
                		//bienesJPanel_dobleClick();
    					//System.out.println("DOUBLE CLICK");
                	}
    				else if (ConstantesLocalGISEIEL.SORT.equals(e.getActionCommand())){
                		//bienesJPanel_dobleClick();ç
    					//System.out.println("SORT");
    					reColorearBloqueo(getJPanelTree().getPatronSelected());
                	}
    				else
    					listaelementosJPanel_actionPerformed();
    			}
    		});
    	}
    	return jPanelListaElementos;
    }
    
    public void listaelementosJPanel_actionPerformed(){
        /** Desde GIS. Si no se ha bloqueado la capa para hacer los cambios, lock es true (evitamos hacer cambios en la feature) */

    	try {
    		((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
			
    		String patternSelected = getJPanelTree().getPatronSelected();
			int selectedRow = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectedRow();
			
			//DESELECCION SI YA ESTUVIESE SELECCIONADO
			if (oldSelectedRow==selectedRow){
				//logger.info("Borrando seleccion:"+oldSelectedRow+" vs "+selectedRow);
				((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().clearSelection();
				oldSelectedRow=-1;
				
				if (GeopistaEditorPanel.getEditor()!= null){
					deselectFeatureSelection();
				}
				
				enableButtonsNotSelected(false);
				return;
			}
			oldSelectedRow=selectedRow;
				
			if (selectedRow == -1) return;
    		
    		if (patternSelected != null && !patternSelected.equals("") && selectedRow >= 0){
    			
    			// Comprobar si el elemento seleccionado pertenece a esta provincia
    			
    			((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(true);
    			((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(true);
    			((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(true);
    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(true);
//    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(true);
    			if(patternSelected.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
    				((BotoneraJPanel)getJPanelBotonera()).setEnabled(true);
                	getJPanelBotonera().getJButtonVersionado().setEnabled(true);
                	getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);


    			}else{
    				//GENERICO
        			//else{
        				 WorkflowEIEL obj= ((EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
        				 if (obj!=null){
        					 recargarElemento(obj,obj.getCodINEProvincia(),obj.getCodINEMunicipio());
        				 }    			
        			//}
    			}
    			
        		
    			
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    }
    
    private void enableButtonsNotSelected(boolean activar){
    	((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(activar);
    	((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(activar);    	
    	((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(activar);
    	((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(activar);
    	((BotoneraJPanel)getJPanelBotonera()).getJButtonValidar().setEnabled(activar);
    }
    
    /**
     * Recargar el elemento y activa los controles necesarios
     * @param codProvincia
     * @param codMunicipio
     * @throws Exception 
     */
    private void recargarElemento(Object obj,String codProvincia,String codMunicipio) throws Exception{
    	((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(true);
		((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(true);
		
		boolean desactivar=true;
		String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
		if (codMunicipio.length()==5){
			if (codMunicipio.equals(idMunicipio))
				desactivar=false;
		}		
		else if ((codProvincia.equals(idMunicipio.substring(0,2))) && 
				(codMunicipio.equals(idMunicipio.substring(2, 5))))
			desactivar=false;		
					
		if (desactivar){
			((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(true);
			((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(true);
			getJPanelBotonera().getJButtonModificar().setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonasignar"));
			((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
			//((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
			getJPanelBotonera().getJButtonValidar().setEnabled(false);
			getJPanelBotonera().getJButtonPublicar().setEnabled(false);
		}
		else{
			((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(true);
			((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(true);
			getJPanelBotonera().getJButtonModificar().setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonmodify"));
			if (obj instanceof WorkflowEIEL){
				if (((WorkflowEIEL)obj).isEstadoTemporal()){
					getJPanelBotonera().getJButtonPublicar().setEnabled(true);
					getJPanelBotonera().getJButtonVersionado().setEnabled(false);
					getJPanelBotonera().getJButtonInformes().setEnabled(false);
					getJPanelBotonera().getJButtonEliminar().setEnabled(true);
					//getJPanelBotonera().getJButtonEliminar().setEnabled(true);
					if (ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_VALIDADOR_EIEL))
						getJPanelBotonera().getJButtonEliminar().setEnabled(false);

				}
				else { 
					if (((WorkflowEIEL)obj).isEstadoPublicable()){
						getJPanelBotonera().getJButtonValidar().setEnabled(true);
						getJPanelBotonera().getJButtonPublicar().setEnabled(false);
						getJPanelBotonera().getJButtonVersionado().setEnabled(false);
						getJPanelBotonera().getJButtonInformes().setEnabled(false);
						getJPanelBotonera().getJButtonEliminar().setEnabled(true);
						if (ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_VALIDADOR_EIEL))
								getJPanelBotonera().getJButtonEliminar().setEnabled(false);
					}
					else { 
						if (((WorkflowEIEL)obj).isEstadoAutoPublicable()){
						getJPanelBotonera().getJButtonValidar().setEnabled(true);
						getJPanelBotonera().getJButtonPublicar().setEnabled(false);
						getJPanelBotonera().getJButtonVersionado().setEnabled(false);
						getJPanelBotonera().getJButtonInformes().setEnabled(false);
						}
						else { 
							if (((WorkflowEIEL)obj).isEstadoBorrable()){
								getJPanelBotonera().getJButtonValidar().setEnabled(false);
								getJPanelBotonera().getJButtonPublicar().setEnabled(false);
								getJPanelBotonera().getJButtonVersionado().setEnabled(false);
								getJPanelBotonera().getJButtonInformes().setEnabled(false);
								getJPanelBotonera().getJButtonModificar().setEnabled(false);
								getJPanelBotonera().getJButtonConsultar().setEnabled(false);
								getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
							}
							else{
								getJPanelBotonera().getJButtonPublicar().setEnabled(false);
								getJPanelBotonera().getJButtonValidar().setEnabled(false);
								getJPanelBotonera().getJButtonInformes().setEnabled(true);	
							}
						}
					}
				}
			}
		}
		
		
		//Tratamiento especifico de capas solo con datos graficos.
    	if (ConstantesLocalGISEIEL.capasEspeciales.containsKey(patternSelected)){
    		getJPanelBotonera().getJButtonAniadir().setEnabled(false);
			getJPanelBotonera().getJButtonModificar().setEnabled(false);
			getJPanelBotonera().getJButtonConsultar().setEnabled(false);			
			getJPanelBotonera().getJButtonVersionado().setEnabled(false);
			//getJPanelBotonera().getJButtonEliminar().setEnabled(false);
			getJPanelBotonera().getJButtonListarTabla().setEnabled(false);
			getJPanelBotonera().getJButtonFiltroInformes().setEnabled(true);
			getJPanelBotonera().getJButtonExportar().setEnabled(false);
			getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(false);
			getJPanelBotonera().getjButtonValidarMPT().setEnabled(false);
    	}
		
		
		
		String clave=InitEIEL.modelosCapas.get(patternSelected);
		desactivarDigitalizacion(clave);
		
		seleccionarFeaturesEditor(obj,clave);

	}

		
		
    
    private void seleccionarFeaturesEditor(Object obj, String clave){
		
		try {
			if (GeopistaEditorPanel.getEditor()!= null){

				Collection lstFeatures;

	    		String nombreTabla=InitEIEL.tablasAlfanumericas.get(patternSelected);    		

				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patternSelected,false,nombreTabla);
							
				//Vamos a verificar si en el caso de que te devuelve mas de un elemento estos son temporales o validos para marcar
				//solo el elemento correcto en el editor ya que hasta ahora te marcaba el temporal o el publicable y el valido
				ArrayList lstDatosTemporales=new ArrayList();       	
				ArrayList lstDatosPublicables=new ArrayList();       	
				ArrayList lstDatosValidos=new ArrayList();       	
				ArrayList lstDatosAutoPublicables=new ArrayList();
				ArrayList lstDatosBorrables=new ArrayList();       	
				Iterator it=lstFeatures.iterator();
			    while (it.hasNext()){
			    	FeatureEIELSimple feature=(FeatureEIELSimple)it.next();
			    	if (feature.getRevision_expirada().equals(Const.REVISION_TEMPORAL)){
			    		lstDatosTemporales.add(feature.getId());
			    	}
			    	else if (feature.getRevision_expirada().equals(Const.REVISION_PUBLICABLE)){
			    		lstDatosPublicables.add(feature.getId());
			    	}
			    	else if (feature.getRevision_expirada().equals(Const.REVISION_VALIDA)){
			    		lstDatosValidos.add(feature.getId());
			    	}
			    	else if (feature.getRevision_expirada().equals(Const.REVISION_PUBLICABLE_PDA)){
			    		lstDatosAutoPublicables.add(feature.getId());
			    	}
			    	else if (feature.getRevision_expirada().equals(Const.REVISION_BORRABLE)){
			    		lstDatosBorrables.add(feature.getId());
			    	}
			    }

				
				if (obj instanceof WorkflowEIEL){
					if (((WorkflowEIEL)obj).isEstadoTemporal()){
						if (lstDatosTemporales.size()>0){
							refreshFeatureSelection(lstDatosTemporales!=null?lstDatosTemporales.toArray():null, clave,Const.REVISION_TEMPORAL);
							return;
						}
					}									
					else if (((WorkflowEIEL)obj).isEstadoPublicable()){
						if (lstDatosPublicables.size()>0){
							refreshFeatureSelection(lstDatosPublicables!=null?lstDatosPublicables.toArray():null, clave,Const.REVISION_PUBLICABLE);
							return;
						}
					}			
					else if (((WorkflowEIEL)obj).isEstadoAutoPublicable()){
						if (lstDatosAutoPublicables.size()>0){
							refreshFeatureSelection(lstDatosAutoPublicables!=null?lstDatosAutoPublicables.toArray():null, clave,Const.REVISION_PUBLICABLE_PDA);
							return;
						}					
					}			
					else if (((WorkflowEIEL)obj).isEstadoBorrable() || ((WorkflowEIEL)obj).isEstadoABorrar()){
						if (lstDatosBorrables.size()>0){
							refreshFeatureSelection(lstDatosBorrables!=null?lstDatosBorrables.toArray():null, clave,Const.REVISION_BORRABLE);
							return;
						}					
					}
				}
				refreshFeatureSelection(lstDatosValidos!=null?lstDatosValidos.toArray():null, clave,Const.REVISION_VALIDA);
			}
		} catch (Exception e) {
		}
			
			
			
    }
			
			
    private void desactivarDigitalizacion(String clave){
		//System.out.println("CLAVE"+clave);
		if ((clave!=null) && 
					( clave.equals("NP") || clave.equals("P1") || clave.equals("SR") || clave.equals("N7") 
							|| clave.equals("OS")|| clave.equals("DI")||clave.equals("SI")|| clave.equals("PN") || clave.equals("PO") || 
							clave.equals("PM")|| clave.equals("PU")|| clave.equals("RB"))){
				getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
				((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
		}
	
    }
    
    
    
    public Object getSelectedElement(){

    	try {
    		String patternSelected = getJPanelTree().getPatronSelected();
    		int selectedRow = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectedRow();
    		
    		if (patternSelected != null && !patternSelected.equals("") && selectedRow>=0){

				((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    			 
    			
    			//GENERICO
    			//else{

        			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(true);
        			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(true);
    				WorkflowEIEL obj= ((EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				return obj;    			
    			//}
    				
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return null;

    }
    

    /**
     * Comprobamos si un elemento es editable
     * @return
     */
    public boolean isEditable(Object selectedElement){

    	boolean editable=true;
    	
		if (selectedElement instanceof CaptacionesEIEL){
			CaptacionesEIEL obj= (CaptacionesEIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		}    	
		else if (selectedElement instanceof Depuradora1EIEL){
			Depuradora1EIEL obj= (Depuradora1EIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
			if (!codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2)))
				editable=true;			
			else if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		}   
		else if (selectedElement instanceof VertederosEIEL){
			VertederosEIEL obj= (VertederosEIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		}    	
		else if (selectedElement instanceof DepositosEIEL){
			DepositosEIEL obj= (DepositosEIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		}   
		else if (selectedElement instanceof PuntosVertidoEIEL){
			PuntosVertidoEIEL obj= (PuntosVertidoEIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		}   
		else if (selectedElement instanceof TratamientosPotabilizacionEIEL){
			TratamientosPotabilizacionEIEL obj= (TratamientosPotabilizacionEIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		} 
		else if (selectedElement instanceof EmisariosEIEL){
			EmisariosEIEL obj= (EmisariosEIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		} 
		else if (selectedElement instanceof ColectorEIEL){
			ColectorEIEL obj= (ColectorEIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		} 
		else if (selectedElement instanceof TramosConduccionEIEL){
			TramosConduccionEIEL obj= (TramosConduccionEIEL)selectedElement;
			String codProvincia=obj.getCodINEProvincia();
			String codMunicipio=obj.getCodINEMunicipio();    	    		
    		if ((codProvincia.equals(ConstantesLocalGISEIEL.idMunicipio.substring(0,2))) && 
    				(codMunicipio.equals(ConstantesLocalGISEIEL.idMunicipio.substring(2, 5))))
    			editable=true;
    		else
    			editable=false;

		}

		return editable;

    }

    
    
    public Object getObjectByRowAndSelectedItem(int selectedRow, Object selectedObject){

    	try {
    		
    		//Verificamos antes de nada si la revision actual es la misma que la de la fila expirada. Si no
    		//es asi ya no hacemos ningun tipo de comprobacion adicional
    		if (selectedObject instanceof WorkflowEIEL){
    			long revisionActual=((WorkflowEIEL)selectedObject).getRevisionActual();
    			long revisionExpirada=((WorkflowEIEL)selectedObject).getRevisionExpirada();
				WorkflowEIEL objeiel= ((EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
				if ((objeiel.getRevisionActual()!=revisionActual) && 
						(objeiel.getRevisionExpirada()!=revisionExpirada))
						return null;
    		}
    		
    		String patternSelected = getJPanelTree().getPatronSelected();

    		if (patternSelected != null && !patternSelected.equals("")){

    			if (patternSelected.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){

    				AbastecimientoAutonomoEIEL obj= ((AbastecimientoAutonomoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				AbastecimientoAutonomoEIEL compareObject = (AbastecimientoAutonomoEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINENucleo().equals(compareObject.getCodINENucleo()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())
    										){
    					return obj;
    				} else{
    					return null;
    				}

    			}
    			//MARKED
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CAPTACIONES)){

    				CaptacionesEIEL obj= ((CaptacionesEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CaptacionesEIEL compareObject = (CaptacionesEIEL) selectedObject;
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){

    				Depuradora1EIEL obj= ((Depuradora1EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				Depuradora1EIEL compareObject = (Depuradora1EIEL) selectedObject;
    				if (obj.getClave().equals(compareObject.getClave()) &&
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){

    				Depuradora2EIEL obj= ((Depuradora2EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				Depuradora2EIEL compareObject = (Depuradora2EIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){

    				CasasConsistorialesEIEL obj= ((CasasConsistorialesEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CasasConsistorialesEIEL compareObject = (CasasConsistorialesEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}

    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){

    				CentrosCulturalesEIEL obj= ((CentrosCulturalesEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CentrosCulturalesEIEL compareObject = (CentrosCulturalesEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){

    				CentrosEnsenianzaEIEL obj= ((CentrosEnsenianzaEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CentrosEnsenianzaEIEL compareObject = (CentrosEnsenianzaEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){

    				InstalacionesDeportivasEIEL obj= ((InstalacionesDeportivasEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				InstalacionesDeportivasEIEL compareObject = (InstalacionesDeportivasEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrdenIdDeportes().equals(compareObject.getOrdenIdDeportes())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){

    				NucleosPoblacionEIEL obj= ((NucleosPoblacionEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				NucleosAbandonadosEIEL compareObject = (NucleosAbandonadosEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){

    				OtrosServMunicipalesEIEL obj= ((OtrosServMunicipalesEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				OtrosServMunicipalesEIEL compareObject = (OtrosServMunicipalesEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    						obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){

    				PadronNucleosEIEL obj= ((PadronNucleosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PadronNucleosEIEL compareObject = (PadronNucleosEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){

    				PadronMunicipiosEIEL obj= ((PadronMunicipiosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PadronMunicipiosEIEL compareObject = (PadronMunicipiosEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    						obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){

    				ParquesJardinesEIEL obj= ((ParquesJardinesEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ParquesJardinesEIEL compareObject = (ParquesJardinesEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else {
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){

    				PlaneamientoUrbanoEIEL obj= ((PlaneamientoUrbanoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PlaneamientoUrbanoEIEL compareObject = (PlaneamientoUrbanoEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) &&
    						obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    							obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){

    				PoblamientoEIEL obj= ((PoblamientoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PoblamientoEIEL compareObject = (PoblamientoEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){

    				RecogidaBasurasEIEL obj= ((RecogidaBasurasEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				RecogidaBasurasEIEL compareObject = (RecogidaBasurasEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())
    										){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){

    				CentrosSanitariosEIEL obj= ((CentrosSanitariosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CentrosSanitariosEIEL compareObject = (CentrosSanitariosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){

    				SaneamientoAutonomoEIEL obj= ((SaneamientoAutonomoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				SaneamientoAutonomoEIEL compareObject = (SaneamientoAutonomoEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINENucleo().equals(compareObject.getCodINENucleo()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){

    				ServiciosSaneamientoEIEL obj= ((ServiciosSaneamientoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ServiciosSaneamientoEIEL compareObject = (ServiciosSaneamientoEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){

    				ServiciosRecogidaBasuraEIEL obj= ((ServiciosRecogidaBasuraEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ServiciosRecogidaBasuraEIEL compareObject = (ServiciosRecogidaBasuraEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) ){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){

    				EdificiosSinUsoEIEL obj= ((EdificiosSinUsoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				EdificiosSinUsoEIEL compareObject = (EdificiosSinUsoEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TANATORIOS)){

    				TanatoriosEIEL obj= ((TanatoriosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TanatoriosEIEL compareObject = (TanatoriosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){

    				VertederosEIEL obj= ((VertederosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				VertederosEIEL compareObject = (VertederosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    								obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){

    				TramosCarreterasEIEL obj= ((CarreterasEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TramosCarreterasEIEL compareObject = (TramosCarreterasEIEL) selectedObject;

    				if (obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    						obj.getCodCarretera().equals(compareObject.getCodCarretera())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DEPOSITOS)){

    				DepositosEIEL obj= ((DepositosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				DepositosEIEL compareObject = (DepositosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
   							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
									obj.getOrdenDeposito().equals(compareObject.getOrdenDeposito())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
    			
    				PuntosVertidoEIEL obj= ((PuntosVertidoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PuntosVertidoEIEL compareObject = (PuntosVertidoEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    								obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){

    				ServiciosAbastecimientosEIEL obj= ((ServiciosAbastecimientosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ServiciosAbastecimientosEIEL compareObject = (ServiciosAbastecimientosEIEL) selectedObject;

    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){

    				CentrosAsistencialesEIEL obj= ((CentrosAsistencialesEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CentrosAsistencialesEIEL compareObject = (CentrosAsistencialesEIEL) selectedObject;

    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrdenAsistencial().equals(compareObject.getOrdenAsistencial())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CABILDO)){

    				CabildoConsejoEIEL obj= ((CabildoConsejoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CabildoConsejoEIEL compareObject = (CabildoConsejoEIEL) selectedObject;
    				
    				if (obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    						obj.getCodIsla().equals(compareObject.getCodIsla())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){

    				CementeriosEIEL obj= ((CementeriosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CementeriosEIEL compareObject = (CementeriosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){

    				EntidadesSingularesEIEL obj= ((EntidadesSingularesEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				EntidadesSingularesEIEL compareObject = (EntidadesSingularesEIEL) selectedObject;

    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){

    				NucleoEncuestado7EIEL obj= ((InfoTerminosMunicipalesEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				NucleoEncuestado7EIEL compareObject = (NucleoEncuestado7EIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){

    				IncendiosProteccionEIEL obj= ((IncendiosProteccionEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				IncendiosProteccionEIEL compareObject = (IncendiosProteccionEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){

    				LonjasMercadosEIEL obj= ((LonjasMercadosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				LonjasMercadosEIEL compareObject = (LonjasMercadosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.MATADEROS)){

    				MataderosEIEL obj= ((MataderosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				MataderosEIEL compareObject = (MataderosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){

    				TratamientosPotabilizacionEIEL obj= ((TratamientosPotabilizacionEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TratamientosPotabilizacionEIEL compareObject = (TratamientosPotabilizacionEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    								obj.getOrdenPotabilizadora().equals(compareObject.getOrdenPotabilizadora())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DISEMINADOS)){

    				DiseminadosEIEL obj= ((DiseminadosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				DiseminadosEIEL compareObject = (DiseminadosEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) ){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){

    				Encuestados1EIEL obj= ((Encuestados1EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				Encuestados1EIEL compareObject = (Encuestados1EIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){

    				Encuestados2EIEL obj= ((Encuestados2EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				Encuestados2EIEL compareObject = (Encuestados2EIEL) selectedObject;

    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
  							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
   								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
   									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    				
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){

    				NucleosAbandonadosEIEL obj= ((NucleosAbandonadosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				NucleosAbandonadosEIEL compareObject = (NucleosAbandonadosEIEL) selectedObject;

    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			//Prueba de concepto Elementos sin informacion alfanumerica.
    			//ALFANUMERICOS
    			//- EMISARIOS
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.EMISARIOS)){

    				EmisariosEIEL obj= ((EmisariosEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				EmisariosEIEL compareObject = (EmisariosEIEL) selectedObject;
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			//- TRAMOS DE CONDUCCION
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TCONDUCCION)){

    				TramosConduccionEIEL obj= ((TramosConduccionEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TramosConduccionEIEL compareObject = (TramosConduccionEIEL) selectedObject;
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getTramo_cn().equals(compareObject.getTramo_cn())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			//- COLECTORES
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TCOLECTOR)){

    				ColectorEIEL obj= ((ColectoresEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ColectorEIEL compareObject = (ColectorEIEL) selectedObject;
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return null;

    }
    	
    	public void refreshFeatureSelection(Object[] featuresId, String nameLayer, String revisionExpirada) {
    		try {
    			
    			boolean selectedFeature = false; 
    			boolean geometriaVacia=false;
    			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    			GeopistaEditorPanel.getEditor().getSelectionManager().clear();

    			GeopistaLayer geopistaLayer=LoadedLayers.getLayerOrGlobal(nameLayer);
                //GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(nameLayer);
                if (geopistaLayer!=null && geopistaLayer.getFeatureCollectionWrapper()!=null && geopistaLayer.getFeatureCollectionWrapper().getFeatures()!=null){
	                Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
	                while (allFeatures.hasNext()) {
	                    Feature feature= (Feature)allFeatures.next();
	                    for (int i=0; i<featuresId.length; i++){
	                    	Long featureID = (Long) featuresId[i];
	                        if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase(featureID.toString())){
	                        	
	                        	try {
	                        		
	                        		//Incidencia 0000096
	                        		//Si la capa que visualizamos es una TC no viene el atributo revisionExpirada. En ese caso
	                        		//visualizamos en el que exista sin comprobar revision expirada
	                        		boolean hasAttribute=true;
	                        		try{
	                        			String revisionExpiradaFeature=String.valueOf((BigDecimal)feature.getAttribute("Revision Expirada"));
	                        		}
	                        		catch (Exception e){
	                        			hasAttribute=false;
	                        		}
	                        		if (hasAttribute){
	                        		String revisionExpiradaFeature=String.valueOf((BigDecimal)feature.getAttribute("Revision Expirada"));
									if (revisionExpiradaFeature!=null && ((revisionExpiradaFeature.equals(revisionExpirada) || (revisionExpiradaFeature.equals("null"))))){
										if (((GeopistaFeature)feature).isGeometryEmpty()){
											selectedFeature = true;
											geometriaVacia=true;
										}
										else{
									    	selectedFeature = true;
									    	geometriaVacia=false;
									    	GeopistaEditorPanel.getEditor().select(geopistaLayer, feature);
										}
									    break;
									}
	                        		}
	                        		else{
	                        			if (((GeopistaFeature)feature).isGeometryEmpty()){
											selectedFeature = true;
											geometriaVacia=true;
										}
										else{
									    	selectedFeature = true;
									    	geometriaVacia=false;
									    	GeopistaEditorPanel.getEditor().select(geopistaLayer, feature);
										}
									    break;
	                        		}
									
								} catch (Exception e) {

								}
	                        }
	                    }
	                }
                }
                
                String warning = null;
                if (!selectedFeature){
                	
                	warning = I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.notfeatureselected");
                	      	
                }
                else{
                	if (geometriaVacia){
                		warning = I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.notgeometryselected");
                	}
                	else
                		warning = "";
                }
                GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().warnUser(warning);  
                
                GeopistaEditorPanel.getEditor().zoomToSelected();

    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}finally{
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

    		
    	}
    	
    	public void deselectFeatureSelection() {
    		try {
    			
    			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    			GeopistaEditorPanel.getEditor().getSelectionManager().clear();

    			
    			//GeopistaEditorPanel.getEditor().destroy()();
    			 
    			
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}finally{
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

    		
    	}
    	
    	
    	/**
    	 * Publicamos el elemento en estado temporal
    	 * 
    	 * @param featuresId
    	 * @param nameLayer
    	 */
    	public void publishFeatureSelection(Object[] featuresId, String nameLayer,int estadoAPublicar) {
    		try {
    			
    			
    			ArrayList<Feature> featuresToUpdate=new ArrayList<Feature>();
    			
    			boolean selectedFeature = false; 
    			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    			GeopistaEditorPanel.getEditor().getSelectionManager().clear();

                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(nameLayer);
                if (geopistaLayer!=null && geopistaLayer.getFeatureCollectionWrapper()!=null && geopistaLayer.getFeatureCollectionWrapper().getFeatures()!=null){
	                Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
	                while (allFeatures.hasNext()) {
	                    Feature feature= (Feature)allFeatures.next();	                    	                    
	                    	                    	                    
	                    for (int i=0; i<featuresId.length; i++){
	                    	Long featureID = (Long) featuresId[i];
	                        if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase(featureID.toString())){
	                        	
	                        	try{
	                        		String revisionExpirada=String.valueOf((BigDecimal)feature.getAttribute("Revision Expirada"));
									if ((revisionExpirada!=null) && 
											((revisionExpirada.equals(Const.REVISION_TEMPORAL)) || 
													(revisionExpirada.equals(Const.REVISION_VALIDA)) ||
													(revisionExpirada.equals(Const.REVISION_PUBLICABLE)) ||
													(revisionExpirada.equals(Const.REVISION_PUBLICABLE_PDA)) ||
													(revisionExpirada.equals(Const.REVISION_BORRABLE)))){
										
										boolean firingEvents = GeopistaEditorPanel.getEditor().getLayerManager().isFiringEvents();
										GeopistaEditorPanel.getEditor().getLayerManager().setFiringEvents(false);
										((GeopistaFeature)feature).setDirty(true);
										GeopistaEditorPanel.getEditor().getLayerManager().setFiringEvents(firingEvents);
										
										featuresToUpdate.add(feature);	                        
			                        	selectedFeature = true;
			                        	GeopistaEditorPanel.getEditor().select(geopistaLayer, feature);
									}
									/*else{
										logger.info("La feature grafica no esta disponible para publicar");
									}*/
	                        		
	                        	}
	                        	catch (Exception e){	                        		
	                        	}
	                        	
	                            break;
	                        }
	                    }
	                }
	                
	                //Indicamos que se quiere actualizar el elemento.
	                if (featuresToUpdate.size()>0){
	                	aplicacion.getBlackboard().put(Const.ESTADO_VALIDACION,estadoAPublicar);
	                	//com.geopista.util.GeopistaUtil.updateFeatures(featuresToUpdate);
	                	
	                	//Es complicado saber si se ha modificado la geometria o los atributos por lo que realizamos las dos operaciones
	                	//No tengo claro para que es necesario enviar los eventos
	                	try {
							FeatureEvent event = new FeatureEvent(featuresToUpdate,FeatureEventType.ATTRIBUTES_MODIFIED,(Layer)geopistaLayer,featuresToUpdate);
							GeopistaEditorPanel.getEditor().featuresChanged(event);
						} catch (Exception e) {
							try {
								FeatureEvent event = new FeatureEvent(featuresToUpdate,FeatureEventType.GEOMETRY_MODIFIED,(Layer)geopistaLayer,featuresToUpdate);
								GeopistaEditorPanel.getEditor().featuresChanged(event);
							} catch (Exception e1) {
							}
						}
	                	aplicacion.getBlackboard().remove(Const.ESTADO_VALIDACION);
	                	//ACLayerSLD.actualizarReglaPintado(geopistaLayer);

	                	
	                }
                }
                
                String warning = null;
                if (!selectedFeature){
                	
                	warning = I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.notfeatureselected");
                	      	
                }
                else{
                	warning = "";
                }
                GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().warnUser(warning);  
                
                GeopistaEditorPanel.getEditor().zoomToSelected();

    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}finally{
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
    	}
    	
    	/**
    	 * Borramos el elemento en estado temporal
    	 * @param featuresId
    	 * @param nameLayer
    	 */
    	public void removeFeatureSelection(Object[] featuresId, String nameLayer,int estadoAPublicar) {
    		try {
    			
    			
    			ArrayList<Feature> featuresToDelete=new ArrayList<Feature>();
    			
    			boolean selectedFeature = false; 
    			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    			GeopistaEditorPanel.getEditor().getSelectionManager().clear();

                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(nameLayer);
                if (geopistaLayer!=null && geopistaLayer.getFeatureCollectionWrapper()!=null && geopistaLayer.getFeatureCollectionWrapper().getFeatures()!=null){
	                Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
	                while (allFeatures.hasNext()) {
	                    Feature feature= (Feature)allFeatures.next();	                    	                    
	                    	                    	                    
	                    for (int i=0; i<featuresId.length; i++){
	                    	Object featureID=null;
	                    	if (featuresId[i] instanceof Integer)
	                    		 featureID = (Integer) featuresId[i];
	                    	else if (featuresId[i] instanceof Long)
	                    		 featureID = (Long) featuresId[i];
	                    	else
	                    		 featureID = (Integer) featuresId[i];
	                    	
	                        if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase(featureID.toString())){
	                        	featuresToDelete.add(feature);	                        
	                        	selectedFeature = true;
	                        	GeopistaEditorPanel.getEditor().select(geopistaLayer, feature);
	                            break;
	                        }
	                    }
	                }
	                
	                //Las actualizamos
	                if (featuresToDelete.size()>0){
	                	if (estadoAPublicar!=-1)
	                		aplicacion.getBlackboard().put(Const.ESTADO_VALIDACION,estadoAPublicar);
	                	com.geopista.util.GeopistaUtil.deleteFeatures(featuresToDelete);
	                	if (estadoAPublicar!=-1)
	                		aplicacion.getBlackboard().remove(Const.ESTADO_VALIDACION);
	                	
	                	
	                	//ES PRECISO TAMBIEN LANZAR EL EVENTO?
	                	//FeatureEvent event = new FeatureEvent(featuresToDelete,FeatureEventType.DELETED,(Layer)geopistaLayer,null);
	                	//GeopistaEditorPanel.getEditor().featuresChanged(event);
	                	
	                	
	                	//Borramos el elemento del panel , evitamos que se propague un cambio global porque ya lo
	                	//hemos borrado antes.
	                	boolean firingEvents = GeopistaEditorPanel.getEditor().getLayerManager().isFiringEvents();
	                	GeopistaEditorPanel.getEditor().getLayerManager().setFiringEvents(false);
	                	geopistaLayer.getFeatureCollectionWrapper().removeAll(featuresToDelete);
	                	GeopistaEditorPanel.getEditor().getLayerManager().setFiringEvents(firingEvents);
	                }                	
                }
                
                String warning = null;
                if (!selectedFeature){
                	
                	warning = I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.notfeatureselected");
                	      	
                }
                else{
                	warning = "";
                }
                GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().warnUser(warning);  
                
                GeopistaEditorPanel.getEditor().zoomToSelected();

    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}finally{
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
    	}
    		

 
    private BotoneraJPanel getJPanelBotonera(){
    	
    	if (jPanelBotonera == null){
    		jPanelBotonera = new BotoneraJPanel();    		
    		jPanelBotonera.setEnabled(false);
    		jPanelBotonera.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
    				botoneraJPanel_actionPerformed();
    			}
    		});

    	}
    	return jPanelBotonera;
    }
    
    private SelectorMunicipioJPanel getJPanelSelectorMunicipio(){
    	
    	if (jPanelSelectorMunicipio== null){
    		jPanelSelectorMunicipio = new SelectorMunicipioJPanel();    		
    		jPanelSelectorMunicipio.setEnabled(false);
    		jPanelSelectorMunicipio.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                	if ((e.getActionCommand()!=null) && (e.getActionCommand().equals("NUCLEO")))
                		selectorNucleoJPanel_actionPerformed();
                	else
                		selectorMunicipioJPanel_actionPerformed();
    			}
    		});

    	}
    	return jPanelSelectorMunicipio;
    }
    
    private void selectorMunicipioJPanel_actionPerformed() {
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            LCGMunicipioEIEL lcgMunicipio=(LCGMunicipioEIEL)jPanelSelectorMunicipio.getMunicipioSeleccionado();
            //System.out.println("Municipio seleccionado:"+lcgMunicipio);
            
            listarDatosTabla(false,lcgMunicipio.getIdMunicipio(),null);
            //ConstantesLocalGISEIEL.idMunicipioSeleccionado=lcgMunicipio.getIdMunicipio();
            reColorearBloqueo(getJPanelTree().getPatronSelected());
            //getJPanelBotonera().getJButtonModificar().setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonmodify"));
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(this,  "Error al listar los datos del municipio", I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"),JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
         
    }
    
    private void selectorNucleoJPanel_actionPerformed() {
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            LCGNucleoEIEL lcgNucleo=(LCGNucleoEIEL)jPanelSelectorMunicipio.getNucleoSeleccionado();
            LCGMunicipioEIEL lcgMunicipio=(LCGMunicipioEIEL)jPanelSelectorMunicipio.getMunicipioSeleccionado();
            
            int idMunicipio=0;
            if (lcgMunicipio!=null)
            	idMunicipio=lcgMunicipio.getIdMunicipio();
            //System.out.println("Municipio seleccionado:"+lcgMunicipio);
            
            listarDatosTabla(false,idMunicipio,lcgNucleo);
            //ConstantesLocalGISEIEL.idMunicipioSeleccionado=lcgMunicipio.getIdMunicipio();
            reColorearBloqueo(getJPanelTree().getPatronSelected());
            //getJPanelBotonera().getJButtonModificar().setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonmodify"));
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(this,  "Error al listar los datos", I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmapseiel"),JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
         
    }
    
    private void abrirValidateMPTDialog(){
    	ValidateMPTDialog mptDialog = new ValidateMPTDialog();
    	mptDialog.setModal(false);
		mptDialog.setVisible(true);
    }
    
    
    public void botoneraJPanel_actionPerformed(){
        try{
        	boolean abrirDialogo=true;
        	
        	final Object selectedItem = getSelectedElement();
        	
        	verificarPermisosWorkflow(selectedItem,getJPanelBotonera().getBotonPressed());
        	
            if (getJPanelBotonera().getBotonPressed() == null) return;
            
            
            boolean listarDatosTable=true;
            boolean abrirValidateMPTDialog = false;

            if (getJPanelTree().getPatronSelected() == null){
            		//Solo realizamos un tratamiento especifico para el Filtro de Informes
            	   	if (getJPanelBotonera().getBotonPressed().equals(ConstantesLocalGISEIEL.OPERACION_FILTRO_INFORMES)){
            	   		abrirDialogo(ConstantesLocalGISEIEL.PATRON_FICHA_MUNICIPAL, getJPanelBotonera().getBotonPressed(),selectedItem);
            	   		return;
            	   	}
            	    else if (getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_VALIDAR_MPT)){
            	    	abrirValidateMPTDialog = true;
                    }
            	   	else
            	   		return;
            }
        	
            ((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
            //botoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);

            if (getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_ELIMINAR)){
            	String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedItem, getJPanelTree().getPatronSelected());
            	if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

            		JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
            				+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
            		dominiosJPanel_actionPerformed();            		
            		return;

            	}
            	else{
            		bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(), true);

            		//Todo confirmacion elemento
            		if (confirm(I18N.get("LocalGISEIEL","localgiseiel.register.tag2"), 
            				I18N.get("LocalGISEIEL","localgiseiel.register.tag3"))){

            			if(getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
            				eliminarElementoEntidadesAgrupadas(selectedItem, getJPanelTree().getPatronSelected());
            			}
            			else{
            				eliminarElemento(selectedItem, getJPanelTree().getPatronSelected());
            			}
            			dominiosJPanel_actionPerformed();
            			getJPanelBotonera().setEnabled(false);
            			getJPanelBotonera().getJButtonAniadir().setEnabled(true);
            			getJPanelBotonera().getJButtonListarTabla().setEnabled(true);
            			getJPanelBotonera().getJButtonFiltroInformes().setEnabled(true);
            			getJPanelBotonera().getJButtonExportar().setEnabled(true);
            			getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(true);
    
            			String layerName=LoadedLayers.getName(getJPanelTree().getPatronSelected());	
   	        		 	GeopistaLayer geopistaLayer=LoadedLayers.getLayerOrGlobal(layerName);	
   	        		 	new ReloadLayerPlugIn().loadFeatures(null,geopistaLayer, GeopistaEditorPanel.getEditor().getLayerViewPanel(),false);
            			//bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
            		}
            	}
            	return;
            }
            else if (getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_DIGITALIZAR)){
            	
            	Hashtable fields = new Hashtable();
            	fields = getFields(getJPanelTree().getPatronSelected());
            	for (Enumeration iterator = fields.keys(); iterator.hasMoreElements();) {
					String key = (String) iterator.nextElement();
					Object value=fields.get(key);
					if(key.equals("clave"))
						aplicacion.getBlackboard().put(ConstantesEIEL.KEY_CLAVE,value);
					else if(key.equals("codprov"))
						aplicacion.getBlackboard().put(ConstantesEIEL.KEY_COD_PROV,value);
					else if(key.equals("codmunic"))
							aplicacion.getBlackboard().put(ConstantesEIEL.KEY_COD_MUNIC,value);
					else if(key.startsWith("orden"))
						aplicacion.getBlackboard().put(ConstantesEIEL.KEY_COD_ORDEN,value);
					else if(key.equals("tramo_cl"))
						aplicacion.getBlackboard().put(ConstantesEIEL.KEY_COD_ORDEN,value);
					else if(key.equals("tramo_cn"))
						aplicacion.getBlackboard().put(ConstantesEIEL.KEY_COD_ORDEN,value);
					else if(key.equals("codentidad"))
						aplicacion.getBlackboard().put(ConstantesEIEL.KEY_COD_ENTIDAD,value);
					else if(key.equals("codpoblamiento"))
						aplicacion.getBlackboard().put(ConstantesEIEL.KEY_COD_POBLAMIENTO,value);
				} 
            	getJPanelBotonera().getJButtonDesactivar().setEnabled(true);
            	GeopistaEditor editor=null;
            	if (AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor") instanceof GeopistaEditorPanel)
            		editor = ((GeopistaEditorPanel)AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
            	else if (AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor") instanceof GeopistaMapPanel)
            		editor = ((GeopistaMapPanel)AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
            
            	
            	GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (editor.getContext().getBlackboard().get(EditingPlugIn.KEY));
    			ToolboxDialog toolbox = geopistaEditingPlugIn.getToolbox(editor.getContext());
    			
    			toolbox.setVisible(true);	
            	
            }
            else if (getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_DESACTIVAR)){
            	
            	Layer layer = selectLayerInLayerManager(getJPanelTree().getPatronSelected(), false);
            	getJPanelBotonera().getJButtonDigitalizar().setEnabled(true);
            	((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(true);
            	getJPanelBotonera().getJButtonDesactivar().setEnabled(false);
            	
            	GeopistaEditor editor = ((GeopistaEditorPanel)AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor")).getEditor();
            	            	
            	GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (editor.getContext().getBlackboard().get(EditingPlugIn.KEY));
    			ToolboxDialog toolbox = geopistaEditingPlugIn.getToolbox(editor.getContext());
    			
    			toolbox.setVisible(false);	
            }
            else if (getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_FILTRAR_REFERENCIADOS)){
            	abrirDialogo=false;	
            }
            else if (getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_PUBLICAR)){
            	
            	LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
           	 	modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_A_PUBLICAR);
           	 	abrirDialogo=false;	      
            }
            else if (getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_VALIDAR)){
            	LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());            
            	modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),-1);
           	 	abrirDialogo=false;	
            }
            else if (abrirValidateMPTDialog ||getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_VALIDAR_MPT)){
            	abrirValidateMPTDialog();	
            	abrirDialogo=false;	
            }

            cancelado=false;
            if (abrirDialogo){
        	
        	        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
        	        progressDialog.setTitle(I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag9"));
        	        progressDialog.report(I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag10")+":"+getJPanelTree().getPatronSelected());
        	       
        	        progressDialog.addComponentListener(new ComponentAdapter(){
        	            public void componentShown(ComponentEvent e){
        	                new Thread(new Runnable(){
        	                    public void run(){
        	                    	currentDialog=null;
        	                    	boolean datosCorrectos=false;
        	                    	try{
	        	                    	while (!datosCorrectos){
		        	                        try{
		        	                        	cancelado=abrirDialogo(getJPanelTree().getPatronSelected(), getJPanelBotonera().getBotonPressed(),selectedItem);
		        	                        	datosCorrectos=true;
		        	                        }
		        	                        catch (LayerNotFoundException e1){
		        	                        	JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
		        	                        			I18N.get("LocalGISEIEL","localgiseiel.capagrafica.nocargada"));
		        	                        	datosCorrectos=true;
		        	                        	
		        	                        }
		        	                        catch(Exception e){   
		        	                        	  String error=getColumnError(e.getCause().toString());
		        	                        	  ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
		          	                            		I18N.get("LocalGISEIEL","localgiseiel.sqlerror.title"), 
		          	                            		I18N.get("LocalGISEIEL","localgiseiel.sqlerror.warning")+error, 
		          	                            		StringUtil.stackTrace(e),180);
		        	                        	  datosCorrectos=false;
		        	                          
		        	                            //return;
		        	                        }
	        	                    	}
        	                    	}
	        	                        finally{
	        	                            progressDialog.setVisible(false);
	        	                        }        	                    	
        	                        
        	                    }
        	              }).start();
        	          }
        	       });
        	       GUIUtil.centreOnWindow(progressDialog);
        	       progressDialog.setResizable(false);
        	       progressDialog.setVisible(true);
        	       

        	    }
            	            	            
            
            if (!cancelado){
	            LCGMunicipioEIEL lcgMunicipio=(LCGMunicipioEIEL)jPanelSelectorMunicipio.getMunicipioSeleccionado();
	            if (lcgMunicipio!=null)
	            	listarDatosTabla(true,lcgMunicipio.getIdMunicipio(),null);
	            else
	            	listarDatosTabla(true,0,null);
	            reColorearBloqueo(getJPanelTree().getPatronSelected());
            }

            if (selectedItem != null){
            	setSeletecdRowByItem(selectedItem);
            }

           
        }
        catch (LayerNotFoundException e1){
        	JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
        			I18N.get("LocalGISEIEL","localgiseiel.capagrafica.nocargada"));
        	
        }
        catch (Exception e){
            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
            		I18N.get("LocalGISEIEL","localgiseiel.sqlerror.title"), 
            		I18N.get("LocalGISEIEL","localgiseiel.sqlerror.warning"), StringUtil.stackTrace(e));
            e.printStackTrace();
        }

        getJPanelBotonera().setBotonPressed(null);

    }
    
    private String getColumnError(String message){
    	try {
			if (message.contains("viola la restric")){
				int indiceInicial=message.indexOf("«");
				int indiceFinal=message.indexOf("»");
				String campo=message.substring(indiceInicial+1,indiceFinal);
				campo=campo.replaceAll("\n", "");
				return "\nEl campo \""+campo+"\" no puede tomar valores nulos";    	
			}
			else
				return "";
		} catch (Exception e) {
			return "";
		}
    }    
    
    private void verificarPermisosWorkflow(Object selectedItem,String tipoOperacion){
    	if  (selectedItem instanceof WorkflowEIEL){
    		WorkflowEIEL selectedElement=(WorkflowEIEL)selectedItem;
    		//Si el usario tiene permiso de publicacion en eiel todas las inserciones que realize
    		//seran temporales.
    		
    		if ((tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)) ||
    			(tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ELIMINAR))||
    			(tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR))||
    			(tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_DIGITALIZAR))){
	    		if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
	    			if (selectedElement.isEstadoValido()){
	    				selectedElement.setEstadoValidacion(ConstantesLocalGISEIEL.ESTADO_TEMPORAL);
	    			}
	    			else if (selectedElement.isEstadoPublicable()){
	    				selectedElement.setEstadoValidacion(ConstantesLocalGISEIEL.ESTADO_TEMPORAL);
	    			}
	    		}
    		}

    	}
    }
    
    public void dialog_actionPerformed(FichasFilterDialog dialog){
        dialog.setVisible(false);
    }
    
    private void setSeletecdRowByItem(Object selectedItem) {
    	try{
    		JTable table = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos();
    		String patternSelected = getJPanelTree().getPatronSelected();
    		for(int i=0; i< table.getRowCount(); i++ ){   			
    			if (getObjectByRowAndSelectedItem(i,selectedItem) != null){
    				((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectionModel().setSelectionInterval(i, i);
    				//return;
    				break;
    			}
    		}
    		
        	//Si esta seleccionado un elemento hay que hacer un tratamiento especial de seleccion
        	int selectedRow = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectedRow();
        	if (selectedRow != -1){
        		try{
    	    		Object obj= ((EIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    	    		if (obj instanceof WorkflowEIEL){
    					if (((WorkflowEIEL)obj).isEstadoTemporal()){
    						getJPanelBotonera().getJButtonPublicar().setEnabled(true);
    						getJPanelBotonera().getJButtonVersionado().setEnabled(false);
    						getJPanelBotonera().getJButtonInformes().setEnabled(false);
    					}
    					else if (((WorkflowEIEL)obj).isEstadoPublicable()){
    						getJPanelBotonera().getJButtonValidar().setEnabled(true);
    						getJPanelBotonera().getJButtonVersionado().setEnabled(false);
    						getJPanelBotonera().getJButtonInformes().setEnabled(false);
    						getJPanelBotonera().getJButtonPublicar().setEnabled(false);
    					}
    					else if (((WorkflowEIEL)obj).isEstadoAutoPublicable()){
    						getJPanelBotonera().getJButtonValidar().setEnabled(true);
    						getJPanelBotonera().getJButtonVersionado().setEnabled(false);
    						getJPanelBotonera().getJButtonInformes().setEnabled(false);
    						getJPanelBotonera().getJButtonPublicar().setEnabled(false);
    					}
    					else{
    						getJPanelBotonera().getJButtonPublicar().setEnabled(false);
    						getJPanelBotonera().getJButtonValidar().setEnabled(false);
    						getJPanelBotonera().getJButtonVersionado().setEnabled(true);
    						getJPanelBotonera().getJButtonInformes().setEnabled(true);
    					}
    				}
        		}
        		catch (Exception e){
        			//e.printStackTrace();
        		}
        	}
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	private Hashtable getFields(String patron){
    	
    	Hashtable fields = new Hashtable();

    	try {
    		
    		Object selectedItem = getSelectedElement();
    		if (selectedItem instanceof EIELPanel){
    		EIELPanel selectedElement = (EIELPanel) getSelectedElement();

    		if (selectedElement != null){
    			fields = selectedElement.getIdentifyFields();
    			Layer layer = selectLayerInLayerManager(patron, true);
    			if (layer != null){

    				GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) layer.getDataSourceQuery().getDataSource();
    				Map driverProperties = geopistaServerDataSource.getProperties();
//    		TODO:		driverProperties.put(GeopistaConnection.SELECTED_FIELDS, fields);
//    				driverProperties.put(GeopistaConnection.SELECTED_LAYER, ((GeopistaLayer)layer).getSystemId());

	    			}
    			}
    		}
    		else
    			new Hashtable();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	return fields;
    }
    
	/**
	 * Selecciona la capa e
	 * @param patron
	 * @param enable
	 * @return
	 */
    private Layer selectLayerInLayerManager(String patron, boolean enable){
    	    	    	
    	if (patron != null){
    		
    		GeopistaLayer geopistaLayer = null;
    		
    		
    		geopistaLayer=LoadedLayers.getLayer(patron);
    		
    		

    		if (geopistaLayer != null){
    			Layerable[] layerable = new Layerable[1];
    			layerable[0] = geopistaLayer;    			
//    			GeopistaEditorPanel.getEditor().getLayerNamePanel().setTargetSelectedLayers(layerable);
    			GeopistaEditorPanel.getEditor().getLayerNamePanel().selectLayerables(layerable);
    			GeopistaEditorPanel.getEditor().updateUI();
    			geopistaLayer.setEditable(enable);
                GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().setStatusMessage("");
    			return geopistaLayer;
    		}
    		else{
                GeopistaEditorPanel.getEditor().getLayerViewPanel().getContext().setStatusMessage("No existe una capa grafica asociada al elemento:"+patron);

    		}

    	}
    	
    	return null;
    }
    
    private void updateFeatures(Object[] listafeatures){
    	
    	if (listafeatures!= null && listafeatures.length>0){

    		ArrayList lstLayers = (ArrayList) GeopistaEditorPanel.getEditor().getLayerManager().getLayers();
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
    			GeopistaEditorPanel.getEditor().featuresChanged(event);
    		}
    	}
    }
    
    private void eliminarElementoEntidadesAgrupadas(Object object, String tipoElemento){
    	
    	Collection lstFeatures = null;

    	try {
    		
    		//Borramos el elemento
    		if  (object instanceof WorkflowEIEL){
    			
        		//String clave=ConstantesLocalGISEIEL.modelosCapas.get(patternSelected);        		
    			//lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(clave);
    		//	String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			
    			
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, null, tipoElemento);
    			
    			if (object instanceof WorkflowEIEL){
    				if (((WorkflowEIEL)object).isEstadoTemporal()){
    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, null,Const.ESTADO_TEMPORAL);
    				}
    				else if (((WorkflowEIEL)object).isEstadoPublicable()){
    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, null,Const.ESTADO_PUBLICABLE);
    				}
    				else if (((WorkflowEIEL)object).isEstadoAutoPublicable()){
    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, null,Const.ESTADO_PUBLICABLE_MOVILIDAD);
    				}
    				else{
    					//TODO 
    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, null,-1);
    					//¿Borramos el elemento ?

    				}
    			}
    			
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
 	}
    
    private void eliminarElemento(Object object, String tipoElemento){
    	
    	Collection lstFeatures = null;

    	try {
    		
    		//Borramos el elemento
    		if  (object instanceof WorkflowEIEL){
    			
        		String clave=InitEIEL.modelosCapas.get(patternSelected);        		
        		String nombreTabla=InitEIEL.tablasAlfanumericas.get(patternSelected); 
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento,false,nombreTabla);
    			
    			ArrayList lstDatosBorrables=new ArrayList();
    			Iterator it=lstFeatures.iterator();
			    while (it.hasNext()){
			    	FeatureEIELSimple feature=(FeatureEIELSimple)it.next();
			    	if (feature.getRevision_expirada().equals(Const.REVISION_TEMPORAL) ||
			    			feature.getRevision_expirada().equals(Const.REVISION_PUBLICABLE) ||
			    			feature.getRevision_expirada().equals(Const.REVISION_VALIDA) ||
			    			feature.getRevision_expirada().equals(Const.REVISION_PUBLICABLE_PDA) ||
			    					feature.getRevision_expirada().equals(Const.REVISION_BORRABLE))
			    		lstDatosBorrables.add(feature.getId());
			    	}	    
    			
			    
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(clave);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			
    			
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    			
    			if (object instanceof WorkflowEIEL){
    				if (((WorkflowEIEL)object).isEstadoABorrar()){
    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,Const.ESTADO_A_BORRAR);
    				}
    				else{
	    				if (((WorkflowEIEL)object).isEstadoTemporal()){
	    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,Const.ESTADO_TEMPORAL);
	    				}
	    				else if (((WorkflowEIEL)object).isEstadoPublicable()){
	    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,Const.ESTADO_PUBLICABLE);
	    				}
	    				else if (((WorkflowEIEL)object).isEstadoAutoPublicable()){
	    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,Const.ESTADO_PUBLICABLE_MOVILIDAD);
	    				}
	    				else{
	    					//TODO 
	    					removeFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,Const.ESTADO_VALIDO);
	    					//¿Borramos el elemento ?
	
	    				}
    				}
    			}
    			
    		}
    		/*
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){

    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		//MARKED	
    		}else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CAPTACIONES)){
    			
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		}else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPURADORAS1)){
    			
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		}else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPURADORAS2)){
    			
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){
    			
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){
    			
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){
    			
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		}
    		
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){
    			
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){
    			
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

    		}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PARQUES_JARDINES)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.POBLAMIENTO)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TANATORIOS)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TANATORIO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.VERTEDERO_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}			
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){
				
				lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CARRETERA_LAYER);
				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
				InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
			
			}
			else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPOSITOS)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CABILDO)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PROVINCIA_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CEMENTERIOS)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CEMENTERIOS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.MATADEROS)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MATADEROS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DISEMINADOS)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.ENCUESTADOS1)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.ENCUESTADOS2)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		//Prueba de concepto Elementos sin informacion alfanumerica.
    		//ALFANUMERICOS
    		//- EMISARIOS
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.EMISARIOS)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}

    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TCOLECTOR)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.COLECTOR_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}
    		else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TCONDUCCION)){
    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
    			String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    			InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
    		}*/
    		
    		if (lstFeatures!=null)
    			updateFeatures(lstFeatures.toArray());

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void bloquearElemento(final Object elemento, final String tipoElemento, final boolean bloquear){
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
        progressDialog.setTitle(I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag3"));
        progressDialog.report(I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag4"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        try{
                            InitEIEL.clienteLocalGISEIEL.bloquearElemento(elemento, bloquear, tipoElemento);
                        }
                        catch(Exception e){                            
                            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
                            		I18N.get("LocalGISEIEL","localgiseiel.sqlerror.title"), 
                            		I18N.get("LocalGISEIEL","localgiseiel.sqlerror.warning"), StringUtil.stackTrace(e));
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
     * Publicacion del elemento para su validacion
     * @param elemento
     * @param tipoElemento
     * @param nuevoEstado 
     */
    private void modificarElementoGrafico(final Object elemento, final String tipoElemento, final int nuevoEstado){
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
        progressDialog.setTitle(I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag5"));
        progressDialog.report(I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag6"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        try{
                        	
                       		
                        	String clave=InitEIEL.modelosCapas.get(patternSelected);
                        	
                        	String nombreTabla=InitEIEL.tablasAlfanumericas.get(patternSelected);
                        	
                        	Collection lstFeatures=null;
                        	if (GeopistaEditorPanel.getEditor()!= null){                   			
                    			lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(elemento, patternSelected,true,nombreTabla); 
                        	}
                        	
                        	switch(nuevoEstado){
                        		case Const.ESTADO_A_PUBLICAR:
                        			if (!ConstantesLocalGISEIEL.capasEspeciales.containsKey(patternSelected)){
                        				InitEIEL.clienteLocalGISEIEL.publicarElemento(elemento, tipoElemento);
                        				logger.info("Elemento alfanumerico publicado correctamente");
                        			}
                        			if (lstFeatures!=null){
                            			publishFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,nuevoEstado);
                            			logger.info("Elemento grafico publicado correctamente como publicable");
                        			}
                        			break;
                        		case Const.ESTADO_TEMPORAL:
                        			if (lstFeatures!=null){
                            			publishFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,nuevoEstado);
                            			logger.info("Elemento grafico publicado correctamente como temporal");
                        			}
                        			break;
                        		default:
                        			if (!ConstantesLocalGISEIEL.capasEspeciales.containsKey(patternSelected)){
	                        			InitEIEL.clienteLocalGISEIEL.validarElemento(elemento, tipoElemento);                                    
	                                    logger.info("Elemento alfanumerico validado correctamente");
                        			}
	                                if (lstFeatures!=null){
	                                    if(elemento instanceof WorkflowEIEL){
	                        				WorkflowEIEL elementoWorkflow = (WorkflowEIEL) elemento;
	                        				if(elementoWorkflow.isEstadoAutoPublicable()){
	                        					elementoWorkflow.setEstadoValidacionAnterior(Const.ESTADO_PUBLICABLE_MOVILIDAD);
	                        					elementoWorkflow.setEstadoValidacion(Const.ESTADO_PUBLICABLE);
	                        					publishFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,Const.ESTADO_PUBLICABLE_MOVILIDAD);
	                        				}                    			
	                        				else                    			
	                        					publishFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, clave,Const.ESTADO_PUBLICABLE);
	                        				logger.info("Elemento grafico validado correctamente");
	                        			}
                                    }
                        			break;
                        	
                        	}
                           
                        }
                        catch (LayerNotFoundException e1){
                        	JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), 
                        			I18N.get("LocalGISEIEL","localgiseiel.capagrafica.nocargada"));
                        	
                        }
                        catch(Exception e){                            
                            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
                            		I18N.get("LocalGISEIEL","localgiseiel.sqlerror.title"), 
                            		I18N.get("LocalGISEIEL","localgiseiel.sqlerror.warning"), StringUtil.stackTrace(e));
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
     * Abrimos el dialogo para la insercion de elementos.
     * @param selectedPattern
     * @param tipoOperacion
     * @param selectedItem
     * @return
     * @throws Exception
     */
    private boolean abrirDialogo(String selectedPattern, String tipoOperacion, Object selectedItem) throws Exception{
    	
    	boolean cancelado=true;
    	boolean editable = true;
    	if(tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR))
    		editable = false;
    		
    	if(tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_FILTRO_INFORMES)){

    		//Revisamos si hay seleccionado un elemento en el panel o si el usuario ha
    		//seleccionado features en el mapa. Con esto generamos un sql
    		boolean disponeFiltro=true;
    		HashMap<String,String> listaFiltros=new HashMap<String,String>();
    		if (!selectedPattern.equals(ConstantesLocalGISEIEL.PATRON_FICHA_MUNICIPAL)){
	    		//Object elementoSeleccionado=getSelectedElement();
    			    			
	    		try {
					listaFiltros=new FiltroByFeature().generateSQLFilterFeaturesSeleccionadas(selectedPattern,selectedItem,locale);
				} catch (NoFilterException e1) {
					disponeFiltro=false;
				}
    		}	    		
	    		
	    		//Le pasamos las tres posibilidades de filtro que hay
	    		//1. Que el usuario haya seleccionado solo el epigrafe
	    		//2. Que el usuario haya seleccionado un elemento en el listado de elemento de un epigrafe
	    		//3. Que el usario seleccionad una zona del mapa en la que hay features.
	    		FichasFilterDialog dialog = new FichasFilterDialog(aplicacion.getMainFrame(),selectedPattern,listaFiltros,locale,disponeFiltro);
				dialog.addActionListener(new java.awt.event.ActionListener(){
		            public void actionPerformed(ActionEvent e){
		            	dialog_actionPerformed((FichasFilterDialog)e.getSource());
		            }
		        });
				dialog.setVisible(true);
				
    	}
    	
    	if(tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_EXPORTAR)){
    		ExcelExporter exporter=new ExcelExporter();
    		JTable table=((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos();
    		exporter.exportTable(table,this);
    		//Object tableModel=(Object)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel();
    	}

    	if (selectedPattern.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//AbastecimientoAutonomoDialog dialog = new AbastecimientoAutonomoDialog(true);
    			
    			AbastecimientoAutonomoDialog dialog;
    			if (currentDialog!=null){
    				dialog=(AbastecimientoAutonomoDialog)currentDialog;
    				dialog.getAbastecimientoAutonomoPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new AbastecimientoAutonomoDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getAbastecimientoAutonomoPanel().getOkPressed()){

    				AbastecimientoAutonomoEIEL abst = dialog.getAbastecimientoAutonomo(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);

    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (selectedItem!= null && selectedItem instanceof AbastecimientoAutonomoEIEL){

    				AbastecimientoAutonomoEIEL selectedElement = (AbastecimientoAutonomoEIEL) selectedItem;
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					AbastecimientoAutonomoDialog dialog = new AbastecimientoAutonomoDialog(selectedElement,false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					AbastecimientoAutonomoDialog dialog = new AbastecimientoAutonomoDialog(selectedElement, editable);
    					dialog.setVisible(true);  

    					if (dialog.getAbastecimientoAutonomoPanel().getOkPressed()){

    						AbastecimientoAutonomoEIEL abst = null;    					
    						abst = dialog.getAbastecimientoAutonomo(selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.au"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof AbastecimientoAutonomoEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							selectedItem);
					dialog.setVisible(true);
				}                
            }    		
    	}
    	//MARKED
    	//TRATAMIENTO ESPECIAL
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.CAPTACIONES)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//CaptacionesDialog dialog = new CaptacionesDialog(true);
    			
    			CaptacionesDialog dialog;
    			if (currentDialog!=null){
    				dialog=(CaptacionesDialog)currentDialog;
    				dialog.getCaptacionesPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new CaptacionesDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getCaptacionesPanel().getOkPressed()){

    				CaptacionesEIEL abst = dialog.getCaptacion(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
    				String idLayer=null;
    				if (geopistaLayer!=null)
    					idLayer= geopistaLayer.getSystemId();
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (selectedItem!= null && selectedItem instanceof CaptacionesEIEL){

    				CaptacionesEIEL selectedElement = (CaptacionesEIEL) selectedItem;
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					CaptacionesDialog dialog = new CaptacionesDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(), true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					CaptacionesDialog dialog = new CaptacionesDialog(selectedElement, editable);
    					dialog.setVisible(true);  

    					if (dialog.getCaptacionesPanel().getOkPressed()){
    						CaptacionesEIEL abst =  dialog.getCaptacion((CaptacionesEIEL) selectedElement);

    						String idLayer=null;
							try {
	    						GeopistaLayer geopistaLayer=null;
	    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
	    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
	    						else
	    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
								//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
								idLayer = geopistaLayer.getSystemId();
							} catch (Exception e) {
							}
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CAPTACIONES_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.CAPTACIONES, I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ca"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof CaptacionesEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(selectedItem);
					dialog.setVisible(true);

				}           
            }
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//Depuradoras1Dialog dialog = new Depuradoras1Dialog(true);
    			
    			Depuradoras1Dialog dialog;
    			if (currentDialog!=null){
    				dialog=(Depuradoras1Dialog)currentDialog;
    				dialog.getDepuradoras1Panel().okPressed=false;
    			}
    				
    			else{
    				dialog = new Depuradoras1Dialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getDepuradoras1Panel().getOkPressed()){

    				Depuradora1EIEL abst = dialog.getDepuradora1(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof Depuradora1EIEL){
    				Depuradora1EIEL selectedElement = (Depuradora1EIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					Depuradoras1Dialog dialog = new Depuradoras1Dialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					Depuradoras1Dialog dialog = new Depuradoras1Dialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getDepuradoras1Panel().getOkPressed()){
    						Depuradora1EIEL abst =  dialog.getDepuradora1((Depuradora1EIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.DEPURADORAS1_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.DEPURADORAS1, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.d1"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof Depuradora1EIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}                   
            }
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//Depuradoras2Dialog dialog = new Depuradoras2Dialog(true);
    			
    			Depuradoras2Dialog dialog;
    			if (currentDialog!=null){
    				dialog=(Depuradoras2Dialog)currentDialog;
    				dialog.getDepuradoras2Panel().okPressed=false;
    			}
    				
    			else{
    				dialog = new Depuradoras2Dialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getDepuradoras2Panel().getOkPressed()){

    				Depuradora2EIEL abst = dialog.getDepuradora2(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof Depuradora2EIEL){
    				Depuradora2EIEL selectedElement = (Depuradora2EIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					Depuradoras2Dialog dialog = new Depuradoras2Dialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					Depuradoras2Dialog dialog = new Depuradoras2Dialog(selectedElement, editable);
    					dialog.setVisible(true);  

    					if (dialog.getDepuradoras2Panel().getOkPressed()){
    						Depuradora2EIEL abst = dialog.getDepuradora2((Depuradora2EIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.DEPURADORAS2_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.DEPURADORAS2, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.d2"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
    			
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof Depuradora2EIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}                  
            }
    	}       
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//CasaConsistorialDialog dialog = new CasaConsistorialDialog(true);
    			
    			CasaConsistorialDialog dialog;
    			if (currentDialog!=null){
    				dialog=(CasaConsistorialDialog)currentDialog;
    				dialog.getCasaConsistorialPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new CasaConsistorialDialog(true);
    				currentDialog=dialog;
    			}
    			
    			dialog.setVisible(true);
    			if (dialog.getCasaConsistorialPanel().getOkPressed()){

    				CasasConsistorialesEIEL abst = dialog.getCasaConsistorial(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof CasasConsistorialesEIEL){
    				CasasConsistorialesEIEL selectedElement = (CasasConsistorialesEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					CasaConsistorialDialog dialog = new CasaConsistorialDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					CasaConsistorialDialog dialog = new CasaConsistorialDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getCasaConsistorialPanel().getOkPressed()){
    						CasasConsistorialesEIEL abst = dialog.getCasaConsistorial((CasasConsistorialesEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.CASAS_CONSISTORIALES, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.cc"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
    			
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof CasasConsistorialesEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
     	}       
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//CentroCulturalDialog dialog = new CentroCulturalDialog(true);
    			CentroCulturalDialog dialog;
    			if (currentDialog!=null){
    				dialog=(CentroCulturalDialog)currentDialog;
    				dialog.getCentroCulturalPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new CentroCulturalDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getCentroCulturalPanel().getOkPressed()){

    				CentrosCulturalesEIEL abst = dialog.getCentroCultural(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof CentrosCulturalesEIEL){
    				CentrosCulturalesEIEL selectedElement = (CentrosCulturalesEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					CentroCulturalDialog dialog = new CentroCulturalDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					CentroCulturalDialog dialog = new CentroCulturalDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getCentroCulturalPanel().getOkPressed()){
    						CentrosCulturalesEIEL abst = dialog.getCentroCultural((CentrosCulturalesEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CENTROS_CULTURALES_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.CENTROS_CULTURALES, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.cu"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof CentrosCulturalesEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }    		
    		
    	}      
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//CentroEnsenianzaDialog dialog = new CentroEnsenianzaDialog(true);
    			CentroEnsenianzaDialog dialog;
    			if (currentDialog!=null){
    				dialog=(CentroEnsenianzaDialog)currentDialog;
    				dialog.getCentroEnsenianzaPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new CentroEnsenianzaDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getCentroEnsenianzaPanel().getOkPressed()){

    				CentrosEnsenianzaEIEL abst = dialog.getCentroEnsenianza(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof CentrosEnsenianzaEIEL){
    				CentrosEnsenianzaEIEL selectedElement = (CentrosEnsenianzaEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					CentroEnsenianzaDialog dialog = new CentroEnsenianzaDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					CentroEnsenianzaDialog dialog = new CentroEnsenianzaDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getCentroEnsenianzaPanel().getOkPressed()){
    						CentrosEnsenianzaEIEL abst = dialog.getCentroEnsenianza((CentrosEnsenianzaEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.CENTROS_ENSENIANZA, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.en"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof CentrosEnsenianzaEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    		
    	}      
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//InstalacionDeportivaDialog dialog = new InstalacionDeportivaDialog(true);
    			InstalacionDeportivaDialog dialog;
    			if (currentDialog!=null){
    				dialog=(InstalacionDeportivaDialog)currentDialog;
    				dialog.getInstalacionDeportivaPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new InstalacionDeportivaDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getInstalacionDeportivaPanel().getOkPressed()){

    				InstalacionesDeportivasEIEL abst = dialog.getInstalacionDeportiva(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof InstalacionesDeportivasEIEL){
    				InstalacionesDeportivasEIEL selectedElement = (InstalacionesDeportivasEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					InstalacionDeportivaDialog dialog = new InstalacionDeportivaDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					InstalacionDeportivaDialog dialog = new InstalacionDeportivaDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getInstalacionDeportivaPanel().getOkPressed()){
    						InstalacionesDeportivasEIEL abst = dialog.getInstalacionDeportiva((InstalacionesDeportivasEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.id"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof InstalacionesDeportivasEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    		
    	}    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//NucleosPoblacionDialog dialog = new NucleosPoblacionDialog(true);
    			NucleosPoblacionDialog dialog;
    			if (currentDialog!=null){
    				dialog=(NucleosPoblacionDialog)currentDialog;
    				dialog.getNucleosPoblacionPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new NucleosPoblacionDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getNucleosPoblacionPanel().getOkPressed()){

    				NucleosPoblacionEIEL abst = dialog.getNucleosPoblacion(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof NucleosPoblacionEIEL){
    				NucleosPoblacionEIEL selectedElement = (NucleosPoblacionEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					NucleosPoblacionDialog dialog = new NucleosPoblacionDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					NucleosPoblacionDialog dialog = new NucleosPoblacionDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getNucleosPoblacionPanel().getOkPressed()){
    						NucleosPoblacionEIEL abst = dialog.getNucleosPoblacion((NucleosPoblacionEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.NUCLEOS_POBLACION_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.NUCLEOS_POBLACION, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.np"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof NucleosPoblacionEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }  
     	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//OtrosServMunicipalesDialog dialog = new OtrosServMunicipalesDialog(true);
    			OtrosServMunicipalesDialog dialog;
    			if (currentDialog!=null){
    				dialog=(OtrosServMunicipalesDialog)currentDialog;
    				dialog.getOtrosServMunicipalesPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new OtrosServMunicipalesDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getOtrosServMunicipalesPanel().getOkPressed()){

    				OtrosServMunicipalesEIEL abst = dialog.getOtrosServMunicipales(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof OtrosServMunicipalesEIEL){
    				OtrosServMunicipalesEIEL selectedElement = (OtrosServMunicipalesEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					OtrosServMunicipalesDialog dialog = new OtrosServMunicipalesDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					OtrosServMunicipalesDialog dialog = new OtrosServMunicipalesDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getOtrosServMunicipalesPanel().getOkPressed()){
    						OtrosServMunicipalesEIEL abst = dialog.getOtrosServMunicipales((OtrosServMunicipalesEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES, I18N.get(
                                "LocalGISEIEL", "localgiseiel.dialog.titulo.ot"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof OtrosServMunicipalesEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    	}    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//PadronNucleosDialog dialog = new PadronNucleosDialog(true);
    			
    			PadronNucleosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(PadronNucleosDialog)currentDialog;
    				dialog.getPadronNucleosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new PadronNucleosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getPadronNucleosPanel().getOkPressed()){

    				PadronNucleosEIEL abst = dialog.getPadronNucleos(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof PadronNucleosEIEL){
    				PadronNucleosEIEL selectedElement = (PadronNucleosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					PadronNucleosDialog dialog = new PadronNucleosDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					PadronNucleosDialog dialog = new PadronNucleosDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getPadronNucleosPanel().getOkPressed()){
    						PadronNucleosEIEL abst = dialog.getPadronNucleos((PadronNucleosEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.PADRON_NUCLEOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.PADRON_NUCLEOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.pn"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof PadronNucleosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    		
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//PadronMunicipiosDialog dialog = new PadronMunicipiosDialog(true);
    			PadronMunicipiosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(PadronMunicipiosDialog)currentDialog;
    				dialog.getPadronMunicipiosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new PadronMunicipiosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getPadronMunicipiosPanel().getOkPressed()){

    				PadronMunicipiosEIEL abst = dialog.getPadronMunicipios(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof PadronMunicipiosEIEL){
    				PadronMunicipiosEIEL selectedElement = (PadronMunicipiosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					PadronMunicipiosDialog dialog = new PadronMunicipiosDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					PadronMunicipiosDialog dialog = new PadronMunicipiosDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getPadronMunicipiosPanel().getOkPressed()){
    						PadronMunicipiosEIEL abst = dialog.getPadronMunicipios((PadronMunicipiosEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.PADRON_MUNICIPIOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.pm"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof PadronMunicipiosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    		
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//ParquesJardinesDialog dialog = new ParquesJardinesDialog(true);
    			ParquesJardinesDialog dialog;
    			if (currentDialog!=null){
    				dialog=(ParquesJardinesDialog)currentDialog;
    				dialog.getParquesJardinesPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new ParquesJardinesDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getParquesJardinesPanel().getOkPressed()){

    				ParquesJardinesEIEL abst = dialog.getParquesJardines(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof ParquesJardinesEIEL){
    				ParquesJardinesEIEL selectedElement = (ParquesJardinesEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					ParquesJardinesDialog dialog = new ParquesJardinesDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					ParquesJardinesDialog dialog = new ParquesJardinesDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getParquesJardinesPanel().getOkPressed()){
    						ParquesJardinesEIEL abst = dialog.getParquesJardines((ParquesJardinesEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.PARQUES_JARDINES_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.PARQUES_JARDINES, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.pj"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof ParquesJardinesEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//PlaneamientoUrbanoDialog dialog = new PlaneamientoUrbanoDialog(true);
    			PlaneamientoUrbanoDialog dialog;
    			if (currentDialog!=null){
    				dialog=(PlaneamientoUrbanoDialog)currentDialog;
    				dialog.getPlaneamientoUrbanoPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new PlaneamientoUrbanoDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getPlaneamientoUrbanoPanel().getOkPressed()){

    				PlaneamientoUrbanoEIEL abst = dialog.getPlaneamientoUrbano(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof PlaneamientoUrbanoEIEL){
    				PlaneamientoUrbanoEIEL selectedElement = (PlaneamientoUrbanoEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					PlaneamientoUrbanoDialog dialog = new PlaneamientoUrbanoDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					PlaneamientoUrbanoDialog dialog = new PlaneamientoUrbanoDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getPlaneamientoUrbanoPanel().getOkPressed()){
    						PlaneamientoUrbanoEIEL abst = dialog.getPlaneamientoUrbano((PlaneamientoUrbanoEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.pu"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof PlaneamientoUrbanoEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//PoblamientoDialog dialog = new PoblamientoDialog(true);
    			PoblamientoDialog dialog;
    			if (currentDialog!=null){
    				dialog=(PoblamientoDialog)currentDialog;
    				dialog.getPoblamientoPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new PoblamientoDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getPoblamientoPanel().getOkPressed()){

    				PoblamientoEIEL abst = dialog.getPoblamiento(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof PoblamientoEIEL){
    				PoblamientoEIEL selectedElement = (PoblamientoEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					PoblamientoDialog dialog = new PoblamientoDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					PoblamientoDialog dialog = new PoblamientoDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getPoblamientoPanel().getOkPressed()){
    						PoblamientoEIEL abst = dialog.getPoblamiento((PoblamientoEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.POBLAMIENTO_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.POBLAMIENTO, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.pobl"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof PoblamientoEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//RecogidaBasurasDialog dialog = new RecogidaBasurasDialog(true);
    			RecogidaBasurasDialog dialog;
    			if (currentDialog!=null){
    				dialog=(RecogidaBasurasDialog)currentDialog;
    				dialog.getRecogidaBasurasPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new RecogidaBasurasDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getRecogidaBasurasPanel().getOkPressed()){

    				RecogidaBasurasEIEL abst = dialog.getRecogidaBasuras(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof RecogidaBasurasEIEL){
    				RecogidaBasurasEIEL selectedElement = (RecogidaBasurasEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					RecogidaBasurasDialog dialog = new RecogidaBasurasDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					RecogidaBasurasDialog dialog = new RecogidaBasurasDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getRecogidaBasurasPanel().getOkPressed()){
    						RecogidaBasurasEIEL abst = dialog.getRecogidaBasuras((RecogidaBasurasEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.RECOGIDA_BASURAS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.RECOGIDA_BASURAS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.rb"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof RecogidaBasurasEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//CentrosSanitariosDialog dialog = new CentrosSanitariosDialog(true);
    			CentrosSanitariosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(CentrosSanitariosDialog)currentDialog;
    				dialog.getCentrosSanitariosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new CentrosSanitariosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getCentrosSanitariosPanel().getOkPressed()){

    				CentrosSanitariosEIEL abst = dialog.getCentrosSanitarios(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof CentrosSanitariosEIEL){
    				CentrosSanitariosEIEL selectedElement = (CentrosSanitariosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					CentrosSanitariosDialog dialog = new CentrosSanitariosDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					CentrosSanitariosDialog dialog = new CentrosSanitariosDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getCentrosSanitariosPanel().getOkPressed()){
    						CentrosSanitariosEIEL abst = dialog.getCentrosSanitarios((CentrosSanitariosEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CENTROS_SANITARIOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.CENTROS_SANITARIOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.cs"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof CentrosSanitariosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//SaneamientoAutonomoDialog dialog = new SaneamientoAutonomoDialog(true);
    			SaneamientoAutonomoDialog dialog;
    			if (currentDialog!=null){
    				dialog=(SaneamientoAutonomoDialog)currentDialog;
    				dialog.getSaneamientoAutonomoPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new SaneamientoAutonomoDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getSaneamientoAutonomoPanel().getOkPressed()){

    				SaneamientoAutonomoEIEL abst = dialog.getSaneamientoAutonomo(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof SaneamientoAutonomoEIEL){
    				SaneamientoAutonomoEIEL selectedElement = (SaneamientoAutonomoEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					SaneamientoAutonomoDialog dialog = new SaneamientoAutonomoDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					SaneamientoAutonomoDialog dialog = new SaneamientoAutonomoDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getSaneamientoAutonomoPanel().getOkPressed()){
    						SaneamientoAutonomoEIEL abst = dialog.getSaneamientoAutonomo((SaneamientoAutonomoEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.sa"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof SaneamientoAutonomoEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
     	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//ServiciosSaneamientoDialog dialog = new ServiciosSaneamientoDialog(true);
    			ServiciosSaneamientoDialog dialog;
    			if (currentDialog!=null){
    				dialog=(ServiciosSaneamientoDialog)currentDialog;
    				dialog.getServiciosSaneamientoPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new ServiciosSaneamientoDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getServiciosSaneamientoPanel().getOkPressed()){

    				ServiciosSaneamientoEIEL abst = dialog.getServiciosSaneamiento(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (getSelectedElement()!= null && getSelectedElement() instanceof ServiciosSaneamientoEIEL){
    				ServiciosSaneamientoEIEL selectedElement = (ServiciosSaneamientoEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					ServiciosSaneamientoDialog dialog = new ServiciosSaneamientoDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					ServiciosSaneamientoDialog dialog = new ServiciosSaneamientoDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getServiciosSaneamientoPanel().getOkPressed()){
    						ServiciosSaneamientoEIEL abst = dialog.getServiciosSaneamiento((ServiciosSaneamientoEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.ss"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof ServiciosSaneamientoEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            } 
    	}
    		else if (selectedPattern.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){

        		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

        			//ServiciosRecogidaBasurasDialog dialog = new ServiciosRecogidaBasurasDialog(true);
        			ServiciosRecogidaBasurasDialog dialog;
        			if (currentDialog!=null){
        				dialog=(ServiciosRecogidaBasurasDialog)currentDialog;
        				dialog.getServiciosRecogidaBasurasPanel().okPressed=false;
        			}
        				
        			else{
        				dialog = new ServiciosRecogidaBasurasDialog(true);
        				currentDialog=dialog;
        			}
        			dialog.setVisible(true);
        			if (dialog.getServiciosRecogidaBasurasPanel().getOkPressed()){

        				ServiciosRecogidaBasuraEIEL abst = dialog.getServiciosRecogidaBasuras(null);
        				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
        				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        				cancelado=false;
        			}
        		}
        		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

        			if (getSelectedElement()!= null && getSelectedElement() instanceof ServiciosRecogidaBasuraEIEL){
        				ServiciosRecogidaBasuraEIEL selectedElement = (ServiciosRecogidaBasuraEIEL) getSelectedElement();
        				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
        				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

        					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
        							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

        					ServiciosRecogidaBasurasDialog dialog = new ServiciosRecogidaBasurasDialog(selectedElement, false);
        					dialog.setVisible(true);  

        				}
        				else{

        					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
        					if (editable)
       				    		editable = isEditable(selectedElement);
        					ServiciosRecogidaBasurasDialog dialog = new ServiciosRecogidaBasurasDialog(selectedElement,editable);
        					dialog.setVisible(true);  

        					if (dialog.getServiciosRecogidaBasurasPanel().getOkPressed()){
        						ServiciosRecogidaBasuraEIEL abst = dialog.getServiciosRecogidaBasuras((ServiciosRecogidaBasuraEIEL) selectedElement);

        						GeopistaLayer geopistaLayer=null;
        						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
        							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
        						else
        							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

        						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
        						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
        							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
        			    		}
        						cancelado=false;
        					}
        					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
        				}
        			}
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                    EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.srb"));
                    dialog.setVisible(true);
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
    				if (getSelectedElement() != null
    						&& getSelectedElement() instanceof ServiciosRecogidaBasuraEIEL) {
    					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
    							getSelectedElement());
    					dialog.setVisible(true);
    				}  
                }
     		}
    		else if (selectedPattern.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){

        		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

        			//EdificiosSinUsoDialog dialog = new EdificiosSinUsoDialog(true);
        			EdificiosSinUsoDialog dialog;
        			if (currentDialog!=null){
        				dialog=(EdificiosSinUsoDialog)currentDialog;
        				dialog.getEdificiosSinUsoPanel().okPressed=false;
        			}
        				
        			else{
        				dialog = new EdificiosSinUsoDialog(true);
        				currentDialog=dialog;
        			}
        			dialog.setVisible(true);
        			if (dialog.getEdificiosSinUsoPanel().getOkPressed()){

        				EdificiosSinUsoEIEL abst = dialog.getEdificiosSinUso(null);
        				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
        				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        				cancelado=false;
        			}
        		}
        		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

        			if (getSelectedElement()!= null && getSelectedElement() instanceof EdificiosSinUsoEIEL){
        				EdificiosSinUsoEIEL selectedElement = (EdificiosSinUsoEIEL) getSelectedElement();
        				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
        				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

        					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
        							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

        					EdificiosSinUsoDialog dialog = new EdificiosSinUsoDialog(selectedElement, false);
        					dialog.setVisible(true);  

        				}
        				else{

        					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
        					if (editable)
       				    		editable = isEditable(selectedElement);
        					EdificiosSinUsoDialog dialog = new EdificiosSinUsoDialog(selectedElement,editable);
        					dialog.setVisible(true);  

        					if (dialog.getEdificiosSinUsoPanel().getOkPressed()){
        						EdificiosSinUsoEIEL abst = dialog.getEdificiosSinUso((EdificiosSinUsoEIEL) selectedElement);

        						GeopistaLayer geopistaLayer=null;
        						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
        							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
        						else
        							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

        						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
        						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
        							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
        			    		}
        						cancelado=false;
        					}
        					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
        				}
        			}
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                    EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.su"));
                    dialog.setVisible(true);
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
    				if (getSelectedElement() != null
    						&& getSelectedElement() instanceof EdificiosSinUsoEIEL) {
    					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
    							getSelectedElement());
    					dialog.setVisible(true);
    				}  
                }
    		}
    		else if (selectedPattern.equals(ConstantesLocalGISEIEL.TANATORIOS)){

        		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

        			//TanatoriosDialog dialog = new TanatoriosDialog(true);
        			TanatoriosDialog dialog;
        			if (currentDialog!=null){
        				dialog=(TanatoriosDialog)currentDialog;
        				dialog.getTanatoriosPanel().okPressed=false;
        			}
        				
        			else{
        				dialog = new TanatoriosDialog(true);
        				currentDialog=dialog;
        			}
        			dialog.setVisible(true);
        			if (dialog.getTanatoriosPanel().getOkPressed()){

        				TanatoriosEIEL abst = dialog.getTanatorios(null);
        				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TANATORIO_LAYER);
        				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        				cancelado=false;
        			}
        		}
        		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

        			if (getSelectedElement()!= null && getSelectedElement() instanceof TanatoriosEIEL){
        				TanatoriosEIEL selectedElement = (TanatoriosEIEL) getSelectedElement();
        				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
        				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

        					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
        							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

        					TanatoriosDialog dialog = new TanatoriosDialog(selectedElement, false);
        					dialog.setVisible(true);  

        				}
        				else{

        					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
        					if (editable)
       				    		editable = isEditable(selectedElement);
        					TanatoriosDialog dialog = new TanatoriosDialog(selectedElement,editable);
        					dialog.setVisible(true);  

        					if (dialog.getTanatoriosPanel().getOkPressed()){
        						TanatoriosEIEL abst = dialog.getTanatorios((TanatoriosEIEL) selectedElement);

        						GeopistaLayer geopistaLayer=null;
        						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
        							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
        						else
        							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

        						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TANATORIO_LAYER);
        						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
        							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
        			    		}
        						cancelado=false;
        					}
        					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
        				}
        			}
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                    EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.TANATORIOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.TANATORIOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.ta"));
                    dialog.setVisible(true);
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
    				if (getSelectedElement() != null
    						&& getSelectedElement() instanceof TanatoriosEIEL) {
    					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
    							getSelectedElement());
    					dialog.setVisible(true);
    				}  
                }
    		}
    		//TRATAMIENTO ESPECIAL
    		else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){

        		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

        			//VertederosDialog dialog = new VertederosDialog(true);
        			VertederosDialog dialog;
        			if (currentDialog!=null){
        				dialog=(VertederosDialog)currentDialog;
        				dialog.getVertederosPanel().okPressed=false;
        			}
        				
        			else{
        				dialog = new VertederosDialog(true);
        				currentDialog=dialog;
        			}
        			dialog.setVisible(true);
        			if (dialog.getVertederosPanel().getOkPressed()){

        				VertederosEIEL abst = dialog.getVertederos(null);
        				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.VERTEDERO_LAYER);
        				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        				cancelado=false;
        			}
        		}
        		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

        			if (selectedItem!= null && selectedItem instanceof VertederosEIEL){
        				VertederosEIEL selectedElement = (VertederosEIEL)selectedItem;
        				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
        				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

        					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
        							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

        					VertederosDialog dialog = new VertederosDialog(selectedElement, false);
        					dialog.setVisible(true);  

        				}
        				else{

        					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
        					if (editable)
       				    		editable = isEditable(selectedElement);
        					VertederosDialog dialog = new VertederosDialog(selectedElement,editable);
        					dialog.setVisible(true);  

        					if (dialog.getVertederosPanel().getOkPressed()){
        						VertederosEIEL abst = dialog.getVertederos((VertederosEIEL) selectedElement);

        						GeopistaLayer geopistaLayer=null;
        						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
        							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
        						else
        							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

        						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.VERTEDERO_LAYER);
        						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
        							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
        			    		}
        						cancelado=false;
        					}
        					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
        				}
        			}
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                    EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.VERTEDEROS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.DATOS_VERTEDEROS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.vt"));
                    dialog.setVisible(true);
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
    				if (selectedItem != null
    						&& selectedItem instanceof VertederosEIEL) {
    					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
    							selectedItem);
    					dialog.setVisible(true);
    				}  
                }
    		}
    		else if (selectedPattern.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){

        		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

        			//CarreterasDialog dialog = new CarreterasDialog(true);
        			CarreterasDialog dialog;
        			if (currentDialog!=null){
        				dialog=(CarreterasDialog)currentDialog;
        				dialog.getCarreterasPanel().okPressed=false;
        			}
        				
        			else{
        				dialog = new CarreterasDialog(true);
        				currentDialog=dialog;
        			}
        			dialog.setVisible(true);
        			if (dialog.getCarreterasPanel().getOkPressed()){

        				TramosCarreterasEIEL abst = dialog.getCarreteras(null);
        				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CARRETERA_LAYER);
        				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        				cancelado=false;
        			}
        		}
        		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

        			if (getSelectedElement()!= null && getSelectedElement() instanceof TramosCarreterasEIEL){
        				TramosCarreterasEIEL selectedElement = (TramosCarreterasEIEL) getSelectedElement();
        				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
        				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

        					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
        							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

        					CarreterasDialog dialog = new CarreterasDialog(selectedElement, false);
        					dialog.setVisible(true);  

        				}
        				else{

        					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
        					if (editable)
       				    		editable = isEditable(selectedElement);
        					CarreterasDialog dialog = new CarreterasDialog(selectedElement,editable);
        					dialog.setVisible(true);  

        					if (dialog.getCarreterasPanel().getOkPressed()){
        						TramosCarreterasEIEL abst = dialog.getCarreteras((TramosCarreterasEIEL) selectedElement);

        						GeopistaLayer geopistaLayer=null;
        						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
        							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
        						else
        							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

        						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CARRETERA_LAYER);
        						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
        						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
        	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
        							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
        			    		}
        						cancelado=false;
        					}
        					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
        				}
        			}
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                    EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CARRETERAS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.TRAMOS_CARRETERAS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.carrt"));
                    dialog.setVisible(true);
        		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
    				if (getSelectedElement() != null
    						&& getSelectedElement() instanceof TramosCarreterasEIEL) {
    					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
    							getSelectedElement());
    					dialog.setVisible(true);
    				}  
                }
    		}
    		//	TRATAMIENTO ESPECIAL
    		else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPOSITOS)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//DepositosDialog dialog = new DepositosDialog(true);
    			DepositosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(DepositosDialog)currentDialog;
    				dialog.getDepositosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new DepositosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getDepositosPanel().getOkPressed()){
    				DepositosEIEL abst = dialog.getDepositos(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (selectedItem!= null && selectedItem instanceof DepositosEIEL){
    				DepositosEIEL selectedElement = (DepositosEIEL) selectedItem;
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					DepositosDialog dialog = new DepositosDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					DepositosDialog dialog = new DepositosDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getDepositosPanel().getOkPressed()){
    						DepositosEIEL abst = dialog.getDepositos((DepositosEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());    					
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						//TODO Modificacion elemento grafico EIEL
    						
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
        						modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.DEPOSITOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.DEPOSITOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.de"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof DepositosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	//TRATAMIENTO ESPECIAL    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//PuntosVertidoDialog dialog = new PuntosVertidoDialog(true);
    			PuntosVertidoDialog dialog;
    			if (currentDialog!=null){
    				dialog=(PuntosVertidoDialog)currentDialog;
    				dialog.getPuntosVertidoPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new PuntosVertidoDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getPuntosVertidoPanel().getOkPressed()){
    				PuntosVertidoEIEL abst = dialog.getPuntosVertido(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PUNTOS_VERTIDO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (selectedItem!= null && selectedItem instanceof PuntosVertidoEIEL){
    				PuntosVertidoEIEL selectedElement = (PuntosVertidoEIEL)selectedItem;
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					PuntosVertidoDialog dialog = new PuntosVertidoDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					PuntosVertidoDialog dialog = new PuntosVertidoDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getPuntosVertidoPanel().getOkPressed()){
    						PuntosVertidoEIEL abst = dialog.getPuntosVertido((PuntosVertidoEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PUNTOS_VERTIDO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.PUNTOS_VERTIDO_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.PUNTOS_VERTIDO, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.pv"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof PuntosVertidoEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			
    			//ServiciosAbastecimientosDialog dialog = new ServiciosAbastecimientosDialog(true);
    			ServiciosAbastecimientosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(ServiciosAbastecimientosDialog)currentDialog;
    				dialog.getServiciosAbastecimientosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new ServiciosAbastecimientosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getServiciosAbastecimientosPanel().getOkPressed()){
    				ServiciosAbastecimientosEIEL abst = dialog.getServiciosAbastecimientos(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof ServiciosAbastecimientosEIEL){
    				ServiciosAbastecimientosEIEL selectedElement = (ServiciosAbastecimientosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					ServiciosAbastecimientosDialog dialog = new ServiciosAbastecimientosDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					ServiciosAbastecimientosDialog dialog = new ServiciosAbastecimientosDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getServiciosAbastecimientosPanel().getOkPressed()){
    						ServiciosAbastecimientosEIEL abst = dialog.getServiciosAbastecimientos((ServiciosAbastecimientosEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.serv_abast"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof ServiciosAbastecimientosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//CentrosAsistencialesDialog dialog = new CentrosAsistencialesDialog(true);
    			CentrosAsistencialesDialog dialog;
    			if (currentDialog!=null){
    				dialog=(CentrosAsistencialesDialog)currentDialog;
    				dialog.getCentrosAsistencialesPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new CentrosAsistencialesDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getCentrosAsistencialesPanel().getOkPressed()){
    				CentrosAsistencialesEIEL abst = dialog.getCentrosAsistenciales(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof CentrosAsistencialesEIEL){
    				CentrosAsistencialesEIEL selectedElement = (CentrosAsistencialesEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					CentrosAsistencialesDialog dialog = new CentrosAsistencialesDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					CentrosAsistencialesDialog dialog = new CentrosAsistencialesDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getCentrosAsistencialesPanel().getOkPressed()){
    						CentrosAsistencialesEIEL abst = dialog.getCentrosAsistenciales((CentrosAsistencialesEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.as"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof CentrosAsistencialesEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.CABILDO)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//CabildoConsejoDialog dialog = new CabildoConsejoDialog(true);
    			CabildoConsejoDialog dialog;
    			if (currentDialog!=null){
    				dialog=(CabildoConsejoDialog)currentDialog;
    				dialog.getCabildoConsejoPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new CabildoConsejoDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getCabildoConsejoPanel().getOkPressed()){
    				CabildoConsejoEIEL abst = dialog.getCabildoConsejo(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PROVINCIA_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof CabildoConsejoEIEL){
    				CabildoConsejoEIEL selectedElement = (CabildoConsejoEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					CabildoConsejoDialog dialog = new CabildoConsejoDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					CabildoConsejoDialog dialog = new CabildoConsejoDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getCabildoConsejoPanel().getOkPressed()){
    						CabildoConsejoEIEL abst = dialog.getCabildoConsejo((CabildoConsejoEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PROVINCIA_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CABILDO_CONSEJO_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.CABILDO, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.ci"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof CabildoConsejoEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//CementeriosDialog dialog = new CementeriosDialog(true);
    			CementeriosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(CementeriosDialog)currentDialog;
    				dialog.getCementeriosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new CementeriosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getCementeriosPanel().getOkPressed()){
    				CementeriosEIEL abst = dialog.getCementerios(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CEMENTERIOS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof CementeriosEIEL){
    				CementeriosEIEL selectedElement = (CementeriosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					CementeriosDialog dialog = new CementeriosDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					CementeriosDialog dialog = new CementeriosDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getCementeriosPanel().getOkPressed()){
    						CementeriosEIEL abst = dialog.getCementerios((CementeriosEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CEMENTERIOS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.CEMENTERIOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.CEMENTERIOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.ce"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof CementeriosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//EntidadesSingularesDialog dialog = new EntidadesSingularesDialog(true);
    			EntidadesSingularesDialog dialog;
    			if (currentDialog!=null){
    				dialog=(EntidadesSingularesDialog)currentDialog;
    				dialog.getEntidadesSingularesPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new EntidadesSingularesDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getEntidadesSingularesPanel().getOkPressed()){
    				EntidadesSingularesEIEL abst = dialog.getEntidadesSingulares(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof EntidadesSingularesEIEL){
    				EntidadesSingularesEIEL selectedElement = (EntidadesSingularesEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					EntidadesSingularesDialog dialog = new EntidadesSingularesDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					EntidadesSingularesDialog dialog = new EntidadesSingularesDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getEntidadesSingularesPanel().getOkPressed()){
    						EntidadesSingularesEIEL abst = dialog.getEntidadesSingulares((EntidadesSingularesEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.ENTIDADES_SINGULARES, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.ce"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof EntidadesSingularesEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
      	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//InfoTerminosMunicipalesDialog dialog = new InfoTerminosMunicipalesDialog(true);
    			InfoTerminosMunicipalesDialog dialog;
    			if (currentDialog!=null){
    				dialog=(InfoTerminosMunicipalesDialog)currentDialog;
    				dialog.getInfoTerminosMunicipalesPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new InfoTerminosMunicipalesDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getInfoTerminosMunicipalesPanel().getOkPressed()){
    				NucleoEncuestado7EIEL abst = dialog.getInfoTerminosMunicipales(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof NucleoEncuestado7EIEL){
    				NucleoEncuestado7EIEL selectedElement = (NucleoEncuestado7EIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					InfoTerminosMunicipalesDialog dialog = new InfoTerminosMunicipalesDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					InfoTerminosMunicipalesDialog dialog = new InfoTerminosMunicipalesDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getInfoTerminosMunicipalesPanel().getOkPressed()){
    						NucleoEncuestado7EIEL abst = dialog.getInfoTerminosMunicipales((NucleoEncuestado7EIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
    		    EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.INFO_TERMINOS_MUNICIPALES_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.NUCLEO_ENCT_7, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.inf_ttmm"));
    		    dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof NucleoEncuestado7EIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
    		}
     	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//IncendiosProteccionDialog dialog = new IncendiosProteccionDialog(true);
    			IncendiosProteccionDialog dialog;
    			if (currentDialog!=null){
    				dialog=(IncendiosProteccionDialog)currentDialog;
    				dialog.getIncendiosProteccionPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new IncendiosProteccionDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getIncendiosProteccionPanel().getOkPressed()){
    				IncendiosProteccionEIEL abst = dialog.getIncendiosProteccion(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof IncendiosProteccionEIEL){
    				IncendiosProteccionEIEL selectedElement = (IncendiosProteccionEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					IncendiosProteccionDialog dialog = new IncendiosProteccionDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					IncendiosProteccionDialog dialog = new IncendiosProteccionDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getIncendiosProteccionPanel().getOkPressed()){
    						IncendiosProteccionEIEL abst = dialog.getIncendiosProteccion((IncendiosProteccionEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.INCENDIOS_PROTECCION, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.ip"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof IncendiosProteccionEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//LonjasMercadosDialog dialog = new LonjasMercadosDialog(true);
    			LonjasMercadosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(LonjasMercadosDialog)currentDialog;
    				dialog.getLonjasMercadosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new LonjasMercadosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getLonjasMercadosPanel().getOkPressed()){
    				LonjasMercadosEIEL abst = dialog.getLonjasMercados(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof LonjasMercadosEIEL){
    				LonjasMercadosEIEL selectedElement = (LonjasMercadosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					LonjasMercadosDialog dialog = new LonjasMercadosDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					LonjasMercadosDialog dialog = new LonjasMercadosDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getLonjasMercadosPanel().getOkPressed()){
    						LonjasMercadosEIEL abst = dialog.getLonjasMercados((LonjasMercadosEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.LONJAS_MERCADOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.LONJAS_MERCADOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.lm"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof LonjasMercadosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.MATADEROS)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//MataderosDialog dialog = new MataderosDialog(true);
    			MataderosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(MataderosDialog)currentDialog;
    				dialog.getMataderosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new MataderosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getMataderosPanel().getOkPressed()){
    				MataderosEIEL abst = dialog.getMataderos(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MATADEROS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof MataderosEIEL){
    				MataderosEIEL selectedElement = (MataderosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					MataderosDialog dialog = new MataderosDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					MataderosDialog dialog = new MataderosDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getMataderosPanel().getOkPressed()){
    						MataderosEIEL abst = dialog.getMataderos((MataderosEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MATADEROS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.MATADEROS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.MATADEROS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.mt"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof MataderosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
     	}
    	//TRATAMIENTO ESPECIAL
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//TratamientosPotabilizacionDialog dialog = new TratamientosPotabilizacionDialog(true);
    			TratamientosPotabilizacionDialog dialog;
    			if (currentDialog!=null){
    				dialog=(TratamientosPotabilizacionDialog)currentDialog;
    				dialog.getTratamientosPotabilizacionPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new TratamientosPotabilizacionDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getTratamientosPotabilizacionPanel().getOkPressed()){
    				TratamientosPotabilizacionEIEL abst = dialog.getTratamientosPotabilizacion(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (selectedItem!= null && selectedItem instanceof TratamientosPotabilizacionEIEL){
    				TratamientosPotabilizacionEIEL selectedElement = (TratamientosPotabilizacionEIEL) selectedItem;
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					TratamientosPotabilizacionDialog dialog = new TratamientosPotabilizacionDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					TratamientosPotabilizacionDialog dialog = new TratamientosPotabilizacionDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getTratamientosPotabilizacionPanel().getOkPressed()){
    						TratamientosPotabilizacionEIEL abst = dialog.getTratamientosPotabilizacion((TratamientosPotabilizacionEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION, I18N.get(
                                "LocalGISEIEL", "localgiseiel.dialog.titulo.abast_tp"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof TratamientosPotabilizacionEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//DiseminadosDialog dialog = new DiseminadosDialog(true);
    			DiseminadosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(DiseminadosDialog)currentDialog;
    				dialog.getDiseminadosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new DiseminadosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getDiseminadosPanel().getOkPressed()){
    				DiseminadosEIEL abst = dialog.getDiseminados(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof DiseminadosEIEL){
    				DiseminadosEIEL selectedElement = (DiseminadosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					DiseminadosDialog dialog = new DiseminadosDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					DiseminadosDialog dialog = new DiseminadosDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getDiseminadosPanel().getOkPressed()){
    						DiseminadosEIEL abst = dialog.getDiseminados((DiseminadosEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.DISEMINADOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.DISEMINADOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.diseminados"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof DiseminadosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//Encuestados1Dialog dialog = new Encuestados1Dialog(true);
    			Encuestados1Dialog dialog;
    			if (currentDialog!=null){
    				dialog=(Encuestados1Dialog)currentDialog;
    				dialog.getEncuestados1Panel().okPressed=false;
    			}
    				
    			else{
    				dialog = new Encuestados1Dialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getEncuestados1Panel().getOkPressed()){
    				Encuestados1EIEL abst = dialog.getEncuestados1(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof Encuestados1EIEL){
    				Encuestados1EIEL selectedElement = (Encuestados1EIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					Encuestados1Dialog dialog = new Encuestados1Dialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					Encuestados1Dialog dialog = new Encuestados1Dialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getEncuestados1Panel().getOkPressed()){
    						Encuestados1EIEL abst = dialog.getEncuestados1((Encuestados1EIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.ENCUESTADOS1_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.ENCUESTADOS1, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.enc1"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof Encuestados1EIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//Encuestados2Dialog dialog = new Encuestados2Dialog(true);
    			Encuestados2Dialog dialog;
    			if (currentDialog!=null){
    				dialog=(Encuestados2Dialog)currentDialog;
    				dialog.getEncuestados2Panel().okPressed=false;
    			}
    				
    			else{
    				dialog = new Encuestados2Dialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getEncuestados2Panel().getOkPressed()){
    				Encuestados2EIEL abst = dialog.getEncuestados2(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof Encuestados2EIEL){
    				Encuestados2EIEL selectedElement = (Encuestados2EIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					Encuestados2Dialog dialog = new Encuestados2Dialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					Encuestados2Dialog dialog = new Encuestados2Dialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getEncuestados2Panel().getOkPressed()){
    						Encuestados2EIEL abst = dialog.getEncuestados2((Encuestados2EIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.ENCUESTADOS2_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.ENCUESTADOS2, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.enc2"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof Encuestados2EIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
     	}
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//NucleosAbandonadosDialog dialog = new NucleosAbandonadosDialog(true);
    			NucleosAbandonadosDialog dialog;
    			if (currentDialog!=null){
    				dialog=(NucleosAbandonadosDialog)currentDialog;
    				dialog.getNucleosAbandonadosPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new NucleosAbandonadosDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getNucleosAbandonadosPanel().getOkPressed()){
    				NucleosAbandonadosEIEL abst = dialog.getNucleosAbandonados(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (getSelectedElement()!= null && getSelectedElement() instanceof NucleosAbandonadosEIEL){
    				NucleosAbandonadosEIEL selectedElement = (NucleosAbandonadosEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					NucleosAbandonadosDialog dialog = new NucleosAbandonadosDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					NucleosAbandonadosDialog dialog = new NucleosAbandonadosDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getNucleosAbandonadosPanel().getOkPressed()){
    						NucleosAbandonadosEIEL abst = dialog.getNucleosAbandonados((NucleosAbandonadosEIEL) selectedElement);
    						
    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.aband"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (getSelectedElement() != null
						&& getSelectedElement() instanceof NucleosAbandonadosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
							getSelectedElement());
					dialog.setVisible(true);
				}  
            }
    	}    
		//Prueba de concepto Elementos sin informacion alfanumerica.
		//ALFANUMERICOS
		//- EMISARIOS
     	//TRATAMIENTO ESPECIAL    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.EMISARIOS)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//EmisoresDialog dialog = new EmisoresDialog(true);
    			EmisoresDialog dialog;
    			if (currentDialog!=null){
    				dialog=(EmisoresDialog)currentDialog;
    				dialog.getEmisoresPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new EmisoresDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getEmisoresPanel().getOkPressed()){

    				EmisariosEIEL abst = dialog.getEmisor(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		 else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			if (selectedItem!= null && selectedItem instanceof EmisariosEIEL){

    				EmisariosEIEL selectedElement = (EmisariosEIEL) selectedItem;
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					EmisoresDialog dialog = new EmisoresDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					EmisoresDialog dialog = new EmisoresDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getEmisoresPanel().getOkPressed()){
    						EmisariosEIEL abst =  dialog.getEmisor((EmisariosEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.EMISARIOS_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.EMISARIOS, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.em"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof EmisariosEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(selectedItem);
					dialog.setVisible(true);
				}
           
            }
    	}
    	
    	
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.TCOLECTOR)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			ColectoresDialog dialog;
    			if (currentDialog!=null){
    				dialog=(ColectoresDialog)currentDialog;
    				dialog.getColectoresPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new ColectoresDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getColectoresPanel().getOkPressed()){

    				ColectorEIEL abst = dialog.getColector(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.COLECTOR_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
					cancelado=false;
    			}
    		}
    		 else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (selectedItem!= null && selectedItem instanceof ColectorEIEL){

    				ColectorEIEL selectedElement = (ColectorEIEL)selectedItem;
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					ColectoresDialog dialog = new ColectoresDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					ColectoresDialog dialog = new ColectoresDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getColectoresPanel().getOkPressed()){
    						ColectorEIEL abst =  dialog.getColector((ColectorEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.COLECTOR_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    	    				if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
							cancelado=false;
    					}
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.TCOLECTOR_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.TCOLECTOR, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.cl"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof ColectorEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(selectedItem);
					dialog.setVisible(true);
				}
           
            }
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.TCONDUCCION)){

    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

    			//TramosConduccionDialog dialog = new TramosConduccionDialog(true);
    			TramosConduccionDialog dialog;
    			if (currentDialog!=null){
    				dialog=(TramosConduccionDialog)currentDialog;
    				dialog.getTramosConduccionPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new TramosConduccionDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getTramosConduccionPanel().getOkPressed()){

    				TramosConduccionEIEL abst = dialog.getTramosConduccion(null);
    				GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
    				String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
					cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){

    			if (selectedItem!= null && selectedItem instanceof TramosConduccionEIEL){

    				TramosConduccionEIEL selectedElement = (TramosConduccionEIEL)selectedItem;
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

    					TramosConduccionDialog dialog = new TramosConduccionDialog(selectedElement, false);
    					dialog.setVisible(true);  

    				}
    				else{

    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					TramosConduccionDialog dialog = new TramosConduccionDialog(selectedElement,editable);
    					dialog.setVisible(true);  

    					if (dialog.getTramosConduccionPanel().getOkPressed()){
    						TramosConduccionEIEL abst =  dialog.getTramosConduccion((TramosConduccionEIEL) selectedElement);

    						GeopistaLayer geopistaLayer=null;
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
    							geopistaLayer=LoadedLayers.forceLoadLayer(getJPanelTree().getPatronSelected(),getJPanelTree());
    						else
    							geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);

    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
    						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
							cancelado=false;
    					}
    					bloquearElemento(selectedItem, getJPanelTree().getPatronSelected(),false);
    				}
    			}
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.TCONDUCCION_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.TCONDUCCION, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.cn"));
                dialog.setVisible(true);
    		} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof TramosConduccionEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(selectedItem);
					dialog.setVisible(true);
//				} else {
//					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(
//							ConstantesLocalGISEIEL.CAPTACIONES_MODEL_COMPLETO_VERSIONADO,
//							ConstantesLocalGISEIEL.CAPTACIONES, I18N.get(
//									"LocalGISEIEL",
//									"localgiseiel.dialog.titulo.ca"));
				}
           
            }
    	}
    	else if (selectedPattern.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
        		
    		if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
    			//EntidadesAgrupadasDialog dialog = new EntidadesAgrupadasDialog(true);
    			EntidadesAgrupadasDialog dialog;
    			if (currentDialog!=null){
    				dialog=(EntidadesAgrupadasDialog)currentDialog;
    				dialog.getEntidadesAgrupadasPanel().okPressed=false;
    			}
    				
    			else{
    				dialog = new EntidadesAgrupadasDialog(true);
    				currentDialog=dialog;
    			}
    			dialog.setVisible(true);
    			if (dialog.getEntidadesAgrupadasPanel().getOkPressed()){

    				EntidadesAgrupadasEIEL agrup = dialog.getEntidadesAgrupadas(null);
    				//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
    				String idLayer=null;
//    				if (geopistaLayer!=null)
//    					idLayer= geopistaLayer.getSystemId();
    				InitEIEL.clienteLocalGISEIEL.insertarElemento(agrup, idLayer, selectedPattern);
    				cancelado=false;
    			}
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR) || tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_CONSULTAR)){
    			
    			
    			if (getSelectedElement()!= null && getSelectedElement() instanceof EntidadesAgrupadasEIEL){
    				EntidadesAgrupadasEIEL selectedElement = (EntidadesAgrupadasEIEL) getSelectedElement();
    				String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
    				if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
    						!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
    					JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
    							+ bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
    					EntidadesAgrupadasDialog dialog = new EntidadesAgrupadasDialog(selectedElement, false);
    					dialog.setVisible(true);  
    				} else{
    					bloquearElemento(selectedElement, getJPanelTree().getPatronSelected(),true);
    					if (editable)
   				    		editable = isEditable(selectedElement);
    					EntidadesAgrupadasDialog dialog = new EntidadesAgrupadasDialog(selectedElement,editable);
    					dialog.setVisible(true);  
    					if (dialog.getEntidadesAgrupadasPanel().getOkPressed()){
    						EntidadesAgrupadasEIEL agrup = dialog.getEntidadesAgrupadas((EntidadesAgrupadasEIEL) selectedElement);
    						//GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    						//String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
    						String idLayer = null;
    						InitEIEL.clienteLocalGISEIEL.insertarElemento(agrup, idLayer, selectedPattern);
    						if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
    							modificarElementoGrafico(selectedItem, getJPanelTree().getPatronSelected(),Const.ESTADO_TEMPORAL);
    			    		}
    						cancelado=false;
    					}
    					bloquearElemento(getSelectedElement(), getJPanelTree().getPatronSelected(),false);
    				}
    			}	
    		}
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_LISTAR)) {
                EIELListaTablaDialog dialog = new EIELListaTablaDialog(
                        ConstantesLocalGISEIEL.AGRUPACIONES6000_MODEL_COMPLETO,
                        ConstantesLocalGISEIEL.AGRUPACIONES6000, I18N.get("LocalGISEIEL",
                                "localgiseiel.dialog.titulo.a6"));
                dialog.getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                dialog.getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
                dialog.setVisible(true);
                
               
    		} 
    		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VERSION)) {
				if (selectedItem != null
						&& selectedItem instanceof EntidadesAgrupadasEIEL) {
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog(selectedItem);
					dialog.setVisible(true);
           
				}
    		}
        		
    		getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
    		getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(false);
		}
    			
    	return cancelado;
    }

    
    private boolean confirm(String tag1, String tag2){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, tag1, tag2, JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }
    
    private HashMap getNumElementosPendientes(String nodo,String locale){
		HashMap c=null;
        try {
			c = eielClient.getNumElementosPendientes(nodo,locale);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return c;       
    }
    
    
    private int getModelIndex(int index){
        return ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).modelIndex(index);
    }
    
	public void enter() {
				
	}

	public void exit() {
				
	}
    
        
}  
