/*
 * Created on 28-jul-2004
 */
package com.geopista.style.sld.ui.impl;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.deegree.graphics.sld.Fill;
import org.deegree.graphics.sld.Font;
import org.deegree.graphics.sld.Graphic;
import org.deegree.graphics.sld.LineSymbolizer;
import org.deegree.graphics.sld.Mark;
import org.deegree.graphics.sld.PointSymbolizer;
import org.deegree.graphics.sld.PolygonSymbolizer;
import org.deegree.graphics.sld.Stroke;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.TextSymbolizer;
import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterEvaluationException;
import org.deegree.services.wfs.filterencoding.Operation;
import org.deegree_impl.services.wfs.filterencoding.ComplexFilter;
import org.deegree_impl.services.wfs.filterencoding.Literal;
import org.deegree_impl.services.wfs.filterencoding.LogicalOperation;
import org.deegree_impl.services.wfs.filterencoding.OperationDefines;
import org.deegree_impl.services.wfs.filterencoding.PropertyIsCOMPOperation;
import org.deegree_impl.services.wfs.filterencoding.PropertyName;

import com.geopista.app.AppContext;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.TreeDomain;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.IGeopistaLayer;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;

/**
 * Clase con Utilidades
 * @author Enxenio S.L.
 */
public class UIUtils {
	
	
    /**
     * Capa
     */
	IGeopistaLayer layer;
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	
    /**
     * Crea un pequeño simbolo para mostrar
     * @param symbolizer 
     * @return 
     */
	public static Color createColorFromSymbolizer(Symbolizer symbolizer) {
		Color result = null;

		if (symbolizer instanceof PointSymbolizer) {
			Graphic graphic = ((PointSymbolizer)symbolizer).getGraphic();
			if (graphic.getMarksAndExtGraphics()[0] instanceof Mark) {
				Fill fill = ((Mark)graphic.getMarksAndExtGraphics()[0]).getFill();
				if (fill != null) {
					try {
						result = fill.getFill(null);
					}
					catch (FilterEvaluationException e) {
						//Do nothing
					}
				}
			}
			else {
				return Color.WHITE;
			}
		}
		else if (symbolizer instanceof LineSymbolizer) {
			Stroke stroke = ((LineSymbolizer)symbolizer).getStroke();
			if (stroke != null) {
				try {
					result = stroke.getStroke(null);				
				}
				catch (FilterEvaluationException e) {
					//Do nothing
				}
			}
		}
		else if (symbolizer instanceof PolygonSymbolizer) {
			Fill fill = ((PolygonSymbolizer)symbolizer).getFill();
			if (fill != null) {
				try {
					result = fill.getFill(null);				
				}
				catch (FilterEvaluationException e) {
					//Do nothing
				}
			}
		}
		else if (symbolizer instanceof TextSymbolizer) {
			Font font = ((TextSymbolizer)symbolizer).getFont();
			try {
				result = font.getColor(null);
			}
			catch (FilterEvaluationException e) {
				//Do nothing
			}
		}
		return result;
	}


    /**
     * Crea una cadena desde un filtro
     * @param filter 
     * @return 
     */
	public static String createStringFromFilter(Filter filter) {
		StringBuffer filterAsText = new StringBuffer();
		
		if (filter == null) {
			filterAsText.append(""); //$NON-NLS-1$
		}
		else if (filter instanceof ComplexFilter) {
			filterAsText.append(createStringFromOperation(((ComplexFilter)filter).getOperation()));
		}
		else {
			filterAsText.append(filter.toXML().toString());
		}
		return filterAsText.toString(); 
	}


	//Miguel (Me da el primer término) (Lo utilizo para los encabezados)
    /**
     * Obtiene propiedad de un filtro
     * @param filter 
     * @return 
     */
	public static String getFilterProperty(Filter filter)
	{
		if (filter instanceof ComplexFilter && ((ComplexFilter)filter).getOperation() instanceof PropertyIsCOMPOperation)
		{
			PropertyIsCOMPOperation operation = (PropertyIsCOMPOperation)((ComplexFilter)filter).getOperation(); 
			Expression firstExpr = ((PropertyIsCOMPOperation)operation).getFirstExpression();
			return ((PropertyName)firstExpr).getValue();
		}
		else
		{
			if (filter instanceof ComplexFilter && ((ComplexFilter)filter).getOperation() instanceof LogicalOperation)
			{
				LogicalOperation loperation = (LogicalOperation)((ComplexFilter)filter).getOperation();
				List arguments = ((LogicalOperation)loperation).getArguments();
				String str="";
				
				//Captura de título para rangos
				if (arguments.size()==2 
				&& ((PropertyIsCOMPOperation)arguments.get(0)).getOperatorId()==OperationDefines.PROPERTYISGREATERTHANOREQUALTO 
				&& ((PropertyIsCOMPOperation)arguments.get(1)).getOperatorId()==OperationDefines.PROPERTYISLESSTHAN)
				{
					str = str + createPartStringFromOperation(((PropertyIsCOMPOperation)arguments.get(0)),true,false,false);
				}
				else
				{
					
				
	                for (int i = 0; i < (arguments.size()); i++) 
	                {
	                	PropertyIsCOMPOperation operation = (PropertyIsCOMPOperation) arguments.get(i); 
	        			str = str + createPartStringFromOperation(operation,true,false,false);
	        			if (i<arguments.size()-1)
	        			{
	        				str = str + ", ";
	        			}
	                }
				}
                return str;
			}
			else
			{	
				return filter.toString();
			}
		}
	}
	

	
	
	/**
     * Funcion recursiva que devuelve una cadena a partir de un filtro
     * Va descomponiendo todas las operaciones de comparación obtenidos de operaciones Lógicas
     * Tambien comprueba si el texto de las expresiones de comparación está asociado a un Dominio y
     * en el caso de estarlo, cambia el valor representado.
     * @param filter Filtro del que se desea generar la cadena
     * @param layer 
     * @param isTitle 
     * @return 
     */
	public static String createStringDomainFromFilter2(Filter filter, IGeopistaLayer layer, boolean isTitle) 
	{
		StringBuffer filterAsText = new StringBuffer();
		
		if (((ComplexFilter)filter).getOperation() instanceof PropertyIsCOMPOperation)
		{
			PropertyIsCOMPOperation operation = (PropertyIsCOMPOperation)((ComplexFilter)filter).getOperation(); 
			Expression firstExpr = ((PropertyIsCOMPOperation)operation).getFirstExpression();
			Expression secondExpr = ((PropertyIsCOMPOperation)operation).getSecondExpression();
			
			if (isTitle)
			{
				filterAsText.append(createStringFromExpression(firstExpr));
			}
			else
			{
				//Si la comparacion es con un = (Tipo = 3), omite el =
				if (operation.getOperatorId()!=OperationDefines.PROPERTYISEQUALTO)
				{
					filterAsText.append(createStringFromOperationID(operation.getOperatorId()));
				}
				filterAsText.append(cogerDominio((GeopistaLayer)layer, ((PropertyName)firstExpr).getValue(),createStringFromExpression(secondExpr)));
			}
//			filterAsText.append(createStringFromExpression(firstExpr));
//			filterAsText.append("-"); //$NON-NLS-1$
//			filterAsText.append(createStringFromOperationID(operation.getOperatorId()));
//			filterAsText.append("-"); //$NON-NLS-1$
//			filterAsText.append(cogerDominio(layer, ((PropertyName)firstExpr).getValue(),createStringFromExpression(secondExpr)));
		}
		else
		{
			if (filter instanceof ComplexFilter && ((ComplexFilter)filter).getOperation() instanceof LogicalOperation)
			{
				LogicalOperation loperation = (LogicalOperation)((ComplexFilter)filter).getOperation();
				List arguments = ((LogicalOperation)loperation).getArguments();
				
//				Añadir NOT en la representación del filtro en caso que del la operación logica sea NOT
				String str="";
            	if (loperation.getOperatorId()==OperationDefines.NOT)
					str="<>";
				
				//Captura de rango
				if (arguments.size()==2 
				&& ((PropertyIsCOMPOperation)arguments.get(0)).getOperatorId()==OperationDefines.PROPERTYISGREATERTHANOREQUALTO 
				&& ((PropertyIsCOMPOperation)arguments.get(1)).getOperatorId()==OperationDefines.PROPERTYISLESSTHAN)
				{
					str = str + aplicacion.getI18nString("LegendStyle.desde") + " " + createPartStringFromOperation(((PropertyIsCOMPOperation)arguments.get(0)),false,false,true) + " " + aplicacion.getI18nString("LegendStyle.hasta") + " " + createPartStringFromOperation(((PropertyIsCOMPOperation)arguments.get(1)),false,false,true);
				}
				else //Es una operacion no conocida
				{
	                for (int i = 0; i < (arguments.size()); i++) 
	                {
	                	if (arguments.get(i) instanceof PropertyIsCOMPOperation)
	                	{
	            			PropertyIsCOMPOperation operation = (PropertyIsCOMPOperation) arguments.get(i); 
	                    	ComplexFilter tmpFilter = new ComplexFilter(operation);
	                    	str = str + createStringDomainFromFilter2(tmpFilter,layer,false);
	                    	if (i<arguments.size()-1)
	            			{
	            				str = str + ", ";
	            			}
	            		}
	            		else
	            		{
	            			if (arguments.get(i) instanceof LogicalOperation)
	            			{
	            				str = str + createStringDomainFromFilter2(  new ComplexFilter(((LogicalOperation)arguments.get(i))),layer,false);
	            			}
	                	}
	                }
				}
                //str = createStringFromOperationID2(loperation.getOperatorId()) + "" + str + ", ";
                filterAsText.append(str);
			}
		}
		return filterAsText.toString();
		
	}
		
	///////////////
	
	

//End Miguel
	
    /**
     * Crea una cadena de una expresión
     * @param expression 
     * @return 
     */
	public static String createStringFromExpression(Expression expression) {
		String expressionAsText = null;
		
		if (expression instanceof PropertyName) 
		{
			expressionAsText = ((PropertyName)expression).getValue(); 
		}
		else if (expression instanceof Literal) {
			expressionAsText = ((Literal)expression).getValue(); 
			try
			{
			double value=Double.parseDouble(expressionAsText);
			expressionAsText=NumberFormat.getNumberInstance().format(value);
			}
			catch(NumberFormatException ex)
			{}
		}
		else {
			expressionAsText = expression.toXML().toString();
		}
		return expressionAsText;
	}
	
	
	
	
	
	
	//Crea el primer termino, la operación y el segundo término (Quito el primero porque es redundante al título)
    /**
     * crea una cadena de un objeto Operation
     * @param operation 
     * @return 
     */
	public static String createStringFromOperation(Operation operation){

		StringBuffer filterAsText = new StringBuffer();
		
		if (operation instanceof PropertyIsCOMPOperation) {
			Expression firstExpr = ((PropertyIsCOMPOperation)operation).getFirstExpression();
			Expression secondExpr = ((PropertyIsCOMPOperation)operation).getSecondExpression();
			filterAsText.append(createStringFromExpression(firstExpr));
			filterAsText.append(" "); //$NON-NLS-1$
			filterAsText.append(createStringFromOperationID(operation.getOperatorId()));
			filterAsText.append(" "); //$NON-NLS-1$
			filterAsText.append(createStringFromExpression(secondExpr));
			
		}
		else if (operation instanceof LogicalOperation) {
			List arguments = ((LogicalOperation)operation).getArguments();
			switch (operation.getOperatorId()) {
				case OperationDefines.AND:
                    for (int i = 0; i < (arguments.size() - 1); i++) {
                        filterAsText.append("("); //$NON-NLS-1$
                        filterAsText.append(createStringFromOperation((Operation)arguments.get(i)));                
                        filterAsText.append(") AND "); //$NON-NLS-1$
                    }
                    filterAsText.append("("); //$NON-NLS-1$
                    filterAsText.append(createStringFromOperation((Operation)arguments.get(arguments.size() - 1)));             
                    filterAsText.append(")"); //$NON-NLS-1$
                    break;
				case OperationDefines.OR:
					for (int i = 0; i < (arguments.size() - 1); i++) {
						filterAsText.append("("); //$NON-NLS-1$
						filterAsText.append(createStringFromOperation((Operation)arguments.get(i)));				
						filterAsText.append(") OR "); //$NON-NLS-1$
					}
					filterAsText.append("("); //$NON-NLS-1$
					filterAsText.append(createStringFromOperation((Operation)arguments.get(arguments.size() - 1)));				
					filterAsText.append(")"); //$NON-NLS-1$
					break;
				case OperationDefines.NOT:
					filterAsText.append("NOT ("); //$NON-NLS-1$
					filterAsText.append(createStringFromOperation((Operation)arguments.get(0)));				
					filterAsText.append(")"); //$NON-NLS-1$
					break;
			}
		}
		else {
			filterAsText.append(operation.toXML().toString());
		}		
		return filterAsText.toString();
	}

	
	//Solo crea el primer término
    /**
     * crea la parte de una cadena que se desea de un objeto Operation
     * @param operation 
     * @param a 
     * @param b 
     * @param c 
     * @return 
     */
	public static String createPartStringFromOperation(Operation operation,boolean a, boolean b, boolean c){

		StringBuffer filterAsText = new StringBuffer();
		
		if (operation instanceof PropertyIsCOMPOperation) {
			Expression firstExpr = ((PropertyIsCOMPOperation)operation).getFirstExpression();
			Expression secondExpr = ((PropertyIsCOMPOperation)operation).getSecondExpression();
			if (a)
			{
				filterAsText.append(createStringFromExpression(firstExpr));
				//filterAsText.append(" "); //$NON-NLS-1$
			}
			if (b)
			{
				filterAsText.append(createStringFromOperationID(operation.getOperatorId()));
			}
			if (c)
			{
				//filterAsText.append(" "); //$NON-NLS-1$
				filterAsText.append(createStringFromExpression(secondExpr));
			}
		}
		else if (operation instanceof LogicalOperation) {
			List arguments = ((LogicalOperation)operation).getArguments();
			switch (operation.getOperatorId()) {
				case OperationDefines.AND:
                    for (int i = 0; i < (arguments.size() - 1); i++) {
                        filterAsText.append("("); //$NON-NLS-1$
                        filterAsText.append(createPartStringFromOperation((Operation)arguments.get(i),a,b,c));                
                        filterAsText.append(") AND "); //$NON-NLS-1$
                    }
                    filterAsText.append("("); //$NON-NLS-1$
                    filterAsText.append(createPartStringFromOperation((Operation)arguments.get(arguments.size() - 1),a,b,c));             
                    filterAsText.append(")"); //$NON-NLS-1$
                    break;
				case OperationDefines.OR:
					for (int i = 0; i < (arguments.size() - 1); i++) {
						filterAsText.append("("); //$NON-NLS-1$
						filterAsText.append(createPartStringFromOperation((Operation)arguments.get(i),a,b,c));				
						filterAsText.append(") OR "); //$NON-NLS-1$
					}
					filterAsText.append("("); //$NON-NLS-1$
					filterAsText.append(createPartStringFromOperation((Operation)arguments.get(arguments.size() - 1),a,b,c));				
					filterAsText.append(")"); //$NON-NLS-1$
					break;
				case OperationDefines.NOT:
					filterAsText.append("NOT ("); //$NON-NLS-1$
					filterAsText.append(createPartStringFromOperation((Operation)arguments.get(0),a,b,c));				
					filterAsText.append(")"); //$NON-NLS-1$
					break;
			}
		}
		else {
			filterAsText.append(operation.toXML().toString());
		}		
		return filterAsText.toString();
	}
	
	
	
	
	
	
    /**
     * Crea una cadena completa de un objeto operation
     * @param operationID 
     * @return 
     */
	public static String createStringFromOperationID(int operationID){
		String operatorAsText = null;
		
		switch(operationID) {
			case OperationDefines.PROPERTYISEQUALTO:
				operatorAsText = "="; //$NON-NLS-1$
				break;
			case OperationDefines.PROPERTYISGREATERTHAN:
				operatorAsText = ">"; //$NON-NLS-1$
				break;
			case OperationDefines.PROPERTYISGREATERTHANOREQUALTO:
				operatorAsText = ">="; //$NON-NLS-1$
				break;
			case OperationDefines.PROPERTYISLESSTHAN:
				operatorAsText = "<"; //$NON-NLS-1$
				break;
			case OperationDefines.PROPERTYISLESSTHANOREQUALTO:
				operatorAsText = "<="; //$NON-NLS-1$
				break;
			case OperationDefines.PROPERTYISBETWEEN:
				operatorAsText = "between"; //$NON-NLS-1$
				break;
			case OperationDefines.AND:
				operatorAsText = "and"; //$NON-NLS-1$
				break;				
			case OperationDefines.OR:
				operatorAsText = "or"; //$NON-NLS-1$
				break;				
			case OperationDefines.NOT:
				operatorAsText = "not"; //$NON-NLS-1$
				break;
			default:
				operatorAsText = "unknown"; //$NON-NLS-1$
				break;
		}
		return operatorAsText;
	}
	
	
//	Miguel
	private static String cogerDominio(GeopistaLayer layer, String col, String text)
	{
		Domain dominio;
		
		GeopistaSchema MySchema = (GeopistaSchema) layer.getFeatureCollectionWrapper().getFeatureSchema();
		dominio = MySchema.getColumnByAttribute(col).getDomain();
		if (dominio!=null)
		{
			int domainType = dominio.getType();
			
			Iterator domainChildren = dominio.getChildren().iterator();
			if (domainType == Domain.CODEDENTRY || domainType == Domain.CODEBOOK || domainType == Domain.TREE) 
			{
				 while (domainChildren.hasNext())
	            {
	                Domain domainChild = (Domain) domainChildren.next();
	                if (text.equals(domainChild.getPattern()))
	                {
	                	return domainChild.getRepresentation();
	                }
	            }
				return text; 
			}
		}
		return text;
	}

    /**
     * Devuelve el color en HEX
     * @param color 
     * @return 
     */
	public static String getHexColor(java.awt.Color color) {
		String[] RGB = new String[256];
		int k =0;
		String[] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$
		for(int i = 0 ; i < 16; i++) {
		   for(int j=0;j<16;j++) {
			 RGB[k] = hex[i] + hex[j];
			 k++;
		   }
		}
		return "" + RGB[color.getRed()] + RGB[color.getGreen()] + RGB[color.getBlue()]; //$NON-NLS-1$
	}

    /**
     * 
     * @param layer 
     * @param attributeName 
     * @return 
     */
	public static SortedSet getNonNullAttributeValues(Layer layer, String attributeName) {
		TreeSet values = new TreeSet();

		for (Iterator i = layer.getFeatureCollectionWrapper().getFeatures()
							   .iterator(); i.hasNext();) {
			Feature feature = (Feature) i.next();

			if (feature.getAttribute(attributeName) != null) {
				values.add(feature.getAttribute(attributeName));
			}
		}

		return values;
	}
	
    /**
     * Devuelve la cadena de un objeto limpia L/R
     * @param object 
     * @return 
     */
	public static Object trimIfString(Object object) {
		return object != null && object instanceof String ? ((String)object).trim() : object;
	}	
}
