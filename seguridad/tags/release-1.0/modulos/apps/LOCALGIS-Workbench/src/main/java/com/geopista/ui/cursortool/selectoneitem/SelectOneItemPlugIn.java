
package com.geopista.ui.cursortool.selectoneitem;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.cursortool.selectoneitem.SelectOneItemTool;
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

