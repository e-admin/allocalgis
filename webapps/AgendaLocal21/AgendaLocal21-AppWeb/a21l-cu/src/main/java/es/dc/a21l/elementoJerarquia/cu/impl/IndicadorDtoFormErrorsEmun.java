/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;

public enum IndicadorDtoFormErrorsEmun implements FormErrorEmun{
	
	NOMBRE_VACIO("nombre","validacion.indicador.nombre.vacio"),
	NOMBRE_EN_USO("nombre","validacion.indicador.nombre.en.uso"),
	NOMBRE_EN_USO_OTRO_USUARIO("nombre","validacion.indicador.nombre.en.uso.otro.usuario"),
	SIN_CAMPOS("nombre","validacion.indicador.sin.campos"),
	SIN_CAMPOS_MOSTRAR("nombre","validacion.indicador.sin.campos.mostrar"),
	SIN_AMBITO("nombre","validacion.indicador.sin.ambito"),
	SIN_CREADOR("loginCreador","validacion.indicador.sin.creador"),
	ERROR_FORMULA("nombre","validacion.indicador.error.formula"),
	ERROR_CRITERIO("nombre","validacion.indicador.error.criterio"),
	ERROR_CRITERIO_NUMERICO("nombre","validacion.indicador.error.criterio.numerico"),
	ERROR_TAMANHO_DESC("descripcion","validacion.indicador.error.descripcion"),
	ERROR_TAMANHO_NOMBRE("nombre","validacion.indicador.error.nombre");
	private String atributo;
	private String cadenaCodigoError;
	
	IndicadorDtoFormErrorsEmun(String atributo,String cadenaCodigoError){
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
