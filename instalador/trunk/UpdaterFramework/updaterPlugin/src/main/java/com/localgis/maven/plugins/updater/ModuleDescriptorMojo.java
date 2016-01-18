/**
 * ModuleDescriptorMojo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.maven.plugins.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.twdata.maven.mojoexecutor.MojoExecutor;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;
import org.twdata.maven.mojoexecutor.MojoExecutor.ExecutionEnvironment;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.Version;
import com.localgis.tools.modules.XMLModuleSerializer;
import com.localgis.tools.modules.exception.XMLError;
import com.localgis.tools.modules.impl.ArtifactImpl;
import com.localgis.tools.modules.impl.ModuleImpl;
import com.localgis.tools.modules.impl.VersionImpl;

/**
 * Generates the module.xml descriptor from dependencies in module's groupid
 * 
 * @requiresProject true
 * @goal moduleDescriptor
 * @phase compile
 */
public class ModuleDescriptorMojo extends AbstractMojo
{

    private static final String COM_LOCALGIS_MODULES = "com.localgis.modules";

    /**
     * @parameter expression="${project.moduleFile}" default-value="${project.build.outputDirectory}/module.xml"
     */
    private File moduleFile;

    /**
     * Conventional name for the updater module
     * 
     * @required
     * @parameter expression="${project.moduleName}"
     */
    private String moduleName;
    /**
     * @required
     * @parameter expression="${project.moduleVersion}"
     */
    private String moduleVersion;

    /**
     * @parameter
     */
    protected String installType;
    
    /**
     * ArtifactId of the meta-module from which this module can upgrade
     * If not specified the default value is the same as the current artifact
     * @optional
     * @parameter default-value="${project.artifactId}
     */
    private String upgradedArtifactId;
    /**
     * Version from which this module can upgrade
     * 
     * @optional
     * @parameter
     */
    private String upgradedVersion;
    /**
     * Artifact's groupId for the deployment of the module
     * 
     * @required
     * @parameter 
     */
    private String referencedGroupId;
    /**
     * ArtifactId for the deployment of the module
     * 
     * @required
     * @parameter
     */
    private String referencedArtifactId;
    /**
     * Artifact's version for the deployment of the module
     * 
     * @required
     * @parameter
     */
    private String referencedVersion;  
    /**
     * Binary Final name
     * @optional
     * @parameter expression="${referencedFinalName}"
     */
    private String referencedFinalName;
    /**
     * The Maven Project Object
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

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

    /** @component */
    private org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;
    /** @component */
    private org.apache.maven.artifact.resolver.ArtifactResolver resolver;
    /** @parameter default-value="${localRepository}" */
    private org.apache.maven.artifact.repository.ArtifactRepository localRepository;
    /** @parameter default-value="${project.remoteArtifactRepositories}" */
    @SuppressWarnings("rawtypes")
    private java.util.List remoteRepositories;
    /** @parameter expression="${outputEncoding}" default-value="${project.reporting.outputEncoding}" */
    private String outputEncoding;
    /**
     * @parameter default-value="${project.dependencies}"
     */
    protected List<Dependency> deps;

   
   
    
    public void execute() throws MojoExecutionException
    {
//	 preRequisites();
	 
	try
	    {
		Module mod = new ModuleImpl(this.moduleName, this.moduleVersion);
		mod.setDescription(this.project.getDescription());
		getLog().info("Registering Meta-Module:" + mod.toString());
		Version version = new VersionImpl(this.referencedVersion);
		com.localgis.tools.modules.Artifact artifact = new ArtifactImpl(this.referencedGroupId, this.referencedArtifactId, version, this.installType, this.referencedFinalName);
		mod.setArtifact(artifact);
		// generate dependencies tree
		populateModuleDependencies(mod);
		// get Upgrade Path from property
		getLog().info("Searching Upgrade Path.");
		populateModuleUpgradePath(mod);
		writeDescriptor(mod, this.moduleFile);

	    } catch (Exception e)
	    {
		throw new MojoExecutionException("Problem with JARs " + e.getLocalizedMessage(), e);
	    }
    }


    private void writeDescriptor(Module thisMod, File outputfile) throws IOException
    {
	OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputfile));// , this.outputEncoding);
	XMLModuleSerializer.write(thisMod, writer);
	writer.close();
    }

    /**
     * Populates thisMod {@link Module} with the modules included as dependencies
     * 
     * @param thisMod
     * @throws ArtifactResolutionException
     * @throws ArtifactNotFoundException
     * @throws IOException
     * @throws XMLError
     */
    public void populateModuleDependencies(Module thisMod) throws ArtifactResolutionException, ArtifactNotFoundException, IOException, XMLError
    {
	for (Dependency dep : this.deps)
	    {
		Artifact artifact = this.artifactFactory.createArtifact(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getScope(), dep.getType());
		if (artifact.getGroupId().startsWith(COM_LOCALGIS_MODULES))
		    {
			Module mod = getDescriptorFromModuleArtifact(artifact);
			thisMod.addDependency(mod);
			getLog().info("Registering module dependency:" + mod.toString());
		    }
	    }
    }

/**
 * Resolves the {@link Artifact} and builds an {@link Module} from the descriptor inside
 * @param artifact
 * @return
 * @throws ArtifactResolutionException
 * @throws ArtifactNotFoundException
 * @throws IOException
 * @throws XMLError
 */
    public Module getDescriptorFromModuleArtifact(Artifact artifact) throws ArtifactResolutionException, ArtifactNotFoundException, IOException, XMLError
    {
	this.resolver.resolve(artifact, this.remoteRepositories, this.localRepository);
	File depJar = artifact.getFile();
	Module mod = XMLModuleSerializer.readModuleFromJar(depJar);
	return mod;
    }
    /**
     * Navigate the previous versions and collect the upgraded module
     * @param mod
     * @throws ArtifactNotFoundException 
     * @throws ArtifactResolutionException 
     * @throws XMLError 
     * @throws IOException 
     */
    private void populateModuleUpgradePath(Module mod) throws ArtifactResolutionException, ArtifactNotFoundException, IOException, XMLError
    {

	if (this.upgradedVersion!=null)
	    {
		Artifact artifact = this.artifactFactory.createArtifact(COM_LOCALGIS_MODULES, this.upgradedArtifactId,this.upgradedVersion, "compile", "jar");
		
		Module upgradesMod = getDescriptorFromModuleArtifact(artifact);
		getLog().info("-Upgrades module:"+upgradesMod);
		mod.setUpgrades(upgradesMod);
	    }
    }
    public void preRequisites() throws MojoExecutionException
    {
	executeUnpack();
	executeTree();
    }

    private void executeUnpack() throws MojoExecutionException
    {
	Plugin plugin = MojoExecutor.plugin("org.apache.maven.plugins", "maven-dependency-plugin");
	String goalUnpack = MojoExecutor.goal("unpack-dependencies");
	Xpp3Dom conf = MojoExecutor.configuration(new Element[0]);
	ExecutionEnvironment env = MojoExecutor.executionEnvironment(this.project, this.session, (PluginManager) this.pluginManager);
	MojoExecutor.executeMojo(plugin, goalUnpack, conf, env);
    }
    private void executeTree() throws MojoExecutionException
    {
	Plugin plugin = MojoExecutor.plugin("org.apache.maven.plugins", "maven-dependency-plugin");
	String goalTree = MojoExecutor.goal("tree");
	Xpp3Dom conf = MojoExecutor.configuration(new Element[0]);
	ExecutionEnvironment env = MojoExecutor.executionEnvironment(this.project, this.session, (PluginManager) this.pluginManager);
	MojoExecutor.executeMojo(plugin, goalTree, conf, env);
    }
  

}
