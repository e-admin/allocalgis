package com.geopista.protocol.ortofoto;

import java.io.File;
import java.io.Serializable;

public class CSolicitudEnvioOrtofoto implements Serializable {

	private static final long serialVersionUID = 1L;

	private File image = null;
	private File worldfile = null;
	private boolean isWorldfileAttached = false;
	
	public File getImage() {
		return image;
	}

	public boolean isWorldfileAttached() {
		return isWorldfileAttached;
	}

	public File getWorldfile() {
		return worldfile;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public void setWorldfileAttached(boolean isWorldfileAttached) {
		this.isWorldfileAttached = isWorldfileAttached;
	}

	public void setWorldfile(File worldfile) {
		this.worldfile = worldfile;
	}

	public CSolicitudEnvioOrtofoto() {
		
	}

}
