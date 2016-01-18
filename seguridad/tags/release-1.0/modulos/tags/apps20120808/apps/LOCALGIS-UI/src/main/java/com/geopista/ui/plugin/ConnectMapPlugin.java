/*
 * Created on 06-sep-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.plugin;

import java.awt.Component;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SearchableFeatureCollection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.model.LayerFamily;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.ObjectNotFoundException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.server.administradorCartografia.SystemMapException;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.ui.GeopistaWorkbenchFrame;
import com.geopista.ui.LockManager;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConnectMapPlugin extends ThreadedBasePlugIn
{
    
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(GeopistaLoadMapPlugIn.class);
    
    public static final String DELETE_FEATURES = "Delete Features";
    
    public static final String ADDED_FEATURES = "Added Features";
    
    public static final String MODIFIED_FEATURES = "Modified Features";
    
    public static final String MODIFIED_CLONES_FEATURES = "Modified Clones Features";
    
    private static final int CANCELAR = 1;
    
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();
    
    private Blackboard blackboard = aplicacion.getBlackboard();
    
    public static final String ORIGINAL_FILE_KEY = "Original File";
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
                workbenchContext);
        
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                        checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
                                checkFactory.createWindowWithExtractMapMustBeActiveCheck());
    }
    
    public String getName()
    {
        return "Connect Map";
    }
    
    public void initialize(PlugInContext context) throws Exception
    {
        
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());
        
        featureInstaller.addMainMenuItem(this, GeopistaUtil.i18n_getname("File"),
                GeopistaUtil.i18n_getname(this.getName()) + "...", null,
                ConnectMapPlugin.createEnableCheck(context.getWorkbenchContext()));
        
    }
    
    
    public boolean execute(PlugInContext context) throws Exception
    {
        
        return true;
        
    }
    
    public void run(final TaskMonitor monitor, PlugInContext context) throws Exception
    {
        Object[] possibleValues = {
                aplicacion.getI18nString("SynchronizePlugIn.Continuar"),
                aplicacion.getI18nString("SynchronizePlugIn.Cancelar") };
        
        final PlugInContext contexto = context;
        final LockManager lockManager = (LockManager) context.getActiveTaskComponent()
        .getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);
        
        if (((GeopistaTaskFrame) context.getActiveInternalFrame()).getTaskFrame()
                .getTask() instanceof GeopistaMap)
        {
            final GeopistaMap geopistaMapSource = ((GeopistaMap) ((GeopistaTaskFrame) context
                    .getActiveInternalFrame()).getTaskFrame().getTask());
            
            List sourceLayers = geopistaMapSource.getLayerManager().getLayers();
            
            //identificador del mapa con el que vamos sincronizar
            //String targetMapSystemId = geopistaMapSource.getSystemId().substring(1);
            
            final Hashtable layerToSynchronize = new Hashtable();
            
            Iterator sourceLayersIterator = sourceLayers.iterator();
            while (sourceLayersIterator.hasNext())
            {
                Object currenLayer = sourceLayersIterator.next();
                
                Hashtable featuresByLayer = new Hashtable();
                if (currenLayer instanceof GeopistaLayer)
                {
                    GeopistaLayer currentGeopistaLayer = (GeopistaLayer) currenLayer;
                    
                    
                    if (!currentGeopistaLayer.isExtracted())
                        continue;
                    
                    LogFeatutesEvents currentLog = currentGeopistaLayer.getLogger();
                    
                    SearchableFeatureCollection searchableSourceFeatureCollection = new SearchableFeatureCollection(
                            currentGeopistaLayer.getFeatureCollectionWrapper()
                            .getWrappee()); 
                    
                    
                    /*Collection tempCollection = currentLog.getNewFeatures();
                    Iterator addFeaturesIterator = tempCollection.iterator();
                    ArrayList addFeatures = new ArrayList();
                    
                    int addedErrors = 0;
                    
                    
                    while (addFeaturesIterator.hasNext())
                    {
                        LogEvent currentEvent = (LogEvent) addFeaturesIterator.next();
                        String currentFeatureID = currentEvent.getFeatureId();
                        GeopistaFeature currentFeature = (GeopistaFeature) searchableSourceFeatureCollection
                        .query(currentFeatureID);
                        if (currentFeature == null)
                        {
                            addedErrors++;
                            continue;
                        }
                        currentFeature.setNew(true);
                        currentFeature.setLayer(currentGeopistaLayer);
                        addFeatures.add(currentFeature);
                        
                    }
                    
                    if (addedErrors > 0)
                    {
                        String tempStringBuffer = MessageFormat
                        .format(
                                aplicacion
                                .getI18nString("SynchronizePlugIn.NumeroFeaturesErroneasInsertar"),
                                new Object[] { String.valueOf(addedErrors),
                                    new Integer(addedErrors),
                                    currentGeopistaLayer.getName() });
                        
                        int selectedValue = JOptionPane.showOptionDialog(aplicacion
                                .getMainFrame(), tempStringBuffer, aplicacion
                                .getI18nString("SynchronizePlugIn.Problemas"), 0,
                                JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                                possibleValues[0]);
                        if (selectedValue == CANCELAR)
                            return;
                    }
                    
                    */
                    
                    ArrayList addFeatures = new ArrayList();                    
                    int addedErrors = 0;
                    Iterator totalFeaturesIterator = searchableSourceFeatureCollection.iterator();
                    
                    while (totalFeaturesIterator.hasNext())
                    {
                        
                        GeopistaFeature currentFeature = (GeopistaFeature)totalFeaturesIterator.next();
                        
                        if (currentFeature.isTempID())
                            addFeatures.add(currentFeature);
                        
                    }
                    
                    
                    featuresByLayer.put(SynchronizePlugIn.ADDED_FEATURES, addFeatures);
                    
                    Collection tempCollection = currentLog.getDeletedFeatures();
                    Iterator deleteFeaturesIterator = tempCollection.iterator();
                    ArrayList deleteFeatures = new ArrayList();
                    
                    int deleteErrors = 0;
                    while (deleteFeaturesIterator.hasNext())
                    {
                        LogEvent currentEvent = (LogEvent) deleteFeaturesIterator.next();
                        String currentFeatureID = currentEvent.getFeatureId();
                        
                        if (!currentFeatureID.startsWith(GeopistaFeature.SYSTEM_ID_FEATURE_SIN_INICIALIZAR))
                        {
                            GeopistaFeature currentFeature = new GeopistaFeature();
                            GeopistaSchema esq = (GeopistaSchema)currentGeopistaLayer.getFeatureCollectionWrapper().getFeatureSchema();
                            Object atributos[] = new Object[esq.getAttributeCount()];
                            currentFeature.setAttributes(atributos);
                            currentFeature.setDeleted(true);
                            currentFeature.setNew(false);
                            currentFeature.setSystemId(currentFeatureID);
                            currentFeature.setLayer(currentGeopistaLayer);
                            currentFeature.setSchema(esq);
                            deleteFeatures.add(currentFeature);
                        }
                    }
                    
                    if (deleteErrors > 0)
                    {
                        String tempStringBuffer = MessageFormat
                        .format(
                                aplicacion
                                .getI18nString("SynchronizePlugIn.NumeroFeaturesErroneasBorrar"),
                                new Object[] { String.valueOf(deleteErrors),
                                    new Integer(deleteErrors),
                                    currentGeopistaLayer.getName() });
                        
                        int selectedValue = JOptionPane.showOptionDialog(aplicacion
                                .getMainFrame(), tempStringBuffer, aplicacion
                                .getI18nString("SynchronizePlugIn.Problemas"), 0,
                                JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                                possibleValues[0]);
                        if (selectedValue == CANCELAR)
                            return;
                    }
                    // las features borradas en la capa con ella misma como
                    // clave
                    featuresByLayer
                    .put(SynchronizePlugIn.DELETE_FEATURES, deleteFeatures);
                    
                    tempCollection = currentLog.getModifiedFeatures();
                    Collection modifedFeaturesCollection = new ArrayList();
                    Iterator modifiedFeaturesIterator = tempCollection.iterator();
                    
                    int modifiedErrors = 0;
                    while (modifiedFeaturesIterator.hasNext())
                    {
                        LogEvent currentEvent = (LogEvent) modifiedFeaturesIterator
                        .next();
                        String currentFeatureID = currentEvent.getFeatureId();
                        GeopistaFeature currentSourceFeature = (GeopistaFeature) searchableSourceFeatureCollection
                        .query(currentFeatureID);
                        
                        
                        if(currentSourceFeature != null)
                        {
                            //es posible que haya que deshabilitar los eventos
                            currentSourceFeature.setDirty(true);
                            currentSourceFeature.setLayer(currentGeopistaLayer);
                            currentSourceFeature.setNew(false);
                            
                            modifedFeaturesCollection.add(currentSourceFeature);                            
                        }
                        
                    }
                    
                    if (modifiedErrors > 0)
                    {
                        String tempStringBuffer = MessageFormat
                        .format(
                                aplicacion
                                .getI18nString("SynchronizePlugIn.NumeroFeaturesErroneasModificar"),
                                new Object[] { String.valueOf(modifiedErrors),
                                    new Integer(modifiedErrors),
                                    currentGeopistaLayer.getName() });
                        
                        int selectedValue = JOptionPane.showOptionDialog(aplicacion
                                .getMainFrame(), tempStringBuffer, aplicacion
                                .getI18nString("SynchronizePlugIn.Problemas"), 0,
                                JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                                possibleValues[0]);
                        if (selectedValue == CANCELAR)
                            return;
                    }
                    
                    featuresByLayer.put(SynchronizePlugIn.MODIFIED_FEATURES,
                            modifedFeaturesCollection);
                    
                    layerToSynchronize.put(currentGeopistaLayer, featuresByLayer);
                }
            }
            
            execute(new UndoableCommand(getName())
                    {
                
                TaskMonitor localMonitor = monitor;
                
                Hashtable localLayerToSynchronizeHashTable = new Hashtable(
                        layerToSynchronize);
                
                public void execute()
                {
                    
                    while(!aplicacion.isLogged())
                    {
                        aplicacion.login();                       
                    }                    
                    
                    Set localLayerToSynchronize = localLayerToSynchronizeHashTable
                    .keySet();
                    Iterator localLayerToSynchronizeIterator = localLayerToSynchronize
                    .iterator();
                    while (localLayerToSynchronizeIterator.hasNext())
                    {
                        GeopistaLayer localTargetLayer = (GeopistaLayer) localLayerToSynchronizeIterator
                        .next();
                        Hashtable currentLayerFeaturesToSynchronize = (Hashtable) localLayerToSynchronizeHashTable
                        .get(localTargetLayer);
                        ArrayList localDeleteFeatures = (ArrayList) currentLayerFeaturesToSynchronize
                        .get(SynchronizePlugIn.DELETE_FEATURES);
                        ArrayList localAddedFeatures = (ArrayList) currentLayerFeaturesToSynchronize
                        .get(SynchronizePlugIn.ADDED_FEATURES);
                        Collection localModifedFeaturesCollection = (Collection) currentLayerFeaturesToSynchronize
                        .get(SynchronizePlugIn.MODIFIED_FEATURES);
                        Collection clonesModifiedFeatures = new ArrayList();
                       
                        if (!exec(localMonitor,  lockManager,
                                localTargetLayer, localDeleteFeatures,
                                localAddedFeatures, localModifedFeaturesCollection,
                                clonesModifiedFeatures))
                            return;
                            
                        
                        currentLayerFeaturesToSynchronize.put(
                                SynchronizePlugIn.MODIFIED_CLONES_FEATURES,
                                clonesModifiedFeatures);
                        
                        //Se desbloquea la capa
                        try
                        {
                            String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
                            AdministradorCartografiaClient acClient = new AdministradorCartografiaClient(
                                    sUrlPrefix + "/AdministradorCartografiaServlet");
                            acClient.unlockLayer(localTargetLayer.getSystemId());
                        
                        } 
                        catch (Exception e1)
                        {
                            // Si falla algo en el desbloqueo seguimos con el resto de
                            // las capas bloqueadas
                            e1.printStackTrace();
                        }
                        
                    }     
                                       
                    
                    //Una vez finalizada la sincronización, se elimina la carpeta local
                    //y se guarda una copia en datos/map.backups
                    reallocateMap(geopistaMapSource);
                    
                    
                    
                    // Se cierra el mapa y se abre el de sistema
                    try
                    {
                        contexto.getActiveInternalFrame().setClosed(true);
                    } catch (PropertyVetoException e)
                    {
                        e.printStackTrace();
                    }
                    
                    geopistaMapSource.setSystemMap(true);
                    geopistaMapSource.setExtracted(false);
                    geopistaMapSource.setSystemId(geopistaMapSource.getSystemId().substring(1));
                    
                    abrirMapa(monitor, contexto, geopistaMapSource);                   
                    
                    
                }
                
                public void unexecute()
                {
                    
                }
                    }, context);            
            
            
        }
    }
    
    /**
     * @param monitor
     * @param geopistaMapTarget
     * @param lockManager
     * @param targetLayer
     */
    private void unexec(final TaskMonitor monitor, final GeopistaMap geopistaMapTarget,
            final LockManager lockManager, final GeopistaLayer targetLayer,
            ArrayList localDeleteFeatures, ArrayList localAddedFeatures,
            Hashtable localModifedFeaturesHashtable, Hashtable clonesModifiedFeatures)
    {
        try
        {
            lockManager.lockSelectedFeatures(localAddedFeatures, monitor);
            lockManager.lockSelectedFeatures(new ArrayList(clonesModifiedFeatures
                    .values()), monitor);
            
            monitor.report(aplicacion.getI18nString("dessincronizandoCapa") + " "
                    + targetLayer.getName());
            targetLayer.getFeatureCollectionWrapper().addAll(localDeleteFeatures);
            
            targetLayer.getFeatureCollectionWrapper().removeAll(localAddedFeatures);
            
            Set modifiedSet = clonesModifiedFeatures.entrySet();
            Iterator modifiedSetIterator = modifiedSet.iterator();
            while (modifiedSetIterator.hasNext())
            {
                Entry currentModifiedEntry = (Entry) modifiedSetIterator.next();
                Feature sourceFeature = (Feature) currentModifiedEntry.getKey();
                Feature targetFeature = (Feature) currentModifiedEntry.getValue();
                Object[] sourceAttributes = sourceFeature.getAttributes();
                targetFeature.setAttributes(sourceAttributes);
            }
            
        } catch (LockException e)
        {
            e.printStackTrace();
        } catch (ACException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                lockManager.unlockAllLockedFeatures((LayerManager)geopistaMapTarget.getLayerManager(),
                        monitor);
                
            } catch (LockException e)
            {
                
            } catch (Exception e)
            {
                
            }
        }
    }
    
    /**
     * @param monitor
     * @param geopistaMapTarget
     * @param lockManager
     * @param targetLayer
     * @param modifedFeaturesHashtable
     */
    private boolean exec(final TaskMonitor monitor, 
            final LockManager lockManager, final GeopistaLayer targetLayer,
            ArrayList localDeleteFeatures, ArrayList localAddedFeatures,
            Collection localModifedFeaturesCollection, Collection clonesModifiedFeatures)
    {
        try
        {
            
            //lockManager.lockSelectedFeatures(localDeleteFeatures, monitor);
            //lockManager.lockSelectedFeatures(new ArrayList(localModifedFeaturesHashtable
            //        .values()), monitor);
            
            monitor.report(aplicacion.getI18nString("sincronizandoCapa") + " "
                    + targetLayer.getName());
            
            
            String language = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", true);
            
            String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
            AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                    sUrlPrefix + "/AdministradorCartografiaServlet");
            administradorCartografiaClient.uploadFeatures(language,localAddedFeatures.toArray(),true,monitor);
            administradorCartografiaClient.uploadFeatures(language,localDeleteFeatures.toArray(),true,monitor);
            administradorCartografiaClient.uploadFeatures(language,localModifedFeaturesCollection.toArray(),true,monitor);
            
            return true;
            
        } catch (ACException e)
        {

            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
                    AppContext.getApplicationContext().getI18nString("ConnectMapPlugin.Error.Sincronizacion.Titulo"), 
                    AppContext.getApplicationContext().getI18nString("ConnectMapPlugin.Error.Sincronizacion.Mensaje"),
                    StringUtil.stackTrace(e));
            return false;
            
        } catch (Exception e)
        {
            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
                    AppContext.getApplicationContext().getI18nString("ConnectMapPlugin.Error.Sincronizacion.Titulo"), 
                    AppContext.getApplicationContext().getI18nString("ConnectMapPlugin.Error.Sincronizacion.Mensaje"),
                    StringUtil.stackTrace(e));
            return false;
        } finally
        {
            
//            try
//            {
//                //lockManager.unlockAllLockedFeatures(geopistaMapTarget.getLayerManager(),
//                //        monitor);
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
        }
    }
    
    
    private void reallocateMap(GeopistaMap selectMap)
    {   
        String dirBase = aplicacion.getUserPreference(
                AppContext.PREFERENCES_DATA_PATH_KEY, AppContext.DEFAULT_DATA_PATH,
                true);
        
        if (!selectMap.isSystemMap())
        {
            
            try
            {
                GeopistaUtil.makeMapBackup(dirBase, selectMap.getName()+"."+aplicacion.getIdMunicipio());
            } 
            catch (IOException e)
            {                
                e.printStackTrace();
            }
            
        } 
        else
        {
            String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
            AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                    sUrlPrefix + "/AdministradorCartografiaServlet");
            try
            {
                if (!aplicacion.isLogged())
                {
                    aplicacion.setProfile("Geopista");
                    aplicacion.login();
                }
                
                if (aplicacion.isLogged())
                {
                    administradorCartografiaClient.deleteMap(selectMap,Locale.getDefault()
                            .toString());
                    
                } else
                {
                    return;
                }
            } catch (Exception e1)
            {
                
                Throwable deleteError = e1.getCause();
                if (deleteError instanceof PermissionException)
                {
                    JOptionPane
                    .showMessageDialog(
                            aplicacion.getMainFrame(),
                            aplicacion
                            .getI18nString("GeopistaListaMapasPanel.NoPermisosBorrarMapa"));
                }
                if (deleteError instanceof SystemMapException)
                {
                    JOptionPane
                    .showMessageDialog(
                            aplicacion.getMainFrame(),
                            aplicacion
                            .getI18nString("GeopistaListaMapasPanel.MapaDeSistemaNoBorrar"));
                }
                if (deleteError instanceof SQLException)
                {
                    JOptionPane
                    .showMessageDialog(
                            aplicacion.getMainFrame(),
                            aplicacion
                            .getI18nString("GeopistaListaMapasPanel.MapaNoSePuedeBorrarPublicado"));
                }
                
                return;
            }
            
        }
        
    }  
    
    private void abrirMapa (TaskMonitor monitor, PlugInContext context, GeopistaMap mapGeopista )
    {
        String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
        File file = null;
        ITask generalTask = null;
        try
        {
            if (!mapGeopista.isSystemMap())
            {
                String dirBase = aplicacion.getUserPreference(
                        AppContext.PREFERENCES_DATA_PATH_KEY, AppContext.DEFAULT_DATA_PATH,
                        true);
                
                file = new File(dirBase+"/"+mapGeopista.getName()+"."+aplicacion.getIdMunicipio(),"geopistamap.gpc");
                GeopistaMap sourceTask = GeopistaMap.getMapUTF8(file.getAbsolutePath());
                monitor.report(aplicacion
                        .getI18nString("GeopistaLoadMapPlugIn.CargandoMapa")
                        + " " + mapGeopista.getName());
                
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
                    // I can't remember why I'm creating a new Task instead of
                    // using
                    // sourceTask. There must be a good reason. [Jon Aquino]
                    
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
                                .getI18nString("GeopistaLoadMapPlugIn.Cargando")
                                + " " + layerable.getName());
                        if (layerable instanceof Layer)
                        {
                            Layer layer = (Layer) layerable;
                            layer
                            .setFeatureCollection(executeQuery(layer
                                    .getDataSourceQuery().getQuery(), layer
                                    .getDataSourceQuery().getDataSource(),
                                    registry, null));
                            layer.setFeatureCollectionModified(false);
                            if (layer instanceof GeopistaLayer)
                            {
                                ((GeopistaLayer) layer).setLocal(true);
                                //if(((GeopistaLayer) layer).isExtracted())
                                {
                                    try
                                    {
                                        ((GeopistaLayer) layer).activateLogger((GeopistaMap)generalTask);
                                    }catch(Exception e)
                                    {
                                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("GeopistaLoadMapPlugIn.errorCargandoLog") + " " + layer.getName() + " "+ aplicacion.getI18nString("GeopistaLoadMapPlugIn.perdidosDatos"));
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
                                .getI18nString("GeopistaLoadMapPlugIn.CargandoMapa")
                                + " " + mapGeopista.getName());
                        AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                                sUrlPrefix + "/AdministradorCartografiaServlet");
                        
                        GeopistaMap sourceTask = administradorCartografiaClient
                        .loadMap(mapGeopista,
                                aplicacion.getUserPreference(
                                        AppContext.PREFERENCES_LOCALE_KEY,
                                        "es_ES", true), null, FilterLeaf.equal(
                                                "1", new Integer(1)),monitor);
                        
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
                        
                        GeopistaMap newTask = new GeopistaMap(context
                                .getWorkbenchContext());
                        
                        ILayerManager newLayerManager = null;
                        
                        WorkbenchGuiComponent workbenchFrame = context
                        .getWorkbenchGuiComponent();
                        
                        if (workbenchFrame instanceof GeopistaWorkbenchFrame)
                        {
                            // I can't remember why I'm creating a new Task
                            // instead of using
                            // sourceTask. There must be a good reason. [Jon
                            // Aquino]
                            
                            newTask.setName(sourceTask.getName());
                            newTask.setExtracted(sourceTask.isExtracted());
                            newTask.setDescription(sourceTask.getDescription());
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
                        
                        ILayerManager sourceLayerManager = sourceTask.getLayerManager();
                        for (Iterator i = sourceLayerManager.getCategories().iterator(); i
                        .hasNext();)
                        {
                            Category sourceLayerCategory = (Category) i.next();
                            
                            CoordinateSystemRegistry registry = CoordinateSystemRegistry
                            .instance(context.getWorkbenchGuiComponent().getContext()
                                    .getBlackboard());
                            
                            newLayerManager.addCategory(sourceLayerCategory.getName());
                            // obtenemos la LayerFamily y le ponemos el atributo
                            // de categoria de sistema a true
                            LayerFamily newLayerFamily = (LayerFamily) newLayerManager
                            .getCategory(sourceLayerCategory.getName());
                            newLayerFamily.setSystemLayerFamily(true);
                            newLayerFamily
                            .setSystemId(((LayerFamily) sourceLayerCategory)
                                    .getSystemId());
                            
                            ArrayList layerables = new ArrayList(sourceLayerCategory
                                    .getLayerables());
                            Collections.reverse(layerables);
                            
                            for (Iterator j = layerables.iterator(); j.hasNext();)
                            {
                                Layerable layerable = (Layerable) j.next();
                                layerable.setLayerManager(newLayerManager);
                                monitor.report(aplicacion
                                        .getI18nString("GeopistaLoadMapPlugIn.Cargando")
                                        + " " + layerable.getName());
                                if (!((GeopistaLayer) layerable).getSystemId()
                                        .equalsIgnoreCase("error"))
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
                                        // properties.put("filtrogeometrico",null);
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
                                    
                                    newLayerManager.addLayerable(sourceLayerCategory
                                            .getName(), layerable, sourceLayerManager
                                            .indexOf((Layer) layerable));
                                } else
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            (Component) context
                                            .getWorkbenchGuiComponent(),
                                            aplicacion
                                            .getI18nString("GeopistaLoadMapPlugIn.CapaErronea"));
                                }
                            }
                            updateTitleToNoModified(newLayerManager, generalTask
                                    .getTaskComponent());
                        }
                    }
                    
                } catch (Exception e)
                {
                    logger.error("run(TaskMonitor, PlugInContext)", e);
                    Throwable errorCause = e.getCause();
                    if(errorCause instanceof PermissionException)
                    {
                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("GeopistaLoadMapPlugIn.NoPermisosEditarMapa") + " " + mapGeopista.getName());
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
                                ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaLoadMapPlugIn.ProblemasCargarMapa") + " " + mapGeopista.getName(), aplicacion.getI18nString("GeopistaLoadMapPlugIn.ProblemasCargarMapa") + " " + mapGeopista.getName(), StringUtil
                                        .stackTrace(e));
                            }
                        }
                        else
                        {
                            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaLoadMapPlugIn.ProblemasCargarMapa") + " " + mapGeopista.getName(), aplicacion.getI18nString("GeopistaLoadMapPlugIn.ProblemasCargarMapa") + " " + mapGeopista.getName(), StringUtil
                                    .stackTrace(e));
                        }
                    }
                    logger.error("run(TaskMonitor, PlugInContext)", e);
                }
                
            }
            
        } catch (Exception e)
        {
            logger.error("run(TaskMonitor, PlugInContext)", e);
            //throw e;
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
    
    private void updateTitleToNoModified(ILayerManager newLayerManager,
            TaskComponent taskFrame)
    {
        
        if (newLayerManager instanceof GeopistaLayerManager)
        {
            ((GeopistaLayerManager) newLayerManager).setDirty(false);
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
    
    
    
    
}
