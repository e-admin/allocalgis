/*
 * Created on 09-jun-2004
 *
 */
package es.enxenio.util.controller;

import java.awt.Component;

import es.enxenio.util.ui.PanelContainer;

/**
 * @author enxenio s.l.
 *
 */
public interface ActionForward {
	
	/**Este método configura la action forward basándose en los parámetros almacenados en un objeto Request
	 * @param request Instancia que contendrá los parámetros necesarios para llevar a cabo la operación
     */
	public void configure(Request request);
	
	public String getTitle();
	public Component getComponent();
	public void setContainer(PanelContainer container);
	public boolean windowClosing();
}
