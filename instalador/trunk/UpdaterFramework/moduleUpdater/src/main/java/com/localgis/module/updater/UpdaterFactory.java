/**
 * UpdaterFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater;

import com.localgis.module.updater.impl.AppWarUpdater;
import com.localgis.module.updater.impl.DataModelUpdater;
import com.localgis.module.updater.impl.FileSystemUpdater;
import com.localgis.module.updater.impl.JNLPClientUpdater;
import com.localgis.module.updater.impl.NullUpdater;
import com.localgis.module.updater.impl.SysWarUpdater;
import com.localgis.tools.modules.Artifact;

public class UpdaterFactory
{
    /**
     * Creates an Updater for the type passed in the argument
     * 
     * @param hint
     * @return
     */
    public static Updater createUpdater(String hint)
    {
	if (hint.equals(Artifact.APP_WAR_INSTALLER))
	    return new AppWarUpdater();
	else if (hint.equals(Artifact.CLIENT_JNLP_INSTALLER))
	    return new JNLPClientUpdater();
	else if (hint.equals(Artifact.DATA_MODEL_INSTALLER))
	    return new DataModelUpdater();
	else if (hint.equals(Artifact.SYS_WAR_INSTALLER))
	    return new SysWarUpdater();
	else if (hint.equals(Artifact.FILE_SYSTEM_INSTALLER))
	    return new FileSystemUpdater();
	else if (hint.equals(Artifact.PACKAGE_INSTALLER))
		return new NullUpdater();
	else if (hint.equals(Artifact.ZIP_INSTALLER))
		return new FileSystemUpdater();
	else if (hint.equals(Artifact.MOVILIDAD_INSTALLER))
		return new FileSystemUpdater(Artifact.MOVILIDAD_INSTALLER);
	else if (hint.equals(Artifact.LIBEXT_INSTALLER))
		return new FileSystemUpdater(Artifact.LIBEXT_INSTALLER);
	else
	    throw new IllegalArgumentException("Updater of type:"+hint+" not found.");

    }
}
