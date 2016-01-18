package com.geopista.utils.alfresco.config;

import java.util.ResourceBundle;
import com.geopista.model.config.AbstractPropertiesStore;
import com.geopista.utils.alfresco.global.AlfrescoConstants;

public class AlfrescoPropertiesStore extends AbstractPropertiesStore{
	
	/**
	 * Logger
	 */
	protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AlfrescoPropertiesStore.class);
    	
	/**
	 * Instancia
	 */
	private static AlfrescoPropertiesStore instance = new AlfrescoPropertiesStore();
	
	/**
	 * Constructor protegido
	 */
	protected AlfrescoPropertiesStore() {		
		config = initializeConfig();
	}
	
	/**
	 * 
	 */
	public static AlfrescoPropertiesStore getAlfrescoPropertiesStore() {
		return instance;
	}
		
	/**
	 * Devuelve un objeto de recursos con el contenido de un properties (localgis)
	 * @return ResourceBundle: Objeto de recursos con el contenido del properties
	 */
	private static ResourceBundle initializeConfig() {
		return initializeConfig(AlfrescoConstants.PROPERTIES_NAME);
	}

}
