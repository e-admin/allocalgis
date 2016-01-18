package com.localgis.module.updater;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Before;
import org.junit.Test;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.XMLModuleSerializer;

public class testClassLoaderUpdate
{

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void test() throws MalformedURLException
    {
	URL dependency=new URL("file://../../../exampleModule/target/exampleModule-0.0.1-SNAPSHOT.jar");
	ClassLoader oldLoader=Thread.currentThread().getContextClassLoader();
	URLClassLoader loader=new URLClassLoader(new URL[]{dependency});
	URL url = loader.getResource("META-INF/module.xml");
	Module mod;
	try
	    {
		mod = XMLModuleSerializer.getModuleFromXMLURL(url);
	    } catch (Exception e)
	    {
		//Unexpected
		throw new RuntimeException(e);
	    } 
    }

}
/**
 * testClassLoaderUpdate.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
