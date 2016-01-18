/**
 * IInfoFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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