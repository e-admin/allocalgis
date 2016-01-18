/**
 * OperativeSystemConfig.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security.dnie.beans;

public class OperativeSystemConfig {

	private String defaultSystemCertStore;
	private String defaultDNIeLibrary;
	public enum osType{WINDOWS_XP, WINDOWS_VISTA, WINDOWS_7, LINUX};
	private osType operativeSystem;
		
	public OperativeSystemConfig(String defaultSystemCertStore,
			String defaultDNIeLibrary, osType operativeSystem) {
		super();
		this.defaultSystemCertStore = defaultSystemCertStore;
		this.defaultDNIeLibrary = defaultDNIeLibrary;
		this.operativeSystem = operativeSystem;
	}
	
	public String getDefaultSystemCertStore() {
		return defaultSystemCertStore;
	}
	
	public void setDefaultSystemCertStore(String defaultSystemCertStore) {
		this.defaultSystemCertStore = defaultSystemCertStore;
	}
	
	public String getDefaultDNIeLibrary() {
		return defaultDNIeLibrary;
	}
	
	public void setDefaultDNIeLibrary(String defaultDNIeLibrary) {
		this.defaultDNIeLibrary = defaultDNIeLibrary;
	}
	
	public osType getOperativeSystem() {
		return operativeSystem;
	}
	
	public void setOperativeSystem(osType operativeSystem) {
		this.operativeSystem = operativeSystem;
	}
		
}
