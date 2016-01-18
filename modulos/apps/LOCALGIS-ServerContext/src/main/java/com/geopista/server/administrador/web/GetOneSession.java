/**
 * GetOneSession.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador.web;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exolab.castor.xml.Marshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.sso.protocol.control.SesionBean;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesControl;
import com.localgis.server.SessionsContextShared;

/**
 * Created by IntelliJ IDEA. User: angeles Date: 16-jun-2004 Time: 15:59:15
 */
public class GetOneSession extends LoggerHttpServlet {
	private static final long serialVersionUID = 1L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GetOneSession.class);
	
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		response.getWriter().append("Authentication Successful!"); 
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//logger.error("INICIO GetOneSession:");
		super.doPost(request);
		try {
			String sIdSesion = request.getParameter("mensajeXML");
			
			PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
	    	
			Sesion sSesion = (Sesion) PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
			if (sSesion!=null){
				SesionBean sbSesion = new SesionBean(sSesion.getIdSesion(),
						sSesion.getIdApp());
				sbSesion.setsName(sSesion.getUserPrincipal().getName());
				sbSesion.setIdEntidad(getUserIdEntidad(sSesion.getUserPrincipal().getName()));
				//sbSesion.setIdMunicipio(sSesion.getIdMunicipio());
				sbSesion.setAlMunicipios(sSesion.getAlMunicipios());
				sbSesion.setIdUser(sSesion.getIdUser());
				Marshaller.marshal(sbSesion, response.getWriter());
			}else{
				Marshaller.marshal(new SesionBean(), response.getWriter());
			}

		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Excepción al solicitar la sesion:" + sw.toString());
			try {
				Marshaller.marshal(new CResultadoOperacion(false,
						"Excepción al solicitar la sesion:" + e.toString()),
						response.getWriter());
			} catch (Exception ex) {
			}
		}
	}

	private String getUserIdEntidad(String userName) {
		String idEntidad = "0";
		Vector<String> vParametros = new Vector<String>();
		vParametros.add(0, userName.toUpperCase());
		CResultadoOperacion rQuery = COperacionesControl.ejecutarQuery(
				"Select id_entidad from IUSERUSERHDR WHERE upper(NAME)=?",
				vParametros);
		try {
			if (rQuery.getVector().size() > 0) {
				idEntidad = ((String) (rQuery.getVector().get(0)) == "") 
						? "0" : (String) (rQuery.getVector().get(0));
			}
		} catch (Exception e) {
			logger.error("Error al obtener la información del usuario "
					+ userName + " de la base de datos", e);
		}
		return idEntidad;
	}

}
