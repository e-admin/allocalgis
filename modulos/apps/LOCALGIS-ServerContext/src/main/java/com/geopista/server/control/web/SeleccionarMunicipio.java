/**
 * SeleccionarMunicipio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.control.web;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.localgis.server.SessionsContextShared;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-may-2004
 * Time: 16:25:39
 */
public class SeleccionarMunicipio extends LoggerHttpServlet
{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SeleccionarMunicipio.class);

    public void doPost (HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
    	super.doPost(request); 
        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
        String  sIdSesion= userPrincipal.getName();
        PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");
        Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
		String sIdMunicipio = request.getParameter (EnviarSeguro.idMunicipio);
		sSesion.setIdMunicipio(sIdMunicipio);
		
        CResultadoOperacion resultado = new CResultadoOperacion(true, sSesion.getIdSesion());


//	    Vector vector = new Vector();
//	    vector.addElement(sIdMunicipio);
//	    resultado.setVector(vector);
        response.setContentType ("text/html");
        Writer writer = response.getWriter();
        writer.write (resultado.buildResponse());
        writer.flush();
	    writer.close();
    }
    
}
