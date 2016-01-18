/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name="Tb_A21l_Categoria")
@PrimaryKeyJoinColumn(name="Id_A21l_ElementoJerarquia")
public class Categoria extends ElementoJerarquia {
	
	//nula si es padre
	@ManyToOne
	@JoinColumn(name="Id_A21l_Categoria_Padre",nullable=true)
	private Categoria categoriaPadre;
	
//    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="categoria",orphanRemoval=true)
//	@OnDelete(action=OnDeleteAction.CASCADE)
//	private Set<Indicador> indicadors= new HashSet<Indicador>();
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="categoriaPadre",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<Categoria> categorias= new HashSet<Categoria>();

	public Categoria getCategoriaPadre() {
		return categoriaPadre;
	}

	public void setCategoriaPadre(Categoria categoriaPadre) {
		this.categoriaPadre = categoriaPadre;
	}

//	public Set<Indicador> getIndicadors() {
//		return indicadors;
//	}
//
//	public void setIndicadors(Set<Indicador> indicadors) {
//		this.indicadors = indicadors;
//	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

}
