/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import es.dc.a21l.criterio.cu.CriterioDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.elementoJerarquia.cu.RelacionDto;

@SuppressWarnings("serial")
public class AtributoHistoricoDto extends DtoBaseHistorico {
	
	private String nombre;
	private Integer ordenVisualizacion;
	private Boolean mostrar;
	private Boolean esAmbito;
	private Boolean esMapa;
	private AtributoFuenteDatosHistoricoDto columna;
	private IndicadorExpresionHistoricoDto indicadorExpresion;
	private String path;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Integer getOrdenVisualizacion() {
		return ordenVisualizacion;
	}
	
	public void setOrdenVisualizacion(Integer ordenVisualizacion) {
		this.ordenVisualizacion = ordenVisualizacion;
	}
	
	public Boolean getMostrar() {
		return mostrar;
	}
	
	public void setMostrar(Boolean mostrar) {
		this.mostrar = mostrar;
	}
	
	public Boolean getEsAmbito() {
		return esAmbito;
	}
	
	public void setEsAmbito(Boolean esAmbito) {
		this.esAmbito = esAmbito;
	}
	
	public Boolean getEsMapa() {
		return esMapa;
	}
	
	public void setEsMapa(Boolean esMapa) {
		this.esMapa = esMapa;
	}
	
	public AtributoFuenteDatosHistoricoDto getColumna() {
		return columna;
	}
	
	public void setColumna(AtributoFuenteDatosHistoricoDto columna) {
		this.columna = columna;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public IndicadorExpresionHistoricoDto getIndicadorExpresion() {
		return indicadorExpresion;
	}
	
	public void setIndicadorExpresion(IndicadorExpresionHistoricoDto indicadorExpresion) {
		this.indicadorExpresion = indicadorExpresion;
	}
}
