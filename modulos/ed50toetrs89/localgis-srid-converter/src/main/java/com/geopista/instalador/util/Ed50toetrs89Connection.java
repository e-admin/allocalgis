/**
 * Ed50toetrs89Connection.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.instalador.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;



import com.geopista.instalador.UTMED50ToGeoETRS89Converter;

public class Ed50toetrs89Connection {
	
	
	private static final String DDBB_DRIVERCLASSNAME = "database.driverClassName";
	private static final String DDBB_SERVERNAME = "database.serverName";
	private static final String DDBB_PORT = "database.port";
	private static final String DDBB_NOMBREBD = "database.nombreBD";
	private static final String DDBB_PASSWORD = "database.password";
	private static final String DDBB_USERNAME = "database.username";

	public static Connection getConnection(final Properties properties)
			throws SQLException, ClassNotFoundException {
	
		Connection con = null;
		final String driverClassName = properties
				.getProperty(DDBB_DRIVERCLASSNAME);
		final String serverName = properties.getProperty(DDBB_SERVERNAME);
		final String port = properties.getProperty(DDBB_PORT);
		final String nombreBD = properties.getProperty(DDBB_NOMBREBD);
		final String user = properties.getProperty(DDBB_USERNAME);
		final String password = properties.getProperty(DDBB_PASSWORD);
	
		Class.forName(driverClassName);
	
		UTMED50ToGeoETRS89Converter.printMessage(Messages.getString("UTMED50ToGeoETRS89Converter.34"));
		DriverManager.setLoginTimeout(10);
		con = DriverManager.getConnection(serverName + ":" + port + "/"
				+ nombreBD, user, password);
	
		UTMED50ToGeoETRS89Converter.printMessage(Messages.getString("UTMED50ToGeoETRS89Converter.37"));
	
		/*
		 * Add the geometry types to the connection. Note that you must cast the
		 * connection to the pgsql-specific connection * implementation before
		 * calling the addDataType() method.
		 */
		((org.postgresql.jdbc3.Jdbc3Connection) con).addDataType("geometry",
				"org.postgis.PGgeometry");
		((org.postgresql.jdbc3.Jdbc3Connection) con).addDataType("box3d",
				"org.postgis.PGbox3d");
	
		con.setAutoCommit(true);
	
		return con;
	}

	public static void closeConnection(Connection con, PreparedStatement ps,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
	
			}
		} catch (Exception e) {
			UTMED50ToGeoETRS89Converter.log.error(e);
		}
	
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
			UTMED50ToGeoETRS89Converter.log.error(e);
		}
	
		try {
			if (con != null) {
				con.close();
				UTMED50ToGeoETRS89Converter.printMessage(Messages
						.getString("UTMED50ToGeoETRS89Converter.137"));
			}
		} catch (Exception e) {
			UTMED50ToGeoETRS89Converter.log.error(e);
		}
	}

}
