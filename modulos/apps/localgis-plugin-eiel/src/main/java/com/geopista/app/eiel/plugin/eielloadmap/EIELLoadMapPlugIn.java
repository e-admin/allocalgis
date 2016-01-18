/**
 * EIELLoadMapPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.plugin.eielloadmap;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.dialogs.EIELListaMapasDialog;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaSchema;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.model.LayerFamily;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.server.administradorCartografia.ObjectNotFoundException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.ui.GeopistaWorkbenchFrame;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class EIELLoadMapPlugIn extends ThreadedBasePlugIn
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(EIELLoadMapPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private GeopistaMap mapEIEL = null;

    private EIELListaMapasDialog ListaMapasDialog = null;

    Frame owner = null;

    public static final FileFilter GEOPISTA_MAP_FILE_FILTER = GUIUtil.createFileFilter(
            aplicacion.getI18nString("EIELLoadMapPlugIn.EIELMapFiles"),
            new String[] { "gpc" });

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        return null;
    }

    public String getName()
    {
        return "EIEL Load Map";
    }

    public void initialize(PlugInContext context) throws Exception
    {
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addMainMenuItem(this, GeopistaFunctionUtils.i18n_getname("File"),
        		GeopistaFunctionUtils.i18n_getname(this.getName()) + "...", null,
                EIELLoadMapPlugIn.createEnableCheck(context.getWorkbenchContext()));

        if (context.getWorkbenchGuiComponent() instanceof Frame)
        {
            if (owner == null)
                owner = (Frame) context.getWorkbenchGuiComponent();
        } else
        {
            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
                    (Component) context.getWorkbenchContext().getIWorkbench().getGuiComponent());
        }
        ListaMapasDialog = new EIELListaMapasDialog(owner);
    }

    
    public boolean execute(PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);

        ListaMapasDialog = new EIELListaMapasDialog(aplicacion.getMainFrame());
        // pedimos la ruta de mapa para cargarlo
        ListaMapasDialog.getMap(context);

        
        mapEIEL = ListaMapasDialog.getMapEIEL();

        ListaMapasDialog.dispose();
        if (mapEIEL == null)
            return false;

        return true;
    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {

        String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
        File file = null;
        ITask generalTask = null;
        try
        {

            if (!mapEIEL.isSystemMap())
            {
                file = new File(mapEIEL.getBasePath(),"geopistamap.gpc");
                
                GeopistaMap sourceTask = GeopistaMap.getMapISO88591(file.getAbsolutePath());
                
                monitor.report(aplicacion
                        .getI18nString("EIELLoadMapPlugIn.CargandoMapa")
                        + " " + mapEIEL.getName());
                
                HashMap orderLayers = (HashMap) sourceTask.getOrdersLayer();
                String mapSystemId = sourceTask.getSystemId();
                JInternalFrame[] loaderMaps = context.getWorkbenchGuiComponent()
                        .getInternalFrames();

                if (loaderMaps != null)
                {
                    for (int i = 0; i < loaderMaps.length; i++)
                    {
                        if (!(loaderMaps[i] instanceof GeopistaTaskFrame)) continue;
                        String actualSystemId = ((GeopistaMap) (((GeopistaTaskFrame) loaderMaps[i])
                                .getTaskFrame()).getTask()).getSystemId();
                        if (mapSystemId.equals(actualSystemId))
                        {
                            (((GeopistaTaskFrame) loaderMaps[i]).getTaskFrame())
                                    .moveToFront();
                            return;
                        }
                    }
                }

                GeopistaMap newTask = new GeopistaMap(context.getWorkbenchContext());

                WorkbenchGuiComponent workbenchFrame = context.getWorkbenchGuiComponent();

                GeopistaLayerManager newLayerManager = null;

                if (workbenchFrame instanceof GeopistaWorkbenchFrame)
                {
                    newTask.setName(sourceTask.getName());
                    newTask.setExtracted(sourceTask.isExtracted());
                    newTask.setDescription(sourceTask.getDescription());
                    newTask.setMapUnits(sourceTask.getMapUnits());
                    newTask.setMapScale(sourceTask.getMapScale());
                    newTask.setMapCoordinateSystem( 
                    		sourceTask.getMapCoordinateSystem()
                    		);
                    newTask.setProjectFile(file);
                    if(sourceTask.getSystemId()!=null&&!sourceTask.getSystemId().trim().equals(""))
                    {
                        newTask.setSystemId(sourceTask.getSystemId());
                    }
                    else
                    {
                        //ponemos una l para saber si el mapa el local o de base de datos
                        newTask.setSystemId("l" + String.valueOf(System.currentTimeMillis()));
                    }
                    
                    workbenchFrame.addTaskFrame(newTask);
                    newLayerManager = (GeopistaLayerManager) newTask.getLayerManager();
                    generalTask = newTask;
                } else
                {
                    ((GeopistaEditor) context.getWorkbenchGuiComponent()).reset();
                    context.getTask().setName(sourceTask.getName());
                    context.getTask().setProjectFile(file);
                    newLayerManager = (GeopistaLayerManager) context.getLayerManager();
                    generalTask = context.getTask();
                }

                ILayerManager sourceLayerManager = sourceTask.getLayerManager();
                for (Iterator i = sourceLayerManager.getCategories().iterator(); i
                        .hasNext();)
                {
                    Category sourceLayerCategory = (Category) i.next();

                    CoordinateSystemRegistry registry = CoordinateSystemRegistry
                            .instance(context.getWorkbenchGuiComponent().getContext()
                                    .getBlackboard());

                    newLayerManager.addCategory(sourceLayerCategory.getName());
                    ((LayerFamily) newLayerManager.getCategory(sourceLayerCategory
                            .getName())).setSystemId(((LayerFamily) sourceLayerCategory)
                            .getSystemId());

                    ArrayList layerables = new ArrayList(sourceLayerCategory
                            .getLayerables());
                    Collections.reverse(layerables);

                    for (Iterator j = layerables.iterator(); j.hasNext();)
                    {
                        Layerable layerable = (Layerable) j.next();
                        layerable.setLayerManager(newLayerManager);
                        monitor.report(aplicacion
                                .getI18nString("EIELLoadMapPlugIn.Cargando")
                                + " " + layerable.getName());
                        if (layerable instanceof Layer)
                        {
                            Layer layer = (Layer) layerable;
                            
                            
                            
                            
                            layer
                                    .setFeatureCollection(executeQuery(layer
                                            .getDataSourceQuery().getQuery(), layer
                                            .getDataSourceQuery().getDataSource(),
                                            registry, null));
                            
                            if (layer instanceof GeopistaLayer)
                            {
                                FeatureSchema schema = layer.getFeatureCollectionWrapper().getFeatureSchema();
                                if (schema instanceof GeopistaSchema)
                                {
                                    ((GeopistaSchema)schema).setGeopistalayer((GeopistaLayer)layer);
                                    
                                }
                            }
                            
                            layer.setFeatureCollectionModified(false);
                            if (layer instanceof GeopistaLayer)
                            {
                                
                                ((GeopistaLayer) layer).setLocal(true);
                                {
                                    try
                                    {
                                        ((GeopistaLayer) layer).activateLogger((GeopistaMap)generalTask);
                                    }catch(Exception e)
                                    {
                                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("EIELLoadMapPlugIn.errorCargandoLog") + " " + layer.getName() + " "+ aplicacion.getI18nString("EIELLoadMapPlugIn.perdidosDatos"));
                                    }
                                }
                            }

                        }
                        Integer layerPosition = null;
                        if (layerable instanceof GeopistaLayer)
                        	layerPosition = (Integer) orderLayers.get(((GeopistaLayer) layerable).getSystemId());
                        else
                        	layerPosition = (Integer) orderLayers.get(layerable.getName());
                        if(layerPosition!=null)
                        {
	                        newLayerManager.addLayerable(sourceLayerCategory.getName(),
	                                layerable,layerPosition.intValue());
                        }
                        else
                        {
                            newLayerManager.addLayerable(sourceLayerCategory.getName(),
	                                layerable);
                        }
                    }
                }
                updateTitleToNoModified(newLayerManager, generalTask.getTaskComponent());

            } else
            {
                try
                {
                    if (!aplicacion.isLogged())
                    {

                        aplicacion.setProfile("Geopista");
                        aplicacion.login();
                    }

                    if (aplicacion.isLogged())
                    {
                        monitor.report(aplicacion
                                .getI18nString("EIELLoadMapPlugIn.CargandoMapa")
                                + " " + mapEIEL.getName());
                        AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                                sUrlPrefix + "AdministradorCartografiaServlet");

                        GeopistaMap sourceTask = administradorCartografiaClient
                                .loadMap(mapEIEL,
                                        UserPreferenceStore.getUserPreference(
                                                UserPreferenceConstants.DEFAULT_LOCALE_KEY,
                                                "es_ES", true), null, FilterLeaf.equal(
                                                "1", new Integer(1)));

                        String mapSystemId = sourceTask.getSystemId();
                        JInternalFrame[] loaderMaps = context.getWorkbenchGuiComponent()
                                .getInternalFrames();

                        if (loaderMaps != null)
                        {
                            for (int i = 0; i < loaderMaps.length; i++)
                            {
                                if (loaderMaps[i] instanceof GeopistaTaskFrame)
                                {
                                    String actualSystemId = ((GeopistaMap) (((GeopistaTaskFrame) loaderMaps[i])
                                            .getTaskFrame()).getTask()).getSystemId();
                                    if (mapSystemId.equals(actualSystemId))
                                    {
                                        (((GeopistaTaskFrame) loaderMaps[i]).getTaskFrame())
                                        .moveToFront();
                                        return;
                                    }
                                }
                            }
                        }

                        GeopistaMap newTask = new GeopistaMap(context
                                .getWorkbenchContext());

                        ILayerManager newLayerManager = null;

                        WorkbenchGuiComponent workbenchFrame = context
                                .getWorkbenchGuiComponent();

                        if (workbenchFrame instanceof GeopistaWorkbenchFrame)
                        {
                            newTask.setName(sourceTask.getName());
                            newTask.setExtracted(sourceTask.isExtracted());
                            //Al fijar la descripción, también fijo la proyección
                            newTask.setDescription(sourceTask.getDescription());
                            newTask.setMapProjection(sourceTask.getMapProjection());
                            newTask.setMapUnits(sourceTask.getMapUnits());
                            newTask.setMapScale(sourceTask.getMapScale());
                            newTask.setMapCoordinateSystem( 
                            		CoordinateSystemRegistry.instance(context.getWorkbenchContext().getBlackboard()).
                            		get(sourceTask.getMapProjection())
                            		);
                            newTask.setSystemId(sourceTask.getSystemId());
                            newTask.setSystemMap(true);
                            
                            workbenchFrame.addTaskFrame(newTask);
                            newLayerManager = newTask.getLayerManager();
                            generalTask = newTask;

                        } else
                        {
                            ((GeopistaEditor) context.getWorkbenchGuiComponent()).reset();
                            context.getTask().setName(sourceTask.getName());
                            context.getTask().setProjectFile(file);
                            newLayerManager = context.getLayerManager();
                            generalTask = context.getTask();
                        }
                        int categoryLayerCount = 0; 
                        ILayerManager sourceLayerManager = sourceTask.getLayerManager();
                        // We get the list of categories in an Array and we need to read it from end to beginning to keep the order
                        List lstCategories = sourceLayerManager.getCategories();
                        
                        ArrayList lstLayers = new ArrayList(sourceLayerManager.getOrderLayers());
                        
                        
                        for (int index=(lstCategories.size()-1); index>=0; index--)
                        {
                        	Category sourceLayerCategory = (Category) lstCategories.get(index);
                        	
                        	
                            CoordinateSystemRegistry registry = CoordinateSystemRegistry
                                    .instance(context.getWorkbenchGuiComponent().getContext()
                                            .getBlackboard());
                            
                            newLayerManager.addCategoryInList(sourceLayerCategory.getName());
                            
                            // obtenemos la LayerFamily y le ponemos el atributo de categoria de sistema a true
                            // siempre y cuando no sea un LayerFamily ficticio para las WMSLayers.
                            if(((LayerFamily)sourceLayerCategory).isSystemLayerFamily()){
                            	LayerFamily newLayerFamily = (LayerFamily) newLayerManager.getCategory(sourceLayerCategory.getName());
                                newLayerFamily.setSystemLayerFamily(true);
                                newLayerFamily.setSystemId(((LayerFamily) sourceLayerCategory).getSystemId());
                            }
                            ArrayList layerables = 
                             	new ArrayList(sourceLayerCategory.getLayerables());
                            
                            categoryLayerCount++; 
                            int layerablesCount = 0; 
                            for (Iterator j = layerables.iterator(); j.hasNext();)
                            {
                                Layerable layerable = (Layerable) j.next();
                                if (layerable!=null){
	                                layerable.setLayerManager(newLayerManager);
	                                monitor.report(aplicacion.getI18nString("EIELLoadMapPlugIn.Cargando") + " " + layerable.getName());
	                                //Adaptacion de Geopista para WMS --->
	                                 
	                                if(layerable instanceof WMSLayer){
	                                	newLayerManager.addLayerablePanel(sourceLayerCategory.getName(), layerable, 0);
	                                }
	                                // <--- Adaptacion de Geopista para WMS 
	                                else if (!((GeopistaLayer) layerable).getSystemId() .equalsIgnoreCase("error"))
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
	                                            properties.put("mapadestino",
	                                                    (GeopistaMap) context.getTask());
	                                        }
	                                        // Introducimos el fitro geometrico si
	                                        // es distinto de null, si se introduce
	                                        // null falla
	                                        // Introducimos el FilterNode
	                                        properties.put("nodofiltro", FilterLeaf.equal(
	                                                "1", new Integer(1)));
	                                        serverDataSource.setProperties(properties);
	
	                                        URL urlLayer = new URL("geopistalayer://default/"
	                                                + ((GeopistaLayer) layer).getSystemId());
	
	                                        layer.setFeatureCollectionModified(false);
	                                        DataSourceQuery dataSourceQuery = new DataSourceQuery();
	                                        dataSourceQuery.setQuery(urlLayer.toString());
	                                        dataSourceQuery.setDataSource(serverDataSource);
	                                        layer.setDataSourceQuery(dataSourceQuery);
	                                        
	                                        
	                                        
	                                        if (layerable instanceof GeopistaLayer)
	                                        {       
	                                            if (workbenchFrame instanceof GeopistaWorkbenchFrame)
	                                            {
	                                                ((GeopistaLayer) layerable)
	                                                    .activateLogger((GeopistaMap) newTask);
	                                            } else
	                                            {
	                                                ((GeopistaLayer) layerable)
	                                                    .activateLogger((GeopistaMap) context.getTask());
	                                            }
	                                           
	                                        }
	                                        
	                                        
	
	                                    }
	                                  newLayerManager.addLayerablePanel(sourceLayerCategory
	                                            .getName(), layerable, layerablesCount);
	                                  layerablesCount++;
	                                  
	                                } else
	                                {
	                                    JOptionPane
	                                            .showMessageDialog(
	                                                    (Component) context
	                                                            .getWorkbenchGuiComponent(),
	                                                    aplicacion
	                                                            .getI18nString("EIELLoadMapPlugIn.CapaErronea"));
	                                }
	                            }
	                            updateTitleToNoModified(newLayerManager, generalTask
	                                    .getTaskComponent());
                            }
                            
                        }

                    }

                } catch (Exception e)
                {
				logger.error("run(TaskMonitor, PlugInContext)", e);
                    Throwable errorCause = e.getCause();
                    if(errorCause instanceof PermissionException)
                    {
                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("EIELLoadMapPlugIn.NoPermisosEditarMapa") + " " + mapEIEL.getName());
                    }
                    else
                    {
                        if(errorCause!=null)
                        {
	                        Throwable subErrorCause = errorCause.getCause();
	                        if(subErrorCause instanceof ObjectNotFoundException)
	                        {
	 
	                            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),subErrorCause.getMessage() + " " + aplicacion.getI18nString("EIELLoadMapPlugIn.DarAltaMunicipio"));
	                        }
	                        else
	                        {
	                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("EIELLoadMapPlugIn.ProblemasCargarMapa") + " " + mapEIEL.getName(), aplicacion.getI18nString("EIELLoadMapPlugIn.ProblemasCargarMapa") + " " + mapEIEL.getName(), StringUtil
	                                .stackTrace(e));
	                        }
                        }
                        else
                        {
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("EIELLoadMapPlugIn.ProblemasCargarMapa") + " " + mapEIEL.getName(), aplicacion.getI18nString("EIELLoadMapPlugIn.ProblemasCargarMapa") + " " + mapEIEL.getName(), StringUtil
                                    .stackTrace(e));
                        }
                    }
					logger.error("run(TaskMonitor, PlugInContext)", e);
                }

            }

        } catch (Exception e)
        {
			logger.error("run(TaskMonitor, PlugInContext)", e);
            throw e;
        }


    }

    private void updateTitleToNoModified(ILayerManager layerManager,
            TaskComponent taskFrame)
    {

        if (layerManager instanceof GeopistaLayerManager)
        {
            ((GeopistaLayerManager) layerManager).setDirty(false);
            if (taskFrame instanceof JInternalFrame)
            {
                String newTitle = taskFrame.getTitle();
                if (newTitle.charAt(0) == '*')
                {
                    newTitle = newTitle.substring(1);
                }
                ((JInternalFrame) taskFrame).setTitle(newTitle);
            }
        }
    }

    private FeatureCollection executeQuery(String query, DataSource dataSource,
            CoordinateSystemRegistry registry, TaskMonitor monitor) throws Exception
    {
        Connection connection = dataSource.getConnection();
        try
        {
            return dataSource.installCoordinateSystem(connection.executeQuery(query,
                    monitor), registry);
        } finally
        {
            connection.close();
        }
    }

}
