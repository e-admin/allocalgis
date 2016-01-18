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
package org.deegree_impl.services.wfs.configuration;

import java.util.HashMap;

import org.deegree.services.wfs.configuration.MasterTable;


/**
 * Implementation of the description interface for the master table of a 
 * feature type.
 * <p>---------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
public class MasterTable_Impl extends TableDescription_Impl implements MasterTable {
    /**
     * Creates a new MasterTable_Impl object.
     *
     * @param name 
     * @param targetName 
     * @param idField 
     * @param isNumber 
     * @param isAutoIncremented 
     * @param insertAllowed 
     * @param updateAllowed 
     * @param deleteAllowed 
     * @param geoFields 
     * @param references 
     */
    MasterTable_Impl( String name, String targetName, String idField, boolean isNumber, 
                      boolean isAutoIncremented, boolean insertAllowed, boolean updateAllowed, 
                      boolean deleteAllowed, HashMap geoFields, HashMap references ) {
        super( name, targetName, idField, isNumber, isAutoIncremented, insertAllowed, updateAllowed, 
               deleteAllowed, geoFields, references );
    }
}