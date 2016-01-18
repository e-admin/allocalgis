/**
 * DeployModMojo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.maven.plugins.updater;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
/**
 * 
 * @author juacas
 * @goal deployMod
 */
public class DeployModMojo extends AbstractMojo
{
    /**
     * The groupId of the artifact to download. Ignored if {@link #artifact} is used.
     * 
     * @parameter expression="${project.groupId}"
     */
    public String groupId = "com.localgis.modules";

    /**
     * The artifactId of the artifact to download. Ignored if {@link #artifact} is used.
     * 
     * @required
     * @parameter expression="${project.artifactId}"
     */
    public String artifactId;
    public void execute() throws MojoExecutionException, MojoFailureException
    {
	// TODO Auto-generated method stub

    }

}
