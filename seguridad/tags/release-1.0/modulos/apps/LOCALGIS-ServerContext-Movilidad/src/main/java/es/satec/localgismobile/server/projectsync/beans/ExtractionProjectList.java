package es.satec.localgismobile.server.projectsync.beans;

import java.util.List;

public class ExtractionProjectList {
	
	private List<ExtractionProject> projList;

	public ExtractionProjectList() {
		super();
	}

	public ExtractionProjectList(List<ExtractionProject> projList) {
		super();
		this.projList = projList;
	}

	public List<ExtractionProject> getProjList() {
		return projList;
	}

	public void setProjList(List<ExtractionProject> projList) {
		this.projList = projList;
	}

	/**
	 * Devuelve el bean en formato XML para ser interpretado en un dispositivo móvil
	 * @return
	 */
	public String toXMLFormat(){
		StringBuffer result = new StringBuffer();		
		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		result.append("\n<ProjectList>");
		for (int i = 0; i < projList.size(); i++) {
			result.append("\n" + projList.get(i).toXMLFormat());
		}
		result.append("\n</ProjectList>");
		return result.toString();
	}
}
