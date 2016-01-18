// $HeadURL:
// /deegreerepository/deegree/src/org/deegree/model/filterencoding/capabilities/FilterCapabilities110Factory.java,v
// 1.1 2005/03/04 16:33:28 mschneider Exp $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2007 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
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
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de
 
 ---------------------------------------------------------------------------*/
package org.deegree.model.filterencoding.capabilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.deegree.datatypes.QualifiedName;
//import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcwebservices.getcapabilities.UnknownOperatorNameException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * 
 * @version $Revision: 1.1 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: satec $
 * 
 * @version 1.0. $Revision: 1.1 $, $Date: 2011/09/19 13:47:32 $
 * 
 * @since 2.0
 */
public class FilterCapabilities110Fragment   {

    /**
     * Creates a new <code>FilterCapabilities110Fragment</code> from the given parameters.
     * 
     * @param url
     * @throws URISyntaxException
     * @throws SAXException
     * @throws IOException
     */
	URL url;
	Element element;
	private URL systemId;
    public FilterCapabilities110Fragment( URL url ) throws IOException, SAXException {
        this.url =  url ;
    }

    /**
     * Creates a new <code>FilterCapabilities110Fragment</code> from the given parameters.
     * 
     * @param element
     * @param systemId
     */
    public FilterCapabilities110Fragment( Element element, URL systemId ) {
        this.element = element;
        this.systemId = systemId;
    }

    /**
     * Returns the object representation for the <code>ogc:Filter_Capabilities</code> root
     * element.
     * 
     * @return object representation for the given <code>ogc:Filter_Capabilities</code> element
     * @throws XMLParsingException
     */
    public FilterCapabilities parseFilterCapabilities()
                            throws XMLParsingException {
        return new FilterCapabilities( parseScalarCapabilities(),
                                       null,
                                       parseIdCapabilities( ));
//                                    		   (Element) XMLTools.getRequiredNode( getRootElement(),
//                                                                                                "ogc:Id_Capabilities",
//                                                                                                nsContext ) ) );
    }

    /**
     * Returns the object representation for an <code>ogc:Spatial_Capabilities</code> element that
     * is compliant to the <code>OpenGIS Filter Encoding Specification 1.1.0</code>.
     * 
     * @return object representation for the given <code>ogc:Spatial_Capabilities</code> element
     * @throws XMLParsingException
     */
  /*
    private SpatialCapabilities parseSpatialCapabilities( Element spatialElement )
                            throws XMLParsingException {

        List geometryOperandList = XMLTools.getNodes( spatialElement,
                                                      "ogc:GeometryOperands/ogc:GeometryOperand/text()", nsContext );
        QualifiedName[] geometryOperands = new QualifiedName[geometryOperandList.size()];
        for ( int i = 0; i < geometryOperands.length; i++ ) {
            geometryOperands[i] = XMLFragment.parseQualifiedName( (Node) geometryOperandList.get( i ) );
        }
        String[] operatorNames = XMLTools.getNodesAsStrings( spatialElement,
                                                             "ogc:SpatialOperators/ogc:SpatialOperator/@name",
                                                             nsContext );
        SpatialOperator[] spatialOperators = new SpatialOperator[operatorNames.length];
        for ( int i = 0; i < operatorNames.length; i++ ) {
            try {
                spatialOperators[i] = OperatorFactory110.createSpatialOperator( operatorNames[i] );
            } catch ( UnknownOperatorNameException e ) {
                throw new XMLParsingException( "Invalid operator name '" + operatorNames[i]
                                               + "' for spatial operator: " + e.getMessage(), e );
            }
        }
        return new SpatialCapabilities( spatialOperators, geometryOperands );
    }*/

    /**
     * Returns the object representation for an <code>ogc:Scalar_Capabilities</code> element that
     * is compliant to the <code>OpenGIS Filter Encoding Specification 1.1.0</code>.
     * 
     * @return object representation for the given <code>ogc:Scalar_Capabilities</code> element
     * @throws XMLParsingException
     */
    private ScalarCapabilities parseScalarCapabilities(   )
                            throws XMLParsingException {

                                                                                                               
        Operator[] comparisonOperators = null;
         

         
                                                              //   "ogc:ComparisonOperator/text()", nsContext );*/
         String[] operatorNames = {OperatorFactory110.LESS_THAN,
        		 OperatorFactory110.GREATER_THAN,
        		 OperatorFactory110.	 LESS_THAN_EQUAL_TO, 
        		 OperatorFactory110.	 EQUAL_TO,
        		 OperatorFactory110.	 LESS_THAN_EQUAL_TO,
        		 OperatorFactory110.	 GREATER_THAN_EQUAL_TO,
        		 OperatorFactory110.	 EQUAL_TO,
        		 OperatorFactory110.	 NOT_EQUAL_TO,
        		 OperatorFactory110.	 LIKE,
        		 OperatorFactory110.	 BETWEEN,
        		 OperatorFactory110.	 NULL_CHECK};	
       
            
            comparisonOperators = new Operator[operatorNames.length];
            for ( int i = 0; i < operatorNames.length; i++ ) {
                try {
                    comparisonOperators[i] = OperatorFactory110.createComparisonOperator( operatorNames[i] );
                } catch ( UnknownOperatorNameException e ) {
                    throw new XMLParsingException( "Invalid operator name '" + operatorNames[i]
                                                   + "' for comparison operator: " + e.getMessage(), e );
                }
            }
         
     
            Operator[] arithmeticOperators  ={ new Function("MIN",1),
            					   new Function ("MAX",1),
            					   new Function ("SIN",1),
            					   new Function ("COS",1),
            					   new Function ("TAN",1)};
            	
           
        return new ScalarCapabilities( /*supportsLogicalOperators*/ true, comparisonOperators, arithmeticOperators );
    }

    /**
     * Returns the object representation for an <code>ogc:Id_Capabilities</code> element that is
     * compliant to the <code>OpenGIS Filter Encoding Specification 1.1.0</code>.
     * 
     * @return object representation for the given <code>ogc:Id_Capabilities</code> element
     * @throws XMLParsingException
     */
    private IdCapabilities parseIdCapabilities()
                            throws XMLParsingException {
        //List eidList = XMLTools.getNodes( element, "ogc:EID", nsContext );
        Element[] eidElements = {};
        Element[] fidElements = {};
            
        	/*new Element[eidList.size()];
        for ( int i = 0; i < eidElements.length; i++ ) {
            eidElements[i] = (Element) eidList.get( i );
        }
        List fidList = XMLTools.getNodes( element, "ogc:FID", nsContext );
        Element[] fidElements = new Element[fidList.size()];
        for ( int i = 0; i < fidElements.length; i++ ) {
            fidElements[i] = (Element) fidList.get( i );
        }*/
        return new IdCapabilities( eidElements, fidElements );
    }
  /*  
    private Function parseFunction( Element element )
                            throws XMLParsingException {
        String name = XMLTools.getRequiredNodeAsString( element, "text()", nsContext );
        int nArgs = XMLTools.getRequiredNodeAsInt( element, "@nArgs", nsContext );
        return new Function( name, nArgs );
    }

    private Operator parseSpatialOperator( Element element )
                            throws XMLParsingException {
        String name = XMLTools.getRequiredNodeAsString( element, "@name", nsContext );
        List geometryOperandList = XMLTools.getNodes( element, "GeometryOperand/text()", nsContext );
        QualifiedName[] operands = new QualifiedName[geometryOperandList.size()];
        for ( int i = 0; i < operands.length; i++ ) {
            operands[i] = XMLFragment.parseQualifiedName( (Node) geometryOperandList.get( i ) );
        }
        return new SpatialOperator( name, operands );
    }
*/    
}