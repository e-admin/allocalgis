package com.geopista.ui.plugin.routeenginetools.genredplugin;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.util.graph.GraphPartitioner;
import org.uva.route.graph.build.dynamic.DynamicGeograficGraphBuilder;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.impedance.EdgeImpedance;
import org.uva.route.graph.structure.impedance.SimpleImpedance;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicInterNetworker;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.managers.GraphMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.genredplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.BasicFeaturePropertiesDialog;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.BasicNetworkFactoryDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.VentanaError;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.build.LocalGisBasicLineGraphGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISGraphBuilder;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.manager.LocalGISAllinMemoryManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
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
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * @description: This plugin creates a list of nets and they are saved in
 *               NetworkMgr. Besides ConvexHull is created of each one of the
 *               generated nets
 **/

public class GenerarRedPlugIn extends ThreadedBasePlugIn {

	private boolean genRedButtonAdded = false;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private PlugInContext context = null;
	protected BasicNetworkFactoryDialog basicDialog;

	private static Logger LOGGER = Logger.getLogger(GenerarRedPlugIn.class);

	public boolean execute(PlugInContext context, BasicNetworkFactoryDialog dialog){
		basicDialog = dialog;
		try {
			this.execute(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean execute(PlugInContext context) throws Exception {
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
							catch (Exception e)
							{
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
		} else{
			basicDialog.dispose();
			basicDialog = null;
		}
		return true;

	}

	private void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {
		// TODO Auto-generated method stub
		run(monitor, context);
	}

	private void generarRedDeCapasSeleccionadas(GraphGenerator linegenerator, HashMap<String, Object> networkProperties) throws Exception{
		final ArrayList<Object> originalFeatures = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());
		if (originalFeatures.size() == 0) {
			VentanaError ventanaerror = new VentanaError(context);
			ventanaerror.addText(I18N.get("genred","routeengine.genred.errormessage.nonfeatureselected"));
			ventanaerror.mostrar();
		} else {

			BasicFeaturePropertiesDialog dialogo = null;
			int lastSelectedLayer = -1;
			CoordinateSystem coodSys = null;

			for (Iterator<Object> feat = originalFeatures.iterator(); feat
			.hasNext();) {

				Feature element = (Feature) feat.next();

				try {
					GeopistaLayer actualLayerWithSelectedItems = null;
					GeopistaLayer selectedLayer = null;
					/// buscamos la layer con la feature seleccionada
					Iterator<GeopistaLayer> layerIterator = context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
					int idSystemLayer = -1;
					while (layerIterator.hasNext()){
						actualLayerWithSelectedItems = layerIterator.next();
						if (actualLayerWithSelectedItems.getFeatureCollectionWrapper().getFeatures().contains(element)){
							idSystemLayer = actualLayerWithSelectedItems.getId_LayerDataBase();
							selectedLayer = actualLayerWithSelectedItems; 

							coodSys =selectedLayer.getLayerManager().getCoordinateSystem();

						}
					}

					if (coodSys != null){
						element.getGeometry().setSRID(coodSys.getEPSGCode());
					}

					if (idSystemLayer != lastSelectedLayer ){
						if (selectedLayer != null ){
							lastSelectedLayer = idSystemLayer;
							dialogo = new BasicFeaturePropertiesDialog(
									selectedLayer,
									I18N.get("genred","routeengine.genred.layerbasicdescription") + selectedLayer.getName()
									, context); 
						}	

						dialogo.setVisible(true);
					}

					createLocalGisDynamicEdgeFromSelectedFeatures(element,
							idSystemLayer, lastSelectedLayer, 
							(LocalGisBasicLineGraphGenerator)linegenerator,  selectedLayer, 
							dialogo,  networkProperties
					);

				} catch (ClassCastException e) {
					e.printStackTrace();
				}
			}


		}

	}

	private void createLocalGisDynamicEdgeFromSelectedFeatures(Feature element,int idSystemLayer, int lastSelectedLayer, 
			LocalGisBasicLineGraphGenerator linegenerator, GeopistaLayer selectedLayer, 
			BasicFeaturePropertiesDialog dialogo, HashMap<String, Object> networkProperties
	) throws Exception {

		CoordinateSystem inCoord = selectedLayer.getLayerManager().getCoordinateSystem();
		element.getGeometry().setSRID(inCoord.getEPSGCode());

		int featureID = -1;
		try{
			if (element instanceof GeopistaFeature){
				featureID = Integer.parseInt(((GeopistaFeature)element).getSystemId());
			} else{
				featureID = element.getID();
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			LOGGER.error(e);
			featureID = -1;
		}

		LocalGISDynamicEdge edge;
		try {
			edge = (LocalGISDynamicEdge) ((LocalGisBasicLineGraphGenerator) linegenerator).add(
					element.getGeometry(),
					featureID, 
					idSystemLayer,
					element.getGeometry().getSRID()
			);


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

						edge.setImpedanceAToB(createNewLocalGisSimpleimpedance(element.getGeometry(), 
								dialogo.getImpedanciaAB(),
								element));
						edge.setImpedanceBToA(createNewLocalGisSimpleimpedance(element.getGeometry(), 
								dialogo.getImpedanciaBA(), 
								element));

					} else if (dialogo.getUniqueDirectionAtoBRadioButton().isSelected()){

						edge.setImpedanceAToB(createNewLocalGisSimpleimpedance(element.getGeometry(), 
								dialogo.getImpedanciaAB(),
								element));

					} else if (dialogo.getUniqueDirectionBtoARadioButton().isSelected()){

						edge.setImpedanceBToA(createNewLocalGisSimpleimpedance(element.getGeometry(), 
								dialogo.getImpedanciaBA(), 
								element));

					} 

				} else {
					edge.setImpedanceBidirecccional(element.getGeometry().getLength());
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
	private void generarRedDeUnaCapa(
			PlugInContext context,
			GraphGenerator linegenerator,
			HashMap<String, Object> networkProperties) throws Exception {

		String nombrecapa = null;
		Object object = basicDialog.getLayerComboBox().getSelectedItem();

		if (object != null){
			if (object instanceof GeopistaLayer){
				nombrecapa = ((GeopistaLayer)object).getSystemId();
			}
			else if (object instanceof Layer){
				nombrecapa = ((Layer)object).getName();
			}
			else{
				nombrecapa =  basicDialog.getLayerComboBox().getSelectedItem().toString();
			}
			// el nombre de la capa seleccionada
			// Calculo el grafo de la capa seleccionada


			final ArrayList<Object> originalFeatures = new ArrayList<Object>(
					context.getLayerManager().getLayer(nombrecapa)
					.getFeatureCollectionWrapper().getWrappee()
					.getFeatures());

			CoordinateSystem coodSys = context.getLayerManager().getLayer(nombrecapa).getLayerManager().getCoordinateSystem();

			for (Iterator<Object> feat = originalFeatures.iterator(); feat
			.hasNext();) {

				Feature element = (Feature) feat.next();
				element.getGeometry().setSRID(coodSys.getEPSGCode());

				try {

					this.createNewLocalGisDinamycEdges(
							(LocalGisBasicLineGraphGenerator) linegenerator, 
							((GeopistaLayer)object).getId_LayerDataBase(), 
							element, 
							element.getGeometry());

				} catch (ClassCastException e) {
					e.printStackTrace();
				}

			}

			if (basicDialog.getDescriptionComboBox().getSelectedIndex() > 0){
				NetworkProperty networkColumnDescProperties = new NetworkProperty();
				networkColumnDescProperties.setNetworkManagerProperty(
						Integer.toString(((GeopistaLayer)object).getId_LayerDataBase()), 
						basicDialog.getDescriptionComboBox().getSelectedItem().toString()
				);
				networkProperties.put("ColumnDescriptor", networkColumnDescProperties);
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
	private void createNewLocalGisDinamycEdges(
			LocalGisBasicLineGraphGenerator linegenerator, int systemIDLayer,
			Feature element, Geometry mls) throws Exception {


		int featureID = -1;
		try{
			if (element instanceof GeopistaFeature){
				featureID = Integer.parseInt(((GeopistaFeature)element).getSystemId());
			} else{
				featureID = element.getID();
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			LOGGER.error(e);
			featureID = -1;
		}

		LocalGISDynamicEdge edge;
		try {
			edge = (LocalGISDynamicEdge) linegenerator.add(
					mls,
					featureID, 
					systemIDLayer,
					element.getGeometry().getSRID()
			);
			if (edge != null){
				this.setImpedanceToLocalGisDynamicEdge( mls, element, edge);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private boolean setImpedanceToLocalGisDynamicEdge(Geometry mls, Feature elementFeature,
			LocalGISDynamicEdge lgDynamicEdge){

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
	private EdgeImpedance createNewLocalGisSimpleimpedance(Geometry mls,
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
	 * @param context
	 * @param nombrered
	 * @param linegenerator
	 * @param networkProperties 
	 */
	public void networksLayerGeneratorFromGraph(PlugInContext context,
			String nombrered, DynamicGraph graph, HashMap<String, Object> networkProperties) {
		// genero el grafo apartir de un lineString

		// guardo la red y su grafo
		NetworkManager networkMgr = FuncionesAuxiliares
		.getNetworkManager(context);

		if (networkMgr.getNetwork(nombrered) == null) {

			Layer zona = creaCapaParticiones(networkMgr, nombrered, graph,
					context, networkProperties);
			LabelStyle labelStyle = new LabelStyle();
			labelStyle.setAttribute("nombreSubred");
			labelStyle.setColor(Color.black);
			labelStyle.setScaling(false);
			labelStyle.setEnabled(true);
			zona.addStyle(labelStyle);

		} else {
			VentanaError ventanaerror = new VentanaError(context);
			ventanaerror.addText(I18N.get("genred","routeengine.genred.errormessage.usedname"));
			ventanaerror.addText(I18N.get("genred","routeengine.genred.errormessage.overwritename"));
			ventanaerror.mostrar();
			if (ventanaerror.OK()) {
				Layer zona = creaCapaParticiones(networkMgr, nombrered,
						graph, context, networkProperties);
				LabelStyle labelStyle = new LabelStyle();
				labelStyle.setAttribute("nombreSubred");
				labelStyle.setColor(Color.black);
				labelStyle.setScaling(false);
				labelStyle.setEnabled(true);
				zona.addStyle(labelStyle);

			}

		}
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
		.add(
				checkFactory
				.createWindowWithLayerManagerMustBeActiveCheck())
				.add(
						checkFactory
						.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
						.add(checkFactory.createTaskWindowMustBeActiveCheck()).add(
								checkFactory.createAtLeastNLayersMustExistCheck(1));
	}


	public FeatureCollection convexHull(FeatureCollection hullFC, Graph g,
			PlugInContext context, int i, List<Geometry> hullList,
			String nombrered) {
		FeatureCollection nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		GeometryFactory fact = new GeometryFactory();
		for (Iterator<Node> iter_nodes = (Iterator<Node>) g.getNodes()
				.iterator(); iter_nodes.hasNext();) {
			Node node = (Node) iter_nodes.next();
			//			Coordinate coord = ((MiOptXYNode) node).getCoordinate();
			Coordinate coord = ((DynamicGeographicNode) node).getCoordinate();
			Point geom_nodes = fact.createPoint(coord);
			Feature featurenodes = new BasicFeature(nodesFeatureCol
					.getFeatureSchema());
			featurenodes.setGeometry(geom_nodes);
			nodesFeatureCol.add(featurenodes);

		}
		int size = nodesFeatureCol.size();
		if (size == 0) {
			VentanaError error = new VentanaError(context);
			error.addText(I18N.get("genred","routeengine.genred.errormessage.emptynodes"));
			error.mostrar();
		} else {
			int count = 0;
			Geometry[] geoms = new Geometry[size];

			for (Iterator<Object> iternodesCol = nodesFeatureCol.iterator(); iternodesCol
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

	public Layer creaCapaParticiones(NetworkManager networkMgr,
			String nombrered, Graph graph, PlugInContext context, HashMap<String, Object> networkProperties) {
		/* TODO 
		 * hacer cambios de basicInternetWorker a null cuando obtienes FuncionesAuxiliares.getNetworManager()
		 */
		if (((BasicNetworkManager)networkMgr).getInterNetworker() == null ){
			BasicInterNetworker binNet = new BasicInterNetworker();
			binNet.setNetworkManager(networkMgr);
			networkMgr.setInterNetworker(binNet);
		}

		Network parent = networkMgr.putNetwork(nombrered, graph);


		for (int i = 0; i < networkProperties.keySet().size(); i++){
			String key = (String) networkProperties.keySet().toArray()[i];
			NetworkProperty value = (NetworkProperty) networkProperties.get(key);
			parent.getProperties().put(key, value);
		}



		// Cargo las propiedades de la red.

		// Consigo saber el numero de redes existentes

		GraphPartitioner part = new GraphPartitioner(graph);
		// obtengo las particiones del grafo
		part.partition();
		// hace la particiï¿½n

		Collection<Object> partitions = part.getPartitions();
		// meto las partisans en una lista
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

		// obtengo los edges y nodes del grafo y los meto en una colecciï¿½n
		for (Iterator<Object> partitionIter = ((Collection<Object>) partitions)
				.iterator(); partitionIter.hasNext();) {

			gr[i] = (Graph) partitionIter.next();

			String subNetworkName = I18N.get("genred","routeengine.genred.subnetworklayername")+ nombrered + i;

			BasicNetwork subNet = new BasicNetwork(subNetworkName);
			
			
			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
			LocalGISStreetRouteReaderWriter	db;
			try {
				db= new LocalGISStreetRouteReaderWriter(routeConnection);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} 
			GraphMemoryManager memmgr=new LocalGISAllinMemoryManager(db);
			DynamicGraph subnetGraph = new DynamicGraph(memmgr);
//			memmgr.setGraph(gr[i]);
			try {
				subnetGraph.getMemoryManager().appendGraph((Graph)gr[i]);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			
			subNet.setGraph(subnetGraph);

			for (int m = 0; m < networkProperties.keySet().size(); m++){
				String key = (String) networkProperties.keySet().toArray()[m];
				NetworkProperty value = (NetworkProperty) networkProperties.get(key);
				subNet.getProperties().put(key, value);
			}

			parent.getSubnetworks().put(subNetworkName, subNet);

			//			((BasicNetworkManager)networkMgr).putSubNetwork(parent, 
			//					subNetworkName, gr[i]);
			// Consigo la capa nodos y aristas.

			// añado


			hullFC = convexHull(hullFC, gr[i], context, i, hullList, nombrered);

			i++; // incrementa el indice para guardar las particiones

		}// fin partitionIter
		Layer zona = context.addLayer(
				I18N.get("genred","routeengine.genred.categorylayername"),
				I18N.get("genred","routeengine.genred.partitionlayername")
				+ nombrered,
				hullFC);
		return zona;
	}


	private void putNetworkPropetiesRecursive(Network network,
			HashMap<String, Object> networkProperties) {
		// TODO Auto-generated method stub
		if (network.getSubnetworks().isEmpty()){
			for (int i = 0; i < networkProperties.keySet().size(); i++){
				String key = (String) networkProperties.keySet().toArray()[i];
				NetworkProperty value = (NetworkProperty) networkProperties.get(key);
				network.getProperties().put(key, value);
			}
		} else{
			for (int i = 0; i < networkProperties.keySet().size(); i++){
				String key = (String) networkProperties.keySet().toArray()[i];
				NetworkProperty value = (NetworkProperty) networkProperties.get(key);
				network.getProperties().put(key, value);
			}
			Iterator<Network> nets = network.getSubnetworks().values().iterator();
			while(nets.hasNext()){
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
			GenerarRedPlugIn explode = new GenerarRedPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			genRedButtonAdded = true;
		}
	}


	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		// TODO Auto-generated method stub
		// consigo el nombre de la red

		String nombrered = basicDialog.getNombreRedTextField().getText();

		GraphGenerator linegenerator = null;
		linegenerator = new LocalGisBasicLineGraphGenerator(new LocalGISGraphBuilder());
		HashMap<String, Object> networkProperties = new HashMap<String, Object>();

		// si se seleccionan un conjunto de features
		if ( basicDialog.getFeatureRadioButton().isSelected() == true) {
			// Creo el grafo de la red seleccionada
			generarRedDeCapasSeleccionadas(linegenerator,networkProperties);
		}
		// si se eligen todas las features de la capa
		else if (basicDialog.getLayerRadioButton().isSelected() == true) {
			generarRedDeUnaCapa(context, linegenerator, networkProperties);
		}

		AllInMemoryManager memmgr = new AllInMemoryManager();
		memmgr.setGraph(linegenerator.getGraph());
		DynamicGraph dynamicGraph = new DynamicGraph(memmgr);			

		networksLayerGeneratorFromGraph(context, nombrered, dynamicGraph, networkProperties);

		basicDialog.dispose();
		basicDialog = null;

	}



}
