/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu;

import es.dc.a21l.base.cu.DtoBase;

public class RelacionesJspDto extends DtoBase {

	private String tablaRelacion;
	private String columnaRelacion;
	private String idFuenteRelacion;
	private String tablaRelacionada;
	private String columnaRelacionada;
	private String idFuenteRelacionada;
	private String ordenVisualizacion;
	private Boolean mostrar;
	
	public String getTablaRelacion() {
		return tablaRelacion;
	}
	public void setTablaRelacion(String tablaRelacion) {
		this.tablaRelacion = tablaRelacion;
	}
	public String getColumnaRelacion() {
		return columnaRelacion;
	}
	public void setColumnaRelacion(String columnaRelacion) {
		this.columnaRelacion = columnaRelacion;
	}
	public String getIdFuenteRelacion() {
		return idFuenteRelacion;
	}
	public void setIdFuenteRelacion(String idFuenteRelacion) {
		this.idFuenteRelacion = idFuenteRelacion;
	}
	public String getTablaRelacionada() {
		return tablaRelacionada;
	}
	public void setTablaRelacionada(String tablaRelacionada) {
		this.tablaRelacionada = tablaRelacionada;
	}
	public String getColumnaRelacionada() {
		return columnaRelacionada;
	}
	public void setColumnaRelacionada(String columnaRelacionada) {
		this.columnaRelacionada = columnaRelacionada;
	}
	public String getIdFuenteRelacionada() {
		return idFuenteRelacionada;
	}
	public void setIdFuenteRelacionada(String idFuenteRelacionada) {
		this.idFuenteRelacionada = idFuenteRelacionada;
	}
	public String getOrdenVisualizacion() {
		return ordenVisualizacion;
	}
	public void setOrdenVisualizacion(String ordenVisualizacion) {
		this.ordenVisualizacion = ordenVisualizacion;
	}
	public Boolean getMostrar() {
		return mostrar;
	}
	public void setMostrar(Boolean mostrar) {
		this.mostrar = mostrar;
	}
}
