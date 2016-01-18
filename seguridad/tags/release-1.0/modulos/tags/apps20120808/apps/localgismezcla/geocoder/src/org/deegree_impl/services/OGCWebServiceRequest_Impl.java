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

import java.util.HashMap;

import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.WebServiceException;


/**
 * This is the base interface for all request on OGC Web Services (OWS). Each
 * class that capsulates a request against an OWS has to implements this
 * interface.
 * <p>--------------------------------------------------------------------</p>
 * @author <a href="mailto:k.lupp@web.de>Katharina Lupp</a>
 * @version 2002-02-28
 */
public class OGCWebServiceRequest_Impl implements OGCWebServiceRequest {
    protected HashMap vendorSpecificParameter = null;
    protected String id = null;
    protected String request = null;
    protected String service = null;
    protected String version = null;

    /**
     * constructor initializing the class with the <OGCWebServiceRequest>
     */
    public OGCWebServiceRequest_Impl(String request, String service, String version, String id, 
                                     HashMap vendorSpecificParameter) {        
        setRequest(request);
        setService(service);
        setId(id);
        setVendorSpecificParameter(vendorSpecificParameter);
        setVersion( version );
    }

    /**
     * The REQUEST parameter indicates which service operation is being invoked.
     * The value shall be the name of one of the operations offered by the OGC
     * Web Service Instance.
     */
    public String getRequest() {
        return request;
    }

    /**
     * sets the REQUEST parameter indicating which service operation is being invoked.
     * The value shall be the name of one of the operations offered by the OGC
     * Web Service Instance.
     */
    public void setRequest(String request) {
        this.request = request;
    }

    /**
     * The required SERVICE parameter indicates which of the available service
     * types at a particular service instance is being invoked.
     */
    public String getService() {
        return service;
    }

    /**
     * sets the required SERVICE parameter indicating which of the available service
     * types at a particular service instance is being invoked.
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * Finally, the requests allow for optional vendor-specific parameters (VSPs)
     * that will enhance the results of a request. Typically, these are used for
     * private testing of non-standard functionality prior to possible
     * standardization. A generic client is not required or expected to make use
     * of these VSPs.
     */
    public HashMap getVendorSpecificParameters()  {        
        return vendorSpecificParameter;
    }
        
    /**
     * Finally, the requests allow for optional vendor-specific parameters (VSPs)
     * that will enhance the results of a request. Typically, these are used for
     * private testing of non-standard functionality prior to possible
     * standardization. A generic client is not required or expected to make use
     * of these VSPs.
     */
    public String getVendorSpecificParameter(String name)  {
        return (String) vendorSpecificParameter.get(name);
    }

    /**
     * adds the vendorSpecificParameter
     */
    public void putVendorSpecificParameter(String paramName, String paramValue) {
        this.vendorSpecificParameter.put(paramName, paramValue);
    }

    /**
     * sets the vendorSpecificParameter
     */
    public void setVendorSpecificParameter(HashMap vendorSpecificParameter) {
        this.vendorSpecificParameter = vendorSpecificParameter;        
    }

    /**
     * returns the ID of a request
     */
    public String getId() {
        return id;
    }

    /**
     * sets the ID of a request
     */
    public void setId(String id) {
        this.id = id;
    }

    /** returns the requested service version
     *
     */
    public String getVersion() {
        return version;
    }
    
    /** sets the requested service version
     *
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /** returns the URI of a HTTP GET request. If the request doesn't support
     * HTTP GET a <tt>WebServiceException</tt> will be thrown
     *
     */
    public String getRequestParameter() throws WebServiceException {
        throw new WebServiceException("HTTP GET isn't supported");
    }

    /**
     *
     *
     * @return 
     */
    public String toString() {
        String ret = null;
        ret = "request = " + request + "\n";
        ret += ("service = " + service + "\n");
        ret += ("vendorSpecificParameter = " + vendorSpecificParameter + "\n");
        ret += ("id = " + id + "\n");
        return ret;
    }
}