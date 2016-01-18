/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.base.utils.enumerados.TipoAtributoFD;

@SuppressWarnings("restriction")
@XmlRootElement(name="atributoFuente")
public class AtributoFuenteDatosDto extends DtoBase {
	private String nombre;
	private TipoAtributoFD tipoAtributo;
	private String definicion;
	private TablaFuenteDatosDto tabla;
	private boolean esFormula;
	private boolean esRelacion;
	
	private String strTipoDatoRelacion;
	private String strColumnaRelacion;
	private String strTablaRelacion;
	private String strFuenteRelacion;
	
	@XmlAttribute(name="nombre")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@XmlAttribute(name="tipoAtributo")
	public TipoAtributoFD getTipoAtributo() {
		return tipoAtributo;
	}
	public void setTipoAtributo(TipoAtributoFD tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}
	@XmlAttribute(name="definicion")
	public String getDefinicion() {
		return definicion;
	}
	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}
	@XmlElement(name="tabla")
	public TablaFuenteDatosDto getTabla() {
		return tabla;
	}
	public void setTabla(TablaFuenteDatosDto tabla) {
		this.tabla = tabla;
	}
	
	@XmlAttribute(name="esFormula")
	public Boolean getEsFormula() {
		return esFormula;
	}

	public void setEsFormula(Boolean esFormula) {
		this.esFormula = esFormula;
	}
	
	@XmlAttribute(name="esRelacion")
	public Boolean getEsRelacion() {
		return esRelacion;
	}

	public void setEsRelacion(Boolean esRelacion) {
		this.esRelacion = esRelacion;
	}
	
	@XmlAttribute(name="strTipoDatoRelacion")
	public String getStrTipoDatoRelacion() {
		return strTipoDatoRelacion;
	}
	
	public void setStrTipoDatoRelacion(String strTipoDatoRelacion) {
		this.strTipoDatoRelacion = strTipoDatoRelacion;
	}
	
	@XmlAttribute(name="strColumnaRelacion")
	public String getStrColumnaRelacion() {
		return strColumnaRelacion;
	}
	
	public void setStrColumnaRelacion(String strColumnaRelacion) {
		this.strColumnaRelacion = strColumnaRelacion;
	}
	
	@XmlAttribute(name="strTablaRelacion")
	public String getStrTablaRelacion() {
		return strTablaRelacion;
	}
	
	public void setStrTablaRelacion(String strTablaRelacion) {
		this.strTablaRelacion = strTablaRelacion;
	}
	
	@XmlAttribute(name="strFuenteRelacion")
	public String getStrFuenteRelacion() {
		return strFuenteRelacion;
	}
	
	public void setStrFuenteRelacion(String strFuenteRelacion) {
		this.strFuenteRelacion = strFuenteRelacion;
	}	
}