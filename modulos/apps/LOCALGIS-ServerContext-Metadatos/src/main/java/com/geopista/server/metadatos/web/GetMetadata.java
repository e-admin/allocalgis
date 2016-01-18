/**
 * GetMetadata.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.metadatos.web;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.control.SesionUtils_LCGIII;
import com.geopista.protocol.metadatos.MD_Metadata;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.server.database.COperacionesMetadatos;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 27-ago-2004
 * Time: 9:09:04
 */
public class GetMetadata extends HttpServlet {
            private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetMetadata.class);
            public void doPost (HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
            {
               CResultadoOperacion resultado=null;
               try
               {
                    JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
                    if (userPrincipal==null)
                    {
                        resultado=new CResultadoOperacion(false,"El usuario no ha sido autenticado por JAAS");
                     }
                     else
                     {
                        String  sIdSesion= userPrincipal.getName();
                        Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                        String sFileIdentifier =request.getParameter(EnviarSeguro.mensajeXML);
                        resultado=COperacionesMetadatos.ejecutarGetMetadata(sFileIdentifier);
                        if (resultado.getResultado()&&resultado.getVector()!=null&&resultado.getVector().size()>0)
                        {
                             MD_Metadata auxMetadato=(MD_Metadata)resultado.getVector().elementAt(0);
                             if(auxMetadato.getId_acl_capa()!=null)
                             {
                                 GeopistaAcl acl=SesionUtils_LCGIII.getPerfil(sSesion,new Long(auxMetadato.getId_acl_capa()).longValue());
                                 if (!acl.checkPermission(new GeopistaPermission(GeopistaPermission.LEER_CAPA)))
                                 {
                                     resultado= new CResultadoOperacion(false,"El usuario no tiene permisos para visualizar los metadatos de esa capa");
                                 }
                             }
                        }
                    }
               }catch(Exception e)
               {
                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                   e.printStackTrace(pw);
                   logger.error("Excepción al actualizar el USUARIO:"+sw.toString());
                   resultado= new CResultadoOperacion(false, "Excepción al actualizar un usuario:"+e.toString());
               }
                Writer writer = response.getWriter();
                writer.write (resultado.buildResponse());
                writer.flush();
                writer.close();
              }
}
