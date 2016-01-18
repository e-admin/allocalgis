/**
 * UpdateGMLBoxAction.java
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

import org.deegree.gml.GMLBox;
import org.deegree.gml.GMLCoord;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateGMLBoxAction {
	
	private GMLBox _gmlBox;
	private double _minx;
	private double _miny;
	private double _maxx;
	private double _maxy;
	private String _srs;
	
	public UpdateGMLBoxAction(GMLBox gmlBox, double minx, double miny, double maxx, double maxy, String srs) {
		
		_gmlBox = gmlBox;
		_minx = minx;
		_miny = miny;
		_maxx = maxx;
		_maxy = maxy;
		_srs = srs;
	}
	
	public Object execute() {
		
		_gmlBox.setSrs(_srs);
		GMLCoord minCoord = _gmlBox.getMin();
		minCoord.setCoord(_minx,_miny);
		_gmlBox.setMin(minCoord);
		GMLCoord maxCoord = _gmlBox.getMax();
		maxCoord.setCoord(_maxx,_maxy);
		_gmlBox.setMax(maxCoord);
		return _gmlBox;
	}
}
