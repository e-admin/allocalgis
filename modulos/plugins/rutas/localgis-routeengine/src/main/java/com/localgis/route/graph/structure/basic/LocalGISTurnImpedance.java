/**
 * LocalGISTurnImpedance.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.impedance.EdgeImpedance;
import org.uva.route.graph.structure.impedance.SimpleImpedance;
import org.uva.route.graph.structure.impedance.TurnImpedances;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.util.NodeUtils;

public class LocalGISTurnImpedance implements Serializable,TurnImpedances {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<TurnImpedance> turnImpedances;
	
	public int getSize(){
		return turnImpedances.size();
	}
	public LocalGISTurnImpedance(){
		this.turnImpedances = new ArrayList<TurnImpedance>();
	}
	public void setTurnImpedance(Integer idEdgeStart,Integer idEdgeEnd,double impedance){
		TurnImpedance turnImpedance = new TurnImpedance(idEdgeStart,idEdgeEnd,impedance);
		if(this.turnImpedances.contains(turnImpedance)){
			getTurnImpedance(idEdgeStart, idEdgeEnd).setImpedance(impedance);
		}else
			turnImpedances.add(turnImpedance);
	}
	public TurnImpedance getTurnImpedance(Integer idEdgeStart,Integer idEdgeEnd){
		if(hasTurnImpedance(idEdgeStart, idEdgeEnd))
			return this.turnImpedances.get(this.turnImpedances.indexOf(new TurnImpedance(idEdgeStart,idEdgeEnd,Double.MAX_VALUE)));
		return new TurnImpedance(idEdgeStart,idEdgeEnd,0.0D);
	}
	public boolean hasTurnImpedance(Integer idEdgeStart,Integer idEdgeEnd){
		if(this.turnImpedances.indexOf(new TurnImpedance(idEdgeStart,idEdgeEnd,Double.MAX_VALUE)) > -1){
			return true;
		}
		return false;
	}
	public ArrayList<TurnImpedance> getTurnImpedances(){
		return turnImpedances;
	}

	public boolean isTurnImpedancesEmpty(){
		return this.turnImpedances.isEmpty();
	}
	
	public TurnImpedance getTurnImpedanceAt(int position){
		return this.turnImpedances.get(position);
	}
	
	public Iterator<TurnImpedance> getTurnImpedancesIterator(){
		return this.turnImpedances.iterator();
	}
	@Override
	public EdgeImpedance getTurnImpedance(Edge from, Edge to) {
		if(!(from instanceof ILocalGISEdge))
			from = (Edge)NodeUtils.unwrapProxies(from);
		if(from instanceof EquivalentEdge)
			from = (Edge)((EquivalentEdge)from).getEquivalentTo();
		if(!(to instanceof ILocalGISEdge))
			to = (Edge)NodeUtils.unwrapProxies(to);
		if(to instanceof EquivalentEdge)
			to = (Edge)((EquivalentEdge)to).getEquivalentTo();
		return new SimpleImpedance(getTurnImpedance(from.getID(),to.getID()).getImpedance());
	}
	@Override
	public void setImpedance(Edge from, Edge to, EdgeImpedance impedance) {
		//TODO: He puesto coste 1 para que me devuelva lo que tiene. Revisar, habra que cambiarlo
		setTurnImpedance(from.getID(),to.getID(),impedance.getCost(1));
		
	}
	
}
