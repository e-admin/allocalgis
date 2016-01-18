/**
 * LocalGISWICWeighter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.route.weighter;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.routeserver.algorithms.IDELabWeighter;


import com.localgis.route.graph.structure.basic.ILocalGISEdge;

/**
 * @author javieraragon
 *
 */
public class LocalGISWICWeighter extends IDELabWeighter{

	@Override
	public double getWeight(Node from, Edge edge) {	
		if (edge instanceof ILocalGISEdge){
			return ((ILocalGISEdge) edge).getEdgeLength();
		} else if (edge instanceof EdgeWithCost){
			return ((EdgeWithCost)edge).getCost(from, 1);
		}
		
		return 0;
	}

}
