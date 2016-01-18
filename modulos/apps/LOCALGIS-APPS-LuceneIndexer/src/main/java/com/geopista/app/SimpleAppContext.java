/**
 * SimpleAppContext.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
//import java.util.PropertyResourceBundle;
//import java.util.ResourceBundle;

import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.util.config.UserPreferenceStore;



/**
 * 
 * 
 * Información compartida en el transcurso de una ejecución de la aplicación.
 * Este contexto de aplicación lo utilizan las aplicaciones para comunicarse con sus partes.
 **/
public class SimpleAppContext 
{	
    

	
    /**
     * Logger for this class
     */
	 private static final Logger logger=Logger.getLogger(SimpleAppContext.class);
	 
	 private static String CONFIGFILE="localgisindexer";
	    	    
	 private static ResourceBundle rBConfiguration;
	 
        
   

    
    /**
     *  
     */
    public SimpleAppContext() {
    	//Carga el fichero de configuración
        rBConfiguration = ResourceBundle.getBundle(CONFIGFILE) ;                    

        
    }
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }	
 

    
    public static String getString(String key){
    	if (!key.contains("pass")) {
    	  logger.debug(key+"="+SimpleAppContext.rBConfiguration.getString(key));
    	}
    	return SimpleAppContext.rBConfiguration.getString(key);
    }
    
    public static String getString(String key, String defaultValue){
    	if (!key.contains("pass")) {
    	  logger.debug(key+"="+SimpleAppContext.rBConfiguration.getString(key));
    	}
    	try {
    	   return SimpleAppContext.rBConfiguration.getString(key);
    	}catch(Exception ex){
    	   return defaultValue;
    	}
    	
    }    
    
    
    
    
    /**
     * Devuelve una conexión compartida a la base de datos.
     * @return
     * @throws SQLException
     */
    public static Connection getJDBCConnection() throws SQLException
    {       
        try
        {
        	Class.forName(getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER)) ; 
            
            String url = getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL);
            String driverClass = getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);
            String user = getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER);
            
            String passwordToRecover=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);        		
            String passwordToShow=passwordToRecover;
            try {
    			EncriptarPassword ec=new EncriptarPassword(passwordToRecover);
    			passwordToShow=ec.desencriptar();
    		} catch (Exception e) {
    			passwordToShow=new String(passwordToRecover);
    		}
            String pass=passwordToShow;
            //String pass =getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
            Enumeration enDrivers = DriverManager.getDrivers();
            
            Class.forName(driverClass) ; 
        	Connection conn = DriverManager.getConnection(url,user,pass);
            
            return conn;
        } catch (SQLException e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        } catch (ClassNotFoundException e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        } catch (Exception e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        }
    }    

    public static void main(String[] args) {
         
    	 /*SimpleAppContext ac=new SimpleAppContext();
    	 logger.info("conexion.url="+ac.rBConfiguration.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL));
    	 try {
    	 ac.getJDBCConnection();
    	 }catch(SQLException sqle) {
    		 sqle.printStackTrace();
    	 }*/
   	 
    }


   
}