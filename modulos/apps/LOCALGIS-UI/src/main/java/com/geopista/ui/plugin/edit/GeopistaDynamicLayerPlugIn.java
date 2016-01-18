/**
 * GeopistaDynamicLayerPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.edit;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.model.DynamicLayer;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class GeopistaDynamicLayerPlugIn extends AbstractPlugIn
{

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private String toolBarCategory = "GeopistaDynamicLayerPlugIn.category";


    public MultiEnableCheck createEnableCheck(
    	final WorkbenchContext workbenchContext) {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                     .add(checkFactory.createAtLeastNGeopistaLayerablesMustBeSelectedCheck(
                    1)).add(new EnableCheck() {
                    	public String check(JComponent component) {
                        	if (workbenchContext.createPlugInContext().getSelectedLayer(0) instanceof DynamicLayer){
	                            ((JCheckBoxMenuItem) component).setSelected(
	                                ((DynamicLayer)workbenchContext.createPlugInContext().getSelectedLayer(0)).isDinamica());
                        	}else{
	                            ((JCheckBoxMenuItem) component).setSelected(false);
                        	}
                            return null;
                        }
        });
    }

    public void initialize(PlugInContext context) throws Exception
    {
    	
        String pluginCategory = aplicacion.getString(toolBarCategory);
    
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        
        JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
                                                          .getGuiComponent()
                                                          .getLayerNamePopupMenu();
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,
              this, aplicacion.getI18nString(this.getName()), true,
              null,
              this.createEnableCheck(context.getWorkbenchContext()));
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);
        int n = context.getSelectedLayers().length;
        for (int i=0; i<n; i++) {
        	if (context.getSelectedLayer(i) instanceof DynamicLayer){
        		DynamicLayer selectedLayer = (DynamicLayer) context.getSelectedLayer(i);
            	selectedLayer.setDinamica(!selectedLayer.isDinamica());
        	}
        }
        return false;
    }


}
