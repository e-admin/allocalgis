/**
 * LocalGISWeighter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.weighter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.traverse.standard.IDELabAStarIterator.AStarNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.ElementNotFoundException;
import org.uva.routeserver.algorithms.IDELabWeighter;
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.street.Incident;

import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.util.IdEdgeNetworkBean;

public class LocalGISWeighter extends IDELabWeighter {
    	public static final Log LOGGER=LogFactory.getLog(LocalGISWeighter.class);
    	
	protected ArrayList<IdEdgeNetworkBean> forbiddenEdges;
	protected NetworkManager manager;
	protected Date actualDate;
	private ArrayList<String> reports = new ArrayList<String>();
	public LocalGISWeighter(){
		actualDate = new Date(System.currentTimeMillis());
	}
	protected Date getActualDate(){
		if(actualDate == null)
			return new Date(System.currentTimeMillis());
		return actualDate;
	}
	public LocalGISWeighter(Date actualDate){
		this.actualDate = actualDate;
	}
	public LocalGISWeighter(ArrayList<IdEdgeNetworkBean> forbiddenEdges,NetworkManager manager){
		this.forbiddenEdges = forbiddenEdges;
		this.manager = manager;
	}
	public LocalGISWeighter(Date actualDate,ArrayList<IdEdgeNetworkBean> forbiddenEdges,NetworkManager manager){
		this.forbiddenEdges = forbiddenEdges;
		this.manager = manager;
		this.actualDate = actualDate;
	}
	@Override
	public double getWeight(Node from, Edge to) {
		Edge edge=to;
		if(edge instanceof NetworkLink)
			return 0;
		double walking = 6*1000/3600;// velocidad por defecto de persona
		if(hasTemporalIncident(edge))
			return EdgeWithCost.INFINITY;
		if(hasIncidents(edge))
			return EdgeWithCost.INFINITY;
		return ((EdgeWithCost) edge).getCost(from, walking);
	}
	protected boolean hasIncidents(Edge edge) {
		if(!(edge instanceof ILocalGISEdge))
			return false;
		ILocalGISEdge edgeCost = (ILocalGISEdge)edge;
		boolean isActive = false;
		if(edgeCost.getIncidents().size()>0) {
			Iterator<Incident> it = edgeCost.getIncidents().iterator();
			while (it.hasNext() && !isActive){
				LocalGISIncident incident = (LocalGISIncident)it.next();
				// Al ser de tipo vehiculo, se evalua si esta deshabilitado o cerrado a vehiculos, sino seria solo deshabilitado.
				if(hasEspecificIncidentType(incident)){
					isActive = incident.isActive(getActualDate());
				}
			}
		}
		return isActive;
	}
	protected boolean hasEspecificIncidentType(LocalGISIncident incident){
		return (incident.getIncidentType() == LocalGISIncident.PATH_DISABLED);
	}

	protected boolean hasTemporalIncident(Edge edge){
		ArrayList<Edge> edges = new ArrayList<Edge>();
		if(this.manager != null){
			Iterator<IdEdgeNetworkBean> it = forbiddenEdges.iterator();
			while(it.hasNext()){
				IdEdgeNetworkBean nBean = it.next();
				Network network = manager.getNetwork(nBean.getNetwork());
				Edge forbiddenEdge = null;
				if(network != null){
					try {
						if(network.getGraph() instanceof DynamicGraph){
							 if(((DynamicGraph)network.getGraph()).getMemoryManager() instanceof AllInMemoryManager)
								 forbiddenEdge = ((AllInMemoryManager)((DynamicGraph)network.getGraph()).getMemoryManager()).getEdge(nBean.getIdEdge());
						}else{
							AllInMemoryManager manager = new AllInMemoryManager();
							manager.setGraph(network.getGraph());
							
								forbiddenEdge = manager.getEdge(nBean.getIdEdge());
							}
					} catch (ElementNotFoundException e) {
	
					}
				edges.add(forbiddenEdge);
				}
			}
		}
		if(this.manager != null && edges.contains(edge)){
			return true;
		}
		return false;
	}
	public double getWeightWithNode(AStarNode from, Edge edge)
	{
		return getWeight(from.getNode(), edge);
	}
	protected void addReportMessage(String msg)
	{
	   this.reports.add(msg);
	   
	}
	public ArrayList<String> getReports()
	{
	    return reports;
	}
}
