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
