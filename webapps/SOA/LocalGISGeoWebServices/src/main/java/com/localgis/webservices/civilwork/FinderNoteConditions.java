/**
 * FinderNoteConditions.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
