/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import es.dc.a21l.base.cu.impl.UtilidadesModelo;
import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresion;
import es.dc.a21l.elementoJerarquia.modelo.Relacion;

@Entity
@Table(name = "tb_a21l_Atributo")
public class Atributo extends EntidadBase {
	@Column(name="nombre")
	private String nombre;
	@Column(name="ordenVisualizacion")
	private Integer ordenVisualizacion;
	@Column(name = "mostrar")
	private Short mostrar = UtilidadesModelo.convertBooleanToShort(true);
	@Column(name = "esAmbito")
	private Short esAmbito = UtilidadesModelo.convertBooleanToShort(true);
	@Column(name = "esMapa")
	private Short esMapa = UtilidadesModelo.convertBooleanToShort(true);
	@JoinColumn(name = "columna", referencedColumnName = "Id", nullable = true)
    @OneToOne
	private AtributoFuenteDatos columna;
	@JoinColumn(name = "idindicadorexpresion", nullable = true)
	@OneToOne
	private IndicadorExpresion indicadorExpresion;
	@JoinColumn(name = "idrelacion", nullable = true)
	@OneToOne
	private Relacion relacion;
	
	
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
	public Short getMostrar() {
		return mostrar;
	}
	public void setMostrar(Short mostrar) {
		this.mostrar = mostrar;
	}
	public Short getEsAmbito() {
		return esAmbito;
	}
	public void setEsAmbito(Short esAmbito) {
		this.esAmbito = esAmbito;
	}
	public Short getEsMapa() {
		return esMapa;
	}
	public void setEsMapa(Short esMapa) {
		this.esMapa = esMapa;
	}
	public AtributoFuenteDatos getColumna() {
		return columna;
	}
	public void setColumna(AtributoFuenteDatos columna) {
		this.columna = columna;
	}
	public IndicadorExpresion getIndicadorExpresion() {
		return indicadorExpresion;
	}
	public void setIndicadorExpresion(IndicadorExpresion indicadorExpresion) {
		this.indicadorExpresion = indicadorExpresion;
	}
	public Relacion getRelacion() {
		return relacion;
	}
	public void setRelacion(Relacion relacion) {
		this.relacion = relacion;
	}	
}
