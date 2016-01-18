//$HeadURL: https://svn.wald.intevation.org/svn/deegree/base/trunk/src/org/deegree/model/filterencoding/PropertyIsInstanceOfOperation.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2007 by:
 Department of Geography, University of Bonn
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
 Aennchenstraße 19
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
package org.deegree.model.filterencoding;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.xml.ElementList;
//import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
//import org.deegree.io.datastore.PropertyPathResolvingException;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureProperty;

/*
import org.deegree.model.spatialschema.Curve;
import org.deegree.model.spatialschema.MultiCurve;
import org.deegree.model.spatialschema.MultiPoint;
import org.deegree.model.spatialschema.Point;
import org.deegree.model.spatialschema.Surface;
import org.deegree.model.spatialschema.SurfacePatch;
import org.deegree.ogcbase.CommonNamespaces;
*/
import org.w3c.dom.Element;

/**
 * deegree-specific <code>ComparisonOperation</code> that allows to check the type of a property.
 * <p>
 * This is useful if the property has an abstract type with several concrete implementations, for
 * example 'gml:_Geometry'.
 * <p>
 * NOTE: Currently supported types to test are:
 * <ul>
 * <li>gml:Point</li>
 * <li>gml:_Curve</li>
 * <li>gml:_Surface</li>
 * </ul>
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.1 $, $Date: 2011/09/19 13:47:32 $
 */
public class PropertyIsInstanceOfOperation extends ComparisonOperation {

    private PropertyName propertyName;

    private QualifiedName typeName;

  //  private static NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();

    /**
     * Creates a new instance of <code>PropertyIsInstanceOfOperation</code>.
     * 
     * @param propertyName
     * @param typeName
     */
    public PropertyIsInstanceOfOperation( PropertyName propertyName, QualifiedName typeName ) {
        super( OperationDefines.PROPERTYISINSTANCEOF );
        this.propertyName = propertyName;
        this.typeName = typeName;
    }

    /**
     * Produces an XML representation of this object.
     */
    public StringBuffer toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append( "<ogc:" ).append( getOperatorName() ).append( "\">" );
        sb.append( propertyName.toXML() );
        sb.append( "<ogc:Literal>" ).append( typeName.getPrefixedName() ).append( "</ogc:Literal>" );
        sb.append( "</ogc:" ).append( getOperatorName() ).append( ">" );
        return sb;
    }

    /**
     * Calculates the <code>Operation</code>'s logical value based on the certain property values
     * of the given feature.
     * 
     * @param feature
     *            that determines the values of <code>PropertyNames</code> in the expression
     * @return true, if the <code>Operation</code> evaluates to true, else false
     * @throws FilterEvaluationException
     *             if the evaluation fails
     */
    public boolean evaluate( Feature feature )
                            throws FilterEvaluationException {

        boolean equals = false;
       /* todo
        Object propertyValue = null;
        try {
            FeatureProperty property = feature.getDefaultProperty( propertyName.getValue() );
            propertyValue = property.getValue();
        } catch ( PropertyPathResolvingException e ) {
            String msg = "Error evaluating PropertyIsInstanceOf operation: " + e.getMessage();
            throw new FilterEvaluationException( msg );
        }

        if ( CommonNamespaces.GMLNS.equals( this.typeName.getNamespace() ) ) {
            String localName = this.typeName.getLocalName();
            if ( "Point".equals( localName ) ) {
                equals = propertyValue instanceof Point || propertyValue instanceof MultiPoint;
            } else if ( "_Curve".equals( localName ) ) {
                equals = propertyValue instanceof Curve || propertyValue instanceof MultiCurve;
            } else if ( "_Surface".equals( localName ) ) {
                equals = propertyValue instanceof Surface || propertyValue instanceof SurfacePatch;
            } else {
                String msg = "Error evaluating PropertyIsInstanceOf operation: " + this.typeName
                             + " is not a supported type to check for.";
                throw new FilterEvaluationException( msg );
            }
        } else {
            String msg = "Error evaluating PropertyIsInstanceOf operation: " + this.typeName
                         + " is not a supported type to check for.";
            throw new FilterEvaluationException( msg );
        }
        */
        return equals;
    }

    /**
     * Given a DOM-fragment, a corresponding Operation-object is built. This method recursively
     * calls other buildFromDOM () - methods to validate the structure of the DOM-fragment.
     * 
     * @throws FilterConstructionException
     *             if the structure of the DOM-fragment is invalid
     */
    
    /*
    public static Operation buildFromDOM( Element element )
                            throws FilterConstructionException {

        // check if root element's name equals 'PropertyIsInstanceOf'
        if ( !element.getLocalName().equals( "PropertyIsInstanceOf" ) )
            throw new FilterConstructionException( "Name of element does not equal 'PropertyIsInstanceOf'!" );

        ElementList children = XMLTools.getChildElements( element );
        if ( children.getLength() != 2 ) {
            throw new FilterConstructionException( "'PropertyIsInstanceOf' requires exactly 2 elements!" );
        }

        PropertyName propertyName = (PropertyName) PropertyName.buildFromDOM( children.item( 0 ) );
        QualifiedName typeName = null;
        try {
            typeName = XMLTools.getRequiredNodeAsQualifiedName( element, "ogc:Literal/text()", nsContext );
        } catch ( XMLParsingException e ) {
            throw new FilterConstructionException( e.getMessage() );
        }
        return new PropertyIsInstanceOfOperation( propertyName, typeName );
    }

*/
    /**
     * @return the propertyName of this Operation
     */
    public PropertyName getPropertyName() {
        return propertyName;
    }
}
