package com.geopista.ui.snap;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkBench;
import com.geopista.ui.plugin.InstallRendererPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.OptionsDialog;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;
import com.vividsolutions.jump.workbench.ui.snap.GridRenderer;
import com.vividsolutions.jump.workbench.ui.snap.SnapOptionsPanel;

public class GeopistaInstallGridPlugIn extends InstallRendererPlugIn {
    
    AppContext appContext = (AppContext) AppContext.getApplicationContext();
    
    public GeopistaInstallGridPlugIn() {
        super(GridRenderer.CONTENT_ID, false);
    }
    protected Renderer.Factory createFactory(final TaskComponent frame) {
        return new Renderer.Factory() {
                public Renderer create() {
                    return new GridRenderer(workbench.getBlackboard(), (LayerViewPanel)frame.getLayerViewPanel());
                }
            };
    }
    private WorkBench workbench;
    public void initialize(PlugInContext context) throws Exception {
        workbench = context.getWorkbenchContext().getIWorkbench();
        super.initialize(context);
        OptionsDialog.instance(context.getWorkbenchContext().getIWorkbench()).addTab(
                appContext.getI18nString("SnapGrid"),
            new SnapOptionsPanel(context.getWorkbenchContext().getIWorkbench().getBlackboard()));    
            

    }
	

}
