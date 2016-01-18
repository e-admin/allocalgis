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
