/**
 * JUMPWorkbench.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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