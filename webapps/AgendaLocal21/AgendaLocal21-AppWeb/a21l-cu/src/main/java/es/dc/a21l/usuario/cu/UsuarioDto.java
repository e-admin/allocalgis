/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;

/**
 *
 * @author Balidea Consulting & Programming
 */

@SuppressWarnings("restriction")
@XmlRootElement(name="usuarioDto")
public class UsuarioDto extends DtoBase{

	
    private String descripcion;
    private Boolean activo;
    private String nombre;
    private String password;
    private String login;
    private String email;
    private Boolean esAdmin;
    private String passwordConfirm;
    private Date fechaRegistro;
    private String passwordOld;
        
    //atributos Jsp
    private List<Long> roles;
    private List<Long> eltosJerarquia; //Elementos sobre los que el usuario tiene permiso
    private String referer;

    public UsuarioDto(){
    	this.roles = new ArrayList<Long>();
		this.eltosJerarquia = new ArrayList<Long>();
	}
    
 
    @XmlAttribute(name="nombre")
    public String getNombre() { return nombre;}
    
	public void setNombre(String nombre) { this.nombre = nombre;}
    
    @XmlAttribute(name="descripcion")
    public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

    @XmlAttribute(name="password")
    public String getPassword() { return password; }
    

	public void setPassword(String password) { this.password = password; }

	@XmlAttribute(name="login")
	public String getLogin() { return login; }
	

	public void setLogin(String login) { this.login = login; }

	@XmlAttribute(name="activo")
	public Boolean getActivo() {
		return activo;
	}
	

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	@XmlAttribute(name="email")
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	@XmlAttribute(name="esAdmin")
	public Boolean getEsAdmin() {
		return esAdmin;
	}


	public void setEsAdmin(Boolean esAdmin) {
		this.esAdmin = esAdmin;
	}

	@XmlAttribute(name="passwordConfirm")
	public String getPasswordConfirm() {
		return passwordConfirm;
	}


	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}


	@XmlAttribute(name="fechaRegistro")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}



	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getPasswordOld() {
		return passwordOld;
	}

	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}  
	
	public List<Long> getEltosJerarquia() {
		return eltosJerarquia;
	}
	public void setEltosJerarquia(List<Long> eltosJerarquia) {
		this.eltosJerarquia = eltosJerarquia;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}
}
