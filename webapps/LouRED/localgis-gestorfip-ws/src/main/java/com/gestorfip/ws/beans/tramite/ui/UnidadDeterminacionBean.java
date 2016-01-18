package com.gestorfip.ws.beans.tramite.ui;

public class UnidadDeterminacionBean {
	
	private int id;
	private int determinacionid;
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
		return determinacionid;
	}
	
	public void setDeterminacionid(int determinacionid) {
		this.determinacionid = determinacionid;
	}
	
	public int getTramite() {
		return tramite;
	}
	
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}


}
