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
import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.JRCustomDataSourceConnectionEditor;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;

import javax.swing.JOptionPane;

/**
 *
 * @author  Administrator
 */
public class JRCustomDataSourceConnection extends it.businesslogic.ireport.IReportConnection {
    
    private String name;
    
    private String factoryClass;
    
    private String methodToCall;
    
    /** Creates a new instance of JDBCConnection */
    
    
    public JRCustomDataSourceConnection() {
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
    
    /*
     *  This method return all properties used by this connection
     */
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("FactoryClass", Misc.nvl(this.getFactoryClass() ,"") );
        map.put("MethodToCall", Misc.nvl(this.getMethodToCall(),""));
       
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
        this.setFactoryClass( (String)map.get("FactoryClass"));
        this.setMethodToCall( (String)map.get("MethodToCall"));
    }
    

    
    /** Getter for property methodToCall.
     * @return Value of property methodToCall.
     *
     */
    public java.lang.String getMethodToCall() {
        return methodToCall;
    }
    
    /** Setter for property methodToCall.
     * @param methodToCall New value of property methodToCall.
     *
     */
    public void setMethodToCall(java.lang.String methodToCall) {
        this.methodToCall = methodToCall;
    }
    
    /** Getter for property factoryClass.
     * @return Value of property factoryClass.
     *
     */
    public java.lang.String getFactoryClass() {
        return factoryClass;
    }
    
    /** Setter for property factoryClass.
     * @param factoryClass New value of property factoryClass.
     *
     */
    public void setFactoryClass(java.lang.String factoryClass) {
        this.factoryClass = factoryClass;
    }
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        try {
        Object obj = Class.forName( factoryClass, true, Thread.currentThread().getContextClassLoader() ).newInstance();
        return (net.sf.jasperreports.engine.JRDataSource) obj.getClass().getMethod( methodToCall, new Class[0]).invoke(obj,new Object[0]);                
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return super.getJRDataSource();
        }
    }
    
    public String getDescription(){ return I18n.getString("connectionType.customDataSource", "Custom JRDataSource"); }

    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRCustomDataSourceConnectionEditor();
    }
    
    public void test() throws Exception
    {
        try {
                Object obj = Class.forName( getFactoryClass() , true, MainFrame.getMainInstance().getReportClassLoader()).newInstance();
                obj.getClass().getMethod( getMethodToCall() , new Class[0]).invoke(obj,new Object[0]);                
        }catch (NoClassDefFoundError ex)
        {
                JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                        I18n.getFormattedString("messages.connection.noClassDefFoundError",
                        "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                        new Object[]{"", ""+ex.getMessage()}),
                        I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                throw new Exception();					
        } 
        catch (ClassNotFoundException ex)
        {
                JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                        I18n.getFormattedString("messages.connection.classNotFoundError",
                        "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                        new Object[]{"", ""+ex.getMessage(), "" + getFactoryClass()}),
                        I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                throw new Exception();				
        } 
        catch (Exception ex)
        {

        JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                I18n.getFormattedString("messages.connection.generalError2",
                "{0}\nGeneral problem:\n {1}",
                new Object[]{"", ""+ex.getMessage()}),
                I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                return;									
        }
        JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
        return;
    }
}
