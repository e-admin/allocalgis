package com.gestorfip.ws.xml.beans.importacion.migracionasistida;

public class ConfLayerValorReferenciaBean {

	private String tramite;
	private DeterminacionLayerBean determinacionLayer;
	private String alias;
	private boolean selected;
	
	public String getTramite() {
		return tramite;
	}
	
	public void setTramite(String tramite) {
		this.tramite = tramite;
	}
	
	public DeterminacionLayerBean getDeterminacionLayer() {
		return determinacionLayer;
	}
	
	public void setDeterminacionLayer(DeterminacionLayerBean determinacionLayer) {
		this.determinacionLayer = determinacionLayer;
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
	
	
}
