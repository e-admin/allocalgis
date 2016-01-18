/*
 * 
 * Created on 22-sep-2005 by juacas
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

public class WorkbenchFrame extends AbstractWorkbenchFrame
{

	public WorkbenchFrame()
	{
	super();
	// TODO Auto-generated constructor stub
	}

	public void addEasyKeyListener(KeyListener l)
	{
	// TODO Auto-generated method stub

	}

	public void removeEasyKeyListener(KeyListener l)
	{
	// TODO Auto-generated method stub

	}

	public String getMBCommittedMemory()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public void setEnvelopeRenderingThreshold(int newEnvelopeRenderingThreshold)
	{
	// TODO Auto-generated method stub

	}

	public void setMaximumFeatureExtentForEnvelopeRenderingInPixels(
			int newMaximumFeatureExtentForEnvelopeRenderingInPixels)
	{
	// TODO Auto-generated method stub

	}

	public void log(String message)
	{
	// TODO Auto-generated method stub

	}

	public String getLog()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public void setMinimumFeatureExtentForAnyRenderingInPixels(
			int newMinimumFeatureExtentForAnyRenderingInPixels)
	{
	// TODO Auto-generated method stub

	}

	public void displayLastStatusMessage()
	{
	// TODO Auto-generated method stub

	}

	public void setStatusMessage(String message)
	{
	// TODO Auto-generated method stub

	}

	public void setTimeMessage(String message)
	{
	// TODO Auto-generated method stub

	}

	public TaskComponent getActiveTaskComponent()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public JInternalFrame getActiveInternalFrame()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public JInternalFrame[] getInternalFrames()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public TitledPopupMenu getCategoryPopupMenu()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public WorkbenchContext getContext()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public Container getDesktopPane()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public int getEnvelopeRenderingThreshold()
	{
	// TODO Auto-generated method stub
	return 0;
	}

	public TitledPopupMenu getLayerNamePopupMenu()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public TitledPopupMenu getWMSLayerNamePopupMenu()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public LayerViewPanelListener getLayerViewPanelListener()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public Map getNodeClassToPopupMenuMap()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public LayerNamePanelListener getLayerNamePanelListener()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public int getMaximumFeatureExtentForEnvelopeRenderingInPixels()
	{
	// TODO Auto-generated method stub
	return 0;
	}

	public int getMinimumFeatureExtentForAnyRenderingInPixels()
	{
	// TODO Auto-generated method stub
	return 0;
	}

	public HTMLFrame getOutputFrame()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public WorkbenchToolBar getToolBar()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public void activateFrame(JInternalFrame frame)
	{
	// TODO Auto-generated method stub

	}

	public void addInternalFrame(JInternalFrame internalFrame)
	{
	// TODO Auto-generated method stub

	}

	public void addInternalFrame(JInternalFrame internalFrame,
			boolean alwaysOnTop, boolean autoUpdateToolBar)
	{
	// TODO Auto-generated method stub

	}

	public TaskFrame addTaskFrame()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public Task createTask()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public TaskFrame addTaskFrame(Task task)
	{
	// TODO Auto-generated method stub
	return null;
	}

	public TaskFrame addTaskFrame(TaskFrame taskFrame)
	{
	// TODO Auto-generated method stub
	return null;
	}

	public void flash(HTMLFrame frame)
	{
	// TODO Auto-generated method stub

	}

	public void handleThrowable(Throwable t)
	{
	// TODO Auto-generated method stub

	}

	public boolean hasInternalFrame(JInternalFrame internalFrame)
	{
	// TODO Auto-generated method stub
	return false;
	}

	public void removeInternalFrame(JInternalFrame internalFrame)
	{
	// TODO Auto-generated method stub

	}

	public void warnUser(String warning)
	{
	// TODO Auto-generated method stub

	}

	public void zoomChanged(Envelope modelEnvelope)
	{
	// TODO Auto-generated method stub

	}

	public Set getChoosableStyleClasses()
	{
	// TODO Auto-generated method stub
	return null;
	}

	public void addChoosableStyleClass(Class choosableStyleClass)
	{
	// TODO Auto-generated method stub

	}

	public void addKeyboardShortcut(int keyCode, int modifiers, PlugIn plugIn,
			EnableCheck enableCheck)
	{
	// TODO Auto-generated method stub

	}

	public WorkbenchToolBar getToolBar(String toolBarName)
	{
	// TODO Auto-generated method stub
	return null;
	}

	public JFrame getMainFrame()
	{
	// TODO Auto-generated method stub
	return null;
	}

}
