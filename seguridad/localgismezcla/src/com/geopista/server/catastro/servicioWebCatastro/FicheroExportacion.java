package com.geopista.server.catastro.servicioWebCatastro;

import java.io.Serializable;
import java.util.ArrayList;

public class FicheroExportacion implements Serializable{

	private String ficheroFinEntrada = null;
	
	private String ficheroVarpad = null;
	
	private ArrayList lstFxcc = null;
	

	public String getFicheroFinEntrada() {
		return ficheroFinEntrada;
	}

	public void setFicheroFinEntrada(String ficheroFinEntrada) {
		this.ficheroFinEntrada = ficheroFinEntrada;
	}

	public String getFicheroVarpad() {
		return ficheroVarpad;
	}

	public void setFicheroVarpad(String ficheroVarpad) {
		this.ficheroVarpad = ficheroVarpad;
	}

	public ArrayList getLstFxcc() {
		return lstFxcc;
	}

	public void setLstFxcc(ArrayList lstFxcc) {
		this.lstFxcc = lstFxcc;
	}
	
}
