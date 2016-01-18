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

import org.deegree.services.wfs.filterencoding.*;
import org.deegree.services.wfs.protocol.*;


/**
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
* @author Andreas Poth <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
* @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
*/
class WFSLock_Impl implements WFSLock {
    private ArrayList featureIds = null;
    private Filter filter = null;
    private String handle = null;
    private String lockAction = null;
    private String typeName = null;

    /**
    * default constructor
    */
    WFSLock_Impl() {
        featureIds = new ArrayList();
    }

    /**
    * constructor initializing the class with the <WFSLock>
    */
    WFSLock_Impl(String lockAction, String typeName, String handle, Filter filter, 
                 String[] featureIds) {
        this();
        setLockAction(lockAction);
        setTypeName(typeName);
        setHandle(handle);
        setFilter(filter);
        setFeatureIds(featureIds);
    }

    /**
    * Specify how the lock should be acquired. ALL indicates to try to 
    * get all feature locks otherwise fail. SOME indicates to try to get 
    * as many feature locks as possible. The default LOCKACTION is ALL.
    */
    public String getLockAction() {
        return lockAction;
    }

    /**
    * sets <LockAction>
    */
    public void setLockAction(String lockAction) {
        this.lockAction = lockAction;
    }

    /**
    * If a filter is not specified, then the optional typeName attribute 
    * can be used to specify that all feature instances of a particular 
    * feature type should be locked.
    */
    public String getTypeName() {
        return typeName;
    }

    /**
    * sets <TypeName>
    */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * The handle attribute is included to allow a server to associate 
     * any text to the response. The purpose of the handle  attribute 
     * is to provide an error handling mechanism for locating 
     * a statement that might fail. Or to identify an InsertResult.
     */
    public String getHandle() {
        return handle;
    }

    /**
    * sets the <Handle>
    */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * A filter specification describes a set of features to operate upon. 
     * The format of the filter is defined in the OGC Filter Encoding 
     * Specification. Optional. No default. Prerequisite: TYPENAME    
     */
    public Filter getFilter() {
        return filter;
    }

    /**
    * sets the <Filter>
    */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * A list of feature identifiers upon which the specified operation 
     * shall be applied. Optional. No default.
     */
    public String[] getFeatureIds() {
        return (String[]) featureIds.toArray(new String[featureIds.size()]);
    }

    /**
    * adds the <FeatureIds>
    */
    public void addFeatureIds(String featureIds) {
        this.featureIds.add(featureIds);
    }

    /**
    * sets the <FeatureIds>
    */
    public void setFeatureIds(String[] featureIds) {
        this.featureIds.clear();

        if (featureIds != null) {
            for (int i = 0; i < featureIds.length; i++) {
                this.featureIds.add(featureIds[i]);
            }
        }
    }
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSLock_Impl.java,v $
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
 * Revision 1.2  2003/04/07 07:26:56  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:25  poth
 * no message
 *
 * Revision 1.5  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.4  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.3  2002/04/26 09:05:36  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
