/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;

@Entity
@Table(name = "Tb_A21l_TablaFuenteDatos")
public class TablaFuenteDatos extends EntidadBase {
	@Column(name = "nombre")
	private String nombre;
	
	@JoinColumn(name = "idindicador", referencedColumnName = "id_a21l_elementojerarquia", nullable = false)
    @OneToOne
	private Indicador indicador;
	@JoinColumn(name = "idfuente", referencedColumnName = "Id", nullable = false)
    @OneToOne
	private Fuente fuente;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="tabla",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<AtributoFuenteDatos> atributoFuenteDatos= new HashSet<AtributoFuenteDatos>();
		
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	
	public Fuente getFuente() {
		return fuente;
	}

	public void setFuente(Fuente fuente) {
		this.fuente = fuente;
	}

	public Set<AtributoFuenteDatos> getAtributoFuenteDatos() {
		return atributoFuenteDatos;
	}

	public void setAtributoFuenteDatos(Set<AtributoFuenteDatos> atributoFuenteDatos) {
		this.atributoFuenteDatos = atributoFuenteDatos;
	}	
	
}
