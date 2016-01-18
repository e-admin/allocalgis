/**
 * LocalGISInterNetworker.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.network;

import java.util.Collection;

import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.io.NetworkLinkReaderWriter;
import org.uva.route.network.io.PersistentInterNetworker;
import org.uva.routeserver.ElementNotFoundException;

public class LocalGISInterNetworker extends PersistentInterNetworker
{
	//private RouteConnectionFactory routeConnectionFactory;
	public LocalGISInterNetworker(NetworkLinkReaderWriter readerwriter)
	{
		super(readerwriter);
	}
	public Network whereIs(Node target)
	{
		//TODO:CAMBIAR
		
		// query all DynamicGraphs for the node 
		Collection<Network> networks=((LocalGISNetworkManager)getNetworkManager()).getAllNetworks().values();
		for (Network network : networks)
		{
			try
			{
				DynamicGraph graph=(DynamicGraph) network.getGraph();
				Node found=graph.getNode(target.getID());
				if (target.getID() == found.getID())
					return network;
			} catch (ElementNotFoundException e)
			{
				// node is not found in this graph.Continue
				continue;
			} 
			catch (ClassCastException e)
			{
				// this graph is not DynamicGraph.Continue
				continue;
			} 
		}
		return null;
	}
	/*public Set<NetworkLink> queryNetworkLinks(Network parentNetwork)
	{
		HashSet<NetworkLink> nlinks=new HashSet<NetworkLink>();
		//Query Manager for name as networkA can be a value bean
		//Network nA= getNetworkManager().getNetwork(parentNetwork.getName());
		LocalGISNetworkDAO lnDAO = new LocalGISNetworkDAO();
		ArrayList<RouteResultSet> routeResultSets = null;
		Connection conn = null;
		Hashtable<Integer,String> ht = null;
		try {
			conn = routeConnectionFactory.getConnection();
			routeResultSets = lnDAO.readNetworkLinks(parentNetwork.getName(),conn );
			ht = lnDAO.getNetworkNames(conn);
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			ConnectionUtilities.closeConnection(conn);
		}
		Iterator<RouteResultSet> it = routeResultSets.iterator();
		while(it.hasNext()){
			RouteResultSet rset = it.next();
			Network networkA = this.getNetworkManager().getNetwork(ht.get(new Integer(rset.getId_subred_nodoA())));
			Node nodeA = null;
			try {
				nodeA = ((DynamicGraph)networkA.getGraph()).getNode(rset.getId_nodoA());
			} catch (ElementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Network networkB = this.getNetworkManager().getNetwork(ht.get(new Integer(rset.getId_subred_nodoB())));
			Node nodeB= null;
			try {
				nodeB = ((DynamicGraph)networkB.getGraph()).getNode(rset.getId_nodoB());
			} catch (ElementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			NetworkLink nk =  new NetworkLink(networkA, nodeA, networkB, nodeB);
			nlinks.add(nk);
		}
		
		
		//nlinks.addAll(nA.getNetworkLinks());
		
		return nlinks;
	}*/
}
