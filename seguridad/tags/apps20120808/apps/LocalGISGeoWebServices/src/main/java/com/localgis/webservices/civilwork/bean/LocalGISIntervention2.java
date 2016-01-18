package com.localgis.webservices.civilwork.bean;

import java.util.Calendar;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;

public class LocalGISIntervention2 {
	private Integer id;
	private String description;
	private Calendar startWarning;
	private Document[] listaDeDocumentos;
	private LayerFeatureBean[] featureRelation;
	private Integer userCreator;
	private Integer idMunicipio;
	private Calendar endedWork;
	private Integer priority;
	private Integer sendToNetwork;
	
	public Integer getSendToNetwork() {
		return sendToNetwork;
	}

	public void setSendToNetwork(Integer sendToNetwork) {
		this.sendToNetwork = sendToNetwork;
	}

	public Calendar getEndedWork() {
		return endedWork;
	}

	public void setEndedWork(Calendar endedWork) {
		this.endedWork = endedWork;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public LocalGISIntervention2(){
		super();
	}
	
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public Integer getUserCreator() {
		return userCreator;
	}
	public void setUserCreator(Integer userCreator) {
		this.userCreator = userCreator;
	}
	public LayerFeatureBean[] getFeatureRelation() {
		return featureRelation;
	}
	public void setFeatureRelation(LayerFeatureBean[] featureRelation) {
		this.featureRelation = featureRelation;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Calendar getStartWarning() {
		return startWarning;
	}
	public void setStartWarning(Calendar startWarning) {
		this.startWarning = startWarning;
	}
	public Document[] getListaDeDocumentos() {
		return listaDeDocumentos;
	}
	public void setListaDeDocumentos(Document[] listaDeDocumentos) {
		this.listaDeDocumentos = listaDeDocumentos;
	}
	private String actuationType;
	private String interventionType;
	private String pattern;
	private Calendar nextWarning;
	private Double foreseenBudget;
	private Double workPercentage;
	private String causes;
	private Integer assignedUser;
	
	
	
	
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
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public Calendar getNextWarning() {
		return nextWarning;
	}
	public void setNextWarning(Calendar nextWarning) {
		this.nextWarning = nextWarning;
	}
	public Double getForeseenBudget() {
		return foreseenBudget;
	}
	public void setForeseenBudget(Double foreseenBudget) {
		this.foreseenBudget = foreseenBudget;
	}
	public Double getWorkPercentage() {
		return workPercentage;
	}
	public void setWorkPercentage(Double workPercentage) {
		this.workPercentage = workPercentage;
	}
	public String getCauses() {
		return causes;
	}
	public void setCauses(String causes) {
		this.causes = causes;
	}
	public Integer getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(Integer assignedUser) {
		this.assignedUser = assignedUser;
	}
}
