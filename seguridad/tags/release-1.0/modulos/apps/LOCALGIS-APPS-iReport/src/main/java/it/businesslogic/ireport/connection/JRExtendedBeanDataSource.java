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
 * JRExtendedBeanDataSource.java
 * 
 * Created on 22 giugno 2004, 14.55
 *
 */

package it.businesslogic.ireport.connection;


import java.util.*;


/**
 * The XML datasource define a row as a path.
 * A path can be truncated.
 * @author  Administrator
 */
public class JRExtendedBeanDataSource implements net.sf.jasperreports.engine.JRDataSource {

    private Object[] beans = null;
    private int index = -1;
    
    public JRExtendedBeanDataSource(Vector beans)
    {
        this.beans = beans.toArray();
    } 
    
    public JRExtendedBeanDataSource(Object[] beans)
    {
        this.beans = beans;
    } 
    
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jRField) throws net.sf.jasperreports.engine.JRException {
        
        String path = jRField.getDescription();
        
        Object val = getPathValue(beans[index], path);
        
        if (val == null) return null;
        if (val.getClass().isAssignableFrom( jRField.getValueClass() )) return val;
        return null;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
                
    }
    
     public boolean next() throws net.sf.jasperreports.engine.JRException {       

        index++;
        if (index >= beans.length) return false;
        return true;
    }
    
   
    private Object getPathValue(Object bean, String path)
    {
        System.out.println("Looking for " + path + " in " + bean);
        
        if (path == null || bean == null || path.trim().length() == 0)
        {
            return bean;
        }
	if (path.startsWith(".")) path = path.substring(1);
        if (path.trim().length() == 0) return bean;
        
        path = path.trim();
        String attr_name = getNextNodeName(path );   
        
        
        /*
        java.lang.reflect.Method m = null;
        try {
            m = bean.getClass().getMethod("get" + attr_name,  new Class[]{});
        } catch (Exception ex){ return null; }
        
        if (m.getReturnType().isArray())
        {
            if (path.equals(attr_name))
            {
                try {
                     return new JRExtendedBeanDataSource( (Object[])m.invoke(bean, new Object[]{}) ); 
                } catch (Exception ex){ return null; }
            }
            else return null;
        }
        else
        {
            if (path.equals(attr_name))
            {
                try {
                    return m.invoke(bean, new Object[]{});
                } catch (Exception ex){ return null; }
            }
            else
            {
                Object child_bean  = null;
                try {
                    child_bean = m.invoke(bean, new Object[]{});
                } catch (Exception ex){ return null; }
                path = path.substring(attr_name.length());
                return getPathValue(child_bean, path );
            }           
        } 
         */
        Class clazz  = null;
        try {
            clazz = org.apache.commons.beanutils.PropertyUtils.getPropertyType(bean,attr_name);
        } catch (Exception ex){ return null; }
    
        if (clazz == null) // Attribute not found!!!!
        {
            return null;
        }
        else if ( clazz.isArray() )
        {
            try {
                     return new JRExtendedBeanDataSource( (Object[])org.apache.commons.beanutils.PropertyUtils.getProperty(bean, attr_name) ); 
                } catch (Exception ex){ return null; }
        }
        else
        {
            if (path.equals(attr_name))
            {
                try {
                    return org.apache.commons.beanutils.PropertyUtils.getProperty(bean, attr_name);
                } catch (Exception ex){ return null; }
            }
            else
            {
                Object child_bean  = null;
                try {
                    child_bean = org.apache.commons.beanutils.PropertyUtils.getProperty(bean, attr_name);
                } catch (Exception ex){ return null; }
                path = path.substring(attr_name.length());
                return getPathValue(child_bean, path );
            }      
        }   
    }
        
    private static String getNextNodeName( String path)
    {
        
        if (path == null || path.length() ==0) return "";
        if (path.startsWith(".")) path = path.substring(1);
        
        String childToTake = path;
        if (path.indexOf(".") >= 0)
        {
                childToTake = path.substring(0,path.indexOf("."));
        }
               
        return childToTake;
    }
}
