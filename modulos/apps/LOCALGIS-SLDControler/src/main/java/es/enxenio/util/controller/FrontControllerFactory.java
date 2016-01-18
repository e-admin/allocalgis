/**
 * FrontControllerFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 15-jun-2004
 */
package es.enxenio.util.controller;

import es.enxenio.util.configuration.ConfigurationParametersManager;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;

/**
 * @author luaces
 */
public class FrontControllerFactory {
	private final static String REQUEST_NAME_PARAMETER =
			"FrontControllerFactory/RequestClassName";

	public static Request createRequest() {
		Class theClass = null;
		try {
			String requestClassName = ConfigurationParametersManager.getParameter(REQUEST_NAME_PARAMETER);
			theClass = Class.forName(requestClassName);
			return (Request) theClass.newInstance();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static FrontController getFrontController() {
		
		return FrontControllerImpl.getInstance();
	}

	public static Session getSession() {
		return SessionImpl.getInstance();
	}
}
