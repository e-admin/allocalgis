/**
 * PropertyAndValue.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.ws.sigm.beans;

import java.io.Serializable;

public class PropertyAndValue implements Serializable {
	
	private String groupTitle;
	private String property;
	private String value;
	
	public PropertyAndValue() {
		
		this.groupTitle="";
		this.property = "";
		this.value = "";
	}

	public PropertyAndValue(String groupTitle, String property, String value) {
		super();
		this.groupTitle=groupTitle;
		this.property = property;
		this.value = value;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}	
		
}
