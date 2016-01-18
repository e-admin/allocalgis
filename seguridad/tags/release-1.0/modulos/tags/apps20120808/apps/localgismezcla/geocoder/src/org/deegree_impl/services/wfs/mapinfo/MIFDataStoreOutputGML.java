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
package org.deegree_impl.services.wfs.mapinfo;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.deegree.gml.GMLFeatureCollection;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureTypeProperty;
import org.deegree.model.geometry.GM_Envelope;
import org.deegree.model.geometry.GM_Object;
import org.deegree.model.geometry.GM_Point;
import org.deegree.services.wfs.DataStoreOutputFormat;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree.tools.Parameter;
import org.deegree.tools.ParameterList;
import org.deegree.xml.DOMPrinter;
import org.deegree.xml.XMLTools;
import org.deegree_impl.gml.GMLFeatureCollection_Impl;
import org.deegree_impl.model.cs.Adapters;
import org.deegree_impl.model.cs.ConvenienceCSFactory;
import org.deegree_impl.model.cs.CoordinateSystem;
import org.deegree_impl.model.geometry.GMLAdapter;
import org.deegree_impl.model.geometry.GM_Object_Impl;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.StringExtend;
import org.opengis.cs.CS_CoordinateSystem;
import org.w3c.dom.Document;


/**
 * Implements the DataStoreOutputFormat interface to format the result of a
 * data accessing class returned within the values of a HashMap as deegree
 * feature collection
 *
 * <p>-----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:35 $
 * <p>
 */
public class MIFDataStoreOutputGML implements DataStoreOutputFormat {
    private GeometryFactory factory = new GeometryFactory();
    private double maxx = -9E99;
    private double maxy = -9E99;
    private double minx = 9E99;
    private double miny = 9E99;

    /**
     * formats the data store at the values of the HashMap into
     * one single data structure.
     */
    public Object format( HashMap map, ParameterList paramList ) throws Exception {
        Debug.debugMethodBegin( this, "format" );

        HashMap propNameCache = new HashMap();

        Parameter p = paramList.getParameter( WFSConstants.NAMESPACE );
        String s = "";

        if ( p != null ) {
            s = (String)p.getValue();
        }

        // create the basic gml document
        StringBuffer gml = new StringBuffer( 250000 );
        gml.append( "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" );
        gml.append( "<gml:Collection xmlns:gml=\"http://www.opengis.net/gml\" " + s + ">" );
        gml.append( "<gml:boundedBy><gml:Box><gml:coord><gml:X>-9E99</gml:X>" );
        gml.append( "<gml:Y>-9E99</gml:Y></gml:coord><gml:coord><gml:X>9E99</gml:X>" );
        gml.append( "<gml:Y>9E99</gml:Y></gml:coord></gml:Box></gml:boundedBy>" );

        Iterator iterator = map.values().iterator();

        while ( iterator.hasNext() ) {
            // get submitted parameters
            ParameterList pl = (ParameterList)iterator.next();
            p = pl.getParameter( WFSConstants.CRS );
            String crs = (String)p.getValue();
            ConvenienceCSFactory fac = ConvenienceCSFactory.getInstance();
            CoordinateSystem cs_ = fac.getCSByName( crs );
            Adapters ada = Adapters.getDefault();
            CS_CoordinateSystem cs = ada.export( cs_ );
        
            p = pl.getParameter( WFSConstants.FEATURES );

            Feature[] feature = (Feature[])p.getValue();
            org.deegree.services.wfs.configuration.FeatureType ft = 
                (org.deegree.services.wfs.configuration.FeatureType)pl.getParameter( WFSConstants.FEATURETYPE ).getValue();
            TableDescription td = ft.getMasterTable();
            // get feature type description of the submitted data features
            org.deegree.model.feature.FeatureType ftype = null;
            FeatureTypeProperty[] ftp = null;

            try {
                if ( ( feature != null ) && ( feature.length > 0 ) ) {
                    ftype = feature[0].getFeatureType();
                    ftp = ftype.getProperties();
                }
            } catch ( Exception e ) {
                System.out.println( "1: " + e );
            }

            // transform features to GML
            for ( int i = 0; i < feature.length; i++ ) {
                Object gg = feature[i].getProperty( td.getIdField() );
                String id = "ID" + i;

                if ( gg != null ) {
                    id = gg.toString();

                    try {
                        double d = Double.parseDouble( id );
                        id = "ID" + (int)d;
                    } catch ( Exception ee ) {
                        id = gg.toString().replace( ' ', '_' );
                    }
                }

                gml.append( "<gml:featureMember>" );
                gml.append( "<" + td.getTargetName() + " fid=\"" + id + "\">" );

                // put each property to the gml-string
                for ( int j = 0; j < ftp.length; j++ ) {
                    //s = ft.getProperty( ftp[j].getName() );
                    // TODO: mapping of feature property names to defined property names
                    s = ftp[j].getName();

                    if ( s == null ) {
                        continue;
                    }

                    String pn = (String)propNameCache.get( s );

                    if ( pn == null ) {
                        pn = formatPropertyName( s );
                        propNameCache.put( s, pn );
                    }

                    //Object o = feature[i].getProperty(ftp[j].getName());
                    Object o = feature[i].getProperty( j );

                    if ( td.isGeoFieldIdentifier( ftp[j].getName() ) ) {
                        // set CRS because geometries comming from a 
                        // postgis DB hasn't one
                        ((GM_Object_Impl)o).setCoordinateSystem( cs );
                        // update the bounding box of the feature collection
                        updateBoundingBox( (GM_Object)o );
                        // transform the geometry to GML
                        s = GMLAdapter.export( (GM_Object)o );
                        gml.append( "<" + pn + ">" ).append( s );
                        gml.append( "</" + pn + ">" );
                    } else {
                        // handle none geometry properties
                        if ( o != null ) {
                            gml.append( "<" + pn + ">" );
                            gml.append( XMLTools.validateCDATA( o.toString() ) );
                            gml.append( "</" + pn + ">" );
                        } else {
                            gml.append( "<" + pn + "/>" );
                        }
                    }
                }

                gml.append( "</" + td.getTargetName() + ">" );
                gml.append( "</gml:featureMember>" );
            }
        }

        gml.append( "</gml:Collection>" );

        Document doc = XMLTools.parse( new StringReader( gml.toString() ) );

        // create the feature collection that will contain all requested
        // features (table rows)
        GMLFeatureCollection fc = new GMLFeatureCollection_Impl( doc.getDocumentElement() );

        fc.setBoundingBox( minx, miny, maxx, maxy );

        p = paramList.getParameter( WFSConstants.FILTER );

        if ( p != null ) {
            s = DOMPrinter.nodeToString( ( (GMLFeatureCollection_Impl)fc ).getAsElement(), "" );
            doc = xsltTransformGetFeature( s, (String)p.getValue() );
            fc = new GMLFeatureCollection_Impl( doc.getDocumentElement() );
        }

        Debug.debugMethodEnd();

        return fc;
    }

    /**
    * the method formats a slash-seperated property name like /cities/ID
    * to a dot-seperated xml-valid name --> cities.ID
    */
    private String formatPropertyName( String property ) {
        Debug.debugMethodBegin( this, "formatPropertyName" );
        property = property.replace( '/', '.' );
        property = property.replace( '@', '.' );
        Debug.debugMethodEnd();
        return StringExtend.validateString( property, "." );
    }
    
    /**
     * updates the bounding box of the <tt>GMLFeatureCollection<tt>.
     */
    private void updateBoundingBox( GM_Object geo ) {
        double minx_ = 0;
        double miny_ = 0;
        double maxx_ = 0;
        double maxy_ = 0;

        if ( geo instanceof GM_Point ) {
            minx_ = ( (GM_Point)geo ).getX();
            miny_ = ( (GM_Point)geo ).getY();
            maxx_ = ( (GM_Point)geo ).getX();
            maxy_ = ( (GM_Point)geo ).getY();
        } else {
            GM_Envelope tmp = geo.getEnvelope();
            minx_ = tmp.getMin().getX();
            miny_ = tmp.getMin().getY();
            maxx_ = tmp.getMax().getX();
            maxy_ = tmp.getMax().getY();
        }

        if ( minx_ < minx ) {
            minx = minx_;
        }

        if ( maxx_ > maxx ) {
            maxx = maxx_;
        }

        if ( miny_ < miny ) {
            miny = miny_;
        }

        if ( maxy_ > maxy ) {
            maxy = maxy_;
        }
    }

    /**
     * transforms the response to a GetRecord/Feature request using
     * a predefined xslt-stylesheet
     */
    private Document xsltTransformGetFeature( String gml, String xsltURL ) {
        Debug.debugMethodBegin( this, "xsltTransformGetFeature" );

        Document document = null;

        try {
            document = XMLTools.parse( new StringReader( gml ) );

            // Use the static TransformerFactory.newInstance() method to instantiate
            // a TransformerFactory. The javax.xml.transform.TransformerFactory
            // system property setting determines the actual class to instantiate --
            // org.apache.xalan.transformer.TransformerImpl.
            TransformerFactory tFactory = TransformerFactory.newInstance();

            // Use the TransformerFactory to instantiate a Transformer that will work with
            // the stylesheet you specify. This method call also processes the stylesheet
            // into a compiled Templates object.
            URL url = new URL( xsltURL );
            Transformer transformer = tFactory.newTransformer( new StreamSource( url.openStream() ) );

            // Use the Transformer to apply the associated Templates object to an XML document
            // (foo.xml) and write the output to a file (foo.out).
            StringWriter sw = new StringWriter();
            transformer.transform( new DOMSource( document ), new StreamResult( sw ) );

            document = XMLTools.parse( new StringReader( sw.toString() ) );
        } catch ( Exception e ) {
            Debug.debugException( e, "an error/fault body for the soap message will be created" );
            //TODO exception
        }

        Debug.debugMethodEnd();
        return document;
    }
}