package com.localgis.tools.modules.impl;


import com.localgis.tools.modules.Artifact;
import com.localgis.tools.modules.Version;

public class ArtifactImpl implements Artifact
{
    private final String artifactId;
    private final String groupId;
    private final String install;
    private final Version version;
    private final String finalName;

    public ArtifactImpl(String groupId, String artifactId, Version version, String install, String finalName) {
		this.groupId=groupId;
		this.artifactId=artifactId;
		this.version=version;
		this.install=install;
		this.finalName=finalName;
    }

    
    public String getArtifactId()
    {
        return this.artifactId;
    }

    
    public String getGroupId()
    {
        return this.groupId;
    }

    
    public String getInstall()
    {
        return this.install;
    }

    
    public Version getVersion()
    {
        return this.version;
    }

    
    public String getPackaging()
    {
	String packaging = PACKAGING_TYPES.get(getInstall());
	return packaging==null?getInstall():packaging;
    }
    
    public String getFinalName() {
		return finalName;
	}
}
/**
 * ArtifactImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
