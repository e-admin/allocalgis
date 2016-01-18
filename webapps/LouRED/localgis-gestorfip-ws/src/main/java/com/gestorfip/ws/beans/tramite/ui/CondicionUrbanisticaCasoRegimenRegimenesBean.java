package com.gestorfip.ws.beans.tramite.ui;

public class CondicionUrbanisticaCasoRegimenRegimenesBean {
	
	  private  int id;
	  private int orden;
	  private String nombre;
	  private String texto;
	  private int padre;
	  private int regimen;
	  
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
	public int getPadre() {
		return padre;
	}
	public void setPadre(int padre) {
		this.padre = padre;
	}
	public int getRegimen() {
		return regimen;
	}
	public void setRegimen(int regimen) {
		this.regimen = regimen;
	}

}
