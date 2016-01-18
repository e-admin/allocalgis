/**
 * NetworkModuleUtilToolsDraw.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.uva.geotools.graph.structure.Graph;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.ui.plugin.routeenginetools.dibujarredplugin.DibujarRedPlugIn;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;

public class NetworkModuleUtilToolsDraw extends NetworkModuleUtilToolsCore
{
    public static FeatureCollection getFeatureCollectionForEdges(String netName, Graph graph, PlugInContext context)
    {
	Collection edges = graph.getEdges();
	// Reutiliza el dibujado de capas por delegación
	DibujarRedPlugIn supportDraw = new DibujarRedPlugIn();
	FeatureCollection edgesFeatureCol = supportDraw.getEdgeFeatures(netName, edges, null, context);
	return edgesFeatureCol;
    }

    public static Layer createGraphLayer(DynamicGraph graph, final PlugInContext context, String netName, String categoryName) throws SQLException,
	    NoSuchAuthorityCodeException, IOException, FactoryException, ElementNotFoundException
    {

	FeatureCollection edgesFeatureCol = getFeatureCollectionForEdges(netName, graph, context);

	// // creo capas con los arcos
	// for (Iterator iter_edges = edges.iterator(); iter_edges.hasNext();) {
	// Edge edge = (Edge) iter_edges.next();
	// Coordinate[] coords = NodeUtils.CoordenadasArco(edge,
	// null, 0, 0);
	//
	// if (edgesFeatureCol == null){
	// if (edge instanceof ILocalGISEdge){
	// edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
	// }else if(edge instanceof LocalGISStreetDynamicEdge){
	// edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
	// System.out.println("Carga de atributos PMR "+(edge instanceof PMRLocalGISStreetDynamicEdge));
	// if(edge instanceof PMRLocalGISStreetDynamicEdge)
	// edgesFeatureCol = EdgesFeatureCollections.getPMRLocalGISStreetDynamicEdgeFeatureCollection(edgesFeatureCol);
	// }else if (edge instanceof DynamicEdge) {
	// edgesFeatureCol = EdgesFeatureCollections.getDynamicEdgeFeatureCollection();
	// }else if (edge instanceof Edge){
	// edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection();
	// }
	// }
	//
	//
	//
	// Feature feature = new BasicFeature(edgesFeatureCol
	// .getFeatureSchema());
	// feature.setAttribute("idEje", new Integer(edge.getID()));
	// feature.setAttribute("coste", new Double(
	// ((EdgeWithCost) edge).getCost()));
	// feature.setAttribute("idNodoA", edge.getNodeA().getID());
	// feature.setAttribute("idNodoB", edge.getNodeB().getID());
	// if (edge instanceof DynamicEdge){
	// feature.setAttribute("impedanciaAB", ((DynamicEdge) edge).getImpedance(edge.getNodeA()).getCost(1));
	// feature.setAttribute("impedanciaBA", ((DynamicEdge) edge).getImpedance(edge.getNodeB()).getCost(1));
	// feature.setAttribute("costeEjeDinamico", ((DynamicEdge) edge).getCost());
	// }
	// if (edge instanceof ILocalGISEdge){
	// feature.setAttribute("longitudEje", ((ILocalGISEdge) edge).getEdgeLength());
	// feature.setAttribute("idFeature", ((ILocalGISEdge) edge).getIdFeature());
	// feature.setAttribute("idCapa", ((ILocalGISEdge) edge).getIdLayer());
	// }
	// if (edge instanceof LocalGISStreetDynamicEdge){
	// feature.setAttribute("regulacionTrafico", ((LocalGISStreetDynamicEdge) edge).getTrafficRegulation().toString());
	// feature.setAttribute("maxVelocidadNominal", RedondearVelocidad(((LocalGISStreetDynamicEdge) edge).getNominalMaxSpeed() * 3600.0 / 1000.0));
	// feature.setAttribute("pintadaRegulacionTrafico", 0);
	// if (edge instanceof PMRLocalGISStreetDynamicEdge){
	// feature.setAttribute("anchuraAcera", ((PMRLocalGISStreetDynamicEdge) edge).getWidth());
	// feature.setAttribute("pendienteTransversal", ((PMRLocalGISStreetDynamicEdge) edge).getTransversalSlope());
	// feature.setAttribute("pendienteLongitudinal", ((PMRLocalGISStreetDynamicEdge) edge).getLongitudinalSlope());
	// feature.setAttribute("tipoEje", ((PMRLocalGISStreetDynamicEdge) edge).getsEdgeType());
	// feature.setAttribute("alturaObstaculo", ((PMRLocalGISStreetDynamicEdge) edge).getObstacleHeight());
	// feature.setAttribute("ejeRelacionadoConId", ((PMRLocalGISStreetDynamicEdge) edge).getRelatedToId());
	// if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.LEFT)
	// feature.setAttribute("ladoAcera", "L");
	// else if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.RIGHT)
	// feature.setAttribute("ladoAcera", "R");
	// if (edge instanceof ZebraDynamicEdge)
	// feature.setAttribute("tipoPasoCebra", ((ZebraDynamicEdge) edge).getsType());
	// }
	// }
	//
	// LineString geom_edge = fact.createLineString(coords);
	// feature.setGeometry(geom_edge);
	// edgesFeatureCol.add(feature);
	//
	// }
	//
	// if (edgesFeatureCol == null){
	// edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection();
	// }

	Layer edgesLayer = context.addLayer(categoryName, netName + " (Repr.Grafo)", edgesFeatureCol);
	LabelStyle labelStylenew = new LabelStyle();
	labelStylenew.setAttribute("coste");
	labelStylenew.setColor(Color.red);
	labelStylenew.setScaling(false);
	// labelStyle.setEnabled(true);
	edgesLayer.addStyle(labelStylenew);
	return edgesLayer;
    }
}
