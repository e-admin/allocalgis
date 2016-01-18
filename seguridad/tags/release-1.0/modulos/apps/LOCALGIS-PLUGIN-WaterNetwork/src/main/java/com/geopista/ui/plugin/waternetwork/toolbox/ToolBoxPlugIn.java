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