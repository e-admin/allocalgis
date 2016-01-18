/**
 * LaunchInstallMojo.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.maven.plugins.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.twdata.maven.mojoexecutor.MojoExecutor;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;
import org.twdata.maven.mojoexecutor.MojoExecutor.ExecutionEnvironment;

import com.localgis.maven.plugin.AbstractUpdaterMojo;
import com.localgis.module.updater.PrompterUserInterface;
import com.localgis.module.updater.Updater;
import com.localgis.module.updater.UpdaterFactory;
import com.localgis.module.updater.UpdaterUserInferfaceHook;
import com.localgis.module.updater.database.OperationsDataBase;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.exception.ModuleNotFoundException;
import com.localgis.tools.modules.exception.XMLError;
import com.localgis.tools.modules.install.LocalGISInstallation;
import com.localgis.tools.modules.install.ReadOnlyURLInstallation;
import com.localgis.tools.modules.install.WebServiceURLInstallation;

/**
 * Obtiene las dependencias necesarias para realizar la instalación completa e
 * itera en el orden adecuado lanzando los procesos de instalación de cada uno
 * de los paquetes.
 * 
 * La secuencia de pasos realizada es:
 * 
 * - Resuelve las dependencias de módulos. - Planifica el orden de iteración.
 * - Descarga la descripción de la instalación local y todas las dependencias
 * en el repositorio local. - Itera configurando los paths de las dependencias y
 * los objetos de contexto de cada actualizador y ejecutando el método de
 * entrada de la actualización o instalación.
 * 
 * @author juacas
 * @goal launchInstall
 * @requiresProject false
 */
public class LaunchInstallMojo extends AbstractUpdaterMojo {
	private static final String REGISTRY_WEB_SERVICE = "WebService";
	private static final String REGISTRY_READ_ONLY = "ReadOnly";

	private static final String LOCALGIS_SYS_URL_SERVER = "localgis_sys_url_server";
	private static final String LOCALGIS_APP_URL_SERVER = "localgis_app_url_server";
	private static final String LOCALGIS_DATABASE_URL = "localgis_database_url";
	private static final String LOCALGIS_DATABASE_PASSWORD = "localgis_database_password";
	private static final String LOCALGIS_DATABASE_USERNAME = "localgis_database_username";
	private static final String LOCALGIS_PSQL_PATH = "localgis_psql_path";

	/**
	 * The Maven Session Object
	 * 
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	protected MavenSession session;
	/**
	 * The Maven PluginManager Object
	 * 
	 * @component
	 * @required
	 */
	protected PluginManager pluginManager;

	
	/**
	 * Output directory for the files needed to install a module: - registry
	 * descriptor. - pom.xml
	 * 
	 * each module will create a folder.
	 * 
	 * @parameter expression="${outputDir}"
	 *            default-value=""
	 */
	public File outputDir;

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
	 * @parameter expression="${promptConfirm}" default-value="true"
	 */
	public boolean promptConfirm = true;

	/**
	 * 
	 * 
	 * @parameter expression="${checkServers}" default-value="true"
	 */
	public boolean checkServers = true;
	
	/**
	 * 
	 * 
	 * @parameter expression="${checkPsql}" default-value="false"
	 */
	public boolean checkPsql = false;

	/**
	 * 
	 * 
	 * @parameter expression="${overWrite}" default-value="false"
	 */
	public boolean overWrite = false;

	/**
	 * 
	 * 
	 * @parameter expression="${excludeModules}"
	 */
	public String excludeModules = "";

	/**
	 * @component
	 */
	Prompter prompter;
	private Properties properties;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
	    if (this.outputDir==null)
		this.outputDir= new File(".");
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
				Artifact jarArtifact = this.artifactFactory.createArtifact(
						this.groupId, this.artifactId, this.version, "compile",
						this.packaging);
				this.resolver.resolve(jarArtifact, this.remoteRepositories,
						this.localRepository);

				Artifact pomArtifact = this.artifactFactory.createArtifact(
						this.groupId, this.artifactId, this.version, "compile",
						"pom");
				this.resolver.resolve(pomArtifact, this.remoteRepositories,
						this.localRepository);

				// Disabled
				// copyPomToWorking(pomArtifact);

				/**
				 * Get installation registry
				 */
				ModuleDependencyTree installedModules = getInstallationRegistry(
						this.installationURL, this.failsIfBadInstallationURL,
						this);
				// Set<Artifact> result =
				// DescribePackMojo.getModuleDependenciesSet(this.groupId,
				// this.artifactId, this.version, this.classifier,
				// this.artifactFactory, this.projectBuilder,
				// this.localRepository, this.remoteRepositories,
				// this.artifactMetadataSource, this.resolver);
				// ModuleDependencyTree registry =
				// DescribePackMojo.getModuleDependencyTree(result, this);

				// Get Modules' sequence of installation
				ModuleDependencyTree pack = DescribePackMojo
						.createPackDescription(this.groupId, this.artifactId,
								this.version, this.packaging, this.classifier,
								this.artifactFactory, this.projectBuilder,
								this.localRepository, this.remoteRepositories,
								this.artifactMetadataSource, this.resolver,
								this);

				List<Module> needed = installedModules
						.needToBeInstalledForPack(pack.getModules(), false); // ordered
																				// list

				// detect conflicts between upgrades and previous dependencies
				Collection<Module> mandatoryUpgrades = installedModules
						.getUpgradablesForPack(needed);
				Collection<Module> toBeUpgraded = new ArrayList<Module>(
						mandatoryUpgrades);
				Collection<Module> compatibles = installedModules
						.getCompatiblesForPack(needed);

				// Notify user about mandatory upgrades
				if (mandatoryUpgrades.size() != 0) {
					StringBuilder msg = new StringBuilder(
							"Following modules need to be upgraded:\n");
					for (Module upgradable : mandatoryUpgrades) {
						Module upgradedMod = installedModules
								.getUpgradePath(upgradable).get(0)
								.getUpgradableMod();
						msg.append("      ").append(upgradedMod).append("\n");
					}
					this.userInterfaceHook.notifyActivity(msg.toString()); // TODO
																			// avoid
																			// null
																			// messages
				}
				// check for remaining incompatibilities
				Collection<Module> incompatibles = new ArrayList<Module>(needed);
				incompatibles.removeAll(pack.getModules());
				incompatibles.removeAll(mandatoryUpgrades);
				// find if can be upgraded
				if (incompatibles.size() != 0) {
					// some are incompatible
					this.userInterfaceHook.notifyActivity("Following modules are incompatible. Remove them! "
									+ incompatibles);
				}
				// User will choose later
				needed.removeAll(compatibles);
				compatibles.removeAll(pack.getModules());

				// dump registry
				// ModuleDependencyTree installedModules =
				// getInstallationRegistry(this.installationURL,this.failIfBadInstallationURL,
				// this);
				// dump classpaths
				getLog().debug("Installation sequence:" + needed);

				// Lista de modulos a excluir si se indica alguno
				HashMap modulosExluidos = new HashMap();
				getLog().info("Modulos Excluidos:" + this.excludeModules);

				if ((this.excludeModules != null) && (!this.excludeModules.equals(""))) {
					String[] modulosExcluir = this.excludeModules.split(";");
					for (int i = 0; i < modulosExcluir.length; i++) {
						modulosExluidos.put(modulosExcluir[i],
								modulosExcluir[i]);
					}
				}

				List<Module> modulosNeeded = new ArrayList<Module>();
				for (Module module : needed) {
					if (!modulosExluidos.containsKey(module.getName())) {
						modulosNeeded.add(module);
					}
				}

				if (this.promptConfirm)
					if (!this.userInterfaceHook.confirmInstalls(modulosNeeded,
							installedModules))
						return;

				// detect potential but OPTIONAL upgrades
				// List<Module> upgradable =
				// installedModules.getCompatiblesForPack(pack.getModules());
				if (this.userInterfaceHook.confirmUpgrades(compatibles,
						installedModules)) {
					needed.addAll(compatibles);
					toBeUpgraded.addAll(compatibles);
				}

				this.userInterfaceHook.notifyActivity("Downloading binaries.");
				// Resolve artifacts
				for (Module module : modulosNeeded) {
					resolveModuleArtifact(module);
				}
				this.userInterfaceHook.notifyActivity("Installing modules.");
				// Install modules sequentially
				for (Module module : modulosNeeded) {
					// TODO Upgrades
					if (toBeUpgraded.contains(module))
						upgradeModule(module, installedModules,
								mandatoryUpgrades);
					else
						installModule(module, installedModules,
								mandatoryUpgrades);
				}
				//
				// TODO register installation
				//
				// TODO UPGRADES
				// TODO el registro debe hacerse en bloques para resolver las
				// dependencias cruzadas
				// TODO mover al exterior del bucle
				// After this upgradeStep is suppoded to be installed update the
				// registry
				// installedModules.removeModule(upgradeStep.getUpgradableMod());
				// installedModules.installModule(upgradeStep);
				// TODO en caso de error registrar el estado del sistema aunque
				// sea inconsistente
				// TODO INSTALLS
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
			} catch (XMLError e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			} catch (DependencyViolationException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			} catch (ModuleNotFoundException e) {
				throw new MojoExecutionException(e.getLocalizedMessage(), e);
			}
		}
	}

	public File copyPomToWorking(Artifact pomArtifact)
			throws MojoExecutionException, IOException, ArtifactResolutionException, ArtifactNotFoundException {
		/**
		 * Copy pom to working directory
		 */
		this.resolver.resolveAlways(pomArtifact, this.remoteRepositories, this.localRepository);
		File src = pomArtifact.getFile();
		File destinationDir= new File(this.outputDir, "poms");
		String name = pomArtifact.toString();
		File pomDir= new File(destinationDir, name.replace(':', '-'));
		if (!pomDir.isDirectory())
		    pomDir.mkdirs();
		File destination = new File(pomDir, "pom.xml");
		copyFile(src, destination);
		return destination;
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

	/**
	 * TODO perform upgrades
	 * 
	 * @param module
	 * @param installedModules
	 * @param upgradables
	 * @throws ArtifactResolutionException
	 * @throws ArtifactNotFoundException
	 * @throws ProjectBuildingException
	 * @throws InvalidDependencyVersionException
	 * @throws DependencyViolationException
	 * @throws MojoExecutionException 
	 * @throws IOException 
	 */
    private void installModule(Module module, ModuleDependencyTree installedModules, Collection<Module> upgradables) throws ArtifactResolutionException,
	    ArtifactNotFoundException, ProjectBuildingException, InvalidDependencyVersionException, DependencyViolationException, MojoExecutionException,
	    IOException
    {
	System.out.println("Overwrite:" + this.overWrite);
	this.getLog().info("============================================================");
	this.getLog().info("|Installing module: " + module);
	this.getLog().info("============================================================");
	this.getLog().info("|" + module.getDescription());
	this.getLog().info("============================================================");

	Updater updater = configureUpdater(module);
	updater.setOverwrite(this.overWrite);
	System.out.println("Overwrite:" + this.overWrite);
	updater.install();
	// register installation
	installedModules.installModule(module);
    }

    public Updater configureUpdater(Module module) throws ArtifactResolutionException, ArtifactNotFoundException, ProjectBuildingException,
	    InvalidDependencyVersionException, MojoExecutionException, IOException
    {
	Artifact jarArtifact = resolveModuleArtifact(module);
	// obtiene su path
	File binaryPath = ((jarArtifact != null) ? jarArtifact.getFile() : null);

	com.localgis.tools.modules.Artifact artifact = module.getArtifact();

	Set<Artifact> dependencies = null;
	// Procesar dependencias si corresponde
	if (needDownloadDependencies(artifact))
	    {
		dependencies = resolveDependencies(artifact);
	    }

	// Crea informe de dependencias
	getLog().debug("Dependencias a procesar: " + ((dependencies != null) ? dependencies : "[Sin dependencias]"));
	// ejecuta su método de instalación
	this.userInterfaceHook.notifyActivity("Installing...");
	// TODO configure and apply Upgrade path. Specifically for SQL modules

	Updater updater = UpdaterFactory.createUpdater(module.getArtifact().getInstall());
	updater.setUserInterfaceFacade(this.userInterfaceHook);
	updater.setModule(module);
	updater.setBinaryArtifact(jarArtifact);
	updater.setDependencies(dependencies);
	updater.setProperties(this.properties);
	return updater;
    }

	public Set<Artifact> resolveDependencies(com.localgis.tools.modules.Artifact artifact) throws ProjectBuildingException,
		InvalidDependencyVersionException, ArtifactResolutionException, ArtifactNotFoundException, MojoExecutionException, IOException
    {
	Artifact pomArtifact = this.artifactFactory.createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion().toString(),
		"compile", "pom");

	File pomFile = copyPomToWorking(pomArtifact);

	Runtime rt = Runtime.getRuntime();
	String OS = System.getProperty("os.name").toLowerCase();
	String mvnExec;
	// TODO test it with Linux
	if (OS.indexOf("win") >= 0)
	    {
		mvnExec = "mvn.bat"; // Process do not recognize "mvn" without extension
	    } else
	    {
		mvnExec = "mvn";
	    }

	String[] commands = { mvnExec, "dependency:resolve" };
	this.userInterfaceHook.showMessage("Resolving dependencies.");
	Process proc = rt.exec(commands, null, pomFile.getParentFile());

	BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	Set<Artifact> dependencies = parseDependenciesFromMVN(stdInput);

	// dependencies = DescribePackMojo
	// .getArtifactDependencies(artifact.getGroupId(), artifact
	// .getArtifactId(), artifact.getVersion().toString(),
	// artifact.getPackaging(), "compile",
	// this.artifactFactory, this.projectBuilder,
	// this.localRepository, this.remoteRepositories,
	// this.artifactMetadataSource, this.resolver, null,
	// this.overWrite);
	return dependencies;
    }

	public Set<Artifact> parseDependenciesFromMVN(BufferedReader stdInput) throws IOException, ArtifactResolutionException,
		ArtifactNotFoundException
    {
	Set<Artifact> dependencies = new HashSet<Artifact>();

	String line = null;
	while ((line = stdInput.readLine()) != null)
	    {

		//this.userInterfaceHook.showMessage(line);

		if (line.startsWith("[INFO]") && (line.endsWith("compile") || line.endsWith("runtime")))
		    {

			String artifactDescr = line.substring(10);
			String[] parts = artifactDescr.split(":");
			String version=parts.length==6?parts[4]:parts[3];
			String scope=parts.length==6?parts[5]:parts[4];
			String groupId2 = parts[0];
			String artifactId2 = parts[1];
			String type = parts[2];
			String classifier = parts.length==6?parts[3]:null;
			Artifact dep = this.artifactFactory.createArtifactWithClassifier(groupId2, artifactId2, version, type,classifier);
			this.resolver.resolve(dep, this.remoteRepositories, this.localRepository);
			dependencies.add(dep);
		    }
	    }
	return dependencies;
    }

	/**
	 * Determinar si para el elemento actual se deben procesar dependencias
	 * 
	 * @return
	 */
	private boolean needDownloadDependencies(
			com.localgis.tools.modules.Artifact artifact) {
		boolean processPackaging = true;

		String packaging = artifact.getPackaging();
		// Indicar tipos para los que NO se deben procesar dependencias
		if (packaging
				.equals(com.localgis.tools.modules.Artifact.PACKAGING_TYPES
						.get(com.localgis.tools.modules.Artifact.SYS_WAR_INSTALLER))
				|| packaging
						.equals(com.localgis.tools.modules.Artifact.PACKAGING_TYPES
								.get(com.localgis.tools.modules.Artifact.APP_WAR_INSTALLER))
				||
				// packaging.equals(com.localgis.tools.modules.Artifact.PACKAGING_TYPES.get(com.localgis.tools.modules.Artifact.RAR_INSTALLER))
				// ||
				artifact.getInstall().equals(
						com.localgis.tools.modules.Artifact.PACKAGE_INSTALLER))
			processPackaging = false;

		return processPackaging;
	}

	/**
	 * Iterates over the upgrade path
	 * 
	 * @param module
	 *            the {@link Module} to be installed at the end
	 * @param installedModules
	 *            registry with current modules
	 * @param upgradables
	 *            list of modules being upgraded at the same time. For
	 *            reference.
	 * @throws InvalidDependencyVersionException
	 * @throws ProjectBuildingException
	 * @throws ArtifactNotFoundException
	 * @throws ArtifactResolutionException
	 * @throws DependencyViolationException
	 * @throws ModuleNotFoundException
	 * @throws MojoExecutionException 
	 * @throws IOException 
	 */
	private void upgradeModule(Module module,
			ModuleDependencyTree installedModules,
			Collection<Module> upgradables) throws ArtifactResolutionException,
			ArtifactNotFoundException, ProjectBuildingException,
			InvalidDependencyVersionException, DependencyViolationException,
			ModuleNotFoundException, MojoExecutionException, IOException {
		List<Module> path = installedModules.getUpgradePath(module);
		// TODO PATCH to avoid original==null
		List<Module> pack = new ArrayList<Module>();
		pack.add(module);
		// TODO search module by name
		Module original = path.get(0).getUpgradableMod();
		if (original == null) {
			pack = installedModules.getCompatiblesForPack(pack);
			original = pack.get(0);
		}

		this.getLog().info(
				"============================================================");
		this.getLog().info("|Upgrading module: " + original);
		this.getLog().info(
				"============================================================");
		this.getLog().info("|" + original.getDescription());
		this.getLog().info(
				"============================================================");
		this.getLog().info("|To module: " + module);
		this.getLog().info(
				"============================================================");
		this.getLog().info("|" + module.getDescription());
		this.getLog().info(
				"============================================================");
		for (Module upgradeStep : path) {
			Updater updater = configureUpdater(upgradeStep);
			updater.upgrade();

		}
	}

	/**
	 * Resuelve el artifact que hay que instalar realmente. Descarga los
	 * binarios
	 * 
	 * @param module
	 * @throws ArtifactNotFoundException
	 * @throws ArtifactResolutionException
	 */
	private Artifact resolveModuleArtifact(Module module)
			throws ArtifactResolutionException, ArtifactNotFoundException {
		// Se ignora el artefacto a instalar porque este tipo sólo agrupa otros
		// instaladores
		if (module.getArtifact().getInstall()
				.equals(com.localgis.tools.modules.Artifact.PACKAGE_INSTALLER))
			return null;

		com.localgis.tools.modules.Artifact artifact = module.getArtifact();
		Artifact jarArtifact = this.artifactFactory.createArtifact(artifact
				.getGroupId(), artifact.getArtifactId(), artifact.getVersion()
				.toString(), "compile", artifact.getPackaging());
		if (this.overWrite) {
			//getLog().info("Resolving always");
			this.resolver.resolveAlways(jarArtifact, this.remoteRepositories,
					this.localRepository);
		} else {
			// getLog().info("Resolving normally");
			this.resolver.resolve(jarArtifact, this.remoteRepositories,
					this.localRepository);
		}

		return jarArtifact;
	}

	public void copyFile(File src, File destination)
			throws MojoExecutionException {
		if (getLog().isInfoEnabled()) {
			getLog().info(
					"Copying " + src.getAbsolutePath() + " to "
							+ destination.getAbsolutePath());
		}
		try {
			FileUtils.copyFile(src, destination);
		} catch (IOException e) {
			throw new MojoExecutionException(
					"Couldn't copy downloaded artifact from "
							+ src.getAbsolutePath() + " to "
							+ destination.getAbsolutePath() + " : "
							+ e.getMessage(), e);
		}
	}

	/**
	 * @deprecated
	 * @param project
	 * @throws MojoExecutionException
	 */
	@Deprecated
	private void executeDeployMod(MavenProject project) throws MojoExecutionException {
		Plugin plugin = MojoExecutor.plugin("com.localgis.maven.plugin", "updaterPlugin", "3.0");
		String goalTree = MojoExecutor.goal("deployMod");
		Xpp3Dom conf = MojoExecutor.configuration(new Element[0]);
		this.session.setCurrentProject(project);
		ExecutionEnvironment env = MojoExecutor.executionEnvironment(project,this.session, this.pluginManager);
		MojoExecutor.executeMojo(plugin, goalTree, conf, env);
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
		List lstKeyUrl = Arrays.asList(new String[] {LOCALGIS_SYS_URL_SERVER,LOCALGIS_APP_URL_SERVER });

		// Comprobamos disponibilidad servidores de aplicacion
		for (int i = 0; i < lstKeyUrl.size(); i++)
			activeServers &= checkAppServer(this.properties.getProperty((String) lstKeyUrl.get(i)));
		// Comprobar disponibilidad servidores BdD
		activeServers &= checkBdDServer();
		
		if (this.checkPsql)
			activeServers &= checkPsql();

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
		/*File f = new File(program);

		if (f.exists()) {
			exists=true;
		} else {
			exists=true;
		}*/
		
		this.userInterfaceHook.showMessage(" Check program: " + program + " ... " + ((exists) ? "SUCCESS" : "FAILURE"));
		return exists;
	}
}
