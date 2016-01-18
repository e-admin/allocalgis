/**
 * AbastecimientoAutonomoEIEL.java
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

public class AbastecimientoAutonomoEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public AbastecimientoAutonomoEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_abast_au","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_abast_au","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_abast_au","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_abast_au","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_abast_au","getCodINENucleo"));
	}	

	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("codpoblamiento", codINEPoblamiento);
		fields.put("codentidad", codINEEntidad);
		return fields;
	}
	
	private String clave = null;
	private String codINEEntidad = null;
	private String codINEPoblamiento = null;
	
	private Integer viviendas = null;
	private Integer poblacionResidente = null;
	private Integer poblacionEstacional = null;
	private Integer viviendasDeficitarias = null;
	private Integer poblacionResidenteDef = null;
	private Integer poblacionEstacionalDef = null;
	private Integer fuentesControladas = null;
	private Integer fuentesNoControladas = null;
	
	private String suficienciaCaudal = null;
	private String observaciones = null;
	
	private Date fechaRevision = null;
	private Integer estadoRevision = null;
	
	
	private VersionEiel version = null;
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCodINEEntidad() {
		return codINEEntidad;
	}
	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}
	public String getCodINENucleo() {
		return codINEPoblamiento;
	}
	public void setCodINENucleo(String codINENucleo) {
		this.codINEPoblamiento = codINENucleo;
	}
	public Integer getViviendas() {
		return viviendas;
	}
	public void setViviendas(Integer viviendas) {
		this.viviendas = viviendas;
	}
	public Integer getPoblacionResidente() {
		return poblacionResidente;
	}
	public void setPoblacionResidente(Integer poblacionResidente) {
		this.poblacionResidente = poblacionResidente;
	}
	public Integer getPoblacionEstacional() {
		return poblacionEstacional;
	}
	public void setPoblacionEstacional(Integer poblacionEstacional) {
		this.poblacionEstacional = poblacionEstacional;
	}
	public Integer getViviendasDeficitarias() {
		return viviendasDeficitarias;
	}
	public void setViviendasDeficitarias(Integer viviendasDeficitarias) {
		this.viviendasDeficitarias = viviendasDeficitarias;
	}
	public Integer getPoblacionResidenteDef() {
		return poblacionResidenteDef;
	}
	public void setPoblacionResidenteDef(Integer poblacionResidenteDef) {
		this.poblacionResidenteDef = poblacionResidenteDef;
	}
	public Integer getPoblacionEstacionalDef() {
		return poblacionEstacionalDef;
	}
	public void setPoblacionEstacionalDef(Integer poblacionEstacionalDef) {
		this.poblacionEstacionalDef = poblacionEstacionalDef;
	}
	public Integer getFuentesControladas() {
		return fuentesControladas;
	}
	public void setFuentesControladas(Integer fuentesControladas) {
		this.fuentesControladas = fuentesControladas;
	}
	public Integer getFuentesNoControladas() {
		return fuentesNoControladas;
	}
	public void setFuentesNoControladas(Integer fuentesNoControladas) {
		this.fuentesNoControladas = fuentesNoControladas;
	}
	public String getSuficienciaCaudal() {
		return suficienciaCaudal;
	}
	public void setSuficienciaCaudal(String suficienciaCaudal) {
		this.suficienciaCaudal = suficienciaCaudal;
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

	@Override
	public String toString() {
		return "AbastecimientoAutonomoEIEL [codINEMunicipio="+codINEMunicipio+",codINEEntidad=" + codINEEntidad
				+ ", codINEPoblamiento=" + codINEPoblamiento + "]";
	}
}
