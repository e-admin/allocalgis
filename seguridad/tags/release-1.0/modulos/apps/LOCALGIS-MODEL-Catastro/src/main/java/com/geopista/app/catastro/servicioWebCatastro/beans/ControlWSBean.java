package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.EntidadGeneradora;

public class ControlWSBean {

	
	private EntidadGeneradora entidadGeneradora;
	
	private InformacionWSFichero informacionFichero;
	
	private String codigoEnvio;
	
	private ArrayList lstIdentificadoresDialogo;

	public ArrayList getLstIdentificadoresDialogo() {
		return lstIdentificadoresDialogo;
	}

	public void setLstIdentificadoresDialogo(ArrayList liddf) {
		this.lstIdentificadoresDialogo = liddf;
	}

	public InformacionWSFichero getInformacionFichero() {
		return informacionFichero;
	}

	public void setInformacionFichero(InformacionWSFichero informacionFichero) {
		this.informacionFichero = informacionFichero;
	}

	public String getCodigoEnvio() {
		return codigoEnvio;
	}

	public void setCodigoEnvio(String codigoEnvio) {
		this.codigoEnvio = codigoEnvio;
	}

	public EntidadGeneradora getEntidadGeneradora() {
		return entidadGeneradora;
	}

	public void setEntidadGeneradora(EntidadGeneradora entidadGeneradora) {
		this.entidadGeneradora = entidadGeneradora;
	}
}
