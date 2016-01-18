package com.geopista.server.administrador.web;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.protocol.administrador.dominios.Domain;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesAdministrador;
import com.geopista.server.administrador.ReloadDomains;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;


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