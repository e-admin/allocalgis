/**
 * ITaskFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui;

import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;

import com.geopista.editor.TaskComponent;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.ITaskNameListener;


public interface ITaskFrame {

	public abstract TaskComponent getTaskFrame();

	public abstract ISelectionManager getSelectionManager();

	public abstract ILayerManager getLayerManager();

	public abstract IInfoFrame getInfoFrame();

	public abstract LayerNamePanel getLayerNamePanel();

	public abstract ILayerViewPanel getLayerViewPanel();

	public abstract ITask getTask();

	public abstract JInternalFrame internalFrameClone();

	public abstract void taskNameChanged(String name);

	public abstract JSplitPane getSplitPane();

	public abstract void add(ITaskNameListener nameListener);

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.TaskFrameProxy#getTaskComponent()
	 */
	public abstract TaskComponent getTaskComponent();

}