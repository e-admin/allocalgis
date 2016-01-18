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

package com.geopista.ui.plugin.toolboxnetwork;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public abstract class ToolboxNetworkPlugIn extends AbstractPlugIn implements ThreadedPlugIn{
    /**
     * @return the toolbox for this plug-in class.
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
//    public ToolboxNetworkDialog getToolbox(WorkbenchContext context) {
    public ToolboxDialog getToolbox(WorkbenchContext context) {
        if (toolbox == null) {
//            toolbox = new ToolboxNetworkDialog(context);
        	toolbox = new ToolboxDialog(context);
            toolbox.setTitle(aplicacion.getI18nString(getName()));
            initializeToolbox(toolbox);       
            toolbox.finishAddingComponents();
        }
        return toolbox;
    }
    
//    private ToolboxNetworkDialog toolbox = null;
    private ToolboxDialog toolbox = null;
    
//    protected abstract void initializeToolbox(ToolboxNetworkDialog toolbox);
    protected abstract void initializeToolbox(ToolboxDialog toolbox);
    
    public void initialize(PlugInContext context) throws Exception {
		
    	createMainMenuItem(new String[] { ("View") },
                GUIUtil.toSmallIcon(EditingPlugIn.ICON), context.getWorkbenchContext());		  
	}

    /**
     * Toolbox subclasses can override this method to implement their
     * own behaviour when the plug-in is called. Remember to call
     * super.execute to make the toolbox visible.
     */
    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        getToolbox(context.getWorkbenchContext()).setVisible(!getToolbox(context.getWorkbenchContext()).isVisible());
        return true;
    }

    /**
     * Creates a menu item with a checkbox beside it that appears when the toolbox
     * is visible.
     * @param icon null to leave unspecified
     */
    public void createMainMenuItem(String[] menuPath, Icon icon, final WorkbenchContext context) 
        throws Exception {
        new FeatureInstaller(context)
            .addMainMenuItem(this, menuPath, aplicacion.getI18nString(getName())+"...", true, icon, new EnableCheck() {
            public String check(JComponent component) {
                ((JCheckBoxMenuItem) component).setSelected(getToolbox(context).isVisible());
                return null;
            }
        });
    }

}
