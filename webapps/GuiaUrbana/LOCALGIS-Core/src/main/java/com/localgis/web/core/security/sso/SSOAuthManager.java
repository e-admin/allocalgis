/**
 * SSOAuthManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.security.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.security.sso.beans.SesionBean;

/*
 * @Author dcaaveiro
 */
public class SSOAuthManager {
	
	private static final Log logger = LogFactory.getLog(SSOAuthManager.class);
	
	public static final String LOGIN_ATTRIBUTE = "LoginAttribute";
	public static final String TOKEN_ATTRIBUTE = "TokenAttribute";
	public static final String SSOACTIVE_ATTRIBUTE = "SSOActive";
    /**
     * Propiedades de configuracion SSO
     */
    public static final String SSO_AUTH_ACTIVE = "sso.authactive";
    public static final String SSO_CONNECTION_SERVER = "sso.connection.server";
    public static final String SSO_USERNAME = "sso.username";
    public static final String SSO_PASSWORD = "sso.password";	

    /**
     * Gestion de la autenticación con SSO 
     * httpServletRequest: HttpServletRequest de la llamada 
     * (si la llamada se hizo desde el Centralizador SSO contendrá el idSesion)
     * 
     * Comprueba si el sistema SSO esta activo, si lo está:
     * Recupera el parametro idSesion y comprueba que no sea null (que exista), si es distinto de null:
     * Llama a la funcion getSession de la clase ADMCARServices la cual a su vez llamará al servicio getOneSession del Core Modelo 
     * y devolverá uan instancia del JavaBean SesionBean con los datos de la sesion
     * Se obtendrá de este JavaBean el nombre el usuario logado, si existe se crea una cookie de sesión en el navegador 
     * autenticando la aplicación web
     */
    public static boolean ssoAuthManager(HttpServletRequest httpServletRequest){
       	boolean result = false;    	
 		try { 				
 			if(Boolean.valueOf(Configuration.getPropertyString(SSO_AUTH_ACTIVE))){  	
 				String SSOIdSesion = (String)httpServletRequest.getParameter("idSesion");
		    	if(SSOIdSesion!=null && !SSOIdSesion.equals("null")){
		    		String url = Configuration.getPropertyString(SSO_CONNECTION_SERVER);
		    		String username = Configuration.getPropertyString(SSO_USERNAME);
		    		String password = Configuration.getPropertyString(SSO_PASSWORD);
		    		String SSOSesionUserName = "";
		    		SesionBean sesionBean = ADMCARServices.getSession(url, SSOIdSesion, username, password);
		    		if(sesionBean.getIdSesion()!=null)
		    			SSOSesionUserName = sesionBean.getsName();		    		
		    		if(!SSOSesionUserName.equals("")){
		    			logger.info("SSOSesionUserName: " + SSOSesionUserName+ "Sesion:"+SSOIdSesion );
		    			httpServletRequest.getSession().setAttribute(LOGIN_ATTRIBUTE, SSOSesionUserName);
		    			httpServletRequest.getSession().setAttribute(TOKEN_ATTRIBUTE, SSOIdSesion);
		    			String SSOActive = (String)httpServletRequest.getParameter("ssoActive");
		    			if(SSOActive!=null)
		    				httpServletRequest.getSession().setAttribute(SSOACTIVE_ATTRIBUTE, true);
	 		    		result = true;	
		    		}
		    	}else logger.info("ssoAuthManager() - No Existe Sesion");
	        } 	
		} catch (Exception e) {
			logger.info("ssoAuthManager() - ERROR: " + e.getMessage());		
		}
		return result;
    }
    
    
    public static boolean checkSSOAuth(String SSOIdSesion){				
 		try {
			if(Boolean.valueOf(Configuration.getPropertyString(SSO_AUTH_ACTIVE))){  	
			    if(!SSOIdSesion.equals("")){
			    	String url = Configuration.getPropertyString(SSO_CONNECTION_SERVER);
			    	String username = Configuration.getPropertyString(SSO_USERNAME);
			    	String password = Configuration.getPropertyString(SSO_PASSWORD);
			   		SesionBean sesionBean = ADMCARServices.getSession(url, SSOIdSesion, username, password);
			   		if(sesionBean.getIdSesion()!=null && sesionBean.getsName()!=null)
			   			return true;
			    }
			}
		} catch (LocalgisConfigurationException e) {
			logger.info("checkSSOAuth() - ERROR: " + e.getMessage());		
		} 
 		logger.info("checkSSOAuth() - No Existe Sesion");
		return false;
    }
    
}