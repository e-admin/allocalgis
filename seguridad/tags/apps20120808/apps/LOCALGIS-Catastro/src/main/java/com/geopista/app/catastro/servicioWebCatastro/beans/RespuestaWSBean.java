package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.Expediente;

public class RespuestaWSBean {
	
	private Expediente expediente ;

	private ArrayList<UnidadErrorElementoWSBean> lstUnidadError;
	
	private String estado;
	
	private UDSAWSBean udsa;
	
	
	public UDSAWSBean getUdsa() {
		return udsa;
	}

	public void setUdsa(UDSAWSBean udsa) {
		this.udsa = udsa;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	public ArrayList<UnidadErrorElementoWSBean> getLstUnidadError() {
		return lstUnidadError;
	}

	public void setLstUnidadError(ArrayList<UnidadErrorElementoWSBean> lstUnidadError) {
		this.lstUnidadError = lstUnidadError;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

}
