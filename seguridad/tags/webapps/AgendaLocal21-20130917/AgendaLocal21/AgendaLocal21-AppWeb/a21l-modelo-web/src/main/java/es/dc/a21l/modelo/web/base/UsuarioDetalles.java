/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.modelo.web.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioDetalles implements UserDetails {
	private static final long serialVersionUID = -392818592437347841L;

	private GrantedAuthority[] authorities;
	private String userName;
	private String password;
	private boolean locked;
	private boolean anonimo;
	private long id;
	private Boolean esAdmin;
	private Boolean invitado;

	public UsuarioDetalles (long id, String userName, String password, boolean locked, boolean anonimo, Set<String> roles) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.locked = locked;
		this.anonimo = anonimo;
		this.authorities = new GrantedAuthority[roles == null ? 0 : roles.size()];
		int i = 0;
		if (roles != null) {
			for (String rol : roles) {
				this.authorities[i++] = new GrantedAuthorityImpl(rol);
			}
		}
	}
	
	public long getId() {
		return this.id;
	}
	
	public boolean isAnonimo() {
		return this.anonimo;
	}
	
	public Collection<GrantedAuthority> getAuthorities() {
		return Arrays.asList(this.authorities);
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.userName;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return !locked;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = authorities;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void setAnonimo(boolean anonimo) {
		this.anonimo = anonimo;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Boolean getEsAdmin() {
		return esAdmin;
	}

	public void setEsAdmin(Boolean esAdmin) {
		this.esAdmin = esAdmin;
	}

	public Boolean getInvitado() {
		return invitado;
	}

	public void setInvitado(Boolean invitado) {
		this.invitado = invitado;
	}
	
	
}
