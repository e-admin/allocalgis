/**
 * PolygonSymbolizer_Impl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 06-ago-2004
 *
 */

package org.deegree_impl.graphics.sld;

import org.deegree.graphics.sld.Fill;
import org.deegree.graphics.sld.Geometry;
import org.deegree.graphics.sld.PolygonSymbolizer;
import org.deegree.graphics.sld.Stroke;
import org.deegree.xml.Marshallable;
import org.deegree_impl.tools.Debug;

/**
 * @author enxenio s.l.
 *
 */

/**
 * Used to render an interior "fill" and an outlining "stroke" for a polygon or
 * other 2D-area geometry. If a point or line are used, the fill is ignored and
 * the stroke is used as described in the LineSymbol. A missing Geometry element
 * selects the default geometry. A missing Fill or Stroke element means that
 * there will be no fill or stroke plotted, respectively. The contained elements
 * are in the conceptual order of their being used and plotted using the "painters
 * model", where the Fill will be rendered first, and then the Stroke will be
 * rendered on top of the Fill.
 * <p>----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/03 12:31:54 $
 */
public class PolygonSymbolizer_Impl extends Symbolizer_Impl
									implements PolygonSymbolizer, Marshallable {
	private Fill fill = null;
	private Stroke stroke = null;

	/**
	 * Creates a new PolygonSymbolizer_Impl object.
	 */
	public PolygonSymbolizer_Impl() {
		super( null );
		setFill( new Fill_Impl() );

		Stroke stroke = new Stroke_Impl();
		setStroke( stroke );
	}

	/**
	 * constructor initializing the class with the <PolygonSymbolizer>
	 */
	PolygonSymbolizer_Impl( Fill fill, Stroke stroke, Geometry geometry, double min, double max ) {
		super( geometry );
		setFill( fill );
		setStroke( stroke );
		setMinScaleDenominator( min );
		setMaxScaleDenominator( max );
	}

	/**
	 * A Fill allows area geometries to be filled. There are two types of fills:
	 * solid-color and repeated GraphicFill. In general, if a Fill element is
	 * omitted in its containing element, no fill will be rendered. The default
	 * is a solid 50%-gray (color "#808080") opaque fill.
	 * @return the fill of the polygon
	 */
	public Fill getFill() {
		return fill;
	}

	/**
	 * sets the <Fill>
	 * @param fill the fill of the polygon
	 */
	public void setFill( Fill fill ) {
		this.fill = fill;
	}

	/**
	 * A Stroke allows a string of line segments (or any linear geometry) to be
	 * rendered. There are three basic types of strokes: solid Color, GraphicFill
	 * (stipple), and repeated GraphicStroke. A repeated graphic is plotted
	 * linearly and has its graphic symbol bended around the curves of the line
	 * string. The default is a solid black line (Color "#000000").
	 * @return the stroke of the polygon
	 */
	public Stroke getStroke() {
		return stroke;
	}

	/**
	 * sets the <Stroke>
	 * @param the stroke of the polygon
	 */
	public void setStroke( Stroke stroke ) {
		this.stroke = stroke;
	}

	/**
	 * Produces a textual representation of this object.
	 * @return the textual representation
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "scale constraint:  >=" + minDenominator + " AND <" + maxDenominator + "\n" );
		sb.append( "<PolygonSymbolizer>\n" );

		if ( getGeometry() != null ) {
			sb.append( getGeometry() ).append( "\n" );
		}

		if ( getFill() != null ) {
			sb.append( getFill() ).append( "\n" );
		}

		if ( getStroke() != null ) {
			sb.append( getStroke() ).append( "\n" );
		}

		sb.append( "</PolygonSymbolizer>\n" );

		return sb.toString();
	}
    
	/**
	 * exports the content of the PolygonSymbolizer as XML formated String
	 *
	 * @return xml representation of the PolygonSymbolizer
	 */
	public String exportAsXML() {
		Debug.debugMethodBegin();
        
		StringBuffer sb = new StringBuffer(1000);
		sb.append( "<PolygonSymbolizer>" );
		if ( geometry != null ) {
			sb.append( ((Marshallable)geometry).exportAsXML() );
		}
		if ( fill != null ) {
			sb.append( ((Marshallable)fill).exportAsXML() );
		}
		if ( stroke != null ) {
			sb.append( ((Marshallable)stroke).exportAsXML() );
		}
		sb.append( "</PolygonSymbolizer>" );
        
		Debug.debugMethodEnd();
		return sb.toString(); 
	}
}

