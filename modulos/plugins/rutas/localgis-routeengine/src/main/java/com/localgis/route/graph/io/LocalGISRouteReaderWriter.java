/**
 * LocalGISRouteReaderWriter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.io;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.route.datastore.RouteResultSet;
import org.uva.route.graph.io.DBRouteServerReaderWriter;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.io.NetworkLinkReaderWriter;
import org.uva.routeserver.ElementNotFoundException;
import org.uva.routeserver.NotImplementedException;

import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.util.UserCancellationException;
import com.localgis.route.graph.build.UIDgenerator.FixedValueUIDGenerator;
import com.localgis.route.graph.build.UIDgenerator.LocalGISDBSequenceUIDGenerator;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.NetworkEdge;
import com.localgis.route.manager.LocalGISAllinMemoryManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.LocalGISGeometryBuilder;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.task.TaskMonitor;

/**
 * 
 * 
 *
 */
// TODO: Pensar como cambiarlo para modo Oracle
public class LocalGISRouteReaderWriter extends DBRouteServerReaderWriter 
implements NetworkPropertiesReaderWriter, NetworkLinkReaderWriter
{

    public static final String NETWORK_LINKS_NETNAME = "NetworkLinks";
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    protected LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
    protected String networkName;
    protected Integer networkId;
    private static Logger LOGGER = Logger.getLogger(LocalGISRouteReaderWriter.class);
    private RouteConnectionFactory routeConnectionFactory;
    private WeakReference<TaskMonitor> taskMonitor;

    protected Connection getConnection()
    {
	return routeConnectionFactory.getConnection();
    }

    public LocalGISRouteReaderWriter(RouteConnectionFactory routeConnectionFactory) {
	super();
	this.routeConnectionFactory = routeConnectionFactory;
	init();
    }

    public String getQueryForReadingNetworkId()
    {
	return networkDAO.getQueryForReadingNetworkId(this.getNetworkName());
    }

    public Graph read() throws IOException
    {
	Graph graph = null;
	Connection connection = null;
	LOGGER.debug("Reading default graph :");
	GraphGenerator generator = NetworkModuleUtil.getBasicLineGraphGenerator();
	connection = getConnection();
	try
	    {
		graph = networkDAO.readNetwork(this.getNetworkName(), generator, connection);
	    } catch (Exception e)
	    {
		LOGGER.error("Error reading graph." + e.getLocalizedMessage());
		throw new IOException(e);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
	return graph;
    }

    protected String getQueryForReadingNetworkId(String networkName)
    {
	return networkDAO.getQueryForReadingNetworkId(networkName);
    }

    protected Object readInternal(ResultSet rs)
    {
	Connection connection = null;
	connection = getConnection();
	Object data = null;
	try
	    {
		data = networkDAO.readInternal(rs, false, connection);
	    } catch (Exception e)
	    {
		LOGGER.error("Error reading resultset", e);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
	return data;
    }

    public void deleteEdge(Edge edge)
    {
	Connection connection = null;
	try
	    {
		connection = getConnection();
		networkDAO.deleteEdge(edge, networkName, connection);
	    } catch (Exception e)
	    {
		LOGGER.error("Error writing edge", e);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
    }

    public List<Integer> getNodesNearTo(org.opengis.geometry.primitive.Point point, double radius, int num) throws IOException
    {
	Connection connection = null;
	List<Integer> nodes = null;
	connection = getConnection();
	try
	    {
		nodes = networkDAO.getNodesNearTo(point, radius, num, this.getNetworkName(), connection);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
	return nodes;
    }

    // Mï¿½todo sobreescrito para evaluar puntos con srid distintos a la hora de realizar la consulta.
    protected String getQueryForReadingNodesByDistance(Geometry point, double radius, int limit)
    {
	return networkDAO.getQueryForReadingNodesByDistance(point, radius, limit, this.getNetworkName());
    }

    private Graph queryDataBaseForGraph(String query) throws SQLException
    {
	Graph newGraph = null;
	Connection connection = null;
	connection = getConnection();
	try
	    {
		newGraph = networkDAO.queryDataBaseForGraph(query, false, connection);
		ArrayList<LocalGISDynamicEdge> edges = new ArrayList(newGraph.getEdges());
		Iterator<LocalGISDynamicEdge> itEdges = edges.iterator();
		while (itEdges.hasNext())
		    {
			LocalGISDynamicEdge localGISEdge = itEdges.next();
			networkDAO.getIncidents(networkName, localGISEdge, connection);
		    }
	    } catch (Exception e)
	    {
		LOGGER.error("Error querying database for graph", e);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
	return newGraph;
    }

    public Graph read(int nodeId) throws IOException, ElementNotFoundException
    {
	try
	    {
		return queryDataBaseForGraph(networkDAO.getQueryForReadingASectionOfGraphRelatedWithID(nodeId, getNetworkName()));
	    } catch (SQLException e)
	    {
		throw new IOException(e + "Imposible read ( " + nodeId + " )");
	    }

    }

    protected String getQueryForReadingEntireGraph()
    {
	return networkDAO.getQueryForReadingEntireGraph(this.getNetworkName());
    }

    public String getQueryForReadingASectionOfGraphRelatedWithID(int id)
    {
	return networkDAO.getQueryForReadingASectionOfGraphRelatedWithID(id, this.getNetworkName());
    }

    protected void init()
    {

	this.idGenerator = new LocalGISDBSequenceUIDGenerator(routeConnectionFactory);

    }

    public int getEdgeUniqueID()
    {
	return this.idGenerator.getEdgeUniqueID();
    }

    public int getNodeUniqueID()
    {
	return this.idGenerator.getNodeUniqueID();
    }

    public void write(Graph g)
    {
	if (g.getEdges().isEmpty())
	    return;
	Connection connection = null;
	connection = getConnection();
	boolean autoCommit = true;
	try
	    {
		autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
	    } catch (SQLException e)
	    {
		LOGGER.error("Error obtaining autocommit status.", e);
	    }
	try
	    {
		networkDAO.write(g, getNetworkName(), networkId, false, connection);
		connection.commit();
	    } catch (Exception e)
	    {
		try
		    {
			connection.rollback();
		    } catch (SQLException e1)
		    {
			LOGGER.error("Rollback error", e);
		    }
		LOGGER.error("Error obtaining autocommit status.", e);
	    }
	finally
	    {
		try
		    {
			connection.setAutoCommit(autoCommit);
		    } catch (SQLException e)
		    {
			LOGGER.error("Error settin autocommit status.", e);
		    }
		ConnectionUtilities.closeConnection(connection);
	    }

    }

    public void writeEdge(Edge edge, Connection connection) throws SQLException
    {
	networkDAO.writeEdge(edge, getNetworkName(), networkId,connection);
    }

    public void writeEdge(Edge edge)
    {
	Connection connection = null;
	connection = getConnection();
	try
	    {
		networkDAO.writeEdge(edge, getNetworkName(), networkId,connection);
	    } catch (Exception e)
	    {
		LOGGER.error("Error writing edge", e);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }

    }

    public void writeNode(Node node, Connection connection) throws SQLException
    {

	networkDAO.writeNode(node, getNetworkName(),networkId, false, connection);

    }

    public void writeNode(Node node)
    {
	Connection connection = null;
	connection = getConnection();
	try
	    {
		networkDAO.writeNode(node, getNetworkName(),networkId, false, connection);
	    } catch (Exception e)
	    {
		LOGGER.error("Error writing node", e);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
    }
    private void writeNodes(Collection<Node> nodes) throws SQLException
    {
	Connection connection = getConnection();
	networkDAO.setTaskMonitor(taskMonitor.get());
	networkDAO.writeNodes(nodes, getNetworkName(),networkId, false, connection);
	connection.commit();
    }
    private void writeEdges(Collection<Edge> edges) throws SQLException
    {
	Connection connection = getConnection();
	networkDAO.setTaskMonitor(taskMonitor.get());
	networkDAO.writeEdges(edges, getNetworkName(),networkId, false, connection);
	connection.commit();
    }
    public String getNetworkName()
    {
	return networkName;
    }

    @Override
    public void setNetworkName(String networkname) throws SQLException
    {
	ResultSet rs = null;
	PreparedStatement st = null;
	Connection connection = getConnection();
	try
	    {
		LocalGISNetworkDAO dao = new LocalGISNetworkDAO();

		this.networkName = networkname;

		Integer networkId = dao.getNetworkId(networkName, connection);
		if (networkId == null)
		    {
			// registra nueva red
			dao.registerNetworkInDatabase(networkname, connection);
			networkId = dao.getNetworkId(networkName, connection);
		    }

		this.networkId = networkId;

	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
    }

    protected void addBatchUpdateEdge(Statement st, Edge e) throws SQLException
    {
	String sql = networkDAO.getBatchQueryUpdateEdge(this.networkName, (LocalGISDynamicEdge) e);
	Connection connection = routeConnectionFactory.getConnection();
	try
	    {
		networkDAO.updateQuery(sql, connection);
	    } catch (Exception e1)
	    {
		LOGGER.error("Error updating network", e1);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }

    }

    protected void addBatchUpdateNode(Statement st, Node node) throws SQLException
    {
	String sql = networkDAO.getBatchQueryUpdateNode(this.networkName, node);
	Connection connection = routeConnectionFactory.getConnection();
	try
	    {
		networkDAO.updateQuery(sql, connection);
	    } catch (Exception e1)
	    {
		LOGGER.error("Error updating network", e1);
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
    }

    // TODO: Metodo cambiado por el interfaz. Devuelve los edges y nodes no actualizados.
    public Graph update(Graph g) throws IOException
    {
	if (g.getEdges().isEmpty() && g.getNodes().isEmpty())
	    return g;
	Connection connection = null;
	connection = getConnection();
	boolean autoCommit = true;
	try
	    {
		autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
	    } catch (SQLException e)
	    {
		LOGGER.error("Error obtaining autocommit status.", e);
	    }
	try
	    {
		updateClientGraphInDatabase(g, connection);
		connection.commit();
	    } catch (UserCancellationException e)
	    {
		try
		    {
			connection.rollback();
		    } catch (SQLException e1){LOGGER.error("Rollback error", e);}
		
		throw e; // rethrow
	    } catch (Exception e)
	    {
		try
		    {
			connection.rollback();
		    } catch (SQLException e1)
		    {
			LOGGER.error("Rollback error", e);
		    }
		LOGGER.error("Error updating graph.", e);
		throw new IOException(e);
	    }
	finally
	    {
		try
		    {
			connection.setAutoCommit(autoCommit);
		    } catch (SQLException e)
		    {
			LOGGER.error("Error settin autocommit status.", e);
		    }
		ConnectionUtilities.closeConnection(connection);
	    }
	return new BasicGraph(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    /**
     * MÃ©todo Update para usarlo con {@link GeopistaConnection} que no soporta Updates
     * 
     * @param g
     * @param connection
     * @throws
     * @throws SQLException
     */
    private void updateClientGraphInDatabase(Graph graph, Connection conn) throws SQLException
    {
	Collection<Node> nodes = graph.getNodes();
	Collection<Edge> edges = graph.getEdges();
	if (edges.isEmpty() && nodes.isEmpty())
	    return;
	Connection connection=conn;
	long timeInit = System.currentTimeMillis();
	this.deleteEdges(edges);
	this.deleteNodes(nodes);
	
	this.writeNodes(nodes);
	
	if (taskMonitor.get() != null)
	    this.taskMonitor.get().report("Tiempo en grabar nodos:" + (System.currentTimeMillis() - timeInit));
	timeInit = System.currentTimeMillis();
	
	this.writeEdges(edges);
	
	connection.commit();
	if (taskMonitor.get() != null)
	    taskMonitor.get().report("Tiempo en grabar arcos:" + (System.currentTimeMillis() - timeInit));

    }

    

    public Graph readByEdge(Integer edgeId) throws IOException, ElementNotFoundException
    {
	try
	    {
		return queryDataBaseForGraph(getQueryForReadingASectionOfGraphRelatedWithEdgeID(edgeId));
	    } catch (SQLException e)
	    {
		throw new IOException("Imposible read ( " + edgeId + " )", e);
	    }

    }

    protected String getQueryForReadingASectionOfGraphRelatedWithEdgeID(Integer edgeId)
    {
	return networkDAO.getQueryForReadingASectionOfGraphRelatedWithEdgeID(networkName, edgeId);
    }

    public void writeNetworkProperties(Map<String, Object> networkProperties) throws IOException
    {
	try
	    {
		networkDAO.writeNetworkProperties(networkName, networkProperties, getConnection());
	    } catch (SQLException e)
	    {
		throw new IOException(e);
	    }
    }

    public void writeNetworkProperties(Map<String, Object> networkProperties, Connection connection) throws SQLException
    {
	networkDAO.writeNetworkProperties(networkName, networkProperties, connection);

    }
/**
 * Reduce la geometria al centroide TODO generalizar
 */
    protected String getQueryForReadingEdgesByDistance(Geometry where, double radius, int limit)
    {
	String sql_edgesNearto = "";
	String pointSectionWKT = LocalGISGeometryBuilder.getWKTSQLSectionGeometry(where, null);
	sql_edgesNearto = "SELECT *,distance(geom," + pointSectionWKT + ") AS distance " + " FROM network_edges WHERE id_network =  "
		+ getQueryForReadingNetworkId() + " AND distance(geom," + pointSectionWKT + ") <" + radius + " AND geom && expand(" + pointSectionWKT
		+ "::geometry," + radius + ") " + " order by distance";
	if (limit != 0)
	    {
		return sql_edgesNearto + " LIMIT " + limit;
	    }
	return sql_edgesNearto;
    }
    protected String getQueryForReadingEdgesByDistance(Point where, double radius, int limit)
    {
	String srid = LocalGISGeometryBuilder.guessSRIDCode(where);
	String sql_edgesNearto = "";
	String pointSectionWKT = LocalGISGeometryBuilder.getWKTSQLSectionGeometry(where, srid);
	sql_edgesNearto = "SELECT *,distance(geom," + pointSectionWKT + ") AS distance " + " FROM network_edges WHERE id_network =  "
		+ getQueryForReadingNetworkId() + " AND distance(geom," + pointSectionWKT + ") <" + radius + " AND geom && expand(" + pointSectionWKT
		+ "::geometry," + radius + ") " + " order by distance";
	if (limit != 0)
	    {
		return sql_edgesNearto + " LIMIT " + limit;
	    }
	return sql_edgesNearto;
    }

    public List<Integer> getEdgesNearTo(Point point, double radius, int num) throws IOException
    {
	Connection connection = null;
	List<Integer> edgesIdsNearto = new ArrayList<Integer>();
	try
	    {
		connection = routeConnectionFactory.getConnection();
		ArrayList<NetworkEdge> networkEdges = new ArrayList<NetworkEdge>(networkDAO.getNetworkEdgesNearTo(connection, this.getNetworkName(), point,
			radius, num));
		Iterator<NetworkEdge> networkEdgesIt = networkEdges.iterator();
		while (networkEdgesIt.hasNext() && edgesIdsNearto.size() < num)
		    {
			NetworkEdge networkEdge = networkEdgesIt.next();
			edgesIdsNearto.add(networkEdge.getIdEdge());
		    }
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(connection);
	    }
	return edgesIdsNearto;
    }

    public void writeIncidents(Edge e, Connection conn) throws SQLException
    {
	LocalGISStreetDynamicEdge edge = (LocalGISStreetDynamicEdge) e;
	networkDAO.setIncidents(networkName,networkId, edge, conn);
    }

    @Override
    protected void addBatchRemoveEdge(Statement st, Edge edge) throws SQLException
    {
	// TODO Auto-generated method stub

    }

    @Override
    protected void addBatchRemoveNode(Statement st, Node node) throws SQLException
    {
	// TODO Auto-generated method stub

    }

    @Override
    protected void addBatchWriteEdge(Statement st, Edge edge) throws SQLException
    {
	// TODO Auto-generated method stub

    }

    @Override
    protected void addBatchWriteNode(Statement st, Node node) throws SQLException
    {
	// TODO Auto-generated method stub

    }

    @Override
    protected DataSource createDataSource()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void createTables() throws SQLException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void destroyTables() throws SQLException, IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean existTables() throws SQLException
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    protected String getQueryForCountingNodes()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected String getQueryForReadingASectionOfGraphInBBox(BoundingBox bbox)
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getQueryForReadingASectionOfGraphRelatedToCoordinate(Point point, double tolerance)
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected String getQueryForRemovingSubGraph(Graph g)
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setUpTables() throws SQLException, IOException
    {
	Connection connection = getConnection();
	NetworkModuleUtil.createNetworkModel(connection);
	ConnectionUtilities.closeConnection(connection);
    }

    @Override
    public void deleteAll() throws IOException
    {
	Connection connection = getConnection();
	networkDAO.deleteNetworkEdgesFromDataBaseById(networkId, connection);
	networkDAO.deleteNetworkNodesFromDataBaseById(networkId, connection);
	networkDAO.deleteNetworkStreetPropertiesFromDataBaseById(getNetworkId(), getConnection());
	ConnectionUtilities.closeConnection(connection);
    }

    @Override
    public void releaseResources()
    {
	// TODO Auto-generated method stub

    }

    @Override
    protected String getQueryForNearestPointToEdge(Point point, int idEdge, String alias)
    {
	return null;
    }

    @Override
    protected String getQueryForNearestPointToEdgeRatio(Point point, int idEdge, String alias)
    {
	return null;
    }

    @Override
    protected String getQueryForCountingEdges()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected Geometry getFeature(int id_edge)
    {
	// TODO Auto-generated method stub
	return null;
    }

    public void deleteNodes(Collection<Node> nodes)
    {
	networkDAO.deleteNodes(nodes, this.networkId, routeConnectionFactory.getConnection());

    }

    public void deleteEdges(Collection<Edge> edges)
    {
	networkDAO.deleteEdges(edges, this.networkId, routeConnectionFactory.getConnection());
    }

    /**
     * Objeto para informar al interfaz de usuario
     * 
     * @param taskMonitor
     */
    public void setTaskMonitor(TaskMonitor taskMonitor)
    {
	this.taskMonitor = new WeakReference(taskMonitor);
    }

    @Override
    public Map<String, NetworkProperty> readNetworkProperties() throws IOException
    {
	try
	    {
		return networkDAO.readNetworkProperties(getNetworkName(), routeConnectionFactory.getConnection());
	    } catch (SQLException e)
	    {
		throw new IOException(e);
	    }
    }

    public int getNetworkId()
    {
	return networkId;
    }
/**
 * obtiene una geometría asociada al arco consultando a la base de datos LocalGIS
 * @param edge
 * @return
 */
	public Geometry getGeometryForEdge(Edge edge,String srid)
	{
		if (edge instanceof ILocalGISEdge)
		{
			ILocalGISEdge lgEdge = (ILocalGISEdge) edge;
			
			Connection conn = routeConnectionFactory.getConnection();
			int idLayer = lgEdge.getIdLayer();
			String queryLayer = networkDAO.getQueryFromIdLayer(conn, idLayer, srid);
			int idFeature = lgEdge.getIdFeature();
			LineString edgeLineString = NetworkModuleUtil.getEdgeLineStringFromGeometry(networkDAO.getMultiLinestringFromQueryAndIdFeature(conn, queryLayer, idFeature, Integer.parseInt(srid), LocalGISGeometryBuilder.getGeometryColumn(idLayer)));
			return NetworkModuleUtil.checkLineStringOrientation(lgEdge, edgeLineString);
		}
		return null;
	}

@Override
public Collection<NetworkLink> getNetworkLinksFor(Network network, NetworkManager netMgr) throws IOException
{
    String reportNetName=null;
    Collection<NetworkLink> results=new ArrayList<NetworkLink>();
    
    LocalGISAllinMemoryManager lgMemmgrA = NetworkModuleUtil.castToLocalGISAllinMemoryManager(network.getGraph());

    if (lgMemmgrA==null || !lgMemmgrA.isLinkedToDatabase())
        {
    	LOGGER.debug("NetworkLink no cargados para grafos locales. LocalGISRouteReaderWriter relaciona grafos de base de datos");
    	return results;
        }
    
    try
	{
	   
	    HashMap<Integer,String> netNamesCache=new HashMap<Integer,String>();
	    
	    Connection connection = getConnection();
	    ArrayList<RouteResultSet> resultSet = networkDAO.readNetworkLinks(network.getName(), connection);
	    // Transforma los registros en NetworkLinks
	    for(RouteResultSet link:resultSet)
		{
		int id_subred_nodoA = link.getId_subred_nodoA();
		String netAName=netNamesCache.get(id_subred_nodoA);
		if (netAName==null)
		    {
			netAName= networkDAO.getNetworkName(id_subred_nodoA, connection);
			netNamesCache.put(id_subred_nodoA, netAName);
		    }
		Network networkA = netMgr.getNetwork(netAName);
		if (networkA==null)
		    continue;
		int id_subred_nodoB = link.getId_subred_nodoB();
		String netBName=netNamesCache.get(id_subred_nodoB);
		if (netBName==null)
		    {
			netBName= networkDAO.getNetworkName(id_subred_nodoB, connection);
			netNamesCache.put(id_subred_nodoB, netBName);
		    }
		Network networkB = netMgr.getNetwork(netBName);
		
		if (networkB==null)
		    continue;
		LocalGISAllinMemoryManager lgMemmgrB = NetworkModuleUtil.castToLocalGISAllinMemoryManager(networkB.getGraph());
		if (lgMemmgrB==null || !lgMemmgrB.isLinkedToDatabase())
		    {
			LOGGER.debug("NetworkLink no relaciona dos grafos de base de datos"+link);
		    	continue;
		    }
		int id_nodoA = link.getId_nodoA();
		int id_nodoB = link.getId_nodoB();
		reportNetName=netAName; // For exception message
		Node nodeA = networkA.getGraph().getNode(id_nodoA);
		reportNetName=netBName;
		Node nodeB = networkB.getGraph().getNode(id_nodoB);
		
		NetworkLink nl = new NetworkLink(networkA, nodeA, networkB, nodeB,new FixedValueUIDGenerator(link.getId_edge()));
		networkA.addNetworkLink(nl);
		networkB.addNetworkLink(nl);
		results.add(nl);
		}
	    return results;
	    
	} catch (NoSuchAuthorityCodeException e)
	{
	   throw new IOException(e);
	} catch (SQLException e)
	{
	    throw new IOException(e);
	} catch (FactoryException e)
	{
	    throw new IOException(e);
	} catch (ElementNotFoundException e)
	{
	  throw new IOException("Estableciendo NetworkLink en un nodo de la red: "+reportNetName,e);
	}
}

@Override
public Collection<NetworkLink> getNetworkLinksFor(Network networkA, Network networkB, NetworkManager networkManager) throws IOException
{
    throw new NotImplementedException();
}

@Override
public void writeNetworkLinks(Network net, NetworkManager networkManager) throws IOException
{
    Set<NetworkLink> netLinks = net.getNetworkLinks();
    BatchConnectionFacade conn= new BatchConnectionFacade(getConnection(), 10);
    int total=netLinks.size();
    int i=1;
    for (NetworkLink networkLink : netLinks)
	{
	    writeNetworkLink(networkLink, conn);
	    if (taskMonitor.get()!=null)
		taskMonitor.get().report(i++,total," Guardando NetworkLink");
	}
}
public boolean writeNetworkLink(NetworkLink networkLink, Connection connection) throws IOException
{
    Network netA = networkLink.getNetworkA();
    Network netB = networkLink.getNetworkB();
LocalGISAllinMemoryManager lgMemmgrA = NetworkModuleUtil.castToLocalGISAllinMemoryManager(netA.getGraph());
LocalGISAllinMemoryManager lgMemmgrB = NetworkModuleUtil.castToLocalGISAllinMemoryManager(netB.getGraph());

if (lgMemmgrA==null || lgMemmgrB==null || !lgMemmgrA.isLinkedToDatabase()|| !lgMemmgrB.isLinkedToDatabase())
    {
	LOGGER.debug("NetworkLink no relaciona dos grafos de base de datos");
	return false;
    }

    try
	{
	    // Debe guardarlo en una red especial con nombre reservado
	    networkDAO.writeEdge(networkLink, NETWORK_LINKS_NETNAME,-1, connection);
	   // networkDAO.writeEdge(networkLink, networkLink.getNetworkB().getName(),-1, connection);
	} catch (SQLException e)
	{
	   throw new IOException(e);
	}

return true;
}

}