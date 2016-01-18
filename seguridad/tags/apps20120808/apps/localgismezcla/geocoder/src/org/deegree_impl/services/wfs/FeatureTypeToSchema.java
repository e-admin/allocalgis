
/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact:

Andreas Poth
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.wfs;

import org.deegree.model.feature.FeatureType;
import org.deegree.model.feature.FeatureTypeProperty;
import org.deegree.xml.XMLTools;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



/**
* The class transforms a list of FeatureTypes to their
* corresponding XML-Schema base on the GML-schema definition.
* 
* <p>------------------------------------------------------------------------</p>
* @author Andreas Poth
* @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
* <p>
*/

public class FeatureTypeToSchema {
	
	private Node rootNode = null;
	private Document doc = null;
		
   /**
    * Only public method of the class. It recieves an array of
    * feature types and transform them to their corresponding
    * GML based XML-schema. Depending on the used constructor
    * the returned root-<tt>Node</tt> is a <tt>org.w3c.dom.Document</tt>
    * or a simple <tt>org.w3c.dom.Node</tt>.
    */	
	public Document[] createFeatureTypeSchema(FeatureType[] types) throws Exception
	{		
		
		Document[] docs = new Document[types.length];
	    
	    // create a complex type for each feature type
	    for (int i = 0; i < types.length; i++) {
	    	docs[i] = XMLTools.create();
	    	Element rootNode = docs[i].createElement( "xsd:schema" );
	    	Element schema = createXSDSchema( rootNode );
	    	createXSDGlobalElement( schema, types[i].getName(), "wfs:"+types[i].getName()+"_Type", 
									"gml:_Feature");
	    	// <xsd:complexType>
	    	Element complexType = createXSDComplexType( schema, types[i].getName()+"_Type");
	    	// <xsd:complexContent>
	    	Element complexContent = createXSDComplexContent( complexType );
	    	// <xsd:extension>
	    	Element extension = createXSDExtension( complexContent, "gml:AbstractFeatureType");
	    	// <xsd:sequence>
	    	Element sequence = createXSDSequence( extension );
	    	// create an element tag for each feature type property
	    	FeatureTypeProperty[] ftp = types[i].getProperties();
	    	for (int j = 0; j < ftp.length; j++) {

	    		if ( ftp[j].getType().equals("java.lang.Integer") ) {
	 				createInteger( sequence, ftp[j] ); 			
	    		}
	    	   else	
	    	    if ( ftp[j].getType().equals("java.lang.Float") ) {
	 				createFloat( sequence, ftp[j] ); 			
	    		}
	    	   else	
	    	    if ( ftp[j].getType().equals("java.lang.Double") ||
	    	         ftp[j].getType().equals("java.math.BigDecimal") ) {
	 				createDouble( sequence, ftp[j] ); 			
	    		}
	    	   else	
	    	    if ( ftp[j].getType().equals("java.lang.String") ) {
	 				createString( sequence, ftp[j] ); 			
	    		} 
	    	   else	
	    	    if ( ftp[j].getType().startsWith("java.lang.Object") ) {
	 				createObject( sequence, ftp[j] ); 			
	    		}	
	    	   else	
	    	    if ( ftp[j].getType().startsWith("org.deegree.model.geometry.GM_") ) {
	 				createGeometry( sequence, ftp[j] ); 			
	    		}		

		    }		    
	    	
	    }
	    
	    return docs;
	} 
	
   /**
    * creates an elements for desribing a property with data type = integer
    * and adds it to the submitted parent
    */	
	private void createInteger(Element parent, FeatureTypeProperty ftp)
	{
		Element element = null;
		if ( ftp.isNullable() ) {
			element = createXSDElement( parent, ftp.getName(), null, true, 0, 1);
		} else {
			element = createXSDElement( parent, ftp.getName(), null, false, 1, 1);
		}
		Element simpleType = createXSDSimpleType( element );
		Element restriction = createXSDRestriction( simpleType, "xsd:integer" );
		Element precision = createXSDPrecision( restriction, 10 );
	}
	
   /**
    * creates an elements for desribing a property with data type = float
    * and adds it to the submitted parent
    */	
	private void createFloat(Element parent, FeatureTypeProperty ftp)
	{
		Element element = null;
		if ( ftp.isNullable() ) {
			element = createXSDElement( parent, ftp.getName(), null, true, 0, 1);
		} else {
			element = createXSDElement( parent, ftp.getName(), null, false, 1, 1);
		}
		Element simpleType = createXSDSimpleType( element );
		Element restriction = createXSDRestriction( simpleType, "xsd:float" );
		Element precision = createXSDPrecision( restriction, 10 );
	}
	
   /**
    * creates an elements for desribing a property with data type = double
    * and adds it to the submitted parent
    */	
	private void createDouble(Element parent, FeatureTypeProperty ftp)
	{
		Element element = null;
		if ( ftp.isNullable() ) {
			element = createXSDElement( parent, ftp.getName(), null, true, 0, 1);
		} else {
			element = createXSDElement( parent, ftp.getName(), null, false, 1, 1);
		}
		Element simpleType = createXSDSimpleType( element );
		Element restriction = createXSDRestriction( simpleType, "xsd:double" );
		Element precision = createXSDPrecision( restriction, 10 );
	}
	
   /**
    * creates an elements for desribing a property with data type = string
    * and adds it to the submitted parent
    */	
	private void createString(Element parent, FeatureTypeProperty ftp)
	{
		Element element = null;
		if ( ftp.isNullable() ) {
			element = createXSDElement( parent, ftp.getName(), null, true, 0, 1);
		} else {
			element = createXSDElement( parent, ftp.getName(), null, false, 1, 1);
		}
		Element simpleType = createXSDSimpleType( element );
		Element restriction = createXSDRestriction( simpleType, "xsd:string" );
	}
	
   /**
    * creates an elements for desribing a property with data type = object
    * and adds it to the submitted parent. This method will/should be used
    * if the data type of a property can't be determined exactly.
    */	
	private void createObject(Element parent, FeatureTypeProperty ftp)
	{
		Element element = null;
		if ( ftp.isNullable() ) {
			element = createXSDElement( parent, ftp.getName(), null, true, 0, 1);
		} else {
			element = createXSDElement( parent, ftp.getName(), null, false, 1, 1);
		}
		Element simpleType = createXSDSimpleType( element );
		Element restriction = createXSDRestriction( simpleType, "xsd:object" );
	}	
	
   /**
    * creates an elements for desribing a property with data type = geometry
    * and adds it to the submitted parent.<p>
    * Notice: for it is possible that a feature type contains different geometry
    * types if created from a ESRI shape file no specialized geometry type is
    * used. All geometries shares the "gml:GeometryType".
    */	
	private void createGeometry(Element parent, FeatureTypeProperty ftp)
	{
		Element element = null;
		if ( ftp.isNullable() ) {
			element = createXSDElement( parent, ftp.getName(), "gml:_Geometry", true, 0, 1);
		} else {
			element = createXSDElement( parent, ftp.getName(), "gml:_Geometry", false, 1, 1);
		}		
	}
	
   /**
    * creates the schema tag of the describtion. If the root is a 
    * <tt>org.w3c.dom.Document</tt> returned <tt>Element</tt> will be
    * the root element.
    */	
	private Element createXSDSchema(Node parent)
	{
		Element element = doc.createElement("xsd:schema");
		parent.appendChild( element );
		element.setAttribute( "targetNamespace","http://www.cubewerx.com/wfs");
		element.setAttribute( "xmlns:xsd","http://www.w3.org/2000/10/XMLSchema");
		element.setAttribute( "xmlns:wfs","http://www.cubewerx.com/wfs");
		element.setAttribute( "xmlns:gml","http://www.opengis.net/gml");
		element.setAttribute( "elementFormDefault","qualified");
		element.setAttribute( "version", "0.1");
		return element;
	}
	
   /**
    * creates tags for elements defined global within the schema
    */	
	private Element createXSDGlobalElement(Node parent, String name, String type, 
										  String substitutionGroup)
	{
		Element element = doc.createElement("xsd:element");
		element.setAttribute( "name",name);
		element.setAttribute( "type",type);
		element.setAttribute( "substitutionGroup",substitutionGroup);
		parent.appendChild( element );
		return element;
	}
	
   /**
    * creates a <xsd:restriction> tag. it is used within simple type
    * definitions
    */	
	private Element createXSDRestriction(Node parent, String base)
	{		
		Element element = doc.createElement("xsd:restriction");
		if (base != null) {
	    	element.setAttribute( "base", base );
	    }
		parent.appendChild( element );
		return element;
	}
	
   /**
    * creates a <xsd:precision> tag. it is used to define the
    * precision of numeric types
    */	
	private Element createXSDPrecision(Node parent, int value)
	{
		Element element = doc.createElement("xsd:precision");
	    element.setAttribute( "value", ""+value );
		parent.appendChild( element );
		return element;
	}
	
   /**
    * creates a <xsd:simpleType> tag
    */	
	private Element createXSDSimpleType(Node parent)
	{
		Element element = doc.createElement("xsd:simpleType");
		parent.appendChild( element );
		return element;
	}
	
   /**
    * creates a <xsd:element> tag that contains the definition of one
    * feature type property
    */	
	private Element createXSDElement(Node parent, String name, String type, 
									boolean nullable, int minOccurs, int maxOccurs)					
	{
		Element element = doc.createElement("xsd:element");
		if (name != null) {
	    	element.setAttribute( "name", name );
	    }
	    if (type != null) {
	    	element.setAttribute( "type", type );
	    }
	    if (nullable) {
	    	element.setAttribute( "nullable", "true" );
	    } else {
	    	element.setAttribute( "nullable", "false" );
	    }
	    if (minOccurs >= 0) {
	    	element.setAttribute( "minOccurs", ""+minOccurs );
	    }
	    if (maxOccurs >= 0) {
	    	element.setAttribute( "maxOccurs", ""+maxOccurs );
	    }
		parent.appendChild( element );
		return element;
	}
	
   /**
    * creates a <xsd:sequence> tag that contains a element tag
    * for each property definied for a feature type
    */	
	private Element createXSDSequence(Node parent)
	{
		Element element = doc.createElement("xsd:sequence");
		parent.appendChild( element );
		return element;
	}
	
   /**
    * creates a <xsd:extension> tag 
    */	
	private Element createXSDExtension(Node parent, String base) throws Exception
	{
		Element element = doc.createElement("xsd:extension");
		if (base != null) {
	    	element.setAttribute( "base", base );
	    } else {
	    	throw new Exception("base isn't allowed to be null within a extension tag");
	    }
		parent.appendChild( element );		
		return element;
	}
	
   /**
    * creates a <xsd:complexContent> tag that contains a
    * feature type definition
    */	
	private Element createXSDComplexContent(Node parent)
	{
		Element element = doc.createElement("xsd:complexContent");
		parent.appendChild( element );
		return element;
	}
	
   /**
    * creates a <xsd:complexType> tag that contains a feature
    * type definition (<xsd:complexContent>).
    */	
	private Element createXSDComplexType(Node parent, String name) throws Exception
	{
		Element element = doc.createElement("xsd:complexType");
		if (name != null) {
	    	element.setAttribute( "name", name );
	    } else {
	    	throw new Exception("base isn't allowed to be null within a extension tag");
	    }
		parent.appendChild( element );
		return element;
	}		

}
