/*----------------    FILE HEADER  ------------------------------------------

This file has been provided to deegree by
Emanuele Tajariol e.tajariol@libero.it
 
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
package org.deegree_impl.services.wfs.bna;

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
import org.deegree.model.geometry.GM_Curve;
import org.deegree.model.geometry.GM_Envelope;
import org.deegree.model.geometry.GM_Exception;
import org.deegree.model.geometry.GM_MultiCurve;
import org.deegree.model.geometry.GM_MultiPoint;
import org.deegree.model.geometry.GM_MultiSurface;
import org.deegree.model.geometry.GM_Object;
import org.deegree.model.geometry.GM_Point;
import org.deegree.model.geometry.GM_Position;
import org.deegree.model.geometry.GM_Surface;
import org.deegree.model.geometry.GM_SurfacePatch;
import org.deegree.services.wfs.DataStoreOutputFormat;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree.tools.Parameter;
import org.deegree.tools.ParameterList;import org.deegree.xml.DOMPrinter;
import org.deegree.xml.XMLTools;
import org.deegree_impl.gml.GMLFeatureCollection_Impl;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.StringExtend;
import org.w3c.dom.Document;


/**
 * Implements the DataStoreOutputFormat interface to format the result of a
 * data accessing class returned within the values of a HashMap as deegree
 * feature collection
 *
 * <p>-----------------------------------------------------------------------</p>
 *
  * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
public class BNADataStoreOutputGML implements DataStoreOutputFormat {
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
            p = pl.getParameter( WFSConstants.FEATURES );

            Feature[] feature = (Feature[])p.getValue();
            org.deegree.services.wfs.configuration.FeatureType ft = (org.deegree.services.wfs.configuration.FeatureType)pl.getParameter( 
                                                                                                                                WFSConstants.FEATURETYPE )
                                                                                                                          .getValue();
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
                        // handle geometry property
                        s = getGeometry( (GM_Object)o, crs );
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
     *
     *
     * @param o 
     * @param crs 
     *
     * @return 
     *
     * @throws GM_Exception 
     */
    private String getGeometry( GM_Object o, String crs ) throws GM_Exception {
        Debug.debugMethodBegin( this, "getGeometry" );

        StringBuffer sb = null;

        // points
        if ( o instanceof GM_Point ) {
            sb = handlePoints( crs, o );
        } else// curves / linestrings
        if ( o instanceof GM_Curve ) {
            sb = handleCurves( crs, o );
        } else// surfaces / polygons
        if ( o instanceof GM_Surface ) {
            sb = handleSurfaces( crs, o );
        } else if ( o instanceof GM_MultiPoint ) {
            sb = handleMultiPoints( crs, o );
        } else if ( o instanceof GM_MultiCurve ) {
            sb = handleMultiCurves( crs, o );
        } else if ( o instanceof GM_MultiSurface ) {
            sb = handleMultiSurfaces( crs, o );
        }

        updateBoundingBox( o );

        Debug.debugMethodEnd();
        return sb.toString();
    }

    /**
     *
     *
     * @param crs 
     * @param o 
     *
     * @return 
     */
    private StringBuffer handlePoints( String crs, GM_Object o ) {
        Debug.debugMethodBegin( this, "handlePoints" );

        StringBuffer sb = new StringBuffer( 40 );

        if ( ( crs != null ) && ( crs.indexOf( ":" ) > -1 ) ) {
            String[] cs = StringExtend.toArray( crs, ":", false );
            crs = "http://www.opengis.net/gml/srs/" + cs[0].toLowerCase() + ".xml#" + cs[1];
            sb.append( "<gml:Point srsName=\"" + crs + "\">" );
        } else {
            sb.append( "<gml:Point>" );
        }

        sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );
        sb.append( ( (GM_Point)o ).getX() + "," + ( (GM_Point)o ).getY() );
        sb.append( "</gml:coordinates>" );
        sb.append( "</gml:Point>" );

        Debug.debugMethodEnd();

        return sb;
    }

    /**
     *
     *
     * @param crs 
     * @param o 
     *
     * @return 
     *
     * @throws GM_Exception 
     */
    private StringBuffer handleCurves( String crs, GM_Object o ) throws GM_Exception {
        Debug.debugMethodBegin( this, "handleCurves" );

        GM_Position[] p = ( (GM_Curve)o ).getAsLineString().getPositions();

        StringBuffer sb = new StringBuffer( p.length * 40 );

        if ( ( crs != null ) && ( crs.indexOf( ":" ) > -1 ) ) {
            String[] cs = StringExtend.toArray( crs, ":", false );
            crs = "http://www.opengis.net/gml/srs/" + cs[0].toLowerCase() + ".xml#" + cs[1];
            sb.append( "<gml:LineString srsName=\"" + crs + "\">" );
        } else {
            sb.append( "<gml:LineString>" );
        }

        sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );

        for ( int i = 0; i < ( p.length - 1 ); i++ ) {
            sb.append( p[i].getX() + "," + p[i].getY() + " " );
        }

        sb.append( p[p.length - 1].getX() + "," + p[p.length - 1].getY() );
        sb.append( "</gml:coordinates>" );
        sb.append( "</gml:LineString>" );

        Debug.debugMethodEnd();

        return sb;
    }

    /**
     *
     *
     * @param crs 
     * @param o 
     *
     * @return 
     *
     * @throws GM_Exception 
     */
    private StringBuffer handleSurfaces( String crs, GM_Object o ) throws GM_Exception {
        Debug.debugMethodBegin( this, "handleSurfaces" );

        GM_Surface sur = (GM_Surface)o;
        GM_SurfacePatch patch = sur.getSurfacePatchAt( 0 );
        GM_Position[] p = patch.getExteriorRing();

        StringBuffer sb = new StringBuffer( p.length * 40 );

        if ( ( crs != null ) && ( crs.indexOf( ":" ) > -1 ) ) {
            String[] cs = StringExtend.toArray( crs, ":", false );
            crs = "http://www.opengis.net/gml/srs/" + cs[0].toLowerCase() + ".xml#" + cs[1];
            sb.append( "<gml:Polygon srsName=\"" + crs + "\">" );
        } else {
            sb.append( "<gml:Polygon>" );
        }

        // exterior ring
        sb.append( "<gml:outerBoundaryIs><gml:LinearRing>" );
        sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );

        for ( int i = 0; i < ( p.length - 1 ); i++ ) {
            sb.append( p[i].getX() + "," + p[i].getY() + " " );
        }

        sb.append( p[p.length - 1].getX() + "," + p[p.length - 1].getY() );
        sb.append( "</gml:coordinates>" );
        sb.append( "</gml:LinearRing></gml:outerBoundaryIs>" );

        // interior rings
        GM_Position[][] ip = patch.getInteriorRings();

        if ( ip != null ) {
            for ( int j = 0; j < ip.length; j++ ) {
                sb.append( "<gml:innerBoundaryIs><gml:LinearRing>" );
                sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );

                for ( int i = 0; i < ( ip[j].length - 1 ); i++ ) {
                    sb.append( ip[j][i].getX() + "," + ip[j][i].getY() + " " );
                }

                sb.append( ip[j][ip[j].length - 1].getX() + "," + ip[j][ip[j].length - 1].getY() );
                sb.append( "</gml:coordinates>" );
                sb.append( "</gml:LinearRing></gml:innerBoundaryIs>" );
            }
        }

        sb.append( "</gml:Polygon>" );

        Debug.debugMethodEnd();

        return sb;
    }

    /**
     *
     *
     * @param crs 
     * @param o 
     *
     * @return 
     */
    private StringBuffer handleMultiPoints( String crs, GM_Object o ) {
        Debug.debugMethodBegin( this, "handleMultiPoints" );

        GM_MultiPoint mp = (GM_MultiPoint)o;

        StringBuffer sb = new StringBuffer( mp.getSize() * 40 );

        String srsName = "";

        if ( ( crs != null ) && ( crs.indexOf( ":" ) > -1 ) ) {
            String[] cs = StringExtend.toArray( crs, ":", false );
            String tmp = "http://www.opengis.net/gml/srs/" + cs[0].toLowerCase() + ".xml#" + 
                         cs[1];
            srsName = " srsName=\"" + tmp + "\"";
        }

        sb.append( "<gml:MultiPoint" ).append( srsName ).append( ">" );

        for ( int i = 0; i < mp.getSize(); i++ ) {
            sb.append( "<gml:pointMember>" );
            sb.append( "<gml:Point" ).append( srsName ).append( ">" );
            sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );
            sb.append( mp.getPointAt( i ).getX() + "," + mp.getPointAt( i ).getY() );
            sb.append( "</gml:coordinates>" );
            sb.append( "</gml:Point>" );
            sb.append( "</gml:pointMember>" );
        }

        sb.append( "<gml:MultiPoint>" );

        Debug.debugMethodEnd();

        return sb;
    }

    /**
     *
     *
     * @param crs 
     * @param o 
     *
     * @return 
     *
     * @throws GM_Exception 
     */
    private StringBuffer handleMultiCurves( String crs, GM_Object o ) throws GM_Exception {
        Debug.debugMethodBegin( this, "handleMultiCurves" );

        GM_MultiCurve mp = (GM_MultiCurve)o;
        GM_Position[][] p = new GM_Position[mp.getSize()][];
        // estimate size and allocate memory for the StringBuffer
        int cnt = 0;

        for ( int j = 0; j < mp.getSize(); j++ ) {
            p[j] = mp.getCurveAt( j ).getAsLineString().getPositions();
            cnt += p[j].length;
        }

        StringBuffer sb = new StringBuffer( cnt * 35 );

        String srsName = "";

        if ( ( crs != null ) && ( crs.indexOf( ":" ) > -1 ) ) {
            String[] cs = StringExtend.toArray( crs, ":", false );
            String tmp = "http://www.opengis.net/gml/srs/" + cs[0].toLowerCase() + ".xml#" + 
                         cs[1];
            srsName = " srsName=\"" + tmp + "\"";
        }

        sb.append( "<gml:MultiLineString" ).append( srsName ).append( ">" );

        for ( int j = 0; j < mp.getSize(); j++ ) {
            sb.append( "<gml:lineStringMember>" );
            sb.append( "<gml:LineString" ).append( srsName ).append( ">" );
            sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );

            for ( int i = 0; i < ( p[j].length - 1 ); i++ ) {
                sb.append( p[j][i].getX() ).append( "," ).append( p[j][i].getY() ).append( " " );
            }

            sb.append( p[j][p[j].length - 1].getX() + "," + p[j][p[j].length - 1].getY() );
            sb.append( "</gml:coordinates>" );
            sb.append( "</gml:LineString>" );
            sb.append( "</gml:lineStringMember>" );
        }

        sb.append( "</gml:MultiLineString>" );

        Debug.debugMethodEnd();

        return sb;
    }

    /**
     *
     *
     * @param crs 
     * @param o 
     *
     * @return 
     *
     * @throws GM_Exception 
     */
    private StringBuffer handleMultiSurfaces( String crs, GM_Object o ) throws GM_Exception {
        Debug.debugMethodBegin( this, "handleMultiSurfaces" );

        GM_MultiSurface mp = (GM_MultiSurface)o;

        StringBuffer sb = new StringBuffer( mp.getSize() * 5000 );

        String srsName = "";

        if ( ( crs != null ) && ( crs.indexOf( ":" ) > -1 ) ) {
            String[] cs = StringExtend.toArray( crs, ":", false );
            String tmp = "http://www.opengis.net/gml/srs/" + cs[0].toLowerCase() + ".xml#" + 
                         cs[1];
            srsName = " srsName=\"" + tmp + "\"";
        }

        sb.append( "<gml:MultiPolygon" ).append( srsName ).append( ">" );

        for ( int k = 0; k < mp.getSize(); k++ ) {
            sb.append( "<gml:polygonMember>" );
            sb.append( "<gml:Polygon" ).append( srsName ).append( ">" );

            GM_Surface sur = (GM_Surface)mp.getSurfaceAt( k );
            GM_SurfacePatch patch = sur.getSurfacePatchAt( 0 );

            // exterior ring
            sb.append( "<gml:outerBoundaryIs><gml:LinearRing>" );
            sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );

            GM_Position[] p = patch.getExteriorRing();

            for ( int i = 0; i < ( p.length - 1 ); i++ ) {
                sb.append( p[i].getX() ).append( "," ).append( p[i].getY() ).append( " " );
            }

            sb.append( p[p.length - 1].getX() ).append( "," ).append( p[p.length - 1].getY() );
            sb.append( "</gml:coordinates>" );
            sb.append( "</gml:LinearRing></gml:outerBoundaryIs>" );

            // interior rings
            GM_Position[][] ip = patch.getInteriorRings();

            if ( ip != null ) {
                for ( int j = 0; j < ip.length; j++ ) {
                    sb.append( "<gml:innerBoundaryIs><gml:LinearRing>" );
                    sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );

                    for ( int i = 0; i < ( ip[j].length - 1 ); i++ ) {
                        sb.append( ip[j][i].getX() + "," + ip[j][i].getY() + " " );
                    }

                    sb.append( ip[j][ip[j].length - 1].getX() + "," + 
                               ip[j][ip[j].length - 1].getY() );
                    sb.append( "</gml:coordinates>" );
                    sb.append( "</gml:LinearRing></gml:innerBoundaryIs>" );
                }
            }

            sb.append( "</gml:Polygon>" );
            sb.append( "</gml:polygonMember>" );
        }

        sb.append( "</gml:MultiPolygon>" );

        Debug.debugMethodEnd();

        return sb;
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