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

import java.util.*;

import org.deegree.services.*;
import org.deegree.services.wfs.protocol.*;

import org.deegree_impl.services.OGCWebServiceResponse_Impl;

import org.w3c.dom.*;


/**
 *  The Interface definies the basic WFS response.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author Andreas Poth <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class WFSBasicResponse_Impl extends OGCWebServiceResponse_Impl implements WFSBasicResponse {
    private ArrayList affectedFeatureTypes = null;

    /**
     * constructor initializing the class with the requests
     */
    WFSBasicResponse_Impl(OGCWebServiceRequest request, Document exception, 
                          String[] affectedFeatureTypes) {
        super(request, exception);
        this.affectedFeatureTypes = new ArrayList();
        setAffectedFeatureTypes(affectedFeatureTypes);
    }

    /**
     * returns a list of feature type names that has been
     * affected by the response.
     */
    public String[] getAffectedFeatureTypes() {
        return (String[]) affectedFeatureTypes.toArray(new String[affectedFeatureTypes.size()]);
    }

    /**
     * adds the <affectedFeatureTypes>
     */
    public void addAffectedFeatureTypes(String affectedFeatureTypes) {
        this.affectedFeatureTypes.add(affectedFeatureTypes);
    }

    /**
     * sets the <affectedFeatureTypes>
     */
    public void setAffectedFeatureTypes(String[] affectedFeatureTypes) {
        this.affectedFeatureTypes.clear();

        if (affectedFeatureTypes != null) {
            for (int i = 0; i < affectedFeatureTypes.length; i++) {
                this.affectedFeatureTypes.add(affectedFeatureTypes[i]);
            }
        }
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = this.getClass().getName() + ":\n";
        ret += ("affectedFeatureTypes: " + affectedFeatureTypes + "\n");
        return ret;
    }
}