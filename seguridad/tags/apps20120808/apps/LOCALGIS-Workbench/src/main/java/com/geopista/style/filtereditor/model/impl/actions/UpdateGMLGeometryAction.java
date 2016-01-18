/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import org.deegree.gml.GMLCoordinates;
import org.deegree.gml.GMLGeometry;
import org.deegree.gml.GMLPoint;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateGMLGeometryAction {
	
	private GMLGeometry _gmlGeometry;
	private String _srs;
	private String _coordinates;
	
	public UpdateGMLGeometryAction(GMLGeometry gmlGeometry,String srs,String coordinates) {
		
		_gmlGeometry = gmlGeometry;
		_srs = srs;
		_coordinates = coordinates;
	}
	
	public Object execute() {
		
		GMLPoint gmlPoint = (GMLPoint)_gmlGeometry;
		gmlPoint.setSrs(_srs);
		GMLCoordinates coordinates = gmlPoint.getCoordinates();
		coordinates.setCoordinates(_coordinates);
		gmlPoint.setCoordinates(coordinates);
		return gmlPoint;
	}
}
