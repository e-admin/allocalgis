package com.geopista.editor;

import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.WorkbenchProperties;
import com.vividsolutions.jump.workbench.driver.DriverManager;
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
}