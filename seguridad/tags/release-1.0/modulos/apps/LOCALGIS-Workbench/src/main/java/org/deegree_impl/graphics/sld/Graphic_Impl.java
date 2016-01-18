
package org.deegree_impl.graphics.sld;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.deegree.graphics.sld.ExternalGraphic;
import org.deegree.graphics.sld.Graphic;
import org.deegree.graphics.sld.Mark;
import org.deegree.graphics.sld.ParameterValueType;
import org.deegree.model.feature.Feature;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree.xml.Marshallable;
import org.deegree_impl.tools.Debug;


/**
 * A Graphic is a "graphic symbol" with an inherent shape, color, and size.
 * Graphics can either be referenced from an external URL in a common format
 * (such as GIF or SVG) or may be derived from a Mark. Multiple external URLs
 * may be referenced with the semantic that they all provide the same graphic in
 * different formats. The "hot spot" to use for rendering at a point or the
 * start and finish handle points to use for rendering a graphic along a line
 * must either be inherent in the external format or are system- dependent. The
 * default size of an image format (such as GIF) is the inherent size of the
 * image. The default size of a format without an inherent size is 16 pixels in
 * height and the corresponding aspect in width. If a size is specified, the
 * height of the graphic will be scaled to that size and the corresponding aspect
 * will be used for the width. The default if neither an ExternalURL nor a Mark
 * is specified is to use the default Mark with a size of 6 pixels. The size is
 * in pixels and the rotation is in degrees clockwise, with 0 (default) meaning
 * no rotation. In the case that a Graphic is derived from a font-glyph Mark, the
 * Size specified here will be used for the final rendering. Allowed
 * CssParameters are "opacity", "size", and "rotation".
 * <p>
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/03 12:31:54 $
 */
public class Graphic_Impl implements Graphic, Marshallable {
    private ArrayList marksAndExtGraphics = new ArrayList();
    private BufferedImage image = null;
    private ParameterValueType opacity = null;
    private ParameterValueType rotation = null;
    private ParameterValueType size = null;

    /**
     * Creates a new <tt>Graphic_Impl</tt> instance.
     * <p>
     * @param marksAndExtGraphics the image will be based upon these
     * @param opacity opacity that the resulting image will have
     * @param size image height will be scaled to this value, respecting
     *        the proportions
     * @param rotation image will be rotated clockwise for positive values,
     *        negative values result in anti-clockwise rotation
     */
    protected Graphic_Impl( Object[] marksAndExtGraphics, ParameterValueType opacity, 
                            ParameterValueType size, ParameterValueType rotation ) {
        setMarksAndExtGraphics( marksAndExtGraphics );
        this.opacity = opacity;
        this.size = size;
        this.rotation = rotation;
    }

    /**
     * Creates a new <tt>Graphic_Impl</tt> instance based on the 
     * default <tt>Mark</tt>: a square.
     * <p>
     * @param opacity opacity that the resulting image will have
     * @param size image height will be scaled to this value, respecting
     *        the proportions
     * @param rotation image will be rotated clockwise for positive values,
     *        negative values result in anti-clockwise rotation
     */
    protected Graphic_Impl( ParameterValueType opacity, ParameterValueType size, 
                            ParameterValueType rotation ) {
        Mark[] marks = new Mark[1];
        marks[0] = new Mark_Impl( "square", null, null );
        setMarksAndExtGraphics( marks );
        this.opacity = opacity;
        this.size = size;
        this.rotation = rotation;
    }

    /**
     * Creates a new <tt>Graphic_Impl</tt> instance based on the 
     * default <tt>Mark</tt>: a square.
     */
    protected Graphic_Impl() {
        this( null, null, null );
    }

    /**
     * Returns an object-array that enables the access to the stored
     * <tt>ExternalGraphic</tt> and <tt>Mark</tt>-instances.
     * <p>
     * @return contains <tt>ExternalGraphic</tt> and <tt>Mark</tt>-objects
     */
    public Object[] getMarksAndExtGraphics() {
        Object[] objects = new Object[marksAndExtGraphics.size()];
        return (Object[])marksAndExtGraphics.toArray( objects );
    }

    /**
     * Sets the <tt>ExternalGraphic</tt>/<tt>Mark<tt>-instances that the image
     * will be based on.
     * <p>
     * @param object to be used as basis for the resulting image
     */
    public void setMarksAndExtGraphics( Object[] object ) {
        image = null;
        this.marksAndExtGraphics.clear();

        if ( object != null ) {
            for ( int i = 0; i < object.length; i++ ) {
                marksAndExtGraphics.add( object[i] );
            }
        }
    }

    /**
     * Adds an Object to an object-array that enables the access to the stored
     * <tt>ExternalGraphic</tt> and <tt>Mark</tt>-instances.
     * <p>
     * @param object to be used as basis for the resulting image
     */
    public void addMarksAndExtGraphic (Object object) {
        marksAndExtGraphics.add( object );
    }
    
    /**
     * Removes an Object from an object-array that enables the access to the stored
     * <tt>ExternalGraphic</tt> and <tt>Mark</tt>-instances.
     * <p>
     * @param object to be used as basis for the resulting image
     */
    public void removeMarksAndExtGraphic (Object object) {
        marksAndExtGraphics.remove( marksAndExtGraphics.indexOf(object) );
    }
    
    /**
     * The Opacity element gives the opacity to use for rendering the graphic.
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails or the value
     *         is invalid
     */
    public double getOpacity( Feature feature ) throws FilterEvaluationException {
        double opacityVal = OPACITY_DEFAULT;

        if ( opacity != null ) {
            String value = opacity.evaluate( feature );

            try {
                opacityVal = Double.parseDouble( value );
            } catch ( NumberFormatException e ) {
                throw new FilterEvaluationException( "Given value for parameter 'opacity' ('" + 
                                                     value + "') has invalid format!" );
            }

            if ( ( opacityVal < 0.0 ) || ( opacityVal > 1.0 ) ) {
                throw new FilterEvaluationException( "Value for parameter 'opacity' (given: '" + 
                                                     value + "') must be between 0.0 and 1.0!" );
            }
        }

        return opacityVal;
    }
    
    public ParameterValueType getOpacity() {
    	return opacity;
    }
    
   /**
    * The Opacity element gives the opacity of to use for rendering the graphic.
    * <p>
    * @param opacity Opacity to be set for the graphic
    */
    public void setOpacity (double opacity)  {
        ParameterValueType pvt = null;
        pvt = StyleFactory.createParameterValueType( "" + opacity );
        this.opacity = pvt; 
    }

    /**
     * The Size element gives the absolute size of the graphic in pixels encoded
     * as a floating-point number. This element is also used in other contexts
     * than graphic size and pixel units are still used even for font size. The
     * default size for an object is context-dependent. Negative values are not
     * allowed.
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails or the value
     *         is invalid
     */
    public double getSize( Feature feature ) throws FilterEvaluationException {
        double sizeVal = SIZE_DEFAULT;

        if ( size != null ) {
            String value = size.evaluate( feature );

            try {
                sizeVal = Double.parseDouble( value );
            } catch ( NumberFormatException e ) {
                throw new FilterEvaluationException( "Given value for parameter 'size' ('" + 
                                                     value + "') has invalid format!" );
            }

            if ( sizeVal <= 0.0 ) {
                throw new FilterEvaluationException( "Value for parameter 'size' (given: '" + 
                                                     value + "') must be greater than 0!" );
            }
        }

        return sizeVal;
    }
    
    public ParameterValueType getSize() {
    	return size;
    }
    
   /**
    * @see getSize
    * <p>
    * @param size size to be set for the graphic
    */
    public void setSize (double size)  {
        ParameterValueType pvt = null;
        pvt = StyleFactory.createParameterValueType( "" + size );
        this.size = pvt; 
    }

	public void setSize (ParameterValueType size)  {
	
		this.size = size; 
	}

    /**
     * The Rotation element gives the rotation of a graphic in the clockwise
     * direction about its center point in radian, encoded as a floating-
     * point number. Negative values mean counter-clockwise rotation. The default
     * value is 0.0 (no rotation).
     * <p>
     * @param feature specifies the <tt>Feature</tt> to be used for evaluation
     *        of the underlying 'sld:ParameterValueType'
     * @return the (evaluated) value of the parameter
     * @throws FilterEvaluationException if the evaluation fails or the value
     *         is invalid
     */
    public double getRotation( Feature feature ) throws FilterEvaluationException {
        double rotVal = ROTATION_DEFAULT;

        if ( rotation != null ) {
            String value = rotation.evaluate( feature );

            try {
                rotVal = Double.parseDouble( value );
            } catch ( NumberFormatException e ) {
                throw new FilterEvaluationException( "Given value for parameter 'rotation' ('" + 
                                                     value + "') has invalid format!" );
            }
        }

        return rotVal;
    }
    
    public ParameterValueType getRotation() {
    	return rotation;
    }

   /**
    * @see getRotation
    * <p>
    * @param rotation rotation to be set for the graphic
    */
    public void setRotation (double rotation)  {
        ParameterValueType pvt = null;
        pvt = StyleFactory.createParameterValueType( "" + rotation );
        this.rotation = pvt;      
    }
    
    /**
     * Returns a <tt>BufferedImage</tt> representing this object.
     * The image respects the 'Opacity', 'Size' and 'Rotation' parameters.
     * If the 'Size'-parameter is omitted, the height of the first
     * <tt>ExternalGraphic</tt> is used. If there is none, the default value
     * of 6 pixels is used.
     * <p>
     * @return the <tt>BufferedImage</tt> ready to be painted
     * @throws FilterEvaluationException if the evaluation fails
     */
    public BufferedImage getAsImage( Feature feature ) throws FilterEvaluationException {
        int intSize = (int)getSize( feature );

        // if size is unspecified, use the height of the first ExternalGraphic
        if ( intSize < 0 ) {
            for ( int i = 0; i < marksAndExtGraphics.size(); i++ ) {
                Object o = marksAndExtGraphics.get( i );
                BufferedImage extImage = null;

                if ( o instanceof ExternalGraphic ) {
                    extImage = ( (ExternalGraphic)o ).getAsImage();
                    intSize = extImage.getHeight();
                    break;
                }
            }
        }

        // if there is none, use default value of 6 pixels
        if ( intSize < 0 ) {
            intSize = 6;
        } 

        image = new BufferedImage( intSize, intSize, BufferedImage.TYPE_INT_ARGB );

        Graphics2D g = (Graphics2D)image.getGraphics();
        g.rotate(Math.toRadians(getRotation(feature))/*2 * Math.PI * getRotation( feature )*/, intSize >> 1, intSize >> 1 );

        for ( int i = 0; i < marksAndExtGraphics.size(); i++ ) {
            Object o = marksAndExtGraphics.get( i );
            BufferedImage extImage = null;

            if ( o instanceof ExternalGraphic ) {
                extImage = ( (ExternalGraphic)o ).getAsImage();
            } else {
                extImage = ( (Mark)o ).getAsImage( feature, intSize );
            }

            g.drawImage( extImage, 0, 0, intSize, intSize, null );
        }

        // use the default Mark if there are no Marks / ExternalGraphics
        // specified at all
        if ( marksAndExtGraphics.size() == 0 ) {
            Mark mark = new Mark_Impl();
            BufferedImage extImage = mark.getAsImage( feature, intSize );
            g.drawImage( extImage, 0, 0, intSize, intSize, null );
        }

        return image;
    }
 
    /**
     * Sets a <tt>BufferedImage</tt> representing this object.
     * The image respects the 'Opacity', 'Size' and 'Rotation' parameters.
     * <p>
     * @param bufferedImage BufferedImage to be set
     */
    public void setAsImage (BufferedImage bufferedImage) {
        image = bufferedImage;
    }
    
    /**
     * exports the content of the Graphic as XML formated String
     *
     * @return xml representation of the Graphic
     */
    public String exportAsXML() {
        Debug.debugMethodBegin();
        
        StringBuffer sb = new StringBuffer(1000);
        sb.append( "<Graphic>" );
        for (int i = 0; i < marksAndExtGraphics.size(); i++) {
            sb.append( ((Marshallable)marksAndExtGraphics.get( i ) ).exportAsXML() );
        }
        if ( opacity != null ) {
            sb.append("<Opacity>");
            sb.append( ((Marshallable)opacity).exportAsXML() );
            sb.append("</Opacity>");
        }
        if ( size != null ) {
            sb.append("<Size>");
            sb.append( ((Marshallable)size).exportAsXML() );
            sb.append("</Size>");
        }
        if ( rotation != null ) {
            sb.append("<Rotation>");
            sb.append( ((Marshallable)rotation).exportAsXML() );
            sb.append("</Rotation>");
        }
        sb.append( "</Graphic>" );
        
        Debug.debugMethodEnd();
        return sb.toString();
    }
    
  
    
}