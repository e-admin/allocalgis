/**
 * SigmEntidadesWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.sigm;

import ieci.tecdoc.sgm.entidades.ws.server.Entidad;
import ieci.tecdoc.sgm.entidades.ws.server.Entidades;
import ieci.tecdoc.sgm.entidades.ws.server.EntidadesWebService;
import ieci.tecdoc.sgm.entidades.ws.server.EntidadesWebServiceServiceLocator;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

public class SigmEntidadesWS {
	
	private static final String CONTEXT_PATH = "SIGEM_EntidadesWS/services/EntidadesWebService?wsdl";

	public static List<Entidad> getAllEntidades(String ws_URL) throws RemoteException, MalformedURLException, ServiceException{		
		EntidadesWebService entidadesWebService = getWebService(ws_URL + CONTEXT_PATH);
		Entidades entidades = entidadesWebService.obtenerEntidades();
		if(entidades.getReturnCode().equals("OK"))
			return Arrays.asList(entidades.getEntidades());	
		return null;			
	}

	private static EntidadesWebService getWebService(String ws_URL) 
	throws MalformedURLException, ServiceException{
		EntidadesWebServiceServiceLocator entidadesWebServiceServiceLocator = new EntidadesWebServiceServiceLocator();
		EntidadesWebService entidadesWebService = entidadesWebServiceServiceLocator.getEntidadesWebService(new URL(ws_URL));
		return entidadesWebService;
	}
	
}
