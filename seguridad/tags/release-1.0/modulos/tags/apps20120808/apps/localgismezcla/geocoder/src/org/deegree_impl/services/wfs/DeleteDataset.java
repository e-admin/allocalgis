/* ------------------------------------------------------------------------
 
 this file is part of the MISLUX project performed by the lat/lon 
 Fitzke/Fretter/Poth GbR for the Grand Ducé of Luxembourg. Its usage
 is limited to lat/lon and the Grand Ducé of Luxembourg
 
Contact:
 
Andreas Poth
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de
 
Jens Fitzke
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: fitzke@lat-lon.de
 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.wfs;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author  Administrator
 */
public final class DeleteDataset {
        
    private Connection con = null;
    
    /** Creates a new instance of DeleteDataset */
    public DeleteDataset(java.sql.Connection con) throws Exception {
                
        this.con = con;
        con.setAutoCommit( false );
        
    }
    
    public void deleteDataset( String table, String where ) throws SQLException {
        Statement stmt = con.createStatement();
        
        String statemet = "DELETE FROM " + table + " WHERE " + where;
        stmt.execute( statemet );
        //con.commit();
        
        stmt.close();
    }
     
    
}
