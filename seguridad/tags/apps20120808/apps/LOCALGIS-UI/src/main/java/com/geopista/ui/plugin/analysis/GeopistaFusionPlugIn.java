/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/


package com.geopista.ui.plugin.analysis;


import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.LockManager;
import com.geopista.ui.plugin.analysis.images.IconLoader;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;


public class GeopistaFusionPlugIn  extends AbstractPlugIn implements ThreadedPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private final static String LAYER = aplicacion.getI18nString("Layer");
    private MultiInputDialog dialog;
    private Layer selectLayer = null;
    private Layer layerSelectedFeatures = null;
    private Collection selectedFeatures = null;
    private GeopistaFeature geopistaFeature = null;

    private String toolBarCategory = "GeopistaFusionPlugIn.category";
    
    private boolean selectFusionButtonAdded = false;

    public GeopistaFusionPlugIn() {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.analysis.languages.GeopistaFusionPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("GeopistaFusionPlugIn",bundle2);
    }
    
      public void initialize(PlugInContext context) throws Exception {

        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
            this,
            createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());     
        
        //Líneas necesarias para añadir el PLugIn a la caja de Edición
        
        /*GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
      	geopistaEditingPlugIn.addAditionalPlugIn(this);*/
      	
      }
    
    public boolean execute(PlugInContext context) throws Exception {
        //Unlike ValidatePlugIn, here we always call #initDialog because we want
        //to update the layer comboboxes. [Jon Aquino]
        initDialog(context);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return false;
        }

        return true;
    }

    private void initDialog(PlugInContext context) {
        dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), I18N.get("GeopistaFusionPlugIn","Union"), true);

        dialog.setSideBarImage(IconLoader.icon("fusionar_capa.gif"));
        dialog.setSideBarDescription(
        		I18N.get("GeopistaFusionPlugIn","CrearUnionFeature"));
        String fieldName = LAYER;
       
        Layer capa = (Layer)context.getCandidateLayer(0);
        
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, capa, null, context.getLayerManager());
        GUIUtil.centreOnWindow(dialog);
    }

    public void run(TaskMonitor monitor, PlugInContext context)
        throws Exception {
    	
        LayerViewPanel layerViewPanel = context.getLayerViewPanel();

        selectLayer = dialog.getLayer(LAYER);
        
        selectedFeatures = layerViewPanel.getSelectionManager().getFeaturesWithSelectedItems();
        
        layerSelectedFeatures = (Layer)((GeopistaFeature)selectedFeatures.iterator().next()).getLayer();

        if(selectedFeatures.size()<2)
        {
          JOptionPane.showMessageDialog((Component)context.getWorkbenchGuiComponent(),I18N.get("GeopistaFusionPlugIn","SeleccionarMasDeUno"));
          return;
        }
        
        if(!comprobarContacto(monitor,selectedFeatures))
        {
          JOptionPane.showMessageDialog((Component)context.getWorkbenchGuiComponent(),I18N.get("GeopistaFusionPlugIn","FeaturesContacto"));
          return;
        }
        
        LockManager lockManager = (LockManager) AppContext.getApplicationContext().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);
        
        Integer lockID = null;
        try
        {
            List tempSelectedFeatures = new ArrayList();
            tempSelectedFeatures.addAll(selectedFeatures);
            lockID = lockManager.lockSelectedFeatures(tempSelectedFeatures, monitor);
        }catch(Exception e)
        {
            ErrorDialog.show(aplicacion.getMainFrame(), 
            		I18N.get("GeopistaFusionPlugIn","ErrorAlBloquearFeatures"), 
            		I18N.get("GeopistaFusionPlugIn","ErrorAlBloquearFeatures"), StringUtil
                    .stackTrace(e));
            return;
        }
          
        Geometry unionGeometry = union(monitor, selectedFeatures);

        Iterator selectedFeaturesIter = selectedFeatures.iterator();
        FeatureSchema sourceSchema = ((GeopistaFeature)selectedFeaturesIter.next()).getSchema();
        
        geopistaFeature = new GeopistaFeature(sourceSchema);
        geopistaFeature.setGeometry(unionGeometry);
        
        if(selectLayer instanceof GeopistaLayer)
        {
            geopistaFeature.setLayer((GeopistaLayer) selectLayer);
        }
        
        execute(new UndoableCommand(getName()) {

                private ArrayList removeFeaturesLocal = new ArrayList(selectedFeatures);
                private Feature addedFeatureLocal = geopistaFeature;
          
                public void execute() {
                  selectLayer.getFeatureCollectionWrapper().removeAll(removeFeaturesLocal);
                  selectLayer.getFeatureCollectionWrapper().add(addedFeatureLocal); 
                }

                public void unexecute() {
                	if(layerSelectedFeatures.equals(selectLayer)){
                		selectLayer.getFeatureCollectionWrapper().addAll(removeFeaturesLocal);
                        selectLayer.getFeatureCollectionWrapper().remove(addedFeatureLocal); 
                	}
                	else{
                		selectLayer.getFeatureCollectionWrapper().remove(addedFeatureLocal); 
                	}
                               
                }
            }, context);
        
        if(lockID!=null)
        {
	        try
	        {
	            lockManager.unlockFeaturesByLockId(lockID, monitor);
	        }catch(Exception e)
	        {
	            ErrorDialog.show(aplicacion.getMainFrame(), 
	            		I18N.get("GeopistaFusionPlugIn","ErrorAlDesbloquearFeatures"), 
	            		I18N.get("GeopistaFusionPlugIn","ErrorAlDesbloquearFeatures"), StringUtil
	                    .stackTrace(e));
	            return;
	        }
        }
    }

    private Geometry union(TaskMonitor monitor, Collection fc) {
        monitor.allowCancellationRequests();
        monitor.report("Computing Union...");

        List unionGeometryList = new ArrayList();

        Geometry currUnion = null;
        int size = fc.size();
        int count = 1;

        for (Iterator i = fc.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            Geometry geom = f.getGeometry();

            if (currUnion == null) {
                currUnion = geom;
            } else {
                currUnion = currUnion.union(geom);
            }

            monitor.report(count++, size, "features");
        }

        

        return currUnion;
    }

    private boolean comprobarContacto(TaskMonitor monitor, Collection features) {
        monitor.allowCancellationRequests();
        monitor.report(I18N.get("GeopistaFusionPlugIn","RealizandoFusion"));

        ArrayList featuresArrayList = new ArrayList(features);
        for (int n=0;n<featuresArrayList.size();n++)
        {
          Feature temp1 = (Feature)featuresArrayList.get(n);
          Geometry geom1 = temp1.getGeometry();
          boolean contacto = false;
          for(int p=0;p<featuresArrayList.size();p++)
          {
            if(p==n) continue;
            Feature temp2 = (Feature)featuresArrayList.get(p); 
            Geometry geom2 = temp2.getGeometry();
            contacto = geom1.touches(geom2);
            if(contacto==true) break;
            
          }
          if(contacto==false) return false;
        }
       
        return true;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
           .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
            .add(checkFactory.createAtLeastNFeaturesMustHaveSelectedItemsCheck(2))
            .add(checkFactory.createExactlyNLayersMustHaveSelectedItemsCheck(1))
            .add(checkFactory.createSelectedLayersMustBeEditableCheck())
            .add(checkFactory.createSelectedItemsLayersMustBeEditableCheck());
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("fusionar_capa.gif");
    }
    
    public void addButton(final ToolboxDialog toolbox)
    {
        if (!selectFusionButtonAdded)
        {
        	GeopistaFusionPlugIn fusion = new GeopistaFusionPlugIn();                 
            toolbox.addPlugIn(fusion, null, fusion.getIcon());
            toolbox.finishAddingComponents();
            toolbox.validate();
            selectFusionButtonAdded = true;
        }
    }
}
