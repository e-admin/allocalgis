/**
 * InvalidateSession.java
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
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesControl;
import com.localgis.server.SessionsContextShared;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-sep-2004
 * Time: 10:03:57
 */

public class InvalidateSession extends LoggerHttpServlet
{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InvalidateSession.class);
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		doPost(request,response);
	}
    
    public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	 super.doPost(request);
         //El identificador de sesión debe venir como el nombre del user principal
    	 
    	 request.getSession(false);
    	 request.getSession().invalidate();
    	 Writer writer=response.getWriter();
         writer.write (new CResultadoOperacion(true, "Sesion invalidada").buildResponse());
         writer.flush();
         writer.close();
     }
    

    /* ------------------------------------------------ */
    /** Create user for test
     * @exception ServletException
     */
    public void init ()
        throws ServletException
    {


    }


    /* ------------------------------------------------ */
    /** Destroy servlet, drop tables.
     */
    public void destroy ()
    {
     }


}
