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

import org.deegree.xml.Marshallable;
import org.deegree.services.*;
import org.deegree.services.wfs.protocol.*;
import org.deegree_impl.tools.Debug;

import org.w3c.dom.*;


/**
 * In response to a transaction request, the web feature server shall
 * generate an object document indicating the termination status of the
 * transaction. In addition, if the transaction request includes <Insert>
 * operations, then the web feature server must report the feature
 * identifiers of all newly created features. In the event that the
 * transaction fails to execute, a web feature server shall also indicate
 * this in the response.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:33 $
 */
class WFSTransactionResponse_Impl extends WFSBasicResponse_Impl 
                    implements WFSTransactionResponse, Marshallable {
    private ArrayList insertResults = null;
    private String handle = null;
    private String locator = null;
    private String message = null;
    private String status = null;

    /**
     * Creates a new WFSTransactionResponse_Impl object.
     *
     * @param request 
     * @param affectedFeatureTypes 
     * @param exception 
     * @param insertResults 
     * @param status 
     * @param handle 
     */
    WFSTransactionResponse_Impl(OGCWebServiceRequest request, String[] affectedFeatureTypes, 
                                Document exception, WFSInsertResult[] insertResults, String status, 
                                String handle) {
        super(request, exception, affectedFeatureTypes);
        this.insertResults = new ArrayList();
        setInsertResult(insertResults);
        setStatus(status);

        if (exception != null) {
            setMessage(null);
            setLocator(null);
        }

        setHandle(handle);
    }

    /**
     * The <InsertResult> is used to delimit one or more feature
     * identifiers of newly created features. The insert results are
     * reported in the order in which the <Insert> operations were
     * specified in the <Transaction> request. Additionally, they can
     * be correlated using the handle attribute.
     */
    public WFSInsertResult[] getInsertResult() {
        WFSInsertResult[] is = new WFSInsertResult[insertResults.size()];
        return (WFSInsertResult[]) insertResults.toArray(is);
    }

    /**
     * @see getInsertResult
     */
    public void addInserResult(WFSInsertResult insertResult) {
        insertResults.add(insertResult);
    }

    /**
     * @see getInsertResult
     */
    public void setInsertResult(WFSInsertResult[] insertResults) {
        this.insertResults.clear();

        if (insertResults != null) {
            for (int i = 0; i < insertResults.length; i++) {
                addInserResult(insertResults[i]);
            }
        }
    }

    /**
     * A transaction can terminate with a status of:
     * <li>SUCCESS: The transaction was successfully completed.
     * <li>FAILED: One or more operations in the transaction failed.
     * <li>PARTIAL: The transaction partially succeeded and the data
     *               may be in an inconsistent state. For systems that
     *               do not support atomic transactions, this outcome is
     *               a distinct possibility.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @see getStatus
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * In the event that a transaction request fails, the <Locator>
     * can be used to indicate which part of the transaction failed.
     * If the failed operation is labeled using a handle attribute then
     * that can be used to locate the failure. Otherwise, the web feature
     * server may try to identify the failure relative to the beginning
     * of the transaction request possibly using line numbers or some other
     * convenient mechanism.
     */
    public String getLocator() {
        return locator;
    }

    /**
     * @see getLocator
     */
    public void setLocator(String locator) {
        this.locator = locator;
    }

    /**
     * Returns any error messages.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @see getMessage
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * The handle attribute is included to allow a server to associate
     * any text to the response. The purpose of the handle
     * attribute is to provide an error handling mechanism for locating
     * a statement that might fail.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * @see getHandle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = this.getClass().getName() + ":\n";
        ret += ("insertResults: " + insertResults + "\n");
        ret += ("status: " + status + "\n");
        ret += ("message: " + message + "\n");
        ret += ("locator: " + locator + "\n");
        ret += ("handle: " + handle + "\n");
        return ret;
    }
    
    /**
     * returns the XML reprsentation of the wfs transaction result
     */
    public String exportAsXML() {
        Debug.debugMethodBegin();
        
        StringBuffer sb = new StringBuffer(2000);
        sb.append( "<WFS_TransactionResponse xmlns='http://www.opengis.net/wfs' ");
        sb.append( "version='1.0.0'>" );
        for (int i = 0; i < insertResults.size(); i++) {
            sb.append( ((Marshallable)insertResults.get(i)).exportAsXML() );
        }
        sb.append( "<TransactionResult>" );
        sb.append( "<" + status + "/>" );
        if ( locator != null ) {
            sb.append( "<Locator>" ).append( locator ).append( "</Locator>" );
        }
        if ( message != null ) {
            sb.append( "<Message>" ).append( message ).append( "</Message>" );
        }
        sb.append( "</TransactionResult>" );
        sb.append( "</WFS_TransactionResponse>" );
        
        Debug.debugMethodEnd();
        return sb.toString();
    }
    
}

/*
 * Changes to this class. What the people haven been up to:
 *
 * $Log: WFSTransactionResponse_Impl.java,v $
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
 * Revision 1.4  2004/02/09 08:00:22  poth
 * no message
 *
 * Revision 1.3  2003/12/05 09:34:17  poth
 * no message
 *
 * Revision 1.2  2003/04/07 07:26:57  poth
 * no message
 *
 * Revision 1.1.1.1  2002/09/25 16:01:24  poth
 * no message
 *
 * Revision 1.3  2002/08/15 10:01:40  ap
 * no message
 *
 * Revision 1.2  2002/08/09 15:36:30  ap
 * no message
 *
 * Revision 1.1  2002/07/10 14:18:26  ap
 * no message
 *
 * Revision 1.3  2002/04/26 09:02:51  ap
 * no message
 *
 * Revision 1.1  2002/04/04 16:17:15  ap
 * no message
 *
 */
