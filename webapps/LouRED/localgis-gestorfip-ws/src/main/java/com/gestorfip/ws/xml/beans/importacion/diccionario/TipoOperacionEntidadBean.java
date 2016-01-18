package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;

public class TipoOperacionEntidadBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1844248789162008815L;

	private String codigo;
	private String nombre;
	private String tipoEntidad;
	
	public TipoOperacionEntidadBean() {
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

	public String getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(String tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}
	
	

}
