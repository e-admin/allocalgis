/**
 * PruebaSld.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import java.io.FileInputStream;
import java.net.URI;

import org.deegree.framework.xml.XMLTools;
import org.deegree.model.filterencoding.AbstractFilter;
import org.deegree.model.filterencoding.Filter;
import org.deegree.model.filterencoding.FilterEvaluationException;
import org.satec.sld.SVG.SVGNodeFeature;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;
import com.tinyline.svg.SVGAttr;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGNode;
import com.tinyline.svg.SVGParser;
public class PruebaSld {

	/**
	 * @param args
	 */

	public static void main1(String[] args) throws Throwable {

		/*	Sax2Parser p2 = new Sax2Parser();
		 	p2.setInputStream( new FileInputStream("parcelaurbana.xml" ) );
			p2.parse();
		 */
		Parser p = new Parser();
		p.setNodeFactory(new DomNodeFactory());
		// Parse the first argument file
		//System.out.println("Parse " + args[0]);

		p.setInputStream( new FileInputStream( "src/test/resources/parcelaurbananull.xml" ) );

		//p.setInputStream( Demo.class.getResourceAsStream( "test.xml" ) );

		p.parse();
		Document d = p.getDocument();
		// Extract the DOM root
		Element root = (Element) d.getRoot();
		/*	
			UserStyle>
			<Name>parcelasurbana:_:TematicoParcelas</Name>
			<Title>parcelasurbana:_:TematicoParcelas</Title>
			<Abstract>parcelasurbana:_:TematicoParcelas</Abstract>
			<FeatureTypeStyle>
				<Rule>
					<Name>regla temática 0</Name>
					<ogc:Filter
		 */
		URI sldNameSpace = new URI("http://www.opengis.net/sld");
		URI ogcNameSpace = new URI("http://www.opengis.net/ogc");

		Element namedLayer = XMLTools.getChildElement("NamedLayer",  sldNameSpace, root);
		Element userStyle = XMLTools.getChildElement("UserStyle",  sldNameSpace, namedLayer);
		Element featureType = XMLTools.getChildElement("FeatureTypeStyle",  sldNameSpace, userStyle);
		Element rule = XMLTools.getChildElement("Rule",  sldNameSpace, featureType);
		Element filterElement = XMLTools.getChildElement("Filter",  ogcNameSpace, rule);
		System.out.println("filter ->" + filterElement.getLocalName());
		Filter filter = null;
		if ( filterElement  != null ) {
			filter = AbstractFilter.buildFromDOM( filterElement  );
		}

		SVGDocument doc = new SVGDocument();
		SVGAttr attrParser = new SVGAttr(100, 100);
		// Create the SVGT stream parser
		SVGParser parser = new SVGParser(attrParser);
		// Parse the input SVGT stream parser into the document
		FileInputStream is = new FileInputStream("src/test/resources/Parcelas_Herrera.svg");
		int errorCode = parser.load(doc, is);
		errorCode = errorCode >> 10;
		if (errorCode != 0) {
			System.out.println("errorcode -> " + errorCode);
		}
		SVGNode rootSVG =  doc.root;
		SVGNode hijo = (SVGNode) rootSVG.children.data[0];

		for (int i =0 ;i < hijo.children.count; i++){
			SVGNode path = (SVGNode) hijo.children.data[i];
			SVGNodeFeature feature = new SVGNodeFeature (path);

			if (filter !=null) {
				try {
					boolean res = filter.evaluate(feature);
					System.out.println("resultado ->" + res + "feature " + feature.getId());
				}
				catch (FilterEvaluationException e) {

					e.printStackTrace();
				}



			}
			else {
				System.out.println("el filtro es null");
			}
		} 




	}


	public static void main(String[] args) throws Throwable {

		/*	Sax2Parser p2 = new Sax2Parser();
			 	p2.setInputStream( new FileInputStream("parcelaurbana.xml" ) );
				p2.parse();
		 */
		System.out.println("empezando");
		Parser p = new Parser();
		p.setNodeFactory(new DomNodeFactory());
		// Parse the first argument file
		//System.out.println("Parse " + args[0]);

		p.setInputStream( new FileInputStream( "src/test/resources/parcelas.sch" ) );

		//p.setInputStream( Demo.class.getResourceAsStream( "test.xml" ) );

		p.parse();
		Document d = p.getDocument();
		// Extract the DOM root
		Element root = (Element) d.getRoot();
		/*	
				UserStyle>
				<Name>parcelasurbana:_:TematicoParcelas</Name>
				<Title>parcelasurbana:_:TematicoParcelas</Title>
				<Abstract>parcelasurbana:_:TematicoParcelas</Abstract>
				<FeatureTypeStyle>
					<Rule>
						<Name>regla temática 0</Name>
						<ogc:Filter
		 */


		// Attributes
		NodeList nodelist = root.getChildNodes();

		for (int i = 0; i < nodelist.getLength(); i++) {
			if ( nodelist.item( i ) instanceof Element ) {
				Element child = (Element) nodelist.item( i );

				String childName = child.getLocalName();
				if ( childName.equals( "attribute" ) ) {
					Element hijo = XMLTools.getRequiredChildElement("name", child);
					System.out.println("hijo ->" + hijo.getNodeValue());
					
					String name = XMLTools.getRequiredStringValue("name", child);
					System.out.println(" name" + name);
					/*
					  = hijo.getNodeValue().getBytes();
					System.out.println(" hex->" + salida);	
					String name = XMLTools.getRequiredStringValue("name", child);
					System.out.println("name ->" + name);
*/
				}
			}
		}


	}
	
	
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
	
	 
	
	public static String toHexString(byte[] block) {
	    StringBuffer buf = new StringBuffer();
	    char[] hexChars = { 
	        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	        'A', 'B', 'C', 'D', 'E', 'F' };
	    int len = block.length;
	    int high = 0;
	    int low = 0;
	    for (int i = 0; i < len; i++) {
	        high = ((block[i] & 0xf0) >> 4);
	        low = (block[i] & 0x0f);
	        buf.append(hexChars[high]);
	        buf.append(hexChars[low]);
	    } 
	    return buf.toString();
	}

}
