package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class Depuradora1EIEL extends WorkflowEIEL implements Serializable, EIELPanel {
	
	public Depuradora1EIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t1_saneam_ed","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t1_saneam_ed","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t1_saneam_ed","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_ed","eiel_t1_saneam_ed","getCodOrden"));
	}
	
	public Hashtable getIdentifyFields() {		
		Hashtable fields = new Hashtable();		
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("orden_ed", codOrden);		
		return fields;
	}
	
	private String clave = null;
	private String codOrden = null;
	
	private String tratPrimario1 = null;
	private String tratPrimario2 = null;
	private String tratPrimario3 = null;
	
	private String tratSecundario1 = null;
	private String tratSecundario2 = null;
	private String tratSecundario3 = null;
	
	private String tratAvanzado1 = null;
	private String tratAvanzado2 = null;
	private String tratAvanzado3 = null;
	
	private String procComplementario1 = null;
	private String procComplementario2 = null;
	private String procComplementario3 = null;
	
	private String tratLodos1 = null;
	private String tratLodos2 = null;
	private String tratLodos3 = null;
	
	private Date fechaRevision = null;
	private Integer estadoRevision = null;
	
	
	private VersionEiel version = null;
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getTratPrimario1() {
		return tratPrimario1;
	}
	public void setTratPrimario1(String tratPrimario1) {
		this.tratPrimario1 = tratPrimario1;
	}
	public String getTratPrimario2() {
		return tratPrimario2;
	}
	public void setTratPrimario2(String tratPrimario2) {
		this.tratPrimario2 = tratPrimario2;
	}
	public String getTratPrimario3() {
		return tratPrimario3;
	}
	public void setTratPrimario3(String tratPrimario3) {
		this.tratPrimario3 = tratPrimario3;
	}
	public String getTratSecundario1() {
		return tratSecundario1;
	}
	public void setTratSecundario1(String tratSecundario1) {
		this.tratSecundario1 = tratSecundario1;
	}
	public String getTratSecundario2() {
		return tratSecundario2;
	}
	public void setTratSecundario2(String tratSecundario2) {
		this.tratSecundario2 = tratSecundario2;
	}
	public String getTratSecundario3() {
		return tratSecundario3;
	}
	public void setTratSecundario3(String tratSecundario3) {
		this.tratSecundario3 = tratSecundario3;
	}
	public String getTratAvanzado1() {
		return tratAvanzado1;
	}
	public void setTratAvanzado1(String tratAvanzado1) {
		this.tratAvanzado1 = tratAvanzado1;
	}
	public String getTratAvanzado2() {
		return tratAvanzado2;
	}
	public void setTratAvanzado2(String tratAvanzado2) {
		this.tratAvanzado2 = tratAvanzado2;
	}
	public String getTratAvanzado3() {
		return tratAvanzado3;
	}
	public void setTratAvanzado3(String tratAvanzado3) {
		this.tratAvanzado3 = tratAvanzado3;
	}
	public String getProcComplementario1() {
		return procComplementario1;
	}
	public void setProcComplementario1(String procComplementario1) {
		this.procComplementario1 = procComplementario1;
	}
	public String getProcComplementario2() {
		return procComplementario2;
	}
	public void setProcComplementario2(String procComplementario2) {
		this.procComplementario2 = procComplementario2;
	}
	public String getProcComplementario3() {
		return procComplementario3;
	}
	public void setProcComplementario3(String procComplementario3) {
		this.procComplementario3 = procComplementario3;
	}
	public String getTratLodos1() {
		return tratLodos1;
	}
	public void setTratLodos1(String tratLodos1) {
		this.tratLodos1 = tratLodos1;
	}
	public String getTratLodos2() {
		return tratLodos2;
	}
	public void setTratLodos2(String tratLodos2) {
		this.tratLodos2 = tratLodos2;
	}
	public String getTratLodos3() {
		return tratLodos3;
	}
	public void setTratLodos3(String tratLodos3) {
		this.tratLodos3 = tratLodos3;
	}
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}
	public String getCodOrden() {
		return codOrden;
	}
	public void setCodOrden(String codOrden) {
		this.codOrden = codOrden;
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
