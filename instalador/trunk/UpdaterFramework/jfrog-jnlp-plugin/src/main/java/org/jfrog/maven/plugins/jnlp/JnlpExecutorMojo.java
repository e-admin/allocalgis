package org.jfrog.maven.plugins.jnlp;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import java.io.IOException;

/**
 * Executes a webstart application using an input jnlp file. Uses javaws to execute the jnlp file.
 *
 * @author Yossi Shaul
 */
@MojoGoal("execute")
public class JnlpExecutorMojo extends AbstractMojo {

    @MojoParameter(expression = "${jnlp.jnlpFullPath}",
            description = "Jnlp file path to execute. Can be local or remote file.")
    private String jnlpFullPath;

    @MojoParameter(expression = "${jnlp.jnlpRootPath}",
            description = "Jnlp file root path to execute. Can be local or remote file.")
    private String jnlpRootPath;

    @MojoParameter(expression = "${jnlp.jnlpRelativePath}",
            description = "Relative path to jnlpRootPath pointing to the jnlp file. " +
                    "Deafult is the maven relative path.")
    private String jnlpRelativePath;

    @MojoParameter(required = true, readonly = true, expression = "${project}", description = "The Maven Project")
    private MavenProject project;

    @MojoComponent
    private ArtifactFactory artifactFactory;

    @MojoComponent
    private ArtifactRepositoryLayout repositoryLayout;

    public void execute() throws MojoExecutionException, MojoFailureException {
        String jnlpPath;
        if (jnlpFullPath != null) {
            jnlpPath = jnlpFullPath;
        } else if (jnlpRootPath != null) {
            if (jnlpRelativePath == null) {
                Artifact artifact = artifactFactory.createArtifact(project.getGroupId(),
                        project.getArtifactId(), project.getVersion(), "compile", "jnlp");
                jnlpRelativePath = repositoryLayout.pathOf(artifact);
                getLog().debug("Using default relative path: " + jnlpRelativePath);
            }

            jnlpPath = jnlpRootPath + "/" + jnlpRelativePath;
        } else {
            throw new MojoExecutionException("jnlpRootPath or jnlpFullPath must be set");
        }

        try {
            String command = "javaws " + jnlpPath;
            getLog().info("Executing: " + command);
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
