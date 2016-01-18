/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;

public enum IndicadorExpresionDtoFormErrorsEmun implements FormErrorEmun {

	EXPRESION_LITERAL_VACIA("expresionLiteral","validacion.indicadorExpresion.expresion.literal.vacia"),
	INDICADOR_VACIO("idIndicador","validacion.indicadorExpresion.indicador.vacio"),
	EXPRESION_VACIA("idExpresion","validacion.indicadorExpresion.expresion.vacia"),
	EXPRESION_TRANSFORMADA_VACIA("expresionTransformada","validacion.indicadorExpresion.expresion.transformada.vacia")
	;
	private String atributo;
	private String cadenaCodigoError;
	
	IndicadorExpresionDtoFormErrorsEmun(String atributo,String cadenaCodigoError){
		this.atributo=atributo;
		this.cadenaCodigoError=cadenaCodigoError;
	}
	
	public String getAtributo() {
		return atributo;
	}

	public String getCadenaCodigoError() {
		return cadenaCodigoError;
	}

}
