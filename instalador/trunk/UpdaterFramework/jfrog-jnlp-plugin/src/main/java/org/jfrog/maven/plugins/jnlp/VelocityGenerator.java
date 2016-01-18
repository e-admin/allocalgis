package org.jfrog.maven.plugins.jnlp;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Jnlp file generator that uses the velocity template engine.
 *
 * @author Yossi Shaul
 */
public class VelocityGenerator {
    private final String templateFilePath;
    private final File targetFile;
    private final Map<String, Object> context;

    private VelocityEngine velocityEngine;
    private File templateDirectory;

    public VelocityGenerator(String templateFilePath, File targetFile, Map<String, Object> context) {
        this.templateFilePath = templateFilePath;
        this.targetFile = targetFile;
        this.context = context;
    }

    public void generate() throws IOException {
        Template template = getJnlpTemplate();
        FileWriter fos = new FileWriter(targetFile);
        try {
            VelocityContext context = createVelocityContext();
            template.merge(context, fos);
        } finally {
            fos.close();
        }
    }

    public void initialize() {
        Properties props = new Properties();
        // use both file and classpath resource loaders
        props.setProperty(RuntimeConstants.RESOURCE_LOADER, "file, class");
        props.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
        if (templateDirectory != null) {
            props.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, templateDirectory.getAbsolutePath());
        }
        //props.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new NullLogChute());

        try {
            velocityEngine = new VelocityEngine(props);
            velocityEngine.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VelocityContext createVelocityContext() {
        return new VelocityContext(context);
    }

    private Template getJnlpTemplate() throws IOException {
        try {
            return velocityEngine.getTemplate(templateFilePath);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public void setTemplateDirectory(File templateDirectory) {
        this.templateDirectory = templateDirectory;
    }

    public static class Builder {
        private final String templateFilePath;
        private final File targetFile;
        private Map<String, Object> context;
        private File templateDirectory;

        public Builder(String templateFilePath, File targetFile) {
            this.templateFilePath = templateFilePath;
            this.targetFile = targetFile;
        }

        public VelocityGenerator build() {
            VelocityGenerator generator = new VelocityGenerator(templateFilePath, targetFile, context);
            generator.setTemplateDirectory(templateDirectory);
            generator.initialize();

            return generator;
        }

        public Builder setContext(Map<String, Object> context) {
            this.context = context;
            return this;
        }

        public Builder setTemplateDirectory(File templateDirectory) {
            this.templateDirectory = templateDirectory;
            return this;
        }
    }

}
