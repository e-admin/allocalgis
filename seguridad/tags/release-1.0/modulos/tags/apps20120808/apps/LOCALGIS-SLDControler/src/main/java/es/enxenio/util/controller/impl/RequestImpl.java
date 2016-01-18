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
