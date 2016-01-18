package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;

public class CaracterDeterminacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6404417325087757473L;

	private String codigo;
	private String nombre;
	private int aplicaciones_max;
	private int aplicaciones_min;
	
	public CaracterDeterminacionBean() {
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

	public int getAplicaciones_max() {
		return aplicaciones_max;
	}

	public void setAplicaciones_max(int aplicacionesMax) {
		aplicaciones_max = aplicacionesMax;
	}

	public int getAplicaciones_min() {
		return aplicaciones_min;
	}

	public void setAplicaciones_min(int aplicacionesMin) {
		aplicaciones_min = aplicacionesMin;
	}
	
	

}
