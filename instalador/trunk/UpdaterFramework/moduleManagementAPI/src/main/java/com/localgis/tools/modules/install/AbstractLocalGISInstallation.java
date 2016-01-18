package com.localgis.tools.modules.install;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTreeImpl;
import com.localgis.tools.modules.exception.DependencyViolationException;


public abstract class AbstractLocalGISInstallation extends ModuleDependencyTreeImpl implements LocalGISInstallation
{

	@Override
	public abstract void connect(URL localGISLocation, String username, String password) throws IOException ;
	
	@Override
	public void installModule(Module mod) throws DependencyViolationException
	    {
		Module instanceMod = resolve(mod);
		Collection<Module> needed = needToBeInstalled(instanceMod,true);
		if (needed.size() != 0)
		    {
			throw new DependencyViolationException("Module " + instanceMod.getName() + "(" + instanceMod.getVersion() + ") has unresolved dependencies: " + needed);
		    }
		super.installModule(instanceMod);
	    }
}
/**
 * AbstractLocalGISInstallation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
