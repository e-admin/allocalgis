/**
 * 
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
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesAdministrador;


/**
 * @author seilagamo
 *
 */
public class NewEntidad extends LoggerHttpServlet {
    

	private static final long serialVersionUID = 7154488014521925686L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(NewEntidad.class);
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	super.doPost(request);	
        CResultadoOperacion resultado = null;
        try {
            Entidad entidad = (Entidad) Unmarshaller.unmarshal(Entidad.class, new StringReader(
                    request.getParameter(EnviarSeguro.mensajeXML)));
            Marshaller.marshal(entidad, new OutputStreamWriter(System.out));
            resultado = COperacionesAdministrador.ejecutarNewEntidad(entidad);
            
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Excepción al crear la nueva ENTIDAD:" + sw.toString());
            resultado = new CResultadoOperacion(false, "Excepción al crear una entidad:"
                    + e.toString());
        }
        Writer writer = response.getWriter();
        writer.write(resultado.buildResponse());
        writer.flush();
        writer.close();
    }
   
   
}
