/**
 * ExportGpxActionForm.java
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

public class ExportGpxActionForm extends ActionForm {

	private Integer idMap;

	private String language;

	/**
	 * Layer ids
	 */
	private String[] lid;
	
	/**
	 * Layers visibility
	 */
	private String lv;
	
	/**
	 * Window Height
	 */
	private String wh;
	
	/**
	 * Window Width
	 */
	private String ww;
	
	private String maxy;
	
	private String maxx;
	
	private String miny;
	
	private String minx;

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors actionErrors = new ActionErrors();
		
		// TODO: Realizar validaciones del formulario de entrada de datos
		if (idMap == null || idMap.intValue() < 0) {
			actionErrors.add("idMap", new ActionMessage("printMap.error.idMap"));
		}
		if (language == null) {
			actionErrors.add("language", new ActionMessage("printMap.error.language"));
		}		

		return actionErrors;
	}

	public Integer getIdMap() {
		return idMap;
	}

	public void setIdMap(Integer idMap) {
		this.idMap = idMap;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String[] getLid(){
		return this.lid;		
	}
	
	public void setLid(String[] layersIds) {
		this.lid = layersIds;
	}

	public void setLv(String lv) {
		this.lv = lv;
	}
	
	public String getWh() {
		return wh;
	}

	public void setWh(String wh) {
		this.wh = wh;
	}

	public String getWw() {
		return ww;
	}

	public void setWw(String ww) {
		this.ww = ww;
	}

	public String getMaxy() {
		return maxy;
	}

	public void setMaxy(String maxy) {
		this.maxy = maxy;
	}

	public String getMaxx() {
		return maxx;
	}

	public void setMaxx(String maxx) {
		this.maxx = maxx;
	}

	public String getMiny() {
		return miny;
	}

	public void setMiny(String miny) {
		this.miny = miny;
	}

	public String getMinx() {
		return minx;
	}

	public void setMinx(String minx) {
		this.minx = minx;
	}

	public String getLv() {
		return lv;
	}

	// Friendly Getters
	public String[] getLayersIds() {
		return lid;
	}
	
	public boolean[] getLayersVisibility() {
		boolean[] layersVisibility = new boolean[lv.length()];
		char[] layersVisibilityArray = lv.toCharArray();
		for (int i = 0; i < layersVisibility.length; i++){
			if (layersVisibilityArray[i] == '0'){
				layersVisibility[i] = false;
			}
			else {
				layersVisibility[i] = true;
			}
		}
		
		return layersVisibility;
	}
	
	public String getWindowHeight(){
		return wh;
	}
	
	public String getWindowWidth(){
		return ww;
	}
}
