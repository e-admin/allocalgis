/**
 * SLDStyleImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 10-may-2004
 */
package com.geopista.style.sld.model.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deegree.graphics.displayelements.Label;
import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.Graphic;
import org.deegree.graphics.sld.GraphicFill;
import org.deegree.graphics.sld.LabelPlacement;
import org.deegree.graphics.sld.Layer;
import org.deegree.graphics.sld.LinePlacement;
import org.deegree.graphics.sld.LineSymbolizer;
import org.deegree.graphics.sld.Mark;
import org.deegree.graphics.sld.ParameterValueType;
import org.deegree.graphics.sld.PointPlacement;
import org.deegree.graphics.sld.PointSymbolizer;
import org.deegree.graphics.sld.PolygonSymbolizer;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Style;
import org.deegree.graphics.sld.StyledLayerDescriptor;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.TextSymbolizer;
import org.deegree.graphics.sld.UserStyle;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree.xml.XMLParsingException;
import org.deegree_impl.graphics.displayelements.LabelFactory;
import org.deegree_impl.graphics.sld.Graphic_Impl;
import org.deegree_impl.graphics.sld.NamedLayer_Impl;
import org.deegree_impl.graphics.sld.SLDFactory;
import org.deegree_impl.graphics.sld.StyleFactory;
import org.deegree_impl.graphics.sld.UserLayer_Impl;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.ScaleRange;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.IViewport;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrameImpl;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;

import es.enxenio.util.exceptions.InternalErrorException;


/**
 * @author Enxenio S.L.
 */
public class SLDStyleImpl extends BasicStyle implements com.geopista.style.sld.model.SLDStyle {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(SLDStyleImpl.class);

	public static final String SYMBOLIZER_SIZES_REFERENCE_SCALE = "SYMBOLIZER_SIZES_REFERENCE_SCALE";

	private static final double	OVERLAP_MARGIN	= -20; //margin to avoid overlap of adjacent segments labels in a linestring (the more negative the less potential overlap)
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


	public SLDStyleImpl() {
		super();
	}

	
	public SLDStyleImpl(Color fillColor, Color strokeColor, String selectedLayerName) {		
		super();
		
		UserStyle aUserStyle;		
		aUserStyle = createBasicStyle(fillColor, strokeColor); 
		Style[] styles = new Style[] {aUserStyle};
		Layer aLayer = null;
		try {
			aLayer = SLDFactory.createNamedLayer(selectedLayerName,null,styles);
		} catch (XMLParsingException e) {
			e.printStackTrace();
		}
		_layers.add(aLayer);
		List styleList = new ArrayList();
		styleList.add(aUserStyle);
		_layersStyles.put(selectedLayerName,styleList);
		addUserStyle(aUserStyle);
		setCurrentStyleName(aUserStyle.getName());
		_actualLayerName = selectedLayerName;
		_type = com.geopista.style.sld.model.SLDStyle.NONE;
	}

	public SLDStyleImpl(String fileName, String styleName, String selectedLayerName) {		
		super();
		
		File styleFile = new File(fileName);
		try {
			InputStreamReader isr = new FileReader(styleFile);
			createStyles(isr);
			_userStyles = (List)_layersStyles.get(selectedLayerName);
			_sldFileName = fileName;
			_actualLayerName = selectedLayerName;
			setCurrentStyleName(styleName);
			if (getUserStyle(getCurrentStyleName()) == null) {
				setCurrentStyleName(getDefaultStyle().getName());
			}
			_type = com.geopista.style.sld.model.SLDStyle.FILE;
		} catch ( IOException e ) {
			WorkbenchFrameImpl.handleThrowable(e, null);
		} 		
	}

	public SLDStyleImpl(String sld, String selectedLayerName) {		
		super();
		
		Reader isr = new StringReader(sld);
		createStyles(isr);
		_userStyles = (List)_layersStyles.get(selectedLayerName);
		_sldFileName = null;
		_actualLayerName = selectedLayerName;
		_type = com.geopista.style.sld.model.SLDStyle.DATABASE;
		setCurrentStyleName(getDefaultStyle().getName());
	}

	public int getType() {
		
		return _type;
	}

	public void setType(int type) {
		
		_type = type;
	}

	public boolean isLocal() {
		
		return _estiloLocal;
	}

	public void setLocal(boolean estiloLocal) {
		
		_estiloLocal = estiloLocal;
	}

	public String getLayerName() {
		
		return _actualLayerName;
	}

	public boolean getLockSymbolSizes() {
		
		boolean existScale = false;		
		AppContext app =(AppContext) AppContext.getApplicationContext();
		Blackboard identificadores = app.getBlackboard();
		Double scaleDenominator = (Double)identificadores.get(SYMBOLIZER_SIZES_REFERENCE_SCALE);

		if (scaleDenominator != null) {
			existScale = true;
		}
		return existScale;
	}
	
	public double getReferenceScale() {
		
		AppContext app =(AppContext) AppContext.getApplicationContext();
		Blackboard identificadores = app.getBlackboard();
		Double scaleDenominator = (Double)identificadores.get(SYMBOLIZER_SIZES_REFERENCE_SCALE);		
		return scaleDenominator.doubleValue();
	}
	
	public boolean isPermanentChanged() {
		return _stylePermanentChanged;
	}
	
	public void setPermanentChanged(boolean _stylePermanentChanged)
	{
	    this._stylePermanentChanged = _stylePermanentChanged;
	}
	
	public void setLayerName(String layerName) {
		_actualLayerName = layerName;
		_userStyles = (List)_layersStyles.get(_actualLayerName);
	}

	public String getSLD() {
		
		StringWriter swSLD = new StringWriter();
		PrintWriter pwSLD = new PrintWriter(swSLD);
		writeSLDFile(pwSLD);
		pwSLD.close();
		return swSLD.toString();
	}
	
	public void setSLD(String sld) {
		Reader isr = new StringReader(sld);
		createStyles(isr);
	}

	public String getCurrentStyleName() {
		return _currentStyleName;
	}

	public void setCurrentStyleName(String currentStyleName) {
		_currentStyleName = currentStyleName;
	}

	public String getSLDFileName() {
		return _sldFileName;	
	}

	public void setSLDFileName(String sldFileName) {
		_sldFileName = sldFileName;	
	}
	
	public List getStyles() {
		return _userStyles;
	}

	public void setStyles(List userStyles) {
		_userStyles = userStyles;
	}

	public void putStyles(int mode, List styles) {
		Iterator stylesIterator;
		UserStyle aStyle;
		
		if (mode == REPLACEALL) _userStyles.clear();
		try {
			stylesIterator = styles.iterator();
			while (stylesIterator.hasNext()) {
				aStyle = (UserStyle)stylesIterator.next(); 
				if (aStyle.getName() != null) removeUserStyle(((UserStyle)aStyle).getName());
				addUserStyle((UserStyle)aStyle);
			}
			_stylePermanentChanged = true;
		} catch ( Exception e ) {
			WorkbenchFrameImpl.handleThrowable(e, null);
		}		
	}
		
	private void createStyles(Reader isr) {

		try {
			StyledLayerDescriptor sld = SLDFactory.createSLD( isr );
			org.deegree.graphics.sld.Layer[] layers = sld.getLayers();
			for (int i=0;i<layers.length;i++) {
				String layerName = layers[i].getName();
				_layers.add(layers[i]);
				org.deegree.graphics.sld.Style[] styles = layers[i].getStyles();
				List layerStyles = new ArrayList();
				for (int j=0;j<styles.length;j++) {
					org.deegree.graphics.sld.Style aStyle = styles[j]; 
					layerStyles.add(aStyle);
				}
				_layersStyles.put(layerName,layerStyles);    
			}		 
		} catch ( XMLParsingException e ) {
			WorkbenchFrameImpl.handleThrowable(e, null);
		}		
	}

	public UserStyle getUserStyle(String styleName) {
		for (Iterator i = _userStyles.iterator(); i.hasNext();) {
			UserStyle s = (UserStyle) i.next();
			if (s.getName().equals(styleName)) {
				return s;
			}
		}
		return null;
	}

	public UserStyle getDefaultStyle() {
		for (Iterator i = _userStyles.iterator(); i.hasNext();) {
			UserStyle s = (UserStyle) i.next();
			if (s.isDefault()) {
				return s;
			}
		}
		return (UserStyle)_userStyles.get(0);
	}

	public void addUserStyle(UserStyle userStyle) {
		_userStyles.add(userStyle);
	}

	public void removeUserStyle(String styleName) {
		for (Iterator i = _userStyles.iterator(); i.hasNext();) {
			UserStyle s = (UserStyle) i.next();
			if (s.getName().equals(styleName)) {
				i.remove();
			}
		}
	}
	
	public Color getFillColor() {
		UserStyle aUserStyle;
		FeatureTypeStyle aFeatureTypeStyle;
		ScaleRange aScaleRange;
		Rule aRule;
		
		aUserStyle = getUserStyle(getCurrentStyleName());
		if (aUserStyle != null) {
			if (aUserStyle.getFeatureTypeStyles().length > 0) {
				// Simplificacion: ignoramos el resto de FeatureTypeStyles
				aFeatureTypeStyle = aUserStyle.getFeatureTypeStyles()[0];
				SLDFacade sldFacade = null;
				try {
					sldFacade = com.geopista.style.sld.model.SLDFactory.getDelegate();
					List scaleRangeList = sldFacade.getScaleRangeList(aFeatureTypeStyle);
					aScaleRange = findScaleRange(scaleRangeList, 0, 9e99);
					if (aScaleRange == null) {
						aScaleRange = (ScaleRange)scaleRangeList.get(0);
					}
					if (aScaleRange.getPolygonList().size() > 0) {
						aRule = (Rule)aScaleRange.getPolygonList().get(0);
						PolygonSymbolizer aSymbolizer = (PolygonSymbolizer)aRule.getSymbolizers()[0];
						return getFillColor(aSymbolizer);
					}
					else if (aScaleRange.getPointList().size() > 0) {
						aRule = (Rule)aScaleRange.getPointList().get(0);
						PointSymbolizer aSymbolizer = (PointSymbolizer)aRule.getSymbolizers()[0];
						return getFillColor(aSymbolizer);
					}
					else if (aScaleRange.getTextList().size() > 0) {
						aRule = (Rule)aScaleRange.getTextList().get(0);
						TextSymbolizer aSymbolizer = (TextSymbolizer)aRule.getSymbolizers()[0];
						return getFillColor(aSymbolizer);
					}
					else return UIManager.getColor("Panel.background");
				} catch(InternalErrorException e) {
					System.err.println(e);
					return UIManager.getColor("Panel.background"); 
				}
			}
		}
		return UIManager.getColor("Panel.background"); 
	}

	private Color getFillColor(PolygonSymbolizer aSymbolizer) {
		org.deegree.graphics.sld.Fill fill = aSymbolizer.getFill();
		if (fill != null) {
			org.deegree.graphics.sld.CssParameter css = (org.deegree.graphics.sld.CssParameter)fill.getCssParameters().get("fill");
			return Color.decode((String)css.getValue().getComponents()[0]);
		}
		else {
			return UIManager.getColor("Panel.background");		
		}
	}

	private Color getFillColor(PointSymbolizer aSymbolizer) {

		Graphic graphic = aSymbolizer.getGraphic();
		Object[] marksAndExtGraphics = graphic.getMarksAndExtGraphics();
		if (marksAndExtGraphics.length > 0) {
			if (marksAndExtGraphics[0] instanceof Mark) {
				Mark aMark = (Mark)marksAndExtGraphics[0];
				org.deegree.graphics.sld.Fill fill = aMark.getFill();
				if (fill != null) {
					org.deegree.graphics.sld.CssParameter css = (org.deegree.graphics.sld.CssParameter)fill.getCssParameters().get("fill");
					return Color.decode((String)css.getValue().getComponents()[0]);
				}
			}
		}
		return UIManager.getColor("Panel.background");		
	}

	private Color getFillColor(TextSymbolizer aSymbolizer) {

		org.deegree.graphics.sld.Font sldFont = aSymbolizer.getFont();
		try {
			return sldFont.getColor(null);
		}
		catch (FilterEvaluationException e) {
			return UIManager.getColor("Panel.background");					
		}
	}

	private ScaleRange findScaleRange(List scaleRangeList, double minScale, double maxScale) {
		Iterator listIterator;
		ScaleRange aScaleRange;
		
		listIterator = scaleRangeList.iterator();
		while (listIterator.hasNext()) {
			aScaleRange = (ScaleRange)listIterator.next(); 
			if ((aScaleRange.getMinScale().doubleValue() == minScale) && (aScaleRange.getMaxScale().doubleValue() == maxScale)) {
				return aScaleRange;  
			}
		}
		return null;
	}

	public Color getLineColor() {
		UserStyle aUserStyle;
		FeatureTypeStyle aFeatureTypeStyle;
		ScaleRange aScaleRange;
		Rule aRule;
		
		aUserStyle = getUserStyle(getCurrentStyleName());
		if (aUserStyle != null) {
			if (aUserStyle.getFeatureTypeStyles().length > 0) {
				// Simplificacion: ignoramos el resto de FeatureTypeStyles
				aFeatureTypeStyle = aUserStyle.getFeatureTypeStyles()[0];
				SLDFacade sldFacade = null;
				try {
					sldFacade = com.geopista.style.sld.model.SLDFactory.getDelegate();
					List scaleRangeList = sldFacade.getScaleRangeList(aFeatureTypeStyle);
					aScaleRange = findScaleRange(scaleRangeList, 0, 9e99);
					if (aScaleRange == null) {
						aScaleRange = (ScaleRange)scaleRangeList.get(0);
					}
					if (aScaleRange.getPolygonList().size() > 0) {
						aRule = (Rule)aScaleRange.getPolygonList().get(0);
						PolygonSymbolizer aSymbolizer = (PolygonSymbolizer)aRule.getSymbolizers()[0];
						return getLineColor(aSymbolizer);
					}
					else if (aScaleRange.getLineList().size() > 0) {
						aRule = (Rule)aScaleRange.getLineList().get(0);
						LineSymbolizer aSymbolizer = (LineSymbolizer)aRule.getSymbolizers()[0];
						return getLineColor(aSymbolizer);
					}
					else if (aScaleRange.getPointList().size() > 0) {
						aRule = (Rule)aScaleRange.getPointList().get(0);
						PointSymbolizer aSymbolizer = (PointSymbolizer)aRule.getSymbolizers()[0];
						return getLineColor(aSymbolizer);
					}
					else return UIManager.getColor("Panel.background");
				} catch(InternalErrorException e) {
					System.err.println(e);
					return UIManager.getColor("Panel.background"); 
				}
			}
		}
		return UIManager.getColor("Panel.background"); 
	}

	private Color getLineColor(PolygonSymbolizer aSymbolizer) {
		org.deegree.graphics.sld.Stroke stroke = aSymbolizer.getStroke();
		if (stroke != null) {
			org.deegree.graphics.sld.CssParameter css = (org.deegree.graphics.sld.CssParameter)stroke.getCssParameters().get("stroke");
			return Color.decode((String)css.getValue().getComponents()[0]);
		}
		else {
			return UIManager.getColor("Panel.background");		
		}
	}

	private Color getLineColor(PointSymbolizer aSymbolizer) {

		Graphic graphic = aSymbolizer.getGraphic();
		Object[] marksAndExtGraphics = graphic.getMarksAndExtGraphics();
		if (marksAndExtGraphics.length > 0) {
			if (marksAndExtGraphics[0] instanceof Mark) {
				Mark aMark = (Mark)marksAndExtGraphics[0];
				org.deegree.graphics.sld.Stroke stroke = aMark.getStroke();
				if (stroke != null) {
					org.deegree.graphics.sld.CssParameter css = (org.deegree.graphics.sld.CssParameter)stroke.getCssParameters().get("stroke");
					return Color.decode((String)css.getValue().getComponents()[0]);
				}
			}
		}
		return UIManager.getColor("Panel.background");		
	}

	private Color getLineColor(LineSymbolizer aSymbolizer) {

		org.deegree.graphics.sld.Stroke stroke = aSymbolizer.getStroke();
		if (stroke != null) {
			org.deegree.graphics.sld.CssParameter css = (org.deegree.graphics.sld.CssParameter)stroke.getCssParameters().get("stroke");
			return Color.decode((String)css.getValue().getComponents()[0]);
		}
		else {
			return UIManager.getColor("Panel.background");		
		}
	}
	
	public int getAlpha() {
		return 255;
	}
	
	public void paint(Feature f, Graphics2D g, IViewport viewport) throws NoninvertibleTransformException {
		UserStyle currentStyle;
		boolean fireDirtyEvents=false;
		if (f instanceof GeopistaFeature){
			fireDirtyEvents=((GeopistaFeature)f).isFireDirtyEvents();
			((GeopistaFeature)f).setFireDirtyEvents(false);
		}
		org.deegree.model.feature.Feature deegreeF = new JUMPFToDeegreeFForFilteringPurpose(f);
		if (f instanceof GeopistaFeature){
			((GeopistaFeature)f).setFireDirtyEvents(fireDirtyEvents);
		}
//		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
	
		currentStyle = getUserStyle(getCurrentStyleName());
		if (currentStyle != null) {
			//TODO: There is no concept of FeatureTypeName in JUMP.
			// All features of a layer are generated form one table, hence, no featureTypeName is needed.
			// However, in GeoPISTA, features from a Layer may come from different tables, 
			// therefore, they must identified with a featureTypeName
			//String featureTypeName = f.getFeatureTypeName();
			FeatureTypeStyle[] fts = currentStyle.getFeatureTypeStyles();
			for ( int k = 0; k < fts.length; k++ ) {
				//If the featureTypeStyle applies to the feature
				//TODO: There is no concept of FeatureTypeName in JUMP.
				//if ( fts[k].getFeatureTypeName() == null || featureTypeName.equals( fts[k].getFeatureTypeName() ) ) {
					Rule[] rules = fts[k].getRules();
					for ( int n = 0; n < rules.length; n++ ) {
						// does the filter rule apply?
						Filter filter = rules[n].getFilter();
						try {
							if (filter != null) {
//								org.deegree.model.feature.Feature auxFeat = new JUMPFToDeegreeFForFilteringPurpose(f);
								if (!filter.evaluate (deegreeF)) {
									continue;
								}
							}
							// Filter expression is true for this feature
							double scaleDenominator = computeScaleDenominator(viewport);
							double minScaleDenominator = rules[n].getMinScaleDenominator();
							double maxScaleDenominator = rules[n].getMaxScaleDenominator();
							Double scaleFactor = null;
							if (getLockSymbolSizes()) {
								double referenceScale = getReferenceScale();
								scaleFactor = new Double(referenceScale/scaleDenominator);
							}
							if ((scaleDenominator >= minScaleDenominator) && (scaleDenominator < maxScaleDenominator)) {
								// TODO: Check the scale
								Symbolizer[] symbolizers = rules[n].getSymbolizers();
								for ( int u = 0; u < symbolizers.length; u++ ) {
									//TODO: JUMP Features have just one geometry property
									// There is no need to use the geometry property of the symbolizer
									// PointSymbolizer
									if ( symbolizers[u] instanceof PointSymbolizer && (currentFilter&SLDStyle.PAINT_POINTS)!=0) {
										paintAsPoint(f, g, viewport, (PointSymbolizer)symbolizers[u],scaleFactor);
									} // LineSymbolizer
									else if ( symbolizers[u] instanceof LineSymbolizer &&(currentFilter&SLDStyle.PAINT_LINES)!=0) {
										paintAsLine(f, g, viewport, (LineSymbolizer)symbolizers[u],scaleFactor);
									} // PolygonSymbolizer
									else if ( symbolizers[u]instanceof PolygonSymbolizer && (currentFilter&SLDStyle.PAINT_POLYGONS)!=0) {
										paintAsPolygon(f, g, viewport, (PolygonSymbolizer)symbolizers[u],scaleFactor);
									} // TextSymbolizer 
									else if ( symbolizers[u] instanceof TextSymbolizer && (currentFilter&SLDStyle.PAINT_LABELS )!=0) {
										paintAsText(f, g, viewport, (TextSymbolizer)symbolizers[u],scaleFactor);
									}
								}
							}
						} catch (FilterEvaluationException e) {
							e.printStackTrace();
							//WorkbenchFrame.handleThrowable(e, null);
							continue;
						}
						catch (IllegalArgumentException ex)
						{
							ex.printStackTrace();
							if (f instanceof com.geopista.feature.GeopistaFeature){
								logger.error("Error al pintar la capa:"+((com.geopista.feature.GeopistaFeature) f).getLayer().getName());
							}
				
						if (logger.isDebugEnabled())
						logger.debug("paint(Feature, Graphics2D, Viewport): "+ex.getLocalizedMessage());
						}
					}
				//}
			}
		}
		return;
	}

	/**Método que calcula la escala de un mapa a partir del Bounding Box, la anchura y altura del mismo
	 * @return La escala del mapa
	 */
	private double computeScaleDenominator(IViewport viewport) {

		Envelope envelope = viewport.getEnvelopeInModelCoordinates();		
		double scale = 0.0;
		double xmin = envelope.getMinX();
		double ymin = envelope.getMinY();
		double xmax = envelope.getMaxX();
		double ymax = envelope.getMaxY();
		/*Calculate the width and height of the map in degrees*/
		double widthMap = xmax-xmin;
		double heightMap = ymax-ymin;
		/*Now we must calculate the map extent in metres*/
		//double widthMetres = (widthMap*(6378137*2*Math.PI))/360;
		//double heightMetres = (heightMap*(6378137*2*Math.PI))/360;
		double widthMetres = widthMap;
		double heightMetres = heightMap;
		/*Now we calculate the scale*/
		scale = ((widthMetres/viewport.getPanel().getWidth())/0.00028);
		return scale;
	}
	
	private UserStyle createBasicStyle(Color fillColor, Color strokeColor) {
		Symbolizer symbolizer;
		FeatureTypeStyle[] featureTypeStyles; 
		UserStyle theUserStyle;
		
		symbolizer = StyleFactory.createLineSymbolizer(strokeColor);
		featureTypeStyles = new FeatureTypeStyle[1];
		featureTypeStyles[0] = StyleFactory.createFeatureTypeStyle(symbolizer); 
		//TODO: This is an illegal cast 
		theUserStyle = (UserStyle) StyleFactory.createStyle("default", "Default user style", "Default user style", featureTypeStyles);
		return theUserStyle;
	}

	public void paintAsPoint(Feature f, Graphics2D g, IViewport viewport, PointSymbolizer symbolizer, Double scaleFactor) 
		throws NoninvertibleTransformException, FilterEvaluationException {

		Image image = createImage(f, symbolizer,scaleFactor);	
		// Point - Draw a point 
		if (f.getGeometry() instanceof com.vividsolutions.jts.geom.Point) {
			Shape shape = toShape(f.getGeometry(), viewport);
			drawPoint(shape, g, image);
		}
		// MultiPoint - Draw each point 
		else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.MultiPoint) {
			GeometryCollection collection = (GeometryCollection)f.getGeometry();
			for (int i = 0; i < collection.getNumGeometries(); i++) {
				Shape shape = toShape(collection.getGeometryN(i), viewport);
				drawPoint(shape, g, image);
			}	
		}
		// MultiLineString, MultiPolygon - Draw each centroid
		else if (f.getGeometry() instanceof GeometryCollection) {
			GeometryCollection collection = (GeometryCollection)f.getGeometry();
			for (int i = 0; i < collection.getNumGeometries(); i++) {
				Shape shape = toShape(collection.getGeometryN(i).getCentroid(), viewport);
				drawPoint(shape, g, image);
			}				
		}
		// LineString, Polygon - Draw the centroid
		else {
			Shape shape = toShape(f.getGeometry().getCentroid(), viewport);
			drawPoint(shape, g, image);
		}
	}

	public static Image createImage(Feature f, PointSymbolizer symbolizer,Double scaleFactor) throws FilterEvaluationException {
		
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
	
		Image image = new BufferedImage( 7, 7, BufferedImage.TYPE_INT_ARGB );
		image.getGraphics().setColor( Color.LIGHT_GRAY );		
		image.getGraphics().fillRect( 0, 0,  9, 9 );
		if (symbolizer.getGraphic() != null ) {
			Graphic_Impl graphic = (Graphic_Impl)symbolizer.getGraphic();
			ParameterValueType oldSize = graphic.getSize();
			double size = 8;
			if (f!= null) {
				 size = symbolizer.getGraphic().getSize(deegreeF);
			}
			if (scaleFactor != null) {
				size = size*scaleFactor.doubleValue();				
			}
			if (size < 1) {
				size = 1.0;
			}
			if (size > 1000) {
				size = 1000;
			}
			symbolizer.getGraphic().setSize(size);
			image = symbolizer.getGraphic().getAsImage(deegreeF);
			graphic.setSize(oldSize);
		}
        deegreeF=null;
		return image;
	}
	
	public static void drawPoint(Shape shape, Graphics2D g, Image image) {
		int x = (int)shape.getBounds2D().getCenterX() - (image.getWidth(null) / 2);
		int y = (int)shape.getBounds2D().getCenterY() - (image.getHeight(null) / 2);
		g.drawImage(image, x, y, null );					
	}

	public static void paintAsLine(Feature f, Graphics2D g, IViewport viewport, LineSymbolizer symbolizer, Double scaleFactor) 
		throws NoninvertibleTransformException, FilterEvaluationException {
		configureGraphicContext(f, g, symbolizer,scaleFactor);
	
		if (f.getGeometry() instanceof GeometryCollection) {
			GeometryCollection collection = (GeometryCollection)f.getGeometry();
			for (int i = 0; i < collection.getNumGeometries(); i++) {
				Shape shape = toShape(collection.getGeometryN(i), viewport);
				drawLine(shape, g);
			}
		}
		else {
			Shape shape = toShape(f.getGeometry(), viewport);
			drawLine(shape, g);
		}
			
	}

	public static void configureGraphicContext(Feature f, Graphics2D g, LineSymbolizer symbolizer, Double scaleFactor) 
		throws FilterEvaluationException {
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
		
		org.deegree.graphics.sld.Stroke stroke = symbolizer.getStroke();
		// no stroke defined -> don't draw anything
		if ( stroke == null ) return;
	
		// Color & Opacity	
		setColor( g, stroke.getStroke(deegreeF), stroke.getOpacity(deegreeF) );
		float[] dash = stroke.getDashArray(deegreeF);
		// use a simple Stroke if dash == null or its length < 2
		// that's faster
		float width = (float)1.0;
		try {
			if (f != null) {
				width = (float)stroke.getWidth(deegreeF);
			}
		} catch (FilterEvaluationException e) {
		}
		if (scaleFactor != null) {
			width = (float)(width*scaleFactor.doubleValue());
		}
		int cap = stroke.getLineCap(deegreeF);
		int join = stroke.getLineJoin(deegreeF);
		BasicStroke bs2 = null;
		if ( ( dash == null ) || ( dash.length < 2 ) ) {
			bs2 = new BasicStroke( width, cap, join );
		} else {
			bs2 = new BasicStroke( width, cap, join, 10.0f, dash, stroke.getDashOffset(deegreeF) );
		}
		g.setStroke( bs2 );
        deegreeF=null;
	}
	
	public static void drawLine(Shape shape, Graphics2D g) {
		g.draw(shape);			
	}

	public void paintAsPolygon(Feature f, Graphics2D g, IViewport viewport, PolygonSymbolizer symbolizer, Double scaleFactor) 
		throws NoninvertibleTransformException, FilterEvaluationException  {
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
		org.deegree.graphics.sld.Fill fill = symbolizer.getFill();
		org.deegree.graphics.sld.Stroke stroke = symbolizer.getStroke();

		if ( fill != null ) {
			configureGraphicContextForFilling(f,g,symbolizer);
			if (f.getGeometry() instanceof GeometryCollection) {
				GeometryCollection collection = (GeometryCollection)f.getGeometry();
				for (int i = 0; i < collection.getNumGeometries(); i++) {
					Shape shape = toShape(collection.getGeometryN(i), viewport);
					g.fill(shape);
				}	
			}
			else {
				Shape shape = toShape(f.getGeometry(), viewport);
				g.fill(shape);
			}
		}

		// only stroke outline, if Stroke-Element is given
		if ( stroke != null ) {
			configureGraphicContextForStroking(f,g,symbolizer,scaleFactor);
			if (f.getGeometry() instanceof GeometryCollection) {
				GeometryCollection collection = (GeometryCollection)f.getGeometry();
				for (int i = 0; i < collection.getNumGeometries(); i++) {
					Shape shape = toShape(collection.getGeometryN(i), viewport);
					g.draw(shape);
				}	
			}
			else {
				Shape shape = toShape(f.getGeometry(), viewport);
				g.draw(shape);
			}
		}
	}
	
	public static void configureGraphicContextForFilling(Feature f, Graphics2D g, PolygonSymbolizer symbolizer) 
		throws FilterEvaluationException {
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
		org.deegree.graphics.sld.Fill fill = symbolizer.getFill();

		if ( fill != null ) {
			double opacity = fill.getOpacity(deegreeF);
			Color color = fill.getFill(deegreeF);               
//			int alpha = (int)Math.round( opacity * 255 );
			int red = color.getRed();
			int green = color.getGreen();
			int blue = color.getBlue();
			color = new Color( red, green, blue);//, alpha );
			GraphicFill gFill = fill.getGraphicFill();
			if ( gFill == null ) {
				setColor(g, color,opacity );
			}
			else {
				BufferedImage gFillImage = gFill.getGraphic().getAsImage(deegreeF);
				int gFillWidth = gFillImage.getWidth(null);
				int gFillHeight = gFillImage.getHeight(null); 						
				BufferedImage texture = new BufferedImage(gFillWidth, gFillHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics2D textureg = texture.createGraphics();
				textureg.drawImage(gFillImage, 0, 0, color, null); 
				Rectangle anchor = new Rectangle( 0, 0, gFillWidth, gFillHeight);
				g.setPaint( new TexturePaint( texture, anchor ) );
			}
		}
		deegreeF=null;
	}

	public static void configureGraphicContextForStroking(Feature f, Graphics2D g, PolygonSymbolizer symbolizer, Double scaleFactor) 
		throws FilterEvaluationException {
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
		org.deegree.graphics.sld.Stroke stroke = symbolizer.getStroke();

		if ( stroke != null ) {
			double opacity = stroke.getOpacity(deegreeF);
			if ( opacity > 0.01 ) {
				Color color = stroke.getStroke(deegreeF);
//				int alpha = (int)Math.round( opacity * 255 );
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				color = new Color( red, green, blue);//, alpha );
				setColor(g, color, opacity );
				
				float[] dash = stroke.getDashArray(deegreeF);
				// use a simple Stroke if dash == null or dash length < 2
				BasicStroke bs2 = null;
				float w = (float)1.0;
				try {
					if (f != null) {
						w = (float)stroke.getWidth(deegreeF);
					}
				} catch (FilterEvaluationException e) {}
				if ( ( dash == null ) || ( dash.length < 2 ) ) {
					bs2 = new BasicStroke( w );
				} else {
					bs2 = new BasicStroke( w, stroke.getLineCap(deegreeF), stroke.getLineJoin(deegreeF), 10.0f, dash, stroke.getDashOffset(deegreeF) );
				}
				g.setStroke( bs2 );
			}
		}
        deegreeF=null;
	}

	public void paintAsText(Feature f, Graphics2D g, IViewport viewport, TextSymbolizer symbolizer, Double scaleFactor) 
		throws NoninvertibleTransformException, FilterEvaluationException  {
		boolean isPointPlacement;
	
		// TODO: No label placement means no label is placed, ain't it?
		LabelPlacement lPlacement = symbolizer.getLabelPlacement();
		if (lPlacement == null) return;
		// TODO: Find out wheter it is point placement or line placement
		PointPlacement pPlacement = lPlacement.getPointPlacement();
		if (pPlacement != null) isPointPlacement = true;
		else isPointPlacement = false;

		org.deegree.graphics.sld.Font sldFont = symbolizer.getFont();
		java.awt.Font font = createFont(f,symbolizer,scaleFactor);
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
		ParameterValueType label = symbolizer.getLabel();
		g.setFont (font);

		String caption = label.evaluate(deegreeF);		
		// sanity check: empty labels are ignored
		if (caption == null || caption.trim().equals ("") || caption.equals("null")) {
			return;
		}
		FontRenderContext frc = g.getFontRenderContext();
		//Rectangle2D bounds    = font.getStringBounds( caption, frc );
		
		/****************************
		 * JPC: support multiline string
		 */
		 //     JPC: manage multiple lines captions
        String[] captionlines=caption.split("\\n|\\\\n");
        Rectangle2D bounds = font.getStringBounds(captionlines[0], frc);
        for (int i = 1; i < captionlines.length; i++)
			{
			Rectangle2D boundofline = font.getStringBounds(captionlines[i], frc);
			bounds.add(bounds.getMaxX()>boundofline.getMaxX()?0:boundofline.getMaxX()-bounds.getMaxX(),
					boundofline.getHeight());			
			}
        
		/**
		 * END
		 */
		
		
		LineMetrics metrics   = font.getLineMetrics( caption, frc );
		//******************SECCIÖN AÑADIDA POR ENXENIO****************************
		//*************************************************************************
				double minx = bounds.getMinX();
		double miny = bounds.getMinY();
		Point2D minPoint2D = new Point2D.Double(minx,miny);
		double maxx = bounds.getMaxX();
		double maxy = bounds.getMaxY();
		Point2D maxPoint2D = new Point2D.Double(maxx,maxy);
		Coordinate min = viewport.toModelCoordinate(minPoint2D);
		Coordinate max = viewport.toModelCoordinate(maxPoint2D);
		int anchoLinea = (int)Math.round(max.x - min.x);
		int altoLinea = (int) (min.y - max.y);
		//***************FIN NUEVA SECCIÓN*****************************************
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();
		int descent = (int) metrics.getDescent ();

		if (isPointPlacement) {
			// Point
			if (f.getGeometry() instanceof Point)
				labelPoint(f, (Point)f.getGeometry(), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
			// MultiPoint 
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.MultiPoint) {
				GeometryCollection collection = (GeometryCollection)f.getGeometry();
				for (int i = 0; i < collection.getNumGeometries(); i++)
					labelPoint(f, (Point)collection.getGeometryN(i), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
			}
			// LineString, LinearRing
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.LineString)
				{
				labelPoint(f, f.getGeometry().getCentroid(), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
				}
			// MultiLineString
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.MultiLineString) {
				GeometryCollection collection = (GeometryCollection)f.getGeometry();
				for (int i = 0; i < collection.getNumGeometries(); i++)
                {
                    if(i%3==0||i==0||i==collection.getNumGeometries()-1) labelPoint(f, collection.getGeometryN(i).getCentroid(), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
                }
			}
			// Polygon
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.Polygon)
				labelPoint(f, f.getGeometry().getCentroid(), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
			
			// MultiPolygon
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.MultiPolygon) {
				GeometryCollection collection = (GeometryCollection)f.getGeometry();
				for (int i = 0; i < collection.getNumGeometries(); i++)
					labelPoint(f, collection.getGeometryN(i).getCentroid(), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
			}
		}
		// if it is line placement
		else { 
			// Point
			if (f.getGeometry() instanceof Point) {
				Point thePoint = (Point)f.getGeometry();
				/*Coordinate[] coordinates = { 
					new Coordinate(thePoint.getX()-(w/2.0), thePoint.getY()), new Coordinate(thePoint.getX()+(w/2.0), thePoint.getY())};*/
				//********************MODIFICADO POR ENXENIO***********************************					
				Coordinate[] coordinates = { 
					new Coordinate(thePoint.getX()-(anchoLinea/2.0), thePoint.getY()), new Coordinate(thePoint.getX()+(anchoLinea/2.0), thePoint.getY())};					
				GeometryFactory gf = new GeometryFactory();
				LineString line = thePoint.getFactory().createLineString(coordinates);
				//labelLine(f, line, g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
				//********************MODIFICADO POR ENXENIO***********************************	
				labelLineWithPointGeometry(f, line, g, viewport, symbolizer, caption, font, anchoLinea, h, sldFont, metrics);
			}
			// MultiPoint 
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.MultiPoint) {
				GeometryCollection collection = (GeometryCollection)f.getGeometry();
				for (int i = 0; i < collection.getNumGeometries(); i++) {
					Point thePoint = (Point)collection.getGeometryN(i);
					/*Coordinate[] coordinates = { 
						new Coordinate(thePoint.getX()-(w/2.0), thePoint.getY()), new Coordinate(thePoint.getX()+(w/2.0), thePoint.getY())};*/
					//********************MODIFICADO POR ENXENIO***********************************					
					Coordinate[] coordinates = { 
						new Coordinate(thePoint.getX()-(anchoLinea/2.0), thePoint.getY()), new Coordinate(thePoint.getX()+(anchoLinea/2.0), thePoint.getY())};							
					GeometryFactory gf = new GeometryFactory();
					LineString line = thePoint.getFactory().createLineString(coordinates);
					//labelLine(f, line, g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
					//********************MODIFICADO POR ENXENIO***********************************	
					labelLineWithPointGeometry(f, line, g, viewport, symbolizer, caption, font, anchoLinea, h, sldFont, metrics);
				}
			}
			// LineString, LinearRing
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.LineString)
				labelLine(f, (LineString)f.getGeometry(), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
			// MultiLineString
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.MultiLineString) {
				GeometryCollection collection = (GeometryCollection)f.getGeometry();
				for (int i = 0; i < collection.getNumGeometries(); i++)
					labelLine(f, (LineString)collection.getGeometryN(i), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
			}
			// Polygon
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.Polygon) {
				Polygon thePolygon = (Polygon)f.getGeometry();
				labelLine(f, thePolygon.getExteriorRing(), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);
			}
			// MultiPolygon
			else if (f.getGeometry() instanceof com.vividsolutions.jts.geom.MultiPolygon) {
				GeometryCollection collection = (GeometryCollection)f.getGeometry();
				for (int i = 0; i < collection.getNumGeometries(); i++) {
					Polygon thePolygon = (Polygon)collection.getGeometryN(i);
					labelLine(f, thePolygon.getExteriorRing(), g, viewport, symbolizer, caption, font, w, h, sldFont, metrics);					
				}
			}
		}
        deegreeF=null;
	}
	
	public static java.awt.Font createFont(Feature f, TextSymbolizer symbolizer, Double scaleFactor) throws FilterEvaluationException{
		// gather font information
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
		org.deegree.graphics.sld.Font sldFont = symbolizer.getFont();
		int fontSize = 8;
		try { 
			fontSize = sldFont.getSize (deegreeF);
		} catch (FilterEvaluationException e) {}
		if (scaleFactor != null) {
			fontSize = (int)(fontSize*scaleFactor.doubleValue());
		}
		java.awt.Font font = new java.awt.Font( sldFont.getFamily (deegreeF), sldFont.getStyle(deegreeF)|sldFont.getWeight (deegreeF), fontSize);
        deegreeF=null;
		return font;
	}

	private void labelPoint(Feature f, Point geo, Graphics2D g, IViewport viewport, TextSymbolizer symbolizer, String caption, java.awt.Font font, int w, int h, org.deegree.graphics.sld.Font sldFont, LineMetrics metrics) throws NoninvertibleTransformException, FilterEvaluationException  {
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);

		// I now that the geometry is a point
		// get screen coordinates
		Shape shape = toShape(geo, viewport);
		//TODO: This is not right
		if (!(shape instanceof Rectangle2D)) {
			// EAT IT
		}
		else {
			int x = (int)((Rectangle2D)shape).getX();
			int y = (int)((Rectangle2D)shape).getY();
			// default placement information
			double rotation = 0.0;
			double[] anchorPoint = { 0, 0.5 };
			double[] displacement = { 0.0, 0.0 };
			// use placement information from SLD
			// I am sure there is a label placement
			LabelPlacement lPlacement = symbolizer.getLabelPlacement();			
			PointPlacement pPlacement = lPlacement.getPointPlacement();
			//I am sure that there is a pointPlacement
			anchorPoint = pPlacement.getAnchorPoint(deegreeF);
			displacement = pPlacement.getDisplacement(deegreeF);
			//[JPC 15-11-06] Deegree uses rotation in sexagesimal degrees 
			//rotation = Math.toRadians(pPlacement.getRotation(deegreeF));
			rotation = pPlacement.getRotation(deegreeF);
			Label labelElement = LabelFactory.createLabel (caption, font, sldFont.getColor(deegreeF), metrics, deegreeF, symbolizer.getHalo(), x, y, w, h, rotation, anchorPoint, displacement);
			labelElement.paint(g);
		}
        deegreeF=null;
	}

	private void labelLine(Feature f, LineString geo, Graphics2D g, IViewport viewport, TextSymbolizer symbolizer, String caption, java.awt.Font font, int w, int h, org.deegree.graphics.sld.Font sldFont, LineMetrics metrics) throws NoninvertibleTransformException, FilterEvaluationException  {
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
	
		Shape shape = toShape(geo, viewport);
		double perpendicularOffset = 0.0;
		int placementType = LinePlacement.TYPE_ABSOLUTE;
		double lineWidth = 3.0;
		int gap = 6;
		// I am sure there is a label placement and a line placement
		LinePlacement linePlacement = symbolizer.getLabelPlacement().getLinePlacement();			
		placementType = linePlacement.getPlacementType (deegreeF);
		perpendicularOffset = linePlacement.getPerpendicularOffset (deegreeF);
		lineWidth = linePlacement.getLineWidth (deegreeF);
		gap = linePlacement.getGap (deegreeF);
		// ideal distance from the line			
		double delta = h / 2.0 + lineWidth / 2.0;	
	
		PathIterator iterator = shape.getPathIterator(null);
		double[] segment = new double[6];
		int segmentType;
		if(!iterator.isDone()) {
			iterator.currentSegment(segment);
			int lastX = (int)segment[0];
			int lastY = (int)segment[1];
			int boxStartX = lastX;
			int boxStartY = lastY;
			iterator.next();		
			while (!iterator.isDone()) {
				iterator.currentSegment(segment);
				iterator.next();
				int x = (int)segment[0];
				int y = (int)segment[1];
				// segment found where endpoint of box should be located?
				//if (LabelFactory.getDistance (boxStartX, boxStartY, x, y) >= w) {
				if (w - LabelFactory.getDistance (boxStartX, boxStartY, x, y) <= OVERLAP_MARGIN) {
					int [] p0 = new int [] { boxStartX, boxStartY };
					int [] p1 = new int [] { lastX, lastY };
					int [] p2 = new int [] { x, y };
					int [] p = LabelFactory.findPointWithDistance (p0, p1, p2, w);
					x = p [0]; y = p [1];
					//lastX = x; lastY = y;
					//***********MODIFICADO***************
					lastX = p2[0]; lastY = p2[1];
					int boxEndX = x;
					int boxEndY = y;
					// does the linesegment run from right to left?
					if (x <= boxStartX) {
						boxEndX = boxStartX;
						boxEndY = boxStartY;
						boxStartX = x;
						boxStartY = y;
						x = boxEndX;
						y = boxEndY;
					}
					double rotation = LabelFactory.getRotation (boxStartX, boxStartY, x, y);
					//double [] deviation = LabelFactory.calcDeviation (new int [] {boxStartX, boxStartY}, new int [] {boxEndX, boxEndY}, eCandidates);
					Label labelElement = LabelFactory.createLabel (caption, font, sldFont.getColor (deegreeF), metrics, deegreeF, symbolizer.getHalo(), boxStartX, boxStartY, w, h, rotation, 0, 0.5, 0, perpendicularOffset);
					labelElement.paint(g);
				}				
				boxStartX = lastX;
				boxStartY = lastY;
			}
		}
        deegreeF=null;
	}
	
	//**********************NUEVO MÉTODO PARA LAS ETIQUETAS CON GEOMETRIAS DE PUNTOS Y ASOCIADAS A LÍNEAS
	private void labelLineWithPointGeometry(Feature f, LineString geo, Graphics2D g, IViewport viewport, TextSymbolizer symbolizer, String caption, java.awt.Font font, int w, int h, org.deegree.graphics.sld.Font sldFont, LineMetrics metrics) throws NoninvertibleTransformException, FilterEvaluationException  {
		org.deegree.model.feature.Feature deegreeF = new JUMPFeatureToDeegreeFeatureMapper(f);
	
		Envelope bufferedEnvelope = EnvelopeUtil.bufferByFraction(viewport.getEnvelopeInModelCoordinates(), 0.05);
		if (!bufferedEnvelope.contains(geo.getEnvelopeInternal())) {
			try {
				Geometry actualGeometry = geo;
				geo = (LineString)EnvelopeUtil.toGeometry(bufferedEnvelope).intersection(actualGeometry);
			} catch (Exception e) {
			}
		}
		Shape shape = viewport.getJava2DConverter().toShape(geo);
		double perpendicularOffset = 0.0;
		int placementType = LinePlacement.TYPE_ABSOLUTE;
		double lineWidth = 3.0;
		int gap = 6;
		LinePlacement linePlacement = symbolizer.getLabelPlacement().getLinePlacement();			
		placementType = linePlacement.getPlacementType (deegreeF);
		perpendicularOffset = linePlacement.getPerpendicularOffset (deegreeF);
		lineWidth = linePlacement.getLineWidth (deegreeF);
		gap = linePlacement.getGap (deegreeF);
		//ideal distance from the line			
		double delta = h / 2.0 + lineWidth / 2.0;		
		PathIterator iterator = shape.getPathIterator(null);
		double[] segment = new double[6];
		int segmentType;
		if(!iterator.isDone()) {
			iterator.currentSegment(segment);
			int lastX = (int)segment[0];
			int lastY = (int)segment[1];
			int boxStartX = lastX;
			int boxStartY = lastY;
			iterator.next();		
			while (!iterator.isDone()) {
				iterator.currentSegment(segment);
				iterator.next();
				int x = (int)segment[0];
				int y = (int)segment[1];
				Coordinate min = geo.getCoordinateN(0);
				Coordinate max = geo.getCoordinateN(1);
				int anchoLinea = (int)Math.round(max.x - min.x);
				//Comparamos en unidades UTM en vez de pixels, debido a la "sospechosa" transformación a pixels
				//que se hace en el shape
				if (anchoLinea >= w) {
					int [] p0 = new int [] { boxStartX, boxStartY };
					int [] p1 = new int [] { lastX, lastY };
					int [] p2 = new int [] { x, y };
					int [] p = LabelFactory.findPointWithDistance (p0, p1, p2, w);
					x = p [0]; y = p [1];
					//lastX = x; lastY = y;
					lastX = p2[0]; lastY = p2[1];
					int boxEndX = x;
					int boxEndY = y;
					// does the linesegment run from right to left?
					if (x <= boxStartX) {
						boxEndX = boxStartX;
						boxEndY = boxStartY;
						boxStartX = x;
						boxStartY = y;
						x = boxEndX;
						y = boxEndY;
					}
					double rotation = 0.0;
					//Para escalas muy pequeñas, boxStartX == x y boxStartY == y, con lo que el rotation que
					//se devolvía era NaN, y eso provocaba que las etiquetas no se pintasen como deberían
					if ((boxStartX != x)&&(boxStartY != y)) {
						rotation = LabelFactory.getRotation (boxStartX, boxStartY, x, y);
					}
					Label labelElement = LabelFactory.createLabel (caption, font, sldFont.getColor (deegreeF), metrics, deegreeF, symbolizer.getHalo(), boxStartX, boxStartY, w, h, rotation, 0, 0.5, 0, perpendicularOffset);
					labelElement.paint(g);
				}				
				boxStartX = lastX;
				boxStartY = lastY;
			}
		}
        deegreeF=null;
	}
	
	/**
	 *
	 *
	 * @param g2 
	 * @param color 
	 * @param opacity 
	 *
	 * @return 
	 */
	private static Graphics2D setColor( Graphics2D g2, Color color, double opacity ) {
		if ( opacity < 0.999 ) {
			final int alpha = (int)Math.round( opacity * 255 );
			final int red = color.getRed();
			final int green = color.getGreen();
			final int blue = color.getBlue();
			if (opacity==1.0) // if color has no alpha then printing retains vector nature.
				color=new Color(red,green,blue);
			else
			color = new Color( red, green, blue, alpha );
		}

		g2.setColor( color );
		return g2;
	}      	

	private static Shape toShape(Geometry geometry, IViewport viewport)
		throws NoninvertibleTransformException {
		//At high magnifications, Java rendering can be sped up by clipping
		//the Geometry to only that portion visible inside the viewport.
		//Hence the code below. [Jon Aquino] 
		// It renders poorly in the printing proccess due to rounding distorsion in the printing bands.
		Geometry actualGeometry = geometry;
		if (false)
		{
		Envelope bufferedEnvelope = EnvelopeUtil.bufferByFraction(viewport
				.getEnvelopeInModelCoordinates(), 0.05);
		
		if (!bufferedEnvelope.contains(actualGeometry.getEnvelopeInternal()))
			{
			try
				{
				actualGeometry = EnvelopeUtil.toGeometry(bufferedEnvelope)
						.intersection(actualGeometry);
				}
			catch (Exception e)
				{
				// Can get a TopologyException if the Geometry is invalid. Eat
				// it. [Jon Aquino]
				// Can get an AssertionFailedException (unable to assign hole to
				// a shell)
				// at high magnifications. Eat it. [Jon Aquino]
				// Alvaro Zabala reports that we can get here with an
				// IllegalArgumentException (points must form a closed
				// linestring)
				// for bad geometries. Eat it. [Jon Aquino]
				}
			}
		}
		return viewport.getJava2DConverter().toShape(actualGeometry);
	}
	
	public void createSLDFile() {
		
		try {
			_stylePermanentChanged = true;
			File sldFile = new File(_sldFileName);
			FileOutputStream fosSLD = new FileOutputStream(sldFile);
			OutputStreamWriter oswSLD = new OutputStreamWriter(fosSLD);
			PrintWriter pwSLD = new PrintWriter(oswSLD);
			writeSLDFile(pwSLD);
			pwSLD.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}
	public void createSLDFile(String encoding) {
		
		try {
			_stylePermanentChanged = true;
			File sldFile = new File(_sldFileName);
			FileOutputStream fosSLD = new FileOutputStream(sldFile);
			OutputStreamWriter oswSLD = new OutputStreamWriter(fosSLD,encoding);
			PrintWriter pwSLD = new PrintWriter(oswSLD);
			writeSLDFile(pwSLD,encoding);
			pwSLD.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	public void createSLDFile2() {
		
		try {
			_stylePermanentChanged = true;
			File sldFile = new File(_sldFileName);
			FileOutputStream fosSLD = new FileOutputStream(sldFile);
			OutputStreamWriter oswSLD = new OutputStreamWriter(fosSLD);
			PrintWriter pwSLD = new PrintWriter(oswSLD);
			writeSLDFile2(pwSLD);
			pwSLD.close();
			JOptionPane.showMessageDialog(null, aplicacion.getI18nString("SLDStyle.EstiloExportado"));
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}
	private void writeSLDFile(PrintWriter pwSLD) {
		writeSLDFile(pwSLD,"ISO-8859-1");
	}

	private String replaceCharacters(String nombre){
		try{
			nombre=nombre.replaceAll("<", "&lt;");	
			nombre=nombre.replaceAll(">", "&gt;");	
		}
		catch (Exception e){
			
		}
		return nombre;
	}
	
	private void writeSLDFile(PrintWriter pwSLD,String encoding) {

		pwSLD.println("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>");
		pwSLD.println("<StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		Iterator layersIterator = _layers.iterator();
		while (layersIterator.hasNext()) {
			org.deegree.graphics.sld.Layer layer = (org.deegree.graphics.sld.Layer)layersIterator.next();
			if ((layer.getName()).equals(_actualLayerName)) {
				//List styles = (List) _layersStyles.get(layer.getName());
				List styles = _userStyles;
				org.deegree.graphics.sld.Style[] styleArray = {};
				styleArray = (org.deegree.graphics.sld.Style[])styles.toArray(styleArray);
				
				try{
					for (int i=0;i<styleArray.length;i++){
						org.deegree_impl.graphics.sld.UserStyle_Impl userStyle=(org.deegree_impl.graphics.sld.UserStyle_Impl)styleArray[i];
						
						String newName=replaceCharacters(userStyle.getName());
						userStyle.setName(newName);	
	
						String newTitle=replaceCharacters(userStyle.getTitle());
						userStyle.setTitle(newTitle);	
						
						String newAbstract=replaceCharacters(userStyle.getAbstract());
						userStyle.setAbstract(newAbstract);	
						
						
						FeatureTypeStyle featureTypeStyles[]=userStyle.getFeatureTypeStyles();
						for (int j=0;j<featureTypeStyles.length;j++){
							FeatureTypeStyle featureTypeStyle=(FeatureTypeStyle)featureTypeStyles[j];
							Rule rules[]=featureTypeStyle.getRules();
							for (int k=0;k<rules.length;k++){
								Rule rule=rules[k];
								String nombre=rule.getName();
								try{
									nombre=nombre.replaceAll("<", "&lt;");	
									nombre=nombre.replaceAll(">", "&gt;");	
								}
								catch (Exception e){}
								rule.setName(nombre);
							}
						}	
					}
				}
				catch (Exception e){
					
				}
					//org.deegree.graphics.sld.Style userStyle=(org.deegree.graphics.sld.Style)styleArray[i];
					//userStyle.				
				layer.setStyles(styleArray);
				if (layer instanceof org.deegree.graphics.sld.NamedLayer) {
					NamedLayer_Impl namedLayer = (NamedLayer_Impl)layer;
					pwSLD.println(namedLayer.exportAsXML());
				}
				else if (layer instanceof org.deegree.graphics.sld.UserLayer) {
					UserLayer_Impl userLayer = (UserLayer_Impl)layer;
					pwSLD.println(userLayer.exportAsXML());
				}
			}
			else {
				if (layer instanceof org.deegree.graphics.sld.NamedLayer) {
					NamedLayer_Impl namedLayer = (NamedLayer_Impl)layer;
					pwSLD.println(namedLayer.exportAsXML());
				}
				else if (layer instanceof org.deegree.graphics.sld.UserLayer) {
					UserLayer_Impl userLayer = (UserLayer_Impl)layer;
					pwSLD.println(userLayer.exportAsXML());
				}					
			}
		}
		pwSLD.println("</StyledLayerDescriptor>");
	}

	private void writeSLDFile2(PrintWriter pwSLD) {

		pwSLD.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		pwSLD.println("<StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		Iterator layersIterator = _layers.iterator();
		while (layersIterator.hasNext()) {
			org.deegree.graphics.sld.Layer layer = (org.deegree.graphics.sld.Layer)layersIterator.next();
			if ((layer.getName()).equals(_actualLayerName)) {
				//List styles = (List) _layersStyles.get(layer.getName());
				int m = _selectedStyles.length;
				org.deegree.graphics.sld.Style[] styleArray = new org.deegree.graphics.sld.Style[m];
				int n = _userStyles.size();
				for (int i=0;i<n;i++){
					int j = 0;
					for (j=0;j<m;j++){
						UserStyle userStyle = (UserStyle)_userStyles.get(i);
						java.util.StringTokenizer st = new java.util.StringTokenizer((String)_selectedStyles[j],":");
						String estilo = (String)_selectedStyles[j];
						while (st.hasMoreTokens())
							estilo = (String)st.nextToken();
						if (userStyle.getName().equals(estilo) || userStyle.getName().equals(_selectedStyles[j])){
							styleArray[j]=(org.deegree.graphics.sld.Style)userStyle;
							break;
						}
					}
				}
				layer.setStyles(styleArray);
				if (layer instanceof org.deegree.graphics.sld.NamedLayer) {
					NamedLayer_Impl namedLayer = (NamedLayer_Impl)layer;
					pwSLD.println(namedLayer.exportAsXML());
				}
				else if (layer instanceof org.deegree.graphics.sld.UserLayer) {
					UserLayer_Impl userLayer = (UserLayer_Impl)layer;
					pwSLD.println(userLayer.exportAsXML());
				}
			}
		}
		pwSLD.println("</StyledLayerDescriptor>");
	}

	
	private int _type;	
	private boolean _estiloLocal = false;
	private boolean _stylePermanentChanged = false;
	private String _sldFileName;
	private HashMap _layersStyles = new HashMap();
	private String _actualLayerName;
	private String _currentStyleName = null;
	private List _layers = new ArrayList();
	private List _userStyles = new ArrayList();
	private String _systemId;
	private Object[] _selectedStyles;
	
	public Object[] getSelectedStyles()
	{
		return _selectedStyles;
	}

	public void setSelectedStyles(Object[] selectedStyles)
	{
		_selectedStyles = selectedStyles;
	}

	public String getSystemId()
	{
	  return _systemId;
	}

	public void setSystemId(String new_systemId)
	{
	  _systemId = new_systemId;
	}

	public void setLayers(List new_layers)
	{
	  _layers = new_layers;
	}

	public int currentFilter=SLDStyle.PAINT_LABELS|SLDStyle.PAINT_POINTS|SLDStyle.PAINT_LINES|SLDStyle.PAINT_POLYGONS;
	/* Allows invokers to activate only one kind of rendering to allow
	 * the rendering in a layered manner, i.e. poligon, point, labels in subsequent calls to paint
	 * As SLDStyle combines all kind of Symbolizers in the same Style this trick was considered a good
	 * compromise for Geopista.
	 * 
	 * @param type bit field 1-paint labels, 2-paint points, 4-paint lines, 8-paint poligons
	 * @see com.geopista.style.sld.model.SLDStyle#setStyleTypeFilter(int)
	 */
	public void setStyleTypeFilter(int type)
	{
	currentFilter=type;
	
	}	
}