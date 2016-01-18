/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl;

import com.geopista.style.filtereditor.model.Expression;

/**
 * @author enxenio s.l.
 *
 */
public class PropertyName extends Expression {
	
	private String _name;
	
	public PropertyName(String name) {
		
		_name = name;
	}
	
	public String getValue() {
		return _name;
	}
	
	public void setValue(String name) {
		_name = name;
	}
	
	public String toString() {
		return "PropertyName: "+_name;
	}
}
