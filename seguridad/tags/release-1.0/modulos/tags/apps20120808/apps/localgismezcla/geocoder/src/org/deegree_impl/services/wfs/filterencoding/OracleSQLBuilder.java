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
package org.deegree_impl.services.wfs.filterencoding;

import org.deegree.gml.GMLBox;
import org.deegree.gml.GMLCoord;
import org.deegree.gml.GMLCoordinates;
import org.deegree.gml.GMLGeometry;
import org.deegree.gml.GMLLineString;
import org.deegree.gml.GMLLinearRing;
import org.deegree.gml.GMLMultiLineString;
import org.deegree.gml.GMLMultiPoint;
import org.deegree.gml.GMLMultiPolygon;
import org.deegree.gml.GMLPoint;
import org.deegree.gml.GMLPolygon;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree_impl.tools.StringExtend;


/**
 * 
 *
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:luigimarinucci@yahoo.com">Luigi Marinucci<a>
 * @version 5.5.2003
 */
public class OracleSQLBuilder extends AbstractSQLBuilder {

    private String SRID = null;

    private boolean indexed;

    /**
     * Creates a new OracleSQLBuilder object.
     *
     * @param ft 
     * @param indexed 
     */
    public OracleSQLBuilder( FeatureType ft, boolean indexed ) {
        super( ft );
        this.indexed = indexed;
        SRID = ft.getInternalCRS();
    }

    /**
     * Genera un frammento SQL per un dato oggetto.
     */
    public StringBuffer spatialOperation2SQL( SpatialOperation operation )
                                      throws Exception {
        StringBuffer sb = new StringBuffer( 1000 );

        switch ( operation.getOperatorId() ) {
            case OperationDefines.BEYOND:
            {
                sb.append( "Spatial operator" )
                  .append( OperationDefines.getNameById( operation.getOperatorId() ) )
                  .append( " not implemented!" );
                break;
            }
            case OperationDefines.CROSSES:
            {
                sb = getCROSSES( operation, sb );
                break;
            }
            case OperationDefines.EQUALS:
            {
                sb = getEQUALS( operation, sb );
                break;
            }
            case OperationDefines.WITHIN:
            {
                sb = getWITHIN( operation, sb );
                break;
            }
            case OperationDefines.OVERLAPS:
            {
                sb = getOVERLAPS( operation, sb );
                break;
            }
            case OperationDefines.TOUCHES:
            {
                sb = getTOUCHES( operation, sb );
                break;
            }
            case OperationDefines.DISJOINT:
            {
                sb = getDISJOINT( operation, sb );
                break;
            }
            case OperationDefines.INTERSECTS:
            {
                sb = getINTERSECTS( operation, sb );
                break;
            }
            case OperationDefines.CONTAINS:
            {
                sb = getCONTAINS( operation, sb );
                break;
            }            
            case OperationDefines.DWITHIN :
            {
              sb=getDWITHIN(operation, sb) ;
              break;
            }
            case OperationDefines.BBOX:
            {
                sb = getBBOX( operation, sb );
                break;
            }
            default:
            {
            }
        }

        return sb;
    }

    /**
     *
     *
     * @param geom 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer GMLGeometryToSDO( GMLGeometry geom, StringBuffer sb )
                                   throws Exception {
        if ( geom instanceof GMLPoint ) {
            GMLCoordinates point = ( (GMLPoint)geom ).getCoordinates();
            String s = point.getCoordinates();
            String sep = "" + point.getCoordinateSeperator();
            String[] vals = StringExtend.toArray( s, sep, false );

            String r = " MDSYS.SDO_GEOMETRY ( 2001," + SRID + ", MDSYS.SDO_POINT_TYPE( " + vals[0] + 
                       "," + vals[1] + ",null),null,null)";
            sb.append( r );
        }

        if ( geom instanceof GMLBox ) {
            GMLCoord min = ( (GMLBox)geom ).getMin();
            GMLCoord max = ( (GMLBox)geom ).getMax();

            double minx = min.getX();
            double miny = min.getY();
            double maxx = max.getX();
            double maxy = max.getY();
            sb.append( "mdsys.sdo_geometry (2003," + SRID + ", null, " )
// Clare              .append( "mdsys.sdo_elem_info_array (1,1003,3)," )
              .append( "mdsys.sdo_elem_info_array (1,1003,1)," )
              .append( "mdsys.sdo_ordinate_array (" )
// Clare              .append( minx + "," + miny + "," + maxx + "," + maxy + "))" );
              .append( minx+","+miny+"," + maxx+","+miny+"," + maxx+","+maxy+","
                     + minx+","+maxy+"," + minx+","+miny + "))" );
        }

        if ( geom instanceof GMLLineString ) {
            String r = " MDSYS.SDO_GEOMETRY ( 2002," + SRID + ", null," + 
                       " MDSYS.SDO_ELEM_INFO_ARRAY (1,2,1)," + " MDSYS.SDO_ORDINATE_ARRAY ( ";

            GMLCoord[] coords = ( (GMLLineString)geom ).getCoords();

            if ( coords != null ) {
                for ( int i = 0; i < coords.length; i++ ) {
                    r += ( coords[i].getX() + "," + coords[i].getY() + " " );
                }
            } else {
                GMLCoordinates coordinates = ( (GMLLineString)geom ).getCoordinates();
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
        }

        if ( geom instanceof GMLMultiLineString ) {
            String r = " MDSYS.SDO_GEOMETRY ( 2002," + SRID + ", null," + 
                       " MDSYS.SDO_ELEM_INFO_ARRAY (XtripleX)," + " MDSYS.SDO_ORDINATE_ARRAY ( ";

            String triple = "";
            int pos = 0;
            GMLLineString[] lines = ( (GMLMultiLineString)geom ).getLineStrings();

            for ( int z = 0; z < lines.length; z++ ) {
                triple += ( "," + ( pos + 1 ) + ",2,1" );

                GMLCoord[] coords = lines[z].getCoords();

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
        }

        if ( geom instanceof GMLMultiPoint ) {
            GMLPoint[] points = ( (GMLMultiPoint)geom ).getPoints();

            String r = " MDSYS.SDO_GEOMETRY ( 2001," + SRID + ", null," + 
                       " MDSYS.SDO_ELEM_INFO_ARRAY (1,1," + points.length + ")," + 
                       " MDSYS.SDO_ORDINATE_ARRAY ( ";

            for ( int i = 0; i < points.length; i++ ) {
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
        }

        if ( geom instanceof GMLPolygon ) {
            GMLLinearRing ex = ( (GMLPolygon)geom ).getExteriorRing();
            GMLLinearRing[] in = ( (GMLPolygon)geom ).getInteriorRings();

            String triple = "1,1003,1";

            String r = " MDSYS.SDO_GEOMETRY ( 2003," + SRID + ", null," + 
                       " MDSYS.SDO_ELEM_INFO_ARRAY (XtripleX)," + " MDSYS.SDO_ORDINATE_ARRAY ( ";

            int pos = 0;
            // Gestione dei bordi esterni
            GMLCoord[] coords = ex.getCoord();

            if ( coords != null ) {
                for ( int i = 0; i < coords.length; i++ ) {
                    r += ( coords[i].getX() + "," + coords[i].getY() + " " );
                    pos += 2;
                }
            } else {
                GMLCoordinates coordinates = ex.getCoordinates();
                String s = coordinates.getCoordinates();
                //String sep = ""+coordinates.getCoordinateSeperator();
                String sep = "" + coordinates.getTupleSeperator();
                String[] vals = StringExtend.toArray( s, sep, false );
                int k = 0;

                for ( int i = 0; i < vals.length; i++ ) {
                    r += ( vals[k] + "," ); //+ vals[k+1] + " ";
                    k += 1;
                    pos += 2;
                }
            }

            // Gestione delle poligonali interne
            if ( in != null ) {
                for ( int z = 0; z < in.length; z++ ) {
                    triple += ( "," + ( pos + 1 ) + ",2003,1" );
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

            r = StringExtend.validateString( r, "," );
            r += "))";
            r = StringExtend.replace( r, "XtripleX", triple, false );
            sb.append( r );
        }

        if ( geom instanceof GMLMultiPolygon ) {

            String r = " MDSYS.SDO_GEOMETRY ( 2007," + SRID + ", null," + 
                       " MDSYS.SDO_ELEM_INFO_ARRAY (XtripleX)," + " MDSYS.SDO_ORDINATE_ARRAY ( ";

            String triple = "";
            int pos = 0;
            GMLPolygon[] polys = ( (GMLMultiPolygon)geom ).getPolygons();

            for ( int b = 0; b < polys.length; b++ ) {
                triple += ( "," + ( pos + 1 ) + ",1003,1" );

                GMLLinearRing ex = polys[b].getExteriorRing();
                GMLLinearRing[] in = polys[b].getInteriorRings();

                // Gestione dei bordi esterni
                GMLCoord[] coords = ex.getCoord();

                if ( coords != null ) {
                    for ( int i = 0; i < coords.length; i++ ) {
                        r += ( "," + coords[i].getX() + "," + coords[i].getY() + " " );
                        pos += 2;
                    }
                } else {
                    GMLCoordinates coordinates = ex.getCoordinates();
                    String s = coordinates.getCoordinates();
                    //String sep = ""+coordinates.getCoordinateSeperator();
                    String sep = "" + coordinates.getTupleSeperator();
                    String[] vals = StringExtend.toArray( s, sep, false );
                    int k = 0;

                    for ( int i = 0; i < vals.length; i++ ) {
                        r += ( vals[k] + "," );
                        k += 1;
                        pos += 2;
                    }
                }

                // Gestione delle poligonali interne
                if ( in != null ) {
                    for ( int z = 0; z < in.length; z++ ) {
                        triple += ( "," + ( pos + 1 ) + ",2003,1" );
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
            }

            r = StringExtend.validateString( r, "," );
            r += "))";
            triple = StringExtend.validateString( triple, "," );
            r = StringExtend.replace( r, "XtripleX", triple, false );
            sb.append( r );
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getCROSSES( SpatialOperation operation, StringBuffer sb )
                             throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'mask=OVERLAPBDYDISJOINT querytype=window') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getEQUALS( SpatialOperation operation, StringBuffer sb )
                            throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'mask=EQUAL querytype=window') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getWITHIN( SpatialOperation operation, StringBuffer sb )
                            throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'mask=INSIDE+COVEREDBY querytype=window') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getTOUCHES( SpatialOperation operation, StringBuffer sb )
                             throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'mask=TOUCH querytype=WINDOW') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getDISJOINT( SpatialOperation operation, StringBuffer sb )
                              throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'mask=DISJOINT querytype=windows') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getINTERSECTS( SpatialOperation operation, StringBuffer sb )
                                throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'mask= ANYINTERACT querytype=WINDOW') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getOVERLAPS( SpatialOperation operation, StringBuffer sb )
                              throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'mask=OVERLAPBDYINTERSECT querytype=window') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getCONTAINS( SpatialOperation operation, StringBuffer sb )
                              throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'mask=CONTAINS+COVERS querytype=WINDOW') = 'TRUE'" );
        } else {
        }

        return sb;
    }
    
    /**
     * calvin added on 10/23/2003
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getDWITHIN( SpatialOperation operation, StringBuffer sb )
                            throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );

        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_within_distance( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_within_distance( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );
            sb.append( ",'distance =" +operation.getDistance() +"') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /**
     *
     *
     * @param operation 
     * @param sb 
     *
     * @return 
     *
     * @throws Exception 
     */
    private StringBuffer getBBOX( SpatialOperation operation, StringBuffer sb )
                          throws Exception {
        String field = propertyName2SQL( operation.getPropertyName() ).toString();
        field = field.substring( 1, field.length() );
        if ( indexed ) {
            if ( not ) {
                sb.append( " not mdsys.sdo_relate ( " + field + "," );
            } else {
                sb.append( " mdsys.sdo_relate ( " + field + "," );
            }

            sb = GMLGeometryToSDO( operation.getGeometry(), sb );

            sb.append( ",'mask= ANYINTERACT querytype=WINDOW') = 'TRUE'" );
        } else {
        }

        return sb;
    }

    /** abstract method that have to be implemented by extending classes to format
     * a database vendor specific time
     *
     */
    public String formatTime( String time ) {
        return "{ t '" + time + "'}";
    }

    /** abstract method that have to be implemented by extending classes to format
     * a database vendor specific timestamp
     *
     */
    public String formatTimestamp( String timestamp ) {
        timestamp = timestamp.replace( 'T', ' ' );
        timestamp = timestamp.replace( 't', ' ' );
        return "{ ts '" + timestamp + "'}";
    }

    /** abstract method that have to be implemented by extending classes to format
     * a database vendor specific date
     *
     */
    public String formatDate( String date ) {
        return "{ d '" + date + "'}";
    }
}
