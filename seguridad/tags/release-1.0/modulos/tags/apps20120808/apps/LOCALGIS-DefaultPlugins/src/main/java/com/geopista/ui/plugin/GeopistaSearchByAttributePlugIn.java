package com.geopista.ui.plugin;
/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/
import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.GeopistaBuscarCapasPanel;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxPlugIn;


public class GeopistaSearchByAttributePlugIn extends ToolboxPlugIn 
{

  ApplicationContext appContext=AppContext.getApplicationContext();
  


  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();



private PlugInContext localContext;



private GeopistaBuscarCapasPanel dialogPanel;  
    

  public String getName() {
        return appContext.getI18nString("GeopistaSearchByAttributePlugInDescription");
  }

  public boolean execute(PlugInContext context) throws Exception {
	this.localContext = context;
  	 super.execute(context);
  	dialogPanel.refresh(context);
	return false;
  }
  public void initialize(PlugInContext context) throws Exception {
  	 String pluginCategory = AppContext.getApplicationContext().getString("GeopistaSearchByAttributes.category");
		context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(pluginCategory)
				.addPlugIn(this.getIcon(), this,
						createEnableCheck(context.getWorkbenchContext()),
						context.getWorkbenchContext());
     
    }

  /**
 * @param workbenchContext
 * @return
 */
private EnableCheck createEnableCheck(WorkbenchContext workbenchContext)
{
	GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
			workbenchContext);

	return new MultiEnableCheck()
			.add(
					checkFactory
							.createWindowWithLayerManagerMustBeActiveCheck())
			.add(
					checkFactory
							.createWindowWithAssociatedTaskFrameMustBeActiveCheck());
}

public ImageIcon getIcon() {
        return IconLoader.icon("Attribute.gif");
  }

/* (non-Javadoc)
 * @see com.vividsolutions.jump.workbench.ui.toolbox.ToolboxPlugIn#initializeToolbox(com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog)
 */
protected void initializeToolbox(ToolboxDialog toolbox)
{
	dialogPanel = new GeopistaBuscarCapasPanel(null,localContext, toolbox);
	dialogPanel.refresh(localContext);
	toolbox.getContentPane().add(dialogPanel);
	
}
}