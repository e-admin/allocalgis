/**
 * GetUsersList.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador.web;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Marshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.ldap.LDAPManager;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 03-jun-2004
 * Time: 18:43:20
 */

public class GetUsersList extends HttpServlet {
        private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetUsersList.class);

        private LDAPManager ldapManager;

        @Override
        public void init() throws ServletException {
        	
        	//TODO Toledo Inicializar
        	//ldapManager = LDAPManager.getInstance();
        }
        
        public void doPost (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
       try
       {
            JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
            String sIdMunicipio="";
            if (userPrincipal!=null)
            {    String  sIdSesion= userPrincipal.getName();
                 Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                if (sSesion!=null)
                  {
                    sIdMunicipio=sSesion.getIdMunicipio();

                }
            }
            
            Vector listaUsuarios = ldapManager.getInstance().getUserListLDAP();
            
            CResultadoOperacion resultado = new CResultadoOperacion();
            resultado.setVector(listaUsuarios);
            resultado.setResultado(true);
            
            Marshaller.marshal(resultado,response.getWriter());
    
       }catch(Exception e)
       {
           java.io.StringWriter sw=new java.io.StringWriter();
           java.io.PrintWriter pw=new java.io.PrintWriter(sw);
           e.printStackTrace(pw);
           logger.error("Excepción al crear el nuevo USUARIO:"+sw.toString());
           try
           {
                Marshaller.marshal(new CResultadoOperacion(false, "Excepción al listar los usuarios:"+e.toString()),response.getWriter());
           }catch(Exception ex){}
       }
      }
}