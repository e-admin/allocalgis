package com.gestorfip.ws.xml.beans.importacion.layers;

import com.gestorfip.ws.xml.beans.importacion.migracionasistida.DeterminacionLayerBean;

public class RegulacionesConfigBean {
	
	private DeterminacionLayerBean[] lstDeterminacionesRegulacionesZonaBean = null;
	private DeterminacionLayerBean[] lstDeterminacionesRegulacionesdGestionBean = null;
	
	
	public DeterminacionLayerBean[] getLstDeterminacionesRegulacionesGestionBean() {
		return lstDeterminacionesRegulacionesdGestionBean;
	}

	public void setLstDeterminacionesRegulacionesGestionBean(
			DeterminacionLayerBean[] lstDeterminacionesRegulacionesGestionBean) {
		this.lstDeterminacionesRegulacionesdGestionBean = lstDeterminacionesRegulacionesGestionBean;
	}

	public DeterminacionLayerBean[] getLstDeterminacionesRegulacionesZonaBean() {
		return lstDeterminacionesRegulacionesZonaBean;
	}

	public void setLstDeterminacionesRegulacionesZonaBean(
			DeterminacionLayerBean[] lstDeterminacionesRegulacionesZonaBean) {
		this.lstDeterminacionesRegulacionesZonaBean = lstDeterminacionesRegulacionesZonaBean;
	}

}
