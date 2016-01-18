/**
 * ClientConsole_v2_00.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.ws.cliente;

import java.io.StringReader;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.fip.FicheroFipConsoleBean;

import es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub;
import es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.FechaRefundido;
import es.mitc.redes.urbanismoenred.serviciosweb.v2_00.UrbrWSStub.FechaRefundidoE;

public class ClientConsole_v2_00 {
	
	static Logger logger = Logger.getLogger(ClientGestorFip.class);
	static int WS_TIMEOUT = 300000; // default value
	static int WS_TIMEOUTFIP = 300000; // default value
	
	static String targetEndPoint = null;
	
	public static String getTargetEndPoint() {
		return targetEndPoint;
	}

	public static void setTargetEndPoint(String targetEndPoint) {
		ClientConsole_v2_00.targetEndPoint = targetEndPoint + "/urbrWS";
	}

	public FicheroFipConsoleBean  obtenerFechaRefundido(ApplicationContext app, int idAmbito)throws Exception{
		FicheroFipConsoleBean fichFipConsole = null;
		UrbrWSStub customer = null;
		UrbrWSStub.FechaRefundidoResponseE response = null;
		
		try { 

			FechaRefundido fe = new FechaRefundido();
			fe.setIdAmbito(idAmbito);
			FechaRefundidoE fee = new FechaRefundidoE();
			fee.setFechaRefundido(fe);
			
			// creamos el soporte
			customer = new UrbrWSStub(targetEndPoint);
			
			
			// invocamos al web service
			response = customer.fechaRefundido(fee);
			String strXml = response.getFechaRefundidoResponse().get_return();
			if (strXml != null) {
				 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				 DocumentBuilder db = dbf.newDocumentBuilder();
				 InputSource is = new InputSource();
				 is.setCharacterStream(new StringReader(strXml));

				 Document doc = db.parse(is);
				 Element element = doc.getDocumentElement();
				 
				 fichFipConsole = new FicheroFipConsoleBean();
				 fichFipConsole.setFecha(element.getAttribute("fecha"));
				 fichFipConsole.setUrlFip(element.getTextContent());
				 
			}
			
		}
		catch (Exception excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la fecha del refundido");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"No se ha podido establecer la comunicacion con el Web Service de Urbanismo.\nPor favor revise la configuracion \"Url del WS de la Consola de Urbanismo.\n", "Error",
				JOptionPane.ERROR_MESSAGE);
			//throw excepcionDeInvocacion;
		}		
		
		return fichFipConsole;
	}
	

}
