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
package org.deegree_impl.services;

import java.util.EventObject;

import org.deegree.services.OGCWebService;
import org.deegree.services.OGCWebServiceClient;
import org.deegree.services.OGCWebServiceEvent;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;


/**
 * This is the defining interface for event objects that contains a request,
 * a response that should be made available for a service.<p></p>
 * the kind of contained imformation can be determined by calling the
 * <tt>getType</tt> method.
 * <p>--------------------------------------------------------------------</p>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version 2002-04-16
 */
final public class OGCWebServiceEvent_Impl extends EventObject implements OGCWebServiceEvent {
    private OGCWebServiceClient client = null;
    private OGCWebServiceRequest request = null;
    private OGCWebServiceResponse response = null;
    private String id = null;
    private int type = -1;

    /**
     * Creates a new OGCWebServiceEvent_Impl object.
     *
     * @param source 
     * @param request 
     * @param message 
     */
    public OGCWebServiceEvent_Impl( Object source, OGCWebServiceRequest request, String message ) {
        super( source );
        this.request = request;
        id = request.getId();
        type = REQUEST;
    }

    /**
     * Creates a new OGCWebServiceEvent_Impl object.
     *
     * @param source 
     * @param request 
     * @param message 
     * @param client 
     */
    public OGCWebServiceEvent_Impl( Object source, OGCWebServiceRequest request, String message, 
                                    OGCWebServiceClient client ) {
        super( source );
        this.request = request;
        id = request.getId();
        this.client = client;
        type = REQUEST;
    }

    /**
     * Creates a new OGCWebServiceEvent_Impl object.
     *
     * @param source 
     * @param response 
     * @param message 
     */
    public OGCWebServiceEvent_Impl( Object source, OGCWebServiceResponse response, String message ) {
        super( source );
        this.id = response.getRequest().getId();
        this.response = response;
        type = RESPONSE;
    }

    /**
     * returns the id of the of the request which performance caused the
     * event.
     */
    public String getId() {
        return id;
    }

    /**
     * returns the type of event. possible values are:
     * <ul>
     * <li>REQUSET
     * <li>RESPONSE
     * <li>MESSAGE
     * <li>EXCEPTION
     * </ul>
     * An EXCEPTION will allways be a response to a request or a message.
     */
    public int getType() {
        return type;
    }

    /**
     * if the event is a REQUEST type the method returns the request transported
     * by the event. otherwise <tt>null</tt> will be returned.
     */
    public OGCWebServiceRequest getRequest() {
        return request;
    }

    /**
     * if the event is a RESPONSE type the method returns the response transported
     * by the event. otherwise <tt>null</tt> will be returned.
     */
    public OGCWebServiceResponse getResponse() {
        return response;
    }

    /**
     * returns the instance of the <tt>OGCWebService</tt> that is the source of
     * the event.
     */
    public OGCWebService getEventSource() {
        return (OGCWebService)super.getSource();
    }

    /**
     * returns the client where to write the result/response or an
     * error message to
     */
    public OGCWebServiceClient getDestination() {
        return client;
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = null;
        ret = getClass().getName() + ":\n";
        ret += ( "request = " + request + "\n" );
        ret += ( "response = " + response + "\n" );
        ret += ( "client = " + client + "\n" );
        ret += ( "type = " + type + "\n" );
        return ret;
    }
}