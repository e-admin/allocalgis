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
package reso.jumpPlugIn.printLayoutPlugIn;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jump.I18N;

//import org.apache.log4j.Logger;

public final class I18NPlug {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(I18NPlug.class);

	//private static Logger LOG = Logger.getLogger(I18N.class);
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
		I18N.setPlugInRessource(pluginName,bundle);
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
		try
		{
			if (jumpi18n == true) {
				ResourceBundle rb = ((ResourceBundle)I18N.plugInsResourceBundle.get(pluginName));
				String cadena = rb.getString(label);
				logger.debug("DEBUG: get(String, String)"+pluginName+" buscaba:"+label);
				return cadena;
			}
			else {
				logger.debug("DEBUG: get(String, String)"+pluginName+" buscaba:"+label);
				return ((ResourceBundle)I18NPlug.plugInResourceBundle.get(pluginName)).getString(label);			
			}
		}
		catch (RuntimeException e)
		{

			logger.error("get(String, String)"+pluginName+" buscaba:"+label);
			return label;
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

		if (jumpi18n == true) {
			MessageFormat mf = new MessageFormat(((ResourceBundle)I18N.plugInsResourceBundle
					.get(pluginName))
					.getString(label));
			return mf.format(objects);
		}
		else {
			MessageFormat mf = new MessageFormat(((ResourceBundle)I18NPlug.plugInResourceBundle
					.get(pluginName))
					.getString(label));
			return mf.format(objects);			
		}
	}


}
