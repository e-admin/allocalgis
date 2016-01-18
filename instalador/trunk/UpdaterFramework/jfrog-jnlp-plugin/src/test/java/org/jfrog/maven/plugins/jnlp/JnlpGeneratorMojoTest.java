package org.jfrog.maven.plugins.jnlp;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Model;
import org.apache.maven.model.Organization;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.IOUtil;
import org.easymock.EasyMock;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import static org.jfrog.maven.plugins.jnlp.TestingTools.createArtifact;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yossi Shaul
 */
public class JnlpGeneratorMojoTest extends AbstractMojoTestCase {

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test(expectedExceptions = MojoFailureException.class)
    public void testFailIfUserJnlpFileNotFound() throws Exception {
        JnlpGeneratorMojo mojo = getMojo("src/test/resources/projects/project1-pom.xml");
        setVariableValueToObject(mojo, "targetDirectory", new File(getBasedir(), "/target/jnlp"));
        setVariableValueToObject(mojo, "templateDirectory", new File(getBasedir(), "src/test/resources/jnlp"));
        setVariableValueToObject(mojo, "templateFileName", "blabla.vm");
        mojo.execute();
    }

    @Test
    public void excludes() throws Exception {
        JnlpGeneratorMojo mojo = getMojo("src/test/resources/projects/project2-pom.xml");

        setVariableValueToObject(mojo, "templateDirectory", new File(getBasedir(), "src/test/resources/jnlp"));
        File targetDirectory = new File(getBasedir(), "/target/jnlp");
        setVariableValueToObject(mojo, "targetDirectory", targetDirectory);
        MavenProjectStub project = new MavenProjectStub();
        setVariableValueToObject(mojo, "project", project);

        Set<Artifact> dependencies = new HashSet<Artifact>();
        dependencies.add(createArtifact("group1", "artifact1", "1.0")); // explicit exclude if group:artifact
        dependencies.add(createArtifact("group1", "artifact2", "1.0")); // not excluded
        dependencies.add(createArtifact("group2", "artifact1", "1.0")); // excluded group
        dependencies.add(createArtifact("group2", "artifact1", "1.1")); // excluded group, excluded version
        dependencies.add(createArtifact("group3", "artifact1", "1.1")); // excluded version
        dependencies.add(createArtifact("group3", "artifact2", "2.0")); // not excluded

        setVariableValueToObject(mojo, "projectRuntimeArtifacts", dependencies);

        mojo.execute();

        File result = new File(targetDirectory, mojo.getTargetFileName());
        Assert.assertTrue(result.exists(), "Generated jnlp not found: " + result.getAbsolutePath());

        String jnlpContent = IOUtil.toString(new FileInputStream(result));

        // not excluded
        Assert.assertTrue(jnlpContent.contains("group1/artifact2/1.0"), "Result:\n" + jnlpContent);
        Assert.assertTrue(jnlpContent.contains("group3/artifact2/2.0"), "Result:\n" + jnlpContent);

        // excluded
        Assert.assertFalse(jnlpContent.contains("group1/artifact1/1.0"), "Result:\n" + jnlpContent);
        Assert.assertFalse(jnlpContent.contains("group2"), "Result:\n" + jnlpContent);
        Assert.assertFalse(jnlpContent.contains("group3/artifact1"), "Result:\n" + jnlpContent);
    }

    @Test
    public void useDefaulrJnlpTemplate() throws Exception {
        JnlpGeneratorMojo mojo = getMojo("src/test/resources/projects/project3-pom.xml");

        Model model = createProjectStub(mojo).getModel();
        File targetDirectory = new File(getBasedir(), "/target/jnlp");
        setVariableValueToObject(mojo, "targetDirectory", targetDirectory);
        setVariableValueToObject(mojo, "projectRuntimeArtifacts", new HashSet());
        setVariableValueToObject(mojo, "targetFileName", "test3.jnlp");
        mojo.execute();

        // validate the main jnlp
        File defaultJnlpResult = new File(targetDirectory, mojo.getTargetFileName());
        Assert.assertTrue(defaultJnlpResult.exists(),
                "Generated jnlp not found: " + defaultJnlpResult.getAbsolutePath());

        Element jnlpElement = getJnlpElement(defaultJnlpResult);
        commonDefaultTemplateAssertions(model, defaultJnlpResult, jnlpElement);

        Element appDescElement = jnlpElement.getChild("application-desc");
        Assert.assertNotNull(appDescElement, "'application-desc' element should not be null");
        Assert.assertEquals(appDescElement.getAttributeValue("main-class"), "il.Main", failureMessage("main-class"));
    }

    @Test
    public void useDefaultJavaFxJnlpTemplate() throws Exception {
        JnlpGeneratorMojo mojo = getMojo("src/test/resources/projects/project4-pom.xml");

        MavenProjectStub project = createProjectStub(mojo);
        Model model = project.getModel();

        File targetDirectory = new File(getBasedir(), "/target/jnlp");
        setVariableValueToObject(mojo, "targetDirectory", targetDirectory);
        setVariableValueToObject(mojo, "projectRuntimeArtifacts", new HashSet());
        setVariableValueToObject(mojo, "targetFileName", "test4.jnlp");

        // project helper mock
        MavenProjectHelper mockProjectHelper = EasyMock.createMock(MavenProjectHelper.class);
        mockProjectHelper.attachArtifact(project, "jnlp", new File(targetDirectory, mojo.getTargetFileName()));
        mockProjectHelper.attachArtifact(project, "jnlp", "browser", mojo.getJavaFxTargetFile());
        EasyMock.replay(mockProjectHelper);
        setVariableValueToObject(mojo, "projectHelper", mockProjectHelper);

        mojo.execute();

        // validate the main jnlp
        File defaultJnlpResult = new File(targetDirectory, mojo.getTargetFileName());
        Assert.assertTrue(defaultJnlpResult.exists(),
                "Generated jnlp not found: " + defaultJnlpResult.getAbsolutePath());

        Element jnlpElement = getJnlpElement(defaultJnlpResult);
        commonDefaultTemplateAssertions(model, defaultJnlpResult, jnlpElement);

        Element appDescElement = jnlpElement.getChild("application-desc");
        Assert.assertNotNull(appDescElement, "'application-desc' element should not be null");
        Element argumentElement = appDescElement.getChild("argument");
        Assert.assertNotNull(argumentElement, "Should have one argument");
        Assert.assertEquals(argumentElement.getText(), "MainJavaFXScript=il.Main",
                failureMessage("main-class argument"));

        // validate the jvafx jnlp
        File javafxResultFile = new File(targetDirectory, mojo.getJavaFxTargetFileName());
        Assert.assertTrue(javafxResultFile.exists(),
                "Generated javafx jnlp not found: " + javafxResultFile.getAbsolutePath());

        Element javafxJnlpElement = getJnlpElement(javafxResultFile);
        commonDefaultTemplateAssertions(model, javafxResultFile, javafxJnlpElement);

        Element appletDescElement = javafxJnlpElement.getChild("applet-desc");
        Assert.assertNotNull(appletDescElement, "'applet-desc' element should not be null");
        Assert.assertEquals(appletDescElement.getAttributeValue("name"), model.getName(), "Applet name not set");
        Assert.assertEquals(appletDescElement.getAttributeValue("width"), "600", "Default applet width not set");
        Assert.assertEquals(appletDescElement.getAttributeValue("height"), "480", "Default applet height not set");
        Element mainJavaFxClassParam = appletDescElement.getChild("param");
        Assert.assertEquals(mainJavaFxClassParam.getAttributeValue("value"), "il.Main",
                failureMessage("applet main-class"));

        // make sure all the expected calls were made to the project helper (to attach artifacts)
        EasyMock.verify(mockProjectHelper);
    }

    private MavenProjectStub createProjectStub(JnlpGeneratorMojo mojo) throws IllegalAccessException {
        // init project params
        Model model = new Model();
        model.setUrl("http://myhome.com");
        model.setName("jnlp-plugin");
        Organization org = new Organization();
        org.setName("jFrog");
        model.setOrganization(org);
        model.setDescription("cool plugin");

        MavenProjectStub project = new MavenProjectStub(model);
        setVariableValueToObject(mojo, "project", project);
        return project;
    }

    private Element getJnlpElement(File defaultJnlpResult) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        Document document = builder.build(defaultJnlpResult);
        Element jnlpElement = document.getRootElement();
        return jnlpElement;
    }

    private Element commonDefaultTemplateAssertions(Model model, File result, Element jnlpElement) {
        Assert.assertEquals(jnlpElement.getAttributeValue("codebase"), model.getUrl(),
                failureMessage("codebase")); // from project
        Assert.assertEquals(jnlpElement.getAttributeValue("href"), result.getName(),
                failureMessage("href")); // from mojo

        Element infoElement = jnlpElement.getChild("information");
        Assert.assertEquals(infoElement.getChildText("title"), model.getName(), failureMessage("title"));
        Assert.assertEquals(infoElement.getChildText("vendor"), model.getOrganization().getName(),
                failureMessage("vendor"));
        Element homepage = infoElement.getChild("homepage");
        Assert.assertNotNull(homepage, "'homepage' element should not be null");
        Assert.assertEquals(homepage.getAttributeValue("href"), model.getUrl(), failureMessage("href"));
        Assert.assertEquals(infoElement.getChildText("description"), model.getDescription(),
                failureMessage("description"));
        Assert.assertNull(infoElement.getChild("offline-allowed"), "'offline-allowed' tag should not exist");

        Element security = jnlpElement.getChild("security");
        Assert.assertNotNull(security, "'security' tag should not be null");
        Assert.assertNotNull(security.getChild("all-permissions"), "'all-permissions' tag should not be null");

        Element resourcesElement = jnlpElement.getChild("resources");
        Element j2seElement = resourcesElement.getChild("j2se");
        Assert.assertNotNull(j2seElement, "'j2se' element should not be null");
        Assert.assertEquals(j2seElement.getAttributeValue("version"), "1.5+", failureMessage("version"));
        return jnlpElement;
    }

    private String failureMessage(String attribute) {
        return String.format("'%s' was not replaced as expected", attribute);
    }

    private JnlpGeneratorMojo getMojo(String pomPath) throws Exception {
        File pomFile = new File(getBasedir(), pomPath);
        JnlpGeneratorMojo mojo = (JnlpGeneratorMojo) lookupMojo("jnlp", pomFile);
        return mojo;
    }

}
