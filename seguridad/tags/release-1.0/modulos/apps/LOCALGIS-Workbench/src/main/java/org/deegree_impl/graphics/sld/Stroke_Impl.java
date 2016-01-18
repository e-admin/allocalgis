package org.deegree_impl.graphics.sld;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.deegree.graphics.sld.CssParameter;
import org.deegree.graphics.sld.GraphicFill;
import org.deegree.graphics.sld.GraphicStroke;
import org.deegree.graphics.sld.Stroke;
import org.deegree.model.feature.Feature;
import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree.xml.Marshallable;
import org.deegree_impl.tools.Debug;

/**
 * A Stroke allows a string of line segments (or any linear geometry) to be
 * rendered. There are three basic types of strokes: solid Color, GraphicFill
 * (stipple), and repeated GraphicStroke. A repeated graphic is plotted linearly
 * and has its graphic symbol bended around the curves of the line string. The
 * default is a solid black line (Color "#000000").
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
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.2 $ $Date: 2010/03/16 14:59:46 $
 */
public class Stroke_Impl extends Drawing_Impl implements org.deegree.graphics.sld.Stroke, Marshallable {
    private GraphicStroke graphicStroke = null;
    private Color color = null; 
    public Color getColor() {
		return color;
	}

	private double smplOpacity = -1;
    private double smplWidth = -1;
    private Expression width = null;
    private int smplLineJoin = -1;
    private int smplLineCap = -1;
    private float[] smplDashArray = null;
    private float smplDashOffset = -1;

    /**
     * Constructs a new <tt>Stroke_Impl<tt>.
     */
    protected Stroke_Impl() {
        super( new HashMap(), null );
    }

    /**
     * Constructs a new <tt>Stroke_Impl<tt>.
     * <p>
     * @param cssParams keys are <tt>Strings<tt> (see above), values are
     *                  <tt>CssParameters</tt>
     */
    protected Stroke_Impl( HashMap cssParams, GraphicStroke graphicStroke, 
                           GraphicFill graphicFill ) {
        super( cssParams, graphicFill );
        this.graphicStroke = graphicStroke;        
        try {
            extractSimpleColor();
            extractSimpleOpacity();
            extractSimpleWidth();
            extractWidth();
            extractSimpleLineJoin();
            extractSimpleLineCap();
            extractSimpleDasharray();
            extractSimpleDashOffset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * extracts the color of the stroke if it is simple (nor Expression) to 
     * avoid new calculation for each call of getStroke(Feature feature) 
     */
    private void extractSimpleColor() throws FilterEvaluationException {
        CssParameter cssParam = (CssParameter)cssParams.get( "stroke" );        
        if ( cssParam != null ) {
            Object[] o = cssParam.getValue().getComponents();
            for (int i = 0; i < o.length; i++) {
                if ( o[i] instanceof Expression ) {                    
                    color = null;
                    break;
                } else {
                    try {
                        color = Color.decode( (String)o[i] );
                    } catch ( NumberFormatException e ) {
                        throw new FilterEvaluationException( "Given value ('" + o[i] + 
                                                         "') for CSS-Parameter 'stroke' " + 
                                                         "does not denote a valid color!" );
                    }
                }
            }
        }
    }
    
    /**
     * returns true if the passed CssParameter contain a simple value
     */
    private boolean isSimple(CssParameter cssParam) {
        boolean simple = true;
        Object[] o = cssParam.getValue().getComponents();
        for (int i = 0; i < o.length; i++) {          
            if ( o[i] instanceof Expression ) {                    
                simple = false;
                break;
            }
        }
        return simple;
    }
    
    /**
     * extracts the opacity of the stroke if it is simple (no Expression) to 
     * avoid new calculation for each call of getStroke(Feature feature) 
     */
    private void extractSimpleOpacity() throws FilterEvaluationException {
        CssParameter cssParam = (CssParameter)cssParams.get( "stroke-opacity" );
        if ( cssParam != null ) {            
            if ( isSimple( cssParam ) ) {
                Object[] o = cssParam.getValue().getComponents();
                try {
                    smplOpacity = Double.parseDouble( (String)o[0] );
                } catch ( NumberFormatException e ) {
                    throw new FilterEvaluationException( 
                            "Given value for parameter 'stroke-opacity' ('" + o[0] + 
                            "') has invalid format!" );
                }

                if ( ( smplOpacity < 0.0 ) || ( smplOpacity > 1.0 ) ) {
                    throw new FilterEvaluationException( 
                            "Value for parameter 'stroke-opacity' (given: '" + o[0] + 
                            "') must be between 0.0 and 1.0!" );
                }
            }
        }
    }
    
    /**
     * extracts the width of the stroke if it is simple (no Expression) to 
     * avoid new calculation for each call of getStroke(Feature feature) 
     */
    private void extractSimpleWidth() throws FilterEvaluationException {
        CssParameter cssParam = (CssParameter)cssParams.get( "stroke-width" );
        if ( cssParam != null ) {            
            if ( isSimple( cssParam ) ) {
                Object[] o = cssParam.getValue().getComponents();
                try {
                    smplWidth = Double.parseDouble( (String)o[0] );
                } catch ( NumberFormatException e ) {
                    throw new FilterEvaluationException( 
                            "Given value for parameter 'stroke-width' ('" + o[0] + 
                            "') has invalid format!" );
                }
                if ( smplWidth <= 0.0  ) {
                    throw new FilterEvaluationException( 
                            "Value for parameter 'stroke-width' (given: '" + smplWidth + 
                            "') must be > 0.0!" );
                }
            }
        }
    }
    
	private void extractWidth() throws FilterEvaluationException {
		CssParameter cssParam = (CssParameter)cssParams.get( "stroke-width" );
		if ( cssParam != null ) { 
			if (!isSimple(cssParam)) {           
				Object[] o = cssParam.getValue().getComponents();
				width = (Expression)o[0];
			}
		}
	}

    /**
     * extracts the line join of the stroke if it is simple (no Expression) to 
     * avoid new calculation for each call of getStroke(Feature feature) 
     */
    private void extractSimpleLineJoin() throws FilterEvaluationException {
        CssParameter cssParam = (CssParameter)cssParams.get( "stroke-linejoin" );
        if ( cssParam != null ) {            
            if ( isSimple( cssParam ) ) {
                Object[] o = cssParam.getValue().getComponents();
                String value = (String)o[0];
                if ( value.equals( "mitre" ) ) {
                    smplLineJoin = Stroke.LJ_MITRE;
                } else if ( value.equals( "round" ) ) {
                    smplLineJoin = Stroke.LJ_ROUND;
                } else if ( value.equals( "bevel" ) ) {
                    smplLineJoin = Stroke.LJ_BEVEL;
                } else {
                    throw new FilterEvaluationException( 
                            "Given value for parameter 'stroke-linejoin' ('" + value + 
                            "') is unsupported. Supported values are: " + 
                            "'mitre', 'round' or 'bevel'!" );
                }
            }
        }
    }
    
    /**
     * extracts the line cap of the stroke if it is simple (no Expression) to 
     * avoid new calculation for each call of getStroke(Feature feature) 
     */
    private void extractSimpleLineCap() throws FilterEvaluationException {
        CssParameter cssParam = (CssParameter)cssParams.get( "stroke-linecap" );
        if ( cssParam != null ) {
            if ( isSimple( cssParam ) ) {
                Object[] o = cssParam.getValue().getComponents();
                String value = (String)o[0];
                if ( value.equals( "butt" ) ) {
                    smplLineCap = Stroke.LC_BUTT;
                } else if ( value.equals( "round" ) ) {
                    smplLineCap = Stroke.LC_ROUND;
                } else if ( value.equals( "square" ) ) {
                    smplLineCap = Stroke.LC_SQUARE;
                } else {
                    throw new FilterEvaluationException( 
                            "Given value for parameter 'stroke-linecap' ('" + value + 
                            "') is unsupported. Supported values are: " + 
                            "'butt', 'round' or 'square'!" );
                }
            }
        }
    }
    
    /**
     * extracts the dasharray of the stroke if it is simple (no Expression) to 
     * avoid new calculation for each call of getStroke(Feature feature) 
     */
    private void extractSimpleDasharray() throws FilterEvaluationException {
        CssParameter cssParam = (CssParameter)cssParams.get( "stroke-dasharray" );
        if ( cssParam != null ) {
            if ( isSimple( cssParam ) ) {
                Object[] o = cssParam.getValue().getComponents();            
                String value = (String)o[0];
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
                        throw new FilterEvaluationException( 
                                "List of values for parameter 'stroke-dashoffset' " + 
                                "contains an invalid token: '" + s + "'!" );
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
    }
    
    /**
     * extracts the dash offset of the stroke if it is simple (no Expression) to 
     * avoid new calculation for each call of getStroke(Feature feature) 
     */
    private void extractSimpleDashOffset() throws FilterEvaluationException {
        CssParameter cssParam = (CssParameter)cssParams.get( "stroke-dashoffset" );
        if ( cssParam != null ) {
            if ( isSimple( cssParam ) ) {
                Object[] o = cssParam.getValue().getComponents();
                String value = (String)o[0];
                try {
                    smplDashOffset = Float.parseFloat( value );
                } catch ( NumberFormatException e ) {
                    throw new FilterEvaluationException( 
                            "Given value for parameter 'stroke-dashoffset' ('" + value + 
                            "') has invalid format!" );
                }
            }
        }
    }

   /**
    * The GraphicStroke element both indicates that a repeated-linear-graphic
    * stroke type will be used.
    * <p>
    * @returns the underlying <tt>GraphicStroke</tt> instance (may be null)
    */
    public GraphicStroke getGraphicStroke() {
        return graphicStroke;
    }
    
    /**
     * The GraphicStroke element both indicates that a repeated-linear-graphic
     * stroke type will be used.
     * @param graphicStroke the graphicStroke element 
     * <p>
     */
    public void setGraphicStroke(GraphicStroke graphicStroke) {
            this.graphicStroke = graphicStroke;
    }

    /**
     * The stroke CssParameter element gives the solid color that will be used
     * for a stroke. The color value is RGB-encoded using two hexadecimal digits
     * per primary-color component, in the order Red, Green, Blue, prefixed with
     * a hash (#) sign. The hexadecimal digits between A and F may be in either
     * uppercase or lowercase. For example, full red is encoded as #ff0000 (with
     * no quotation marks). The default color is defined to be black (#000000) in
     * the context of the LineSymbolizer, if the stroke CssParameter element is
     * absent.
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails
     */
    public Color getStroke(Feature feature) throws FilterEvaluationException {        
        Color awtColor = COLOR_DEFAULT;

        if ( color == null ) {
            // evaluate color depending on the passed feature's properties
            CssParameter cssParam = (CssParameter)cssParams.get( "stroke" );

            if ( cssParam != null ) {
                String s = cssParam.getValue( feature );

                try {
                    awtColor = Color.decode( s );
                } catch ( NumberFormatException e ) {
                    throw new FilterEvaluationException( "Given value ('" + s + 
                                                         "') for CSS-Parameter 'stroke' " + 
                                                         "does not denote a valid color!" );
                }
            }
        } else {
            awtColor = color;
        }

        return awtColor;
    }

   /**
    * @see getStroke
    * <p>
    * @param stroke the stroke to be set
    */
    public void setStroke (Color stroke)  {
        this.color = stroke;
        CssParameter strokeColor = 
            StyleFactory.createCssParameter( "stroke", StyleFactory.getColorAsHex( stroke ) );
        cssParams.put( "stroke", strokeColor);
    }
           
    /**
     * The stroke-opacity CssParameter element specifies the level of translucency
     * to use when rendering the stroke. The value is encoded as a floating-point
     * value (float) between 0.0 and 1.0 with 0.0 representing completely
     * transparent and 1.0 representing completely opaque, with a linear scale of
     * translucency for intermediate values. For example, 0.65 would represent 65%
     * opacity. The default value is 1.0 (opaque).
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails
     */
    public double getOpacity( Feature feature ) throws FilterEvaluationException {
        double opacity = OPACITY_DEFAULT;

        if ( smplOpacity < 0 ) {
            CssParameter cssParam = (CssParameter)cssParams.get( "stroke-opacity" );

            if ( cssParam != null ) {
                // evaluate opacity depending on the passed feature's properties
                String value = cssParam.getValue( feature );

                try {
                    opacity = Double.parseDouble( value );
                } catch ( NumberFormatException e ) {
                    throw new FilterEvaluationException( 
                            "Given value for parameter 'stroke-opacity' ('" + value + 
                            "') has invalid format!" );
                }

                if ( ( opacity < 0.0 ) || ( opacity > 1.0 ) ) {
                    throw new FilterEvaluationException( 
                            "Value for parameter 'stroke-opacity' (given: '" + value + 
                            "') must be between 0.0 and 1.0!" );
                }
            }
        } else {
            opacity = smplOpacity;
        }

        return opacity;
    }
    
   /**
    * @see getOpacity
    * <p>
    * @param opacity the opacity to be set for the stroke
    */
    public void setOpacity (double opacity) {
        if ( opacity > 1 ) {
            opacity = 1;
        } else if ( opacity < 0 ) {
            opacity = 0;
        }
        this.smplOpacity = opacity;
        CssParameter strokeOp = StyleFactory.createCssParameter( "stroke-opacity", "" + opacity );
        cssParams.put( "stroke-opacity", strokeOp);
    }

    /**
     * The stroke-width CssParameter element gives the absolute width (thickness)
     * of a stroke in pixels encoded as a float. (Arguably, more units could be
     * provided for encoding sizes, such as millimeters or typesetter's points.)
     * The default is 1.0. Fractional numbers are allowed (with a system-dependent
     * interpretation) but negative numbers are not.
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails
     */
    public double getWidth( Feature feature ) throws FilterEvaluationException {
        double width = WIDTH_DEFAULT;

        if ( smplWidth < 0 ) {
            // evaluate smplWidth depending on the passed feature's properties
            CssParameter cssParam = (CssParameter)cssParams.get( "stroke-width" );

            if ( cssParam != null ) {
                String value = cssParam.getValue( feature );

                try {
                    width = Double.parseDouble( value );
                } catch ( NumberFormatException e ) {
                    throw new FilterEvaluationException( "Given value for parameter 'stroke-width' ('" + 
                                                         value + "') has invalid format!" );
                }

                if ( width <= 0.0 ) {
                    throw new FilterEvaluationException( "Value for parameter 'stroke-width' (given: '" + 
                                                         value + "') must be greater than 0!" );
                }
            }
        } else {
            width = smplWidth;
        }

        return width;
    }
    
    public Expression getWidthAsExpression(Feature feature) throws FilterEvaluationException{

		Expression widthAsExpression = null;
		CssParameter cssParam = (CssParameter)cssParams.get( "stroke-width" );
		if (cssParam != null) {
			if (!isSimple(cssParam)) {
				widthAsExpression = (Expression)cssParam.getValue().getComponents()[0]; 
			}	
		}
		return widthAsExpression;
    }
    
   /**
    * @see getWidth
    * <p>
    * @param width the width to be set for the stroke
    */
    public void setWidth (double width)  {
        if ( width <= 0 ) width = 1;
        this.smplWidth = width;        
        CssParameter strokeWi = StyleFactory.createCssParameter( "stroke-width", "" + width );
        cssParams.put( "stroke-width", strokeWi );
    }

    /** 
     * The stroke-linejoin CssParameter element encode enumerated values telling
     * how line strings should be joined (between line segments). The values are
     * represented as content strings. The allowed values for line join are mitre,
     * round, and bevel.
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails
     */
    public int getLineJoin( Feature feature ) throws FilterEvaluationException {
        int lineJoin = LJ_DEFAULT;

        if ( smplLineJoin < 0 ) {
            CssParameter cssParam = (CssParameter)cssParams.get( "stroke-linejoin" );

            if ( cssParam != null ) {
                String value = cssParam.getValue( feature );

                if ( value.equals( "mitre" ) ) {
                    lineJoin = Stroke.LJ_MITRE;
                } else if ( value.equals( "round" ) ) {
                    lineJoin = Stroke.LJ_ROUND;
                } else if ( value.equals( "bevel" ) ) {
                    lineJoin = Stroke.LJ_BEVEL;
                } else {
                    throw new FilterEvaluationException( 
                            "Given value for parameter 'stroke-linejoin' ('" + value + 
                            "') is unsupported. Supported values are: " + 
                            "'mitre', 'round' or 'bevel'!" );
                }
            }
        } else {
            lineJoin = smplLineJoin;
        }

        return lineJoin;
    }
    
   /**
    * @see getLineJoin
    * <p>
    * @param lineJoin the lineJoin to be set for the stroke
    */
    public void setLineJoin (int lineJoin) {
        String join = null;
        if (lineJoin == Stroke.LJ_MITRE) { 
            join = "mitre";            
        } else if (lineJoin == Stroke.LJ_ROUND)  {
            join = "round";
        } else if (lineJoin == Stroke.LJ_BEVEL)   {
            join = "bevel";
        } else {
            // default
            lineJoin = Stroke.LJ_BEVEL;
            join = "bevel";
        }
        smplLineJoin = lineJoin;
        CssParameter strokeLJ = StyleFactory.createCssParameter( "stroke-linejoin", join );
        cssParams.put( "stroke-linejoin", strokeLJ );
    }

    /**
     * Thestroke-linecap CssParameter element encode enumerated values telling
     * how line strings should be capped (at the two ends of the line string).
     * The values are represented as content strings. The allowed values for line
     * cap are butt, round, and square. The default values are system-dependent.
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails
     */
    public int getLineCap( Feature feature ) throws FilterEvaluationException {
        int lineCap = LC_DEFAULT;

        CssParameter cssParam = (CssParameter)cssParams.get( "stroke-linecap" );

        if ( cssParam != null ) {
            String value = cssParam.getValue( feature );

            if ( value.equals( "butt" ) ) {
                lineCap = Stroke.LC_BUTT;
            } else if ( value.equals( "round" ) ) {
                lineCap = Stroke.LC_ROUND;
            } else if ( value.equals( "square" ) ) {
                lineCap = Stroke.LC_SQUARE;
            } else {
                throw new FilterEvaluationException( 
                        "Given value for parameter 'stroke-linecap' ('" + value + 
                        "') is unsupported. Supported values are: " + 
                        "'butt', 'round' or 'square'!" );
            }
        }

        return lineCap;
    }
    
   /**
    * @see getLineCap
    * <p>
    * @param lineCap lineCap to be set for the stroke
    */
    public void setLineCap (int lineCap) {
        String cap = null;
        if (lineCap == Stroke.LC_BUTT) { 
            cap = "butt";
        } else if (lineCap == Stroke.LC_ROUND)  {
            cap = "round";
        } else if (lineCap == Stroke.LC_SQUARE)   {
            cap = "square";
        } else {
            // default;
            cap = "round";
            lineCap = Stroke.LC_SQUARE;
        }
        smplLineCap = lineCap;
        CssParameter strokeCap = StyleFactory.createCssParameter( "stroke-linejoin", cap );
        cssParams.put( "stroke-linecap", strokeCap );
    }
     
    /**
     * Evaluates the 'stroke-dasharray' parameter as defined in OGC 02-070.
     * The stroke-dasharray CssParameter element encodes a dash pattern as a
     * series of space separated floats. The first number gives the length in
     * pixels of dash to draw, the second gives the amount of space to leave, and
     * this pattern repeats. If an odd number of values is given, then the pattern
     * is expanded by repeating it twice to give an even number of values. Decimal
     * values have a system-dependent interpretation (usually depending on whether
     * antialiasing is being used). The default is to draw an unbroken line.
     * <p>
     * @param value the encoded pattern
     * @throws FilterEvaluationException if the eevaluation fails or the
     *         encoded pattern is erroneous
     * @return the decoded pattern as an array of float-values (null if the
     *         parameter was not specified)
     */
    public float[] getDashArray( Feature feature ) throws FilterEvaluationException {
        CssParameter cssParam = (CssParameter)cssParams.get( "stroke-dasharray" );

        float[] dashArray = null;
        if ( smplDashArray == null ) {
            if ( cssParam == null ) {
                return null;
            }

            String value = cssParam.getValue( feature );

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
                    throw new FilterEvaluationException( 
                            "List of values for parameter 'stroke-dashoffset' " + 
                            "contains an invalid token: '" + s + "'!" );
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
    * @see getDashArray
    * <p>
    * @param dashArray the dashArray to be set for the Stroke
    */ 
    public void setDashArray ( float[] dashArray)  {
        if ( dashArray != null ) {
            String s = "";
            for (int i = 0; i < dashArray.length-1; i++) {
                s = s + dashArray[i] + ",";                
        }
        s = s + dashArray[dashArray.length-1];
        smplDashArray = dashArray;
        CssParameter strokeDash = StyleFactory.createCssParameter( "stroke-dasharray", s );
        cssParams.put( "stroke-dasharray", strokeDash );
        }
    }
    
    /**
     * The stroke-dashoffset CssParameter element specifies the distance as a
     * float into the stroke-dasharray pattern at which to start drawing.
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails
     */
    public float getDashOffset( Feature feature ) throws FilterEvaluationException {
        float dashOffset = 0;

        if ( smplDashOffset < 0 ) {
            CssParameter cssParam = (CssParameter)cssParams.get( "stroke-dashoffset" );
            if ( cssParam != null ) {
                String value = cssParam.getValue( feature );

                try {
                    dashOffset = Float.parseFloat( value );
                } catch ( NumberFormatException e ) {
                    throw new FilterEvaluationException( 
                            "Given value for parameter 'stroke-dashoffset' ('" + value + 
                            "') has invalid format!" );
                }
            }
        } else {
            dashOffset = smplDashOffset;
        }

        return dashOffset;
    }
    
   /**
    * The stroke-dashoffset CssParameter element specifies the distance as a
    * float into the stroke-dasharray pattern at which to start drawing.
    * <p>
    * @param dashOffset the dashOffset to be set for the Stroke
    */
    public void setDashOffset (float dashOffset) {
        if ( dashOffset < 0 ) dashOffset = 0;
        smplDashOffset = dashOffset;
        CssParameter strokeDashOff = StyleFactory.createCssParameter( "stroke-dashoffset", "" + dashOffset );
        cssParams.put( "stroke-dashoffset", strokeDashOff );
    }
     
    /**
     * exports the content of the Stroke as XML formated String
     *
     * @return xml representation of the Stroke
     */
    public String exportAsXML() {
        Debug.debugMethodBegin();
        
        StringBuffer sb = new StringBuffer(1000);
        sb.append( "<Stroke>" );
        
        if ( graphicFill != null ) {
            sb.append( ((Marshallable)graphicFill).exportAsXML() );
        } else  if ( graphicStroke != null ) {
            sb.append( ((Marshallable)graphicStroke).exportAsXML() );
        }
        Iterator iterator = cssParams.values().iterator();
        while ( iterator.hasNext() ) {
            sb.append( ((Marshallable)iterator.next()).exportAsXML() );
        }
        
        sb.append( "</Stroke>" );
        
        Debug.debugMethodEnd();
        return sb.toString();
    }

    
}