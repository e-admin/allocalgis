/**
 * LocalGISSpatialMemoryAdaptor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.manager;

import java.util.TreeMap;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.route.util.DistanceCheckVisitorJTS;
import org.uva.route.util.DistanceGraphVisitor;
import org.uva.route.util.GeometryFinderForVisitor;
import org.uva.route.util.VisitorFactory;
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.managers.SpatialMemoryAdaptor;

import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class LocalGISSpatialMemoryAdaptor extends SpatialMemoryAdaptor implements VisitorFactory
{
    private DistanceCheckVisitorJTS visitor;
    PlugInContext context=null;

    public LocalGISSpatialMemoryAdaptor(AllInMemoryManager memoryManager)
    {
	super(memoryManager);
	setVisitorFactory(this);
    }

    @Override
    public DistanceGraphVisitor getInstance(double tolerance, TreeMap<Double, Graphable> result, Geometry where, int num)
    {
	this.visitor=new DistanceCheckVisitorJTS(tolerance, result, where, num);
	visitor.setGeometryFinder(new GeometryFinderForVisitor()
	    {
		
		@Override
		public Geometry findJTSGeometry(Graphable component)
		{
		   if ( component instanceof Edge)
		       {
			  
			   return NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal((Edge)component, context);
		       }
		   else
		       return null;
		}
		
		@Override
		public org.opengis.geometry.Geometry findGeometry(Graphable component)
		{
		    return null;
		}
	    });
	return visitor;
    }

    public void setContext(PlugInContext context)
    {
        this.context = context;
    }

    public DistanceCheckVisitorJTS getVisitor()
    {
        return visitor;
    }

    public PlugInContext getContext()
    {
        return context;
    }
   

}
