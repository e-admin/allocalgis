package com.geopista.app.loadEIELData.beans;

import java.util.ArrayList;
import java.util.Hashtable;

public class LoadInfo {

	int id;
	String name;	
	String loadFilePath;
	ArrayList<String> loadFiles;
	ArrayList<MunicipalityInfo> loadMunicipalities;
		
	public LoadInfo() {
		super();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoadFilePath() {
		return loadFilePath;
	}

	public void setLoadFilePath(String loadFilePath) {
		this.loadFilePath = loadFilePath;
	}

	public ArrayList<String> getLoadFiles() {
		return loadFiles;
	}

	public void setLoadFiles(ArrayList<String> loadFiles) {
		this.loadFiles = loadFiles;
	}

	public ArrayList<MunicipalityInfo> getLoadMunicipalities() {
		return loadMunicipalities;
	}

	public void setLoadMunicipalities(
			ArrayList<MunicipalityInfo> loadMunicipalities) {
		this.loadMunicipalities = loadMunicipalities;
	}
	
	@Override
	public String toString() {
		return "{" + getId() + "," + getName() + "," + getLoadFilePath() + "," + getLoadFiles() + "," + getLoadMunicipalities() + "}";
	}	
	
}
