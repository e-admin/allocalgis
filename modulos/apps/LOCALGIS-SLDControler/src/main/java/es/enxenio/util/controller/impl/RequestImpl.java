/**
 * RequestImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 10-jun-2004
 *
 */
package es.enxenio.util.controller.impl;

import java.util.HashMap;

import es.enxenio.util.controller.Request;
/**
 * @author enxenio s.l.
 *
 */
public class RequestImpl implements Request {

	private HashMap _parameters;
	
	public RequestImpl() {
		
		_parameters = new HashMap();
	}
	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.Request#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String paramKey) {
		
		return _parameters.get(paramKey);
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.Request#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String paramKey, Object paramValue) {
		
		_parameters.put(paramKey,paramValue);
	}

}
