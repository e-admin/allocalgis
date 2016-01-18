/**
 * NetworkModuleUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.batik.bridge.UpdateManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.io.GraphReaderWriter;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.io.SpatialReaderWriter;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.manager.AbstractMemoryManager;
import org.uva.route.manager.SpatialMemoryManager;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicInterNetworker;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.managers.AllInMemoryExternalSourceGraphMemoryManager;
import org.uva.routeserver.managers.ChangesMonitoredMemoryManager;
import org.uva.routeserver.managers.GraphMemoryManager;
import org.uva.routeserver.managers.UpdateMonitor;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;
import com.localgis.route.graph.build.LocalGisBasicLineGraphGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISGraphBuilder;
import com.localgis.route.graph.build.dynamic.LocalGISStreetBasicLineGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphBuilder;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphGenerator;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.manager.LocalGISAllinMemoryManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.ConnectionUtilities;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jump.util.FileUtil;

public class NetworkModuleUtil
{
    static final Log LOGGER=LogFactory.getLog(NetworkModuleUtil.class);
    public static boolean comprobarTablaTramos(Connection conn)
    {
	String tableTramos = "select * from network_edges limit 1";
	PreparedStatement tramosPreparedStatement = null;
	try
	    {
		tramosPreparedStatement = conn.prepareStatement(tableTramos);
		tramosPreparedStatement.executeQuery();
	    } catch (SQLException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return false;
	    }
	finally
	    {
		try
		    {
			tramosPreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }
	return true;
    }

    public static boolean comprobarTablaNodos(Connection conn)
    {
	// TODO Auto-generated method stub
	String tableNodos = "select * from network_nodes limit 1";
	PreparedStatement nodosPreparedStatement = null;
	try
	    {
		nodosPreparedStatement = conn.prepareStatement(tableNodos);
		nodosPreparedStatement.executeQuery();
	    } catch (SQLException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return false;
	    }
	finally
	    {
		try
		    {
			nodosPreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }
	return true;
    }

    public static boolean comprobarTablaSubRedes(Connection conn)
    {
	String tableSubredes = "select * from networks limit 1";
	PreparedStatement subredesPreparedStatement = null;
	try
	    {
		subredesPreparedStatement = conn.prepareStatement(tableSubredes);
		subredesPreparedStatement.executeQuery();
	    } catch (SQLException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return false;
	    }
	finally
	    {
		try
		    {
			subredesPreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }
	return true;
    }

    /**
     * Crea el modelo de soporte para redes si es necesario Solo para depuración o versiones previas a Localgis 3.0
     * 
     * @deprecated 3.0
     * @param conn
     */
    public static void createNetworkModel(Connection conn)
    {
	boolean tableSubredesExist = NetworkModuleUtil.comprobarTablaSubRedes(conn);
	boolean tableNodosExist = NetworkModuleUtil.comprobarTablaNodos(conn);
	boolean tableTramosExist = NetworkModuleUtil.comprobarTablaTramos(conn);
	if (!tableSubredesExist)
	    {
		crearTablaSubredes(conn);
	    }
	if (!tableNodosExist)
	    {
		crearTablaNodos(conn);
	    }
	if (!tableTramosExist)
	    {
		crearTablaTramos(conn);
	    }
    }

    /**
     * TODO Los modelos de datos se deben instalar desde el servidor y no en una aplicación de cliente
     * 
     * @deprecated
     */
    @Deprecated
    public static void crearTablaSubredes(Connection conn)
    {
	PreparedStatement subredesCreatePreparedStatement = null;
	String createTablesubredes = "CREATE TABLE networks(id_network serial NOT NULL," + "network_name character(20) NOT NULL unique,"
		+ "comment character(100)," + "CONSTRAINT networks_pkey PRIMARY KEY (id_network) );" + "ALTER TABLE networks OWNER TO geopista;";
	try
	    {
		subredesCreatePreparedStatement = conn.prepareStatement(createTablesubredes);
		subredesCreatePreparedStatement.execute();
	    } catch (SQLException e)
	    {
		throw new RuntimeException("No puedo crear tabla para soporte de redes.");
	    }
	finally
	    {
		try
		    {
			subredesCreatePreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }

    }

    /**
     * TODO Los modelos de datos se deben instalar desde el servidor y no en una aplicación de cliente
     * 
     * @deprecated
     */
    @Deprecated
    public static void crearTablaNodos(Connection conn)
    {

	PreparedStatement nodosCreatePreparedStatement = null;
	String tablanodos = "CREATE TABLE network_nodes(" + "id_network integer NOT NULL," + "id_node integer NOT NULL," + "node_geometry geometry,"
		+ "CONSTRAINT network_nodes_pkey PRIMARY KEY (id_network, id_node));" + "ALTER TABLE network_nodes OWNER TO geopista";

	try
	    {
		nodosCreatePreparedStatement = conn.prepareStatement(tablanodos);
		nodosCreatePreparedStatement.execute();
	    } catch (SQLException e)
	    {
		throw new RuntimeException("No puedo crear tabla para soporte de nodos.");
	    }
	finally
	    {
		try
		    {
			nodosCreatePreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }
    }

    /**
     * TODO Los modelos de datos se deben instalar desde el servidor y no en una aplicación de cliente
     * 
     * @deprecated
     */
    @Deprecated
    public static void crearTablaTramos(Connection conn)
    {
	PreparedStatement tramosCreatePreparedStatement = null;
	String tablatramos = "CREATE TABLE network_edges(" + "id_network integer NOT NULL," + "id_edge integer NOT NULL," + "id_nodea integer NOT NULL,"
		+ "id_network_nodea integer NOT NULL," + "id_nodeb integer NOT NULL," + "id_network_nodwb integer NOT NULL,"
		+ "edge_length double precision NOT NULL," + "id_layer int4," + "id_feature numeric(8),"
		+ "CONSTRAINT network_edges_pkey PRIMARY KEY (id_network, id_edge));" + "ALTER TABLE network_edges OWNER TO geopista";

	try
	    {
		tramosCreatePreparedStatement = conn.prepareStatement(tablatramos);
		tramosCreatePreparedStatement.execute();
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	finally
	    {
		try
		    {
			tramosCreatePreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }

    }

    public static final int _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY = 100000;

    /**
     * Crea un {@link GraphGenerator} para la entidad actual según {@link AppContext#getIdEntidad()}
     * 
     * @return
     */
    public static GraphGenerator getBasicLineGraphGenerator()
    {
	return getBasicLineGraphGenerator(AppContext.getIdEntidad());
    }

    /**
     * Crea un LocalGisBasicLineGraphGenerator con un plan de numeración de elementos basado en el código de entidad Reserva
     * {@link #_MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY}={@value #_MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY} elementos en cada entidad.
     * 
     * @param entidad
     * @return
     */
    private static GraphGenerator getBasicLineGraphGenerator(int entidad)
    {
	SequenceUIDGenerator uidGenerator = getUIDGenerator(entidad);
	LocalGISGraphBuilder graphBuilder = new LocalGISGraphBuilder(uidGenerator);
	LocalGisBasicLineGraphGenerator generator = new LocalGisBasicLineGraphGenerator(graphBuilder);
	return generator;
    }

    public static GraphGenerator getLocalGISStreetBasicLineGraphGenerator(int entidad)
    {
	SequenceUIDGenerator uidGenerator = getUIDGenerator(entidad);
	LocalGISGraphBuilder graphBuilder = new LocalGISStreetGraphBuilder(uidGenerator);
	LocalGISStreetBasicLineGenerator generator = new LocalGISStreetBasicLineGenerator(graphBuilder);
	return generator;

    }

    public static SequenceUIDGenerator getUIDGenerator(int entidad)
    {
	SequenceUIDGenerator uidGenerator = new SequenceUIDGenerator();
	uidGenerator.setSeq(entidad * _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY);
	uidGenerator.setMaxId((entidad + 1) * _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY - 1);
	return uidGenerator;
    }
   

    /**
     * Reduce la geometria a un linestrng adecuado para representar un Edge
     * 
     * @param geometryObject
     * @return
     * @throws IllegalArgumentException
     *             si la geometria no se puede simplificar adecuadamente
     */
    public static LineString getEdgeLineStringFromGeometry(Geometry geometryObject)
    {
	LineString lineStringFromGeometry = null;
	if (geometryObject instanceof MultiLineString)
	    {
		MultiLineString mls = (MultiLineString) geometryObject;
		if (!(mls.isEmpty()))
		    {

			Coordinate[] coordmls = mls.getCoordinates();// FIX No se debe sobre-simplificar a un linestring lineal!

			// Coordinate[] coordmls= null;
			// //add ls built for the first and last coordinate.
			// Coordinate c0=mls.getGeometryN(0).getCoordinate();
			// Coordinate c2=mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates()[
			// mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates().length -1
			// ];
			// coordmls=new Coordinate[] {c0, c2};
			CoordinateSequence seq = new CoordinateArraySequence(coordmls);
			lineStringFromGeometry = new LineString(seq, new GeometryFactory());
		    }

	    } else if (geometryObject instanceof LineString)
	    {
		lineStringFromGeometry = (LineString) geometryObject;
	    } else
	    {
		throw new IllegalArgumentException("Geometry " + geometryObject + " is not compatible with Linestring.");
	    }
	return lineStringFromGeometry;
    }

   

    public static Network addNewNetwork(NetworkManager networkMgr, String nombrered, Graph graph, Map<String, Object> networkProperties)
    {
	/*
	 * TODO hacer cambios de basicInternetWorker a null cuando obtienes FuncionesAuxiliares.getNetworManager()
	 */
	if (((BasicNetworkManager) networkMgr).getInterNetworker() == null)
	    {
		BasicInterNetworker binNet = new BasicInterNetworker();
		binNet.setNetworkManager(networkMgr);
		networkMgr.setInterNetworker(binNet);
	    }

	Network parent = networkMgr.putNetwork(nombrered, graph);

	// Cargo las propiedades de la red.
	for (int i = 0; i < networkProperties.keySet().size(); i++)
	    {
		String key = (String) networkProperties.keySet().toArray()[i];
		NetworkProperty value = (NetworkProperty) networkProperties.get(key);
		parent.getProperties().put(key, value);
	    }
	return parent;
    }

   
    /**
     * Crea un Graph con gestión de LocalGIS
     * 
     * @param graph
     * @return
     */
    public static DynamicGraph getNewInMemoryGraph(Graph graph)
    {
	try
	    {
		LocalGISAllinMemoryManager memmgr = new LocalGISAllinMemoryManager(new NullStore());
		memmgr.setAutoCommit(false);
		DynamicGraph subnetGraph = new DynamicGraph(memmgr);
		if (graph != null)
		    subnetGraph.getMemoryManager().appendGraph(graph);
		return subnetGraph;
	    } catch (IOException e)
	    {
		throw new RuntimeException(e);
	    }
    }
    
    public static String getQueryFromIdLayer(Connection con, int idLayer)
    {

	String unformattedQuery = "";
	String sqlQuery = "select name from layers where id_layer = " + idLayer;
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	try
	    {
		if (con != null)
		    {
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idLayer);
			rs = preparedStatement.executeQuery();

			if (rs.next())
			    {
				unformattedQuery = rs.getString("name");
			    }
			preparedStatement.close();
			rs.close();
		    }
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }

	return unformattedQuery;

    }

    public static enum Operations {
	LOAD, WRITE, LOAD_MERGING
    };

    
    /**
     * Cambia los identificadores del grafo toBeRenumberedGraph El {@link UpdateManager} que pueda tener el grafo puede contener elementos duplicados al haber
     * cambiado la clave principal de los objetos, pero no se hace nada al respecto WARNING!.
     * 
     * @param referenceGraph
     * @param toBeRenumberedGraph
     * @return {@link SequenceUIDGenerator} con la numeración ajustada para continuar trabajando en el grafo.
     */
    public static SequenceUIDGenerator renumberElements(Graph referenceGraph, Graph toBeRenumberedGraph)
    {
	SequenceUIDGenerator uid = getUIDGenerator(referenceGraph, AppContext.getIdEntidad());

	Collection<Node> previousNodes = toBeRenumberedGraph.getNodes();
	Collection<Edge> previousEdges = toBeRenumberedGraph.getEdges();

	for (Node node : previousNodes)
	    {
		node.setID(uid.getNodeUniqueID());
	    }
	for (Edge edge : previousEdges)
	    {
		edge.setID(uid.getEdgeUniqueID());
	    }
	// rebuild Maps 
	if (toBeRenumberedGraph instanceof DynamicGraph)
	{
		DynamicGraph dynGraph = (DynamicGraph) toBeRenumberedGraph;
		GraphMemoryManager absMemMgr = dynGraph.getMemoryManager();
		absMemMgr.getEdgesInMemory();
		if (absMemMgr instanceof AbstractMemoryManager) {
			AbstractMemoryManager memMgr = (AbstractMemoryManager) absMemMgr;
			memMgr.rebuildMap();// TODO refactorizar internamente en el memoryManager
		}
	}
	return uid;
    }

    /**
     * Calcula la secuencia con el máximo identificador presente en el grafo
     * 
     * @param selectedGraph
     * @return
     */
    public static SequenceUIDGenerator getUIDGenerator(Graph selectedGraph, int entidad)
    {
	int max = entidad * _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY;
	for (Edge edge : selectedGraph.getEdges())
	    {
		max = Math.max(max, edge.getID());
	    }
	for (Node node : selectedGraph.getNodes())
	    {
		max = Math.max(max, node.getID());
	    }
	SequenceUIDGenerator uidGenerator = new SequenceUIDGenerator();
	uidGenerator.setSeq(max + 1);
	uidGenerator.setMaxId((entidad + 1) * _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY - 1);
	return uidGenerator;
    }

    /**
     * Extrae el memorymanager de tip {@link LocalGISAllinMemoryManager}
     * 
     * @param graph
     * @return null si no es un grafo gestionado por {@link LocalGISAllinMemoryManager}
     */
    public static LocalGISAllinMemoryManager castToLocalGISAllinMemoryManager(Graph graph)
    {

	if (graph instanceof DynamicGraph)
	    {
		DynamicGraph dynGraph = (DynamicGraph) graph;
		GraphMemoryManager memMgr = dynGraph.getMemoryManager();
		if (memMgr instanceof LocalGISAllinMemoryManager)
		    {
			return (LocalGISAllinMemoryManager) memMgr;
		    }
	    }
	return null;
    }
    /**
     * Extrae el memorymanager de tip {@link LocalGISAllinMemoryManager}
     * 
     * @param graph
     * @return null si no es un grafo gestionado por {@link LocalGISAllinMemoryManager}
     */
    public static SpatialMemoryManager castToSpatialMemoryManager(Graph graph)
    {

	if (graph instanceof DynamicGraph)
	    {
		DynamicGraph dynGraph = (DynamicGraph) graph;
		GraphMemoryManager memMgr = dynGraph.getMemoryManager();
		if (memMgr instanceof SpatialMemoryManager)
		    {
			return (SpatialMemoryManager) memMgr;
		    }
	    }
	return null;
    }
    /**
     * Extrae el readerWriter del grafo
     * 
     * @param graph
     * @return null si no es un grafo gestionado 
     */
    public static SpatialReaderWriter extractSpatialReaderWriter(Graph graph)
    {

	if (graph instanceof DynamicGraph)
	    {
		DynamicGraph dynGraph = (DynamicGraph) graph;
		GraphMemoryManager memMgr = dynGraph.getMemoryManager();
		if (memMgr instanceof AllInMemoryExternalSourceGraphMemoryManager)
		    {
			AllInMemoryExternalSourceGraphMemoryManager storeMemMgr = ((AllInMemoryExternalSourceGraphMemoryManager) memMgr);
			GraphReaderWriter readerWriter = storeMemMgr.getStore();
			if (readerWriter instanceof SpatialReaderWriter)
				return (SpatialReaderWriter)readerWriter;
		    }
	    }
	return null;
    }
    /**
     * Guarda las propiedades en la base de datos.
     * 
     * @param selectedNet
     */
    public static void writeNetworkProperties(Network selectedNet) throws IOException
    {
	LocalGISAllinMemoryManager lgMgr = castToLocalGISAllinMemoryManager(selectedNet.getGraph());
	if (lgMgr != null)
	    if (lgMgr.getStore() instanceof LocalGISRouteReaderWriter)
		((LocalGISRouteReaderWriter) lgMgr.getStore()).writeNetworkProperties(selectedNet.getProperties());
    }

    public static String getLayerIDFromIntIdLayer(Connection con, int idLayer)
    {

	String unformattedQuery = "";
	String sqlQuery = "select name from layers where id_layer = " + idLayer;
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	try
	    {
		if (con != null)
		    {
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idLayer);
			rs = preparedStatement.executeQuery();

			if (rs.next())
			    {
				unformattedQuery = rs.getString("name");
			    }
			preparedStatement.close();
			rs.close();
		    }
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }

	return unformattedQuery;

    }

    /**
     * Comprueba la orientación del {@link LineString} y devuelve la geometria acorde al {@link Edge}
     * 
     * @param edge
     * @param lineString
     * @return
     */
    public static LineString checkLineStringOrientation(Edge edge, LineString lineString)
    {
	edge = (Edge) NodeUtils.unwrapProxies(edge);
	XYNode xyNodeA = (XYNode) ((edge.getNodeA() instanceof XYNode) ? edge.getNodeA() : null);
	XYNode xyNodeB = (XYNode) ((edge.getNodeB() instanceof XYNode) ? edge.getNodeB() : null);

	Coordinate coordNodeA = xyNodeA != null ? xyNodeA.getCoordinate() : null;
	Coordinate coordNodeB = xyNodeB != null ? xyNodeB.getCoordinate() : null;
	// Calcula la distancia de un extremo
	Coordinate lineStringStartCoord = lineString.getCoordinateN(0);
	Coordinate lineStringEndCoord = lineString.getCoordinateN(lineString.getNumPoints() - 1);
	boolean needReverse = false;
	if (coordNodeB == null && coordNodeA != null) // use only coordNodeA
	    {
		double distanceToStart = lineStringStartCoord.distance(coordNodeA);
		double distanceToEnd = lineStringEndCoord.distance(coordNodeA);
		if (distanceToStart < distanceToEnd)
		    needReverse = true;
	    } else if (coordNodeB != null && coordNodeA == null) // use only coordNodeB
	    {
		double distanceToStart = lineStringStartCoord.distance(coordNodeB);
		double distanceToEnd = lineStringEndCoord.distance(coordNodeB);
		if (distanceToStart > distanceToEnd)
		    needReverse = true;
	    }
	if (coordNodeB != null && coordNodeA != null) // use best match for both coords
	    {
		double distanceNodeA = lineStringStartCoord.distance(coordNodeA);
		double distanceNodeB = lineStringStartCoord.distance(coordNodeB);
		if (distanceNodeA > distanceNodeB)
		    needReverse = true;
	    }

	if (!needReverse)
	    return lineString;
	else
	    {

		// Class<? extends LineString> clasz = lineString.getClass();
		// Method[] methods = clasz.getMethods();
		// Method method=clasz.getMethod("reverse", null);
		// LineString rev=(LineString) method.invoke(lineString, null);
		LineString rev = (LineString) lineString.reverse(); // JPC misterioso BUG que hace que no se encuentre el mÃ©todo Â¿Â¿Â¿Â¿????
		return rev;

	    }
    }

    public static Network getSubNetwork(NetworkManager networkMgr, String red, String subredName)
    {
	Network selectedNet = null;
	if (red != null && !red.equals(""))
	    {
		if (subredName != null && !subredName.equals(""))
		    {
			selectedNet = networkMgr.getNetwork(red).getSubNetwork(subredName);
		    } else
		    {
			selectedNet = networkMgr.getNetwork(red);
		    }
	    }
	return selectedNet;
    }

    public static void addDirtyGraphablesIsSupported(Graph graph, List<? extends Graphable> edgesWithFeaturesList)
    {
	if (graph instanceof DynamicGraph)
	    {
		GraphMemoryManager memmgr = (((DynamicGraph) graph).getMemoryManager());
		if (memmgr instanceof ChangesMonitoredMemoryManager)
		    {
			UpdateMonitor memMonitor = null;
			ChangesMonitoredMemoryManager monitoredMemmgr = (ChangesMonitoredMemoryManager) memmgr;
			memMonitor = monitoredMemmgr.getUpdateMonitor();
			memMonitor.addDirtyGraphables(edgesWithFeaturesList);
		    }
	    }
    }

    

    /**
     * Actualiza todos los {@link LocalGISStreetDynamicEdge} para fijar la networkId de los nodeA y nodeB
     * 
     * @param thisNetwork
     * @param networkId
     */
    public static void updateLocalGISEdgesWithNetworkId(Graph graph, int networkId)
    {
	// Graph graph = thisNetwork.getGraph();
	for (Object edges : graph.getEdges())
	    {
		if (edges instanceof LocalGISStreetDynamicEdge)
		    {
			LocalGISStreetDynamicEdge lgEdge = (LocalGISStreetDynamicEdge) edges;
			lgEdge.setIdNetworkNodeA(networkId);
			lgEdge.setIdNetworkNodeB(networkId);
		    }
	    }
    }

    /**
     * AÃ±ade el contenido del {@link StringBuilder} al fichero  localPath+"/cargaMediciones.txt"
     * @param stBuilder
     */
    public static void logActivityToFile(StringBuilder stBuilder)
	{
	    try{
		String retornoDeCarro=System.getProperty("line.separator");
	    	String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY, UserPreferenceConstants.DEFAULT_DATA_PATH, true);
	    	StringBuilder log=new StringBuilder("========================================").append(retornoDeCarro);
	    	log.append("Paso de parámetros finalizada el:").append(new Date()).append(retornoDeCarro);
	    	log.append("========================================").append(retornoDeCarro);
	    	log.append(stBuilder);
	    	File file = new File (localPath+"/cargaMediciones.txt");
	    	if (file.exists())
	    		file.delete();
	    	FileUtil.setContents(file.getAbsolutePath(), log.toString());
	    }catch(Exception e){
	    	LOGGER.error("Error al escribir en el fichero de logs ",e);
	    	e.printStackTrace();
	    }
	}

	public static Map<String, NetworkProperty> readNetworkPropertiesFromDB(String netWorkName, Connection connection) throws SQLException
	{
	LocalGISNetworkDAO dao = new LocalGISNetworkDAO();
	
	return dao.readNetworkProperties(netWorkName, connection);
	}

	static GraphGenerator getLocalGISStreetGraphGenerator(int entidad)
	{
	SequenceUIDGenerator uidGenerator = NetworkModuleUtil.getUIDGenerator(entidad);
	LocalGISGraphBuilder graphBuilder = new LocalGISStreetGraphBuilder(uidGenerator);
	LocalGISStreetGraphGenerator generator = new LocalGISStreetGraphGenerator(graphBuilder);
	return generator;
	
	}

	public static Geometry getDBGeometryForEdge(Edge edge, Graph graph,String srid)
	{
		LocalGISRouteReaderWriter spatialRW=extractLocalGISSpatialReaderWriter(graph);
		if (spatialRW!=null)
		{
		Geometry geom = spatialRW.getGeometryForEdge(edge,srid);
		return geom;
		}
		else
		return null;
	}

	private static LocalGISRouteReaderWriter extractLocalGISSpatialReaderWriter(Graph graph)
	{
		SpatialReaderWriter rw = extractSpatialReaderWriter(graph);
		if (rw instanceof LocalGISRouteReaderWriter) {
			LocalGISRouteReaderWriter lgRW = (LocalGISRouteReaderWriter) rw;
			return lgRW;
		}
		return null;
	}
}
