package com.geopista.ui.plugin.routeenginetools.leerreddebase;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.uva.geotools.graph.io.standard.AbstractReaderWriter;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import com.localgis.route.graph.build.UIDgenerator.FixedValueUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.build.dynamic.GeographicGraphGenerator;
import org.uva.route.graph.io.DBRouteServerReaderWriter;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.geographic.GeographicNode;
import org.uva.route.graph.structure.geographic.GeographicNodeWithTurnImpedances;
import org.uva.route.graph.structure.impedance.TurnImpedances;
import org.uva.route.graph.structure.proxy.ProxyNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicInterNetworker;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.ElementNotFoundException;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.routeenginetools.leerreddebase.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.DialogForDataStorePlugin;
import com.geopista.ui.plugin.routeenginetools.routeutil.EdgesFeatureCollections;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToLoadFromDataStore;
import com.localgis.route.graph.build.dynamic.LocalGISGraphGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphGenerator;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISTurnImpedance;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datastore.ConnectionManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class LeerRedDeBasePlugIn extends DialogForDataStorePlugin {
	Graph graph;

	private Layer capadebase;

	private boolean saveRedDatabaseButtonAdded;
	private boolean bPMRGraph = false;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	public boolean execute(final PlugInContext context) {

		return super.execute(context);
	}

	public String getName() {
		return I18N
				.get("leerredbase","AddDatastoreLayerPlugIn");
	}

	private Layer createLayer(final PanelToLoadFromDataStore panel,
			final PlugInContext context) throws Exception {


		if (graph != null){
			NetworkManager networkMgr = FuncionesAuxiliares
			.getNetworkManager(context);

			if (((BasicNetworkManager)networkMgr).getInterNetworker() == null ){
				BasicInterNetworker binNet = new BasicInterNetworker();
				binNet.setNetworkManager(networkMgr);
				networkMgr.setInterNetworker(binNet);
			}


			Network parent = networkMgr.putNetwork("RedBaseDatos", null);
			RouteConnectionFactory routeConnectionFactory = new GeopistaRouteConnectionFactoryImpl();
			((LocalGISNetworkManager) networkMgr).putSubNetworkWithProperties(parent, panel.getSubredSelected(), graph, routeConnectionFactory);
			Collection edges = graph.getEdges();


			GeometryFactory fact = new GeometryFactory();
			FeatureCollection edgesFeatureCol = null;
			;
			// creo capas con los arcos
			for (Iterator iter_edges = edges.iterator(); iter_edges.hasNext();) {
				Edge edge = (Edge) iter_edges.next();
				Coordinate[] coords = NodeUtils.CoordenadasArco(edge,
						null, 0, 0);

				if (edgesFeatureCol == null){
					if (edge instanceof LocalGISDynamicEdge){
						edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
					}else if(edge instanceof LocalGISStreetDynamicEdge){
						edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
						System.out.println("Carga de atributos PMR "+(edge instanceof PMRLocalGISStreetDynamicEdge));
						if(edge instanceof PMRLocalGISStreetDynamicEdge)
							edgesFeatureCol = EdgesFeatureCollections.getPMRLocalGISStreetDynamicEdgeFeatureCollection(edgesFeatureCol);
					}else if (edge instanceof DynamicEdge) {
						edgesFeatureCol = EdgesFeatureCollections.getDynamicEdgeFeatureCollection();
					}else if (edge instanceof Edge){
						edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection();
					}
				}

//				if (edge instanceof LocalGISDynamicEdge){
//					if (edgesFeatureCol == null){
//						edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
//					}
//					Feature feature = new BasicFeature(edgesFeatureCol
//							.getFeatureSchema());
//					feature.setAttribute("idEje", new Integer(edge.getID()));
//					feature.setAttribute("coste", ((LocalGISDynamicEdge) edge).getCost());
//					LineString geom_edge = fact.createLineString(coords);
//					feature.setGeometry(geom_edge);
//					edgesFeatureCol.add(feature);
//				} else if (edge instanceof LocalGISStreetDynamicEdge){
//					if (edgesFeatureCol == null){
//						edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
//					}
//					Feature feature = new BasicFeature(edgesFeatureCol
//							.getFeatureSchema());
//					feature.setAttribute("idEje", new Integer(edge.getID()));
//					feature.setAttribute("coste", ((LocalGISStreetDynamicEdge) edge).getCost());
//					LineString geom_edge = fact.createLineString(coords);
//					feature.setGeometry(geom_edge);
//					edgesFeatureCol.add(feature);
//				}

				Feature feature = new BasicFeature(edgesFeatureCol
						.getFeatureSchema());
				feature.setAttribute("idEje", new Integer(edge.getID()));
				feature.setAttribute("coste", new Double(
						((EdgeWithCost) edge).getCost()));
				feature.setAttribute("idNodoA", edge.getNodeA().getID());
				feature.setAttribute("idNodoB", edge.getNodeB().getID());
				if (edge instanceof DynamicEdge){
					feature.setAttribute("impedanciaAB", ((DynamicEdge) edge).getImpedance(edge.getNodeA()).getCost(1));
					feature.setAttribute("impedanciaBA", ((DynamicEdge) edge).getImpedance(edge.getNodeB()).getCost(1));
					feature.setAttribute("costeEjeDinamico", ((DynamicEdge) edge).getCost());
				}
				if (edge instanceof ILocalGISEdge){
					feature.setAttribute("longitudEje", ((ILocalGISEdge) edge).getEdgeLength());
					feature.setAttribute("idFeature", ((ILocalGISEdge) edge).getIdFeature());
					feature.setAttribute("idCapa", ((ILocalGISEdge) edge).getIdLayer());
				}
				if (edge instanceof LocalGISStreetDynamicEdge){
					feature.setAttribute("regulacionTrafico", ((LocalGISStreetDynamicEdge) edge).getTrafficRegulation().toString());
					feature.setAttribute("maxVelocidadNominal", RedondearVelocidad(((LocalGISStreetDynamicEdge) edge).getNominalMaxSpeed() * 3600.0 / 1000.0));
					feature.setAttribute("pintadaRegulacionTrafico", 0);
					if (edge instanceof PMRLocalGISStreetDynamicEdge){
						feature.setAttribute("anchuraAcera", ((PMRLocalGISStreetDynamicEdge) edge).getWidth());
						feature.setAttribute("pendienteTransversal", ((PMRLocalGISStreetDynamicEdge) edge).getTransversalSlope());
						feature.setAttribute("pendienteLongitudinal", ((PMRLocalGISStreetDynamicEdge) edge).getLongitudinalSlope());
						feature.setAttribute("tipoEje", ((PMRLocalGISStreetDynamicEdge) edge).getsEdgeType());
						feature.setAttribute("alturaObstaculo", ((PMRLocalGISStreetDynamicEdge) edge).getObstacleHeight());
						feature.setAttribute("ejeRelacionadoConId", ((PMRLocalGISStreetDynamicEdge) edge).getRelatedToId());
						if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.LEFT)
							feature.setAttribute("ladoAcera", "L");
						else if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.RIGHT)
							feature.setAttribute("ladoAcera", "R");
						if (edge instanceof ZebraDynamicEdge)
							feature.setAttribute("tipoPasoCebra", ((ZebraDynamicEdge) edge).getsType());
					}
				}

				LineString geom_edge = fact.createLineString(coords);
				feature.setGeometry(geom_edge);
				edgesFeatureCol.add(feature);

			}

			if (edgesFeatureCol == null){
				edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection();
			}
			Layer edgesLayer = context.addLayer("Red de BBDD", panel
					.getSubredSelected(), edgesFeatureCol);
			LabelStyle labelStylenew = new LabelStyle();
			labelStylenew.setAttribute("coste");
			labelStylenew.setColor(Color.red);
			labelStylenew.setScaling(false);
			// labelStyle.setEnabled(true);
			edgesLayer.addStyle(labelStylenew);
			return edgesLayer;
		} else{
			JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(),
					"Error al leer Red de la Base de Datos. No se ha encontrado ninguna Red en la Base de Datos");
			return null;
		}

	}

	private double RedondearVelocidad(double speed)
	{
		try{
			return Math.round(speed*Math.pow(10,2))/Math.pow(10,2);
		}catch (Exception e) {
			return speed;
		}
	}

	protected PanelToLoadFromDataStore createPanel(PlugInContext context) {
		return new PanelToLoadFromDataStore(context.getWorkbenchContext());
	}

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.leerreddebase.language.RouteEngine_LeerRedBasei18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("leerredbase",bundle);

		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(this.aplicacion.getI18nString("ui.MenuNames.TOOLS.ROUTEENGINEADMINISTRATION")).addPlugIn(
				this.getIcon(),
                this,
                createEnableCheck(context.getWorkbenchContext()),
                context.getWorkbenchContext());

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
				.add(checkFactory.createTaskWindowMustBeActiveCheck());
	}

	protected boolean leerGrafodeBase(PanelToLoadFromDataStore panel,
			TaskMonitor monitor, PlugInContext context) throws Exception {

		bPMRGraph = false;
		if (panel.getSubredSelected().trim().endsWith("PMR"))
			bPMRGraph = true;

		monitor.allowCancellationRequests();
		monitor.report("Cargando grafo de base de datos");

		ConnectionManager.instance(context.getWorkbenchContext());



		if (!AppContext.getApplicationContext().isLogged()){
			AppContext.getApplicationContext().login();
		}
		try {
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			DBRouteServerReaderWriter db = null;
			GeographicGraphGenerator graphGenerator = null;

			if (isStreetNetwork(panel.getSubredSelected())){

				graphGenerator = new LocalGISStreetGraphGenerator();
				db = new LocalGISStreetRouteReaderWriter(connectionFactory, bPMRGraph);
			} else{

				graphGenerator = new LocalGISGraphGenerator();
				db = new LocalGISRouteReaderWriter(connectionFactory);
			}

			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);

			db.setNetworkName(panel.getSubredSelected());

			SpatialAllInMemoryExternalSourceMemoryManager manager = new SpatialAllInMemoryExternalSourceMemoryManager(db);
            graph = new DynamicGraph(manager);
            //TODO: RUBENGOMEZ -- Si se agregan los nodos directamente al cargar, reescribe el nodo y lo cambia de id.
            if(graphGenerator instanceof LocalGISStreetGraphGenerator){
            	if (db instanceof LocalGISStreetRouteReaderWriter){
            		ArrayList<Integer> idNodesWithTurnImpedances = ((LocalGISStreetRouteReaderWriter)db).getNodesWithTurnImpedances();
            		if (idNodesWithTurnImpedances!= null && !idNodesWithTurnImpedances.isEmpty()){
            			for(int i=0; i < idNodesWithTurnImpedances.size(); i++){
            				Node simpleNode=null;
            				try{
            					simpleNode = ((DynamicGraph) graph).getNode(idNodesWithTurnImpedances.get(i));
            				}catch (ElementNotFoundException e) {
							}catch (Exception e) {
								e.printStackTrace();
							}
            				if (simpleNode != null){
            					// Existe el nodo con TurnImpedances
            					LocalGISTurnImpedance turnImpedances =((LocalGISStreetRouteReaderWriter)db).getNodeTurnImpedances(simpleNode.getID());
            					if (turnImpedances != null){
            						UniqueIDGenerator uidGen = new FixedValueUIDGenerator(simpleNode.getID());
            						GeographicNodeWithTurnImpedances nodeTurn = new GeographicNodeWithTurnImpedances(
            								((GeographicNode)simpleNode).getPosition(),
            								turnImpedances,
            								uidGen);

            						 try
            						 {
            							 if(simpleNode instanceof ProxyNode)
            								 simpleNode = (Node) NodeUtils.unwrapProxies(simpleNode);
            							 if(!(simpleNode instanceof GeographicNodeWithTurnImpedances))
            								 ((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)graph).getMemoryManager()).replaceNodeInstance(simpleNode, nodeTurn);
            						 }
            						 catch (Exception e)
            						 {
            						 // TODO Auto-generated catch block
            						 e.printStackTrace();
            						 }
            					}
            				}
            			}
            		}
            	}
            }

            graph.getEdges();


		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		/*
		 * Ahora aqui representaremos del grafo sus arcos
		 */
		monitor
				.report(I18N
						.get("leerredbase","routeengine.leerred.creatinglayer"));

		createLayer(panel, context);
		return true;

	}

	private boolean isStreetNetwork(String networkName) {
		boolean resultado = false;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs=null;

		try{

			String sql = "SELECT * FROM network_street_properties where id_network = " + getIdNetwork(networkName); //+ getIdNetwork(networkName);
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn= connectionFactory.getConnection();
			st= conn.prepareStatement(sql);
			st.execute();
			rs = st.getResultSet();
			// process resultset and create list of nodes
			while (rs.next())
			{
				return true;
			}

		}catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			ConnectionUtilities.closeConnection(conn, st, rs);
		}

		return resultado;
	}

	private int getIdNetwork(String networkName) {
		boolean resultado = false;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs=null;

		try{

			String sqlid_subred = "(SELECT id_network FROM networks WHERE network_name='"
				+ networkName + "')";

			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn= connectionFactory.getConnection();
			st= conn.prepareStatement(sqlid_subred);
			rs = st.executeQuery();

			// process resultset and create list of nodes
			while (rs.next())
			{
				return rs.getInt("id_network");
			}

		}catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			ConnectionUtilities.closeConnection(conn, st, rs);
		}

		return 0;
	}


	protected boolean leeroguardarGrafoenBase(WithOutConnectionPanel panel,
			TaskMonitor monitor, PlugInContext context) throws Exception {
		return leerGrafodeBase((PanelToLoadFromDataStore) panel, monitor, context);
	}

	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("leerredbase","routeengine.leerred.iconfile"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!saveRedDatabaseButtonAdded)
		{
			toolbox.addToolBar();
			LeerRedDeBasePlugIn explode = new LeerRedDeBasePlugIn();
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			saveRedDatabaseButtonAdded = true;
		}
	}

}