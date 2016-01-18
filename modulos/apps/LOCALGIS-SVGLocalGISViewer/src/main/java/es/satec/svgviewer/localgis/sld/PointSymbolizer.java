/**
 * PointSymbolizer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

/**
 * Used to render a "graphic" at a point. If a line-string or polygon geometry is used with this
 * symbol, then the semantic is to use the centroid of the geometry, or any similar representative
 * point. The meaning of the contained elements are discussed with the element definitions below. If
 * the Geometry element is omitted, then the "default" geometry for the feature type is used. (Many
 * feature types will have only one geometry attribute.) If the Graphic element is omitted, then
 * nothing will be plotted.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public class PointSymbolizer extends AbstractSymbolizer {

    private Graphic graphic = null;

    /**
     * Creates a new PointSymbolizer object.
     */
    public PointSymbolizer() {
        //super( null, "org.deegree.graphics.displayelements.PointDisplayElement" );
        Stroke stroke = new Stroke();
        Fill fill = new Fill();
        Mark mark = new Mark( "square", stroke, fill );
        Object[] mae = new Object[] { mark };
        graphic = new Graphic(mae, 5);
    }

    /**
     * constructor initializing the class with the <PointSymbolizer>
     */
    PointSymbolizer( Graphic graphic, double min, double max ) {
        //super( geometry, "org.deegree.graphics.displayelements.PointDisplayElement" );

        if ( graphic == null ) {
            graphic = new Graphic();
        }

        setGraphic( graphic );
        setMinScaleDenominator( min );
        setMaxScaleDenominator( max );
    }

    /**
     * constructor initializing the class with the <PointSymbolizer>
     */
    PointSymbolizer( Graphic graphic, String responsibleClass, double min, double max ) {
        //super( geometry, responsibleClass );

        if ( graphic == null ) {
            graphic = new Graphic();
        }

        setGraphic( graphic );
        setMinScaleDenominator( min );
        setMaxScaleDenominator( max );
    }

    /**
     * A Graphic is a "graphic symbol" with an inherent shape, color, and size. Graphics can either
     * be referenced from an external URL in a common format (such as GIF or SVG) or may be derived
     * from a Mark. Multiple external URLs may be referenced with the semantic that they all provide
     * the same graphic in different formats. The "hot spot" to use for rendering at a point or the
     * start and finish handle points to use for rendering a graphic along a line must either be
     * inherent in the external format or are system- dependent. The default size of an image format
     * (such as GIF) is the inherent size of the image. The default size of a format without an
     * inherent size is 16 pixels in height and the corresponding aspect in width. If a size is
     * specified, the height of the graphic will be scaled to that size and the corresponding aspect
     * will be used for the width. The default if neither an ExternalURL nor a Mark is specified is
     * to use the default Mark with a size of 6 pixels. The size is in pixels and the rotation is in
     * degrees clockwise, with 0 (default) meaning no rotation. In the case that a Graphic is
     * derived from a font-glyph Mark, the Size specified here will be used for the final rendering.
     * Allowed CssParameters are "opacity", "size", and "rotation".
     * 
     * @return the graphic of the point
     * 
     */
    public Graphic getGraphic() {
        return graphic;
    }

    /**
     * sets the <Graphic>
     * 
     * @param graphic
     *            the graphic of the point
     * 
     */
    public void setGraphic( Graphic graphic ) {
        this.graphic = graphic;
    }

    public void dispose(){
    	if (graphic!=null)
    		graphic.dispose();
    	
    }
}