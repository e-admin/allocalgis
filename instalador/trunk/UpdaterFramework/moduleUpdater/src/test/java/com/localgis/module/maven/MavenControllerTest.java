package com.localgis.module.maven;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MavenControllerTest extends MavenController
{

    
    private MavenController mavenController;

    @Before
    public void setUp() throws Exception
    {
	this.mavenController=new MavenController();
    }

    @Test
    public void testProcessCLI()
    {
	String result = this.mavenController.processCLI("-a exampleModule -dir tmp".split(" "));
	assertNotNull(result);
	assertTrue(result.startsWith("Parsing failed."));
	
	result = this.mavenController.processCLI("-v 0.0.1-SNAPSHOT -dir tmp".split(" "));
	assertNotNull(result);
	assertTrue(result.startsWith("Parsing failed."));
	
	result = this.mavenController.processCLI("-a exampleModule -v 0.0.1-SNAPSHOT -dir tmp".split(" "));
	assertNotNull(result);
	assertTrue(result.startsWith("ok"));
	
	result = this.mavenController.processCLI("-a exampleModule -v 0.0.1-SNAPSHOT -dir tmp -u 23".split(" "));
	assertNotNull(result);
	assertTrue(result.startsWith("Parsing failed."));
	
	
    }

}
/**
 * MavenControllerTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
