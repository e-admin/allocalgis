/**
 * SigmTramitacionWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.sigm;

import ieci.tdw.ispac.services.ws.server.Cadena;
import ieci.tecdoc.sgm.tram.ws.server.Expediente;
import ieci.tecdoc.sgm.tram.ws.server.InfoBProcedimiento;
import ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos;
import ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService;
import ieci.tecdoc.sgm.tram.ws.server.TramitacionWebServiceServiceLocator;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import javax.xml.rpc.ServiceException;

public class SigmTramitacionWS {

	private static final String CONTEXT_PATH = "SIGEM_TramitacionWS/services/TramitacionWebService?wsdl";

	public static List<InfoBProcedimiento> getAllProcedimientos(String url, String idEntidad, String nombreProcedimiento) throws RemoteException, MalformedURLException, ServiceException{		
		TramitacionWebService tramitacionWebService = getWebService(url + CONTEXT_PATH);
		ListaInfoBProcedimientos listaInfoBProcedimientos = tramitacionWebService.getProcedimientosPorTipo(idEntidad, 1, nombreProcedimiento);
		if(listaInfoBProcedimientos.getReturnCode().equals("OK"))
			return Arrays.asList(listaInfoBProcedimientos.getProcedimientos());
		return null;	
	}
	
	public static Expediente getExpediente(String url, String idEntidad, String numExpediente) throws RemoteException, MalformedURLException, ServiceException{		
		TramitacionWebService tramitacionWebService = getWebService(url + CONTEXT_PATH);
		Expediente expediente = tramitacionWebService.getExpediente(idEntidad, numExpediente);
		if(expediente.getReturnCode().equals("OK"))
			return expediente;
		return null;	
	}
	
	public static Cadena obtenerRegistrosEntidad(String url, String idEntidad, String nombreEntidad, String numExpediente) throws RemoteException, MalformedURLException, ServiceException{		
		TramitacionWebService tramitacionWebService = getWebService(url + CONTEXT_PATH);
		Cadena cadena = tramitacionWebService.obtenerRegistrosEntidad(idEntidad, nombreEntidad, numExpediente);
		if(cadena.getReturnCode().equals("OK"))
			return cadena;
		return null;	
	}

	public static Cadena busquedaAvanzada(String url, String idEntidad, String searchFormName, String searchXML) throws RemoteException, MalformedURLException, ServiceException{		
		TramitacionWebService tramitacionWebService = getWebService(url + CONTEXT_PATH);		
		Cadena cadena = tramitacionWebService.busquedaAvanzada(idEntidad, "PROC-LOM", searchFormName, searchXML, 1);
		if(cadena.getReturnCode().equals("OK"))
			return cadena;
		return null;	
	}
	
	private static TramitacionWebService getWebService(String url) 
	throws MalformedURLException, ServiceException{
		TramitacionWebServiceServiceLocator tramitacionWebServiceServiceLocator = new TramitacionWebServiceServiceLocator();
		TramitacionWebService tramitacionWebService = tramitacionWebServiceServiceLocator.getTramitacionWebService(new URL(url));
		return tramitacionWebService;
	}
	
}
