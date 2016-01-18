/**
 * SigmCatalogoTramitesWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.sigm;

import ieci.tecdoc.sgm.catalogo.ws.server.CatalogoTramitesWebService;
import ieci.tecdoc.sgm.catalogo.ws.server.CatalogoTramitesWebServiceServiceLocator;
import ieci.tecdoc.sgm.catalogo.ws.server.Tramite;
import ieci.tecdoc.sgm.catalogo.ws.server.TramiteConsulta;
import ieci.tecdoc.sgm.catalogo.ws.server.Tramites;
import ieci.tecdoc.sgm.core.services.dto.Entidad;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

public class SigmCatalogoTramitesWS {

	private static final String CONTEXT_PATH = "SIGEM_CatalogoTramitesWS/services/CatalogoTramitesWebService?wsdl";

	public static List<Tramite> getAllTramitesByEntidad(String url, String idEntidad) throws RemoteException, MalformedURLException, ServiceException{
		Entidad entidad = new Entidad();
		entidad.setIdentificador(idEntidad);
		entidad.setHabilitada(true);
		return query(url, new TramiteConsulta(), entidad);
	}
	
	public static List<Tramite> getTramites(String url, String idEntidad) throws RemoteException, MalformedURLException, ServiceException{		
		CatalogoTramitesWebService catalogoTramitesWebService = getWebService(url + CONTEXT_PATH);
		Entidad entidad = new Entidad();
		entidad.setIdentificador(idEntidad);
		Tramites tramites = catalogoTramitesWebService.getProcedures(entidad);
		if(tramites.getReturnCode().equals("OK"))
			return Arrays.asList(tramites.getTramites());
		return null;	
	}
	
	public static List<Tramite> query(String url, TramiteConsulta tramiteConsulta, Entidad entidad) throws RemoteException, MalformedURLException, ServiceException{		
		CatalogoTramitesWebService catalogoTramitesWebService = getWebService(url + CONTEXT_PATH);
		Tramites tramites = catalogoTramitesWebService.query(tramiteConsulta, entidad);
		if(tramites.getReturnCode().equals("OK"))
			return Arrays.asList(tramites.getTramites());
		return null;	
	}	
	
	private static CatalogoTramitesWebService getWebService(String url) 
	throws MalformedURLException, ServiceException{
		CatalogoTramitesWebServiceServiceLocator catalogoTramitesWebServiceServiceLocator = new CatalogoTramitesWebServiceServiceLocator();
		CatalogoTramitesWebService catalogoTramitesWebService = catalogoTramitesWebServiceServiceLocator.getCatalogoTramitesWebService(new URL(url));
		return catalogoTramitesWebService;
	}
	
}
