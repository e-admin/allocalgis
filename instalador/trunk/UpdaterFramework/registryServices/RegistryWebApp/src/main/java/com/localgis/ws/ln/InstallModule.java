package com.localgis.ws.ln;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTreeImpl;
import com.localgis.tools.modules.XMLModuleSerializer;
import com.localgis.tools.modules.exception.XMLError;

public class InstallModule {
	
	public static String  getRegistry() throws Exception{
			
		return OperacionesBBDD.getRegistry();
	}
	
	
	public static Module parseModule(String version) throws IOException, XMLError{

		InputStream in = new ByteArrayInputStream(version.getBytes("UTF-8"));  
		Module module = XMLModuleSerializer.getModuleFromXMLStream(in);
		
		return module;
	}
	
	public static void installModule(String newModule) throws Exception
	{
		Module moduleToInstall = InstallModule.parseModule(newModule);
		
		String systemVersionInstall = getRegistry();	
		
		InputStream is = new ByteArrayInputStream(systemVersionInstall.getBytes("UTF-8"));
		ModuleDependencyTreeImpl mdt = (ModuleDependencyTreeImpl) XMLModuleSerializer.getModuleDependencyTreeFromXMLStream(is);
		
		mdt.installModule(moduleToInstall);

		StringWriter buffer1 = new StringWriter();	
		XMLModuleSerializer.write(mdt, buffer1);

		OperacionesBBDD.updateSystemVersion(buffer1.toString());
		OperacionesBBDD.activeModule(newModule, true);
	}


	public static void removeModule(String theModule) throws Exception
	{
//	    Module moduleToRemove = InstallModule.parseModule(theModule);
//		
//		String systemVersionInstall = getRegistry();	
//		
//		InputStream is = new ByteArrayInputStream(systemVersionInstall.getBytes("UTF-8"));
//		ModuleDependencyTreeImpl mdt = (ModuleDependencyTreeImpl) XMLModuleSerializer.getModuleDependencyTreeFromXMLStream(is);
//		
//		mdt.removeModule(moduleToRemove);
//
//		StringWriter buffer1 = new StringWriter();	
//		XMLModuleSerializer.write(mdt, buffer1);

		OperacionesBBDD.activeModule(theModule, false);
//		OperacionesBBDD.updateSystemVersion(buffer1.toString());
	}

}
/**
 * InstallModule.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
