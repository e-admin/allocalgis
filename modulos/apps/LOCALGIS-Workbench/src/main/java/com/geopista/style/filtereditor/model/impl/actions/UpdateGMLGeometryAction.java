/**
 * UpdateGMLGeometryAction.java
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

import org.deegree.gml.GMLCoordinates;
import org.deegree.gml.GMLGeometry;
import org.deegree.gml.GMLPoint;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateGMLGeometryAction {
	
	private GMLGeometry _gmlGeometry;
	private String _srs;
	private String _coordinates;
	
	public UpdateGMLGeometryAction(GMLGeometry gmlGeometry,String srs,String coordinates) {
		
		_gmlGeometry = gmlGeometry;
		_srs = srs;
		_coordinates = coordinates;
	}
	
	public Object execute() {
		
		GMLPoint gmlPoint = (GMLPoint)_gmlGeometry;
		gmlPoint.setSrs(_srs);
		GMLCoordinates coordinates = gmlPoint.getCoordinates();
		coordinates.setCoordinates(_coordinates);
		gmlPoint.setCoordinates(coordinates);
		return gmlPoint;
	}
}
