package com.localgis.module.updater.impl;

import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

import com.localgis.module.updater.Updater;
import com.localgis.module.updater.UpdaterUserInferfaceHook;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleReference;
import com.localgis.tools.modules.exception.DependencyViolationException;

public class NullUpdater extends AbstractLocalGISUpdater
{

	
	public void install() throws DependencyViolationException {
	
	}

	
	public void upgrade() throws DependencyViolationException {
		
	}

	
	public void uninstall() throws DependencyViolationException {
		
	}

	
	public void installOrUpgrade() {
		
	}

	
	public boolean checkInstallationValidity() {
		return true;
	}

	
	public boolean checkUninstallationValidity() {
		return true;
	}

	
}
/**
 * NullUpdater.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
