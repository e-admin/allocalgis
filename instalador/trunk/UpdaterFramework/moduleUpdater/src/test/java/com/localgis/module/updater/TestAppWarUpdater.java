/**
 * TestAppWarUpdater.java
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

import com.localgis.module.updater.impl.AppWarUpdater;
import com.localgis.tools.modules.Artifact;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.impl.ArtifactImpl;
import com.localgis.tools.modules.impl.ModuleImpl;
import com.localgis.tools.modules.impl.VersionImpl;

public class TestAppWarUpdater extends LocalgisUpdaterTester
{
	/**
	 * Definir un módulo ficticio de ejemplo para las pruebas
	 */
	@Override
	protected Module getTestModule()
	{
		Module mod= new ModuleImpl("cliente War Pruebas", "3.0.0");
		mod.addDependency(new ModuleImpl("cliente War Pruebas","3.0.0"));


		// Artifact que contiene los ficheros reales a distribuir
		org.apache.maven.artifact.Artifact artifact = getMavenActifact();

		// Este jnlpJar hay que crearlo con el único objetivo de probar que todo funciona
		artifact.setFile(new File("target/test-classes/RegistryWebApp.war"));
		//TODO: pruebas local
		//	artifact.setFile(new File("D:/CODIGOS/LocalGIS3/Instalador/UpdaterFramework/moduleUpdater/src/test/resources/RegistryWebApp.war"));

		Artifact localgisArtifact=new ArtifactImpl(artifact.getGroupId(),
				artifact.getArtifactId(),
				new VersionImpl(artifact.getVersion()),
				Artifact.APP_WAR_INSTALLER, null);
		mod.setArtifact(localgisArtifact);

		return mod;
	}
	/**
	 * Artifact que representa el binario que contiene la información para la instalación.
	 * @return
	 */
	public org.apache.maven.artifact.Artifact getMavenActifact()
	{
		// Este JAR contiene parte del código del cliente y fichero JNLP y otros recursos a determinar.
		// 
		org.apache.maven.artifact.Artifact artifact= new DefaultArtifact("com.localgis.clients",
				"AppWarTest", VersionRange.createFromVersion("3.0.0"),
				"compile", "war", "war", null);

		artifact.setFile(new File("target/test-classes/RegistryWebApp.war"));
		//TODO: pruebas local
		//    artifact.setFile(new File("D:/CODIGOS/LocalGIS3/Instalador/UpdaterFramework/moduleUpdater/src/test/resources/RegistryWebApp.war"));

		return artifact;
	}

	@Override
	protected Updater instantiateUpdater()
	{
		return new AppWarUpdater();
	}
	/**
	 * TODO Definir en este método la lista simulada de binarios que hay que manejar para los casos de prueba.
	 */
	@Override
	protected Set<org.apache.maven.artifact.Artifact> createTestDependencyList()
	{
		Set<org.apache.maven.artifact.Artifact> deps=new HashSet<org.apache.maven.artifact.Artifact>();
		// Ejemplo de creación



		return deps;
	}
}
