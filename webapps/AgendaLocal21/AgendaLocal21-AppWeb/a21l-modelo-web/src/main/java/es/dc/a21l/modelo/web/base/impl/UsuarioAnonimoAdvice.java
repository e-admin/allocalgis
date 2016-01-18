/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.modelo.web.base.impl;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import es.dc.a21l.modelo.web.base.UsuarioDetalles;

public class UsuarioAnonimoAdvice implements MethodBeforeAdvice {
	private UsuarioDetalles usuarioAnonimo;

	@Autowired
	public void setUsuarioAnonimo(UsuarioDetalles usuarioAnonimo) {
		this.usuarioAnonimo = usuarioAnonimo;
	}

	public void before(Method arg0, Object[] arg1, Object arg2)
			throws Throwable {
		if (SecurityContextHolder.getContext().getAuthentication() == null)
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(
							this.usuarioAnonimo, this.usuarioAnonimo
									.getPassword(), this.usuarioAnonimo
									.getAuthorities()));
	}
}
