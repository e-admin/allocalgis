package com.geopista.app.reports;

public class ReportInfo {
	
	private String filename;
	
	private String description;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		if (description != null){
			return description;
		}
		else {
			return "(Sin descripcion)";
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
