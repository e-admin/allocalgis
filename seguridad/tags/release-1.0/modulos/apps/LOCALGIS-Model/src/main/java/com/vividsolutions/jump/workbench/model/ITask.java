/*
 * 
 * Created on 03-jun-2005 by juacas
 *
 * 
 */
package com.vividsolutions.jump.workbench.model;

import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.event.InternalFrameListener;

import com.geopista.editor.TaskComponent;

import com.vividsolutions.jump.workbench.ui.IInfoFrame;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.ISelectionManager;
import com.vividsolutions.jump.workbench.ui.ITaskFrame;

import com.vividsolutions.jump.workbench.ui.LayerNamePanel;


/**
 * TODO Documentación
 * @author juacas
 *
 */
public interface ITask
{
	public abstract void add(ITaskNameListener nameListener);

	public abstract void setName(String name);

	public abstract void setProjectFile(File projectFile);

	public abstract ILayerManager getLayerManager();

	public abstract File getProjectFile();

	public abstract String toString();

	public abstract String getName();

	public abstract Collection getCategories();

	/**
	 * Called by Java2XML
	 */
	public abstract void addCategory(Category category);

	public abstract ITaskFrame getTaskFrame();

	public abstract TaskComponent getTaskComponent();

	public abstract void addInternalFrameListener(InternalFrameListener l);

	public abstract void setVisible(boolean aFlag);

	public abstract void setFrameIcon(Icon icon);

	public abstract ISelectionManager getSelectionManager();

	public abstract void setMaximum(boolean b)
			throws java.beans.PropertyVetoException;

	public abstract void setSelected(boolean selected)
			throws java.beans.PropertyVetoException;

	public abstract void requestFocus();

	public abstract void moveToFront();

	public abstract ITask getTask();

	public abstract ILayerViewPanel getLayerViewPanel();

	public abstract boolean isVisible();

	public abstract IInfoFrame getInfoFrame();

	public abstract LayerNamePanel getLayerNamePanel();
}