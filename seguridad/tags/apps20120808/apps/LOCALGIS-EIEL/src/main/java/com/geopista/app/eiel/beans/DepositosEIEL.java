package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class DepositosEIEL extends WorkflowEIEL implements Serializable, EIELPanel{

	public DepositosEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_abast_de","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_abast_de","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_abast_de","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_de","eiel_t_abast_de","getOrdenDeposito"));
	}

	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);		
		fields.put("orden_de", ordenDeposito);		
		return fields;
	}
	
	private String clave = null;
	private String ordenDeposito = null;
	
	private Integer capacidad = null;
	private Integer estadoRevision = null;
	
	private String ubicacion = null;
	private String titularidad = null;
	private String gestor = null;
	private String estado = null;
	private String proteccion = null;
	private String fechaLimpieza = null;
	private String contador = null;
	private String observaciones = null;

	private Date fechaRevision = null;
	private Date fechaInstalacion = null;
	

	private VersionEiel version = null;
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}


	public String getOrdenDeposito() {
		return ordenDeposito;
	}

	public void setOrdenDeposito(String ordenDeposito) {
		this.ordenDeposito = ordenDeposito;
	}

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public Integer getEstadoRevision() {
		return estadoRevision;
	}

	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getTitularidad() {
		return titularidad;
	}

	public void setTitularidad(String titularidad) {
		this.titularidad = titularidad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getProteccion() {
		return proteccion;
	}

	public void setProteccion(String proteccion) {
		this.proteccion = proteccion;
	}

	public String getFechaLimpieza() {
		return fechaLimpieza;
	}

	public void setFechaLimpieza(String fechaLimpieza) {
		this.fechaLimpieza = fechaLimpieza;
	}

	public String getContador() {
		return contador;
	}

	public void setContador(String contador) {
		this.contador = contador;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public Date getFechaInstalacion() {
		return fechaInstalacion;
	}

	public void setFechaInstalacion(Date fechaInstalacion) {
		this.fechaInstalacion = fechaInstalacion;
	}


	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(VersionEiel version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public VersionEiel getVersion() {
		return version;
	}
		
	
	
}
