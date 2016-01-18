package com.localgis.tools.modules.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.localgis.tools.modules.install.ReadOnlyURLInstallation;

public class TestURLInstallation {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConnect() {
		URL testURL = this.getClass().getClassLoader().getResource("localgisInstallation.xml");
		try {
			ReadOnlyURLInstallation install=ReadOnlyURLInstallation.loadFromURL(testURL);
	
			install.connect(testURL, "", "");
			assertTrue(install.getModules().size()==3);
		} catch (IOException e) {
			fail("IO Exception");
		}
		
	}

}
/**
 * TestURLInstallation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
