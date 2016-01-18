/*
 * * The GEOPISTA project is a set of tools and applications to manage
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 07-sep-2004 by juacas
 *
 * 
 */
package com.geopista.ui.plugin;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import javax.swing.JInternalFrame;

import com.geopista.editor.TaskComponent;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

public abstract class InstallRendererPlugIn extends AbstractPlugIn {
    private Object contentID;

    private boolean aboveLayerables;
    public InstallRendererPlugIn(Object contentID, boolean aboveLayerables) {
        this.contentID = contentID;
        this.aboveLayerables = aboveLayerables;
    }

    public void initialize(PlugInContext context) throws Exception {
        JInternalFrame[] frames = context.getWorkbenchGuiComponent().getInternalFrames();

        if(frames!=null)

        {
            for (int i = 0; i < frames.length; i++) {
    //            if (frames[i] instanceof TaskFrame) {
    //                ensureHasRenderer((TaskComponent) frames[i]);
    //            }
    //            else
                ensureHasRenderer((TaskComponent) frames[i]);
        }
        }
        else
        {
                ensureHasRenderer((TaskComponent) context.getWorkbenchGuiComponent());    
        }

        context.getWorkbenchGuiComponent().getDesktopPane().addContainerListener(new ContainerAdapter() {
            public void componentAdded(ContainerEvent e) {
                if (!(e.getChild() instanceof TaskFrame)) {
                    return;
                }

                TaskFrame taskFrame = (TaskFrame) e.getChild();
                ensureHasRenderer(taskFrame);
            }
        });
    }

    private void ensureHasRenderer(final TaskComponent taskComponent) {
        if (aboveLayerables) {
            taskComponent.getLayerViewPanel().getRenderingManager().putAboveLayerables(
                contentID,
                createFactory(taskComponent));
        } else {
            taskComponent.getLayerViewPanel().getRenderingManager().putBelowLayerables(
                contentID,
                createFactory(taskComponent));
        }
    }

    protected abstract Renderer.Factory createFactory(TaskComponent frame);
}
