/**
 * GetSesions.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.LoggerHttpServlet;
import com.localgis.server.SessionsContextShared;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 16-jun-2004
 * Time: 15:59:15
 */
public class GetSesions extends LoggerHttpServlet {
    private static final long serialVersionUID = 9149159948554732570L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetSesions.class);
            public void doPost (HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
            {
               super.doPost(request);	
               try
               {
                    JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
                    String sIdMunicipio="";
                    if (userPrincipal!=null)
                    {    String  sIdSesion= userPrincipal.getName();
                    
                    	PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
        	    	                    
                         Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                        if (sSesion!=null)
                          {
                            sIdMunicipio=sSesion.getIdMunicipio();

                        }
                    }
                    Marshaller.marshal(PasarelaAdmcar.listaSesiones.getListaSesionesSimple(sIdMunicipio),response.getWriter());
             //       Marshaller.marshal(PasarelaAdmcar.listaSesiones, new OutputStreamWriter(System.out));


               }catch(Exception e)
               {
                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                   e.printStackTrace(pw);
                   logger.error("Excepción al crear el nuevo USUARIO:"+sw.toString());
                   try
                   {
                        Marshaller.marshal(new CResultadoOperacion(false, "Excepción al crear un usuario:"+e.toString()),response.getWriter());
                   }catch(Exception ex){}
               }
              }
}

