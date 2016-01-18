/**
 * SLDFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.deegree.framework.xml.ElementList;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.model.filterencoding.AbstractFilter;
import org.deegree.model.filterencoding.ComplexFilter;
import org.deegree.model.filterencoding.Expression;
import org.deegree.model.filterencoding.FalseFilter;
import org.deegree.model.filterencoding.Filter;
import org.deegree.model.filterencoding.LogicalOperation;
import org.deegree.model.filterencoding.OperationDefines;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.japisoft.fastparser.ParseException;
import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;

public class SLDFactory {
	
	

    private static URL baseURL = null;
	
	private static Logger logger = (Logger) Logger.getInstance(SLDFactory.class);

	public static StyledLayerDescriptor createSLD(InputStream issld , URL  url) throws XMLParsingException {
		logger.debug("Cargando estilo sld");
		try {
			baseURL = url;
			Parser p = new Parser();
			p.setNodeFactory(new DomNodeFactory());
			p.setInputStream(issld);
			p.parse();
			Document d = p.getDocument();
			// Extract the DOM root
			Element e = (Element) d.getRoot();

			// optional: <Name>
			String name = XMLTools.getStringValue( "Name", e, null );
			// optional: <Title>
			String title = XMLTools.getStringValue( "Title", e, null );
			// optional: <Abstract>
			String abstract_ = XMLTools.getStringValue( "Abstract", e, null );

			// required: version-Attribute
			String version = e.getAttribute("version");

			// optional: <NamedLayer>(s) / <UserLayer>(s)
			NodeList nodelist = e.getChildNodes();
			Vector layerVector = new Vector();

			for (int i = 0; i < nodelist.getLength(); i++) {
				if ( nodelist.item( i ) instanceof Element ) {
					Element child = (Element) nodelist.item( i );

					String childName = child.getLocalName();
					if ( childName.equals( "NamedLayer" ) ) {
						layerVector.addElement( createNamedLayer( child ) );
					}
				}
			}

			AbstractLayer[] layers = new AbstractLayer[layerVector.size()];
			layerVector.copyInto(layers);

			return new StyledLayerDescriptor( name, title, version, abstract_, layers );
			
		} catch (ParseException ex) {
			throw new XMLParsingException(ex.getMessage());
		} catch (Exception e) {
			logger.error (e);
			logger.error("Error al parsear el SLD");
			throw new XMLParsingException(e);
		}
	}

	/**
	 * Creates a <tt>TextSymbolizer</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'TextSymbolizer'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'TextSymbolizer'-<tt>Element</tt>
	 * @param min
	 *            scale-constraint to be used
	 * @param max
	 *            scale-constraint to be used
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>TextSymbolizer</tt>-instance
	 */
	private static TextSymbolizer createTextSymbolizer( Element element, double min, double max ) throws XMLParsingException {
		logger.debug("Creando TextSymbolizer");
		
		// optional: <Label>
		Expression expLabel = null;
		Element labelElement = XMLTools.getChildElement("Label", element);
		if (labelElement != null && labelElement.hasChildNodes()) {
			Element labelChild = XMLTools.getFirstChildElement(labelElement);
			if (labelChild != null) {
				expLabel = Expression.buildFromDOM(labelChild);
			}
		}

		// optional: <Font>
		Font font = null;
		Element fontElement = XMLTools.getChildElement( "Font", element );

		if ( fontElement != null ) {
			font = createFont( fontElement );
		}

		// optional: <LabelPlacement>
		LabelPlacement labelPlacement = null;
		Element lpElement = XMLTools.getChildElement( "LabelPlacement", element );

		if ( lpElement != null ) {
			labelPlacement = createLabelPlacement( lpElement );
		} else {
			labelPlacement = new LabelPlacement(0.f, 0.f, 0, 0);
		}

		// optional: <Fill>
		Fill fill = null;
		Element fillElement = XMLTools.getChildElement( "Fill", element );

		if ( fillElement != null ) {
			fill = createFill( fillElement );
		}

		TextSymbolizer ps = null;
		String respClass = XMLTools.getAttrValue( element, "responsibleClass");
		if ( respClass == null ) {
			ps = new TextSymbolizer(expLabel, font, labelPlacement, fill, min, max );
		} else {
			ps = new TextSymbolizer(respClass, expLabel, font, labelPlacement, fill, min, max );
		}

		return ps;
	}

	/**
	 * Creates a <tt>LabelPlacement</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'LabelPlacement'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'LabelPlacement'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>LabelPlacement</tt>-instance
	 */
	private static LabelPlacement createLabelPlacement( Element element ) throws XMLParsingException {
		logger.debug("Creando LabelPlacement");
		LabelPlacement labelPlacement = null;

		Element pPlacement = XMLTools.getChildElement(element, "PointPlacement");
		if (pPlacement != null) {
			float anchorPointX = 0.f;
			float anchorPointY = 0.f;
			int displacementX = 0;
			int displacementY = 0;
			
			try {
				Element anchorPoint = XMLTools.getChildElement(pPlacement, "AnchorPoint");
				if (anchorPoint != null) {
					anchorPointX = Float.parseFloat(XMLTools.getStringValue("AnchorPointX", anchorPoint, "0.0"));
					anchorPointY = Float.parseFloat(XMLTools.getStringValue("AnchorPointY", anchorPoint, "0.0"));
				}
	
				Element displacement = XMLTools.getChildElement(pPlacement, "Displacement");
				if (displacement != null) {
					displacementX = (int) Float.parseFloat(XMLTools.getStringValue("DisplacementX", displacement, "0"));
					displacementY = (int) Float.parseFloat(XMLTools.getStringValue("DisplacementY", displacement, "0"));
				}
			} catch (Exception e) {
				logger.error(e);
			}
			
			labelPlacement = new LabelPlacement(anchorPointX, anchorPointY, displacementX, displacementY);
		}

		return labelPlacement;
	}

	/**
	 * Creates a <tt>Font</tt>-instance according to the contents of the DOM-subtree starting at
	 * the given 'Font'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'Font'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>Font</tt>-instance
	 */
	private static Font createFont( Element element ) throws XMLParsingException {

		// optional: <CssParameter>s
		ElementList nl = XMLTools.getChildElements( "CssParameter", element );
		Hashtable cssParams = new Hashtable( nl.getLength() );

		for ( int i = 0; i < nl.getLength(); i++ ) {
			CssParameter cssParam = createCssParameter( nl.item( i ) );
			cssParams.put( cssParam.getName(), cssParam );
		}

		return new Font( cssParams );
	}

	/**
	 * Creates a <tt>ParameterValueType</tt>-instance according to the contents of the
	 * DOM-subtree starting at the given <tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the <tt>Element</tt> (must be of the type sld:ParameterValueType)
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>ParameterValueType</tt>-instance
	 */
//	private static ParameterValueType createParameterValueType( Element element )
//	throws XMLParsingException {
//		// mix of text nodes and <wfs:Expression>-elements
//		ArrayList componentList = new ArrayList();
//		NodeList nl = element.getChildNodes();
//
//		for ( int i = 0; i < nl.getLength(); i++ ) {
//			Node node = nl.item( i );
//
//			switch ( node.getNodeType() ) {
//			case Node.TEXT_NODE: {
//				componentList.add( node.getNodeValue() );
//				break;
//			}
//			case Node.ELEMENT_NODE: {
//				Expression expression = Expression.buildFromDOM( (Element) node );
//				componentList.add( expression );
//				break;
//			}
//			default:
//				throw new XMLParsingException( "Elements of type 'ParameterValueType' may only "
//						+ "consist of CDATA and 'ogc:Expression'-elements!" );
//			}
//		}
//
//		Object[] components = componentList.toArray( new Object[componentList.size()] );
//		return new ParameterValueType( components );
//	}

	/**
	 * Creates a <tt>NamedStyle</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'NamedStyle'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'NamedStyle'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>NamedStyle</tt>-instance
	 */
	private static NamedStyle createNamedStyle( Element element ) throws XMLParsingException {
		logger.debug("Creando NamedStyle");
		// required: <Name>
		String name = XMLTools.getRequiredStringValue( "Name", element );

		return new NamedStyle( name );
	}

	/**
	 * 
	 */
	public static NamedStyle createNamedStyle( String name ) {
		return new NamedStyle( name );
	}

	/**
	 * Creates a <tt>NamedLayer</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'UserLayer'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'NamedLayer'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>NamedLayer</tt>-instance
	 */
	private static NamedLayer createNamedLayer( Element element ) throws XMLParsingException {
		logger.debug("Creando NamedLayer");
		// required: <Name>
		String name = XMLTools.getRequiredStringValue( "Name", element );

		// optional: <NamedStyle>(s) / <UserStyle>(s)
		NodeList nodelist = element.getChildNodes();
		Vector styleList = new Vector();

		for ( int i = 0; i < nodelist.getLength(); i++ ) {
			if ( nodelist.item( i ) instanceof Element ) {
				Element child = (Element) nodelist.item( i );

				String childName = child.getLocalName();

				if ( childName.equals( "NamedStyle" ) ) {
					styleList.addElement( createNamedStyle( child ) );
				} else if ( childName.equals( "UserStyle" ) ) {
					styleList.addElement( createUserStyle( child  ) );
				}
			}
		}

		AbstractStyle[] styles = new AbstractStyle[styleList.size()];
		styleList.copyInto(styles); 
		return new NamedLayer( name, styles );
	}

	/**
	 * 
	 */
	public static NamedLayer createNamedLayer( String name, AbstractStyle[] styles ) {
		return new NamedLayer( name, styles );
	}

	/**
	 * Creates a <tt>UserStyle</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'UserStyle'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'UserStyle'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>UserStyle</tt>-instance
	 */
	private static UserStyle createUserStyle( Element element  ) throws XMLParsingException {
		logger.debug("Creando UserStyle");
		// optional: <Name>
		String name = XMLTools.getStringValue( "Name", element, null );
		// optional: <Title>
		String title = XMLTools.getStringValue( "Title", element, null );
		// optional: <Abstract>
		String abstract_ = XMLTools.getStringValue( "Abstract", element, null );

		// optional: <IsDefault>
		String defaultString = XMLTools.getStringValue( "IsDefault", element, null );
		boolean isDefault = false;

		if ( defaultString != null ) {
			if ( defaultString.equals( "1" ) ) {
				isDefault = true;
			}
		}

		// required: <FeatureTypeStyle> (s)
		ElementList nl = XMLTools.getChildElements( "FeatureTypeStyle", element );
		FeatureTypeStyle[] styles = new FeatureTypeStyle[nl.getLength()];

		if ( styles.length == 0 ) {
			throw new XMLParsingException( "Required child-element 'FeatureTypeStyle' of element "
					+ "'UserStyle' is missing!" );
		}

		for ( int i = 0; i < nl.getLength(); i++ ) {
			styles[i] = createFeatureTypeStyle( nl.item( i )  );
		}

		return new UserStyle( name, title, abstract_, isDefault, styles );
	}

	/**
	 * Creates a <tt>FeatureTypeStyle</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'FeatureTypeStyle'-<tt>Element</tt>.
	 * <p>
	 * TODO: The ElseFilter currently does not work correctly with FeatureFilters.
	 * <p>
	 * 
	 * @param element
	 *            the 'FeatureTypeStyle'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>FeatureTypeStyle</tt>-instance
	 */
	public static FeatureTypeStyle createFeatureTypeStyle( Element element  ) throws XMLParsingException {
		logger.debug("Creando FeatureTypeStyle");
		// optional: <Name>
		String name = XMLTools.getStringValue( "Name", element, null );
		// optional: <Title>
		String title = XMLTools.getStringValue( "Title", element, null );
		// optional: <Abstract>
		String abstract_ = XMLTools.getStringValue( "Abstract", element, null );
		// optional: <FeatureTypeName>
		String featureTypeName = XMLTools.getStringValue( "FeatureTypeName", element, null );

		// optional: several <Rule> / <SemanticTypeIdentifier>
		NodeList nodelist = element.getChildNodes();
		Vector ruleList = new Vector();
		Vector typeIdentifierList = new Vector();

		// collect Filters of all Rules
		Vector filters = new Vector();
		// collect all Rules that have an ElseFilter
		Vector elseRules = new Vector();

		for ( int i = 0; i < nodelist.getLength(); i++ ) {
			if ( nodelist.item( i ) instanceof Element ) {
				Element child = (Element) nodelist.item( i );

				String childName = child.getLocalName();

				if ( childName.equals( "Rule" ) ) {
					Rule rule = createRule( child  );
					if ( rule.hasElseFilter() ) {
						elseRules.addElement( rule );
					} else if ( rule.getFilter() == null || rule.getFilter() instanceof ComplexFilter ) {
						filters.add( rule.getFilter() );
					}
					ruleList.addElement( rule );
				} else if ( childName.equals( "SemanticTypeIdentifier" ) ) {
					typeIdentifierList.addElement( XMLTools.getStringValue( child ) );
				}
			}
		}
		// compute and set the ElseFilter for all ElseFilter-Rules
		Filter elseFilter = null;
		// a Rule exists with no Filter at all -> elseFilter = false
		if ( filters.contains( null ) ) {
			elseFilter = new FalseFilter();
			// one Rule with a Filter exists -> elseFilter = NOT Filter
		} else if ( filters.size() == 1 ) {
			elseFilter = new ComplexFilter( OperationDefines.NOT );
			List arguments = ( (LogicalOperation) ( (ComplexFilter) elseFilter ).getOperation() ).getArguments();
			ComplexFilter complexFilter = (ComplexFilter) filters.get( 0 );
			arguments.add( complexFilter.getOperation() );
			// several Rules with Filters exist -> elseFilter = NOT (Filter1 OR Filter2 OR...)
		} else if ( filters.size() > 1 ) {
			ComplexFilter innerFilter = new ComplexFilter( OperationDefines.OR );
			elseFilter = new ComplexFilter( innerFilter, null, OperationDefines.NOT );
			List arguments = ( (LogicalOperation) innerFilter.getOperation() ).getArguments();
			Enumeration en = filters.elements();
			while ( en.hasMoreElements() ) {
				ComplexFilter complexFilter = (ComplexFilter) en.nextElement();
				arguments.add( complexFilter.getOperation() );
			}
		}
		Enumeration en = elseRules.elements();
		while (en.hasMoreElements()) {
			( (Rule) en.nextElement() ).setFilter( elseFilter );
		}

		Rule[] rules = new Rule[ruleList.size()];
		ruleList.copyInto(rules);
		String[] typeIdentifiers = new String[typeIdentifierList.size()];
		typeIdentifierList.copyInto(typeIdentifiers);

		return new FeatureTypeStyle( name, title, abstract_, featureTypeName, typeIdentifiers, rules );
	}

	/**
	 * Creates a <tt>Rule</tt>-instance according to the contents of the DOM-subtree starting at
	 * the given 'Rule'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'Rule'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>Rule</tt>-instance
	 */
	private static Rule createRule( Element element  ) throws XMLParsingException {
		logger.debug("Creando Rule");
		// optional: <Name>
		String name = XMLTools.getStringValue( "Name", element, null );
		// optional: <Title>
		String title = XMLTools.getStringValue( "Title", element, null );
		// optional: <Abstract>
		String abstract_ = XMLTools.getStringValue( "Abstract", element, null );

		// optional: <Filter>
		boolean isAnElseFilter = false;
		Filter filter = null;
		Element filterElement = XMLTools.getChildElement( "Filter", element );
		if ( filterElement != null ) {
			filter = AbstractFilter.buildFromDOM( filterElement );
		}

		// optional: <ElseFilter>
		Element elseFilterElement = XMLTools.getChildElement( "ElseFilter", element );
		if ( elseFilterElement != null ) {
			isAnElseFilter = true;
		}

		if ( ( filterElement != null ) && ( elseFilterElement != null ) ) {
			throw new XMLParsingException( "Element 'Rule' may contain a 'Filter'- or "
					+ "an 'ElseFilter'-element, but not both!" );
		}

		double min = 0.0;
		double max = 9E99;
		try {
			// optional: <MinScaleDenominator>
			min = Double.parseDouble(XMLTools.getStringValue("MinScaleDenominator", element, "0.0"));
			// optional: <MaxScaleDenominator>
			max = Double.parseDouble(XMLTools.getStringValue("MaxScaleDenominator", element, "9E99"));
		} catch (Exception e) {
			logger.error(e);
		}
		
		// optional: different Symbolizer-elements
		NodeList symbolizerNL = element.getChildNodes();
		Vector symbolizerList = new Vector( symbolizerNL.getLength() );

		for ( int i = 0; i < symbolizerNL.getLength(); i++ ) {
			if ( symbolizerNL.item( i ) instanceof Element ) {
				Element symbolizerElement = (Element) symbolizerNL.item( i );

				String symbolizerName = symbolizerElement.getLocalName();

				if ( symbolizerName.equals( "LineSymbolizer" ) ) {
					symbolizerList.addElement( createLineSymbolizer( symbolizerElement, min, max ) );
				} else if ( symbolizerName.equals( "PointSymbolizer" ) ) {
					symbolizerList.addElement( createPointSymbolizer( symbolizerElement, min, max  ) );
				} else if ( symbolizerName.equals( "PolygonSymbolizer" ) ) {
					symbolizerList.addElement( createPolygonSymbolizer( symbolizerElement, min, max ) );
				} else if ( symbolizerName.equals( "TextSymbolizer" ) ) {
					symbolizerList.addElement( createTextSymbolizer( symbolizerElement, min, max ) );
				}
			}
		}

		Symbolizer[] symbolizers = new Symbolizer[symbolizerList.size()];
		symbolizerList.copyInto(symbolizers);

		return new Rule( symbolizers, name, title, abstract_, filter, isAnElseFilter, min, max );
	}

	/**
	 * Creates a <tt>PointSymbolizer</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'PointSymbolizer'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'PointSymbolizer'-<tt>Element</tt>
	 * @param min
	 *            scale-constraint to be used
	 * @param max
	 *            scale-constraint to be used
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>PointSymbolizer</tt>-instance
	 */
	private static PointSymbolizer createPointSymbolizer( Element element, double min, double max ) throws XMLParsingException {
		logger.debug("Creando PointSymbolizer");
		// optional: <Graphic>
		Graphic graphic = null;
		Element graphicElement = XMLTools.getChildElement("Graphic", element);

		if ( graphicElement != null ) {
			graphic = createGraphic( graphicElement  );
		}

		PointSymbolizer ps = null;
		String respClass = XMLTools.getAttrValue(element, "responsibleClass", null);
		if ( respClass == null ) {
			ps = new PointSymbolizer( graphic, min, max );
		} else {
			ps = new PointSymbolizer( graphic, respClass, min, max );
		}

		return ps;
	}

	/**
	 * Creates a <tt>LineSymbolizer</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'LineSymbolizer'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'LineSymbolizer'-<tt>Element</tt>
	 * @param min
	 *            scale-constraint to be used
	 * @param max
	 *            scale-constraint to be used
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>LineSymbolizer</tt>-instance
	 */
	private static LineSymbolizer createLineSymbolizer( Element element, double min, double max ) throws XMLParsingException {
		logger.debug("Creando LineSymbolizer");
		// optional: <Stroke>
		Stroke stroke = null;
		Element strokeElement = XMLTools.getChildElement("Stroke", element);

		if ( strokeElement != null ) {
			stroke = createStroke( strokeElement );
		}

		LineSymbolizer ls = null;
		String respClass = XMLTools.getAttrValue( element, "responsibleClass", null );
		if ( respClass == null ) {
			ls = new LineSymbolizer( stroke, min, max );
		} else {
			ls = new LineSymbolizer( stroke, respClass, min, max );
		}
		return ls;
	}

	/**
	 * Creates a <tt>PolygonSymbolizer</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'PolygonSymbolizer'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'PolygonSymbolizer'-<tt>Element</tt>
	 * @param min
	 *            scale-constraint to be used
	 * @param max
	 *            scale-constraint to be used
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>PolygonSymbolizer</tt>-instance
	 */
	private static PolygonSymbolizer createPolygonSymbolizer( Element element, double min, double max ) throws XMLParsingException {
		logger.debug("Creando PolygonSymbolizer");
		// optional: <Fill>
		Fill fill = null;
		Element fillElement = XMLTools.getChildElement( "Fill", element );

		if ( fillElement != null ) {
			fill = createFill( fillElement );
		}

		// optional: <Stroke>
		Stroke stroke = null;
		Element strokeElement = XMLTools.getChildElement( "Stroke", element );

		if ( strokeElement != null ) {
			stroke = createStroke( strokeElement );
		}

		PolygonSymbolizer ps = null;
		String respClass = XMLTools.getAttrValue( element, "responsibleClass", null );
		if ( respClass == null ) {
			ps = new PolygonSymbolizer( fill, stroke, min, max );
		} else {
			ps = new PolygonSymbolizer( fill, stroke, respClass, min, max );
		}

		return ps;
	}

	/**
	 * Creates a <tt>Fill</tt>-instance according to the contents of the DOM-subtree starting at
	 * the given 'Fill'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'Fill'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>Fill</tt>-instance
	 */
	private static Fill createFill( Element element ) throws XMLParsingException {
		// optional: <CssParameter>s
		ElementList nl = XMLTools.getChildElements( "CssParameter", element );
		Hashtable cssParams = new Hashtable( nl.getLength() );

		for ( int i = 0; i < nl.getLength(); i++ ) {
			CssParameter cssParam = createCssParameter( nl.item( i ) );
			cssParams.put( cssParam.getName(), cssParam );
		}

		return new Fill(cssParams);
	}

	/**
	 * Creates an <tt>ExternalGraphic</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'ExternalGraphic'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'ExternalGraphic'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>ExternalGraphic</tt>-instance
	 */
	private static ExternalGraphic createExternalGraphic( Element element, URL baseURL ) throws XMLParsingException {
		logger.debug("Creando ExternalGraphic");
		// required: <OnlineResource>
		Element onlineResourceElement = XMLTools.getRequiredChildElement( "OnlineResource", element );

		// required: href-Attribute (in <OnlineResource>)
		String href = XMLTools.getRequiredAttrValue( "xlink:href", onlineResourceElement );
		
		logger.debug("url graphic->" + href);
		URL url = null;
		try {
			url = new URL( href );
			
			
		} catch ( MalformedURLException e ) {
//			LOG.logDebug( e.getMessage(), e );
			throw new XMLParsingException( "Value ('" + href + "') of attribute 'href' of "
					+ "element 'OnlineResoure' does not denote a valid URL" );
		}

		// required: <Format> (in <OnlineResource>)
		String format = XMLTools.getRequiredStringValue( "Format", element );

		return new ExternalGraphic( format, url,baseURL );
	}

    /**
	 * Creates a <tt>Mark</tt>-instance according to the contents of the DOM-subtree starting at
	 * the given 'Mark'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'Mark'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>Mark</tt>-instance
	 */
	private static Mark createMark( Element element ) throws XMLParsingException {
		logger.debug("Creando Mark");
		Stroke stroke = null;
		Fill fill = null;

		// optional: <WellKnownName>
		String wkn = XMLTools.getStringValue( "WellKnownName", element, null );

		// optional: <Stroke>
		Element strokeElement = XMLTools.getChildElement( "Stroke", element );

		if ( strokeElement != null ) {
			stroke = createStroke( strokeElement );
		}

		// optional: <Fill>
		Element fillElement = XMLTools.getChildElement( "Fill", element );

		if ( fillElement != null ) {
			fill = createFill( fillElement );
		}

		return new Mark( wkn, stroke, fill );
	}

	/**
	 * Creates a <tt>Stroke</tt>-instance according to the contents of the DOM-subtree starting
	 * at the given 'Stroke'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'Stroke'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>Stroke</tt>-instance
	 */
	private static Stroke createStroke( Element element ) throws XMLParsingException {
		// optional: <CssParameter>s
		ElementList nl = XMLTools.getChildElements( "CssParameter", element );
		Hashtable cssParams = new Hashtable( nl.getLength() );

		for ( int i = 0; i < nl.getLength(); i++ ) {
			CssParameter cssParam = createCssParameter( nl.item( i ) );
			cssParams.put( cssParam.getName(), cssParam );
		}

		return new Stroke(cssParams);
	}

	/**
	 * Creates a <tt>Graphic</tt>-instance according to the contents of the DOM-subtree starting
	 * at the given 'Graphic'-element.
	 * <p>
	 * 
	 * @param element
	 *            the 'Graphic'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>Graphic</tt>-instance
	 */
	private static Graphic createGraphic( Element element  ) throws XMLParsingException {
		logger.debug("Creando Graphic");
		// optional: <Size>
		int size = -1;

		// optional: <ExternalGraphic>s / <Mark>s
		NodeList nodelist = element.getChildNodes();
		Vector marksAndExtGraphicsList = new Vector();

		for ( int i = 0; i < nodelist.getLength(); i++ ) {
			if ( nodelist.item( i ) instanceof Element ) {
				Element child = (Element) nodelist.item( i );

				String childName = child.getLocalName();

				if ( childName.equals( "ExternalGraphic" ) ) {
					marksAndExtGraphicsList.addElement( createExternalGraphic( child , baseURL) );
				} else if ( childName.equals( "Mark" ) ) {
					marksAndExtGraphicsList.addElement( createMark( child ) );
				} else if ( childName.equals( "Size" ) ) {
					String value = "";
					Element ogcLiteral = XMLTools.getChildElement(child, "Literal");
					if (ogcLiteral == null) {
						value = XMLTools.getStringValue(child);
					}
					else {
						value = XMLTools.getStringValue(ogcLiteral);
					}
					try {
						size = (int) Float.parseFloat(value);
					} catch (NumberFormatException e) {
						logger.error(e);
					}
				}
			}
		}

		Object[] marksAndExtGraphics = new Object[marksAndExtGraphicsList.size()];
		marksAndExtGraphicsList.copyInto(marksAndExtGraphics);

		return new Graphic(marksAndExtGraphics, size);
	}

	/**
	 * Creates a <tt>CssParameter</tt>-instance according to the contents of the DOM-subtree
	 * starting at the given 'CssParameter'-<tt>Element</tt>.
	 * <p>
	 * 
	 * @param element
	 *            the 'CssParamter'-<tt>Element</tt>
	 * @throws XMLParsingException
	 *             if a syntactic or semantic error in the DOM-subtree is encountered
	 * @return the constructed <tt>CssParameter</tt>-instance
	 */
	private static CssParameter createCssParameter(Element element) throws XMLParsingException {
		// required: name-Attribute
		String name = XMLTools.getRequiredAttrValue("name", element);
		String value = "";
		Element ogcLiteral = XMLTools.getChildElement(element, "Literal");
		if (ogcLiteral == null) {
			value = XMLTools.getStringValue(element);
		}
		else {
			value = XMLTools.getStringValue(ogcLiteral);
		}

		return ( new CssParameter( name, value ) );
	}
	
}
