package com.geopista.server.administrador.web;

import com.geopista.protocol.CResultadoOperacion;

import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;

import com.geopista.protocol.administrador.Usuario;

import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesAdministrador;
import com.geopista.server.administrador.init.Constantes;
import com.localgis.server.SessionsContextShared;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import admcarApp.PasarelaAdmcar;

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
                 Constantes.SUPERUSER = "SYSSUPERUSER";
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