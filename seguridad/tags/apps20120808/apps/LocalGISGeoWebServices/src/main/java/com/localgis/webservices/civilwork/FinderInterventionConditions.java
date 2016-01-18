package com.localgis.webservices.civilwork;

import java.util.Calendar;

public class FinderInterventionConditions {
	private String description;
	private String associatedAction;
	private Calendar fromStart;
	private Calendar toStart;
	private Calendar fromNext;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAssociatedAction() {
		return associatedAction;
	}
	public void setAssociatedAction(String associatedAction) {
		this.associatedAction = associatedAction;
	}
	public Calendar getFromStart() {
		return fromStart;
	}
	public void setFromStart(Calendar fromStart) {
		this.fromStart = fromStart;
	}
	public Calendar getToStart() {
		return toStart;
	}
	public void setToStart(Calendar toStart) {
		this.toStart = toStart;
	}
	public Calendar getFromNext() {
		return fromNext;
	}
	public void setFromNext(Calendar fromNext) {
		this.fromNext = fromNext;
	}
	public Calendar getToNext() {
		return toNext;
	}
	public void setToNext(Calendar toNext) {
		this.toNext = toNext;
	}
	public Integer getStartElement() {
		return startElement;
	}
	public void setStartElement(Integer startElement) {
		this.startElement = startElement;
	}
	public Integer getRange() {
		return range;
	}
	public void setRange(Integer range) {
		this.range = range;
	}
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
	public Double getForeseenBudgetFrom() {
		return foreseenBudgetFrom;
	}
	public void setForeseenBudgetFrom(Double foreseenBudgetFrom) {
		this.foreseenBudgetFrom = foreseenBudgetFrom;
	}
	public Double getForeseenBudgetTo() {
		return foreseenBudgetTo;
	}
	public void setForeseenBudgetTo(Double foreseenBudgetTo) {
		this.foreseenBudgetTo = foreseenBudgetTo;
	}
	public Double getWorkPercentageFrom() {
		return workPercentageFrom;
	}
	public void setWorkPercentageFrom(Double workPercentageFrom) {
		this.workPercentageFrom = workPercentageFrom;
	}
	public Double getWorkPercentageTo() {
		return workPercentageTo;
	}
	public void setWorkPercentageTo(Double workPercentageTo) {
		this.workPercentageTo = workPercentageTo;
	}
	public String getCauses() {
		return causes;
	}
	public void setCauses(String causes) {
		this.causes = causes;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public String getOrderColumns() {
		return orderColumns;
	}
	public void setOrderColumns(String orderColumns) {
		this.orderColumns = orderColumns;
	}
	private Calendar toNext;
	private Integer startElement;
	private Integer range;
	private Integer idMunicipio;
	private String actuationType;
	private String interventionType;
	private Double foreseenBudgetFrom;
	private Double foreseenBudgetTo;
	private Double workPercentageFrom;
	private Double workPercentageTo;
	private String causes;
	private String features;
	private String orderColumns;
}
