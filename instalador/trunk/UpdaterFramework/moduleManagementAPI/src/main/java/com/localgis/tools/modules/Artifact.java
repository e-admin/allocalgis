/**
 * Artifact.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules;

import java.util.HashMap;
import java.util.Map;

public interface Artifact
{

    public static final String FILE_SYSTEM_INSTALLER = "file";
    public static final String APP_WAR_INSTALLER = "appWAR";
    public static final String SYS_WAR_INSTALLER = "sysWAR";
    public static final String CLIENT_JNLP_INSTALLER = "clientJNLP";
    public static final String DATA_MODEL_INSTALLER = "dataModel";
	public static final String PACKAGE_INSTALLER = "package";
	public static final String RAR_INSTALLER = "rar";
	public static final String ZIP_INSTALLER = "zip";
	public static final String MOVILIDAD_INSTALLER = "movilidad";
	public static final String DOCROOT_INSTALLER = "docroot";
	public static final String LIBEXT_INSTALLER = "libext";

    /**
     * Map between the installation type and the packaging of the artifacts.
     */
    public static final Map<String,String> PACKAGING_TYPES = new HashMap<String, String>()
    	    {{
    		put(DATA_MODEL_INSTALLER,"jar");
    		put(CLIENT_JNLP_INSTALLER,"jar");
    		put(FILE_SYSTEM_INSTALLER,"rar");
    		put(SYS_WAR_INSTALLER,"war");
    		put(APP_WAR_INSTALLER,"war");
    		put(PACKAGE_INSTALLER,"jar");
    		put(RAR_INSTALLER,"rar");
    		put(ZIP_INSTALLER,"zip");
    		put(MOVILIDAD_INSTALLER,"rar");
    		put(DOCROOT_INSTALLER,"rar");
    		put(LIBEXT_INSTALLER,"rar");
    	    }};
    public abstract Version getVersion();
/**
 * Type of installation operation
 * @return
 */
    public abstract String getInstall();

    public abstract String getGroupId();

    public abstract String getArtifactId();
/**
 * 
 * @return maven packaging for InstallType {@link #getInstall()}
 */
    public abstract String getPackaging();
    
    public abstract String getFinalName();
}
