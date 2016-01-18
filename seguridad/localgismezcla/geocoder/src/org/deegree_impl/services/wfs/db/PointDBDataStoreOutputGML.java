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
package org.deegree_impl.services.wfs.db;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.deegree.gml.GMLFeatureCollection;
import org.deegree.model.table.Table;
import org.deegree.services.wfs.DataStoreOutputFormat;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.GeoFieldIdentifier;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree.tools.Parameter;
import org.deegree.tools.ParameterList;
import org.deegree.xml.DOMPrinter;
import org.deegree.xml.XMLTools;
import org.deegree_impl.gml.GMLFeatureCollection_Impl;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.StringExtend;
import org.w3c.dom.Document;

/**
 * Implements the DataStoreOutputFormat interface to format the result of a
 * data accessing class returned within the values of a HashMap as GML
 * feature collection
 *
 * <p>-----------------------------------------------------------------------</p>
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
public class PointDBDataStoreOutputGML implements DataStoreOutputFormat {
    
    private double minx = 9E99;
    private double miny = 9E99;
    private double maxx = -9E99;
    private double maxy = -9E99;
    private GeometryFactory factory = new GeometryFactory();
    
    /**
     * formats the data store at the values of the HashMap into
     * one single data structure.
     */
    public Object format(HashMap map, ParameterList paramList) throws Exception {
        Debug.debugMethodBegin( this, "format" );
        
        Parameter p = paramList.getParameter( WFSConstants.NAMESPACE );
        String s = "";
        if ( p != null ) s = (String)p.getValue();
        
        // create the basic gml document        
        StringBuffer gml = new StringBuffer(250000); 
        gml.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
        gml.append( "<gml:Collection xmlns:gml=\"http://www.opengis.net/gml\" " + s + ">");
        gml.append( "<gml:boundedBy><gml:Box><gml:coord><gml:X>-9E99</gml:X>" );
        gml.append( "<gml:Y>-9E99</gml:Y></gml:coord><gml:coord><gml:X>9E99</gml:X>" );
        gml.append( "<gml:Y>9E99</gml:Y></gml:coord></gml:Box></gml:boundedBy>" );
        
        Iterator iterator = map.values().iterator();
        
        // for each table within the received HashMap
        while ( iterator.hasNext() ) {
            
            ParameterList pl = (ParameterList)iterator.next();
            p = pl.getParameter( WFSConstants.TABLE );
            Table table = (Table)p.getValue();
            
            // for each row of each table a feature will be created and added to
            // the feature collection
            gml = tableToGML( table, pl, gml );
        }
        gml.append("</gml:Collection>");

        Document doc = XMLTools.parse( new StringReader( gml.toString() ) );
                
        // create the feature collection that will contain all requested
        // features (table rows)
        GMLFeatureCollection fc = new GMLFeatureCollection_Impl( doc.getDocumentElement() );
        
        fc.setBoundingBox( minx, miny, maxx, maxy );
        
        p = paramList.getParameter( WFSConstants.FILTER );
        if ( p != null ) {
            s = DOMPrinter.nodeToString( ((GMLFeatureCollection_Impl)fc).getAsElement(), "" );
            doc = xsltTransformGetFeature(s, (String)p.getValue() );
            fc = new GMLFeatureCollection_Impl( doc.getDocumentElement() );
        }
        
        Debug.debugMethodEnd();
        
        return fc;
    }
    
    /**
     * creates a <tt>GMLFeatureCollection</tt> from a <tt>Table</tt>. the
     * method is recursivly called to create complex features.
     */
    private StringBuffer tableToGML(Table table, ParameterList pl,
                                    StringBuffer gml) throws Exception {
        Debug.debugMethodBegin( this, "tableToGML" );
        
        ArrayList baseStack = new ArrayList();
        HashMap propNameCache = new HashMap();
                
        String tableName = table.getTableName();
        FeatureType ft = (FeatureType)pl.getParameter( WFSConstants.FEATURETYPE ).getValue();
        String[] columnNames = table.getColumnNames();
        TableDescription td = ft.getTableByName( tableName );

        // get id property name
        String idProp = td.getIdField();        
        // get coordinate reference system
        String crs = (String)pl.getParameter( WFSConstants.CRS ).getValue();
        
        for (int r = 0; r < table.getRowCount(); r++) {
            
            gml.append( "<gml:featureMember>" );            
            
            Object[] row = table.getRow( r );
            
            int cnt = 0;
            for (int i = 0; i < row.length; i++) {
                if ( row[i] != null ) {
                    cnt+= row[i].toString().length();
                }
            }
            
            StringBuffer feat = new StringBuffer(cnt);
            
            // clear register of already handled geo fields
            baseStack.clear();
                
            String id = "ID" + r;            
            // transform the current row of the table to a GMLFeature
            for (int i = 0; i < row.length; i++) {                
                                
                // get feature id
                if ( columnNames[i].equalsIgnoreCase( idProp ) ) {
                    id = row[i].toString();                               
                    try {
                        double d = Double.parseDouble( id );
                        id = "ID" + (int)d;
                    } catch (Exception ee) {
                        id = id.replace(' ','_');
                    }
                }
                
                String pn = null;
                if ( columnNames[i].equals( "COUNTCOUNT" ) ) {
                    pn = "_COUNT_";
                } else {                    
                    String s = ft.getPropertyFromAlias( columnNames[i] );
                    if ( s == null ) s = ft.getPropertyFromAlias( tableName + "." + columnNames[i] );
                    if ( s == null ) s = tableName + "." + columnNames[i];
                    pn = (String)propNameCache.get( columnNames[i] ); 
                    if ( pn == null ) {
                        pn = formatPropertyName( s );
                        propNameCache.put( columnNames[i], pn );
                    }
                }

                // get geometry column identfier
                GeoFieldIdentifier gfi = td.getGeoFieldIdentifier( columnNames[i] );
 
                if ( gfi == null ) {
                    
                    // create and add none-geometry properties to the feature
                    if ( row[i] != null && row[i] instanceof Table ) {                                                
                        // create complex property
                        StringBuffer gml_ = new StringBuffer( "<gml:Collection>" );
                        // recursive call of the method
                        gml_ = tableToGML( (Table)row[i], pl, gml_ );
                        gml_.append( "</gml:Collection>" );
                        feat.append( "<" + pn + ">" ).append( gml_.toString() );
                        feat.append( "</" + pn + ">" );
                    } else {                                   
                        if ( row[i] != null  ) {                            
                            feat.append( "<" + pn + ">" );
                            if ( row[i] != null ) {
                                feat.append( XMLTools.validateCDATA( row[i].toString() ) );
                            }
                            feat.append( "</" + pn + ">" );
                        } else {
                            // create an empty property                            
                            feat.append( "<" + pn + "/>" );
                        }
                    }                    
                } else {
                    String base = gfi.getDatastoreFieldBaseName();       
                    // check if geom field has already been handled
                    if ( !baseStack.contains( base ) ) {
                        
                        String s = ft.getPropertyFromAlias( base );
                        if ( s == null ) s = ft.getPropertyFromAlias( tableName + "." + base );
                        if ( s == null ) s = tableName + "." + base;
                        pn = (String)propNameCache.get( base ); 
                        if ( pn == null ) {
                            pn = formatPropertyName( s );
                            propNameCache.put( base, pn );
                        }
                        
                        baseStack.add( base );
                        int dim = gfi.getDimension();
                        // get x- and y-column indizes
                        int x = getColumnIndex( columnNames, base + "_X" );
                        int y = getColumnIndex( columnNames, base + "_Y" );
// not supported yet                        
//                        int z = -1;
//                        if ( dim == 3 ) z = getColumnIndex( columnNames, base + "_Z" );  
                        // create and add geometry property to the feature                         
                        if ( row[x] != null && row[y] != null ) {                            
                            updateBoundingBox( row[x].toString(), row[y].toString() );
                            String s_ = getGeoProperty( pn, row[x].toString(), row[y].toString(), crs );
                            feat.append( "<" + pn + ">" ).append( s_ );
                            feat.append( "</" + pn + ">" );
                        } else {
                            // create an empty property                            
                            feat.append( "<" + pn + "/>" );
                        }                       
                    }
                }

            }            
            
            gml.append( "<" + td.getTargetName() + " fid=\"" + id + "\">" );
            // add feature to the feature collection
            gml.append( feat );
            
            gml.append( "</" + td.getTargetName() + ">" );
            gml.append( "</gml:featureMember>" );            
        }
        
        Debug.debugMethodEnd();

        return gml;
    }        
    
   /**
    * the method formats a slash-seperated property name like /cities/ID
    * to a dot-seperated xml-valid name --> cities.ID
    */ 
    private String formatPropertyName(String property)
    {
        property = property.replace( '/', '.' );
        property = property.replace( '@', '.' );
        return StringExtend.validateString( property, "." );
    }
    
    /**
     * returns the index of the submitted name within the also submitted
     * array of column names
     */
    private int getColumnIndex(String[] columnNames, String name) {

        int pos = name.lastIndexOf('.');
        if ( pos > -1 ) {
            name = name.substring( pos+1, name.length() );
        }
        for (int i = 0; i < columnNames.length; i++) {
            if ( columnNames[i].equalsIgnoreCase( name ) ) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * creates a <tt>GMLProperty<tt> from its <tt>String<tt> representation.
     */
    private String getGeoProperty(String name, String x, String y, String crs ) throws Exception {
        
        StringBuffer sb = new StringBuffer(50);
        
        if ( crs != null && crs.indexOf( ":" ) > -1 ) {
            String[] cs = StringExtend.toArray( crs, ":", false );
            crs = "http://www.opengis.net/gml/srs/" + cs[0].toLowerCase() + ".xml#" + cs[1];
            sb.append( "<gml:Point srsName=\"" + crs + "\">" );
        } else {
            sb.append( "<gml:Point>" );
        }
        sb.append( "<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" );
        sb.append( x + "," + y );
        sb.append( "</gml:coordinates>" );
        sb.append( "</gml:Point>" );        
        
        return sb.toString();
        
    }
    
    /**
     * updates the bounding box of the <tt>GMLFeatureCollection<tt>.
     */
    private void updateBoundingBox(String xs, String ys) {
        double x = Double.parseDouble( xs );
        double y = Double.parseDouble( ys );
        
        if ( x < minx ) minx = x;
        if ( x > maxx ) maxx = x;
        
        if ( y < miny ) miny = y;
        if ( y > maxy ) maxy = y;
        
    }
    
    
    /**
     * transforms the response to a GetRecord/Feature request using
     * a predefined xslt-stylesheet
     */
    private Document xsltTransformGetFeature(String gml, String xsltURL) {
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
            Transformer transformer =
            tFactory.newTransformer( new StreamSource( url.openStream() ) );
            
            // Use the Transformer to apply the associated Templates object to an XML document
            // (foo.xml) and write the output to a file (foo.out).
            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult( sw ));
            
            document = XMLTools.parse( new StringReader( sw.toString() ) );
            
        } catch(Exception e) {
            Debug.debugException( e, "an error/fault body for the soap message will be created");
            //TODO exception
        }
        
        Debug.debugMethodEnd();
        return document;
    }
    
}
