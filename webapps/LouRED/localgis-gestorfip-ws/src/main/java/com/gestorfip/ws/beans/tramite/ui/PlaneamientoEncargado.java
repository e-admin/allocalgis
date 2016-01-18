package com.gestorfip.ws.beans.tramite.ui;

public class PlaneamientoEncargado {
	
	private int id;
	private String nombre;
	private TipoTramiteBean tipoTramite;
	private int idFip;
	private String municipio;
	

	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public int getIdFip() {
		return idFip;
	}
	public void setIdFip(int idFip) {
		this.idFip = idFip;
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
	public TipoTramiteBean getTipoTramite() {
		return tipoTramite;
	}
	public void setTipoTramite(TipoTramiteBean tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

}
