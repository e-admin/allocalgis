package com.gestorfip.ws.xml.beans.importacion.migracionasistida;


public class ConfLayerDeterminacionAplicadaBean {

	private String aliasDeterminacion;
	private DeterminacionLayerBean determinacionLayer;
	private ConfLayerValorBean[] lstValores;
	private ConfLayerValorReferenciaBean[] lstValoresReferencia;
	private ConfLayerRegimenEspecificoBean[] lstRegimenesEspecificos;
	
	boolean selected;
	
	public String getAliasDeterminacion() {
		return aliasDeterminacion;
	}
	
	public void setAliasDeterminacion(String aliasDeterminacion) {
		this.aliasDeterminacion = aliasDeterminacion;
	}
	
	public DeterminacionLayerBean getDeterminacionLayer() {
		return determinacionLayer;
	}
	
	public void setDeterminacionLayer(DeterminacionLayerBean determinacionLayer) {
		this.determinacionLayer = determinacionLayer;
	}
	
	public ConfLayerValorBean[] getLstValores() {
		return lstValores;
	}
	
	public void setLstValores(ConfLayerValorBean[] lstValores) {
		this.lstValores = lstValores;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public ConfLayerValorReferenciaBean[] getLstValoresReferencia() {
		return lstValoresReferencia;
	}

	public void setLstValoresReferencia(
			ConfLayerValorReferenciaBean[] lstValoresReferencia) {
		this.lstValoresReferencia = lstValoresReferencia;
	}

	public ConfLayerRegimenEspecificoBean[] getLstRegimenesEspecificos() {
		return lstRegimenesEspecificos;
	}

	public void setLstRegimenesEspecificos(
			ConfLayerRegimenEspecificoBean[] lstRegimenesEspecificos) {
		this.lstRegimenesEspecificos = lstRegimenesEspecificos;
	}

	
}
