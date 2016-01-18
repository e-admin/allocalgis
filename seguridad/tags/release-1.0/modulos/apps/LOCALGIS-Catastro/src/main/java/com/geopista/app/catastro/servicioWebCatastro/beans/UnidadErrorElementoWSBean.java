package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.util.ArrayList;


public class UnidadErrorElementoWSBean {

	private IdentificadorWSBean identificador;
	
	private ArrayList <ErrorWSBean>lstErrores;

	public IdentificadorWSBean getIdentificador() {
		return identificador;
	}

	public void setIdentificador(IdentificadorWSBean identificador) {
		this.identificador = identificador;
	}

	public ArrayList getLstErrores() {
		return lstErrores;
	}

	public void setLstErrores(ArrayList <ErrorWSBean> lstErrores) {
		this.lstErrores = lstErrores;
	}
}
