/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import es.dc.a21l.base.modelo.EntidadBase;

@Entity
@Table(name="Tb_A21l_Relacion")
public class Relacion extends EntidadBase {

	@Column(name="nombreTablaRelacion")
	private String nombreTablaRelacion;
	
	@Column(name="nombreColumnaRelacion")
	private String nombreColumnaRelacion;
	
	@Column(name="nombreTablaRelacionada")
	private String nombreTablaRelacionada;
	
	@Column(name="nombreColumnaRelacionada")
	private String nombreColumnaRelacionada;
	
	@Column(name="idfuenterelacion")
	private String idFuenteRelacion;
	
	@Column(name="idfuenterelacionada")
	private String idFuenteRelacionada;
	
	@JoinColumn(name = "idindicador", referencedColumnName = "id_a21l_elementojerarquia", nullable = false)
    @OneToOne
    private Indicador indicador;

	public String getNombreTablaRelacion() {
		return nombreTablaRelacion;
	}

	public void setNombreTablaRelacion(String nombreTablaRelacion) {
		this.nombreTablaRelacion = nombreTablaRelacion;
	}

	public String getNombreColumnaRelacion() {
		return nombreColumnaRelacion;
	}

	public void setNombreColumnaRelacion(String nombreColumnaRelacion) {
		this.nombreColumnaRelacion = nombreColumnaRelacion;
	}

	public String getNombreTablaRelacionada() {
		return nombreTablaRelacionada;
	}

	public void setNombreTablaRelacionada(String nombreTablaRelacionada) {
		this.nombreTablaRelacionada = nombreTablaRelacionada;
	}

	public String getNombreColumnaRelacionada() {
		return nombreColumnaRelacionada;
	}

	public void setNombreColumnaRelacionada(String nombreColumnaRelacionada) {
		this.nombreColumnaRelacionada = nombreColumnaRelacionada;
	}

	public String getIdFuenteRelacion() {
		return idFuenteRelacion;
	}

	public void setIdFuenteRelacion(String idFuenteRelacion) {
		this.idFuenteRelacion = idFuenteRelacion;
	}

	public String getIdFuenteRelacionada() {
		return idFuenteRelacionada;
	}

	public void setIdFuenteRelacionada(String idFuenteRelacionada) {
		this.idFuenteRelacionada = idFuenteRelacionada;
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}
}
