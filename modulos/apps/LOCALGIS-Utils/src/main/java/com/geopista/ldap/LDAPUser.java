/**
 * LDAPUser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ldap;

/**
 * 
 * @author yraton
 *
 */

public class LDAPUser {
	
	private String uid;
	private String nombre;
	private String mail;
	 private String nombreCompleto;

	
	
	public String getUid() {
		return uid;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	  public void setNombreCompleto(String nombreCompleto) {
	        this.nombreCompleto = nombreCompleto;
	    }
	public void setName(String upperCase) {
		this.nombre=upperCase;
		
	}

}
