package com.geopista.model.config;

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
	
}
