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

//define persimos entre elementos de jerarquia y roles
@Entity
@Table(name="Tb_A21l_Rol_ElementoJerarquia",uniqueConstraints={@UniqueConstraint(columnNames={"Id_A21l_Rol","Id_A21l_ElementoJerarquia"})})
public class RolElementoJerarquia extends EntidadBase {
	
	@ManyToOne
	@JoinColumn(name="Id_A21l_Rol",nullable=false)
	private Rol rol;
	
	@ManyToOne
	@JoinColumn(name="Id_A21l_ElementoJerarquia",nullable=false)
	private ElementoJerarquia elementoJerarquia;
	
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	public ElementoJerarquia getElementoJerarquia() {
		return elementoJerarquia;
	}
	public void setElementoJerarquia(ElementoJerarquia elementoJerarquia) {
		this.elementoJerarquia = elementoJerarquia;
	}
	
	

}
