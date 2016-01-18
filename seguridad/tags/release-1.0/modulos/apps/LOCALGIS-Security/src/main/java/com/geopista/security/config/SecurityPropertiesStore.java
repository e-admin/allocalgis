package com.geopista.security.config;

import java.util.ResourceBundle;

import com.geopista.consts.config.SecurityConstants;
import com.geopista.model.config.AbstractPropertiesStore;

public class SecurityPropertiesStore extends AbstractPropertiesStore{
	
	/**
	 * Logger
	 */
	protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SecurityPropertiesStore.class);
    	
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
		return initializeConfig(SecurityConstants.PROPERTIES_NAME);
	}

}
