package com.geopista.app.catastro.servicioWebCatastro;

import com.geopista.app.catastro.model.beans.Derecho;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Persona;

public class IdentificadorDerecho {
	
	private Expediente expediente ;

	
	private Persona representante;
	
	private Derecho derecho;
	
	private String codigoDelegacion;
	
	private String codigoMunicipio;

	public String getCodigoDelegacion() {
		return codigoDelegacion;
	}

	public void setCodigoDelegacion(String codigoDelegacion) {
		this.codigoDelegacion = codigoDelegacion;
	}

	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Persona getRepresentante() {
		return representante;
	}

	public void setRepresentante(Persona representante) {
		this.representante = representante;
	}

	public Derecho getDerecho() {
		return derecho;
	}

	public void setDerecho(Derecho derecho) {
		this.derecho = derecho;
	}


}
