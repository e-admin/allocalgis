/**
 * LocalgisUpdaterTester.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater;

import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import junit.framework.Assert;

import org.apache.maven.artifact.Artifact;
import org.junit.Before;
import org.junit.Test;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.install.DefaultLocalGISInstallation;
import com.localgis.tools.modules.install.LocalGISInstallation;

public abstract class LocalgisUpdaterTester
{
	private Module module;
	
	protected Properties properties;
	protected Set<Artifact> dependencies;
	protected Updater updater;
	
	protected LocalGISInstallation installation;

	protected abstract Module getTestModule();
	protected abstract Updater instantiateUpdater();
	protected abstract Set<Artifact> createTestDependencyList();
	
	@Before
	public void setUp() throws Exception
	{
		this.properties= new Properties();
		InputStream resourceAsStream = this.getClass().getResourceAsStream("/install.properties");
		this.properties.load(resourceAsStream);

		this.dependencies=createTestDependencyList();
		this.updater=instantiateUpdater();
		this.module = getTestModule();
		
		this.updater.setProperties(this.properties);
		this.updater.setDependencies(this.dependencies);
		this.updater.setModule(this.module);
		this.updater.setBinaryArtifact(this.getMavenActifact());
		this.installation=new DefaultLocalGISInstallation();
		this.updater.setUserInterfaceFacade(new DebugPrompter());
	}
	
	/**
	 * Artifact que representa el binario que contiene la información para la instalación.
	 * @return
	 */
	protected abstract org.apache.maven.artifact.Artifact getMavenActifact();
	/**
	 * Test the normal installation
	 * @throws DependencyViolationException 
	 */
	@Test
	public void testInstallModule() throws DependencyViolationException
	{
		Module mod = this.updater.getModule();
		Collection<Module> deps = mod.dependsOn(); // dependencias
		// prepara un entorno propicio
		this.installation.installPack(deps);
		// instala con todas las dependencias
		this.updater.install();
		Assert.assertTrue(this.updater.checkInstallationValidity());
	}

	/**
	 * Test the normal installation
	 * @throws DependencyViolationException 
	 */
	@Test
	public void testUninstallModule() throws DependencyViolationException
	{
		Module mod = this.updater.getModule();
		Collection<Module> deps = mod.dependsOn(); // dependencias
		// prepara un entorno propicio
		this.installation.installPack(deps);
		// desintalar con todas las dependencias
		this.updater.uninstall();
		Assert.assertTrue(this.updater.checkUninstallationValidity());
	}

	/**
	 * Test the normal installation
	 * @throws DependencyViolationException 
	 */
	@Test
	public void testUpgradeModule() throws DependencyViolationException
	{
		Module mod = this.updater.getModule();
		Collection<Module> deps = mod.dependsOn(); // dependencias
		// prepara un entorno propicio
		this.installation.installPack(deps);
		// upgrade con todas las dependencias
		this.updater.upgrade();
		Assert.assertTrue(this.updater.checkInstallationValidity());
	}
}
