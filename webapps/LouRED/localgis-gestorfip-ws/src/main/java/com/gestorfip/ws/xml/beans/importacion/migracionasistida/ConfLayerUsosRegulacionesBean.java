package com.gestorfip.ws.xml.beans.importacion.migracionasistida;

public class ConfLayerUsosRegulacionesBean {

	private String alias;
	private DeterminacionLayerBean determinacionUso = null;
	private ConfRegulacionesBean[] lstRegulaciones;
	private boolean selected;
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public DeterminacionLayerBean getDeterminacionUso() {
		return determinacionUso;
	}
	
	public void setDeterminacionUso(DeterminacionLayerBean determinacionUso) {
		this.determinacionUso = determinacionUso;
	}
	
	public ConfRegulacionesBean[] getLstRegulaciones() {
		return lstRegulaciones;
	}
	
	public void setLstRegulaciones(ConfRegulacionesBean[] lstRegulaciones) {
		this.lstRegulaciones = lstRegulaciones;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	

}
