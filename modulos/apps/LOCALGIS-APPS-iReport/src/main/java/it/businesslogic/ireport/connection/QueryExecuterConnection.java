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
 * QueryExecuterConnection.java
 * 
 * Created on 4 giugno 2003, 18.15
 *
 */

package it.businesslogic.ireport.connection;
import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.QueryExecuterConnectionEditor;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  Administrator
 */
public class QueryExecuterConnection extends it.businesslogic.ireport.IReportConnection {
    
    private String name;
    
    private java.util.HashMap map = null;
    
    /** Creates a new instance of JDBCConnection */
    public QueryExecuterConnection() {
        map = new java.util.HashMap();
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
        return false;
    }
    
    /*
     *  This method return all properties used by this connection
     */
    public java.util.HashMap getProperties()
    {    
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
        this.map = map;
    }
    

    
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        return null;
    }
    
    public String getDescription(){ return I18n.getString("connectionType.queryExecuter", "Query Executer mode"); }
    
            
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new QueryExecuterConnectionEditor();
    }
}
