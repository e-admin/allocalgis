/**
 * LaunchUninstallMojo.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.maven.plugins.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.codehaus.plexus.components.interactivity.Prompter;

import com.localgis.maven.plugin.AbstractUpdaterMojo;
import com.localgis.module.updater.PrompterUserInterface;
import com.localgis.module.updater.UpdaterUserInferfaceHook;
import com.localgis.module.updater.database.OperationsDataBase;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.exception.ModuleNotFoundException;
import com.localgis.tools.modules.exception.XMLError;
import com.localgis.tools.modules.impl.ArtifactImpl;
import com.localgis.tools.modules.impl.ModuleImpl;
import com.localgis.tools.modules.install.LocalGISInstallation;
import com.localgis.tools.modules.install.ReadOnlyURLInstallation;
import com.localgis.tools.modules.install.WebServiceURLInstallation;

/**
 * Obtiene las dependencias necesarias para realizar la instalaci처n completa e
 * itera en el orden adecuado lanzando los procesos de instalaci처n de cada uno
 * de los paquetes.
 * 
 * La secuencia de pasos realizada es:
 * 
 * - Resuelve las dependencias de m처dulos. - Planifica el orden de iteraci처n.
 * - Descarga la descripci처n de la instalaci처n local y todas las dependencias
 * en el repositorio local. - Itera configurando los paths de las dependencias y
 * los objetos de contexto de cada actualizador y ejecutando el m챕todo de
 * entrada de la actualizaci처n o instalaci처n.
 * 
 * @author dcaaveiro
 * @goal launchUninstall
 * @requiresProject false
 */
public class LaunchUninstallMojo extends AbstractUpdaterMojo {
	private static final String REGISTRY_WEB_SERVICE = "WebService";
	private static final String REGISTRY_READ_ONLY = "ReadOnly";

	private static final String LOCALGIS_APP_URL_SERVER = "localgis_app_url_server";
	private static final String LOCALGIS_DATABASE_URL = "localgis_database_url";
	private static final String LOCALGIS_DATABASE_PASSWORD = "localgis_database_password";
	private static final String LOCALGIS_DATABASE_USERNAME = "localgis_database_username";
	private static final String LOCALGIS_PSQL_PATH = "localgis_psql_path";

	/**
	 * URL for installation descriptor. If URL can't be opened an empty
	 * installation is used
	 * 
	 * @required
	 * @parameter expression="${installationURL}"
	 */
	public URL installationURL;
	/**
	 * Type of the registry specified by {@link #installationURL} valores
	 * {@value #REGISTRY_READ_ONLY}, {@value #REGISTRY_WEB_SERVICE}
	 * 
	 * @parameter expression="${registryType}"
	 */
	public String registryType = REGISTRY_WEB_SERVICE;
	/**
	 * File with installation properties. If not provided install.properties is
	 * used
	 * 
	 * @parameter expression="${propertiesFile}"
	 *            default-value="install.properties"
	 */
	public File propertiesFile;
	/**
	 * Fails if there are no response from installationURL
	 * 
	 * @parameter expression="${failsIfBadInstallationURL}"
	 *            default-value="false"
	 */
	public boolean failsIfBadInstallationURL = false;

	/**
	 * Disabled@required false Disabled@ parameter expression=
	 * "${component.com.localgis.module.updater.UpdaterUserInferfaceHook}"
	 * default-value=null
	 */
	public UpdaterUserInferfaceHook userInterfaceHook = null;

	/**
	 * 
	 * 
	 * @parameter expression="${checkServers}" default-value="true"
	 */
	public boolean checkServers = true;

	/**
	 * @component
	 */
	Prompter prompter;
	private Properties properties;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
//	    if (this.outputDir==null)
//		this.outputDir= new File(".");
		this.properties = new Properties();
		try {
			this.properties.load(new FileInputStream(this.propertiesFile));
		} catch (FileNotFoundException e1) {
			throw new MojoExecutionException("Properties File not found:"
					+ this.propertiesFile.getAbsolutePath(), e1);
		} catch (IOException e1) {
			throw new MojoExecutionException("Can't read properties file:"
					+ this.propertiesFile.getAbsolutePath(), e1);
		}

		this.userInterfaceHook = new PrompterUserInterface(this.prompter);

		this.userInterfaceHook.showMessage("Module: "
				+ this.artifactId);

		// Comprobar disponibilidad servidores: detener instalacion si no estan
		// disponibles
		if (!checkActiveServers())
			throw new MojoExecutionException(
					"Server Unavailable: Verify that all servers are available");
		else {
			this.userInterfaceHook.showMessage("Perform the installation of the module:"
							+ this.artifactId);
		
			// TODO Ask User interface if provided
			if (this.userInterfaceHook != null) {
				getLog().info(" Interactive Session.");
			} else {
				getLog().info(" Non-Interative Session.");
			}
			try {
				
				Module module = new ModuleImpl(this.artifactId, this.version);
				module.setDescription(this.artifactId);
		
				removeModule(module);
			} catch (ArtifactResolutionException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			} catch (ArtifactNotFoundException e) {
				this.getLog().error("A needed artifact was not found!");
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			} catch (ProjectBuildingException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			} catch (InvalidDependencyVersionException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			} catch (IOException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);		
			} catch (DependencyViolationException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			} catch (ModuleNotFoundException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			}
		}	
	}

	/**
	 * Gets an {@link ModuleDependencyTree} representing the actual installed
	 * modules Can return instances of {@link LocalGISInstallation}
	 * {@link ReadOnlyURLInstallation}
	 * 
	 * @param installationURL
	 * @param fails
	 * @param mojo
	 * @return
	 * @throws MojoFailureException
	 * @throws IOException
	 * @throws XMLError
	 */
	public ModuleDependencyTree getInstallationRegistry(URL installationURL,
			boolean fails, Mojo mojo) throws MojoFailureException, IOException,
			XMLError {
		ModuleDependencyTree registry = null;
		try {
			if (REGISTRY_READ_ONLY.equals(this.registryType)) {
				// Obtener datos instalacion actual
				registry = new ReadOnlyURLInstallation(installationURL);
			} else if (REGISTRY_WEB_SERVICE.equals(this.registryType)) {
				getLog().info(
						"Contacting with WebService at: " + installationURL);
				// Obtener conexion servicio
				registry = new WebServiceURLInstallation(installationURL);
				// Obtener datos instalacion actual
				((WebServiceURLInstallation) registry)
						.loadInstallationFromWebService();
			} else {
				getLog().warn(
						" Registry type " + this.registryType
								+ " not supported. Please use -DregistryType=["
								+ REGISTRY_READ_ONLY + "]|"
								+ REGISTRY_WEB_SERVICE);
				throw new IllegalArgumentException("Registry type "
						+ this.registryType + " not supported.");
			}
		} catch (IOException e) {
			getLog().warn(
					"Installation can't be obtained at " + this.installationURL);
			if (fails) {
				getLog().error(
						"If this is a new installation use -DfailIfBadInstallationURL=false to allow clean installation of registry services.");
				throw new MojoFailureException(
						"Installation can't be obtained at "
								+ this.installationURL
								+ " Installation can't proceed.", e);
			}

			if (REGISTRY_READ_ONLY.equals(this.registryType)) {
				mojo.getLog().warn("Empty installation will be used.");
				registry = ReadOnlyURLInstallation.loadFromURL(mojo.getClass()
						.getResource("/installationEmpty.xml"));
			}
		}
		return registry;
	}

    
    private void removeModule(Module module) throws ArtifactResolutionException,
    ArtifactNotFoundException, ProjectBuildingException, InvalidDependencyVersionException, DependencyViolationException, MojoExecutionException,
    IOException, ModuleNotFoundException
	{
		this.getLog().info("============================================================");
		this.getLog().info("|DEACTIVATING MODULE: " + module);
		this.getLog().info("============================================================");
		
		new WebServiceURLInstallation(installationURL).removeModule(module);
		
		this.getLog().info("============================================================");
		this.getLog().info("|DEACTIVATION SUCCESS");
		this.getLog().info("============================================================");
	}

	/**
	 * Comprobamos disponibilidad del servidores
	 * 
	 * @return Con comprobar que tenemos acceso a alguna operacion del servidor
	 *         es suficiente
	 */
	private boolean checkActiveServers() {
		boolean activeServers = true;

		if (!this.checkServers)
			return true;

		this.userInterfaceHook.notifyActivity("CHECKING SERVERS ...");

		// Creamos lista con servidores a comprobar
		//List lstKeyUrl = Arrays.asList(new String[] {LOCALGIS_SYS_URL_SERVER,LOCALGIS_APP_URL_SERVER });
		List lstKeyUrl = Arrays.asList(new String[] {LOCALGIS_APP_URL_SERVER});

		// Comprobamos disponibilidad servidores de aplicacion
		for (int i = 0; i < lstKeyUrl.size(); i++)
			activeServers &= checkAppServer(this.properties.getProperty((String) lstKeyUrl.get(i)));
		// Comprobar disponibilidad servidores BdD
		activeServers &= checkBdDServer();
		
//		if (this.checkPsql)
//			activeServers &= checkPsql();

		this.userInterfaceHook.notifyActivity("CHECK SERVERS: "+ ((activeServers) ? "SUCCESS" : "FAILURE. Server Unavailable: Verify that all servers are available"));

		return activeServers;
	}

	/**
	 * Comprobamos disponibilidad del servidores
	 * 
	 * @param urlServer
	 * @return Con comprobar que tenemos acceso a alguna operacion del servidor
	 *         es suficiente
	 */
	private boolean checkAppServer(String urlServer) {
		boolean isActive = false;
		try {
			URL url = new URL(urlServer);
			Object content = url.getContent();
			isActive = true;
		} catch (MalformedURLException e) {
			isActive = false;
		} catch (IOException e) {
			isActive = false;
			//e.printStackTrace();
		}
		this.userInterfaceHook.showMessage(" Check server: " + urlServer + " ... " + ((isActive) ? "SUCCESS" : "FAILURE"));

		return isActive;
	}

	/**
	 * Comprobamos disponibilidad servidor BdD
	 * 
	 * @return Comprobamos que realiza conexion con parametros indicados
	 * @throws SQLException
	 */
	private boolean checkBdDServer() {
		Connection connection = null;
		boolean isActive = false;
		String dbURL = "";
		try {
			dbURL = this.properties.getProperty(LOCALGIS_DATABASE_URL);
			String dbUserName = this.properties.getProperty(LOCALGIS_DATABASE_USERNAME);
			String dbPassword = this.properties.getProperty(LOCALGIS_DATABASE_PASSWORD);
			connection = OperationsDataBase.getConnection(dbURL, dbUserName,dbPassword);
		} catch (SQLException e1) {
			isActive = false;
			//e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			isActive = false;
		} finally {
			try {
				if (connection != null) {
					connection.close();
					isActive = true;
				}
			} catch (SQLException e) {
				isActive = false;
			}
		}

		this.userInterfaceHook.showMessage(" Check BdD server: " + dbURL + " ... " + ((isActive) ? "SUCCESS" : "FAILIURE"));

		return isActive;
	}

	private boolean checkPsql() {

		boolean exists=false;
		
		String program = this.properties.getProperty(LOCALGIS_PSQL_PATH);
		if (program==null){
				this.userInterfaceHook.showMessage(" Check program: psql program no existe. Debe definir la variable:" + 
						LOCALGIS_PSQL_PATH + " en el fichero de propiedades "+((exists) ? "SUCCESS" : "FAILURE"));
				return false;
		}
		try {
			String[] args = {program, "---help" };
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(args);
			int exitVal = proc.waitFor();
			//System.out.println("Process exitValue: " + exitVal);
			exists=true;
		} catch (IOException e) {
			exists=false;
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		this.userInterfaceHook.showMessage(" Check program: " + program + " ... " + ((exists) ? "SUCCESS" : "FAILURE"));
		return exists;
	}
}
