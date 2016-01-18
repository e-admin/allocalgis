/**
 * TaskComponent.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.editor;
import javax.swing.Icon;
import javax.swing.event.InternalFrameListener;

import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.ITaskNameListener;
import com.vividsolutions.jump.workbench.ui.IInfoFrame;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.ISelectionManager;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;

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