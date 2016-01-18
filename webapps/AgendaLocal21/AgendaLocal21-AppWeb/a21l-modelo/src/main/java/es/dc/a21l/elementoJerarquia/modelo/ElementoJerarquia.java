/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.usuario.modelo.RolElementoJerarquia;
import es.dc.a21l.usuario.modelo.UsuarioElementoJerarquia;

@Entity(name="Tb_A21l_ElementoJerarquia")
@Inheritance(strategy=InheritanceType.JOINED)
public class ElementoJerarquia extends EntidadBase implements Serializable {

	@Column(name="Nombre",nullable=false)
	private String nombre;
	
	@Column(name="Descripcion")
	private String descripcion;
	
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="elementoJerarquia",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<UsuarioElementoJerarquia> usuarioElementosJerarquias= new HashSet<UsuarioElementoJerarquia>();

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="elementoJerarquia",orphanRemoval=true)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<RolElementoJerarquia> rolElementoJerarquias= new HashSet<RolElementoJerarquia>();
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<UsuarioElementoJerarquia> getUsuarioElementosJerarquias() {
		return usuarioElementosJerarquias;
	}

	public void setUsuarioElementosJerarquias(
			Set<UsuarioElementoJerarquia> usuarioElementosJerarquias) {
		this.usuarioElementosJerarquias = usuarioElementosJerarquias;
	}

	public Set<RolElementoJerarquia> getRolElementoJerarquias() {
		return rolElementoJerarquias;
	}

	public void setRolElementoJerarquias(
			Set<RolElementoJerarquia> rolElementoJerarquias) {
		this.rolElementoJerarquias = rolElementoJerarquias;
	}
	
}
