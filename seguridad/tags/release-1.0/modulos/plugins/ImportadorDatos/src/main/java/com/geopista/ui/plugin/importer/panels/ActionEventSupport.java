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
