/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.comun.utils.impl;

import es.dc.a21l.comun.utils.UrlConstructorSW;

public class UrlConstructorSWImpl implements UrlConstructorSW {

	private static final String pathSerparador="/";
	private String url;
	private String urlbase;
//	private ServicioConfiguracionGeneral servicioConfiguracionGeneral;
//	
//	
//	@Autowired
//	public void setServicioConfiguracionGeneral(
//			ServicioConfiguracionGeneral servicioConfiguracionGeneral) {
//		this.servicioConfiguracionGeneral = servicioConfiguracionGeneral;
//	}

	public UrlConstructorSWImpl(String urlBaseSW){
		this.url=urlBaseSW;
		this.urlbase=urlBaseSW;
	}

	public String getUrl() {
		return url;
	}

	public UrlConstructorSW setParametroCadena(Object parametro) {
		url=url+pathSerparador+parametro.toString();
		return this;
	}

	public UrlConstructorSW borrarParametrosCadena() {
		url=urlbase;
		return this;
	}
	
	public UrlConstructorSW borraUltimoParametroCadena(){
		url=url.substring(0, url.lastIndexOf(pathSerparador));
		return this;
	}
	
	@Override
	public String toString() {
		return getUrl();
	}
}
