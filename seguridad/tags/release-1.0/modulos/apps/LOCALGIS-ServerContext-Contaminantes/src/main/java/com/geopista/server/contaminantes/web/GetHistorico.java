package com.geopista.server.contaminantes.web;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASPrincipal;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.PeticionBusquedaHistorico;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesContaminantes;

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
 * Date: 19-abr-2005
 * Time: 12:35:35
 */
public class GetHistorico  extends LoggerHttpServlet {
       private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetHistorico.class);
            public void doPost (HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
            {
               super.doPost(request);
               CResultadoOperacion resultado;
               try
               {
                   JAASPrincipal userPrincipal = (JAASPrincipal)request.getUserPrincipal();
                   if (userPrincipal==null)
                   {
                       resultado=new CResultadoOperacion(false,"El usuario no ha sido autenticado por JAAS");
                   }
                   else
                   {
                       String sIdSesion = userPrincipal.getName();
                       Sesion sSesion = PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
                       String sMunicipio = request.getParameter(EnviarSeguro.idMunicipio);
                       sSesion.setIdMunicipio(sMunicipio);
                       PeticionBusquedaHistorico peticion=(PeticionBusquedaHistorico)Unmarshaller.unmarshal(PeticionBusquedaHistorico.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
                       resultado=COperacionesContaminantes.getListaHistorico(peticion,sSesion);
                   }
               }catch(Exception e)
               {
                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                   e.printStackTrace(pw);
                   logger.error("Excepción al conseguir la lista parcial de metadatos:"+sw.toString());
                   resultado=new CResultadoOperacion(false, "Excepción al conseguir la lista de metadatos:"+e.toString());
               }
                Writer writer = response.getWriter();
                writer.write (resultado.buildResponse());
                writer.flush();
                writer.close();
              }
    }

