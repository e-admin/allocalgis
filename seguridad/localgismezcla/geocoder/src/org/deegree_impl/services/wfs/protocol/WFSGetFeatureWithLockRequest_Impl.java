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
package org.deegree_impl.services.wfs.protocol;

import java.util.HashMap;

import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.protocol.WFSGetFeatureWithLockRequest;
import org.deegree.services.wfs.protocol.WFSNative;
import org.deegree.services.wfs.protocol.WFSQuery;


/**
 *  The class defines the WFSGetFeatureWithLockRequest_Impl
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author Andreas Poth <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class WFSGetFeatureWithLockRequest_Impl extends WFSGetFeatureRequest_Impl
    implements WFSGetFeatureWithLockRequest {
    /**
     * Creates a new WFSGetFeatureWithLockRequest_Impl object.
     *
     * @param version 
     * @param id 
     * @param vendorSpecificParameter 
     * @param native_ 
     * @param outputFormat 
     * @param handle 
     * @param filter 
     * @param maxFeatures 
     * @param startPosition 
     * @param query 
     * @param propertyNames 
     * @param featureIds 
     * @param typeNames 
     */
    WFSGetFeatureWithLockRequest_Impl(String version, String id, HashMap vendorSpecificParameter, 
                                      WFSNative native_, String outputFormat, String handle, 
                                      Filter filter, int maxFeatures, int startPosition, 
                                      WFSQuery[] query, String[] propertyNames, String[] featureIds, 
                                      String[] typeNames) {
        super(version, id, vendorSpecificParameter, native_, outputFormat, handle, filter, 
              maxFeatures, startPosition, query, propertyNames, featureIds, typeNames);
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = this.getClass().getName();
        ret += super.toString();
        return ret;
    }
}