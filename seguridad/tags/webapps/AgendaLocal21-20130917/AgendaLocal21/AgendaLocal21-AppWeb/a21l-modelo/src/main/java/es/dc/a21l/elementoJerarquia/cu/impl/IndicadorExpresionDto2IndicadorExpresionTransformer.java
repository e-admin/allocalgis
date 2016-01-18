/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresion;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresionRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.expresion.modelo.ExpresionRepositorio;

public class IndicadorExpresionDto2IndicadorExpresionTransformer extends TransformadorDtoBase2EntidadBase<IndicadorExpresionDto, IndicadorExpresion> {

	private ExpresionRepositorio expresionRepositorio;
	private IndicadorRepositorio indicadorRepositorio;
	
	public IndicadorExpresionDto2IndicadorExpresionTransformer(Mapper mapper,IndicadorExpresionRepositorio indicadorExpresionRepositorio,ExpresionRepositorio expresionRepositorio,IndicadorRepositorio indicadorRepositorio) {
		super(mapper);
		this.expresionRepositorio=expresionRepositorio;
		this.indicadorRepositorio=indicadorRepositorio;
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<IndicadorExpresionDto, IndicadorExpresion>(indicadorExpresionRepositorio, this));
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(IndicadorExpresionDto origen,IndicadorExpresion destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setExpresion(expresionRepositorio.carga(origen.getIdExpresion()));
		destino.setIndicador(indicadorRepositorio.carga(origen.getIdIndicador()));
	}

}
