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
 * JRCustomDataSourceConnection.java
 * 
 * Created on 4 giugno 2003, 18.15
 *
 */

package it.businesslogic.ireport.connection;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;

import java.util.HashMap;
import java.util.Iterator;
/**
 *
 * @author  Administrator
 */
public class JRCustomConnection extends it.businesslogic.ireport.IReportConnection {
    
    public static final String CLASSNAME_PROPERTY ="iReportConnectionClassName";
    public static final String PROPERTY_PREFIX ="prop_";
    
    /**
     * The map to store the static connection parameters
     */
    private java.util.HashMap innerConnectionProperties = new java.util.HashMap();
    private IReportConnection innerConnection = null;
    private String iReportConnectionClassName = null;
    
    /** Creates a new instance of JDBCConnection */
    
    
    public JRCustomConnection() {
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    public java.sql.Connection getConnection() {       
        if (getInnerConnection() == null) return super.getConnection();
        return getInnerConnection().getConnection();
    }
    
    public boolean isJDBCConnection() {
        if (getInnerConnection() == null) return super.isJDBCConnection();
        return getInnerConnection().isJDBCConnection();
    }
    
    /*
     *  This method return all properties used by this connection
     */
    public java.util.HashMap getProperties()
    {    
        HashMap map = new HashMap();
        HashMap map2 = getInnerConnectionProperties();
        Iterator iterator = map2.keySet().iterator();
        while (iterator.hasNext())
        {
             Object key = iterator.next();
             map.put(PROPERTY_PREFIX + key, map2.get(key));
        }
        
        if (getIReportConnectionClassName() != null)
        {
            map.put(CLASSNAME_PROPERTY, getIReportConnectionClassName());
        }
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
        HashMap map2 = new HashMap();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext())
        {
            String key = ""+iterator.next();
            Object value = map.get(key);
            
            if (key.startsWith(PROPERTY_PREFIX))
            {
                map2.put(key.substring(PROPERTY_PREFIX.length()), value);
            }
            else if (key.equals(CLASSNAME_PROPERTY))
            {
                this.setIReportConnectionClassName( ""+map.get( CLASSNAME_PROPERTY ) );
            }
        }
        
        setInnerConnectionProperties( map2 );
        setInnerConnection(null);
    }

    
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        if (getInnerConnection() == null) return super.getJRDataSource();
        return getInnerConnection().getJRDataSource();
    }

    public java.util.Map getSpecialParameters(java.util.Map map) throws net.sf.jasperreports.engine.JRException {

        if (getInnerConnection() == null) return super.getSpecialParameters(map);
        return getInnerConnection().getSpecialParameters(map);
    }

    public java.util.Map disposeSpecialParameters(java.util.Map map) {

        if (getInnerConnection() == null) return super.disposeSpecialParameters(map);
        return getInnerConnection().disposeSpecialParameters(map);
    }

    public boolean isJRDataSource() {

        if (getInnerConnection() == null) return super.isJRDataSource();
        return getInnerConnection().isJRDataSource();
    }


    public String getIReportConnectionClassName() {
        return iReportConnectionClassName;
    }

    public void setIReportConnectionClassName(String iReportConnectionClassName) {
        this.iReportConnectionClassName = iReportConnectionClassName;
    }

    public IReportConnection getInnerConnection() {
        
        if (innerConnection == null)
        {
            try {
            
                innerConnection = (IReportConnection)Class.forName( getIReportConnectionClassName(), true,  MainFrame.getMainInstance().getReportClassLoader() ).newInstance();
                innerConnection.loadProperties( getInnerConnectionProperties() );
                
            } catch (Throwable ex)
            {
                ex.printStackTrace();
            }
        }
        
        return innerConnection;
    }

    public void setInnerConnection(IReportConnection innerConnection) {
        this.innerConnection = innerConnection;
    }

    public java.util.HashMap getInnerConnectionProperties() {
        return innerConnectionProperties;
    }

    public void setInnerConnectionProperties(java.util.HashMap innerConnectionProperties) {
        this.innerConnectionProperties = innerConnectionProperties;
    }
    
    public void test() throws Exception 
    {
        IReportConnection testConnection =  (IReportConnection)Class.forName( getIReportConnectionClassName(), true,  MainFrame.getMainInstance().getReportClassLoader() ).newInstance();
        if (testConnection != null)
        {
            testConnection.loadProperties( getInnerConnectionProperties() );
            testConnection.test();
        }
    }
    
    public String getDescription(){ return I18n.getString("connectionType.customIReportConnection", "Custom iReport connection"); }
}
