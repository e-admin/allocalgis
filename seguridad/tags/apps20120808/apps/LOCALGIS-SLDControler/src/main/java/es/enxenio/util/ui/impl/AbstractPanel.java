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
