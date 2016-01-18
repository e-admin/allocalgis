/**
 * EdgesFeatureCollections.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.dynamic.DynamicEdge;

import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;

import es.uva.idelab.route.algorithm.SidewalkEdge;
/**
 * Traspaso de atributos a un {@link FeatureSchema} para hacer equivalencia con los grafos
 * Debe mantenerse en concordancia con {@link ILocalGISEdge#setAttribute(String, Object)}
 * @see ILocalGISEdge#setAttribute(String, Object)
 * @author juacas
 *
 */
public class EdgesFeatureCollections
{

    public static FeatureCollection getEdgeFeatureCollection(FeatureCollection edgesFeatureCol)
    {

	if (edgesFeatureCol == null)
	    edgesFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();

	FeatureSchema featureSchema = edgesFeatureCol.getFeatureSchema();
	addAttributeIfNotPresent(featureSchema, "idEje", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "coste", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "idNodoA", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "idNodoB", AttributeType.INTEGER);

	return edgesFeatureCol;
    }

    public static FeatureCollection getDynamicEdgeFeatureCollection(FeatureCollection dynamicEdgesFeatureCol)
    {

	if (dynamicEdgesFeatureCol == null)
	    dynamicEdgesFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();

	FeatureSchema featureSchema = dynamicEdgesFeatureCol.getFeatureSchema();
	addAttributeIfNotPresent(featureSchema, "idEje", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "coste", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "idNodoA", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "idNodoB", AttributeType.INTEGER);

	// if (edge instanceof DynamicEdge){
	addAttributeIfNotPresent(featureSchema, "impedanciaAB", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "impedanciaBA", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "costeEjeDinamico", AttributeType.DOUBLE);

	return dynamicEdgesFeatureCol;
    }

    // FIX JPC modificado estos m√©todos para que pueda alterar un esqeuma ya existente para complementar los atributos
    public static FeatureCollection getLocalGISDynamicEdgeFeatureCollection(FeatureCollection ldDynamicEdgesFeatureCol)
    {
	if (ldDynamicEdgesFeatureCol == null)
	    {
		ldDynamicEdgesFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();
	    }

	FeatureSchema featureSchema = ldDynamicEdgesFeatureCol.getFeatureSchema();
	addAttributeIfNotPresent(featureSchema, "idEje", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "coste", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "idNodoA", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "idNodoB", AttributeType.INTEGER);

	// if (edge instanceof DynamicEdge){
	addAttributeIfNotPresent(featureSchema, "impedanciaAB", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "impedanciaBA", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "costeEjeDinamico", AttributeType.DOUBLE);

	// if (edge instanceof ILocalGISEdge){
	addAttributeIfNotPresent(featureSchema, "longitudEje", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "idFeature", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "idCapa", AttributeType.INTEGER);

	return ldDynamicEdgesFeatureCol;
    }

    public static void addAttributeIfNotPresent(FeatureSchema featureSchema, String name, AttributeType type)
    {
	if (!featureSchema.hasAttribute(name))
	    featureSchema.addAttribute(name, type);
    }

    public static FeatureCollection getLocalGISStreetDynamicEdgeFeatureCollection(FeatureCollection streetEdgesFeatureCol)
    {

	if (streetEdgesFeatureCol == null)
	    streetEdgesFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();

	FeatureSchema featureSchema = streetEdgesFeatureCol.getFeatureSchema();
	addAttributeIfNotPresent(featureSchema, "idEje", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "coste", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "idNodoA", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "idNodoB", AttributeType.INTEGER);

	// if (edge instanceof DynamicEdge){
	addAttributeIfNotPresent(featureSchema, "impedanciaAB", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "impedanciaBA", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "costeEjeDinamico", AttributeType.DOUBLE);

	// if (edge instanceof ILocalGISEdge){
	addAttributeIfNotPresent(featureSchema, "longitudEje", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "idFeature", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "idCapa", AttributeType.INTEGER);

	// if (edge instanceof LocalGISStreetDynamicEdge){
	addAttributeIfNotPresent(featureSchema, "regulacionTrafico", AttributeType.STRING);
	addAttributeIfNotPresent(featureSchema, "maxVelocidadNominal", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "pintadaRegulacionTrafico", AttributeType.INTEGER);

	return streetEdgesFeatureCol;
    }

    public static FeatureCollection getPMRLocalGISStreetDynamicEdgeFeatureCollection(FeatureCollection featureCollection)
    {

	FeatureSchema featureSchema = featureCollection.getFeatureSchema();
	addAttributeIfNotPresent(featureSchema, "anchuraAcera", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "pendienteTransversal", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "pendienteLongitudinal", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "ejeRelacionadoConId", AttributeType.INTEGER);
	addAttributeIfNotPresent(featureSchema, "tipoEje", AttributeType.STRING);
	addAttributeIfNotPresent(featureSchema, "alturaObstaculo", AttributeType.DOUBLE);
	addAttributeIfNotPresent(featureSchema, "tipoPasoCebra", AttributeType.STRING);
	addAttributeIfNotPresent(featureSchema, "ladoAcera", AttributeType.STRING);
	return featureCollection;
    }

    public static void copyAttributeValues(Edge edge, Feature feature)
    {
        if (edge instanceof DynamicEdge)
            {
        	feature.setAttribute("impedanciaAB", ((DynamicEdge) edge).getImpedance(edge.getNodeA()).getCost(1));
        	feature.setAttribute("impedanciaBA", ((DynamicEdge) edge).getImpedance(edge.getNodeB()).getCost(1));
        	feature.setAttribute("costeEjeDinamico", ((DynamicEdge) edge).getCost());
            }
        if (edge instanceof ILocalGISEdge)
            {
        	feature.setAttribute("longitudEje", ((ILocalGISEdge) edge).getEdgeLength());
        	feature.setAttribute("idFeature", ((ILocalGISEdge) edge).getIdFeature());
        	feature.setAttribute("idCapa", ((ILocalGISEdge) edge).getIdLayer());
            }
        if (edge instanceof LocalGISStreetDynamicEdge)
            {
        	LocalGISStreetDynamicEdge streetEdge = (LocalGISStreetDynamicEdge) edge;
        	feature.setAttribute("regulacionTrafico", streetEdge.getTrafficRegulation().toString());
        	feature.setAttribute("maxVelocidadNominal", EdgesFeatureCollections.RedondearVelocidad(streetEdge.getNominalMaxSpeed() * 3600 / 1000));
        	feature.setAttribute("pintadaRegulacionTrafico", 0);
            }
        if (edge instanceof PMRLocalGISStreetDynamicEdge)
            {
        	PMRLocalGISStreetDynamicEdge pmrEdge = (PMRLocalGISStreetDynamicEdge) edge;
        	feature.setAttribute("anchuraAcera", pmrEdge.getWidth());
        	feature.setAttribute("pendienteTransversal", pmrEdge.getTransversalSlope());
        	feature.setAttribute("pendienteLongitudinal", pmrEdge.getLongitudinalSlope());
        	feature.setAttribute("tipoEje", pmrEdge.getsEdgeType());
        	feature.setAttribute("alturaObstaculo", pmrEdge.getObstacleHeight());
        	if (pmrEdge.getCalculatedSide() == SidewalkEdge.LEFT)
        	    feature.setAttribute("ladoAcera", "L");
        	else if (pmrEdge.getCalculatedSide() == SidewalkEdge.RIGHT)
        	    feature.setAttribute("ladoAcera", "R");
        	feature.setAttribute("ejeRelacionadoConId", pmrEdge.getRelatedToId());
        	if (edge instanceof ZebraDynamicEdge)
        	    {
        		feature.setAttribute("tipoPasoCebra", ((ZebraDynamicEdge) edge).getsType());
        	    }
            }
        }

    public static double RedondearVelocidad(double speed)
    {
    	try{
    		return Math.round(speed*Math.pow(10,2))/Math.pow(10,2);
    	}catch (Exception e) {
    		return speed;
    	}
    }

}
