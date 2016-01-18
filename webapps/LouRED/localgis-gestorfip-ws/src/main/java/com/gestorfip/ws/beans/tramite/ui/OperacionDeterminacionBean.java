package com.gestorfip.ws.beans.tramite.ui;


public class OperacionDeterminacionBean {
	
	  private int id; 
	  private int  tipo;
	  private long orden;
	  private String  texto;
	  private int operadora_determinacionid;
	  private DeterminacionBean operada_determinacion;
	  private int tramite;
	  
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	
	public long getOrden() {
		return orden;
	}
	
	public void setOrden(long orden) {
		this.orden = orden;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getOperadora_determinacionid() {
		return operadora_determinacionid;
	}
	
	public void setOperadora_determinacionid(int operadoraDeterminacionid) {
		operadora_determinacionid = operadoraDeterminacionid;
	}
	
	public DeterminacionBean getOperada_determinacion() {
		return operada_determinacion;
	}
	
	public void setOperada_determinacion(DeterminacionBean operadaDeterminacion) {
		operada_determinacion = operadaDeterminacion;
	}
	
	public int getTramite() {
		return tramite;
	}
	
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}
	
	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

}
