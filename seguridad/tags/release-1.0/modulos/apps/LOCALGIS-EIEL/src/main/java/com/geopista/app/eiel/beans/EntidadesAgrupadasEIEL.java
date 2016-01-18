package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class EntidadesAgrupadasEIEL extends WorkflowEIEL implements Serializable, EIELPanel{

	public EntidadesAgrupadasEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Entidad","codEntidad","eiel_t_entidades_agrupadas","getCodEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Poblamiento","codNucleo","eiel_t_entidades_agrupadas","getCodNucleo"));
	}	
			
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codEntidad", codEntidad);
		fields.put("codNucleo", codNucleo);
		return fields;
	}
	private String codEntidad = null;
	private String codNucleo = null;
	private String codEntidad_agrupada = null;
	private String codNucleo_agrupado=null;
	private VersionEiel version = null;
	

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

	public String getCodEntidad() {
		return codEntidad;
	}

	public void setCodINEEntidad(String codEntidad) {
		this.codEntidad = codEntidad;
	}

	public String getCodNucleo() {
		return codNucleo;
	}

	public void setCodINENucleo(String codNucleo) {
		this.codNucleo = codNucleo;
	}

	public String getCodEntidad_agrupada() {
		return codEntidad_agrupada;
	}

	public void setCodINEEntidad_agrupada(String codEntidad_agrupada) {
		this.codEntidad_agrupada = codEntidad_agrupada;
	}

	public String getCodNucleo_agrupado() {
		return codNucleo_agrupado;
	}

	public void setCodINENucleo_agrupado(String codNucleo_agrupado) {
		this.codNucleo_agrupado = codNucleo_agrupado;
	}


}
