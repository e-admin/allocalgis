package com.geopista.server.metadatos.web;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.net.EnviarSeguro;

import com.geopista.protocol.metadatos.MD_Metadata;
import com.geopista.server.database.COperacionesMetadatos;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Writer;
import java.io.StringReader;
import java.util.Vector;

import org.exolab.castor.xml.Unmarshaller;
import org.mortbay.jaas.JAASUserPrincipal;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaAcl;
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
 * Date: 20-ago-2004
 * Time: 10:18:44
 */
public class SaveMetadata  extends HttpServlet {
       private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(NewContact.class);
            public void doPost (HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
            {
               CResultadoOperacion resultado=null;
               try
               {
                   //El identificador de sesión debe venir como el nombre del user principal
                    String sIdMunicipio=null;
                    JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
                    if (userPrincipal==null)
                    {
                         resultado=new CResultadoOperacion(false,"El usuario no ha sido autenticado por JAAS");
                    }
                    else
                    {
                         Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(userPrincipal.getName());
                         if (sSesion!=null){
	                         sIdMunicipio= request.getParameter(EnviarSeguro.idMunicipio);
	                         sSesion.setIdMunicipio(sIdMunicipio);
                         }
                         MD_Metadata metadato=(MD_Metadata)Unmarshaller.unmarshal(MD_Metadata.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
                         if (metadato.getId_capa()!=null)
                         {
                                String id_acl=COperacionesMetadatos.getIdAclCapa(metadato.getId_capa());
                                if (id_acl!=null)
                                {
                                    GeopistaAcl acl = sSesion.getPerfil(new Long(id_acl).longValue());
                                    if (!acl.checkPermission(new GeopistaPermission(GeopistaPermission.ESCRIBIR_CAPA)))
                                    {
                                        resultado= new CResultadoOperacion(false,"El usuario no tiene permisos para grabar los metadatos de esa capa");
                                    }
                                }

                         }
                         if (resultado==null)
                         {
                            if (metadato.getFileidentifier()==null)
                                 resultado=COperacionesMetadatos.ejecutarNewMetadata(metadato,sIdMunicipio);
                            else
                                resultado=COperacionesMetadatos.ejecutarUpdateMetadata(metadato,sIdMunicipio);
                            Vector vResultados = new Vector();
                            vResultados.add(metadato);
                            logger.debug("ID de la identificacion del metadato: "+(metadato.getIdentificacion()!=null?metadato.getIdentificacion().getIdentification_id():"Identificacion nula"));
                            resultado.setVector(vResultados);
                         }
                    }
             }catch(Exception e)
               {
                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                   e.printStackTrace(pw);
                   logger.error("Excepción al crear el nuevo METADATO:"+sw.toString()+" XML:"+request.getParameter(EnviarSeguro.mensajeXML));
                   resultado= new CResultadoOperacion(false, "Excepción al crear un metadato:"+e.toString());
               }
                Writer writer = response.getWriter();
                writer.write (resultado.buildResponse());
                writer.flush();
                writer.close();
              }
}


