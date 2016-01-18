/**
 * IRasterImageLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package pirolPlugIns.utilities.RasterImageSupport;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;

import javax.media.jai.PlanarImage;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.IViewport;

public interface IRasterImageLayer {

	/**
	 *@inheritDoc
	 */
	public abstract Blackboard getBlackboard();

	public abstract Object clone() throws CloneNotSupportedException;

	/**
	 * Creates the image to draw
	 */
	public abstract BufferedImage createImage(ILayerViewPanel layerViewPanel);

	/**
	 * deletes image from RAM (if it is not to be keeped and if the RAM consumption is high)
	 * and calls the garbage collector, if the <code>garbageCollect</code> is true.
	 *@param garbageCollect if true the garbage collector will be called (this parameter may be overridden, if there is not enough RAM available...)
	 */
	public abstract boolean clearImage(boolean garbageCollect);

	/**
	 * flushes all images from the RAM.
	 *@param garbageCollect if true the garbage collector will be called (this parameter may be overridden, if there is not enough RAM available...)
	 */
	public abstract void flushImages(boolean garbageCollect);

	public abstract void reLoadImage();

	/**
	 * @return Envelope with the real world coordinates of the image
	 */
	public abstract Envelope getEnvelope();

	/**
	 * Sets the Envelope object containing the real world coordinates (e.g. WGS84) of the image - this needs to be set (if it wasn't given to the constructor)!
	 *@param envelope the Envelope
	 */
	public abstract void setEnvelope(Envelope envelope);

	/**
	 * for java2xml
	 *@return the Envelope as string
	 */
	public abstract String getXmlEnvelope();

	/**
	 * for java2xml
	 *@param envStr the Envelope as string
	 */
	public abstract void setXmlEnvelope(String envStr);

	/**
	 * Method to change the coordinates of the image and later apply the
	 * changes to the RasterImageLayer by using {@link RasterImageLayer#setGeometryAsEnvelope(Geometry)}.
	 *@return return the envelope (= bounding box) as a geometry, 
	 */
	public abstract Polygon getEnvelopeAsGeometry();

	/**
	 * Method to set the coordinates of the image, e.g. after changing them after using {@link RasterImageLayer#getEnvelopeAsGeometry()}.
	 */
	public abstract void setGeometryAsEnvelope(Geometry geometry);

	public abstract BufferedImage getTileAsImage(Envelope wantedEnvelope);

	public abstract Rectangle getDrawingRectangle(double imgWidth,
			double imgHeight, Envelope imageEnv, IViewport viewport)
			throws NoninvertibleTransformException;

	/**
	 * Sets the image that will be shown in the map (also sets some interally used flags)
	 *@param image image that will be shown in the map
	 */
	public abstract void setImage(javax.media.jai.PlanarImage image);

	public abstract void setImageSet(boolean imageSet);

	public abstract boolean isImageNull();

	/**
	 * returns the image 
	 *@return the image
	 */
	public abstract PlanarImage getImage();

	/**
	 *@return true, if the image object was set at least once, else false
	 */
	public abstract boolean isImageSet();

	/**
	 * Returns the transparency level of the image. The transparencyLevel controlls the transparency level of the whole image (all pixels). It
	 * is independent of the transparency color, that replaces a certain color in the image. 
	 * The transparencyLevel is expressed as a float within a range from 0.0 (no transparency) to 1.0 (full transparency).
	 *@return the transparency level of the image
	 */
	public abstract double getTransparencyLevel();

	/**
	 * Sets the transparency level of the image. This controlls the transparency level of the whole image (all pixels). It
	 * is independent of the transparency color, that replaces a certain color in the image. 
	 * The transparencyLevel is expressed as a float within a range from 0.0 (no transparency) to 1.0 (full transparency).
	 *@param transparencyLevel the transparency level of the image
	 */
	public abstract void setTransparencyLevel(double transparencyLevel);

	/**
	 * Sets the transparency level of the image. This controlls the transparency level of the whole image (all pixels). It
	 * is independent of the transparency color, that replaces a certain color in the image. 
	 * The transparencyLevel is expressed as a percentage within a range from 0 (no transparency) to 100 (full transparency).
	 *@param transparencyInPercent the transparency level of the image
	 */
	public abstract void setTransparencyLevelInPercent(int transparencyInPercent);

	/**
	 * Gets the color which will be drawn with a zero opacity in the Jump map
	 *@return color that will be replaced with transoarent pixels
	 */
	public abstract Color getTransparentColor();

	/**
	 * Sets the color which will be drawn with a zero opacity in the Jump map
	 *@param transparentColor the color for transparency
	 */
	public abstract void setTransparentColor(Color transparentColor);

	/**
	 *@return the current offset (to the viewport's <code>(0,0)</code>) in x direction 
	 */
	public abstract int getXOffset();

	/**
	 *@return the current offset (to the viewport's <code>(0,0)</code>) in y direction 
	 */
	public abstract int getYOffset();

	/**
	 * Sets the image's files name (if image is not to be keeped) - this needs to be set!
	 *@param imageFileName the file name of the image
	 */
	public abstract void setImageFileName(String imageFileName);

	/**
	 * 
	 *@return the file name of the image represented by this instance of the <code>RasterImageLayer</code>
	 */
	public abstract String getImageFileName();

	/**
	 * check, if image will be keeped in RAM or if it will be reloaded from a file
	 * if needed
	 *@return true if image will be keeped in RAM, else false
	 */
	public abstract boolean isNeedToKeepImage();

	/**
	 * toogle, if image will be keeped in RAM or if it will be reloaded from a file
	 * if needed
	 *@param needToKeepImage true if image is supposed be keeped in RAM, else false
	 */
	public abstract void setNeedToKeepImage(boolean needToKeepImage);

	/**
	 *@return the height of the source image
	 */
	public abstract int getOrigImageHeight();

	/**
	 *@return the width of the source image
	 */
	public abstract int getOrigImageWidth();

	/**
	 * for java2xml
	 *@param origImageHeight
	 */
	public abstract void setOrigImageHeight(int origImageHeight);

	/**
	 * for java2xml
	 *@param origImageWidth
	 */
	public abstract void setOrigImageWidth(int origImageWidth);

	/**
	 * shows or hides the image in the Jump map
	 *@param visible
	 */
	public abstract void setVisible(boolean visible);

	/**
	 * @see #firingAppearanceEvents
	 *@return true if appearance events are fired automatically, false if not
	 */
	public abstract boolean isFiringAppearanceEvents();

	/**
	 * @see #firingAppearanceEvents
	 *@param firingAppearanceEvents true if appearance events are to be fired automatically, false if not
	 */
	public abstract void setFiringAppearanceEvents(
			boolean firingAppearanceEvents);

	public abstract boolean isVisible();

	public abstract int getMaxPixelsForFastDisplayMode();
	

}