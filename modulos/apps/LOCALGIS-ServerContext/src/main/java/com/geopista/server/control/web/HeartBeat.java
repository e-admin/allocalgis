/**
 * HeartBeat.java
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.control.Sesion;
import com.geopista.server.database.CPoolDatabase;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-sep-2004
 * Time: 10:03:57
 */
public class HeartBeat extends HttpServlet
{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HeartBeat.class);
    public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	/*try {
			JAASUserPrincipal jassUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
			Sesion userSesion=null;
			if (jassUserPrincipal!=null)
				userSesion = PasarelaAdmcar.listaSesiones.getSesion(jassUserPrincipal.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	
         logger.debug("HEARBEAT de servidor funcionando. "+request.getContextPath()+"->Numero de conexiones: "+CPoolDatabase.getNumeroConexiones());
         //System.out.println("HEARBEAT de servidor funcionando. "+request.getContextPath()+"->Numero de conexiones: "+CPoolDatabase.getNumeroConexiones());
         response.setContentType ("text/html");
         Writer writer = response.getWriter();
         writer.write (com.geopista.security.SecurityManager.HeartBeat);
         writer.flush();
         writer.close();
     }
    public void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doPost(request, response);
     }
}

