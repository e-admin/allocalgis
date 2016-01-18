package com.localgis.maven.plugins.updater;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.dependency.GetMojo;

import com.localgis.maven.plugin.AbstractUpdaterMojo;

/**
 * Reports the steps of the installation.
 *
 * @goal simulate
 * @requiresProject false
 * 
 * @requiresDependencyResolution
 */
public class SimulateMojo extends AbstractUpdaterMojo
{

    /**
     * @component
     * @readonly
     */
    private ArtifactFactory artifactFactory;

    /**
     * @component
     * @readonly
     */
    private ArtifactResolver artifactResolver;

    /**
     * @component
     * @readonly
     */
    private ArtifactRepositoryFactory artifactRepositoryFactory;

    /**
     * Map that contains the layouts.
     *
     * @component role="org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout"
     */
    private Map repositoryLayouts;

    /**
     * @component
     * @readonly
     */
    private ArtifactMetadataSource source;

   
    /**
     * The groupId of the artifact to download. Ignored if {@link #artifact} is used.
     *
     * @parameter expression="${groupId}"
     */
    private final String groupId="com.localgis.modules";

    /**
     * The artifactId of the artifact to download. Ignored if {@link #artifact} is used.
     * @required
     * @parameter expression="${artifactId}"
     */
    private String artifactId;

    /**
     * The version of the artifact to download. Ignored if {@link #artifact} is used.
     * @required
     * @parameter expression="${version}"
     */
    private String version;

    /**
     * The classifier of the artifact to download. Ignored if {@link #artifact} is used.
     * @parameter expression="${classifier}"
     * @since 2.3
     */
    private String classifier;

    /**
     * The packaging of the artifact to download. Ignored if {@link #artifact} is used.
     * @parameter expression="${packaging}" default-value="jar"
     */
    private final String packaging = "jar";

    /**
     * The id of the repository from which we'll download the artifact
     * @parameter expression="${repoId}" default-value="temp"
     * @deprecated Use remoteRepositories
     */
    @Deprecated
    private final String repositoryId = "temp";

    /**
     * The url of the repository from which we'll download the artifact. DEPRECATED Use remoteRepositories
     * 
     * @deprecated Use remoteRepositories
     * @parameter expression="${repoUrl}"
     */
    @Deprecated
    private String repositoryUrl;

    /**
     * Repositories in the format id::[layout]::url or just url, separated by comma.
     * ie. central::default::http://repo1.maven.apache.org/maven2,myrepo::::http://repo.acme.com,http://repo.acme2.com
     * 
     * @parameter expression="${remoteRepositories}"
     */
    private String remoteRepositories;

    /**
     * A string of the form groupId:artifactId:version[:packaging][:classifier].
     * 
     * @parameter expression="${artifact}"
     */
    private String artifact;



    /**
     *
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @required
     * @readonly
     */
    private List pomRemoteRepositories;

    /**
     * Download transitively, retrieving the specified artifact and all of its dependencies.
     * @parameter expression="${transitive}" default-value=true
     */
    private final boolean transitive = true;
    
   
    
    public SimulateMojo() {
	System.out.println("Entering.");
    }
    public void execute() throws MojoExecutionException, MojoFailureException
    {
	getLog().info("================================================");
	getLog().info(" Describing the modules implied in this upgrade");
	getLog().info("================================================");
	
	Artifact artifact = this.artifactFactory.createBuildArtifact(this.groupId,this.artifactId,this.version, "jar");
	resolveTransitively(artifact);
	getLog().info(" Artifact:"+artifact);
	
	
	//List<File> files= getArtifactFileList(artifact);
	
//	try
//	    {
//		MavenProject proj = this.projectBuilder.buildFromRepository(artifact, this.pomRemoteRepositories, this.localRepository);
//	    } catch (ProjectBuildingException e)
//	    {
//		throw new MojoExecutionException("project:"+artifact,e);
//	    }
	
	
    }
    
    private List<File> getArtifactFileList(Artifact artifact) throws MojoExecutionException
    {
	try
	    {
		this.artifactResolver.resolve(artifact, this.pomRemoteRepositories, this.localRepository);
		
	    } catch (ArtifactResolutionException e)
	    {
		throw new MojoExecutionException("Can't resolve artifact",e);
	    } catch (ArtifactNotFoundException e)
	    {
		throw new MojoExecutionException("Can't find artifact",e);
	    }
	return null;	
    }
    public void resolveTransitively(Artifact artifact) throws MojoExecutionException, MojoFailureException
    {
	
	GetMojo getMojo=new GetMojo();
	setValue(getMojo, "groupId", artifact.getGroupId());
	setValue(getMojo,"artifactId", artifact.getArtifactId());
	setValue(getMojo,"version", artifact.getVersion());
	
	setValue(getMojo,"artifactFactory", this.artifactFactory);
	setValue(getMojo,"artifactResolver", this.artifactResolver);
	setValue(getMojo,"artifactRepositoryFactory", this.artifactRepositoryFactory);
	setValue(getMojo,"repositoryLayouts", this.repositoryLayouts);
	setValue(getMojo,"source", this.source);
	setValue(getMojo,"localRepository", this.localRepository);
	setValue(getMojo,"remoteRepositories", this.remoteRepositories);
	setValue(getMojo,"artifact", this.artifact);
	setValue(getMojo,"pomRemoteRepositories", this.pomRemoteRepositories);
		
	getMojo.execute();
    }
    public void resolveTransitively2(String groupId, String artifactId, String version) throws MojoExecutionException
    {
//	getLog().debug("Session:"+this.session);
//	getLog().debug("Project:"+this.project);
//	
//	ExecutionEnvironment environment=MojoExecutor.executionEnvironment(this.project, this.session, this.pluginManager);
//	MojoExecutor.executeMojo(
//			MojoExecutor.plugin("org.apache.maven.plugins", "maven-dependency-plugin","2.4"),
//			MojoExecutor.goal("get"),
//			MojoExecutor.configuration(
//				MojoExecutor.element("groupId", groupId),
//				MojoExecutor.element("artifactId", artifactId),
//				MojoExecutor.element("version", version)
//				),
//			environment			
//		);	
    }
    private void setValue(Object o, String field, Object value) throws MojoFailureException
    {
	    Class c = o.getClass();
	    Field _field;
	    try {
	      _field = c.getDeclaredField(field);
	      _field.setAccessible(true);
	      _field.set(o, value);
	    } catch (Exception e) {
	      throw new MojoFailureException(e.getMessage());
	    }
	  }
}
