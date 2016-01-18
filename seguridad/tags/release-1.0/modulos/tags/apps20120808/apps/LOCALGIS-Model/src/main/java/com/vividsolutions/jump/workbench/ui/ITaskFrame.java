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