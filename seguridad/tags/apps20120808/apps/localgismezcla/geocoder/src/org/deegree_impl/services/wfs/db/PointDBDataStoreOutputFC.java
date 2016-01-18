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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.feature.FeatureTypeProperty;
import org.deegree.model.geometry.GM_Object;
import org.deegree.model.table.Table;
import org.deegree.services.wfs.DataStoreOutputFormat;
import org.deegree.services.wfs.WFSConstants;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.GeoFieldIdentifier;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree.tools.Parameter;
import org.deegree.tools.ParameterList;
import org.deegree_impl.model.cs.Adapters;
import org.deegree_impl.model.cs.ConvenienceCSFactory;
import org.deegree_impl.model.cs.CoordinateSystem;
import org.deegree_impl.model.feature.FeatureFactory;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.deegree_impl.tools.Debug;
import org.opengis.cs.CS_CoordinateSystem;


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
public class PointDBDataStoreOutputFC implements DataStoreOutputFormat {
    ConvenienceCSFactory csFactory = ConvenienceCSFactory.getInstance();
    GeometryFactory geoFactory = new GeometryFactory();

    /**
     * formats the data store at the values of the HashMap into
     * one single data structure.
     */
    public Object format( HashMap map, ParameterList parameter ) throws Exception {
        Debug.debugMethodBegin( this, "format" );

        // calculate total number of features
        Iterator iterator = map.values().iterator();
        int initcap = 0;

        while ( iterator.hasNext() ) {
            ParameterList pl = (ParameterList)iterator.next();
            Parameter p = pl.getParameter( WFSConstants.TABLE );
            Table table = (Table)p.getValue();
            initcap += table.getRowCount();
        }

        FeatureFactory factory = new FeatureFactory();

        // create the feature collection that will contain all requested
        // features (table rows)
        FeatureCollection fc = factory.createFeatureCollection( "" + map.hashCode(), initcap );

        iterator = map.values().iterator();

        // for each table within the received HashMap
        while ( iterator.hasNext() ) {
            ParameterList pl = (ParameterList)iterator.next();
            Parameter p = pl.getParameter( WFSConstants.TABLE );
            Table table = (Table)p.getValue();
            fc = tableToFC( table, pl, fc );
        }

        Debug.debugMethodEnd();

        return fc;
    }

    /**
     * creates a <tt>FeatureCollection</tt> from a <tt>Table</tt>. the
     * method is recursivly called to create complex features.
     */
    private FeatureCollection tableToFC( Table table, ParameterList pl, FeatureCollection fc )
                                 throws Exception {
        Debug.debugMethodBegin();

        ArrayList baseStack = new ArrayList();
                
        String tableName = table.getTableName();
        FeatureType ft = (FeatureType)pl.getParameter( WFSConstants.FEATURETYPE ).getValue();
        String[] columnNames = table.getColumnNames();
        TableDescription td = ft.getTableByName( tableName );

        // get id property name
        String idProp = td.getIdField();        
        // get coordinate reference system
        String crs = (String)pl.getParameter( WFSConstants.CRS ).getValue();

        FeatureFactory factory = new FeatureFactory();

        org.deegree.model.feature.FeatureType ftype = createFeatureType( ft, table );
        String idBase = td.getTargetName();

        FeatureProperty[] fp = new FeatureProperty[ftype.getProperties().length];

        // for each row a feature will be created and added to
        // the feature collection
        for ( int r = 0; r < table.getRowCount(); r++ ) {
            baseStack.clear();

            String id = "" + r;
            Object[] row = table.getRow( r );
            int k = 0;

            for ( int i = 0; i < row.length; i++ ) {
                
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
                    pn = s;
                }
            
                // get geometry column identfier
                GeoFieldIdentifier gfi = td.getGeoFieldIdentifier( columnNames[i] );

                if ( gfi == null ) {
                    if ( row[i] instanceof Table ) {
                        // create and add complex property to the feature
                        TableDescription td_ = ft.getTableByName( ( (Table)row[i] ).getTableName() );
                        FeatureCollection fc_ = factory.createFeatureCollection( 
                                                        td_.getTargetName(), 1000 );
                        fc_ = tableToFC( (Table)row[i], pl, fc_ );
                        fp[k++] = factory.createFeatureProperty( pn, fc_ );
                    } else {
                        // create and add none-geometry property to the feature
                        fp[k++] = factory.createFeatureProperty( pn, row[i] );
                    }
                } else {
                    String base = gfi.getDatastoreFieldBaseName();

                    // check if geom field has already been handled
                    if ( !baseStack.contains( base ) ) {
                        baseStack.add( base );
                        
                        String s = ft.getPropertyFromAlias( base );
                        if ( s == null ) {
                            s = ft.getPropertyFromAlias( table.getTableName() + "." + base );
                        }
                        if ( s == null ) {
                            s = table.getTableName() + "." + base;
                        }

                        pn = s;

                        int dim = gfi.getDimension();
                        // get x- and y-column indizes
                        int x = getColumnIndex( columnNames, base + "_X" );
                        int y = getColumnIndex( columnNames, base + "_Y" );

                        // not supported yet
                        // int z = -1;
                        // if ( dim == 3 ) z = getColumnIndex( columnNames, base + "_Z" );
                        // create and add geometry property to the feature
                        if ( ( row[x] != null ) && ( row[y] != null ) ) {
                            GM_Object geo = getGeometry( row[x].toString(), row[y].toString(), crs );
                            fp[fp.length - 1] = factory.createFeatureProperty( pn, geo );
                        } else {
                            fp[fp.length - 1] = factory.createFeatureProperty( pn, null );
                        }
                    }
                }
            }

            // add feature to the feature collection
            Feature feature = factory.createFeature( id, ftype, fp );
            fc.appendFeature( feature );
        }

        Debug.debugMethodEnd();

        return fc;
    }

    /**
     * returns the feature type calculated from the column names and
     * column types of the submitted table
     */
    private org.deegree.model.feature.FeatureType createFeatureType( FeatureType ft, Table table ) {

        String[] columnNames = table.getColumnNames();
        String[] columnTypes = table.getColumnTypes();

        TableDescription td = ft.getTableByName( table.getTableName() );
        GeoFieldIdentifier[] gfis = td.getGeoFieldIdentifier();
    
        FeatureFactory factory = new FeatureFactory();
        FeatureTypeProperty[] ftp = null;

        int t = 0;

        for ( int i = 0; i < gfis.length/gfis[i].getDimension(); i++ ) {
            t++;
        }

        // feature type without geo property
        ftp = new FeatureTypeProperty[columnTypes.length - t];

        int k = 0;
        ArrayList baseStack = new ArrayList();
        int cc = 0;

        for ( int i = 0; i < columnNames.length; i++ ) {
            String pn = columnNames[i];

            if ( pn == null ) {
                continue;
            }

            if ( columnNames[i].equals( "COUNTCOUNT" ) ) {
                // if a count(*) statement has been performed set the property
                // name to "_COUNT_"
                pn = "_COUNT_";
            } else {
                String s = ft.getPropertyFromAlias( columnNames[i] );
                if ( s == null ) {
                    s = ft.getPropertyFromAlias( table.getTableName() + "." + columnNames[i] );
                }
                if ( s == null ) {
                    s = table.getTableName() + "." + columnNames[i];
                }
                pn = s;
            }
            
            GeoFieldIdentifier gfi = td.getGeoFieldIdentifier( columnNames[i] );
            if ( gfi == null ) {
                if ( columnTypes[i].equals( "org.deegree.model.table.Table" ) ) {
                    // if the current column is complex (type == table) create
                    // a FeatureTypeProperty with type == FeatureCollection
                    ftp[k++] = factory.createFeatureTypeProperty( pn, 
                                                                  "org.deegree.model.feature.FeatureCollection", 
                                                                  true );
                } else {
                    // create a FeatureTypeProperty with type of the current
                    // column
                    ftp[k++] = factory.createFeatureTypeProperty( pn, columnTypes[i], true );
                }
            } else {
                
                // create a geometry feature type
                String base = gfi.getDatastoreFieldBaseName();      
                String s = ft.getPropertyFromAlias( base );
                if ( s == null ) {
                    s = ft.getPropertyFromAlias( table.getTableName() + "." + base );
                }
                if ( s == null ) {
                    s = table.getTableName() + "." + base;
                }
                pn = s;     

                // check if geom field has already been handled
                if ( !baseStack.contains( base ) ) {
                    baseStack.add( base );   
                    // create a FeatureTypeProperty with type of the current
                    // column
                    ftp[k++] = factory.createFeatureTypeProperty( pn, "org.deegree.model.geometry.GM_Object", true );
                }
            }
        }

        return factory.createFeatureType( null, null, table.getTableName(), ftp );
    }

    /**
     * returns the index of the column with the submitted name. if the
     * column isn't contained within the columnNames array -1 will be
     * returned
     */
    private int getColumnIndex( String[] columnNames, String name ) {
        int pos = name.lastIndexOf( '.' );

        if ( pos > -1 ) {
            name = name.substring( pos + 1, name.length() );
        }

        for ( int i = 0; i < columnNames.length; i++ ) {
            if ( columnNames[i].equalsIgnoreCase( name ) ) {
                return i;
            }
        }

        return -1;
    }

    /**
     * returns a <tt>GM_Point</tt> object created from the submitted
     * x, y value and coordinate reference system.
     */
    private GM_Object getGeometry( String x, String y, String crs ) {
        CS_CoordinateSystem cs = null;

        try {
            CoordinateSystem cs_ = csFactory.getCSByName( crs );
            cs = Adapters.getDefault().export( cs_ );
        } catch ( Exception e ) {
            System.out.println( e );
        }

        return geoFactory.createGM_Point( Double.parseDouble( x ), Double.parseDouble( y ), cs );
    }
}