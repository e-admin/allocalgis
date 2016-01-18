package com.localgis.app.gestionciudad.beans;

import java.util.Calendar;

import javax.naming.ConfigurationException;


public class LocalGISIntervention extends LocalGISNote {
	
	
	private String actuationType;
	private String interventionType;
	private String pattern;
	private Calendar nextWarning;
	private Calendar executionDate;
	private Double foreseenBudget;
	private Double workPercentage;
	private String causes;
	private Integer assignedUser;
	private Integer priority;
	private Integer addToRoutes;
	
	
	

	/**
	 * @return the executionDate
	 */
	public Calendar getExecutionDate() {
		return executionDate;
	}
	/**
	 * @param executionDate the executionDate to set
	 */
	public void setExecutionDate(Calendar executionDate) {
		this.executionDate = executionDate;
	}
	public LocalGISIntervention(){
		super();
	}
	public LocalGISIntervention(int interventionID, String interventionDescription, 
			String actuationType, Calendar	startintervention, Calendar nextIntervention,
			String interventionCause,Document[] doculist) throws ConfigurationException{
		
		super(1,1);
		this.setId(interventionID);
		this.setDescription(interventionDescription);
		this.actuationType = actuationType;
		this.setStartWarning(startintervention);
		this.nextWarning = nextIntervention;
		this.causes = interventionCause;
		this.setListaDeDocumentos(doculist);
	}
	
	public LocalGISIntervention(int interventionID, int idUserCreator, int idMunicipio, String
			interventionDescription, LayerFeatureBean[] featureRelation, Document[] documentList, Calendar startWarning, 
			String actuationType, String interventionType, String pattern, Calendar nextWarning,
			Double foreseenBudget, Double workPercentage, String Causes, int assignedUser) throws ConfigurationException{
		super(idUserCreator,idMunicipio);
		setId(interventionID);
		setDescription(interventionDescription);
		setFeatureRelation(featureRelation);
		setListaDeDocumentos(documentList);
		setStartWarning(startWarning);
		
		
		this.actuationType = actuationType;
		this.interventionType = interventionType;
		this.pattern = pattern;
		this.nextWarning = nextWarning;
		this.foreseenBudget = foreseenBudget;
		this.workPercentage = workPercentage;
		this.causes = Causes;
		this.assignedUser = assignedUser;
	}
	
	public LocalGISIntervention(LocalGISNote note) throws ConfigurationException{
		super(note.getUserCreator(),note.getIdMunicipio());
		setId(note.getId());
		setDescription(note.getDescription());
		setFeatureRelation(note.getFeatureRelation());
		setListaDeDocumentos(note.getListaDeDocumentos());
		setStartWarning(note.getStartWarning());
	}
	
	
	public LocalGISIntervention(Integer id,
			String description, Calendar startWarning,
			Integer userCreator, Integer idMunicipio,
			String actuationType, String interventionType,
			String pattern, Calendar nextWarning,
			Double foreseenBudget, Double workPercentage,
			String causes, Integer assignedUser) throws ConfigurationException {
		super(userCreator,idMunicipio,startWarning);
		this.setActuationType(actuationType);
		this.setAssignedUser(assignedUser);
		this.setCauses(causes);
		this.setDescription(description);
		this.setForeseenBudget(foreseenBudget);
		this.setId(id);
		this.setInterventionType(interventionType);
		this.setNextWarning(nextWarning);
		this.setPattern(pattern);
		this.setWorkPercentage(workPercentage);
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	/**
	 * @return the addToRoutes
	 */
	public Integer getAddToRoutes() {
		return addToRoutes;
	}
	
	
	/**
	 * @param addToRoutes the addToRoutes to set
	 * 
	 * Si recibe cualquier número que no sea 0 o 1 
	 */
	public void setAddToRoutes(Integer addToRoutes) {
		if (addToRoutes>=0 && addToRoutes<=2){
			this.addToRoutes = addToRoutes;
		} else{
			this.addToRoutes = null;
		}
	}
	
}
