/*
 * Created on 27-sep-2004
 *
 */
package com.geopista.style.sld.ui.impl;

import java.util.HashMap;

/**
 * @author enxenio s.l.
 *
 */
public class GraphicFormatManager {

	private HashMap _graphicFormats;
	
	public GraphicFormatManager() {
		
		_graphicFormats = new HashMap();
		_graphicFormats.put("jpg","image/jpeg");
		_graphicFormats.put("gif","image/gif");
		_graphicFormats.put("png","image/png");
		_graphicFormats.put("bmp","image/bmp");
		_graphicFormats.put("svg","image/svg+xml");
	}
	
	public String getFormat(String graphicURL) {
		
		int size = graphicURL.length(); 
		int pointPos = graphicURL.indexOf(".");
		String aux = graphicURL;
		while (pointPos != -1) {
			aux = aux.substring(pointPos+1,size);
			size = aux.length();
			pointPos = aux.indexOf(".");		
		}
		String format = (String)_graphicFormats.get(aux.toLowerCase());
		return format;
	}
}
