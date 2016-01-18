package com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CondicionUrbanisticaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -717188513847649497L;

	private String codigoentidad_entidad;
	private String codigodeterminacion_determinacion;
	
	private String codigoTramiteentidad_entidad;
	private String codigoTramitedeterminacion_determinacion;
	
	

	private List<CasoBean> casos = new ArrayList<CasoBean>();
	
	public CondicionUrbanisticaBean() {
		// TODO Auto-generated constructor stub
	}

	public String getCodigoentidad_entidad() {
		return codigoentidad_entidad;
	}

	public void setCodigoentidad_entidad(String codigoentidadEntidad) {
		codigoentidad_entidad = codigoentidadEntidad;
	}

	public String getCodigodeterminacion_determinacion() {
		return codigodeterminacion_determinacion;
	}

	public void setCodigodeterminacion_determinacion(
			String codigodeterminacionDeterminacion) {
		codigodeterminacion_determinacion = codigodeterminacionDeterminacion;
	}

	public List<CasoBean> getCasos() {
		return casos;
	}

	public void setCasos(List<CasoBean> casos) {
		this.casos = casos;
	}

	public String getCodigoTramiteentidad_entidad() {
		return codigoTramiteentidad_entidad;
	}

	public void setCodigoTramiteentidad_entidad(String codigoTramiteentidadEntidad) {
		codigoTramiteentidad_entidad = codigoTramiteentidadEntidad;
	}

	public String getCodigoTramitedeterminacion_determinacion() {
		return codigoTramitedeterminacion_determinacion;
	}

	public void setCodigoTramitedeterminacion_determinacion(
			String codigoTramitedeterminacionDeterminacion) {
		codigoTramitedeterminacion_determinacion = codigoTramitedeterminacionDeterminacion;
	}
}
