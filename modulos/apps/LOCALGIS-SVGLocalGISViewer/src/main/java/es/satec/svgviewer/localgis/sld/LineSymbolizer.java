/**
 * LineSymbolizer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

/**
 * Used to render a "stroke" along a linear geometry. If a point geometry is used, it should be
 * interpreted as a line of zero length and two end caps. If a polygon is used, then its closed
 * outline is used as the line string (with no end caps). A missing Geometry element selects the
 * default geometry. A missing Stroke element means that nothing will be plotted.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public class LineSymbolizer extends AbstractSymbolizer {

    private Stroke stroke = null;

    /**
     * Creates a new LineSymbolizer object.
     */
    public LineSymbolizer() {
        //super( null, "org.deegree.graphics.displayelements.LineStringDisplayElement" );

        Stroke stroke = new Stroke();
        setStroke( stroke );
    }

    /**
     * constructor initializing the class with the <LineSymbolizer>
     */
    LineSymbolizer( Stroke stroke, double min, double max ) {
        //super( geometry, "org.deegree.graphics.displayelements.LineStringDisplayElement" );
        setStroke( stroke );
        setMinScaleDenominator( min );
        setMaxScaleDenominator( max );
    }

    /**
     * constructor initializing the class with the <LineSymbolizer>
     */
    LineSymbolizer( Stroke stroke, String responsibleClass, double min, double max ) {
        //super( geometry, responsibleClass );
        // super( geometry, "org.deegree.graphics.displayelements.LineStringDisplayElement" );
        setStroke( stroke );
        setMinScaleDenominator( min );
        setMaxScaleDenominator( max );
    }

    /**
     * A Stroke allows a string of line segments (or any linear geometry) to be rendered. There are
     * three basic types of strokes: solid Color, GraphicFill (stipple), and repeated GraphicStroke.
     * A repeated graphic is plotted linearly and has its graphic symbol bended around the curves of
     * the line string. The default is a solid black line (Color "#000000").
     * 
     * @return the Stroke
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * sets the <Stroke>
     * 
     * @param stroke
     *            the Stroke
     * 
     */
    public void setStroke( Stroke stroke ) {
        this.stroke = stroke;
    }
    
    public void dispose(){

    }
}