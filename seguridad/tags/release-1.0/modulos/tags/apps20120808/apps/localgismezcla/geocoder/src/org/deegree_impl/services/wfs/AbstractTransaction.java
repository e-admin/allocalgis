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

import java.util.ArrayList;

import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.wfs.configuration.DatastoreConfiguration;
import org.deegree.services.wfs.protocol.WFSDelete;
import org.deegree.services.wfs.protocol.WFSInsert;
import org.deegree.services.wfs.protocol.WFSOperation;
import org.deegree.services.wfs.protocol.WFSUpdate;


/**
 * class defining the basic processing of a DescribFeatureType request
 * <p>-------------------------------------------------------------------------</p>
 *
 * @author Andreas Poth
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
abstract public class AbstractTransaction extends org.deegree_impl.services.wfs.WFSMainLoop {
    
    protected DatastoreConfiguration config  = null;
    
    public AbstractTransaction(org.deegree_impl.services.wfs.AbstractDataStore parent,
                               OGCWebServiceRequest request) {
        super( parent, request );
    }
    
    /**
     * returns the list of feature types that are affected by a Transaction
     */
    protected String[] getAffectedFeatureTypes(WFSOperation[] operations) {
        ArrayList list = new ArrayList();
        
        for (int i = 0; i < operations.length; i++) {
            
            if ( operations[i] instanceof WFSInsert ) {
                String[] ft = ((WFSInsert)operations[i]).getFeatureTypes();
                for (int j = 0; j < ft.length; j++) {
                    if ( parent.isKnownFeatureType( ft[j] ) ) {
                        list.add( ft[j] );
                    }
                }
            }
            else
                if ( operations[i] instanceof WFSUpdate ) {
                    //((WFSUpdate)operations[i]).
                }
                else
                    if ( operations[i] instanceof WFSDelete ) {
                    }
                    else {
                        // native request
                    }
        }
        
        return (String[])list.toArray( new String[list.size()] );
    }
    
    
}
