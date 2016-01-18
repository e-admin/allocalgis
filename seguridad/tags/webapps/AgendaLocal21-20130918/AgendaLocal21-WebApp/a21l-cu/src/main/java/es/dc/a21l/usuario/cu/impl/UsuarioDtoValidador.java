/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.ValidadorDtoBase;
import es.dc.a21l.usuario.cu.UsuarioDto;

public class UsuarioDtoValidador extends ValidadorDtoBase<UsuarioDto> {
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String USUARIO_INVITADO="Invitado";
	@Override
	protected void aplicaValidacion(UsuarioDto dto, EncapsuladorErroresSW erros) {
		if(dto.getNombre()==null || dto.getNombre().isEmpty())
			erros.addError(UsuarioDtoFormErrorsEmun.NOMBREVACIO);
		if(StringUtils.isBlank(dto.getPassword())){
				erros.addError(UsuarioDtoFormErrorsEmun.CONTRASENHAVACIA);
		}
		if(dto.getId()==0L && !dto.getPassword().equals(dto.getPasswordConfirm())){
			erros.addError(UsuarioDtoFormErrorsEmun.CONTRASENHASDISITNTAS);
		}
		if(dto.getLogin()==null || dto.getLogin().isEmpty()){
			erros.addError(UsuarioDtoFormErrorsEmun.IDENTIFICADOR_VACIO);
		}
		if(dto.getLogin()!=null && dto.getLogin().contains(" "))
			erros.addError(UsuarioDtoFormErrorsEmun.IDENTIFICADORCONESPACIOS);
		
		if(dto.getEmail()==null || dto.getEmail().isEmpty() || !Pattern.matches(EMAIL_PATTERN, dto.getEmail())){
			erros.addError(UsuarioDtoFormErrorsEmun.EMAIL_NO_VALIDO);
		}
		if(dto.getLogin()!=null && StringUtils.equalsIgnoreCase(dto.getLogin(), USUARIO_INVITADO)){
			erros.addError(UsuarioDtoFormErrorsEmun.NOMBRE_RESERVADO);
		}
	}

}
