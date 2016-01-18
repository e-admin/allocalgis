/**
 * MobileAssignCellsPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.ui.dialogs.beans.ComboItemGraticuleListener;
import com.geopista.ui.dialogs.beans.ExtractionProject;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.mobile.MobileAssignCellsPanel01;
import com.geopista.ui.dialogs.mobile.MobileAssignCellsPanel02;
import com.geopista.ui.dialogs.mobile.MobilePluginI18NResource;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaAbstractSaveMapPlugIn;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.config.UserPreferenceStore;
import com.geostaf.ui.plugin.generate.GraticuleCreatorEngine;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class MobileAssignCellsPlugin extends GeopistaAbstractSaveMapPlugIn{
	
	private static final String _ZIP_EXT = ".zip";
	public static final String PERMISOS_CELDAS_PROP = "permisos_celdas.properties";
	//private static final Log logger = LogFactory.getLog(MobileExtractPlugin.class);
    private Blackboard blackboard = Constants.APLICACION.getBlackboard();
    public static final String PluginMobileExtracti18n = "PluginMobileExtracti18n";
	
//    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
//    {
//        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
//
//        return new MultiEnableCheck().add(
//                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
//                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
//                checkFactory.createWindowWithSystemMapMustBeActiveCheck());
//    }
    
	/**
	 * Inicialización del plugin para cargarlo desde el fichero workbench.properties
	 */
	public void initialize(PlugInContext context) throws Exception {
		//para internacionalización
	    Locale currentLocale = I18N.getLocaleAsObject();        
	    ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.mobile.language.PluginMobileExtracti18n",currentLocale);      
	    I18N.plugInsResourceBundle.put(PluginMobileExtracti18n,bundle);
		
	    FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      
	    EnableCheckFactory checkFactory = context.getCheckFactory();
        featureInstaller.addMainMenuItem(this,
                new String[] { "Tools", MobilePluginI18NResource.GEOPISTAConfiguration_proyectMovil }, I18N.get(PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPlugin_asignUsu),
                false, null,
                new MultiEnableCheck().add(
                    checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                    .add(checkFactory.createAdminUserCheck()));
    	((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar("Movilidad").addPlugIn(this.getIcon(),
				this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());		
	
	}
	
    public String getName()
    {
        return I18N.get(PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPlugin_asignUsu);
    }
    
	public ImageIcon getIcon() {
		return IconLoader.icon("/com/geopista/ui/images/movilidad_permisos.png");
	}
    
    public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
				.add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck());
	}
    
    public boolean execute(PlugInContext context) throws Exception
    {
        //context.getActiveInternalFrame().setSize(640, 480);
        reportNothingToUndoYet(context);
        
        //limpiamos lo que haya podido quedar de ejecuciones anteriores
        emptyBlackboard();

        WizardDialog d = new WizardDialog(GeopistaUtil.getFrame(context
                .getWorkbenchGuiComponent()), Constants.APLICACION.getI18nString("ExtractDialogUserAssign"),
                context.getErrorHandler());
        d.init(new WizardPanel[] {
        		new MobileAssignCellsPanel01("MobileAssignCellsPanel01", "MobileAssignCellsPanel02", context),
        		new MobileAssignCellsPanel02("MobileAssignCellsPanel02", null, context)
        		});

        // Set size after #init, because #init calls #pack.
        d.setSize(520, 650);
        d.setLocation(10, 20);
        GUIUtil.centreOnWindow(d);
        d.setVisible(true);        
        
        if (!d.wasFinishPressed())
        {
        	LayerManager layerManager = context.getLayerManager();
            Layer graticuleLayer = layerManager.getLayer(GraticuleCreatorEngine.getGraticuleName());
            //si existe una cuadrícula la borramos
            if(graticuleLayer!=null){
            	layerManager.remove(graticuleLayer);
            }
            return false;
        }

        return true;

    }
    
    /**
     * Borra lo que haya utilizado del blackboard en sus anteriores ejecuciones
     */
    private void emptyBlackboard() {
		blackboard.remove(MobileAssignCellsPanel01.SELECTED_EXTRACT_PROJECT);
		blackboard.remove(MobileAssignCellsPanel02.MOBILE_USERS_GRATICULES);
	}
    
    /*
     * Se ejecuta una vez finalizado el interfaz despues de pasar por todas las pantallas.
     * (non-Javadoc)
     * @see com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn#run(com.vividsolutions.jump.task.TaskMonitor, com.vividsolutions.jump.workbench.plugin.PlugInContext)
     */
    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {
    	try {
	    	monitor.report(I18N.get(PluginMobileExtracti18n,MobilePluginI18NResource.MobileAssignCellsPlugin_asignCeld));
	    	
	    	//obtenemos la lista de celdas y los usuarios asignados
	    	HashMap<String, String> celdasUsuarios = (HashMap<String, String>) blackboard.get(MobileAssignCellsPanel02.MOBILE_USERS_GRATICULES);
	    	ExtractionProject eProject =  (ExtractionProject) blackboard.get(MobileAssignCellsPanel01.SELECTED_EXTRACT_PROJECT);
	    	
	    	//guardamos la informacion en base de datos
	    	final String sUrlPrefix = Constants.APLICACION.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL);
	      	AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
	                     sUrlPrefix + WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME);
	      	administradorCartografiaClient.setAssignCellsExtractProject(eProject.getIdProyecto(), celdasUsuarios);  	
	      	
	    	//creamos el fichero de properties
	       	String dirBase = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY, UserPreferenceConstants.DEFAULT_DATA_PATH, true);
	       	dirBase+=File.separator+"maps";
	        String dirMapName = eProject.getNombreProyecto() + "." + eProject.getIdProyecto();
	        File dirBaseMake = new File(dirBase, dirMapName);
	      	File filePerm = new File(dirBaseMake, PERMISOS_CELDAS_PROP);
	      	FileOutputStream fOutPerm = new FileOutputStream(filePerm);
	      	Properties propCellUsers = new Properties();
		   	String idCelda = null;
		   	String idUsuario = null;
		   	Iterator<String> keyIterator = celdasUsuarios.keySet().iterator();
		    while (keyIterator.hasNext()) {
				idCelda = (String) keyIterator.next();
				idUsuario =  celdasUsuarios.get(idCelda);
				//si estan sin asignar no metemos nada
		    	if(!idUsuario.equals(ComboItemGraticuleListener.SIN_ASIGNAR)){
		    		propCellUsers.put(idCelda, idUsuario);
		    	}
			}    	
	      	propCellUsers.store(fOutPerm, "Fichero de properties IdCelda=IdUsuario");
	      	fOutPerm.close();
	      	
			//subida http del fichero de permisos
	    	List<File> listaRutasFicherosUpload = new ArrayList<File>();
	    	listaRutasFicherosUpload.add(filePerm);
	    	List<String> paramNames = new ArrayList<String>();
	    	List<String> paramValues = new ArrayList<String>();
	    	paramNames.add(GeopistaUtil.HTTP_FILE_TYPE_HEADER);
	    	paramNames.add(GeopistaUtil.HTTP_ZIP_REFER_HEADER);
	    	paramValues.add(GeopistaUtil.HTTP_PROP_HEADER);
	    	paramValues.add(eProject.getNombreProyecto()+"."+eProject.getIdProyecto()+_ZIP_EXT);
	    	GeopistaUtil.httpUploadFiles(Constants.URL_SERVER, listaRutasFicherosUpload, paramNames, paramValues);
      	
		}finally {
	      	//borramos la cuadrícula
	    	LayerManager layerManager = context.getLayerManager();
	        Layer graticuleLayer = layerManager.getLayer(GraticuleCreatorEngine.getGraticuleName());
	        if(graticuleLayer!=null){
	        	layerManager.remove(graticuleLayer);
	        }
		}
    }

}

