/**
 * ToolBoxPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.waternetwork.toolbox;

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

public abstract class ToolBoxPlugIn extends AbstractPlugIn implements ThreadedPlugIn{
	/**
     * @return the toolbox for this plug-in class.
     */
    private static AppContext app = (AppContext) AppContext.getApplicationContext();
	private ToolboxDialog toolbox = null;
    protected abstract void initializeToolbox(ToolboxDialog toolbox);

    public ToolboxDialog getToolbox(WorkbenchContext context){
        if (toolbox == null) {
        	toolbox = new ToolboxDialog(context);
            toolbox.setTitle(app.getI18nString(getName()));
            initializeToolbox(toolbox);       
            toolbox.finishAddingComponents();
        }
        return toolbox;
    }    

    public void initialize(PlugInContext context) throws Exception{
    	createMainMenuItem(new String[] { ("View") },
            GUIUtil.toSmallIcon(EditingPlugIn.ICON), context.getWorkbenchContext());		  
	}

    /**
     * Toolbox subclasses can override this method to implement their
     * own behaviour when the plug-in is called. Remember to call
     * super.execute to make the toolbox visible.
     */
    public boolean execute(PlugInContext context) throws Exception{
        reportNothingToUndoYet(context);
        getToolbox(context.getWorkbenchContext()).setVisible(!getToolbox(context.getWorkbenchContext()).isVisible());
        return true;
    }

    /**
     * Creates a menu item with a checkbox beside it that appears when the toolbox
     * is visible.
     * @param icon null to leave unspecified
     */
    public void createMainMenuItem(String[] menuPath, Icon icon, final WorkbenchContext context) throws Exception{
        new FeatureInstaller(context)
            .addMainMenuItem(this, menuPath, app.getI18nString(getName())+"...", true, icon, new EnableCheck(){
            public String check(JComponent component){
                ((JCheckBoxMenuItem) component).setSelected(getToolbox(context).isVisible());
                return null;
            }
        });
    }
}