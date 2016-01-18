/**
 * SelectItemsByCircleFromSelectedLayersPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.selectitemsbycirclefromselectedlayers;


import java.awt.event.MouseEvent;

import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.editing.helpclassesselection.SelectItemsByCircleTool;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.QuasimodeTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;



/**
 * @description:
 *    selects items of the actual layer
 *    and informs about the number of selected items
 * 
 * @author sstein
 *
 */
public class SelectItemsByCircleFromSelectedLayersPlugIn extends AbstractPlugIn{
	
	private boolean selectSelectItemsByCircleButtonAdded = false;
		
    public void initialize(final PlugInContext context) throws Exception {
	    	    		
	    GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
	    geopistaEditingPlugIn.addAditionalPlugIn(this);
		
    }
    	    
	public boolean execute(PlugInContext context) throws Exception{	    
        
	    return true;
	}
	
	public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectSelectItemsByCircleButtonAdded)
        {           
                        
        	MouseEvent event = new MouseEvent(toolbox,
    				MouseEvent.MOUSE_CLICKED,
    				System.currentTimeMillis(), 0,
    				0, 0,1,
    				true);	
            toolbox.getContext();
            QuasimodeTool sit = new QuasimodeTool(new SelectItemsByCircleTool(toolbox.getContext())).add(
    				new QuasimodeTool.ModifierKeySpec(true, false, false),
    				null);	
            sit.mousePressed(event); 
        	toolbox.add(sit);
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectSelectItemsByCircleButtonAdded = true;
        }
    }

     
}
