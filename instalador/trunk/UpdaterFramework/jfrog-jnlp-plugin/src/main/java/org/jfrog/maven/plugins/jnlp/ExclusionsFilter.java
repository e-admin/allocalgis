package org.jfrog.maven.plugins.jnlp;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;

/**
 * TODO: Documentation
 *
 * @author Yossi Shaul
 */
public class ExclusionsFilter {
    private final List<String> excludePatterns;

    public ExclusionsFilter(List<String> excludes) {
        this.excludePatterns = excludes;
    }

    public boolean meetConditions(Artifact artifact) {
        if (excludePatterns == null || excludePatterns.isEmpty()) {
            return false;
        }

        for (String pattern : excludePatterns) {
            String[] excludePatternTokens = pattern.split(":");
            excludePatternTokens = StringUtils.stripAll(excludePatternTokens);
            if (matches(artifact, excludePatternTokens)) {
                return true;
            }
        }
        return false;   // not excluded
    }

    protected boolean matches(Artifact artifact, String[] pattern) {
        boolean result = false;
        if (pattern.length > 0) {
        	//Buscar coincidencias del patron para el grupo: valor exacto, el grupo, el grupo y todos dentro del mismo grupo (.*)
            result = (pattern[0].equals("*")) || 
            		 (artifact.getGroupId().equals(pattern[0])) || 
            		 (pattern[0].endsWith(".*") && 
            			//Grupo o subgrupos
            		  ((artifact.getGroupId().equals(pattern[0].substring(0, pattern[0].lastIndexOf(".*"))) ||
            		   (artifact.getGroupId().startsWith(pattern[0].substring(0, pattern[0].lastIndexOf("*")))))));
        }

        if (result && pattern.length > 1) {
            result = pattern[1].equals("*") || artifact.getArtifactId().equals(pattern[1]);
        }

        if (result && pattern.length > 2) {
            result = pattern[2].equals("*") || artifact.getVersion().equals(pattern[2]);
        }

        return result;
    }

}
