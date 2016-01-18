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
