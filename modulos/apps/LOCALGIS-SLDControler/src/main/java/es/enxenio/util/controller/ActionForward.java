/**
 * ActionForward.java
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
