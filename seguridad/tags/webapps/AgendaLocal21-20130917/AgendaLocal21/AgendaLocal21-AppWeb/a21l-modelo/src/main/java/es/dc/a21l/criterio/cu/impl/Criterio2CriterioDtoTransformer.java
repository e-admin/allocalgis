/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.TransformadorEntidadBase2DtoBase;
import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.criterio.cu.TipoOperandoCriterioEmun;
import es.dc.a21l.criterio.modelo.Criterio;

public class Criterio2CriterioDtoTransformer extends TransformadorEntidadBase2DtoBase<Criterio, CriterioDto> {

	public Criterio2CriterioDtoTransformer(Mapper mapper) {
		super(mapper);
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(Criterio origen,CriterioDto destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		if(origen.getTipoOperandoIzq().equals(TipoOperandoCriterioEmun.CRITERIO))
			destino.setIdCriterioIzq(origen.getCriterioIzq().getId());
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoCriterioEmun.CRITERIO))
			destino.setIdCriterioDch(origen.getCriterioDch().getId());
		
		if(origen.getAtributo()!=null)
		destino.setIdAtributo(origen.getAtributo().getId());
	}

}
