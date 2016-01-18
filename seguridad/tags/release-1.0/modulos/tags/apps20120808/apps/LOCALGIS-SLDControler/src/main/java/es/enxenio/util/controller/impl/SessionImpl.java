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
