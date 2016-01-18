/**
 * LocalgisManagerBuilderSingleton.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.localgiscoreutils;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.LocalgisManagerBuilderFactory;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;

public class LocalgisManagerBuilderSingleton {
	
	private static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	private static LocalgisManagerBuilder localgisManagerBuilder = null;
	
	
	public static LocalgisManagerBuilder getLocalgisManagerBuilder() {
		return getLocalgisManagerBuilder(true);
	}
	public static LocalgisManagerBuilder getLocalgisManagerBuilder(boolean useJDBCEnvironment) {
		try {
			if (localgisManagerBuilder != null){
				return localgisManagerBuilder;
			}
			else {
				// Configurando el core de localgis
				String dbUser = appContext.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER);
				String dbPass = appContext.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
				String driverName = appContext.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);
				String url = appContext.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL);
				String host = ConnectionUtils.getHostname(url);
				String port = ConnectionUtils.getPort(url);				
				String dbName = ConnectionUtils.getDatabaseName(url);
				String mapServerUrl = appContext.getString(UserPreferenceConstants.MAPSERVER_URL);
									
				Configuration.setPropertyString("db.username", dbUser);
				Configuration.setPropertyString("db.password", dbPass);
				Configuration.setPropertyString("db.host", host);
				Configuration.setPropertyString("db.port", port);
				Configuration.setPropertyString("db.name", dbName);
				Configuration.setPropertyString("jdbc.driver", driverName);				
				Configuration.setPropertyString("mapserver.url.server", mapServerUrl);
				
				localgisManagerBuilder = LocalgisManagerBuilderFactory.createLocalgisManagerBuilder(useJDBCEnvironment);
				return localgisManagerBuilder;
			}
		} catch (LocalgisConfigurationException e) {			
			e.printStackTrace();
		} catch (LocalgisInitiationException e) {			
			e.printStackTrace();
		}
		
		return localgisManagerBuilder;
	}
}
