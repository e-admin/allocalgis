/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import org.apache.commons.lang3.StringUtils;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.ValidadorDtoBase;
import es.dc.a21l.usuario.cu.RolDto;

public class RolDtoValidador extends ValidadorDtoBase<RolDto> {
	
	@Override
	protected void aplicaValidacion(RolDto dto, EncapsuladorErroresSW erros) {
		
		if(dto.getNombre()==null || dto.getNombre().isEmpty()) {
			erros.addError(UsuarioDtoFormErrorsEmun.NOMBREVACIO);
		}
		
		if(StringUtils.length(dto.getDescripcion())>255){
			erros.addError(UsuarioDtoFormErrorsEmun.ERROR_TAMANHO_DESC);
		}
		
		if (dto.getEltosJerarquia() == null || dto.getEltosJerarquia().isEmpty()){
			erros.addError(RolDtoFormErrorsEmun.PERMISOVACIO);
		}
	}

}
