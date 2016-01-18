/**
 * LoginFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.localgis.web.core.security.sso.SSOAuthManager;

//import com.localgis.web.wm.security.SSOAuthManager;


/**
 * Filtro que se encarga de comprobar si se ha realizado login en la aplicacion
 * 
 * @author albegarcia
 * 
 */
public class LoginFilter implements Filter {

    /**
     * Constante que indica el nombre del atributo donde guardamos si se ha
     * hecho login o no. El valor del atributo será el nombre del usuario con el
     * que se hizo login
     */
    public static final String LOGIN_ATTRIBUTE = "LoginAttribute";
    private static Logger logger = Logger.getLogger(LoginFilter.class);
    /**
     * Página de login
     */
    private String loginAction;

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
        loginAction = config.getInitParameter("loginAction");
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*
         * Si no se ha hecho login guardamos la pagina pedida en la session y
         * redirigimos a la pantalla de login
         */
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpSession session = httpServletRequest.getSession();
        String loginActionWithContext = httpServletRequest.getContextPath()+loginAction;
        //System.out.println(session.getAttribute(LOGIN_ATTRIBUTE));
		if (!httpServletRequest.getRequestURI().equals(loginActionWithContext) && session.getAttribute(LOGIN_ATTRIBUTE) == null && !SSOAuthManager.ssoAuthManager(httpServletRequest)) {		
			request.getRequestDispatcher(loginAction).forward(request, response);
			return;
		}		
        chain.doFilter(request, response);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }

}
