package com.localgis.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.localgis.ws.ln.InstallModule;


@WebService(name="Registry", targetNamespace="http://www.localgis.com/ws")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class Registry 
{
    	@WebMethod(operationName = "getRegistry")
	public String getRegistry() throws Exception 
    	{	 
		return InstallModule.getRegistry();
	}
	
    	@WebMethod
	public boolean installModule(String newModule) throws Exception
    	{
		
		InstallModule.installModule(newModule); 
		return true;

	}
    	@WebMethod
	public boolean removeModule(String theModule) throws Exception
	{
		
		InstallModule.removeModule(theModule); 
		return true;

	}
}
/**
 * Registry.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
