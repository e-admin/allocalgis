/**
 * NewRol.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador.web;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Rol;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesAdministrador;
import com.localgis.server.SessionsContextShared;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 02-jun-2004
 * Time: 12:24:47
 */


public class NewRol extends LoggerHttpServlet {
    
	private static final long serialVersionUID = 1L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UpdateRol.class);
            public void doPost (HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
            {
            	super.doPost(request);	
               CResultadoOperacion resultado=null;
               try
               {
                     Rol rol=(Rol)Unmarshaller.unmarshal(Rol.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
                     Marshaller.marshal(rol,new OutputStreamWriter(System.out));
                     JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
                     String sIdUser="";
                     String sIdEntidad="";
                     if (userPrincipal!=null)
                     {
                          String  sIdSesion= userPrincipal.getName();
                        
                          PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
              	    	
                          Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                          if (sSesion!=null)
                          {
                                sIdUser=sSesion.getIdUser();
                                sIdEntidad=sSesion.getIdEntidad();
                          }
                     }
                     resultado=COperacionesAdministrador.ejecutarNewRol(rol, sIdUser, sIdEntidad);
                     //rActualizar= new CResultadoOperacion(true, "Actualizacion realizada con exito");

               }catch(Exception e)
               {
                   resultado= new CResultadoOperacion(false, "Excepción al realizar la actualizarion:"+e.toString());
               }
                Writer writer = response.getWriter();
                writer.write (resultado.buildResponse());
                writer.flush();
                writer.close();
              }
}