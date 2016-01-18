/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu;

import java.io.InputStream;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.base.utils.enumerados.TiposFuente;
import es.dc.a21l.usuario.cu.UsuarioDto;

/**
 *
 * @author Balidea Consulting & Programming
 */

@SuppressWarnings("restriction")
@XmlRootElement(name="fuenteDto")
public class FuenteDto extends DtoBase implements Comparable<FuenteDto>{

    private String nombre;
    private String descripcion;
    private String infoConexion;
    private Date fechaRegistro;
    private TiposFuente tipo;
    private UsuarioDto registradaPor;
    private String usuario;
    private String password;
    private String fich_csv_gml;
    private String fich_shp;
    private String fich_shx;
    private String fich_dbf;
    private short escatalogointerno;
    private boolean columnasGeometricasErroneas;
    private String charSeparador;
    private String tCodificacion;
    private Long idCaracterSeparador;
    private Long idTipoCodificacion;
    
    
  
    //@XmlAttribute(name="id")
    //@Override
    //public long getId() { return super.getId();}

    @XmlAttribute(name="nombre")
	public String getNombre() {	return nombre;	}
	public void setNombre(String nombre) { this.nombre = nombre; }

	@XmlAttribute(name="descripcion")
	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	@XmlAttribute(name="infoConexion")
	public String getInfoConexion() { return infoConexion; }
	public void setInfoConexion(String infoConexion) { this.infoConexion = infoConexion; }

	@XmlAttribute(name="fechaRegistro")
	public Date getFechaRegistro() { return fechaRegistro; }
	public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

	@XmlAttribute(name="tipo")
	public TiposFuente getTipo() { return tipo; }
	public void setTipo(TiposFuente tipo) {	this.tipo = tipo; }
	public void setTipo(String tipo) {	this.tipo = TiposFuente.valueOf(tipo); }
	
	@XmlAttribute(name="esCatalogoInterno")
	public short getEsCatalogoInterno() {
		return escatalogointerno;
	}
	public void setEsCatalogoInterno(short esCatalogoInterno) {
		this.escatalogointerno = esCatalogoInterno;
	}
	
	@XmlElement(name="registradaPor")
	public UsuarioDto getRegistradaPor() { return registradaPor; }
	public void setRegistradaPor(UsuarioDto registradaPor) { this.registradaPor = registradaPor; }

	@XmlElement(name="usuario")
	public String getUsuario() { return usuario; }
	public void setUsuario(String usuario) { this.usuario = usuario; }

	@XmlElement(name="password")
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	@XmlElement(name="fich_csv_gml")
	public String getFich_csv_gml() {
		return fich_csv_gml;
	}
	public void setFich_csv_gml(String fich_csv_gml) {
		this.fich_csv_gml = fich_csv_gml;
	}
	@XmlElement(name="fich_shp")
	public String getFich_shp() {
		return fich_shp;
	}
	public void setFich_shp(String fich_shp) {
		this.fich_shp = fich_shp;
	}
	@XmlElement(name="fich_shx")
	public String getFich_shx() {
		return fich_shx;
	}
	public void setFich_shx(String fich_shx) {
		this.fich_shx = fich_shx;
	}
	@XmlElement(name="fich_dbf")
	public String getFich_dbf() {
		return fich_dbf;
	}
	public void setFich_dbf(String fich_dbf) {
		this.fich_dbf = fich_dbf;
	}
	public boolean isColumnasGeometricasErroneas() {
		return columnasGeometricasErroneas;
	}
	public void setColumnasGeometricasErroneas(boolean columnasGeometricasErroneas) {
		this.columnasGeometricasErroneas = columnasGeometricasErroneas;
	}
	public int compareTo(FuenteDto o) {
		return this.getNombre().toLowerCase().compareTo(o.getNombre().toLowerCase());
	}
	@XmlAttribute(name="charSeparador")
	public String getCharSeparador() {
		return charSeparador;
	}
	public void setCharSeparador(String charSeparador) {
		this.charSeparador = charSeparador;
	}
	@XmlAttribute(name="tCodificacion")
	public String gettCodificacion() {
		return tCodificacion;
	}
	public void settCodificacion(String tCodificacion) {
		this.tCodificacion = tCodificacion;
	}
	@XmlAttribute(name="idCaracterSeparador")
	public Long getIdCaracterSeparador() {
		return idCaracterSeparador;
	}
	public void setIdCaracterSeparador(Long idCaracterSeparador) {
		this.idCaracterSeparador = idCaracterSeparador;
	}
	@XmlAttribute(name="idTipoCodificacion")
	public Long getIdTipoCodificacion() {
		return idTipoCodificacion;
	}
	public void setIdTipoCodificacion(Long idTipoCodificacion) {
		this.idTipoCodificacion = idTipoCodificacion;
	}
	
	
	
}