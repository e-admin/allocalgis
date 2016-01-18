package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class CabildoConsejoEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public CabildoConsejoEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_cabildo_consejo","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Isla","cod_isla","eiel_t_cabildo_consejo","getCodIsla"));
	}	

	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("cod_isla", codIsla);
		return fields;
	}

	private String codINEProvincia = null;
	private String codIsla = null;
	private String denominacion = null;

	
	private VersionEiel version = null;
	
	
	public String getCodIsla() {
		return codIsla;
	}
	public void setCodIsla(String codIsla) {
		this.codIsla = codIsla;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(VersionEiel version) {
		this.version = version;
	}
	/**
	 * @return the version
	 */
	public VersionEiel getVersion() {
		return version;
	}
	
	
	
}
