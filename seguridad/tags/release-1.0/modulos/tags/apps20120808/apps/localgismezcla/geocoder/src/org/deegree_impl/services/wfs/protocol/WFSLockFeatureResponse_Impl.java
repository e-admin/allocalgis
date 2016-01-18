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

import org.w3c.dom.*;


/**
 * In response to a LockFeature request, the web feature server
 * shall generate an XML document containing a lock identifier
 * that a client application can reference when operating upon
 * the locked features. The response can also contain optional blocks
 * depending on the value of the lockAction attribute.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author Andreas Poth <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class WFSLockFeatureResponse_Impl extends WFSBasicResponse_Impl implements WFSLockFeatureResponse {
    private ArrayList featuresLocked = null;
    private ArrayList featuresNotLocked = null;
    private String lockId = null;

    /**
     * constructor initializing the class with the <WFSLockFeatureResponse>
     */
    WFSLockFeatureResponse_Impl(OGCWebServiceRequest request, Document exception, 
                                String[] affectedFeatureTypes, String lockId, 
                                String[] featuresLocked, String[] featuresNotLocked) {
        super(request, exception, affectedFeatureTypes);
        this.featuresLocked = new ArrayList();
        this.featuresNotLocked = new ArrayList();
        setLockId(lockId);
        setFeaturesLocked(featuresLocked);
        setFeaturesNotLocked(featuresNotLocked);
    }

    /**
     * returns the id of the locking action. the may be used to
     * identify the request that locks a feature.
     */
    public String getLockId() {
        return lockId;
    }

    /**
     * sets the id of the locking action. the may be used to
     * identify the request that locks a feature.
     */
    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    /**
     * The method returns the feature identifiers of all the features
     * that were locked by the LockFeature request.
     */
    public String[] getFeaturesLocked() {
        return (String[]) featuresLocked.toArray(new String[featuresLocked.size()]);
    }

    /**
     * adds the <FeaturesLocked>
     */
    public void addFeaturesLocked(String featuresLocked) {
        this.featuresLocked.add(featuresLocked);
    }

    /**
     * sets the <FeaturesLocked>
     */
    public void setFeaturesLocked(String[] featuresLocked) {
        this.featuresLocked.clear();

        if (featuresLocked != null) {
            for (int i = 0; i < featuresLocked.length; i++) {
                this.featuresLocked.add(featuresLocked[i]);
            }
        }
    }

    /**
     * The method returns the feature identifiers of all the features
     * that could not be locked by the LockFeature request. (possibly
     * because they were already locked by someone else).
     */
    public String[] getFeaturesNotLocked() {
        return (String[]) featuresNotLocked.toArray(new String[featuresNotLocked.size()]);
    }

    /**
     * adds the <FeaturesNotLocked>
     */
    public void addFeaturesNotLocked(String featuresNotLocked) {
        this.featuresNotLocked.add(featuresNotLocked);
    }

    /**
     * sets the <FeaturesNotLocked>
     */
    public void setFeaturesNotLocked(String[] featuresNotLocked) {
        this.featuresNotLocked.clear();

        if (featuresNotLocked != null) {
            for (int i = 0; i < featuresNotLocked.length; i++) {
                this.featuresNotLocked.add(featuresNotLocked[i]);
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
        ret += ("lockId: " + lockId + "\n");
        ret += ("featuresLocked: " + featuresLocked + "\n");
        ret += ("featuresNotLocked: " + featuresNotLocked + "\n");
        return ret;
    }
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSLockFeatureResponse_Impl.java,v $
 * Revision 1.1  2009/07/09 07:25:33  miriamperez
 * Rama única LocalGISDOS
 *
 * Revision 1.1  2009/04/13 10:16:50  miriamperez
 * Meto en LocalGISPrivado el módulo del geocoder al completo
 *
 * Revision 1.1  2006/08/31 11:15:30  angeles
 * *** empty log message ***
 *
 * Revision 1.1.1.1  2006/08/17 11:34:01  angeles
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:55  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:25  poth
 * no message
 *
 * Revision 1.6  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.5  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.4  2002/07/04 14:55:07  ap
 * no message
 *
 * Revision 1.3  2002/04/26 09:05:36  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
