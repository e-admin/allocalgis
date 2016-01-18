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
 * IReportScriptlet.java
 * 
 * Created on 26 settembre 2004, 16.25
 *
 */

package it.businesslogic.ireport;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import net.sf.jasperreports.engine.JRScriptletException;
/**
 *
 * @author  Administrator
 */
public class IReportScriptlet extends net.sf.jasperreports.engine.JRAbstractScriptlet
{
        // Vector of things to collect...
        HashMap series = new java.util.HashMap();
        HashMap groupStarted = new java.util.HashMap();
        
	/**
	 *
	 */
	public void beforeReportInit() throws JRScriptletException
	{
        }


	/**
	 *
	 */
	public void afterReportInit() throws JRScriptletException
	{
               
        }


	/**
	 *
	 */
	public void beforePageInit() throws JRScriptletException
	{

	}


	/**
	 *
	 */
	public void afterPageInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void beforeColumnInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void afterColumnInit() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void beforeGroupInit(String groupName) throws JRScriptletException
	{
            
	}


	/**
	 *
	 */
	public void afterGroupInit(String groupName) throws JRScriptletException
	{
           resetSeries(groupName);
        }


	/**
	 *
	 */
	public void beforeDetailEval() throws JRScriptletException
	{
	}


	/**
	 *
	 */
	public void afterDetailEval() throws JRScriptletException
	{
            processSeries();
	}
        
        protected void processSeries()
        {
            // Looking for serie_to_calc in variables...
            Set vars = variablesMap.keySet();
            Iterator iter = vars.iterator();
            while( iter.hasNext())
            {
                String key = (String)iter.next();
                if (key.startsWith("SERIE_"))
                {
                    Vector serie = (Vector)series.get(key);
                    if (serie == null)
                    {
                        serie = new Vector();
                        series.put(key,serie);
                    }
                    try {
                        serie.add( getVariableValue(key) );
                    } catch (Exception ex) {}
                }
            }
        }
        
        protected void resetSeries(String group)
        {
            // Looking for serie_to_calc in variables...
            Set vars = variablesMap.keySet();
            Iterator iter = vars.iterator();
            while( iter.hasNext())
            {
                String key = (String)iter.next();
                if (key.startsWith("SERIE_") && key.indexOf("G_" + group)> 0)
                {
                    series.remove(key);
                }
            }
        }
        
        public String getSerieAsString(String name)
        {
            Vector v =  (Vector)series.get(name);
            Enumeration enum_v = v.elements();
            String tot = "";
            while (enum_v.hasMoreElements())
            {
                String s = ""+enum_v.nextElement();
                tot += s + "\n";
            }
            
            return tot;
        }
        
        public Vector getSerie(String serieName)
        {
            
            Vector v = (Vector)series.get(serieName);
            if (v==null)
            {
                v = new Vector();
                series.put(serieName, v);
                
            }
            
            return v;
        }    
        
        
        public Boolean addValueToSerie(String serieName, Object value)
        {
            Vector v = getSerie(serieName);
            v.add( value );
            return new Boolean(false);
        }
        
        public Boolean resetSerie(String serieName)
        {
            series.remove(serieName);
            return new Boolean(false);
        }
}
