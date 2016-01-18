/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 * 
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * For more information, contact:
 * 
 * 
 * www.geopista.com
 * 
 * Created on 11-jun-2004 by juacas
 * 
 *  
 */
package com.geopista.app;

import java.awt.Frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
//import java.util.PropertyResourceBundle;
//import java.util.ResourceBundle;



import java.util.Vector;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;



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
         
    	 SimpleAppContext ac=new SimpleAppContext();
    	 logger.info("conexion.url="+ac.rBConfiguration.getString("conexion.url"));
    	 try {
    	 ac.getJDBCConnection();
    	 }catch(SQLException sqle) {
    		 sqle.printStackTrace();
    	 }
   	 
    }


   
}