/**
 * DistanceLinkingPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.analysis;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JToolBar;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.DistanceLinkingPanel;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxPlugIn;

public class DistanceLinkingPlugIn extends ToolboxPlugIn
		implements
			ThreadedPlugIn
{
private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext)
	{
		GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
				workbenchContext);

		return new MultiEnableCheck()
				.add(
						checkFactory
								.createWindowWithLayerManagerMustBeActiveCheck())
				.add(
						checkFactory
								.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
				.add(
						checkFactory.createAtLeastNLayersMustExistCheck(2));//  .add(checkFactory.createLayerStreetBeExistCheck())
		// .add(checkFactory.createLayerNumerosPoliciaBeExistCheck());

	}

	private DistanceLinkingPanel dialogPanel = null;
	private PlugInContext localContext;
	private JDialog streetOptions = null;
	private JToolBar streetToolBar = new JToolBar();
	private ToolboxDialog toolbox;
  private String toolBarCategory = "DistanceLinkingPlugIn.category";
	public DistanceLinkingPlugIn() {
		
	}

	public boolean execute(PlugInContext context) throws Exception
	{
		super.execute(context);
		this.localContext = context;
	
		dialogPanel.setup(context);
		return false;
	}

	public ImageIcon getIcon()
	{
		return IconLoader.icon("DistanceLinking.gif");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vividsolutions.jump.workbench.plugin.PlugIn#getName()
	 */
	public String getName()
	{

		return "ConnectFeaturesByDistancePlugIn";
	}

	public void initialize(PlugInContext context) throws Exception
	{
		 String pluginCategory = aplicacion.getString(toolBarCategory);
		context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(pluginCategory)
				.addPlugIn(this.getIcon(), this,
						createEnableCheck(context.getWorkbenchContext()),
						context.getWorkbenchContext());

		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vividsolutions.jump.workbench.ui.toolbox.ToolboxPlugIn#initializeToolbox(com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog)
	 */
	protected void initializeToolbox(ToolboxDialog toolbox)
	{
	    this.toolbox=toolbox;
		//if (dialogPanel == null) 
			dialogPanel = new DistanceLinkingPanel(toolbox);
		dialogPanel.setup(localContext);
		toolbox.getContentPane().add(dialogPanel);
		//toolbox.add(new DrawLinkingVectorTool(), false, toolbox,
		// warpingPanel);
		
	}

	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception
	{

	}

}