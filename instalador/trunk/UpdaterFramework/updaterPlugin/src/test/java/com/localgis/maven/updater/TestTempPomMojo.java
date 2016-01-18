package com.localgis.maven.updater;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.localgis.maven.plugins.updater.TempPomMojo;

public class TestTempPomMojo extends AbstractMojoTestCase
{

    @Override
    protected void setUp() throws Exception
    {
	// TODO Auto-generated method stub
	super.setUp();
    }

    public void testTempPomMojo() throws Exception
    {
	File testPom=new File (getBasedir(),"target/test-classes/unit/updaterPlugin/tempPom/plugin-config.xml");
	TempPomMojo tempPom = (TempPomMojo) lookupMojo("tempPom", testPom);
    }
}
/**
 * TestTempPomMojo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
