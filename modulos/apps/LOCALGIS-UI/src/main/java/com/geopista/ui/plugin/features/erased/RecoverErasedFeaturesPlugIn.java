/**
 * RecoverErasedFeaturesPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 12-may-2004
 */
package com.geopista.ui.plugin.features.erased;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.Version;
import com.geopista.ui.plugin.features.erased.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import es.enxenio.util.configuration.ConfigurationParametersManager;

/**
 * Con este plugin se recuperan features dadas de baja o modificadas en anteriores revisiones
 * @author COTESA
 */
public class RecoverErasedFeaturesPlugIn extends AbstractPlugIn {
	private final static String LAST_TAB_KEY = RecoverErasedFeaturesPlugIn.class.getName() +
		" - LAST TAB";

  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private String toolBarCategory = "RecoverErasedFeaturesPlugIn.category";
  private PlugInContext context;

	public RecoverErasedFeaturesPlugIn() {
	    Locale loc=Locale.getDefault();      	 
	    ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.features.erased.languages.Erasedi18n",loc,this.getClass().getClassLoader());    	
	    I18N.plugInsResourceBundle.put("Erased",bundle2);    	
	}

    public boolean execute(PlugInContext context) throws Exception {
    	if (JOptionPane.showConfirmDialog(
				null,
				I18N.get("Erased","RecoverErasedFeaturesQuestion"),
				I18N.get("Erased","RecoverErasedFeatures"),
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
    		final GeopistaLayer layer = (GeopistaLayer)context.getSelectedLayer(0);
	    	boolean firingEvents = layer.getLayerManager().isFiringEvents();
	    	this.context = context;
    		try{
    	    	layer.getLayerManager().setFiringEvents(false);
	    		final Object[] features = context.getLayerViewPanel().
	    													getSelectionManager().getFeaturesWithSelectedItems().toArray();
	    		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(),
	        			context.getErrorHandler());
	            progressDialog.setTitle(I18N.get("LoadSystemLayers.Cargando"));
	            progressDialog.report(I18N.get("LoadSystemLayers.Cargando"));
	            progressDialog.addComponentListener(new ComponentAdapter() {
	                public void componentShown(ComponentEvent e) {
	                    //Wait for the dialog to appear before starting the task. Otherwise
	                    //the task might possibly finish before the dialog appeared and the
	                    //dialog would never close. [Jon Aquino]
	                    new Thread(new Runnable(){
	                        public void run()
	                        {
	                        	boolean exito = false;
	                            try
	                            {
	                            	int n = features.length;
	                            	for (int i=0;i<n;i++){
	                            		((GeopistaFeature)features[i]).setDirty(true);
	                            		Object[] arrayFeatures = new Object[1];
	                            		arrayFeatures[0] = features[i];
	                            		exito = execute(arrayFeatures, layer, progressDialog);
	                            	}
	                            }catch(Exception e){
	                            	if (exito == false){
	                    	            JOptionPane.showMessageDialog(null,I18N.get("Erased","ErrorFeaturesRecuperadas"));
	                            	}
	                            }finally
	                            {
	                                progressDialog.setVisible(false);
	                            } 
	                        }
	                    }).start();
	                }
	            });
	            GUIUtil.centreOnWindow(progressDialog);
	            progressDialog.setVisible(true);
	            reportNothingToUndoYet(context);
	            JOptionPane.showMessageDialog(null,I18N.get("Erased","FeaturesRecuperadas"));
	    		return true;
    		}finally{
    	       	layer.getLayerManager().setFiringEvents(firingEvents);
    		}
	    }else{
	       	return false;
        }
    }
    
    public boolean execute(Object[] features, GeopistaLayer layer, TaskMonitorDialog monitor)
        throws Exception {
        try{
        	GeopistaConnection geopistaConnection = (GeopistaConnection) layer.getDataSourceQuery().getDataSource().getConnection();
        	DriverProperties driverProperties = geopistaConnection.getDriverProperties();
        	Version version = new Version();
        	version.setbRecuperarFeatures(false);
        	driverProperties.put(UserPreferenceConstants.VERSION, version);
        	driverProperties.put("srid_destino", context.getLayerManager().getCoordinateSystem().getEPSGCode());
	        geopistaConnection.executeUpdateFeatures(features, monitor);
        }catch(Exception e)
        {
            System.out.println("Error al grabar la feature:"+e.toString());
            return false;
        }
        finally{
        }
        return true;
    }

	public void initialize(PlugInContext context) throws Exception {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
			this, createEnableCheck(context.getWorkbenchContext()),context.getWorkbenchContext());
		ConfigurationParametersManager.addConfigurationFile("GeoPista.properties");
		
		FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      
	    JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
	                                                        .getGuiComponent()
	                                                        .getLayerNamePopupMenu();
	    featureInstaller.addPopupMenuItem(layerNamePopupMenu,
	            this, aplicacion.getI18nString(this.getName()) + "...", false,
	            GUIUtil.toSmallIcon(this.getIcon()),
	            this.createEnableCheck(context.getWorkbenchContext()));
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("btn_recuperar.png");
	}


	public String getName() {
		return I18N.get("Erased","RecoverErasedFeatures");
	}

	public MultiEnableCheck createEnableCheck(
		final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

		return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
		 .add(checkFactory.createExactlyOneLayerMustBeSelectedCheck())
		 .add(checkFactory.createNotLastVersionedLayer())
		 .add(checkFactory.createCheckVersionedLayer());
	}
}



