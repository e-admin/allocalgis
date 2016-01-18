/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;
import es.dc.a21l.usuario.modelo.Usuario;

@Entity(name="Tb_A21l_EstiloVisualizacion")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="tipoDiagrama")
public class EstiloVisualizacion extends EntidadBase implements Serializable {

	@Column(name="tamanhoFuente")
	private Double tamanhoFuente;
	@Column(name="tipoFuente")
    private String tipoFuente;
		
	public Double getTamanhoFuente() {
		return tamanhoFuente;
	}
	public void setTamanhoFuente(Double tamanhoFuente) {
		this.tamanhoFuente = tamanhoFuente;
	}
	public String getTipoFuente() {
		return tipoFuente;
	}
	public void setTipoFuente(String tipoFuente) {
		this.tipoFuente = tipoFuente;
	}
}