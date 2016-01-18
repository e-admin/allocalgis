package com.geopista.app.eiel;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;



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
        	Class.forName(getString("conexion.driver")) ; 
            
            String url = getString("conexion.url");
            String driverClass = getString("conexion.driver");
            String user = getString("conexion.user");
            String pass =getString("conexion.pass");
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
    	 logger.info("conexion.url="+ac.rBConfiguration.getString("conexion.url"));
    	 try {
    	 ac.getJDBCConnection();
    	 }catch(SQLException sqle) {
    		 sqle.printStackTrace();
    	 }
   	 
    }


}
