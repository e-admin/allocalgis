package com.gestorfip.ws.xml.beans.importacion.migracionasistida;


public class ConfLayerBean {
	
	private String nameLayer;
	private ConfLayerDeterminacionAplicadaBean[] lstConfLayerDeterminacionAplicada;
	private ConfLayerUsosRegulacionesBean[] lstConfUsosRegulaciones;
	private boolean aplicada;
	
	public String getNameLayer() {
		return nameLayer;
	}
	
	public void setNameLayer(String nameLayer) {
		this.nameLayer = nameLayer;
	}
	
	public ConfLayerDeterminacionAplicadaBean[] getLstConfLayerDeterminacionAplicada() {
		return lstConfLayerDeterminacionAplicada;
	}
	
	public void setLstConfLayerDeterminacionAplicada(
			ConfLayerDeterminacionAplicadaBean[] lstConfLayerDeterminacionAplicada) {
		this.lstConfLayerDeterminacionAplicada = lstConfLayerDeterminacionAplicada;
	}
	
	public ConfLayerUsosRegulacionesBean[] getLstConfUsosRegulaciones() {
		return lstConfUsosRegulaciones;
	}
	
	public void setLstConfUsosRegulaciones(
			ConfLayerUsosRegulacionesBean[] lstConfUsosRegulaciones) {
		this.lstConfUsosRegulaciones = lstConfUsosRegulaciones;
	}

	public boolean isAplicada() {
		return aplicada;
	}

	public void setAplicada(boolean aplicada) {
		this.aplicada = aplicada;
	}

}
