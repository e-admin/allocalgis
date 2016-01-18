package com.geopista.ui.plugin.importer.beans;

import java.io.Serializable;

public class NodoDominio implements Serializable {


	private String idDomain;
	private String patron;
	private String descripcion;


	public NodoDominio(){

	}
	
	public NodoDominio(String idDomain, String patron, String descripcion){
		this.idDomain = idDomain;
		this.patron = patron;
		this.descripcion = descripcion;
	}

	public String getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(String idDomain) {
		this.idDomain = idDomain;
	}

	public String getPatron() {
		return patron;
	}

	public void setPatron(String patron) {
		this.patron = patron;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
