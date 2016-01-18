/**
 * ExternalInfoRouteLN.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.ln;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.NetworkEdge;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.LocalGISGeometryBuilder;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

public class ExternalInfoRouteLN 
{
	public VirtualNodeInfo getVirtualNodeInfo(RouteConnectionFactory connectionFactory,Point point,NetworkManager networkManager,String actualNetwork,double tolerance)
	{
		VirtualNodeInfo virtualNodeInfo = null;
		Network network = networkManager.getNetwork(actualNetwork);
//		if(network.getGraph() instanceof BasicGraph){
//			try {
//				return getVirtualNodeInfo(point, networkManager, actualNetwork);
//			} catch (NoSuchAuthorityCodeException e) {
//				
//				e.printStackTrace();
//			} catch (FactoryException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		else
//		if(network.getGraph() instanceof DynamicGraph){
//			DynamicGraph dGraph = (DynamicGraph) network.getGraph();
//			if(!(dGraph.getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager))
//			{
//				try {
//					return getVirtualNodeInfo(point, networkManager, actualNetwork);
//				} catch (NoSuchAuthorityCodeException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (FactoryException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}// BUG else?
//		}
		Connection connection = null;
		try {

			connection = connectionFactory.getConnection();
			LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
			NetworkEdge finalEdge = null;
			String finalNetworkName = "";
			
			ArrayList<NetworkEdge> networkEdges;

			networkEdges = new ArrayList<NetworkEdge>(networkDAO.getNetworkEdgesNearTo(connection, actualNetwork, point, tolerance, 1));
			if(networkEdges.size() == 0)
					throw new RuntimeException("Nearest Edge not found. Try increasing tolerance!");
			NetworkEdge actualEdge = networkEdges.get(0);
			if(finalEdge == null){
					finalEdge = actualEdge;
					finalNetworkName = actualNetwork;
				}else{
					if(finalEdge.getDistance()>actualEdge.getDistance()){
						finalEdge = actualEdge;
						finalNetworkName = actualNetwork;
					}
				}
			Graph graph = network.getGraph();
				
				
				
			//ArrayList<Edge> edges = new ArrayList<Edge>();
			Edge nearestEdge = getEdgeFromMemoryManagerByID(graph, finalEdge.getIdEdge());
			
			/*Iterator<Edge> itE = edges.iterator();
			Edge nearestEdge = null;
			while(itE.hasNext() && nearestEdge == null){
				Edge edge = itE.next();
				if(edge.getID() == finalEdge.getIdEdge())
					nearestEdge = edge;
			}*/
			if (nearestEdge != null){
				virtualNodeInfo = new VirtualNodeInfo(); 
				networkDAO.setSplittedLineStrings(virtualNodeInfo, nearestEdge, point,connection);
				virtualNodeInfo.setEdge(nearestEdge);
				virtualNodeInfo.setDistance(finalEdge.getDistance());
				virtualNodeInfo.setNetworkName(finalNetworkName);
			}
			if (virtualNodeInfo == null)
				System.out.println("virtualNodeInfo is NULL");
			else{
				System.out.println("virtualNodeInfo NOT is NULL");
				System.out.println("virtualNodeInfo.getEdge "+virtualNodeInfo.getEdge() == null);
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (ElementNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}finally{
			ConnectionUtilities.closeConnection(connection);
		}
		return virtualNodeInfo;
	}
	private Edge getEdgeFromMemoryManagerByID(Graph graph, Integer idEdge) {
		// TODO Auto-generated method stub
		try{
			return ((DynamicGraph) graph).getMemoryManager().getEdge(idEdge);
		}catch (ElementNotFoundException e) {
			// Salta una excepciï¿½n de element not found.
			// intentamos buscarlo en la lista de edges con un iterator o bien
			// recorriendo la lista de nodes, buscando los edges
			Network selectedNetwork = null;
			Edge selectedEdge = null;
			if (graph != null){
				Iterator<Node> nodes = graph.getNodes().iterator();
				Node actualNode = null;
				while(nodes.hasNext()){
					actualNode = nodes.next();
					if (actualNode!=null){
						Iterator<Graphable> iterNodesGrapahbles  = actualNode.getEdges().iterator();
						while(iterNodesGrapahbles.hasNext()){
							Graphable graphable = iterNodesGrapahbles.next(); 
//							System.out.println(graphable.getClass().getName());
							if (graphable.getID() == idEdge){
								if (graphable instanceof ILocalGISEdge && graphable.getID() == idEdge){
									selectedEdge = (Edge) graphable;
								}
							}
						}
					}
				}
			}
			return selectedEdge;
		}
	}
	public VirtualNodeInfo getVirtualNodeInfo(Point point,NetworkManager networkManager,String actualNetwork) throws NoSuchAuthorityCodeException, FactoryException{
		VirtualNodeInfo virtualNodeInfo = null;
			String srid=LocalGISGeometryBuilder.guessSRIDCode(point);
			Coordinate coords = new Coordinate(point.getDirectPosition().getCoordinate()[0],point.getDirectPosition().getCoordinate()[1]);
			com.vividsolutions.jts.geom.Point referencePoint = LocalGISGeometryBuilder.getJTSPointFromPrimitivePoint(point);
			NetworkEdge finalEdge = null;
			String finalNetworkName = actualNetwork;
			Network network = networkManager.getNetwork(actualNetwork);
			Graph graph = network.getGraph();
			ArrayList<Edge> edges = new ArrayList(graph.getEdges());
			Iterator<Edge> it = edges.iterator();
			double distance = Double.POSITIVE_INFINITY;
			Edge nearestEdge = null;
			LineString nearestEdgeLinestring = null;
			while(it.hasNext()){
				Edge edge = it.next();
				if (edge instanceof EquivalentEdge) // ignore Virtual elements 
				    continue;
				XYNode nodeA = (XYNode)edge.getNodeA();
				XYNode nodeB = (XYNode)edge.getNodeB();
//BUG JPC usa linestring	Coordinate[] coordinates = new Coordinate[]{nodeA.getCoordinate(),nodeB.getCoordinate()};
//				LineString edgeLinestring = LocalGISGeometryBuilder.getLineStringFromCoordinates(coordinates, Integer.parseInt(LocalGISGeometryBuilder.guessSRIDCode(point)));
				
// BUG Esta función no se puede usar en entorno de base de datos. Usar basado en red.
				LineString edgeLinestring=NetworkModuleUtil.getEdgeLineStringFromGeometry(NetworkModuleUtil.getDBGeometryForEdge(edge,graph,srid));
				double actualDistance = referencePoint.distance(edgeLinestring);
				if(actualDistance < distance)
				    {
					distance = actualDistance;
					nearestEdge = edge;
					nearestEdgeLinestring = edgeLinestring;
				}
			}
			//Aqui tenemos ya el tramo final
			//Ahora necesitamos calcular el equivalente a esto
			//networkDAO.getVirtualDistance(connection, nearestEdge, point,virtualNodeInfo);
			VirtualNodeInfo vnf = new VirtualNodeInfo();
			LocalGISGeometryBuilder.getVirtualDistance(nearestEdgeLinestring,referencePoint,vnf);
			vnf.setEdge(nearestEdge);
			vnf.setDistance(distance);
			vnf.setNetworkName(finalNetworkName);
		return vnf;
	}

	public int getNextDatabaseIdNode(RouteConnectionFactory connectionFactory, int networkId,int networkNode){
		
		return /*networkId*/1000000000/*+networkNode*/;
	}
	public int getNextDatabaseIdEdge(RouteConnectionFactory connectionFactory){
		int i = 0;
		return i;
	}
	public int getNetworkId(RouteConnectionFactory connectionFactory,String networkName) {
		LocalGISNetworkDAO lnDAO = new LocalGISNetworkDAO();
		Connection connection = connectionFactory.getConnection();
		int i = 0;
		try{
			i = lnDAO.getNetworkId(networkName, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionUtilities.closeConnection(connection);
		}
		return i;
	}
	
}
