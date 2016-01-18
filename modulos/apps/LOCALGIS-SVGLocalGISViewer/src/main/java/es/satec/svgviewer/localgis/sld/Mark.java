/**
 * Mark.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

import org.deegree.model.filterencoding.FilterEvaluationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * A Mark takes a "shape" and applies coloring to it. The shape can be derived either from a
 * well-known name (such as "square"), an external URL in various formats (such as, perhaps GIF), or
 * from a glyph of a font. Multiple external formats may be used with the semantic that they all
 * contain the equivalent shape in different formats. If an image format is used that has inherent
 * coloring, the coloring is discarded and only the opacity channel (or equivalent) is used. A Halo,
 * Fill, and/or Stroke is applied as appropriate for the shape's source format.
 * <p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public class Mark {

	private Image image = null;

	private Fill fill = null;

	private String wellKnownName = null;

	private Stroke stroke = null;

	/**
	 * Constructor for the default <tt>Mark</tt>.
	 */
	Mark() {
	}

	/**
	 * constructor initializing the class with the <Mark>
	 */
	Mark( String wellKnownName, Stroke stroke, Fill fill ) {
		setWellKnownName( wellKnownName );
		setStroke( stroke );
		setFill( fill );
	}

	/**
	 * Gives the well known name of a Mark's shape. Allowed values include at least "square",
	 * "circle", "triangle", "star", "cross", and "x", though map servers may draw a different
	 * symbol instead if they don't have a shape for all of these. Renderings of these marks may be
	 * made solid or hollow depending on Fill and Stroke parameters. The default value is "square".
	 * 
	 * @return the WK-Name of the mark
	 * 
	 */
	public String getWellKnownName() {
		return wellKnownName;
	}

	/**
	 * Sets the well known name of a Mark's shape. Allowed values include at least "square",
	 * "circle", "triangle", "star", "cross", and "x", though map servers may draw a different
	 * symbol instead if they don't have a shape for all of these. Renderings of these marks may be
	 * made solid or hollow depending on Fill and Stroke parameters. The default value is "square".
	 * 
	 * @param wellKnownName
	 *            the WK-Name of the mark
	 * 
	 */
	public void setWellKnownName( String wellKnownName ) {
		this.wellKnownName = wellKnownName;
	}

	/**
	 * A Fill allows area geometries to be filled. There are two types of fills: solid-color and
	 * repeated GraphicFill. In general, if a Fill element is omitted in its containing element, no
	 * fill will be rendered. The default is a solid 50%-gray (color "#808080") opaque fill.
	 * 
	 * @return the fill of the mark
	 */
	public Fill getFill() {
		return fill;
	}

	/**
	 * sets the <Fill>
	 * 
	 * @param fill
	 *            the fill of the mark
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
	 * @return the stroke of the mark
	 */
	public Stroke getStroke() {
		return stroke;
	}

	/**
	 * sets <Stroke>
	 * 
	 * @param stroke
	 *            the stroke of the mark
	 */
	public void setStroke( Stroke stroke ) {
		this.stroke = stroke;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param size
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Image getAsImage(int size) {
		double fillOpacity = 1.0;
		double strokeOpacity = 1.0;
		float[] dash = null;
		float dashOffset = 0;
		int cap = SWT.CAP_ROUND;
		int join = SWT.JOIN_ROUND;
		float width = 1;
		Color fillColor = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		Color strokeColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

		if ( fill != null ) {
			fillOpacity = fill.getOpacity();
			fillColor = fill.getFill();
		}

		if ( stroke != null ) {
			strokeOpacity = stroke.getOpacity();
			strokeColor = stroke.getStroke();
			dash = stroke.getDashArray();
			cap = stroke.getLineCap();
			join = stroke.getLineJoin();
			width = (float) stroke.getWidth();
			dashOffset = stroke.getDashOffset();
		}

		if ( wellKnownName == null ) {
			wellKnownName = "square";
		}

		if ( wellKnownName.equalsIgnoreCase( "circle" ) ) {
			image = drawCircleOnImage( size, fillOpacity, fillColor, strokeOpacity, strokeColor, dash, dashOffset, width, cap,
					join );
		} else if ( wellKnownName.equalsIgnoreCase( "triangle" ) ) {
			image = drawTriangleOnImage( size, fillOpacity, fillColor, strokeOpacity, strokeColor, dash, dashOffset, width,
					cap, join );
		} else if ( wellKnownName.equalsIgnoreCase( "cross" ) ) {
			image = drawCross1OnImage( size, strokeOpacity, strokeColor, dash, dashOffset, width, cap, join );
		} else if ( wellKnownName.equalsIgnoreCase( "x" ) ) {
			image = drawCross2OnImage( size, strokeOpacity, strokeColor, dash, dashOffset, width, cap, join );
		} else if ( wellKnownName.equalsIgnoreCase( "star" ) ) {
			image = drawStarOnImage( size, fillOpacity, fillColor, strokeOpacity, strokeColor, dash, dashOffset, width, cap,
					join );
		} else {
			image = drawSquareOnImage( size, fillOpacity, fillColor, strokeOpacity, strokeColor, dash, dashOffset, width, cap,
					join );
		}

		return image;
	}

	/**
	 * Sets the mark as an image. Rhis method is not part of the sld specifications but it is added
	 * to speed up applications.
	 * 
	 * @param bufferedImage
	 *            the bufferedImage to be set for the mark
	 */
	public void setAsImage( Image image ) {
		this.image = image;
	}

	private void setGCDrawingParams(GC gc, float[] dash, float dashOffset, float width, int cap, int join,
			Color strokeColor, double strokeOpacity, Color fillColor, double fillOpacity) {

		gc.setLineWidth((int)width);
		gc.setLineCap(cap);
		gc.setLineJoin(join);
		if ( ( dash != null ) && ( dash.length >= 2 ) ) {
			int[] dash2 = new int[dash.length];
			for (int i=0; i<dash.length; i++) {
				dash2[i] = (int) dash[i];
			}
			gc.setLineDash(dash2);
		}

		if (strokeColor!=null) gc.setForeground(strokeColor);
		if (fillColor!=null) gc.setBackground(fillColor);
	}

	/**
	 * Draws a scaled instance of a triangle mark according to the given parameters.
	 * 
	 * @param size
	 *            resulting image's height and width
	 * @param fillOpacity
	 *            opacity value for the filled parts of the image
	 * @param fillColor
	 *            <tt>Color</tt> to be used for the fill
	 * @param strokeOpacity
	 *            opacity value for the stroked parts of the image
	 * @param strokeColor
	 *            <tt>Color</tt> to be used for the strokes
	 * @param dash
	 *            dash array for rendering boundary line
	 * @param width
	 *            of the boundary line
	 * @param cap
	 *            of the boundary line
	 * @param join
	 *            of the boundary line
	 * @param dashOffset
	 * 
	 * @return image displaying a triangle
	 */
	public Image drawTriangleOnImage( int size, double fillOpacity, Color fillColor, double strokeOpacity,
			Color strokeColor, float[] dash, float dashOffset, float width, int cap, int join ) {

		int offset = (int) ( width * 2 + 1 ) / 2;
		Image image = new Image(Display.getCurrent(), size + offset * 2, size + offset * 2);

		int[] points = new int[6];
		points[0] = offset;
		points[1] = offset;
		points[2] = size / 2 + offset;
		points[3] = size - 1 + offset;
		points[4] = size - 1 + offset;
		points[5] = offset;

		GC gc = new GC(image);
		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, fillColor, fillOpacity );
		gc.fillPolygon(points);
		gc.drawPolygon(points);
		gc.dispose();

		return image;
	}

	/**
	 * Draws a five-pointed star (pentagram) according to the given parameters.
	 * 
	 * @param size
	 *            resulting image's height and width
	 * @param fillOpacity
	 *            opacity value for the filled parts of the image
	 * @param fillColor
	 *            <tt>Color</tt> to be used for the fill
	 * @param strokeOpacity
	 *            opacity value for the stroked parts of the image
	 * @param strokeColor
	 *            <tt>Color</tt> to be used for the strokes
	 * @param dash
	 *            dash array for rendering boundary line
	 * @param width
	 *            of the boundary line
	 * @param cap
	 *            of the boundary line
	 * @param join
	 *            of the boundary line
	 * @param dashOffset
	 * 
	 * @return an image of a pentagram
	 */
	public Image drawStarOnImage( int size, double fillOpacity, Color fillColor, double strokeOpacity,
			Color strokeColor, float[] dash, float dashOffset, float width, int cap, int join ) {
		int offset = (int) ( width * 2 + 1 ) / 2;
		Image image = new Image(Display.getCurrent(), size + offset * 2, size + offset * 2);

		int s = size / 2;
		int x0 = s;
		int y0 = s;
		double sin36 = Math.sin(36*Math.PI/180.0);
		double cos36 = Math.cos(36*Math.PI/180.0);
		double sin18 = Math.sin(18*Math.PI/180.0);
		double cos18 = Math.cos(18*Math.PI/180.0);
		int smallRadius = (int) ( s * sin18 / Math.sin(54*Math.PI/180.0) );

		int p0X = x0;
		int p0Y = y0 - s;
		int p1X = x0 + (int) ( smallRadius * sin36 );
		int p1Y = y0 - (int) ( smallRadius * cos36 );
		int p2X = x0 + (int) ( s * cos18 );
		int p2Y = y0 - (int) ( s * sin18 );
		int p3X = x0 + (int) ( smallRadius * cos18 );
		int p3Y = y0 + (int) ( smallRadius * sin18 );
		int p4X = x0 + (int) ( s * sin36 );
		int p4Y = y0 + (int) ( s * cos36 );
		int p5Y = y0 + smallRadius;
		int p6X = x0 - (int) ( s * sin36 );
		int p7X = x0 - (int) ( smallRadius * cos18 );
		int p8X = x0 - (int) ( s * cos18 );
		int p9X = x0 - (int) ( smallRadius * sin36 );

		int[] points = new int[] { p0X, p0Y, p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, p0X, p5Y, p6X, p4Y, p7X, p3Y, p8X, p2Y, p9X, p1Y};
		for ( int i = 0; i < points.length; i++ ) {
			points[i] = points[i] + offset;
		}

		GC gc = new GC(image);
		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, fillColor, fillOpacity );
		gc.fillPolygon(points);
		gc.drawPolygon(points);
		gc.dispose();

		return image;
	}

	/**
	 * Draws a scaled instance of a circle mark according to the given parameters.
	 * 
	 * @param size
	 *            resulting image's height and widthh
	 * @param fillOpacity
	 *            opacity value for the filled parts of the image
	 * @param fillColor
	 *            <tt>Color</tt> to be used for the fill
	 * @param strokeOpacity
	 *            opacity value for the stroked parts of the image
	 * @param strokeColor
	 *            <tt>Color</tt> to be used for the strokes
	 * @param dash
	 *            dash array for rendering boundary line
	 * @param width
	 *            of the boundary line
	 * @param cap
	 *            of the boundary line
	 * @param join
	 *            of the boundary line
	 * @param dashOffset
	 * 
	 * @return image displaying a circle
	 * @throws FilterEvaluationException
	 */
	public Image drawCircleOnImage( int size, double fillOpacity, Color fillColor, double strokeOpacity,
			Color strokeColor, float[] dash, float dashOffset, float width, int cap, int join ) {
		int offset = (int) ( width * 2 + 1 ) / 2;
		Image image = new Image(Display.getCurrent(), size + offset * 2, size + offset * 2);

		GC gc = new GC(image);
		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, fillColor, fillOpacity );
		gc.fillOval( offset, offset, size, size );
		gc.drawOval( offset, offset, size, size );
		gc.dispose();

		return image;
	}

	/**
	 * Draws a scaled instance of a square mark according to the given parameters.
	 * 
	 * @param size
	 *            resulting image's height and widthh
	 * @param fillOpacity
	 *            opacity value for the filled parts of the image
	 * @param fillColor
	 *            <tt>Color</tt> to be used for the fill
	 * @param strokeOpacity
	 *            opacity value for the stroked parts of the image
	 * @param strokeColor
	 *            <tt>Color</tt> to be used for the strokes
	 * @param dash
	 *            dash array for rendering boundary line
	 * @param width
	 *            of the boundary line
	 * @param cap
	 *            of the boundary line
	 * @param join
	 *            of the boundary line
	 * @param dashOffset
	 * 
	 * @return image displaying a square
	 */
	public Image drawSquareOnImage( int size, double fillOpacity, Color fillColor, double strokeOpacity,
			Color strokeColor, float[] dash, float dashOffset, float width, int cap, int join ) {
		int offset = (int) ( width * 2 + 1 ) / 2;
		Image image = new Image(Display.getCurrent(), size + offset * 2, size + offset * 2);

		GC gc = new GC(image);
		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, fillColor, fillOpacity );
		gc.fillRectangle( offset, offset, size, size );
		gc.drawRectangle( offset, offset, size - 1, size - 1 );
		gc.dispose();

		return image;
	}

	/**
	 * Draws a scaled instance of a cross mark (a "+") according to the given parameters.
	 * 
	 * @param size
	 *            resulting image's height and widthh
	 * @param strokeOpacity
	 *            opacity value for the stroked parts of the image
	 * @param strokeColor
	 *            <tt>Color</tt> to be used for the strokes
	 * @param dash
	 *            dash array for rendering boundary line
	 * @param width
	 *            of the boundary line
	 * @param cap
	 *            of the boundary line
	 * @param join
	 *            of the boundary line
	 * @param dashOffset
	 * 
	 * @return image displaying a cross (a "+")
	 */
	public Image drawCross1OnImage( int size, double strokeOpacity, Color strokeColor, float[] dash, float dashOffset,
			float width, int cap, int join ) {

		int offset = (int) ( width * 2 + 1 ) / 2;
		Image image = new Image(Display.getCurrent(), size + offset * 2, size + offset * 2);

		GC gc = new GC(image);
		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, null, -1);
		gc.drawLine( offset, size / 2 + offset, size - 1 + offset, size / 2 + offset );
		gc.drawLine( size / 2 + offset, offset, size / 2 + offset, size - 1 + offset );
		gc.dispose();

		return image;
	}

	/**
	 * Draws a scaled instance of a cross mark (an "X") according to the given parameters.
	 * 
	 * @param size
	 *            resulting image's height and widthh
	 * @param strokeOpacity
	 *            opacity value for the stroked parts of the image
	 * @param strokeColor
	 *            <tt>Color</tt> to be used for the strokes
	 * @param dash
	 *            dash array for rendering boundary line
	 * @param width
	 *            of the boundary line
	 * @param cap
	 *            of the boundary line
	 * @param join
	 *            of the boundary line
	 * @param dashOffset
	 * 
	 * @return image displaying a cross (a "X")
	 */
	public Image drawCross2OnImage( int size, double strokeOpacity, Color strokeColor, float[] dash, float dashOffset,
			float width, int cap, int join ) {

		int offset = (int) ( width * 2 + 1 ) / 2;
		Image image = new Image(Display.getCurrent(), size + offset * 2, size + offset * 2);

		GC gc = new GC(image);
		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, null, -1);
		gc.drawLine( offset, offset, size - 1 + offset, size - 1 + offset );
		gc.drawLine( offset, size - 1 + offset, size - 1 + offset, offset );
		gc.dispose();

		return image;
	}

	public void paint(GC gc, int x, int y, int size) {
		double fillOpacity = 1.0;
		double strokeOpacity = 1.0;
		float[] dash = null;
		float dashOffset = 0;
		int cap = SWT.CAP_ROUND;
		int join = SWT.JOIN_ROUND;
		float width = 1;
		Color fillColor = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		Color strokeColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

		if ( fill != null ) {
			fillOpacity = fill.getOpacity();
			fillColor = fill.getFill();
		}

		if ( stroke != null ) {
			strokeOpacity = stroke.getOpacity();
			strokeColor = stroke.getStroke();
			dash = stroke.getDashArray();
			cap = stroke.getLineCap();
			join = stroke.getLineJoin();
			width = (float) stroke.getWidth();
			dashOffset = stroke.getDashOffset();
		}

		if ( wellKnownName == null ) {
			wellKnownName = "square";
		}

		if ( wellKnownName.equalsIgnoreCase( "circle" ) ) {
			drawCircle(gc, x, y, size, fillOpacity, fillColor, strokeOpacity, strokeColor, dash, dashOffset, width, cap,
					join );
		} else if ( wellKnownName.equalsIgnoreCase( "triangle" ) ) {
			drawTriangle(gc, x, y, size, fillOpacity, fillColor, strokeOpacity, strokeColor, dash, dashOffset, width,
					cap, join );
		} else if ( wellKnownName.equalsIgnoreCase( "cross" ) ) {
			drawCross1(gc, x, y, size, strokeOpacity, strokeColor, dash, dashOffset, width, cap, join );
		} else if ( wellKnownName.equalsIgnoreCase( "x" ) ) {
			drawCross2(gc, x, y, size, strokeOpacity, strokeColor, dash, dashOffset, width, cap, join );
		} else if ( wellKnownName.equalsIgnoreCase( "star" ) ) {
			drawStar(gc, x, y, size, fillOpacity, fillColor, strokeOpacity, strokeColor, dash, dashOffset, width, cap,
					join );
		} else {
			drawSquare(gc, x, y, size, fillOpacity, fillColor, strokeOpacity, strokeColor, dash, dashOffset, width, cap,
					join );
		}

	}

	public void drawTriangle(GC gc, int x, int y, int size, double fillOpacity, Color fillColor, double strokeOpacity,
			Color strokeColor, float[] dash, float dashOffset, float width, int cap, int join ) {

		int offset = (int) ( width * 2 + 1 ) / 2;
		int ox = x - size/2 - offset;
		int oy = y - size/2 - offset;

		int[] points = new int[6];
		points[0] = ox + offset;
		points[1] = oy + offset;
		points[2] = ox + size / 2 + offset;
		points[3] = oy + size - 1 + offset;
		points[4] = ox + size - 1 + offset;
		points[5] = oy + offset;

		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, fillColor, fillOpacity );
		gc.fillPolygon(points);
		gc.drawPolygon(points);
	}

	public void drawStar(GC gc, int x, int y, int size, double fillOpacity, Color fillColor, double strokeOpacity,
			Color strokeColor, float[] dash, float dashOffset, float width, int cap, int join ) {
		int offset = (int) ( width * 2 + 1 ) / 2;
		int ox = x - size/2 - offset;
		int oy = y - size/2 - offset;

		int s = size / 2;
		int x0 = s;
		int y0 = s;
		double sin36 = Math.sin(36*Math.PI/180.0);
		double cos36 = Math.cos(36*Math.PI/180.0);
		double sin18 = Math.sin(18*Math.PI/180.0);
		double cos18 = Math.cos(18*Math.PI/180.0);
		int smallRadius = (int) ( s * sin18 / Math.sin(54*Math.PI/180.0) );

		int p0X = x0;
		int p0Y = y0 - s;
		int p1X = x0 + (int) ( smallRadius * sin36 );
		int p1Y = y0 - (int) ( smallRadius * cos36 );
		int p2X = x0 + (int) ( s * cos18 );
		int p2Y = y0 - (int) ( s * sin18 );
		int p3X = x0 + (int) ( smallRadius * cos18 );
		int p3Y = y0 + (int) ( smallRadius * sin18 );
		int p4X = x0 + (int) ( s * sin36 );
		int p4Y = y0 + (int) ( s * cos36 );
		int p5Y = y0 + smallRadius;
		int p6X = x0 - (int) ( s * sin36 );
		int p7X = x0 - (int) ( smallRadius * cos18 );
		int p8X = x0 - (int) ( s * cos18 );
		int p9X = x0 - (int) ( smallRadius * sin36 );

		int[] points = new int[] { ox+p0X, oy+p0Y, ox+p1X, oy+p1Y, ox+p2X, oy+p2Y, ox+p3X, oy+p3Y,
			ox+p4X, oy+p4Y, ox+p0X, oy+p5Y, ox+p6X, oy+p4Y, ox+p7X, oy+p3Y, ox+p8X, oy+p2Y, ox+p9X, oy+p1Y};
		for ( int i = 0; i < points.length; i++ ) {
			points[i] = points[i] + offset;
		}

		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, fillColor, fillOpacity );
		gc.fillPolygon(points);
		gc.drawPolygon(points);
	}

	public void drawCircle(GC gc, int x, int y, int size, double fillOpacity, Color fillColor, double strokeOpacity,
			Color strokeColor, float[] dash, float dashOffset, float width, int cap, int join ) {
		int offset = (int) ( width * 2 + 1 ) / 2;
		int ox = x - size/2 - offset;
		int oy = y - size/2 - offset;

		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, fillColor, fillOpacity );
		gc.fillOval(ox+offset, oy+offset, size, size );
		gc.drawOval(ox+offset, oy+offset, size, size );
	}

	public void drawSquare(GC gc, int x, int y, int size, double fillOpacity, Color fillColor, double strokeOpacity,
			Color strokeColor, float[] dash, float dashOffset, float width, int cap, int join ) {
		int offset = (int) ( width * 2 + 1 ) / 2;
		int ox = x - size/2 - offset;
		int oy = y - size/2 - offset;

		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, fillColor, fillOpacity );
		gc.fillRectangle(ox+offset, oy+offset, size, size );
		gc.drawRectangle(ox+offset, oy+offset, size - 1, size - 1 );
	}

	public void drawCross1(GC gc, int x, int y, int size, double strokeOpacity, Color strokeColor, float[] dash, float dashOffset,
			float width, int cap, int join ) {

		int offset = (int) ( width * 2 + 1 ) / 2;
		int ox = x - size/2 - offset;
		int oy = y - size/2 - offset;

		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, null, -1);
		gc.drawLine(ox+offset, oy+size / 2 + offset, ox+size - 1 + offset, oy+size / 2 + offset );
		gc.drawLine(ox+size / 2 + offset, oy+offset, ox+size / 2 + offset, oy+size - 1 + offset );
	}

	public void drawCross2(GC gc, int x, int y, int size, double strokeOpacity, Color strokeColor, float[] dash, float dashOffset,
			float width, int cap, int join ) {

		int offset = (int) ( width * 2 + 1 ) / 2;
		int ox = x - size/2 - offset;
		int oy = y - size/2 - offset;

		setGCDrawingParams(gc, dash, dashOffset, width, cap, join, strokeColor, strokeOpacity, null, -1);
		gc.drawLine(ox+offset, oy+offset, ox+size - 1 + offset, oy+size - 1 + offset );
		gc.drawLine(ox+offset, oy+size - 1 + offset, ox+size - 1 + offset, oy+offset );
	}
}
