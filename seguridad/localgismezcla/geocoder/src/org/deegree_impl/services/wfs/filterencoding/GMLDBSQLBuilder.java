/*----------------    FILE HEADER  ------------------------------------------
 
This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de
 
This library is free software; you can redistribute it OR/AND
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, AND (at your option) any later version.
 
This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.
 
You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
Contact:
 
OR_reas Poth
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
 * Generation of SQL-fragments from a Filter object for DBs containing
 * geometries as GML geometries considering the deegree WFS assumptions
 * about naming the required columns.
 * FIXME:
 *   - complete configurability of the Builder
 *   - add support for missing spatial operations
 * @author Markus Schneider
 * @author <a href="mailto:poth@lat-lon.de">OR_reas Poth</a>
 * @version 10.08.2002
 * @see Filter
 */
public class GMLDBSQLBuilder extends AbstractSQLBuilder {
    
    private HashMap propertyToColumn = null;
    
    public GMLDBSQLBuilder(FeatureType ft) {
        super( ft );
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer spatialOperation2SQL(SpatialOperation operation) throws Exception {
        
        StringBuffer sb = new StringBuffer(2000);
        
        switch (operation.getOperatorId()) {
            case OperationDefines.EQUALS:
            case OperationDefines.DISJOINT:
            case OperationDefines.INTERSECTS:
            case OperationDefines.TOUCHES:
            case OperationDefines.CROSSES:
            case OperationDefines.WITHIN: 
            case OperationDefines.CONTAINS:
            case OperationDefines.OVERLAPS:
            case OperationDefines.BEYOND: {
                sb.append("Spatial operator").
                append(OperationDefines.getNameById(operation.getOperatorId())).
                append(" not implemented!");
                break;
            }
            case OperationDefines.BBOX: {
                GM_Envelope box = GMLAdapter.createGM_Envelope ((GMLBox) operation.getGeometry());
                GM_Position min = box.getMin();
                GM_Position max = box.getMax();
                
                double minx = min.getX();
                double miny = min.getY();
                double maxx = max.getX();
                double maxy = max.getY();
                
                double tmp = 0;
                if (minx > maxx) {
                    tmp = minx; minx = maxx; maxx = tmp;
                }
                if (miny > maxy) {
                    tmp = miny; miny = maxy; maxy = tmp;
                }
                
                String q = propertyName2SQL( operation.getPropertyName() ).toString().trim();

                int pos = q.lastIndexOf( " " );
                if ( pos > 0 ) {
                    q = q.substring( 0, pos ) + " ";
                } else {
                    q = "";
                }                
                String field = ft.getDatastoreField( operation.getPropertyName().getValue() )[0];
                
//                String minx_c = field + "minx";
//                String miny_c = field + "minY";
//                String maxx_c = field + "maxX";
//                String maxy_c = field + "maxY";
                
//                String minx_c = "x1";
//                String miny_c = "y1";
//                String maxx_c = "x2";
//                String maxy_c = "y2";
           
                String minx_c = "minx";
                String miny_c = "minY";
                String maxx_c = "maxX";
                String maxy_c = "maxY";
//                
                sb.append( q );
               if ( q == "" ) {
                    sb.append("( ");
                }
                if ( not ) {
                    sb.append("( " + minx + " > " + minx_c +" OR " + miny + " > " +miny_c+ " OR ");
                    sb.append( maxx + " < " + maxx_c +" OR " + maxy + " < " +maxy_c+ ") ");
                    sb.append( " AND " );
                    sb.append("( " + minx + " < " + minx_c +" OR " + miny + " < " +miny_c+ " OR ");
                    sb.append( maxx + " > " + maxx_c +" OR " + maxy + " > " +maxy_c+ ") ");
                    sb.append( " AND " );                    
                    sb.append("( " + minx + " < " + minx_c +" OR " + minx + " >= " +maxx_c+ " OR ");
                    sb.append( miny + " > " + miny_c +" OR " + maxy + " <= " +miny_c+ ") ");
                    sb.append( " AND " );
                    sb.append("( " + minx + " < " + minx_c +" OR " + minx + " >= " +maxx_c+ " OR ");
                    sb.append( miny + " >= " + maxy_c +" OR " + maxy + " < " +maxy_c+ ") ");
                    sb.append( " AND " );
                    sb.append("( " + maxx + " <= " + minx_c +" OR " + maxx + " > " +maxx_c+ " OR ");
                    sb.append( miny + " > " + miny_c +" OR " + maxy + " <= " +miny_c+ ") ");
                    sb.append( " AND " );
                    sb.append("( " + maxx + " <= " + minx_c +" OR " + maxx + " > " +maxx_c+ " OR ");
                    sb.append( miny + " >= " + maxy_c +" OR " + maxy + " < " +maxy_c+ ") ");
                    sb.append( " AND " );
                    sb.append("( " + miny + " < " + miny_c +" OR " + miny + " >= " +maxy_c+ " OR ");
                    sb.append( minx + " > " + minx_c +" OR " + maxx + " <= " +minx_c+ ") ");
                    sb.append( " AND " );
                    sb.append("( " + miny + " < " + miny_c +" OR " + miny + " >= " +maxy_c+ " OR ");
                    sb.append( minx + " >= " + maxx_c +" OR " + maxx + " < " +maxx_c+ ") ");
                    sb.append( " AND " );
                    sb.append("( " + maxy + " <= " + miny_c +" OR " + maxy + " > " +maxy_c+ " OR ");
                    sb.append( minx + " > " + minx_c +" OR " + maxx + " <= " +minx_c+ ") ");
                    sb.append( " AND " );
                    sb.append("( " + maxy + " <= " + miny_c +" OR " + maxy + " > " +maxy_c+ " OR ");
                    sb.append( minx + " >= " + maxx_c +" OR " + maxx + " < " +maxx_c+ ") ");
                } else {
                    sb.append("( " + minx + " <= " + minx_c +" AND " + miny + " <= " +miny_c+ " AND ");
                    sb.append( maxx + " >= " + maxx_c +" AND " + maxy + " >= " +maxy_c+ ") ");
                    sb.append( " OR " );
                    sb.append("( " + minx + " >= " + minx_c +" AND " + miny + " >= " +miny_c+ " AND ");
                    sb.append( maxx + " <= " + maxx_c +" AND " + maxy + " <= " +maxy_c+ ") ");
                    sb.append( " OR " );                    
                    sb.append("( " + minx + " >= " + minx_c +" AND " + minx + " < " +maxx_c+ " AND ");
                    sb.append( miny + " <= " + miny_c +" AND " + maxy + " > " +miny_c+ ") ");
                    sb.append( " OR " );
                    sb.append("( " + minx + " >= " + minx_c +" AND " + minx + " < " +maxx_c+ " AND ");
                    sb.append( miny + " < " + maxy_c +" AND " + maxy + " >= " +maxy_c+ ") ");
                    sb.append( " OR " );
                    sb.append("( " + maxx + " > " + minx_c +" AND " + maxx + " <= " +maxx_c+ " AND ");
                    sb.append( miny + " <= " + miny_c +" AND " + maxy + " > " +miny_c+ ") ");
                    sb.append( " OR " );
                    sb.append("( " + maxx + " > " + minx_c +" AND " + maxx + " <= " +maxx_c+ " AND ");
                    sb.append( miny + " < " + maxy_c +" AND " + maxy + " >= " +maxy_c+ ") ");
                    sb.append( " OR " );
                    sb.append("( " + miny + " >= " + miny_c +" AND " + miny + " < " +maxy_c+ " AND ");
                    sb.append( minx + " <= " + minx_c +" AND " + maxx + " > " +minx_c+ ") ");
                    sb.append( " OR " );
                    sb.append("( " + miny + " >= " + miny_c +" AND " + miny + " < " +maxy_c+ " AND ");
                    sb.append( minx + " < " + maxx_c +" AND " + maxx + " >= " +maxx_c+ ") ");
                    sb.append( " OR " );
                    sb.append("( " + maxy + " > " + miny_c +" AND " + maxy + " <= " +maxy_c+ " AND ");
                    sb.append( minx + " <= " + minx_c +" AND " + maxx + " > " +minx_c+ ") ");
                    sb.append( " OR " );
                    sb.append("( " + maxy + " > " + miny_c +" AND " + maxy + " <= " +maxy_c+ " AND ");
                    sb.append( minx + " < " + maxx_c +" AND " + maxx + " >= " +maxx_c+ ") ");
                }
                
                sb.append(" ) ");
                
                break;
            }
            default: {
            } 
        }
        return sb;
    }
    
    /** format a database vendor specific time
     *
     */
    public String formatTime(String time) {        
        return "{ t '" + time + "'}";
    }
    
    /** format a database vendor specific timestamp
     *
     */
    public String formatTimestamp(String timestamp) {
        timestamp = timestamp.replace( 'T', ' ' );
        timestamp = timestamp.replace( 't', ' ' );
        return "{ ts '" + timestamp + "'}";
    }
    
    /** format a database vendor specific date
     *
     */
    public String formatDate(String date) {
        return "{ d '" + date + "'}";
    }
    
}
