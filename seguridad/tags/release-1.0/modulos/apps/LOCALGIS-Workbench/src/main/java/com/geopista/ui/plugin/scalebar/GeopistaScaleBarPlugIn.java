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
 * Created on 26-ago-2004 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.scalebar;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import javax.swing.JComponent;

import com.geopista.editor.TaskComponent;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.TaskFrame;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;


	public class GeopistaScaleBarPlugIn extends AbstractPlugIn
	{
    
		GeopistaScaleBarRenderer scaleBarRenderer;
		
	    public boolean execute(PlugInContext context) throws Exception {
	        reportNothingToUndoYet(context);
	        if (scaleBarRenderer==null) return true;
	        scaleBarRenderer.setEnabled(!scaleBarRenderer.isEnabled(
	                context.getLayerViewPanel()), context.getLayerViewPanel());
	        context.getLayerViewPanel().getRenderingManager().render(scaleBarRenderer.CONTENT_ID);

	        return true;
	    }
	    private Object contentID;

	    private boolean aboveLayerables;
	    public GeopistaScaleBarPlugIn()
	    {
	    	this((Object)GeopistaScaleBarRenderer.CONTENT_ID,true);
	    }
	    public GeopistaScaleBarPlugIn(Object contentID, boolean aboveLayerables) {
	        this.contentID = contentID;
	        this.aboveLayerables = aboveLayerables;
	    }

	    public void initialize(PlugInContext context) throws Exception {
	        /*JInternalFrame[] frames = context.getWorkbenchFrame().getInternalFrames();

	        for (int i = 0; i < frames.length; i++) {
	            if (frames[i] instanceof TaskFrame) {
	                ensureHasRenderer((TaskFrame) frames[i]);
	            }
	        }*/
	    ensureHasRenderer(context.getActiveTaskComponent());
	        ((JComponent) context.getWorkbenchGuiComponent().getDesktopPane()).addContainerListener(new ContainerAdapter() {
	            public void componentAdded(ContainerEvent e) {
	                if (!(e.getChild() instanceof TaskFrame)) {
	                    return;
	                }

	                TaskFrame taskFrame = (TaskFrame) e.getChild();
	                ensureHasRenderer(taskFrame);
	            }
	        });
	    }

	    private void ensureHasRenderer(final TaskComponent taskFrame) {
	    	if (taskFrame==null) return;
	        if (aboveLayerables) {
	            taskFrame.getLayerViewPanel().getRenderingManager().putAboveLayerables(
	                contentID,
	                createFactory(taskFrame));
	        } else {
	            taskFrame.getLayerViewPanel().getRenderingManager().putBelowLayerables(
	                contentID,
	                createFactory(taskFrame));
	        }
	    }
	    protected Renderer.Factory createFactory(final TaskComponent frame) {
	    	scaleBarRenderer=new GeopistaScaleBarRenderer((LayerViewPanel)frame.getLayerViewPanel());
	        return new Renderer.Factory() {
	                public Renderer create() {
	                    return scaleBarRenderer;
	                }
	            };
	    }
	}

