/**
 * ZebraDynamicEdge.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;

import com.vividsolutions.jump.feature.Feature;


/**
 * Clase que extiende un PMRLocalGISStreetDynamicEdge, para crear el concepto de pasos de cebra
 */
public class ZebraDynamicEdge extends PMRLocalGISStreetDynamicEdge
{
	private String sType;
	
	public String getsType() {
		return sType;
	}

	public void setType(String sType) {
		this.sType = sType;
	}

	public ZebraDynamicEdge(String sType,Node nodeA, Node nodeB, int idNetworkNodeA,int idNetworkNodeB,UniqueIDGenerator uidGenerator, Feature feature)
	{
		super(nodeA, nodeB,  idNetworkNodeA, idNetworkNodeB,uidGenerator,feature);
		this.setType(sType);
    	this.setTransversalSlope(0);
    	this.setLongitudinalSlope(0);
    	this.setEdgeType("ZEBRA");
	}

    public String toString()
    {
        return (new StringBuilder(String.valueOf(super.toString()))).toString();
    }
}
