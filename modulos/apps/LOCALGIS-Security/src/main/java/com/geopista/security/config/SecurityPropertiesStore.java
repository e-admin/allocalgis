/**
 * SecurityPropertiesStore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SecurityPropertiesStore {
	
	/**
	 * Logger
	 */
	protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SecurityPropertiesStore.class);
    	
	/**
	 * Contantes
	 */
	private static final String PROPERTIES_NAME = "localgisSecurity.properties";
		
	/**
	 * Variables
	 */
	protected static ResourceBundle config;
	
	/**
	 * Instancia
	 */
	private static SecurityPropertiesStore instance = new SecurityPropertiesStore();
	
	/**
	 * Constructor protegido
	 */
	protected SecurityPropertiesStore() {		
		config = initializeConfig();
	}
	
	/**
	 * 
	 */
	public static SecurityPropertiesStore getSecurityPropertiesStore() {
		return instance;
	}
		
	/**
	 * Devuelve un objeto de recursos con el contenido de un properties (localgis)
	 * @return ResourceBundle: Objeto de recursos con el contenido del properties
	 */
	private static ResourceBundle initializeConfig() {
		return initializeConfig(PROPERTIES_NAME);
	}
	
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
	

}
