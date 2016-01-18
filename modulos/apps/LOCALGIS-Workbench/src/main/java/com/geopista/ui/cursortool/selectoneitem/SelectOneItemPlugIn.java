/**
 * SelectOneItemPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.cursortool.selectoneitem;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class SelectOneItemPlugIn extends AbstractPlugIn
{
    private boolean selectOneItemButtonAdded = false;
    
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
        if (!selectOneItemButtonAdded)
        {            
        	toolbox.addToolBar();
            toolbox.add(new SelectOneItemTool());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectOneItemButtonAdded = true;
        }
    }
}

