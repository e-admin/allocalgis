package com.geopista.server.administrador.web;

import org.exolab.castor.xml.Marshaller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Vector;

import admcarApp.PasarelaAdmcar;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.sso.protocol.control.SesionBean;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesControl;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

/**
 * Created by IntelliJ IDEA. User: angeles Date: 16-jun-2004 Time: 15:59:15
 */
public class GetOneSession extends LoggerHttpServlet {
	private static final long serialVersionUID = 1L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GetOneSession.class);

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.error("INICIO GetOneSession:");
		super.doPost(request);
		try {
			String sIdSesion = request.getParameter("mensajeXML");
			Sesion sSesion = (Sesion) PasarelaAdmcar.listaSesiones
					.getSesion(sIdSesion);
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
			logger.error("Excepción al crear el nuevo USUARIO:" + sw.toString());
			try {
				Marshaller.marshal(new CResultadoOperacion(false,
						"Excepción al crear un usuario:" + e.toString()),
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
