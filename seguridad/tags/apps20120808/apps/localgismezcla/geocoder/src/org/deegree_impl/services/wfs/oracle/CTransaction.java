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
package org.deegree_impl.services.wfs.oracle;

import java.io.StringReader;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.deegree.gml.GMLCoord;
import org.deegree.gml.GMLCoordinates;
import org.deegree.gml.GMLFeature;
import org.deegree.gml.GMLGeometry;
import org.deegree.gml.GMLLineString;
import org.deegree.gml.GMLLinearRing;
import org.deegree.gml.GMLMultiLineString;
import org.deegree.gml.GMLMultiPoint;
import org.deegree.gml.GMLMultiPolygon;
import org.deegree.gml.GMLPoint;
import org.deegree.gml.GMLPolygon;
import org.deegree.gml.GMLProperty;
import org.deegree.model.table.Table;
import org.deegree.services.OGCWebServiceException;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.OGCWebServiceResponse;
import org.deegree.services.wfs.configuration.DatastoreConfiguration;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.protocol.WFSDelete;
import org.deegree.services.wfs.protocol.WFSInsert;
import org.deegree.services.wfs.protocol.WFSInsertResult;
import org.deegree.services.wfs.protocol.WFSOperation;
import org.deegree.services.wfs.protocol.WFSTransactionRequest;
import org.deegree.services.wfs.protocol.WFSUpdate;
import org.deegree.xml.XMLTools;
import org.deegree_impl.gml.GMLFactory;
import org.deegree_impl.io.DBConnectionPool;
import org.deegree_impl.io.OracleSpatialAccess;
import org.deegree_impl.io.SQLTools;
import org.deegree_impl.services.OGCWebServiceException_Impl;
import org.deegree_impl.services.wfs.InsertException;
import org.deegree_impl.services.wfs.InsertTree;
import org.deegree_impl.services.wfs.WFSMainLoop;
import org.deegree_impl.services.wfs.protocol.WFSProtocolFactory;
import org.deegree_impl.tools.Debug;
import org.deegree_impl.tools.StringExtend;
import org.w3c.dom.Document;


/**
 * class defining the processing of a transaction request
 */
class CTransaction extends WFSMainLoop {
    private DBConnectionPool pool = null;
    private DatastoreConfiguration config = null;

    /**
     * Creates a new CTransaction object.
     *
     * @param parent 
     * @param request 
     */
    public CTransaction( OracleDataStore parent, OGCWebServiceRequest request ) {
        super( parent, request );
        config = parent.getConfiguration();
        pool = parent.getConnectionPool();
    }

    /**
     *
     *
     * @param request 
     *
     * @return 
     */
    protected OGCWebServiceResponse[] performRequest( OGCWebServiceRequest request ) {
        Debug.debugMethodBegin( this, "performRequest" );

        OGCWebServiceResponse[] response = new OGCWebServiceResponse[1];

        WFSTransactionRequest tr = (WFSTransactionRequest)request;
        String handle = tr.getHandle();
        String lockId = tr.getLockId();
        String releaseAction = tr.getReleaseAction();
        WFSOperation[] operations = tr.getOperations();
        String[] aft = getAffectedFeatureTypes( operations );
        ArrayList inR = new ArrayList();
        int[] oI = new int[operations.length];

        try {
            // handle operations defined by the request
            for ( int i = 0; i < operations.length; i++ ) {
                if ( operations[i] instanceof WFSInsert ) {
                    Object[] o = performInsert( (WFSInsert)operations[i] );

                    if ( o[0] != null ) {
                        inR.add( o[0] );
                    }

                    // if this value equals 0 no expection raised during
                    // insertion. if the value is positiv some insertions
                    // failed. if the value is < 0 all insertions failed
                    oI[i] = ( (Integer)o[1] ).intValue();
                } else if ( operations[i] instanceof WFSUpdate ) {
                } else if ( operations[i] instanceof WFSDelete ) {
                } else {
                    // native request
                }
            }

            String status = null;
            boolean succ = true;
            boolean failed = true;

            for ( int i = 0; i < oI.length; i++ ) {
                if ( oI[i] != 0 ) {
                    succ = false;
                }

                if ( oI[i] > 0 ) {
                    failed = false;
                }
            }

            if ( succ ) {
                status = "SUCCESS";
            } else if ( failed ) {
                status = "FAILED";
            } else {
                status = "PARTIAL";
            }

            // create response object
            WFSInsertResult[] ir = new WFSInsertResult[inR.size()];
            ir = (WFSInsertResult[])inR.toArray( ir );
            response[0] = 
            	WFSProtocolFactory.createWFSTransactionResponse( request, aft, 
            													 null, ir, status, 
																 handle );
        } catch ( Exception e ) {
            Debug.debugException( e, null );

            OGCWebServiceException exce = 
                new OGCWebServiceException_Impl( "CTransaction: performRequest", 
                                                 StringExtend.stackTraceToString( e.getStackTrace() ) );
            response[0] = WFSProtocolFactory.createWFSGetFeatureResponse( request, aft, exce, null );
        }

        Debug.debugMethodEnd();
        return response;
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
                    if ( parent.isKnownFeatureType( ft[j] ) ) {
                        list.add( ft[j] );
                    }
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
    * performs an insertion of one or more features into the database
    */
    private Object[] performInsert( WFSInsert insert ) throws Exception {
        Debug.debugMethodBegin( this, "performInsert (WFSInsert)" );

        WFSInsertResult result = null;
        java.sql.Connection con = null;
        org.deegree.services.wfs.configuration.Connection connect = config.getConnection();

        OracleSpatialAccess osa = null;
        // database insert attempts
        int k1 = 0;
        // successful inserts
        int k2 = 0;

        try {
            // get jdbc connection and access object for oracle spatial
            con = pool.acuireConnection( connect.getDriver(), connect.getLogon(), connect.getUser(), 
                                         connect.getPassword() );
            osa = new OracleSpatialAccess( con, connect.getSpatialVersion() );
            osa.setAutoCommit( false );

            String[] ftNames = insert.getFeatureTypes();
            ArrayList idlist = new ArrayList();
            String handle = "";

            for ( int i = 0; i < ftNames.length; i++ ) {
                String ftName = ftNames[i];

                if ( parent.isKnownFeatureType( ftName ) ) {
                    org.deegree.services.wfs.configuration.FeatureType featureType = 
                            config.getFeatureType( ftName );
                    GMLFeature[] gmlFeatures = insert.getFeatures( ftName );

                    for ( int j = 0; j < gmlFeatures.length; j++ ) {
                        GMLFeature gmlFeature = gmlFeatures[j];
                        GMLProperty[] gmlProperties = gmlFeature.getProperties();

                        try {
                            k1++;

                            InsertTree tree = InsertTree.buildFromGMLFeature( gmlFeature, 
                                                                              featureType );
                            performInsert( tree, osa );
                            k2++;
                        } catch ( Exception e ) {
                            Debug.debugException( e, "" );

                            // rollback if insertion failed
                            osa.rollback();

                            // store/collect error message(s) as handle that will be
                            // submitted to the WFSInsertResult
                            handle += ( e.getMessage() + "\n" );
                        }

                        osa.commit();
                    }
                }
            }

            String[] ids = (String[])idlist.toArray( new String[idlist.size()] );
            result = WFSProtocolFactory.createWFSInsertResult( handle, ids );
        } catch ( SQLException e ) {
            if ( osa != null ) {
                osa.rollback();
            }

            Debug.debugException( e, "" );
        }

        if ( pool != null ) {
            pool.releaseConnection( con, connect.getDriver(), connect.getLogon(), connect.getUser(), 
                                    connect.getPassword() );
        }

        // calculate success status
        // 0 = success
        // > 0 = failed partially
        // < 0 = failed completely
        int k = k1 - k2;

        if ( k2 == 0 ) {
            k *= -1;
        }

        Debug.debugMethodEnd();
        return new Object[] { result, new Integer( k ) };
    }

    /**
     *
     *
     * @param tree 
     * @param osa 
     *
     * @return 
     *
     * @throws Exception 
     * @throws InsertException 
     */
    public ArrayList performInsert( InsertTree tree, OracleSpatialAccess osa )
                            throws Exception {
        Debug.debugMethodBegin( this, "performInsert (InsertTree, DBAccess)" );

        String dbVendor = osa.getDataBaseVendor().toUpperCase();
        Iterator it = tree.getSons().iterator();
        Reference[] refs = tree.getTableDescription().getReferences();

        // descend into the connected subtrees
        while ( it.hasNext() ) {
            InsertTree subTree = (InsertTree)it.next();
            ArrayList foreignKey = performInsert( subTree, osa );

            if ( ( foreignKey == null ) || ( foreignKey.size() == 0 ) ) {
                break;
            }

            // inherited foreignKey is only used if subTree is not a 
            // jointable AND the current table does not have an explicit
            // value for the foreignKey-field
            if ( ( refs != null ) && !subTree.isJoinTable() ) {
                for ( int i = 0; i < refs.length; i++ ) {
                    if ( refs[i].getTargetTable()
                                .equalsIgnoreCase( subTree.getTableDescription().getName() ) ) {
                        if ( tree.getFieldValue( refs[i].getTableField() ) == null ) {
                            tree.putFieldValue( refs[i].getTableField(), foreignKey );
                        }

                        break;
                    }
                }
            }
        }

        // now insert the data contained in the fields of the current
        // InsertTree-object (as these should be fields of the table)
        String table = tree.getTableDescription().getName().toUpperCase();
        String idField = tree.getTableDescription().getIdField().toUpperCase();
        ArrayList idValues = (ArrayList)tree.getFieldValue( idField );
        HashMap columnTypes = osa.getColumnTypesAsInt( table, null );

        if ( columnTypes == null ) {
            throw new InsertException( "Table '" + table + "' has no columnTypes?!" );
        }

        // if the current table has no values for fields at all, do nothing
        if ( tree.isFieldsEmpty() ) {
            return null;
        }

        for ( int ii = 0; ii < idValues.size(); ii++ ) {
            //
            // STEP a: SELECT the data (check for an identical row)
            //
            it = tree.getFieldsKeys();

            StringBuffer select = new StringBuffer( "SELECT * FROM " ).append( table )
                                                                      .append( " WHERE " );

            if ( tree.getTableDescription().isIdFieldNumber() ) {
                select.append( idField ).append( "= " ).append( idValues.get( ii ) );
            } else {
                select.append( idField ).append( "= '" ).append( idValues.get( ii ) ).append( "'" );
            }

            Debug.debugSimpleMessage( select.toString() );

            Table result = osa.performTableQuery( select.toString(), 0, 1 );

            if ( ( result != null ) && ( result.getRowCount() > 0 ) ) {
                //Found identical row and leave the current loop turn
                continue;
            }

            //
            // STEP B: INSERT the data
            //
            StringBuffer fieldList = new StringBuffer( "(" );
            StringBuffer valueList = new StringBuffer( "VALUES(" );
            it = tree.getFieldsKeys();

            while ( it.hasNext() ) {
                String key = (String)it.next();
                ArrayList vals = (ArrayList)tree.getFieldValue( key );
                int type = tree.getFeatureType().getDatastoreFieldType( table + "." + key );

                if ( type == FeatureType.UNKNOWN ) {
                    // not defined in configuration-file, so ask from db
                    Integer typeFromDB = (Integer)columnTypes.get( key );

                    if ( typeFromDB == null ) {
                        throw new InsertException( "Table '" + table + "' has no field named '" + 
                                                   key + "'" );
                    }

                    type = typeFromDB.intValue();
                    if ( type == Types.STRUCT ) {
                        // deegree geometry type code
                        type = FeatureType.GEOMETRY;
                    }
                }

                fieldList.append( key );

                if ( vals.get( ii ) == null ) {
                    valueList.append( "NULL" );
                } else {
                    switch ( type ) {
                        case Types.BIGINT:
                        case Types.NUMERIC:
                        case Types.REAL:
                        case Types.SMALLINT:
                        case Types.TINYINT:
                        case Types.DOUBLE:
                        case Types.FLOAT:
                        case Types.INTEGER:
                        case Types.DECIMAL:
                        {
                            valueList.append( vals.get( ii ) );
                            break;
                        }
                        case Types.CHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR:
                        {
                            if ( ( (String)vals.get( ii ) ).length() == 0 ) {
                                valueList.append( "NULL" );
                            } else {
                                valueList.append( "'" )
                                         .append( (String)vals.get( ii ) ) 
                                         .append( "'" );
                            }

                            break;
                        }
                        case Types.DATE:
                        case Types.TIME:
                        case Types.TIMESTAMP:
                        {
                            valueList.append( SQLTools.formatDate( dbVendor, (String)vals.get( ii ) ) );
                            break;
                        }
                        case org.deegree.services.wfs.configuration.FeatureType.GEOMETRY:
                        {
                            if ( ( (String)vals.get( ii ) ).length() == 0 ) {
                                valueList.append( "NULL" );
                            } else {
                                valueList.append( getGeomInsert( (String)vals.get( ii ) ) );
                            }

                            break;
                        }
                        default:
                            throw new InsertException( table + "." + key + " has invalid type: " + 
                                                       type );
                    }
                }

                if ( it.hasNext() ) {
                    fieldList.append( "," );
                    valueList.append( "," );
                }
            }

            fieldList.append( ")" );
            valueList.append( ")" );

            StringBuffer stmt = new StringBuffer( valueList.length() + 1000 ).append( 
                                        "INSERT INTO " ).append( table ).append( " " )
                                                                             .append( fieldList )
                                                                             .append( " " )
                                                                             .append( valueList );
            Debug.debugSimpleMessage( stmt.toString() );

            osa.performInsert( stmt.toString() );
        }

        Debug.debugMethodEnd();
        return idValues;
    }

    /**
    * returns the name of the database/table column extracted from
    * the name of a gml property
    */
    private String getColumnName( String propertyName ) {
        String[] s = StringExtend.toArray( propertyName, ".", false );
        return s[s.length - 1];
    }

    /**
    * generates a sql insert statement from the submitted feature
    * that inserts its values into a table thats name equals the
    * name of the features feature type.
    */
    private String getGeomInsert( String gml ) throws Exception {
        Debug.debugMethodBegin( this, "getGeomInsert" );

        Document doc = XMLTools.parse( new StringReader( gml ) );

        GMLGeometry geom = (GMLGeometry)GMLFactory.createGMLGeometry( doc.getDocumentElement() );

        String stmt = null;

        if ( geom instanceof GMLPoint ) {
            stmt = addPointProperty( (GMLPoint)geom );
        } else if ( geom instanceof GMLLineString ) {
            stmt = addLineStringProperty( (GMLLineString)geom );
        } else if ( geom instanceof GMLPolygon ) {
            stmt = addPolygonProperty( (GMLPolygon)geom );
        } else if ( geom instanceof GMLMultiPoint ) {
            stmt = addMultiPointProperty( (GMLMultiPoint)geom );
        } else if ( geom instanceof GMLMultiLineString ) {
            stmt = addMultiLineStringProperty( (GMLMultiLineString)geom );
        } else if ( geom instanceof GMLMultiPolygon ) {
            stmt = addMultiPolygonProperty( (GMLMultiPolygon)geom );
        }

        Debug.debugMethodEnd();
        return stmt;
    }

    /**
    * adds a point to the insert statement
    */
    private String addPointProperty( GMLPoint point ) {
        Debug.debugMethodBegin( this, "addPointProperty" );

        String r = "MDSYS.SDO_GEOMETRY ( 2001, null, MDSYS.SDO_POINT_TYPE (";
        // Coords are deprecated in GML3
        GMLCoord coord = point.getCoord();

        if ( coord != null ) {
            r += ( coord.getX() + "," + coord.getY() + ",null), null, null)" );
        } else {
            GMLCoordinates coordinates = point.getCoordinates();
            String s = coordinates.getCoordinates();
            String sep = "" + coordinates.getCoordinateSeperator();
            String[] vals = StringExtend.toArray( s, sep, false );
            r += ( vals[0] + "," + vals[1] + ",null), null, null)" );
        }

        Debug.debugMethodEnd();
        return r;
    }

    /**
    * adds a linestring to the insert statement
    */
    private String addLineStringProperty( GMLLineString line ) {
        Debug.debugMethodBegin( this, "addLineStringProperty" );

        String r = " MDSYS.SDO_GEOMETRY ( 2002, null, null," + 
                   " MDSYS.SDO_ELEM_INFO_ARRAY (1,2,1)," + " MDSYS.SDO_ORDINATE_ARRAY ( ";
        // Coords are deprecated in GML3
        GMLCoord[] coords = line.getCoords();

        if ( coords != null ) {
            for ( int i = 0; i < coords.length; i++ ) {
                r += ( coords[i].getX() + "," + coords[i].getY() + " " );
            }
        } else {
            GMLCoordinates coordinates = line.getCoordinates();
            String s = coordinates.getCoordinates();
            String sep = "" + coordinates.getCoordinateSeperator();
            String[] vals = StringExtend.toArray( s, sep, false );
            int k = 0;

            for ( int i = 0; i < ( vals.length / 2 ); i++ ) {
                r += ( vals[k] + "," + vals[k + 1] + " " );
                k += 2;
            }
        }

        r += "))";

        Debug.debugMethodEnd();
        return r;
    }

    /**
    * adds a polygon to the insert statement
    */
    private String addPolygonProperty( GMLPolygon poly ) {
        Debug.debugMethodBegin( this, "addPolygonProperty" );

        GMLLinearRing ex = poly.getExteriorRing();
        GMLLinearRing[] in = poly.getInteriorRings();

        String triple = "1,1003,1";

        String r = " MDSYS.SDO_GEOMETRY ( 2003, null, null," + 
                   " MDSYS.SDO_ELEM_INFO_ARRAY (XtripleX)," + " MDSYS.SDO_ORDINATE_ARRAY ( ";

        int pos = 0;
        // handle exterior ring
        GMLCoord[] coords = ex.getCoord();

        // Coords are deprecated in GML3
        if ( coords != null ) {
            for ( int i = 0; i < coords.length; i++ ) {
                r += ( coords[i].getX() + "," + coords[i].getY() );
                pos += 2;
                if(i != coords.length-1) {
                   r += ", ";
                   //r += " "; TODO: test what's correct and generic
               }
            }
        } else {
            GMLCoordinates coordinates = ex.getCoordinates();
            String s = coordinates.getCoordinates();
            String sep = "" + coordinates.getCoordinateSeperator();
            String[] vals = StringExtend.toArray( s, sep, false );
            int k = 0;

            for ( int i = 0; i < ( vals.length / 2 ); i++ ) {
                r += ( vals[k] + "," + vals[k + 1] + " " );
                k += 2;
                pos += 2;
            }
        }

        // handle interior rings if available
        if ( in != null ) {
            for ( int z = 0; z < in.length; z++ ) {
                triple += ( "," + ( pos + 1 ) + ",2003,1" );

                // Coords are deprecated in GML3
                coords = in[z].getCoord();

                if ( coords != null ) {
                    for ( int i = 0; i < coords.length; i++ ) {
                        r += ( coords[i].getX() + "," + coords[i].getY() + " " );
                        pos += 2;
                    }
                } else {
                    GMLCoordinates coordinates = in[z].getCoordinates();
                    String s = coordinates.getCoordinates();
                    String sep = "" + coordinates.getCoordinateSeperator();
                    String[] vals = StringExtend.toArray( s, sep, false );
                    int k = 0;

                    for ( int i = 0; i < ( vals.length / 2 ); i++ ) {
                        r += ( vals[k] + "," + vals[k + 1] + " " );
                        k += 2;
                        pos += 2;
                    }
                }
            }
        }

        r += "))";
        r = StringExtend.replace( r, "XtripleX", triple, false );

        Debug.debugMethodEnd();
        return r;
    }

    /**
    * adds a multipoint to the insert statement
    */
    private String addMultiPointProperty( GMLMultiPoint mpoint ) {
        Debug.debugMethodBegin( this, "addMultiPointProperty" );

        GMLPoint[] points = mpoint.getPoints();

        String r = " MDSYS.SDO_GEOMETRY ( 2001, null, null," + 
                   " MDSYS.SDO_ELEM_INFO_ARRAY (1,1," + points.length + ")," + 
                   " MDSYS.SDO_ORDINATE_ARRAY ( ";

        for ( int i = 0; i < points.length; i++ ) {
            // Coords are deprecated in GML3
            GMLCoord coord = points[i].getCoord();

            if ( coord != null ) {
                r += ( coord.getX() + "," + coord.getY() + " " );
            } else {
                GMLCoordinates coordinates = points[i].getCoordinates();
                String s = coordinates.getCoordinates();
                String sep = "" + coordinates.getCoordinateSeperator();
                String[] vals = StringExtend.toArray( s, sep, false );
                r += ( vals[0] + "," + vals[1] + " " );
            }
        }

        r += "))";

        Debug.debugMethodEnd();
        return r;
    }

    /**
    * adds a multilinestring to the insert statement
    */
    private String addMultiLineStringProperty( GMLMultiLineString mline ) {
        Debug.debugMethodBegin( this, "addMultiLineStringProperty" );

        String r = " MDSYS.SDO_GEOMETRY ( 2002, null, null," + 
                   " MDSYS.SDO_ELEM_INFO_ARRAY (XtripleX)," + " MDSYS.SDO_ORDINATE_ARRAY ( ";

        String triple = "";
        int pos = 0;
        GMLLineString[] lines = mline.getLineStrings();

        for ( int z = 0; z < lines.length; z++ ) {
            triple += ( "," + ( pos + 1 ) + ",2,1" );

            GMLCoord[] coords = lines[z].getCoords();

            // Coords are deprecated in GML3
            if ( coords != null ) {
                for ( int i = 0; i < coords.length; i++ ) {
                    r += ( coords[i].getX() + "," + coords[i].getY() + " " );
                    pos += 2;
                }
            } else {
                GMLCoordinates coordinates = lines[z].getCoordinates();
                String s = coordinates.getCoordinates();
                String sep = "" + coordinates.getCoordinateSeperator();
                String[] vals = StringExtend.toArray( s, sep, false );
                int k = 0;

                for ( int i = 0; i < ( vals.length / 2 ); i++ ) {
                    r += ( vals[k] + "," + vals[k + 1] + " " );
                    k += 2;
                    pos += 2;
                }
            }
        }

        r += "))";
        triple = StringExtend.validateString( triple, "," );
        r = StringExtend.replace( r, "XtripleX", triple, false );

        Debug.debugMethodEnd();
        return r;
    }

    /**
    * adds a multipolygon to the insert statement
    */
    private String addMultiPolygonProperty( GMLMultiPolygon mpoly ) {
        Debug.debugMethodBegin( this, "addMultiPolygonProperty" );

        String r = " MDSYS.SDO_GEOMETRY ( 2003, null, null," + 
                   " MDSYS.SDO_ELEM_INFO_ARRAY (XtripleX)," + " MDSYS.SDO_ORDINATE_ARRAY ( ";

        String triple = "1,1003,1";
        int pos = 0;
        GMLPolygon[] polys = mpoly.getPolygons();

        for ( int b = 0; b < polys.length; b++ ) {
            triple += ( "," + ( pos + 1 ) + ",1003,1" );

            GMLLinearRing ex = polys[b].getExteriorRing();
            GMLLinearRing[] in = polys[b].getInteriorRings();

            // handle exterior ring
            GMLCoord[] coords = ex.getCoord();

            // Coords are deprecated in GML3
            if ( coords != null ) {
                for ( int i = 0; i < coords.length; i++ ) {
                    r += ( coords[i].getX() + "," + coords[i].getY() + " " );
                    pos += 2;
                }
            } else {
                GMLCoordinates coordinates = ex.getCoordinates();
                String s = coordinates.getCoordinates();
                String sep = "" + coordinates.getCoordinateSeperator();
                String[] vals = StringExtend.toArray( s, sep, false );
                int k = 0;

                for ( int i = 0; i < ( vals.length / 2 ); i++ ) {
                    r += ( vals[k] + "," + vals[k + 1] + " " );
                    k += 2;
                    pos += 2;
                }
            }

            // handle interior rings if available
            if ( in != null ) {
                for ( int z = 0; z < in.length; z++ ) {
                    triple += ( "," + ( pos + 1 ) + ",2003,1" );
                    coords = in[z].getCoord();

                    // Coords are deprecated in GML3
                    if ( coords != null ) {
                        for ( int i = 0; i < coords.length; i++ ) {
                            r += ( coords[i].getX() + "," + coords[i].getY() + " " );
                            pos += 2;
                        }
                    } else {
                        GMLCoordinates coordinates = in[z].getCoordinates();
                        String s = coordinates.getCoordinates();
                        String sep = "" + coordinates.getCoordinateSeperator();
                        String[] vals = StringExtend.toArray( s, sep, false );
                        int k = 0;

                        for ( int i = 0; i < ( vals.length / 2 ); i++ ) {
                            r += ( vals[k] + "," + vals[k + 1] + " " );
                            k += 2;
                            pos += 2;
                        }
                    }
                }
            }
        }

        r += "))";
        triple = StringExtend.validateString( triple, "," );
        r = StringExtend.replace( r, "XtripleX", triple, false );

        Debug.debugMethodEnd();
        return r;
    }
}