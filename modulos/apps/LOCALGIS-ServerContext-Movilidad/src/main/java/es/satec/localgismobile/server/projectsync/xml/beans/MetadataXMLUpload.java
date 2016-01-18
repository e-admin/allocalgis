/**
 * MetadataXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.beans;

import java.util.HashMap;
import java.util.List;

public class MetadataXMLUpload {
	
	private String changeType;
	private String layerId;
	private HashMap<String,String> idFeatures;
	private List<TabXMLUpload> tabList;
	private String geometry;
	private String id;

	public MetadataXMLUpload(HashMap<String,String> idFeatures, String id,List<TabXMLUpload> tabList, String layerId, String changeType, String geometry) {
		super();
		this.idFeatures = idFeatures;
		this.id=id;
		this.tabList = tabList;
		this.layerId = layerId;
		this.changeType = changeType;
		this.setGeometry(geometry);
	}
	public HashMap<String,String> getIdFeatures() {
		return idFeatures;
	}
	public void setIdFeatures(HashMap<String,String> idFeatures) {
		this.idFeatures = idFeatures;
	}
	public void putIdFeatures(String name,String value) {
		this.idFeatures.put(name,value);
	}
	public List<TabXMLUpload> getTabList() {
		return tabList;
	}
	public void setTabList(List<TabXMLUpload> tabList) {
		this.tabList = tabList;
	}
	public String getLayerId() {
		return layerId;
	}
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
	public String getGeometry() {
		return geometry;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
