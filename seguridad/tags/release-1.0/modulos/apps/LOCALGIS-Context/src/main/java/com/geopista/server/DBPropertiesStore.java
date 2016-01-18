package com.geopista.server;

import java.util.ResourceBundle;

import com.geopista.consts.config.DBConstants;
import com.geopista.model.config.AbstractPropertiesStore;

public class DBPropertiesStore extends AbstractPropertiesStore{
	
	/**
	 * Logger
	 */
	protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DBPropertiesStore.class);
    	
	/**
	 * Instancia
	 */
	private static DBPropertiesStore instance = new DBPropertiesStore();
	
	/**
	 * Constructor protegido
	 */
	protected DBPropertiesStore() {		
		config = initializeConfig();
	}
	
	/**
	 * 
	 */
	public static DBPropertiesStore getDBContext() {
		return instance;
	}
		
	/**
	 * Devuelve un objeto de recursos con el contenido de un properties (localgis)
	 * @return ResourceBundle: Objeto de recursos con el contenido del properties
	 */
	private static ResourceBundle initializeConfig() {
		return initializeConfig(DBConstants.PROPERTIES_NAME);
	}
	
}
