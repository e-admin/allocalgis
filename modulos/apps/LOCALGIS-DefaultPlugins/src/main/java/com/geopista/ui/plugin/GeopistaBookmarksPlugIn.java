/**
 * GeopistaBookmarksPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.dialogs.GeopistaGestionBookmarksDialog;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;


public class GeopistaBookmarksPlugIn extends AbstractPlugIn 
{

  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private String toolBarCategory = "GeopistaBookmarksPlugIn.category";
  
  public String getName() {
        return "Bookmarks";
  }

  public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
           .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck());
    }
private JFrame d; // persistente entre llamadas
  public boolean execute(PlugInContext context) throws Exception {
    
    if (d!=null && d.isFocusableWindow())
    	d.show();
    else
    d = new GeopistaGestionBookmarksDialog(context);
    return true;
  }
  public void initialize(PlugInContext context) throws Exception {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
            this,
            createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());
    }

  public ImageIcon getIcon() {
        return IconLoader.icon("bookmark.gif");
  }
}
