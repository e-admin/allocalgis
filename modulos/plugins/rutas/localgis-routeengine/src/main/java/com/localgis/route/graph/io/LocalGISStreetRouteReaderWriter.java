/**
 * LocalGISStreetRouteReaderWriter.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.io;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.opengis.referencing.FactoryException;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.localgis.route.datastore.LocalGISStreetResultSet;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISTurnImpedance;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.RouteConnectionFactory;
/**
 * ReaderWriter Genérico para {@link Connection} estándar. 
 * No válido para connections que no soporten Update o BatchUpdates
 * @author juacas
 *
 */
// TODO: Pensar como cambiarlo para modo Oracle
public class LocalGISStreetRouteReaderWriter extends LocalGISRouteReaderWriter  {
	
	boolean bPMRGraph = true;
	int inicioID = -1;
	int finID = -1;
	
	public LocalGISStreetRouteReaderWriter(RouteConnectionFactory routeConnectionFactory) 
	{
		super(routeConnectionFactory);
	}
	public LocalGISStreetRouteReaderWriter(RouteConnectionFactory routeConnectionFactory, boolean bPMRGraph) throws SQLException {
		super(routeConnectionFactory);
		//System.out.println("LocalGISStreetRouteReaderWriter");
		//System.out.println(bPMRGraph);
		this.bPMRGraph = bPMRGraph;
		if (this.bPMRGraph)
			this.networkDAO.setPMRGraph(true);
		// TODO Auto-generated constructor stub
	}
	public LocalGISStreetRouteReaderWriter(RouteConnectionFactory routeConnectionFactory, boolean bPMRGraph, int inicio, int fin) throws SQLException {
		super(routeConnectionFactory);
		this.bPMRGraph = bPMRGraph;
		if (this.bPMRGraph)
			this.networkDAO.setPMRGraph(true);
		this.inicioID=inicio;
		this.finID = fin;
	}
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(LocalGISStreetRouteReaderWriter.class);
	
	
	public Graph read() throws IOException {
		Graph graph = null;
		Connection connection = null;
		LOGGER.debug("Reading street graph :");
	GraphGenerator generator = (GraphGenerator) this.getProperty(GENERATOR);
	if (generator==null)
	    {
		LOGGER.debug("Using default Generator. Not setted in properties!");
		generator= NetworkModuleUtilWorkbench.getLocalGISStreetGraphGenerator();
	    }
		connection = getConnection();
		try{
			if (inicioID != -1)
				graph = networkDAO.readNetwork(this.getNetworkName(), generator,connection, inicioID, finID);
			else
				graph = networkDAO.readNetwork(this.getNetworkName(), generator,connection);
		}catch (FactoryException e) {
			LOGGER.error("Error reading graph.",e);
			throw new IOException(e);
		}
		catch(SQLException e)
		{
		    throw new IOException(e);
		}
		finally{
		
			ConnectionUtilities.closeConnection(connection);
		}
		return graph;
	}
	protected void getStreetData(String networkName,LocalGISStreetResultSet rss) {
		Connection connection = null;
		try {
			connection = getConnection();
			networkDAO.getStreetData(networkName, rss,connection);
		} catch (Exception e) {
			LOGGER.error("Error writing edge",e);
		} finally{
			ConnectionUtilities.closeConnection(connection);
		}
	}
	
	
	protected Object readInternal(ResultSet rs) {
		Connection connection = null;
		connection = getConnection();
		Object data = null;
		try {
			data = networkDAO.readInternal(rs, true,connection);
		}catch (Exception e) {
			LOGGER.error("Error reading resultset",e);
		}finally{
			ConnectionUtilities.closeConnection(connection);
		}
		return data;
		
	}
	public void write(Graph g){
	    if (g.getNodes().isEmpty() && g.getEdges().isEmpty())
		return;
		Connection connection = null;
		connection = getConnection();
		boolean autoCommit = true;
		try {
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			LOGGER.error("Error obtaining autocommit status.",e);
		}
		try{
			networkDAO.write(g,getNetworkName(),getNetworkId(),true,connection);
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.error("Rollback error",e);
			}
			LOGGER.error("Error obtaining autocommit status.",e);
		}finally{
			try {
				connection.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				LOGGER.error("Error settin autocommit status.",e);
			}
			ConnectionUtilities.closeConnection(connection);
		}

	}
	public void writeEdge( Edge edge) {
		Connection connection = null;
		try {
			connection = getConnection();
			networkDAO.writeEdge(edge,networkName,this.getNetworkId(),connection);
		} catch (Exception e) {
			LOGGER.error("Error writing edge",e);
		} finally{
			ConnectionUtilities.closeConnection(connection);
		}
	}
	public void deleteEdge( Edge edge) {
		Connection connection = null;
		try {
			connection = getConnection();
			networkDAO.deleteEdge(edge,networkName,connection);
		} catch (Exception e) {
			LOGGER.error("Error writing edge",e);
		} finally{
			ConnectionUtilities.closeConnection(connection);
		}
	}
	public void writeNode( Node node) {
		Connection connection = null;
		connection = getConnection();
		try {
			networkDAO.writeNode(node, getNetworkName(),getNetworkId(),true,connection);
		} catch (Exception e) {
			LOGGER.error("Error writing edge",e);
		} finally{
			ConnectionUtilities.closeConnection(connection);
		}
	}
	public Graph readByEdge(Integer edgeId) throws IOException, ElementNotFoundException
	{
		try
		{
			return queryDataBaseForGraph(getQueryForReadingASectionOfGraphRelatedWithEdgeID(edgeId));
		} catch (SQLException e)
		{
			throw new IOException("Imposible read ( " + edgeId + " )",e);
		}

	}
	private Graph queryDataBaseForGraph(String query) throws SQLException{
		Graph newGraph = null;
		Connection connection = null;
		connection = getConnection();
		try {
			newGraph = networkDAO.queryDataBaseForGraph(query, true,connection);
			ArrayList<LocalGISStreetDynamicEdge> edges = new ArrayList(newGraph.getEdges());
			Iterator<LocalGISStreetDynamicEdge> itEdges = edges.iterator();
			while(itEdges.hasNext()){
				LocalGISStreetDynamicEdge localGISEdge = itEdges.next();
				networkDAO.getIncidents(getNetworkName(), localGISEdge, connection);
				networkDAO.getStreetData(getNetworkName(), localGISEdge, connection);
			}
			ArrayList<Node> nodes = new ArrayList(newGraph.getNodes());
			ArrayList<Integer> idNodesWithImpedance = networkDAO.getNodesWithImpedance(networkName, connection);
			Iterator<Node> itNodes = nodes.iterator();
			while(itNodes.hasNext()){
				Node node = itNodes.next();
				Integer idNode = node.getID();
				if(idNodesWithImpedance.contains(idNode)){
					node.setObject(networkDAO.getImpedanceMatrix(networkName, node.getID(), connection));
					((DynamicGraph)newGraph).getMemoryManager();
				}
				
			}
		} catch (Exception e) {
			LOGGER.error("Error querying database for graph",e);
		}finally{
			ConnectionUtilities.closeConnection(connection);
		}
		return newGraph;
	}
	public void writeStreetData(Edge e, Connection conn) throws SQLException
	{
		LocalGISStreetDynamicEdge edge = (LocalGISStreetDynamicEdge) e;
		networkDAO.setStreetData(networkName,getNetworkId(), edge, conn);
	}
	
	public ArrayList<Integer> getNodesWithTurnImpedances(){
		Connection connection = null;
		try{
			connection = getConnection();
			return  networkDAO.getNodesWithImpedance(networkName, connection);
		}catch (Exception e) {
			LOGGER.error("Error querying for nodes with turn impedances",e);
			return null;
		}finally{
			ConnectionUtilities.closeConnection(connection);
		}
	}
	
	public LocalGISTurnImpedance getNodeTurnImpedances(Integer idNode){
		Connection connection = null;
		try{
			connection = getConnection();
			return  networkDAO.getImpedanceMatrix(networkName, idNode, connection);
		}catch (Exception e) {
			LOGGER.error("Error querying for nodes with turn impedances",e);
			return null;
		}finally{
			ConnectionUtilities.closeConnection(connection);
		}
	}
	@Override
	public void remove(Graph g) throws IOException
	{
	   networkDAO.deleteNodes(g.getNodes(), getNetworkId(), getConnection());
	   networkDAO.deleteEdges(g.getEdges(), getNetworkId(), getConnection());

	}
}