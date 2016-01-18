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

package com.vividsolutions.jump.workbench.ui.snap;

import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OptionsDialog;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.plugin.InstallRendererPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

import com.geopista.editor.WorkBench;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;

public class InstallGridPlugIn extends InstallRendererPlugIn {
    AppContext appContext = (AppContext) AppContext.getApplicationContext();
    public InstallGridPlugIn() {
        super(GridRenderer.CONTENT_ID, false);
    }
    protected Renderer.Factory createFactory(final TaskFrame frame) {
        return new Renderer.Factory() {
                public Renderer create() {
                    return new GridRenderer(workbench.getBlackboard(), frame.getLayerViewPanel());
                }
            };
    }
    private WorkBench workbench;
    public void initialize(PlugInContext context) throws Exception {
        workbench = context.getWorkbenchContext().getIWorkbench();
        super.initialize(context);
        OptionsDialog.instance(context.getWorkbenchContext().getIWorkbench()).addTab(appContext.getI18nString("lblTituloGeopistaInstallGridPlugIn"),
            new SnapOptionsPanel(context.getWorkbenchContext().getIWorkbench().getBlackboard()));        
    }

}
