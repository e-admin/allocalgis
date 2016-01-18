/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import es.dc.a21l.base.modelo.EntidadBase;

/**
 *
 * @author Balidea Consulting & Programming
 */
@Entity
@Table(name="Tb_A21l_RangosVisualizacionMapa")
public class RangosVisualizacionMapa extends EntidadBase{
	
	@OneToOne
	@JoinColumn(name="idestilomapa",nullable=false)
	private EstiloVisualizacionMapa estiloMapa;
	
	@Column(name="inicio")
	private Double inicio;
	
	@Column(name="fin")
	private Double fin;
	
	@Column(name="color")
	private String color;
	
	public EstiloVisualizacionMapa getEstiloMapa() {
		return estiloMapa;
	}
	public void setEstiloMapa(EstiloVisualizacionMapa estiloMapa) {
		this.estiloMapa = estiloMapa;
	}
	public Double getInicio() {
		return inicio;
	}
	public void setInicio(Double inicio) {
		this.inicio = inicio;
	}
	public Double getFin() {
		return fin;
	}
	public void setFin(Double fin) {
		this.fin = fin;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
