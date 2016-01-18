package com.gestorfip.ws.xml.beans.importacion.layers;

import com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica.CondicionUrbanisticaBean;

public class RegulaConfBean {
	
	private String valorReferenciaDeterminacion;
	private String valorReferenciaTramite;
	private CondicionUrbanisticaBean cu;
	
	public String getValorReferenciaTramite() {
		return valorReferenciaTramite;
	}

	public void setValorReferenciaTramite(String valorReferenciaTramite) {
		this.valorReferenciaTramite = valorReferenciaTramite;
	}

	
	
	public String getValorReferenciaDeterminacion() {
		return valorReferenciaDeterminacion;
	}
	
	public void setValorReferenciaDeterminacion(String valorReferenciaDeterminacion) {
		this.valorReferenciaDeterminacion = valorReferenciaDeterminacion;
	}
	
	public CondicionUrbanisticaBean getCu() {
		return cu;
	}
	
	public void setCu(CondicionUrbanisticaBean cu) {
		this.cu = cu;
	}

}
