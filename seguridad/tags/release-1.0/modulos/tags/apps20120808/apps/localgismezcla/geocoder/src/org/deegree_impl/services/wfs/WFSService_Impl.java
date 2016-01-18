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

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.deegree.services.Handler;
import org.deegree.services.OGCWebServiceClient;
import org.deegree.services.OGCWebServiceEvent;
import org.deegree.services.OGCWebServiceException;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;
import org.deegree.services.WebServiceException;
import org.deegree.services.wfs.GetFeatureResponseHandler;
import org.deegree.services.wfs.capabilities.GetFeature;
import org.deegree.services.wfs.capabilities.WFSCapabilities;
import org.deegree.services.wfs.protocol.WFSDescribeFeatureTypeResponse;
import org.deegree.services.wfs.protocol.WFSGetCapabilitiesRequest;
import org.deegree.services.wfs.protocol.WFSGetCapabilitiesResponse;
import org.deegree.services.wfs.protocol.WFSGetFeatureRequest;
import org.deegree.services.wfs.protocol.WFSGetFeatureResponse;
import org.deegree.services.wfs.protocol.WFSLockFeatureResponse;
import org.deegree.services.wfs.protocol.WFSTransactionResponse;
import org.deegree.xml.Marshallable;
import org.deegree.xml.XMLTools;
import org.deegree_impl.services.OGCWebServiceEvent_Impl;
import org.deegree_impl.services.OGCWebServiceException_Impl;
import org.deegree_impl.services.OGCWebServiceResponse_Impl;
import org.deegree_impl.services.OGCWebService_Impl;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.Debug;
import org.w3c.dom.Document;


/**
 *
 * This is the OGC conform web feature server class within the deegree framework.
 * A <tt>WFSService_Impl</tt> extends the <tt>WFSService</tt> interface to act
 * like a OGC web service. This means that a WFS is callable through the
 * <tt>doService</tt>-method inherited from <tt>OGCWebService</tt>.
 *
 * <p>-------------------------------------------------------------------------</p>
 *
 * @author Andreas Poth
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
public class WFSService_Impl extends OGCWebService_Impl implements Handler {
    
    private Handler dispatcher = null;
    protected Map clients = Collections.synchronizedMap( new HashMap() );
    

    /**
     * the constructor receives an instance of o WFSCapabilities object that
     * enables accessing the capabilities of the WFS which includes the
     * assaciation between services, feature types and responsible classes.
     * The second parameter is an instance of the (a) WFSDispatcher that
     * handles the requests and calls the responsible data store classes
     * (e.g. OracleDataStore).
     */
    public WFSService_Impl( WFSCapabilities capabilities ) {
        this.capabilities = capabilities;
    }

    /**
     * removes all resources allocated
     */
    public void finalize() {
        dispatcher.removeHandler( this );
    }

    /**
     * registers a Handler so this Handler is able to act as a proxy
     * to the registered handler
     */
    public void registerHandler( Handler handler ) {
        this.dispatcher = handler;
    }

    /**
     * @see registerHandler
     */
    public void removeHandler( Handler handler ) {
        if ( handler.equals( dispatcher ) ) {
            dispatcher = null;
        }
    }
       
    /**
     * implements the <tt>doService</tt> method inherited from the
     * <tt>OGCWebService</tt> interface via <tt>WFSService<tt>. The method
     * analyses the request and calls the WFSDispatcher for performing it.
     */
    public synchronized void doService( OGCWebServiceEvent event ) throws WebServiceException {
        Debug.debugMethodBegin( this, "doService" );

        OGCWebServiceRequest request = event.getRequest();    
        clients.put( request.getId(), event.getDestination() );

        event = new OGCWebServiceEvent_Impl( this, request, "Request sent by " + 
                                             "the WFSService.", null );
        handleRequest( event );

        Debug.debugMethodEnd();
    }
    
    /**
     * the method performs the handling of the passed OGCWebServiceEvent directly 
     * and returns the result to the calling class/method
     *
     * @param request request (WMS, WCS, WFS, WCAS, WCTS, WTS, Gazetter) to perform
     *
     * @throws WebServiceException 
     */
    public OGCWebServiceResponse doService( OGCWebServiceRequest request ) throws WebServiceException {
        Debug.debugMethodBegin();        
        Debug.debugMethodEnd();
        throw new NoSuchMethodError( "doService(OGCWebServiceRequest)" );
    }

    /**
     * handles a request against an OGC web service
     */
    public void handleRequest( OGCWebServiceEvent event ) {
        Debug.debugMethodBegin( this, "handleRequest" );

        // call the dispatcher to handle the request
        OGCWebServiceRequest request = event.getRequest();
        if ( !( request instanceof WFSGetCapabilitiesRequest ) ) {
            dispatcher.handleRequest( event );
        } else {
            try {
                handleGetCapabilitiesResponse( request );
            } catch ( Exception ex ) {
                Debug.debugException( ex, null );
                OGCWebServiceClient client = 
                    (OGCWebServiceClient)clients.get( request.getId() );
                client.write( ex );
                clients.remove( request.getId() );
            }
        }

        Debug.debugMethodEnd();
    }

    /**
     * returns true if the handler is interested in a event
     */
    public synchronized boolean isInterested( OGCWebServiceEvent event ) {
        if ( event.getType() == OGCWebServiceEvent.RESPONSE ) {
            String id1 = event.getId();
            //String id2 = request.getId();
            String[] ids = (String[])clients.keySet().toArray( new String[ clients.size() ] );
            for (int i = 0; i < ids.length; i++) {
                if ( ids[i].equals( id1 ) ){
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * receives the response from the WFSDispatcher. Calling this method
     * an internal flag is set that indicates that the waiting loop can
     * be aborted without an exception.
     */
    public synchronized void handleResponse( OGCWebServiceEvent event ) {
        Debug.debugMethodBegin( this, "doResponse" );

        OGCWebServiceResponse response = event.getResponse();
        OGCWebServiceRequest request = response.getRequest();
        OGCWebServiceClient client = (OGCWebServiceClient)clients.get( request.getId() );
        // write the response to the request into the client
        // of the HttpServletResponse object belonging to the request
        try {          
            OGCWebServiceException exce = null;            
            if ( response.getException() != null ) {
                exce = new OGCWebServiceException_Impl( response.getException() );                
            }
         
            // if exception != null an exception raised performing the request
            if ( response instanceof WFSGetCapabilitiesResponse ) {
                if ( exce != null ) {
                    OGCWebServiceResponse res = 
                    	WFSProtocolFactory.createWFSGetCapabilitiesResponse( request, exce, null);
                    OGCWebServiceEvent event_ = new OGCWebServiceEvent_Impl( this, res, "" );
                    client.write( event_ );
                } else {
                    // should never be called because getCapabilities shall
                    // be handled directly by handleRequest(..)                
                    handleGetCapabilitiesResponse( request );
                }
            } else if ( response instanceof WFSGetFeatureResponse ) {
                if ( exce != null ) {
                    OGCWebServiceResponse res = 
                    	WFSProtocolFactory.createWFSGetFeatureResponse( request, null, exce, null);
                    OGCWebServiceEvent event_ = new OGCWebServiceEvent_Impl( this, res, "" );
                    client.write( event_ );
                } else {
                    // get responsible class for handling the defined response format
                    String outputFormat = ( (WFSGetFeatureRequest)request ).getOutputFormat();

                    GetFeature gf = ((WFSCapabilities)capabilities).getCapability().getRequest().getGetFeature();
                    GetFeatureResponseHandler gfrh = gf.getClassForFormat( outputFormat );

                    if ( gfrh == null ) {
                        throw new Exception( "Unsupported result format: " + outputFormat );
                    } else {
                        gfrh.handleResponse( (WFSGetFeatureResponse)response, client, 
                                             (WFSCapabilities)capabilities );
                    }
                }
            } else if ( response instanceof WFSDescribeFeatureTypeResponse ) {
                if ( exce != null ) {
                    OGCWebServiceResponse res = 
                    	WFSProtocolFactory.createWFSDescribeFeatureTypeResponse( request, null, exce, null);
                    OGCWebServiceEvent event_ = new OGCWebServiceEvent_Impl( this, res, "" );
                    client.write( event_ );
                } else {
                    handleDescribeFeatureTypeResponse( response );
                }
            } else if ( response instanceof WFSLockFeatureResponse ) {
                if ( exce != null ) {
                    OGCWebServiceResponse res = 
                    	WFSProtocolFactory.createWFSGetFeatureResponse( request, null, exce, null);
                    //FIXME
                } else {
                    handleLockFeatureResponse();
                }
            } else if ( response instanceof WFSTransactionResponse ) {
                if ( exce != null ) {
                    OGCWebServiceResponse res = 
                    	WFSProtocolFactory.createWFSTransactionResponse( request, null, exce, null, null, "");
                    OGCWebServiceEvent event_ = new OGCWebServiceEvent_Impl( this, res, "" );
                    client.write( event_ );
                } else {
                    handleTransactionResponse( response );
                }
            } else {
                // if the result isn't an instance of a WFS response just deliever
                // it to the client
                OGCWebServiceEvent event_ = new OGCWebServiceEvent_Impl( this, response, "" );
                client.write( event_ );
            }
        } catch ( Exception ex ) {
            Debug.debugException( ex, null );
            OGCWebServiceException exce = 
                new OGCWebServiceException_Impl( "WFSService_Impl", ex.toString() );
            Document doc = null;
            try {
                StringReader sr = new StringReader( ((Marshallable)exce).exportAsXML() );
                doc = XMLTools.parse( sr );
            } catch(Exception ee) {
                System.out.println(ee);	
            }
            OGCWebServiceResponse res = 
                new OGCWebServiceResponse_Impl( request, doc );               
            OGCWebServiceEvent event_ = new OGCWebServiceEvent_Impl( this, res, "" );
            client.write( event_ );
        }
        
        clients.remove( request.getId() );

        Debug.debugMethodEnd();
    }

    /**
     * handles the response of an GetCapabilities request
     */
    private void handleGetCapabilitiesResponse(OGCWebServiceRequest request) throws Exception {
        Debug.debugMethodBegin( this, "handleGetCapabilitiesResponse" );                
        
        OGCWebServiceResponse resp = 
        	WFSProtocolFactory.createWFSGetCapabilitiesResponse( request, null, 
                                                 (WFSCapabilities)capabilities );
        OGCWebServiceEvent event = new OGCWebServiceEvent_Impl( this, resp, "" );

        OGCWebServiceClient client = (OGCWebServiceClient)clients.get( request.getId() );
        client.write( event );
        clients.remove( request.getId() );

        Debug.debugMethodEnd();
    }

    /**
     * handles the response of an DescribeFeatureType request
     */
    private void handleDescribeFeatureTypeResponse(OGCWebServiceResponse response) throws Exception {
        Debug.debugMethodBegin( this, "handleDescribeFeatureTypeResponse" );

        OGCWebServiceEvent event = new OGCWebServiceEvent_Impl( this, response, "" );
      
        OGCWebServiceClient client = (OGCWebServiceClient)clients.get( response.getRequest().getId() );
        client.write( event );
        clients.remove( response.getRequest().getId() );

        Debug.debugMethodEnd();
    }

    /**
     * handles the response of an LockFeature request
     */
    private void handleLockFeatureResponse() throws Exception {
        throw new NoSuchMethodException( "Lock Feature not supported yet" );
    }

    /**
     * handles the response of an Transaction request
     */
    private void handleTransactionResponse(OGCWebServiceResponse response) throws Exception {
        Debug.debugMethodBegin( this, "handleTransactionResponse" );

        OGCWebServiceEvent event = new OGCWebServiceEvent_Impl( this, response, "" );
        OGCWebServiceClient client = (OGCWebServiceClient)clients.get( response.getRequest().getId() );
        client.write( event );
        clients.remove( response.getRequest().getId() );

        Debug.debugMethodEnd();
    }
}