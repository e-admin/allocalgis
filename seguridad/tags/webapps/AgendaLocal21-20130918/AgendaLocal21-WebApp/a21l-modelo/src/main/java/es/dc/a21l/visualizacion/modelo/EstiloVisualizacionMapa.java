/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;
import es.dc.a21l.usuario.modelo.Usuario;
import es.dc.a21l.usuario.modelo.UsuarioElementoJerarquia;

@Entity
@Table(name="Tb_A21l_EstiloVisualizacionMapa")
public class EstiloVisualizacionMapa extends EntidadBase {

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="estiloMapa")
	private Set<RangosVisualizacionMapa> rangos= new HashSet<RangosVisualizacionMapa>();
	
	@JoinColumn(name = "Id_A21l_Usuario", nullable = false)
    @OneToOne
    private Usuario usuario;
	
	@JoinColumn(name = "Id_A21l_Indicador", nullable = false)
    @OneToOne
	public Indicador indicador;
	
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
	public Set<RangosVisualizacionMapa> getRangos() {
		return rangos;
	}
	public void setRangos(Set<RangosVisualizacionMapa> rangos) {
		this.rangos = rangos;
	}
}