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

import java.util.ArrayList;

import org.deegree.services.wfs.protocol.WFSOperation;
import org.deegree.services.wfs.protocol.WFSTransactionRequest;


/**
 * The Transaction interface is used to describe data transformation
 * operations that are to be applied to web accessible features. The web
 * feature server receives a transaction request and either processes
 * it directly or possibly translates it into the language of a target
 * datastore and then has the datastore execute the transaction. When the
 * transaction has been completed, the web feature server will generate an
 * XML response document indicating the termination status of the transaction.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author Andreas Poth <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class WFSTransactionRequest_Impl extends WFSBasicRequest_Impl implements WFSTransactionRequest {
    private ArrayList operations = null;
    private String handle = null;
    private String lockId = null;
    private String releaseAction = null;
    private String version = null;

    /**
     * constructor initializing the class with the <WFSTransactionRequest>
     */
    WFSTransactionRequest_Impl(String version, String id, String lockId, WFSOperation[] operations, 
                               String handle, String releaseAction) {
        super("Transaction", version, id, null, null);
        this.operations = new ArrayList();
        setLockId(lockId);
        setOperations(operations);
        setHandle(handle);
        setReleaseAction(releaseAction);
        setVersion(version);
    }

    /**
     * A <Transaction> element is used to define a single transaction
     * composed of zero or more <Insert>, <Update>, or <Delete> elements.
     * An empty <Transaction> request is valid but not very useful.
     * <p>
     * An operation is meant to be applied to a single feature type, but
     * multiple operations on multiple feature types can be packaged within
     * a single Transaction request.
     */
    public WFSOperation[] getOperations() {
        return (WFSOperation[]) operations.toArray(new WFSOperation[operations.size()]);
    }

    /**
     * adds the <Operations>
     */
    public void addOperations(WFSOperation Operations) {
        this.operations.add(operations);
    }

    /**
     * sets the <Operations>
     */
    public void setOperations(WFSOperation[] operations) {
        this.operations.clear();

        if (operations != null) {
            for (int i = 0; i < operations.length; i++) {
                this.operations.add(operations[i]);
            }
        }
    }

    /**
     * A value of ALL indicates that all feature locks should be
     * released when a transaction terminates. A value of SOME indicates
     * that only those records that are modified should be released.
     * The remaining locks are maintained. The default RELEASEACTION is ALL.
     */
    public String getReleaseAction() {
        return releaseAction;
    }

    /**
     * sets the <ReleaseAction>
     */
    public void setReleaseAction(String releaseAction) {
        this.releaseAction = releaseAction;
    }

    /**
     * returns the id that handles the locking associated to the request
     */
    public String getLockId() {
        return lockId;
    }

    /**
     * returns the id that handles the locking associated to the request
     */
    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    /**
     * returns a descriptor that enables easy identifying the transaction
     */
    public String getHandle() {
        return handle;
    }

    /**
     * sets a descriptor that enables easy identifying the transaction
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * returns the version of the WFS
     */
    public String getVersion() {
        return version;
    }

    /**
     * sets the version of the WFS
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = this.getClass().getName();
        ret += ("releaseAction: " + releaseAction + "\n");
        ret += ("lockId: " + lockId + "\n");
        ret += ("handle: " + handle + "\n");
        ret += ("version: " + version + "\n");
        ret += "operations: \n";
        for (int i = 0; i < operations.size(); i++) {
            ret += (" -  " + operations.get(i) +"\n" );
        }
        return ret;
    }
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSTransactionRequest_Impl.java,v $
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
 * Revision 1.4  2004/01/26 08:10:37  poth
 * no message
 *
 * Revision 1.3  2003/11/26 17:05:37  poth
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:57  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:24  poth
 * no message
 *
 * Revision 1.7  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.6  2002/07/10 14:18:26  ap
 * no message
 *
 * Revision 1.5  2002/07/09 15:10:11  ap
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
