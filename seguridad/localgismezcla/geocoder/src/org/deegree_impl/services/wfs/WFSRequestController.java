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

import java.io.*;

import java.util.*;

import org.deegree.gml.*;
import org.deegree.model.feature.*;
import org.deegree.model.geometry.GM_Position;
import org.deegree.services.*;
import org.deegree.services.wfs.*;
import org.deegree.services.wfs.protocol.*;
import org.deegree.services.wfs.protocol.WFSGetCapabilitiesResponse;
import org.deegree.xml.*;

import org.deegree_impl.gml.*;
import org.deegree_impl.model.feature.*;
import org.deegree_impl.services.*;
import org.deegree_impl.services.wfs.protocol.*;
import org.deegree_impl.tools.*;

import org.w3c.dom.*;


/**
 * the class administers all request that are in progress
 * by the WFS (Dispatcher)
 * <p>--------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 */
final public class WFSRequestController implements RequestController {
    private Map handler = Collections.synchronizedMap( new HashMap() );
    private OGCWebServiceRequest request = null;

    /**
     * Creates a new WFSRequestController object.
     *
     * @param request 
     */
    public WFSRequestController( OGCWebServiceRequest request ) {
        this.request = request;

        if ( request instanceof WFSDescribeFeatureTypeRequest ) {
            String[] typeNames = ( (WFSDescribeFeatureTypeRequest)request ).getTypeNames();

            for ( int i = 0; i < typeNames.length; i++ ) {
                addRequestPart( typeNames[i] );
            }
        } else if ( request instanceof WFSGetFeatureRequest ) {
            WFSQuery[] queries = ( (WFSGetFeatureRequest)request ).getQuery();

            for ( int i = 0; i < queries.length; i++ ) {
                addRequestPart( queries[i].getTypeName() );
            }
        } else if ( request instanceof WFSGetCapabilitiesRequest ) {
            addRequestPart( request );
        } else if ( request instanceof WFSTransactionRequest ) {
            WFSOperation[] opreations = ( (WFSTransactionRequest)request ).getOperations();
            String[] aft = getAffectedFeatureTypes( opreations );

            for ( int i = 0; i < aft.length; i++ ) {
                addRequestPart( aft[i] );
            }
        }
    }

    /**
     * returns all feature types affected by the current request
     */
    private String[] getAffectedFeatureTypes() {
        Debug.debugMethodBegin( this, "getAffectedFeatureTypes" );

        String[] afft = new String[handler.size()];
        Iterator iterator = handler.keySet().iterator();
        int k = 0;

        while ( iterator.hasNext() ) {
            afft[k++] = (String)iterator.next();
        }

        Debug.debugMethodEnd();
        return afft;
    }

    /**
     * returns the list of feature types that are affected by a Transaction
     */
    private String[] getAffectedFeatureTypes( WFSOperation[] operations ) {
        ArrayList list = new ArrayList();

        for ( int i = 0; i < operations.length; i++ ) {
            if ( operations[i] instanceof WFSInsert ) {
                String[] ft = ( (WFSInsert)operations[i] ).getFeatureTypes();

                for ( int j = 0; j < ft.length; j++ ) {
                    list.add( ft[j] );
                }
            } else if ( operations[i] instanceof WFSUpdate ) {
                //((WFSUpdate)operations[i]).
            } else if ( operations[i] instanceof WFSDelete ) {
            } else {
                // native request
            }
        }

        return (String[])list.toArray( new String[list.size()] );
    }

    /**
     * Adds a part of the whole request to the controller. The parameter
     * identifies the listener that's responsible for performing this part of
     * the request.
     */
    public void addRequestPart( Object obj ) {
        Debug.debugMethodBegin( this, "addRequestPart" );

        this.handler.put( obj, new Boolean( false ) );

        Debug.debugMethodEnd();
    }

    /**
     * return the request handled by the class
     */
    public OGCWebServiceRequest getRequest() {
        return request;
    }

    /**
     * returns true if all operations of the request
     * are performed
     */
    public boolean requestFinished() {
        Iterator iterator = handler.values().iterator();

        boolean finished = true;

        while ( iterator.hasNext() ) {
            if ( iterator.next() instanceof Boolean ) {
                finished = false;
                break;
            }
        }

        return finished;
    }

    /**
     * adds a response to the request controlled by this class
     * or a part of it.
     */
    public void addResponse( OGCWebServiceResponse response ) {
        Debug.debugMethodBegin( this, "addResponse" );

        if ( response instanceof WFSGetFeatureResponse || 
                 response instanceof WFSTransactionResponse || 
                 response instanceof WFSDescribeFeatureTypeResponse ) {
            String[] afft = ( (WFSBasicResponse)response ).getAffectedFeatureTypes();

            for ( int i = 0; i < afft.length; i++ ) {
                if ( i == 0 ) {
                    handler.put( afft[i], response );
                } else {
                    handler.put( afft[i], " " );
                }
            }
        }

        Debug.debugMethodEnd();
    }

    /**
     * creates a resonse object merging all independed
     * responses that has been resulted from perfoming
     * the request
     */
    public OGCWebServiceResponse getResponse() throws Exception {
        Debug.debugMethodBegin( this, "getResponse" );

        OGCWebServiceResponse response = null;

        if ( request instanceof WFSDescribeFeatureTypeRequest ) {
            response = createDescribeFeatureTypeResponse();
        } else if ( request instanceof WFSGetFeatureRequest ) {
            response = createGetFeatureResponse();
        } else if ( request instanceof WFSGetCapabilitiesRequest ) {
            response = createCapabilitiesResponse();
        } else if ( request instanceof WFSTransactionRequest ) {
            response = createTransactionResponse();
        }

        Debug.debugMethodEnd();

        return response;
    }

    /**
     * creates a complete response object from the partial responses
     * of different data store to a DescribFeatureType Request
     */
    private WFSDescribeFeatureTypeResponse createDescribeFeatureTypeResponse()
                                                                      throws DispatcherException {
        Debug.debugMethodBegin( this, "createDescribeFeatureTypeResponse" );

        //ArrayList schemas = new ArrayList();
        Iterator iterator = handler.values().iterator();
        OGCWebServiceException excep = null;
        Document[] schemaDocs = null;

        // insert all other schemas into the response object
        try {
            ArrayList schemas = new ArrayList();

            StringReader sr = new StringReader( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
                                                "<xsd:schema xmlns:xsd=\"http://www.w3.org/" + 
                                                "2001/XMLSchema\" elementFormDefault" + 
                                                "=\"qualified\" attributeFormDefault" + 
                                                "=\"unqualified\"><xsd:import " + 
                                                "schemaLocation=\"http://www.deegree.org/xml" + 
                                                "/schemas/wcts/feature.xsd\"/>" + 
                                                "<xsd:element name=\"ResultCollection\" " + 
                                                "substitutionGroup=\"gml:_FeatureCollection\">" + 
                                                "</xsd:element></xsd:schema>" );

            schemas.add( XMLTools.parse( sr ) );

            while ( iterator.hasNext() ) {
                Object res = iterator.next();

                if ( !( res instanceof String ) ) {
                    WFSDescribeFeatureTypeResponse response = (WFSDescribeFeatureTypeResponse)res;

                    // if an exception raised while performing the request leaf the
                    // while loop
                    if ( response.getException() != null ) {
                        excep = new OGCWebServiceException_Impl( response.getException() );
                        break;
                    }

                    schemas.add( response.getFeatureTypeSchema() );
                }
            }

            schemaDocs = (Document[])schemas.toArray( new Document[schemas.size()] );
        } catch ( Exception e ) {
            Debug.debugException( e, " - " );
        }

        String[] afft = getAffectedFeatureTypes();

        // merge all schema to one general schema
        Document schema = null;

        try {
            schema = XMLTools.mergeSchemas( schemaDocs );
        } catch ( Exception ex ) {
            throw new DispatcherException( ex.toString() );
        }

        WFSDescribeFeatureTypeResponse response_ = 
        		WFSProtocolFactory.createWFSDescribeFeatureTypeResponse( request, 
        														afft, excep, schema );

        Debug.debugMethodEnd();

        return response_;
    }

    /**
     * creates a complete response object from the partial responses
     * of different data store to a GetFeature Request
     */
    private OGCWebServiceResponse createGetFeatureResponse() {
        Debug.debugMethodBegin( this, "createGetFeatureResponse" );

        OGCWebServiceResponse response = null;
        GMLFeatureCollection gmlfc = null;
        FeatureCollection fc = null;
        Document doc = null;
        String format = null;
        OGCWebServiceException excep = null;

        int c = 0;
        Iterator iterator = handler.values().iterator();

        while ( iterator.hasNext() ) {
            Object obj = iterator.next();

            if ( !( obj instanceof String ) ) {
                OGCWebServiceResponse response_ = (OGCWebServiceResponse)obj;

                // if an exception raised while performing the request leaf the
                // while loop
                if ( response_.getException() != null ) {
                    excep = new OGCWebServiceException_Impl( response_.getException() );
                    break;
                }

                format = ( (WFSGetFeatureRequest)response_.getRequest() ).getOutputFormat();

                if ( format.equals( WFSConstants.GML ) ) {
                    gmlfc = formatGMLFC( gmlfc, (WFSGetFeatureResponse)response_ );
                } else if ( format.equals( WFSConstants.FEATURECOLLECTION ) ) {
                    fc = formatFC( fc, (WFSGetFeatureResponse)response_ );
                } else if ( format.equals( WFSConstants.XML ) ) {
                    doc = formatXML( doc, (WFSGetFeatureResponse)response_ );
                } else {
                    doc = formatXML( doc, (WFSGetFeatureResponse)response_ );
                }
            }
        }

        String[] afft = getAffectedFeatureTypes();

        if ( fc != null ) {
            // FeatureCollection response format
            response = WFSProtocolFactory.createWFSGetFeatureResponse( request, afft, excep, fc );
        } else if ( gmlfc != null ) {
            // GMLFeatureCollection response format
            response = WFSProtocolFactory.createWFSGetFeatureResponse( request, afft, excep, gmlfc );
        } else {
            // XML response format
            response = WFSProtocolFactory.createWFSGetFeatureResponse( request, afft, excep, doc );
        }

        Debug.debugMethodEnd();

        return response;
    }

    /**
     * formats the result of a GetFeature request to GML and adds it to
     * the submitted <tt>GMLFeatureCollection</tt>
     */
    private GMLFeatureCollection formatGMLFC( GMLFeatureCollection gmlfc, 
                                              WFSGetFeatureResponse response ) {
        Debug.debugMethodBegin( this, "formatGMLFC" );

        GMLFeatureCollection fc_ = (GMLFeatureCollection)response.getResponse();

        // initializes gml feature collection at first loop
        if ( gmlfc == null ) {
            gmlfc = GMLFactory.createGMLFeatureCollection( "ResultCollection" );

            double[] bb = getFCBoundingBox( fc_ );
            gmlfc.setBoundingBox( bb[0], bb[1], bb[2], bb[3] );
        } else {
            double[] bb1 = getFCBoundingBox( fc_ );
            double[] bb2 = getFCBoundingBox( gmlfc );

            if ( (bb2[0] > -9E99 && bb2[0] < bb1[0]) || bb1[0] <= -9E99 ) {
                bb1[0] = bb2[0];
            }

            if ( (bb2[1] > -9E99 && bb2[1] < bb1[1]) || bb1[1] <= -9E99  ) {
                bb1[1] = bb2[1];
            }

            if (  (bb2[2] < 9E99 && bb2[2] > bb1[2]) || bb1[2] >= 9E99  ) {
                bb1[2] = bb2[2];
            }

            if ( (bb2[3] < 9E99 && bb2[3] > bb1[3]) || bb1[3] >= 9E99 ) {
                bb1[3] = bb2[3];
            }

            gmlfc.setBoundingBox( bb1[0], bb1[1], bb1[2], bb1[3] );
        }

        if ( fc_ != null ) {
            // add namespaces to the main feature collection
            Element elem = ( (GMLFeatureCollection_Impl)fc_ ).getAsElement();
            Element element = ( (GMLFeatureCollection_Impl)gmlfc ).getAsElement();

            NamedNodeMap nnm = elem.getAttributes();

            for ( int i = 0; i < nnm.getLength(); i++ ) {
                element.setAttribute( nnm.item( i ).getNodeName(), nnm.item( i ).getNodeValue() );
            }

            GMLFeature[] features = fc_.getFeatures();

            // add features of the current collection to the
            // basic collection
            if ( features != null ) {
                for ( int i = 0; i < features.length; i++ ) {
                    gmlfc.addFeature( features[i] );
                }
            }
        }

        Debug.debugMethodEnd();
        return gmlfc;
    }

    /**
     *
     *
     * @param fc 
     *
     * @return 
     */
    private double[] getFCBoundingBox( GMLFeatureCollection fc ) {
        double[] bb = new double[4];
        GMLBox box = fc.getBoundedBy();

        if ( box.getMin() != null ) {
            bb[0] = box.getMin().getX();
            bb[1] = box.getMin().getY();
            bb[2] = box.getMax().getX();
            bb[3] = box.getMax().getY();
        } else {
            GMLCoordinates[] c = box.getCoordinates();

            if ( c != null ) {
                GM_Position[] pos1 = GMLCoordinatesParser_Impl.coordinatesToPoints( c[0] );
                GM_Position[] pos2 = GMLCoordinatesParser_Impl.coordinatesToPoints( c[1] );
                double minx = pos1[0].getX();
                double miny = pos1[0].getY();
                double maxx = pos2[0].getX();
                double maxy = pos2[0].getY();

                if ( minx > maxx ) {
                    double tmp = minx;
                    minx = maxx;
                    maxx = tmp;
                }

                if ( miny > maxy ) {
                    double tmp = miny;
                    miny = maxy;
                    maxy = tmp;
                }

                bb[0] = minx;
                bb[1] = miny;
                bb[2] = maxx;
                bb[3] = maxy;
            }
        }

        return bb;
    }

    /**
     * formats the result of a GetFeature request as <tt>Feature</tt>s and adds
     * them to the submitted <tt>FeatureCollection</tt>
     */
    private FeatureCollection formatFC( FeatureCollection fc, WFSGetFeatureResponse response ) {
        Debug.debugMethodBegin( this, "formatGMLFC" );

        // initializes feature collection at first loop
        if ( fc == null ) {
            FeatureFactory ff = new FeatureFactory();
            fc = ff.createFeatureCollection( request.getId(), handler.size() );
        }

        FeatureCollection fc_ = (FeatureCollection)response.getResponse();

        if ( fc_ != null ) {
            Feature[] features = fc_.getAllFeatures();

            // add features of the current collection to the
            // basic collection
            if ( features != null ) {
                for ( int i = 0; i < features.length; i++ ) {
                    try {
                        fc.appendFeature( features[i] );
                    } catch ( Exception ex ) {
                        System.out.println( ex );
                    }
                }
            }
        }

        Debug.debugMethodEnd();
        return fc;
    }

    /**
     * formats the result of a GetFeature request as <tt>Element</tt>s and adds
     * them to the submitted <tt>Document</tt>
     */
    private Document formatXML( Document doc, WFSGetFeatureResponse response ) {
        Debug.debugMethodBegin( this, "formatXML" );

        if ( doc == null ) {
            doc = (Document)response.getResponse();
        } else {
            Document doc_ = (Document)response.getResponse();
            Element root = doc.getDocumentElement();
            Element root_ = doc_.getDocumentElement();
            NodeList nl = root_.getChildNodes();

            if ( nl != null ) {
                for ( int i = 0; i < nl.getLength(); i++ ) {
                    XMLTools.insertNodeInto( nl.item( i ), root );
                }
            }
        }

        Debug.debugMethodEnd();
        return doc;
    }

    /**
     * creates a complete response object from the partial responses
     * of different data store to a GetFeature Request
     */
    private OGCWebServiceResponse createCapabilitiesResponse() {
        Debug.debugMethodBegin( this, "createCapabilitiesResponse" );

        WFSGetCapabilitiesResponse response = null;

        // create a new and empty response object that will contain
        // the complete response, merged from all responses of different
        // data stores
        Iterator iterator = handler.values().iterator();
        response = (WFSGetCapabilitiesResponse)iterator.next();

        Debug.debugMethodEnd();

        return response;
    }

    /**
     *
     */
    private OGCWebServiceResponse createTransactionResponse() {
        Debug.debugMethodBegin( this, "createTransactionResponse" );

        OGCWebServiceException excep = null;
        ArrayList insertR = new ArrayList();

        int c = 0;
        Iterator iterator = handler.values().iterator();

        while ( iterator.hasNext() ) {
            Object obj = iterator.next();

            if ( !obj.toString().equals( " " ) ) {
                WFSTransactionResponse response_ = (WFSTransactionResponse)obj;

                // if request not succeed create an OGCWebServiceException object
                if ( !response_.getStatus().equalsIgnoreCase( "SUCCESS" ) ) {
                    String locator = response_.getLocator();
                    String message = response_.getMessage();
                    excep = new OGCWebServiceException_Impl( locator, message );
                    break;
                }

                // the response object contains WFSInsertResult objects
                // collect them within an ArrayList
                WFSInsertResult[] ir = response_.getInsertResult();

                if ( ir != null ) {
                    for ( int i = 0; i < ir.length; i++ ) {
                        insertR.add( ir[i] );
                    }
                }
            }
        }

        WFSInsertResult[] ir = new WFSInsertResult[insertR.size()];
        ir = (WFSInsertResult[])insertR.toArray( ir );

        String handle = ( (WFSTransactionRequest)request ).getHandle();
        OGCWebServiceResponse response = 
        		WFSProtocolFactory.createWFSTransactionResponse( request, 
                                                               getAffectedFeatureTypes(), 
                                                               excep, ir, "SUCCESS", 
                                                               handle );

        Debug.debugMethodEnd();

        return response;
    }
}