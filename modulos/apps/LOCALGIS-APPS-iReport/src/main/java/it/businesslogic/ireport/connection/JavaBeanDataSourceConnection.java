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
 * JavaBeanDataSourceConnection.java
 * 
 * Created on 4 giugno 2003, 18.15
 *
 */

package it.businesslogic.ireport.connection;
import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.JavaBeanDataSourceConnectionEditor;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;

import javax.swing.JOptionPane;
/**
 *
 * @author  Administrator
 */
public class JavaBeanDataSourceConnection extends it.businesslogic.ireport.IReportConnection {
    
    public static String BEAN_ARRAY = "BEAN_ARRAY";
    public static String BEAN_COLLECTION = "BEAN_COLLECTION";
    
    private String name;
    
    private String factoryClass;
    
    private String methodToCall;
    
    private boolean useFieldDescription;
    
    private String type = "BEAN_COLLECTION";
    
    /** Creates a new instance of JDBCConnection */
    
    
    public JavaBeanDataSourceConnection() {
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
        map.put("Type", Misc.nvl(this.getType(),""));
        map.put("UseFieldDescription", ""+this.isUseFieldDescription());
       
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
        this.setFactoryClass( (String)map.get("FactoryClass"));
        this.setMethodToCall( (String)map.get("MethodToCall"));
        if (map.containsKey("UseFieldDescription"))
        {
            this.setUseFieldDescription( ((String)map.get("UseFieldDescription")).equals("true") );
        }
        if (map.containsKey("Type"))
        {
            this.setType( (String)map.get("Type"));
        }
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
     * Getter for property type.
     * @return Value of property type.
     */
    public java.lang.String getType() {
        return type;
    }    
   
    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }    
    
        /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        try {
            
                Class clazz = Thread.currentThread().getContextClassLoader().loadClass(factoryClass);
	        Object obj = clazz.newInstance();
       		Object return_obj = obj.getClass().getMethod( methodToCall, new Class[0]).invoke(null,new Object[0]);   
       		
       		if (return_obj != null) 
       		{
	       		if (Misc.nvl(this.getType(),"").equals(BEAN_ARRAY) )
	       		{			
       				return new net.sf.jasperreports.engine.data.JRBeanArrayDataSource((Object[]) return_obj, isUseFieldDescription());
                        }
       			else if (Misc.nvl(this.getType(),"").equals(BEAN_COLLECTION) )
       			{
       				return new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource((java.util.Collection) return_obj, isUseFieldDescription());	
       			}
       		}
       		return new net.sf.jasperreports.engine.JREmptyDataSource();
       		             
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return super.getJRDataSource();
        }
    }

    public boolean isUseFieldDescription() {
        return useFieldDescription;
    }

    public void setUseFieldDescription(boolean useFieldDescription) {
        this.useFieldDescription = useFieldDescription;
    }
    
    public String getDescription(){ return I18n.getString("connectionType.javabeans", "JavaBeans set datasource"); }
    
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JavaBeanDataSourceConnectionEditor();
    }
    
    public void test() throws Exception 
    {
            try {
                Object obj = Class.forName(getFactoryClass(), true, MainFrame.getMainInstance().getReportClassLoader()).newInstance();
                Object ret_obj = obj.getClass().getMethod( getMethodToCall(), new Class[0]).invoke(null,new Object[0]);                
            
                if (ret_obj != null && getType() == BEAN_COLLECTION && (ret_obj instanceof  java.util.Collection))
                {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                }
                else if (ret_obj != null  && getType() == BEAN_ARRAY && (ret_obj instanceof  Object[]))
                {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.notValidValueReturned",
                            "The method don't return a valid Array or java.util.Collection!\n"),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                }
                
            }catch (NoClassDefFoundError ex)
            {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getFormattedString("messages.connection.noClassDefFoundError",
                            "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                            new Object[]{"", ""+ex.getMessage()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    throw ex;					
            } 
            catch (ClassNotFoundException ex)
            {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getFormattedString("messages.connection.classNotFoundError",
                            "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                            new Object[]{"", ""+ex.getMessage(), "" + getFactoryClass()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    throw ex;				
            } 
            catch (Exception ex)
            {
            	ex.printStackTrace();
            	
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                I18n.getFormattedString("messages.connection.generalError2",
                "{0}\nGeneral problem:\n {1}",
                new Object[]{"", ""+ex.getMessage()}),
                I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                throw ex;									
            }
    }
}
