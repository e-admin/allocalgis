/**
 * QueryPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.michaelm.jump.query;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
//import com.vividsolutions.jump.workbench.ui.MenuNames;

/**
 * QueryPlugIn
 * @author Michaël MICHAUD
 * @version 0.1.0 (4 Dec 2004)
 */ 
public class QueryPlugIn extends AbstractPlugIn {
    static QueryDialog queryDialog;
	public static String pluginname = "query";
	
    public void initialize(PlugInContext context) {
		I18NPlug.setPlugInRessource(pluginname, "language.query");
		
		String pluginCategory = AppContext.getApplicationContext().getString("GeopistaSearchByAttributes.category");
		context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(pluginCategory)
				.addPlugIn(this.getIcon(), this,
						createEnableCheck(context.getWorkbenchContext()),
						context.getWorkbenchContext());
//		if (I18NPlug.jumpi18n == true) {
//		    context.getFeatureInstaller().addMainMenuItem(this,
//		        new String[]
//	            {MenuNames.TOOLS, 
//		 	    I18NPlug.get(pluginname, "jump.query.menu")},
//		        I18NPlug.get(pluginname, "jump.query.menuitem"), false, null, null);
//		}
//		else 
		{
			context.getFeatureInstaller().addMainMenuItem(this,
				new String[] 
				{"Tools", AppContext.getApplicationContext().getI18nString("SimpleQuery")},
				AppContext.getApplicationContext().getI18nString("SimpleQuery"),
			    false, null, QueryPlugIn.createEnableCheck(context.getWorkbenchContext()));
		}    	   
    }
                                                      
    public boolean execute(PlugInContext context) throws Exception {
//        if (queryDialog==null) {
            queryDialog = new QueryDialog(context);
//        }
//        queryDialog.initUI();
        return false;
    }
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
                checkFactory.createAtLeastNLayersMustExistCheck(1));

    }
    
    public ImageIcon getIcon() {
        return IconLoader.icon("Attribute.gif");
  }
    
}
