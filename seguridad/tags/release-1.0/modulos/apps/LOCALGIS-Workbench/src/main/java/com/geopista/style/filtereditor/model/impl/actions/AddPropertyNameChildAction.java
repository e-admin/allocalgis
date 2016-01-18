/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import com.geopista.style.filtereditor.model.impl.PropertyName;

/**
 * @author enxenio s.l.
 *
 */
public class AddPropertyNameChildAction {

	public AddPropertyNameChildAction() {}
	
	public Object execute() {
		
		return new PropertyName("");
	}
}
