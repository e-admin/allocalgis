/**
 * AddGMLGeometryAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import org.deegree.gml.GMLException;
import org.deegree.model.geometry.GM_Point;
import org.deegree_impl.gml.GMLFactory;
import org.deegree_impl.model.cs.Adapters;
import org.deegree_impl.model.cs.ConvenienceCSFactory;
import org.deegree_impl.model.cs.CoordinateSystem;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.opengis.cs.CS_CoordinateSystem;

/**
 * @author enxenio s.l.
 *
 */
public class AddGMLGeometryAction {
	
	private static final String GEOGRAPHICS_ETRS89 = "4258";
	
	public AddGMLGeometryAction() {}
	
	public Object execute() throws GMLException {
		
		ConvenienceCSFactory factory = ConvenienceCSFactory.getInstance();
		CoordinateSystem cs = factory.getCSByName("EPSG:"+GEOGRAPHICS_ETRS89);
		Adapters adapters = Adapters.getDefault();
		CS_CoordinateSystem csCoordinateSystem = adapters.export(cs);
		GM_Point point = GeometryFactory.createGM_Point(0.0,0.0,csCoordinateSystem);
		return GMLFactory.createGMLGeometry(point);	
	}
}
