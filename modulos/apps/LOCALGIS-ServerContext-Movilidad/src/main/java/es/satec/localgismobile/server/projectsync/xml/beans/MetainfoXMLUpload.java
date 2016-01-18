/**
 * MetainfoXMLUpload.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.projectsync.xml.beans;

import java.util.List;

/**
 * Bean para almacenar la información sobre los metadatos
 * @author irodriguez
 *
 */
public class MetainfoXMLUpload {

	private List<MetadataXMLUpload> metadataList;
	
	public MetainfoXMLUpload(List<MetadataXMLUpload> metadataMap) {
		super();
		this.metadataList = metadataMap;
	}

	public List<MetadataXMLUpload> getMetadataMap() {
		return metadataList;
	}

	public void setMetadataMap(List<MetadataXMLUpload> metadataList) {
		this.metadataList = metadataList;
	}


}
