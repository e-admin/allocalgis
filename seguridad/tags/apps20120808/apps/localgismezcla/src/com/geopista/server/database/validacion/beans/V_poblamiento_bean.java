package com.geopista.server.database.validacion.beans;

import java.io.Serializable;

public class V_poblamiento_bean implements Serializable{

	   String provincia="-";
	   String municipio="-";
	   String entidad="-";
	   String poblamient="-";
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public String getPoblamient() {
		return poblamient;
	}
	public void setPoblamient(String poblamient) {
		this.poblamient = poblamient;
	}
	
	
}
