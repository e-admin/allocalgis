/**
 * GraphicFormatManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
