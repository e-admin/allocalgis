/**
 * RemoveSession.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador.web;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exolab.castor.xml.Marshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.sso.protocol.control.SesionBean;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administrador.init.Constantes;
import com.geopista.server.database.COperacionesControl;
import com.localgis.server.LocalgisSerlvetContextListener;
import com.localgis.server.SessionsContextShared;

/**
 * Created by IntelliJ IDEA. User: angeles Date: 16-jun-2004 Time: 15:59:15
 */
public class RemoveSession extends LoggerHttpServlet {
	private static final long serialVersionUID = 1L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(RemoveSession.class);
	
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		doPost(request,response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request);
		try {
			String sIdSesion = request.getParameter("sesion");
			
			PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
	    	
			if (sIdSesion==null)
				sIdSesion="";
			Sesion sSesion = (Sesion) PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
			if (sSesion!=null){
				 logger.info("Eliminando sesion:"+sSesion.getIdSesion());
				 Object sesionBorrada=PasarelaAdmcar.listaSesiones.delete(sSesion.getIdSesion());
				 if (sesionBorrada==null)
					 logger.info("La sesion no se ha borrado correctamente:"+sSesion.getIdSesion());
				 COperacionesControl.grabarLogout(sSesion.getIdSesion());
				 
				 SessionsContextShared.getContextShared().setSharedAttribute(this.getServletContext(), "UserSessions", PasarelaAdmcar.listaSesiones);
				 
				 
			     response.getWriter().write("Sesion"+sIdSesion+" borrada\n");
			     response.getWriter().write("Sesiones disponibles\n");
				 for (Enumeration<ISesion> e=PasarelaAdmcar.listaSesiones.getLista().elements();e.hasMoreElements();){
					 Sesion sesion=(Sesion) e.nextElement();
					 long fechaTopeConexion=(sesion.getFechaConexion().getTime()+(Constantes.TTL_USUARIOS*60*1000));
			         response.getWriter().write("Sesion-> Usuario: "+ sesion.getUserPrincipal().getName()+ " Aplicacion "+sesion.getIdApp()+". Identificador de sesion:"+ sesion.getIdSesion()+" Fecha Tope:"+new Date(fechaTopeConexion)+"\n");
				 }
			     
			}else{
		         response.getWriter().write("Sesiones disponibles\n");
				 for (Enumeration<ISesion> e=PasarelaAdmcar.listaSesiones.getLista().elements();e.hasMoreElements();){
					 Sesion sesion=(Sesion) e.nextElement();
					 long fechaTopeConexion=(sesion.getFechaConexion().getTime()+(Constantes.TTL_USUARIOS*60*1000));
			         response.getWriter().write("Sesion-> Usuario: "+ sesion.getUserPrincipal().getName()+ " Aplicacion "+sesion.getIdApp()+". Identificador de sesion:"+ sesion.getIdSesion()+" Fecha Tope:"+new Date(fechaTopeConexion)+"\n");
				 }
					 				
			}

		} catch (Exception e) {
			logger.error("Excepción al borrar la sesion:",e);
			response.getWriter().write("Error al intentar borrar la sesion\n");
		}
	}


}
