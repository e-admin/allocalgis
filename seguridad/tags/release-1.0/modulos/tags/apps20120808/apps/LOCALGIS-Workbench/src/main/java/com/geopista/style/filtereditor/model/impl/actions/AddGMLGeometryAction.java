/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import org.deegree.gml.GMLException;
import org.deegree.model.geometry.GM_Point;
import org.deegree_impl.gml.GMLFactory;
import org.deegree_impl.model.cs.Adapters;
import org.deegree_impl.model.cs.ConvenienceCSFactory;
import org.deegree_impl.model.cs.CoordinateSystem;
import org.deegree_impl.model.geometry.GeometryFactory;
import org.opengis.cs.CS_CoordinateSystem;

/**
 * @author enxenio s.l.
 *
 */
public class AddGMLGeometryAction {
	
	public AddGMLGeometryAction() {}
	
	public Object execute() throws GMLException {
		
		ConvenienceCSFactory factory = ConvenienceCSFactory.getInstance();
		CoordinateSystem cs = factory.getCSByName("EPSG:4326");
		Adapters adapters = Adapters.getDefault();
		CS_CoordinateSystem csCoordinateSystem = adapters.export(cs);
		GM_Point point = GeometryFactory.createGM_Point(0.0,0.0,csCoordinateSystem);
		return GMLFactory.createGMLGeometry(point);	
	}
}
