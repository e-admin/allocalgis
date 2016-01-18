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
public class Literal extends Expression {
	
	private String _value;
	
	public Literal(String value) {
		
		_value = value;
	}
	
	public String getValue() {
		return _value;
	}
	
	public void setValue(String value) {
		_value = value;
	}

	public String toString() {
		return "Literal: "+_value;
	}
}
