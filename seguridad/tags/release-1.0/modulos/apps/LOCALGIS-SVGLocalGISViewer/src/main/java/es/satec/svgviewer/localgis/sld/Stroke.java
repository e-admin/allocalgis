package es.satec.svgviewer.localgis.sld;

import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * A Stroke allows a string of line segments (or any linear geometry) to be rendered. There are
 * three basic types of strokes: solid Color, GraphicFill (stipple), and repeated GraphicStroke. A
 * repeated graphic is plotted linearly and has its graphic symbol bended around the curves of the
 * line string. The default is a solid black line (Color "#000000").
 * <p>
 * The supported CSS-Parameter names are:
 * <ul>
 * <li>stroke (color)
 * <li>stroke-opacity
 * <li>stroke-width
 * <li>stroke-linejoin
 * <li>stroke-linecap
 * <li>stroke-dasharray
 * <li>stroke-dashoffset
 * <p>
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public class Stroke extends Drawing {
	
	private static final Logger logger = (Logger) Logger.getInstance(Stroke.class);

	public static final int LJ_MITRE = SWT.JOIN_MITER;

	public static final int LJ_ROUND = SWT.JOIN_ROUND;

	public static final int LJ_BEVEL = SWT.JOIN_BEVEL;

	public static final int LC_BUTT = SWT.CAP_FLAT;

	public static final int LC_ROUND = SWT.CAP_ROUND;

	public static final int LC_SQUARE = SWT.CAP_SQUARE;

	// default values
	public static final Color COLOR_DEFAULT = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	public static final double OPACITY_DEFAULT = 1.0;

	public static final double WIDTH_DEFAULT = 1.0;

	public static final int LJ_DEFAULT = LJ_MITRE;

	public static final int LC_DEFAULT = LC_BUTT;

	private Color color = null;

	private double smplOpacity = -1;

	private double smplWidth = -1;

	private int smplLineJoin = -1;

	private int smplLineCap = -1;

	private float[] smplDashArray = null;

	private float smplDashOffset = -1;

	/**
	 * Constructs a new <tt>Stroke<tt>.
	 */
	protected Stroke() {
		super(new Hashtable());
	}

	/**
	 * Constructs a new <tt>Stroke<tt>.
	 * <p>
	 * @param cssParams keys are <tt>Strings<tt> (see above), values are
	 *                  <tt>CssParameters</tt>
	 * @param graphicStroke 
	 * @param graphicFill
	 */
	protected Stroke(Hashtable cssParams) {
		super(cssParams);
		try {
			extractSimpleColor();
			extractSimpleOpacity();
			extractSimpleWidth();
			extractSimpleLineJoin();
			extractSimpleLineCap();
			extractSimpleDasharray();
			extractSimpleDashOffset();
		} catch ( Exception e ) {
			logger.error(e);
		}
	}

	/**
	 * extracts the color of the stroke if it is simple (nor Expression) to avoid new calculation
	 * for each call of getStroke(Feature feature)
	 */
	private void extractSimpleColor() {
		CssParameter cssParam = (CssParameter) cssParams.get( "stroke" );
		if ( cssParam != null ) {
			String s = cssParam.getValue();
			try {
				if (s.length()==7 && s.charAt(0)=='#') {
					color = new Color(Display.getCurrent(),
							Integer.parseInt(s.substring(1, 3), 16),
							Integer.parseInt(s.substring(3, 5), 16),
							Integer.parseInt(s.substring(5, 7), 16));
				}
			} catch ( NumberFormatException e ) {
				logger.error(e);
			}
		}
	}

	/**
	 * extracts the opacity of the stroke if it is simple (no Expression) to avoid new calculation
	 * for each call of getStroke(Feature feature)
	 */
	private void extractSimpleOpacity() {
		CssParameter cssParam = (CssParameter) cssParams.get( "stroke-opacity" );
		if ( cssParam != null ) {
			try {
				smplOpacity = Double.parseDouble(cssParam.getValue());
			} catch ( NumberFormatException e ) {
				logger.error(e);
			}

			if ( ( smplOpacity < 0.0 ) || ( smplOpacity > 1.0 ) ) {
				smplOpacity = OPACITY_DEFAULT;
			}
		}
	}

	/**
	 * extracts the width of the stroke if it is simple (no Expression) to avoid new calculation for
	 * each call of getStroke(Feature feature)
	 */
	private void extractSimpleWidth() {
		CssParameter cssParam = (CssParameter) cssParams.get( "stroke-width" );
		if ( cssParam != null ) {
			try {
				smplWidth = Double.parseDouble(cssParam.getValue());
			} catch ( NumberFormatException e ) {
				logger.error(e);
			}
			if ( smplWidth < 0.0 ) {
				smplWidth = WIDTH_DEFAULT;
			}
		}
	}

	/**
	 * extracts the line join of the stroke if it is simple (no Expression) to avoid new calculation
	 * for each call of getStroke(Feature feature)
	 */
	private void extractSimpleLineJoin() {
		CssParameter cssParam = (CssParameter) cssParams.get( "stroke-linejoin" );
		if ( cssParam != null ) {
			String value = cssParam.getValue();
			if ( value.equals( "mitre" ) ) {
				smplLineJoin = Stroke.LJ_MITRE;
			} else if ( value.equals( "round" ) ) {
				smplLineJoin = Stroke.LJ_ROUND;
			} else if ( value.equals( "bevel" ) ) {
				smplLineJoin = Stroke.LJ_BEVEL;
			} else {
				smplLineJoin = LJ_DEFAULT;
			}
		}
	}

	/**
	 * extracts the line cap of the stroke if it is simple (no Expression) to avoid new calculation
	 * for each call of getStroke(Feature feature)
	 */
	private void extractSimpleLineCap() {
		CssParameter cssParam = (CssParameter) cssParams.get( "stroke-linecap" );
		if ( cssParam != null ) {
			String value = cssParam.getValue();
			if ( value.equals( "butt" ) ) {
				smplLineCap = Stroke.LC_BUTT;
			} else if ( value.equals( "round" ) ) {
				smplLineCap = Stroke.LC_ROUND;
			} else if ( value.equals( "square" ) ) {
				smplLineCap = Stroke.LC_SQUARE;
			} else {
				smplLineCap = LC_DEFAULT;
			}
		}
	}

	/**
	 * extracts the dasharray of the stroke if it is simple (no Expression) to avoid new calculation
	 * for each call of getStroke(Feature feature)
	 */
	private void extractSimpleDasharray() {
		CssParameter cssParam = (CssParameter) cssParams.get( "stroke-dasharray" );
		if ( cssParam != null ) {
			String value = cssParam.getValue();
			StringTokenizer st = new StringTokenizer( value, ",; " );
			int count = st.countTokens();
			float[] dashArray;

			if ( ( count % 2 ) == 0 ) {
				dashArray = new float[count];
			} else {
				dashArray = new float[count * 2];
			}

			int k = 0;
			while ( st.hasMoreTokens() ) {
				String s = st.nextToken();
				try {
					dashArray[k++] = Float.parseFloat( s );
				} catch ( NumberFormatException e ) {
					logger.error(e);
				}
			}

			// odd number of values -> the pattern must be repeated twice
			if ( ( count % 2 ) == 1 ) {
				int j = 0;
				while ( k < ( ( count * 2 ) - 1 ) ) {
					dashArray[k++] = dashArray[j++];
				}
			}
			smplDashArray = dashArray;
		}
	}

	/**
	 * extracts the dash offset of the stroke if it is simple (no Expression) to avoid new
	 * calculation for each call of getStroke(Feature feature)
	 */
	private void extractSimpleDashOffset() {
		CssParameter cssParam = (CssParameter) cssParams.get( "stroke-dashoffset" );
		if ( cssParam != null ) {
			String value = cssParam.getValue();
			try {
				smplDashOffset = Float.parseFloat( value );
			} catch ( NumberFormatException e ) {
				logger.error(e);
			}
		}
	}

	/**
	 * The stroke CssParameter element gives the solid color that will be used for a stroke. The
	 * color value is RGB-encoded using two hexadecimal digits per primary-color component, in the
	 * order Red, Green, Blue, prefixed with a hash (#) sign. The hexadecimal digits between A and F
	 * may be in either uppercase or lowercase. For example, full red is encoded as #ff0000 (with no
	 * quotation marks). The default color is defined to be black (#000000) in the context of the
	 * LineSymbolizer, if the stroke CssParameter element is absent.
	 * <p>
	 * 
	 * @return the (evaluated) value of the parameter
	 * @throws FilterEvaluationException
	 *             if the evaluation fails
	 */
	public Color getStroke() {
		Color swtColor = COLOR_DEFAULT;

		if ( color == null ) {
			// evaluate color depending on the passed feature's properties
			CssParameter cssParam = (CssParameter) cssParams.get( "stroke" );

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
		} else {
			swtColor = color;
		}

		return swtColor;
	}

	/**
	 * @see org.deegree.graphics.sld.Stroke#getStroke(Feature)
	 *      <p>
	 * @param stroke
	 *            the stroke to be set
	 */
	public void setStroke( Color stroke ) {
		this.color = stroke;

		String hexRed = Integer.toHexString(color.getRed());
		if (hexRed.length() == 1) hexRed = "0" + hexRed;
		String hexGreen = Integer.toHexString(color.getGreen());
		if (hexGreen.length() == 1) hexGreen = "0" + hexGreen;
		String hexBlue = Integer.toHexString(color.getBlue());
		if (hexBlue.length() == 1) hexBlue = "0" + hexBlue;
		
		CssParameter strokeColor = new CssParameter( "stroke", "#" + hexRed + hexGreen + hexBlue);
		cssParams.put( "stroke", strokeColor );
	}

	/**
	 * The stroke-opacity CssParameter element specifies the level of translucency to use when
	 * rendering the stroke. The value is encoded as a floating-point value (float) between 0.0 and
	 * 1.0 with 0.0 representing completely transparent and 1.0 representing completely opaque, with
	 * a linear scale of translucency for intermediate values. For example, 0.65 would represent 65%
	 * opacity. The default value is 1.0 (opaque).
	 * <p>
	 * 
	 * @param feature
	 *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
	 *            'sld:ParameterValueType'
	 * @return the (evaluated) value of the parameter
	 * @throws FilterEvaluationException
	 *             if the evaluation fails
	 */
	public double getOpacity() {
		double opacity = OPACITY_DEFAULT;

		if ( smplOpacity < 0 ) {
			CssParameter cssParam = (CssParameter) cssParams.get( "stroke-opacity" );

			if ( cssParam != null ) {
				// evaluate opacity depending on the passed feature's properties
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
		} else {
			opacity = smplOpacity;
		}

		return opacity;
	}

	/**
	 * @see org.deegree.graphics.sld.Stroke#getOpacity(Feature)
	 *      <p>
	 * @param opacity
	 *            the opacity to be set for the stroke
	 */
	public void setOpacity( double opacity ) {
		if ( opacity > 1 ) {
			opacity = 1;
		} else if ( opacity < 0 ) {
			opacity = 0;
		}
		this.smplOpacity = opacity;
		CssParameter strokeOp = new CssParameter( "stroke-opacity", "" + opacity );
		cssParams.put( "stroke-opacity", strokeOp );
	}

	/**
	 * The stroke-width CssParameter element gives the absolute width (thickness) of a stroke in
	 * pixels encoded as a float. (Arguably, more units could be provided for encoding sizes, such
	 * as millimeters or typesetter's points.) The default is 1.0. Fractional numbers are allowed
	 * (with a system-dependent interpretation) but negative numbers are not.
	 * <p>
	 * 
	 * @param feature
	 *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
	 *            'sld:ParameterValueType'
	 * @return the (evaluated) value of the parameter
	 * @throws FilterEvaluationException
	 *             if the evaluation fails
	 */
	public double getWidth() {
		double width = WIDTH_DEFAULT;

		if ( smplWidth < 0 ) {
			// evaluate smplWidth depending on the passed feature's properties
			CssParameter cssParam = (CssParameter) cssParams.get( "stroke-width" );

			if ( cssParam != null ) {
				String value = cssParam.getValue();

				try {
					width = Double.parseDouble( value );
				} catch ( NumberFormatException e ) {
					logger.error(e);
				}

				if ( width <= 0.0 ) {
					width = WIDTH_DEFAULT;
				}
			}
		} else {
			width = smplWidth;
		}

		return width;
	}

	/**
	 * @see org.deegree.graphics.sld.Stroke#getWidth(Feature)
	 *      <p>
	 * @param width
	 *            the width to be set for the stroke
	 */
	public void setWidth( double width ) {
		if ( width <= 0 )
			width = 1;
		this.smplWidth = width;
		CssParameter strokeWi = new CssParameter( "stroke-width", "" + width );
		cssParams.put( "stroke-width", strokeWi );
	}

	/**
	 * The stroke-linejoin CssParameter element encode enumerated values telling how line strings
	 * should be joined (between line segments). The values are represented as content strings. The
	 * allowed values for line join are mitre, round, and bevel.
	 * <p>
	 * 
	 * @param feature
	 *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
	 *            'sld:ParameterValueType'
	 * @return the (evaluated) value of the parameter
	 * @throws FilterEvaluationException
	 *             if the evaluation fails
	 */
	public int getLineJoin() {
		int lineJoin = LJ_DEFAULT;

		if ( smplLineJoin < 0 ) {
			CssParameter cssParam = (CssParameter) cssParams.get( "stroke-linejoin" );

			if ( cssParam != null ) {
				String value = cssParam.getValue();

				if ( value.equals( "mitre" ) ) {
					lineJoin = Stroke.LJ_MITRE;
				} else if ( value.equals( "round" ) ) {
					lineJoin = Stroke.LJ_ROUND;
				} else if ( value.equals( "bevel" ) ) {
					lineJoin = Stroke.LJ_BEVEL;
				} else {
					lineJoin = LJ_DEFAULT;
				}
			}
		} else {
			lineJoin = smplLineJoin;
		}

		return lineJoin;
	}

	/**
	 * @see org.deegree.graphics.sld.Stroke#getLineJoin(Feature)
	 *      <p>
	 * @param lineJoin
	 *            the lineJoin to be set for the stroke
	 */
	public void setLineJoin( int lineJoin ) {
		String join = null;
		if ( lineJoin == Stroke.LJ_MITRE ) {
			join = "mitre";
		} else if ( lineJoin == Stroke.LJ_ROUND ) {
			join = "round";
		} else if ( lineJoin == Stroke.LJ_BEVEL ) {
			join = "bevel";
		} else {
			// default
			lineJoin = Stroke.LJ_BEVEL;
			join = "bevel";
		}
		smplLineJoin = lineJoin;
		CssParameter strokeLJ = new CssParameter( "stroke-linejoin", join );
		cssParams.put( "stroke-linejoin", strokeLJ );
	}

	/**
	 * Thestroke-linecap CssParameter element encode enumerated values telling how line strings
	 * should be capped (at the two ends of the line string). The values are represented as content
	 * strings. The allowed values for line cap are butt, round, and square. The default values are
	 * system-dependent.
	 * <p>
	 * 
	 * @param feature
	 *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
	 *            'sld:ParameterValueType'
	 * @return the (evaluated) value of the parameter
	 * @throws FilterEvaluationException
	 *             if the evaluation fails
	 */
	public int getLineCap() {
		int lineCap = LC_DEFAULT;

		if ( smplLineJoin < 0 ) {

			CssParameter cssParam = (CssParameter) cssParams.get( "stroke-linecap" );

			if ( cssParam != null ) {
				String value = cssParam.getValue();

				if ( value.equals( "butt" ) ) {
					lineCap = Stroke.LC_BUTT;
				} else if ( value.equals( "round" ) ) {
					lineCap = Stroke.LC_ROUND;
				} else if ( value.equals( "square" ) ) {
					lineCap = Stroke.LC_SQUARE;
				} else {
					lineCap = LC_DEFAULT;
				}
			}
		} else {
			lineCap = smplLineCap;
		}

		return lineCap;
	}

	/**
	 * @see org.deegree.graphics.sld.Stroke#getLineCap(Feature)
	 *      <p>
	 * @param lineCap
	 *            lineCap to be set for the stroke
	 */
	public void setLineCap( int lineCap ) {
		String cap = null;
		if ( lineCap == Stroke.LC_BUTT ) {
			cap = "butt";
		} else if ( lineCap == Stroke.LC_ROUND ) {
			cap = "round";
		} else if ( lineCap == Stroke.LC_SQUARE ) {
			cap = "square";
		} else {
			// default;
			cap = "round";
			lineCap = Stroke.LC_SQUARE;
		}
		smplLineCap = lineCap;
		CssParameter strokeCap = new CssParameter( "stroke-linecap", cap );
		cssParams.put( "stroke-linecap", strokeCap );
	}

	/**
	 * Evaluates the 'stroke-dasharray' parameter as defined in OGC 02-070. The stroke-dasharray
	 * CssParameter element encodes a dash pattern as a series of space separated floats. The first
	 * number gives the length in pixels of dash to draw, the second gives the amount of space to
	 * leave, and this pattern repeats. If an odd number of values is given, then the pattern is
	 * expanded by repeating it twice to give an even number of values. Decimal values have a
	 * system-dependent interpretation (usually depending on whether antialiasing is being used).
	 * The default is to draw an unbroken line.
	 * <p>
	 * 
	 * @param feature
	 *            the encoded pattern
	 * @throws FilterEvaluationException
	 *             if the eevaluation fails or the encoded pattern is erroneous
	 * @return the decoded pattern as an array of float-values (null if the parameter was not
	 *         specified)
	 */
	public float[] getDashArray() {
		CssParameter cssParam = (CssParameter) cssParams.get( "stroke-dasharray" );

		float[] dashArray = null;
		if ( smplDashArray == null ) {
			if ( cssParam == null ) {
				return null;
			}

			String value = cssParam.getValue();

			StringTokenizer st = new StringTokenizer( value, ",; " );
			int count = st.countTokens();

			if ( ( count % 2 ) == 0 ) {
				dashArray = new float[count];
			} else {
				dashArray = new float[count * 2];
			}

			int i = 0;
			while ( st.hasMoreTokens() ) {
				String s = st.nextToken();
				try {
					dashArray[i++] = Float.parseFloat( s );
				} catch ( NumberFormatException e ) {
					logger.error(e);
				}
			}

			// odd number of values -> the pattern must be repeated twice
			if ( ( count % 2 ) == 1 ) {
				int j = 0;
				while ( i < ( ( count * 2 ) - 1 ) ) {
					dashArray[i++] = dashArray[j++];
				}
			}
		} else {
			dashArray = smplDashArray;
		}

		return dashArray;
	}

	/**
	 * @see org.deegree.graphics.sld.Stroke#getDashArray(Feature)
	 *      <p>
	 * @param dashArray
	 *            the dashArray to be set for the Stroke
	 */
	public void setDashArray( float[] dashArray ) {
		if ( dashArray != null ) {
			String s = "";
			for ( int i = 0; i < dashArray.length - 1; i++ ) {
				s = s + dashArray[i] + ",";
			}
			s = s + dashArray[dashArray.length - 1];
			smplDashArray = dashArray;
			CssParameter strokeDash = new CssParameter( "stroke-dasharray", s );
			cssParams.put( "stroke-dasharray", strokeDash );
		}
	}

	/**
	 * The stroke-dashoffset CssParameter element specifies the distance as a float into the
	 * stroke-dasharray pattern at which to start drawing.
	 * <p>
	 * 
	 * @param feature
	 *            specifies the <tt>Feature</tt> to be used for evaluation of the underlying
	 *            'sld:ParameterValueType'
	 * @return the (evaluated) value of the parameter
	 * @throws FilterEvaluationException
	 *             if the evaluation fails
	 */
	public float getDashOffset() {
		float dashOffset = -1;

		if ( smplDashOffset < 0 ) {
			CssParameter cssParam = (CssParameter) cssParams.get( "stroke-dashoffset" );
			if ( cssParam != null ) {
				String value = cssParam.getValue();

				try {
					dashOffset = Float.parseFloat( value );
				} catch ( NumberFormatException e ) {
					logger.error(e);
				}
			}
		} else {
			dashOffset = smplDashOffset;
		}

		return dashOffset;
	}

	/**
	 * The stroke-dashoffset CssParameter element specifies the distance as a float into the
	 * stroke-dasharray pattern at which to start drawing.
	 * <p>
	 * 
	 * @param dashOffset
	 *            the dashOffset to be set for the Stroke
	 */
	public void setDashOffset( float dashOffset ) {
		if ( dashOffset < 0 )
			dashOffset = 0;
		smplDashOffset = dashOffset;
		CssParameter strokeDashOff = new CssParameter( "stroke-dashoffset", "" + dashOffset );
		cssParams.put( "stroke-dashoffset", strokeDashOff );
	}

}
