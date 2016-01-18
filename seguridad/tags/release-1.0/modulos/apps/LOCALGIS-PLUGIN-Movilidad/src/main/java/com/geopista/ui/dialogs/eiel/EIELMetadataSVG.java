package com.geopista.ui.dialogs.eiel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.List;

import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.MetadataSVG;
import com.geopista.ui.dialogs.global.Utils;



public class EIELMetadataSVG extends MetadataSVG{
	
	//datos recogidos del SVG
	private String eielFileName;
	private HashMap<String,String> idAttributes = new HashMap<String,String>();
	//datos recogidos de BBDD
	private List eiel = new ArrayList();

	public EIELMetadataSVG(String encabezado, String grupo, String path, String numCelda, String nombreMetadato, HashMap<String,String> idAttributes) {
		super(encabezado,grupo,path,numCelda,nombreMetadato);
		this.eielFileName = updateEielFileName();
		this.idAttributes = idAttributes;
	}

	public List getEIEL() {
		return eiel;
	}
	
	public void setEIEL(List eiel) {
		this.eiel = eiel;
	}
	
	public void addEIEL(Object eiel) {
		this.eiel.add(eiel);
	}

	public String getEIELFileName() {
		return eielFileName;
	}
	
	public HashMap<String, String> getAttributes() {
		return idAttributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.idAttributes = attributes;
	}
	
	public void addAttributes(String attributeName, String attributeValue) {
		this.idAttributes.put(attributeName, attributeValue);
	}

	/**
	 * Calcula el nombre del fichero para la feature indicada
	 */
	private String updateEielFileName() {
		return numCelda + nombreMetadato + "_meta0.svg";	
	}
	
}
