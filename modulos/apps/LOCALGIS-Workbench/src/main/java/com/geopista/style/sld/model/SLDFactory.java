/**
 * SLDFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 07-jun-2004
 *
 */
package com.geopista.style.sld.model;

import java.awt.Color;
import java.util.ResourceBundle;

import com.geopista.style.sld.model.impl.SLDStyleImpl;

import es.enxenio.util.exceptions.InternalErrorException;


/**
 * @author enxenio s.l.
 *
 */
public class SLDFactory {
	
	private final static String FACADE_NAME_PARAMETER =
			"SLD/FacadeFactory/delegateClassName";

	private final static Class delegateClass = getDelegateClass();
	
	private SLDFactory() {}
	
	private static Class getDelegateClass() {
	
		Class theClass = null;
		try {
			ResourceBundle Rb = ResourceBundle.getBundle("GeoPista");
			String delegateClassName = Rb.getString(FACADE_NAME_PARAMETER); 
			theClass = Class.forName(delegateClassName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return theClass;
		
	}
	
	public static SLDFacade getDelegate() throws InternalErrorException {
		
		try {		
			return (SLDFacade) delegateClass.newInstance();
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
		
	}

	public static SLDStyle createSLDStyle(String fileName, String styleName, String layerName) {
			return new SLDStyleImpl(fileName, styleName, layerName);
	}

	public static SLDStyle createSLDStyle(String sld, String layerName) {
			return new SLDStyleImpl(sld,layerName);
	}

	public static SLDStyle createDefaultSLDStyle(String layerName) {
			return new SLDStyleImpl(Color.WHITE, Color.BLACK, layerName);
	}
}
