package com.localgis.tools.modules.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.localgis.tools.modules.impl.PostgresLocalGISInstallation;

public class TestPostgresInstallation {

	PostgresLocalGISInstallation install;
	@Before
	public void setUp() throws Exception {
		
		install=new PostgresLocalGISInstallation();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConnect() {
		if (true) return;
		try {
			install.connect(new URL("http://dbserver:5432/localgis"), "${localgis.dbUser}", "${localgis.dbPassword}");
		} catch (MalformedURLException e) {
			fail("No maneja URLs correctas.");
		} catch (IOException e) {
			fail("Fallo al conectar.");
		}
		assertTrue("conexi√≥n no inicializada", install.isInitialized());
	}

	@Test
	public void testRemoveModuleFromCatalog() {
		if (true) return;
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateModuleToCatalog() {
		if (true) return;
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateModule() {
		if (true) return;
		fail("Not yet implemented");
	}

}
/**
 * TestPostgresInstallation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
