package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;

public class InstrumentoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8397242803804277456L;

	private String codigo;
	private String nombre;
	
	public InstrumentoBean() {
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
