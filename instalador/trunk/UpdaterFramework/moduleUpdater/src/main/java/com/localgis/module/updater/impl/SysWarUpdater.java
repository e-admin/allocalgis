/**
 * SysWarUpdater.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater.impl;

import java.io.File;


public class SysWarUpdater extends AppWarUpdater
{
	private static final String LOCALGIS_SYS_WAR_DEPLOY_PATH = "localgis_sysWAR_deployPath";
	private static final String LOCALGIS_SYS_URL_SERVER = "localgis_sys_url_server";

	/**
	 * Retorna directorio destino para despliegue (jetty)
	 * @return
	 */
	@Override
	protected String getOutputDeployPath() {
		return properties.getProperty(LOCALGIS_SYS_WAR_DEPLOY_PATH);
	}

	/**
	 * Retorna url de acceso al servidor para control de servidor activo (jetty)
	 * @return
	 */
	@Override
	protected String getUrlServer() {
		return properties.getProperty(LOCALGIS_SYS_URL_SERVER);
	}
	
	
	protected boolean isDeployValid (String deployAppWarPath, String nameDeployApp) {
		//FIXME: Pendiente determinar condiciones para validacion de instalacion
		return true;
	}

	protected boolean isUndeployValid (String appPath, String nameApp) {
		//FIXME: Pendiente determinar condiciones para validacion de instalacion
		return true;
	}
	
}
