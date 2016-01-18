package com.geopista.ui.plugin;

import javax.swing.JInternalFrame;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.GeopistaPreferencesPanel;
import com.geopista.ui.dialogs.SystemConfigPanel;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.OptionsDialog;
import com.vividsolutions.jump.workbench.ui.snap.GridRenderer;

public class GeopistaOptionsPlugIn extends AbstractPlugIn {
    AppContext appContext = (AppContext) AppContext.getApplicationContext();

    public boolean execute(PlugInContext context) throws Exception {
      
        reportNothingToUndoYet(context);
        GUIUtil.centreOnWindow(dialog(context));
        dialog(context).setVisible(true);
        if (dialog(context).wasOKPressed()) {
        	if(context.getLayerViewPanel()!=null){
        		context
        		.getLayerViewPanel()
        		.getRenderingManager()
        		.render(
        				GridRenderer.CONTENT_ID,
        				true);
        	}
            /*JInternalFrame[] frames = context.getWorkbenchGuiComponent().getInternalFrames();
            for (int i = 0; i < frames.length; i++) {
                if (frames[i] instanceof LayerViewPanelProxy) {
                    ((LayerViewPanelProxy) frames[i])
                        .getLayerViewPanel()
                        .getRenderingManager()
                        .render(
                        GridRenderer.CONTENT_ID,
                        true);
                }
            }*/
        }
        return dialog(context).wasOKPressed();
        
    }
    private OptionsDialog dialog(PlugInContext context) {
        return OptionsDialog.instance(context.getWorkbenchContext().getIWorkbench());
    }
    public void initialize(PlugInContext context) throws Exception {
      
        dialog(context)
        .addTab(appContext.getI18nString("userPreferencesOptionsPlugIn"), GUIUtil.resize(
                IconLoader.icon("preferences.gif"), 16),
                new GeopistaPreferencesPanel());
        dialog(context)
        .addTab(appContext.getI18nString("systemConfigOptionsPlugIn"), GUIUtil.resize(
                IconLoader.icon("Wrench.gif"), 16),
                new SystemConfigPanel());
    }

}
