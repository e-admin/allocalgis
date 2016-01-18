
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

package com.geopista.ui.cursortool.drawconstrainedcircle;

import java.util.Locale;
import java.util.ResourceBundle;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.QuasimodeTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class DrawConstrainedCirclePlugIn extends AbstractPlugIn
{
    private boolean circleButtonAdded = false;
    static String errorSeeOutputWindow; 
    
    public void initialize(final PlugInContext context) throws Exception
    {
      //add a listener so that when the toolbox dialog opens the constrained tools will be added
        //we can't just add the tools directly at this point since the toolbox isn't ready yet

    	GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
    	geopistaEditingPlugIn.addAditionalPlugIn(this);
    	
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.cursortool.drawconstrainedcircle.languages.DrawConstrainedCircleTooli18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("DrawConstrainedCircleTool",bundle2);
        
        errorSeeOutputWindow =I18N.get("Error-See-Output-Window");
    }
  
    public boolean execute(PlugInContext context) throws Exception
    {
        try
        {
            CursorTool circleTool = DrawConstrainedCircleTool.create((LayerNamePanelProxy) context.getActiveInternalFrame());
            context.getLayerViewPanel().setCurrentCursorTool(circleTool); 
            return true;
        }
        catch (Exception e)
        {
            context.getWorkbenchFrame().warnUser(errorSeeOutputWindow);
            context.getWorkbenchFrame().getOutputFrame().createNewDocument();
            context.getWorkbenchFrame().getOutputFrame().addText("DrawConstrainedCircleTool Exception:" + e.toString());
            return false;
        }
    }
    
        
    public void addButton(final ToolboxDialog toolbox)
    {
        if (!circleButtonAdded)
        {            
            QuasimodeTool quasimodeTool = new QuasimodeTool(DrawConstrainedCircleTool.create(toolbox.getContext()));
            quasimodeTool.add(new QuasimodeTool.ModifierKeySpec(true, false, false), null);
            quasimodeTool.add(new QuasimodeTool.ModifierKeySpec(true, true, false), null);
            toolbox.add(quasimodeTool, null);
            toolbox.finishAddingComponents();
            toolbox.validate();
            circleButtonAdded = true;
        }
    }
}

