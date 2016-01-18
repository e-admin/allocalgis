/**
 * UpdateUser.java
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
import com.geopista.protocol.administrador.EncriptarPassword;
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
 * Date: 25-may-2004
 * Time: 15:00:53
 */
public class UpdateUser extends LoggerHttpServlet {
       private static final long serialVersionUID = 3207348213621099394L;
		private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UpdateUser.class);
        public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
        {
            CResultadoOperacion rActualizar=null;
           try
           {
                 Usuario user=(Usuario)Unmarshaller.unmarshal(Usuario.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
                 //Marshaller.marshal(user,new OutputStreamWriter(System.out));
                 JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
                 String sIdUserActualizador="";
                 String sIdMunicipio="";
                 String sNameUserAdministrador="";
                 if (userPrincipal!=null)
                 {
                      String  sIdSesion= userPrincipal.getName();
                      
                      PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
          	    	
                      Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                      if (sSesion!=null)
                      {
                            sIdUserActualizador=sSesion.getIdUser();
                            sIdMunicipio=sSesion.getIdMunicipio();
                            sNameUserAdministrador=sSesion.getUserPrincipal().getName();
                      }
                 }
                 if(Constantes.SUPERUSER.equalsIgnoreCase(user.getName())&&
                    (!Constantes.SUPERUSER.equalsIgnoreCase(sNameUserAdministrador)))
                 {
                     rActualizar= new CResultadoOperacion(false, "No se puede actualizar el usuario super administrador "+Constantes.SUPERUSER);
                 }
                 else
                 {
                     //encriptamos la password
                	 EncriptarPassword enc=new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
                     user.setPassword(enc.encriptar(user.getPassword()));
                     rActualizar=COperacionesAdministrador.ejecutarActualizarUser(user, sIdUserActualizador, sIdMunicipio);
                 }
           }catch(Exception e)
           {
               rActualizar= new CResultadoOperacion(false, "Excepción al realizar la actualizacion del usuario:"+e.toString());
               logger.error("Exception",e);
           }
            Writer writer = response.getWriter();
            writer.write (rActualizar.buildResponse());
            writer.flush();
	        writer.close();
          }
}