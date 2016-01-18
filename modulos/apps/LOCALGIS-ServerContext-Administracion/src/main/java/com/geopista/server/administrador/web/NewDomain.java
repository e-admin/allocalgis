/**
 * NewDomain.java
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
import java.util.Vector;

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
 * Date: 09-jun-2004
 * Time: 17:58:08
 */
public class NewDomain  extends LoggerHttpServlet
 {
      
	private static final long serialVersionUID = 2017155811547898847L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UpdateRol.class);
       public void doPost (HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
       {
    	   	   super.doPost(request);	
               CResultadoOperacion resultado=null;
               try
               {
                     Domain domain=(Domain)Unmarshaller.unmarshal(Domain.class,new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
                     Marshaller.marshal(domain,new OutputStreamWriter(System.out));
                     resultado=COperacionesAdministrador.ejecutarNewDomain(domain);
                     if (resultado.getResultado())
                         domain.setIdDomain(resultado.getDescripcion());
                     logger.debug("Identificador del Nodo insertado: "+domain.getIdDomain());
                     Vector vResultados = new Vector();
                     vResultados.add(domain);
                     resultado.setVector(vResultados);
                     try {new ReloadDomains();}catch(Exception e){}
               }catch(Exception e)
               {

                   java.io.StringWriter sw=new java.io.StringWriter();
                   java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("Excepción al crear el nuevo domminio padre:"+sw.toString());
                   resultado= new CResultadoOperacion(false, "Excepción al crear el nuevo domminio padre:"+e.toString());
               }
                Writer writer = response.getWriter();
                writer.write (resultado.buildResponse());
                writer.flush();
                writer.close();
              }
}
