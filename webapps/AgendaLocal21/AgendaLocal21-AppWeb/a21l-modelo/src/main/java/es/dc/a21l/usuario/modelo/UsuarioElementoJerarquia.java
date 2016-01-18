/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.modelo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.ElementoJerarquia;


//Define permisos entre el usuario y elementos de la jerarquia
@Entity
@Table(name="Tb_A21l_Usuario_ElementoJerarquia",uniqueConstraints={@UniqueConstraint(columnNames={"Id_A21l_Usuario","Id_A21l_ElementoJerarquia"})})
public class UsuarioElementoJerarquia extends EntidadBase {
	
	@ManyToOne
	@JoinColumn(name="Id_A21l_Usuario",nullable=false)
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="Id_A21l_ElementoJerarquia",nullable=false)
	private ElementoJerarquia elementoJerarquia;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ElementoJerarquia getElementoJerarquia() {
		return elementoJerarquia;
	}

	public void setElementoJerarquia(ElementoJerarquia elementoJerarquia) {
		this.elementoJerarquia = elementoJerarquia;
	}

}
