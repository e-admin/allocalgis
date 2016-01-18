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

import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;
import org.w3c.dom.Document;


/**
 * This is the base interface for all responses to OGC Web Services (OWS)
 * requests. Each class that capsulates a response within an OWS has to implement
 * this interface.
 * <p>--------------------------------------------------------------------</p>
 * @author <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @version 2002-03-01
 */
public class OGCWebServiceResponse_Impl implements OGCWebServiceResponse{
    
    private OGCWebServiceRequest request = null;
    private Document exception = null;
    
    /**
     * constructor initializing the class with the <OGCWebServiceResponse_Impl>
     */
    public OGCWebServiceResponse_Impl(OGCWebServiceRequest request, Document exception) {
        setRequest(request);
        setException(exception);
    }
    
    /**
     * returns the request that causes the response.
     */
    public OGCWebServiceRequest getRequest() {
        return request;
    }
    
    /**
     * sets the request that causes the response.
     */
    public void setRequest(OGCWebServiceRequest request) {
        this.request = request;
    }
    
    /**
     * returns an XML encoding of the exception that raised. If no exception
     * raised <tt>null</tt> will be returned.
     */
    public Document getException() {
        return exception;
    }
    
    /**
     * sets an XML encoding of the exception that raised. If no exception
     * raised <tt>null</tt> will be returned.
     */
    public void setException(Document exception) {
        this.exception = exception;
    }
    
    
    
    public String toString() {
        String ret = null;
        ret = getClass().getName() + ":\n";
        ret += "request = " + request + "\n";
        ret += "exception = " + exception + "\n";
        return ret;
    }
    
}
