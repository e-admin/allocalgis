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

import org.deegree.services.wfs.protocol.*;


/**
 * Web connections are inherently stateless. Unfortunately,
 * this means that the semantics of serializable transactions are
 * not preserved. To understand the issue consider an UPDATE operation.
 * <p>
 * The client fetches a feature instance. The feature is then modified
 * on the client side, and submitted back to the database, via a
 * Transaction request for update. Serializability is lost since there
 * is nothing to guarantee that while the feature was being modified on
 * the client side, another client did not come along and update that
 * same feature in the database.
 * <p>
 * One way to ensure serializability is to require that access to data
 * be done in a mutually exclusive manner; that is while one transaction
 * accesses a data item, no other transaction can modify the same data
 * item. This can be accomplished by using locks that control access to
 * the data.
 * <p>
 * The purpose of the LockFeature interface is to expose a long term feature
 * locking mechanism to ensure consistency. The lock is considered long
 * term because network latency would make feature locks last relatively
 * longer than native commercial database locks.
 * <p>
 * The LockFeature interface is optional and need only be implemented if
 * the underlying datastore supports (or can be made to support) data locking.
 * In addition, the implementation of locking is completely opaque to the client.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class WFSLockFeatureRequest_Impl extends WFSBasicRequest_Impl implements WFSLockFeatureRequest {
    private ArrayList locks = null;
    private int expiry = 0;

    /**
     * constructor initializing the class with the <WFSLockFeatureRequest>
     */
    WFSLockFeatureRequest_Impl(String version, String id, HashMap vendorSpecificParameter, 
                               WFSNative native_, int getExpiry, WFSLock[] locks) {
        super("LockFeatureRequest", version, id, vendorSpecificParameter, native_);
        this.locks = new ArrayList();
        setExpiry(expiry);
        setLocks(locks);
    }

    /**
     * The <Expiry> element is used to set a limit on how long the
     * lock should be held in the event that a transaction is never
     * issued that will release the lock. The expiry limit is specified
     * in minutes. Once the specified number of minutes have elapsed the
     * lock will be released if it still exists. Any further transactions
     * issued against that lock will fail.
     */
    public int getExpiry() {
        return expiry;
    }

    /**
     * sets the <Expiry>
     */
    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    /**
     * <p>A LockFeature request can be used to acquire a lock on a single feature or a set of
     * features defined using the <Filter> element as defined in the OGC Filter Encoding
     * Specification [Ref2].
     * <p>If a filter is not specified, then the optional typeName attribute can be used to specify
     * that all feature instances of a particular feature type should be locked.
     * <p>The optional lockAction attribute is used to control how feature locks are acquired. A
     * lock action of ALL indicates that the web feature server should try to acquire a lock on
     * all requested features; if all features cannot be locked then the operation should fail. If
     * the lock action is set to SOME, then the web feature server tries to lock as many of the
     * requested features as it can. The default lock action shall be ALL.
     */
    public WFSLock[] getLocks() {
        return (WFSLock[]) locks.toArray(new WFSLock[locks.size()]);
    }

    /**
     * adds the <Locks>
     */
    public void addLocks(WFSLock locks) {
        this.locks.add(locks);
    }

    /**
     * sets the <Locks>
     */
    public void setLocks(WFSLock[] locks) {
        this.locks.clear();

        if (locks != null) {
            for (int i = 0; i < locks.length; i++) {
                this.locks.add(locks[i]);
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
        ret += ("expiry: " + expiry + "\n");
        ret += ("locks: " + locks + "\n");
        return ret;
    }
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSLockFeatureRequest_Impl.java,v $
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
 * Revision 1.3  2004/02/09 08:00:22  poth
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:55  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:25  poth
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
 *
 */
