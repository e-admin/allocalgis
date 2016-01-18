/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.modelo;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.dc.a21l.base.cu.impl.UtilidadesModelo;
import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuario;

	/**
	 *
	 * @author Balidea Consulting & Programming
	 */
	@Entity
	@Table(name = "Tb_A21l_Usuario",uniqueConstraints={@UniqueConstraint(columnNames={"Login"})})
	public class Usuario extends EntidadBase {
	    
		@Column(name = "Nombre",nullable=false)
	    private String nombre;

	    @Column(name = "Descripcion")
	    private String descripcion;

	    @Column(name = "Activo", nullable=false)
	    private Short activo= UtilidadesModelo.convertBooleanToShort(true);
	    
	    @Column(name = "Password",nullable=false)
	    private String password;
	    
	    @Column(name = "Login",nullable=false)
	    private String login;
	    
	    @Column(name="Email",nullable=false)
	    private String email;
	    
	    @Column(name="EsAdmin",nullable=false)
	    private Short esAdmin = UtilidadesModelo.convertBooleanToShort(true);
	    
	    @Column(name="FechaRegistro",nullable=false)
	    private Date fechaRegistro;
	    

	    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario",orphanRemoval=true)
		@OnDelete(action=OnDeleteAction.CASCADE)
		private Set<UsuarioElementoJerarquia> usuarioElementosJerarquias= new HashSet<UsuarioElementoJerarquia>();
	    
	    
	    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario",orphanRemoval=true)
		@OnDelete(action=OnDeleteAction.CASCADE)
		private Set<UsuarioRol> usuarioRols= new HashSet<UsuarioRol>();
	    
	    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario",orphanRemoval=true)
		@OnDelete(action=OnDeleteAction.CASCADE)
		private Set<IndicadorUsuario> indicadorUsuarios= new HashSet<IndicadorUsuario>();
	    
	    
	    
	    public Set<IndicadorUsuario> getIndicadorUsuarios() {
			return indicadorUsuarios;
		}

		public void setIndicadorUsuarios(Set<IndicadorUsuario> indicadorUsuarios) {
			this.indicadorUsuarios = indicadorUsuarios;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public Boolean getActivo() {
			return UtilidadesModelo.convertShortToBoolean(this.activo);
		}

		public void setActivo(Boolean activo) {
			this.activo = UtilidadesModelo.convertBooleanToShort(activo);
		}

		public Boolean getEsAdmin() {
			return UtilidadesModelo.convertShortToBoolean(this.esAdmin);
		}

		public void setEsAdmin(Boolean esAdmin) {
			this.esAdmin = UtilidadesModelo.convertBooleanToShort(esAdmin);
		}

		public Set<UsuarioElementoJerarquia> getUsuarioElementosJerarquias() {
			return usuarioElementosJerarquias;
		}

		public void setUsuarioElementosJerarquias(
				Set<UsuarioElementoJerarquia> usuarioElementosJerarquias) {
			this.usuarioElementosJerarquias = usuarioElementosJerarquias;
		}

		public Set<UsuarioRol> getUsuarioRols() {
			return usuarioRols;
		}

		public void setUsuarioRols(Set<UsuarioRol> usuarioRols) {
			this.usuarioRols = usuarioRols;
		}

		public Date getFechaRegistro() {
			return fechaRegistro;
		}

		public void setFechaRegistro(Date fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
		}    
		
	}
