package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class PadronMunicipiosEIEL extends WorkflowEIEL implements Serializable, EIELPanel {
	
	public PadronMunicipiosEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_padron_ttmm","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_padron_ttmm","getCodINEMunicipio"));
	}

	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);		
		return fields;
	}
	
	private Integer hombres_a1 = null;
	private Integer mujeres_a1 = null;
	private Integer totPobl_a1 = null;
	private Integer hombres_a2 = null;
	private Integer mujeres_a2 = null;
	private Integer totPobl_a2 = null;	
	
	private Integer fecha_a1 = null;
	private Integer fecha_a2 = null;
	
	private String observaciones = null;
	
	private Date fechaActualizacion = null;	
	private Integer estadoRevision = null;
	
	
	private VersionEiel version = null;
	
	public Integer getHombres_a1() {
		return hombres_a1;
	}
	public void setHombres_a1(Integer hombres_a1) {
		this.hombres_a1 = hombres_a1;
	}
	public Integer getMujeres_a1() {
		return mujeres_a1;
	}
	public void setMujeres_a1(Integer mujeres_a1) {
		this.mujeres_a1 = mujeres_a1;
	}
	public Integer getTotPobl_a1() {
		return totPobl_a1;
	}
	public void setTotPobl_a1(Integer totPobl_a1) {
		this.totPobl_a1 = totPobl_a1;
	}
	public Integer getHombres_a2() {
		return hombres_a2;
	}
	public void setHombres_a2(Integer hombres_a2) {
		this.hombres_a2 = hombres_a2;
	}
	public Integer getMujeres_a2() {
		return mujeres_a2;
	}
	public void setMujeres_a2(Integer mujeres_a2) {
		this.mujeres_a2 = mujeres_a2;
	}
	public Integer getTotPobl_a2() {
		return totPobl_a2;
	}
	public void setTotPobl_a2(Integer totPobl_a2) {
		this.totPobl_a2 = totPobl_a2;
	}
	
    /**
     * @return the fecha_a1
     */
    public Integer getFecha_a1() {
        return fecha_a1;
    }
    
    /**
     * @param fecha_a1 the fecha_a1 to set
     */
    public void setFecha_a1(Integer fecha_a1) {
        this.fecha_a1 = fecha_a1;
    }
    
    /**
     * @return the fecha_a2
     */
    public Integer getFecha_a2() {
        return fecha_a2;
    }
    
    /**
     * @param fecha_a2 the fecha_a2 to set
     */
    public void setFecha_a2(Integer fecha_a2) {
        this.fecha_a2 = fecha_a2;
    }
    public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
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
