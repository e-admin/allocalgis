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
public class DeleteEntidad extends LoggerHttpServlet {

    private static final long serialVersionUID = 330617569430019164L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DeleteEntidad.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CResultadoOperacion resultado = null;
        super.doPost(request);	 

        try {
            
            Entidad entidad = (Entidad) Unmarshaller.unmarshal(Entidad.class, new StringReader(
                    request.getParameter(EnviarSeguro.mensajeXML)));
            Marshaller.marshal(entidad, new OutputStreamWriter(System.out));
            resultado = COperacionesAdministrador.ejecutarDeleteEntidad(entidad);
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Excepción al eliminar la ENTIDAD: " + sw.toString());
            resultado = new CResultadoOperacion(false,
                    "Excepción al eliminar una entidad:" + e.toString());
        }
        Writer writer = response.getWriter();
        writer.write(resultado.buildResponse());
        writer.flush();
        writer.close();
    }
}
