/**
 * CServletContaminantes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.contaminantes.web;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CEnvioOperacion;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.CConstantes;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesContaminantes;


public class CServletContaminantes extends LoggerHttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CServletContaminantes.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.debug("Inicio.");
		doPost(request, response);
	}

	private String buildResponse(CResultadoOperacion resultadoOperacion) {
		try {
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
			marshaller.setEncoding("ISO-8859-1");
			marshaller.marshal(resultadoOperacion);

			String response = sw.toString();
			logger.debug("response: " + response);

			return response;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return "";
		}

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		super.doPost(request);
		logger.debug("Inicio.");

		PrintWriter out = response.getWriter();
		CResultadoOperacion resultadoOperacion;
		
		try {
			JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
			Sesion userSession = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());

			Principal userPrincipal = userSession.getUserPrincipal();
			Enumeration userPerms = userSession.getRoleGroup().members();

			CEnvioOperacion envioOperacion = (com.geopista.protocol.CEnvioOperacion) Unmarshaller.unmarshal(com.geopista.protocol.CEnvioOperacion.class, new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
			logger.debug("envioOperacion.getComando(): " + envioOperacion.getComando());

			
			switch (envioOperacion.getComando()) {
				case CConstantes.CMD_GET_SEARCHED_ADDRESSES_BY_NUMPOLICIA:
					resultadoOperacion = COperacionesContaminantes.getSearchedAddressesByNumPolicia(envioOperacion.getHashtable(), userSession.getIdMunicipio());
					break;
				default:
					logger.warn("Comand not found. envioOperacion.getComando(): " + envioOperacion.getComando());
					resultadoOperacion = new CResultadoOperacion(false, "Command not found");
					break;
			}
			
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Excepcion al procesar peticion:" + sw.toString());
			resultadoOperacion = new CResultadoOperacion(false, "Excepcion al procesar peticion:" + e.toString());
		}

		String respuesta = buildResponse(resultadoOperacion);
		out.print(respuesta);
		out.flush();
		out.close();
	}
}
