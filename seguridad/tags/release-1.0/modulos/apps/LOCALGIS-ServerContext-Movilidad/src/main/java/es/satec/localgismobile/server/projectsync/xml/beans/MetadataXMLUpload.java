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
