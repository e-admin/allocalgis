/**
 * Encuestados1EIEL.java
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

public class Encuestados1EIEL extends WorkflowEIEL implements Serializable, EIELPanel{

	public Encuestados1EIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_nucl_encuest_1","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_nucl_encuest_1","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_nucl_encuest_1","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_nucl_encuest_1","getCodINEPoblamiento"));
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
	
	private Integer padron = null;
	private Integer poblacionEstacional = null;
	private Integer altitud = null;
	private Integer viviendasTotales = null;
	private Integer plazasHoteleras = null;
	private Integer plazasCasasRurales = null;
	
	private String accesibilidad = null;
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

	public Integer getPadron() {
		return padron;
	}

	public void setPadron(Integer padron) {
		this.padron = padron;
	}

	public Integer getPoblacionEstacional() {
		return poblacionEstacional;
	}

	public void setPoblacionEstacional(Integer poblacionEstacional) {
		this.poblacionEstacional = poblacionEstacional;
	}

	public Integer getAltitud() {
		return altitud;
	}

	public void setAltitud(Integer altitud) {
		this.altitud = altitud;
	}

	public Integer getViviendasTotales() {
		return viviendasTotales;
	}

	public void setViviendasTotales(Integer viviendasTotales) {
		this.viviendasTotales = viviendasTotales;
	}

	public Integer getPlazasHoteleras() {
		return plazasHoteleras;
	}

	public void setPlazasHoteleras(Integer plazasHoteleras) {
		this.plazasHoteleras = plazasHoteleras;
	}

	public Integer getPlazasCasasRurales() {
		return plazasCasasRurales;
	}

	public void setPlazasCasasRurales(Integer plazasCasasRurales) {
		this.plazasCasasRurales = plazasCasasRurales;
	}

	public String getAccesibilidad() {
		return accesibilidad;
	}

	public void setAccesibilidad(String accesibilidad) {
		this.accesibilidad = accesibilidad;
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

	@Override
	public String toString() {
		return "Encuestados1EIEL [codINEMunicipio="+codINEMunicipio+",codINEEntidad=" + codINEEntidad
				+ ", codINEPoblamiento=" + codINEPoblamiento + "]";
	}
		
	
	
}
