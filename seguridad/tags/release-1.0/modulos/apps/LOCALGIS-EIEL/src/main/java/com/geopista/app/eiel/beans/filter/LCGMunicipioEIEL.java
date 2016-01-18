package com.geopista.app.eiel.beans.filter;

import java.io.Serializable;

public class LCGMunicipioEIEL implements Serializable{

	private int idMunicipio;
	private String nombreOficial;
	
	
	public LCGMunicipioEIEL(int idMunicipio, String nombreOficial) {
		super();
		this.idMunicipio = idMunicipio;
		this.nombreOficial = nombreOficial;
	}
	public LCGMunicipioEIEL() {
		// TODO Auto-generated constructor stub
	}
	public int getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(int idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getNombreOficial() {
		return nombreOficial;
	}
	public void setNombreOficial(String nombreOficial) {
		this.nombreOficial = nombreOficial;
	}

	@Override
	public String toString() {
		return "LCGMunicipioEIEL [idMunicipio=" + idMunicipio
				+ ", nombreOficial=" + nombreOficial + "]";
	}

}
