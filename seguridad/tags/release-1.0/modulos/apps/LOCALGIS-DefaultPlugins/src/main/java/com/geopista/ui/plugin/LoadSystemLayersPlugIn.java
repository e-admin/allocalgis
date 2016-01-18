package com.geopista.ui.plugin;

/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.model.LayerFamily;
import com.geopista.server.administradorCartografia.ACLayerFamily;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.server.administradorCartografia.ObjectNotFoundException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.ui.dialogs.LoadSystemLayersPanel01;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class LoadSystemLayersPlugIn extends ThreadedBasePlugIn {

	
	private static final Log	logger	= LogFactory.getLog(LoadSystemLayersPlugIn.class);

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();
	private Blackboard blackboard = aplicacion.getBlackboard();

	public LoadSystemLayersPlugIn() {
	}

	public void initialize(PlugInContext context) throws Exception {

		FeatureInstaller featureInstaller = new FeatureInstaller(context
				.getWorkbenchContext());

		JPopupMenu layerNamePopupMenu = context.getWorkbenchContext()
				.getIWorkbench().getGuiComponent().getLayerNamePopupMenu();
		featureInstaller.addPopupMenuItem(context.getWorkbenchContext()
				.getIWorkbench().getGuiComponent().getCategoryPopupMenu(),
				this, aplicacion.getI18nString(this.getName()) + "...", false,
				null, LoadSystemLayersPlugIn.createEnableCheck(context
						.getWorkbenchContext()));
		featureInstaller.addMainMenuItem(this,
				aplicacion.getI18nString("File"), aplicacion.getI18nString(this
						.getName())
						+ "...", null, LoadSystemLayersPlugIn
						.createEnableCheck(context.getWorkbenchContext()));

	}

	public boolean execute(PlugInContext context) throws Exception {

		if (!aplicacion.isLogged()) {

			aplicacion.setProfile("Geopista");
			aplicacion.login();
		}

		if (aplicacion.isLogged()) {

			WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context
					.getWorkbenchGuiComponent()), aplicacion
					.getI18nString("LoadSystemLayerDialog"), context
					.getErrorHandler());
			d.init(new WizardPanel[] { new LoadSystemLayersPanel01(
					"LoadSystemLayersPanel01", null, context) });

			// Set size after #init, because #init calls #pack. [Jon Aquino]
			d.setSize(550, 450);
			GUIUtil.centreOnWindow(d);
			d.setVisible(true);
			if (!d.wasFinishPressed()) {
				return false;
			}

			return true;
		} else {
			return false;
		}
	}

	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {

		String nameLayerFamily=null;
		String layerToLoad="NoDisponible";
		try{
	        String sUrlPrefix = aplicacion.getString(Constantes.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA);
	        AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(sUrlPrefix);
			ArrayList layerFamiliesIDsList = (ArrayList) blackboard
					.get("SelectedLayerFamilies");
	
			Iterator layerFamiliesIDsListIter = layerFamiliesIDsList.iterator();
	
			ArrayList listaErrPerm = new ArrayList();
			
			while (layerFamiliesIDsListIter.hasNext()) {
				ACLayerFamily idLayerFamily = (ACLayerFamily) layerFamiliesIDsListIter
						.next();
	
				nameLayerFamily=idLayerFamily.getName();
				monitor.report(aplicacion
						.getI18nString("LoadSystemLayers.LayerFamily")
						+ " " + idLayerFamily.getName());
				String[] layerIDs = administradorCartografiaClient
						.getLayerIDs(idLayerFamily.getId());
	
				for (int n = layerIDs.length - 1; n >= 0; n--) {
					monitor.report(aplicacion
							.getI18nString("LoadSystemLayers.Cargando"));
					boolean layerRepeated = false;
					List currentLayers = context.getLayerManager().getLayers();
					Iterator currentLayersIterator = currentLayers.iterator();
					while (currentLayersIterator.hasNext()) {
						Layer currentLayer = (Layer) currentLayersIterator.next();
						if (currentLayer instanceof GeopistaLayer) {
							String currentSystemId = ((GeopistaLayer) currentLayer)
									.getSystemId();
							if (currentSystemId.trim().equals(layerIDs[n])) {
								layerRepeated = true;
								break;
							}
						}
					}
	
					if (layerRepeated)
						continue;
	
					IGeopistaLayer layer = null;
					
					
					try {
						GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
						Map properties = new HashMap();
						// Introducimos el mapa Origen
						properties.put("mapadestino", (GeopistaMap) context
								.getTask());
						// Introducimos el fitro geometrico si es distinto de null,
						// si se introduce null falla
						// properties.put("filtrogeometrico",null);
						// Introducimos el FilterNode
						properties.put("nodofiltro", FilterLeaf.equal("1",
								new Integer(1)));
						
						// Introducimos el srid del mapa destino. Importante para
						// cuando se crear un mapa en el editor.
						try {
							properties.put("srid_destino", Integer.valueOf(context
									.getLayerManager().getCoordinateSystem()
									.getEPSGCode()));
						} catch (Exception e1) {							
						}
	
						serverDataSource.setProperties(properties);
						GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource
								.getConnection();
	
						layerToLoad=layerIDs[n];
						// Creamos una coleccion para almacenar las excepciones que
						// se producen
						Collection exceptions = new ArrayList();
						// preparamos la url de la layer
						URL urlLayer = new URL("geopistalayer://default/"
								+ layerIDs[n]);
						// URL urlLayer = new URL("geopistalayer:"+layerIDs[n]);
	
						geopistaConnection.executeQuery(urlLayer.toString(),
								exceptions, monitor);
	
						boolean cancelado=false;
						if (exceptions.size() > 0) {
							// En caso de que sea una excepcion de permisos,
							Iterator recorreExcepcion = exceptions.iterator();
							while (recorreExcepcion.hasNext()) {
								// Revisar el efecto del continue
								Exception e = (Exception) recorreExcepcion.next();
								if (e instanceof CancelException){
									JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa/capa cancelada");
									cancelado=true;;
								}
								else if (e
										.getCause()
										.getLocalizedMessage()
										.toString()
										.equals(
												"PermissionException: Geopista.Layer.Leer")) {
									listaErrPerm.add(e);
								} 
								else if (e instanceof CancelException){
									JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa/capa cancelada");
									cancelado=true;
								}
								else {
									JOptionPane
											.showMessageDialog(
													(Component) context
															.getWorkbenchGuiComponent(),
													aplicacion
															.getI18nString("LoadSystemLayers.CapaErronea")+" Familia:"+nameLayerFamily+" Capa:"+layerToLoad);
								}
							}
							
							if (cancelado)
								break;
							continue;
						}
						layer = geopistaConnection.getLayer();
						DataSourceQuery dataSourceQuery = new DataSourceQuery();
						dataSourceQuery.setQuery(urlLayer.toString());
						dataSourceQuery.setDataSource(serverDataSource);
						layer.setDataSourceQuery(dataSourceQuery);
	
						// layer=administradorCartografiaClient.loadLayer((GeopistaMap)
						// context.getTask()
						// ,layerIDs[n],aplicacion.getUserPreference(AppContext.PREFERENCES_LOCALE_KEY,"es_ES",true),null,FilterLeaf.equal("1",new
						// Integer(1)));
					} catch (Exception e) {
						JOptionPane.showMessageDialog((Component) context
								.getWorkbenchGuiComponent(), aplicacion
								.getI18nString("LoadSystemLayers.CapaErronea")+" Familia:"+nameLayerFamily+" Capa:"+layerToLoad);
						continue;
					}
	
					if (layer != null
							&& !layer.getSystemId().equalsIgnoreCase("error")) {
						context.getLayerManager().addLayer(idLayerFamily.getName(),
								layer);
						((LayerFamily) context.getLayerManager().getCategory(
								idLayerFamily.getName()))
								.setSystemLayerFamily(true);
						((LayerFamily) context.getLayerManager().getCategory(
								idLayerFamily.getName())).setSystemId(String
								.valueOf(idLayerFamily.getId()));
					} else {
						JOptionPane.showMessageDialog((Component) context
								.getWorkbenchGuiComponent(), aplicacion
								.getI18nString("LoadSystemLayers.CapaErronea")+" Familia:"+nameLayerFamily+" Capa:"+layerToLoad);
					}
				}
	
			}
			if (listaErrPerm.size() > 0) {
				JOptionPane.showMessageDialog((Component) context
						.getWorkbenchGuiComponent(), (listaErrPerm.size())
						+ " capa(s) no han sido abiertas por falta de permisos");
			}
		}
		catch (CancelException e1){
      	  JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa cancelada");
		}
		 catch (Exception e)
         {
			logger.error("run(TaskMonitor, PlugInContext)", e);
             Throwable errorCause = e.getCause();
             if(errorCause instanceof PermissionException)
             {
             	String error=errorCause.getMessage();
                 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
                 		aplicacion.getI18nString("LoadSystemLayersPlugIn.NoPermisosCargarCapa")  + " Familia:"+nameLayerFamily+" Capa:"+layerToLoad+"\n Error detallado:"+error);
             }
             else
             {
                 if(errorCause!=null)
                 {
                     Throwable subErrorCause = errorCause.getCause();
                     if(subErrorCause instanceof ObjectNotFoundException)
                     {

                         JOptionPane.showMessageDialog(aplicacion.getMainFrame(),subErrorCause.getMessage() + " " + aplicacion.getI18nString("GeopistaLoadMapPlugIn.DarAltaMunicipio"));
                     }
                     else
                     {
                         ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("LoadSystemLayersPlugIn.ProblemasCargarCapa") + " " + nameLayerFamily, aplicacion.getI18nString("GeopistaLoadMapPlugIn.ProblemasCargarMapa") + " " + nameLayerFamily, StringUtil
                             .stackTrace(e));
                     }
                 }
                 else
                 {
                     ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("LoadSystemLayersPlugIn.ProblemasCargarCapa") + " " +nameLayerFamily, aplicacion.getI18nString("GeopistaLoadMapPlugIn.ProblemasCargarMapa") + " " + nameLayerFamily, StringUtil
                             .stackTrace(e));
                 }
             }
				logger.error("run(TaskMonitor, PlugInContext)", e);
         }
	}

	public static MultiEnableCheck createEnableCheck(
			final WorkbenchContext workbenchContext) {
		GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
				workbenchContext);

		return new MultiEnableCheck().add(checkFactory
				.createWindowWithLayerManagerMustBeActiveCheck());

	}

}
