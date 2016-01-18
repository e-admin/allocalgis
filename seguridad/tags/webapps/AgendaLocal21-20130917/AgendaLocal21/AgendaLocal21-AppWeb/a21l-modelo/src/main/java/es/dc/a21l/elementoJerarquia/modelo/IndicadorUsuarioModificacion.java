/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import es.dc.a21l.base.modelo.EntidadBase;
//Hace referencia a que usuario modifica el indicador
@Entity
@Table(name="Tb_A21l_Indicador_Modificacion")
public class IndicadorUsuarioModificacion extends EntidadBase {

	
	@Column(name="Id_A21l_Indicador",nullable=false)
	private Long idIndicador;
	
	@Column(name="Id_A21l_Usuario",nullable=false)
	private Long idUsuario;
	
	@Column(name="login_usuario",nullable=true)
	private String loginUsuario;
	
	@Column(name="nombre_indicador",nullable=true)
	private String nombreIndicador;
	
	@Column(name="accion",nullable=true)
	private String accion;

	public Long getIdIndicador() {
		return idIndicador;
	}

	public void setIdIndicador(Long idIndicador) {
		this.idIndicador = idIndicador;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getNombreIndicador() {
		return nombreIndicador;
	}

	public void setNombreIndicador(String nombreIndicador) {
		this.nombreIndicador = nombreIndicador;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
	
		
}
