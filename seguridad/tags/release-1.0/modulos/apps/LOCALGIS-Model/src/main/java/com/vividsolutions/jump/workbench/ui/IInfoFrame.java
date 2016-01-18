package com.vividsolutions.jump.workbench.ui;

import javax.swing.JPanel;

import com.geopista.editor.TaskComponent;
import com.vividsolutions.jump.workbench.model.ILayerManager;


public interface IInfoFrame {

	public abstract ILayerManager getLayerManager();

	public abstract TaskComponent getTaskComponent();

	public abstract ITaskFrame getTaskFrame();

	public abstract JPanel getAttributeTab();

	public abstract JPanel getGeometryTab();

	public abstract void setSelectedTab(JPanel tab);

	public abstract IInfoModel getModel();

	public abstract void surface();

	public abstract ISelectionManager getSelectionManager();

	public abstract LayerNamePanel getLayerNamePanel();

	public abstract ILayerViewPanel getLayerViewPanel();

}