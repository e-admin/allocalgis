package com.gestorfip.ws.beans.tramite.ui;

import java.io.Serializable;

public class TipoTramiteBean implements Serializable {

	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 6027688207267556022L;

	private String codigo;
	private String nombre;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public TipoTramiteBean() {
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
	
	

}
