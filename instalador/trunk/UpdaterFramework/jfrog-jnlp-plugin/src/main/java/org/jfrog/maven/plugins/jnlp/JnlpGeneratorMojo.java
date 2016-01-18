package org.jfrog.maven.plugins.jnlp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoPhase;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

/**
 * Generates jnlp file based on an input or default velocity template.
 */
@MojoPhase("package")
@MojoGoal("jnlp")
@MojoRequiresDependencyResolution("runtime")
public class JnlpGeneratorMojo extends AbstractMojo {
    private final String DEFAULT_JNLP_TEMPLATE = "template/default.jnlp.vm";
    private final String DEFAULT_JAVAFX_JNLP_TEMPLATE = "template/default-javafx.jnlp.vm";

    @MojoParameter(defaultValue = "${project.basedir}/src/main/jnlp",
            description = "Directory containing the jnlp template file.")
    private File templateDirectory;

    @MojoParameter(expression = "${jnlp.templateFileName}",
            description = "The jnlp template file (relative to the template directory). " +
                    "If not set use the default jnlp template.")
    private String templateFileName;

    @MojoParameter(defaultValue = "${project.build.directory}/jnlp",
            description = "The target directory of the generated jnlp file.")
    private File targetDirectory;

    @MojoParameter(defaultValue = "${project.artifactId}.jnlp", alias = "targetFile",
            description = "The target file name. The default is the name of the input file.")
    private String targetFileName;

    @MojoParameter(defaultValue = "false", expression = "${jnlp.mavenStyleResourceLinks}",
            description = "If set to true the jar resources href path will use maven layout. " +
                    "i.e., /groupId/artifactId/filename")
    private boolean mavenStyleResourceLinks;

    @MojoParameter(defaultValue = "false", expression = "${jnlp.attach}",
            description = "Whether to attach the generated jnlp file to the deployable artifact list.")
    private boolean attach;

    @MojoParameter(defaultValue = "false", expression = "${jnlp.generateJavafxJnlp}",
            description = "If true and using the default template, the plugin will also generate a jnlp file " +
                    "using the default javafx template. If attach is true the generated jnlp will be " +
                    "attached with the classifier 'browser'.")
    private boolean generateJavaFxJnlp;

    @MojoParameter(defaultValue = "false", expression = "${jnlp.verbose}",
            description = "Print verbose output. Default is false. If running in debug mode will be set to true.")
    private boolean verbose;

    @MojoParameter(
            description = "Group id and artifact id in the form of groupId:artifactId of the jar " +
                    "with the main class. If not specified and mainClass is specified the plugin will " +
                    "try to detect the main jar by reflection.")
    private String mainJar;

    @MojoParameter(expression = "${jnlp.mainClass}",
            description = "Fully qualified name of the main class. If specified and mainJar is not specified " +
                    "the plugin will try to detect the main jar by reflection.")
    private String mainClass;

    @MojoParameter(description = "List of dependencies to exclude in the form of groupId:artifactId:version.")
    private List<String> excludes;
    @MojoParameter(description = "List of dependencies to include in the form of groupId:artifactId:version. Takes precedence over excludes. If this is defined excludes parameter is ignored.")
    private List<String> includes;

    @MojoParameter(description = "Additional key:value parameters to replace in the template.")
    private Map<String, String> additionalParameters;

    /*
    * Read only injectable parameters and components
    */
    @MojoParameter(required = true, readonly = true, expression = "${project}",
            description = "The Maven Project")
    private MavenProject project;

    @MojoParameter(readonly = true, expression = "${project.artifacts}",
            description = "The project dependency artifacts")
    private Set<Artifact> projectRuntimeArtifacts;

    @MojoComponent(description = "project-helper instance, used to make addition of resources simpler")
    private MavenProjectHelper projectHelper;

    @MojoComponent
    private ArtifactRepositoryLayout repositoryLayout;
    
    @MojoParameter (required = false, defaultValue = "", description = "Relative path for dependencies")
    private String relativePathDependencies;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (getLog().isDebugEnabled()) {
            this.verbose = true;
        }
        printDebugInfo();

        if (this.templateFileName == null) {
            getLog().info("'templateFileName' not set. Using default jnlp template.");
            this.templateFileName = this.generateJavaFxJnlp ? this.DEFAULT_JAVAFX_JNLP_TEMPLATE : this.DEFAULT_JNLP_TEMPLATE;
        } else {
            File jnlpTemplate = new File(this.templateDirectory, this.templateFileName);
            if (!jnlpTemplate.exists()) {
                throw new MojoFailureException("Couldn't find jnlp template: " + jnlpTemplate.getAbsolutePath());
            }
        }

        createTargetDirectoryIfNotExist();

        // add current project artifact is exists
        Artifact currentProjectArtifact = this.project.getArtifact();
        if (currentProjectArtifact != null && currentProjectArtifact.getFile() != null) {
            this.projectRuntimeArtifacts.add(currentProjectArtifact);
        }

        if (StringUtils.isNotBlank(this.mainClass) && StringUtils.isBlank(this.mainJar)) {
            resolveMainJar();
        }

        generateJnlpFile();
        if (this.generateJavaFxJnlp) {
            doGenerateJavaFxJnlp();
        }

        if (this.attach) {
            logIfVerbose("Attaching generated jnlp file to the project artifacts");
            this.projectHelper.attachArtifact(this.project, "jnlp", getTargetFile());
            if (this.generateJavaFxJnlp) {
                // also attach the generated javafx jnlp file
                this.projectHelper.attachArtifact(this.project, "jnlp", "browser", getJavaFxTargetFile());
            }
        }
    }

    private void generateJnlpFile() throws MojoExecutionException {
        File targetFile = getTargetFile();
        VelocityGenerator.Builder builder = new VelocityGenerator.Builder(this.templateFileName, targetFile);
        builder.setContext(createTemplateContext())
                .setTemplateDirectory(this.templateDirectory);

        try {
            VelocityGenerator generator = builder.build();
            generator.generate();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate jnlp file: " + e.getMessage(), e);
        }
    }

    private void doGenerateJavaFxJnlp() throws MojoExecutionException {
        VelocityGenerator.Builder builder = new VelocityGenerator.Builder(
                this.DEFAULT_JAVAFX_JNLP_TEMPLATE, getJavaFxTargetFile());
        builder.setContext(createJavaFxTemplateContext());

        try {
            VelocityGenerator generator = builder.build();
            generator.generate();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate javafx jnlp file: " + e.getMessage(), e);
        }
    }

    private File getTargetFile() {
        return new File(this.targetDirectory, this.targetFileName);
    }

    private void resolveMainJar() throws MojoExecutionException {
        logIfVerbose("Looking for jar containing main class: " + this.mainClass);
        for (Artifact artifact : this.projectRuntimeArtifacts) {
            URL artifactFileUrl;
            try {
                artifactFileUrl = artifact.getFile().toURI().toURL();
            } catch (MalformedURLException e) {
                throw new MojoExecutionException("Failed to get artifact file", e);
            }

            boolean mainJarFound = false;
            ClassLoader cl = new URLClassLoader(new URL[]{artifactFileUrl});
            try {
                Class.forName(this.mainClass, false, cl);
                mainJarFound = true;
            } catch (ClassNotFoundException e) {
                getLog().debug("Artifact " + artifact + " doesn't contain the main class.");
            } catch (NoClassDefFoundError e) {
                // class exists in the jar but fails probablt because we don't have all dependency in the class path
                getLog().debug("Artifact " + artifact + " seems to contain the main class " +
                        "(got NoClassDefFoundError: + " + e.getMessage() + ").");
                mainJarFound = true;
            }
            if (mainJarFound) {
                logIfVerbose("Artifact containing main class detected: " + artifact);
                this.mainJar = artifact.getGroupId() + ":" + artifact.getArtifactId();
                return;
            }
        }
    }

    /**
     * Constructs a string representing the jar resources.
     * <p/>
     * Each line contains the jar resource declaration, the required href attribute and additional optional attributes:
     * main, version, download. For example:
     * <pre>
     *     &lt;jar href="myjar-1.0.jar" main="true" download="eager"/&gt;
     * </pre>
     *
     * @return The jar resourcess list based of the included dependencies.
     */
    private String getDependenciesString() {
    	String href = ""; 
    	String relativePath = ((this.relativePathDependencies != null && !this.relativePathDependencies.equals(""))? (this.relativePathDependencies + "/") : ""); 
        ExclusionsFilter excludesFilter = new ExclusionsFilter(this.excludes);
        ExclusionsFilter includesFilter= new ExclusionsFilter(this.includes);
        
        StringBuilder sb = new StringBuilder();
        Iterator<Artifact> artifactsIter = this.projectRuntimeArtifacts.iterator();
        boolean useIncludes = this.includes!=null && !this.includes.isEmpty();
        while (artifactsIter.hasNext()) {
            Artifact artifact = artifactsIter.next();
            if ((useIncludes && includesFilter.meetConditions(artifact)) || (!useIncludes && !excludesFilter.meetConditions(artifact))) {
            	sb.append("<jar href=\"");
            	if (this.mavenStyleResourceLinks) {
            		href = this.repositoryLayout.pathOf(artifact);
            	} else {
            		href = artifact.getFile().getName();
            	}
            	sb.append(relativePath + href);
            	sb.append("\"");
            	if (isMainArtifact(artifact)) {
            		sb.append(" main=\"true\"");
            	}
            	sb.append("/>");
            	if (artifactsIter.hasNext()) {
            		sb.append("\n        ");
            	}
            }
        }

        String jarResources = sb.toString();
        logIfVerbose("Jar resources:\n" + jarResources);
        return jarResources;
    }

    private boolean isMainArtifact(Artifact artifact) {
        if (StringUtils.isBlank(this.mainJar)) {
            return false;
        }
        return this.mainJar.equals(artifact.getGroupId() + ":" + artifact.getArtifactId());
    }

    private void printDebugInfo() {
        logIfVerbose(ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE));
    }

    private void logIfVerbose(String message) {
        if (this.verbose) {
            getLog().info(message);
        }
    }

    private Map<String, Object> createTemplateContext() {
        Map<String, Object> context = new HashMap<String, Object>();

        // add all the project properties
        Properties projectProperties = this.project.getProperties();
        addAllProperties(context, projectProperties);

        context.put("dependencies", getDependenciesString());

        if (this.mainClass != null) {
            context.put("mainClass", this.mainClass);
        }

        if (this.additionalParameters != null) {
            context.putAll(this.additionalParameters);
        }

        Model projectModel = this.project.getModel();
        context.put("project", projectModel);

        context.put("informationTitle", projectModel.getName());
        context.put("informationDescription", projectModel.getDescription());
        if (projectModel.getOrganization() != null) {
            context.put("informationVendor", projectModel.getOrganization().getName());
            context.put("informationHomepage", projectModel.getOrganization().getUrl());
        }

        context.put("targetFileName", this.targetFileName);

        if (this.DEFAULT_JAVAFX_JNLP_TEMPLATE.equals(this.templateFileName)) {
            context.put("_javafxApplication", true);
        }

        // add all system properties (override if exist)
        Properties systemProps = System.getProperties();
        addAllProperties(context, systemProps);

        getLog().debug("Template context: " + context);
        return context;
    }

    private Map<String, Object> createJavaFxTemplateContext() {
        Map<String, Object> context = createTemplateContext();
        context.put("targetFileName", getJavaFxTargetFileName());
        context.put("_javafxApplication", false);
        context.put("_javafxApplet", true);
        if (!context.containsKey("appletWidth")) {
            context.put("appletWidth", 600);
        }
        if (!context.containsKey("appletHeight")) {
            context.put("appletHeight", 480);
        }
        return context;
    }

    private void addAllProperties(Map<String, Object> context, Properties systemProps) {
        for (String key : systemProps.stringPropertyNames()) {
            // replace dots with underscores to overcome velocity
            String escapedKey = key.replaceAll("\\.", "_");
            context.put(escapedKey, systemProps.getProperty(key));
        }
    }

    private void createTargetDirectoryIfNotExist() throws MojoFailureException {
        if (!this.targetDirectory.exists()) {
            if (!this.targetDirectory.mkdirs()) {
                throw new MojoFailureException(
                        "Failed to create target directory: " + this.targetDirectory.getAbsolutePath());
            }
        }
    }

    public String getTargetFileName() {
        return this.targetFileName;
    }

    File getJavaFxTargetFile() {
        return new File(this.targetDirectory, getJavaFxTargetFileName());
    }

    String getJavaFxTargetFileName() {
        // add '-browser' before the file extension
        int extensionIndex = this.targetFileName.lastIndexOf(".");
        if (extensionIndex > 0) {
            return this.targetFileName.substring(0, extensionIndex) + "-browser" +
                    this.targetFileName.substring(extensionIndex, this.targetFileName.length());
        } else {
            return this.targetFileName + "-browser";
        }
    }
}
