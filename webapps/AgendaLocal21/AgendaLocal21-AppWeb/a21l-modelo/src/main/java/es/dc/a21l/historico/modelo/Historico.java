/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.historico.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;

@SuppressWarnings("serial")
@Entity
@Table(name="Tb_A21l_Historico")
public class Historico extends EntidadBase implements Serializable {

	public Historico(){
		this.indicador = new Indicador();
	}
	
	@Column(name="Fecha", nullable=false)
	private Date fecha;
	
	 
    @OneToOne
	@JoinColumn(name = "idindicador", referencedColumnName = "id_a21l_elementojerarquia", nullable = false)
	private Indicador indicador;

    @Column(name="Datos")
//    @Lob
//    @Type(type="org.hibernate.type.BinaryType")
    @Basic(fetch = FetchType.LAZY)
	private byte[] datos;
	
	@Column(name="Mapa")
//	@Lob
//	@Type(type="org.hibernate.type.BinaryType")
	@Basic(fetch = FetchType.LAZY)
	private byte[] mapa;
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}  
	
	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}
	
	public byte[] getDatos() {
		return datos;
	}

	public void setDatos(byte[] datos) {
		this.datos = datos;
	}
	
	public byte[] getMapa() {
		return mapa;
	}

	public void setMapa(byte[] mapa) {
		this.mapa = mapa;
	}

}
