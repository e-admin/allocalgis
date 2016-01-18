/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.cu.impl;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.ValidadorDtoBase;
import es.dc.a21l.expresion.cu.ExpresionDto;
import es.dc.a21l.expresion.cu.TipoOperacionEmun;
import es.dc.a21l.expresion.cu.TipoOperandoEmun;


//validador interno sin mensajes de spring
public class ExpresionDtoValidador extends ValidadorDtoBase<ExpresionDto> {
	
	@Override
	protected void aplicaValidacion(ExpresionDto dto,EncapsuladorErroresSW erros) {
		super.aplicaValidacion(dto, erros);
		
		//REGLAS GENERALES
		
		if(dto.getTipoOperandoIzq()==null || dto.getTipoOperandoIzq().equals(TipoOperandoEmun.SIN_OPERANDO) || dto.getTipoOperandoDch()==null || dto.getTipoOperacion()==null){
			erros.setHashErrors(true);
			return;
		}
		
		if(!dto.getTipoOperacion().equals(TipoOperacionEmun.ABS) && dto.getTipoOperandoDch().equals(TipoOperandoEmun.SIN_OPERANDO)){
			erros.setHashErrors(true);
			return;
		}
		
		
		//OPERADOR IZQUIERDO
		
		if(dto.getTipoOperandoIzq().equals(TipoOperandoEmun.EXPRESION) && (dto.getIdExpresionIzq()==null || dto.getIdExpresionIzq()==0L)){
				erros.setHashErrors(true);
				return;
		}
		
		
		if(dto.getTipoOperandoIzq().equals(TipoOperandoEmun.FUENTE_DATOS) && (dto.getAtributoFuenteDatosDtoIzq()==null || dto.getAtributoFuenteDatosDtoIzq().getId()==0L)){
			erros.setHashErrors(true);
			return;
		}
		
		if(dto.getTipoOperandoIzq().equals(TipoOperandoEmun.LITERAL) && dto.getLiteralIzq()==null){
			erros.setHashErrors(true);
			return;
		}
		
		
		//OPERADOR DERECHO
		
		if(dto.getTipoOperandoDch().equals(TipoOperandoEmun.EXPRESION) && (dto.getIdExpresionDch()==null || dto.getIdExpresionDch()==0L)){
			erros.setHashErrors(true);
			return;
		}
	
	
		if(dto.getTipoOperandoDch().equals(TipoOperandoEmun.FUENTE_DATOS) && (dto.getAtributoFuenteDatosDtoDch()==null || dto.getAtributoFuenteDatosDtoDch().getId()==0L)){
			erros.setHashErrors(true);
			return;
		}
	
		if(dto.getTipoOperandoDch().equals(TipoOperandoEmun.LITERAL) && dto.getLiteralDch()==null){
			erros.setHashErrors(true);
			return;
		}
		
	}
	

}
