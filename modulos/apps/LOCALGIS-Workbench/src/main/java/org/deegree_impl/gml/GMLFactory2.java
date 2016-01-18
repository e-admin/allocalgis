/**
 * GMLFactory2.java
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
package org.deegree_impl.gml;

import org.deegree.gml.GMLBox;
import org.deegree.gml.GMLCoord;
import org.w3c.dom.Element;

/**
 * @author enxenio s.l.
 *
 */
public class GMLFactory2 {

	public static GMLBox createGMLBox(double minx,double miny,double maxx,double maxy,String srs) {
		
		GMLDocument_Impl doc = new GMLDocument_Impl();
		Element coord = doc.getDocument()
						   .createElementNS( "http://www.opengis.net/gml", "gml:coord" );

		GMLCoord minCoord = new GMLCoord_Impl(coord);
		minCoord.setCoord(minx,miny);
		GMLCoord maxCoord = new GMLCoord_Impl(coord);
		maxCoord.setCoord(maxx,maxy);
		GMLBox gmlBox = GMLBox_Impl.createGMLBox(doc.getDocument());
		gmlBox.setMin(minCoord);
		gmlBox.setMax(maxCoord);

		try {
			gmlBox.setSrs( "http://www.opengis.net/gml/srs/" + srs );
		} catch ( Exception e ) {
			gmlBox.setSrs( "null" );
		}
		return gmlBox;
	}
}
