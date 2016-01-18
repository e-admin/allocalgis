/**
 * EdgesGeometryProducer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate;

import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;

import com.vividsolutions.jts.geom.Geometry;

public interface EdgesGeometryProducer
{
/**
 * Devuelve la geometría que representa el arco. La geometría está orientada en el sentido del {@link Path}
 * @param actualEdge Arco a representar. Puede ser un {@link EquivalentEdge} que represente una fracción de otro arco
 * @param origEdge  Arco base del grafo que contiene la información original. Puede coincidir con actualEdge en la mayor parte de los casos.
 * @param routePath {@link Path} con la que se determina la secuencia concreta de nodos y orientación de las geometrías.
 * @return
 */
Geometry getEdgeGeometry(Edge actualEdge, Edge origEdge, Path routePath);
/**
 * Determina origEdge {@link #getEdgeGeometry(Edge, Edge, Path)} si actualEdge es un {@link EquivalentEdge}
 * @param actualEdge
 * @param routePath
 * @return
 * @see #getEdgeGeometry(Edge, Edge, Path)
 */
Geometry getEdgeGeometry(Edge actualEdge, Path routePath);

}
