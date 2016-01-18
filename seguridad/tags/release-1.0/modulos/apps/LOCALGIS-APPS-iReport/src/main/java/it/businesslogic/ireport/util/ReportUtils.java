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
 * ReportUtils.java
 * 
 */

package it.businesslogic.ireport.util;

import java.util.*;


public class ReportUtils {
	
	/**
		Esempio di utilizzo:
		
		encodeParameters($P{REPORT_MAP},
				new String[]{
				   "REPORT_ID=107",
				   "02_Area_Manager_ID=" + $F{Manager_ID},
				   "PIPPO=Pluto"
				});	
		
		Ii parametri2 hanno precedenza su parametri1
	
	*/
	public static String encodeParameters(Map parametri1, String[] parametri2)
	{
		String url = "";
		HashMap param_map = new HashMap();
		if (parametri1 == null) parametri1 = new HashMap();
		if (parametri2 == null) parametri2 = new String[]{};
		
		param_map.putAll( parametri1 );
		
		for (int i=0; i<parametri2.length; ++i)
		{
			
			if (parametri2[i].indexOf("=") > 0)
			{	
				String key = parametri2[i].substring(0, parametri2[i].indexOf("="));
				String val = parametri2[i].substring(parametri2[i].indexOf("=")+1);
				
				parametri1.put(key, val);				
			}
		}
		
		Set keys = parametri1.keySet();
		Iterator params_iterator = keys.iterator();
		while (params_iterator.hasNext())
		{
			try {
				String key = (String)params_iterator.next();
				Object val = (Object)parametri1.get(key);
			
				if (url.length() > 0) url += "&";
			
				url += java.net.URLEncoder.encode(key,"UTF-8") + "=" + java.net.URLEncoder.encode(""+val,"UTF-8");
			
			} catch (Exception ex)
			{}
		}
		
		return url;
	}
}
