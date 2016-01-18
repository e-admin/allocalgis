/**
 * SelectAllDynamicFencePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.selectalldynamicfence;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkBench;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * @author seilagamo
 *
 */
public class SelectAllDynamicFencePlugIn extends AbstractPlugIn {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    TaskMonitorDialog progressDialog;
    WorkBench workbench;
    
    public void initialize(final PlugInContext context) throws Exception {
        
        Locale loc=Locale.getDefault();
        ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.selectalldynamicfence.languages.SelectAllDynamicFencePlugInPlugIni18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("SelectAllDynamicFencePlugIn", bundle2);
        
//        GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
//        geopistaEditingPlugIn.addAditionalPlugIn(this);
        
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench().getGuiComponent().getLayerNamePopupMenu();
        featureInstaller.addPopupMenuItem(layerNamePopupMenu,this, this.getName() + "...", 
                false,null, this.createEnableCheck(context.getWorkbenchContext()));
    }
    
    
    public EnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
            .add(checkFactory.createExactlyOneDynamicLayerMustBeSelectedCheck());      
    }
    
    public String getName(){
        String name = StringUtil.toFriendlyName(I18N.get("SelectAllDynamicFencePlugIn", "SelectAllDynamicFence"));
        return name;
    }
    
//    public Icon getIcon() {
//        return IconLoader.icon("selectalldynamicfence");
//    }
    
    private void getFeatures(PlugInContext context){
        GeopistaLayer layer = null;
        Layer[] layers = context.getSelectedLayers();
        int n = layers.length;
        int i = 0;
        for(i=0;i<n;i++){
            layer = (GeopistaLayer)layers[i];
            if (layer instanceof DynamicLayer)
                break;
        }
    	ILayerManager layerManager = layer.getLayerManager();
        boolean firingEvents = layerManager.isFiringEvents();
        layerManager.setFiringEvents(false);
        try{
            if (i<n){
                CoordinateSystem inCoord = layer.getLayerManager().getCoordinateSystem();
                GeopistaConnection geopistaConnection = (GeopistaConnection) layer.getDataSourceQuery().getDataSource().getConnection();
                DriverProperties driverProperties = geopistaConnection.getDriverProperties();
                driverProperties.put("srid_destino",inCoord.getEPSGCode());
                geopistaConnection = new GeopistaConnection(driverProperties);
                
                //Creamos una coleccion para almacenar las excepciones que se producen
                Collection exceptions = new ArrayList();
                //Miro en qué srid por defecto se almacenan las features en la base de datos
//                String sridDefecto = geopistaConnection.getSRIDDefecto();
//                CoordinateSystem outCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridDefecto));
				String nameLayer = layer.getSystemId();
                layerManager.getLayer(nameLayer).setDinamica(false);
				FeatureCollection featureCollection = geopistaConnection.executeQueryLayer(layer.getDataSourceQuery().getQuery(),exceptions,null,(GeopistaLayer)layer);
		        layer = new GeopistaLayer(layer.getName(), layerManager.generateLayerFillColor(), featureCollection, layerManager);
                layerManager.getLayer(nameLayer).setDinamica(false);
            }else{
                ((GeopistaMap) context.getTask()).getLayerViewPanel().getContext().warnUser(
                        I18N.get("NingunaCapaDinamica"));               
            }
        }catch(Exception e){
            ErrorDialog.show((Component) ((WorkbenchGuiComponent) SwingUtilities
                    .getAncestorOfClass(WorkbenchGuiComponent.class, (Component) context
                            .getWorkbenchContext().getIWorkbench().getGuiComponent())), aplicacion
                    .getI18nString("GeopistaLoadMapPlugIn.CapaErronea"), e.getCause().getMessage(),
                    StringUtil.stackTrace(e));
        }finally{
        	layerManager.setFiringEvents(firingEvents);
        }
        
    }
    
    public boolean execute(final PlugInContext context) throws Exception {

//        workbench =((WorkbenchGuiComponent) SwingUtilities.getAncestorOfClass(WorkbenchGuiComponent.class,
//                (GeopistaWorkbenchFrame) context
//                .getWorkbenchContext().getIWorkbench().getGuiComponent())).getContext().getIWorkbench();
        progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(I18N.get("LoadSystemLayers.Cargando"));
        progressDialog.report(I18N.get("LoadSystemLayers.Cargando"));
        progressDialog.addComponentListener(new ComponentAdapter() {

            public void componentShown(ComponentEvent e) {
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            getFeatures(context);
                        } catch (Exception e) {
                        } finally {
                            progressDialog.setVisible(false);
                        }
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        reportNothingToUndoYet(context);
    	context.getLayerViewPanel().getRenderingManager().renderAll(true);
        return true;
    }
    
    /**
     * Notifies the UndoManager that this PlugIn did not modify any model states,
     * and therefore the undo history should remain unchanged. Call this method
     * inside #execute(PlugInContext).
     */
    protected void reportNothingToUndoYet(PlugInContext context) {
        context.getLayerManager().getUndoableEditReceiver()
            .reportNothingToUndoYet();
    }

}
