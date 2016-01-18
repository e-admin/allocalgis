package com.geopista.feature;

import org.satec.sld.SVG.SVGNodeFeature;

public class AutoFieldDomain extends Domain {

	public AutoFieldDomain(String name, String description) {}
	
	public void setPattern(String pattern) {}

	public String getRepresentation() {
		return null;
	}

	public int getType() {
		return 0;
	}

	public boolean validate(SVGNodeFeature feature, String Name, Object Value) {
		return false;
	}
}
