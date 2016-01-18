/**
 * DeleteContact.java
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exolab.castor.xml.Unmarshaller;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.metadatos.CI_ResponsibleParty;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.database.COperacionesMetadatos;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-jul-2004
 * Time: 12:29:08
 */
public class DeleteContact extends HttpServlet {
        private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeleteContact.class);
        public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
        {
            CResultadoOperacion rDelete=null;
           try
           {
                 CI_ResponsibleParty contact=(CI_ResponsibleParty)Unmarshaller.unmarshal(CI_ResponsibleParty.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
                 //Marshaller.marshal(user,new OutputStreamWriter(System.out));
                 logger.info("Borrando el contacto:"+contact.getId()+" Nombre: "+contact.getIndividualName()+ " Empresa:" + contact.getOrganisationName());
                 rDelete=COperacionesMetadatos.ejecutarDeleteContact(contact);

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
