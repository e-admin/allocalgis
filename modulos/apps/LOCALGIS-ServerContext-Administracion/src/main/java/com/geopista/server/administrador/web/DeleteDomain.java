/**
 * DeleteDomain.java
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.dominios.Domain;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administrador.ReloadDomains;
import com.geopista.server.database.COperacionesAdministrador;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-oct-2004
 * Time: 15:08:52
 */
public class DeleteDomain  extends LoggerHttpServlet {
      	private static final long serialVersionUID = 4523713570480399366L;
		private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeleteDomain.class);
        public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
        {
        	super.doPost(request); 
        	
            CResultadoOperacion rDelete=null;
           try
           {
                 Domain nodo=(Domain)Unmarshaller.unmarshal(Domain.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
                 Marshaller.marshal(nodo,new OutputStreamWriter(System.out));
                 logger.info("Borrando el Domain:"+nodo.getIdDomain()+" Descripcion: "+nodo.getName());
                 rDelete=COperacionesAdministrador.ejecutarDeleteDomain(nodo);
                 try {new ReloadDomains();}catch(Exception e){}
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