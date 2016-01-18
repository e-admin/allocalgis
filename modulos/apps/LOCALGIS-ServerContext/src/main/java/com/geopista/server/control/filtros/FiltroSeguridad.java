/**
 * FiltroSeguridad.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.control.filtros;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import com.geopista.protocol.CResultadoOperacion;

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
    writer.println("Filtro de seguridad pasado para "+sPath);
   writer.println("===============");
    // Log the resulting string
    writer.flush();
    //if (!(sPath.indexOf(HeartBeat)>=0) && !(sPath.indexOf(CServletIntentos)>=0))
    //	filterConfig.getServletContext().log(sw.getBuffer().toString());
    chain.doFilter (request, response);

  }
}
