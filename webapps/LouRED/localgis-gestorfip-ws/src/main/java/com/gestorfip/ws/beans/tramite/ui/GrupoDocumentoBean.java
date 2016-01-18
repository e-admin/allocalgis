package com.gestorfip.ws.beans.tramite.ui;

import java.io.Serializable;

public class GrupoDocumentoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4416040215626745645L;

	private int id;
	private String codigo;
	private String nombre;

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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

}
