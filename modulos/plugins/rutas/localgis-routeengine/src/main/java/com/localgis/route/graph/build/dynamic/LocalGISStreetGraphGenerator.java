/**
 * LocalGISStreetGraphGenerator.java
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

import com.localgis.route.datastore.LocalGISStreetResultSet;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.vividsolutions.jts.geom.LineString;

/**
 * @author rubengomez
 * Clase que extiende del DynamicGraphGenerator para cargar los objetos node y edge en el grafo
 */
public class LocalGISStreetGraphGenerator extends GeographicGraphGenerator
{

    public LocalGISStreetGraphGenerator(GraphBuilder graphBuilder) {

	super();
	setGraphBuilder(graphBuilder);
	}
	public Graphable add(Object obj)
	{

		return addRouteResultSet((LocalGISStreetResultSet) obj);
	}

	protected Graphable addRouteResultSet(RouteResultSet resultset)
	{
		LocalGISStreetResultSet rs= (LocalGISStreetResultSet)resultset;

		int idnodoA=rs.getId_nodoA();
		int idnodoB=rs.getId_nodoB();
		int idnetnodoA=rs.getId_subred_nodoA();
		int idnetnodoB=rs.getId_subred_nodoB();
		Point pnodeA=rs.getPointNodoA();
		Point pnodeB=rs.getPointNodoB();

		//this Generator only build XYNodes as resultSet place nodes somewhere in the space
		Node n1=null, n2=null;

		// check first ID and
		// look up first node and create if necessary
//TODO BUG! No soporta nodos de redes distintas. ES un caso excepcional pero posible con el modelo actual.
		GraphBuilder graphBuilder = getGraphBuilder();
		if ((n1=(Node) idnodo2node.get(idnodoA)) == null)
		{
			/*if(rs.getTurnImpedanceNodeA()!= null){
				n1=(Node) ((LocalGISStreetGraphBuilder)getGraphBuilder()).buildImpedanceNodeWithTurnImpedances(pnodeA,rs.getTurnImpedanceNodeA(),new FixedValueUIDGenerator(idnodoA));
				System.out.println("Test. Generado un nodo con impedancias!!!!!!!!!");
			}else{*/
				n1=(Node) graphBuilder.buildNode();
				n1.setID(idnodoA);
				((GeographicNode) n1).setPosition(pnodeA);
			//}
			if(rs.getTurnImpedanceNodeA()!= null)System.out.println("Id impedancia = " + n1.getID());
			graphBuilder.addNode(n1);
			idnodo2node.put(idnodoA, n1);
		}

		// check second ID and
		// look up first node and create if necessary

		if ((n2=(Node) idnodo2node.get(idnodoB)) == null)
		{

			/*if(rs.getTurnImpedanceNodeB()!= null){
				n2=(Node) ((LocalGISStreetGraphBuilder)getGraphBuilder()).buildImpedanceNodeWithTurnImpedances(pnodeB,rs.getTurnImpedanceNodeB(),new FixedValueUIDGenerator(idnodoB));
				System.out.println("Test. Generado un nodo con impedancias!!!!!!!!!");
			}else{*/
				n2=(Node) graphBuilder.buildNode();
				n2.setID(idnodoB);
				((GeographicNode) n2).setPosition(pnodeB);
			//}
			if(rs.getTurnImpedanceNodeB()!= null)System.out.println("Id impedancia = " + n2.getID());
			graphBuilder.addNode(n2);
			idnodo2node.put(idnodoB, n2);
		}

		// build the edge if edge already
		// wasn't created

		Edge e= null;
		LocalGISStreetGraphBuilder streetGraphBuilder = (LocalGISStreetGraphBuilder)graphBuilder;
		if (rs.isbPMRGraph()){
			if (rs.getsEdgeType().equals("EDGE"))
				e = streetGraphBuilder.buildPMREdge(n1, n2,idnetnodoA,idnetnodoB,new FixedValueUIDGenerator(rs.getId_edge()),null);
			else
				e = streetGraphBuilder.buildZebraEdge(rs.getsType(),n1, n2,idnetnodoA,idnetnodoB,new FixedValueUIDGenerator(rs.getId_edge()),null);
		}else
			e =streetGraphBuilder.buildEdge(n1, n2,idnetnodoA,idnetnodoB,new FixedValueUIDGenerator(rs.getId_edge()),null);

		//e.setID(rs.getId_edge());
		// Como diferencia de la clase padre, se carga el id de feature y el id de layer. Aqui se pueden cargar otros costes

		if (e instanceof ILocalGISEdge)
		    {
			ILocalGISEdge streetDynamicEdge = (ILocalGISEdge) e;
			streetDynamicEdge.setIdFeature(rs.getIdFeature());
			streetDynamicEdge.setIdLayer(rs.getIdLayer());
			streetDynamicEdge.setImpedance(n1,new SimpleImpedance(rs.getImpedanceAToB()));
			streetDynamicEdge.setImpedance(n2,new SimpleImpedance(rs.getImpedanceBToA()));
			streetDynamicEdge.setEdgeLength(rs.getLength());
			streetDynamicEdge.setGeometry((LineString) rs.getGeom());
		    }
	
		if (e instanceof LocalGISStreetDynamicEdge)
			{
			    LocalGISStreetDynamicEdge streetDynamicEdge = (LocalGISStreetDynamicEdge) e;
			    streetDynamicEdge.setNominalMaxSpeed(rs.getNominalMaxSpeed());
			    streetDynamicEdge.setTrafficRegulation(rs.getStreetTrafficRegulation());
			    if(rs.getIncidents() != null){
				Iterator<Incident> it = rs.getIncidentIterator();
				while (it.hasNext()){
					streetDynamicEdge.putIncident(it.next());
				}
			    }
			}
		
		
		if (e instanceof PMRLocalGISStreetDynamicEdge)
        	    {
        		PMRLocalGISStreetDynamicEdge pmrStreetDynamicEdge = (PMRLocalGISStreetDynamicEdge) e;
        		pmrStreetDynamicEdge.setWidth(rs.getWidth());
        		pmrStreetDynamicEdge.setTransversalSlope(rs.getTransversalSlope());
        		pmrStreetDynamicEdge.setLongitudinalSlope(rs.getLongitudinalSlope());
        		pmrStreetDynamicEdge.setEdgeType(rs.getsEdgeType());
        		if (pmrStreetDynamicEdge.getsEdgeType().equals("EDGE"))
        		    {
        			pmrStreetDynamicEdge.setEdgeType("EDGE");
        		    } else
        		    {
        			((ZebraDynamicEdge) e).setEdgeType("ZEBRA");
        			if (rs.getsType().equals("SIN REBAJE"))
        			    ((ZebraDynamicEdge) e).setType("SIN REBAJE");
        			else
        			    ((ZebraDynamicEdge) e).setType("CON REBAJE");
        		    }
        		pmrStreetDynamicEdge.setCalculatedSide(rs.getCalculatedSide());
        		pmrStreetDynamicEdge.setRelatedToId(rs.getRelatedToId());
        		pmrStreetDynamicEdge.setGeometry(rs.getGeom());
        	    }
		

		graphBuilder.addEdge(e);
		return (e);
	}
	public GraphGenerator createGraphGenerator(){
		return this;
	}
}
