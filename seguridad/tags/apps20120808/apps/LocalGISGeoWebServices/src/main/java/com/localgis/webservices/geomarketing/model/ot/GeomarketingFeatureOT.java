package com.localgis.webservices.geomarketing.model.ot;

public class GeomarketingFeatureOT {
	private String wktGeometry;
	private Integer srid;
	public Integer getSrid() {
		return srid;
	}
	public void setSrid(Integer srid) {
		this.srid = srid;
	}
	private String attributeName;
	private String municipio;
	private Integer[] id;
	public String getWktGeometry() {
		return wktGeometry;
	}
	public void setWktGeometry(String wktGeometry) {
		this.wktGeometry = wktGeometry;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public void setMunicipio(String int1) {
		this.municipio = int1;
		
	}
	public String getMunicipio(){
		return this.municipio;
	}
	public Integer[] getId() {
		return this.id;
	}
	public void setId(Integer[] id){
		this.id = id;
	}
}
