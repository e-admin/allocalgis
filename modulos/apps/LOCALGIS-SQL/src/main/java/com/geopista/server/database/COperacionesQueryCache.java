/**
 * COperacionesQueryCache.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class COperacionesQueryCache {

	private static Logger log = Logger.getLogger(COperacionesQueryCache.class);
	static HashMap queryCache = new HashMap();

	public static String getQueryCache(Connection connection, String idQuery) {

		PreparedStatement psSQL=null;
		ResultSet rsSQL=null;
		String query=null;

		try {

			if (queryCache.containsKey(idQuery)) {
				//log.info("Query existe en la cache:");
				query = (String) queryCache.get(idQuery);
			} else {
				psSQL = connection.prepareStatement("select query from query_catalog where id=?");
				psSQL.setString(1, idQuery);
				rsSQL = psSQL.executeQuery();

				if (rsSQL.next())
					query = rsSQL.getString("query");

				//log.info("Ejecutando query: " + query);

				if (query != null) {
					queryCache.put(idQuery, query);
				}
				
			}
		} catch (Exception e) {

		} finally {
			try {
				if (rsSQL != null)
					rsSQL.close();
				if (psSQL != null)
					psSQL.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return query;

	}
	
	
}
