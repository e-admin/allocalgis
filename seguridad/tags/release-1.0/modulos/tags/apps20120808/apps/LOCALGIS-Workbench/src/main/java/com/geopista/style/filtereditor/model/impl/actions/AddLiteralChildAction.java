/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import com.geopista.style.filtereditor.model.impl.Literal;

/**
 * @author enxenio s.l.
 *
 */
public class AddLiteralChildAction {
	
	public AddLiteralChildAction() {}
	
	public Object execute() {
		
		return new Literal("");	
	}
}
