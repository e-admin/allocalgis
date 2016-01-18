/**
 * DeleteUser.java
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
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;


import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Usuario;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administrador.init.Constantes;
import com.geopista.server.database.COperacionesAdministrador;
import com.localgis.server.SessionsContextShared;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 03-jun-2004
 * Time: 18:43:20
 */

public class DeleteUser extends LoggerHttpServlet {
        private static final long serialVersionUID = 7322960730592143916L;
		private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeleteUser.class);
        public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
        {
        	super.doPost(request);	   
            CResultadoOperacion rDelete=null;
           try
           {
                 Usuario user=(Usuario)Unmarshaller.unmarshal(Usuario.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
                 Marshaller.marshal(user,new OutputStreamWriter(System.out));
                 logger.info("Borrando el usuario:"+user.getId()+" Nombre: "+user.getName());
                 
                 if(Constantes.SUPERUSER.equalsIgnoreCase(user.getName()))
                     rDelete= new CResultadoOperacion(false, "No se puede borrar al usuario super administrador "+Constantes.SUPERUSER);
                 else
                 {
                     //Esto servira para saber quien ha borrado el rol
                     JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
                     String sIdUser="";
                     String sIdMunicipio="";
                     if (userPrincipal!=null)
                     {
                          String  sIdSesion= userPrincipal.getName();
                          
                          PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
              	    	
                          Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                          if (sSesion!=null)
                          {
                                sIdUser=sSesion.getIdUser();
                                sIdMunicipio=sSesion.getIdMunicipio();
                          }
                     }
                     rDelete=COperacionesAdministrador.ejecutarDeleteUser(user, sIdMunicipio);
                 }
           }catch(Exception e)
           {
               rDelete= new CResultadoOperacion(false, "Excepción al realizar la actualizarion:"+e.toString());
           }
            Writer writer = response.getWriter();
            writer.write (rDelete.buildResponse());
            writer.flush();
	        writer.close();
          }
}