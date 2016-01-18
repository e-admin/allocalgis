package com.geopista.app.reports.localgiscoreutils;

import com.geopista.app.AppContext;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.LocalgisManagerBuilderFactory;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;

public class LocalgisManagerBuilderSingleton {
	
	private static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	private static LocalgisManagerBuilder localgisManagerBuilder = null;
	
	public static LocalgisManagerBuilder getLocalgisManagerBuilder() {
		try {
			if (localgisManagerBuilder != null){
				return localgisManagerBuilder;
			}
			else {
				// Configurando el core de localgis
				String dbUser = appContext.getString("conexion.user");
				String dbPass = appContext.getString("conexion.pass");
				String driverName = appContext.getString("conexion.driver");
				String url = appContext.getString("conexion.url");
				String host = ConnectionUtils.getHostname(url);
				String port = ConnectionUtils.getPort(url);				
				String dbName = ConnectionUtils.getDatabaseName(url);
				String mapServerUrl = appContext.getString(AppContext.URL_MAPSERVER);
									
				Configuration.setPropertyString("db.username", dbUser);
				Configuration.setPropertyString("db.password", dbPass);
				Configuration.setPropertyString("db.host", host);
				Configuration.setPropertyString("db.port", port);
				Configuration.setPropertyString("db.name", dbName);
				Configuration.setPropertyString("jdbc.driver", driverName);				
				Configuration.setPropertyString("mapserver.url.server", mapServerUrl);
				
				localgisManagerBuilder = LocalgisManagerBuilderFactory.createLocalgisManagerBuilder();
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
