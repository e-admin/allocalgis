package com.geopista.app.eiel.beans;

import java.io.Serializable;

public class FeatureEIELSimple implements Serializable {

	
	private long id;
	private String revision_expirada;
	public FeatureEIELSimple(long id, String revision_expirada) {
		this.id=id;
		this.revision_expirada=revision_expirada;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRevision_expirada() {
		return revision_expirada;
	}
	public void setRevision_expirada(String revision_expirada) {
		this.revision_expirada = revision_expirada;
	}
	
	
}
