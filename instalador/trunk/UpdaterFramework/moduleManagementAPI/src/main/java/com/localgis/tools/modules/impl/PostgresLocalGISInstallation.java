package com.localgis.tools.modules.impl;

import java.io.IOException;
import java.net.URL;

import com.localgis.tools.modules.ModuleReference;
import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.install.AbstractLocalGISInstallation;

public class PostgresLocalGISInstallation extends AbstractLocalGISInstallation {
	
	@Override
	public void connect(URL localGISLocation, String username, String password)
			throws IOException {
		// TODO Auto-generated method stub

	}

	public void updateModule(ModuleReference mod) throws DependencyViolationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
/**
 * PostgresLocalGISInstallation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
