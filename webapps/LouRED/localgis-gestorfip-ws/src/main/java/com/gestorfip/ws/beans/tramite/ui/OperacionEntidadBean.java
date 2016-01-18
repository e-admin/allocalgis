package com.gestorfip.ws.beans.tramite.ui;


public class OperacionEntidadBean {
	
	  private int id; 
	  private int tipo; 
	  private long orden;
	  private String texto;
	  private int operadora_entidadid; 
	  private EntidadBean operada_entidad;
	  private double propiedadesadscripcion_cuantia;
	  private String propiedadesadscripcion_texto;
	  private int propiedadesadscripcion_unidad_determinacionid;
	  private int propiedadesadscripcion_tipo_determinacionid;
	  private int tramite;
	  
	public int getTramite() {
		return tramite;
	}

	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
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
	
	public int getOperadora_entidadid() {
		return operadora_entidadid;
	}
	
	public void setOperadora_entidadid(int operadoraEntidadid) {
		operadora_entidadid = operadoraEntidadid;
	}
	
	public EntidadBean getOperada_entidad() {
		return operada_entidad;
	}
	
	public void setOperada_entidad(EntidadBean operadaEntidad) {
		operada_entidad = operadaEntidad;
	}
	
	public double getPropiedadesadscripcion_cuantia() {
		return propiedadesadscripcion_cuantia;
	}
	
	public void setPropiedadesadscripcion_cuantia(
			double propiedadesadscripcionCuantia) {
		propiedadesadscripcion_cuantia = propiedadesadscripcionCuantia;
	}
	
	public String getPropiedadesadscripcion_texto() {
		return propiedadesadscripcion_texto;
	}
	
	public void setPropiedadesadscripcion_texto(String propiedadesadscripcionTexto) {
		propiedadesadscripcion_texto = propiedadesadscripcionTexto;
	}
	
	public int getPropiedadesadscripcion_unidad_determinacionid() {
		return propiedadesadscripcion_unidad_determinacionid;
	}
	
	public void setPropiedadesadscripcion_unidad_determinacionid(
			int propiedadesadscripcionUnidadDeterminacionid) {
		propiedadesadscripcion_unidad_determinacionid = propiedadesadscripcionUnidadDeterminacionid;
	}
	
	public int getPropiedadesadscripcion_tipo_determinacionid() {
		return propiedadesadscripcion_tipo_determinacionid;
	}
	
	public void setPropiedadesadscripcion_tipo_determinacionid(
			int propiedadesadscripcionTipoDeterminacionid) {
		propiedadesadscripcion_tipo_determinacionid = propiedadesadscripcionTipoDeterminacionid;
	}

}
