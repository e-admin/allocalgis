package com.geopista.app.catastro.model.beans;

import java.util.Date;


public class CreacionExpedientesResponse {

	private Expediente expediente;
	
	private EntidadGeneradora entidadGeneradora;
	
	/**
     * Código de la entidad generadora.
     */
    private int codigoEntidadGeneradora;
    
	private Date fechaGeneracion;

	private Date horaGeneracion;
	
	private String tipo;
	
	private int codigoEnvio;
	
	
	
	public int getCodigoEnvio() {
		return codigoEnvio;
	}

	public void setCodigoEnvio(int codigoEnvio) {
		this.codigoEnvio = codigoEnvio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	
	public Date getHoraGeneracion() {
		return horaGeneracion;
	}
	
	public void setHoraGeneracion(Date horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}
	public int getCodigoEntidadGeneradora() {
		return codigoEntidadGeneradora;
	}
	
	public void setCodigoEntidadGeneradora(int codigoEntidadGeneradora) {
		this.codigoEntidadGeneradora = codigoEntidadGeneradora;
	}
	
	public Expediente getExpediente() {
		return expediente;
	}
	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
	
	public EntidadGeneradora getEntidadGeneradora() {
		return entidadGeneradora;
	}
	public void setEntidadGeneradora(EntidadGeneradora entidadGeneradora) {
		this.entidadGeneradora = entidadGeneradora;
	}

	
}
