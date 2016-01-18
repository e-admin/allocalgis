/**
 * GeopistaInstallGridPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
