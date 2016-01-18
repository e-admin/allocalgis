/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.modelo;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import es.dc.a21l.elementoJerarquia.modelo.Indicador;
import es.dc.a21l.usuario.modelo.Usuario;

@Entity
@Table(name="Tb_A21l_EstiloVisualizacionDiagramaBarras")
@PrimaryKeyJoinColumn(name="Id_A21l_EstiloVisualizacion")
@DiscriminatorValue("Barras")
public class EstiloVisualizacionDiagramaBarras extends EstiloVisualizacion {

	@Column(name="tamanho") 
	private Integer tamanho;
	
	@Column(name="color")
	private String color;
	
	@JoinColumn(name = "Id_A21l_Usuario", nullable = false)
    @OneToOne
    private Usuario usuario;
	
	@JoinColumn(name = "Id_A21l_Indicador", nullable = false)
    @OneToOne
	public Indicador indicador;
	
	public Integer getTamanho() {
		return tamanho;
	}
	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Indicador getIndicador() {
		return indicador;
	}
	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}	
	
}