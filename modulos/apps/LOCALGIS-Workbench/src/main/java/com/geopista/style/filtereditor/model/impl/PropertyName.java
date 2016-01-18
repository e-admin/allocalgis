/**
 * PropertyName.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
