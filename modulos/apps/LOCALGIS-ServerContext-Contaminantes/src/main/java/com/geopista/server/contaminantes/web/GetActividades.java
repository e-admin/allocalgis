/**
 * GetActividades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.contaminantes.web;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CEnvioOperacion;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesContaminantes;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 14-oct-2004
 * Time: 16:16:52
 */
public class GetActividades extends LoggerHttpServlet {
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetActividades.class);

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request);
		CResultadoOperacion resultado;
		try {
			JAASUserPrincipal userPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
			if (userPrincipal == null) {
				resultado = new CResultadoOperacion(false, "El usuario no ha sido autenticado por JAAS");
			} else {
				String sIdSesion = userPrincipal.getName();
				Sesion sSesion = PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                String sMunicipio = request.getParameter(EnviarSeguro.idMunicipio);
                sSesion.setIdMunicipio(sMunicipio);

				CEnvioOperacion envioOperacion = (CEnvioOperacion) Unmarshaller.unmarshal(CEnvioOperacion.class, new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
				Vector actividadesContaminantes = COperacionesContaminantes.getSearchedActividadesContaminantes(envioOperacion.getHashtable(), sSesion.getIdMunicipio());

				resultado = new CResultadoOperacion(true, "Actividades contaminantes listadas correctamente.");
				resultado.setVector(actividadesContaminantes);
			}
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Excepción al conseguir la lista de actividades:" + sw.toString());
			resultado = new CResultadoOperacion(false, "Excepción al conseguir la lista de actividades:" + e.toString());
		}
		Writer writer = response.getWriter();
		writer.write(resultado.buildResponse());
		writer.flush();
		writer.close();
	}
}
