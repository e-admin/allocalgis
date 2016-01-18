package com.geopista.ui.warp;

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

import java.awt.BorderLayout;

import javax.swing.JComponent;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxPlugIn;
import com.vividsolutions.jump.workbench.ui.warp.DeleteIncrementalWarpingVectorTool;
import com.vividsolutions.jump.workbench.ui.warp.DeleteWarpingVectorTool;
import com.vividsolutions.jump.workbench.ui.warp.DrawIncrementalWarpingVectorTool;
import com.vividsolutions.jump.workbench.ui.warp.DrawWarpingVectorTool;
import com.vividsolutions.jump.workbench.ui.warp.WarpingPanel;

public class GeopistaWarpingPlugIn extends ToolboxPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  

    public void initialize(PlugInContext context) throws Exception {
        createMainMenuItem(
              new String[] { aplicacion.getI18nString("Tools"), "GeopistaWarpingPlugIn.Warping" },
            GUIUtil.toSmallIcon(IconLoader.icon("GoalFlag.gif")),
            context.getWorkbenchContext());
    }

    protected void initializeToolbox(ToolboxDialog toolbox) {
        WarpingPanel warpingPanel = new WarpingPanel(toolbox);
        toolbox.getCenterPanel().add(warpingPanel, BorderLayout.CENTER);
        add(new DrawWarpingVectorTool(), false, toolbox, warpingPanel);
        add(new DeleteWarpingVectorTool(), false, toolbox, warpingPanel);
        toolbox.getToolBar().addSeparator();        
        add(new DrawIncrementalWarpingVectorTool(warpingPanel), true, toolbox, warpingPanel);
        add(new DeleteIncrementalWarpingVectorTool(warpingPanel), true, toolbox, warpingPanel);
        //Set y so it is positioned below Editing toolbox. [Jon Aquino]
        toolbox.setInitialLocation(new GUIUtil.Location(20, true, 175, false));   
    }

    private void add(
        CursorTool tool,
        final boolean incremental,
        ToolboxDialog toolbox,
        final WarpingPanel warpingPanel) {
        //Logic for enabling either the incremental-warping-vector tools or the warping-vector
        //tools, depending on whether the Warp Incrementally checkbox is selected or not. [Jon Aquino]
        toolbox.add(tool, new EnableCheck() {
            public String check(JComponent component) {
                if (incremental && warpingPanel.isWarpingIncrementally()) {
                    return null;
                }
                if (!incremental && !warpingPanel.isWarpingIncrementally()) {
                    return null;
                }
                return "Incremental warping must be " + (incremental ? "enabled" : "disabled");
            }
        });
    }

}
