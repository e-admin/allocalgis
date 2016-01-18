/**
 * LocalGISApp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.centralizadorsso.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
*
* @author  dcaaveiro
*/
public class LocalGISApp implements Serializable{
	
	/*
	 * app: Identificador de la aplicación
	 * dictionary: Diccionario de texto a mostrar
	 * acl: Rol de acceso
	 * perm: Permiso mínimo para acceder a la aplicación
	 * appType: Tipo de aplicación ("DESKTOP" o "WEB")
	 * path: Ruta de llamada de la aplicación
	 */
	
	public static final String TIPO_APP_DESKTOP = "DESKTOP";
	public static final String TIPO_APP_WEB = "WEB";
	
	private String app;
	private HashMap<String, String> dictionary;
	private String acl;
	private String perm;
	private String appType;
	private String path;
	private Boolean active;
	
	public LocalGISApp(){		
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public HashMap<String, String> getDictionary() {
		return dictionary;
	}

	public void setDictionary(HashMap<String, String> dictionary) {
		this.dictionary = dictionary;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	public String getPerm() {
		return perm;
	}

	public void setPerm(String perm) {
		this.perm = perm;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}	
	
}
