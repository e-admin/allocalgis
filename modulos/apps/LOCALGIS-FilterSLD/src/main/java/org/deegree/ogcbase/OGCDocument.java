/**
 * OGCDocument.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
// $HeadURL: https://svn.wald.intevation.org/svn/deegree/base/trunk/src/org/deegree/ogcbase/OGCDocument.java $
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
 53115 Bonn
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
package org.deegree.ogcbase;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.w3c.dom.Text;

   
public class OGCDocument {

    public static PropertyPath parsePropertyPath( Text textNode )
                            throws XMLParsingException {

        String path = XMLTools.getStringValue( textNode );
        String[] steps = StringTools.toArray( path, "/", false );
        List propertyPathSteps = new ArrayList ( steps.length );

        for ( int i = 0; i < steps.length; i++ ) {
            PropertyPathStep propertyStep = null;
            QualifiedName propertyName = null;
            String step = steps[i];
            boolean isAttribute = false;
            boolean isIndexed = false;
            int selectedIndex = -1;

            // check if step begins with '@' -> must be the final step then
            if ( step.startsWith( "@" ) ) {
                if ( i != steps.length - 1 ) {
                    String msg = "PropertyName '" + path + "' is illegal: the attribute specifier may only "
                                 + "be used for the final step.";
                    throw new XMLParsingException( msg );
                }
                step = step.substring( 1 );
                isAttribute = true;
            }

            // check if the step ends with brackets ([...])
            if ( step.endsWith( "]" ) ) {
                if ( isAttribute ) {
                    String msg = "PropertyName '" + path + "' is illegal: if the attribute specifier ('@') is used, "
                                 + "index selection ('[...']) is not possible.";
                    throw new XMLParsingException( msg );
                }
                int bracketPos = step.indexOf( '[' );
                if ( bracketPos < 0 ) {
                    String msg = "PropertyName '" + path + "' is illegal. No opening brackets found for step '" + step
                                 + "'.";
                    throw new XMLParsingException( msg );
                }
                try {
                    selectedIndex = Integer.parseInt( step.substring( bracketPos + 1, step.length() - 1 ) );
                } catch ( NumberFormatException e ) {
                    String msg = "PropertyName '" + path + "' is illegal. Specified index '"
                                 + step.substring( bracketPos + 1, step.length() - 1 ) + "' is not a number.";
                    throw new XMLParsingException( msg );
                }
                step = step.substring( 0, bracketPos );
                isIndexed = true;
            }

            // determine namespace prefix and binding (if any)
            int colonPos = step.indexOf( ':' );
            if ( colonPos < 0 ) {
                propertyName = new QualifiedName( step );
            } else {
                String prefix = step.substring( 0, colonPos );
                step = step.substring( colonPos + 1 );
                URI namespace = null;
                try {
                    namespace = XMLTools.getNamespaceForPrefix( prefix, textNode );
                } catch ( URISyntaxException e ) {
                    throw new XMLParsingException( "Error parsing PropertyName: " + e.getMessage() );
                }
                if ( namespace == null ) {
                    throw new XMLParsingException( "PropertyName '" + path + "' uses an unbound namespace prefix: "
                                                   + prefix );
                }
                propertyName = new QualifiedName( prefix, step, namespace );
            }

            if ( isAttribute ) {
                propertyStep = PropertyPathFactory.createAttributePropertyPathStep( propertyName );
            } else if ( isIndexed ) {
                propertyStep = PropertyPathFactory.createPropertyPathStep( propertyName, selectedIndex );
            } else {
                propertyStep = PropertyPathFactory.createPropertyPathStep( propertyName );
            }
            propertyPathSteps.add( propertyStep );
        }
        return PropertyPathFactory.createPropertyPath( propertyPathSteps );
    }
}
