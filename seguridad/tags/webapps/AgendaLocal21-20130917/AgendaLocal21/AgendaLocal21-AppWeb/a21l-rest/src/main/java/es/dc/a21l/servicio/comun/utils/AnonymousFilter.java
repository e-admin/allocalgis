/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.comun.utils;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class AnonymousFilter extends GenericFilterBean {

	public void doFilter(ServletRequest request, ServletResponse response,	FilterChain filtros) throws IOException, ServletException {

//	    if (SecurityContextHolder.getContext().getAuthentication() == null) {
//	    	SecurityContextHolder.getContext().setAuthentication(
//	    			new UsernamePasswordAuthenticationToken(
//	    					"anonimo", 
//	    					"anonimo", 
//	    					Arrays.asList(
//	    							new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_ANONIMO")})));
//	    }
	    filtros.doFilter(request, response);
	}

}
