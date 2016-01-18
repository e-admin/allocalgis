/*
 * Created on 27-jul-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.awt.Color;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.deegree.services.wfs.filterencoding.FilterConstructionException;
import org.deegree_impl.graphics.sld.StyleFactory;
import org.deegree_impl.graphics.sld.StyleFactory2;
import org.deegree_impl.services.wfs.filterencoding.ComplexFilter;
import org.deegree_impl.services.wfs.filterencoding.Literal;
import org.deegree_impl.services.wfs.filterencoding.LogicalOperation;
import org.deegree_impl.services.wfs.filterencoding.OperationDefines;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsCOMPOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyName;

import com.geopista.style.sld.model.ScaleRange;

/**
 * @author enxenio s.l.
 *
 */
public class CreateRuleAction {

	private String _ruleName;
	private HashMap _style;
	private String _propertyName;
	private Integer _operationID;
	private List _expressions;
	private Integer _insert;
	private ScaleRange _scaleRange;
	
	public CreateRuleAction(String ruleName, HashMap style,String propertyName, 
		Integer operationID,List expressions,Integer insert,ScaleRange scaleRange) {
			
		_ruleName = ruleName;
		_style = style;
		_propertyName = propertyName;
		_operationID = operationID;
		_expressions = expressions;
		_insert = insert;
		_scaleRange = scaleRange;
	}
	
	public Object doExecute() {
		
		String ruleName = null;
		double minScale = _scaleRange.getMinScale().doubleValue();
		double maxScale = _scaleRange.getMaxScale().doubleValue();
		if (_ruleName != null) {
			ruleName = _ruleName;
		}
		Filter filter = null;
		if ((_propertyName != null)&&(_operationID != null)) {
			filter = createFilter(_propertyName,_operationID,_expressions);
		}
		Symbolizer[] symbolizerArray = null;
		if (!_style.isEmpty()) {
			symbolizerArray = createSymbolizer(_style);
		}
		Rule rule = StyleFactory.createRule(symbolizerArray,ruleName,null,null,null,filter,
			false,minScale,maxScale);
		if (_insert.intValue() == 1) {
			_scaleRange.addRule(rule);
		}
		return rule;
	}
	
	private Filter createFilter(String propertyName, Integer operationID, List expressions) {
		
		ComplexFilter complexFilter = null;
		PropertyName propertyNameElement = new PropertyName(propertyName);
		if (operationID.intValue() == OperationDefines.PROPERTYISEQUALTO) {
			//PropertyIsEqual operation
			Literal literal = new Literal((String)expressions.get(0));
			PropertyIsCOMPOperation propertyIsCOMPOperation = 
				new PropertyIsCOMPOperation(operationID.intValue(),propertyNameElement,literal);
			complexFilter = new ComplexFilter(propertyIsCOMPOperation);
		}
		else if (operationID.intValue() == OperationDefines.PROPERTYISBETWEEN) {
			//PropertyIsBetween operation
			Literal literal1 = new Literal((String)expressions.get(0));
			Literal literal2 = new Literal((String)expressions.get(1));
			PropertyIsCOMPOperation propertyIsCOMPOperationGreater = 
				new PropertyIsCOMPOperation(OperationDefines.PROPERTYISGREATERTHANOREQUALTO,propertyNameElement,literal1);
			PropertyIsCOMPOperation propertyIsCOMPOperationLess = 
				new PropertyIsCOMPOperation(OperationDefines.PROPERTYISLESSTHAN,propertyNameElement,literal2);
			ArrayList arguments = new ArrayList();
			arguments.add(propertyIsCOMPOperationGreater);
			arguments.add(propertyIsCOMPOperationLess);
			LogicalOperation andOperation = null;
			try {
				andOperation = new LogicalOperation(OperationDefines.AND,arguments);
			} catch (FilterConstructionException e) {
				e.printStackTrace();
			}
			complexFilter = new ComplexFilter(andOperation);
		}
		return complexFilter;
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
		Fill fill = StyleFactory.createFill(colorFill,opacityFill, graphicFill);
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
