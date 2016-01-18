package com.geopista.app.loadEIELData.beans;

public class MunicipalityInfo {

	String name;
	String ine;
	String srid;
	String shpFilesPath;
	String originDriver;
	String originBBDDConnectionPath;
	String originUser;
	String originPassword;
		
	public MunicipalityInfo() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIne() {
		return ine;
	}
	
	public String getIneProvincia() {
		return ine.substring(0,2);
	}
	
	public String getIneMunicipio() {
		return ine.substring(2,5);
	}
	

	public void setIne(String ine) {
		this.ine = ine;
	}

	public String getSrid() {
		return srid;
	}
	
	public void setSrid(String srid) {
		this.srid = srid;
	}
	
	public String getShpFilesPath() {
		return shpFilesPath;
	}

	public void setShpFilesPath(String shpFilesPath) {
		this.shpFilesPath = shpFilesPath;
	}

	public String getOriginDriver() {
		return originDriver;
	}

	public void setOriginDriver(String originDriver) {
		this.originDriver = originDriver;
	}

	public String getOriginBBDDConnectionPath() {
		return originBBDDConnectionPath;
	}

	public void setOriginBBDDConnectionPath(String originBBDDConnectionPath) {
		this.originBBDDConnectionPath = originBBDDConnectionPath;
	}

	public String getOriginUser() {
		return originUser;
	}

	public void setOriginUser(String originUser) {
		this.originUser = originUser;
	}

	public String getOriginPassword() {
		return originPassword;
	}

	public void setOriginPassword(String originPassword) {
		this.originPassword = originPassword;
	}

	@Override
	public String toString() {
		return "{" + getIne() + "," + getName() + "," + getShpFilesPath() + "," + getOriginBBDDConnectionPath() + "}";
	}	
	
}
