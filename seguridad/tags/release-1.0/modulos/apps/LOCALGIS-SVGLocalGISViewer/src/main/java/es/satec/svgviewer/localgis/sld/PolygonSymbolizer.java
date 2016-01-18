package es.satec.svgviewer.localgis.sld;

/**
 * Used to render an interior "fill" and an outlining "stroke" for a polygon or other 2D-area
 * geometry. If a point or line are used, the fill is ignored and the stroke is used as described in
 * the LineSymbol. A missing Geometry element selects the default geometry. A missing Fill or Stroke
 * element means that there will be no fill or stroke plotted, respectively. The contained elements
 * are in the conceptual order of their being used and plotted using the "painters model", where the
 * Fill will be rendered first, and then the Stroke will be rendered on top of the Fill.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public class PolygonSymbolizer extends AbstractSymbolizer {

    private Fill fill = null;

    private Stroke stroke = null;

    /**
     * Creates a new PolygonSymbolizer object.
     */
    public PolygonSymbolizer() {
        //super( null, "org.deegree.graphics.displayelements.PolygonDisplayElement" );
        setFill( new Fill() );

        Stroke stroke = new Stroke();
        setStroke( stroke );
    }

    /**
     * constructor initializing the class with the <PolygonSymbolizer>
     */
    PolygonSymbolizer( Fill fill, Stroke stroke, double min, double max ) {
        //super( geometry, "org.deegree.graphics.displayelements.PolygonDisplayElement" );
        setFill( fill );
        setStroke( stroke );
        setMinScaleDenominator( min );
        setMaxScaleDenominator( max );
    }

    /**
     * constructor initializing the class with the <PolygonSymbolizer>
     */
    PolygonSymbolizer( Fill fill, Stroke stroke, String responsibleClass, double min, double max ) {
        //super( geometry, responsibleClass );
        setFill( fill );
        setStroke( stroke );
        setMinScaleDenominator( min );
        setMaxScaleDenominator( max );
    }

    /**
     * A Fill allows area geometries to be filled. There are two types of fills: solid-color and
     * repeated GraphicFill. In general, if a Fill element is omitted in its containing element, no
     * fill will be rendered. The default is a solid 50%-gray (color "#808080") opaque fill.
     * 
     * @return the fill of the polygon
     */
    public Fill getFill() {
        return fill;
    }

    /**
     * sets the <Fill>
     * 
     * @param fill
     *            the fill of the polygon
     */
    public void setFill( Fill fill ) {
        this.fill = fill;
    }

    /**
     * A Stroke allows a string of line segments (or any linear geometry) to be rendered. There are
     * three basic types of strokes: solid Color, GraphicFill (stipple), and repeated GraphicStroke.
     * A repeated graphic is plotted linearly and has its graphic symbol bended around the curves of
     * the line string. The default is a solid black line (Color "#000000").
     * 
     * @return the stroke of the polygon
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * sets the <Stroke>
     * 
     * @param stroke
     *            the stroke of the polygon
     */
    public void setStroke( Stroke stroke ) {
        this.stroke = stroke;
    }

    /**
     * Produces a textual representation of this object.
     * 
     * @return the textual representation
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append( "scale constraint:  >=" + minDenominator + " AND <" + maxDenominator + "\n" );
        sb.append( "<PolygonSymbolizer>\n" );

        if ( getFill() != null ) {
            sb.append( getFill() ).append( "\n" );
        }

        if ( getStroke() != null ) {
            sb.append( getStroke() ).append( "\n" );
        }

        sb.append( "</PolygonSymbolizer>\n" );

        return sb.toString();
    }
    public void dispose(){

    }
}