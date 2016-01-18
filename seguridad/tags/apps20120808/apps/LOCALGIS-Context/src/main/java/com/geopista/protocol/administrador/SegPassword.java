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
