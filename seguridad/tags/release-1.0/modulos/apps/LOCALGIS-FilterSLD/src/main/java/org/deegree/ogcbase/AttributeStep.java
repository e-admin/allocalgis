//$HeadURL: https://svn.wald.intevation.org/svn/deegree/base/trunk/src/org/deegree/ogcbase/AttributeStep.java $
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
package org.deegree.ogcbase;

import org.deegree.datatypes.QualifiedName;

/**
 * {@link PropertyPathStep} implementation that selects an attribute's value (using the attribute
 * name as property name).
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.1 $, $Date: 2011/09/19 13:47:32 $
 * 
 * @see PropertyPathStep
 */
public class AttributeStep extends PropertyPathStep {

    /**
     * Creates a new instance of <code>AttributeStep</code> that selects the attribute with the
     * given name.
     * 
     * @param attrName
     */
    AttributeStep( QualifiedName attrName ) {
        super( attrName );
    }

    
    public int hashCode() {
        return this.propertyName.hashCode();
    }

   
    public boolean equals( Object obj ) {
        if ( !( obj instanceof AttributeStep ) ) {
            return false;
        }
        AttributeStep that = (AttributeStep) obj;
        return this.propertyName.equals( that.propertyName );
    }

    
    public String toString() {
        return '@' + this.propertyName.getPrefixedName();
    }
}
