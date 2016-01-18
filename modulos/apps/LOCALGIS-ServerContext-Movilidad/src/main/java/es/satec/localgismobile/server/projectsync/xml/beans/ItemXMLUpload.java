/**
 * ItemXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.beans;

import java.util.List;

public class ItemXMLUpload {

	private String reflectMethod;
	private String updatable;
	private String value;
	private String type;
	private List<String> subItems;
	
	public ItemXMLUpload(String reflectMethod, String updatable,String value, String type, List<String> subItems) {
		super();
		this.reflectMethod = reflectMethod;
		this.updatable=updatable;
		this.value = value;
		this.type = type;
		this.subItems = subItems;
	}
	public String getReflectMethod() {
		return reflectMethod;
	}
	public void setReflectMethod(String reflectMethod) {
		this.reflectMethod = reflectMethod;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getSubItems() {
		return subItems;
	}
	public void setSubItems(List<String> subItems) {
		this.subItems = subItems;
	}
	public void setUpdatable(String updatable) {
		this.updatable = updatable;
	}
	public String getUpdatable() {
		return updatable;
	}
}
