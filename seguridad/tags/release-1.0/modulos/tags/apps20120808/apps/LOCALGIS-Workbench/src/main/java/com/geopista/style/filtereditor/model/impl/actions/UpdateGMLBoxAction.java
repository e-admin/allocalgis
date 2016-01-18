/*
 * Created on 20-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl.actions;

import org.deegree.gml.GMLBox;
import org.deegree.gml.GMLCoord;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateGMLBoxAction {
	
	private GMLBox _gmlBox;
	private double _minx;
	private double _miny;
	private double _maxx;
	private double _maxy;
	private String _srs;
	
	public UpdateGMLBoxAction(GMLBox gmlBox, double minx, double miny, double maxx, double maxy, String srs) {
		
		_gmlBox = gmlBox;
		_minx = minx;
		_miny = miny;
		_maxx = maxx;
		_maxy = maxy;
		_srs = srs;
	}
	
	public Object execute() {
		
		_gmlBox.setSrs(_srs);
		GMLCoord minCoord = _gmlBox.getMin();
		minCoord.setCoord(_minx,_miny);
		_gmlBox.setMin(minCoord);
		GMLCoord maxCoord = _gmlBox.getMax();
		maxCoord.setCoord(_maxx,_maxy);
		_gmlBox.setMax(maxCoord);
		return _gmlBox;
	}
}
