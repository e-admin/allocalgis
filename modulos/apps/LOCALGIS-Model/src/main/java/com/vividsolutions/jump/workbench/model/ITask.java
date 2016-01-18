/**
 * ITask.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 03-jun-2005 by juacas
 *
 * 
 */
package com.vividsolutions.jump.workbench.model;

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