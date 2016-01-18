/**
 * SetConfigurationFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Filtro que se encarga de introducir en la request un objeto que indica si estamos en la parte publica o privada
 */
public class SetConfigurationFilter implements Filter {

    /**
     * Configuracion que gestiona el filtro
     */
    private String configuration;

    /**
     * 
     */
    public void init(FilterConfig config) throws ServletException {
        configuration = config.getInitParameter("configurationLocalgisWeb");
    }

    /**
     * 
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        httpServletRequest.setAttribute("configurationLocalgisWeb", configuration);
        chain.doFilter(request, response);
    }

    /**
     * Sobreescitura del destructor
     */
    public void destroy() {
    }

}
