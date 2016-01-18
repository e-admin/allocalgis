package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;

public class TipoOperacionDeterminacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8479048690486022219L;

	private String codigo;
	private String nombre;
	
	public TipoOperacionDeterminacionBean() {
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
