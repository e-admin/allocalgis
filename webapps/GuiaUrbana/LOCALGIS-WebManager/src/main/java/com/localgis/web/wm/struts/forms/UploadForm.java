/**
 * UploadForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

public class UploadForm extends ActionForm {


	private static final long serialVersionUID = 58066362288516032L;

	
	private Integer municipio;
	
	private Integer layer;
	
	private String  type;
	
	private FormFile file;

	private FormFile cabecera;
	private FormFile cabecera2;
	
	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public Integer getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Integer municipio) {
		this.municipio = municipio;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FormFile getCabecera() {
		return cabecera;
	}

	public void setCabecera(FormFile cabecera) {
		this.cabecera = cabecera;
	}

	public FormFile getCabecera2() {
		return cabecera2;
	}

	public void setCabecera2(FormFile cabecera2) {
		this.cabecera2 = cabecera2;
	}

	
}
