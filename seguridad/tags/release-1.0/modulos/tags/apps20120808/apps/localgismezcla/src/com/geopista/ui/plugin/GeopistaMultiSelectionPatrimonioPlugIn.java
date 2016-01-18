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
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class GeopistaMultiSelectionPatrimonioPlugIn extends AbstractPlugIn
{
   ApplicationContext appContext=AppContext.getApplicationContext();
   private String toolBarCategory = "GeopistaMultiSelectionPatrimonioPlugIn.category";
   
   static public final ImageIcon ICON=IconLoader.icon("multiselection.gif");
	 public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
            .add(checkFactory.createLayerInmueblesBeExistCheck());

    }

  public String getName() {
        return appContext.getI18nString("GeopistaMultiSelectionPatrimonioPlugInDescription");
  }
  
  public void initialize(PlugInContext context) throws Exception
  {
      String pluginCategory = appContext.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(
      		getIcon(), this,
      		createEnableCheck(context.getWorkbenchContext()),
			context.getWorkbenchContext());


        JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

       
        featureInstaller.addPopupMenuItem(popupMenu, this,
        		appContext.getI18nString(getName()), false,
				GUIUtil.toSmallIcon(ICON),
				createEnableCheck(context.getWorkbenchContext()));
      
    
  }

  public boolean execute (PlugInContext context) throws Exception
  {
       JDialog d = new com.geopista.app.patrimonio.GeopistaMultiSelectionPatrimonioDialog(context);
       return true;
  }
  
   public ImageIcon getIcon() {
        return ICON;
    }

  
}
