/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.TransformadorEntidadBase2DtoBase;
import es.dc.a21l.fuente.cu.FuenteDto;
import es.dc.a21l.fuente.modelo.Fuente;

public class Fuente2FuenteDtoTransformer extends TransformadorEntidadBase2DtoBase<Fuente, FuenteDto>{
	public Fuente2FuenteDtoTransformer(Mapper mapper) {
		super(mapper);
	}
	@Override
	protected void aplicaPropiedadesEstendidas(Fuente origen, FuenteDto destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		if(origen.getCaracterSeparador()!=null){
			destino.setIdCaracterSeparador(origen.getCaracterSeparador().getId());
			destino.setCharSeparador(origen.getCaracterSeparador().getCaracter());
		}
		if(origen.getTipoCodificacion()!=null){
			destino.setIdTipoCodificacion(origen.getTipoCodificacion().getId());
			destino.settCodificacion(origen.getTipoCodificacion().getNombre());
		}
	}
}

