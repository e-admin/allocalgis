/**
 * IndexStep.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
//$HeadURL: https://svn.wald.intevation.org/svn/deegree/base/trunk/src/org/deegree/ogcbase/IndexStep.java $
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
package org.deegree.ogcbase;

import org.deegree.datatypes.QualifiedName;

/**
 * {@link PropertyPathStep} implementation that selects a specified occurence of an element (using
 * the element name as property name).
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.1 $, $Date: 2011/09/19 13:47:32 $
 * 
 * @see PropertyPathStep
 */
public class IndexStep extends PropertyPathStep {

    private int selectedIndex;

    /**
     * Creates a new instance of <code>IndexStep</code> that selects the specified occurence of
     * the element with the given name.
     * 
     * @param elementName
     * @param selectedIndex
     */
    IndexStep( QualifiedName elementName, int selectedIndex ) {
        super( elementName );
        this.selectedIndex = selectedIndex;
    }

    /**
     * Returns the index of the selected element.
     * 
     * @return the index of the selected element
     */
    public int getSelectedIndex() {
        return this.selectedIndex;
    }

 
    public int hashCode() {
        return this.selectedIndex + this.propertyName.hashCode();
    }
 
    public boolean equals( Object obj ) {
        if ( !( obj instanceof IndexStep ) ) {
            return false;
        }
        IndexStep that = (IndexStep) obj;
        return this.getSelectedIndex() == that.getSelectedIndex() && this.propertyName.equals( that.propertyName );
    }

  
    public String toString() {
        return this.propertyName.getPrefixedName() + "[" + this.selectedIndex + "]";
    }
}
