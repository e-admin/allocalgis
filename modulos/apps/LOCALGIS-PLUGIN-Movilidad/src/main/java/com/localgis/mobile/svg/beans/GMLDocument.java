/**
 * GMLDocument.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.mobile.svg.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Representación de un archivo GML
 * @author irodriguez
 *
 */
public class GMLDocument {

	private String systemId;				//identificador de la capa
	private List<String> listaNombreAtr; 	//lista de nombre de columnas de las features
	private List<FeatureGML> featuresList; 	//lista de features
	private boolean editable; 				//indica si la capa es editable o de sólo lectura
	private boolean active;					//Indica si la capa esta activa
	private int geometryType;				//indica el tipo de geometria que se puede insertar en la capa
	
	public GMLDocument(){
		super();
		this.listaNombreAtr = new ArrayList<String>();
		this.featuresList = new ArrayList<FeatureGML>();
	}
	public GMLDocument(List<String> listaNombreAtr, List<FeatureGML> featuresList, boolean editable,boolean active,
				String systemId, int geometryType) {
		super();
		this.listaNombreAtr = listaNombreAtr;
		this.featuresList = featuresList;
		this.editable = editable;
		this.active=active;
		this.systemId = systemId;
		this.geometryType = geometryType;
	}
	public List<String> getListaNombreAtr() {
		return listaNombreAtr;
	}
	public void setListaNombreAtr(List<String> listaNombreAtr) {
		this.listaNombreAtr = listaNombreAtr;
	}
	public List<FeatureGML> getFeaturesList() {
		return featuresList;
	}
	public void setFeaturesList(List<FeatureGML> featuresList) {
		this.featuresList = featuresList;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public int getGeometryType() {
		return geometryType;
	}
	public void setGeometryType(int geometryType) {
		this.geometryType = geometryType;
	}
}
