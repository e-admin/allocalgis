/**
 * LoadVersionedInventarioPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 12-may-2004
 */
package com.geopista.ui.plugin.layers;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.IMainInventario;
import com.geopista.app.inventario.InventarioInternalFrame;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.protocol.Version;
import com.geopista.ui.plugin.layers.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
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
public class LoadVersionedInventarioPlugIn extends AbstractPlugIn {
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	private String toolBarCategory = "LoadVersionedLayersPlugIn.category";
	private TaskMonitorDialog progressDialog;

	public LoadVersionedInventarioPlugIn() {
	    Locale loc=Locale.getDefault();      	 
	    ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.layers.languages.Versioni18n",loc,this.getClass().getClassLoader());    	
	    I18N.plugInsResourceBundle.put("Version",bundle2);    	
	}

	public boolean seleccionVersion(PlugInContext context) throws Exception {
        InventarioInternalFrame jFrame = (InventarioInternalFrame)((IMainInventario)((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getMainFrame()).getIFrame();
	 	DialogoSeleccionVersion dialogo = new DialogoSeleccionVersion(context.getWorkbenchFrame());
		dialogo.setVisible(true);
		dialogo.setResizable(true);
        Version version = new Version();
        if (!dialogo.getFecha().equals("")){
	        version.setFecha(dialogo.getFecha());
	               
	        com.geopista.protocol.inventario.Const.fechaVersion = dialogo.getFecha();
	        version.setFeaturesActivas(true);
	        AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.VERSION,version);
	    	jFrame.getJPanelMap().getGeopistaEditor().loadMap("geopista:///"+Constantes.idMapaInventario);
	        if (!dialogo.getFecha().equals("")){
		        jFrame.setFecha(formatearFecha(dialogo.getFecha()));
		        jFrame.getInventarioJPanel().tipoBienesJPanel.fireActionPerformed();
	        }
        }
		return true;
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
	            this, this.getName() + "...", false,
	            GUIUtil.toSmallIcon(this.getIcon()),
	            this.createEnableCheck(context.getWorkbenchContext()));
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("temporal.gif");
	}

	public String getName() {
		return I18N.get("Version","LoadVersionedLayers");
	}

	public MultiEnableCheck createEnableCheck(
		final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
		return null;

		/*return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
									 .add(checkFactory.createExactlyOneLayerMustBeSelectedCheck())
									 .add(checkFactory.createCheckVersionedLayer());*/
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
    
    private String formatearFecha (String fechaInicial){
    	String[] arrayFecha = fechaInicial.split("-");
    	String annio = arrayFecha[0];
    	String mes = arrayFecha[1];
    	String[] resto = arrayFecha[2].split(" ");
    	String dia = resto[0];
    	String hora = resto[1];
    	
    	return dia+"-"+mes+"-"+annio+" "+hora;
    }
}



