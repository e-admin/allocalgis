/**
 * CombinedSchemaCalculator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools;

import java.util.HashSet;
import java.util.Set;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.network.NetworkLink;
import org.uva.route.util.NodeUtils;

import com.geopista.ui.plugin.routeenginetools.routeutil.EdgesFeatureCollections;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;


public class CombinedSchemaCalculator
{

    // Cache para optimizar el bucle
    Set<Class> registeredTypes = new HashSet<Class>();

    /**
     * Genera esquemas para las features combinando lo necesario para los edges que se le van pasando
     * 
     * @author juacas
     * 
     */
    public FeatureCollection getUpdatedFeatureSchema(FeatureCollection edgesFeatureCol, Edge edge)
    {
	edge=(Edge) NodeUtils.equivalentTo(edge);
	// ignora el analisis del esquema si ya está registrado
	if (edgesFeatureCol == null)
	    registeredTypes.clear();
	
	if (edge==null)
	    {
		return edgesFeatureCol==null?AddNewLayerPlugIn.createBlankFeatureCollection():edgesFeatureCol;
	    }
	if (registeredTypes.contains(edge.getClass()))
	    return edgesFeatureCol;
	else
	    registeredTypes.add(edge.getClass());

	if (edge instanceof ILocalGISEdge)
	    {
		edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection(edgesFeatureCol);
	    } 
	if (edge instanceof LocalGISStreetDynamicEdge)
	    {
		edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection(edgesFeatureCol);
	    }
	if (edge instanceof DynamicEdge)
	    {
		edgesFeatureCol = EdgesFeatureCollections.getDynamicEdgeFeatureCollection(edgesFeatureCol);
	    } 
	if (edge instanceof Edge)
	    {
		edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection(edgesFeatureCol);
	    }
	FeatureSchema featureSchema = edgesFeatureCol.getFeatureSchema();
	EdgesFeatureCollections.addAttributeIfNotPresent(featureSchema, "networkName", AttributeType.STRING);

	if (edge instanceof NetworkLink)
	    {
		EdgesFeatureCollections.addAttributeIfNotPresent(featureSchema, "idEje", AttributeType.INTEGER);
		EdgesFeatureCollections.addAttributeIfNotPresent(featureSchema, "coste", AttributeType.DOUBLE);
	    }
	if (edge instanceof PMRLocalGISStreetDynamicEdge)
	    {
		edgesFeatureCol = EdgesFeatureCollections.getPMRLocalGISStreetDynamicEdgeFeatureCollection(edgesFeatureCol);
	    }
	registeredTypes.add(edge.getClass());
	return edgesFeatureCol;
    }

    public FeatureCollection addAttributeIfNotPresent(FeatureCollection fCollection, String name, AttributeType type)
    {
	EdgesFeatureCollections.addAttributeIfNotPresent(fCollection.getFeatureSchema(), name, type);
	return fCollection;
    }
}
