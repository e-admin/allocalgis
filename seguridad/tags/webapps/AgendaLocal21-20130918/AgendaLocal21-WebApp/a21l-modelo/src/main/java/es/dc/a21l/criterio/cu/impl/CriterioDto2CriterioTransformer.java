/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.criterio.cu.TipoOperandoCriterioEmun;
import es.dc.a21l.criterio.modelo.Criterio;
import es.dc.a21l.criterio.modelo.CriterioRepositorio;
import es.dc.a21l.fuente.modelo.AtributoRepositorio;

public class CriterioDto2CriterioTransformer extends TransformadorDtoBase2EntidadBase<CriterioDto, Criterio> {

	private AtributoRepositorio atributoRepositorio;
	private CriterioRepositorio criterioRepositorio;
	
	public CriterioDto2CriterioTransformer(Mapper mapper, CriterioRepositorio criterioRepositorio,AtributoRepositorio atributoRepositorio) {
		super(mapper);
		this.atributoRepositorio=atributoRepositorio;
		this.criterioRepositorio=criterioRepositorio;
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<CriterioDto, Criterio>(criterioRepositorio, this));
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(CriterioDto origen,Criterio destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		
		if(origen.getIdAtributo()!=null && origen.getIdAtributo()!=0L)
			destino.setAtributo(atributoRepositorio.carga(origen.getIdAtributo()));
		
		
		if(origen.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.CRITERIO)){
			destino.setCriterioIzq(criterioRepositorio.carga(origen.getIdCriterioIzq()));
			destino.setLiteralIzq(null);
		}
		
		if(origen.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.LITERAL)){
			destino.setCriterioIzq(null);
		}
		
		if(origen.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.PARAMETRO_ESPERADO)){
			destino.setCriterioIzq(null);
			destino.setLiteralIzq(null);
		}
		
		
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.CRITERIO)){
			destino.setCriterioDch(criterioRepositorio.carga(origen.getIdCriterioDch()));
			destino.setLiteralDch(null);
		}
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.LITERAL)){
			destino.setCriterioDch(null);
		}
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.PARAMETRO_ESPERADO) || origen.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.SIN_OPERANDO) ){
			destino.setCriterioDch(null);
			destino.setLiteralDch(null);
		}
				
	}

}
