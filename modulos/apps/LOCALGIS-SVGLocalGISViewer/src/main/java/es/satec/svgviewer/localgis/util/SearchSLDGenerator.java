/**
 * SearchSLDGenerator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SearchSLDGenerator {
	
	public static final String OP_EQUAL_TO = "PropertyIsEqualTo";
	public static final String OP_NOT_EQUAL_TO = "PropertyIsNotEqualTo";
	public static final String OP_LESS_THAN = "PropertyIsLessThan";
	public static final String OP_GREATER_THAN = "PropertyIsGreaterThan";
	public static final String OP_LESS_THAN_OR_EQUAL_TO = "PropertyIsLessThanOrEqualTo";
	public static final String OP_GREATER_THAN_OR_EQUAL_TO = "PropertyIsGreaterThanOrEqualTo";
	public static final String OP_LIKE = "PropertyIsLike";
	public static final String OP_NULL = "PropertyIsNull";
	public static final String OP_BETWEEN = "PropertyIsBetween";

	/**
	 * Genera el XML para el estilo de la busqueda.
	 * @param currentStyle
	 * @param filterOperator
	 * @param filterProperty
	 * @param filterLiteral
	 * @param filterLiteral2
	 * @param stroke
	 * @param strokeWidth
	 * @param fill
	 * @return
	 */
	public static String generateSearchSLD(String currentStyle, String filterOperator, String filterProperty,
			String filterLiteral, String filterLiteral2, Color stroke, int strokeWidth, Color fill) {
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		// StyledLayerDescriptor
		sb.append("<StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		// NamedLayer
		sb.append("<NamedLayer>");
		sb.append("<Name>Search</Name>");
		// UserStyle
		sb.append("<UserStyle>");
		sb.append("<Name>" + currentStyle + "</Name>");
		sb.append("<Title>" + currentStyle + "</Title>");
		sb.append("<Abstract>" + currentStyle + "</Abstract>");
		// FeatureTypeStyle
		sb.append("<FeatureTypeStyle>");
		// Rule
		sb.append("<Rule>");
		sb.append("<Name>Search rule</Name>");
		// Filter
		sb.append("<ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\">");
		String opAttributes = "";
		String preparedFilterLiteral = "";
		if (filterOperator.equals(OP_LIKE)) {
			// Se escapan los caracteres comodin
			opAttributes = " wildCard=\"*\" singleChar=\"#\" escapeChar=\"!\"";
			preparedFilterLiteral = filterLiteral;
			preparedFilterLiteral = escape('!', preparedFilterLiteral, '!');
			preparedFilterLiteral = escape('*', preparedFilterLiteral, '!');
			preparedFilterLiteral = escape('#', preparedFilterLiteral, '!');
			preparedFilterLiteral = "*" + preparedFilterLiteral + "*";
		}
		else if (filterOperator.equals(OP_NULL)) {
			preparedFilterLiteral = null;
		}
		else {
			preparedFilterLiteral = filterLiteral;
		}
		sb.append("<ogc:" + filterOperator + opAttributes + ">");
		sb.append("<ogc:PropertyName>" + filterProperty + "</ogc:PropertyName>");
		if (filterOperator.equals(OP_BETWEEN)) {
			sb.append("<LowerBoundary><ogc:Literal>" + filterLiteral + "</ogc:Literal></LowerBoundary>");
			sb.append("<UpperBoundary><ogc:Literal>" + filterLiteral2 + "</ogc:Literal></UpperBoundary>");
		}
		else {
			if (preparedFilterLiteral != null) {
				sb.append("<ogc:Literal>" + preparedFilterLiteral + "</ogc:Literal>");
			}
		}
		sb.append("</ogc:" + filterOperator + ">");
		sb.append("</ogc:Filter>");
		// PolygonSymbolizer
		sb.append("<PolygonSymbolizer>");
		if (fill != null) {
			String fillHex = toHexStringColor(fill);
			sb.append("<Fill><CssParameter name=\"fill\">" + fillHex + "</CssParameter></Fill>");
		}
		if (stroke != null || strokeWidth >= 0) {
			sb.append("<Stroke>");
			if (stroke != null) {
				String strokeHex = toHexStringColor(stroke);
				sb.append("<CssParameter name=\"stroke\">" + strokeHex + "</CssParameter>");
			}
			if (strokeWidth >= 0) {
				sb.append("<CssParameter name=\"stroke-width\">" + strokeWidth + "</CssParameter>");
			}
			sb.append("</Stroke>");
		}
		sb.append("</PolygonSymbolizer>");
		sb.append("</Rule>");
		sb.append("</FeatureTypeStyle>");
		sb.append("</UserStyle>");
		sb.append("</NamedLayer>");
		sb.append("</StyledLayerDescriptor>");
		
		return sb.toString();
	}
	
	/**
	 * Escapa el caracter indicado del String que se proporciona con el caracter de escape. 
	 */
	private static String escape(char escapedChar, String string, char escapeChar) {
		int index = 0;
		while (index != -1) {
			index = string.indexOf(escapedChar, index);
			if (index != -1) {
				string = string.substring(0, index) + escapeChar + string.substring(index, string.length());
				index+=2;
			}
		}
		return string;
	}
	
	/**
	 * Convierte un color SWT a un String hexadecimal de la forma #rrggbb
	 */
	private static String toHexStringColor(Color color) {
		String hexRed = Integer.toHexString(color.getRed());
		if (hexRed.length() == 1) hexRed = "0" + hexRed;
		String hexGreen = Integer.toHexString(color.getGreen());
		if (hexGreen.length() == 1) hexGreen = "0" + hexGreen;
		String hexBlue = Integer.toHexString(color.getBlue());
		if (hexBlue.length() == 1) hexBlue = "0" + hexBlue;
		return "#" + hexRed + hexGreen + hexBlue;
	}
	
	public static void main(String args[]) {
		System.out.println(generateSearchSLD("A", OP_LIKE, "lastName", "J*o!h#n", null,
			Display.getDefault().getSystemColor(SWT.COLOR_BLUE), 1, Display.getDefault().getSystemColor(SWT.COLOR_BLUE)));
	}
}
