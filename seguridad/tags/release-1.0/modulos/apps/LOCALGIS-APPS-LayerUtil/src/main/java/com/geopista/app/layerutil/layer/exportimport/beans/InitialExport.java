package com.geopista.app.layerutil.layer.exportimport.beans;

import java.util.List;

public class InitialExport {

	private List<String> Tables;
	private List<String> Domains;
	private List<String> Layers;
	
	public List<String> getTables() {
		return Tables;
	}
	
	public void setTables(List<String> tables) {
		Tables = tables;
	}
	
	public List<String> getDomains() {
		return Domains;
	}
	
	public void setDomains(List<String> domains) {
		Domains = domains;
	}
	
	public List<String> getLayers() {
		return Layers;
	}
	
	public void setLayers(List<String> layers) {
		Layers = layers;
	}	
}
