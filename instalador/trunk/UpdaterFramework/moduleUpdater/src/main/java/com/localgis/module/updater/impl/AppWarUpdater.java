/**
 * AppWarUpdater.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.util.FileUtils;

import com.localgis.module.utilitys.UpdaterUtilities;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.exception.DependencyViolationException;

public class AppWarUpdater extends AbstractLocalGISUpdater {

	private static final String LOCALGIS_APP_WAR_DEPLOY_PATH = "localgis_appWAR_deployPath";
	private static final String LOCALGIS_APP_URL_SERVER = "localgis_app_url_server";
	
	
	public void install() throws DependencyViolationException {
		try {
			installModule(getModule(), getBinaryArtifact());
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void uninstall() throws DependencyViolationException {
		try {
			uninstallModule(getModule());
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean checkInstallationValidity() {
		try {
			return checkInstallationModule(getModule());
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean checkUninstallationValidity() {
		try {
			return checkUninstallationModule(getModule());
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public void upgrade() throws DependencyViolationException {
		String nameModule = "";
		String mensajeBase = getMessageResource("text.operacion.upgrade");
		try {
			nameModule = getFullFinalNameArtifact(module);
			this.userInterfaceFacade.notifyActivity(mensajeBase+ ": " + nameModule);
			//Desinstalacion modulo version anterior (al que actuliza el modulo actual)
			uninstallModule(getModule().getUpgradableMod());
			//Nueva instalacion
			install();
			showMessageSuccess(mensajeBase);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
	}
	
	
	public void installOrUpgrade() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Instalacion modulo war: 
	 * Reemplazo de properties
	 * Despliegue de aplicacion: copia fichero en directorio de despliegue y realiza validacion
	 * @param module
	 * @param binaryArtifact
	 * @throws DependencyViolationException
	 */
	private void installModule(Module module, Artifact binaryArtifact) throws DependencyViolationException {
		String nameModule = "";
		File updatedWar = null;
		File moduleAppWar = null;
		File moduleTargetFile = null;
		File targetDirectory = null;
		String deployAppWarPath = "";
		String mensajeBase = getMessageResource("text.operacion.install");
		try {
			nameModule = getFullFinalNameArtifact(module);
			this.userInterfaceFacade.notifyActivity(mensajeBase + ": " + nameModule);
			//Control servidor dispobible
			if (checkServer()) 
			{
				//Fichero procesar y rutas de destino
				moduleAppWar = binaryArtifact.getFile();
				deployAppWarPath = getOutputDeployPath();
				targetDirectory = new File(deployAppWarPath);
				moduleTargetFile = new File(targetDirectory, nameModule);
				//Reemplazar properties
				updatedWar = replaceProperties(moduleAppWar, properties);
				//Desplegamos aplicacion
				deployApp(updatedWar, moduleTargetFile);
				//Validacion
				boolean isDeploy = checkInstallationModule(module);
				if (!isDeploy) {
					uninstallModule(module);
					showMessageFailed(mensajeBase);
				}
				else 
					showMessageSuccess(mensajeBase);
			}
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Desinstalacion modulo war:
	 * Borrar fichero aplicacion correspondiente
	 * @param module
	 * @throws DependencyViolationException
	 */
	private void uninstallModule(Module module) throws DependencyViolationException {
		String nameModule = "";
		String mensajeBase = getMessageResource("text.operacion.uninstall");
		try {
			nameModule = getFullFinalNameArtifact(module);
			this.userInterfaceFacade.notifyActivity(mensajeBase + ": " + nameModule);
			//Control servidor dispobible determinar que se desea hacer
			if (checkServer()) 
			{
				//Eliminar despliegue
				undeployApp(getOutputDeployPath(), nameModule);
				//Validacion
				boolean isUndeploy = checkUninstallationModule(module);
				if (!isUndeploy)
					showMessageFailed(mensajeBase);
				else 
					showMessageSuccess(mensajeBase);
			}
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Comprueba instalacion correcta de aplicacion
	 * @param module
	 * @return
	 */
	private boolean checkInstallationModule(Module module) {
		boolean checkInstal = false;
		String nameModule = "";
		String mensajeBase = getMessageResource("text.operacion.checkInstall");
		try {
			nameModule = getFullFinalNameArtifact(module);
			this.userInterfaceFacade.notifyActivity(mensajeBase + ": " + nameModule);
			//Validar instalacion: comprueba existencia directorio despliegue
			checkInstal = isDeployValid (getOutputDeployPath(), nameModule);
			//Mensaje estado instalacion
			String mensajeEstado = getMessageResource("text.resultadoOperacion.instalacionCompletada");
			if (checkInstal)
				showMessageSuccess(mensajeEstado);
			else
				showMessageFailed(mensajeEstado);
			//Mensaje fin proceso
			showMessageSuccess(mensajeBase);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
		return checkInstal;
	}
	
	/**
	 * Comprueba desinstalacion correcta de aplicacion
	 * @param module
	 * @return
	 */
	private boolean checkUninstallationModule(Module module) {
		boolean checkUninstal = false;
		String nameModule = "";
		String mensajeBase = getMessageResource("text.operacion.checkUninstall");
		try {
			nameModule = getFullFinalNameArtifact(module);
			this.userInterfaceFacade.notifyActivity(mensajeBase + ": " + nameModule);
			//Validar instalacion: comprueba existencia directorio despliegue
			checkUninstal = isUndeployValid (getOutputDeployPath(), nameModule);
			//Mensaje estado instalacion
			String mensajeEstado = getMessageResource("text.resultadoOperacion.desinstalacionCompletada");
			if (checkUninstal)
				showMessageSuccess(mensajeEstado);
			else
				showMessageFailed(mensajeEstado);
			//Mensaje fin proceso
			showMessageSuccess(mensajeBase);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
		return checkUninstal;
	}

	
	/**
	 * Retorna directorio destino para despliegue (tomcat)
	 * @return
	 */
	protected String getOutputDeployPath () {
		return properties.getProperty(LOCALGIS_APP_WAR_DEPLOY_PATH);
	}
	/**
	 * Retorna url de acceso al servidor para control de servidor activo (tomcat)
	 * @return
	 */
	protected String getUrlServer () {
		return properties.getProperty(LOCALGIS_APP_URL_SERVER);
	}
	
	/**
	 * Comprobamos disponibilidad del servidor
	 * @return Con comprobar que tenemos acceso a alguna operacion del servidor es suficiente
	 */
	private boolean checkServer () {
		Exception exception = null;
		boolean isServerActive = false;
		try {
			URL url=new URL(getUrlServer());
			Object content = url.getContent();
			isServerActive = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			exception = e;
		} catch (IOException e) {
			showMessageFailed(getMessageResource("text.resultadoOperacion.servidorNoDisponible"));
			exception = e;
		}
		
		//Si el servidor no esta disponible: interrumpir accion
		if (!isServerActive)
			throw new RuntimeException(exception);
		
		return isServerActive;
	}
	
	/**
	 * Parse properties generando fichero para despliegue
	 * @param moduleApp
	 * @param properties
	 * @return
	 */
	private File replaceProperties (File moduleApp, Properties properties) {
		File updatedApp = null;
		String mensajeBase = getMessageResource("text.proceso.reemplazoProperties");
		try {
			//Reemplazar properties
			showMessageInitProcess(mensajeBase);
			updatedApp = UpdaterUtilities.replaceJarFiles(moduleApp, properties);
			showMessageSuccess(mensajeBase);
		} catch (IOException e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
		return updatedApp;
	}
	
	/**
	 * Realiza proceso de despliegue: copia fichero app en directorio despliegue y comprueba despliegue realizado
	 * @param fileApp
	 * @param moduleTargetFile
	 * @return
	 */
	private void deployApp (File fileApp, File moduleTargetFile) {
		String mensajeBase = getMessageResource("text.proceso.desplegandoAplicacion");
		try {
			//Copiamos war a directorio para despliegue
			showMessageInitProcess(mensajeBase);
			FileUtils.copyFile(fileApp, moduleTargetFile);
			showMessageSuccess(mensajeBase);
		} catch (IOException e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Control de espera para verificacion deploy de la aplicacion
	 * @param deployAppWarPath
	 * @param nameDeployApp
	 * @return
	 */
	protected boolean isDeployValid (String deployAppWarPath, String nameDeployApp) {
		String mensajeBase = getMessageResource("text.proceso.tiempoEsperaDeploy");
		//Comprobar operacion deploy
		return checkOperacionRealizada(true, deployAppWarPath, nameDeployApp, mensajeBase);
	}
	
	/**
	 * Deshacer despliegue: eliminar app
	 * @param appPath
	 * @param nameApp
	 */
	private void undeployApp (String appPath, String nameApp) {
		String mensajeBase = getMessageResource("text.proceso.eliminandoDespliegueAplicacion");
		try {
			//Eliminar fichero despliegue aplicacion
			showMessageInitProcess(mensajeBase);
			File fileApp = new File(appPath, nameApp);
			if (fileApp != null && (fileApp.exists() && fileApp.isFile()))
				fileApp.delete();
			showMessageSuccess(mensajeBase);
		}catch (Exception e) {
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Control de espera para verificacion deploy de la aplicacion
	 * @param appPath
	 * @param nameApp
	 * @return
	 */
	protected boolean isUndeployValid (String appPath, String nameApp) {
		String mensajeBase = getMessageResource("text.proceso.tiempoEsperaUndeploy");
		//Comprobar operacion undeploy
		return checkOperacionRealizada(false, appPath, nameApp, mensajeBase);
	}
	
	
	/**
	 * Control de espera para verificacion de operacion deploy/undeploy segun parametros enviados
	 * @param checkDeploy:	True comprueba deploy/False compueba undeploy
	 * @param appPath
	 * @param nameApp
	 * @param mensajeBase
	 * @return
	 */
	private boolean checkOperacionRealizada (boolean checkDeploy, String appPath, String nameApp, String mensajeBase) {
		int intentosDeploy = 0;
		boolean operacionRealizada = false;
		
		int maxIntentos = getNumMaxIntentosEspera();
		int sleepTime = getIntervaloTiempoEspera();
			
		try {
			showMessageInitProcess(mensajeBase);
			//Control de espera por intentos u operacion realizada
			while ((intentosDeploy < maxIntentos) && !operacionRealizada) 
			{
				//Comprobamos si se ha realizado operacion deploy/undeploy realizada segun parametros enviados (para undeploy NO debe existir directorio)
				if (checkDeploy)
					operacionRealizada = existDirectory(appPath, nameApp);
				else
					operacionRealizada = !existDirectory(appPath, nameApp);
				
				if (!operacionRealizada) {
					intentosDeploy ++;
					try {
						this.getUserInterfaceFacade().reportProgress(getMessageResource("text.proceso.puntoEspera"), intentosDeploy, maxIntentos);
						Thread.sleep(sleepTime);
					}catch(Exception e){
						showMessageFailed(mensajeBase);
						throw new RuntimeException(e);
					}
				}
			}
			
			//Informar si se realizado despliegue
			if (operacionRealizada)
				showMessageSuccess(mensajeBase);
			else 
				showMessageFailed(mensajeBase + getMessageResource("text.resultadoOperacion.agotadoTiempoEspera"));
		}catch(Exception e){
			showMessageFailed(mensajeBase);
			throw new RuntimeException(e);
		}
		
		return operacionRealizada;
	}
	
	/**
	 * Comprueba si se ha generado el directorio deploy
	 * @param appPath
	 * @param nameApp
	 * @return
	 */
	private boolean existDirectory (String appPath, String nameApp) {
		boolean existe = false;
		if (nameApp != null) {
			//Obtener nombre del directorio deploy y comprobar si existe
			String nameDeploy = nameApp.substring(0, nameApp.lastIndexOf('.'));
			File fileDeploy = new File(appPath, nameDeploy);
			existe = ((fileDeploy != null) && (fileDeploy.exists() && fileDeploy.isDirectory()));
		}
		return existe;
	}

	/**
	 * Numero de intentos maximos para realizar comprobacion de operacion
	 * @return
	 */
	private int getNumMaxIntentosEspera () {
		//FIXME Establecer mediante properties ¿?
		int numIntentos = 2;
		return numIntentos;
	}
	
	/**
	 * Duracion del intervalo (en ms) entre intentos de comprobacion de operacion
	 * @return
	 */
	private int getIntervaloTiempoEspera () {
		//FIXME Establecer mediante properties ¿?
		int sleepTime = 20000;
		return sleepTime;
	}
}
