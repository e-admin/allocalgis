/**
 * StatisticalDataOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.ot;

public class StatisticalDataOT {
	private Integer idMunicipio;
	private String actuationType;
	private String interventionType;
	private Integer numInterventions;
	private Double media;
	private Double stdDeviation;
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getActuationType() {
		return actuationType;
	}
	public void setActuationType(String actuationType) {
		this.actuationType = actuationType;
	}
	public String getInterventionType() {
		return interventionType;
	}
	public void setInterventionType(String interventionType) {
		this.interventionType = interventionType;
	}
	public Integer getNumInterventions() {
		return numInterventions;
	}
	public void setNumInterventions(Integer numInterventions) {
		this.numInterventions = numInterventions;
	}
	public Double getMedia() {
		return media;
	}
	public void setMedia(Double media) {
		this.media = media;
	}
	public Double getStdDeviation() {
		return stdDeviation;
	}
	public void setStdDeviation(Double stdDeviation) {
		this.stdDeviation = stdDeviation;
	}

}
