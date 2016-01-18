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
package org.deegree_impl.services.wfs;

import java.net.URL;

import org.deegree.services.OGCWebServiceClient;
import org.deegree.services.OGCWebServiceEvent;
import org.deegree.services.OGCWebServiceResponse;
import org.deegree.services.capabilities.DCPType;
import org.deegree.services.capabilities.HTTP;
import org.deegree.services.wfs.GetFeatureResponseHandler;
import org.deegree.services.wfs.capabilities.WFSCapabilities;
import org.deegree.services.wfs.protocol.WFSGetFeatureResponse;
import org.deegree_impl.gml.GMLFeatureCollection_Impl;
import org.deegree_impl.services.OGCWebServiceEvent_Impl;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.Debug;
import org.w3c.dom.Element;


/**
 * class for handling GetFeatureResponses formated as GML
 *
 * <p>-----------------------------------------------------</p>
 *
 * @author Andreas Poth
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
public class GMLResponseHandler implements GetFeatureResponseHandler {
    /**
     * the method handles the response to a get feature request of the
     * WFSDispatcher and write it to the submittetd WFS client
     */
    public void handleResponse( WFSGetFeatureResponse response, OGCWebServiceClient client, 
                                WFSCapabilities capabilities ) throws Exception {
        Debug.debugMethodBegin( this, "handleResponse" );
       
        Object result = response.getResponse();
        String[] aff = response.getAffectedFeatureTypes();

        GMLFeatureCollection_Impl fc = (GMLFeatureCollection_Impl)result;

        // get address for referencing DescribeFeatureType request
        // using http get
        URL[] url = null;
        DCPType[] dcp = capabilities.getCapability().getRequest().getDescribeFeatureType()
                                    .getDCPType();

        for ( int i = 0; i < dcp.length; i++ ) {
            HTTP http = (HTTP)dcp[i].getProtocol();
            URL[] url_ = http.getGetOnlineResources();

            if ( ( url_ != null ) && ( url_.length > 0 ) ) {
                url = url_;
                break;
            }
        }

        // construct schema location attribute as reference to the
        // DescribeFeatureType request
        if ( url != null ) {
            Element element = fc.getAsElement();
            String port = "";
            if (url[0].getPort()!=-1) port=":"+url[0].getPort();
            String s = "http://www.deegree.org " + url[0].getProtocol() + "://" + url[0].getHost()+ port + url[0].getPath();            
            s += "?request=DescribeFeatureType&amp;typename=";

            for ( int i = 0; i < aff.length; i++ ) {
                s += aff[i];

                if ( i != ( aff.length - 1 ) ) {
                    s += ",";
                }
            }

            element.setAttribute( "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" );
            element.setAttribute( "xsi:schemaLocation", s );
        }

        OGCWebServiceResponse resp = WFSProtocolFactory.createWFSGetFeatureResponse( response.getRequest(), 
                                                                     aff, null, result );
        OGCWebServiceEvent event = new OGCWebServiceEvent_Impl( this, resp, "" );

        // it's a GMLFeatureCollection
        //client.write( result );
        client.write( event );

        Debug.debugMethodEnd();
    }
}