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

import java.util.HashMap;

import org.deegree.gml.GMLBox;
import org.deegree.model.geometry.GM_Envelope;
import org.deegree.model.geometry.GM_Position;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree_impl.model.geometry.GMLAdapter;


/**
 * 
 *
 * @version $Revision: 1.1 $
 * @author <a href="mailto:wanhoff@giub.uni-bonn.de">Jeronimo Wanhoff</a> 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 */
public class MySQLSQLBuilder extends AbstractSQLBuilder {
    private HashMap propertyToColumn = null;

    /**
     * Creates a new PostGISSQLBuilder object.
     *
     * @param ft 
     */
    public MySQLSQLBuilder(FeatureType ft) {
        super( ft );
    }

    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer spatialOperation2SQL( SpatialOperation operation )
                                      throws Exception {
        StringBuffer sb = new StringBuffer( 2000 );

        switch ( operation.getOperatorId() ) {
            case OperationDefines.EQUALS:
            case OperationDefines.DISJOINT:
            case OperationDefines.INTERSECTS:
            case OperationDefines.TOUCHES:
            case OperationDefines.CROSSES:
            case OperationDefines.WITHIN:
            case OperationDefines.CONTAINS:
            case OperationDefines.OVERLAPS:
            case OperationDefines.BEYOND:
            {
                sb.append( "Spatial operator" )
                  .append( OperationDefines.getNameById( operation.getOperatorId() ) )
                  .append( " not implemented!" );
                break;
            }
            case OperationDefines.BBOX:
            {
                GM_Envelope box = GMLAdapter.createGM_Envelope( (GMLBox)operation.getGeometry() );
                GM_Position min = box.getMin();
                GM_Position max = box.getMax();

                double minx = min.getX();
                double miny = min.getY();
                double maxx = max.getX();
                double maxy = max.getY();

                double tmp = 0;

                if ( minx > maxx ) {
                    tmp = minx;
                    minx = maxx;
                    maxx = tmp;
                }

                if ( miny > maxy ) {
                    tmp = miny;
                    miny = maxy;
                    maxy = tmp;
                }

                String field = propertyName2SQL( operation.getPropertyName() ).toString();

                //field = ft.getDatastoreField( field )[0];
                field = ft.getDatastoreField( operation.getPropertyName().getValue() )[0];
                field = field.substring( 7, field.length()-1 );
                sb.append("( ");                
                sb.append( "Intersects( " + field + "," ); 
                sb.append( "GeomFromText('Polygon((" + minx + " " +miny + "," );
                sb.append( minx + " " + maxy + "," + maxx + " " + maxy + "," );
                sb.append( maxx + " " + miny + "," + minx + " " + miny + "))')" );
                sb.append(" ) )");
                break;
            }
            default:
            {
            }
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