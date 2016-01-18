/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;

import es.dc.a21l.base.modelo.GestionErrores.enumerados.FormErrorEmun;

public enum FuenteFormErrorsEmun implements FormErrorEmun {
	
	NOMBREVACIO("nombre","validacion.fuente.nombre.vacio"),
	INFOCONEXIONVACIO("infoConexion","validacion.fuente.info.conexion.vacio"),
	TIPOVACIO("tipo","validacion.fuente.tipo.vacio"),
	FICHERO_ERROR("fich_csv_gml","validacion.fuente.fichero.error"),
	ERROR_ELIMINAR_FICHERO("fich_csv_gml","validacion.fuente.eliminar.fichero.error"),
	ERROR_ALMACENAR_FICHERO("fich_csv_gml","validacion.fuentes.fichero_no_almacenado"),
	ERROR_SHAPEFILE("fich_shp","validacion.fuentes.shapefile.ficheros.obligatorios"),
	NOMBRE_EN_USO("nombre","validacion.fuentes.nombre.en.uso"),
	ERROR_ELIMINAR_FUENTE_INDICADORES_ASOCIADOS("nombre","validacion.fuente.eliminar.fuente.indicadores.asociados"),
	INFO_CONEXION_NO_URL("infoConexion","validacion.fuente.info.conexion.no.url"),
	ERROR_CSV_GML("fich_csv_gml","validacion.fuentes.fichero_obligatorio")
	;
	
	
	private String atributo;
	private String cadenaCodigoError;

	FuenteFormErrorsEmun(String atributo,String cadenaCodigoError){
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

