package com.gestorfip.ws.xml.beans.importacion.layers;


public class LayerConfigBean {
	
	private UsosConfigBean usosConfig = null;
	private RegulacionesConfigBean regulacionesConfig = null;
	private EdificabilidadConfigBean edificabilidadConfig = null;

	public EdificabilidadConfigBean getEdificabilidadConfig() {
		return edificabilidadConfig;
	}

	public void setEdificabilidadConfig(
			EdificabilidadConfigBean edificabilidadConfig) {
		this.edificabilidadConfig = edificabilidadConfig;
	}

	public RegulacionesConfigBean getRegulacionesConfig() {
		return regulacionesConfig;
	}

	public void setRegulacionesConfig(RegulacionesConfigBean regulacionesConfig) {
		this.regulacionesConfig = regulacionesConfig;
	}

	public UsosConfigBean getUsosConfig() {
		return usosConfig;
	}

	public void setUsosConfig(UsosConfigBean usosConfig) {
		this.usosConfig = usosConfig;
	}
	
}
