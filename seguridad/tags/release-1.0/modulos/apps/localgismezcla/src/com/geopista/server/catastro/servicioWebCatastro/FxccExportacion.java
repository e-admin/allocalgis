package com.geopista.server.catastro.servicioWebCatastro;

import java.io.Serializable;

public class FxccExportacion implements Serializable{

	private String refCatastral = null;
			
	private String fxcc = null;
	
	private String lstImg = null;

	public String getLstImg() {
		return lstImg;
	}

	public void setLstImg(String lstImg) {
		this.lstImg = lstImg;
	}

	public String getRefCatastral() {
		return refCatastral;
	}

	public void setRefCatastral(String refCatastral) {
		this.refCatastral = refCatastral;
	}

	public String getFxcc() {
		return fxcc;
	}

	public void setFxcc(String fxcc) {
		this.fxcc = fxcc;
	}

	
	
}
