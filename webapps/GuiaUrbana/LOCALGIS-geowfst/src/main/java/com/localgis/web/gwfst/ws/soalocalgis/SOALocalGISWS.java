/**
 * SOALocalGISWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.soalocalgis;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPHeaderElement;

import com.localgis.ln.ISOALocalGIS;
import com.localgis.ln.ISOALocalGISHttpBindingStub;
import com.localgis.ln.ISOALocalGISLocator;
import com.localgis.ln.ISOALocalGISPortType;
import com.localgis.model.ot.EntidadOT;
import com.localgis.model.ot.MunicipioOT;

public class SOALocalGISWS {

	private static final String CONTEXT_PATH = "SOALocalGIS/services/ISOALocalGIS";
	
	public static EntidadOT [] getEntidadMunicipios(String url, Integer codigoINE) throws RemoteException, MalformedURLException, ServiceException, SOAPException {		
		
		ISOALocalGISPortType portType = new ISOALocalGISLocator().getISOALocalGISHttpPort(new URL(url + CONTEXT_PATH));
//		ISOALocalGISPortType iSOALocalGISPortType = new ISOALocalGISLocator().getISOALocalGISHttpPort();
//		ISOALocalGISPortType iSOALocalGISPortType2 = new ISOALocalGISLocator().getISOALocalGISHttpPort();
		ISOALocalGISHttpBindingStub httpBindingStub = (ISOALocalGISHttpBindingStub) portType;
//		ISOALocalGISHttpBindingStub a = new ISOALocalGISHttpBindingStub(new URL(""),null);
		SOAPHeaderElement header = new SOAPHeaderElement("namespace", "WS_Header");
		SOAPElement node = header.addChildElement("Username");  
		node.addTextNode("syssuperuser");  		  
		SOAPElement node2 = header.addChildElement("Password");  
		node2.addTextNode("sysgeopass");  
		httpBindingStub.setHeader(header);
		httpBindingStub.setUsername("syssuperuser");
		httpBindingStub.setPassword("sysgeopass");
		EntidadOT [] entidades = portType.obtenerEntidadMunicipios(codigoINE);
		if(entidades != null && entidades.length>0)
			return entidades;
		return null;	
	}	
	
}
