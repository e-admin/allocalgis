package com.geopista.editor;
import javax.swing.Icon;
import javax.swing.event.InternalFrameListener;

import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.ITaskNameListener;
import com.vividsolutions.jump.workbench.ui.IInfoFrame;

import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.ISelectionManager;

/**
 * Debe ser un Component
 * @author juacas
 *
 */
public interface TaskComponent
{
  public boolean isVisible();
  public ILayerViewPanel getLayerViewPanel();
  // REVISAR: Creo que getTask debe devolver el objeto 
  //Task, el proyecto que agrupa varias capas en lugar 
  //de la ventana gráfica que es más bien TaskFrame [Juan Pablo]
  //public TaskComponent getTask();
  public ITask getTask();
  public void moveToFront();
  public void requestFocus();
  public void setSelected(boolean selected) throws java.beans.PropertyVetoException;
  public void setMaximum(boolean b) throws java.beans.PropertyVetoException;
  public ISelectionManager getSelectionManager();
  public void setFrameIcon(Icon icon);
  public void setVisible(boolean aFlag);
  public void addInternalFrameListener(InternalFrameListener l);
  public TaskComponent getTaskFrame();
  public ILayerManager getLayerManager();
  public String getName();
  public void add(ITaskNameListener nameListener);
  public IInfoFrame getInfoFrame();
  public LayerNamePanel getLayerNamePanel();
  public String getTitle();
}