package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.Expediente;

public class UDSAWSBean {
	
	private Expediente expediente = new Expediente();
	private ArrayList lstUnidadDatosIntercambio;

	public ArrayList getLstUnidadDatosIntercambio() {
		return lstUnidadDatosIntercambio;
	}

	public void setLstUnidadDatosIntercambio(ArrayList lstUnidadDatosIntercambio) {
		this.lstUnidadDatosIntercambio = lstUnidadDatosIntercambio;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
	
	

}
