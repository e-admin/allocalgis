/**
 * LocalGISGraphGenerator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.build.dynamic;

import java.util.Iterator;

import org.opengis.geometry.primitive.Point;
import org.uva.geotools.graph.build.GraphBuilder;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.FixedValueUIDGenerator;
import org.uva.route.datastore.RouteResultSet;
import org.uva.route.graph.build.dynamic.GeographicGraphGenerator;
import org.uva.route.graph.structure.geographic.GeographicNode;
import org.uva.route.graph.structure.impedance.SimpleImpedance;
import org.uva.routeserver.street.Incident;

import com.localgis.route.datastore.LocalGISResultSet;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;


/**
 * @author rubengomez
 * Clase que extiende del DynamicGraphGenerator para cargar los objetos node y edge en el grafo
 * para procesar unicamente {@link LocalGISResultSet}
 */
public class LocalGISGraphGenerator extends GeographicGraphGenerator{

    public LocalGISGraphGenerator(GraphBuilder builder) {
		
		super();
	setGraphBuilder(builder);
	}
	public Graphable add(Object obj)
	{
		return addRouteResultSet((LocalGISResultSet) obj);
	}

	protected Graphable addRouteResultSet(RouteResultSet resultset)
	{
		LocalGISResultSet rs= (LocalGISResultSet)resultset;

		int idnodoA=rs.getId_nodoA();
		int idnodoB=rs.getId_nodoB();
		Point pnodeA=rs.getPointNodoA();
		Point pnodeB=rs.getPointNodoB();

		//this Generator only build XYNodes as resultSet place nodes somewhere in the space
		Node n1=null, n2=null;
		
		// check first ID and
		// look up first node and create if necessary

		if ((n1=(Node) idnodo2node.get(idnodoA)) == null)
		{
			n1=(Node) getGraphBuilder().buildNode();
			n1.setID(idnodoA);
			((GeographicNode) n1).setPosition(pnodeA);

			getGraphBuilder().addNode(n1);
			idnodo2node.put(n1.getID(), n1);
		}

		// check second ID and
		// look up first node and create if necessary

		if ((n2=(Node) idnodo2node.get(idnodoB)) == null)
		{

			n2=(Node) getGraphBuilder().buildNode();
			n2.setID(idnodoB);
			
			((GeographicNode) n2).setPosition(pnodeB);

			getGraphBuilder().addNode(n2);
			idnodo2node.put(n2.getID(), n2);
		}

		// build the edge if edge already
		// wasn't created

		Edge e=((LocalGISGraphBuilder)getGraphBuilder()).buildEdge(n1, n2,new FixedValueUIDGenerator(rs.getId_edge()));

		e.setID(rs.getId_edge());
		// Como diferencia de la clase padre, se carga el id de feature y el id de layer. Aqui se pueden cargar otros costes
	// TODO: Obtener la geometría y/o la feature del arco
		LocalGISDynamicEdge lgEdge = (LocalGISDynamicEdge) e;
		lgEdge.setIdFeature(rs.getIdFeature());
		lgEdge.setIdLayer(rs.getIdLayer());
		lgEdge.setImpedance(n1,new SimpleImpedance(rs.getImpedanceAToB()));
		lgEdge.setImpedance(n2,new SimpleImpedance(rs.getImpedanceBToA()));
		lgEdge.setEdgeLength(rs.getLength());

		if(rs.getIncidents() != null){
			Iterator<Incident> it = rs.getIncidentIterator();
			while (it.hasNext()){
				lgEdge.putIncident(it.next());
			}
		}
		getGraphBuilder().addEdge(e);
		return (e);
	}
	
	
	public GraphGenerator createGraphGenerator(){
		return this;
	}
}
