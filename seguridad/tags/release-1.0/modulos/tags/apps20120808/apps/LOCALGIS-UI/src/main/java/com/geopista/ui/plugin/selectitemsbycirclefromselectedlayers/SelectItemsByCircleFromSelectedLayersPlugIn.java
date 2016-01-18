package com.geopista.ui.plugin.selectitemsbycirclefromselectedlayers;


import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;
import com.geopista.app.AppContext;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.editing.helpclassesselection.DrawFenceTool;
import com.geopista.ui.plugin.editing.helpclassesselection.SelectItemsByCircleTool;
import com.geopista.ui.plugin.selectitemsbycirclefromselectedlayers.images.IconLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
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
