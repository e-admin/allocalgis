package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;

public class TipoAmbitoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1352207730476356733L;

	private String codigo;
	private String nombre;
	
	public TipoAmbitoBean() {
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
