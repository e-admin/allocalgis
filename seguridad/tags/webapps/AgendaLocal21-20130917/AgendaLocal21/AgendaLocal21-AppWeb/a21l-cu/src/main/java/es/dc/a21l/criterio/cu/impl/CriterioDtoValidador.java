/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.cu.impl;

import org.apache.commons.lang3.StringUtils;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.ValidadorDtoBase;
import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.criterio.cu.TipoOperacionCriterioEmun;
import es.dc.a21l.criterio.cu.TipoOperandoCriterioEmun;
import es.dc.a21l.expresion.cu.TipoOperandoEmun;

public class CriterioDtoValidador extends ValidadorDtoBase<CriterioDto> {

	
	@Override
	protected void aplicaValidacion(CriterioDto dto, EncapsuladorErroresSW erros) {
		super.aplicaValidacion(dto, erros);
		
		//REGLAS GENERALES
		
		if(dto.getTipoOperandoIzq()==null || dto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.SIN_OPERANDO) || dto.getTipoOperandoDch()==null || dto.getTipoOperacion()==null){
			erros.setHashErrors(true);
			return;
		}
		
		if(!dto.getTipoOperacion().equals(TipoOperacionCriterioEmun.NOT) && dto.getTipoOperandoDch().equals(TipoOperandoEmun.SIN_OPERANDO)){
			erros.setHashErrors(true);
			return;
		}
		
		if(StringUtils.isBlank(dto.getCadenaCriterio()) && dto.getIdAtributo()!=null && dto.getIdAtributo()!=0L){
			erros.setHashErrors(true);
			return;
		}
		
		if(!StringUtils.isBlank(dto.getCadenaCriterio()) && (dto.getIdAtributo()==null || dto.getIdAtributo()==0L)){
			erros.setHashErrors(true);
			return;
		}
		
		
		//OPERADOR IZQUIERDO
		
		if(dto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.CRITERIO) && (dto.getIdCriterioIzq()==null || dto.getIdCriterioIzq()==0L)){
				erros.setHashErrors(true);
				return;
		}
		
		if((dto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.LITERAL) && dto.getLiteralIzq()==null) 
				&& (dto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.LITERAL) && dto.getStrLiteralIzq() == null)){
			erros.setHashErrors(true);
			return;
		}
		
		if(dto.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.PARAMETRO_ESPERADO) && ((dto.getIdCriterioIzq()!=null && dto.getIdCriterioIzq()!=0L) || (dto.getLiteralIzq()!=null))){
			erros.setHashErrors(true);
			return;
		}
		
		
		//OPERADOR DERECHO
		
		if(dto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.CRITERIO) && (dto.getIdCriterioDch()==null || dto.getIdCriterioDch()==0L)){
			erros.setHashErrors(true);
			return;
		}
	
		if((dto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.LITERAL) && dto.getLiteralDch()==null) 
				&& (dto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.LITERAL) && dto.getStrLiteralDch() == null)){
			erros.setHashErrors(true);
			return;
		}
		
		if(dto.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.PARAMETRO_ESPERADO) && ((dto.getIdCriterioDch()!=null && dto.getIdCriterioDch()!=0L) || (dto.getLiteralDch()!=null))){
			erros.setHashErrors(true);
			return;
		}
		
	}
}
