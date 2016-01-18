/**
 * SelectItemsByFenceFromSelectedLayersPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.selectitemsbyfencefromselectedlayers;


import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

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

