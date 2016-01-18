/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.metadatos.cu.impl;

import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;

public enum MetadatosFormErrorsEmun implements FormErrorEmun {

	FICHERO_ERROR("resultadoOperacion","validacion.fuente.fichero.error"),
	ERROR_ELIMINAR_FICHERO("resultadoOperacion","validacion.fuente.eliminar.fichero.error"),
	ERROR_ALMACENAR_FICHERO("resultadoOperacion","validacion.fuentes.fichero_no_almacenado"),
	ERROR_FORMATO_FICHERO("resultadoOperacion","validacion.fuentes.fichero.no.valido"),
	;
	
	
	private String atributo;
	private String cadenaCodigoError;

	MetadatosFormErrorsEmun(String atributo,String cadenaCodigoError){
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

