/**
 * LocalGISStreetGraphBuilder.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.build.dynamic;

import org.opengis.geometry.primitive.Point;
import org.uva.geotools.graph.build.GraphBuilder;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.geographic.GeographicNode;
import org.uva.route.graph.structure.geographic.GeographicNodeWithTurnImpedances;
import org.uva.route.graph.structure.impedance.TurnImpedances;

import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.vividsolutions.jump.feature.Feature;



/**
 * @author rubengomez
 * Clase que extiende del DynamicGraphBuilder para construir los objetos node y edge
 */
public class LocalGISStreetGraphBuilder extends LocalGISGraphBuilder{
	
    public LocalGISStreetGraphBuilder(UniqueIDGenerator uidGen) {
	super(uidGen);
	}

	/**
	 * @see GraphBuilder#buildEdge(Node, Node)
	 */
	public Edge buildEdge(Node nodeA, Node nodeB) {
		return (new LocalGISStreetDynamicEdge((GeographicNode) nodeA, (GeographicNode) nodeB,0,0,this.getUIDGenerator(),null));
	}
	
        public Edge buildEdge(Node nA, Node nB, int idNetworkNodeA,int idNetworkNodeB,int idFeature, int idLayer, double edgeLength, Feature feature)
        {
    		return (new LocalGISStreetDynamicEdge(nA, nB,idNetworkNodeA,idNetworkNodeB, idFeature, idLayer, edgeLength, this.getUIDGenerator(), feature));
        }
	public Edge buildEdge(Node nodeA, Node nodeB,int idNetworkNodeA,int idNetworkNodeB,UniqueIDGenerator idGenerator, Feature feature) {
		return (new LocalGISStreetDynamicEdge((GeographicNode) nodeA, (GeographicNode) nodeB, idNetworkNodeA, idNetworkNodeB,idGenerator,feature));
	}
	public Edge buildPMREdge(Node nodeA, Node nodeB,int idNetworkNodeA,int idNetworkNodeB,UniqueIDGenerator idGenerator, Feature feature) {
		return (new PMRLocalGISStreetDynamicEdge((GeographicNode) nodeA, (GeographicNode) nodeB, idNetworkNodeA, idNetworkNodeB,idGenerator,feature));
	}
	
	public Edge buildZebraEdge(String sType,Node nodeA, Node nodeB, int idNetworkNodeA,int idNetworkNodeB,UniqueIDGenerator idGenerator, Feature feature) {
		return (new ZebraDynamicEdge(sType,(GeographicNode) nodeA, (GeographicNode) nodeB, idNetworkNodeA, idNetworkNodeB,idGenerator,feature));
	}

	public Edge buildEdge(DynamicEdge e,int idNetworkNodeA,int idNetworkNodeB, int idFeature, int idLayer, double edgeLength, Feature feature)
	{
		return new LocalGISStreetDynamicEdge(e, idNetworkNodeA, idNetworkNodeB,idFeature,idLayer,edgeLength,this.getUIDGenerator(),feature);
	}
	
	public Edge buildPMREdge(DynamicEdge e,int idNetworkNodeA,int idNetworkNodeB, int idFeature, int idLayer, double edgeLength, Feature feature){
		return (new PMRLocalGISStreetDynamicEdge(e, idNetworkNodeA, idNetworkNodeB,idFeature,idLayer,edgeLength,this.getUIDGenerator(),feature));
	}
	public Edge buildPMREdge(Node nA, Node nB, int idNetworkNodeA,int idNetworkNodeB,int idFeature, int idLayer, double edgeLength, Feature feature)
	{
		return (new PMRLocalGISStreetDynamicEdge(nA, nB, idNetworkNodeA, idNetworkNodeB, idFeature, idLayer, edgeLength, this.getUIDGenerator(), feature));
	}
	public Node buildImpedanceNode() {
		return new GeographicNodeWithTurnImpedances(null, null, getUIDGenerator());
	}
	public Node buildImpedanceNodeWithTurnImpedances(Point point,TurnImpedances turn,UniqueIDGenerator idGenerator) {
		return new GeographicNodeWithTurnImpedances(point, turn, idGenerator);
	}

	@Override
	public Edge buildEdge(Node nodeA, Node nodeB, UniqueIDGenerator idGenerator)
	{
	    return new LocalGISStreetDynamicEdge(nodeA, nodeB, 0,0,idGenerator,null);
	}
}
