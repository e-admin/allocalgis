/*
 * Created on 07-jun-2004
 */
package com.geopista.style.sld.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.NoninvertibleTransformException;
import java.util.List;

import org.deegree.graphics.sld.UserStyle;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.IViewport;
import com.vividsolutions.jump.workbench.ui.Viewport;

/**
 * @author Enxenio S.L.
 */
public interface SLDStyle extends com.vividsolutions.jump.workbench.ui.renderer.style.Style {
	public static int INSERTANDREPLACE = 0;
	public static int REPLACEALL = 1;

	public static final int NONE = 0;
	public static final int DATABASE = 1;
	public static final int FILE = 2;

	public static final int PAINT_LABELS=1;
	public static final int PAINT_POINTS=2;
	public static final int PAINT_LINES=4;
	public static final int PAINT_POLYGONS=8;
	public static final int PAINT_ALL=SLDStyle.PAINT_LABELS|SLDStyle.PAINT_POINTS|SLDStyle.PAINT_LINES|SLDStyle.PAINT_POLYGONS;

	public int getType();
	public boolean isPermanentChanged();
	public String getCurrentStyleName();
	public void setCurrentStyleName(String currentStyleName);
	public void putStyles(int mode, List styles);
	public List getStyles();
	public UserStyle getUserStyle(String styleName);
	public void addUserStyle(UserStyle userStyle);
	public void removeUserStyle(String styleName);
	public Color getFillColor();
	public Color getLineColor();
	public int getAlpha();
	public void paint(Feature f, Graphics2D g, IViewport viewport) throws NoninvertibleTransformException;
	public void createSLDFile();
	public String getSLD();
	public void setSLD(String sld);
	public String getSystemId();
	public void setSystemId(String systemId);
	public void setSLDFileName(String fileName);
	public void setLayerName(String layerName);
	public void setLayers(List layers);
	public void setStyles(List styles);
	public void setSelectedStyles(Object[] selectedStyles);
	public void createSLDFile2();

	/* Allows invokers to activate only one kind of rendering to allow
	 * the rendering in a layered manner, i.e. poligon, point, labels in subsequent calls to paint
	 * As SLDStyle combines all kind of Symbolizers in the same Style this trick was considered a good
	 * compromise for Geopista.
	 * 
	 * @param type bit field 1-paint labels, 2-paint points, 4-paint lines, 8-paint poligons
	 * @see com.geopista.style.sld.model.SLDStyle#setStyleTypeFilter(int)
	 */
	public void setStyleTypeFilter(int type);
}