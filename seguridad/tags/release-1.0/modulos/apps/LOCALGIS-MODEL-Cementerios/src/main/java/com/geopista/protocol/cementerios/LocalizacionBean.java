package com.geopista.protocol.cementerios;

import java.io.Serializable;



/**
 * Clase que implementa un objeto de tipo localizacion
 */

//TODO: creo que no deben extender de cementeriobean..
//public class LocalizacionBean extends CementerioBean implements Serializable{

public class LocalizacionBean implements Serializable{
	
	private long id = -1;
	private String descripcion;
	private float valor;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

}
