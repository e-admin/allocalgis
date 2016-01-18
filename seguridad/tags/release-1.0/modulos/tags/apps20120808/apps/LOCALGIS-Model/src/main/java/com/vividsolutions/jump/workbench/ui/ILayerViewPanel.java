package com.vividsolutions.jump.workbench.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jump.util.Blackboard;

import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.CategoryEvent;


import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager;
import com.vividsolutions.jump.workbench.ui.renderer.java2D.Java2DConverter;

public interface ILayerViewPanel {

	public abstract double getAppliedScale();

	public abstract void setAppliedScale(double appScale);

	/**
	 * @param layerManager
	 * @param context
	 */
	public abstract void init(ILayerManager layerManager,
			LayerViewPanelContext context);

	public abstract IToolTipWriter getToolTipWriter();

	//In Java 1.3, if you try and do a #mouseClicked or a #mouseDragged on an
	//inactive internal frame, it won't work. [Jon Aquino]
	//In Java 1.4, the #mouseDragged will work, but not the #mouseClicked.
	//See the Sun Java Bug Database, ID 4398733. The evaluation for Bug ID 4256525
	//states that the fix is scheduled for the Java release codenamed Tiger.
	//[Jon Aquino]
	public abstract String getToolTipText(MouseEvent event);

	/**
	 * The Fence layer will be excluded.
	 */
	public abstract Map visibleLayerToFeaturesInFenceMap();

	/**
	 * The Fence layer will be included.
	 */
	public abstract Map visibleLayerToFeaturesInFenceMap(Geometry fence);

	public abstract void setCurrentCursorTool(CursorTool currentCursorTool);

	/**
	 * When a layer is added, if this flag is false, the viewport will be zoomed
	 * to the extent of the layer.
	 */
	public abstract void setViewportInitialized(boolean viewportInitialized);

	public abstract CursorTool getCurrentCursorTool();

	/**
	 *  Note: the popup menu is shown only if the user right-clicks the panel.
	 *  Thus, popup-menu event handlers don't need to check whether the return
	 *  value is null.
	 */
	public abstract java.awt.Point getLastClickedPoint();

	public abstract IViewport getViewport();

	public abstract Java2DConverter getJava2DConverter();

	/**
	 *@return    the fence in model-coordinates, or null if there is no fence
	 */
	public abstract Geometry getFence();

	public abstract ILayerManager getLayerManager();

	public abstract void featuresChanged(FeatureEvent e);

	public abstract void categoryChanged(CategoryEvent e);

	public abstract void layerChanged(LayerEvent e);

	/**
	 * Returns an image with the dimensions of this panel. Note that the image has
	 * an alpha component, and thus is not suitable for creating JPEGs -- they will look
	 * pinkish.
	 */
	public abstract Image createBlankPanelImage();

	public abstract void repaint();

	public abstract void superRepaint();

	public abstract void paintComponent(Graphics g);

	public abstract void erase(Graphics2D g);

	public abstract void fill(Graphics2D g, Color color);

	public abstract LayerViewPanelContext getContext();

	public abstract void addListener(LayerViewPanelListener listener);

	public abstract void removeListener(LayerViewPanelListener listener);

	/**
	 * @return d rounded off to the distance represented by one pixel
	 */
	public abstract String format(double d);

	public abstract void fireSelectionChanged();

	public abstract IRenderingManager getRenderingManager();

	//Not sure where this method should reside. [Jon Aquino]
	public abstract Collection featuresWithVertex(Point2D viewPoint,
			double viewTolerance, Collection features)
			throws NoninvertibleTransformException;

	public abstract void dispose();

	/**
	 * @param millisecondDelay the GUI will be unresponsive for this length of time,
	 * so keep it short!
	 */
	public abstract void flash(final Shape shape, Color color, Stroke stroke,
			final int millisecondDelay);

	public abstract ISelectionManager getSelectionManager();

	public abstract Blackboard getBlackboard();

	public abstract void flash(final GeometryCollection geometryCollection)
			throws NoninvertibleTransformException;

	public abstract double getNormalizedScale(double scale);

	public abstract double getScaleFromNormalized(double normScale);

	public abstract void firePropertyChange(String propertyName,
			Object oldValue, Object newValue);
	
	public abstract int getWidth();
	public abstract int getHeight();

}