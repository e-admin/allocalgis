package com.geopista.ui.plugin.edit;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.LockManager;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.util.GeopistaUtil;
import com.geopista.model.DynamicLayer;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.FeatureInfoTool;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInfoPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

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
