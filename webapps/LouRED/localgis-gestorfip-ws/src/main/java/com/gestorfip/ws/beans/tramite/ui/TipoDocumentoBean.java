package com.gestorfip.ws.beans.tramite.ui;

import java.io.Serializable;

public class TipoDocumentoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -286961449160197809L;

	private int id;
	private String codigo;
	private String nombre;
	
	public TipoDocumentoBean() {
		// TODO Auto-generated constructor stub
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
