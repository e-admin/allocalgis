/**
 * LocalGISNetworkManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.network;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.datastore.RouteResultSet;
import org.uva.route.graph.path.PathNotFoundException;
import org.uva.route.graph.path.RoutePath;
import org.uva.route.graph.structure.VirtualNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.routeserver.ElementNotFoundException;
import org.uva.routeserver.algorithms.IDELabWeighter;
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.RouteConnectionFactory;


public class LocalGISNetworkManager extends BasicNetworkManager {
	private static final Log logger = LogFactory.getLog(LocalGISNetworkManager.class);
	
	public Network getNetwork(String name){
		Set<String> keys = this.networks.keySet();
		Iterator<String> it = keys.iterator();
		Network network = null;
		while (it.hasNext() && network == null){
			String actualNetwork = it.next();
			if(actualNetwork.trim().equals(name.trim()))
				network = this.networks.get(actualNetwork);
			else{
				Map map = networks.get(actualNetwork).getSubnetworks();
				Set subKeys = map.keySet();
				Iterator subNetworks = subKeys.iterator();
				while(subNetworks.hasNext() && network == null){
					String subNetwork =(String)subNetworks.next();
					if(subNetwork.trim().equals(name.trim()))
						network = (Network)map.get(subNetwork);
				}
			}
		}
		return network;
		
	}
	// Nuevo generador de networks para agregar automï¿½ticamente las properties a la network.
	public Network putNetworkWithProperties(String name, Graph graph,RouteConnectionFactory routeConnectionFactory) throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException, ElementNotFoundException
	{
		Network network = putNetwork(name, graph);
		Map<String, NetworkProperty> props = NetworkModuleUtil.readNetworkPropertiesFromDB(name, routeConnectionFactory.getConnection());
		network.addProperties(props);
		return network;
	}
	
//	public Network putSubNetworkWithProperties(Network parentNetwork,String name, Graph graph,RouteConnectionFactory routeConnectionFactory) throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException, ElementNotFoundException
//	{
//		//Network network = putNetwork(name, graph);
//	    	Network network=putSubNetwork(parentNetwork, name, graph);
//		putProperties( network, routeConnectionFactory);
//		
//		return network;
//	}
//	public void putProperties(Network network, RouteConnectionFactory routeConnectionFactory)
//	{
//	    Graph graph=network.getGraph();
//	    String name=network.getName();
//	    
//	    if(graph instanceof DynamicGraph && // Si es un grafo dinamico 
//	    		((DynamicGraph)graph).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager && // si tiene un memory manager
//	    		((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)graph).getMemoryManager()).getStore() instanceof LocalGISRouteReaderWriter)// si tiene un reader de base de datos
//	       { 
//	    	LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
//	    	Connection connection = routeConnectionFactory.getConnection();
//	    	Map<String, NetworkProperty> networkProperties = null;
//	    	try {
//	    		networkProperties = networkDAO.readNetworkProperties(name, connection);
//	    	} catch (SQLException e) {
//	    		logger.error("Error obtaining network properties", e);
//	    	}finally{
//	    		ConnectionUtilities.closeConnection(connection);
//	    	}
//	    	Map<String, Object> properties = network.getProperties();
//	    	Set<String> keys = networkProperties.keySet();
//	    	Iterator<String> it = keys.iterator();
//	    	while(it.hasNext()){
//	    		String key = it.next();
//	    		properties.put(key, networkProperties.get(key));
//	    	}
//	    	try{
//	    		Network networkBaseDeDatos = this.getNetwork("RedBaseDatos"); // TODO: BUG porque se busca esto?
//	    		if (networkBaseDeDatos==null)
//	    		{
//	    			logger.warn("Network 'RedBaseDatos' not found! Don't know why it is searched! ???");
//	    			return;
//	    		}
//		Map<String, Network> subnetworks = networkBaseDeDatos.getSubnetworks();
//	    	for (Network nw : subnetworks.values())
//	    		    {
//	    			setNetworkLinks(nw,connection);
//	    		    }
//	    	
//	    	}catch(Exception e){
//	    		e.printStackTrace();
//	    	}
//	    	
//	    }
//	}
	/**
	 * Lee los networkLinks de la base de datos y los registra en las redes
	 * 
	 * TODO Pasar la operación al Internetworker
	 * @param network
	 * @param connection
	 * @throws SQLException
	 * @throws NoSuchAuthorityCodeException
	 * @throws IOException
	 * @throws FactoryException
	 * @throws ElementNotFoundException
	 * @deprecated
	 */
	public void setNetworkLinks(Network network,Connection connection) throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException, ElementNotFoundException {
		LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
		ArrayList<RouteResultSet> networkLinks = networkDAO.readNetworkLinks(network.getName(), connection);
		Iterator<RouteResultSet> it = networkLinks.iterator();
		while(it.hasNext()){
			RouteResultSet link = it.next();
			Network networkNodeA = getNetwork(networkDAO.getNetworkName(link.getId_subred_nodoA(), connection));
			int id_nodoA = link.getId_nodoA();
			Network networkNodeB = getNetwork(networkDAO.getNetworkName(link.getId_subred_nodoB(), connection));
			int id_nodoB = link.getId_nodoB();
			Node nodeA = null;
			Node nodeB = null;
			if( networkNodeA == null || networkNodeB == null)
				return;
			
			if(networkNodeA.getGraph() instanceof BasicGraph)
			    {
				AllInMemoryManager manager = new AllInMemoryManager();
				manager.setGraph(networkNodeA.getGraph());
				DynamicGraph graph = new DynamicGraph(manager);
				nodeA = graph.getNode(id_nodoA);
			}else{
				nodeA = ((DynamicGraph)networkNodeA.getGraph()).getNode(id_nodoA);
			}
			if(networkNodeB.getGraph() instanceof BasicGraph){
				AllInMemoryManager manager = new AllInMemoryManager();
				manager.setGraph(networkNodeB.getGraph());
				DynamicGraph graph = new DynamicGraph(manager);
				nodeB = graph.getNode(id_nodoA);
			}else{
				
				nodeB = ((DynamicGraph)networkNodeB.getGraph()).getNode(id_nodoB);
			}
			
			NetworkLink nl = new NetworkLink(networkNodeA, nodeA, networkNodeB, nodeB,new SequenceUIDGenerator());
			if(!networkLinkExists(networkNodeA,nl))
				networkNodeA.addNetworkLink(nl);
			if(!networkLinkExists(networkNodeB,nl))
				networkNodeB.addNetworkLink(nl);
		}
		
		
	}
	
	private boolean networkLinkExists(Network networkNode, NetworkLink nl) {
		Iterator<NetworkLink> networkLinks = networkNode.getNetworkLinks().iterator();
		while(networkLinks.hasNext()){
			NetworkLink bdNetworkLink = networkLinks.next();
			if(
					bdNetworkLink.getNetworkA().getName().trim().equals(nl.getNetworkA().getName().trim()) && 
					bdNetworkLink.getNetworkB().getName().trim().equals(nl.getNetworkB().getName().trim()) &&
					bdNetworkLink.getNodeA().getID() == nl.getNodeA().getID() && 
					bdNetworkLink.getNodeB().getID() == nl.getNodeB().getID()
			)
				return true;
		}
		return false;
	}
	/*
	 * Metodo para agregar un network link a un network
	 */
	public boolean addNetworkLink(Network network,Connection connection,NetworkLink networkLink) throws Exception {
		LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
		Set<NetworkLink> keys = networkLink.getNetworkA().getNetworkLinks();
		Iterator<NetworkLink> it = keys.iterator();
		boolean isInserted = true;
		while (it.hasNext()){
			NetworkLink nl = it.next();
			if(nl.getNetworkA().getName().equals(networkLink.getNetworkA().getName()) &&
					nl.getNetworkB().getName().equals(networkLink.getNetworkB().getName()) &&
					nl.getNodeA().getID() == networkLink.getNodeA().getID() && 
					nl.getNodeB().getID() == networkLink.getNodeB().getID()){
				isInserted = false;
			}
		}
		if(isInserted){
			networkLink.getNetworkA().addNetworkLink(networkLink);
			networkLink.getNetworkB().addNetworkLink(networkLink);
			//Solo escribe los cambios si estan en base de datos
//			if(connection != null && networkLink.getNetworkA().getGraph() instanceof DynamicGraph && networkLink.getNetworkB().getGraph() instanceof DynamicGraph 
//					&& ((DynamicGraph)networkLink.getNetworkA().getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager &&
//					((DynamicGraph)networkLink.getNetworkA().getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager){
					    
		// TODO ¿por qué se graban dos arcos? Un networklink es un solo elemento.
//				networkDAO.writeEdge(networkLink, networkLink.getNetworkA().getName(),-1, connection);
//				networkDAO.writeEdge(networkLink, networkLink.getNetworkB().getName(),-1, connection);
		networkDAO.writeEdge(networkLink, LocalGISRouteReaderWriter.NETWORK_LINKS_NETNAME,-1, connection);
    
//			}
		}
		return isInserted;
	}
	
	public void findClosestGeometriesFromPoint(Point source,double tolerance,int num){
		Set<String> keys = getNetworks().keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()){
			String actualNetworkName = it.next();
			DynamicGraph graph = (DynamicGraph)getNetwork(actualNetworkName).getGraph();
			graph.getEdgesNearTo(source, tolerance, num);
		}
	}
	public Network detachNetwork(String networkName)
	{
		Network netToDetach = super.detachNetwork(networkName);
		if(getNetwork(networkName) != null){
			//estï¿½ en una subNetwork
			Set<String> keys = this.networks.keySet();
			Iterator<String> it = keys.iterator();
			Network network = null;
//			while (it.hasNext() && getNetwork(networkName) != null){
			while (it.hasNext() ){// BUG Borra solo la primera que encuentre de acuerdo con la semantica del valor de return
				String actualNetwork = it.next();
				Network level1Net = networks.get(actualNetwork);
				if(level1Net.getSubnetworks() != null && level1Net.getSubnetworks().size() >0)
				    {
					Network network2 = level1Net.getSubNetwork(networkName);
					if(network2 != null)
						return level1Net.getSubnetworks().remove(networkName);// JPC return la primera ocurrencia
				}
			}			
		}
		return netToDetach;
	}
	public Map<String,Network> getAllNetworks() {
		// TODO Auto-generated method stub
		HashMap<String, Network> resultMap = new HashMap<String, Network>();
		Iterator<Network> netsiIterator = this.networks.values().iterator();
		while(netsiIterator.hasNext()){
			Network actualNetwork = netsiIterator.next();
			if (actualNetwork!=null){
				if (actualNetwork.getSubnetworks().isEmpty()){
					resultMap.put(actualNetwork.getName(), actualNetwork);				
				} else{
					resultMap.putAll(getSubNetworksRecursive(actualNetwork));
				}
			} 
		}
		
		return resultMap;
	}
	private Map<String,Network> getSubNetworksRecursive(
			Network network) {
		// TODO Auto-generated method stub
		HashMap<String, Network> result = new HashMap<String, Network>();
		if (network != null){
			if (network.getSubnetworks().isEmpty()){
				result.put(network.getName(), network);
			} else{
				Iterator<Network> it = network.getSubnetworks().values().iterator();
				while(it.hasNext()){
					result.putAll(getSubNetworksRecursive(it.next()));
				}
			}
		}
		return result;
	}
	public RoutePath findEvaluatedShortestPath(Node start, Node target, IDELabWeighter weighter) throws PathNotFoundException, ElementNotFoundException
{
		
	Set<VirtualNode> vNodes = new HashSet<VirtualNode>();
	if (start instanceof VirtualNode)
		vNodes.add((VirtualNode) start);
	if (target instanceof VirtualNode)
		vNodes.add((VirtualNode) target);
		
	RoutePath path = findShortestPath(start, target, weighter,null,vNodes);
	if(path.getTotalCost() == Double.POSITIVE_INFINITY)
		throw new PathNotFoundException("Infinite Path", new Exception());
	return path;
}
}
