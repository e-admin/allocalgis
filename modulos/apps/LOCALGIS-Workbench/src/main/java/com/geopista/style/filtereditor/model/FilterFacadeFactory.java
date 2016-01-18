/**
 * FilterFacadeFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
