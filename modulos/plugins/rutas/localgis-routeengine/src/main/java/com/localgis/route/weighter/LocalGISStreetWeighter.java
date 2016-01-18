/**
 * LocalGISStreetWeighter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.weighter;

import java.util.ArrayList;
import java.util.Date;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.impedance.NodeWithAStarWeighter;
import org.uva.route.graph.structure.proxy.ProxyEdge;
import org.uva.route.graph.traverse.standard.IDELabAStarIterator.AStarNode;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.street.Street;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.util.IdEdgeNetworkBean;


public class LocalGISStreetWeighter extends LocalGISWeighter {
	
	public LocalGISStreetWeighter(Date actualDate,ArrayList<IdEdgeNetworkBean> temporal,NetworkManager networkMgr) {
		super(actualDate,temporal,networkMgr);
	}
	public LocalGISStreetWeighter(ArrayList<IdEdgeNetworkBean> temporal,NetworkManager networkMgr) {
		super(temporal,networkMgr);
	}

	public LocalGISStreetWeighter() {
		super();
	}
	public LocalGISStreetWeighter(Date date){
		super(date);
	}
	@Override
	public double getWeight(Node from, Edge to) {
		Edge edge=to;
//		if (edge instanceof EquivalentEdge){
//			edge = (Edge) ((EquivalentEdge) edge).getEquivalentTo();
//			//System.out.println(edge.getClass().getName());
//		}
		if (edge instanceof ProxyEdge){
			System.out.println(edge.getClass().getName());
		}
		if(edge instanceof NetworkLink)
			return 0;
		double cost=0;
		double walking = 50*1000/3600;// velocidad por defecto de vehiculo en sistema internacional
		if(hasTemporalIncident(edge))
			{
			    String msg = "Encontrado arco con incidencias temporales: Arco:"+edge.getID();
			    this.addReportMessage(msg);
			    return EdgeWithCost.INFINITY;
			}
		else{
			if (edge instanceof Street)
			{
				LocalGISStreetDynamicEdge street=(LocalGISStreetDynamicEdge) edge;
				// Buscar incidentes
				if(hasIncidents(edge))
					{
					    String msg = "Encontrado arco con incidencias: Arco:"+edge.getID();
					    this.addReportMessage(msg);
					    return EdgeWithCost.INFINITY;
					}
				if(hasTrafficRegulation(street,from,to))
					{
					    String msg = "Encontrado arco con regulaciones de tráfico (sentido prohibido): Arco:"+edge.getID();
					    this.addReportMessage(msg);
					    return EdgeWithCost.INFINITY;
					}
				cost = street.getCost(from, street.getNominalMaxSpeed());
			}
			else{
				if(hasIncidents(edge))
					{
					    String msg = "Encontrado arco con incidencias: Arco:"+edge.getID();
					    this.addReportMessage(msg);
					    return EdgeWithCost.INFINITY;
					}
				cost = ((EdgeWithCost) edge).getCost(from, walking);
			}
		}
		return cost;
	}
	private boolean hasTrafficRegulation(LocalGISStreetDynamicEdge street,Node from,Edge to) {
		if(street.getTrafficRegulation() == StreetTrafficRegulation.FORBIDDEN || 
				(street.getNodeA().equals(from) && street.getTrafficRegulation() == StreetTrafficRegulation.INVERSE) ||
				(street.getNodeB().equals(from) && street.getTrafficRegulation() == StreetTrafficRegulation.DIRECT)
				){
			return true;
		}
		return false;
	}
	
	protected boolean hasEspecificIncidentType(LocalGISIncident incident){
		return (incident.getIncidentType() == LocalGISIncident.PATH_CLOSED_TO_VEHICLES || incident.getIncidentType() == LocalGISIncident.PATH_DISABLED);
	}
	public double getWeightWithNode(AStarNode from, Edge edge)
	{
		double nodeCost=0;
		Node node=(Node) NodeUtils.unwrapProxies(from.getNode());
		if (node instanceof NodeWithAStarWeighter)
		{
			NodeWithAStarWeighter nodeWithWeighter=(NodeWithAStarWeighter) node;
			nodeCost=nodeWithWeighter.getAStarWeight(from, edge, getWalkerImpulse());
		}
		return nodeCost + getWeight(from.getNode(), edge);
	}
}
