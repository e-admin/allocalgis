/**
 * SegPassword.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import com.geopista.app.AppConstants;


public class SegPassword implements java.io.Serializable, Cloneable{
	
	private String user;
	private boolean bloqueado;
	private boolean aviso;
	private boolean caducado;
	private int intentos;
	private int intentos_reiterados = 0;
	private boolean usuarioNoExiste;
	
	public SegPassword(){
		intentos = AppConstants.INTENTOS_MINIMOS;
	}
	
	public boolean isUsuarioNoExiste() {
		return usuarioNoExiste;
	}



	public void setUsuarioNoExiste(boolean usuarioNoExiste) {
		this.usuarioNoExiste = usuarioNoExiste;
	}



	public int getIntentos_reiterados() {
		return intentos_reiterados;
	}

	public void setIntentos_reiterados(int intentos_reiterados) {
		this.intentos_reiterados = intentos_reiterados;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	public boolean isAviso() {
		return aviso;
	}
	public void setAviso(boolean aviso) {
		this.aviso = aviso;
	}
	public boolean isCaducado() {
		return caducado;
	}
	public void setCaducado(boolean caducado) {
		this.caducado = caducado;
	}
	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}	
	
}
