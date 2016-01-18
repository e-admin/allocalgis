package com.geopista.server.control.filtros;


import org.w3c.dom.css.Counter;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Writer;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.net.EnviarSeguro;

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-abr-2004
 * Time: 15:19:39
 * To change this template use Options | File Templates.
 */
public class FiltroSeguridad  implements Filter {
  private FilterConfig filterConfig = null;
  private static final String HeartBeat="HeartBeat";
  private static final String CServletIntentos = "CServletIntentos";
  private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FiltroSeguridad.class);


  public void init(FilterConfig filterConfig)
    throws ServletException {
    this.filterConfig = filterConfig;
  }
  public void destroy() {
    this.filterConfig = null;
  }
  public void doFilter(ServletRequest request,
    ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    if (filterConfig == null)
      return;
    StringWriter sw = new StringWriter();
    PrintWriter writer = new PrintWriter(sw);
   JAASUserPrincipal userPrincipal = (JAASUserPrincipal)((HttpServletRequest)request).getUserPrincipal();
    String sPath=((HttpServletRequest)request).getServletPath();
    if (sPath==null) sPath="";
    if (userPrincipal==null && !(sPath.indexOf(HeartBeat)>=0) && !(sPath.indexOf(CServletIntentos)>=0))
     {
            writer.println();
            writer.println("===============");
            writer.println("Filtro de seguridad no pasado para "+sPath);
            writer.println("===============");
            logger.error("Filtro de seguridad no pasado para "+sPath);
            CResultadoOperacion resultado = new CResultadoOperacion(false, "Error en la configuracion de autenticacion mirar el fichero web.xml");
            response.setContentType ("text/html");
            Writer writerRES = response.getWriter();
            writerRES.write (resultado.buildResponse());
            writerRES.flush();
	        writerRES.close();
         filterConfig.getServletContext().log(sw.getBuffer().toString());
            return;
        }
    writer.println();
    writer.println("===============");
    writer.println("Filtro de seguridad pasado" );
     writer.println("===============");
    // Log the resulting string
    writer.flush();
    if (!(sPath.indexOf(HeartBeat)>=0) && !(sPath.indexOf(CServletIntentos)>=0))
    	filterConfig.getServletContext().log(sw.getBuffer().toString());
    chain.doFilter (request, response);

  }
}
