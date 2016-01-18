/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.TransformadorEntidadBase2DtoBase;
import es.dc.a21l.expresion.cu.ExpresionDto;
import es.dc.a21l.expresion.cu.TipoOperandoEmun;
import es.dc.a21l.expresion.modelo.Expresion;
import es.dc.a21l.fuente.cu.impl.AtributoFuenteDatos2AtributoFuenteDatosDtoTransformer;

public class Expresion2ExpresionDtoTransformer extends TransformadorEntidadBase2DtoBase<Expresion, ExpresionDto> {
	

	public Expresion2ExpresionDtoTransformer(Mapper mapper) {
		super(mapper);
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(Expresion origen,ExpresionDto destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		if(origen.getTipoOperandoIzq().equals(TipoOperandoEmun.EXPRESION)){
			destino.setIdExpresionIzq(origen.getExpresionIzq().getId());
		}
		if(origen.getTipoOperandoIzq().equals(TipoOperandoEmun.FUENTE_DATOS)){
			destino.setAtributoFuenteDatosDtoIzq(new AtributoFuenteDatos2AtributoFuenteDatosDtoTransformer(getMapper()).transform(origen.getAtributoFuenteDatosIzq()));
		}
		
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoEmun.EXPRESION)){
			destino.setIdExpresionDch(origen.getExpresionDch().getId());
		}
		if(origen.getTipoOperandoDch().equals(TipoOperandoEmun.FUENTE_DATOS)){
			destino.setAtributoFuenteDatosDtoDch(new AtributoFuenteDatos2AtributoFuenteDatosDtoTransformer(getMapper()).transform(origen.getAtributoFuenteDatosDch()));
		}
		
	}
	

}
