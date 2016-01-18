/**
 * WorkBenchGeometryProducer.java
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
import org.uva.route.util.NodeUtils;

import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class WorkBenchGeometryProducer implements EdgesGeometryProducer
{
    private PlugInContext context;
    private GeometryFactory geometryFactory;

    public WorkBenchGeometryProducer(PlugInContext context, GeometryFactory geometryFactory) {
	this.context = context;
	this.geometryFactory = geometryFactory;
    }

    /**
     * Determina el arco original en el caso de que actualEdge sea un EquivalentEdge
     * 
     * @param actualEdge
     * @param routePath
     * @return
     */
    @Override
    public Geometry getEdgeGeometry(Edge actualEdge, Path routePath)
    {
	// Si el tramo es un ProxyEdge cogemos el getGraphable().
	actualEdge = (Edge) NodeUtils.unwrapProxies(actualEdge);

	Edge origEdge = actualEdge; // Con los tramos cortados no coincide

	if (actualEdge instanceof EquivalentEdge)
	    {
		EquivalentEdge equivalentEdge = (EquivalentEdge) origEdge;
		origEdge = (Edge) equivalentEdge.getEquivalentTo();
	    }
	return getEdgeGeometry(actualEdge, origEdge, routePath);
    }

    @Override
    public Geometry getEdgeGeometry(Edge actualEdge, Edge origEdge, Path routePath)
    {
	actualEdge = (Edge) NodeUtils.unwrapProxies(actualEdge);
	// Si el tramo es un ILocalGisEdge intentamos obtener la geometrï¿½a original
	Geometry geom = null;
	try
	    {
		if (actualEdge instanceof EquivalentEdge)
		    {
			// TODO calcular fragmento del arco virtual.
			geom = NetworkModuleUtilWorkbench.findLocalGeometry(origEdge, context);
		    }
		else
		if (actualEdge instanceof ILocalGISEdge)
		    {
			geom = NetworkModuleUtilWorkbench.findLocalGeometry(actualEdge, context);
			if (geom == null)
			    {
				int SRID = 0;

				try
				    {
					SRID = context.getLayerManager().getCoordinateSystem().getEPSGCode();
					if (SRID > 0)
					    {
						geom = WriteRoutePathInfoWithDialog.getOriginalGeometryFromEdgeIdLayerIdFeature((ILocalGISEdge) actualEdge,
							SRID);
					    }
				    } catch (Exception e)
				    {
					e.printStackTrace(); // Best try
				    }
			    }
			if (geom == null)
			    {
				// SI ha ocurrido algï¿½n error al obtener la geometrï¿½a original del tramos
				// intentamos asignarle el lineString del propio tramo.
				Coordinate[] coords = NodeUtils.CoordenadasArco(origEdge, routePath, 0, 1);
				geom = (LineString) geometryFactory.createLineString(coords);
			    }
		    } else
		    {
			Coordinate[] coords = NodeUtils.CoordenadasArco(actualEdge, routePath, 0, 1);
			geom = (LineString) geometryFactory.createLineString(coords);
		    }
	    } catch (Exception e)
	    {
		e.printStackTrace();
	    }
	return geom;
    }

}
