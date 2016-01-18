/**
 * SessionImpl.java
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

import es.enxenio.util.controller.Session;
/**
 * @author enxenio s.l
 *
 */
/**Esta clase será una instancia única (Singleton)*/
public class SessionImpl implements Session {
	
	private HashMap _parameters;
	static private SessionImpl _session;
	
	private SessionImpl() {
		_parameters = new HashMap();
	}
	
	public static SessionImpl getInstance() {
		if (_session == null) {
			_session = new SessionImpl();
		}
		return _session;
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.Session#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String paramKey) {
		
		return _parameters.get(paramKey);
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.Session#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String paramKey, Object paramValue) {
		
		_parameters.put(paramKey, paramValue);
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.Session#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String paramKey) {
		
		_parameters.remove(paramKey);
	}
	
	public void clean() {
		_parameters.clear();
	}

}
