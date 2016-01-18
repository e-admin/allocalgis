/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * JUMP is Copyright (C) 2003 Vivid Solutions
 *
 * This program implements extensions to JUMP and is
 * Copyright (C) 2004 Integrated Systems Analysts, Inc.
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
 * Integrated Systems Analysts, Inc.
 * 630C Anchors St., Suite 101
 * Fort Walton Beach, Florida
 * USA
 *
 * (850)862-7321
 */

package com.geopista.ui.cursortool.editing;

import java.awt.event.MouseEvent;

import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.QuasimodeTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class DrawLineStringPlugIn extends AbstractPlugIn
{
    private boolean selectDrawLineStringButtonAdded = false;
    
    public void initialize(final PlugInContext context) throws Exception
    {
      
    	GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
    	geopistaEditingPlugIn.addAditionalPlugIn(this);    	
       
    }
  
    public boolean execute(PlugInContext context) throws Exception
    {
        return true;
    }
    
    public void addButton(final ToolboxDialog toolbox)
    {
    	if (!selectDrawLineStringButtonAdded)
    	{    		
    		MouseEvent event = new MouseEvent(toolbox,
    				MouseEvent.MOUSE_CLICKED,
    				System.currentTimeMillis(), 0,
    				0, 0,1,
    				true);		   
    		QuasimodeTool drawLineStringTool = new QuasimodeTool(DrawLineStringTool.create(toolbox.getContext())).add(
    				new QuasimodeTool.ModifierKeySpec(true, false, false),
    				null);		   		   
    		drawLineStringTool.mousePressed(event);

    		toolbox.add(drawLineStringTool);
    		toolbox.finishAddingComponents();
    		toolbox.validate();
    		selectDrawLineStringButtonAdded = true;
    	}
    }
}

