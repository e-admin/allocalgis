package com.gestorfip.ws.beans.tramite.ui;


public class TramiteBean {

	
	private TipoTramiteBean tipoTramite;
	private String codigo;
	private String texto;
	private int id;
	private int fip;
	//se utiliza para el nombre del tramite encargado
	private String nombre;
	
	private DeterminacionBean[] lstDeterminacionesBean;
	


	public DeterminacionBean[] getLstDeterminacionesBean() {
		return lstDeterminacionesBean;
	}

	public void setLstDeterminacionesBean(DeterminacionBean[] lstDeterminacionesBean) {
		this.lstDeterminacionesBean = lstDeterminacionesBean;
	}

	public TipoTramiteBean getTipoTramite() {
		return tipoTramite;
	}
	
	public void setTipoTramite(TipoTramiteBean tipoTramite) {
		this.tipoTramite = tipoTramite;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFip() {
		return fip;
	}

	public void setFip(int fip) {
		this.fip = fip;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
