package com.geopista.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.geopista.consts.config.DBConstants;

public class ServerContext {
	
	/**
	 * Logger
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DBPropertiesStore.class);
    
	/**
	 * Variables
	 */
	private static ResourceBundle config = initializeConfig();
	
	/**
	 * Instancia
	 */
	private DBPropertiesStore instance = new DBPropertiesStore();
	
	/**
	 * Constructor protegido
	 */
	protected ServerContext(){		
	}
	
	/**
	 * 
	 */
	public DBPropertiesStore getServerContext(){
		return instance;
	}
	
	/**
	 * Devuelve un objeto de recursos con el contenido de un properties
	 * @param path: 
	 * @return ResourceBundle: Objeto de recursos con el contenido del properties
	 */
	private static ResourceBundle initializeConfig(String path){
        String userDirPath = System.getProperty("user.home");        
        File userDir = new File(userDirPath);
        File propFile = new File(userDir, path);
        
        if (!propFile.exists()){
            logger.info("initializeProperties(String) - Fichero inexistente. Copiando fichero de propiedades. : String userDirPath = " + userDirPath);
            Properties properties = new Properties();
            try {
                properties.load(DBPropertiesStore.class.getResourceAsStream("/" + propFile.getName()));
                properties.store(new FileOutputStream(propFile), "Server Local Config");
            } catch (IOException e1){                
                logger.warn("initializeProperties(String) - No se puede realizar la copia local de la configuración. Usando configuración por defecto. : File propFile = " + propFile, e1);
                return ResourceBundle.getBundle("localgisServer");
            }
        }
        
        try {
            FileInputStream stream = new FileInputStream(propFile);
            ResourceBundle rb = new PropertyResourceBundle(stream);            
            logger.info("initializeProperties(String) - Cargando configuración. : File propFile = " + propFile);
            
            return rb;
        } catch (IOException e) {
            logger.error("initializeProperties(String) - No se puede cargar el fichero de configuración. : File propFile = " + propFile, e);
        }
        
        return ResourceBundle.getBundle("localgisServer");
    }
	
	/**
	 * Devuelve un objeto de recursos con el contenido de un properties (localgis)
	 * @return ResourceBundle: Objeto de recursos con el contenido del properties
	 */
	private static ResourceBundle initializeConfig()
    {
		return initializeConfig(DBConstants.PROPERTIES_NAME);
	}
	
	/**
	 * Devuelve el valor de una propiedad solicitada
	 * @param paramId: Nombre de la propiedad
	 * @return String: Valor de la propiedad 
	 */
	public static String getString(String paramId){		
		return getString(paramId, null);
    }
	
	/**
	 * Devuelve el valor de una propiedad solicitada
	 * @param paramId: paramId: Nombre de la propiedad
	 * @param defaultString: Valor por defecto
	 * @return String: Valor de la propiedad 
	 */
	public static String getString(String paramId, String defaultString){
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
