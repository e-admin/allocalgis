package es.satec.localgismobile.server.projectsync.xml.beans;

import java.util.Map;

public class ResourcesXMLUpload {

	private Map<String, ResourceXMLUpload> resourcesMap;
	
	public ResourcesXMLUpload(Map<String, ResourceXMLUpload> resourcesMap) {
		super();
		this.resourcesMap = resourcesMap;
	}

	public Map<String, ResourceXMLUpload> getResourcesMap() {
		return resourcesMap;
	}

	public void setResourcesMap(Map<String, ResourceXMLUpload> resourcesMap) {
		this.resourcesMap = resourcesMap;
	}
	
}
