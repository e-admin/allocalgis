/**
 * IViewport.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.ui.renderer.java2D.Java2DConverter;


public interface IViewport {

	public abstract ILayerViewPanel getPanel();

	public abstract void addListener(ViewportListener l);

	public abstract void removeListener(ViewportListener l);

	public abstract Java2DConverter getJava2DConverter();

	public abstract ZoomHistory getZoomHistory();

	public abstract void update() throws NoninvertibleTransformException;

	public abstract void update(boolean fireZoomChanged)
			throws NoninvertibleTransformException;

	public abstract double getScale();

	/**
	 * Set both values but repaint once.
	 */
	public abstract void initialize(double newScale,
			Point2D newViewOriginAsPerceivedByModel);

	public abstract Point2D getOriginInModelCoordinates();

	/**
	 * Of widthOfNewViewAsPerceivedByOldView and heightOfNewViewAsPerceivedByOldView,
	 * this method will choose the one producing the least zoom.
	 */
	public abstract void zoom(Point2D centreOfNewViewAsPerceivedByOldView,
			double widthOfNewViewAsPerceivedByOldView,
			double heightOfNewViewAsPerceivedByOldView)
			throws NoninvertibleTransformException;

	public abstract Point2D toModelPoint(Point2D viewPoint)
			throws NoninvertibleTransformException;

	public abstract Coordinate toModelCoordinate(Point2D viewPoint)
			throws NoninvertibleTransformException;

	public abstract Point2D toViewPoint(Point2D modelPoint)
			throws NoninvertibleTransformException;

	public abstract Point2D toViewPoint(Coordinate modelCoordinate)
			throws NoninvertibleTransformException;

	public abstract Envelope toModelEnvelope(double x1, double x2, double y1,
			double y2) throws NoninvertibleTransformException;

	public abstract AffineTransform getModelToViewTransform()
			throws NoninvertibleTransformException;

	public abstract void setModelToViewTransform(AffineTransform model);

	public abstract Envelope getEnvelopeInModelCoordinates();

	//<<TODO:IMPROVE>> Currently the zoomed image is aligned west in the viewport.
	//It should be centred. [Jon Aquino]
	public abstract void zoom(Envelope modelEnvelope)
			throws NoninvertibleTransformException;

	public abstract void zoom(Envelope modelEnvelope, boolean fireZoomChanged)
			throws NoninvertibleTransformException;

	public abstract void setScale(double scale);

	public abstract void zoomToFullExtent()
			throws NoninvertibleTransformException;

	public abstract Envelope fullExtent();

	public abstract void zoomToViewPoint(
			Point2D centreOfNewViewAsPerceivedByOldView, double zoomFactor)
			throws NoninvertibleTransformException;

	public abstract Collection toViewPoints(Collection modelCoordinates)
			throws NoninvertibleTransformException;

	public abstract Rectangle2D toViewRectangle(Envelope envelope)
			throws NoninvertibleTransformException;

	public abstract double getNormalizedScaleForPPM(double scale, double ppm);

	public abstract double getNormalizedScaleForWidth(double scale,
			double widthMeters);

	/**
	 * Get scale for normalized Scale
	 * @param normScale
	 * @param ppm
	 * @return
	 */
	public abstract double getScaleForNSnPPM(double normScale, double ppm);

}