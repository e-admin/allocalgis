/**
 * PropertyName.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
//$HeadURL: https://svn.wald.intevation.org/svn/deegree/base/trunk/src/org/deegree/model/filterencoding/PropertyName.java $
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
 Aennchenstra√üe 19
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
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.ogcbase.OGCDocument;
import org.deegree.ogcbase.PropertyPath;
import org.deegree.ogcbase.PropertyPathFactory;
import org.deegree.ogcbase.PropertyPathResolvingException;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Encapsulates the information of a PropertyName element.
 * 
 * @author Markus Schneider
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.1 $, $Date: 2011/09/19 13:47:32 $
 */
public class PropertyName extends Expression {

    /** the PropertyName's value (as an XPATH expression). */
    private PropertyPath propertyPath;

    /**
     * Creates a new instance of <code>PropertyName</code>.
     * 
     * @deprecated use #PropertyName(QualifiedName) instead
     */
    public PropertyName( String value ) {
        this( new QualifiedName( value ) );
    }

    /**
     * Creates a new instance of <code>PropertyName</code>.
     * 
     * @param elementName
     */
    public PropertyName( QualifiedName elementName ) {
        this( PropertyPathFactory.createPropertyPath( elementName ) );
    }

    /**
     * Creates a new instance of <code>PropertyName</code>.
     * 
     * @param value
     */
    public PropertyName( PropertyPath value ) {
        id = ExpressionDefines.PROPERTYNAME;
        setValue( value );
    }

    /**
     * Given a DOM-fragment, a corresponding Expression-object is built.
     * 
     * @throws FilterConstructionException
     *             if the structure of the DOM-fragment is invalid
     */
    public static Expression buildFromDOM( Element element )
                            throws FilterConstructionException {
        // check if root element's name equals 'PropertyName'
        if ( !element.getLocalName().toLowerCase().equals( "propertyname" ) ) {
            throw new FilterConstructionException( "Name of element does not equal "
                                                   + "'PropertyName'!" );
        }
        PropertyPath propertyPath;
        try {
            Text node = (Text) element.getFirstChild();
        	
        	//TODO sacar el valor del Nodo de texto para poder parsearlo abajo.
            
            //Text node = (Text) XMLTools.getRequiredNode( element, "text()",
                                                      //   CommonNamespaces.getNamespaceContext() );
            propertyPath = OGCDocument.parsePropertyPath(node);
        } catch ( XMLParsingException e ) {
            throw new FilterConstructionException( e.getMessage() );
        }
        return new PropertyName( propertyPath );
    }

    /**
     * Returns the PropertyName's value.
     */
    public PropertyPath getValue() {
        return this.propertyPath;
    }

    /**
     * @see org.deegree.model.filterencoding.PropertyName#getValue()
     */
    public void setValue( PropertyPath value ) {
        this.propertyPath = value;
    }

    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML() {
    	return new StringBuffer("not implementado que eh muuu chungo");
    }
    
    /*
    public StringBuffer toXML() {
        StringBuffer sb = new StringBuffer( 200 );
        sb.append( "<ogc:PropertyName" );

        // TODO use methods from XMLTools
        Map namespaceMap = this.propertyPath.getNamespaceContext().getNamespaceMap();
        Iterator prefixIter = namespaceMap.keySet().iterator();
        while ( prefixIter.hasNext() ) {
            String prefix = (String) prefixIter.next();
            if ( !CommonNamespaces.XMLNS_PREFIX.equals( prefix ) ) {
                URI namespace = (URI) namespaceMap.get( prefix );
                sb.append( " xmlns:" );
                sb.append( prefix );
                sb.append( "=" );
                sb.append( "\"" );
                sb.append( namespace );
                sb.append( "\"" );
            }
        }
        sb.append( ">" ).append( propertyPath ).append( "</ogc:PropertyName>" );
        return sb;
    }*/

    /**
     * Returns the <tt>PropertyName</tt>'s value (to be used in the evaluation of a complexer
     * <tt>Expression</tt>). If the value is a geometry, an instance of <tt>Geometry</tt> is
     * returned, if it appears to be numerical, a <tt>Double</tt>, else a <tt>String</tt>.
     * <p>
     * TODO: Improve datatype handling.
     * <p>
     * 
     * @param feature
     *            that determines the value of this <tt>PropertyName</tt>
     * @return the resulting value
     * @throws FilterEvaluationException
     *             if the <Feature>has no <tt>Property</tt> with a matching name
     */
    public Object evaluate( Feature feature )
                            throws FilterEvaluationException {

        FeatureProperty property = null;
        try {
            property = feature.getDefaultProperty( this.propertyPath );
        } catch ( PropertyPathResolvingException e ) {
            e.printStackTrace();
            throw new FilterEvaluationException( e.getMessage() );
        }
        
        if ( property == null){
         //    && ft.getProperty( this.propertyPath.getStep( 0 ).getPropertyName() ) == null ) {
            throw new FilterEvaluationNoPropertyException( "Feature '" + feature.getId() 
                                                 + "' has no property identified by '"
                                                 + propertyPath + "'!");
        }

        if ( property == null || property.getValue() == null ) {
            return null;
        }
        Object object = property.getValue();
        // TODO if ( object instanceof Number || object instanceof Geometry ) {
        if ( object instanceof Number  ) {
            return object;
        }
        return object.toString();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * @return <code>true</code> if this object is the same as the obj argument;
     *         <code>false</code> otherwise
     */
 
    public boolean equals( Object other ) {
        if ( other == null || !( other instanceof PropertyName ) ) {
            return false;
        }
        return propertyPath.equals( ( (PropertyName) other ).getValue() );
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return a string representation of the object
     */
    
    public String toString () {
        return this.propertyPath.getAsString();
    }
}
