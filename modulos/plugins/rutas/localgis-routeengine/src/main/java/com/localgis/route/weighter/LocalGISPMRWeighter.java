/**
 * LocalGISPMRWeighter.java
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

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.traverse.standard.IDELabAStarIterator.AStarNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.ElementNotFoundException;
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.street.Incident;

import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.localgis.util.IdEdgeNetworkBean;

public class LocalGISPMRWeighter extends LocalGISWeighter {

	protected ArrayList<IdEdgeNetworkBean> forbiddenEdges;
	protected NetworkManager manager;
	protected Date actualDate;
	protected double minPavementWidth;
	protected double maxTransversalSlope;
	protected double maxLongitudinalSlope;
	private boolean hasDisability;
	
	public LocalGISPMRWeighter(double pavementWidth,double transversalSlope,double longitudinalSlope, boolean hasDisability)
	{
	    actualDate = new Date(System.currentTimeMillis());
		this.minPavementWidth=pavementWidth;
		this.maxTransversalSlope=transversalSlope;
		this.maxLongitudinalSlope=longitudinalSlope;
		this.hasDisability=hasDisability;
	}
	
	protected Date getActualDate(){
		if(actualDate == null)
			return new Date(System.currentTimeMillis());
		return actualDate;
	}
	public LocalGISPMRWeighter(Date actualDate){
		this.actualDate = actualDate;
	}
	public LocalGISPMRWeighter(ArrayList<IdEdgeNetworkBean> forbiddenEdges,NetworkManager manager){
		this.forbiddenEdges = forbiddenEdges;
		this.manager = manager;
	}
	public LocalGISPMRWeighter(Date actualDate,ArrayList<IdEdgeNetworkBean> forbiddenEdges,NetworkManager manager){
		this.forbiddenEdges = forbiddenEdges;
		this.manager = manager;
		this.actualDate = actualDate;
	}
	public LocalGISPMRWeighter(CalcRutaConfigFileReaderWriter configProperties)
	{
	   this(Double.parseDouble(configProperties.getPavementWidth()), Double.parseDouble(configProperties.getTransversalSlope()), Double.parseDouble(configProperties.getLongitudinalSlope()), configProperties.getDisabilityType()!=null);	   
	}
	@Override
	public double getWeight(Node from, Edge to)
	{
	    double walking = 1.66;// velocidad por defecto de persona (6*1000/3600)
	    
	Edge dynamicEdge = (Edge) NodeUtils.unwrapProxies(to);
//	if (dynamicEdge instanceof EquivalentEdge)
//	    {
//		dynamicEdge =(Edge)((EquivalentEdge)dynamicEdge).getEquivalentTo();
//		if (dynamicEdge instanceof EquivalentEdge)
//				dynamicEdge =(Edge)((EquivalentEdge)dynamicEdge).getEquivalentTo();
//	    }
	if (dynamicEdge instanceof PMRLocalGISStreetDynamicEdge)
		    {
			PMRLocalGISStreetDynamicEdge pmrEdge =(PMRLocalGISStreetDynamicEdge)dynamicEdge;
			if (pmrEdge.getsEdgeType().equals("ZEBRA"))
			    {
			//	System.out.println(pmrEdge.getsEdgeType()+" "+((ZebraDynamicEdge)pmrEdge).getsType());
				if (!this.hasDisability || ((ZebraDynamicEdge)pmrEdge).getsType().equals("CON REBAJE"))
					{
					    return pmrEdge.getCost(from, walking);
					}
				else
					{
					    String msg = "Encontrado paso de cebra SIN REBAJE: Arco:"+pmrEdge.getID();
					    this.addReportMessage(msg);
					    return EdgeWithCost.INFINITY;
					}
			}
			else
			if (pmrEdge.getsEdgeType().equals("EDGE"))
			    {
				if (	maxLongitudinalSlope<pmrEdge.getLongitudinalSlope() ||
					maxTransversalSlope<pmrEdge.getTransversalSlope()   ||
					minPavementWidth>pmrEdge.getWidth() ||
					(pmrEdge.getObstacleHeight()<1.80 && pmrEdge.getObstacleHeight()>0)
				    )
				{
				    String msg = "Encontrado tramo que no cumple restricciones: Arco:"+pmrEdge.getID();
				    this.addReportMessage(msg);
				    return EdgeWithCost.INFINITY;
				}
			}
		 }
	else
	   if(dynamicEdge instanceof NetworkLink)
		{
		    return 0;
		}
			
			if(hasTemporalIncident(dynamicEdge))
				{
				    String msg = "Encontrado arco con incidencias temporales: Arco:"+dynamicEdge.getID();
				    this.addReportMessage(msg);
				    return EdgeWithCost.INFINITY;
				}
			if(hasIncidents(dynamicEdge))
				{
				    String msg = "Encontrado arco con incidencias: Arco:"+dynamicEdge.getID();
				    this.addReportMessage(msg);
				    return EdgeWithCost.INFINITY;
				}
			if (dynamicEdge instanceof EdgeWithCost)
				{
				    double cost = ((EdgeWithCost) dynamicEdge).getCost(from, walking);
				    return cost;
				}
			else
				{
				    return 0;
				}
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
	public double getMinPavementWidth()
	{
	    return minPavementWidth;
	}
	
	public double getMaxTransversalSlope()
	{
	    return maxTransversalSlope;
	}
	
	public double getMaxLongitudinalSlope()
	{
	    return maxLongitudinalSlope;
	}
	
}