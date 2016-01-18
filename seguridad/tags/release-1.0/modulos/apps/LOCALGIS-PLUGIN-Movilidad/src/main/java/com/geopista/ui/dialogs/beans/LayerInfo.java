package com.geopista.ui.dialogs.beans;

public class LayerInfo {
	
	private String name;
	private String systemId;
	private int geometryType;
	
	public LayerInfo() {
		super();
	}
	public LayerInfo(String name, String systemId, int geometryType) {
		super();
		this.name = name;
		this.systemId = systemId;
		this.geometryType = geometryType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public int getGeometryType() {
		return geometryType;
	}
	public void setGeometryType(int geometryType) {
		this.geometryType = geometryType;
	}
}
