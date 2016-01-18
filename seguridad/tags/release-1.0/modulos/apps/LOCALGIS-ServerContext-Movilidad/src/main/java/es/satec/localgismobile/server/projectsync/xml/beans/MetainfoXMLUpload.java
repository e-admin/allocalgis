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
