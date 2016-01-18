/**
 * EIELMetadataSVG.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.eiel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.geopista.ui.dialogs.global.MetadataSVG;



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
