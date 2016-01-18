package com.localgis.app.gestionciudad.beans;

public class LayerFeatureBean {
	private Integer idLayer;
	private Integer idFeature;
	public LayerFeatureBean(){
		super();
	}
	public LayerFeatureBean(Integer idLayer,Integer idFeature){
		this.idFeature = idFeature;
		this.idLayer = idLayer;
	}
	public Integer getIdLayer() {
		return idLayer;
	}
	public void setIdLayer(Integer idLayer) {
		this.idLayer = idLayer;
	}
	public Integer getIdFeature() {
		return idFeature;
	}
	public void setIdFeature(Integer idFeature) {
		this.idFeature = idFeature;
	}
}
