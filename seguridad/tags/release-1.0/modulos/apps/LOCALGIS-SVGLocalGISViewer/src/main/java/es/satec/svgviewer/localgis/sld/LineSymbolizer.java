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