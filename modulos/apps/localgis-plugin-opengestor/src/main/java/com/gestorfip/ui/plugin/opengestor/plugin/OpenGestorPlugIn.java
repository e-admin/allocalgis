/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.gestorfip.ui.plugin.opengestor.plugin;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;

import com.geopista.app.AppContext;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.ui.plugin.opengestor.images.IconLoader;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class OpenGestorPlugIn extends AbstractPlugIn {
	
	private static AppContext appContext = (AppContext) AppContext.getApplicationContext();
	
	
    public boolean execute(PlugInContext context) throws Exception {

        JSplitPane splitpane = (JSplitPane)appContext.getBlackboard().get(ConstantesGestorFIP.OPEN_GESTORFIP);
       
        
       if( splitpane.getDividerLocation() <= 1){
    	   splitpane.setDividerLocation(400);
       }
       else{
    	   splitpane.setDividerLocation(0);
       }
      
        return true;
    }

    public void initialize(PlugInContext context) throws Exception {

    	 context.getFeatureInstaller().addMainMenuItem(this,
                 new String[] { "OpenGestorFip" },
                 getName(),
                 false,
                 null,
                 null);


    	 context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar("OpenGestorFip").addPlugIn(
    			 								getIcon(), this,
    			 								createEnableCheck(context.getWorkbenchContext()),
    			 								context.getWorkbenchContext());
    	
    	
    	JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();

        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addPopupMenuItem(popupMenu,
                this, AppContext.getApplicationContext().getI18nString(this.getName()), false, null,
                createEnableCheck(context.getWorkbenchContext()));
                
         

      }
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck();
    }
    
    public ImageIcon getIcon() {
		return IconLoader.icon("OpenGestorFip.gif");
	}
}
