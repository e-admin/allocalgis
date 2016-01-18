/**
 * TestDataTypes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.layerutil.datosexternos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.prefs.Preferences;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.geopista.ui.plugin.external.ConnectionUtility;
import com.geopista.ui.plugin.external.ExternalDataSource;




public class TestDataTypes extends QueryUtility {

	
	public TestDataTypes() {
		super();
	}

	public static void main(String args[]) {
		
		Hashtable drivers = ConnectionUtility.findAllDrivers();
		ConnectionUtility.loadDrivers(drivers);

		/*
		 * -Dgeopista.jdbc.driver.oracle.class=oracle.jdbc.driver.OracleDriver
		 * -Dgeopista.jdbc.driver.oracle.url=jdbc:oracle:thin:@<server>[:1521]:database_name
		 * -Dgeopista.jdbc.driver.postgres.class=org.postgresql.Driver
		 * -Dgeopista.jdbc.driver.postgres.url=jdbc:postgresql:[<//host>[:<5432>/]]<database>
		 * -Dgeopista.jdbc.driver.mysql.class=com.mysql.jdbc.Driver
		 * -Dgeopista.jdbc.driver.mysql.url=jdbc:mysql://&lt;hostname&gt;[:&lt;3306&gt;]/&lt;dbname&gt;
		 */
		ExternalDataSource externalDataSource = new ExternalDataSource();

		String query = null;
		String table = null;
		final int POSTGRES = 1;
		final int ORACLE = 2;
		final int MYSQL = 3;
		final int SQLSERVER = 4;
		
		int bd = ORACLE;
		switch (bd) {
		case POSTGRES:
			externalDataSource.setConnectString("jdbc:postgresql://localgis:5432/geopista");
			externalDataSource.setDriver("org.postgresql.Driver");
			externalDataSource.setUserName("geopista");
			externalDataSource.setPassword("geopista");
			query = "select * from AA_persona";
			table = "AA_PERSONA";
			break;
		case ORACLE:
			externalDataSource.setConnectString("jdbc:oracle:thin:@claudia.malab.satec.es:1521:DBNRED");
			externalDataSource.setDriver("oracle.jdbc.driver.OracleDriver");
			externalDataSource.setUserName("chat");
			externalDataSource.setPassword("satec");
			query = "select * from AA_persona";
			table = "AA_PERSONA";
			break;
		case MYSQL:
			externalDataSource.setConnectString("jdbc:mysql://claudia.malab.satec.es:3306/santanyi");
			externalDataSource.setDriver("com.mysql.jdbc.Driver");
			externalDataSource.setUserName("santanyi");
			externalDataSource.setPassword("santanyi");
			query = "select * from AA_persona";
			table = "AA_PERSONA";
			break;
		case SQLSERVER:
			externalDataSource.setConnectString("jdbc:mysql://claudia.malab.satec.es:3306/santanyi");
			externalDataSource.setDriver("com.mysql.jdbc.Driver");
			externalDataSource.setUserName("santanyi");
			externalDataSource.setPassword("santanyi");
			query = "select * from AA_persona";
			table = "AA_PERSONA";
			break;
		}

		new QueryUtility().creaColumnas(query, table, externalDataSource);

	}

	public boolean creaColumnas(String select, String nombreTabla,
			ExternalDataSource externalDataSource) {

		TablesDBOperations operaciones = new TablesDBOperations();
		ColumnRow columnaRow = new ColumnRow();
		Statement stmt;
		ResultSet rs = null;
		Connection connection = ConnectionUtility.getConnection(externalDataSource);
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(select);
			ResultSetMetaData rsmd = rs.getMetaData();

			if (rsmd != null) {
				int numColumnas = rsmd.getColumnCount();

				/*
				 * Determinamos a traves de la información de preferencia el
				 * tipo de base de datos de geopista para el fichero de
				 * configuración:
				 */

				Preferences pref = Preferences.userRoot().node(GEOPISTA_PACKAGE);
				String url = pref.get(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL, "");
				String bbdd = "pgsql";
				if (url.indexOf("jdbc:postgresql") >= 0) {
					bbdd = "pgsql";
				} else if (url.indexOf("jdbc:oracle") >= 0) {
					bbdd = "oracle";
				}

				for (int i = 1; i <= numColumnas; i++) {
					System.out.println("NOMBRE=" + rsmd.getColumnName(i)
							+ " | NOMBRETIPO=" + rsmd.getColumnTypeName(i)
							+ " | PRECISION=" + rsmd.getPrecision(i)
							+ " | SCALE=" + rsmd.getScale(i)
							+ " | DISPLAYSIZE=" + rsmd.getColumnDisplaySize(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				rs.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return false;
	}
}
