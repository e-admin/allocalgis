/**
 * PMRDynamicNode.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

import java.util.ArrayList;
import java.util.List;

import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.structure.geographic.GeographicNode;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicNode;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.geotools.graph.structure.Edge;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.Point;
import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Clase que extiende un DynamicNode, para crear el concepto de nodo de acera
 */
@SuppressWarnings("serial")
public class PMRDynamicNode extends DynamicGeographicNode
implements GeographicNode, XYNode{

	public PMRDynamicNode(UniqueIDGenerator uidGenerator) {
		super(uidGenerator.getNodeUniqueID());
		// TODO Auto-generated constructor stub
	}
	
	public boolean isIntersectionNode(){
		if (super.getEdges().size() > 2)
			return true;
		return false;
	}
	
	public List getEdgesList(){
		List edgesList = this.getEdges();
		List<Edge> orderEdgesList = new ArrayList<Edge>();
		if (edgesList.size() <= 2){
			return edgesList;
		}else{
			int n = edgesList.size();
			Coordinate crossCoordinate = this.getCoordinate();
			Edge edge = (Edge)edgesList.get(0);
			orderEdgesList.add(edge);			
			for (int j=1;j<n;j++){
				Coordinate coordinateA = ((DynamicGeographicNode)edge.getNodeA()).getCoordinate();
				if (coordinateA.equals(this.getCoordinate()))
					coordinateA = ((DynamicGeographicNode)edge.getNodeB()).getCoordinate();
				Edge edgeFarRight = null;
				double minDegrees = 360;
				for (int i=1;i<n;i++){
					if (orderEdgesList.contains(edgesList.get(i))) continue;
					Coordinate coordinateB = ((DynamicGeographicNode)((Edge)edgesList.get(i)).getNodeA()).getCoordinate();
					if (coordinateB.equals(this.getCoordinate()))
						coordinateB = ((DynamicGeographicNode)((Edge)edgesList.get(i)).getNodeB()).getCoordinate();
					double degrees = Angle.angleBetween(coordinateA, this.getCoordinate(), coordinateB);
					if (degrees< minDegrees){
						minDegrees = degrees;
						edgeFarRight = (Edge)edgesList.get(i);
					}
				}
				orderEdgesList.add(edgeFarRight);
			}
		}
		return orderEdgesList;
	}

    public Coordinate getCoordinate()
    {
        double coords[] = getPosition().getDirectPosition().getCoordinate();
        return new Coordinate(coords[0], coords[1]);
    }

    public void setCoordinate(Coordinate c)
    {
        org.opengis.referencing.crs.CoordinateReferenceSystem crs = getPosition().getCoordinateReferenceSystem();
        setPosition(GeographicNodeUtil.createPoint(c, crs));
    }

}
