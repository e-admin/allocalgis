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
