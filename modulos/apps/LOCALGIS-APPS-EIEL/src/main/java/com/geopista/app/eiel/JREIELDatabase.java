/**
 * JREIELDatabase.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.JReportDatabase;
import com.geopista.util.config.UserPreferenceStore;

public class JREIELDatabase extends JReportDatabase{
	
	protected static Logger logger = Logger.getLogger(JREIELDatabase.class);
	 /**
     * Obtiene una conexión con la base de datos
     * @return
     */
    public static Connection getConnection() {
    	com.geopista.app.AppContext appContext = (com.geopista.app.AppContext) com.geopista.app.AppContext.getApplicationContext();
		//String url = appContext.getString("conexion.url.jasper");
		//String user = appContext.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER);
		String url=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL,"",false);
		String user=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER,"consultas",false);
		
		String pass =appContext.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
		//String pass = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);
		String driver = appContext.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);

		Connection conn = null;

		try {
			if (url.startsWith("jdbc:rmi")) {
	            conn = getDBConnection (user,pass,url, "org.objectweb.rmijdbc.Driver",true);         
			} else {
	            conn = getDBConnection (user,pass,url, driver,true);         
			}

		} catch (SQLException e1) {
//			throw new ReportProcessingException();
			//logger.error("Contraseña del usuario:"+UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false));
			e1.printStackTrace();
		}

		return conn;
	};
	
}
