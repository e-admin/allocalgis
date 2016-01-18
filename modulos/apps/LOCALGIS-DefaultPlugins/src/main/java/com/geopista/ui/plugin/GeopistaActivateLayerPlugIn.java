/**
 * GeopistaActivateLayerPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.ui.plugin;

import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class GeopistaActivateLayerPlugIn extends AbstractPlugIn {


    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    
    public GeopistaActivateLayerPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {

      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
      
      JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
                                                        .getGuiComponent()
                                                        .getLayerNamePopupMenu();
      featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            this, aplicacion.getI18nString(this.getName()), true,
            null,
            this.createEnableCheck(context.getWorkbenchContext()));
      
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        boolean makeActiva = !((GeopistaLayer)context.getSelectedLayer(0)).isActiva();
        for (Iterator i = Arrays.asList(context.getSelectedLayers()).iterator(); i.hasNext(); ) {
            GeopistaLayer selectedLayer = (GeopistaLayer) i.next();
            selectedLayer.setActiva(!selectedLayer.isActiva());
        }
        
        return false;
    }

    

    

    public MultiEnableCheck createEnableCheck(
        final WorkbenchContext workbenchContext) {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                     .add(checkFactory.createAtLeastNGeopistaLayerablesMustBeSelectedCheck(
                1)).add(new EnableCheck() {
                    public String check(JComponent component) {
                        ((JCheckBoxMenuItem) component).setSelected(
                            ((GeopistaLayer)workbenchContext.createPlugInContext().getSelectedLayer(0)).isActiva());
                        return null;
                    }
                });
    }

    
}
