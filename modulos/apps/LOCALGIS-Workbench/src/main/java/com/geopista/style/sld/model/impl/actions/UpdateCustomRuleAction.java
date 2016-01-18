/**
 * UpdateCustomRuleAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 27-jul-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.awt.Color;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.deegree.graphics.sld.ExternalGraphic;
import org.deegree.graphics.sld.Fill;
import org.deegree.graphics.sld.Font;
import org.deegree.graphics.sld.Graphic;
import org.deegree.graphics.sld.GraphicFill;
import org.deegree.graphics.sld.LabelPlacement;
import org.deegree.graphics.sld.LinePlacement;
import org.deegree.graphics.sld.LineSymbolizer;
import org.deegree.graphics.sld.Mark;
import org.deegree.graphics.sld.ParameterValueType;
import org.deegree.graphics.sld.PointPlacement;
import org.deegree.graphics.sld.PointSymbolizer;
import org.deegree.graphics.sld.PolygonSymbolizer;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Stroke;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.TextSymbolizer;
import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree_impl.graphics.sld.StyleFactory;
import org.deegree_impl.graphics.sld.StyleFactory2;

import com.geopista.style.sld.model.ScaleRange;
/**
 * @author enxenio s.l.
 *
 */
public class UpdateCustomRuleAction {

	private String _ruleName;
	private ScaleRange _scaleRange;
	private HashMap _style;
	private Filter _filter;
	private Integer _insert;
	private Rule _rule;

	
	public UpdateCustomRuleAction(String ruleName,ScaleRange scaleRange,
		HashMap style,Filter filter,Integer insert,Rule rule) {
			
		_ruleName = ruleName;
		_scaleRange = scaleRange;
		_style = style;
		_filter = filter;
		_insert = insert;
		_rule = rule;
	}
	
	public Object doExecute() {
		
		if (_ruleName != null) {
			_rule.setName(_ruleName);
		}
		_rule.setMinScaleDenominator(_scaleRange.getMinScale().doubleValue());
		_rule.setMaxScaleDenominator(_scaleRange.getMaxScale().doubleValue());

		if (_filter != null) {		
			_rule.setFilter(_filter);
		}
		else {
			_rule.setFilter(null);
		}
		if (!_style.isEmpty()) {
			Symbolizer[] symbolizerArray = createSymbolizer(_style); 
			_rule.setSymbolizers(symbolizerArray);
		}
		if (_insert.intValue() == 1) {
			_scaleRange.addRule(_rule);
		}
		return _rule;
	}
		
	private Symbolizer[] createSymbolizer(HashMap style) {
		
		Symbolizer[] symbolizerArray = new Symbolizer[1];
		String symbolizerType = (String)style.get("SymbolizerType");
		if ((symbolizerType.toLowerCase()).equals("pointgraphic")) {
			createPointGraphic(style,symbolizerArray);
		}
		else if ((symbolizerType.toLowerCase()).equals("pointmark")) {
			createPointMark(style,symbolizerArray);
		}
		else if ((symbolizerType.toLowerCase()).equals("line")) {
			createLine(style,symbolizerArray);
		}
		else if ((symbolizerType.toLowerCase()).equals("polygon")) {
			createPolygon(style,symbolizerArray);
		}
		else if ((symbolizerType.toLowerCase()).equals("textline")) {
			createTextLine(style,symbolizerArray);
		}
		else if ((symbolizerType.toLowerCase()).equals("textpoint")) {
			createTextPoint(style,symbolizerArray);
		}		
		return symbolizerArray;
	}
	
	private void createPointGraphic(HashMap style,Symbolizer[] symbolizer) {
		
		String url = (String)style.get("Url");
		String format = (String)style.get("Format");
		Expression sizeAsExpression = (Expression)style.get("Size");
		Expression[] expressionArray = new Expression[]{sizeAsExpression};
		Double rotationAsDouble = (Double)style.get("Rotation");
		double rotation = 0.0;
		if (rotationAsDouble != null) {
			rotation = rotationAsDouble.doubleValue();
		}
		Double opacityAsDouble = (Double)style.get("Opacity");
		double opacity = 1.0;
		if (opacityAsDouble != null){
			opacity = opacityAsDouble.doubleValue(); 
		}
		try {
			ExternalGraphic externalGraphic = StyleFactory.createExternalGraphic(url,format);
			ParameterValueType sizeParameterValueType = StyleFactory.createParameterValueType(expressionArray);
			Graphic graphic = StyleFactory2.createGraphic(externalGraphic,null,opacity,
				sizeParameterValueType,rotation);
			PointSymbolizer pointSymbolizer = StyleFactory.createPointSymbolizer(graphic);
			symbolizer[0] = pointSymbolizer;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void createPointMark(HashMap style, Symbolizer[] symbolizer) {
		
		String wellKnownName = (String)style.get("WellKnownName");
		Integer colorFillAsInteger = (Integer)style.get("ColorFill");
		Double opacityFillAsDouble = (Double)style.get("OpacityFill");
		double opacityFill = 1.0;
		if (opacityFillAsDouble != null) {
			opacityFill = opacityFillAsDouble.doubleValue();
		}
		Color colorFill = new Color(colorFillAsInteger.intValue());
		Fill fill = StyleFactory.createFill(colorFill,opacityFill);
		Integer colorStrokeAsInteger = (Integer)style.get("ColorStroke");
		Expression widthAsExpression = (Expression)style.get("Width");		
		Double opacityStrokeAsDouble = (Double)style.get("OpacityStroke");
		double opacityStroke = 1.0;
		if (opacityStrokeAsDouble != null) {
			opacityStroke = opacityStrokeAsDouble.doubleValue();
		}
		float[] dashArray = (float[])style.get("DashArray");
		String lineJoin = "mitre";
		if (style.get("LineJoin") != null) {
			lineJoin = (String)style.get("LineJoin");
		}
		String lineCap = "butt";
		if (style.get("LineCap") != null) {
			lineCap = (String)style.get("LineCap");
		}
		Color colorStroke = new Color(colorStrokeAsInteger.intValue());
		Stroke stroke = StyleFactory2.createStroke(colorStroke,widthAsExpression,opacityStroke,
			dashArray,lineJoin,lineCap);
		Mark mark = StyleFactory.createMark(wellKnownName,fill,stroke);
		Expression sizeAsExpression = (Expression)style.get("Size");
		Expression[] expressionArray = new Expression[]{sizeAsExpression};
		ParameterValueType sizeParameterValueType = StyleFactory.createParameterValueType(expressionArray);
		Double rotationAsDouble = (Double)style.get("Rotation");
		double rotation = 0.0;
		if (rotationAsDouble != null) {
			rotation = rotationAsDouble.doubleValue();
		}
		Double opacityAsDouble = (Double)style.get("Opacity");
		double opacity = 1.0;
		if (opacityAsDouble != null){
			opacity = opacityAsDouble.doubleValue(); 
		}
		Graphic graphic = StyleFactory2.createGraphic(null,mark,opacity,
			sizeParameterValueType,rotation);
		PointSymbolizer pointSymbolizer = StyleFactory.createPointSymbolizer(graphic);
		symbolizer[0] = pointSymbolizer;
	}
	
	private void createLine(HashMap style, Symbolizer[] symbolizer) {
		
		Integer colorStrokeAsInteger = (Integer)style.get("ColorStroke");
		Expression widthAsExpression = (Expression)style.get("Width");
		Double opacityStrokeAsDouble = (Double)style.get("OpacityStroke");
		double opacityStroke = 1.0;
		if (opacityStrokeAsDouble != null) {
			opacityStroke = opacityStrokeAsDouble.doubleValue();
		}
		float[] dashArray = (float[])style.get("DashArray");
		String lineJoin = "mitre";
		if (style.get("LineJoin") != null) {
			lineJoin = (String)style.get("LineJoin");
		}
		String lineCap = "butt";
		if (style.get("LineCap") != null) {
			lineCap = (String)style.get("LineCap");
		}
		Color colorStroke = new Color(colorStrokeAsInteger.intValue());
		Stroke stroke = StyleFactory2.createStroke(colorStroke,widthAsExpression,opacityStroke,
			dashArray,lineJoin,lineCap);
		LineSymbolizer lineSymbolizer = StyleFactory.createLineSymbolizer(stroke);
		symbolizer[0] = lineSymbolizer;		
	}
	
	private void createPolygon(HashMap style, Symbolizer[] symbolizer) {
		
		Integer colorFillAsInteger = null;
		Color colorFill = null;
		if (style.get("ColorFill") != null) {
			colorFillAsInteger = (Integer)style.get("ColorFill");
			colorFill = new Color(colorFillAsInteger.intValue());
		}
		Double opacityFillAsDouble = (Double)style.get("OpacityFill");
		double opacityFill = 1.0;
		if (opacityFillAsDouble != null) {
			opacityFill = opacityFillAsDouble.doubleValue();
		}
		GraphicFill graphicFill = null;
		if ((style.get("ExternalGraphicURL") != null)&&(style.get("ExternalGraphicFormat") != null)) {
			String externalGraphicURL = (String)style.get("ExternalGraphicURL");
			String externalGraphicFormat = (String)style.get("ExternalGraphicFormat");
			ExternalGraphic externalGraphic = null;
			try { 
				externalGraphic = 
					StyleFactory.createExternalGraphic(externalGraphicURL,externalGraphicFormat);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			String graphicSize = ((Integer)style.get("ExternalGraphicSize")).toString();
			Graphic graphic = StyleFactory2.createGraphic(externalGraphic,null,graphicSize);
			graphicFill = StyleFactory.createGraphicFill(graphic);
		}
		
		Fill fill = StyleFactory.createFill(colorFill,opacityFill,graphicFill);
		Integer colorStrokeAsInteger = (Integer)style.get("ColorStroke");
		Expression widthAsExpression = (Expression)style.get("Width");		
		Double opacityStrokeAsDouble = (Double)style.get("OpacityStroke");
		double opacityStroke = 1.0;
		if (opacityStrokeAsDouble != null) {
			opacityStroke = opacityStrokeAsDouble.doubleValue();
		}
		float[] dashArray = (float[])style.get("DashArray");
		String lineJoin = "mitre";
		if (style.get("LineJoin") != null) {
			lineJoin = (String)style.get("LineJoin");
		}
		String lineCap = "butt";
		if (style.get("LineCap") != null) {
			lineCap = (String)style.get("LineCap");
		}
		Color colorStroke = new Color(colorStrokeAsInteger.intValue());
		Stroke stroke = StyleFactory2.createStroke(colorStroke,widthAsExpression,opacityStroke,
			dashArray,lineJoin,lineCap);
		PolygonSymbolizer polygonSymbolizer = StyleFactory.createPolygonSymbolizer(stroke,fill);
		symbolizer[0] = polygonSymbolizer;		
	}
	
	private void createTextLine(HashMap style,Symbolizer[] symbolizer) {
		
		String fontFamily = (String)style.get("FontFamily");
		Boolean italicAsBoolean = (Boolean)style.get("Italic");
		boolean italic = false;
		if (italicAsBoolean != null) {
			italic = italicAsBoolean.booleanValue();
		}
		Boolean boldAsDouble = (Boolean) style.get("Bold");
		boolean bold = false;
		if (boldAsDouble != null) {
			bold = boldAsDouble.booleanValue();
		}
		Double fontSizeAsDouble = (Double) style.get("FontSize");
		double fontSize = 10;
		if (fontSizeAsDouble != null) {
			fontSize = fontSizeAsDouble.doubleValue();
		}
		Font font = StyleFactory.createFont(fontFamily,italic,bold,fontSize);
		Integer colorFontAsInteger = (Integer)style.get("ColorFont");	
		Color colorFont = new Color(colorFontAsInteger.intValue());
		font.setColor(colorFont);
		String atributeName = (String)style.get("AttributeName");
		Double perpendicularOffsetAsDouble = (Double)style.get("PerpendicularOffset");
		double perpendicularOffset = 0;
		if (perpendicularOffsetAsDouble != null) {
			perpendicularOffset = perpendicularOffsetAsDouble.doubleValue();
		}
		LinePlacement linePlacement = StyleFactory.createLinePlacement(perpendicularOffset);
		LabelPlacement labelPlacement = StyleFactory.createLabelPlacement(linePlacement);
		TextSymbolizer textSymbolizer = StyleFactory.createTextSymbolizer( null, StyleFactory2.createLabel(atributeName), font, labelPlacement, null, null, 0, Double.MAX_VALUE);
		symbolizer[0] = textSymbolizer;	
	}
	
	private void createTextPoint(HashMap style,Symbolizer[] symbolizer) {
		
		String fontFamily = (String)style.get("FontFamily");
		Boolean italicAsBoolean = (Boolean)style.get("Italic");
		boolean italic = false;
		if (italicAsBoolean != null) {
			italic = italicAsBoolean.booleanValue();
		}
		Boolean boldAsDouble = (Boolean) style.get("Bold");
		boolean bold = false;
		if (boldAsDouble != null) {
			bold = boldAsDouble.booleanValue();
		}
		Double fontSizeAsDouble = (Double) style.get("FontSize");
		double fontSize = 10;
		if (fontSizeAsDouble != null) {
			fontSize = fontSizeAsDouble.doubleValue();
		}
		Font font = StyleFactory.createFont(fontFamily,italic,bold,
			fontSize);
		Integer colorFontAsInteger = (Integer)style.get("ColorFont");
		Color colorFont = new Color(colorFontAsInteger.intValue());
		font.setColor(colorFont);
		String atributeName = (String)style.get("AttributeName");
		Double anchorXAsDouble = (Double)style.get("AnchorX");
		double anchorX = 0.0;
		if (anchorXAsDouble != null) {
			anchorX = anchorXAsDouble.doubleValue();
		}
		Double anchorYAsDouble = (Double)style.get("AnchorY");
		double anchorY = 0.5;
		if (anchorYAsDouble != null) {
			anchorY = anchorYAsDouble.doubleValue();
		}
		Double displacementXAsDouble = (Double)style.get("DisplacementX");
		double displacementX = 0;
		if (displacementXAsDouble != null) {
			displacementX = displacementXAsDouble.doubleValue();
		}
		Double displacementYAsDouble = (Double)style.get("DisplacementY");
		double displacementY = 0;
		if (displacementYAsDouble != null) {
			displacementY = displacementYAsDouble.doubleValue();
		}
		Double rotationAsDouble = (Double)style.get("Rotation");
		double rotation = 0.0;
		if (rotationAsDouble != null) {
			rotation = rotationAsDouble.doubleValue();	
		}
		PointPlacement pointPlacement = StyleFactory.createPointPlacement(anchorX,
			anchorY,displacementX,displacementY,rotation);
		LabelPlacement labelPlacement = StyleFactory.createLabelPlacement(pointPlacement);
		TextSymbolizer textSymbolizer = StyleFactory.createTextSymbolizer( null, StyleFactory2.createLabel(atributeName), font, labelPlacement, null, null, 0, Double.MAX_VALUE);		
		symbolizer[0] = textSymbolizer;	
	}
}
