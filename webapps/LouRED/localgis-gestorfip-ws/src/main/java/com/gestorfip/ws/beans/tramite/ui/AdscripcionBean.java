package com.gestorfip.ws.beans.tramite.ui;

public class AdscripcionBean {
	
	private int id;
	private int entidadOrigen;
	private int entidadDestino;
	private double cuantia;
	private String texto;
	private int unidad;
	private int tipo;
	private int tramite;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEntidadOrigen() {
		return entidadOrigen;
	}
	public void setEntidadOrigen(int entidadOrigen) {
		this.entidadOrigen = entidadOrigen;
	}
	public int getEntidadDestino() {
		return entidadDestino;
	}
	public void setEntidadDestino(int entidadDestino) {
		this.entidadDestino = entidadDestino;
	}
	public double getCuantia() {
		return cuantia;
	}
	public void setCuantia(double cuantia) {
		this.cuantia = cuantia;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getUnidad() {
		return unidad;
	}
	public void setUnidad(int unidad) {
		this.unidad = unidad;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public int getTramite() {
		return tramite;
	}
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

}
