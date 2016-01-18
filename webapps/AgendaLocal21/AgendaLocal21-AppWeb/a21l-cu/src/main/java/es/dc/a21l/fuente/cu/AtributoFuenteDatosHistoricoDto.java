/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;

@SuppressWarnings("serial")
public class AtributoFuenteDatosHistoricoDto extends DtoBaseHistorico {
	
	private String nombre;
	private TipoAtributoFD tipoAtributo;
	private String definicion;
	private boolean esFormula;
	private boolean esRelacion;
	
	private String strTipoDatoRelacion;
	private String strColumnaRelacion;
	private String strTablaRelacion;
	private String strFuenteRelacion;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public TipoAtributoFD getTipoAtributo() {
		return tipoAtributo;
	}
	
	public void setTipoAtributo(TipoAtributoFD tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}
	
	
	public String getDefinicion() {
		return definicion;
	}
	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}
	
	public Boolean getEsFormula() {
		return esFormula;
	}

	public void setEsFormula(Boolean esFormula) {
		this.esFormula = esFormula;
	}
	
	public Boolean getEsRelacion() {
		return esRelacion;
	}

	public void setEsRelacion(Boolean esRelacion) {
		this.esRelacion = esRelacion;
	}
	
	public String getStrTipoDatoRelacion() {
		return strTipoDatoRelacion;
	}
	
	public void setStrTipoDatoRelacion(String strTipoDatoRelacion) {
		this.strTipoDatoRelacion = strTipoDatoRelacion;
	}
	
	public String getStrColumnaRelacion() {
		return strColumnaRelacion;
	}
	
	public void setStrColumnaRelacion(String strColumnaRelacion) {
		this.strColumnaRelacion = strColumnaRelacion;
	}
	
	public String getStrTablaRelacion() {
		return strTablaRelacion;
	}
	
	public void setStrTablaRelacion(String strTablaRelacion) {
		this.strTablaRelacion = strTablaRelacion;
	}
	
	public String getStrFuenteRelacion() {
		return strFuenteRelacion;
	}
	
	public void setStrFuenteRelacion(String strFuenteRelacion) {
		this.strFuenteRelacion = strFuenteRelacion;
	}	
}