package com.gestorfip.ws.beans.tramite.ui;

public class CaracteresDeterminacionBean {
	
	  private int id; 
	  private String codigo;
	  private String nombre;
	  private int aplicaciones_max;
	  private int aplicaciones_min;
	  private int fip;
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getFip() {
		return fip;
	}
	public void setFip(int fip) {
		this.fip = fip;
	}
	

}
