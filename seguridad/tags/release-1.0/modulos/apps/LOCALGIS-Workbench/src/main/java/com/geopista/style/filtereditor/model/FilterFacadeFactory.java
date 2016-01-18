/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model;

import es.enxenio.util.configuration.ConfigurationParametersManager;
import es.enxenio.util.exceptions.InternalErrorException;

/**
 * @author enxenio s.l.
 *
 */
public class FilterFacadeFactory {
	
	private final static String FACADE_NAME_PARAMETER =
			"FilterFacadeFactory/delegateClassName";

	private final static Class delegateClass = getDelegateClass();
	
	private FilterFacadeFactory() {}
	
	private static Class getDelegateClass() {
	
		Class theClass = null;
		try {
			String delegateClassName = 
				ConfigurationParametersManager.getParameter(FACADE_NAME_PARAMETER);
			theClass = Class.forName(delegateClassName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return theClass;
		
	}
	
	public static FilterFacade getDelegate() throws InternalErrorException {
		
		try {		
			return (FilterFacade) delegateClass.newInstance();
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
		
	}

	public static void main (String[] args) {}

}
