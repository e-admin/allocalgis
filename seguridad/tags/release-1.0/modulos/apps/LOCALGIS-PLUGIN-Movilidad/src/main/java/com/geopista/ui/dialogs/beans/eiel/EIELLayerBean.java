package com.geopista.ui.dialogs.beans.eiel;

import java.util.ArrayList;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;

public class EIELLayerBean {

	private String clave;
	private String codEIEL;
	private String classId;
	private String skeleton;
	private ArrayList<LCGCampoCapaTablaEIEL> relacionFields;
	
	public EIELLayerBean(String clave, String codEIEL, String classId, String skeleton, ArrayList<LCGCampoCapaTablaEIEL> relacionFields){
		this.clave=clave;
		this.codEIEL=codEIEL;
		this.classId=classId;
		this.skeleton=skeleton;
		this.relacionFields=relacionFields;		
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}	
	
	public String getCodEIEL() {
		return codEIEL;
	}

	public void setCodEIEL(String codEIEL) {
		this.codEIEL = codEIEL;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	public String getClassId() {
		return classId;
	}
	
	public String getSkeleton() {
		return skeleton;
	}

	public void setSkeleton(String skeleton) {
		this.skeleton = skeleton;
	}

	public ArrayList<LCGCampoCapaTablaEIEL> getRelacionFields() {
		return relacionFields;
	}

	public void setRelacionFields(ArrayList<LCGCampoCapaTablaEIEL> relacionFields) {
		this.relacionFields = relacionFields;
	}
	
}
