/**
 * GeopistaMapUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;

import java.awt.Component;
import java.io.File;
import java.net.URL;
import java.security.acl.AclNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.ActualLayers;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.model.IGeopistaMap;
import com.geopista.model.LayerFamily;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.GeopistaWorkbenchFrame;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.TaskFrame;

/**
 * Funciones útiles
 */
public class GeopistaMapUtil extends I18NUtils
{
	/**
	 * Logger for this class
	 */
	//private static final Log	logger	= LogFactory.getLog(GeopistaUtil.class);
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GeopistaMapUtil.class);

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();




    /**
     * 
     * @param mapPath 
     * @param layerManager 
     * @param blackboard 
     * @param workbenchFrame 
     * @throws java.lang.Exception 
     */
	public static void loadMap(String mapPath, ILayerManager layerManager, Blackboard blackboard, WorkbenchGuiComponent workbenchFrame) throws Exception
	{

		File file = new File(mapPath);
		try
		{

			GeopistaMap sourceTask = GeopistaMap.getMapUTF8(mapPath);
			GeopistaMap newTask = new GeopistaMap();

			if (workbenchFrame instanceof GeopistaWorkbenchFrame)
			{
				// I can't remember why I'm creating a new Task instead of using
				// sourceTask. There must be a good reason. [Jon Aquino]
				newTask.setName(sourceTask.getName());
				newTask.setExtracted(sourceTask.isExtracted());
				newTask.setDescription(sourceTask.getDescription());
				newTask.setMapUnits(sourceTask.getMapUnits());
				newTask.setMapScale(sourceTask.getMapScale());
				newTask.setMapCoordinateSystem( 
						CoordinateSystemRegistry.instance(blackboard).
						get(sourceTask.getMapProjection())
				);
				newTask.setProjectFile(file);
				workbenchFrame.addTaskFrame(newTask);
			} else
			{
				((GeopistaEditor) workbenchFrame).reset();
				newTask = sourceTask;
				newTask.setName(GUIUtil.nameWithoutExtension(file));
				newTask.setProjectFile(file);
			}

			LayerManager sourceLayerManager = (LayerManager)sourceTask.getLayerManager();
			for (Iterator i = sourceLayerManager.getCategories().iterator(); i.hasNext();)
			{
				Category sourceLayerCategory = (Category) i.next();

				LayerManager newLayerManager = (LayerManager)newTask.getLayerManager();
				CoordinateSystemRegistry registry = CoordinateSystemRegistry.instance(blackboard);

				newLayerManager.addCategory(sourceLayerCategory.getName());

				ArrayList layerables = new ArrayList(sourceLayerCategory.getLayerManager().getOrderLayers());
				
				for (Iterator j = layerables.iterator(); j.hasNext();)
				{
					Layerable layerable = (Layerable) j.next();
					layerable.setLayerManager(newLayerManager);
					if (layerable instanceof Layer)
					{
						Layer layer = (Layer) layerable;
						layer.setFeatureCollection(executeQuery(layer.getDataSourceQuery().getQuery(), layer.getDataSourceQuery().getDataSource(), registry, null));

						GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();

						Map properties = new HashMap();

						// Introducimos el mapa Origen
						properties.put("mapadestino", newTask);

						// Introducimos el fitro geometrico si es distinto de
						// null, si se introduce null falla
						// properties.put("filtrogeometrico",null);
						// Introducimos el FilterNode
						properties.put("nodofiltro", FilterLeaf.equal("1", new Integer(1)));
						serverDataSource.setProperties(properties);

						URL urlLayer = new URL("geopistalayer://default/" + ((IGeopistaLayer) layer).getSystemId());

						layer.setFeatureCollectionModified(false);
						DataSourceQuery dataSourceQuery = new DataSourceQuery();
						dataSourceQuery.setQuery(urlLayer.toString());
						dataSourceQuery.setDataSource(serverDataSource);
						layer.setDataSourceQuery(dataSourceQuery);
						layer.setFeatureCollectionModified(false);

					}

					newLayerManager.addLayerable(sourceLayerCategory.getName(), layerable);
				}
			}

		} catch (Exception e)
		{
			logger
			.error(
					"loadMap(String, LayerManager, Blackboard, WorkbenchGuiComponent)",
					e);
			throw e;
		}

	}

	private static FeatureCollection executeQuery(String query, DataSource dataSource, CoordinateSystemRegistry registry, TaskMonitor monitor) throws Exception
	{
		Connection connection = dataSource.getConnection();
		try
		{
			return dataSource.installCoordinateSystem(connection.executeQuery(query, monitor), registry);
		} finally
		{
			connection.close();
		}
	}


    /**
     * 
     * @param urlMapGeopista 
     * @param workbenchContext 
     * @param monitor 
     * @throws java.security.acl.AclNotFoundException 
     * @throws com.geopista.server.administradorCartografia.ACException 
     * @throws java.lang.Exception 
     */
	
	public static void loadDataBaseMap(String urlMapGeopista, WorkbenchContext workbenchContext, TaskMonitor monitor) throws AclNotFoundException, ACException, Exception{
		loadDataBaseMap(urlMapGeopista,null,workbenchContext,monitor);
	}

	public static void loadDataBaseMap(String urlMapGeopista, String[] filtro,WorkbenchContext workbenchContext, TaskMonitor monitor) throws AclNotFoundException, ACException, Exception
	{

		long startMils=Calendar.getInstance().getTimeInMillis();
		String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
		File file = null;

		if (!aplicacion.isLogged())
		{

			aplicacion.setProfile("Geopista");
			aplicacion.login();
		}

		if (aplicacion.isLogged())
		{
			GeopistaMap tempMap = new GeopistaMap();
			// monitor.report(aplicacion.getI18nString("GeopistaLoadMapPlugIn.CargandoMapa")+"
			// "+tempMap.getName());
			AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(sUrlPrefix + "/AdministradorCartografiaServlet");

			tempMap.setSystemId(urlMapGeopista);
			tempMap.setIdEntidad(aplicacion.getIdEntidad());
			tempMap.setIdMunicipio(aplicacion.getIdMunicipio());

			IGeopistaMap sourceTask = administradorCartografiaClient.loadMap(tempMap, UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true), null, FilterLeaf.equal("1", new Integer(1)),monitor);

			long endMils=Calendar.getInstance().getTimeInMillis();
			logger.info("Tiempo Total carga Mapa Paso 1:"+sourceTask.getName()+" :"+(endMils-startMils)+" mils"+" IdMunicipio:"+aplicacion.getIdMunicipio());
			System.out.println("Tiempo Total carga Mapa Paso 1:"+sourceTask.getName()+" :"+(endMils-startMils)+" mils"+" IdMunicipio:"+aplicacion.getIdMunicipio());
			String mapSystemId = sourceTask.getSystemId();
			JInternalFrame[] loaderMaps = workbenchContext.getIWorkbench().getGuiComponent().getInternalFrames();

			if (loaderMaps != null)
			{
				for (int i = 0; i < loaderMaps.length; i++)
				{
					String actualSystemId = ((GeopistaMap) (((TaskFrame) loaderMaps[i]).getTaskFrame()).getTask()).getSystemId();
					if (actualSystemId.equals(mapSystemId))
					{
						(((TaskFrame) loaderMaps[i]).getTaskFrame()).moveToFront();
						return;
					}
				}
			}

			GeopistaMap newTask = new GeopistaMap(workbenchContext);

			ILayerManager newLayerManager = null;
			ActualLayers actualLayers=null;

			WorkbenchGuiComponent workbenchFrame = workbenchContext.getIWorkbench().getGuiComponent();

			if (workbenchFrame instanceof GeopistaWorkbenchFrame)
			{
				// I can't remember why I'm creating a new Task instead of using
				// sourceTask. There must be a good reason. [Jon Aquino]
				newTask.setName(sourceTask.getName());
				newTask.setExtracted(sourceTask.isExtracted());
				newTask.setDescription(sourceTask.getDescription());
                newTask.setMapProjection(sourceTask.getMapProjection());
				newTask.setMapUnits(sourceTask.getMapUnits());
				newTask.setMapScale(sourceTask.getMapScale());
				newTask.setMapCoordinateSystem( 
						CoordinateSystemRegistry.instance(workbenchContext.getBlackboard()).
						get(sourceTask.getMapProjection())
				);
				newTask.setSystemId(sourceTask.getSystemId());
				workbenchFrame.addTaskFrame(newTask);
				newLayerManager = newTask.getLayerManager();
			} else
			{
				//CAMBIO PARA NO BORRAR LAS CAPAS DEL MAPA QUE ESTUVIERAN CREADAS
				
				if (filtro!=null)
					actualLayers=((GeopistaEditor) workbenchContext.getIWorkbench().getGuiComponent()).reset(filtro);
				else
					((GeopistaEditor) workbenchContext.getIWorkbench().getGuiComponent()).reset();
				
				//((GeopistaEditor) workbenchContext.getIWorkbench().getGuiComponent()).reset();
				
				workbenchContext.getTask().setName(sourceTask.getName());
				workbenchContext.getTask().setProjectFile(file);
				if (workbenchContext.getTask() instanceof GeopistaMap){
					((GeopistaMap)workbenchContext.getTask()).setMapProjection(sourceTask.getMapProjection());
					
					
					//SATEC. Control del SRID a utilizar para cargar multiples
					Integer srid_destino=-1;
					if ((AppContext.getApplicationContext().getBlackboard().get(Const.KEY_USE_SAME_SRID)!=null) &&
							((Integer)AppContext.getApplicationContext().getBlackboard().get(Const.KEY_USE_SAME_SRID)==1)){
						srid_destino=Integer.parseInt((String)AppContext.getApplicationContext().getBlackboard().get(Const.KEY_SRID_MAPA),10);
						((GeopistaMap)workbenchContext.getTask()).setMapCoordinateSystem( 
								CoordinateSystemRegistry.instance(workbenchContext.getBlackboard()).
								get(srid_destino)
						);
					}
					else{
						((GeopistaMap)workbenchContext.getTask()).setMapCoordinateSystem( 
								CoordinateSystemRegistry.instance(workbenchContext.getBlackboard()).
								get(sourceTask.getMapProjection())
						);
					}
					
					
					
					
					
					((GeopistaMap)workbenchContext.getTask()).setSystemId(sourceTask.getSystemId());
					//JARC Se debe reconocer el mapa como mapa del sistema para poder actualizarlo desde los modulos
					((GeopistaMap)workbenchContext.getTask()).setSystemMap(sourceTask.isSystemMap());
					((GeopistaEditor) workbenchContext.getIWorkbench().getGuiComponent()).addTaskFrame((Task)workbenchContext.getTask());
				}
				if (workbenchContext.getTask() instanceof GeopistaMap){
					newLayerManager = ((GeopistaMap)workbenchContext.getTask()).getLayerManager();
				}
				else{
					newLayerManager = workbenchContext.getLayerManager();
				}

			}
			
			//TODO.
			//Las capas o me equivoco o ya vienen ordenadas (¿Es preciso ordenarlas de nuevo?)

            int categoryLayerCount = 0;
			ILayerManager sourceLayerManager = sourceTask.getLayerManager();
			for (Iterator i = sourceLayerManager.getCategories().iterator(); i.hasNext();)
			{
				Category sourceLayerCategory = (Category) i.next();

				CoordinateSystemRegistry registry = CoordinateSystemRegistry.instance(workbenchContext.getIWorkbench().getGuiComponent().getContext().getBlackboard());

				newLayerManager.addCategory(sourceLayerCategory.getName());
                /* obtenemos la LayerFamily y le ponemos el atributo de categoria de sistema a true
				LayerFamily newLayerFamily = (LayerFamily) newLayerManager.getCategory(sourceLayerCategory.getName());
				newLayerFamily.setSystemLayerFamily(true);
				newLayerFamily.setSystemId(((LayerFamily) sourceLayerCategory).getSystemId());
                */
                // obtenemos la LayerFamily y le ponemos el atributo de categoria de sistema a true
                // siempre y cuando no sea un LayerFamily ficticio para las WMSLayers.
                if(((LayerFamily)sourceLayerCategory).isSystemLayerFamily()){
                    LayerFamily newLayerFamily = (LayerFamily) newLayerManager.getCategory(sourceLayerCategory.getName());
                    newLayerFamily.setSystemLayerFamily(true);
                    newLayerFamily.setSystemId(((LayerFamily) sourceLayerCategory).getSystemId());
                }
				ArrayList layerables = new ArrayList(sourceLayerCategory.getLayerables());
				Collections.reverse(layerables);
                categoryLayerCount++;
                int layerablesCount = 0;
				for (Iterator j = layerables.iterator(); j.hasNext();)
				{
					Layerable layerable = (Layerable) j.next();
					layerable.setLayerManager(newLayerManager);
					// monitor.report(aplicacion.getI18nString("GeopistaLoadMapPlugIn.Cargando")
							// + " " + layerable.getName());
                    //Adaptacion de Geopista para WMS --->
                    layerablesCount++;
                    if(layerable instanceof WMSLayer){
                    	newLayerManager.addLayerable(sourceLayerCategory.getName(), layerable, layerablesCount); 
                    }
                    // <--- Adaptacion de Geopista para WMS
                    else if (!((IGeopistaLayer) layerable).getSystemId().equalsIgnoreCase("error"))
					{
						if (layerable instanceof Layer)
						{
							Layer layer = (Layer) layerable;
							GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();

							Map properties = new HashMap();
							if (workbenchFrame instanceof GeopistaWorkbenchFrame)
							{
								// Introducimos el mapa Origen
								properties.put("mapadestino", newTask);
							} else
							{
								properties.put("mapadestino",(GeopistaMap) workbenchContext.getTask());
							}
							// Introducimos el fitro geometrico si
							// es distinto de null, si se introduce
							// null falla
							// properties.put("filtrogeometrico",null);
							// Introducimos el FilterNode
							properties.put("nodofiltro", FilterLeaf.equal("1", new Integer(1)));
							serverDataSource.setProperties(properties);

							URL urlLayer = new URL("geopistalayer://default/"+ ((IGeopistaLayer) layer).getSystemId());

							layer.setFeatureCollectionModified(false);
							DataSourceQuery dataSourceQuery = new DataSourceQuery();
							dataSourceQuery.setQuery(urlLayer.toString());
							dataSourceQuery.setDataSource(serverDataSource);
							layer.setDataSourceQuery(dataSourceQuery);

						}

						newLayerManager.addLayerable(sourceLayerCategory.getName(), layerable, sourceLayerManager.indexOf((Layer) layerable));
					} else
					{
						JOptionPane.showMessageDialog((Component) workbenchContext.getIWorkbench().getGuiComponent(), aplicacion.getI18nString("GeopistaLoadMapPlugIn.CapaErronea"));
					}
				}
			}
			
			try {
				//Añadimos las capas a mantener si las hubiera
				if (actualLayers!=null){
					int lastPosition=newLayerManager.size();
					Iterator it=actualLayers.getLayers().iterator();
					while (it.hasNext()){
						Layerable actualLayerable=(Layerable)it.next();						
						boolean encontrado=false;
						ArrayList lstLayers = new ArrayList(sourceLayerManager.getOrderLayers());
			            Iterator itLayers = lstLayers.iterator();
			            while (itLayers.hasNext()){
			            	Layerable layerable = (Layerable)itLayers.next();
			            	if (layerable!=null && layerable.getName().equals(actualLayerable.getName())){
			            		encontrado=true;
			            		break;								
			            	}
			            }
			            if(!encontrado){
			            	String categoryName=actualLayers.getCategory(actualLayerable.getName());
							lastPosition=lastPosition+1;
			            	newLayerManager.addLayerable(categoryName, actualLayerable, lastPosition);	
			            }
					}
					
					
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


            //Coloco las capas en el orden adecuado. Esto hay que incluirlo
            //porque de lo contrario las capas se ordenan solo dentro de sus catgorias
            //JAVIER
			ArrayList lstLayers = new ArrayList(sourceLayerManager.getOrderLayers());
            Iterator itLayers = lstLayers.iterator();
            while (itLayers.hasNext()){
            	Layerable layerable = (Layerable)itLayers.next();
            	if (layerable!=null){
                	String categoryName = layerable.getLayerManager().getCategory(layerable).getName();
                	newLayerManager.remove(layerable);                                  
                	newLayerManager.addLayerablePanel(categoryName, layerable, lstLayers.indexOf(layerable));
            	}
            	else{
            		logger.warn("Se ha producido un error al cargar la capa:"+layerable);
            	}
            }
			
			endMils=Calendar.getInstance().getTimeInMillis();
			System.out.println("Tiempo Total carga Mapa Paso 2:"+sourceTask.getName()+" :"+(endMils-startMils)+" mils"+" IdMunicipio:"+aplicacion.getIdMunicipio());

		}

	}
}