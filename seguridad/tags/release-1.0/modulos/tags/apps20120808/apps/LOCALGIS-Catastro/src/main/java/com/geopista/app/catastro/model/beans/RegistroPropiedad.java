package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

public class RegistroPropiedad implements Serializable{
	
	/**
	 * Código de la provincia.
	 */
	private String codigoProvincia;
	
	/**
	 * Código del Registro de la propiedad.
	 */
	private String codigoRegistroPropiedad;
	
	/**
	 * Registro de la propiedad = codigoProvincia + codigoRegistroPropiedad
	 */
	private String registroPropiedad;
	
	
	
	
	public RegistroPropiedad(String registroPropiedad) {
		this.registroPropiedad = registroPropiedad;
		this.codigoProvincia = registroPropiedad.substring(0,2);
		this.codigoRegistroPropiedad = registroPropiedad.substring(2,5);
	}
	
	

	public RegistroPropiedad(String codigoProvincia, String codigoRegistroPropiedad) {
		this.codigoProvincia = codigoProvincia;
		this.codigoRegistroPropiedad = codigoRegistroPropiedad;
		this.registroPropiedad = codigoProvincia + codigoRegistroPropiedad;
	}



	/**
	 * Devuelve el registro de la propiedad.
	 * @return String con el registro de la propiedad.
	 */
	public String getRegistroPropiedad() {
		return registroPropiedad;
	}

	/**
	 * Guarda el registro de la propiedad.
	 * @param registroPropiedad String con el registro de la propiedad a cargar.
	 */
	public void setRegistroPropiedad(String registroPropiedad) {
		this.registroPropiedad = registroPropiedad;
	}

	/**
	 * Devuelve el código de la provincia.
	 * @return String con el código de la provincia.
	 */
	public String getCodigoProvincia() {
		return codigoProvincia;
	}

	/**
	 * Guarda el código de la provincia.
	 * @param codigoProvincia  String con el código de la provincia.
	 */
	public void setCodigoProvincia(String codigoProvincia) {
		this.codigoProvincia = codigoProvincia;
	}

	/**
	 * Devuelve el código del registro de la propiedad.
	 * @return String con el código del registro de la propiedad.
	 */
	public String getCodigoRegistroPropiedad() {
		return codigoRegistroPropiedad;
	}

	/**
	 * Guarda el código del registro de la propiedad.
	 * @param codigoRegistroPropiedad String con el registro de la propiedad a cargar.
	 */
	public void setCodigoRegistroPropiedad(String codigoRegistroPropiedad) {
		this.codigoRegistroPropiedad = codigoRegistroPropiedad;
	}
	
	

}
