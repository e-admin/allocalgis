package com.gestorfip.ws.beans.tramite.ui;

public class RegulacionesEspecificasBean {

	private int id;
	private int orden;
	private String nombre;
	private String texto;
	private int madre;
	private int determinacion;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getOrden() {
		return orden;
	}
	
	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getMadre() {
		return madre;
	}
	
	public void setMadre(int madre) {
		this.madre = madre;
	}
	
	public int getDeterminacion() {
		return determinacion;
	}
	
	public void setDeterminacion(int determinacion) {
		this.determinacion = determinacion;
	}
	
}
