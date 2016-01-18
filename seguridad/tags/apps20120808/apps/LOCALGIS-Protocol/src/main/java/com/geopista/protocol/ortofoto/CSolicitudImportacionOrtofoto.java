package com.geopista.protocol.ortofoto;

public class CSolicitudImportacionOrtofoto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
    private String extension;
    private String epsg;
	private String imageName;
	private String worldfileName;
	private boolean worldfileAttached;
	
	public boolean isWorldfileAttached() {
		
		return worldfileAttached;
	}

	public void setWorldfileAttached(boolean worldfileAttached) {
		
		this.worldfileAttached = worldfileAttached;
	}

	public CSolicitudImportacionOrtofoto() {
		
	}
	
	public String getImageName() {
		
		return imageName;
	}

	public String getWorldfileName() {
		
		return worldfileName;
	}

	public void setImageName(String imageName) {
		
		this.imageName = imageName;
	}

	public void setWorldfileName(String worldfileName) {
		
		this.worldfileName = worldfileName;
	}

	public CSolicitudImportacionOrtofoto(String epsg) {

		this.epsg = epsg;
	}
	
	public String getEpsg() {
		
		return epsg;
	}
	
	public void setEpsg(String epsg) {
		
		this.epsg = epsg;
	}

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
	
}
