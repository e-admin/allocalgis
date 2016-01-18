package com.localgis.ws.ln;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OperacionesBBDD {

	static Log logger = LogFactory.getLog(OperacionesBBDD.class);


	public static DataSource getDataSource() throws NamingException 
	{
		Context initContext = new InitialContext();
		Context envContext  = (Context)initContext.lookup("java:/comp/env");
		return (DataSource)envContext.lookup("jdbc/systemversion");
		//DataSource ds = (DataSource)initContext.lookup("jdbc:odbc:systemversionw");
		// return ds;
	}

	private static Connection getConnection() throws ClassNotFoundException, SQLException{
		Connection conn = null;

		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
		conn = DriverManager.getConnection("jdbc:odbc:systemversionw"); 
		return conn;
	}

	public static void installRegistryDataModel() throws Exception  {
		try {
			createTableSystemVersion();
		} catch (Exception e) {
			logger.error("Error crear datamodel RegistryWS:" + e.getMessage());
			throw e;
		}
	}
	
	public static void updateRegistryDataModel() throws Exception  {
		//TODO: Pendiente implementan segun necesidad
	}
	
	public static void createTableSystemVersion() throws Exception 
	{
		String sSQL = null ;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();

			//Determinar si existe tabla si no: creamos
			rs = conn.getMetaData().getTables(conn.getCatalog(), null, "systemversion", null);
			if (!rs.next()) {
				//Crear estructura inicial registro versiones
				sSQL = " CREATE TABLE  systemversion (\"version\" TEXT) ";
				logger.debug(" SQL: " + sSQL);
				pst = conn.prepareStatement(sSQL);
				pst.executeUpdate();
				//Insertar modelo datos vacio
				sSQL = " INSERT INTO  systemversion VALUES " +
					   " ('<?xml version=\"1.0\" encoding=\"UTF-8\"?><modules></modules>')";
				logger.debug(" SQL: " + sSQL);
				pst = conn.prepareStatement(sSQL);
				pst.executeUpdate();
			} 
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
	}
	
	

	public static String getRegistry() throws Exception{

		logger.debug("getSystemVersion Inicio");
		String systemVersion = null ;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();
			//conn = getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();

			st = conn.createStatement();
			rs = st.executeQuery("SELECT version FROM systemversion ");
			while (rs.next()) {
				systemVersion = rs.getString("version");		
			}
			rs.close();
			st.close();


		} catch (Exception e) {
			logger.error("Error while connecting to the DB:");
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally{
			ConnectionUtilities.closeConnection(conn,st,rs);
		}
		logger.info("getSystemVersion Fin");
		return systemVersion;

	}


	public static boolean updateSystemVersion(String version) throws Exception{

		logger.debug("updateSystemVersion Inicio");
		boolean state = false;
		String sSQL = null ;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection conn = null;
		try {

			//conn = getConnection();
			// Conexion a la BD
			conn = getDataSource().getConnection();

			if(conn == null || conn.isClosed()) throw new Exception ();

			sSQL = "UPDATE systemversion SET version = ? ";
			pst = conn.prepareStatement(sSQL);
			pst.setString(1, version);
			pst.executeUpdate();	
			state = true;
			
			

		} catch (Exception e) {
			logger.error("Error while connecting to the DB:");
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		logger.info("updateSystemVersion Fin");
		return state;

	}
	
	public static boolean activeModule(String module, boolean active) throws Exception  {
		logger.debug("activeModule Inicio");
		boolean state = false;
		String sSQL = null ;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection conn = null;
		try {

			//conn = getConnection();
			// Conexion a la BD
			conn = getDataSource().getConnection();

			if(conn == null || conn.isClosed()) throw new Exception ();
			
			//sSQL = "UPDATE apps SET active=false WHERE app=? ";
			sSQL = "UPDATE apps SET active=? WHERE install_name=? ";
			pst = conn.prepareStatement(sSQL);
			pst.setBoolean(1, active);
			pst.setString(2, module);
			pst.executeUpdate();	
			state = true;
			logger.info("activeModule Modulo: " + module + " active: " + active);

		} catch (Exception e) {
			logger.error("Error while connecting to the DB:");
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		logger.info("activeModule Fin");
		return state;
	}

}
/**
 * OperacionesBBDD.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
