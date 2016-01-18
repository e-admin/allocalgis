/**
 * AppContext.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.geopista.app.UserPreferenceConstants;



public class AppContext {

    /**
     * Logger for this class
     */
	 private static final Logger logger=Logger.getLogger(AppContext.class);
	 
	 private static String CONFIGFILE="eielindicadoresbatchgenerator";
	    	    
	 private static ResourceBundle rBConfiguration;
	 
        
   

    
    /**
     *  
     */
    public AppContext() {
    	//Carga el fichero de configuración
       init();               

        
    }
    
    public static void init() {
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
    	  logger.debug(key+"="+AppContext.rBConfiguration.getString(key));
    	}
    	return AppContext.rBConfiguration.getString(key);
    }
    
    public static String getString(String key, String defaultValue){
    	if (!key.contains("pass")) {
    	  logger.debug(key+"="+AppContext.rBConfiguration.getString(key));
    	}
    	try {
    	   return AppContext.rBConfiguration.getString(key);
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
            
            String pass =AppContext.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
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
         
    	 AppContext ac=new AppContext();
    	 logger.info("conexion.url="+ac.rBConfiguration.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL));
    	 try {
    	 ac.getJDBCConnection();
    	 }catch(SQLException sqle) {
    		 sqle.printStackTrace();
    	 }
   	 
    }


}
