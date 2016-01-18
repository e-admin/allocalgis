/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;


import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresionRepositorio;
import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.modelo.Atributo;
import es.dc.a21l.fuente.modelo.AtributoRepositorio;
	
public class AtributoDto2AtributoTransformer extends  TransformadorDtoBase2EntidadBase<AtributoDto, Atributo>  {
	private IndicadorExpresionRepositorio indicadorExpresionRepositorio;
	
	public AtributoDto2AtributoTransformer(Mapper mapper, AtributoRepositorio atributoRepositorio, IndicadorExpresionRepositorio indicadorExpresionRepositorio) {
        super(mapper);
        this.indicadorExpresionRepositorio = indicadorExpresionRepositorio;
        setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<AtributoDto, Atributo>(atributoRepositorio, this));
    }
	
	@Override
	protected void aplicaPropiedadesEstendidas(AtributoDto origen,Atributo destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		if ( origen.getIndicadorExpresion()!=null)
			destino.setIndicadorExpresion(indicadorExpresionRepositorio.carga(origen.getIndicadorExpresion().getId()));
	}

}