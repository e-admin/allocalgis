package com.geopista.app.alptolocalgis.beans;

import java.io.Serializable;
import java.util.Date;

public class OperacionAlp implements Serializable {

	private Integer idOperacion = null;
	private String xml = null;
	private String tipoOperacion = null;
	private Date fechaOperacion = null;
	
	public Integer getIdOperacion() {
		return idOperacion;
	}
	public void setIdOperacion(Integer idOperacion) {
		this.idOperacion = idOperacion;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	
}