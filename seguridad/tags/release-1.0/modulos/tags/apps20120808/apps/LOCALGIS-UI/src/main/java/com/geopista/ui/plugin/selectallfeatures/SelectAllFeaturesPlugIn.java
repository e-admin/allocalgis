package com.geopista.ui.plugin.selectallfeatures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.profiles3D.Profiles3DPlugIn;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.FenceLayerFinder;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
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
