/**
 * JReportDatabase.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.geopista.app.utilidades.UtilsDrivers;

public class JReportDatabase {
	
	protected static Logger logger = Logger.getLogger(JReportDatabase.class);
	 	
	public static Connection getConnection(String url,String user,String pass,String driver) {
		return getConnection(url,user,pass,driver,true);
	}
		
	public static Connection getConnection(String url,String user,String pass,String driver,boolean registerDriver) {
		Connection conn = null;

		try {
			if (url.startsWith("jdbc:rmi")) {
	            conn = getDBConnection (user,pass,url, "org.objectweb.rmijdbc.Driver",registerDriver);         
			} else {
	            conn = getDBConnection (user,pass,url, driver,registerDriver);         
			}

		} catch (SQLException e1) {
//			throw new ReportProcessingException();
			logger.error("Contraseña del usuario:"+pass);
			e1.printStackTrace();
		}

		return conn;
	}
	
	
	protected static Connection getDBConnection (String username, String password,
			String thinConn, String driverClass,boolean registerDriver) throws SQLException{
		Connection con = null;

		try {
			if (registerDriver)
				UtilsDrivers.registerDriver(driverClass);
			//Class.forName(driverClass);
			
			con= DriverManager.getConnection(thinConn, username, password);
			con.setAutoCommit(false);
		} catch (Exception e){
			logger.error("Error al obtener la conexion con la base de datos.", e);
		}

		return con;
	}
	
}
