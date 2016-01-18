package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que implementa un objeto de tipo intervencion 
 */
public class IntervencionBean extends ElemFeatureBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String informe;
    private Date fechaInicio;
    private Date fechaFin;
    private String localizacion;
    private String codigo;
    private String estado;
    private int id_intervencion = -1;
    
    
	public int getId_intervencion() {
		return id_intervencion;
	}
	public void setId_intervencion(int id_intervencion) {
		this.id_intervencion = id_intervencion;
	}
	public String getInforme() {
		return informe;
	}
	public void setInforme(String informe) {
		this.informe = informe;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

}
