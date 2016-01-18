package com.geopista.ui.plugin.searchcatreference;
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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaSchema;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterNode;
import com.geopista.ui.GeopistaOneLayerAttributeTab;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.CloneableInternalFrame;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.SelectionManager;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.TaskFrameProxy;

import fr.michaelm.jump.query.QueryPlugIn;



public class SearchReferenciaCatastralPlugIn extends ThreadedBasePlugIn 
{
   public static final String REFERENCIA_CATASTRAL = "referencia_catastral";
   private ApplicationContext appContext = AppContext.getApplicationContext();
   private String toolBarCategory = "GeopistaNumerosPoliciaPlugIn.category";
   private ReferenciaCatastralDialog referenciaCatastralDialog = null;
   public static final String SearchReferenciaCatastralI18N = "SearchReferenciaCatastralI18N";
      
   public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {

        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
            .add(checkFactory.createAtLeastLayerWithColumnReferenciaCatastralBeExits());

  }

  public String getName() {
        return I18N.get(SearchReferenciaCatastralI18N,"SearchReferenciaCatastralPlugIn.ReferenciaCatastralPlugInDescription");
  }

  public boolean execute(PlugInContext context) throws Exception {

     
      
      
     Collection totalRefCatastralLayers = new ArrayList();
     Collection refLayers = context.getLayerManager().getLayers();
     Iterator refLayersIterator = refLayers.iterator();
     while(refLayersIterator.hasNext())
     {
         Layer currentLayer = (Layer) refLayersIterator.next();
         if(currentLayer instanceof GeopistaLayer)
         {
             GeopistaSchema currentSchema = (GeopistaSchema) currentLayer.getFeatureCollectionWrapper().getFeatureSchema();
             String attributeName = currentSchema.getAttributeByColumn(REFERENCIA_CATASTRAL);
             
             if(attributeName!=null)
             {
                 totalRefCatastralLayers.add(currentLayer);
             }
         }
         
     }
     
     referenciaCatastralDialog = new ReferenciaCatastralDialog(appContext.getMainFrame(),true,totalRefCatastralLayers);
     referenciaCatastralDialog.setVisible(true);
     
     if(referenciaCatastralDialog.wasOkPressed())
     {
         return true;
     }
     
     return false;
    
  }
  public void initialize(PlugInContext context) throws Exception {
      
      Locale currentLocale = I18N.getLocaleAsObject();        
      
      ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.searchcatreference.language.SearchReferenciaCatastralPlugIni18n",currentLocale);      

      I18N.plugInsResourceBundle.put(SearchReferenciaCatastralI18N,bundle2);
      
       String pluginCategory = appContext.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(
          		getIcon(), this,
          		createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());
        
        context.getFeatureInstaller().addMainMenuItem(this,
                new String[] 
                {"Tools", AppContext.getApplicationContext().getI18nString("SimpleQuery")},
                I18N.get(SearchReferenciaCatastralI18N,"SearchReferenciaCatastralPlugIn.SearchReferenciaCatastral"),
                false, null, QueryPlugIn.createEnableCheck(context.getWorkbenchContext()));
 
    }

  public ImageIcon getIcon() {
        return IconLoader.icon("referenciacatastral.gif");
  }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {
        String referenciaCatastral = getReferenciaCatastralDialog().getReferenciaCatastralPanel().getReferenciaCatastralJTextField().getText();
        referenciaCatastral = referenciaCatastral.toUpperCase();
        
        Layer currentLayer = (Layer) getReferenciaCatastralDialog().getReferenciaCatastralPanel().getRefLayersJComboBox().getSelectedItem();        
        
        GeopistaSchema schemaNumpolice = (GeopistaSchema) currentLayer.getFeatureCollectionWrapper().getFeatureSchema();
        String attributeName = schemaNumpolice.getAttributeByColumn(REFERENCIA_CATASTRAL);
        
        
        Collection searchFeatures = GeopistaFunctionUtils.searchReferenciaCatastral(currentLayer,referenciaCatastral,attributeName);
        Iterator searchFeaturesIterator = searchFeatures.iterator();
        if(searchFeatures.size()==0)
        {
               
            String sUrlPrefix = appContext.getString("geopista.conexion.servidor");
            AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(sUrlPrefix + "/AdministradorCartografiaServlet");
            
            FilterNode fn = (FilterNode) ((GeopistaConnection) currentLayer.getDataSourceQuery().getDataSource().getConnection()).getDriverProperties().get("nodofiltro");
            
            GeopistaLayer geopistaLayer = acClient.searchByAttribute((GeopistaMap) context.getTask(),
                    (GeopistaLayer) currentLayer, appContext.getUserPreference(
                            AppContext.PREFERENCES_LOCALE_KEY, "es_ES", true), fn, REFERENCIA_CATASTRAL, 
                                    referenciaCatastral, true, Integer.valueOf(currentLayer
                                            .getLayerManager().getCoordinateSystem().getEPSGCode()));                                                
            
            searchFeatures = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
            searchFeaturesIterator = searchFeatures.iterator();
        }   
        if(searchFeatures.size()==0) {
            JOptionPane.showMessageDialog(appContext.getMainFrame(), I18N.get(SearchReferenciaCatastralI18N,"SearchReferenciaCatastralPlugIn.NoExisteReferenciaCatastral") + " " + referenciaCatastral);
        }
        else
        if(searchFeatures.size()==1)
        {
            Feature currentFeature = (Feature) searchFeaturesIterator.next();
            context.getLayerViewPanel().getSelectionManager().clear();
            context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(currentLayer, currentFeature);

            if (context.getWorkbenchContext().getIWorkbench().getGuiComponent()  instanceof GeopistaEditor){
            	
            	 ((GeopistaEditor) context.getWorkbenchContext().getIWorkbench().
            			 getGuiComponent()).fireGeopistaSelectionChanged( context.getLayerViewPanel().getSelectionManager().getFeatureSelection());
            }            	    
            context.getLayerViewPanel().getViewport().zoom(currentFeature.getGeometry().getEnvelopeInternal());
        }
        else
        {
            final ViewAttributesFrame frame = new ViewAttributesFrame(context,searchFeatures,currentLayer);
            ((WorkbenchGuiComponent)context.getWorkbenchGuiComponent()).addInternalFrame(frame);
        }
        
        
    }

    /**
     * @return the referenciaCatastralDialog
     */
    private ReferenciaCatastralDialog getReferenciaCatastralDialog()
    {
        return referenciaCatastralDialog;
    }
    
    private class ViewAttributesFrame extends JInternalFrame
    implements LayerManagerProxy, SelectionManagerProxy,
        LayerNamePanelProxy, TaskFrameProxy, LayerViewPanelProxy {
    private LayerManager layerManager;
    private GeopistaOneLayerAttributeTab attributeTab;

    public ViewAttributesFrame(PlugInContext context, Collection attributeCollection, Layer layer) {
        this.layerManager = context.getLayerManager();
        addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosed(InternalFrameEvent e) {
                    //Assume that there are no other views on the model [Jon Aquino]
                    attributeTab.getModel().dispose();
                }
            });
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        getContentPane().setLayout(new BorderLayout());

        attributeTab = new GeopistaOneLayerAttributeTab(context.getWorkbenchContext(),
                //(TaskComponent) context.getActiveInternalFrame(), this).setLayer(attributeCollection,layer);
        		(TaskComponent)context.getActiveTaskComponent(), this).setLayer(attributeCollection,layer);

        
        addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameOpened(InternalFrameEvent e) {
                    attributeTab.getToolBar().updateEnabledState();
                }
            });
        getContentPane().add(attributeTab, BorderLayout.CENTER);
        setSize(500, 300);
        
        updateTitle(attributeTab.getLayer());
        context.getLayerManager().addLayerListener(new LayerListener() {
                public void layerChanged(LayerEvent e) {
                    if (attributeTab.getLayer() != null) {
                        updateTitle(attributeTab.getLayer());
                    }
                }

                public void categoryChanged(CategoryEvent e) {
                }

                public void featuresChanged(FeatureEvent e) {
                }
            });
        Assert.isTrue(!(this instanceof CloneableInternalFrame),
            "There can be no other views on the InfoModel");
    }
    public LayerViewPanel getLayerViewPanel() {
        return (LayerViewPanel)getTaskComponent().getLayerViewPanel();
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }

    private void updateTitle(Layer layer) {
        setTitle((layer.isEditable() ? "Edit" : "View") + " Attributes: " +
            layer.getName());
    }

    public TaskComponent getTaskComponent() {
        return attributeTab.getTaskFrame();
    }

    public SelectionManager getSelectionManager() {
        return attributeTab.getPanel().getSelectionManager();
    }

    public LayerNamePanel getLayerNamePanel() {
        return attributeTab;
    }
 
}
}