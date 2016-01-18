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

package com.vividsolutions.jump.demo.delineation;

import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.*;
import com.vividsolutions.jump.workbench.ui.snap.*;


/**
 *  Sets up and installs the delineation tool. In the future, we will be able to
 *  install CursorTools directly from a jar that users can drop into an
 *  appropriate directory. There will be no need for an installation plug-in
 *  like this one.
 */
public class InstallDelineationToolPlugIn extends AbstractPlugIn {
    public InstallDelineationToolPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        return true;
    }

    public void initialize(PlugInContext context) throws Exception {
        List snapPolicies = Arrays.asList(new SnapPolicy[]{ new SnapToFeaturesPolicy() });
        //Combine the Edit, Create and SnapIndicator tools using "Or".
        //If the user drags on a vertex, the Edit tool will become active.
        //Otherwise, if the user clicks somewhere on the panel, the Create tool
        //will become active.
        //Combine the result with a Snap Indicator tool using "And".
        //The Snap Indicator tool displays a green or red dot depending on whether
        //or not the mouse is close enough to a feature to snap to it.
        //Because we are using "And", the Snap Indicator tool is always displayed.
        context.getWorkbenchGuiComponent().getToolBar().addCursorTool("Delineate",
            new AndCompositeTool(new CursorTool[] {
                    new SnapIndicatorTool(snapPolicies),
                    new OrCompositeTool(new AbstractCursorTool[] {
                            
                //Add Edit before Create so that it will always be tried first. [Jon Aquino]
                new EditDelineationTool(snapPolicies), new CreateDelineationTool(snapPolicies)
                        })
                }));
    }
}
