/**
 * PadronNucleosEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class PadronNucleosEIEL extends WorkflowEIEL implements Serializable, EIELPanel {
	
	public PadronNucleosEIEL(){
		//REVISAR TABLA eiel_t_?
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_padron_nd","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_padron_nd","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_padron_nd","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_padron_nd","getCodINEPoblamiento"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("codpoblamiento", codINEPoblamiento);
		fields.put("codentidad", codINEEntidad);		
		return fields;
	}	
	
	private String codINEEntidad = null;
	private String codINEPoblamiento = null;
	
	private Integer hombres_a1= null;
	private Integer mujeres_a1 = null;
	private Integer totPobl_a1 = null;
	private Integer hombres_a2 = null;
	private Integer mujeres_a2 = null;
	private Integer totPobl_a2 = null;	
	
	private Integer fecha_a1 = null;
	private Integer fecha_a2 = null;
	
	private String observaciones = null;
	
	private Date fechaRevision = null;
	private Integer estadoRevision = null;
	
	private VersionEiel version = null;
	
	
	public String getCodINEEntidad() {
		return codINEEntidad;
	}
	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}

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
	public String getCodINEPoblamiento() {
		return codINEPoblamiento;
	}
	public void setCodINEPoblamiento(String codINEPoblamiento) {
		this.codINEPoblamiento = codINEPoblamiento;
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
