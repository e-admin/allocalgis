package com.geopista.ui.plugin.selectitemsbyfencefromselectedlayers;


import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Icon;

import com.geopista.ui.cursortool.editing.DrawPolygonTool;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.editing.helpclassesselection.DrawFenceTool;
import com.geopista.ui.plugin.selectitemsbyfencefromselectedlayers.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.QuasimodeTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


public class SelectItemsByFenceFromSelectedLayersPlugIn extends AbstractPlugIn{ 
	
	private boolean selectOneItemButtonAdded = false;

    public void initialize(final PlugInContext context) throws Exception {
    	
    	Locale loc=Locale.getDefault();
      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.selectitemsbyfencefromselectedlayers.languages.SelectItemsByFenceFromSelectedLayersPlugIni18n",loc,this.getClass().getClassLoader());
    	
        I18N.plugInsResourceBundle.put("SelectItemsByFenceFromSelectedLayersPlugIn",bundle2);
    	
        String pathMenuNames[] =new String[] { MenuNames.EDIT };
        
		String name = I18N.get("SelectItemsByFenceFromSelectedLayersPlugIn","SelectItemsByFenceFromSelectedLayersPlugIn");
        /*context.getFeatureInstaller().addMainMenuItem(this, pathMenuNames, name,
    			false, null,
    			createEnableCheck(context.getWorkbenchContext()));*/
		
	    GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
	    geopistaEditingPlugIn.addAditionalPlugIn(this);
		
	}
    
    public String getName(){
    	String name = I18N.get("SelectItemsByFenceFromSelectedLayersPlugIn","SelectItemsByFenceFromSelectedLayersPlugIn");
    	return name;
    }
    
    public Icon getIcon() {
        return IconLoader.icon("selecteditemsbyfence.gif");
    }
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);        
        return new MultiEnableCheck()
                        .add(checkFactory.createAtLeastNLayersMustExistCheck(1));        
    }
        
	public boolean execute(PlugInContext context) throws Exception{
	    
		/*this.reportNothingToUndoYet(context);
        try
        {
            CursorTool polyTool = DrawFenceTool.create((LayerNamePanelProxy) context.getActiveInternalFrame(), context);
            context.getLayerViewPanel().setCurrentCursorTool(polyTool);
        }
        catch (Exception e)
        {            
            return false;
        }

		System.gc();*/		
	    return true;
	}

	public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectOneItemButtonAdded)
        {
            //SelectItemsByFenceFromSelectedLayersPlugIn selectItemsByFenceFromSelectedLayersPlugIn = new SelectItemsByFenceFromSelectedLayersPlugIn();            
            //toolbox.addPlugIn(selectItemsByFenceFromSelectedLayersPlugIn, null, selectItemsByFenceFromSelectedLayersPlugIn.getIcon());
        	toolbox.getContext();
        	QuasimodeTool drawPolygonTool = new QuasimodeTool(DrawFenceTool.create((LayerNamePanelProxy) toolbox.getContext(), toolbox.getContext())).add(
    				new QuasimodeTool.ModifierKeySpec(true, false, false),
    				null);		 
        	toolbox.add(drawPolygonTool);
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectOneItemButtonAdded = true;
        }
    } 
}

