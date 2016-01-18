
package com.geopista.ui.plugin.selectonecemeteryitem;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.selectonecemeteryitem.SelectOneCemeteryItemTool;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class SelectOneCemeteryItemPlugIn extends AbstractPlugIn
{
    private boolean selectOneCemeteryItemButtonAdded = false;
    
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
        if (!selectOneCemeteryItemButtonAdded)
        {            
        	toolbox.addToolBar();
            toolbox.add(new SelectOneCemeteryItemTool());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectOneCemeteryItemButtonAdded = true;
        }
    }
}

