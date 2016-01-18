/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.ValidadorDtoBase;
import es.dc.a21l.usuario.cu.UsuarioRolDto;

public class UsuarioRolDtoValidador extends ValidadorDtoBase<UsuarioRolDto> {
	
	@Override
	protected void aplicaValidacion(UsuarioRolDto dto, EncapsuladorErroresSW erros) {
		if(dto.getRolDto()==null || dto.getRolDto().getId()==0L){
			erros.addError(UsuarioRolDtoFormErrorsEmun.SIN_ROL);
		}
		if(dto.getIdUsuario()==null || dto.getIdUsuario()==0L){
			erros.addError(UsuarioRolDtoFormErrorsEmun.SIN_USUARIO);
		}
	}

}
