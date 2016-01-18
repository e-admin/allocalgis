/**
 * StyleFactory2.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 19-jul-2004
 */
package org.deegree_impl.graphics.sld;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.deegree.graphics.sld.CssParameter;
import org.deegree.graphics.sld.Extent;
import org.deegree.graphics.sld.ExternalGraphic;
import org.deegree.graphics.sld.Fill;
import org.deegree.graphics.sld.Font;
import org.deegree.graphics.sld.Geometry;
import org.deegree.graphics.sld.Graphic;
import org.deegree.graphics.sld.GraphicFill;
import org.deegree.graphics.sld.GraphicStroke;
import org.deegree.graphics.sld.Halo;
import org.deegree.graphics.sld.LabelPlacement;
import org.deegree.graphics.sld.LinePlacement;
import org.deegree.graphics.sld.Mark;
import org.deegree.graphics.sld.ParameterValueType;
import org.deegree.graphics.sld.PointPlacement;
import org.deegree.graphics.sld.Stroke;
import org.deegree.services.wfs.filterencoding.Expression;
import org.deegree.xml.ElementList;
import org.deegree.xml.XMLParsingException;
import org.deegree.xml.XMLTools;
import org.deegree_impl.services.wfs.filterencoding.Expression_Impl;
import org.deegree_impl.services.wfs.filterencoding.PropertyName;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author enxenio s.l.
 */
public class StyleFactory2 {
	
	private static String sldNS = "http://www.opengis.net/sld";
	private static String xlnNS = "http://www.w3.org/1999/xlink";
	private static String ogcNS = "http://www.opengis.net/ogc";
	
	public static ParameterValueType createLabel(String attributeName) {
		
		return StyleFactory.createParameterValueType(new Expression[]{new PropertyName(attributeName)});
	}

	public static Graphic cloneGraphic(Graphic graphic) {
	
		Graphic_Impl graphic_Impl = (Graphic_Impl)graphic;
		String graphicAsString = graphic_Impl.exportAsXML();
		int firstTagEnd = graphicAsString.indexOf(">");
		graphicAsString = graphicAsString.substring(0, firstTagEnd) + " xmlns=\"" + sldNS + "\"" + " xmlns:ogc=\""+ ogcNS +"\""+graphicAsString.substring(firstTagEnd); 
		StringReader stringReader = new StringReader(graphicAsString);
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document docJDOM = null;
		Document docDOM = null;
		Graphic cloneGraphic = null;
		try {
			docJDOM = builder.build(stringReader);
			DOMOutputter domOutputter = new DOMOutputter();
			docDOM = domOutputter.output(docJDOM);
			Element elementDOM = (Element)docDOM.getDocumentElement();
			cloneGraphic = createGraphic(elementDOM);
		} catch (JDOMException e) {
			e.printStackTrace();
 		} catch (IOException e) {
            System.err.println(e);
        } catch (XMLParsingException xmle) {
			xmle.printStackTrace();
		}
		return cloneGraphic;
	}
	
	public static Stroke cloneStroke(Stroke stroke) {
	
		Stroke_Impl stroke_Impl = (Stroke_Impl)stroke;
		String strokeAsString = stroke_Impl.exportAsXML();
		int firstTagEnd = strokeAsString.indexOf(">");
		strokeAsString = strokeAsString.substring(0, firstTagEnd) + " xmlns=\"" + sldNS + "\"" + " xmlns:ogc=\""+ ogcNS +"\""+ strokeAsString.substring(firstTagEnd); 
		StringReader stringReader = new StringReader(strokeAsString);
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document docJDOM = null;
		Document docDOM = null;
		Stroke cloneStroke = null;
		try {
			docJDOM = builder.build(stringReader);
			DOMOutputter domOutputter = new DOMOutputter();
			docDOM = domOutputter.output(docJDOM);
			Element elementDOM = (Element)docDOM.getDocumentElement();
			cloneStroke = createStroke(elementDOM);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
            System.err.println(e);
        } catch (XMLParsingException xmle) {
			xmle.printStackTrace();
		}
		return cloneStroke;
				
	}

	public static Fill cloneFill(Fill fill) {
	
		Fill_Impl fill_Impl = (Fill_Impl)fill;
		String fillAsString = fill_Impl.exportAsXML();
		int firstTagEnd = fillAsString.indexOf(">");
		fillAsString = fillAsString.substring(0, firstTagEnd) + " xmlns=\"" + sldNS + "\"" + " xmlns:ogc=\""+ ogcNS +"\""+ fillAsString.substring(firstTagEnd); 
		StringReader stringReader = new StringReader(fillAsString);
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document docJDOM = null;
		Document docDOM = null;
		Fill cloneFill = null;
		try {
			docJDOM = builder.build(stringReader);
			DOMOutputter domOutputter = new DOMOutputter();
			docDOM = domOutputter.output(docJDOM);
			Element elementDOM = (Element)docDOM.getDocumentElement();
			cloneFill = createFill(elementDOM);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
            System.err.println(e);
        } catch (XMLParsingException xmle) {
			xmle.printStackTrace();
		}
		return cloneFill;
				
	}

	public static Font cloneFont(Font font) {
	
		Font_Impl font_Impl = (Font_Impl)font;
		String fontAsString = font_Impl.exportAsXML();
		int firstTagEnd = fontAsString.indexOf(">");
		fontAsString = fontAsString.substring(0, firstTagEnd) + " xmlns=\"" + sldNS + "\"" + " xmlns:ogc=\""+ ogcNS +"\""+ fontAsString.substring(firstTagEnd); 
		StringReader stringReader = new StringReader(fontAsString);
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document docJDOM = null;
		Document docDOM = null;
		Font cloneFont = null;
		try {
			docJDOM = builder.build(stringReader);
			DOMOutputter domOutputter = new DOMOutputter();
			docDOM = domOutputter.output(docJDOM);
			Element elementDOM = (Element)docDOM.getDocumentElement();
			cloneFont = createFont(elementDOM);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
            System.err.println(e);
        } catch (XMLParsingException xmle) {
			xmle.printStackTrace();
		}
		return cloneFont;
				
	}

	public static Halo cloneHalo(Halo halo) {
	
		Halo_Impl halo_Impl = (Halo_Impl)halo;
		String haloAsString = halo_Impl.exportAsXML();
		int firstTagEnd = haloAsString.indexOf(">");
		haloAsString = haloAsString.substring(0, firstTagEnd) + " xmlns=\"" + sldNS + "\"" + " xmlns:ogc=\""+ ogcNS +"\""+ haloAsString.substring(firstTagEnd); 
		StringReader stringReader = new StringReader(haloAsString);
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document docJDOM = null;
		Document docDOM = null;
		Halo cloneHalo = null;
		try {
			docJDOM = builder.build(stringReader);
			DOMOutputter domOutputter = new DOMOutputter();
			docDOM = domOutputter.output(docJDOM);
			Element elementDOM = (Element)docDOM.getDocumentElement();
			cloneHalo = createHalo(elementDOM);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
            System.err.println(e);
        } catch (XMLParsingException xmle) {
			xmle.printStackTrace();
		}
		return cloneHalo;
				
	}

	public static LabelPlacement cloneLabelPlacement(LabelPlacement labelPlacement) {
	
		LabelPlacement_Impl labelPlacement_Impl = (LabelPlacement_Impl)labelPlacement;
		String labelPlacementAsString = labelPlacement_Impl.exportAsXML();
		int firstTagEnd = labelPlacementAsString.indexOf(">");
		labelPlacementAsString = labelPlacementAsString.substring(0, firstTagEnd) + " xmlns=\"" + sldNS + "\"" + " xmlns:ogc=\""+ ogcNS +"\""+ labelPlacementAsString.substring(firstTagEnd); 
		StringReader stringReader = new StringReader(labelPlacementAsString);
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document docJDOM = null;
		Document docDOM = null;
		LabelPlacement cloneLabelPlacement = null;
		try {
			docJDOM = builder.build(stringReader);
			DOMOutputter domOutputter = new DOMOutputter();
			docDOM = domOutputter.output(docJDOM);
			Element elementDOM = (Element)docDOM.getDocumentElement();
			cloneLabelPlacement = createLabelPlacement(elementDOM);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
            System.err.println(e);
        } catch (XMLParsingException xmle) {
			xmle.printStackTrace();
		}
		return cloneLabelPlacement;
				
	}

	public static ParameterValueType cloneLabel(ParameterValueType label) {
	
		ParameterValueType_Impl label_Impl = (ParameterValueType_Impl)label;
		String labelAsString = label_Impl.exportAsXML();
		labelAsString = "<Label" + " xmlns=\"" + sldNS + "\"" + " xmlns:ogc=\""+ ogcNS +"\"" + ">" +
						labelAsString + 
						"</Label>"; 
		StringReader stringReader = new StringReader(labelAsString);
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document docJDOM = null;
		Document docDOM = null;
		ParameterValueType cloneLabel = null;
		try {
			docJDOM = builder.build(stringReader);
			DOMOutputter domOutputter = new DOMOutputter();
			docDOM = domOutputter.output(docJDOM);
			Element elementDOM = (Element)docDOM.getDocumentElement();
			cloneLabel = createParameterValueType(elementDOM);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
        } catch (XMLParsingException xmle) {
			xmle.printStackTrace();
		}
		return cloneLabel;
				
	}

	
	public static Geometry createGeometry(String propertyName) {
		return new Geometry_Impl(propertyName, null);
	}
	
	/**
	* Creates a <tt>Graphic</tt>-instance according to the contents of
	* the DOM-subtree starting at the given 'Graphic'-element.
	* <p>
	* @param element the 'Graphic'-<tt>Element</tt>
	* @throws XMLParsingException if a syntactic or semantic error
	*         in the DOM-subtree is encountered
	* @return the constructed <tt>Graphic</tt>-instance
	*/
   public static Graphic createGraphic( Element element ) throws XMLParsingException {
	   Graphic graphic = null;

	   // optional: <Opacity>
	   ParameterValueType opacity = null;
	   // optional: <Size>
	   ParameterValueType size = null;
	   // optional: <Rotation>
	   ParameterValueType rotation = null;
	   // optional: <ExternalGraphic>s / <Mark>s
	   NodeList nodelist = element.getChildNodes();
	   ArrayList marksAndExtGraphicsList = new ArrayList();

	   for ( int i = 0; i < nodelist.getLength(); i++ ) {
		   if ( nodelist.item( i ) instanceof Element ) {
			   Element child = (Element)nodelist.item( i );
			String namespace = child.getNamespaceURI();

			if ( !sldNS.equals( namespace ) ) {
				continue;
			}

			String childName = child.getLocalName();

			   if ( childName.equals( "ExternalGraphic" ) ) {
				   marksAndExtGraphicsList.add( createExternalGraphic( child ) );
			   } else if ( childName.equals( "Mark" ) ) {
				   marksAndExtGraphicsList.add( createMark( child ) );
			   } else if ( childName.equals( "Opacity" ) ) {
				   opacity = createParameterValueType( child );
			   } else if ( childName.equals( "Size" ) ) {
				   size = createParameterValueType( child );
			   } else if ( childName.equals( "Rotation" ) ) {
				   rotation = createParameterValueType( child );
			   }
		 }
	   }

	   Object[] marksAndExtGraphics = (Object[])marksAndExtGraphicsList.toArray( 
					new Object[marksAndExtGraphicsList.size()] );

	   return new Graphic_Impl( marksAndExtGraphics, opacity, size, rotation );
	}
	
	/**
	 * Creates an <tt>ExternalGraphic</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'ExternalGraphic'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'ExternalGraphic'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>ExternalGraphic</tt>-instance
	 */
	public static ExternalGraphic createExternalGraphic( Element element ) throws XMLParsingException {
		// required: <OnlineResource>
		Element onlineResourceElement = XMLTools.getRequiredChildByName( "OnlineResource", sldNS, 
																		 element );

		// required: href-Attribute (in <OnlineResource>)
		String href = XMLTools.getRequiredAttrValue( "href", xlnNS, onlineResourceElement );
		URL url = null;

		try {
			System.out.println("HREF:"+href);
			url = new URL( href );
		} catch ( MalformedURLException e ) {
			throw new XMLParsingException( "Value ('" + href + "') of attribute 'href' of " + 
										   "element 'OnlineResoure' does not denote a valid URL: " + 
										   e.getMessage() );
		}

		// required: <Format> (in <OnlineResource>)
		String format = XMLTools.getRequiredStringValue( "Format", sldNS, element );

		return new ExternalGraphic_Impl( format, url );
	}
	
	/**
	 * Creates a <tt>Mark</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'Mark'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'Mark'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>Mark</tt>-instance
	 */
	public static Mark createMark( Element element ) throws XMLParsingException {
		Stroke stroke = null;
		Fill fill = null;

		// optional: <WellKnownName>
		String wkn = XMLTools.getStringValue( "WellKnownName", sldNS, element, null );

		// optional: <Stroke>
		Element strokeElement = XMLTools.getChildByName( "Stroke", sldNS, element );

		if ( strokeElement != null ) {
			stroke = createStroke( strokeElement );
		}

		// optional: <Fill>
		Element fillElement = XMLTools.getChildByName( "Fill", sldNS, element );

		if ( fillElement != null ) {
			fill = createFill( fillElement );
		}

		return new Mark_Impl( wkn, stroke, fill );
	}

	/**
	 * Creates a <tt>Stroke</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'Stroke'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'Stroke'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>Stroke</tt>-instance
	 */
	public static Stroke createStroke( Element element ) throws XMLParsingException {
		GraphicFill gf = null;
		GraphicStroke gs = null;

		// optional: <GraphicFill>
		Element gfElement = XMLTools.getChildByName( "GraphicFill", sldNS, element );

		if ( gfElement != null ) {
			gf = createGraphicFill( gfElement );
		}

		// optional: <GraphicStroke>
		Element gsElement = XMLTools.getChildByName( "GraphicStroke", sldNS, element );

		if ( gsElement != null ) {
			gs = createGraphicStroke( gsElement );
		}

		// optional: <CssParameter>s
		ElementList nl = XMLTools.getChildElementsByName( "CssParameter", sldNS, element );
		HashMap cssParams = new HashMap( nl.getLength() );

		for ( int i = 0; i < nl.getLength(); i++ ) {
			CssParameter cssParam = createCssParameter( (Element)nl.item( i ) );
			cssParams.put( cssParam.getName(), cssParam );
		}

		return new Stroke_Impl( cssParams, gs, gf );
	}

	/**
	 * Creates a <tt>Fill</tt>-instance according to the contents of the
	 * DOM-subtree starting at the given 'Fill'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'Fill'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>Fill</tt>-instance
	 */
	public static Fill createFill( Element element ) throws XMLParsingException {
		// optional: <GraphicFill>
		GraphicFill graphicFill = null;
		Element graphicFillElement = XMLTools.getChildByName( "GraphicFill", sldNS, element );

		if ( graphicFillElement != null ) {
			graphicFill = createGraphicFill( graphicFillElement );
		}

		// optional: <CssParameter>s
		ElementList nl = XMLTools.getChildElementsByName( "CssParameter", sldNS, element );
		HashMap cssParams = new HashMap( nl.getLength() );

		for ( int i = 0; i < nl.getLength(); i++ ) {
			CssParameter cssParam = createCssParameter( (Element)nl.item( i ) );
			cssParams.put( cssParam.getName(), cssParam );
		}

		return new Fill_Impl( cssParams, graphicFill );
	}

	/**
	 * Creates a <tt>GraphicFill</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'GraphicFill'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'GraphicFill'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>GraphicFill</tt>-instance
	 */
	public static GraphicFill createGraphicFill( Element element ) throws XMLParsingException {
		// required: <Graphic>
		Element graphicElement = XMLTools.getRequiredChildByName( "Graphic", sldNS, element );
		Graphic graphic = createGraphic( graphicElement );

		return new GraphicFill_Impl( graphic );
	}

	/**
	 * Creates a <tt>GraphicStroke</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'GraphicStroke'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'GraphicStroke'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>GraphicStroke</tt>-instance
	 */
	public static GraphicStroke createGraphicStroke( Element element ) throws XMLParsingException {
		// required: <Graphic>
		Element graphicElement = XMLTools.getRequiredChildByName( "Graphic", sldNS, element );
		Graphic graphic = createGraphic( graphicElement );

		return new GraphicStroke_Impl( graphic );
	}

	/**
	 * Creates a <tt>CssParameter</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'CssParameter'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'CssParamter'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>CssParameter</tt>-instance
	 */
	public static CssParameter createCssParameter( Element element ) throws XMLParsingException {
		// required: name-Attribute
		String name = XMLTools.getRequiredAttrValue( "name", element );
		ParameterValueType pvt = createParameterValueType( element );

		return ( new CssParameter_Impl( name, pvt ) );
	}
	
	/**
	 * Creates a <tt>ParameterValueType</tt>-instance according to the contents
	 * of the DOM-subtree starting at the given <tt>Element</tt>.
	 * <p>
	 * @param element the <tt>Element</tt> (must be of the type
	 *        sld:ParameterValueType)
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>ParameterValueType</tt>-instance
	 */
	public static ParameterValueType createParameterValueType( Element element )
												 throws XMLParsingException {
		// mix of text nodes and <wfs:Expression>-elements
		ArrayList componentList = new ArrayList();
		NodeList nl = element.getChildNodes();

		for ( int i = 0; i < nl.getLength(); i++ ) {
			Node node = nl.item( i );

			switch ( node.getNodeType() ) {
				case Node.TEXT_NODE:
				{
					componentList.add( node.getNodeValue() );
					break;
				}
				case Node.ELEMENT_NODE:
				{
					Expression expression = Expression_Impl.buildFromDOM( (Element)node );
					componentList.add( expression );
					break;
				}
				default:
					throw new XMLParsingException( 
							"Elements of type 'ParameterValueType' may only " + 
							"consist of CDATA and 'ogc:Expression'-elements!" );
			}
		}

		Object[] components = (Object[])componentList.toArray( new Object[componentList.size()] );
		return new ParameterValueType_Impl( components );
	}
	
	/**
	 * Creates a <tt>Font</tt>-instance according to the contents of the
	 * DOM-subtree starting at the given 'Font'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'Font'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>Font</tt>-instance
	 */
	public static Font createFont( Element element ) throws XMLParsingException {

		// optional: <CssParameter>s
		ElementList nl = XMLTools.getChildElementsByName ("CssParameter", sldNS, element);
		HashMap cssParams = new HashMap( nl.getLength() );

		for ( int i = 0; i < nl.getLength(); i++ ) {
			CssParameter cssParam = createCssParameter( (Element)nl.item( i ) );
			cssParams.put( cssParam.getName(), cssParam );
		}

		return new Font_Impl( cssParams );
	}

	/**
	 * Creates a <tt>Halo</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'Halo'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'Halo'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>Halo</tt>-instance
	 */
	private static Halo createHalo( Element element ) throws XMLParsingException {
		// optional: <Radius>
		ParameterValueType radius = null;
		Element radiusElement = XMLTools.getChildByName( "Radius", sldNS, element );

		if ( radiusElement != null ) {
			radius = createParameterValueType( radiusElement );
		}

		// optional: <Fill>
		Fill fill = null;
		Element fillElement = XMLTools.getChildByName( "Fill", sldNS, element );

		if ( fillElement != null ) {
			fill = createFill( fillElement );
		}

		// optional: <Stroke>
		Stroke stroke = null;
		Element strokeElement = XMLTools.getChildByName( "Stroke", sldNS, element );

		if ( strokeElement != null ) {
			stroke = createStroke( strokeElement );
		}

		return new Halo_Impl( radius, fill, stroke );
	}

	/**
	 * Creates a <tt>LabelPlacement</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'LabelPlacement'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'LabelPlacement'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>LabelPlacement</tt>-instance
	 */
	private static LabelPlacement createLabelPlacement( Element element ) throws XMLParsingException {
		LabelPlacement labelPlacement = null;

		// required: <PointPlacement> / <LinePlacement>
		NodeList nodelist = element.getChildNodes();
		PointPlacement pPlacement = null;
		LinePlacement lPlacement = null;

		for ( int i = 0; i < nodelist.getLength(); i++ ) {
			if ( nodelist.item( i ) instanceof Element ) {
				Element child = (Element)nodelist.item( i );
				String namespace = child.getNamespaceURI();

				if ( !sldNS.equals( namespace ) ) {
					continue;
				}

				String childName = child.getLocalName();

				if ( childName.equals( "PointPlacement" ) ) {
					pPlacement = createPointPlacement( child );
				} else if ( childName.equals( "LinePlacement" ) ) {
					lPlacement = createLinePlacement( child );
				}
			}
		}

		if ( ( pPlacement != null ) && ( lPlacement == null ) ) {
			labelPlacement = new LabelPlacement_Impl( pPlacement );
		} else if ( ( pPlacement == null ) && ( lPlacement != null ) ) {
			labelPlacement = new LabelPlacement_Impl( lPlacement );
		} else {
			throw new XMLParsingException( "Element 'LabelPlacement' must contain exactly one " + 
										   "'PointPlacement'- or one 'LinePlacement'-element!" );
		}

		return labelPlacement;
	}

	/**
	 * Creates a <tt>PointPlacement</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'PointPlacement'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'PointPlacement'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>PointPlacement</tt>-instance
	 */
	private static PointPlacement createPointPlacement( Element element ) throws XMLParsingException {

		// optional: auto-Attribute (this is deegree-specific)
		boolean auto = false;
		String autoStr = XMLTools.getAttrValue (element, "auto");

		if (autoStr != null && autoStr.equals ("true")) {
			auto = true;
		}
        
		// optional: <AnchorPoint>
		ParameterValueType[] anchorPoint = null;
		Element apElement = XMLTools.getChildByName( "AnchorPoint", sldNS, element );

		if ( apElement != null ) {
			anchorPoint = new ParameterValueType[2];

			Element apXElement = XMLTools.getChildByName( "AnchorPointX", sldNS, apElement );
			Element apYElement = XMLTools.getChildByName( "AnchorPointY", sldNS, apElement );

			if ( ( apXElement == null ) || ( apYElement == null ) ) {
				throw new XMLParsingException( "Element 'AnchorPoint' must contain exactly one " + 
											   "'AnchorPointX'- and one 'AnchorPointY'-element!" );
			}

			anchorPoint[0] = createParameterValueType( apXElement );
			anchorPoint[1] = createParameterValueType( apYElement );
		}

		// optional: <Displacement>
		ParameterValueType[] displacement = null;
		Element dElement = XMLTools.getChildByName( "Displacement", sldNS, element );

		if ( dElement != null ) {
			displacement = new ParameterValueType[2];

			Element dXElement = XMLTools.getChildByName( "DisplacementX", sldNS, dElement );
			Element dYElement = XMLTools.getChildByName( "DisplacementY", sldNS, dElement );

			if ( ( dXElement == null ) || ( dYElement == null ) ) {
				throw new XMLParsingException( "Element 'Displacement' must contain exactly one " + 
											   "'DisplacementX'- and one 'DisplacementY'-element!" );
			}

			displacement[0] = createParameterValueType( dXElement );
			displacement[1] = createParameterValueType( dYElement );
		}

		// optional: <Rotation>
		ParameterValueType rotation = null;
		Element rElement = XMLTools.getChildByName( "Rotation", sldNS, element );

		if ( rElement != null ) {
			rotation = createParameterValueType( rElement );
		}

		return new PointPlacement_Impl( anchorPoint, displacement, rotation, auto );
	}

	/**
	 * Creates a <tt>LinePlacement</tt>-instance according to the contents of
	 * the DOM-subtree starting at the given 'LinePlacement'-<tt>Element</tt>.
	 * <p>
	 * @param element the 'LinePlacement'-<tt>Element</tt>
	 * @throws XMLParsingException if a syntactic or semantic error
	 *         in the DOM-subtree is encountered
	 * @return the constructed <tt>LinePlacement</tt>-instance
	 */
	private static LinePlacement createLinePlacement( Element element ) throws XMLParsingException {

		// optional: <PerpendicularOffset>
		ParameterValueType pOffset = null;
		Element pOffsetElement = XMLTools.getChildByName ("PerpendicularOffset", sldNS, element);

		if ( pOffsetElement != null ) {
			pOffset = createParameterValueType( pOffsetElement );
		}

		// optional: <Gap> (this is deegree-specific)
		ParameterValueType gap = null;
		Element gapElement = XMLTools.getChildByName ("Gap", sldNS, element);

		if ( gapElement != null ) {
			gap = createParameterValueType( gapElement );
		}

		// optional: <LineWidth> (this is deegree-specific)
		ParameterValueType lineWidth = null;
		Element lineWidthElement = XMLTools.getChildByName ("LineWidth", sldNS, element);

		if ( lineWidthElement != null ) {
			lineWidth = createParameterValueType( lineWidthElement );
		}

		return new LinePlacement_Impl (pOffset, lineWidth, gap);
	}

	/**
	  * creates a graphic object
	  *
	  * @param externalGraphic an external graphic to use if displayable
	  * @param mark a mark to use
	  * @param opacity - the opacity of the graphic
	  * @param size - the size of the graphic
	  *
	  * @return the graphic created
	  */
	 public static Graphic createGraphic( ExternalGraphic externalGraphic, Mark mark,String size) {
            
		 Object[] mae = null;
		 if ( externalGraphic != null && mark != null ) {
			 mae = new Object[] { externalGraphic, mark };
		 } else if ( externalGraphic != null ) {
			 mae = new Object[] { externalGraphic };
		 } else if ( mark != null ) {
			 mae = new Object[] { mark };
		 }
		 ParameterValueType graphicSize = StyleFactory.createParameterValueType(size);
		 return new Graphic_Impl( mae, null, graphicSize, null );
	 }	
	 
	/**
	  * creates a graphic object
	  *
	  * @param externalGraphic an external graphic to use if displayable
	  * @param mark a mark to use
	  * @param opacity - the opacity of the graphic
	  * @param size - the size of the graphic
	  * @param rotation - the rotation from the top of the page of the graphic
	  *
	  * @return the graphic created
	  */
	 public static Graphic createGraphic( ExternalGraphic externalGraphic, Mark mark,double opacity,ParameterValueType size,
	 	double rotation) {
            
		 Object[] mae = null;
		 if ( externalGraphic != null && mark != null ) {
			 mae = new Object[] { externalGraphic, mark };
		 } else if ( externalGraphic != null ) {
			 mae = new Object[] { externalGraphic };
		 } else if ( mark != null ) {
			 mae = new Object[] { mark };
		 }
		 ParameterValueType graphicOpacity = StyleFactory.createParameterValueType(""+opacity);
		 ParameterValueType graphicRotation = StyleFactory.createParameterValueType(""+rotation);
		 return new Graphic_Impl( mae,graphicOpacity,size, graphicRotation);
	 }	

	public static Stroke createStroke( Color color, Expression width, double opacity, 
								float[] dashArray, String lineJoin, String lineCap ) {
		HashMap cssParams = new HashMap();
        
		CssParameter stroke = createCssParameter( "stroke", getColorAsHex( color ) );
		cssParams.put( "stroke", stroke );
		CssParameter strokeOp = createCssParameter( "stroke-opacity", "" + opacity );
		cssParams.put( "stroke-opacity", strokeOp);
		CssParameter strokeWid = createCssParameter( "stroke-width", width );
		cssParams.put( "stroke-width", strokeWid );
		CssParameter strokeLJ = createCssParameter( "stroke-linejoin", lineJoin );
		cssParams.put( "stroke-linejoin", strokeLJ );
		CssParameter strokeCap = createCssParameter( "stroke-linecap", lineCap );
		cssParams.put( "stroke-linecap", strokeCap );
        
		 if ( dashArray != null ) {
			String s = "";
			for (int i = 0; i < dashArray.length-1; i++) {
				s = s + dashArray[i] + ",";                
			}
			s = s + dashArray[dashArray.length-1];
			CssParameter strokeDash = createCssParameter( "stroke-dasharray", s );
			cssParams.put( "stroke-dasharray", strokeDash );
		}

		return new Stroke_Impl( cssParams, null, null);
	}

	public static CssParameter createCssParameter(String name,Expression expr) {
		Expression[] expressionArray = new Expression[]{expr};
		ParameterValueType pvt = StyleFactory.createParameterValueType(expressionArray);
		return new CssParameter_Impl( name, pvt );
	}

	public static CssParameter createCssParameter(String name, String value) {
		ParameterValueType pvt = StyleFactory.createParameterValueType( value );
		return new CssParameter_Impl( name, pvt );
	}

	public static String getColorAsHex(Color color) {
		String r = Integer.toHexString( color.getRed() );
		if ( r.length() < 2 ) r = "0"+r;
		String g = Integer.toHexString( color.getGreen() );
		if ( g.length() < 2 ) g = "0"+g;
		String b = Integer.toHexString( color.getBlue() );
		if ( b.length() < 2 ) b = "0"+b;
		return "#" + r + g + b;
	}

	 public static Extent createExtent(String name,String value) {
	 	
	 	return new Extent_Impl(name,value);
	 }
}
