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

package com.geopista.ui.plugin;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.dialogs.GeopistaMapPropertiesDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * GeopistaMapPropertiesPlugIn
 * PlugIn que sirve para configurar determinadas propiedades del mapa
 */
public class GeopistaMapPropertiesPlugIn extends AbstractPlugIn
{
  private ApplicationContext appContext=AppContext.getApplicationContext();
  private String toolBarCategory = "GeopistaMapPropertiesPlugIn.category";

  public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
           .add(checkFactory.createTaskWindowMustBeActiveCheck()) ;

  }
  
  /**
   * getName()
   * Método para obtener la descripción del plugin
   * 
   * 
   */
  public String getName() {
        return appContext.getI18nString("GeopistaMapPropertiesPlugInDescription");
  }

  public boolean execute(PlugInContext context) throws Exception {


    JDialog d = new GeopistaMapPropertiesDialog(context);
    return true;
  }
  public void initialize(PlugInContext context) throws Exception {
       String pluginCategory = appContext.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
            this,
            createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());
    }

  public ImageIcon getIcon() {
        return IconLoader.icon("mapproperties.gif");
  }
}