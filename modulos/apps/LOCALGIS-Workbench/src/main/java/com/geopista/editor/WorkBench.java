/**
 * WorkBench.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.editor;

import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.WorkbenchProperties;
import com.vividsolutions.jump.workbench.driver.DriverManager;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.plugin.PlugInManager;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
/**
 *Interfaz que implementa el objeto que inicia y lanza el workbench 
 * como:
 *  - JUMPWorkBench
 *  - GEOPISTAWorkBench
 *  - GeopistaEditor
 * 
 */
public interface WorkBench 
{
  public WorkbenchFrame getFrame();
  public WorkbenchGuiComponent getGuiComponent();
  public Blackboard getBlackboard();
  public WorkbenchProperties getProperties();
  public PlugInManager getPlugInManager();
  public DriverManager getDriverManager();
  public WorkbenchContext getContext();
  public void addLayerListener(LayerListener layerListener);
}