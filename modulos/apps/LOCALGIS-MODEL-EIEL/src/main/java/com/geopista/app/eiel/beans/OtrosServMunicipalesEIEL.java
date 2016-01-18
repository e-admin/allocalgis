/**
 * OtrosServMunicipalesEIEL.java
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

public class OtrosServMunicipalesEIEL extends WorkflowEIEL implements Serializable, EIELPanel {

	public OtrosServMunicipalesEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_otros_serv_munic","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_otros_serv_munic","getCodINEMunicipio"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);		
		return fields;
	}

	
	private String swInfGeneral = null;
	private String swInfTuristica = null;
	private String swGbElectronico = null;
	private String ordSoterramiento = null;
	private String enEolica = null;
	private String enSolar = null;
	private String plMareomotriz = null;
	private String otEnergias = null;
	private String observaciones = null;
	
	private Integer kwEolica = null;
	private Integer kwSolar = null;
	private Integer kwMareomotriz = null;
	private Integer kwOtEnergias = null;
	private String coberturaTlf=null;
	private String teleCable=null;
	private Integer estadoRevision = null;

	private Date fechaRevision = null;
	
	
	private VersionEiel version = null;
	
	
	public String getSwInfGeneral() {
		return swInfGeneral;
	}
	public void setSwInfGeneral(String swInfGeneral) {
		this.swInfGeneral = swInfGeneral;
	}
	public String getSwInfTuristica() {
		return swInfTuristica;
	}
	public void setSwInfTuristica(String swInfTuristica) {
		this.swInfTuristica = swInfTuristica;
	}
	public String getSwGbElectronico() {
		return swGbElectronico;
	}
	public void setSwGbElectronico(String swGbElectronico) {
		this.swGbElectronico = swGbElectronico;
	}
	public String getOrdSoterramiento() {
		return ordSoterramiento;
	}
	public void setOrdSoterramiento(String ordSoterramiento) {
		this.ordSoterramiento = ordSoterramiento;
	}
	public String getEnEolica() {
		return enEolica;
	}
	public void seteEnEolica(String enEolica) {
		this.enEolica = enEolica;
	}
	public String getEnSolar() {
		return enSolar;
	}
	public void setEnSolar(String enSolar) {
		this.enSolar = enSolar;
	}
	public String getPlMareomotriz() {
		return plMareomotriz;
	}
	public void setPlMareomotriz(String plMareomotriz) {
		this.plMareomotriz = plMareomotriz;
	}
	public String getOtEnergias() {
		return otEnergias;
	}
	public void setOtEnergias(String otEnergias) {
		this.otEnergias = otEnergias;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Integer getKwEolica() {
		return kwEolica;
	}
	public void setKwEolica(Integer kwEolica) {
		this.kwEolica = kwEolica;
	}
	public Integer getKwSolar() {
		return kwSolar;
	}
	public void setKwSolar(Integer kwSolar) {
		this.kwSolar = kwSolar;
	}
	public Integer getKwMareomotriz() {
		return kwMareomotriz;
	}
	public void setKwMareomotriz(Integer kwMareomotriz) {
		this.kwMareomotriz = kwMareomotriz;
	}
	public Integer getKwOtEnergias() {
		return kwOtEnergias;
	}
	public void setKwOtEnergias(Integer kwOtEnergias) {
		this.kwOtEnergias = kwOtEnergias;
	}
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}
	public String getCoberturaTlf() {
		return coberturaTlf;
	}
	public void setCoberturaTlf(String coberturaTlf) {
		this.coberturaTlf = coberturaTlf;
	}
	public String getTeleCable() {
		return teleCable;
	}
	public void setTeleCable(String teleCable) {
		this.teleCable = teleCable;
	}
	public void setEnEolica(String enEolica) {
		this.enEolica = enEolica;
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
