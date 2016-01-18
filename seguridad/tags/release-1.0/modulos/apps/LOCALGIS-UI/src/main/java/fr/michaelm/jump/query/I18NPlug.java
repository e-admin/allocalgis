/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package fr.michaelm.jump.query;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Locale;
import java.lang.String;
import java.util.Hashtable;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.JUMPWorkbench;



public final class I18NPlug {
    private static String TestLocale;
    public static boolean jumpi18n = true;
	private static Hashtable plugInResourceBundle = new Hashtable();
    
	  /** 
	   * Set plugin I18N resource file
	   * Tries to use locale set in command line (if set)
	   * @param pluginName (path + name)
	   * @param reference of the bundle file
	   */
	  public static void setPlugInRessource(String pluginName, String bundle){

//		try {
//			TestLocale = JUMPWorkbench.I18N_SETLOCALE;
//		} catch (java.lang.NoSuchFieldError s) {
//			jumpi18n = false ;
//		}
//		
//		if (jumpi18n == true) {
//			
//	  	   if (JUMPWorkbench.I18N_SETLOCALE == "") {
//	  		// No locale has been specified at startup: choose default locale
//			I18N.plugInsResourceBundle.put(pluginName, ResourceBundle.getBundle(bundle));
//	  	   }
//	  	   else {
//			  String lang = JUMPWorkbench.I18N_SETLOCALE.split("_")[0];
//			  try {
//				 String country = JUMPWorkbench.I18N_SETLOCALE.split("_")[1];
//				 Locale locale = new Locale(lang, country);
//				 I18N.plugInsResourceBundle.put(pluginName, ResourceBundle.getBundle(bundle, locale));
//			  } catch (java.lang.ArrayIndexOutOfBoundsException e) {
//				 Locale locale = new Locale(lang);		 	  	
//				 I18N.plugInsResourceBundle.put(pluginName, ResourceBundle.getBundle(bundle, locale));
//			  }
//	  	   }
//		}
//		else 
	  {
			// in this case we use the default .properties file (en)
			
			Locale locale = Locale.getDefault();
			I18NPlug.plugInResourceBundle.put(pluginName, ResourceBundle.getBundle(bundle, locale));				
		}
	 }

	/**
	 * Process text with the locale 'pluginName_<locale>.properties' file
	 * 
	 * @param pluginName (path + name)
	 * @param label
	 * @return i18n label
	 */
	public static String get(String pluginName, String label)
	{		
//		if (jumpi18n == true) {
//		  return ((ResourceBundle)I18N.plugInsResourceBundle
//					  .get(pluginName))
//					  .getString(label);
//		}
//		else 
	{
		  return ((ResourceBundle)I18NPlug.plugInResourceBundle
						.get(pluginName))
						.getString(label);			
		}
	}
	  
	/**
	 * Process text with the locale 'pluginName_<locale>.properties' file
	 * 
	 * @param pluginName (path + name)
	 * @param label with argument insertion : {0} 
	 * @param objects
	 * @return i18n label
	 */
	public static String getMessage(String pluginName, String label, Object[] objects){
		
//		if (jumpi18n == true) {
//		    MessageFormat mf = new MessageFormat(((ResourceBundle)I18N.plugInsResourceBundle
//											  .get(pluginName))
//											  .getString(label));
//		return mf.format(objects);
//		}
//		else 
	{
			MessageFormat mf = new MessageFormat(((ResourceBundle)I18NPlug.plugInResourceBundle
												  .get(pluginName))
												  .getString(label));
			return mf.format(objects);			
		}
	}


}
