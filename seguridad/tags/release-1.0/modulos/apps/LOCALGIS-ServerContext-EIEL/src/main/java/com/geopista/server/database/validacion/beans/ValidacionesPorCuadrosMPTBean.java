package com.geopista.server.database.validacion.beans;

import java.io.Serializable;

public class ValidacionesPorCuadrosMPTBean implements Serializable{
	
	private int id;
	private String nombre;
	private int cuadroid;

	public int getCuadroid() {
		return cuadroid;
	}
	public void setCuadroid(int cuadroid) {
		this.cuadroid = cuadroid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
