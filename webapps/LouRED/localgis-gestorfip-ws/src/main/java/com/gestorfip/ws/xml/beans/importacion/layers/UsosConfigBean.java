package com.gestorfip.ws.xml.beans.importacion.layers;

import com.gestorfip.ws.xml.beans.importacion.migracionasistida.DeterminacionLayerBean;


public class UsosConfigBean {
	
	private DeterminacionLayerBean[] lstDeterminacionesUsosZonaBean = null;
	private DeterminacionLayerBean[] lstDeterminacionesUsosGestionBean = null;
	
	
	public DeterminacionLayerBean[] getLstDeterminacionesUsosGestionBean() {
		return lstDeterminacionesUsosGestionBean;
	}

	public void setLstDeterminacionesUsosGestionBean(
			DeterminacionLayerBean[] lstDeterminacionesUsosGestionBean) {
		this.lstDeterminacionesUsosGestionBean = lstDeterminacionesUsosGestionBean;
	}

	public DeterminacionLayerBean[] getLstDeterminacionesUsosZonaBean() {
		return lstDeterminacionesUsosZonaBean;
	}

	public void setLstDeterminacionesUsosZonaBean(
			DeterminacionLayerBean[] lstDeterminacionesUsosZonaBean) {
		this.lstDeterminacionesUsosZonaBean = lstDeterminacionesUsosZonaBean;
	}

}
