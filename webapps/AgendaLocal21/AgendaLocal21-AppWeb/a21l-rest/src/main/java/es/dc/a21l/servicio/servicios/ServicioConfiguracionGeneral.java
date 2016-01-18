/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.servicio.servicios;

import java.util.Map;

import es.dc.a21l.base.utils.enumerados.TiposFuente;

public interface ServicioConfiguracionGeneral {
	public String getRutaFicherosFuentes();
	public void setRutaFicherosFuentes(String rutaFicherosFuentes);
	public String getPathCsv();
	public void setPathCsv(String pathCsv);
	public String getPathGml();
	public void setPathGml(String pathGml);
	public String getPathShapefile();
	public void setPathShapefile(String pathShapefile);
	public String getCaracterSeparadorCSV();
	public void setCaracterSeparadorCSV(String caracterSeparadorCSV);
	public String getPathMetadatos();
	public void setPathMetadatos(String pathMetadatos);	
	public Map<TiposFuente, String> getMapaPathTiposFuente();	
	public String getCatalogoUrl();
	public void setCatalogoUrl(String catalogoUrl);	
	public String getCatalogoNome();
	public void setCatalogoNome(String catalogoNome);	
	public String getCatalogoUser();
	public void setCatalogoUser(String catalogoUser);	
	public String getCatalogoPass();
	public void setCatalogoPass(String catalogoPass);	
	public String getCatalogoData();
	public void setCatalogoData(String catalogoData);
	public String getCodigoCordenadas();
	public void setCodigoCordenadas(String codigoCordenadas);
	public String getCaracterSeparadorDecimales();
	public void setCaracterSeparadorDecimales(String caracterSeparadorDecimales);
}