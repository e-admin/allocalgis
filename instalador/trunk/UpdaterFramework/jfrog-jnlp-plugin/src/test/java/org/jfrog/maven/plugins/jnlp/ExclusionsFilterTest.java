package org.jfrog.maven.plugins.jnlp;

import org.apache.maven.artifact.Artifact;
import static org.jfrog.maven.plugins.jnlp.TestingTools.createArtifact;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Tests the ExclusionsFilter.
 *
 * @author Yossi Shaul
 */
@Test
public class ExclusionsFilterTest {

    public void nullExcludes() {
        ExclusionsFilter filter = new ExclusionsFilter(null);
        Artifact artifact = createArtifact("org.apache", "maven", "");
        Assert.assertFalse(filter.meetConditions(artifact), "Null excludes should exclude nothing");
    }

    public void emptyExcludes() {
        ExclusionsFilter filter = new ExclusionsFilter(Collections.<String>emptyList());
        Artifact artifact = createArtifact("org.*", "maven", "");
        Assert.assertFalse(filter.meetConditions(artifact), "Empty excludes should exclude nothing");
    }

    public void exactExclude() {
        ExclusionsFilter filter = new ExclusionsFilter(Arrays.asList("com.sun.oracle:le:1.3.8"));
        Artifact toExclude = createArtifact("com.sun.oracle", "le", "1.3.8");
        Artifact toInclude = createArtifact("com.sun.oracle", "le", "1.3.9");
        Assert.assertTrue(filter.meetConditions(toExclude), "Exact match - should be excluded");
        Assert.assertFalse(filter.meetConditions(toInclude), "Should not match");
    }

    public void excludeAllVersions() {
        ExclusionsFilter filter = new ExclusionsFilter(Arrays.asList("commons:commons"));
        Artifact artifact1 = createArtifact("commons", "commons", "1.2");
        Artifact artifact2 = createArtifact("commons", "commons", "3.9");
        Assert.assertTrue(filter.meetConditions(artifact1), "All versions should be excluded");
        Assert.assertTrue(filter.meetConditions(artifact2), "All versions should be excluded");
    }

    public void excludeByGroupId() {
        ExclusionsFilter filter = new ExclusionsFilter(Arrays.asList("commons"));
        Artifact artifact1 = createArtifact("commons", "art1", "1.2");
        Artifact artifact2 = createArtifact("commons", "art2", "3.9");
        Artifact artifact3 = createArtifact("common", "art2", "3.9");
        Assert.assertTrue(filter.meetConditions(artifact1), "Group is excluded");
        Assert.assertTrue(filter.meetConditions(artifact2), "Group is excluded");
        Assert.assertFalse(filter.meetConditions(artifact3), "Group is should not be excluded");
    }

    public void excludeByArtifactId() {
        ExclusionsFilter filter = new ExclusionsFilter(Arrays.asList("*:art1:*"));
        Artifact artifact1 = createArtifact("commons", "art1", "1.2");
        Artifact artifact2 = createArtifact("commons", "art2", "3.9");
        Assert.assertTrue(filter.meetConditions(artifact1), "Artifact is excluded");
        Assert.assertFalse(filter.meetConditions(artifact2), "Should not be excluded - artifact id doesn't match");
    }

}
