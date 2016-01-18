/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.servicios.impl;

import java.util.HashMap;
import java.util.Map;

import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.servicio.servicios.ServicioConfiguracionGeneral;

public class ServicioConfiguracionGeneralImpl implements ServicioConfiguracionGeneral {

	private String rutaFicherosFuentes;
	private String pathCsv;
	private String pathGml;
	private String pathShapefile;
	private String pathMetadatos;
	private String caracterSeparadorCSV;
	private String catalogoUrl;
	private String catalogoNome;
	private String catalogoUser;
	private String catalogoPass;
	private String catalogoData;
	private String codigoCordenadas;
	
	private String caracterSeparadorDecimales;
	
	public String getCaracterSeparadorDecimales() {
		return caracterSeparadorDecimales;
	}
	public void setCaracterSeparadorDecimales(String caracterSeparadorDecimales) {
		this.caracterSeparadorDecimales = caracterSeparadorDecimales;
	}	
	public String getRutaFicherosFuentes() {
		return rutaFicherosFuentes;
	}
	public void setRutaFicherosFuentes(String rutaFicherosFuentes) {
		this.rutaFicherosFuentes = rutaFicherosFuentes;
	}
	public String getPathCsv() {
		return rutaFicherosFuentes + pathCsv;
	}
	public void setPathCsv(String pathCsv) {
		this.pathCsv = pathCsv;
	}
	public String getPathGml() {
		return rutaFicherosFuentes + pathGml;
	}
	public void setPathGml(String pathGml) {
		this.pathGml = pathGml;
	}
	public String getPathShapefile() {
		return rutaFicherosFuentes + pathShapefile;
	}
	public void setPathShapefile(String pathShapefile) {
		this.pathShapefile = pathShapefile;
	}
	public String getCaracterSeparadorCSV() {
		return caracterSeparadorCSV;
	}
	public void setCaracterSeparadorCSV(String caracterSeparadorCSV) {
		this.caracterSeparadorCSV = caracterSeparadorCSV;
	}
	public String getPathMetadatos() {
		return rutaFicherosFuentes + pathMetadatos;
	}
	public void setPathMetadatos(String pathMetadatos) {
		this.pathMetadatos = pathMetadatos;
	}
	
	public Map<TiposFuente, String> getMapaPathTiposFuente(){
		Map<TiposFuente, String> result= new HashMap<TiposFuente, String>();
		result.put(TiposFuente.CSV, getPathCsv());
		result.put(TiposFuente.GML, getPathGml());
		result.put(TiposFuente.SHAPEFILE, getPathShapefile());
		return result;
		
 	}
		
	public String getCatalogoUrl() {
		return catalogoUrl;
	}
	public void setCatalogoUrl(String catalogoUrl) {
		this.catalogoUrl = catalogoUrl;
	}
	
	public String getCatalogoNome() {
		return catalogoNome;
	}
	public void setCatalogoNome(String catalogoNome) {
		this.catalogoNome = catalogoNome;
	}
	
	public String getCatalogoUser() {
		return catalogoUser;
	}
	public void setCatalogoUser(String catalogoUser) {
		this.catalogoUser = catalogoUser;
	}
	
	public String getCatalogoPass() {
		return catalogoPass;
	}
	public void setCatalogoPass(String catalogoPass) {
		this.catalogoPass = catalogoPass;
	}
	
	public String getCatalogoData() {
		return catalogoData;
	}
	public void setCatalogoData(String catalogoData) {
		this.catalogoData = catalogoData;
	}
	public String getCodigoCordenadas() {
		return codigoCordenadas;
	}
	public void setCodigoCordenadas(String codigoCordenadas) {
		this.codigoCordenadas = codigoCordenadas;
	}
	
}