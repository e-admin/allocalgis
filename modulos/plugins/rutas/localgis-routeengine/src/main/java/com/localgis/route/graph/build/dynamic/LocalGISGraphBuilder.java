/**
 * LocalGISGraphBuilder.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.build.dynamic;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.build.dynamic.DynamicGeograficGraphBuilder;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;

import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.vividsolutions.jump.feature.Feature;



/**
 * @author rubengomez
 * Clase que extiende del DynamicGraphBuilder para construir los objetos LocalGisDynamicEdge y edge
 * A PARTIR DE SUS NODOS
 */
public class LocalGISGraphBuilder extends DynamicGeograficGraphBuilder{
	
    public LocalGISGraphBuilder(UniqueIDGenerator uidGenerator) {
	super(uidGenerator);
	}

	/**
	 * @see GraphBuilder#buildEdge(Node, Node)
	 */
	public Edge buildEdge(Node nodeA, Node nodeB,UniqueIDGenerator idGenerator) {
		
		return (new LocalGISDynamicEdge(
					(DynamicGeographicNode) nodeA, 
					(DynamicGeographicNode) nodeB,
					idGenerator
					));
	}
	public Edge buildNetworkLink(Network networkA,Node nodeA,Network networkB, Node nodeB){
		return new NetworkLink(networkA, nodeA, networkB, nodeB, this.getUIDGenerator());
	}

    public Edge buildEdge(Node nA, Node nB,int networkA,int networkB, int idFeature, int idLayer, double edgeLength, Feature feature)
    {
	return (new LocalGISDynamicEdge(nA, nB, networkA, networkB, idFeature, idLayer, edgeLength, this.getUIDGenerator(), feature));
    }

    @Override
    public Graph getGraph()
    {
	return NetworkModuleUtil.getNewInMemoryGraph(super.getGraph());
    }
}
