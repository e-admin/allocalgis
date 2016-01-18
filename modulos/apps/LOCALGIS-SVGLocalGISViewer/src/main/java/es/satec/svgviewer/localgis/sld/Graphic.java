/**
 * Graphic.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

import java.util.Vector;

import org.deegree.model.filterencoding.FilterEvaluationException;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * A Graphic is a "graphic symbol" with an inherent shape, color, and size. Graphics can either be
 * referenced from an external URL in a common format (such as GIF or SVG) or may be derived from a
 * Mark. Multiple external URLs may be referenced with the semantic that they all provide the same
 * graphic in different formats. The "hot spot" to use for rendering at a point or the start and
 * finish handle points to use for rendering a graphic along a line must either be inherent in the
 * external format or are system- dependent. The default size of an image format (such as GIF) is
 * the inherent size of the image. The default size of a format without an inherent size is 16
 * pixels in height and the corresponding aspect in width. If a size is specified, the height of the
 * graphic will be scaled to that size and the corresponding aspect will be used for the width. The
 * default if neither an ExternalURL nor a Mark is specified is to use the default Mark with a size
 * of 6 pixels. The size is in pixels and the rotation is in degrees clockwise, with 0 (default)
 * meaning no rotation. In the case that a Graphic is derived from a font-glyph Mark, the Size
 * specified here will be used for the final rendering. Allowed CssParameters are "opacity", "size",
 * and "rotation".
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: satec $
 * 
 * @version. $Revision: 1.2 $, $Date: 2011/12/20 09:35:31 $
 */
public class Graphic {

	// default values
	public static final double OPACITY_DEFAULT = 1.0;

	public static final double SIZE_DEFAULT = -1;

	public static final double ROTATION_DEFAULT = 0.0;

	private Vector marksAndExtGraphics = new Vector();

	private Image image = null;

	private double size = SIZE_DEFAULT;

	/**
	 * Creates a new <tt>Graphic</tt> instance.
	 * <p>
	 * 
	 * @param marksAndExtGraphics
	 *            the image will be based upon these
	 * @param opacity
	 *            opacity that the resulting image will have
	 * @param size
	 *            image height will be scaled to this value, respecting the proportions
	 * @param rotation
	 *            image will be rotated clockwise for positive values, negative values result in
	 *            anti-clockwise rotation
	 */
	protected Graphic( Object[] marksAndExtGraphics, double size) {
		setMarksAndExtGraphics( marksAndExtGraphics );
		this.size = size;
	}

	/**
	 * Creates a new <tt>Graphic</tt> instance based on the default <tt>Mark</tt>: a square.
	 * <p>
	 * 
	 * @param opacity
	 *            opacity that the resulting image will have
	 * @param size
	 *            image height will be scaled to this value, respecting the proportions
	 * @param rotation
	 *            image will be rotated clockwise for positive values, negative values result in
	 *            anti-clockwise rotation
	 */
	protected Graphic(double size) {
		Mark[] marks = new Mark[1];
		marks[0] = new Mark( "square", null, null );
		setMarksAndExtGraphics( marks );
		this.size = size;
	}

	public double getSize() {
		return size;
	}

	/**
	 * Creates a new <tt>Graphic</tt> instance based on the default <tt>Mark</tt>: a square.
	 */
	protected Graphic() {
		this(null, SIZE_DEFAULT);
	}

	/**
	 * Returns an object-array that enables the access to the stored <tt>ExternalGraphic</tt> and
	 * <tt>Mark</tt> -instances.
	 * <p>
	 * 
	 * @return contains <tt>ExternalGraphic</tt> and <tt>Mark</tt> -objects
	 * 
	 */
	public Object[] getMarksAndExtGraphics() {
		Object[] objects = new Object[marksAndExtGraphics.size()];
		marksAndExtGraphics.copyInto( objects );
		return objects;
	}

	/**
	 * Sets the <tt>ExternalGraphic</tt>/ <tt>Mark<tt>-instances that the image
	 * will be based on.
	 * <p>
	 * @param object to be used as basis for the resulting image
	 */
	public void setMarksAndExtGraphics( Object[] object ) {
		image = null;
		this.marksAndExtGraphics.removeAllElements();

		if ( object != null ) {
			for ( int i = 0; i < object.length; i++ ) {
				marksAndExtGraphics.addElement( object[i] );
			}
		}
	}

	/**
	 * Adds an Object to an object-array that enables the access to the stored
	 * <tt>ExternalGraphic</tt> and <tt>Mark</tt> -instances.
	 * <p>
	 * 
	 * @param object
	 *            to be used as basis for the resulting image
	 */
	public void addMarksAndExtGraphic( Object object ) {
		marksAndExtGraphics.addElement( object );
	}

	/**
	 * Removes an Object from an object-array that enables the access to the stored
	 * <tt>ExternalGraphic</tt> and <tt>Mark</tt> -instances.
	 * <p>
	 * 
	 * @param object
	 *            to be used as basis for the resulting image
	 */
	public void removeMarksAndExtGraphic( Object object ) {
		marksAndExtGraphics.removeElementAt( marksAndExtGraphics.indexOf( object ) );
	}

	public void setSize( double size ) {
		this.size = size;
	}

	/**
	 * Returns a <tt>BufferedImage</tt> representing this object. The image respects the
	 * 'Opacity', 'Size' and 'Rotation' parameters. If the 'Size'-parameter is omitted, the height
	 * of the first <tt>ExternalGraphic</tt> is used. If there is none, the default value of 6
	 * pixels is used.
	 * <p>
	 * 
	 * @return the <tt>BufferedImage</tt> ready to be painted
	 * @throws FilterEvaluationException
	 *             if the evaluation fails
	 */
	public Image getAsImage() {
		int intSizeX = (int) size;
		int intSizeY = intSizeX;

		// calculate the size of the first ExternalGraphic
		int intSizeImgX = -1;
		int intSizeImgY = -1;
		for ( int i = 0; i < marksAndExtGraphics.size(); i++ ) {
			Object o = marksAndExtGraphics.elementAt( i );
			if ( o instanceof ExternalGraphic ) {
				Image extImage = ( (ExternalGraphic) o ).getAsImage(intSizeX, intSizeY);
				intSizeImgX = extImage.getImageData().width;
				intSizeImgY = extImage.getImageData().height;
				break;
			}
		}

		if ( intSizeX < 0 ) {
			// if size is unspecified
			if ( intSizeImgX < 0 ) {
				// if there are no ExternalGraphics, use default value of 6 pixels
				intSizeX = 6;
				intSizeY = 6;
			} else {
				// if there are ExternalGraphics, use width and height of the first
				intSizeX = intSizeImgX;
				intSizeY = intSizeImgY;
			}
		} else {
			// if size is specified
			if ( intSizeImgX < 0 ) {
				// if there are no ExternalGraphics, use default intSizeX
				intSizeY = intSizeX;
			} else {
				// if there are ExternalGraphics, use the first to find the height
				intSizeY = (int) Math.round( ( ( (double) intSizeImgY ) / ( (double) intSizeImgX ) ) * intSizeX );
			}
		}

		image = new Image(Display.getCurrent(), intSizeX, intSizeY);

		GC gc = new GC(image);
		
		for ( int i = 0; i < marksAndExtGraphics.size(); i++ ) {
			Object o = marksAndExtGraphics.elementAt( i );
			Image extImage = null;

			if ( o instanceof ExternalGraphic ) {
				extImage = ( (ExternalGraphic) o ).getAsImage( intSizeX, intSizeY);
			} else {
				extImage = ( (Mark) o ).getAsImage(intSizeX );
			}

			gc.drawImage( extImage, 0, 0, extImage.getImageData().width, extImage.getImageData().height, 0, 0, intSizeX, intSizeY);
		}

		// use the default Mark if there are no Marks / ExternalGraphics
		// specified at all
		if ( marksAndExtGraphics.size() == 0 ) {
			Mark mark = new Mark();
			Image extImage = mark.getAsImage(intSizeX );
			gc.drawImage( extImage, 0, 0, extImage.getImageData().width, extImage.getImageData().height, 0, 0, intSizeX, intSizeY);
		}
		gc.dispose();

		return image;
	}

	/**
	 * Sets a <tt>BufferedImage</tt> representing this object. The image respects the 'Opacity',
	 * 'Size' and 'Rotation' parameters.
	 * <p>
	 * 
	 * @param bufferedImage
	 *            BufferedImage to be set
	 */
	public void setAsImage( Image image ) {
		this.image = image;
	}

	public void paint(GC gc, int x, int y) {
		int intSizeX = (int) size;
		int intSizeY = intSizeX;

		// calculate the size of the first ExternalGraphic
		int intSizeImgX = -1;
		int intSizeImgY = -1;
		for ( int i = 0; i < marksAndExtGraphics.size(); i++ ) {
			Object o = marksAndExtGraphics.elementAt( i );
			if ( o instanceof ExternalGraphic ) {
				Image extImage = ( (ExternalGraphic) o ).getAsImage(intSizeX, intSizeY);
				if (extImage != null && !extImage.isDisposed()) {
					intSizeImgX = extImage.getImageData().width;
					intSizeImgY = extImage.getImageData().height;
					break;
				}
			}
		}

		if ( intSizeX < 0 ) {
			// if size is unspecified
			if ( intSizeImgX < 0 ) {
				// if there are no ExternalGraphics, use default value of 6 pixels
				intSizeX = 6;
				intSizeY = 6;
			} else {
				// if there are ExternalGraphics, use width and height of the first
				intSizeX = intSizeImgX;
				intSizeY = intSizeImgY;
			}
		} else {
			// if size is specified
			if ( intSizeImgX < 0 ) {
				// if there are no ExternalGraphics, use default intSizeX
				intSizeY = intSizeX;
			} else {
				// if there are ExternalGraphics, use the first to find the height
				intSizeY = (int) Math.round( ( ( (double) intSizeImgY ) / ( (double) intSizeImgX ) ) * intSizeX );
			}
		}

		for ( int i = 0; i < marksAndExtGraphics.size(); i++ ) {
			Object o = marksAndExtGraphics.elementAt( i );
			Image extImage = null;

			if ( o instanceof ExternalGraphic ) {
				extImage = ( (ExternalGraphic) o ).getAsImage( intSizeX, intSizeY);
				if (extImage != null && !extImage.isDisposed()) {
					gc.drawImage( extImage, 0, 0, extImage.getImageData().width, extImage.getImageData().height, x-intSizeX/2, y-intSizeY/2, intSizeX, intSizeY);
				}
			} else {
				( (Mark) o ).paint(gc, x, y, intSizeX);
			}

		}

		// use the default Mark if there are no Marks / ExternalGraphics
		// specified at all
		if ( marksAndExtGraphics.size() == 0 ) {
			Mark mark = new Mark();
			mark.paint(gc, x, y, intSizeX );
		}

	}
	public void dispose(){
		if ((image!=null) && (!image.isDisposed()))
			image.dispose();
		
		for ( int i = 0; i < marksAndExtGraphics.size(); i++ ) {
			Object o = marksAndExtGraphics.elementAt( i );
			if ( o instanceof ExternalGraphic ) {
				Image image=((ExternalGraphic) o).getImage();
				if ((image!=null) && (!image.isDisposed()))
						image.dispose();
			}
		}
	}
}
