package com.geopista.app.catastro.servicioWebCatastro.beans;

import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.NumeroFincaRegistral;

public class IdentificadorFincaRegistral {
	
	private NumeroFincaRegistral numeroFincaRegistral;
	
	private Expediente expediente;

	public NumeroFincaRegistral getNumeroFincaRegistral() {
		return numeroFincaRegistral;
	}

	public void setNumeroFincaRegistral(NumeroFincaRegistral numeroFincaRegistral) {
		this.numeroFincaRegistral = numeroFincaRegistral;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

}
