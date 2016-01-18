package com.gestorfip.ws.xml.beans.importacion.layers;

import java.io.Serializable;

public class CaracterDeterminacionLayerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6404417325087757473L;

	private String codigo;
	private String nombre;
	
	public CaracterDeterminacionLayerBean() {
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
