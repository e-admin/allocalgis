package com.geopista.ui.plugin.routeenginetools.tsp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.path.PathNotFoundException;
import org.uva.route.graph.path.RoutePath;
import org.uva.route.graph.path.WrongPathException;
import org.uva.route.graph.structure.VirtualNode;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.proxy.ProxyEdge;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.ElementNotFoundException;
import org.uva.routeserver.algorithms.TSPAlgorithm;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.EdgesFeatureCollections;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.ParejaOrigenDestino;
import com.geopista.ui.plugin.routeenginetools.routeutil.VentanaError;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.WriteRoutePathInformationUtil;
import com.geopista.ui.plugin.routeenginetools.tsp.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.weighter.LocalGISStreetWeighter;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.IdEdgeNetworkBean;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/*
 * Este plugin ejecuta el algoritmo Traveling-Sales-Man
 */

public class TSPAbiertoForzadoPlugIn extends ThreadedBasePlugIn implements ThreadedPlugIn,
ActionListener {

	PlugInContext context;
	Node source, end;
	Graph graph;
	double coste_ruta_total = 0.0;
	double coste_ruta = 0.0, coste_ruta_opt = Double.MAX_VALUE;
	int[] camino_opt;
	Vector<Path> tsp_final = new Vector<Path>();
	int rutas_examinadas = 0;
	int ruta_fallida = 0; // flag que advierte si la ruta es incorrecta
	Hashtable<ParejaOrigenDestino, Path> camino_orides = new Hashtable<ParejaOrigenDestino, Path>();
	Hashtable<Path, Double> cost_camino = new Hashtable<Path, Double>();
	int forzar_origen = 0;
	int[] col_nodos;
	String subred;
	Vector<String> subredSet;
	NetworkManager networkMgr;

	private CalcRutaConfigFileReaderWriter configProperties = null;

	private boolean tspButtonAdded = false;
	private ArrayList<VirtualNodeInfo> virtualNodesInf;
	private HashMap<Node, VirtualNodeInfo> nodeInfoVirtualList;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	public boolean execute(PlugInContext context) throws Exception {
		if(context.getLayerViewPanel() == null)
			return false;
		configProperties = new CalcRutaConfigFileReaderWriter();
		if(configProperties.getRedesNames() == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.emptyconfiguration"));
			return false;
		}
		// inicializa las variables
		this.coste_ruta_total = 0.0;// almacena el coste total de un conjunto de
		// rutas
		this.coste_ruta = 0.0;// almacena el coste de una ruta
		this.coste_ruta_opt = Double.MAX_VALUE;// guarda el coste de la ruta
		// optima,
		// inicialmente tendra el maximo valor double
		this.tsp_final = new Vector<Path>();// recoge el camino final
		this.rutas_examinadas = 0;// nï¿½mero de rutas examinadas
		this.networkMgr = FuncionesAuxiliares.getNetworkManager(context);

		Set<String> redesSet = networkMgr.getNetworks().keySet();// obtener lista de
		// redes ya calculadas
		subredSet = new Vector<String>();
		this.context = context;
		this.virtualNodesInf = new ArrayList<VirtualNodeInfo>();
		this.nodeInfoVirtualList = new HashMap<Node, VirtualNodeInfo>();


		EnableCheck[] check = null;

		Layer tspPointsLayer = context.getLayerManager().getLayer("Puntos para TSP");
		if (tspPointsLayer != null){
			Iterator<Feature> it = tspPointsLayer.getFeatureCollectionWrapper().getFeatures().iterator();
			this.camino_opt = new int[tspPointsLayer.getFeatureCollectionWrapper().getFeatures().size()];
			int i = 0;
			while(it.hasNext()){
				Feature feature = it.next();
				VirtualNodeInfo nodeInfo = TSPClickPlugIn.getNodesInfo().get(feature.getAttribute("nodeId"));
				virtualNodesInf.add(nodeInfo);
				VirtualNode virutalNode = ((LocalGISNetworkManager)networkMgr).addNewVirtualNode(
						(DynamicGraph) networkMgr.getNetwork(nodeInfo.getNetworkName()).getGraph(),
						nodeInfo.getEdge(),
						nodeInfo.getRatio(), 
						TSPAbiertoForzadoPlugIn.class);
				this.nodeInfoVirtualList.put(virutalNode,nodeInfo);
				this.camino_opt[i] = virutalNode.getID();
				i++;
			}


		} else {
			
			String[] redes = this.configProperties.getRedesNames();

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
				return false;
			}

			subred = redes[0];
			//			if ( ((BasicNetworkManager) networkMgr).getNetwork(redes[0]) != null){
			//				graph = ((BasicNetworkManager) networkMgr).getNetwork(redes[0]).getGraph();
			//			} else{
			//				graph = ((BasicNetworkManager) networkMgr).getSubred(getRedNameFromSubRed(redes[0]), subred).getGraph();
			//			}

			if (networkMgr.getNetwork(redes[0]) != null ){
				graph = networkMgr.getNetwork(redes[0]).getGraph();
			} else{
				Iterator it = networkMgr.getNetworks().keySet().iterator();
				while (it.hasNext()){
					String nombreRed = (String) it.next();
					if (networkMgr.getNetwork(nombreRed).getSubNetwork(redes[0]) != null){
						graph = networkMgr.getNetwork(nombreRed).getSubNetwork(redes[0]).getGraph();
					}
				}
			}

			final ArrayList<Object> featuresselected = new ArrayList<Object>(
					context.getLayerViewPanel().getSelectionManager()
					.getFeaturesWithSelectedItems());

			int cnodos = featuresselected.size();
			this.camino_opt = new int[cnodos];
			for (int i = 0; i < cnodos; i++){
				camino_opt[i] = ((Feature) featuresselected.get(i)).getInteger(1);
			}
		}
		col_nodos = camino_opt;
		if (col_nodos.length < 3) {// si se introducieron menos de 3 nodos
			VentanaError ventanaerror = new VentanaError(context);
			ventanaerror.addText("Introduzca al menos 3 nodos");
			ventanaerror.mostrar();
			return false;

		} else {

			forzar_origen = 1;
			final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
			final PlugInContext runContext = this.context;
			progressDialog.setTitle("TaskMonitorDialog.Wait");
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
			
			if (!nodeInfoVirtualList.isEmpty()){
				((LocalGISNetworkManager)networkMgr).endUseOfVirtualNodes(TSPAbiertoForzadoPlugIn.class);
			}
			return false;
		}

	}// fin de execute

	private void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {
		// TODO Auto-generated method stub
		run(monitor, context);
	}
	
	public void initialize(PlugInContext context) throws Exception {
		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.tsp.language.RouteEngine_TSPi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("tsp",bundle);


		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);

	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createBlackBoardMustBeElementsCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));

	}

	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();// getSource recoge quï¿½ objeto fue pulsado

	}

	/*
	 * cadenaToVector recibe una cadena en la cual cada elemento estara separado
	 * por "," y la transforma en un vector de enteros que serï¿½ el parametro
	 * devuelto
	 */

	public int[] cadenaToVector(String cadenadenodos) {

		String[] col_nodos = null;
		// consigo del cuadro de text el entero que representa la ID del nodo
		// origen

		col_nodos = cadenadenodos.split(",");
		int[] int_nodos = new int[col_nodos.length];
		for (int i = 0; i < col_nodos.length; i++) {
			int_nodos[i] = Integer.parseInt(col_nodos[i]);
		}
		return (int_nodos);
	}

	@SuppressWarnings("unchecked")
	public void dibujarCaminos(String subred, List<RoutePath> list,
			double coste_ruta_opt, TaskMonitor monitor) {
		monitor.report("Imprimiendo Resultados...");
		FeatureCollection pathFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		FeatureCollection nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
//		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
//				AttributeType.INTEGER);
		VirtualNodeInfo startNodeInfo = null;
		VirtualNodeInfo endNodeinfo = null;
		int recorrerNodesInfo = 0;

		for (Iterator<RoutePath> itr = list.iterator(); itr.hasNext();) {
			
			Path p = (Path) itr.next();
			int nodeid = p.getFirst().getID();
			startNodeInfo = virtualNodesInf.get(0);
			endNodeinfo = virtualNodesInf.get(0+1);
//			virtualNodesInf.get(0).

			GeometryFactory fact1 = new GeometryFactory();
			List<Object> edges_camino = p.getEdges();

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
			pathFeatureCol.getFeatureSchema().addAttribute("Coste_Ruta",
					AttributeType.DOUBLE);
			pathFeatureCol.getFeatureSchema().addAttribute("Coste_Ruta_Total",
					AttributeType.DOUBLE);

			// Dibujo nodos
			for (Iterator<Object> iter_nodes = p.iterator(); iter_nodes
			.hasNext();) {
				Node node = (Node) iter_nodes.next();
				Coordinate coord = ((XYNode) node).getCoordinate();
				Point geom_nodes = fact1.createPoint(coord);
				Feature feature = new BasicFeature(nodesFeatureCol
						.getFeatureSchema());
				feature.setGeometry(geom_nodes);
//				feature.setAttribute("nodeId", new Integer(node.getID()));
				nodesFeatureCol.add(feature);
			}

			// Dibujo edges

			IGeopistaLayer layer = null;
			FeatureCollection edgesFeatureCol = null;

			int pos_inicio = 0, pos_fin = 1, tramo = edges_camino.size();
			int lastIdLayer = 0;
			for (Iterator<Object> iter_edges = edges_camino.iterator(); iter_edges
			.hasNext();) {
				Edge edge_camino = (Edge) iter_edges.next();
				if (edge_camino instanceof ProxyEdge){
					edge_camino = (Edge)((ProxyEdge) edge_camino).getGraphable();
				}
				if (edgesFeatureCol == null){
					if (edge_camino instanceof LocalGISDynamicEdge){
						edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
					} else if (edge_camino instanceof LocalGISStreetDynamicEdge){
						edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
					}else if (edge_camino instanceof NetworkLink){
						FeatureCollection networkLinkFeatureCol = AddNewLayerPlugIn
						.createBlankFeatureCollection();
						
						networkLinkFeatureCol.getFeatureSchema().addAttribute("idEje",
								AttributeType.INTEGER);
						networkLinkFeatureCol.getFeatureSchema().addAttribute("coste",
								AttributeType.DOUBLE);
						edgesFeatureCol = networkLinkFeatureCol;
					} else if (edge_camino instanceof DynamicEdge){
						edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
					}
				}
				
				if (edge_camino instanceof ILocalGISEdge){

					if (lastIdLayer != ((ILocalGISEdge) edge_camino).getIdLayer()){

						layer = getOriginalLayer(edge_camino);
						if (layer != null){
							lastIdLayer = layer.getId_LayerDataBase();
						} else{
							lastIdLayer = ((ILocalGISEdge) edge_camino).getIdLayer();
						}
					}

					ArrayList<Object> originalFeatures = new ArrayList<Object>();

					if (layer != null){
						originalFeatures = new ArrayList<Object>(layer.getFeatureCollectionWrapper().
								getWrappee().getFeatures());
					}

					Iterator it = originalFeatures.iterator();

					Feature featureedges = null;
					GeopistaFeature f2 = null;
					while (it.hasNext()){
						f2 = (GeopistaFeature) it.next();
						if (Integer.parseInt(f2.getSystemId()) == ((ILocalGISEdge)edge_camino).getIdFeature()){
							featureedges = f2;
							break;
						}
					}

					if (featureedges != null){
						edgesFeatureCol.add(featureedges);
						pathFeatureCol.add(featureedges);
					} else{
						featureedges = new BasicFeature(edgesFeatureCol.getFeatureSchema());

						Coordinate[] coords = NodeUtils.CoordenadasArco(
								edge_camino, p, pos_inicio, pos_fin);
						LineString ls = (LineString) fact1.createLineString(coords);

						featureedges.setGeometry(ls);

						edgesFeatureCol.add(featureedges);
						pathFeatureCol.add(featureedges);
					}
					
				}else if (edge_camino instanceof NetworkLink){
					Feature featureedges1 = new BasicFeature(edgesFeatureCol.getFeatureSchema());
					Coordinate[] coords = NodeUtils.CoordenadasArco(
							edge_camino, p, pos_inicio, pos_fin);
					LineString ls = (LineString) fact1.createLineString(coords);

					featureedges1.setGeometry(ls);
					edgesFeatureCol.add(featureedges1);
					pathFeatureCol.add(featureedges1);
				}
				pos_inicio++;
				pos_fin++;
				if (pos_fin == p.size() + 1) {
					pos_fin = 0;
				}
				tramo--;
			}// fin del for de edges

			if (p != null && p.isValid()){
				Node firstNode = p.getFirst();
				Node lastNode = p.getLast();
				VirtualNodeInfo firstNodeInfo = nodeInfoVirtualList.get(firstNode);
				VirtualNodeInfo lastNodeInfo = nodeInfoVirtualList.get(lastNode);
				LineString init = null;
				LineString end = null;
				
				if (p.getEdges().size() > 2){
					Feature firstFeature = null;
					Feature lastFeature = null;
					firstFeature = (Feature) edgesFeatureCol.getFeatures().get(0);
					lastFeature = (Feature) edgesFeatureCol.getFeatures().get(edgesFeatureCol.size() -1);

					if (firstFeature.getGeometry().distance(firstNodeInfo.getLinestringAtoV()) >
					firstFeature.getGeometry().distance(firstNodeInfo.getLinestringVtoB()) ){
						init = firstNodeInfo.getLinestringVtoB();
					}else{
						init = firstNodeInfo.getLinestringAtoV();
					}

					if (lastFeature.getGeometry().distance(lastNodeInfo.getLinestringAtoV()) >
					lastFeature.getGeometry().distance(lastNodeInfo.getLinestringVtoB()) ){
						end = lastNodeInfo.getLinestringVtoB();
					}else{
						end = lastNodeInfo.getLinestringAtoV();
					}


				} else {
					if (firstNodeInfo.getLinestringAtoV().distance(lastNodeInfo.getLinestringAtoV()) == 0){
						init = firstNodeInfo.getLinestringAtoV();
						end = lastNodeInfo.getLinestringAtoV();
					} else if (firstNodeInfo.getLinestringAtoV().distance(lastNodeInfo.getLinestringVtoB()) == 0){
						init = firstNodeInfo.getLinestringAtoV();
						end = lastNodeInfo.getLinestringVtoB();
					} else if (firstNodeInfo.getLinestringVtoB().distance(lastNodeInfo.getLinestringAtoV()) == 0){
						init = firstNodeInfo.getLinestringVtoB();
						end = lastNodeInfo.getLinestringAtoV();
					} else if (firstNodeInfo.getLinestringVtoB().distance(lastNodeInfo.getLinestringVtoB()) == 0){
						init = firstNodeInfo.getLinestringVtoB();
						end = lastNodeInfo.getLinestringVtoB();
					}
				}
				
				Feature startVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
				startVirtualNodeFeature.setGeometry(init);
				edgesFeatureCol.add(startVirtualNodeFeature);
				pathFeatureCol.add(startVirtualNodeFeature);

				Feature endVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
				endVirtualNodeFeature.setGeometry(end);
				edgesFeatureCol.add(endVirtualNodeFeature);
				pathFeatureCol.add(endVirtualNodeFeature);

			}

//				Feature firstFeature = (Feature) edgesFeatureCol.getFeatures().get(0);
//				Feature startVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
//
//				double distanciaAV = startNodeInfo.getLinestringAtoV().distance(firstFeature.getGeometry());
//				double distanciaVB = startNodeInfo.getLinestringVtoB().distance(firstFeature.getGeometry());
//				if (distanciaAV < distanciaVB){
//					startVirtualNodeFeature.setGeometry(startNodeInfo.getLinestringAtoV());
//					distanciaInicio = startNodeInfo.getLinestringAtoV().getLength();
//				}else{
//					startVirtualNodeFeature.setGeometry(startNodeInfo.getLinestringVtoB());
//					distanciaInicio = startNodeInfo.getLinestringVtoB().getLength();
//				}
//				edgesFeatureCol.add(startVirtualNodeFeature);
//
//
//
//				Feature lastFeature = (Feature) edgesFeatureCol.getFeatures().get(edgesFeatureCol.size() -1);
//				Feature endVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
//
//				distanciaAV = endNodeinfo.getLinestringAtoV().distance(lastFeature.getGeometry());
//				distanciaVB = endNodeinfo.getLinestringVtoB().distance(lastFeature.getGeometry());
//				if (distanciaAV < distanciaVB){
//					endVirtualNodeFeature.setGeometry(endNodeinfo.getLinestringAtoV());
//					distanciaFinal = endNodeinfo.getLinestringAtoV().getLength();
//				}else{
//					endVirtualNodeFeature.setGeometry(endNodeinfo.getLinestringVtoB());
//					distanciaFinal = endNodeinfo.getLinestringVtoB().getLength();
//				}
//				edgesFeatureCol.add(endVirtualNodeFeature);
//			}

			Layer edgesLayer = context.addLayer("TSP", "Camino "
					+ p.getLast().getID() + "-" + p.getFirst().getID(),
					pathFeatureCol);

			BasicStyle bs = new BasicStyle();
			bs.setLineColor(edgesLayer.getBasicStyle().getLineColor());
			bs.setLineWidth(4);
			bs.setEnabled(true);

			edgesLayer.addStyle(bs);


			pathFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();

		}// fin for paths
		Layer nodesLayer = context.addLayer("TSP",
				"Nodos visitados en la ruta cïclica", nodesFeatureCol);
//		LabelStyle labelStyle = new LabelStyle();
//		labelStyle.setAttribute("nodeId");
//		labelStyle.setColor(Color.black);
//		labelStyle.setScaling(false);
//		labelStyle.setEnabled(true);
//		nodesLayer.addStyle(labelStyle);

	}// fin dibujar caminos

	/*
	 * params camino vector de enteros donde se encuentras las IDs de los nodos
	 * start es el ï¿½ndice de un elemento en camino desde el cual se realizaran
	 * todas las permutaciones posibles n nï¿½mero de elemento de camino
	 */

	public void permute(int[] camino, int start, int n)
	throws WrongPathException, ElementNotFoundException {

		int[] componer_ruta = new int[camino.length];
		rutas_examinadas++;

		// "...Calculando la ruta cï¿½clica");
		System.out.println("");
		System.out.print("La Ruta examinada es: ");
		for (int z = 0; z < camino.length; z++) {
			System.out.print(camino[z] + " ");
		}
		System.out.println("");
		Vector<Path> tsp = new Vector<Path>();
		int nodo_start = -1, nodo_end = -1;// se inicializa la variables
		// auxiliares para guardar la ID de
		// los nodos
		Collection<Node> nodes = (Collection<Node>) graph.getNodes();

		// almacenamos en nodo_start y en nodo_end la componente i e i+1
		// para conseguir cada par de nodos y crear el camino minimo entre ellos

		for (int w = 0; w < camino.length; w++) {
			componer_ruta[w] = camino[w];
			nodo_start = camino[w];

			if (w + 1 != camino.length) {
				nodo_end = camino[w + 1];

			} else {
				nodo_end = camino[0];

			}

			// find a source node
			NodeUtils.nodoOrigenyDestino(nodes, nodo_start, nodo_end);
			source = NodeUtils.getOrigen();
			end = NodeUtils.getDestino();

			if (source == null || end == null) {
				ruta_fallida = 1;

			} else {
				Path path = camino_orides.get(new ParejaOrigenDestino(
						nodo_start, nodo_end));
				if (path == null) {
					
					TSPAlgorithm tspAlg = new TSPAlgorithm(networkMgr, path, true, null);

					camino_orides.put(new ParejaOrigenDestino(nodo_start,
							nodo_end), path);
					cost_camino.put(path, tspAlg.getCostOfFinalRoute());

				}// fin del if_null
				tsp.add(path);
				coste_ruta = coste_ruta + cost_camino.get(path);
			}// del else
		}// del for
		coste_ruta_total = coste_ruta;
		if (coste_ruta_total < coste_ruta_opt) {
			for (int a = 0; a < camino.length; a++) {
				camino_opt[a] = componer_ruta[a];
			}
			coste_ruta_opt = coste_ruta_total;
			tsp_final = tsp;

		}
		coste_ruta = 0.0;
		tsp = new Vector<Path>();

		int tmp = 0;

		if (start < n) {
			for (int i = n - 2; i >= start; i--) {
				for (int j = i + 1; j < n; j++) {
					// swap ps[i] <--> ps[j]
					tmp = camino[i];
					camino[i] = camino[j];
					camino[j] = tmp;
					permute(camino, i + 1, n);
				}

				// Undo all modifications done by
				// recursive calls and swapping
				tmp = camino[i];
				for (int k = i; k < n - 1;)
					camino[k] = camino[++k];
				camino[n - 1] = tmp;
			}
		}

	}// del mï¿½todo

	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		monitor.report("Calculando la ruta cíclica...");

		int tinicial = (int) System.currentTimeMillis();// tiempo inicial

		LocalGISStreetWeighter weighter = null;
		ArrayList<IdEdgeNetworkBean> temporal = (ArrayList<IdEdgeNetworkBean>) aplicacion.getBlackboard().get("temporalincidents");
		if(temporal == null)
			weighter = new LocalGISStreetWeighter();
		else
			weighter = new LocalGISStreetWeighter(temporal,networkMgr);
		TSPAlgorithm algorithm = null;
		try{
			algorithm =((LocalGISNetworkManager)this.networkMgr).findTSPRoutes(
				new ArrayList<Node>(nodeInfoVirtualList.keySet()), 
				false, 
				weighter);
		}catch (PathNotFoundException e){
			JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), I18N.get("calcruta","routeengine.calcularruta.errormessage.pathnotfound"));
			return;
		}
		int tfinal = (int) System.currentTimeMillis();
		int templeado = tfinal - tinicial;
		if (ruta_fallida != 0) {

			VentanaError ventanaerror = new VentanaError(context);
			ventanaerror
			.addText("Alguno de los dos nodos seleccionados es incorrecto");
			ventanaerror.mostrar();
			ruta_fallida = 0;
		} else {

			List<RoutePath> paths = algorithm.getTSP();
			//eliminamos el ultimo elemento de la ruta
			paths.remove(paths.size()-1);
			
			dibujarCaminos(subred, paths, coste_ruta_opt, monitor);
			
			
			// Escribir rutas de TSP
			context.getWorkbenchFrame().getOutputFrame().createNewDocument();
			context.getWorkbenchFrame().getOutputFrame().show();
			for(int i=0; i < paths.size(); i++){
//			for (Iterator<RoutePath> itr = algorithm.getTSP().iterator(); itr.hasNext();){ç
				monitor.report("Generando informe ruta: " + (i+1));
				Path path = paths.get(i);
				VirtualNodeInfo startNodeInfo = null;
				VirtualNodeInfo endNodeInfo=null;
				if (path!=null && path.isValid() && !path.isEmpty()){
					try{
						startNodeInfo = this.virtualNodesInf.get(i);
						endNodeInfo = null;
						if ((i+1) >= paths.size()){
							endNodeInfo = this.virtualNodesInf.get(0);
						} else{
							endNodeInfo = this.virtualNodesInf.get(i+1);
						}
					}catch (Exception e) {
						// TODO: handle exception
					}
					context.getWorkbenchFrame().getOutputFrame().append("<b>Camino " + (i+1) + "</b>");
					if (startNodeInfo!=null && endNodeInfo!=null){
						WriteRoutePathInformationUtil.writeRoutePathInformation(context.getWorkbenchFrame().getOutputFrame(),
								path.getEdges(), path, context, startNodeInfo, endNodeInfo);
					}
				}
			}

		}

	}

	public ImageIcon getIcon() {
		return IconLoader.icon("tsp_forced.png");
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!tspButtonAdded  )
		{
//			toolbox.addToolBar();
			TSPAbiertoForzadoPlugIn explode = new TSPAbiertoForzadoPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			tspButtonAdded = true;
		}
	}

	private IGeopistaLayer getOriginalLayer(Edge edge_camino) {
		IGeopistaLayer layer;
		GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
		Map properties = new HashMap();
		//Introducimos el mapa Origen
		properties.put("mapadestino",(GeopistaMap) context.getTask());
		//Introducimos el fitro geometrico si es distinto de null, si se introduce null falla

		//Introducimos el FilterNode
		properties.put("nodofiltro",FilterLeaf.equal("1",new Integer(1)));
		//Introducimos el srid del mapa destino
		properties.put("srid_destino", Integer.valueOf(context.getLayerManager().getCoordinateSystem().getEPSGCode()));

		serverDataSource.setProperties(properties);
		GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource.getConnection();

		//Creamos una coleccion para almacenar las excepciones que se producen
		Collection exceptions = new ArrayList();
		//preparamos la url de la layer
		String layerID = "tramosvia";
		RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
		layerID = getQueryFromIdLayer(connectionFactory.getConnection(), ((ILocalGISEdge) edge_camino).getIdLayer());
		URL urlLayer = null;
		try {
			urlLayer = new URL("geopistalayer://default/"+ layerID);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		geopistaConnection.executeQuery(urlLayer.toString(),exceptions,null);
		layer = geopistaConnection.getLayer(); 


		return layer;
	}

	public String getQueryFromIdLayer(Connection con, int idLayer) {

		String unformattedQuery = "";
		String sqlQuery = "select name from layers where id_layer = " + idLayer;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idLayer);
			rs = preparedStatement.executeQuery ();

			if(rs.next()){ 
				unformattedQuery = rs.getString("name");
			}
			preparedStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return unformattedQuery;

	}

	private String getRedNameFromSubRed(String subred) {
		// TODO Auto-generated method stub
		Iterator it = this.networkMgr.getNetworks().keySet().iterator();
		while(it.hasNext()){
			String redName = (String) it.next(); 
			Iterator it2 = this.networkMgr.getNetwork(redName).getSubnetworks().keySet().iterator();
			while(it2.hasNext()){
				if (this.networkMgr.getNetwork(redName).getSubNetwork((String) it2.next()) != null){
					return redName;
				}
			}
		}
		return null;
	}
}