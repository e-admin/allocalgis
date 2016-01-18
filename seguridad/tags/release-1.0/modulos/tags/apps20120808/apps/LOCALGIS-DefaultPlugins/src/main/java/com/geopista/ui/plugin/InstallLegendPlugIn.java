package com.geopista.ui.plugin;


import com.geopista.editor.TaskComponent;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

/**
 * Ensures that all TaskFrames get a scale bar.
 */
public class InstallLegendPlugIn  extends InstallRendererPlugIn {
    public InstallLegendPlugIn() {
        super(LegendRenderer.CONTENT_ID, true);
    }
    protected Renderer.Factory createFactory(final TaskComponent frame) {
        return new Renderer.Factory() {
                public Renderer create() {
                    return new LegendRenderer((LayerViewPanel)frame.getLayerViewPanel());
                }
            };
    }

}
