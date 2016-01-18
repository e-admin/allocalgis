/**
 * SelectAllFeaturesPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.selectallfeatures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class SelectAllFeaturesPlugIn extends ThreadedBasePlugIn
{

    /**
     * @param args
     */
    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        
        
        return true;
    }
    
    public void initialize(PlugInContext context) throws Exception
    {
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        
        
        featureInstaller.addMainMenuItem(this, new String[]{MenuNames.EDIT},
                AppContext.getApplicationContext().getI18nString(this.getName()),false, null,
                SelectAllFeaturesPlugIn.createEnableCheck(context.getWorkbenchContext()));
        
    }
    
    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {
        
        Collection selectedLayers = Arrays.asList(context.getSelectedLayers());
        
        Iterator selectedLayersIterator = selectedLayers.iterator();
        
        
        
        while(selectedLayersIterator.hasNext())
        {
            Layer currentLayer = (Layer) selectedLayersIterator.next();
            context.getLayerViewPanel().getSelectionManager().unselectItems(currentLayer);
            context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(currentLayer,currentLayer.getFeatureCollectionWrapper().getWrappee().getFeatures());
            context.getLayerViewPanel().getSelectionManager().updatePanel();
            
        }


    }

    

    public static MultiEnableCheck createEnableCheck(
        WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                                     .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(
                1));
    }

}
