/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.expresion.cu.ExpresionDto;
import es.dc.a21l.expresion.cu.TipoOperandoEmun;
import es.dc.a21l.expresion.modelo.Expresion;
import es.dc.a21l.expresion.modelo.ExpresionRepositorio;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatosRepositorio;

public class ExpresionDto2ExpresionTransformer extends TransformadorDtoBase2EntidadBase<ExpresionDto, Expresion> {

	private ExpresionRepositorio expresionRepositorio;
	private AtributoFuenteDatosRepositorio atributoFuenteDatosRepositorio;
	
	public ExpresionDto2ExpresionTransformer(Mapper mapper,ExpresionRepositorio expresionRepositorio,AtributoFuenteDatosRepositorio atributoFuenteDatosRepositorio) {
		super(mapper);
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<ExpresionDto, Expresion>(expresionRepositorio, this));
		this.expresionRepositorio=expresionRepositorio;
		this.atributoFuenteDatosRepositorio= atributoFuenteDatosRepositorio;
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(ExpresionDto origen,Expresion destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		
		if(origen.getTipoOperandoIzq().equals(TipoOperandoEmun.EXPRESION)){
			destino.setExpresionIzq(expresionRepositorio.carga(origen.getIdExpresionIzq()));
			destino.setAtributoFuenteDatosIzq(null);
			destino.setLiteralIzq(null);
		}
		
		if(origen.getTipoOperandoIzq().equals(TipoOperandoEmun.FUENTE_DATOS)){
			destino.setAtributoFuenteDatosIzq(atributoFuenteDatosRepositorio.carga(origen.getAtributoFuenteDatosDtoIzq().getId()));
			destino.setLiteralIzq(null);
			destino.setExpresionIzq(null);
		}
		
		if(origen.getTipoOperandoIzq().equals(TipoOperandoEmun.LITERAL)){
			destino.setAtributoFuenteDatosIzq(null);
			destino.setExpresionIzq(null);
		}
		
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoEmun.EXPRESION)){
			destino.setExpresionDch(expresionRepositorio.carga(origen.getIdExpresionDch()));
			destino.setAtributoFuenteDatosDch(null);
			destino.setLiteralDch(null);
		}
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoEmun.FUENTE_DATOS)){
			destino.setAtributoFuenteDatosDch(atributoFuenteDatosRepositorio.carga(origen.getAtributoFuenteDatosDtoDch().getId()));
			destino.setExpresionDch(null);
			destino.setLiteralDch(null);
		}
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoEmun.LITERAL)){
			destino.setAtributoFuenteDatosDch(null);
			destino.setExpresionDch(null);
		}
		
		if(origen.getTipoOperandoDch().equals(TipoOperandoEmun.SIN_OPERANDO)){
			destino.setAtributoFuenteDatosDch(null);
			destino.setExpresionDch(null);
			destino.setLiteralDch(null);
		}
		
	}
	

}
