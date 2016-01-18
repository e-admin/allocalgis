package com.gestorfip.ws.beans.configuracion;

public class ConfiguracionGestor {
	
	private int id;
	private int idVersion;
	private int idCrs;
	
	public int getIdCrs() {
		return idCrs;
	}

	public void setIdCrs(int idCrs) {
		this.idCrs = idCrs;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdVersion() {
		return idVersion;
	}

	public void setIdVersion(int idVersion) {
		this.idVersion = idVersion;
	}
}
