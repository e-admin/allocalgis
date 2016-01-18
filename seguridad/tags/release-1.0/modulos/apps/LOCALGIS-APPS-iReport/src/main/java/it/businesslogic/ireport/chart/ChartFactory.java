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
 * ChartFactory.java
 * 
 * Created on 28 settembre 2004, 23.55
 *
 */

package it.businesslogic.ireport.chart;
import java.util.*;
import java.awt.*;
/**
 *
 * @author  Administrator
 */
public abstract class ChartFactory {
    
   static public java.awt.Image drawChart(String[] parameters, it.businesslogic.ireport.IReportScriptlet scriptlet)
   {
       /* redefine it!! */
       return null;
   }
   
   
    protected static int getParameterAsInteger(String name, java.util.Properties props, int defaultValue)
    {
        String val =  props.getProperty(name, ""+defaultValue);
        try {
            return Integer.parseInt(val);
        } catch (Exception ex) { }
        return defaultValue; 
    }
    
    protected static double getParameterAsDouble(String name, java.util.Properties props, double defaultValue)
    {
        String val =  props.getProperty(name, ""+defaultValue);
        try {
            return Double.parseDouble(val);
        } catch (Exception ex) { }
        return defaultValue; 
    }
    
    
    protected static boolean getParameterAsBoolean(String name, java.util.Properties props, boolean defaultValue)
    {
        String val =  props.getProperty(name, ""+defaultValue);
        try {
            return val.toUpperCase().equals("TRUE");   // from java 1.5.0 only... = Boolean.parseBoolean(val);
        } catch (Exception ex) { }
        return defaultValue; 
    }
    
    protected static java.awt.Color getParameterAsColor(String name, java.util.Properties props, java.awt.Color defaultValue)
    {
        String val =  props.getProperty(name,"");
        try {
            
            java.awt.Color c = parseColorString(val);
            if (c != null) return c;
            
        } catch (Exception ex) { }
        return defaultValue; 
    }
    
    protected static java.util.Vector getSeries(String name, java.util.Properties props, it.businesslogic.ireport.IReportScriptlet scriptlet)
    {

        if (scriptlet == null)
        {
            Vector v =  new Vector();
            v.add(new Double(1));
            v.add(new Double(5));
            v.add(new Double(3));
            v.add(new Double(8));
            v.add(new Double(3));
            v.add(new Double(5));
            v.add(new Double(1));
            v.add(new Double(7));
            v.add(new Double(6));
            v.add(new Double(9));
            
            return v;
        }
        if (!props.containsKey( name ) ) return new Vector();
        String varName = (String)props.getProperty(name);
        
        
        return scriptlet.getSerie( varName );
    }
    
    public static java.awt.Color parseColorString(String newValue)
    {
        if (newValue == null) return null;
        
        newValue = newValue.trim();
        if (!newValue.startsWith("[") || !newValue.endsWith("]"))
        {
            return null;
        }
        
        int r = 0;
        int g = 0;
        int b = 0;
        String rgbValues = newValue.substring(1,newValue.length()-1);  
        try {
        
            StringTokenizer st = new StringTokenizer(rgbValues, ",",false);
            r = Integer.parseInt( st.nextToken() );
            g = Integer.parseInt( st.nextToken() );
            b = Integer.parseInt( st.nextToken() );
        } catch (Exception ex) { return null; }
        
        Color c = new Color(r,g,b);
        return c;
        
    }
}
