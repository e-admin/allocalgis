package com.localgis.webservices.geomarketing.model.ot;

import java.io.Serializable;


public class GeoMarketingOT implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3698476635979670868L;
	private Integer numHabitants;
	private Integer numMales;
	private Integer numFemales;
	private RangeData[] ranges; 
	private StudiesLevel levelStudies;
	private Integer spanishHabitants;
	private Integer foreignHabitants;
	private Integer externalValue;
	private String municipio;
	private String attributeName;
	private Integer[] idFeature;
	
	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Integer getNumFemales() {
		return numFemales;
	}

	public void setNumFemales(Integer numFemales) {
		this.numFemales = numFemales;
	}

	public Integer getNumHabitants() {
		return numHabitants;
	}

	public void setNumHabitants(Integer numHabitants) {
		this.numHabitants = numHabitants;
	}

	public void setNumMales(Integer numMales) {
		this.numMales = numMales;
	}

	public Integer getNumMales() {
		return numMales;
	}

	public void setRanges(RangeData[] ranges) {
		this.ranges = ranges;
	}

	public RangeData[] getRanges() {
		return ranges;
	}

	public void setLevelStudies(StudiesLevel levelStudies) {
		this.levelStudies = levelStudies;
		
	}
	public StudiesLevel getLevelStudies(){
		return this.levelStudies;
	}
	public Integer getSpanishHabitants(){
		return this.spanishHabitants;
	}
	public void setSpanishHabitants(Integer spanishHabitants) {
		this.spanishHabitants = spanishHabitants;
		
	}
	public Integer getForeignHabitants(){
		return this.foreignHabitants;
	}
	public void setForeignHabitants(Integer foreignHabitants) {
		this.foreignHabitants = foreignHabitants;
	}

	public void setExternalValue(Integer externalValue) {
		this.externalValue = externalValue;
	}

	public Integer getExternalValue() {
		return externalValue;
	}

	public Integer[] getIdFeature() {
		return this.idFeature;
	}
	public void setIdFeature(Integer[] idFeature){
		this.idFeature = idFeature;
	}
}
