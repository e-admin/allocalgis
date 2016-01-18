package es.satec.svgviewer.localgis.sld;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * The Font element identifies a font of a certain family, style, weight, size and color.
 * <p>
 * The supported CSS-Parameter names are:
 * <ul>
 * <li>font-family
 * <li>font-style
 * <li>font-weight
 * <li>font-size
 * <li>font-color
 * <p>
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */
public class Font {

	private static Logger logger = (Logger) Logger.getInstance(Font.class);
	
    public static final int STYLE_NORMAL = SWT.NORMAL;

    public static final int STYLE_ITALIC = SWT.ITALIC;

    public static final int STYLE_OBLIQUE = SWT.ITALIC;

    public static final int WEIGHT_NORMAL = SWT.NORMAL;

    public static final int WEIGHT_BOLD = SWT.BOLD;

    public static final int SIZE_DEFAULT = 10;

    public static final Color COLOR_DEFAULT = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);

    private Hashtable cssParams = null;

    /**
     * Constructs a new <tt>Font<tt>.
     * <p>
     * @param cssParams keys are <tt>Strings<tt> (see above), values are
     *                  <tt>CssParameters</tt>
     */
    protected Font(Hashtable cssParams) {
        this.cssParams = cssParams;
    }

    /**
     * returns the Map of the CssParameters describing a Font
     * 
     * @return the Map of the CssParameters describing a Font
     */
    public Hashtable getCssParameters() {
        return cssParams;
    }

    /**
     * Returns the (evaluated) value of the font's CssParameter 'font-family'.
     * <p>
     * 
     * @param feature
     *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
     *            'sld:ParameterValueType'
     * @return the (evaluated) <tt>String</tt> value of the parameter
     * @throws FilterEvaluationException
     *             if the evaluation fails
     */
    public String getFamily() {
        CssParameter cssParam = (CssParameter) cssParams.get( "font-family" );

        if ( cssParam == null ) {
            return null;
        }

        return cssParam.getValue();
    }

    /**
     * Sets the value of the font's CssParameter 'font-family'.
     * <p>
     * 
     * @param family
     *            font family to be set
     */
    public void setFamily(String family) {
        CssParameter fontFamily = new CssParameter( "font-family", "" + family );
        cssParams.put( "font-family", fontFamily );
    }

    /**
     * Returns the (evaluated) value of the font's CssParameter 'font-style'.
     * <p>
     * 
     * @param feature
     *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
     *            'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException
     *             if the evaluation fails or the specified style is not one of the following:
     *             'normal', 'italic' and 'oblique'
     */
    public int getStyle() {
        CssParameter cssParam = (CssParameter) cssParams.get( "font-style" );

        if ( cssParam == null ) {
            return STYLE_NORMAL;
        }

        String s = cssParam.getValue();

        if ( s.equals( "italic" ) ) {
            return STYLE_ITALIC;
        } else if ( s.equals( "oblique" ) ) {
            return STYLE_OBLIQUE;
        } else {
            return STYLE_NORMAL;
        }
    }

    /**
     * Sets the value of the font's CssParameter 'font-style'.
     * <p>
     * 
     * @param style
     *            font-style to be set
     */
    public void setStyle( int style ) {
        CssParameter fontStyle = new CssParameter( "font-style", "" + style );
        cssParams.put( "font-style", fontStyle );
    }

    /**
     * Returns the (evaluated) value of the font's CssParameter 'font-weight' as a
     * <tt>ParameterValueType</tt>.
     * <p>
     * 
     * @param feature
     *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
     *            'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException
     *             if the evaluation fails or the specified weight is not one of the following:
     *             'normal' and 'bold'
     */
    public int getWeight() {
        CssParameter cssParam = (CssParameter) cssParams.get( "font-weight" );

        if ( cssParam == null ) {
            return WEIGHT_NORMAL;
        }

        String s = cssParam.getValue();

        if ( s.equals( "bold" ) ) {
            return WEIGHT_BOLD;
        } else {
        	return WEIGHT_NORMAL;
        }
    }

    /**
     * Sets the value of the font's CssParameter 'font-weight'.
     * <p>
     * 
     * @param weight
     *            font-weight to be set
     */
    public void setWeight( int weight ) {
        CssParameter fontWeight = new CssParameter( "font-weight", "" + weight );
        cssParams.put( "font-weight", fontWeight );
    }

    /**
     * Returns the (evaluated) value of the font's CssParameter 'font-size'.
     * <p>
     * 
     * @param feature
     *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
     *            'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException
     *             if the evaluation fails or the value does not denote a valid number or the number
     *             is not greater or equal zero
     */
    public int getSize() {
        CssParameter cssParam = (CssParameter) cssParams.get( "font-size" );
        int sizeInt = SIZE_DEFAULT;

        if ( cssParam != null ) {
            String s = cssParam.getValue();

            try {
                sizeInt = (int) Float.parseFloat(s);
            } catch ( NumberFormatException e ) {
                logger.error(e);
            }

            if ( sizeInt <= 0 ) {
                sizeInt = SIZE_DEFAULT;
            }
        }

        return sizeInt;
    }

    /**
     * Returns the (evaluated) value of the font's CssParameter 'font-size'.
     * <p>
     * 
     * @param size
     *            font-size to be set
     */
    public void setSize( int size ) {
        CssParameter fontSize = new CssParameter( "font-size", "" + size );
        cssParams.put( "font-size", fontSize );
    }

    /**
     * Returns the (evaluated) value of the font's CssParameter 'font-color'.
     * <p>
     * 
     * @param feature
     *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
     *            'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException
     *             if the evaluation fails
     */
    public Color getColor() {
        CssParameter cssParam = (CssParameter) cssParams.get( "font-color" );
        Color swtColor = COLOR_DEFAULT;

        if ( cssParam != null ) {
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
     * Sets the value of the font's CssParameter 'font-color'.
     * <p>
     * 
     * @param color
     *            the font-color to be set
     */
    public void setColor( Color color ) {
		String hexRed = Integer.toHexString(color.getRed());
		if (hexRed.length() == 1) hexRed = "0" + hexRed;
		String hexGreen = Integer.toHexString(color.getGreen());
		if (hexGreen.length() == 1) hexGreen = "0" + hexGreen;
		String hexBlue = Integer.toHexString(color.getBlue());
		if (hexBlue.length() == 1) hexBlue = "0" + hexBlue;

		String hex = "#" + hexRed + hexGreen + hexBlue;
        CssParameter fontColor = new CssParameter( "font-color", hex );
        cssParams.put( "font-color", fontColor );
    }

}
