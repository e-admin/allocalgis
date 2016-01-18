package es.satec.svgviewer.localgis.sld;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * A Fill allows area geometries to be filled. There are two types of fills: solid-color and
 * repeated GraphicFill. In general, if a Fill element is omitted in its containing element, no fill
 * will be rendered. The default is a solid 50%-gray (color "#808080") opaque fill.
 * <p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */
public class Fill extends Drawing {

	private static Logger logger = (Logger) Logger.getInstance(Fill.class);
	
    // default values
    public static final Color FILL_DEFAULT = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);

    public static final double OPACITY_DEFAULT = 1.0;

    /**
     * Constructs a new <tt>Fill</tt>.
     */
    protected Fill() {
        super(new Hashtable());
    }

    /**
     * Constructs a new <tt>Fill</tt>.
     */
    protected Fill(Hashtable cssParams) {
        super(cssParams);
    }

    /**
     * Returns the (evaluated) value of the fill's CssParameter 'fill'.
     * <p>
     * 
     * @param feature
     *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
     *            'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException
     *             if the evaluation fails or the value is invalid
     */
    public Color getFill() {
        Color swtColor = null;

        CssParameter cssParam = (CssParameter) cssParams.get( "fill" );

        if ( cssParam != null ) {
        	swtColor = FILL_DEFAULT;
            String s = cssParam.getValue();

            try {
	            if (s.length()==7 && s.charAt(0)=='#') {
	            	swtColor = new Color(Display.getCurrent(),
	            			Integer.parseInt(s.substring(1, 3), 16),
	            			Integer.parseInt(s.substring(3, 5), 16),
	            			Integer.parseInt(s.substring(5, 7), 16));
	            }
            } catch ( NumberFormatException e ) {
                logger.error(e);
            }
        }

        return swtColor;
    }

    /**
     * sets the value of the fill's CssParameter 'fill' as a simple color
     * 
     * @param color
     *            color to be set
     */
    public void setFill( Color color ) {
		String hexRed = Integer.toHexString(color.getRed());
		if (hexRed.length() == 1) hexRed = "0" + hexRed;
		String hexGreen = Integer.toHexString(color.getGreen());
		if (hexGreen.length() == 1) hexGreen = "0" + hexGreen;
		String hexBlue = Integer.toHexString(color.getBlue());
		if (hexBlue.length() == 1) hexBlue = "0" + hexBlue;

		String hex = "#" + hexRed + hexGreen + hexBlue;
        CssParameter fill = new CssParameter( "fill", hex );

        cssParams.put( "fill", fill );
    }

    /**
     * Returns the (evaluated) value of the fill's CssParameter 'fill-opacity'.
     * <p>
     * 
     * @param feature
     *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
     *            'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException
     *             if the evaluation fails or the value is invalid
     */
    public double getOpacity() {
        double opacity = OPACITY_DEFAULT;

        CssParameter cssParam = (CssParameter) cssParams.get( "fill-opacity" );

        if ( cssParam != null ) {
            String value = cssParam.getValue();

            try {
                opacity = Double.parseDouble( value );
            } catch ( NumberFormatException e ) {
                logger.error(e);
            }

            if ( ( opacity < 0.0 ) || ( opacity > 1.0 ) ) {
                opacity = OPACITY_DEFAULT;
            }
        }

        return opacity;
    }

    /**
     * sets the value of the opacity's CssParameter 'opacity' as a value. Valid values ranges from 0 ..
     * 1. If a value < 0 is passed it will be set to 0. If a value > 1 is passed it will be set to
     * 1.
     * 
     * @param opacity
     *            opacity to be set
     */
    public void setOpacity( double opacity ) {

        if ( opacity > 1 ) {
            opacity = 1;
        } else if ( opacity < 0 ) {
            opacity = 0;
        }

        CssParameter fillOp = new CssParameter( "fill-opacity", "" + opacity );
        cssParams.put( "fill-opacity", fillOp );
    }

}
