/**
 * TestSysWarUpdater.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;

import com.localgis.module.updater.impl.SysWarUpdater;
import com.localgis.tools.modules.Artifact;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.impl.ArtifactImpl;
import com.localgis.tools.modules.impl.ModuleImpl;
import com.localgis.tools.modules.impl.VersionImpl;

public class TestSysWarUpdater extends LocalgisUpdaterTester
{
	/**
	 * Definir un módulo ficticio de ejemplo para las pruebas
	 */
	@Override
	protected Module getTestModule()
	{
		Module mod= new ModuleImpl("Registry Services", "3.0.0");
		mod.addDependency(new ModuleImpl("DataModelCore","3.0.0"));
		// Artifact que contiene los ficheros reales a distribuir
		org.apache.maven.artifact.Artifact artifact = getMavenActifact();
		// Este syswardemo hay que crearlo con el único objetivo de probar que todo funciona
		artifact.setFile(new File("target/test-classes/RegistryWebApp.war"));
//		artifact.setFile(new File("test/resources/RegistryService-3.0.0-SNAPSHOT.war"));
		Artifact localgisArtifact=new ArtifactImpl(artifact.getGroupId(),
				artifact.getArtifactId(),
				new VersionImpl(artifact.getVersion()),
				Artifact.SYS_WAR_INSTALLER, null);
		mod.setArtifact(localgisArtifact);

		return mod;
	}
	/**
	 * Artifact que representa el binario que contiene la información para la instalación.
	 * @return
	 */
	public org.apache.maven.artifact.Artifact getMavenActifact()
	{
		org.apache.maven.artifact.Artifact artifact= new DefaultArtifact("com.localgis.webapp",
				"softwareDist", VersionRange.createFromVersion("1.1.1"),
				"compile", "war", "war", null);
		
		artifact.setFile(new File("target/test-classes/RegistryWebApp.war"));
//		artifact.setFile(new File("target/test-classes/RegistryService-3.0.0-SNAPSHOT.war"));
		return artifact;
	}

	@Override
	protected Updater instantiateUpdater()
	{
		return new SysWarUpdater();
	}
	/**
	 * TODO Definir en este método la lista simulada de binarios que hay que manejar para los casos de prueba.
	 */
	@Override
	protected Set<org.apache.maven.artifact.Artifact> createTestDependencyList()
	{
		// Ejemplo de creación
		org.apache.maven.artifact.Artifact act= new DefaultArtifact("ejemplo.group", 
				"ejemploid",
				VersionRange.createFromVersion("1.1.1"),
				"compile", "jar", "jar", null);
		
//		act.setFile(new File("test/resources/libreriainteresante.jar"));
		act.setFile(new File("target/test-classes/RegistryWebApp.war"));
		
		Set<org.apache.maven.artifact.Artifact> deps=new HashSet<org.apache.maven.artifact.Artifact>();
		deps.add(act);
		return deps;
	}

}
