/*
 * Created on 20-oct-2004
 *
 */
package com.geopista.style.sld.model.impl;

/**
 * @author enxenio s.l.
 *
 */
public class FeatureAttribute {
	
	private String _name;
	private String _type;
	
	public FeatureAttribute(String name,String type) {
		_name = name;
		_type = type;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getType() {
		return _type;
	}
	
	public String toString() {
		return _name;
	}
}
