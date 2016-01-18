package com.geopista.ui.dialogs.mobile;


import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.geopista.app.AppContext;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.administrador.ListaUsuarios;
import com.geopista.protocol.administrador.Usuario;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.ui.dialogs.beans.ComboItemGraticuleListener;
import com.geopista.ui.dialogs.beans.ExtractionProject;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.plugin.mobile.MobileAssignCellsPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geostaf.ui.plugin.generate.GraticuleCreatorEngine;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;


public class MobileAssignCellsPanel02  extends JPanel implements WizardPanel
{
	public static final String MOBILE_USERS_GRATICULES = "MOBILE_USERS_GRATICULES";
    private Blackboard blackboard  = Constants.APLICACION.getBlackboard();
    private String localId = null;
    private WizardContext wizardContext;
    private PlugInContext context;  
    private List<JComboBox> jComboList;
    
    /**
     * @return null to turn the Next button into a Finish button
     */
    
    private String nextID = null;
	private JScrollPane jPaneInternal; 
	private String[] arrayNombresUsuarios; //array de usuarios de los combos
	private JComboBox jcomboSelecAll;
	private List<Usuario> listaUsuariosPermisos; //lista de usuarios con permiso de lectura sobre las capas extraídas

    public MobileAssignCellsPanel02 (String id, String nextId, PlugInContext context)
    {
        this.nextID = nextId;
        this.localId = id;
        this.context = context; 
        this.arrayNombresUsuarios = null;
        this.listaUsuariosPermisos = null;
    }
    
	  public void enteredFromLeft(Map dataMap)
	  {
	      if(!Constants.APLICACION.isLogged())
	      {
	    	  Constants.APLICACION.login();
	      }
	      if(!Constants.APLICACION.isLogged())
	      {
	          wizardContext.cancelWizard();
	          return;
	      }
	      
	      try
	      {
	           jbInit();
	      }
	      catch(Exception e)
	      {
	          e.printStackTrace();
	      }
	  }
    
    private void jbInit() throws Exception
    {
    	this.removeAll();
    	
    	//creamos y centramos la cuadrícula con los valores pasados
//    	crearCentrarCuadricula(context, (Double) blackboard.get(GraticuleCreatorPlugIn.CELL_SIDE_LENGTH_X), 
//    			(Double) blackboard.get(GraticuleCreatorPlugIn.CELL_SIDE_LENGTH_Y), 
//    			(Integer) blackboard.get(GraticuleCreatorPlugIn.LAYER_HEIGHT_IN_CELLS),
//    			(Integer) blackboard.get(GraticuleCreatorPlugIn.LAYER_WIDTH_IN_CELLS), 
//    			(Coordinate) blackboard.get(GraticuleCreatorPlugIn.SOUTHWEST_CORNER_OF_LEFT_LAYER));
        
    	this.setLayout(new GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.ipadx = -21;
        gridBagConstraints1.ipady = 141;
        java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
        this.add(getGraticuleCells(), gridBagConstraints1);
        this.add(getPanelButtonAllUser(), gridBagConstraints2);
    }
    
    /**
     * Crea una cuadrícula con los parámetros indicados y centra el mapa sobre la misma
     * @param plugContext
     * @param cellSideLengthX
     * @param cellSideLengthY
     * @param layerHeightInCells
     * @param layerWidthInCells
     * @param southwestCornerOfLeftLayer
     * @throws NoninvertibleTransformException
     */
	public static void crearCentrarCuadricula(PlugInContext plugContext, double cellSideLengthX, double cellSideLengthY, 
			int layerWidthInCells, int layerHeightInCells, Coordinate southwestCornerOfLeftLayer) {
        //creación de la última cuadrícula
        GraticuleCreatorEngine graticuleCreator = new GraticuleCreatorEngine();
        graticuleCreator.setCellSideLengthX(cellSideLengthX);
        graticuleCreator.setCellSideLengthY(cellSideLengthY); 
        graticuleCreator.setLayerWidthInCells(layerWidthInCells);
        graticuleCreator.setLayerHeightInCells(layerHeightInCells);
        graticuleCreator.setSouthwestCornerOfLeftLayer(southwestCornerOfLeftLayer);
//        graticuleCreator.setCellSideLengthX((Double) blackboard.get(GraticuleCreatorPlugIn.CELL_SIDE_LENGTH_X));
//        graticuleCreator.setCellSideLengthY((Double) blackboard.get(GraticuleCreatorPlugIn.CELL_SIDE_LENGTH_Y)); 
//        graticuleCreator.setLayerWidthInCells((Integer) blackboard.get(GraticuleCreatorPlugIn.LAYER_WIDTH_IN_CELLS));
//      graticuleCreator.setLayerHeightInCells((Integer) blackboard.get(GraticuleCreatorPlugIn.LAYER_HEIGHT_IN_CELLS));
//        graticuleCreator.setSouthwestCornerOfLeftLayer((Coordinate) blackboard.get(GraticuleCreatorPlugIn.SOUTHWEST_CORNER_OF_LEFT_LAYER));
        graticuleCreator.execute(plugContext);
        
        //centrado de la visión
		double minX = graticuleCreator.getSouthwestCornerOfLeftLayer().x;
		double minY = graticuleCreator.getSouthwestCornerOfLeftLayer().y;
        double maxX = minX + graticuleCreator.getLayerWidthInCells() * graticuleCreator.getCellSideLengthX();
		double maxY = minY + graticuleCreator.getLayerHeightInCells() * graticuleCreator.getCellSideLengthY();
		Envelope modelEnvelope = new Envelope(minX, maxX, minY, maxY);
		try {
			plugContext.getLayerViewPanel().getViewport().zoom(modelEnvelope);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	private Component getPanelButtonAllUser() {
		JPanel jpanelAll = new JPanel();
		jcomboSelecAll = new JComboBox(arrayNombresUsuarios);
		JButton jButtonSelecAll = new JButton(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel02_asignTodas));
		jButtonSelecAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int selectIndex = jcomboSelecAll.getSelectedIndex();
				for (int i = 0; i < jComboList.size(); i++) {
					jComboList.get(i).setSelectedIndex(selectIndex);
				}
			}
		});
		
		//añadimos al panel final
		jpanelAll.add(jButtonSelecAll);
		jpanelAll.add(jcomboSelecAll);

		return jpanelAll;
	}

	private JScrollPane getGraticuleCells() {
		//sacamos las cuadrículas de la rejilla
        LayerManager layerManager = context.getLayerManager();
        Layer graticuleLayer = layerManager.getLayer(GraticuleCreatorEngine.getGraticuleName());
        if(graticuleLayer!=null){
	        FeatureCollectionWrapper fCollWrapper = graticuleLayer.getFeatureCollectionWrapper();
	        List featList = fCollWrapper.getFeatures();
	        jComboList = new ArrayList<JComboBox>();
	        Feature feature = null;
	        Integer cellId = null;
	        JPanel jPaneCells = new JPanel();
	        int cols = featList.size()/2;
	        if(featList.size()%2!=0){
	        	cols++;
	        }	        
	        jPaneCells.setLayout(new GridLayout(cols, 2));
	        
//	        //capas extraídas
//	        ArrayList<GeopistaLayer> writeableLayers = (ArrayList<GeopistaLayer>) blackboard.get(MobileExtractPanel01.MOBILE_WRITEABLE_LAYERS);
//	        ArrayList<GeopistaLayer> readableLayers = (ArrayList<GeopistaLayer>) blackboard.get(MobileExtractPanel01.MOBILE_READABLE_LAYERS);
//	        ArrayList<String> listLayersId = new ArrayList<String>();
//	        GeopistaLayer geopistaLayer = null;
//	        for(int i=0; i<writeableLayers.size(); i++){
//	        	geopistaLayer = writeableLayers.get(i);
//	        	listLayersId.add(String.valueOf(geopistaLayer.getId_LayerDataBase()));
//	        }
//	        for(int i=0; i<readableLayers.size(); i++){
//	        	geopistaLayer = readableLayers.get(i);
//	        	listLayersId.add(String.valueOf(geopistaLayer.getId_LayerDataBase()));
//	        }
	        
	        //obtenemos el proyecto de extracción seleccionado de la pantalla anterior
	        ExtractionProject eProject =  (ExtractionProject) blackboard.get(MobileAssignCellsPanel01.SELECTED_EXTRACT_PROJECT);
	        
	        JPanel auxPanel = null;
	        arrayNombresUsuarios = null;
	        listaUsuariosPermisos = null;
	        HashMap<String, String> usuariosAsignados = null;
	        final String sUrlPrefix = Constants.APLICACION.getString("geopista.conexion.servidorurl");
	        try {	
	        	//añadimos los usuarios a los combos
	        	AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                        sUrlPrefix + WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME);
	        	ListaUsuarios usersRealList = (ListaUsuarios) administradorCartografiaClient.getUsersPermLayers(eProject.getIdExtractLayersList());
	        	//usuarios ya asignados
	        	usuariosAsignados = administradorCartografiaClient.getAssignCellsExtractProject(eProject.getIdProyecto());
	        	//usuarios con permisos sobre las capas
	        	Hashtable<String, Usuario> usuariosReales = (Hashtable<String, Usuario>) usersRealList.gethUsuarios();
	        	Set<String> keySetUsers = usuariosReales.keySet();
	        	arrayNombresUsuarios = new String[keySetUsers.size()+1];
	        	arrayNombresUsuarios[0] = ComboItemGraticuleListener.SIN_ASIGNAR;
	        	listaUsuariosPermisos = new ArrayList<Usuario>();
	        	String userKey = null;
	        	Usuario usuario = null;
	        	int k=1;
	        	for (Iterator iterator = keySetUsers.iterator(); iterator.hasNext();) {
					userKey = (String) iterator.next();
					usuario = usuariosReales.get(userKey);
					arrayNombresUsuarios[k] = usuario.getName();
					listaUsuariosPermisos.add(usuario);
					k++;
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			JComboBox jcombo = null;
			ILayerViewPanel layerViewPanel = (ILayerViewPanel) context.getLayerViewPanel();
			JLabel jLabelCelda = null;
			String cellIdStr = null;
			Map<Layer, HashSet<Feature>> visibleLayerToFeaturesInFenceMap = null;
			graticuleLayer.setVisible(false); //para quitar la cuadrícula de la intersección
	        //añadimos el identificador de cada una al panel
	        for (int i = 0; i < featList.size(); i++) {
	        	feature = (Feature) featList.get(i);
	        	visibleLayerToFeaturesInFenceMap = layerViewPanel.visibleLayerToFeaturesInFenceMap(feature.getGeometry());
	        	cellId = (Integer) feature.getAttribute(GraticuleCreatorEngine.ATR_CELL_ID);
	        	auxPanel = new JPanel();
	        	jLabelCelda = new JLabel(I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel02_celda) + cellId);
	        	jcombo = new JComboBox(arrayNombresUsuarios);
	        	cellIdStr = String.valueOf(cellId);
	        	jcombo.setName(cellIdStr);
	        	
	        	//celdas con features o sin ellas
	        	if(visibleLayerToFeaturesInFenceMap==null || visibleLayerToFeaturesInFenceMap.size()==0){
	        		jLabelCelda.setEnabled(false);
	        		jcombo.setEnabled(false);
	        	}
	        	else {
	        		jLabelCelda.setForeground(Color.RED);
		        	jcombo.addItemListener(new ComboItemGraticuleListener(layerViewPanel, graticuleLayer, feature, jLabelCelda, wizardContext));
		        	jComboList.add(jcombo);
	        	}
	        	
	        	auxPanel.add(jLabelCelda);
	        	auxPanel.add(jcombo);
	        	jPaneCells.add(auxPanel);
	        }
	        graticuleLayer.setVisible(true);
	        	
	        //seleccionamos los usuario asignados en los combos
	        if(usuariosAsignados!=null && usuariosAsignados.size()!=0){
		        for (int i = 0; i < jComboList.size(); i++) {
		        	jcombo = jComboList.get(i);
		        	cellIdStr = jcombo.getName();
		        	if(usuariosAsignados.containsKey(cellIdStr)){
		        		seleccionaUsuarioComboCelda(jcombo, usuariosAsignados.get(cellIdStr));
		        	}
				}
	        }
	        
			jPaneInternal = new JScrollPane();
			jPaneInternal.setViewportView(jPaneCells);
			jPaneInternal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
		
		return jPaneInternal;
	}
    
	/**
	 * Selecciona un elemento del combo en caso de existir
	 * @param jcombo
	 * @param listaUsuariosPermisos2
	 * @param idUser
	 */
    private void seleccionaUsuarioComboCelda(JComboBox jcombo, String idUser) {
    	Usuario u = null;
    	//buscamos en la lista de usuarios con permisos reales sobre las capas extraídas
    	for(int i=0; i<listaUsuariosPermisos.size();i++){
    		u = listaUsuariosPermisos.get(i);
    		if(u.getId().equals(idUser)){
    			jcombo.setSelectedIndex(i+1);
    		}
    	}
	}

	/**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
     	/** guardamos las celdas que tienen usuario asignado **/
    	JComboBox jCombo = null;
    	String idCelda;
    	String idSelectedUsuario;
    	HashMap<String, String> celdasUsuarios = new HashMap<String, String>(); //lista de usuarios asignados a la celda que es índice
    	int selectedIndex;
    	for (int i = 0; i < jComboList.size(); i++) {
			jCombo = jComboList.get(i);
			idCelda = jCombo.getName();
			selectedIndex = jCombo.getSelectedIndex();
			idSelectedUsuario = ComboItemGraticuleListener.SIN_ASIGNAR;
			if(selectedIndex > 0){
				idSelectedUsuario = listaUsuariosPermisos.get(selectedIndex-1).getId();
			}
			celdasUsuarios.put(idCelda, idSelectedUsuario);
		}
    	
    	//añadimos al clipboard el map generado
    	blackboard.put(MOBILE_USERS_GRATICULES, celdasUsuarios);    
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    
    public void remove(InputChangedListener listener)
    {
        
    }
    
    public String getTitle()
    {
        return this.getName();
    }
    
    public String getID()
    {
        return localId;
    }
    
    public String getInstructions()
    {
        return I18N.get(MobileAssignCellsPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPanel02_asignUser);
    }
    
    public boolean isInputValid()
    {        
    	JComboBox jCombo = null;
    	//si existe algún usuario seleccionado
    	for (int i = 0; i < jComboList.size(); i++) {
    		jCombo = jComboList.get(i);
			if(jCombo.getSelectedIndex()!=0){
				return true;
			}
		}
    	return false;
    }
    
    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    public void setNextID(String nextID)
    {
        this.nextID=nextID;
    }
    public String getNextID()
    {
        return nextID;
    }
       
    
    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
   
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"  

