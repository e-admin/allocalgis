/**
 * XMLTools.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.deegree.framework.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML Tools based on JAXP 1.1 for parsing documents and retrieving node values/node attributes.
 * Furthermore this utility class provides node retrieval based on XPath expressions.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.2 $, $Date: 2011/11/07 17:23:50 $
 */
public final class XMLTools {

	private static final Logger log = (Logger) Logger.getInstance(XMLTools.class);

	private XMLTools() {
		// hidden constructor to prevent instantiation
	}

	// ------------------------------------------------------------------------
	// String value methods
	// ------------------------------------------------------------------------

	/**
	 * Returns the text contained in the specified element.
	 * 
	 * @param node
	 *            current element
	 * @return the textual contents of the element
	 */
	public static String getStringValue( Node node ) {
		NodeList children = node.getChildNodes();
		StringBuffer sb = new StringBuffer( children.getLength() * 500 );
		if ( node.getNodeValue() != null ) {
			sb.append( node.getNodeValue().trim() );
		}
		if ( node.getNodeType() != Node.ATTRIBUTE_NODE ) {
			for ( int i = 0; i < children.getLength(); i++ ) {
				if ( children.item( i ).getNodeType() == Node.TEXT_NODE
						|| children.item( i ).getNodeType() == Node.CDATA_SECTION_NODE ) {
					sb.append( children.item( i ).getNodeValue() );
				}
			}
		}
		return sb.toString();
	}
	
	 
	    

	/**
	 * Returns the text contained in the specified child element of the given element.
	 * 
	 * @param name
	 *            name of the child element
	 * @param node
	 *            current element
	 * @param defaultValue
	 *            default value if element is missing
	 * @return the textual contents of the element or the given default value, if missing
	 */
	public static String getStringValue( String name, Node node, String defaultValue ) {

		String value = defaultValue;
		Element element = getChildElement( name, node );

		if ( element != null ) {
			value = getStringValue( element );
		}
		if ( value == null || value.equals( "" ) ) {
			value = defaultValue;
		}

		return value;
	}

	/**
	 * Returns the text contained in the specified child element of the given element.
	 * 
	 * @param name
	 *            name of the child element
	 * @param namespace
	 *            namespace of the child element
	 * @param node
	 *            current element
	 * @return the textual contents of the element or null, if it is missing
	 * @throws XMLParsingException
	 *             if the specified child element is missing
	 */
	public static String getRequiredStringValue( String name, Node node ) throws XMLParsingException {
		Element element = getRequiredChildElement( name, node );
		return getStringValue( element );
	}

	/**
	 * Returns the value of the specified node attribute.
	 * 
	 * @param name
	 *            name of attribute
	 * @param namespaceURI
	 *            namespace of attribute
	 * @param node
	 *            current element
	 * @return the textual contents of the attribute
	 * @throws XMLParsingException
	 *             if specified attribute is missing
	 */
	public static String getRequiredAttrValue( String name, Node node ) throws XMLParsingException {

		String value = null;
		NamedNodeMap atts = node.getAttributes();
		if ( atts != null ) {
			Attr attribute = null;
			attribute = (Attr) atts.getNamedItem( name );
			
			if ( attribute != null ) {
				value = attribute.getValue();
			}
		}
		if ( value == null ) {
			throw new XMLParsingException( "Required attribute " + name + " of element "
					+ node.getNodeName() + " is missing." );
		}
		return value;
	}
	
	/**
	 * Returns the value of the specified node attribute.
	 * 
	 * @param name
	 *            name of attribute
	 * @param namespaceURI
	 *            namespace of attribute
	 * @param node
	 *            current element
	 * @return the textual contents of the attribute
	 * @throws XMLParsingException
	 *             if specified attribute is missing
	 */
	public static ArrayList getRequiredAttrValues( String name, Node node ) throws XMLParsingException {
		ArrayList attributes = new ArrayList();
		NamedNodeMap atts = node.getAttributes();
		if ( atts != null ) {
			Attr attribute = null;
			attribute = (Attr) atts.getNamedItem( name );
			
			if ( attribute != null ) {
				String [] values = splitStr(attribute.getValue(),",");
				for(int i=0;i<values.length;i++)
					try {
						attributes.add(URLDecoder.decode(values[i],"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
			}
		}
//		if ( value == null ) {
//			throw new XMLParsingException( "Required attribute " + name + " of element "
//					+ node.getNodeName() + " is missing." );
//		}
		return attributes;
	}

	/**
	 * Returns a Strings array of delimitation.
	 * 
	 * @param str
	 *            String
	 * @param delim
	 *            Delimitator
	 * @return Strings array
	 */
	public static String [] splitStr(String str, String delim) {
        StringTokenizer stringTokenizer = new StringTokenizer( str, delim );
        String[] strArr = new String[stringTokenizer.countTokens()];
        int i = 0;
        while( stringTokenizer.hasMoreTokens() ) {
            strArr[i] = stringTokenizer.nextToken();
        }
        return strArr;
    }
	
	/**
	 * Returns the specified child element of the given element. If there is more than one element
	 * with that name, the first one is returned.
	 * 
	 * @param name
	 *            name of the child element
	 * @param namespaceURI
	 *            namespaceURI of the child element
	 * @param node
	 *            current element
	 * @return the element or null, if it is missing
	 * @throws XMLParsingException
	 *             if the specified child element is missing
	 * @throws XMLParsingException
	 * @todo refactoring required
	 */
	public static Element getRequiredChildElement( String name, Node node ) throws XMLParsingException {

		NodeList nl = node.getChildNodes();
		Element element = null;
		Element childElement = null;

		if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
			for ( int i = 0; i < nl.getLength(); i++ ) {
				if ( nl.item( i ) instanceof Element ) {
					element = (Element) nl.item( i );
					if ( element.getLocalName().equals( name ) ) {
						childElement = element;
						break;
					}
				}
			}
		}

		if ( childElement == null ) {
			throw new XMLParsingException( "Required child-element " + name + " of element "
					+ node.getNodeName() + " is missing." );
		}

		return childElement;
	}

	/**
	 * Returns the specified child element of the given element. If there is more than one with that
	 * name, the first one is returned.
	 * 
	 * @param name
	 *            name of the child element
	 * @param namespaceURI
	 *            namespace of the child element
	 * @param node
	 *            current element
	 * @return the element or null, if it is missing
	 * @TODO refactoring required
	 */
	public static Element getChildElement( String name, Node node ) {

		NodeList nl = node.getChildNodes();
		Element element = null;
		Element childElement = null;

		if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
			for ( int i = 0; i < nl.getLength(); i++ ) {
				if ( nl.item( i ) instanceof Element ) {
					element = (Element) nl.item( i );
					if ( element.getLocalName().equals( name ) ) {
						childElement = element;
						break;
					}
				}
			}
		}
		return childElement;
	}

	/**
	 * Returns the specified child elements of the given element.
	 * 
	 * @param name
	 *            name of the child elements
	 * @param namespaceURI
	 *            namespaceURI of the child elements
	 * @param node
	 *            current element
	 * @return list of matching child elements
	 */
	public static ElementList getChildElements( String name, Node node ) {

		NodeList nl = node.getChildNodes();
		Element element = null;
		ElementList elementList = new ElementList();

		if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
			for ( int i = 0; i < nl.getLength(); i++ ) {
				if ( nl.item( i ) instanceof Element ) {
					element = (Element) nl.item( i );

					String s = element.getNamespaceURI();

					if ( element.getLocalName().equals( name ) ) {
						elementList.addElement( element );
					}
				}
			}
		}
		return elementList;
	}

	/**
	 * 
	 * Create a new and empty DOM document.
	 * 
	 * @return a new and empty DOM document.
	 */
	public static Document create() {
		return getDocumentBuilder().newDocument();
	}

	/**
	 * Create a new document builder with:
	 * <UL>
	 * <li>namespace awareness = true
	 * <li>whitespace ignoring = false
	 * <li>validating = false
	 * <li>expandind entity references = false
	 * </UL>
	 * 
	 * @return new document builder
	 */
	public static synchronized DocumentBuilder getDocumentBuilder() {
		DocumentBuilder builder = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware( true );
			factory.setExpandEntityReferences( false );
			factory.setIgnoringElementContentWhitespace( false );
			factory.setValidating( false );
			builder = factory.newDocumentBuilder();
		} catch ( Exception ex ) {
			log.error( ex.getMessage(), ex );
		}
		return builder;
	}

	/**
	 * Returns the specified attribute value of the given node.
	 * 
	 * @param node
	 *            current element
	 * @param attrName
	 *            local name of the attribute
	 * 
	 * @return the value of the attribute or null, if it is missing
	 * @see #getAttrValue(Node, URI, String, String) instead
	 */
	public static String getAttrValue( Node node, String attrName ) {
		NamedNodeMap atts = node.getAttributes();
		if ( atts == null ) {
			return null;
		}
		Attr a = (Attr) atts.getNamedItem( attrName );
		if ( a != null ) {
			return a.getValue();
		}
		return null;
	}

	/**
	 * Returns the specified attribute value of the given node.
	 * 
	 * @param node
	 *            current element
	 * @param namespaceURI
	 *            namespace of the attribute
	 * @param attrName
	 *            local name of the attribute
	 * @param defaultVal
	 *            default value to be returned if attribute is nat available
	 * 
	 * @return the value of the attribute or null, if it is missing
	 */
	public static String getAttrValue( Node node, String attrName, String defaultVal ) {
		NamedNodeMap atts = node.getAttributes();
		if ( atts == null ) {
			return defaultVal;
		}
		Attr a = null;
		a = (Attr) atts.getNamedItem( attrName );
		if ( a != null ) {
			return a.getValue();
		}
		return defaultVal;
	}

	/**
	 * Parses an XML document and returns a DOM object. The underlying input stream is closed at the
	 * end.
	 * 
	 * @param reader
	 *            accessing the resource to parse
	 * @return a DOM object, if en error occurs the response is <code>null</code>
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Document parse( Reader reader )
	throws IOException, SAXException {
		javax.xml.parsers.DocumentBuilder parser = null;
		Document doc = null;
		try {
			DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
			fac.setNamespaceAware( true );
			fac.setValidating( false );
			fac.setIgnoringElementContentWhitespace( false );
			fac.setValidating( false );
			parser = fac.newDocumentBuilder();
			doc = parser.parse( new InputSource( reader ) );
		} catch ( ParserConfigurationException ex ) {
			throw new IOException( "Unable to initialize DocumentBuilder: " + ex.getMessage() );
		} catch ( Exception e ) {
			throw new SAXException( e.getMessage() );
		} finally {
			reader.close();
		}
		return doc;
	}

	/**
	 * Parses an XML document and returns a DOM object.
	 * 
	 * @param is
	 *            accessing the resource to parse
	 * @return a DOM object
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Document parse( InputStream is )
	throws IOException, SAXException {
		return parse( new InputStreamReader( is ) );

	}

	/**
	 * Copies one node to another node.
	 * 
	 * @param source
	 * @param dest
	 * @return the copied node
	 */
	public static Node copyNode( Node source, Node dest ) {
		if ( source.getNodeType() == Node.TEXT_NODE ) {
			Text tn = dest.getOwnerDocument().createTextNode( getStringValue( source ) );
			return tn;
		}
		NamedNodeMap attr = source.getAttributes();
		if ( attr != null ) {
			for ( int i = 0; i < attr.getLength(); i++ ) {
				( (Element) dest ).setAttribute( attr.item( i ).getNodeName(), attr.item( i ).getNodeValue() );
			}
		}
		NodeList list = source.getChildNodes();
		for ( int i = 0; i < list.getLength(); i++ ) {
			if ( !( list.item( i ) instanceof Text ) ) {
				if ( !( list.item( i ) instanceof Comment ) ) {
					Element en = dest.getOwnerDocument().createElementNS( list.item( i ).getNamespaceURI(),
							list.item( i ).getNodeName() );
					if ( list.item( i ).getNodeValue() != null ) {
						en.setNodeValue( list.item( i ).getNodeValue() );
					}
					Node n = copyNode( list.item( i ), en );
					dest.appendChild( n );
				}
			} else if ( ( list.item( i ) instanceof CDATASection ) ) {
				CDATASection cd = dest.getOwnerDocument().createCDATASection( list.item( i ).getNodeValue() );
				dest.appendChild( cd );
			} else {
				Text tn = dest.getOwnerDocument().createTextNode( list.item( i ).getNodeValue() );
				dest.appendChild( tn );
			}
		}
		return dest;
	}

	/**
	 * Appends a node to an element.
	 * <p>
	 * The node can be from the same document or a different one (it is automatically imported, if
	 * necessary).
	 * 
	 * @param source
	 * @param dest
	 * @return the element that is appended to
	 */
	public static Node insertNodeInto( Node source, Node dest ) {
		Document dDoc = null;
		Document sDoc = source.getOwnerDocument();
		if ( dest instanceof Document ) {
			dDoc = (Document) dest;
		} else {
			dDoc = dest.getOwnerDocument();
		}
		if ( dDoc.equals( sDoc ) ) {
			dest.appendChild( source );
		} else {
			Element element = dDoc.createElementNS( source.getNamespaceURI(), source.getNodeName() );
			dest.appendChild( element );
			// FIXME why not use Document.import() here? copyNode seems broken...
			copyNode( source, element );
		}
		return dest;
	}

	/**
	 * Returns the first child element of the submitted node.
	 * 
	 * @param node
	 * @return the first child element of the submitted node.
	 */
	public static Element getFirstChildElement( Node node ) {
		NodeList nl = node.getChildNodes();
		Element element = null;
		if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
			for ( int i = 0; i < nl.getLength(); i++ ) {
				if ( nl.item( i ) instanceof Element ) {
					element = (Element) nl.item( i );
					break;
				}
			}
		}
		return element;
	}

	/**
	 * Returns the first child element of the submitted node that matches the given
	 *             local name.
	 * 
	 * @param node
	 * @param name
	 * @return the child element
	 */
	public static Element getChildElement( Node node, String name ) {
		NodeList nl = node.getChildNodes();
		Element element = null;
		Element childElement = null;
		if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
			for ( int i = 0; i < nl.getLength(); i++ ) {
				if ( nl.item( i ) instanceof Element ) {
					element = (Element) nl.item( i );

					if ( element.getNodeName().equals( name ) ) {
						childElement = element;

						break;
					}
				}
			}
		}
		return childElement;
	}

	/**
	 * sets the value of an existing node
	 * 
	 * @param target
	 * @param nodeValue
	 */
	public static void setNodeValue( Element target, String nodeValue ) {
		NodeList nl = target.getChildNodes();
		for ( int i = 0; i < nl.getLength(); i++ ) {
			target.removeChild( nl.item( i ) );
		}
		Text text = target.getOwnerDocument().createTextNode( nodeValue );
		target.appendChild( text );
	}


	   public static URI getNamespaceForPrefix( String prefix, Node node )
    throws URISyntaxException {
    	if ( node == null ) {
    		return null;
    	}
    	if ( node.getNodeType() == Node.ELEMENT_NODE ) {
    		NamedNodeMap nnm = node.getAttributes();
    		if ( nnm != null ) {
    			System.out.println ( "(searching namespace for prefix (" + prefix
    					+ "), resulted in a namedNodeMap for the currentNode: " + node.getNodeName() );
    			for ( int i = 0; i < nnm.getLength(); i++ ) {
    				Attr a = (Attr) nnm.item( i );
    				System.out.println  ( "\t(searching namespace for prefix (" + prefix + "), resulted in an attribute: "
    						+ a.getName() );

    				if ( a.getName().startsWith( "xmlns:" ) && a.getName().endsWith( ':' + prefix ) ) {
    					return new URI( a.getValue() );
    				} else if ( prefix == null && a.getName().equals( "xmlns" ) ) {
    					return new URI( a.getValue() );
    				}
    			}
    		}
    	} else if ( node.getNodeType() == Node.ATTRIBUTE_NODE ) {
    		return getNamespaceForPrefix( prefix, ( (Attr) node ).getOwnerElement() );
    	}
    	return getNamespaceForPrefix( prefix, node.getParentNode() );
    }

  
    public static ElementList getChildElements( Node node ) {
        NodeList children = node.getChildNodes();
        ElementList list = new ElementList();
        for ( int i = 0; i < children.getLength(); i++ ) {
            if ( children.item( i ).getNodeType() == Node.ELEMENT_NODE ) {
                list.elements.add( (Element) children.item( i ) );
            }
        }
        return list;
    }
 

public static Element getChildElement( String name, URI namespaceURI, Node node ) {

    String namespace = namespaceURI == null ? null : namespaceURI.toString();

    NodeList nl = node.getChildNodes();
    Element element = null;
    Element childElement = null;

    if ( ( nl != null ) && ( nl.getLength() > 0 ) ) {
        for ( int i = 0; i < nl.getLength(); i++ ) {
            if ( nl.item( i ) instanceof Element ) {
                element = (Element) nl.item( i );
                String s = element.getNamespaceURI();
                if ( ( s == null && namespace == null ) || ( namespace != null && namespace.equals( s ) ) ) {
                    if ( element.getLocalName().equals( name ) ) {
                        childElement = element;
                        break;
                    }
                }
            }
        }
    }
    return childElement;
}

}