/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import org.apache.commons.lang3.StringUtils;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.ValidadorDtoBase;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;

public class IndicadorDtoValidador extends ValidadorDtoBase<IndicadorDto> {
	
	@Override
	protected void aplicaValidacion(IndicadorDto dto,EncapsuladorErroresSW erros) {
		super.aplicaValidacion(dto, erros);
		if(StringUtils.isBlank(dto.getNombre())){
			erros.addError(IndicadorDtoFormErrorsEmun.NOMBRE_VACIO);
		}
		if(StringUtils.isBlank(dto.getLoginCreador())){
			erros.addError(IndicadorDtoFormErrorsEmun.SIN_CREADOR);
		}
		if(StringUtils.length(dto.getDescripcion())>255){
			erros.addError(IndicadorDtoFormErrorsEmun.ERROR_TAMANHO_DESC);
		}
		if(StringUtils.length(dto.getNombre())>255){
			erros.addError(IndicadorDtoFormErrorsEmun.ERROR_TAMANHO_NOMBRE);
		}
	}

}
