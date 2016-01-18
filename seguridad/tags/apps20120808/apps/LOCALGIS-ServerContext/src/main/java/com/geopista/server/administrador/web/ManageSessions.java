package com.geopista.server.administrador.web;


import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;


import admcarApp.PasarelaAdmcar;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.Operacion;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesAdministrador;


public class ManageSessions extends LoggerHttpServlet {

	private static final long serialVersionUID = 6334011650610334866L;
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ManageSessions.class);
         
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		super.doPost(request);
		CResultadoOperacion resultado=null;
		
		try {	
			 Operacion op = (Operacion) Unmarshaller.unmarshal(Operacion.class, 
					 new StringReader(request.getParameter(EnviarSeguro.mensajeXML)));
			 
			 Marshaller.marshal(op, new OutputStreamWriter(System.out));
	         resultado = COperacionesAdministrador.ejecutarSelectDetallesOperacion(op);
	            
		}
		catch(Exception e) {

            java.io.StringWriter sw=new java.io.StringWriter();
            java.io.PrintWriter pw=new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("[ManageSessions] Excepción al obtener los detalles de una operación:"+sw.toString());
            resultado= new CResultadoOperacion(false, "Error al obtener los detalles de una operación:"+e.toString());
        }
         
		Writer writer = response.getWriter();
         writer.write (resultado.buildResponse());
         writer.flush();
         writer.close();
       
	}
}
