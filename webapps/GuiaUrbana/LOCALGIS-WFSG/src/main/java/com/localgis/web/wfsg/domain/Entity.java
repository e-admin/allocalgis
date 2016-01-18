/**
 * Entity.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg.domain;

import java.util.ArrayList;
import java.util.List;

public class Entity {
	
	private String name;
	private String typeName;
	private List fields = new ArrayList();
	private List elements = new ArrayList();
	
	

	public Entity(){
		// Empty Constructor
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public List getFields() {
		return fields;
	}

	public void setFields(List fields) {
		this.fields = fields;
	}

	public void addFields(EntityField aField){
		this.fields.add(aField);
	}
	
	public List getElements() {
		return elements;
	}

	public void setElements(List elements) {
		this.elements = elements;
	}
	
	public void addElement(ElementEntity anElement){
		this.elements.add(anElement);
	}
}
