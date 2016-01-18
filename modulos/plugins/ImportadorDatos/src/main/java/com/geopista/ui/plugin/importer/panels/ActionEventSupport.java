/**
 * ActionEventSupport.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.importer.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.EventListenerList;

public class ActionEventSupport {
	/**
	 * Activa Action events
	 */
	 private EventListenerList actionListeners = new EventListenerList();  //  @jve:decl-index=0:

	    public void addActionListener(ActionListener listener) {
	        actionListeners.add(ActionListener.class,listener);
	    }
	    
	    public void removeActionListener(ActionListener listener) {
	        actionListeners.remove(ActionListener.class,listener);
	    }

	    public void fireActionEvent(Object source, int id, String command) {
	    	ActionListener[] listeners=actionListeners.getListeners(ActionListener.class);
	    	for (ActionListener actionListener : listeners) 
	    	{
	    		actionListener.actionPerformed(new ActionEvent(source, id, command));
			}
	        
	    }
	    public void forwardActionEvent(ActionEvent ev) {
	    	ActionListener[] listeners=actionListeners.getListeners(ActionListener.class);
	    	for (ActionListener actionListener : listeners) 
	    	{
	    		actionListener.actionPerformed(ev);
			}
	        
	    }

}
