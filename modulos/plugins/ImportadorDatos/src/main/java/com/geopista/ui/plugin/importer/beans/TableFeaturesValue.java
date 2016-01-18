/**
 * TableFeaturesValue.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.importer.beans;

import com.geopista.feature.ValidationError;

public class TableFeaturesValue {

	private ValidationError ve;
	private Object obj;
	private String name;
	private int type;


	public static final int DATE_TYPE=1;
	public static final int BOOLEAN_TYPE=2;

	public TableFeaturesValue(){

	}

	public TableFeaturesValue(String name, Object obj, ValidationError ve){
		this (name, obj, ve, 0);
	}

	public TableFeaturesValue(String name, Object obj, ValidationError ve, int type){
		this.obj = obj;
		this.ve = ve;
		this.name = name;
		this.type = type;
	}

	public ValidationError getVe() {
		return ve;
	}

	public void setVe(ValidationError ve) {
		this.ve = ve;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean equals(Object o)
	{
		if (!(o instanceof TableFeaturesValue) || o==null
				|| ((TableFeaturesValue)o).getName()==null)
			return false;

		if (((TableFeaturesValue)o).getName().equalsIgnoreCase(this.getName()))
			return true;
		else 
			return false;
	}
}
