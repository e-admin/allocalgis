/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import org.deegree.gml.GMLBox;
import org.deegree_impl.gml.GMLFactory2;

/**
 * @author enxenio s.l.
 *
 */
public class AddGMLBoxAction {

	public AddGMLBoxAction() {}
	
	public Object execute() {
		
		GMLBox defaultGMLbox = GMLFactory2.createGMLBox(0.0,0.0,0.0,0.0,"EPSG.XML#4326");
		return defaultGMLbox;
	}
}
