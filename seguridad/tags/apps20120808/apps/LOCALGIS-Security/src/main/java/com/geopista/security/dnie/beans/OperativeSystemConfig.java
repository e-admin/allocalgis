package com.geopista.security.dnie.beans;

public class OperativeSystemConfig {

	private String defaultSystemCertStore;
	private String defaultDNIeLibrary;
	public enum osType{WINDOWS,LINUX};
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
