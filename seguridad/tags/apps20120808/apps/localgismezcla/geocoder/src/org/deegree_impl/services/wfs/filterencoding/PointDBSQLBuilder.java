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

import org.deegree.model.geometry.GM_Position;
import org.deegree.services.wfs.configuration.FeatureType;

/**
 * Generation of SQL-fragments from a Filter object for DBs containing
 * point geometries
 * FIXME:
 *   - complete configurability of the Builder
 *   - add support for missing spatial operations
 * @author Markus Schneider
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version 10.08.2002
 * @see Filter
 */
public class PointDBSQLBuilder extends AbstractSQLBuilder {
    
    public PointDBSQLBuilder(FeatureType ft) {
        super( ft );
    }
    
    /**
     * Generates a SQL-fragment for the given object.
     */
    public StringBuffer spatialOperation2SQL(SpatialOperation operation) throws Exception {
        
        StringBuffer sb = new StringBuffer(1000);
        
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
                GM_Position min = operation.getBoundingBox().getMin();
                GM_Position max = operation.getBoundingBox().getMax();
                
//                double minx = Math.round(min.getX()*100)/100d;
//                double miny = Math.round(min.getY()*100)/100d;
//                double maxx = Math.round(max.getX()*100)/100d;
//                double maxy = Math.round(max.getY()*100)/100d;
                
                double minx = Math.round(min.getX());
                double miny = Math.round(min.getY());
                double maxx = Math.round(max.getX());
                double maxy = Math.round(max.getY());
                
                double tmp = 0;
                if (minx > maxx) {
                    tmp = minx; minx = maxx; maxx = tmp;
                }
                if (miny > maxy) {
                    tmp = miny; miny = maxy; maxy = tmp;
                }
                String field = propertyName2SQL( operation.getPropertyName() ).toString();
                field = field.substring( 1, field.length() );
                String x = field + "_X"; 
                String y = field + "_Y";
                
                if ( not ) {
                    sb.append( " " + x + " < " + minx );
                    sb.append( " OR " + x + " > " + maxx );
                    sb.append( " OR " + y + " < " + miny );
                    sb.append( " OR " + y + " > " + maxy  );
                } else {
                    sb.append( " " + x + " >= " + minx );
                    sb.append( " AND " + x + " <= " + maxx );
                    sb.append( " AND " + y + " >= " + miny );
                    sb.append( " AND " + y + " <= " + maxy  );
                }
                
                break;
            }
            default: {
            }
        }
        return sb;
    }
    
   /** abstract method that have to be implemented by extending classes to format
     * a database vendor specific time
     *
     */
    public String formatTime(String time) {
        return time;
    }
    
    /** abstract method that have to be implemented by extending classes to format
     * a database vendor specific timestamp
     *
     */
    public String formatTimestamp(String time) {
        return time;
    }
    
    /** abstract method that have to be implemented by extending classes to format
     * a database vendor specific date
     *
     */
    public String formatDate(String date) {
        return date;
    }
    
}
