package es.satec.localgismobile.server.projectsync.xml.beans;

import java.util.List;

public class TabXMLUpload {

	private String classId;
	private List<ItemXMLUpload> itemList;
	
	public TabXMLUpload(String classId, List<ItemXMLUpload> itemList) {
		super();
		this.classId = classId;
		this.itemList = itemList;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public List<ItemXMLUpload> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemXMLUpload> itemList) {
		this.itemList = itemList;
	}
	
	
}
