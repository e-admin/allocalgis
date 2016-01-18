package com.gestorfip.ws.xml.beans.importacion.layers;



public class EdificabilidadConfigBean {
	
	private EdificabilidadBean[] lstEdificabilidadZonaBean = null;
	private EdificabilidadBean[] lstEdificabilidadGestionBean = null;
	private String nameColumnEdificabilidad = null;
	private DeterminacionStyleBean detEdificabilidad= null;
	
	public String getNameColumnEdificabilidad() {
		return nameColumnEdificabilidad;
	}

	public void setNameColumnEdificabilidad(String nameColumnEdificabilidad) {
		this.nameColumnEdificabilidad = nameColumnEdificabilidad;
	}

	public DeterminacionStyleBean getDetEdificabilidad() {
		return detEdificabilidad;
	}

	public void setDetEdificabilidad(DeterminacionStyleBean detEdificabilidad) {
		this.detEdificabilidad = detEdificabilidad;
	}

	public EdificabilidadBean[] getLstEdificabilidadZonaBean() {
		return lstEdificabilidadZonaBean;
	}

	public void setLstEdificabilidadZonaBean(
			EdificabilidadBean[] lstEdificabilidadZonaBean) {
		this.lstEdificabilidadZonaBean = lstEdificabilidadZonaBean;
	}

	public EdificabilidadBean[] getLstEdificabilidadGestionBean() {
		return lstEdificabilidadGestionBean;
	}

	public void setLstEdificabilidadGestionBean(
			EdificabilidadBean[] lstEdificabilidadGestionBean) {
		this.lstEdificabilidadGestionBean = lstEdificabilidadGestionBean;
	}


	

}
