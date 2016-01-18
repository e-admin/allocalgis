/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * JREmptyDatasourceConnection.java
 * 
 * Created on 4 giugno 2003, 18.15
 *
 */

package it.businesslogic.ireport.connection;
import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.JREmptyDataSourceConnectionEditor;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;
import net.sf.jasperreports.engine.JREmptyDataSource;

/**
 *
 * @author  Administrator
 */
public class JREmptyDatasourceConnection extends it.businesslogic.ireport.connection.NullConnection {
    
    private int records = 1;
    
    /** Creates a new instance of JDBCConnection */
    public JREmptyDatasourceConnection() {
        this.setName("Empty datasource connection");
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    public java.sql.Connection getConnection() {       
            return null;
    }
    
    public boolean isJDBCConnection() {
        return false;
    }
    
    public boolean isJRDataSource() {
        return true;
    }
    
   /*
     *  This method return all properties used by this connection
     */
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("records", "" + this.getRecords() );
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
        this.setRecords( Integer.parseInt( Misc.nvl( (String)map.get("records"),"1") ) );
    }
    
    
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        return new JREmptyDataSource(getRecords());
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }
    
    public String getDescription(){ return I18n.getString("connectionType.emptyDataSource", "Empty data source"); }
    
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JREmptyDataSourceConnectionEditor();
    }
}
