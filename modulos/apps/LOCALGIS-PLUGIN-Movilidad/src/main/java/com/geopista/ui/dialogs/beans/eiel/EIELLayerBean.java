/**
 * EIELLayerBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
