package com.geopista.ui.plugin.routeenginetools.genredplugin;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.util.graph.GraphPartitioner;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.impedance.EdgeImpedance;
import org.uva.route.graph.structure.impedance.SimpleImpedance;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.genredplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.BasicFeaturePropertiesDialog;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.BasicNetworkFactoryDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilToolsDraw;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.VentanaError;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.build.LocalGisGraphGenerator;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.network.NetworkProperty;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDatasetFactory;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ISelectionManager;
import com.vividsolutions.jump.workbench.ui.SelectionManager;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * @description: This plugin creates a list of nets and they are saved in
 *               NetworkMgr. Besides ConvexHull is created of each one of the
 *               generated nets
 **/

public class GenerarRedPlugIn extends ThreadedBasePlugIn {

	protected boolean genRedButtonAdded = false;
	protected static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	protected PlugInContext context = null;
	protected BasicNetworkFactoryDialog basicDialog;

	private static Logger LOGGER = Logger.getLogger(GenerarRedPlugIn.class);

	public boolean execute(PlugInContext context, BasicNetworkFactoryDialog dialog){
		basicDialog = dialog;
		
			this.execute(context);
		
		return false;
	}

	public boolean execute(PlugInContext context) 
 {
		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;

		if (basicDialog == null){
			basicDialog = new BasicNetworkFactoryDialog(context.getWorkbenchFrame(), 
					I18N.get("genred","routeengine.genred.plugintitle"), 
					context);
		}

		basicDialog.setVisible(true);
		if (basicDialog.wasOKPressed()){
			processActions();
		} else{
			basicDialog.dispose();
			basicDialog = null;
		}
		return true;

	}
	/**
	 * Lanza el proceso de generación de redes en un hilo. Implementar el mÃ©todo {@link #run(TaskMonitor, PlugInContext)}.
	 */
	protected void processActions()
	{
	    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    final PlugInContext runContext = this.context;
	    progressDialog.setTitle("TaskMonitorDialog.Wait");
	    progressDialog.report(I18N.get("genred","routeengine.genred.taskmonitormessage"));
	    progressDialog.addComponentListener(new ComponentAdapter()
	    {
	    	public void componentShown(ComponentEvent e)
	    	{   
	    		new Thread(new Runnable()
	    		{
	    			public void run()
	    			{
	    				try
	    				{
	    				    withTaskMonitorDo(progressDialog, runContext);
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

	protected void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) 
	{
		// TODO Auto-generated method stub
		run(monitor, context);
	}

	private void generarRedDeFeaturesSeleccionadas(GraphGenerator linegenerator, HashMap<String, Object> networkProperties)
	{

	ISelectionManager selectionManager = context.getLayerViewPanel().getSelectionManager();
	Object object = basicDialog.getLayerComboBox().getSelectedItem();
if (object==null)
    {
	JOptionPane.showMessageDialog(basicDialog, "Debe seleccionar una capa origen.", "Falta una capa", JOptionPane.ERROR_MESSAGE);
	return;
    }
	// Calculo el grafo de las features seleccionadas sea cual sea la capa?

GeopistaLayer lyr=(GeopistaLayer)object;
final ArrayList<Object> originalFeatures = new ArrayList<Object>(selectionManager.getFeaturesWithSelectedItems(lyr));

generateGraphFromLayerFeatures(lyr, originalFeatures, linegenerator, networkProperties);
// El campo descriptor es un nombre de columna de la Feature pero se usa para toda la red.
// para marcar en la red cual es el campo del que sacar de LocalGIS un texto descriptivo
    if (basicDialog.getDescriptionComboBox().getSelectedIndex() > 0)
				    {
    	NetworkProperty networkColumnDescProperties = new NetworkProperty();
    	networkColumnDescProperties.setNetworkManagerProperty(
    			Integer.toString(((GeopistaLayer)object).getId_LayerDataBase()), 
    			basicDialog.getDescriptionComboBox().getSelectedItem().toString()
					);
    	networkProperties.put("ColumnDescriptor", networkColumnDescProperties);
		}
// TODO habilitar el dialogo para seleccionar descripción y bidireccionalidad de cada capa
//	
//	
//	Collection layersWithSelectedItems = selectionManager.getLayersWithSelectedItems();
//
//		if (layersWithSelectedItems.size() == 0) {
//			VentanaError ventanaerror = new VentanaError(context);
//			ventanaerror.addText(I18N.get("genred","routeengine.genred.errormessage.nonfeatureselected"));
//			ventanaerror.setVisible();
//		} else 
//		{
//
//		BasicFeaturePropertiesDialog dialogo = null;
//			int lastSelectedLayer = -1;
//			CoordinateSystem coodSys = null;
//			// Itera para cada capa con elementos
//		for (Object selLayer : layersWithSelectedItems)
//		    {
//		final ArrayList<Object> originalFeatures = new ArrayList<Object>(selectionManager.getFeaturesWithSelectedItems((Layer) selLayer));
//		generateGraphFromLayerFeatures((GeopistaLayer) selLayer, originalFeatures, linegenerator, networkProperties);
//		    }
//		for (Iterator<Object> feat = originalFeatures.iterator(); feat
//			.hasNext();) {
//
//				Feature element = (Feature) feat.next();
//			// FIX JPC
//			// try {
//					GeopistaLayer actualLayerWithSelectedItems = null;
//					GeopistaLayer selectedLayer = null;
//					/// buscamos la layer con la feature seleccionada
//
//			Iterator<GeopistaLayer> layerIterator = layersWithSelectedItems.iterator();
//					int idSystemLayer = -1;
//					while (layerIterator.hasNext()){
//						actualLayerWithSelectedItems = layerIterator.next();
//				if (actualLayerWithSelectedItems.getFeatureCollectionWrapper().getFeatures().contains(element))
//				    {
//							idSystemLayer = actualLayerWithSelectedItems.getId_LayerDataBase();
//							selectedLayer = actualLayerWithSelectedItems; 
//
//							coodSys =selectedLayer.getLayerManager().getCoordinateSystem();
//				    }
//					}
//
//					if (coodSys != null){
//						element.getGeometry().setSRID(coodSys.getEPSGCode());
//					}
//
//			if (idSystemLayer != lastSelectedLayer)
//			    {
//						if (selectedLayer != null ){
//							lastSelectedLayer = idSystemLayer;
//							dialogo = createPropertiesDialog(selectedLayer); 
//				    } // BUG Else
//
//						dialogo.setVisible(true);
//			    } // BUG Â¿else?
//
//					createLocalGisDynamicEdgeFromSelectedFeatures(element,idSystemLayer, lastSelectedLayer,
//						(LocalGisBasicLineGraphGenerator)linegenerator,  selectedLayer, 
//							dialogo,  networkProperties
//					);
//
//			// } catch (ClassCastException e) {
//			// e.printStackTrace();
//			// }
//			}
//
//
//		}

	}

	protected BasicFeaturePropertiesDialog createPropertiesDialog(GeopistaLayer selectedLayer)
	{
	    return new BasicFeaturePropertiesDialog(
	    		selectedLayer,
	    		I18N.get("genred","routeengine.genred.layerbasicdescription") + selectedLayer.getName()
	    		, context);
	}

    protected void createLocalGisDynamicEdgeFromSelectedFeatures(Feature feature, int idSystemLayer, int lastSelectedLayer, LocalGisGraphGenerator linegenerator,
	    GeopistaLayer selectedLayer,BasicFeaturePropertiesDialog dialogo, HashMap<String, Object> networkProperties	) 
 {

		CoordinateSystem inCoord = selectedLayer.getLayerManager().getCoordinateSystem();
	feature.getGeometry().setSRID(inCoord.getEPSGCode());

		int featureID = -1;
		try{
			// Si isTempID()==true la feature no tiene un systemId inicializado como ocurre con las features locales
		if (feature instanceof GeopistaFeature && !((GeopistaFeature) feature).isTempID())
		    {
			featureID = Integer.parseInt(((GeopistaFeature) feature).getSystemId());
			} else{
			featureID = feature.getID();
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			LOGGER.error(e);
			featureID = -1;
		}

		ILocalGISEdge edge;
		try {
		edge = (ILocalGISEdge) ((LocalGisGraphGenerator) linegenerator).add(feature.getGeometry(),
					featureID, 
					idSystemLayer,
					feature.getGeometry().getSRID(), feature);


			if (edge != null){
				if (dialogo.wasOKPressed()){

					if (dialogo.getDescriptionComboBox().getSelectedIndex() > 0){
						NetworkProperty networkColumnDescProperties = new NetworkProperty();
						networkColumnDescProperties.setNetworkManagerProperty(
								Integer.toString(selectedLayer.getId_LayerDataBase()), 
								dialogo.getDescriptionComboBox().getSelectedItem().toString()
						);
						networkProperties.put("ColumnDescriptor", networkColumnDescProperties);
					}

					if (dialogo.getBidirectionRadioButton().isSelected()){

					edge.setImpedanceAToB(createNewLocalGisSimpleimpedance(feature.getGeometry(),
 dialogo.getImpedanciaAB(), feature));
					edge.setImpedanceBToA(createNewLocalGisSimpleimpedance(feature.getGeometry(),
 dialogo.getImpedanciaBA(), feature));

					} else if (dialogo.getUniqueDirectionAtoBRadioButton().isSelected()){

					edge.setImpedanceAToB(createNewLocalGisSimpleimpedance(feature.getGeometry(),
 dialogo.getImpedanciaAB(), feature));

					} else if (dialogo.getUniqueDirectionBtoARadioButton().isSelected()){

					edge.setImpedanceBToA(createNewLocalGisSimpleimpedance(feature.getGeometry(),
 dialogo.getImpedanciaBA(), feature));

					} 

				} else {
				edge.setImpedanceBidirecccional(feature.getGeometry().getLength());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param context
	 * @param linegenerator
	 * @param networkProperties
	 */
	protected void generarRedDeUnaCapa(PlugInContext context,GraphGenerator linegenerator,
			HashMap<String, Object> networkProperties)
	{
		Object object = basicDialog.getLayerComboBox().getSelectedItem();

			// Calculo el grafo de la capa seleccionada

		GeopistaLayer lyr=(GeopistaLayer)object;
		final ArrayList<Object> originalFeatures = new ArrayList<Object>(lyr.getFeatureCollectionWrapper().getWrappee()	.getFeatures());

		generateGraphFromLayerFeatures(lyr, originalFeatures, linegenerator, networkProperties);
		// El campo descriptor es un nombre de columna de la Feature pero se usa para toda la red.
		// para marcar en la red cual es el campo del que sacar de LocalGIS un texto descriptivo
		    if (basicDialog.getDescriptionComboBox().getSelectedIndex() > 0)
		        {
		    	NetworkProperty networkColumnDescProperties = new NetworkProperty();
		    	networkColumnDescProperties.setNetworkManagerProperty(
		    			Integer.toString(((GeopistaLayer)object).getId_LayerDataBase()), 
		    			basicDialog.getDescriptionComboBox().getSelectedItem().toString()
		    	);
		    	networkProperties.put("ColumnDescriptor", networkColumnDescProperties);
		    }
	}

	public void generateGraphFromLayerFeatures(GeopistaLayer lyr, final ArrayList<Object> selectedFeatures, GraphGenerator linegenerator,
		HashMap<String, Object> networkProperties)
	{
	    CoordinateSystem coodSys = lyr.getLayerManager().getCoordinateSystem();

	    for (Iterator<Object> feat = selectedFeatures.iterator(); feat.hasNext();)
			{

				Feature element = (Feature) feat.next();
				try
				    {
					element.getGeometry().setSRID(coodSys.getEPSGCode());
				    } catch (UnsupportedOperationException e1)
				    {
					LOGGER.debug(e1);
				    }

				try {

					this.createNewLocalGisDinamycEdges(
							(LocalGisGraphGenerator) linegenerator, 
	    				lyr.getId_LayerDataBase(), 
							element, 
							element.getGeometry());

				} catch (ClassCastException e) {
					e.printStackTrace();
				}

			}

	}


	/**
	 * @param lstring
	 * @param linegenerator
	 * @param object
	 * @param element
	 * @param mls
	 * @param i
	 */
    protected ILocalGISEdge createNewLocalGisDinamycEdges(LocalGisGraphGenerator linegenerator, int systemIDLayer,
			Feature element, Geometry mls)
    {
		int featureID = -1;
		try{
			// Si isTempID()==true la feature no tiene un systemId inicializado como ocurre con las features locales
			if (element instanceof GeopistaFeature && !((GeopistaFeature) element).isTempID())
			{
				String systemId = ((GeopistaFeature)element).getSystemId();
				featureID = Integer.parseInt(systemId);
			} else{
				featureID = element.getID();
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			LOGGER.error(e);
			featureID = -1;
		}

	ILocalGISEdge edge = (ILocalGISEdge) linegenerator.add(mls, featureID, systemIDLayer, element.getGeometry().getSRID(), element);
		if (edge != null)
		    {
				this.setImpedanceToLocalGisDynamicEdge( mls, element, edge);
		    }
	return edge;
	}


	private boolean setImpedanceToLocalGisDynamicEdge(Geometry mls, Feature elementFeature,
			ILocalGISEdge lgDynamicEdge){

		if (basicDialog.getBidirectionRadioButton().isSelected()){

			lgDynamicEdge.setImpedanceAToB(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaAB(),
					elementFeature));
			lgDynamicEdge.setImpedanceBToA(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaBA(), 
					elementFeature));

		} else if (basicDialog.getUniqueDirectionAtoBRadioButton().isSelected()){

			lgDynamicEdge.setImpedanceAToB(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaAB(),
					elementFeature));

		} else if (basicDialog.getUniqueDirectionBtoARadioButton().isSelected()){

			lgDynamicEdge.setImpedanceBToA(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaBA(), 
					elementFeature));

		} else{
			return false;
		}

		return true;
	}


	/**
	 * @param mls
	 * @param e
	 * @param element 
	 */
    protected EdgeImpedance createNewLocalGisSimpleimpedance(Geometry mls,
			JComboBox impedanceComboBox, Feature elementFeature) {

		double cost = 0;

		if (impedanceComboBox.getSelectedIndex() >= 0){
			if (impedanceComboBox.getSelectedItem().equals("Longitud")){
				if (mls instanceof LineString){
					cost = mls.getLength();
				} 
				else if (mls instanceof MultiLineString){
					for (int i = 0; i < mls.getNumGeometries(); i++) {
						cost = cost + mls.getGeometryN(i).getLength();
					}
				}
			} else {
				if (elementFeature != null){
					cost = 	Double.parseDouble(
							(String)elementFeature.getAttribute(
									(String) impedanceComboBox.getSelectedItem()
							)
					);
				}
			}
		}

		return new SimpleImpedance(cost);
	}

	/**
	 * Crea capa de particiones y las aÃ±ade al workbench
	 * AÃ±ade la red y las particiones al NetworkManager
	 * TODO resolver la forma más sencilla de manejar las particiones y subredes.
	 * @param context
	 * @param nombrered
	 * @param linegenerator
	 * @param networkProperties 
	 */
    public void networksLayerGeneratorFromGraph(PlugInContext context, String nombrered, Graph graph, HashMap<String, Object> networkProperties)
    {
	// guardo la red y su grafo
	NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);

	if (networkMgr.getNetwork(nombrered) != null)
	    {
		VentanaError ventanaerror = new VentanaError(context);
		ventanaerror.addText(I18N.get("genred", "routeengine.genred.errormessage.usedname"));
		ventanaerror.addText(I18N.get("genred", "routeengine.genred.errormessage.overwritename"));
		ventanaerror.setVisible();
		if (!ventanaerror.OK())
		    {
			return;
		    }
		else
		    {
			networkMgr.detachNetwork(nombrered);
		    }
	    }
	
	Network parent = NetworkModuleUtil.addNewNetwork(networkMgr, nombrered, graph, networkProperties);

	Layer zona = creaCapaParticiones(networkMgr, nombrered, graph, context, networkProperties);
	LabelStyle labelStyle = new LabelStyle();
	labelStyle.setAttribute("nombreSubred");
	labelStyle.setColor(Color.black);
	labelStyle.setScaling(false);
	labelStyle.setEnabled(true);
	zona.addStyle(labelStyle);

    }

	private ArrayList<Feature> getOriginalFeatures (String nombreCapa){
		ArrayList<Feature> result = null;

		if (nombreCapa == null || nombreCapa.equals("")){
			result = (ArrayList<Feature>) context.getLayerViewPanel().getSelectionManager()
			.getFeaturesWithSelectedItems();
		}else{
			result = (ArrayList<Feature>) context.getLayerManager().getLayer(nombreCapa).getFeatureCollectionWrapper()
			.getWrappee().getFeatures();
		}
		return result;

	}

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.genredplugin.language.RouteEngine_GenRedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("genred",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1))
		.add(checkFactory.createExactlyNLayersMustHaveSelectedItemsCheck(1));
	}


	public FeatureCollection convexHull(FeatureCollection hullFC, Graph g,
			PlugInContext context, int i, List<Geometry> hullList,
			String nombrered) {
	FeatureCollection graphFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();
	FeatureSchema featureSchema = graphFeatureCol.getFeatureSchema();

		GeometryFactory fact = new GeometryFactory();

	// TODO use LocalgisEdges and the features stored into it
	boolean useEdges = true;
	if (useEdges)
	    {
		graphFeatureCol=NetworkModuleUtilToolsDraw.getFeatureCollectionForEdges(nombrered,g,context);

//		for (Iterator<Edge> iter_edges = (Iterator<Edge>) g.getEdges().iterator(); iter_edges.hasNext();)
//		    {
//			Edge edge = (Edge) iter_edges.next();
//			Feature featureForEdge = null;
//
//			if (edge instanceof ILocalGISEdge) // use feature if available
//			    {
//				ILocalGISEdge lgEdge = (ILocalGISEdge) edge;
//				featureForEdge= NetworkModuleUtilWorkbench.findFeatureForEdge(lgEdge, context);
////				if (lgEdge.getFeature() != null)
////				    {
////					featureForEdge = lgEdge.getFeature();
////				    } else if (lgEdge.getGeometry() != null)
////				    {
////					featureForEdge = new BasicFeature(featureSchema);
////					featureForEdge.setGeometry(lgEdge.getGeometry());
////				    } else
////				    // store both ends
////				    {
////					Feature featurenode = createFeatureFromNode(featureSchema, fact, edge.getNodeA());
////					graphFeatureCol.add(featurenode);
////					featurenode = createFeatureFromNode(featureSchema, fact, edge.getNodeB());
////					graphFeatureCol.add(featurenode);
////				    }
//			    }
//			if (featureForEdge == null) // crea un candidato suficiente
//			    {
//			    featureForEdge = new BasicFeature(featureSchema);
//			    featureForEdge.setGeometry(NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge, context));
//			    }
//				graphFeatureCol.add(featureForEdge);
//			 
//			   
//		    }
	    } else
	    {
		for (Iterator<Node> iter_nodes = (Iterator<Node>) g.getNodes().iterator(); iter_nodes.hasNext();)
		    {
			Node node = (Node) iter_nodes.next();
			// Coordinate coord = ((MiOptXYNode) node).getCoordinate();
			Feature featurenode = createFeatureFromNode(featureSchema, fact, node);
			graphFeatureCol.add(featurenode);
		    }
	    }

		int size = graphFeatureCol.size();
		if (size == 0) {
			VentanaError error = new VentanaError(context);
			error.addText(I18N.get("genred","routeengine.genred.errormessage.emptynodes"));
			error.setVisible();
		} else {
		/**
		 * Calculate the convexHull of the collection
		 */
			int count = 0;
			Geometry[] geoms = new Geometry[size];

			for (Iterator<Object> iternodesCol = graphFeatureCol.iterator(); iternodesCol
			.hasNext();) {
				Feature f = (Feature) iternodesCol.next();
				Geometry geom = f.getGeometry();
				if (geom == null)
					continue;
				if (fact == null)
					fact = geom.getFactory();

				geoms[count++] = geom;
			}
			GeometryCollection gc = fact.createGeometryCollection(geoms);
			Geometry hull = gc.convexHull();

			hullList.add(hull);
			Feature particion = new BasicFeature(hullFC.getFeatureSchema());
			particion.setGeometry(hull);

			particion.setAttribute("ID_particion", new Integer(i));
			particion.setAttribute("nombreSubred", "Sub" + nombrered + i);
			hullFC.add(particion);

		}// fin else
		return hullFC;
	}

    private Feature createFeatureFromNode(FeatureSchema featureSchema, GeometryFactory fact, Node node)
    {
	Coordinate coord = ((DynamicGeographicNode) node).getCoordinate();
	Point geom_nodes = fact.createPoint(coord);
	Feature featurenodes = new BasicFeature(featureSchema);
	featurenodes.setGeometry(geom_nodes);
	return featurenodes;
    }

	public Layer creaCapaParticiones(NetworkManager networkMgr,
			String nombrered, Graph graph, PlugInContext context, HashMap<String, Object> networkProperties) {
		

		// Consigo saber el numero de redes existentes

		GraphPartitioner part = new GraphPartitioner(graph);
		// obtengo las particiones del grafo
		part.partition();
		// hace la particion

		Collection<Object> partitions = part.getPartitions();
		// meto las partitions en una lista
		int num_subredes = partitions.size();
		// numero de particiones (lo mismo que numero de subredes)

		// obtengo los grafos independientes
		Graph[] gr = new Graph[num_subredes];

		int i = 0;

		List<Geometry> hullList = new ArrayList<Geometry>();
		FeatureCollection hullFC = FeatureDatasetFactory.createFromGeometry(hullList);

		hullFC.getFeatureSchema().addAttribute("ID_particion",
				AttributeType.INTEGER);
		hullFC.getFeatureSchema().addAttribute("nombreSubred",
				AttributeType.STRING);
		
		Network parent = networkMgr.getNetwork(nombrered);
		// obtengo los edges y nodes del grafo y los meto en una coleccion
		for (Iterator<Object> partitionIter = ((Collection<Object>) partitions)
				.iterator(); partitionIter.hasNext();) {

			gr[i] = (Graph) partitionIter.next();

			String subNetworkName = I18N.get("genred","routeengine.genred.subnetworklayername")+ nombrered + i;
			
		// TODO ï¿½porquï¿½ se guarda en este momento?
//JPC: 2012-10-02
//			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
//			LocalGISStreetRouteReaderWriter	db;
//			try {
//				db= new LocalGISStreetRouteReaderWriter(routeConnection);
//			} catch (SQLException e) {
//				throw new RuntimeException(e);
//			}
//			GraphMemoryManager memmgr=new LocalGISAllinMemoryManager(db);
//			memmgr.setGraph(gr[i]);
			Graph subgraph = (Graph) gr[i];
			
			DynamicGraph subnetGraph=NetworkModuleUtil.getNewInMemoryGraph(subgraph);

			
			Network subNet = networkMgr.putSubNetwork(parent,subNetworkName, subnetGraph);
			subNet.addProperties(networkProperties);
			

			//			((BasicNetworkManager)networkMgr).putSubNetwork(parent, 
			//					subNetworkName, gr[i]);
			// Consigo la capa nodos y aristas.

			// aÃ±ado


		hullFC = convexHull(hullFC, subgraph, context, i, hullList, nombrered);

			i++; // incrementa el indice para guardar las particiones

		}// fin partitionIter
		Layer zona = context.addLayer(
				I18N.get("genred","routeengine.genred.categorylayername"),
				I18N.get("genred","routeengine.genred.partitionlayername")
				+ nombrered,
				hullFC);

		return zona;
	}




    protected void putNetworkPropetiesRecursive(Network network, HashMap<String, Object> networkProperties)
    {
	for (int i = 0; i < networkProperties.keySet().size(); i++)
	    {
		String key = (String) networkProperties.keySet().toArray()[i];
		NetworkProperty value = (NetworkProperty) networkProperties.get(key);
		network.getProperties().put(key, value);
	    }
	if (!network.getSubnetworks().isEmpty())
	    {
		Iterator<Network> nets = network.getSubnetworks().values().iterator();
		while (nets.hasNext())
		    {
			putNetworkPropetiesRecursive(nets.next(), networkProperties);
		    }
	    }
    }


	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("genred","routeengine.genred.iconfile"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!genRedButtonAdded  )
		{
			//			toolbox.addToolBar();
		// FIX BUG GenerarRedPlugIn explode = new GenerarRedPlugIn();
		// toolbox.addPlugIn(explode, null, explode.getIcon());
		toolbox.addPlugIn(this, null, this.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			genRedButtonAdded = true;
		}
	}


	public void run(TaskMonitor monitor, PlugInContext context)
	 {
		// TODO Auto-generated method stub
		// consigo el nombre de la red

		String nombrered = basicDialog.getNombreRedTextField().getText();
		// Necesita un generador de LocalGisStreetEdge
		GraphGenerator linegenerator = getGraphGenerator();

		HashMap<String, Object> networkProperties = new HashMap<String, Object>();

		// si se seleccionan un conjunto de features
		if ( basicDialog.getFeatureRadioButton().isSelected() == true) {
			// Creo el grafo de la red seleccionada
			generarRedDeFeaturesSeleccionadas(linegenerator,networkProperties);
		}
		// si se eligen todas las features de la capa
		else if (basicDialog.getLayerRadioButton().isSelected() == true) {
			generarRedDeUnaCapa(context, linegenerator, networkProperties);
		}
		basicDialog.dispose();
		basicDialog = null;
		
				
	Graph graph = linegenerator.getGraph();
	if (graph.getEdges().isEmpty())
	    {
		context.getWorkbenchGuiComponent().warnUser("No se ha generado ninguna red. Consulte logs.");
	    }
	else
	    {		
		DynamicGraph dynamicGraph = NetworkModuleUtil.getNewInMemoryGraph(graph);	
		networksLayerGeneratorFromGraph(context, nombrered, dynamicGraph, networkProperties);
	    }
		

	}

	protected GraphGenerator getGraphGenerator()
	{
	    return NetworkModuleUtil.getBasicLineGraphGenerator();
	}



}
