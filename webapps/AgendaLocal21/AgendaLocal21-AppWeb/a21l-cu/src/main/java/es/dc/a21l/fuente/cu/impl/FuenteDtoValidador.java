/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.fuente.cu.FuenteDto;

public class FuenteDtoValidador implements Validador<FuenteDto> {

	public final static String URL_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	public void valida(FuenteDto dto, EncapsuladorErroresSW errores) {
		//Nombre no puede ser vacio
		if(dto.getNombre()==null || dto.getNombre().isEmpty())
			errores.addError(FuenteFormErrorsEmun.NOMBREVACIO);
		//El tipo no puede ser vacio
		if(dto.getTipo()==null){
			errores.addError(FuenteFormErrorsEmun.TIPOVACIO);
		}else{
			//El campo infoconexion no puede estar vacio
			if((dto.getTipo()==TiposFuente.BDESPACIAL || dto.getTipo()==TiposFuente.ODBC ||dto.getTipo()==TiposFuente.MYSQL || dto.getTipo()==TiposFuente.ORACLE || dto.getTipo()==TiposFuente.WFS  )&& StringUtils.isBlank(dto.getInfoConexion()))
				errores.addError(FuenteFormErrorsEmun.INFOCONEXIONVACIO);
			//Si es Shapefile debe tener fichero dbf y shp minimo
			if(dto.getTipo()==TiposFuente.SHAPEFILE && (StringUtils.isBlank(dto.getFich_shp()) || StringUtils.isBlank(dto.getFich_dbf()) )) {
				errores.addError(FuenteFormErrorsEmun.ERROR_SHAPEFILE);
			}
			if((dto.getTipo().equals(TiposFuente.GML) || dto.getTipo().equals(TiposFuente.CSV)) && StringUtils.isBlank(dto.getFich_csv_gml())){
				errores.addError(FuenteFormErrorsEmun.ERROR_CSV_GML);
			}
				
			//Si es WFS el campo infoConexion debe contener una url valida.
			if ( dto.getTipo()==TiposFuente.WFS && !StringUtils.isBlank(dto.getInfoConexion())) {
				if ( !Pattern.matches(URL_PATTERN, dto.getInfoConexion())) {
					errores.addError(FuenteFormErrorsEmun.INFO_CONEXION_NO_URL);
				}
			}
		}

	}
}
