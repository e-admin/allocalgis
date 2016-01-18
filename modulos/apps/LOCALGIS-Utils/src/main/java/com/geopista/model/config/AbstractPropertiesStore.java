/**
 * AbstractPropertiesStore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model.config;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class AbstractPropertiesStore {
	
	/**
	 * Logger
	 */
	protected static org.apache.log4j.Logger logger;
	
	/**
	 * Variables
	 */
	protected static ResourceBundle config;
	
	/**
	 * Devuelve un objeto de recursos con el contenido de un properties
	 * @param name: Nombre del fichero properties 
	 * @return ResourceBundle: Objeto de recursos con el contenido del properties
	 */
	protected static ResourceBundle initializeConfig(String name){
		return ResourceBundle.getBundle(name.replace(".properties", ""));
    }	
	
	/**
	 * Devuelve el valor de una propiedad solicitada
	 * @param paramId: Nombre de la propiedad
	 * @return String: Valor de la propiedad 
	 */
	public String getString(String paramId){		
		return getString(paramId, "");
    }
	
	/**
	 * Devuelve el valor de una propiedad solicitada
	 * @param paramId: paramId: Nombre de la propiedad
	 * @param defaultString: Valor por defecto
	 * @return String: Valor de la propiedad 
	 */
	public String getString(String paramId, String defaultString){
		if(config != null){
        	try {
            	return config.getString(paramId);
            } catch(MissingResourceException ex) {
            	logger.error(ex);         	
            } 
		}
    	return defaultString;
    }
	
	public Map getMap(){
		 Map<String, String> map = new HashMap<String, String>(); 
		 
	    Enumeration<String> keys = config.getKeys(); 
	    while (keys.hasMoreElements()) { 
	      String key = keys.nextElement(); 
	      map.put(key, config.getString( key)); 
	    } 
	 
	    return map; 
	}
	
	
}
