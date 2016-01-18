package com.localgis.maven.updater;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.localgis.maven.plugins.updater.SimulateMojo;



public class TestSimulateMojo extends AbstractMojoTestCase
{

    private SimulateMojo simulate;

    @Override
    protected void setUp() throws Exception
    {
	super.setUp();
	File testPom=new File (getBasedir(),"target/test-classes/unit/updaterPlugin/simulate/plugin-config.xml");
	assert testPom.exists();
	
	this.simulate= (SimulateMojo) lookupMojo("simulate", testPom );
	//this.simulate.project=new MavenProjectStub();
	assertNotNull( this.simulate );
    }

    public void testSimulateMojo() throws Exception
    {
	this.simulate.execute();
    }

}
/**
 * TestSimulateMojo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
