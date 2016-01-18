package com.gestorfip.ws.xml.beans.importacion.layers;



public class EdificabilidadStyleBean {

	private EdificabilidadBean[] lstAliasValorEdificabilidad;	
	private String nameColumnEdificabilidad;
	private DeterminacionStyleBean detEdificabilidad;

	
	public DeterminacionStyleBean getDetEdificabilidad() {
		return detEdificabilidad;
	}

	public void setDetEdificabilidad(DeterminacionStyleBean detEdificabilidad) {
		this.detEdificabilidad = detEdificabilidad;
	}

	
	public String getNameColumnEdificabilidad() {
		return nameColumnEdificabilidad;
	}
	
	public void setNameColumnEdificabilidad(String nameColumnEdificabilidad) {
		this.nameColumnEdificabilidad = nameColumnEdificabilidad;
	}
	
	public EdificabilidadBean[] getLstAliasValorEdificabilidad() {
		return lstAliasValorEdificabilidad;
	}

	public void setLstAliasValorEdificabilidad(
			EdificabilidadBean[] lstAliasValorEdificabilidad) {
		this.lstAliasValorEdificabilidad = lstAliasValorEdificabilidad;
	}
}
