package com.gestorfip.ws.beans.tramite.ui;




public class FipBean {

	  private int id; 
	  private String pais; 
	  private String version; 
	  private String srs; 
	  private String fecha;
	  private String fechaConsolidacion;
	  private int idAmbito; 
	  
	  private String municipio;


	private TramiteBean tramiteCatalogoSistematizado;
	  private TramiteBean tramitePlaneamientoEncargado;
	  private TramiteBean tramitePlaneamientoVigente;

	private DeterminacionBean[] lstDeterminacionesBean;
	  
	

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getPais() {
		return pais;
	}
	
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getSrs() {
		return srs;
	}
	
	public void setSrs(String srs) {
		this.srs = srs;
	}
	
	public TramiteBean getTramiteCatalogoSistematizado() {
		return tramiteCatalogoSistematizado;
	}
	
	public void setTramiteCatalogoSistematizado(
			TramiteBean tramiteCatalogoSistematizado) {
		this.tramiteCatalogoSistematizado = tramiteCatalogoSistematizado;
	}
	
	public TramiteBean getTramitePlaneamientoEncargado() {
		return tramitePlaneamientoEncargado;
	}
	
	public void setTramitePlaneamientoEncargado(
			TramiteBean tramitePlaneamientoEncargado) {
		this.tramitePlaneamientoEncargado = tramitePlaneamientoEncargado;
	}

	public DeterminacionBean[] getLstDeterminacionesBean() {
		return lstDeterminacionesBean;
	}

	public void setLstDeterminacionesBean(DeterminacionBean[] lstDeterminacionesBean) {
		this.lstDeterminacionesBean = lstDeterminacionesBean;
	}
	
	  
	 public TramiteBean getTramitePlaneamientoVigente() {
		return tramitePlaneamientoVigente;
	}

	public void setTramitePlaneamientoVigente(TramiteBean tramitePlaneamientoVigente) {
		this.tramitePlaneamientoVigente = tramitePlaneamientoVigente;
	}
	
	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String getFechaConsolidacion() {
		return fechaConsolidacion;
	}

	public void setFechaConsolidacion(String fechaConsolidacion) {
		this.fechaConsolidacion = fechaConsolidacion;
	}

	public int getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(int idAmbito) {
		this.idAmbito = idAmbito;
	}
	
	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
}
