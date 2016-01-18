/**
 * DescribePackMojo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.maven.plugins.updater;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.model.Repository;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;

import com.localgis.maven.plugin.AbstractUpdaterMojo;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.ModuleDependencyTreeImpl;
import com.localgis.tools.modules.XMLModuleSerializer;
import com.localgis.tools.modules.exception.XMLError;

/**
 * Generate a hierarchical description of the modules needed to install a target module
 * 
 * @author juacas
 * @goal describePack
 * @requiresProject false
 */
public class DescribePackMojo extends AbstractUpdaterMojo
{
   
    /**
     * Output file for the registry descriptor. If not specified output is the standard output
     * 
     * @parameter expression="${outputFile}"
     */
    public File outputFile;
   
    public void execute() throws MojoExecutionException, MojoFailureException
    {
	try
	    {
		ModuleDependencyTree registry = DescribePackMojo.createPackDescription(this.groupId, this.artifactId, this.version, this.packaging, this.classifier, this.artifactFactory, this.projectBuilder, this.localRepository, this.remoteRepositories, this.artifactMetadataSource, this.resolver,this);
		XMLModuleSerializer.writeDescriptor(registry, this.outputFile);

	    } catch (Exception e)
	    {
		throw new MojoExecutionException(" Can't describe the package", e);
	    }

    }
    public static ModuleDependencyTree createPackDescription(String groupId, String artifactId, String version, String packaging, String classifier, ArtifactFactory artifactFactory, MavenProjectBuilder projectBuilder, ArtifactRepository artifactRepository, List<Repository> remoteRepositories, ArtifactMetadataSource artifactMetadataSource, ArtifactResolver artifactResolver, Mojo mojo) throws ProjectBuildingException, InvalidDependencyVersionException, ArtifactResolutionException,
	    ArtifactNotFoundException, IOException, XMLError
    {
	Set<Artifact> result = DescribePackMojo.getModuleDependenciesSet(groupId,artifactId,version, packaging, classifier, artifactFactory, projectBuilder, artifactRepository, remoteRepositories, artifactMetadataSource, artifactResolver);
	ModuleDependencyTree registry = DescribePackMojo.getModuleDependencyTree(result, mojo);
	return registry;
    }
/**
 * Obtiene las dependencias que un Updater necesita para planificar la instalaci√≥n.
 * 
 * @param groupId
 * @param artifactId
 * @param version
 * @param classifier
 * @param artifactFactory
 * @param projectBuilder
 * @param localRepository
 * @param remoteRepositories
 * @param artifactMetadataSource2
 * @param artifactResolver
 * @return
 * @throws ProjectBuildingException
 * @throws InvalidDependencyVersionException
 * @throws ArtifactResolutionException
 * @throws ArtifactNotFoundException
 */
    public static Set<Artifact> getModuleDependenciesSet(String groupId,String artifactId,String version, String packaging, String classifier, ArtifactFactory artifactFactory, MavenProjectBuilder projectBuilder, ArtifactRepository localRepository, List<Repository> remoteRepositories, ArtifactMetadataSource artifactMetadataSource2, ArtifactResolver artifactResolver) throws ProjectBuildingException, InvalidDependencyVersionException, ArtifactResolutionException,
	    ArtifactNotFoundException
    {
	ArtifactFilter filter = new ArtifactFilter()
	    {		
		public boolean include(Artifact artifact)
		{
		    return (artifact.getGroupId().startsWith("com.localgis.modules"));
		}
	    };
	Set<Artifact> result = getArtifactDependencies(groupId, artifactId, version, packaging, classifier, artifactFactory, projectBuilder, localRepository,
		remoteRepositories, artifactMetadataSource2, artifactResolver, filter,false);
	return result;
    }
public static Set<Artifact> getArtifactDependencies(String groupId, String artifactId, String version, String packaging, String classifier, ArtifactFactory artifactFactory,
	MavenProjectBuilder projectBuilder, ArtifactRepository localRepository, List<Repository> remoteRepositories,
	ArtifactMetadataSource artifactMetadataSource2, ArtifactResolver artifactResolver, ArtifactFilter filter,boolean overWrite) throws ProjectBuildingException,
	InvalidDependencyVersionException, ArtifactResolutionException, ArtifactNotFoundException
{
    Artifact pomArtifact = artifactFactory.createArtifact(groupId, artifactId, version, classifier, "pom");
    Artifact jarArtifact = artifactFactory.createArtifact(groupId, artifactId, version, classifier, packaging);
    MavenProject pomProject = projectBuilder.buildFromRepository(pomArtifact, remoteRepositories, localRepository);
    Set<Artifact> artifacts = pomProject.createArtifacts(artifactFactory, null, null);
    // List<String> patterns = new ArrayList<String>();
    // patterns.add("com.localgis.modules:*:*:*");
    
    if (packaging!=null && packaging.equals("rar")){
    	  //Solo nos quedamos con las dependencias iniciales sin resolver. Quizas se pueda realizar de otra forma
    	  ArtifactResolutionResult arr = artifactResolver.resolveTransitively(artifacts, pomArtifact, pomProject.getManagedVersionMap(),
					localRepository, remoteRepositories, artifactMetadataSource2, filter);
    	  if (overWrite){
    		  //System.out.println("Resolving dependencies always");
    		  artifactResolver.resolveAlways(jarArtifact, remoteRepositories, localRepository);
    	  }
    	  else
    		  artifactResolver.resolve(jarArtifact, remoteRepositories, localRepository);
    	  
    	  Set<Artifact> result = arr.getArtifacts();
    	  Set<Artifact> resultOK = new LinkedHashSet();
    	  Iterator<Artifact> it=result.iterator();
    	  while (it.hasNext()){
    		  Artifact artifact=it.next();
    		  Iterator<Artifact> it2=artifacts.iterator();
    		  boolean encontrado=false;
    		  while (it2.hasNext()){
    			  Artifact artifact2=it2.next();
    			  if (artifact.getArtifactId().equals(artifact2.getArtifactId())){
    				  encontrado=true;
    				  break;
    			  }
    		  }
    		  if (encontrado)
    			  resultOK.add(artifact);
    	  }
    	  
    	  //resultOK.add(jarArtifact);
  	      return resultOK;    
    }
    else{
	    ArtifactResolutionResult arr = artifactResolver.resolveTransitively(artifacts, pomArtifact, pomProject.getManagedVersionMap(),
	    					localRepository, remoteRepositories, artifactMetadataSource2, filter);
	    if (overWrite){
  		  //System.out.println("Resolving dependencies always");
  		  artifactResolver.resolveAlways(jarArtifact, remoteRepositories, localRepository);
	    }
	    else
	    	artifactResolver.resolve(jarArtifact, remoteRepositories, localRepository);
	    
	    Set<Artifact> result = arr.getArtifacts();
	    result.add(jarArtifact);
	    return result;
    }
}

    public static ModuleDependencyTree getModuleDependencyTree(Set<Artifact> result, Mojo mojo) throws IOException, XMLError
    {
	ModuleDependencyTree registry = new ModuleDependencyTreeImpl();
	for (Object artif : result)
	    {
		mojo.getLog().info(artif.toString());
		Artifact artifact = (Artifact) artif;
		Module mod = XMLModuleSerializer.readModuleFromJar(artifact.getFile());
		registry.register(mod);
	    }
	return registry;
    }

   
}
