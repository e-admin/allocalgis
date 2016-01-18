/**
 * AbstractPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 06-ago-2004
 */
package es.enxenio.util.ui.impl;

import java.awt.Component;

import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.Request;
import es.enxenio.util.ui.PanelContainer;

/**
 * @author luaces
 */
public abstract class AbstractPanel extends javax.swing.JPanel implements ActionForward {

	public void configure(Request request) {
		System.out.println(getClass().getName() + ": implement configure!");
	}

	public String getTitle() {
		System.out.println(getClass().getName() + ": implement getTitle!");
		return "Nonamed";		
	}

	public boolean windowClosing() {
		return true;
	}

	public Component getComponent() {
			return this;
	}
	
	public void setContainer(PanelContainer container) {
			_container = container;
	}
	
	protected PanelContainer _container;
}
