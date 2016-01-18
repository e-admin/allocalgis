package com.geopista.server.metadatos.web;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.database.COperacionesMetadatos;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Writer;
import java.io.StringReader;
import java.util.Vector;

import org.mortbay.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;
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
 * Date: 11-oct-2004
 * Time: 15:20:59
 */
public class UpdateAll   extends HttpServlet {
        private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UpdateAll.class);
        public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
        {
            CResultadoOperacion rActualizar=null;
           try
           {
               String sIdMunicipio=null;
              JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
              if (userPrincipal!=null)
              {
                Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(userPrincipal.getName());
                if (sSesion!=null){
                    sIdMunicipio= request.getParameter(EnviarSeguro.idMunicipio);
                    sSesion.setIdMunicipio(sIdMunicipio);
                }
              }
              Vector parametros =(Vector)Unmarshaller.unmarshal(Vector.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
              String sCampo=(String)parametros.elementAt(0);
              String sValor=(String)parametros.elementAt(1);
              logger.info("Actualizando todos los metadatos "+sCampo+" nuevo valor "+ sValor);
              rActualizar=COperacionesMetadatos.ejecutarUpdateAll(sCampo,sValor,sIdMunicipio);

           }catch(Exception e)
           {
               rActualizar= new CResultadoOperacion(false, "Excepción al actualizar todos los metadatos:"+e.getMessage());
           }
            Writer writer = response.getWriter();
            writer.write (rActualizar.buildResponse());
            writer.flush();
	        writer.close();
          }
}
