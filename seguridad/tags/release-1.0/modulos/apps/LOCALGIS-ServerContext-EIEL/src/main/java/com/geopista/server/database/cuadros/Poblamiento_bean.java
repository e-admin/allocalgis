package com.geopista.server.database.cuadros;

import java.io.Serializable;

public class Poblamiento_bean implements Serializable{

	  private String provincia="";
	  private String municipio="";
	  private String entidad="";
	  private String poblamient="";
	  private String denominacion= "";
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
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
