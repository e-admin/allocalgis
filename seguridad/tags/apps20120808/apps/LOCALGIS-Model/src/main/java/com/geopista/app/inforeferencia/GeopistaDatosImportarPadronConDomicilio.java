package com.geopista.app.inforeferencia;

import java.util.ArrayList;

import com.geopista.app.catastro.GeopistaDatosImportarPadron;

public class GeopistaDatosImportarPadronConDomicilio {
	private ArrayList<Integer> idDomicilio;
	private GeopistaDatosImportarPadron data;
	public ArrayList<Integer> getIdDomicilio() {
		return idDomicilio;
	}
	public void setIdDomicilio(ArrayList<Integer> idDomicilio) {
		this.idDomicilio = idDomicilio; 
	} 
	public GeopistaDatosImportarPadron getData() {
		return data;
	}
	public void setData(GeopistaDatosImportarPadron data) {
		this.data = data;
	}
	public void addIdDomicilio(Integer idDomicilio){
		if(this.idDomicilio == null)
			this.idDomicilio = new ArrayList<Integer>();
		if(!this.idDomicilio.contains(idDomicilio))
			this.idDomicilio.add(idDomicilio);
	}
	
}
