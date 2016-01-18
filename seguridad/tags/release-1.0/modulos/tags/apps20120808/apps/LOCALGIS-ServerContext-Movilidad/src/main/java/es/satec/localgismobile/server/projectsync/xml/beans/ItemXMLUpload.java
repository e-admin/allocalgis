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
