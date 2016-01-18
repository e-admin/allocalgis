package com.geopista.protocol.cementerios;

import java.io.Serializable;

public class ErrorBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String superPatron;
	private String patron;
	
	private String message;
	private String title;

	/******* Métodos Get y set *******/
	
	public String getSuperPatron() {
		return superPatron;
	}

	public void setSuperPatron(String superPatron) {
		this.superPatron = superPatron;
	}

	public String getPatron() {
		return patron;
	}

	public void setPatron(String patron) {
		this.patron = patron;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
