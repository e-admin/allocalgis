package com.geopista.ui.plugin.switchselection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.jfree.util.ArrayUtilities;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.selectallfeatures.SelectAllFeaturesPlugIn;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class SwitchSelectionPlugIn extends ThreadedBasePlugIn
{

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
        
        Iterator layerWithSelectItemIterator = selectedLayers.iterator();
        
        
        
        while(layerWithSelectItemIterator.hasNext())
        {
            Layer currentLayer = (Layer) layerWithSelectItemIterator.next();
            
            Collection selectFeatures = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(currentLayer);
            Collection allFeatures = currentLayer.getFeatureCollectionWrapper().getWrappee().getFeatures();
            
            Iterator allFeaturesIterator = allFeatures.iterator();
            
            Collection switchCollection = new ArrayList();
            
            while(allFeaturesIterator.hasNext())
            {
                Object currentFeature = (Object) allFeaturesIterator.next();
                if(!selectFeatures.contains(currentFeature))
                {
                    switchCollection.add(currentFeature);
                }
                
            }
            
            context.getLayerViewPanel().getSelectionManager().unselectItems(currentLayer,selectFeatures);
            context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(currentLayer,switchCollection);
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
