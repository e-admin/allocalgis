/**
 * PropertyAndName.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.sigm.beans;

import java.io.Serializable;

public class PropertyAndName implements Serializable{

	private String groupTitle;
	private String property;
	private String name;
	private String type;
	private Boolean searchActive;
	private Boolean active;
	
	public PropertyAndName() {
		this.groupTitle = "";
		this.property = "";
		this.name = "";
		this.type = "";
		this.searchActive = false;
		this.active = false;		
	}

	public PropertyAndName(String groupTitle,String property, String name, String type, Boolean searchActive, Boolean active) {
		super();
		this.groupTitle = groupTitle;
		this.property = property;
		this.name = name;
		this.type = type;
		this.searchActive = searchActive;
		this.active = active;
	}
	
	public String getGroupTitle() {
		return groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getSearchActive() {
		return searchActive;
	}

	public void setSearchActive(Boolean searchActive) {
		this.searchActive = searchActive;
	}	
	
}
