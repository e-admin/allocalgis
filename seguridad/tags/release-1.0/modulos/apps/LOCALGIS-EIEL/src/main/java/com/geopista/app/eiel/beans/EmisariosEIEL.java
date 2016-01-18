package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class EmisariosEIEL extends WorkflowEIEL implements Serializable,EIELPanel {
	
	public EmisariosEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_saneam_tem","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_saneam_tem","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_saneam_tem","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Tramo","tramo_em","eiel_t_saneam_tem","getCodOrden"));
		//relacionFields.add(new LCGCampoCapaTablaEIEL("Pmi","pmi","eiel_t_saneam_tem","getPMI"));
		//relacionFields.add(new LCGCampoCapaTablaEIEL("Pmf","pmf","eiel_t_saneam_tem","getPMF"));
		//HAY QUE MIRAR DE DONDE SACAR TRAMOS PMI Y PMF(NO HAY DATOS EN LA TABLA)
	}	
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();		
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("orden_em", codOrden);	
		//fields.put("pmi", PMI);
		//fields.put("pmf", PMF);	
		return fields;
	}
	
	private String clave = null;
	private String codOrden = null;
	
	private Float PMI = null;
	private Float PMF = null;
	private String codProv_Pobl = null;
	private String codMunic_Pobl = null;
	private String codentidad_Pobl = null;
	private String codPoblamiento_Pobl = null;
	private String observaciones = null;
	private Date fechaRevision = null;
	private Integer estado_revision = null;
	private String estado=null;
	private String material=null;
	private String tipo_red=null;
	private Date fecha_inst=null;
		
	private VersionEiel version = null;
	
	
	private String titularidad = null;
	private String gestor = null;
	private String sist_impulsion = null;

	
	public String getTitularidad() {
		return titularidad;
	}
	public void setTitularidad(String titularidad) {
		this.titularidad = titularidad;
	}
	public String getGestion() {
		return gestor;
	}
	public void setGestion(String gestor) {
		this.gestor = gestor;
	}
	public String getSistema() {
		return sist_impulsion;
	}
	public void setSistema(String sist_impulsion) {
		this.sist_impulsion = sist_impulsion;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}


	public String getCodOrden() {
		return codOrden;
	}
	public void setCodOrden(String codOrden) {
		this.codOrden = codOrden;
	}
	public Float getPMI() {
		return PMI;
	}
	public void setPMI(Float PMI) {
		this.PMI = PMI;
	}
	public Float getPMF() {
		return PMF;
	}
	public void setPMF(Float PMF) {
		this.PMF = PMF;
	}
	public String getCodProv_Pobl() {
		return codProv_Pobl;
	}
	public void setCodProv_Pobl(String codProv_Pobl) {
		this.codProv_Pobl = codProv_Pobl;
	}
	public String getCodMunic_Pobl() {
		return codMunic_Pobl;
	}
	public void setCodMunic_Pobl(String codMunic_Pobl) {
		this.codMunic_Pobl = codMunic_Pobl;
	}
	public String getCodEntidad_Pobl() {
		return codentidad_Pobl;
	}
	public void setCodEntidad_Pobl(String codentidad_Pobl) {
		this.codentidad_Pobl = codentidad_Pobl;
	}
	public String getCodPoblamiento_Pobl() {
		return codPoblamiento_Pobl;
	}
	public void setCodPoblamiento_Pobl(String codPoblamiento_Pobl) {
		this.codPoblamiento_Pobl = codPoblamiento_Pobl;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}
	public Integer getEstado_Revision() {
		return estado_revision;
	}
	public void setEstado_Revision(Integer estado_revision) {
		this.estado_revision = estado_revision;
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
		
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getTipo_red() {
		return tipo_red;
	}
	public void setTipo_red(String tipo_red) {
		this.tipo_red = tipo_red;
	}
	public Date getFecha_inst() {
		return fecha_inst;
	}
	public void setFecha_inst(Date fecha_inst) {
		this.fecha_inst = fecha_inst;
	}

}
