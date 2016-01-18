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
@Table(name="Tb_A21l_EstiloVisualizacionDiagramaSectores")
@PrimaryKeyJoinColumn(name="Id_A21l_EstiloVisualizacion")
@DiscriminatorValue("Sectores")
public class EstiloVisualizacionDiagramaSectores extends EstiloVisualizacion {

	@Column(name="diametro") 
	private Integer diametro;
	
	@Column(name="colores")
	private String colores;
	
	@JoinColumn(name = "Id_A21l_Usuario", nullable = false)
    @OneToOne
    private Usuario usuario;
	
	@JoinColumn(name = "Id_A21l_Indicador", nullable = false)
    @OneToOne
	public Indicador indicador;
	
	public Integer getDiametro() {
		return diametro;
	}
	public void setDiametro(Integer diametro) {
		this.diametro = diametro;
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
	public String getColores() {
		return colores;
	}
	public void setColores(String colores) {
		this.colores = colores;
	}
}