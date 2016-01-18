package com.gestorfip.ws.xml.beans.importacion.layers;

import com.gestorfip.ws.xml.beans.importacion.migracionasistida.DeterminacionLayerBean;

public class DeterminacionStyleBean {

	private DeterminacionLayerBean determinacion ;
	private String alias;
	
	public DeterminacionLayerBean getDeterminacion() {
		return determinacion;
	}
	
	public void setDeterminacion(DeterminacionLayerBean determinacion) {
		this.determinacion = determinacion;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias.toLowerCase();
	}
}
