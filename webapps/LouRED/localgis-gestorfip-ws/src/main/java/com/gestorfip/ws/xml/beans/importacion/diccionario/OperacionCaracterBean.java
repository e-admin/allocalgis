package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;

public class OperacionCaracterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6394010200954217390L;

	private String tipoOperacionDeterminacion;
	private String caracterOperadora;
	private String caracterOperada;

	public OperacionCaracterBean() {

	}

	public String getTipoOperacionDeterminacion() {
		return tipoOperacionDeterminacion;
	}

	public void setTipoOperacionDeterminacion(String tipoOperacionDeterminacion) {
		this.tipoOperacionDeterminacion = tipoOperacionDeterminacion;
	}

	public String getCaracterOperadora() {
		return caracterOperadora;
	}

	public void setCaracterOperadora(String caracterOperadora) {
		this.caracterOperadora = caracterOperadora;
	}

	public String getCaracterOperada() {
		return caracterOperada;
	}

	public void setCaracterOperada(String caracterOperada) {
		this.caracterOperada = caracterOperada;
	}
	
	
}
