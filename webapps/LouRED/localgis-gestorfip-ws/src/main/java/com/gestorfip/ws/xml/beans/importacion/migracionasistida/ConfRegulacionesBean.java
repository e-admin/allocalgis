package com.gestorfip.ws.xml.beans.importacion.migracionasistida;

public class ConfRegulacionesBean {

	private DeterminacionLayerBean regulacion;
	private DeterminacionLayerBean regulacionValor;
	private String alias;
	private boolean selected;
	
	public DeterminacionLayerBean getRegulacion() {
		return regulacion;
	}
	
	public void setRegulacion(DeterminacionLayerBean regulacion) {
		this.regulacion = regulacion;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public DeterminacionLayerBean getRegulacionValor() {
		return regulacionValor;
	}

	public void setRegulacionValor(DeterminacionLayerBean regulacionValor) {
		this.regulacionValor = regulacionValor;
	}
	
	
	

}
