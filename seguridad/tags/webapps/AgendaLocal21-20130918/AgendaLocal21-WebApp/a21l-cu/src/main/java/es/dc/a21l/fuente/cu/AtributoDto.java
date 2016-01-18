/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.elementoJerarquia.cu.RelacionDto;

@SuppressWarnings("restriction")
@XmlRootElement(name="atributo")
public class AtributoDto extends DtoBase {
	private String nombre;
	private Integer ordenVisualizacion;
	private Boolean mostrar;
	private Boolean esAmbito;
	private Boolean esMapa;
	private AtributoFuenteDatosDto columna;
	private IndicadorExpresionDto indicadorExpresion;
	private RelacionDto relacion;
	private String path;
	private CriterioDto criterio;
	
	
	@XmlAttribute(name="nombre")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@XmlAttribute(name="ordenVisualizacion")
	public Integer getOrdenVisualizacion() {
		return ordenVisualizacion;
	}
	public void setOrdenVisualizacion(Integer ordenVisualizacion) {
		this.ordenVisualizacion = ordenVisualizacion;
	}
	@XmlAttribute(name="mostrar")
	public Boolean getMostrar() {
		return mostrar;
	}
	public void setMostrar(Boolean mostrar) {
		this.mostrar = mostrar;
	}
	@XmlAttribute(name="esAmbito")
	public Boolean getEsAmbito() {
		return esAmbito;
	}
	public void setEsAmbito(Boolean esAmbito) {
		this.esAmbito = esAmbito;
	}
	@XmlAttribute(name="esMapa")
	public Boolean getEsMapa() {
		return esMapa;
	}
	public void setEsMapa(Boolean esMapa) {
		this.esMapa = esMapa;
	}
	@XmlElement(name="columna")
	public AtributoFuenteDatosDto getColumna() {
		return columna;
	}
	public void setColumna(AtributoFuenteDatosDto columna) {
		this.columna = columna;
	}
	@XmlElement(name="path")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@XmlElement(name="indicadorExpresion")
	public IndicadorExpresionDto getIndicadorExpresion() {
		return indicadorExpresion;
	}
	public void setIndicadorExpresion(IndicadorExpresionDto indicadorExpresion) {
		this.indicadorExpresion = indicadorExpresion;
	}
	@XmlElement(name="relacion")
	public RelacionDto getRelacion() {
		return relacion;
	}
	public void setRelacion(RelacionDto relacion) {
		this.relacion = relacion;
	}
	
	@XmlElement(name="criterio")
	public CriterioDto getCriterio() {
		return criterio;
	}
	public void setCriterio(CriterioDto criterio) {
		this.criterio = criterio;
	}
}
