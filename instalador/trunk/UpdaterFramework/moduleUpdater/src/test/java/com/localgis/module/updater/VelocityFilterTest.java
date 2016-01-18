/**
 * VelocityFilterTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 */
package com.localgis.module.updater;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.junit.Before;
import org.junit.Test;

import com.localgis.module.utilitys.VelocityFilter;

public class VelocityFilterTest
{

    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * Test.
     * @throws IOException 
     * @throws VelocityException 
     * @throws ResourceNotFoundException 
     */
    @Test
    public void test() throws ResourceNotFoundException, VelocityException, IOException
    {
	Properties props=new Properties();
	String value = "TESTSUBSTITUTED";
	props.put("testVar",value);
	props.put("test_Var",value);
	StringReader reader=new StringReader("Testing text with a var ${testVar} in it. ${test_Var} con puntos.");
	StringWriter writer = new StringWriter();
	
	VelocityFilter filter=new VelocityFilter(props);
	filter.translateStream(reader, writer);
	
	String result =writer.toString();
	assertTrue(result.contains(value));
    }
    
    
    @Test
    public void testWar() throws ResourceNotFoundException, VelocityException, IOException
    {
	Properties props=new Properties();
	String value = "TESTSUBSTITUTED";
	props.put("testVar",value);
	URL resource = this.getClass().getClassLoader().getResource("RegistryWebApp.war");
	JarFile jarFile = new JarFile(resource.getFile());
	ZipEntry entry = jarFile.getEntry("texto.properties");
	InputStream is = jarFile.getInputStream(entry);
	InputStreamReader reader=new InputStreamReader(is);
	
	StringWriter writer=new StringWriter();
	
	VelocityFilter filter=new VelocityFilter(props);
	filter.translateStream(reader, writer);
	
	String result =writer.toString();
	assertTrue(result.contains(value));
    }

}
