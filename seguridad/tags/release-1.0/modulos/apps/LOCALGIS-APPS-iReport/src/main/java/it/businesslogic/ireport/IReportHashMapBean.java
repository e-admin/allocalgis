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
 * IReportHashMapBean.java
 * 
 * Created on March 8, 2006, 2:23 PM
 *
 */

package it.businesslogic.ireport;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author gtoffoli
 */
public class IReportHashMapBean  implements Cloneable {
    
    private HashMap beanProperties = new HashMap();
    
    /** Creates a new instance of IReportDynamicBean */
    public IReportHashMapBean() {
    }

    public HashMap getBeanProperties() {
        return beanProperties;
    }

    public void setBeanProperties(HashMap beanProperties) {
        this.beanProperties = beanProperties;
    }
    
    /** Create a object copy */
    public Object clone()
    {
            IReportHashMapBean newBean = new IReportHashMapBean();
            return clone(newBean);
    }
    
    /** this method copies all properties of this objects into newBean attibutes */
    public Object clone(IReportHashMapBean newBean)
    {
            Iterator i_keys = getBeanProperties().keySet().iterator();

            while (i_keys.hasNext())
            {
                Object key = i_keys.next();
                newBean.getBeanProperties().put(key,getBeanProperties().get(key));
            }

            return newBean;
    }  
        
    /**
     * Look for property in the map. The value must be true or false.
     * If the property is not found, the defaultValue is returned.
     */
    public boolean getBooleanValue(String property, boolean defaultValue)
    {
        Object prop = getBeanProperties().get(property);
        if (prop == null) return defaultValue;
        return Boolean.valueOf( prop.toString() ).booleanValue();
    }
    
    /**
     * Look for property in the map.
     * If the property is not found, the defaultValue is returned.
     */
    public String getStringValue(String property, String defaultValue)
    {
        Object prop = getBeanProperties().get(property);
        if (prop == null) return defaultValue;
        return prop.toString();
    }
    
    /**
     *  Look for property in the map. The value must be a Color object.
     * If the property is not found, the defaultValue is returned.
     */
    public java.awt.Color getColorValue(String property, java.awt.Color defaultValue)
    {
        Object prop = getBeanProperties().get(property);
        if (prop == null || !(prop instanceof java.awt.Color)) return defaultValue;
        return (java.awt.Color)prop;
    }
    
    /**
     * Look for property in the map. The value must rapresent a valid number.
     * If the property is not found, the defaultValue is returned.
     *
     * If the number is not valid, a NumberFormatException is thrown
     */
    public int getIntValue(String property, int defaultValue)
    {
        Object prop = getBeanProperties().get(property);
        if (prop == null) return defaultValue;
        try {
        return Integer.valueOf( prop.toString() ).intValue();
        } catch (Exception ex)
        {
            getBeanProperties().remove(property);
            return defaultValue;
        }
    }
    
    /**
     * Shurtcut for getBeanProperties().put(property, value)
     * 
     * If property is null, the method does nothing
     */
    public void setPropertyValue(String property, Object value)
    {
        if (property == null) return;
        getBeanProperties().put(property,value);
    }
    
    /**
     * Shurtcut for getBeanProperties().get(property)
     * 
     * If property is null, the method return null
     */
    public Object getPropertyValue(String property)
    {
        if (property == null) return null;
        return getBeanProperties().get(property);
    }
    
    /**
     * Look for property in the map. The value must rapresent a valid number.
     * If the property is not found, the defaultValue is returned.
     *
     * If the number is not valid, a NumberFormatException is thrown
     */
    public int getDoubleValue(String property, int defaultValue)
    {
        Object prop = getBeanProperties().get(property);
        if (prop == null) return defaultValue;
        return Integer.valueOf( prop.toString() ).intValue();
    }
    
}
