/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;

@SuppressWarnings("restriction")
@XmlRootElement(name="relacionDto")
public class RelacionDto extends DtoBase {

	private String nombreTablaRelacion;
	private String nombreColumnaRelacion;
	private String nombreTablaRelacionada;
	private String nombreColumnaRelacionada;
	private String idFuenteRelacion;
	private String idFuenteRelacionada;
	private IndicadorDto indicador;
	
	
	@XmlAttribute(name="nombreTablaRelacion")
	public String getNombreTablaRelacion() {
		return nombreTablaRelacion;
	}
	public void setNombreTablaRelacion(String nombreTablaRelacion) {
		this.nombreTablaRelacion = nombreTablaRelacion;
	}
	
	@XmlAttribute(name="nombreColumnaRelacion")
	public String getNombreColumnaRelacion() {
		return nombreColumnaRelacion;
	}
	public void setNombreColumnaRelacion(String nombreColumnaRelacion) {
		this.nombreColumnaRelacion = nombreColumnaRelacion;
	}
	
	@XmlAttribute(name="nombreTablaRelacionada")
	public String getNombreTablaRelacionada() {
		return nombreTablaRelacionada;
	}
	public void setNombreTablaRelacionada(String nombreTablaRelacionada) {
		this.nombreTablaRelacionada = nombreTablaRelacionada;
	}
	
	@XmlAttribute(name="nombreColumnaRelacionada")
	public String getNombreColumnaRelacionada() {
		return nombreColumnaRelacionada;
	}
	public void setNombreColumnaRelacionada(String nombreColumnaRelacionada) {
		this.nombreColumnaRelacionada = nombreColumnaRelacionada;
	}
	
	@XmlAttribute(name="idFuenteRelacion")
	public String getIdFuenteRelacion() {
		return idFuenteRelacion;
	}
	public void setIdFuenteRelacion(String idFuenteRelacion) {
		this.idFuenteRelacion = idFuenteRelacion;
	}
	
	@XmlAttribute(name="idFuenteRelacionada")
	public String getIdFuenteRelacionada() {
		return idFuenteRelacionada;
	}
	public void setIdFuenteRelacionada(String idFuenteRelacionada) {
		this.idFuenteRelacionada = idFuenteRelacionada;
	}
	
	@XmlElement(name="indicador")
	public IndicadorDto getIndicador() {
		return indicador;
	}
	public void setIndicador(IndicadorDto indicador) {
		this.indicador = indicador;
	}
}
