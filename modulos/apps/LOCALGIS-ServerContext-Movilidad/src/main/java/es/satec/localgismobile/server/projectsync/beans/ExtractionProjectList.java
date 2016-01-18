/**
 * ExtractionProjectList.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
