package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.util.Date;

public class VersionEiel implements Serializable{

	private int idVersion;
	private String usuario = null;
	private Date fecha = null;
	private String accion = null;
	
	
	public int getIdVersion() {
		return idVersion;
	}
	public void setIdVersion(int idVersion) {
		this.idVersion = idVersion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
}
