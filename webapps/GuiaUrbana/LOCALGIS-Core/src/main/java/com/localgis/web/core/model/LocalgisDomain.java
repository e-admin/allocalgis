package com.localgis.web.core.model;

import com.vividsolutions.jts.geom.Geometry;

public class LocalgisDomain {
	
	private String pattern;
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern=pattern;
	}

	private String traduccion;
	
	public String getTraduccion() {
		return traduccion;
	}

	public void setTraduccion(String traduccion) {
		this.traduccion=traduccion;
	}
}
