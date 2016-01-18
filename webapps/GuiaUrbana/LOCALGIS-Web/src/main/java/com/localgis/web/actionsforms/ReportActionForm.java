/**
 * ReportActionForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actionsforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ReportActionForm extends ActionForm {

	private String idLayers;
	private String idFeatures;
	
	//Coordenadas del la posicion del elemento que se ha solicitado.
	private String x;
	private String y;

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors actionErrors = new ActionErrors();
		
		// TODO: Realizar validaciones del formulario de entrada de datos
		/*if (idMap == null || idMap.intValue() < 0) {
			actionErrors.add("idMap", new ActionMessage("printMap.error.idMap"));
		}
		if (language == null) {
			actionErrors.add("language", new ActionMessage("printMap.error.language"));
		}*/		

		return actionErrors;
	}

	
	public String getIdLayers() {
		return idLayers;
	}

	public void setIdLayers(String idLayers) {
		this.idLayers = idLayers;
	}

	public String getIdFeatures() {
		return idFeatures;
	}

	public void setIdFeature(String idFeatures) {
		this.idFeatures = idFeatures;
	}


	public String getX() {
		return x;
	}


	public void setX(String x) {
		this.x = x;
	}


	public String getY() {
		return y;
	}


	public void setY(String y) {
		this.y = y;
	}
}
