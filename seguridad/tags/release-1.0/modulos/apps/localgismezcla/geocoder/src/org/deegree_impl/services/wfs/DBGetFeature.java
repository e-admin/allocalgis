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

package org.deegree_impl.services.wfs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.deegree.model.table.Table;
import org.deegree.services.OGCWebServiceRequest;
import org.deegree.services.wfs.configuration.Connection;
import org.deegree.services.wfs.configuration.FeatureType;
import org.deegree.services.wfs.configuration.Reference;
import org.deegree.services.wfs.configuration.TableDescription;
import org.deegree.services.wfs.protocol.WFSQuery;
import org.deegree_impl.io.DBAccess;
import org.deegree_impl.io.DBConnectionPool;
import org.deegree_impl.tools.Debug;

/**
 * class defining the basic processing of a getFeature request for databases
 * <p>-------------------------------------------------------------------------</p>
 *
 * @author Andreas Poth
 * @version $Revision: 1.1 $ $Date: 2009/07/09 07:25:32 $
 * <p>
 */
abstract public class DBGetFeature extends AbstractGetFeature {
    
    protected DBConnectionPool pool = null;
    protected int depth = 0;
    private static Map ct = null;
    
    // create a static register for database FK/PK field types
    static {
        if ( ct == null ) {
            ct = Collections.synchronizedMap( new HashMap(100) );
        }
    }
    
    protected DBGetFeature(AbstractDataStore parent, OGCWebServiceRequest request) {
        super( parent, request );
        pool = DBConnectionPool.getInstance();
        depth = 0;
    }
    
    protected void reset() {
        depth = 0;
    }
    
    /**
     * creates the relations resp. the complext data schema by replacing
     * the foreign key of the master table(s) with the related data.
     */
    protected Table getRelations(Table table, FeatureType featureType,
                                 WFSQuery query) throws Exception {
        Debug.debugMethodBegin( this, "getRelations");                

        String[] cNames = table.getColumnNames();
        String[] cTypes = table.getColumnTypes();
        
        TableDescription td = featureType.getTableByName( table.getTableName() );
        
        if ( td != null ) {
            // check for each field of the table if it's a foreign key.
            // if so, solve the relation and replace the fk with the
            // content it points to.
            for (int i = 0; i < cNames.length; i++) { 

                // if true, then the current field is a foreign key
                Reference[] ref = td.getReferences( cNames[i] );
                if ( ref != null ) { 

                    for (int z = 0; z < ref.length; z++) {
                        int colIdx = -1;                        
                        if ( depth < 3 ) {
                            if ( ref[z].isReplaceable() ) {
                                // if current column is a foreign key replace the datatype
                                // of the column temporary with Object
                                table.setColumnType( i, "java.lang.Object" );
                            }
                            String tt = ref[z].getTargetTable();
                            String tf = ref[z].getTargetField();
                            
                            for (int k = 0; k < table.getRowCount(); k++) {
                                Table tab = null;
                                java.sql.Connection con = null;
                                Connection connect = config.getConnection();
                                try {
                                    // get jdbc connection and access object for oracle spatial
                                    con = pool.acuireConnection( connect.getDriver(), connect.getLogon(),
                                                                 connect.getUser(), connect.getPassword() );
                                    DBAccess osa = new DBAccess( con );                                
                                    String q = "SELECT DISTINCT ";                                
                                    String s_ = getAffectedFields( tt, featureType, query );                                                        
                                    if ( s_.equals("-") ) continue;
                                    q += s_;
                                    q += " FROM " + tt + " WHERE " + tf + " = ";
                                    if ( ct.get( tt+"."+tf ) == null ) {                                        
                                        HashMap ctt = osa.getColumnTypes( tt, new String[]{tf} );
                                        // cache the columns data type
                                        ct.put( tt+"."+tf, ctt.get(tf) );
                                    } 
                                    if ( ct.get(tt+"."+tf).equals("java.lang.String") ) {
                                        q += "'" + table.getValueAt( k, i ) + "'";
                                    } else {
                                        q += table.getValueAt( k, i );
                                    }    
                                    tab = osa.performTableQuery( q );                        
                                    tab.setTableName( tt );
                                } catch(Exception e) {
                                    // release database connection back to the connection pool
                                    pool.releaseConnection( con, connect.getDriver(), connect.getLogon(),
                                                            connect.getUser(), connect.getPassword() );
                                    throw new Exception( e.toString() );
                                }
                                // release database connection back to the connection pool
                                pool.releaseConnection( con, connect.getDriver(), connect.getLogon(),
                                                        connect.getUser(), connect.getPassword() );
                                // recursive call of this method for replacing foreign keys
                                // with content
                                depth++;
                                tab = getRelations( tab, featureType, query );
                                depth--;

                                if ( ref[z].isReplaceable() ) {
                                    // replace foreign key with content (table) if it's
                                    // marked as replaceable
                                    table.setValueAt( tab, k, i );
                                } else {
                                    // add a new column for the content referenced by
                                    // the foreign key
                                    if ( colIdx < 0 ) {
                                        // this is to provide creating a new column more
                                        // then one time for a foreign key
                                        table.addColumn( ref[z].getTargetTable(),
                                                         "org.deegree.model.table.Table" );
                                                         colIdx = table.getColumnCount()-1;
                                    }
                                    String[] fdf = table.getColumnNames();
                                    table.setValueAt( tab, k, colIdx );
                                }

                            }
                            if ( ref[z].isReplaceable() ) {
                                // if current column is a foreign key replace the datatype
                                // of the column to Table and get the table(s)/row(s)
                                table.setColumnType( i, "org.deegree.model.table.Table" );
                            }
                        }                         
                    }
                }
            }
        }
        
        Debug.debugMethodEnd();
        return table;
    }
    
    
}
