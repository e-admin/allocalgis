/**
 * UpdateAll.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.metadatos.web;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.exolab.castor.xml.Unmarshaller;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.database.COperacionesMetadatos;


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
