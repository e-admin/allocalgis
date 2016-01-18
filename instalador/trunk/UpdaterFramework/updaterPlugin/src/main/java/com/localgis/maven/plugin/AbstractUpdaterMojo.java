/**
 * AbstractUpdaterMojo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.maven.plugin;

import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Repository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProjectBuilder;
/**
 * MOJO oriented to a non-local project
 * @author juacas
 *
 */
public abstract class AbstractUpdaterMojo extends AbstractMojo
{

    /** @component */
    public org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;
    /** @component */
    public org.apache.maven.artifact.resolver.ArtifactResolver resolver;
    /** @component */
    public MavenProjectBuilder projectBuilder;
    /** @component */
    public ArtifactMetadataSource artifactMetadataSource;

    /** @parameter default-value="${project.remoteArtifactRepositories}" */
    public java.util.List<Repository> remoteRepositories;
    /** 
     * @parameter default-value="${localRepository}" 
     * @readonly
     */
    public ArtifactRepository localRepository;
    /**
     * The groupId of the artifact to download. Ignored if {@link #artifact} is used.
     * 
     * @parameter expression="${groupId}"
     */
    public String groupId = "com.localgis.modules";

    /**
     * The artifactId of the artifact to download. Ignored if {@link #artifact} is used.
     * 
     * @required
     * @parameter expression="${artifactId}"
     */
    public String artifactId;

    /**
     * The version of the artifact to download. Ignored if {@link #artifact} is used.
     * 
     * @required
     * @parameter expression="${moduleVersion}"
     */
    public String version;

    /**
     * The classifier of the artifact to download. Ignored if {@link #artifact} is used.
     * 
     * @parameter expression="${classifier}"
     * @since 2.3
     */
    public String classifier;

    /**
     * The packaging of the artifact to download. Ignored if {@link #artifact} is used.
     * 
     * @parameter expression="${packaging}" default-value="jar"
     */
    public String packaging = "jar";
}
