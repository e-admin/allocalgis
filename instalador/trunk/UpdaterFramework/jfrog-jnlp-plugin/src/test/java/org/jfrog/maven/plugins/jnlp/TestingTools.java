package org.jfrog.maven.plugins.jnlp;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.apache.maven.plugin.testing.stubs.DefaultArtifactHandlerStub;

/**
 * @author Yossi Shaul
 */
public class TestingTools {

    public static Artifact createArtifact(String groupId, String artifactId, String version) {
        Artifact artifact = new ArtifactStub() {
            @Override
            public ArtifactHandler getArtifactHandler() {
                return new DefaultArtifactHandlerStub("jar");
            }

            @Override
            public String getBaseVersion() {
                return getVersion();
            }

            public ArtifactMetadata getMetadata(Class metadataClass) {
                throw new UnsupportedOperationException("what");
            }
        };
        artifact.setGroupId(groupId);
        artifact.setArtifactId(artifactId);
        artifact.setVersion(version);
        return artifact;
    }
}
