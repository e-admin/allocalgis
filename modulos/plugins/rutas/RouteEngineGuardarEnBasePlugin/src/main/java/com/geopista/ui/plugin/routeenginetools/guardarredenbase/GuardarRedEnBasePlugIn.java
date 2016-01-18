/**
 * GuardarRedEnBasePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.guardarredenbase;

import java.io.IOException;
import java.sql.Connection;
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
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.guardarredenbase.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.DialogForDataStorePlugin;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil.Operations;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilToolsCore;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSaveInDataStore;
import com.geopista.ui.plugin.routeenginetools.routeutil.VentanaError;
import com.geopista.util.UserCancellationException;
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
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createBlackBoardMustBeElementsCheck())
		.add(checkFactory.createNetworksMustBeLoadedCheck())
		.add(checkFactory.checkIsOnline())
		;
	}

	@Override
	protected WithOutConnectionPanel createPanel(PlugInContext context) {
		return new PanelToSaveInDataStore(context);
		// creas tu panel a parte del cuadradito de base
	}

	protected void guardarGrafoenBase(String networkName,	TaskMonitor monitor, PlugInContext context) throws SQLException
	{
		monitor.report("Guardando subred en Base de Datos");
		this.monitor = monitor;

		try {
			if (AppContext.getApplicationContext().isLogged()){
				Connection connection = AppContext.getApplicationContext().getConnection();
				if (connection != null){
					conn = connection;
				}
				else
				{
					// Database metadata is not supported
					// therefore, you cannot get metadata at runtime
					throw new IllegalStateException("No hay una conexión abierta disponible para guardar la red.");
				}
			}

		
				
			// Si es necesario crea el modelo en la base de datos
				//NetworkModuleUtil.createNetworkModel(conn);

				guardarenBase(monitor, networkName, context);
			

		} catch (IOException sqle) {
			throw new RuntimeException("No se pueden guardar la red en la base de datos.",sqle);
		}

	}


	public void run(TaskMonitor monitor, final PlugInContext context) {

		try {
			PanelToSaveInDataStore panel = (PanelToSaveInDataStore)connectedNetworkPanel;
			String networkName = panel.getNombreenBase();
			linkGraphToStoreAndCreateLayer(networkName, monitor, context);
			
			
		} catch(UserCancellationException e)
		{
		    return;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	protected void linkGraphToStoreAndCreateLayer(String networkName,TaskMonitor monitor, PlugInContext context) throws SQLException
	{
	   
	    guardarGrafoenBase(networkName, monitor, context);
	}


	public boolean guardarenBase(TaskMonitor monitor,
			String nombreRed,
			PlugInContext context) throws IOException 
	{
	    if (nombreRed==null || "".equals(nombreRed))
		{
		    VentanaError error = new VentanaError(context);
		    error.addText("No se puede utilizar un nombre de red vacío.");
		    error.setVisible();
		    return false;
		}
	    
		monitor.report("Guardando Subred en Base de Datos");
		boolean guardado;
		try
		    {
			guardado = false;
			DynamicGraph resultGraph = null;
			LocalGISNetworkDAO networkDAO=new LocalGISNetworkDAO();
			
			Integer id = networkDAO.getNetworkId(nombreRed, conn);
			if (id==null)
			    {
				// si no hay ninguna subred con ese nombre
				guardado = guardarNuevaRedEnBaseDeDatos(nombreRed, (PanelToSaveInDataStore) connectedNetworkPanel, context);
			    }
			else
			    {
				// hay subredes con el mismo nombre.
				// se muestra dialogo con el error y opcion de cambiarlo o sobreescribirlo
				guardado = cambiarSobreescribirRed((PanelToSaveInDataStore) connectedNetworkPanel, context, guardado); 
			    }
		    } catch (SQLException e)
		    {
			throw new IOException(e);
		    }
		catch ( UserCancellationException e)
		{
		    return false;
		}
		catch (Exception e)
		    {
			throw new RuntimeException(e);
		    }
		
////		String sq = "SELECT network_name FROM networks WHERE network_name='"
////			+ panel.getNombreenBase() + "'";
////		ResultSet rs = null;
////		try {
////			PreparedStatement st = conn.prepareStatement(sq);
////			rs = st.executeQuery();
////			if (!rs.next()) {
////				// si no hay ninguna subred con ese nombre
////				guardado = guardarNuevaRedEnBaseDeDatos(panel.getNombreenBase(), panel, context);
////			} else {
////				// hay subredes con el mismo nombre.
////				// se muestra dialogo con el error y opcion de cambiarlo o sobreescribirlo
////				guardado = cambiarSobreescribirRed(panel, context, guardado);
////			}
//		}  catch (SQLException ex){
//			ex.printStackTrace();
//			return false;
//		} catch (Exception e){
//			e.printStackTrace();
//			return false;
//		}
		return guardado;

	}

	private static String textFieldNuevaRedName = "Nuevared"; 

	private boolean cambiarSobreescribirRed(PanelToSaveInDataStore panel,
			PlugInContext context,
			boolean guardado) throws SQLException, Exception {
		VentanaError ventanaerror = createInitializaErrorWindow(context, panel);

		boolean canceled = false;
		while (!canceled && !guardado){
			ventanaerror.setVisible();
			if (ventanaerror.OK())
			{
				// si se da Ok en la ventana de error: SE DESEA MODIFICAR EL NOMBRE;
				String userSelectedNetworkName = ventanaerror.getTextField(textFieldNuevaRedName);
				if (panel.getNombreenBase().equals(userSelectedNetworkName))
				{
					// se ha introducido el mismo nombre que hay en la base de datos.
					// se pide confirmacion para sobreescribir.
					if (confirmarSobreescribirNombreRed())
					{
						// se confirma y se sobreescribe.
					    	NetworkModuleUtilToolsCore.clearNetwork(userSelectedNetworkName);
						guardado = guardarEnRedExistenteBaseDeDatos(userSelectedNetworkName,panel, context);
					}
				}
				else
				{
					// se guarda con un nuevo nombre de la red.
					guardado = guardarNuevaRedEnBaseDeDatos(userSelectedNetworkName, panel, context);
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
	/**
	 * Guarda red en un nombre nuevo
	 * @param nombreRed
	 * @param panel
	 * @param context
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
private boolean guardarNuevaRedEnBaseDeDatos(String nombreRed, PanelToSaveInDataStore panel,PlugInContext context) throws SQLException, Exception 
{
   
    	LocalGISNetworkDAO networkdDAO=new LocalGISNetworkDAO();
    	networkdDAO.registerNetworkInDatabase(nombreRed,conn);
    	
	return guardarEnRedExistenteBaseDeDatos(nombreRed, panel, context);
}



/**
 * Guarda en un nombre ya existente
 * @param nuevoNombreParaRed se ignora
 * @param panel
 * @param context
 * @return
 * @throws IOException 
 */
	private boolean guardarEnRedExistenteBaseDeDatos(String nuevoNombreParaRed, PanelToSaveInDataStore panel,PlugInContext context) throws IOException
	{
//		try{

			NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
			
			String red = panel.getRedSeleccionada();
			String subredName = panel.getSubredseleccionada();
			Network selectedNet = NetworkModuleUtil.getSubNetwork(networkMgr, red, subredName);
//		boolean readerAccess=true;
//		if (readerAccess)
//		{
		   
		    NetworkModuleUtil.writeNetworkProperties(selectedNet);
		    NetworkModuleUtilToolsCore.linkNetworkToStore(nuevoNombreParaRed, selectedNet,Operations.WRITE,false,monitor);
		    return true;
//		}
		
		
		   
	// escribe la red en base de datos con el nombre nuevo
	//TODO: BUG JPC previamente no usaba el nombre nuevo
//			Graph dataBaseGraph = writeGraph(panel, selectedNet.getGraph() , context, nuevoNombreParaRed);
			
			// TODO JPC no parece razonable sustituir el modelo tras la grabación. Mejor cargarlo de nuevo con el driver adecuado
//			if (dataBaseGraph != null)
//			{
//				// Sustituye el modelo en memoria
//				selectedNet.setGraph(dataBaseGraph);
//				resultado = true;
//			}
//		} catch (SQLException ex){
//			ex.printStackTrace();
//			return false;
//		} catch (Exception e){
//			e.printStackTrace();
//			return false;
//		}
//		return resultado;
	}

/**
     * Guarda la red en base de datos sobreescribiendo si existe otra con el mismo nombre 
     * TODO Contemplar el grabado de una red proveniente de BBDD --> actualizar cambios y respetar ids.
     * TODO esto debería ser tarea de LocalGISMemoryManager
     * @param panel
     * @param graph
     * @param context
     * @param subred
     *            nuevo nombre de red
     * @return
     * @throws Exception
     */
	private Graph writeGraph(PanelToSaveInDataStore panel,	Graph graph, PlugInContext context, String subred) throws Exception {

//		long timeInit = System.currentTimeMillis();
		Graph resultGraph = null;
		LocalGISRouteReaderWriter db = null;
//		this.getEdgesLayerModifications(graph, context, subred);

//		System.out.println("Tiempo en actualizar el grafo:" + (System.currentTimeMillis() - timeInit) );

		try {
			
			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
			if (!graph.getEdges().isEmpty())
			{
				if (graph.getEdges().iterator().next() instanceof LocalGISStreetDynamicEdge)
				{
					db = new LocalGISStreetRouteReaderWriter(routeConnection); 
				} else {
					db = new LocalGISRouteReaderWriter(routeConnection);		
				}
			}//else?
			else
			{
				return graph;
			}
			
//			db.setNetworkName(panel.getNombreenBase()); // TODO BUG ignoraba el parámetro subred que es el que el usuario desea
			db.setNetworkName(subred);
			
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			Connection conn = connectionFactory.getConnection();

//			if (graph != null) // TODO BUG graph no puede ser null aquí !!!
			{
				Collection<Node> nodes;
				Collection<Edge> edges;
	// Asume que si los nombres coinciden es que la red seleccionada proviene de la base de datos Â¡Â¡Incorrecto!!			
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
					} else if (e instanceof ILocalGISEdge){
						db.writeEdge(e, conn);
						Set incidents = ((ILocalGISEdge)e).getIncidents();
						if(!incidents.isEmpty())
							db.writeIncidents(e, conn);
					}
					i ++;
				}
				
//				System.out.println("Tiempo en grabar arcos:" + (System.currentTimeMillis() - timeInit) );

			db.writeNetworkProperties(NetworkModuleUtilWorkbench.getNetworkManager(context).getNetwork(subred).getProperties(), conn);

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
     * @deprecated unused
     */
    @Deprecated
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
