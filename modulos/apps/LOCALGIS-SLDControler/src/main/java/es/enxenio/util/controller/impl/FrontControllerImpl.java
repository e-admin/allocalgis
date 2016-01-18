/**
 * FrontControllerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 09-jun-2004
 *
 */
package es.enxenio.util.controller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import es.enxenio.util.configuration.ConfigurationParametersManager;
import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;

/**
 * @author enxenio s.l.
 *
 */
public class FrontControllerImpl implements FrontController {

	private HashMap _acciones;
	private HashMap _forwards;
	private SAXBuilder _builder;
	static private FrontControllerImpl _frontController;
	private final static String ACTIONS_FILE_PARAMETER =
			"FrontControllerFactory/ActionsURL";
	private final static String ACTIONFORWARDS_FILE_PARAMETER =
			"FrontControllerFactory/ActionForwardsURL";
	
	/**Clase de tipo Singleton (instancia única)*/
	private FrontControllerImpl() {
		_acciones = new HashMap();
		_forwards = new HashMap();
		_builder = new SAXBuilder();
	}
	
	public static FrontControllerImpl getInstance() {
		if (_frontController == null) {
			try {
				_frontController = new FrontControllerImpl();
				Class frontControllerImplClass = FrontControllerImpl.class;
				ClassLoader classLoader = frontControllerImplClass.getClassLoader();
				InputStream actionsInputStream = classLoader.getResourceAsStream(ConfigurationParametersManager.getParameter(ACTIONS_FILE_PARAMETER));
				_frontController.addActions(new InputStreamReader(actionsInputStream));
				InputStream forwardsInputStream = classLoader.getResourceAsStream(ConfigurationParametersManager.getParameter(ACTIONFORWARDS_FILE_PARAMETER));
				_frontController.addActionForwards(new InputStreamReader(forwardsInputStream));
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return _frontController;
	}
	
	public void addActions(InputStreamReader isr) {
		
		Document doc = new Document();
		try {
			doc = _builder.build(isr);	
		} catch (JDOMException e) {
			System.err.println(e);
		} catch (IOException e) {
            System.err.println(e);
        }
		Element docRoot = doc.getRootElement();
		List actionList = docRoot.getChildren();
		Iterator actionListIterator = actionList.iterator();
		while (actionListIterator.hasNext()) {
			Element element = (Element) actionListIterator.next();
			if (element.getName().equals("Include")) {
				String includeResource = element.getText();
				Class frontControllerImplClass = FrontControllerImpl.class;
				ClassLoader classLoader = frontControllerImplClass.getClassLoader();
				InputStream inputStream = classLoader.getResourceAsStream(includeResource);
				addActions(new InputStreamReader(inputStream));
			}
			else {
				Element actionKeyElement = getElement(element,"Key");
				Element actionClassElement = getElement(element,"Class");
				String actionKey = actionKeyElement.getText();
				String actionClass = actionClassElement.getText();
				_acciones.put(actionKey,actionClass);
			}
		}
	}
	
	public void addActionForwards(InputStreamReader isr) {
		
		Document doc = new Document();
		try {
			doc = _builder.build(isr);	
		} catch (JDOMException e) {
			System.err.println(e);
		} catch (IOException e) {
            System.err.println(e);
        }
		Element docRoot = doc.getRootElement();
		List forwardList = docRoot.getChildren();
		Iterator forwardListIterator = forwardList.iterator();
		while (forwardListIterator.hasNext()) {
			Element element = (Element) forwardListIterator.next();
			if (element.getName().equals("Include")) {
				String includeResource = element.getText();
				Class frontControllerImplClass = FrontControllerImpl.class;
				ClassLoader classLoader = frontControllerImplClass.getClassLoader();
				InputStream inputStream = classLoader.getResourceAsStream(includeResource);
				addActionForwards(new InputStreamReader(inputStream));
			}
			else {
				Element actionKeyElement = getElement(element,"Key");
				Element actionClassElement = getElement(element,"Class");
				String actionKey = actionKeyElement.getText();
				String actionClass = actionClassElement.getText();
				_forwards.put(actionKey,actionClass);
			}
		}
	}
	
	public Action getAction(String actionKey) {
		
		String actionClass = (String)_acciones.get(actionKey);
		Action action = null;
		try {
			action = (Action)Class.forName(actionClass).newInstance();
		} catch (Exception e) {
			System.err.println(e);
		}
		return action;
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.FrontController#setAction(java.lang.String, java.lang.String)
	 */
	public void setAction(String actionKey, String actionClass) {
		
		_acciones.put(actionKey,actionClass);
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.FrontController#getForward(java.lang.String)
	 */
	public ActionForward getForward(String forwardKey) {
		
		String actionForwardClass = (String) _forwards.get(forwardKey);
		ActionForward actionForward = null;
		try {
			actionForward = (ActionForward)Class.forName(actionForwardClass).newInstance();
			
		} catch (Exception e) {
			System.err.println(e);
		}
		return actionForward;
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.FrontController#setForward(java.lang.String, java.lang.String)
	 */
	public void setForward(String forwardKey, String actionForward) {
		
		_forwards.put(forwardKey,actionForward);
	}
	
	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.FrontController#clearActions()
	 */
	public void clearActions() {
		
		_acciones.clear();
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.FrontController#clearActionForwards()
	 */
	public void clearActionForwards() {
		
		_forwards.clear();
	}

	private Element getElement(Element parentElement,String elementName) {

		Element elementFound = null;
		List children = parentElement.getChildren();
		Iterator childrenIterator = children.iterator();
		while (childrenIterator.hasNext()) {
			Element element = (Element) childrenIterator.next();
			if ((element.getName()).equals(elementName)) {
				elementFound = element;
			}
		}
		return elementFound;
	}
}
