/**
 * LocalGisGraphGenerator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.build;

import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;

public interface LocalGisGraphGenerator
{

    /**
     * LocalgisEdges has references to Localgis' Features and layers
     * 
     * @param geometryObject
     * @param idFeature
     * @param idLayer
     * @param geometrySRID
     * @param element
     * @return
     * @throws Exception
     */
    public abstract ILocalGISEdge add(Geometry geometryObject, int idFeature, int idLayer, int geometrySRID, Feature feature);

}