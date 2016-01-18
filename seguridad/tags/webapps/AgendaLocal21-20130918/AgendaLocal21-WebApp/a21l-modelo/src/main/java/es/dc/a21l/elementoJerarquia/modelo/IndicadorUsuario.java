/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.usuario.modelo.Usuario;

//Hace referencia a que usuario creo el indicador
@Entity
@Table(name="Tb_A21l_Indicador_Usuario",uniqueConstraints=@UniqueConstraint(columnNames={"Id_A21l_Indicador","Id_A21l_Usuario"}))
public class IndicadorUsuario extends EntidadBase {

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Id_A21l_Indicador",nullable=false)
	private Indicador indicador;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Id_A21l_Usuario",nullable=true)
	private Usuario usuario;

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
