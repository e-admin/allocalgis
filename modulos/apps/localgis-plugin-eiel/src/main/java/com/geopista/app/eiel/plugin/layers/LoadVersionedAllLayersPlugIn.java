/**
 * LoadVersionedAllLayersPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 12-may-2004
 */
package com.geopista.app.eiel.plugin.layers;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.plugin.layers.images.IconLoader;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.Version;
import com.geopista.ui.plugin.layers.DialogoSeleccionVersion;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import es.enxenio.util.configuration.ConfigurationParametersManager;

/**
 * @author COTESA
 */
public class LoadVersionedAllLayersPlugIn extends AbstractPlugIn {
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	private String toolBarCategory = "LoadVersionedAllLayersPlugIn.category";
	private TaskMonitorDialog progressDialog;

	public LoadVersionedAllLayersPlugIn() {
	    Locale loc=Locale.getDefault();      	 
	    ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.layers.languages.Versioni18n",loc,this.getClass().getClassLoader());    	
	    I18N.plugInsResourceBundle.put("Version",bundle2);    	
	}

	public boolean seleccionVersion(PlugInContext context) throws Exception {


    	LayerManager layerManager = context.getLayerManager();
        boolean firingEvents = layerManager.isFiringEvents();
        layerManager.setFiringEvents(false);
		
        try {
	 		DialogoSeleccionVersion dialogo = new DialogoSeleccionVersion(context.getWorkbenchFrame());
			dialogo.setVisible(true);
			dialogo.setResizable(true);
			
	        CoordinateSystem inCoord = layerManager.getCoordinateSystem();
	        
	        
	        Version version = new Version();
	        version.setFecha(dialogo.getFecha());
	        version.setUltimaRevision(dialogo.getUltimaRevision());

			ArrayList<GeopistaLayer> layers = (ArrayList<GeopistaLayer>) layerManager.getLayers();
			GeopistaLayer layer;

			
			for (int i=0;i<layers.size();i++){
				layer = layers.get(i);
		        if (layer instanceof DynamicLayer){
//		        	context.getLayerViewPanel().getSelectionManager().clear();
		        	context.getLayerManager().getLayer(i).setDinamica(true);
		        	layer.setDinamica(true);
		        }
		        	
	        	if (layer.isVersionable() && (aplicacion.getBlackboard().get("cargarFeatures") == null || (Boolean)aplicacion.getBlackboard().get("cargarFeatures") == true)){
	        		GeopistaConnection geopistaConnection = (GeopistaConnection) layer.getDataSourceQuery().getDataSource().getConnection();
	        		DriverProperties driverProperties = geopistaConnection.getDriverProperties();
	    	        driverProperties.put("srid_destino",inCoord.getEPSGCode());
	    	        if (dialogo.getFecha().equals(""))
	    	        	version.setRevisionActual(layer.getRevisionActual());
	    	        else
	    	        	version.setRevisionActual(-1);
	    	        version.setFeaturesActivas(true);
	    	        driverProperties.put(UserPreferenceConstants.VERSION,version);
	    	        geopistaConnection = new GeopistaConnection(driverProperties);
			        //Creamos una coleccion para almacenar las excepciones que se producen
			        Collection exceptions = new ArrayList();
			        //Se realiza la query para buscar las features almacenadas en la base de datos antes de esa fecha
			        FeatureCollection featureCollection = geopistaConnection.executeQueryLayer(layer.getDataSourceQuery().getQuery(),exceptions,null,(GeopistaLayer)layer);
			        long revisionActual = layer.getRevisionActual();
			        layer = new GeopistaLayer(layer.getName(), layerManager.generateLayerFillColor(), featureCollection, layerManager);
			        layer.setLayerManager(layerManager);
			        if (version.getUltimaRevision() == revisionActual){
			        	layer.setRevisionActual(-1);
//			        	layer.setEditable(true);
//			        	context.getLayerManager().getLayer(i).setEditable(true);
			        	((GeopistaLayer)context.getLayerManager().getLayer(i)).setRevisionActual(-1);
			        }else{
			        	layer.setRevisionActual(revisionActual);
//			        	layer.setEditable(false);
//			        	context.getLayerManager().getLayer(i).setEditable(false);
			        	((GeopistaLayer)context.getLayerManager().getLayer(i)).setRevisionActual(revisionActual);
			        }
	        	}
		        
			}
			return true;
        } finally {
        	layerManager.setFiringEvents(firingEvents);
        }
	}

	public void initialize(PlugInContext context) throws Exception {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
			this,
			createEnableCheck(context.getWorkbenchContext()),
			context.getWorkbenchContext());
		ConfigurationParametersManager.addConfigurationFile("GeoPista.properties");
		
		FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      
	    JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
	                                                        .getGuiComponent()
	                                                        .getLayerNamePopupMenu();
	    featureInstaller.addPopupMenuItem(layerNamePopupMenu,
	            this, this.getName() + "...", true ,
	            GUIUtil.toSmallIcon(this.getIcon()),
	            this.createEnableCheck(context.getWorkbenchContext()));
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("loadAllVersionedLayers.gif");
	}

	public String getName() {
		return "Carga de todas las capas versionadas";
	}

	public MultiEnableCheck createEnableCheck(
		final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

		return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
									 .add(checkFactory.createExactlyOneLayerMustBeSelectedCheck())
									 .add(checkFactory.createCheckVersionedLayer());
	}
	
    public boolean execute(final PlugInContext context) throws Exception {
    	progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
    	progressDialog.setTitle(I18N.get("SeleccionVersion"));
    	progressDialog.report(I18N.get("SeleccionVersion"));
    	progressDialog.addComponentListener(new ComponentAdapter() {

    		public void componentShown(ComponentEvent e) {
    			new Thread(new Runnable() {

	                public void run() {
	                	try {
	                    	seleccionVersion(context);
	                    } catch (Exception e) {
	                    	ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("Version","SeleccionarRevision"), I18N.get("Version","SeleccionarRevision"), StringUtil
		                                .stackTrace(e));
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
}



