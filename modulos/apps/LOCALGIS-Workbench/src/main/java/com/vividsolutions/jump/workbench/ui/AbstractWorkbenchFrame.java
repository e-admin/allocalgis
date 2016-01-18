/**
 * AbstractWorkbenchFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 16-may-2005 by juacas
 *
 * 
 */
package com.vividsolutions.jump.workbench.ui;

import java.awt.Container;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import com.geopista.editor.TaskComponent;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugIn;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public abstract class AbstractWorkbenchFrame extends JFrame 
implements  LayerViewPanelContext, ViewportListener
{
	/**
	 * Unlike #add(KeyListener), listeners registered using this method are
	 * notified when KeyEvents occur on this frame's child components. Note:
	 * Bug: KeyListeners registered using this method may receive events
	 * multiple times.
	 * 
	 * @see #addKeyboardShortcut
	 */
	public abstract void addEasyKeyListener(KeyListener l);

	public abstract void removeEasyKeyListener(KeyListener l);

	public abstract String getMBCommittedMemory();

	/**
	 * @param newEnvelopeRenderingThreshold
	 *                   the number of on-screen features above which envelope
	 *                   rendering should occur
	 */
	public abstract void setEnvelopeRenderingThreshold(
			int newEnvelopeRenderingThreshold);

	public abstract void setMaximumFeatureExtentForEnvelopeRenderingInPixels(
			int newMaximumFeatureExtentForEnvelopeRenderingInPixels);

	public abstract void log(String message);

	public abstract String getLog();

	public abstract void setMinimumFeatureExtentForAnyRenderingInPixels(
			int newMinimumFeatureExtentForAnyRenderingInPixels);

	public abstract void displayLastStatusMessage();

	public abstract void setStatusMessage(String message);

	public abstract void setTimeMessage(String message);

	public abstract TaskComponent getActiveTaskComponent();
	public abstract JInternalFrame getActiveInternalFrame();
	public abstract JInternalFrame[] getInternalFrames();

	public abstract TitledPopupMenu getCategoryPopupMenu();

	public abstract WorkbenchContext getContext();

	public abstract Container getDesktopPane();

	public abstract int getEnvelopeRenderingThreshold();

	public abstract TitledPopupMenu getLayerNamePopupMenu();

	public abstract TitledPopupMenu getWMSLayerNamePopupMenu();

	public abstract LayerViewPanelListener getLayerViewPanelListener();

	public abstract Map getNodeClassToPopupMenuMap();

	public abstract LayerNamePanelListener getLayerNamePanelListener();

	public abstract int getMaximumFeatureExtentForEnvelopeRenderingInPixels();

	public abstract int getMinimumFeatureExtentForAnyRenderingInPixels();

	public abstract HTMLFrame getOutputFrame();

	public abstract WorkbenchToolBar getToolBar();

	// Aqui no creo que deba ir un TaskFrame por si se quiere activar otro tipo de JinternalFrame
	public abstract void activateFrame(JInternalFrame frame);

	/**
	 * If internalFrame is a LayerManagerProxy, the close behaviour will be
	 * altered so that the user is prompted if it is the last window on the
	 * LayerManager.
	 */
	public abstract void addInternalFrame(final JInternalFrame internalFrame);

	// REVISAR: No parece necesario evitar el JInternalFrame
	public abstract void addInternalFrame(final JInternalFrame internalFrame,
			boolean alwaysOnTop, boolean autoUpdateToolBar);

	public abstract TaskFrame addTaskFrame();

	public abstract Task createTask();

	public abstract TaskFrame addTaskFrame(Task task);

	public abstract TaskFrame addTaskFrame(TaskFrame taskFrame);

	public abstract void flash(final HTMLFrame frame);

	/**
	 * Can be called regardless of whether the current thread is the AWT event
	 * dispatch thread.
	 * 
	 * @param t
	 *                   Description of the Parameter
	 */
	public abstract void handleThrowable(final Throwable t);
	
	public abstract boolean hasInternalFrame(JInternalFrame internalFrame);

	public abstract void removeInternalFrame(JInternalFrame internalFrame);

	public abstract void warnUser(String warning);

	public abstract void zoomChanged(Envelope modelEnvelope);

	/**
	 * Fundamental Style classes (like BasicStyle, VertexStyle, and LabelStyle)
	 * cannot be removed, and are thus excluded from the choosable Style
	 * classes.
	 */
	public abstract Set getChoosableStyleClasses();

	public abstract void addChoosableStyleClass(Class choosableStyleClass);

	/**
	 * Adds a keyboard shortcut for a plugin. logs plugin exceptions.
	 * 
	 * note - attaching to keyCode 'a', modifiers =1 will detect shift-A
	 * events. It will *not* detect caps-lock-'a'. This is due to
	 * inconsistencies in java.awt.event.KeyEvent. In the unlikely event you
	 * actually do want to also also attach to caps-lock-'a', then make two
	 * shortcuts - one to keyCode 'a' and modifiers =1 (shift-A) and one to
	 * keyCode 'A' and modifiers=0 (caps-lock A).
	 * 
	 * For more details, see the java.awt.event.KeyEvent class - it has a full
	 * explaination.
	 * 
	 * @param keyCode
	 *                   What key to attach to (See java.awt.event.KeyEvent)
	 * @param modifiers 0=
	 *                   none, 1=shift, 2= cntrl, 8=alt, 3=shift+cntrl, etc... See the
	 *                   modifier mask constants in the Event class
	 * @param plugIn
	 *                   What plugin to execute
	 * @param enableCheck
	 *                   Is the key enabled at the moment?
	 */
	public abstract void addKeyboardShortcut(final int keyCode,
			final int modifiers, final PlugIn plugIn,
			final EnableCheck enableCheck);

	public abstract WorkbenchToolBar getToolBar(String toolBarName);

	/* (non-Javadoc)
	 * @see com.geopista.editor.WorkbenchGuiComponent#getMainFrame()
	 */public abstract JFrame getMainFrame();
}