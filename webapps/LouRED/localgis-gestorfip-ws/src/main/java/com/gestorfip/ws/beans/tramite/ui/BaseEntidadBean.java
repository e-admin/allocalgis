package com.gestorfip.ws.beans.tramite.ui;

public class BaseEntidadBean {
	
	private int id;
	private int entidadid;
	private int tramite;
	private String nombre;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getDeterminacionid() {
		return entidadid;
	}
	
	public void setEntidadid(int entidadid) {
		this.entidadid = entidadid;
	}
	
	public int getTramite() {
		return tramite;
	}
	
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}


}
