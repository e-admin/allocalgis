/**
 * LoginFilterFeatures.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.localgis.web.core.security.dnie.CertificateAuthManager;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Filtro que se encarga de comprobar si se ha realizado login en la aplicacion
 */
public class LoginFilterFeatures implements Filter {

	/**
	 * Constantes
	 */
    public static final String LOGIN_ATTRIBUTE = "LoginAttribute";
    
    /**
     * Variables
     */
    private String loginAction;

    /**
     * Inicialización
     * @param config: 
     * @throws ServletException: 
     */
    public void init(FilterConfig config) throws ServletException {
        loginAction = config.getInitParameter("loginAction");
    }

    /**
     * 
     * 
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	/*
         * Si no se ha hecho login guardamos la pagina pedida en la session y
         * redirigimos a la pantalla de login
         */
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpSession session = httpServletRequest.getSession();
        String loginActionWithContext = httpServletRequest.getContextPath()+loginAction;
        //COMENTAR
        //session.setAttribute(LoginFilterFeatures.LOGIN_ATTRIBUTE, "satec_pre");
        //
        if (!httpServletRequest.getRequestURI().equals(loginActionWithContext) && session.getAttribute(LOGIN_ATTRIBUTE) == null) {	  
    		String fullRequestURL = httpServletRequest.getRequestURL().toString();
        	if(httpServletRequest.getParameter("requesturi")==null){
				Enumeration e = httpServletRequest.getParameterNames();
				String requesturi = "";
				if(e.hasMoreElements()) fullRequestURL += "?";
				while(e.hasMoreElements()){
					String attribute = (String) e.nextElement();				
					fullRequestURL += attribute + "=" + httpServletRequest.getParameter(attribute);	
					if(e.hasMoreElements()) fullRequestURL += "&";
				}
			}else fullRequestURL = httpServletRequest.getParameter("requesturi");
        	request.setAttribute("fullRequestURL", fullRequestURL);	
            //-----NUEVO----->
    		CertificateAuthManager.dnieAuthManager(httpServletRequest);
    		//---FIN NUEVO--->
        	request.getRequestDispatcher(loginAction).forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * Sobreescritura del destructor
     */
    public void destroy() {
    }

}
