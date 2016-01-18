package com.gestorfip.ws.xml.beans.importacion.layers;



public class LayerStylesBean {
	
	private DeterminacionStyleBean[] lstUsos;
	private DeterminacionStyleBean[] lstRegulaciones;
	private EdificabilidadStyleBean edificabilidad;
	private String nameLayer;
	
	public String getNameLayer() {
		return nameLayer;
	}

	public void setNameLayer(String nameLayer) {
		this.nameLayer = nameLayer;
	}

	public DeterminacionStyleBean[] getLstUsos() {
		return lstUsos;
	}
	
	public void setLstUsos(DeterminacionStyleBean[] lstUsos) {
		this.lstUsos = lstUsos;
	}
	
	public DeterminacionStyleBean[] getLstRegulaciones() {
		return lstRegulaciones;
	}
	
	public void setLstRegulaciones(DeterminacionStyleBean[] lstRegulaciones) {
		this.lstRegulaciones = lstRegulaciones;
	}

	public EdificabilidadStyleBean getEdificabilidad() {
		return edificabilidad;
	}

	public void setEdificabilidad(EdificabilidadStyleBean edificabilidad) {
		this.edificabilidad = edificabilidad;
	}

}
