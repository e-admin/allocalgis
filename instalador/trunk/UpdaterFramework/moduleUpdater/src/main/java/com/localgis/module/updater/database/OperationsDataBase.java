package com.localgis.module.updater.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class OperationsDataBase {
	
	private static final Logger log=Logger.getLogger(OperationsDataBase.class.toString());;
	
	private static final String dbDrvr = "org.postgresql.Driver"; 
//    private static final String dbName = "geopista"; 
//
//    private static final String dbHost = "jdbc:postgresql://192.168.247.175:"+dbPort+"/"+dbName; 
//    private static final String userName = "geopista";
//    private static final String password = "01g7PT3a";
    
    private String mensajeError = ""; 
   // private static Connection conn = null; 
	
//    public static Connection getConnectionDataBase(String username, String password){ 
//    
//        try{ 
//            Class.forName(dbDrvr).newInstance(); 
//        }catch(ClassNotFoundException cnfe){ 
//            return null; 
//        }catch(InstantiationException ie){ 
//            return null; 
//        }catch(IllegalAccessException iae){ 
//            return null; 
//        } 
//
//        try{ 
//            con = DriverManager.getConnection(dbHost, username, password); 
//        }catch(SQLException sqle){ 
//            return null; 
//        } 
//
//        return con; 
//    } 
    
	public static DataSource getDataSource() throws NamingException, SQLException 
	{
	    Context initContext = new InitialContext();
	    Context envContext  = (Context)initContext.lookup("java:/comp/env");

	   	return (DataSource)envContext.lookup("jdbc/systemversion");
	    //DataSource ds = (DataSource)initContext.lookup("jdbc:odbc:systemversionw");
	   // return ds;
	}
	
	public static Connection getConnection(String dbHost, String userName, String password) throws ClassNotFoundException, SQLException{
		
		//System.out.println("dbDrvr:<"+dbDrvr+">");
		//System.out.println("dbHost:<"+dbHost+">");
		//System.out.println("userName:<"+userName+">");
		//System.out.println("password:<"+password+">");
		Class.forName(dbDrvr);
		DriverManager.setLoginTimeout(10);
		Connection conn = DriverManager.getConnection(dbHost, userName , password);
		
//		Connection conn = null;
//		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
//		conn = DriverManager.getConnection("jdbc:odbc:systemversionw"); 
		return conn;
	}
	
	public static void checkDataBase(){
		
		
	}
	
	public static boolean executeQuery(Connection conn, String query) throws SQLException { 
        boolean result = false;
		Statement sentencias = null; 
        try{ 
            sentencias = conn.createStatement(); 
            sentencias.execute(query); 
            result = true;
        } catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
        	sentencias.close();
        }
        return result; 
    } 
	

}
/**
 * OperationsDataBase.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
