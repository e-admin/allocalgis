/**
 * CertificateAuthManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.security.dnie;

import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.core.config.Configuration;

public class CertificateAuthManager {
	private static final Log logger = LogFactory.getLog(CertificateAuthManager.class);

    /**
     * Propiedades de configuracion DNIe
     */
    public static final String DNIE_AUTH_ACTIVE = "dnie.authactive";
    public static final String HTTP_CONNECTION_PORT = "http.connection.port";
    public static final String HTTP_CONNECTION_URL = "http.connection.url";
   
    public static boolean dnieAuthManager(HttpServletRequest httpServletRequest){
       	boolean result = false;    	
 		try { 		
 			httpServletRequest.getSession().setAttribute(HTTP_CONNECTION_PORT, httpServletRequest.getLocalPort());
 			if(httpServletRequest.getSession().getAttribute(HTTP_CONNECTION_URL)==null){
	 			String fullRequestURL = httpServletRequest.getRequestURL().toString();
	 			fullRequestURL += "?" + httpServletRequest.getQueryString();
	 			httpServletRequest.getSession().setAttribute(HTTP_CONNECTION_URL, fullRequestURL);
 			}
 			if(Boolean.valueOf(Configuration.getPropertyString(DNIE_AUTH_ACTIVE))){  	
 				httpServletRequest.setAttribute(DNIE_AUTH_ACTIVE, true); 				
	        } 	
 			else httpServletRequest.setAttribute(DNIE_AUTH_ACTIVE, false);
		} catch (Exception e) {
			logger.info("ssoAuthManager() - ERROR: " + e.getMessage());		
		}
		return result;
    }
    
	public static String getNIFfromSubjectDN(String subjectDN){
		try{
			//RegEx para NIF o NIE
			Pattern pattern = Pattern.compile("([0-9]{8})[A-Za-z]|[X-Zx-z]([0-9]{7})[A-Za-z]");			
			Matcher m = pattern.matcher(subjectDN);
			//(devuelve la primera ocurrencia de nif o nie)
			while (m.find()) return m.group().toUpperCase();			
		}
		catch(Exception e){
			System.out.println(e);			
		}	
		return null;
	}
}
