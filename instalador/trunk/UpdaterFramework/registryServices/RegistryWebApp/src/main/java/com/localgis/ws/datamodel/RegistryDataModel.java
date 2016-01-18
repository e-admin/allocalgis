package com.localgis.ws.datamodel;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.localgis.ws.ln.OperacionesBBDD;

public class RegistryDataModel implements ServletContextListener {

	static Log logger = LogFactory.getLog(RegistryDataModel.class);

	public void contextInitialized(ServletContextEvent e) {
		try {
			//Proceso modelo de datos para registro web de control de versiones
			logger.debug("Iniciar datamodel RegistryWS");
			processDataModelRegistryWS ();
			//Proceso modelo de datos para registro web de control de versiones
			logger.info("Datamodel RegistryWS cargado ...");
		} catch (Exception exception) {
			logger.error("Error al inicializar datamodel RegistryWS:" + exception.getMessage());
			logger.error(exception.getStackTrace());
		}
		//TODO:
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	private void processDataModelRegistryWS () {
		try {
			//Procesar creacion datamodel registry
			OperacionesBBDD.installRegistryDataModel();
			//Procesasr update datamodel registry
			OperacionesBBDD.updateRegistryDataModel();
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}

}/**
 * RegistryDataModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * RegistryDataModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
