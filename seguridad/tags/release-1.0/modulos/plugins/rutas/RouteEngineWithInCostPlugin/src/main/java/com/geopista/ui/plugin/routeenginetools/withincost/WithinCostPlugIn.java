package com.geopista.ui.plugin.routeenginetools.withincost;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.path.PathNotFoundException;
import org.uva.route.graph.path.RoutePath;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.VirtualNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.proxy.ProxyEdge;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.ElementNotFoundException;
import org.uva.routeserver.algorithms.WithinCostAlgorithm;
import org.uva.routeserver.managers.AllInMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.VentanaError;
import com.geopista.ui.plugin.routeenginetools.withincost.dialogs.WithInCostDialog;
import com.geopista.ui.plugin.routeenginetools.withincost.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.withincost.utils.RouteEngineWICDrawPointTool;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.ln.ExternalInfoRouteLN;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.weighter.LocalGISWeighter;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.IdEdgeNetworkBean;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
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
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/*
 * this plugin executes WithinCost Algorithm
 */

public class WithinCostPlugIn extends AbstractPlugIn {
	/**
	 * Logger for this class
	 */
	private static final Log logger	=LogFactory.getLog(WithinCostPlugIn.class);
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private static CalcRutaConfigFileReaderWriter configProperties = null;
	private static HashMap<String, Network> configuatorNetworks;
	private static VirtualNodeInfo nodeInfo = null;
	private static Layer pointLayer = null;
	private static FeatureCollection nodesFeatureCol;


	private boolean WICRouteButtonAdded = false;	


	//	MultiInputDialog dial;
	static WithInCostDialog dialog = null;

	Vector<String> subredSet;
	static PlugInContext context;

	static Node source = null;
	static double coste = 0.0;
	JRadioButton jcaminos, jzona;
	private static Graph graph = null;
	private String subred = null;
	static NetworkManager networkMgr;

	private static int	idnodo_start;
	private static VirtualNodeInfo virtualNodesInf;

	public boolean execute(PlugInContext context) throws Exception {
		if(context.getLayerViewPanel() == null)
			return false;
		this.source = null;
		this.coste = 0.0;
		this.graph = null;
		this.subred = null;

		this.networkMgr = FuncionesAuxiliares.getNetworkManager(context);
		this.configProperties = new CalcRutaConfigFileReaderWriter();
		configuatorNetworks = new HashMap<String, Network>();
		Set<String> redesSet = ((BasicNetworkManager) networkMgr).keySetredes();
		subredSet = new Vector<String>();
		this.context = context;

		if (context.getLayerManager().getLayer("Puntos para WIC") != null){
			context.getLayerManager().remove(pointLayer);
		}

		nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
				AttributeType.INTEGER);

		pointLayer = context.addLayer("Puntos WIC",
				"Puntos para WIC",
				nodesFeatureCol);

		ToolboxDialog toolbox = new ToolboxDialog(context.getWorkbenchContext());
		context.getLayerViewPanel().setCurrentCursorTool(RouteEngineWICDrawPointTool.create(toolbox.getContext()));
	
		return false;

	}

	private static void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {
		toRun(monitor, context);
	}

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.withincost.language.RouteEngine_WICi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("withincost",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);


	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(
				checkFactory
				.createWindowWithLayerManagerMustBeActiveCheck())
				.add(
						checkFactory
						.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
						.add(checkFactory.createTaskWindowMustBeActiveCheck()).add(
								checkFactory.createBlackBoardMustBeElementsCheck());
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

	}

	@SuppressWarnings("unchecked")
	public static void dibujarCaminos(String subred, Double coste,
			Collection<RoutePath> paths, TaskMonitor monitor) {
		monitor.report("Dibujando caminos...");
		FeatureCollection pathFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		FeatureCollection nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
				AttributeType.INTEGER);
		pathFeatureCol.getFeatureSchema().addAttribute("NombreSub",
				AttributeType.STRING);
		pathFeatureCol.getFeatureSchema().addAttribute("Camino",
				AttributeType.STRING);
		pathFeatureCol.getFeatureSchema().addAttribute("Tramo",
				AttributeType.INTEGER);
		pathFeatureCol.getFeatureSchema().addAttribute("nodeId_Origen",
				AttributeType.INTEGER);
		pathFeatureCol.getFeatureSchema().addAttribute("nodeId_Destino",
				AttributeType.INTEGER);
		pathFeatureCol.getFeatureSchema().addAttribute("Coste_Tramo",
				AttributeType.DOUBLE);
		pathFeatureCol.getFeatureSchema().addAttribute("Coste_Introducido",
				AttributeType.DOUBLE);

		for (Iterator<RoutePath> itr = paths.iterator(); itr.hasNext();) {
			Path p = (Path) itr.next();
			GeometryFactory fact1 = new GeometryFactory();
			List<Object> edges_camino = p.getEdges();
			if(edges_camino == null){
				edges_camino = new ArrayList();
				Iterator it = p.iterator();
				while(it.hasNext()){
					Node node =(Node)it.next();
					if(it.hasNext())
						edges_camino.add(node.getEdge((Node)it.next()));
				}
			}
			for (Iterator<Object> iter_nodes = p.iterator(); iter_nodes.hasNext();) {
				Node node = (Node) iter_nodes.next();
				Coordinate coord = ((XYNode) node).getCoordinate();
				Point geom_nodes = fact1.createPoint(coord);
				Feature feature = new BasicFeature(nodesFeatureCol
						.getFeatureSchema());
				feature.setGeometry(geom_nodes);
				feature.setAttribute("nodeId", new Integer(node.getID()));
				nodesFeatureCol.add(feature);
			}

			int pos_inicio = 0, pos_fin = 1, tramo = edges_camino.size();

			for (Iterator<Object> iter_edges = edges_camino.iterator(); iter_edges
			.hasNext();) {
				Edge edge_camino = (Edge) iter_edges.next();
				if(edge_camino instanceof ProxyEdge){
					edge_camino = ((Edge)((ProxyEdge)edge_camino).getGraphable());
				}
				Coordinate[] coords = NodeUtils.CoordenadasArco(
						edge_camino, p, pos_inicio, pos_fin);
				double edge_long = 0.0;
				if(!(edge_camino instanceof NetworkLink))
					edge_long = ((EdgeWithCost) edge_camino).getCost();
				LineString geom_edge = (LineString) fact1
				.createLineString(coords);
				Feature featureedges = new BasicFeature(pathFeatureCol
						.getFeatureSchema());
				featureedges.setGeometry(geom_edge);
				featureedges.setAttribute("NombreSub", new String(subred));
				featureedges.setAttribute("Camino", new String(p.getLast()
						.getID()
						+ "-" + p.getFirst().getID()));
				featureedges.setAttribute("Tramo", new Integer(tramo));
				featureedges.setAttribute("nodeId_Origen", new Integer(
						NodeUtils.id_start));
				featureedges.setAttribute("nodeId_Destino", new Integer(
						NodeUtils.id_final));
				featureedges.setAttribute("Coste_Tramo", new Double(edge_long));
				;
				featureedges.setAttribute("Coste_Introducido",
						new Double(coste));
				pathFeatureCol.add(featureedges);
				pos_inicio++;
				pos_fin++;
				if (pos_fin == p.size() + 1) {
					pos_fin = 0;
				}
				tramo--;

			}// fin for edges_camino

		}// fin de for path

		Layer edgesLayer = context.addLayer("WithinCost",
				"Camino - resultado ", pathFeatureCol);

		BasicStyle bs = new BasicStyle();
		bs.setLineColor(edgesLayer.getBasicStyle().getLineColor());
		bs.setLineWidth(4);
		bs.setEnabled(true);

		edgesLayer.addStyle(bs);

		Layer nodesLayer = context.addLayer("WithinCost", "Nodos Alcanzados",
				nodesFeatureCol);

	}// fin de caminos

	public void itemStateChanged(ItemEvent e) {
		boolean isSelected;
		isSelected = (e.getStateChange() == ItemEvent.SELECTED);
		if (e.getItemSelectable() == jcaminos) {
			if (isSelected == true) {
				System.out.print("caminos activado");
				jzona.setSelected(false);

			} else {
				System.out.print("caminos desactivada");
				jzona.setSelected(true);
				jcaminos.setSelected(false);
			}
		}

		if (e.getItemSelectable() == jzona) {
			if (isSelected == true) {
				System.out.print("zona activada");

				jzona.setSelected(true);
				jcaminos.setSelected(false);
			} else {
				System.out.print("zona desactivada");
				jcaminos.setSelected(true);
				jzona.setSelected(false);

			}

		}
	}

	public static void toRun(TaskMonitor monitor, PlugInContext context) throws PathNotFoundException, ElementNotFoundException{

		monitor.report("Calculando los nodos y caminos alcanzados..");

		String[] redes = configProperties.getRedesNames();
		// consigue el grafo de las features finalmente elegidas
		//		String red = listaredes.getSelectedItem().toString();
		Graph graphsubred = null;

		if (redes != null){
			if (networkMgr.getNetwork(redes[0]) != null ){
				graphsubred = networkMgr.getNetwork(redes[0]).getGraph();
			} else{
				Iterator it = networkMgr.getNetworks().keySet().iterator();
				while (it.hasNext()){
					String nombreRed = (String) it.next();
					if (networkMgr.getNetwork(nombreRed).getSubNetwork(redes[0]) != null){
						graphsubred = networkMgr.getNetwork(nombreRed).getSubNetwork(redes[0]).getGraph();
					}
				}
			}
		}
		if (graphsubred == null){
			VentanaError ventanaerror = new VentanaError(context);
			ventanaerror
			.addText("Error en la configuración. Inicie el Configurador de Rutas");
			ventanaerror.mostrar();
		} else {

			graph = graphsubred;


			String subred = redes[0];

			// consigo el coste de la ventana y lo utilizamos
			coste = Double.parseDouble(dialog.getCostField().getText().trim());
			if (coste < 0) {
				// Ventana avisando coste es menor que cero
				VentanaError ventanaerror = new VentanaError(context);
				ventanaerror.addText("El coste debe ser un numero positivo.");
				ventanaerror.mostrar();

			} else {
				int cnodos = 0;
				VirtualNode virtualNode = null;
				if (pointLayer != null){
					Iterator<Feature> it = pointLayer.getFeatureCollectionWrapper().getFeatures().iterator();
					int i = 0;
					while(it.hasNext()){
						Feature feature = it.next();
						virtualNode = ((LocalGISNetworkManager)networkMgr).addNewVirtualNode(
								(DynamicGraph) networkMgr.getNetwork(nodeInfo.getNetworkName()).getGraph(),
								nodeInfo.getEdge(),
								nodeInfo.getRatio(), 
								WithinCostPlugIn.class);
						i++;
						idnodo_start = virtualNode.getID();
//						source = virutalNode;
						cnodos = 1;
					}


				} else {
					final ArrayList<Object> featuresselected = new ArrayList<Object>(
							context.getLayerViewPanel().getSelectionManager()
							.getFeaturesWithSelectedItems());
					cnodos = featuresselected.size();
					Feature featureorigen = (Feature) featuresselected.get(0);

					idnodo_start = featureorigen.getInteger(1);
				}

				if (cnodos > 0){
					

					System.out.println("El coste introducido es " + coste);
					LocalGISWeighter weighter;

					AllInMemoryManager memmgr = new AllInMemoryManager();
					memmgr.setGraph(graph);
					DynamicGraph dyngraph = new DynamicGraph(memmgr);

					try{
						source=dyngraph.getNode(idnodo_start);
					}catch (ElementNotFoundException e) {
						// TODO: handle exception
						source = null;
						for (int i=0; i < redes.length; i++){
							memmgr = new AllInMemoryManager();
							memmgr.setGraph(networkMgr.getNetwork(getRedNameFromSubRed(redes[i])).getSubNetwork(redes[i]).getGraph());
							graph = new DynamicGraph(memmgr);
							try{
								source = (Node) dyngraph.getNode(idnodo_start);
							} catch (ElementNotFoundException e2) {
								// TODO: handle exception
							}
							subred = redes[i];
							if (source != null){
								break;
							}
						}	
					}

					if (source == null){
						VentanaError ventanaerror = new VentanaError(context);
						ventanaerror
						.addText("Alguno de los dos nodos seleccionados es incorrecto");
						ventanaerror.mostrar();
					} else{
						
//						ArrayList<IdEdgeNetworkBean> temporal = (ArrayList<IdEdgeNetworkBean>) aplicacion.getBlackboard().get("temporalincidents");
//						if (configProperties.getTipoVehiculo().equals("En Coche")){
//							if(temporal == null)
//								weighter = new LocalGISStreetWeighter();
//							else
//								weighter = new LocalGISStreetWeighter(temporal,networkMgr);
//						} else{
//							if(temporal == null)
//								weighter = new LocalGISWeighter();
//							else
//								weighter = new LocalGISWeighter(temporal,networkMgr);
//						}
						ArrayList<IdEdgeNetworkBean> temporal = (ArrayList<IdEdgeNetworkBean>) aplicacion.getBlackboard().get("temporalincidents");
						if(temporal == null)
							weighter = new LocalGISWeighter();
						else
							weighter = new LocalGISWeighter(temporal,networkMgr);

						HashSet<Graphable> prohibidos = new HashSet<Graphable>(1);
						prohibidos.add(nodeInfo.getEdge());
						
						HashSet<VirtualNode> permitidos = new HashSet<VirtualNode>(1);
						permitidos.add(virtualNode);

						
						
						WithinCostAlgorithm wca=((BasicNetworkManager)FuncionesAuxiliares.getNetworkManager(context)).computeExactWithinCostRoutes(source, coste, weighter, prohibidos, permitidos);
//						WithinCostAlgorithm wca=((BasicNetworkManager)FuncionesAuxiliares.getNetworkManager(context)).computeWithinCostRoutes(source, coste, weighter);

						Collection<RoutePath> paths=wca.getPaths();

						if (dialog.getDrawPathRadioButton().isSelected() == true) {

							dibujarCaminos(subred, coste, paths, monitor);
						}
						if (dialog.getDrawZoneRadioButton().isSelected() == true) {
							dibujarzona(paths, (XYNode) source, monitor);
						}

						context.getWorkbenchFrame().getOutputFrame().createNewDocument();
						context.getWorkbenchFrame().getOutputFrame().show();
						context.getWorkbenchFrame().getOutputFrame().addText(
								"Información WithinCost");
						context.getWorkbenchFrame().getOutputFrame().addText(
								"La Red utilizada es " + subred);
						context.getWorkbenchFrame().getOutputFrame().addText(
								"El nodo fuente es " + source.getID());
						context.getWorkbenchFrame().getOutputFrame().addText(
								"El coste introducido es: " + coste);
						context.getWorkbenchFrame().getOutputFrame().addText(
								"El tiempo empleado en ejecución es: " + wca.getRunTime()
								+ " milisegundos");
						context.getWorkbenchFrame().getOutputFrame().addText(
								"Nº de nodos visitados es: " + wca.getNumReachedNodes());
						context.getWorkbenchFrame().getOutputFrame().addText(
								"Nº de nodos alcanzados es: " + wca.getNumReachedNodes());
						// htmlframe.append("Los nodos alcanzados son: ");
					}
				} else{
					VentanaError ventanaerror = new VentanaError(context);
					ventanaerror
					.addText("Debe Seleccionar al menos un nodo.");
					ventanaerror.mostrar();
				}
			}
		}
		networkMgr.endUseOfVirtualNodes(WithinCostPlugIn.class);
	}

	@SuppressWarnings("unchecked")
	public static void dibujarzona(Collection<RoutePath> paths, XYNode inicio,
			TaskMonitor monitor) {
		monitor.report("Dibujando la zona abarcada ...");
		FeatureCollection nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
				AttributeType.INTEGER);
		GeometryFactory fact = new GeometryFactory();

		for (Iterator<RoutePath> itr = paths.iterator(); itr.hasNext();) {
			Path p = (Path) itr.next();
			for (Iterator<Object> iter_nodes = p.iterator(); iter_nodes
			.hasNext();) {
				Node node = (Node) iter_nodes.next();
				Coordinate coord = ((XYNode) node).getCoordinate();
				Point geom_nodes = fact.createPoint(coord);
				Feature feature = new BasicFeature(nodesFeatureCol
						.getFeatureSchema());
				feature.setGeometry(geom_nodes);
				nodesFeatureCol.add(feature);

			}

		}
		Coordinate coordinico = inicio.getCoordinate();
		Point geom_node = fact.createPoint(coordinico);
		Feature featurenodes = new BasicFeature(nodesFeatureCol
				.getFeatureSchema());
		featurenodes.setAttribute("nodeId", new Integer(inicio.getID()));
		featurenodes.setGeometry(geom_node);
		int size = nodesFeatureCol.size();
		if (size == 0) {
			VentanaError error = new VentanaError(context);
			error.addText("no hay nodos en la coleccion");
			error.mostrar();
		} else {
			int count = 0;
			Geometry[] geoms = new Geometry[size];

			for (Iterator<Object> i = nodesFeatureCol.iterator(); i.hasNext();) {
				Feature f = (Feature) i.next();
				Geometry geom = f.getGeometry();
				if (geom == null)
					continue;
				if (fact == null)
					fact = geom.getFactory();

				geoms[count++] = geom;
			}
			GeometryCollection gc = fact.createGeometryCollection(geoms);
			Geometry hull = gc.convexHull();
			List<Geometry> hullList = new ArrayList<Geometry>();
			hullList.add(hull);

			FeatureCollection hullFC = FeatureDatasetFactory
			.createFromGeometry(hullList);
//			hullFC.add(featurenodes);
			Layer zona = context.addLayer("Zona Cubierta", "Origen "
					+ inicio.getID() + " con coste " + coste, hullFC);

			BasicStyle bs = new BasicStyle();
			bs.setLineColor(zona.getBasicStyle().getLineColor());
			bs.setFillColor(zona.getBasicStyle().getLineColor().brighter().brighter());
			bs.setAlpha(100);
			bs.setLineWidth(4);
			bs.setEnabled(true);

			zona.addStyle(bs);

//			LabelStyle labelStyle = new LabelStyle();
//			labelStyle.setAttribute("nodeId");
//			labelStyle.setColor(Color.black);
//			labelStyle.setScaling(false);
//			labelStyle.setEnabled(true);
//			zona.addStyle(labelStyle);
		}

	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!WICRouteButtonAdded)
		{
			//			toolbox.addToolBar();
			WithinCostPlugIn explode = new WithinCostPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			WICRouteButtonAdded = true;
		}
	}

	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("withincost","routeengine.wic.iconfile"));
	}

	private static String getRedNameFromSubRed(String subred) {
		// TODO Auto-generated method stub
		Iterator it = networkMgr.getNetworks().keySet().iterator();
		while(it.hasNext()){
			String redName = (String) it.next(); 
			Iterator it2 = networkMgr.getNetwork(redName).getSubnetworks().keySet().iterator();
			while(it2.hasNext()){
				if (networkMgr.getNetwork(redName).getSubNetwork((String) it2.next()) != null){
					return redName;
				}
			}
		}
		return null;
	}

	public static UndoableCommand createAddCommand(Point point,
			RouteEngineWICDrawPointTool routeEngineWICDrawPointTool) {
		// TODO Auto-generated method stub
		final Point p = point;

		return new UndoableCommand(routeEngineWICDrawPointTool.getName()) {
			public void execute() {
				addSourceToRoute(p, false);
				dialog = new WithInCostDialog(context);

				if (dialog.wasOKPressed() == true) {
					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
					final PlugInContext runContext = context;
					progressDialog.setTitle("TaskMonitorDialog.Wait");
					progressDialog.report(I18N.get("withincost","routeengine.wic.taskmonitormessage"));
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

				}// fin del ok
				dialog.dispose();
			}

			public void unexecute() {

			}
		};
	}

	private static void addSourceToRoute(Point p, boolean b) {
		// TODO Auto-generated method stub
		String[] redes = configProperties.getRedesNames();
		GeometryFactory fact = new GeometryFactory();
		try {
			if (redes != null){

				NetworkManager networkMgr = FuncionesAuxiliares
				.getNetworkManager(context);

				CoordinateSystem coordSys =context.getLayerManager().getCoordinateSystem();				
				if (coordSys != null){
					p.setSRID(coordSys.getEPSGCode());
				}
				CoordinateReferenceSystem crs = CRS.decode("EPSG:"+coordSys.getEPSGCode());
				org.opengis.geometry.primitive.Point primitivePoint = GeographicNodeUtil.createISOPoint(p,crs);
				ExternalInfoRouteLN externalInfoRouteLN = new ExternalInfoRouteLN();
				ArrayList<VirtualNodeInfo> virtualNodesInfo = new ArrayList<VirtualNodeInfo>();
				for(int i = 0; i < redes.length; i++){

					configuatorNetworks.put(redes[i], ((LocalGISNetworkManager) networkMgr).getAllNetworks().get(redes[i]) );
					VirtualNodeInfo nodeInfo = null;
					try{
						nodeInfo = externalInfoRouteLN.getVirtualNodeInfo(new GeopistaRouteConnectionFactoryImpl(), 
								primitivePoint, 
								networkMgr, 
								redes[i], 
								100);
					}catch(Exception e){
						// En caso de no encontrarlo, no hacer nada. En el bucle debe encontrar uno
					}
					if (nodeInfo != null){
						virtualNodesInfo.add(nodeInfo);
					}
				}
				if(virtualNodesInfo.size() == 0)
					return;
				Iterator<VirtualNodeInfo> it = virtualNodesInfo.iterator();
				double lastDistante = -1;
				VirtualNodeInfo selectedNodeInfo = null;
				while(it.hasNext()){
					VirtualNodeInfo vNodeinfo = it.next();
					if (lastDistante == -1 || lastDistante > vNodeinfo.getDistance()){
						selectedNodeInfo = vNodeinfo;
						lastDistante = vNodeinfo.getDistance();
					}
				}

				nodeInfo = selectedNodeInfo;
				//				
				Coordinate coord = selectedNodeInfo.getLinestringVtoB().getCoordinateN(0);
				Point geom_nodes = fact.createPoint(coord);
				nodesFeatureCol = AddNewLayerPlugIn
				.createBlankFeatureCollection();
				nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
						AttributeType.INTEGER);
				Feature feature = new BasicFeature(nodesFeatureCol
						.getFeatureSchema());
				feature.setGeometry(geom_nodes);
				//				feature.setAttribute("nodeId", new Integer(node.getID()));
				feature.setAttribute("nodeId",selectedNodeInfo.hashCode());


				pointLayer.getFeatureCollectionWrapper().add(feature);

			} else {

			}
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		   		
	}


}