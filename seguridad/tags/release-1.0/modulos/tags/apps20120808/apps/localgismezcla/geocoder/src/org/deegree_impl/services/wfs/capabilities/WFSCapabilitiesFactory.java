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
package org.deegree_impl.services.wfs.capabilities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.deegree.model.geometry.GM_Envelope;
import org.deegree.model.geometry.GM_Position;
import org.deegree.services.capabilities.DCPType;
import org.deegree.services.capabilities.HTTP;
import org.deegree.services.capabilities.MetadataURL;
import org.deegree.services.capabilities.Service;
import org.deegree.services.wfs.capabilities.Capability;
import org.deegree.services.wfs.capabilities.DescribeFeatureType;
import org.deegree.services.wfs.capabilities.FeatureType;
import org.deegree.services.wfs.capabilities.FeatureTypeList;
import org.deegree.services.wfs.capabilities.GetCapabilities;
import org.deegree.services.wfs.capabilities.GetFeature;
import org.deegree.services.wfs.capabilities.GetFeatureWithLock;
import org.deegree.services.wfs.capabilities.LockFeature;
import org.deegree.services.wfs.capabilities.Operation;
import org.deegree.services.wfs.capabilities.Request;
import org.deegree.services.wfs.capabilities.Transaction;
import org.deegree.services.wfs.capabilities.WFSCapabilities;
import org.deegree.xml.XMLTools;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.deegree_impl.services.capabilities.DCPType_Impl;
import org.deegree_impl.services.capabilities.HTTP_Impl;
import org.deegree_impl.services.capabilities.MetadataURL_Impl;
import org.deegree_impl.services.capabilities.Service_Impl;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.NetWorker;
import org.deegree_impl.tools.StringExtend;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Factory class for creating WFS capability classes from a WFS capabilities
 * XML document that's conform to OGC WFS  specification.
 *
 * <p>----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version 2002-04-16
 */
public final class WFSCapabilitiesFactory {
    /**
    * factory method for creating a <tt>WFSCapabilities</tt> object from
    * a file that contains a OGC WFS 1.0 conform XML capabilities document
    */
    public static synchronized WFSCapabilities createCapabilities( URL url )
                                                           throws IOException, SAXException, 
                                                                  Exception {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "createCapabilities" );

        InputStreamReader isr = new InputStreamReader( url.openStream() );

        WFSCapabilities capabilities = createCapabilities( isr );

        Debug.debugMethodEnd();

        return capabilities;
    }

    /**
    * factory method for creating a <tt>WFSCapabilities</tt> object from
    * a file that contains a OGC WFS 1.0 conform XML capabilities document
    */
    public static synchronized WFSCapabilities createCapabilities( Reader reader )
                                                           throws IOException, SAXException, 
                                                                  Exception {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "createCapabilities" );

        Document doc = XMLTools.parse( reader );

        WFSCapabilities capabilities = createCapabilities( doc );

        Debug.debugMethodEnd();

        return capabilities;
    }

    /**
    * factory method for creating a <tt>WFSCapabilities</tt> object from
    * a OGC WFS 1.0 conform XML capabilities document
    */
    public static synchronized WFSCapabilities createCapabilities( Document doc )
                                                           throws Exception {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "createCapabilities" );

        Element root = doc.getDocumentElement();

        //get general service informations
        String version = XMLTools.getAttrValue( root, "version" );
        String updateSequence = XMLTools.getAttrValue( root, "updateSequence" );
        // get service section 
        Element element = (Element)root.getElementsByTagName( "Service" ).item( 0 );
        Service service = getService( element );

        // get capability section
        element = (Element)root.getElementsByTagName( "Capability" ).item( 0 );

        Capability capability = getCapability( element );

        // get feature type list
        element = (Element)root.getElementsByTagName( "FeatureTypeList" ).item( 0 );

        FeatureTypeList ftl = getFeatureTypeList( element );
        // create capabilities object
        WFSCapabilities_Impl capabilities = new WFSCapabilities_Impl( capability, ftl, version, 
                                                                      updateSequence, service );

        Debug.debugMethodEnd();

        return capabilities;
    }

    /**
    * returns an instance of an object that capsulates the service element of
    * the WFS capabilities.
    */
    private static Service getService( Element serviceElement ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getService" );

        // get service name
        Element element = XMLTools.getNamedChild( serviceElement, "Name" );
        String name = element.getFirstChild().getNodeValue();

        // get service title
        element = XMLTools.getNamedChild( serviceElement, "Title" );

        String title = element.getFirstChild().getNodeValue();

        // get service abstract
        element = XMLTools.getNamedChild( serviceElement, "Abstract" );

        String abstract_ = "";

        if ( element != null ) {
            abstract_ = element.getFirstChild().getNodeValue();
        }

        // get service keywords
        element = XMLTools.getNamedChild( serviceElement, "Keywords" );

        String[] keywords = null;

        if ( element != null ) {
            keywords = getKeywords( element );
        }

        // get service online resource
        element = XMLTools.getNamedChild( serviceElement, "OnlineResource" );

        URL onlineResource = null;

        try {
            onlineResource = new URL( element.getFirstChild().getNodeValue() );
        } catch ( Exception ex ) {
            System.out.println( "getService: " + ex );
        }

        // get service fees
        element = XMLTools.getNamedChild( serviceElement, "Fees" );

        String fees = null;

        if ( element != null ) {
            fees = element.getFirstChild().getNodeValue();
        }

        // get service access constraints
        element = XMLTools.getNamedChild( serviceElement, "AccessConstraints" );

        String accessConstraints = null;

        if ( element != null ) {
            accessConstraints = element.getFirstChild().getNodeValue();
        }

        Service service = new Service_Impl( name, title, abstract_, keywords, onlineResource, null, 
                                            fees, accessConstraints );

        Debug.debugMethodEnd();
        return service;
    }

    /**
    * returns the keywords associated with the service
    */
    private static String[] getKeywords( Element keywordsElement ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getKeywords" );

        String[] kw = null;

        Node node = keywordsElement.getFirstChild();

        if ( node != null ) {
            String keywords = node.getNodeValue();

            if ( keywords != null ) {      	
                kw = StringExtend.toArray( keywords, ",;", true );
            }
        }

        Debug.debugMethodEnd();
        return kw;
    }

    /**
    * returns an instance of an object that capsulates the service element of
    * the WMS capabilities.
    */
    private static Capability getCapability( Element capElement ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getCapability" );

        // get capability request element
        Element element = XMLTools.getNamedChild( capElement, "Request" );
        Request request = getRequest( element );

        // get capability vendor specific capabilities
        element = XMLTools.getNamedChild( capElement, "VendorSpecificCapabilities" );

        Document vendor = getVendorSpecificCapabilities( element );

        Capability capability = new Capability_Impl( request, vendor );

        Debug.debugMethodEnd();
        return capability;
    }

    /**
    * returns an instance of an object that capsulates the request element of
    * the WMS capabilities/capability.
    */
    private static Request getRequest( Element requestElement ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getRequest" );

        // get GetCapabilities object/operation (mandatory)
        Element elem = XMLTools.getNamedChild( requestElement, "GetCapabilities" );
        DCPType[] dcpTypes = getDCPType( elem.getElementsByTagName( "DCPType" ) );
        GetCapabilities getCapa = new GetCapabilities_Impl( dcpTypes );

        // get DescribeFeatureType object/operation (optional)
        elem = XMLTools.getNamedChild( requestElement, "DescribeFeatureType" );

        DescribeFeatureType dft = null;

        if ( elem != null ) {
            dft = getDescribeFeatureType( elem );
        }

        // get Transaction object/operation (optional)
        elem = XMLTools.getNamedChild( requestElement, "Transaction" );

        Transaction transaction = null;

        if ( elem != null ) {
            dcpTypes = getDCPType( elem.getElementsByTagName( "DCPType" ) );
            transaction = new Transaction_Impl( dcpTypes );
        }

        // get GetFeature object/operation (optional)
        elem = XMLTools.getNamedChild( requestElement, "GetFeature" );

        GetFeature getFeature = null;

        if ( elem != null ) {
            getFeature = getGetFeature( elem );
        }

        // get GetFeatureWithLock object/operation (optional)
        elem = XMLTools.getNamedChild( requestElement, "GetFeatureWithLock" );

        GetFeatureWithLock getFeatureWithLock = null;

        if ( elem != null ) {
            getFeatureWithLock = getGetFeatureWithLock( elem );
        }

        // get LockFeature object/operation (optional)
        elem = XMLTools.getNamedChild( requestElement, "LockFeature" );

        LockFeature lockFeature = null;

        if ( elem != null ) {
            dcpTypes = getDCPType( elem.getElementsByTagName( "DCPType" ) );
            lockFeature = new LockFeature_Impl( dcpTypes );
        }

        Request request = new Request_Impl( getCapa, dft, transaction, getFeature, 
                                            getFeatureWithLock, lockFeature );

        Debug.debugMethodEnd();
        return request;
    }

   /**
    * creates list of DCPTypes
    */
    private static DCPType[] getDCPType( NodeList nl ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getDCPType" );

        ArrayList list = new ArrayList();

        for ( int k = 0; k < nl.getLength(); k++ ) {
            Element dcpElement = (Element)nl.item( k );
            Element httpElement = (Element)dcpElement.getElementsByTagName("HTTP" ).item( 0 );

            HTTP http = null;

            try {
                NodeList getL = httpElement.getElementsByTagName( "Get" );
                URL[] getOR = null;
                if (getL != null) {
                    getOR = new URL[getL.getLength()];

                    for ( int i = 0; i < getL.getLength(); i++ ) {
                        getOR[i] = new URL( XMLTools.getAttrValue( getL.item( i ), "onlineResource" ) );
                    }
                }

                NodeList postL = httpElement.getElementsByTagName( "Post" );
                URL[] postOR = null;
                if (postL != null) {
                    postOR = new URL[postL.getLength()];

                    for ( int i = 0; i < postL.getLength(); i++ ) {
                        postOR[i] = new URL( XMLTools.getAttrValue( postL.item( i ), "onlineResource" ) );
                    }
                }

                http = new HTTP_Impl( getOR, postOR );
            } catch ( Exception e ) {
                System.out.println( "getDCPType: " + e );
            }

            list.add( new DCPType_Impl( http ) );
        }

        Debug.debugMethodEnd();
        return (DCPType[])list.toArray( new DCPType[list.size()] );
    }

    /**
    * returns an instance of an DescribeFeatureType object generated from
    * the submitted dom element
    */
    private static DescribeFeatureType getDescribeFeatureType( Element element ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getDescribeFeatureType" );

        DCPType[] dcpTypes = getDCPType( element.getElementsByTagName( "DCPType" ) );

        // get schema description languages
        Element elem = XMLTools.getNamedChild( element, "SchemaDescriptionLanguage" );
        NodeList nl = elem.getChildNodes();
        ArrayList list = new ArrayList();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            if ( nl.item( i ) instanceof Element ) {
                list.add( nl.item( i ).getNodeName() );
            }
        }

        String[] sdl = (String[])list.toArray( new String[list.size()] );

        DescribeFeatureType describeFeatureType = new DescribeFeatureType_Impl( sdl, dcpTypes );

        Debug.debugMethodEnd();
        return describeFeatureType;
    }

     /**
    * returns an instance of an GetFeature object generated from
    * the submitted dom element
    */
    private static GetFeature getGetFeature( Element element ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getGetFeature" );

        DCPType[] dcpTypes = getDCPType( element.getElementsByTagName( "DCPType" ) );

        // get schema description languages
        Element elem = XMLTools.getNamedChild( element, "ResultFormat" );
        NodeList nl = elem.getChildNodes();
        ArrayList list = new ArrayList();
        ArrayList list2 = new ArrayList();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            if ( nl.item( i ) instanceof Element ) {
                String s = XMLTools.getAttrValue( nl.item( i ), "className" );
                if (s == null) {
                    s = "org.deegree_impl.services.wfs.GMLResponseHandler";
                }
                list.add( nl.item( i ).getNodeName() );
                list2.add( s );
            }
        }

        String[] resultFormat = (String[])list.toArray( new String[list.size()] );
        String[] classes = (String[])list2.toArray( new String[list2.size()] );

        GetFeature getFeature = new GetFeature_Impl( resultFormat, classes, dcpTypes );

        Debug.debugMethodEnd();
        return getFeature;
    }

     /**
    * returns an instance of an GetFeatureWithLock object generated from
    * the submitted dom element
    */
    private static GetFeatureWithLock getGetFeatureWithLock( Element element) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory","getGetFeatureWithLock" );

        DCPType[] dcpTypes = getDCPType( element.getElementsByTagName("DCPType" ) );

        // get schema description languages
        Element elem = XMLTools.getNamedChild( element, "ResultFormat" );
        NodeList nl = elem.getChildNodes();
        ArrayList list = new ArrayList();
        ArrayList list2 = new ArrayList();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            if ( nl.item( i ) instanceof Element ) {
                String s = XMLTools.getAttrValue( nl.item( i ), "className");
                if (s == null) {
                    s = "org.deegree_impl.services.wfs.GMLResponseHandler";
                }
                list.add( nl.item( i ).getNodeName() );
                list2.add( s );
            }
        }

        String[] resultFormat = (String[])list.toArray( new String[list.size()] );
        String[] classes = (String[])list2.toArray( new String[list2.size()]);
        GetFeatureWithLock getFeature = 
            new GetFeatureWithLock_Impl( resultFormat, classes, dcpTypes );

        Debug.debugMethodEnd();
        return getFeature;
    }

    /**
    * returns an instance of an object that capsulates the vendor specific
    * capabilities element of the WMS capabilities/capability.
    */
    private static Document getVendorSpecificCapabilities( Element vendorElement ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getVendorSpecificCapabilities" );

        Document doc = null;

        if ( vendorElement != null ) {
            javax.xml.parsers.DocumentBuilder parser = null;

            try {
                parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            } catch ( ParserConfigurationException ex ) {
                System.out.println( "getVendorSpecificCapabilities: " + ex );
            }

            doc = parser.newDocument();
            XMLTools.insertNodeInto( vendorElement, doc );
        }

        Debug.debugMethodEnd();
        return doc;
    }

    /**
    * creates a bounding box from a string containing a comma seperated list
    * of corner coorinates (minx, miny, maxx, maxy)
    */
    private static GM_Envelope createBoundingBox( Element element ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "createBoundingBox" );

        double minx = Double.parseDouble( XMLTools.getAttrValue( element, "minx" ) );
        double miny = Double.parseDouble( XMLTools.getAttrValue( element, "miny" ) );
        double maxx = Double.parseDouble( XMLTools.getAttrValue( element, "maxx" ) );
        double maxy = Double.parseDouble( XMLTools.getAttrValue( element, "maxy" ) );

        GeometryFactory factory = new GeometryFactory();
        GM_Position min = factory.createGM_Position( minx, miny );
        GM_Position max = factory.createGM_Position( maxx, maxy );

        GM_Envelope envelope = factory.createGM_Envelope( min, max );

        Debug.debugMethodEnd();
        return envelope;
    }

    /**
    * returns an instance of an object that capsulates the WFS's
    * feature type list.
    */
    private static FeatureTypeList getFeatureTypeList( Element element ) throws Exception {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getFeatureTypeList" );

        Element elem = XMLTools.getNamedChild( element, "Operations" );
        Operation[] operations = getOperations( elem );

        String className = null;
        URL url = null;
        elem = XMLTools.getNamedChild( element, "ResponsibleClass" );

        if ( elem != null ) {
            className = XMLTools.getAttrValue( elem, "className" );
            url = new URL( XMLTools.getAttrValue( elem, "configURL" ) );
        }

        NodeList nl = element.getElementsByTagName( "FeatureType" );

        FeatureType[] featureTypes = getFeatureTypes( nl, operations, className, url );

        FeatureTypeList ftl = new FeatureTypeList_Impl( operations, featureTypes );

        // set feature types parent list
        for ( int i = 0; i < featureTypes.length; i++ ) {
            ( (FeatureType_Impl)featureTypes[i] ).setParentList( ftl );
        }

        Debug.debugMethodEnd();
        return ftl;
    }

    /**
    * returns an list of operations
    */
    private static Operation[] getOperations( Element element ) throws MalformedURLException {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getOperations" );

        Operation[] operations = null;

        if ( element != null ) {
            ArrayList list = new ArrayList();
            NodeList nl = element.getChildNodes();

            for ( int i = 0; i < nl.getLength(); i++ ) {
                if ( nl.item( i ) instanceof Element ) {
                    String name = nl.item( i ).getNodeName();
                    list.add( new Operation_Impl( name ) );
                }
            }

            operations = (Operation[])list.toArray( new Operation[list.size()] );
        }

        Debug.debugMethodEnd();
        return operations;
    }

      /**
    * returns an list of feature types that are contained within the
    * FeatureTypeList element of the capabilities.
    */
    private static FeatureType[] getFeatureTypes( NodeList nl, Operation[] operations_,  String className, URL configURL )
                                          throws Exception {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getFeatureTypes");

        ArrayList list = new ArrayList();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            Element element = (Element)nl.item( i );

            // get service name
            Element elem = XMLTools.getNamedChild( element, "Name" );
            String name = elem.getFirstChild().getNodeValue();

            // get service title
            elem = XMLTools.getNamedChild( element, "Title" );

            String title = null;

            if ( elem != null ) {
                Node node = elem.getFirstChild();
                if ( node != null ) {
                    title = node.getNodeValue();
                }
            }

            String abstract_ = XMLTools.getStringValue( "Abstract", null, element, null );

            elem = XMLTools.getNamedChild( element, "Operations" );

            Operation[] operations = getOperations( elem );

            // get spatial reference system
            elem = XMLTools.getNamedChild( element, "SRS" );

            String srs = elem.getFirstChild().getNodeValue();

            // get service keywords
            elem = XMLTools.getNamedChild( element, "Keywords" );

            String[] keywords = null;

            if ( elem != null ) {
                keywords = getKeywords( elem );
            }

            // get service lat lon bounding box
            elem = XMLTools.getNamedChild( element, "LatLonBoundingBox" );
            if ( elem == null ) {
                elem = XMLTools.getNamedChild( element, "LatLongBoundingBox");
            }

            GM_Envelope bbox = createBoundingBox( elem );

            // get meta data URL
            NodeList nl_ = element.getElementsByTagName( "MetadataURL" );
            MetadataURL[] metadataURL = null;

            if ( elem != null ) {
                metadataURL = getMetadataURL( nl_ );
            }

            // override responsible class and its configuration file
            // if defined seperatly for this feature type
            elem = XMLTools.getNamedChild( element, "ResponsibleClass" );

            if ( elem != null ) {
                className = XMLTools.getAttrValue( elem, "className" );
                configURL = new URL( XMLTools.getAttrValue( elem, "configURL" ) );
                if ( !NetWorker.existsURL( configURL ) ) {
                    throw new Exception( "URL to configuration file for featuretype: '" +
                                         name + "' dosen't exists at the defined " +
                                         "position! Please check the WFS capabilities." );
                }
            }

            // initialize with inherited operations
           FeatureType ft = createFeatureType( name, title, abstract_, srs, bbox, null, keywords,
                                                operations_, metadataURL, className, configURL );

            // override inherited operations if new ones are defined
            if ( operations != null ) {
                for ( int j = 0; j < operations.length; j++ ) {
                    ( (FeatureType_Impl)ft ).addOperation( operations[j] );
                }
            }

            list.add( ft );
        }

        Debug.debugMethodEnd();
        return (FeatureType[])list.toArray( new FeatureType[list.size()] );
    }

    /**
    * creates a <code>FeatureType</code> object
    */
    public static FeatureType createFeatureType( String name, String title, String abstract_, 
                                                 String srs, GM_Envelope latLonBoundingBox, 
                                                 FeatureTypeList parentList, String[] keywords, 
                                                 Operation[] operations, MetadataURL[] metadataURL, 
                                                 String responsibleClassName, URL configURL ) {
        return new FeatureType_Impl( name, title, abstract_, srs, latLonBoundingBox, parentList, 
                                     keywords, operations, metadataURL, responsibleClassName, 
                                     configURL );
    }

    /**
    * returns a feature types MetadataURL
    */
    private static MetadataURL[] getMetadataURL( NodeList nl ) {
        Debug.debugMethodBegin( "WFSCapabilitiesFactory", "getMetadataURL" );

        ArrayList list = new ArrayList();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            Element element = (Element)nl.item( i );

            String format = XMLTools.getAttrValue( element, "Format" );
            String type = XMLTools.getAttrValue( element, "type" );

            URL onlineResource = null;

            try {
                String s = element.getFirstChild().getNodeValue();
                onlineResource = new URL( s );
            } catch ( Exception ex ) {
                System.out.println( "getMetadataURL: " + ex );
            }

            list.add( new MetadataURL_Impl( type, format, onlineResource ) );
        }

        Debug.debugMethodEnd();
        return (MetadataURL[])list.toArray( new MetadataURL[list.size()] );
    }
    
    
    public static void main(String[] args) {
        try {
            URL url = new URL( "file:///c:/temp/wfs.xml" );
            WFSCapabilitiesFactory.createCapabilities( url );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}