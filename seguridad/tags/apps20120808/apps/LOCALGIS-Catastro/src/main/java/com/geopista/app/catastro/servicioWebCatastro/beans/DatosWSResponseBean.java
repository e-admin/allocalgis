package com.geopista.app.catastro.servicioWebCatastro.beans;

public class DatosWSResponseBean {
	
	public  static final String ESTADO_OK = "OK";
	
	private ControlWSBean control;
	
	private RespuestaWSBean respuesta;

	public RespuestaWSBean getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(RespuestaWSBean respuesta) {
		this.respuesta = respuesta;
	}

	public ControlWSBean getControl() {
		return control;
	}

	public void setControl(ControlWSBean control) {
		this.control = control;
	}

}
