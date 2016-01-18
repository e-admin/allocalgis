/**
 * ComparisonOperation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
//$HeadURL: https://svn.wald.intevation.org/svn/deegree/base/trunk/src/org/deegree/model/filterencoding/ComparisonOperation.java $
/*----------------    FILE HEADER  ------------------------------------------
 
 This file is part of deegree.
 Copyright (C) 2001-2006 by:
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
package org.deegree.model.filterencoding;

import org.w3c.dom.Element;

/**
 * Encapsulates the information of a comparison_ops entity (as defined in the Filter DTD).
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.1 $, $Date: 2011/09/19 13:47:32 $
 */
public abstract class ComparisonOperation extends AbstractOperation {

    ComparisonOperation( int operationId ) {
        super( operationId );
    }

    /**
     * Given a DOM-fragment, a corresponding Operation-object is built. This method recursively
     * calls other buildFromDOM () - methods to validate the structure of the DOM-fragment.
     * 
     * @throws FilterConstructionException
     *             if the structure of the DOM-fragment is invalid
     */
    public static Operation buildFromDOM( Element element )
                            throws FilterConstructionException {

        // check if root element's name is a known operator
        String name = element.getLocalName();
        int operatorId = OperationDefines.getIdByName( name );
        ComparisonOperation operation = null;

        switch ( operatorId ) {
        case OperationDefines.PROPERTYISEQUALTO:
        case OperationDefines.PROPERTYISLESSTHAN:
        case OperationDefines.PROPERTYISGREATERTHAN:
        case OperationDefines.PROPERTYISLESSTHANOREQUALTO:
        case OperationDefines.PROPERTYISGREATERTHANOREQUALTO: {
            operation = (ComparisonOperation) PropertyIsCOMPOperation.buildFromDOM( element );
            break;
        }
        case OperationDefines.PROPERTYISLIKE: {
            operation = (ComparisonOperation) PropertyIsLikeOperation.buildFromDOM( element );
            break;
        }
        case OperationDefines.PROPERTYISNULL: {
            operation = (ComparisonOperation) PropertyIsNullOperation.buildFromDOM( element );
            break;
        }
        case OperationDefines.PROPERTYISBETWEEN: {
            operation = (ComparisonOperation) PropertyIsBetweenOperation.buildFromDOM( element );
            break;
        }
        case OperationDefines.PROPERTYISINSTANCEOF: {
            operation = (ComparisonOperation) PropertyIsInstanceOfOperation.buildFromDOM( element );
            break;
        }        
        default: {
            throw new FilterConstructionException( "'" + name + "' is not a comparison operator!" );
        }
        }
        return operation;
    }
}

/***************************************************************************************************
 * <code> Changes to this class. What the people have been up to:
 * 
 * $Log: ComparisonOperation.java,v $
 * Revision 1.1  2011/09/19 13:47:32  satec
 * MODELO EIEL
 *
 * Revision 1.3  2010/05/03 08:41:19  satec
 * *** empty log message ***
 *
 * Revision 1.1  2009/03/31 15:54:49  roger
 * Creación de módulo FIlter SLD que implementa los filtros OGC sobre Features SVG
 *
 * Revision 1.6  2007/01/11 16:27:25  mschneider
 * Added new ComparisonOperation: PropertyIsInstanceOfOperation.
 * Revision 1.5 2006/07/12 14:46:14 poth comment footer added
 * </code>
 **************************************************************************************************/