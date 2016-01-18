/**
 * BeanShellEditorPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.michaelm.jump.bsheditor;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

import fr.michaelm.bsheditor.BeanShellEditor;
import fr.michaelm.bsheditor.images.IconLoader;

public class BeanShellEditorPlugIn extends AbstractPlugIn {
    
    public void initialize(PlugInContext context) {
        context.getFeatureInstaller().addMainMenuItem(this,
                                                      new String[] { "Scripting" },
                                                      AppContext.getApplicationContext().getI18nString(getName()),
                                                      false,
                                                      null,
                                                      null);
        
        ApplicationContext appContext=AppContext.getApplicationContext();
		
		context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(appContext.getString("CalculateFeature.category")).addPlugIn(
				getIcon(), this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());
    
    }
                                                      
    public boolean execute(PlugInContext context) throws Exception {
        Map map = new HashMap();
        map.put("wc", context.getWorkbenchContext());
        BeanShellEditor e = new BeanShellEditor(map, null);
        return true;
    }
    
	public ImageIcon getIcon() {
		return IconLoader.icon("Script16.gif");
	}
	
	public String getName() { 
		 return  "BeanShellEditor"; 
	}
	 
	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

//		return new MultiEnableCheck()
//		.add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
//		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
//		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
//		.add(checkFactory.createAtLeastNLayersMustExistCheck(2));

		return new MultiEnableCheck();
	}

}
