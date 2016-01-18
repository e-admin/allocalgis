package com.localgis.webservices.civilwork;

import java.util.Calendar;

public class FinderNoteConditions {

	private String description;
	private Calendar from;
	private Calendar to;
	private Integer startElement;
	private Integer range;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Calendar getFrom() {
		return from;
	}
	public void setFrom(Calendar from) {
		this.from = from;
	}
	public Calendar getTo() {
		return to;
	}
	public void setTo(Calendar to) {
		this.to = to;
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
	public String getOrderByColumns() {
		return orderByColumns;
	}
	public void setOrderByColumns(String orderByColumns) {
		this.orderByColumns = orderByColumns;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	private Integer idMunicipio;
	private String orderByColumns; // nombreColumn:ASC;nombreColumn:DESC
	private String features; //idLayer:idFeature;idLayer:idFeature;

}
