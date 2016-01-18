package com.geopista.ui.plugin.routeenginetools.guardarredenbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.dynamic.DynamicNode;
import org.uva.route.graph.structure.dynamic.IDynamicNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.AllInMemoryExternalSourceGraphMemoryManager;
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.guardarredenbase.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.DialogForDataStorePlugin;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSaveInDataStore;
import com.geopista.ui.plugin.routeenginetools.routeutil.VentanaError;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class GuardarRedEnBasePlugIn extends DialogForDataStorePlugin {
	Connection conn;

	private boolean saveRedDatabaseButtonAdded;
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	public static TaskMonitor monitor = null;

	public boolean execute(final PlugInContext context) {
		return super.execute(context);
	}

	public void initialize(PlugInContext context) {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.guardarredenbase.language.RouteEngine_GuardarRedEnBasei18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("guardarredbase",bundle);

		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(this.aplicacion.getI18nString("ui.MenuNames.TOOLS.ROUTEENGINEADMINISTRATION")).addPlugIn(this.getIcon(),
				this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());


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

	@Override
	protected WithOutConnectionPanel createPanel(PlugInContext context) {
		return new PanelToSaveInDataStore(context);
		// creas tu panel a parte del cuadradito de base
	}

	protected boolean guardarGrafoenBase(PanelToSaveInDataStore panel,
			TaskMonitor monitor, PlugInContext context) throws Throwable {
		monitor.report("Guardando subred en Base de Datos");
		this.monitor = monitor;

		try {
			if (AppContext.getApplicationContext().isLogged()){
				Connection connection = AppContext.getApplicationContext().getConnection();
				if (connection != null){
					conn = connection;
				}
			}

			if (conn == null) {
				return false;
				// Database metadata is not supported
				// therefore, you cannot get metadata at runtime
			} else {

				boolean tableSubredesExist = ComprobarTablaSubRedes();
				boolean tableNodosExist = ComprobarTabaNodos();
				boolean tableTramosExist = ComprobarTablaTramos();

				// si hay registros en el rs entonces estï¿½ la tabla.
				if ((!tableSubredesExist) || (!tableNodosExist) || (!tableTramosExist)) {
					// si // no// hay// ningun// registro// de
					// ninguna// de// las// bases
					crearTablas(tableSubredesExist, tableNodosExist, tableTramosExist);
				}

				return guardarenBase(monitor, panel, context);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			//			System.out.println("No existen las tablas");
			return false;
		}

	}

	private boolean ComprobarTablaTramos() {
		// TODO Auto-generated method stub
		String tableTramos = "select * from network_edges limit 1";
		PreparedStatement tramosPreparedStatement = null;
		try {
			tramosPreparedStatement = this.conn.prepareStatement(tableTramos);
			tramosPreparedStatement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}finally {
			try{tramosPreparedStatement.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return true;
	}

	private boolean ComprobarTabaNodos() {
		// TODO Auto-generated method stub
		String tableNodos = "select * from network_nodes limit 1";
		PreparedStatement nodosPreparedStatement = null;
		try {
			nodosPreparedStatement = this.conn.prepareStatement(tableNodos);
			nodosPreparedStatement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} finally {
			try{nodosPreparedStatement.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return true;
	}

	private boolean ComprobarTablaSubRedes() {
		// TODO Auto-generated method stub
		String tableSubredes = "select * from networks limit 1";
		PreparedStatement subredesPreparedStatement = null;
		try {
			subredesPreparedStatement = this.conn.prepareStatement(tableSubredes);
			subredesPreparedStatement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} finally {
			try{subredesPreparedStatement.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return true;
	}

	protected boolean leeroguardarGrafoenBase(WithOutConnectionPanel panel,
			TaskMonitor monitor, PlugInContext context) throws Throwable {

		return guardarGrafoenBase((PanelToSaveInDataStore) panel, monitor, context);

	}

	public void crearTablaSubredes() {
		PreparedStatement subredesCreatePreparedStatement = null;
		String createTablesubredes = "CREATE TABLE networks(id_network serial NOT NULL,"
			+ "network_name character(20) NOT NULL unique,"
			+ "comment character(100),"
			+ "CONSTRAINT networks_pkey PRIMARY KEY (id_network) );"
			+ "ALTER TABLE networks OWNER TO geopista;";
		try {
			subredesCreatePreparedStatement = this.conn.prepareStatement(createTablesubredes);
			subredesCreatePreparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{subredesCreatePreparedStatement.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	public void crearTablaNodos() {

		PreparedStatement nodosCreatePreparedStatement = null;
		String tablanodos = "CREATE TABLE network_nodes("
			+ "id_network integer NOT NULL," 
			+ "id_node integer NOT NULL,"
			+ "node_geometry geometry,"
			+ "CONSTRAINT network_nodes_pkey PRIMARY KEY (id_network, id_node));" 
			+ "ALTER TABLE network_nodes OWNER TO geopista";


		try {
			nodosCreatePreparedStatement = this.conn.prepareStatement(tablanodos);
			nodosCreatePreparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{nodosCreatePreparedStatement.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
	}

	public void crearTablaTramos() {
		PreparedStatement tramosCreatePreparedStatement = null;
		String tablatramos = "CREATE TABLE network_edges("
			+ "id_network integer NOT NULL," 
			+ "id_edge integer NOT NULL,"
			+ "id_nodea integer NOT NULL,"
			+ "id_network_nodea integer NOT NULL,"
			+ "id_nodeb integer NOT NULL,"
			+ "id_network_nodwb integer NOT NULL,"
			+ "edge_length double precision NOT NULL,"
			+ "id_layer int4,"
			+ "id_feature numeric(8),"
			+ "CONSTRAINT network_edges_pkey PRIMARY KEY (id_network, id_edge));"
			+ "ALTER TABLE network_edges OWNER TO geopista";

		try {
			tramosCreatePreparedStatement = this.conn.prepareStatement(tablatramos);
			tramosCreatePreparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{tramosCreatePreparedStatement.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	public void crearTablas(boolean existeTablaSubredes,boolean existeTablaNodos,boolean existeTablaTramos) {

		if (! existeTablaSubredes){
			crearTablaSubredes();
		}
		if (! existeTablaNodos) {
			crearTablaNodos();
		}
		if (! existeTablaTramos){
			crearTablaTramos();
		}
	}

	public boolean guardarenBase(TaskMonitor monitor,
			PanelToSaveInDataStore panel,
			PlugInContext context) {
		monitor.report("Guardando Subred en Base de Datos");
		boolean guardado = false;
		DynamicGraph resultGraph = null;

		String sq = "SELECT network_name FROM networks WHERE network_name='"
			+ panel.getNombreenBase() + "'";
		ResultSet rs = null;
		try {
			PreparedStatement st = conn.prepareStatement(sq);
			rs = st.executeQuery();
			if (!rs.next()) {
				// si no hay ninguna subred con ese nombre
				guardado = guardarRedEnBaseDeDatos(panel.getNombreenBase(), panel, context);
			} else {
				// hay subredes con el mismo nombre.
				// se muestra dialogo con el error y opcion de cambiarlo o sobreescribirlo
				guardado = cambiarSobreescribirRed(panel, context, guardado);
			}
		}  catch (SQLException ex){
			ex.printStackTrace();
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return guardado;

	}

	private static String textFieldNuevaRedName = "Nuevared"; 

	private boolean cambiarSobreescribirRed(PanelToSaveInDataStore panel,
			PlugInContext context,
			boolean guardado) throws SQLException, Exception {
		VentanaError ventanaerror = createInitializaErrorWindow(context, panel);

		boolean canceled = false;
		while (!canceled && !guardado){
			ventanaerror.mostrar();
			if (ventanaerror.OK()){
				// si se da Ok en la ventana de error: SE DESEA MODIFICAR EL NOMBRE;
				if (panel.getNombreenBase().equals(ventanaerror.getTextField(textFieldNuevaRedName))){
					// se ha introducido el mismo nombre que hay en la base de datos.
					// se pide confirmacion para sobreescribir.
					if (confirmarSobreescribirNombreRed()){
						// se confirma y se sobreescribe.
						guardado = sobreEscribirRedEnBaseDeDatos(ventanaerror.getTextField(textFieldNuevaRedName),panel, context);
					}
				} else{
					// se guarda el nuevo nombre de la red.
					guardado = guardarRedEnBaseDeDatos(ventanaerror.getTextField(textFieldNuevaRedName), panel, context);
				}
			} else{
				canceled = true;
			}
		}
		return guardado;
	}

	public VentanaError createInitializaErrorWindow(PlugInContext context, PanelToSaveInDataStore panel){

		VentanaError createdWindowError = new VentanaError(context);
		createdWindowError.addText(I18N.get("guardarredbase","routeengine.guardarred.errormessage.existsubred1"));
		createdWindowError.addText(I18N.get("guardarredbase","routeengine.guardarred.errormessage.existsubred2"));


		createdWindowError.addTextField(textFieldNuevaRedName, panel.getNombreenBase(), 20, null, "Nuevo nombre para la red:");

		return createdWindowError;
	}

	private boolean sobreEscribirRedEnBaseDeDatos(String nuevoNombreParaRed, PanelToSaveInDataStore panel,
			PlugInContext context) {
		boolean resultado = false;
		// TODO Auto-generated method stub
		try{
			PreparedStatement st;
/*			String sql = "UPDATE networks SET id_network=DEFAULT, network_name='" + 
			nuevoNombreParaRed + "', comment='Esto es una prueba' where network_name='" +
			panel.getNombreenBase() + "';";

			st = conn.prepareStatement(sql);
			st.executeUpdate();
			if (st != null)
				st.close();
			conn.close();*/
			NetworkManager networkMgr = FuncionesAuxiliares
			.getNetworkManager(context);
			String red = panel.getRedSeleccionada();
			String subred = panel.getSubredseleccionada();
			Network net = null;
			if (red != null && !red.equals("")){
				if (subred != null && !subred.equals("")){
					net = networkMgr.getNetwork(red).getSubNetwork(subred);
				} else{
					net = networkMgr.getNetwork(red);
				}
			}

			Graph dataBaseGraph = writeGraph(panel, net.getGraph() , context, subred);
			if (dataBaseGraph != null){
				net.setGraph(dataBaseGraph);
				resultado = true;
			}
		} catch (SQLException ex){
			ex.printStackTrace();
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return resultado;
	}

	/**
	 * @param panel
	 * @param resultado
	 * @param graph
	 * @return
	 * @throws Exception 
	 */
	private Graph writeGraph(PanelToSaveInDataStore panel,
			Graph graph, PlugInContext context, String subred) throws Exception {

//		long timeInit = System.currentTimeMillis();
		Graph resultGraph = null;
		LocalGISRouteReaderWriter db = null;
//		this.getEdgesLayerModifications(graph, context, subred);

//		System.out.println("Tiempo en actualizar el grafo:" + (System.currentTimeMillis() - timeInit) );

		try {
			
			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
			if (!graph.getEdges().isEmpty()){
				if (graph.getEdges().toArray()[0] instanceof LocalGISStreetDynamicEdge){
					db = new LocalGISStreetRouteReaderWriter(routeConnection); 
				} else {
					db = new LocalGISRouteReaderWriter(routeConnection);		
				}
			}
			
			
//			LocalGISStreetRouteReaderWriter streetDb = new LocalGISStreetRouteReaderWriter(routeConnection);

			db.setNetworkName(panel.getNombreenBase());
//			streetDb.setNetworkName(panel.getNombreenBase());
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			Connection conn = connectionFactory.getConnection();
			if (graph != null) {
				Collection<Node> nodes;
				Collection<Edge> edges;
				if (panel.getRedSeleccionada().equals(panel.getNombreenBase())) // graba solo los cambios
				{
					nodes = ((AllInMemoryManager)((DynamicGraph)graph).getMemoryManager()).getUpdateMonitor().getDirtyNodes();
					edges = ((AllInMemoryManager)((DynamicGraph)graph).getMemoryManager()).getUpdateMonitor().getDirtyEdges();
				}
				else // como es una copia grabamos todo el grafo de nuevo con nuevo Networkid
				{
					nodes = graph.getNodes();
					edges = graph.getEdges();
				}
				// limpiar registros que vamos a insertar (NOTA: el driver GeopistaPreparedStatement no soporta Updates)
				
				db.deleteNodes(nodes);
				db.deleteEdges(edges);
				
				int i = 1; 
				for (Iterator<Node> itr = nodes.iterator(); 
				itr.hasNext();) {	
					this.monitor.report("Guardando Nodos (" + i + " de " + graph.getNodes().size() + ").");
					Node node = itr.next();
//					rebuildNode(node, conn);
					db.writeNode(node , conn);
					i++;
				}
//				System.out.println("Tiempo en grabar nodos:" + (System.currentTimeMillis() - timeInit) );

				i = 1;
				for (Iterator<Edge> itr = edges.iterator(); itr.hasNext();) {
					this.monitor.report("Guardando Tramos (" + i + " de " + graph.getEdges().size() + ").");
					Edge e =  itr.next();

					if (e instanceof LocalGISStreetDynamicEdge){
//						rebuildEdge(e,conn);
						db.writeEdge(e, conn);
						Set incidents = ((ILocalGISEdge)e).getIncidents();
						if(!incidents.isEmpty())
							db.writeIncidents(e, conn);
						((LocalGISStreetRouteReaderWriter)db).writeStreetData(e, conn);
					} else if (e instanceof LocalGISDynamicEdge){
						db.writeEdge(e, conn);
						Set incidents = ((ILocalGISEdge)e).getIncidents();
						if(!incidents.isEmpty())
							db.writeIncidents(e, conn);
					}
					i ++;
				}
				
//				System.out.println("Tiempo en grabar arcos:" + (System.currentTimeMillis() - timeInit) );

				db.writeNetworkProperties(FuncionesAuxiliares.getNetworkManager(context).getNetwork(
 						subred).getProperties(), conn 
				);

//				System.out.println("Tiempo en grabar propiedades:" + (System.currentTimeMillis() - timeInit) );
				//graph
				//db
				//BasicExternalSourceMemoryManager mman = new BasicExternalSourceMemoryManager(db);
				//SpatialAllInMemoryExternalSourceMemoryManager mman  = new SpatialAllInMemoryExternalSourceMemoryManager(db);
				//mman.setGraph(graph);
				resultGraph = graph;//new DynamicGraph(mman);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			ConnectionUtilities.closeConnection(conn);
		}
		return resultGraph;
	}

	/**
	 * @param graph
	 * @param context
	 * @param subred
	 */
	private void getEdgesLayerModifications(Graph graph, PlugInContext context,
			String subred) {
		try {
			
//			BasicGraph bgraph = new BasicGraph();
//			bgraph.setEdges(graph.getEdges());
//			bgraph.setNodes(graph.getNodes());
			
			GeopistaLayer nodesLayer = (GeopistaLayer) selectedSubRedLayer(context, "Nodos-" + subred);
			ArrayList<DynamicNode> listaDeNodes = new ArrayList<DynamicNode>();
			if (nodesLayer != null){
				Collection<GeopistaFeature> nodesFeartures = nodesLayer.getFeatureCollectionWrapper().getFeatures();
				Iterator it = nodesFeartures.iterator();
				while (it.hasNext()){
					GeopistaFeature gf = (GeopistaFeature) it.next();
					for (int i=0; i< graph.getEdges().size(); i ++){
						if (((Node)graph.getNodes().toArray()[i]).getID() == (Integer)gf.getAttribute("nodeId") ){
							DynamicGeographicNode node = (DynamicGeographicNode)graph.getNodes().toArray()[i];

							int nodeId = (Integer) gf.getAttribute("nodeId");
							node.setID(nodeId);

							node.setObject(gf.getAttribute("GEOMETRY"));

							listaDeNodes.add(node);
							break;
						}
					}
				}
			}
			
			if (listaDeNodes != null && !listaDeNodes.isEmpty()){
				BasicGraph bg = new BasicGraph();
//				bg.setEdges(edges);
				bg.setNodes(listaDeNodes);
				((BasicGraph) graph).setNodes(listaDeNodes);
			}
			
			
			GeopistaLayer edgesLayer = (GeopistaLayer) selectedSubRedLayer(context, "Arcos-" + subred);
			ArrayList<Edge> listaDeEdges = new ArrayList<Edge>();
			if (edgesLayer != null){
				Collection<Edge> edgesFeatures = edgesLayer.getFeatureCollectionWrapper().getFeatures();
				Iterator it = edgesFeatures.iterator();
				while (it.hasNext()){
					GeopistaFeature gf= (GeopistaFeature) it.next();
					for (int i=0; i < graph.getEdges().size(); i++){
						if (((Edge)graph.getEdges().toArray()[i]).getID() == (Integer)gf.getAttribute("idEje") ){
							
							if (gf.getSchema().hasAttribute("anchuraAcera")){
								
								// si tiene nominalMaxSpeed es de tipo Callejero
								PMRLocalGISStreetDynamicEdge streetEdge = (PMRLocalGISStreetDynamicEdge) graph.getEdges().toArray()[i];
								
								
								if (gf.getAttribute("idNodoA") != null && (Integer)gf.getAttribute("idNodoA") > 0){
									IDynamicNode node = null;
									int idNode = (Integer)gf.getAttribute("idNodoA");
									for(int m = 0; m < graph.getNodes().size() ; m ++){
										if (idNode == ((IDynamicNode)graph.getNodes().toArray()[m]).getID()){
											node = ((IDynamicNode)graph.getNodes().toArray()[m]);
											break;
										}
									}
									streetEdge.setNodeA(node);
								}
								
								if (gf.getAttribute("idNodoB") != null && (Integer)gf.getAttribute("idNodoB") > 0){
									IDynamicNode node = null;
									int idNode = (Integer)gf.getAttribute("idNodoB");
									for(int m = 0; m < graph.getNodes().size() ; m ++){
										if (idNode == ((IDynamicNode)graph.getNodes().toArray()[m]).getID()){
											node = ((IDynamicNode)graph.getNodes().toArray()[m]);
											break;
										}
									}
									streetEdge.setNodeB(node);
								}
								
								if (gf.getAttribute("impedanciaAB") != null && (Double)gf.getAttribute("impedanciaAB") > 0){
									streetEdge.setImpedanceAToB((Double)gf.getAttribute("impedanciaAB"));
								}
								
								if (gf.getAttribute("impedanciaBA") != null && (Double)gf.getAttribute("impedanciaBA") > 0){
									streetEdge.setImpedanceBToA((Double)gf.getAttribute("impedanciaBA"));
								}
								
								if (gf.getAttribute("longitudEje") != null && (Double)gf.getAttribute("longitudEje") > 0){
									streetEdge.setEdgeLength((Double)gf.getAttribute("longitudEje"));
								}
								
								if (gf.getAttribute("idFeature") != null && (Integer)gf.getAttribute("idFeature") > 0){
									streetEdge.setIdFeature((Integer)gf.getAttribute("idFeature"));
								}
								
								if (gf.getAttribute("idCapa") != null && (Integer)gf.getAttribute("idCapa") > 0){
									streetEdge.setIdLayer((Integer)gf.getAttribute("idCapa"));
								}
															
								if (gf.getAttribute("anchuraAcera") != null && (Double)gf.getAttribute("anchuraAcera") > 0){
									streetEdge.setWidth((Double) gf.getAttribute("anchuraAcera"));
								}
								
								if (gf.getAttribute("pendienteTransversal") != null && (Double)gf.getAttribute("pendienteTransversal") > 0){
									streetEdge.setTransversalSlope((Double) gf.getAttribute("pendienteTransversal"));
								}
								
								if (gf.getAttribute("pendienteLongitudinal") != null && (Double)gf.getAttribute("pendienteLongitudinal") > 0){
									streetEdge.setLongitudinalSlope((Double) gf.getAttribute("pendienteLongitudinal"));
								}
								
								listaDeEdges.add(streetEdge);
							}else if (gf.getSchema().hasAttribute("maxVelocidadNominal")){
								
								// si tiene nominalMaxSpeed es de tipo Callejero
								LocalGISStreetDynamicEdge streetEdge = (LocalGISStreetDynamicEdge) graph.getEdges().toArray()[i];
								
								if (gf.getAttribute("coste") != null && (Double)gf.getAttribute("coste") > 0){
									streetEdge.setCost((Double)gf.getAttribute("coste"));
								}
								
								if (gf.getAttribute("idNodoA") != null && (Integer)gf.getAttribute("idNodoA") > 0){
									IDynamicNode node = null;
									int idNode = (Integer)gf.getAttribute("idNodoA");
									for(int m = 0; m < graph.getNodes().size() ; m ++){
										if (idNode == ((IDynamicNode)graph.getNodes().toArray()[m]).getID()){
											node = ((IDynamicNode)graph.getNodes().toArray()[m]);
											break;
										}
									}
									streetEdge.setNodeA(node);
								}
								
								if (gf.getAttribute("idNodoB") != null && (Integer)gf.getAttribute("idNodoB") > 0){
									IDynamicNode node = null;
									int idNode = (Integer)gf.getAttribute("idNodoB");
									for(int m = 0; m < graph.getNodes().size() ; m ++){
										if (idNode == ((IDynamicNode)graph.getNodes().toArray()[m]).getID()){
											node = ((IDynamicNode)graph.getNodes().toArray()[m]);
											break;
										}
									}
									streetEdge.setNodeB(node);
								}
								
								if (gf.getAttribute("impedanciaAB") != null && (Double)gf.getAttribute("impedanciaAB") > 0){
									streetEdge.setImpedanceAToB((Double)gf.getAttribute("impedanciaAB"));
								}
								
								if (gf.getAttribute("impedanciaBA") != null && (Double)gf.getAttribute("impedanciaBA") > 0){
									streetEdge.setImpedanceBToA((Double)gf.getAttribute("impedanciaBA"));
								}
								
								if (gf.getAttribute("longitudEje") != null && (Double)gf.getAttribute("longitudEje") > 0){
									streetEdge.setEdgeLength((Double)gf.getAttribute("longitudEje"));
								}
								
								if (gf.getAttribute("idFeature") != null && (Integer)gf.getAttribute("idFeature") > 0){
									streetEdge.setIdFeature((Integer)gf.getAttribute("idFeature"));
								}
								
								if (gf.getAttribute("idCapa") != null && (Integer)gf.getAttribute("idCapa") > 0){
									streetEdge.setIdLayer((Integer)gf.getAttribute("idCapa"));
								}
								
								if (gf.getAttribute("regulacionTrafico") != null && !((String)gf.getAttribute("regulacionTrafico")).equals("")){
									if (((String)gf.getAttribute("regulacionTrafico")).equals("FORBIDDEN")){
										streetEdge.setTrafficRegulation(StreetTrafficRegulation.FORBIDDEN);	
									} else if (((String)gf.getAttribute("regulacionTrafico")).equals("INVERSE")){
										streetEdge.setTrafficRegulation(StreetTrafficRegulation.INVERSE);
									} else if (((String)gf.getAttribute("regulacionTrafico")).equals("BIDIRECTIONAL")){
										streetEdge.setTrafficRegulation(StreetTrafficRegulation.BIDIRECTIONAL);
									}else if (((String)gf.getAttribute("regulacionTrafico")).equals("DIRECT")){
										streetEdge.setTrafficRegulation(StreetTrafficRegulation.DIRECT);
									}
									
								}
								
								if (gf.getAttribute("maxVelocidadNominal") != null && (Double)gf.getAttribute("maxVelocidadNominal") > 0){
									streetEdge.setNominalMaxSpeed((Double) gf.getAttribute("maxVelocidadNominal"));
								}
								
								listaDeEdges.add(streetEdge);

							} else {
								// si no tiene nominalMaxSpeed es de tipo normal?
								LocalGISDynamicEdge dynamicEdge = (LocalGISDynamicEdge) graph.getEdges().toArray()[i];
								
								if (gf.getAttribute("coste") != null && (Double)gf.getAttribute("coste") > 0){
									dynamicEdge.setCost((Double)gf.getAttribute("coste"));
								}
								
								if (gf.getAttribute("idNodoA") != null && (Integer)gf.getAttribute("idNodoA") > 0){
									IDynamicNode node = null;
									int idNode = (Integer)gf.getAttribute("idNodoA");
									for(int m = 0; m < graph.getNodes().size() ; m ++){
										if (idNode == ((IDynamicNode)graph.getNodes().toArray()[m]).getID()){
											node = ((IDynamicNode)graph.getNodes().toArray()[m]);
											break;
										}
									}
									dynamicEdge.setNodeA(node);
								}
								
								if (gf.getAttribute("idNodoB") != null && (Integer)gf.getAttribute("idNodoB") > 0){
									IDynamicNode node = null;
									int idNode = (Integer)gf.getAttribute("idNodoB");
									for(int m = 0; m < graph.getNodes().size() ; m ++){
										if (idNode == ((IDynamicNode)graph.getNodes().toArray()[m]).getID()){
											node = ((IDynamicNode)graph.getNodes().toArray()[m]);
											break;
										}
									}
									dynamicEdge.setNodeB(node);
								}
								
								if (gf.getAttribute("impedanciaAB") != null && (Double)gf.getAttribute("impedanciaAB") > 0){
									dynamicEdge.setImpedanceAToB((Double)gf.getAttribute("impedanciaAB"));
								}
								
								if (gf.getAttribute("impedanciaBA") != null && (Double)gf.getAttribute("impedanciaBA") > 0){
									dynamicEdge.setImpedanceBToA((Double)gf.getAttribute("impedanciaBA"));
								}
								
								if (gf.getAttribute("longitudEje") != null && (Double)gf.getAttribute("longitudEje") > 0){
									dynamicEdge.setEdgeLength((Double)gf.getAttribute("longitudEje"));
								}
								
								if (gf.getAttribute("idFeature") != null && (Integer)gf.getAttribute("idFeature") > 0){
									dynamicEdge.setIdFeature((Integer)gf.getAttribute("idFeature"));
								}
								
								if (gf.getAttribute("idCapa") != null && (Integer)gf.getAttribute("idCapa") > 0){
									dynamicEdge.setIdFeature((Integer)gf.getAttribute("idCapa"));
								}
	
								listaDeEdges.add(dynamicEdge);
							}
							break;
						}

					}
				}
			}		
			
			if (listaDeEdges != null && !listaDeEdges.isEmpty()){
				((BasicGraph)graph).setEdges(listaDeEdges);
			}
			
			
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private boolean guardarRedEnBaseDeDatos(String nombreRed, PanelToSaveInDataStore panel,
			PlugInContext context) throws SQLException, Exception {

		boolean resultado = false;
		try{
			
			NetworkManager networkMgr = FuncionesAuxiliares
			.getNetworkManager(context);
			String red = panel.getRedSeleccionada();
			String subred = panel.getSubredseleccionada();
			Network net = null;
			if (red != null && !red.equals("")){
				if (subred != null && !subred.equals("")){
					net = networkMgr.getNetwork(red).getSubNetwork(subred);
				} else{
					net = networkMgr.getNetwork(red);
				}
			}
			
			PreparedStatement st;
			String sql = "INSERT INTO networks VALUES(DEFAULT,'"
				+ nombreRed + "',"				
				+ "'')";
			
			st = conn.prepareStatement(sql);
			st.executeUpdate();
			if (st != null)
				st.close();
			conn.close();
			
			Graph dataBaseGraph = writeGraph(panel, net.getGraph() , context, subred);
			if (dataBaseGraph != null){
				net.setGraph(dataBaseGraph);
				resultado = true;
			}

		}  catch (SQLException ex){
			ex.printStackTrace();
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return resultado;
	}

	private void rebuildNode(Node node,Connection connection) throws SQLException {
		LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
		node.setID(nDAO.getNextDatabaseIdNode(connection));
	}
	private void rebuildEdge(Edge edge,Connection connection) throws SQLException {
		LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
		edge.setID(nDAO.getNextDatabaseIdEdge(connection));
	}
	public Collection selectedSubRedLayerFeatures(PlugInContext context, String red, String subred) {
		ArrayList selectedFeatures = new ArrayList();

		Layer selectedLayer = null;
		if (red != null && !red.equals("") ) {
			if (subred != null && !subred.equals("")){
				if ( red.equals("RedBaseDatos") ||
						red.equals("RedLocal")){
					// La subred que se ha elegido es una subred cargada de la base de datos.
					// sólo habría que obtener la capa correspondiente y recorrer las features.

					selectedLayer = context.getLayerManager().getLayer(subred);
					if (selectedLayer != null){
						Iterator it2 = selectedLayer.getFeatureCollectionWrapper().getFeatures().iterator();
						while (it2.hasNext()){
							selectedFeatures.add(it2.next());
						}
					}

				} else {

					// recorremos las capas y miramos si es una capa de particiones generada.
					Iterator<Layer> it3 = context.getLayerManager().getLayers().iterator();
					Layer actualLayer = null;
					while (it3.hasNext()){
						actualLayer = it3.next();
						if ( actualLayer != null){
							if (actualLayer.getName().equals("Particiones-"+ red)){
								// es una capa de 'Particiones-'
								// buscamos la feature correspondiente.
								Iterator<Feature> itFeatures = actualLayer.getFeatureCollectionWrapper().getFeatures().iterator();
								Feature feature = null;
								while (itFeatures.hasNext()){
									feature = itFeatures.next();
									if (feature.getString("nombreSubred").equals(subred)){
										selectedFeatures.add(feature);
									}
								}
							}
						}
					}

				}
			}

		}
		// Como seleccionar las features....      

		return selectedFeatures;
	}

	public Layer selectedSubRedLayer( PlugInContext context, String subred) {
		ArrayList selectedFeatures = new ArrayList();

		Layer selectedLayer = null;
		if (subred != null && !subred.equals("") ) {
			if (subred != null && !subred.equals("")){
				// La subred que se ha elegido es una subred cargada de la base de datos.
				// sólo habría que obtener la capa correspondiente y recorrer las features.
				selectedLayer = context.getLayerManager().getLayer(subred);
			}

		}
		// Como seleccionar las features....      

		return selectedLayer;
	}

	public boolean confirmarSobreescribirNombreRed(){
		// TODO Auto-generated method stub

		// mensaje del dialogo.
		String mensaje = I18N.get("guardarredbase","routeengine.guardarred.overwritemessage.title");
		String titulo =  I18N.get("guardarredbase","routeengine.guardarred.overwritemessage.text");

		int seleccion = JOptionPane.showOptionDialog(
				AppContext.getApplicationContext().getMainFrame(),
				mensaje, 
				titulo,
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,    // null para icono por defecto.
				new Object[] { "Aceptar", "cancelar"},   // null para YES, NO y CANCEL
		"cancelar");

		if (seleccion == 0)
			return true;

		return false;
	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("guardarredbase","routeengine.guardarred.iconfile"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!saveRedDatabaseButtonAdded)
		{
			toolbox.addToolBar();
			GuardarRedEnBasePlugIn explode = new GuardarRedEnBasePlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			saveRedDatabaseButtonAdded = true;
		}
	}

}
