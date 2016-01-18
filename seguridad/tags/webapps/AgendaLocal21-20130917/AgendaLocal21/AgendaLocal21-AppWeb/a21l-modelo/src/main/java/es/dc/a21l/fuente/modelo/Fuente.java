/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import es.dc.a21l.base.cu.impl.UtilidadesModelo;
import es.dc.a21l.base.modelo.EntidadBase;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.usuario.modelo.Usuario;
 
	/**
	 *
	 * @author Balidea Consulting & Programming
	 */
	@Entity
	@Table(name = "tb_a21l_fuente")
	public class Fuente extends EntidadBase {
	    
		@Column(name = "Nombre")
		private String nombre;
		@Column(name = "Descripcion")
	    private String descripcion;
		@Column(name = "infoConexion")
	    private String infoConexion;
		@Column(name = "fechaRegistro")
	    private Date fechaRegistro;
		@Column(name = "tipo")
	    private TiposFuente tipo;
		@JoinColumn(name = "registrada_por", referencedColumnName = "Id", nullable = false)
	    @OneToOne
	    private Usuario registradaPor;
		@Column(name="usuario")
		private String usuario;
		@Column(name="password")
		private String password;
		@Column(name="fich_csv_gml")
		private String fich_csv_gml;
		@Column(name="fich_shp")
		private String fich_shp;
		@Column(name="fich_shx")
		private String fich_shx;
		@Column(name="fich_dbf")
		private String fich_dbf;
		@Column(name = "escatalogointerno")
		private short escatalogointerno = (short)0;
		@JoinColumn(name = "id_caracter_separador", referencedColumnName = "Id", nullable = true)
	    @OneToOne
	    private CaracterSeparador caracterSeparador;
		@JoinColumn(name = "id_tipo_codificacion", referencedColumnName = "Id", nullable = true)
	    @OneToOne
	    private TipoCodificacion tipoCodificacion;
		
		
		
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
		public String getInfoConexion() {
			return infoConexion;
		}
		public void setInfoConexion(String infoConexion) {
			this.infoConexion = infoConexion;
		}
		public Date getFechaRegistro() {
			return fechaRegistro;
		}
		public void setFechaRegistro(Date fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
		}
		public TiposFuente getTipo() {
			return tipo;
		}
		public void setTipo(TiposFuente tipo) {
			this.tipo = tipo;
		}
		public Usuario getRegistradaPor() {
			return registradaPor;
		}
		public void setRegistradaPor(Usuario registradaPor) {
			this.registradaPor = registradaPor;
		}
		public String getUsuario() {
			return usuario;
		}
		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getFich_csv_gml() {
			return fich_csv_gml;
		}
		public void setFich_csv_gml(String fich_csv_gml) {
			this.fich_csv_gml = fich_csv_gml;
		}
		public String getFich_shp() {
			return fich_shp;
		}
		public void setFich_shp(String fich_shp) {
			this.fich_shp = fich_shp;
		}
		public String getFich_shx() {
			return fich_shx;
		}
		public void setFich_shx(String fich_shx) {
			this.fich_shx = fich_shx;
		}
		public String getFich_dbf() {
			return fich_dbf;
		}
		public void setFich_dbf(String fich_dbf) {
			this.fich_dbf = fich_dbf;
		}

		public short getEsCatalogoInterno() {
			return this.escatalogointerno;
		}
		public void setEsCatalogoInterno(short esInterno) {
			this.escatalogointerno = esInterno;
		}
		public CaracterSeparador getCaracterSeparador() {
			return caracterSeparador;
		}
		public void setCaracterSeparador(CaracterSeparador caracterSeparador) {
			this.caracterSeparador = caracterSeparador;
		}
		public TipoCodificacion getTipoCodificacion() {
			return tipoCodificacion;
		}
		public void setTipoCodificacion(TipoCodificacion tipoCodificacion) {
			this.tipoCodificacion = tipoCodificacion;
		}
		
	}
	