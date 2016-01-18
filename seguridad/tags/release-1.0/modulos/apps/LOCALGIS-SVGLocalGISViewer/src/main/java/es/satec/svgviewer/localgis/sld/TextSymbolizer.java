package es.satec.svgviewer.localgis.sld;

import org.deegree.model.filterencoding.Expression;

/**
 * Used to render a text label, according to the parameters. A missing Geometry, Label, Font, or
 * LabelPlacement element selects the default value or behavior for the element. The default Label,
 * Font, and LabelPlacement are system- dependent. Multiple Font elements may be used to specify
 * alternate fonts in order of preference in case a map server does not support the first
 * preference. A missing Halo or Fill element means that no halo or fill will be plotted,
 * respectively. The Fill is rendered over top of the Halo, and the Halo includes the interiors of
 * the font glyphs.
 * <p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public class TextSymbolizer extends AbstractSymbolizer {

	private Fill fill = null;

	private Font font = null;

	private LabelPlacement labelPlacement = null;

	private Expression expLabel = null;

	/**
	 * constructor initializing the class with the <TextSymbolizer>
	 */
	TextSymbolizer(Expression expLabel, Font font, LabelPlacement labelPlacement,
			Fill fill, double min, double max ) {
		//super("org.deegree.graphics.displayelements.LabelDisplayElement" );
		setLabel(expLabel);
		setFont( font );
		setLabelPlacement( labelPlacement );
		setFill( fill );
		setMinScaleDenominator( min );
		setMaxScaleDenominator( max );
	}

	/**
	 * constructor initializing the class with the <TextSymbolizer>
	 */
	TextSymbolizer(String responsibleClass, Expression expLabel, Font font,
			LabelPlacement labelPlacement, Fill fill, double min, double max ) {
		super(responsibleClass );
		setLabel(expLabel);
		setFont( font );
		setLabelPlacement( labelPlacement );
		setFill( fill );
		setMinScaleDenominator( min );
		setMaxScaleDenominator( max );
	}

	public Expression getExpLabel() {
		return expLabel;
	}

	public void setLabel(Expression expLabel) {
		this.expLabel = expLabel;
	}

	/**
	 * Identifies a Font of a certain family, style, and size.
	 * 
	 * @return the font
	 * 
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Sets a Font of a certain family, style, and size.
	 * 
	 * @param font
	 *            the font
	 * 
	 */
	public void setFont( Font font ) {
		this.font = font;
	}

	/**
	 * Used to position a label relative to a point or a line string. For a point, you can specify
	 * the anchor point of the label and a linear displacement from the point (so that you can also
	 * plot a graphic symbol at the point). For a line-string placement, you can specify a
	 * perpendicular offset (so you can draw a stroke on the line).
	 * <p>
	 * </p>
	 * MORE PARAMETERS ARE PROBABLY NEEDED HERE.
	 * 
	 * @return the labelPlacement
	 * 
	 */
	public LabelPlacement getLabelPlacement() {
		return labelPlacement;
	}

	/**
	 * sets the <LabelPlacement>
	 * 
	 * @param labelPlacement
	 *            the labelPlacement
	 * 
	 */
	public void setLabelPlacement( LabelPlacement labelPlacement ) {
		this.labelPlacement = labelPlacement;
	}

	/**
	 * A Fill allows area geometries to be filled. There are two types of fills: solid-color and
	 * repeated GraphicFill. In general, if a Fill element is omitted in its containing element, no
	 * fill will be rendered. The default is a solid 50%-gray (color "#808080") opaque fill.
	 * 
	 * @return the fill
	 * 
	 */
	public Fill getFill() {
		return fill;
	}

	/**
	 * sets the <Fill>
	 * 
	 * @param fill
	 *            the fill
	 * 
	 */
	public void setFill( Fill fill ) {
		this.fill = fill;
	}
	
    public void dispose(){

    }
}