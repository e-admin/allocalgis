/*
 * 
 * Created on 07-jun-2005 by juacas
 *
 * 
 */
package com.vividsolutions.jump.workbench;

import com.geopista.editor.WorkBench;
import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.driver.DriverManager;
import com.vividsolutions.jump.workbench.plugin.PlugInManager;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public interface JUMPWorkbench extends WorkBench
{
	

	public abstract DriverManager getDriverManager();

	/**
	 * The properties file; not to be confused with the WorkbenchContext
	 * properties.
	 */
	public abstract WorkbenchProperties getProperties();

	public abstract WorkbenchGuiComponent getGuiComponent();

	public abstract WorkbenchFrame getFrame();

	public abstract WorkbenchContext getContext();

	public abstract PlugInManager getPlugInManager();

	//<<TODO>> Make some properties persistent using a #makePersistent(key) method. [Jon Aquino]
	public abstract Blackboard getBlackboard();
}