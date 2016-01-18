/*
 * Created on 06-sep-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.plugin.synchronizeautocad;

import java.awt.Component;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SearchableFeatureCollection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.ui.LockManager;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.LogEvent;
import com.geopista.ui.plugin.LogFeatutesEvents;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class SynchronizeAutoCADPlugIn extends ThreadedBasePlugIn
{

    /**
     * Logger for this class
     */

    public static final String DELETE_FEATURES = "Delete Features";

    public static final String ADDED_FEATURES = "Added Features";

    public static final String MODIFIED_FEATURES = "Modified Features";

    public static final String MODIFIED_CLONES_FEATURES = "Modified Clones Features";

    private static final int CANCELAR = 1;

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    public static final String ORIGINAL_FILE_KEY = "Original File";
    
    public SynchronizeAutoCADPlugIn(){

    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.synchronizeautocad.languages.SynchronizeAutoCADPlugIni18n",loc,this.getClass().getClassLoader());    	
    	I18N.plugInsResourceBundle.put("SynchronizeAutoCADPlugIn",bundle2);
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
                workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
                checkFactory.createWindowWithNoSystemMapMustBeActiveCheck()).add(
                checkFactory.createWindowWithAutoCADMapMustBeActiveCheck());
    }

    public String getName()
    {
        return I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD");
    }

    public void initialize(PlugInContext context) throws Exception
    {

        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addMainMenuItem(this, GeopistaUtil.i18n_getname("File"),
                this.getName() + "...", null,
                SynchronizeAutoCADPlugIn.createEnableCheck(context.getWorkbenchContext()));

    }

    public boolean execute(PlugInContext context) throws Exception
    {
    	if(!aplicacion.isLogged())
        {

             aplicacion.setProfile("Geopista");
             aplicacion.login();
        }

        if(aplicacion.isLogged())
        {
        	return true;
        }
        else{
        	return false;
        }

    }
 
    public void run(final TaskMonitor monitor, PlugInContext context) throws Exception
    { 
    	
    	monitor.report(I18N.get("SynchronizeAutoCADPlugIn",
		"SynchronizeAutoCAD.SynchronizingMap"));
    	
    	final String GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA = "geopista.conexion.administradorcartografia";

		String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
		AdministradorCartografiaClient admCarClient = new AdministradorCartografiaClient(sUrlPrefix + "/AdministradorCartografiaServlet");
        
    	Object[] possibleValues = {
    			I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.Continue"),
    			I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.Cancel") };

    	final LockManager lockManager = (LockManager) context.getActiveTaskComponent()
    	.getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);

    	if (((GeopistaTaskFrame) context.getActiveInternalFrame()).getTaskFrame()
    			.getTask() instanceof GeopistaMap)
    	{
    		final Hashtable layerToSynchronize = new Hashtable();

    		GeopistaMap geopistaMapSource = ((GeopistaMap) ((GeopistaTaskFrame) context
    				.getActiveInternalFrame()).getTaskFrame().getTask());

    		
    		
    		List sourceLayers = geopistaMapSource.getLayerManager().getLayers();
    		Iterator sourceLayersIterator = sourceLayers.iterator();
    		while (sourceLayersIterator.hasNext())
    		{
    			Object currenLayer = sourceLayersIterator.next();
    			Hashtable featuresByLayer = new Hashtable();

    			if (currenLayer instanceof GeopistaLayer)
    			{

    				try{
    					    					
    					GeopistaLayer currentGeopistaLayer = (GeopistaLayer) currenLayer;

    					GeopistaLayer systemLayer= admCarClient.loadLayer(currentGeopistaLayer.getSystemId(), 
    							"es_ES", null, FilterLeaf.equal("1", new Integer(1)));
    					
    					GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
    					Map properties = new HashMap();
    					properties.put("nodofiltro",FilterLeaf.equal("1",new Integer(1)));
    					serverDataSource.setProperties(properties);
    					
    					URL urlLayer = new URL(((AppContext) aplicacion).getString(GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA));
    					
    					DataSourceQuery dataSourceQuery = new DataSourceQuery();
    					dataSourceQuery.setQuery(urlLayer.toString());
    					dataSourceQuery.setDataSource(serverDataSource);
    					systemLayer.setDataSourceQuery(dataSourceQuery);

    					if (systemLayer == null)
    						continue;

    					GeopistaLayerManager layerManager = new GeopistaLayerManager();
    					layerManager.addLayerListener((LayerListener)context.getWorkbenchContext().getWorkbench());
    					//ASD
    					layerManager.setCoordinateSystem(geopistaMapSource.getLayerManager().getCoordinateSystem());
    					layerManager.addLayer(GeopistaUtil.chooseCategory(context), systemLayer);
    					systemLayer.setLayerManager(layerManager);    			
    					
    					LogFeatutesEvents currentLog = currentGeopistaLayer.getLogger();

    					SearchableFeatureCollection searchableSourceFeatureCollection = new SearchableFeatureCollection(
    							currentGeopistaLayer.getFeatureCollectionWrapper()
    							.getWrappee());
    					SearchableFeatureCollection searchableTargetFeatureCollection = new SearchableFeatureCollection(
    							systemLayer.getFeatureCollectionWrapper().getWrappee());

    					Collection tempCollection = currentLog.getNewFeatures();
    					Iterator addFeaturesIterator = tempCollection.iterator();
    					ArrayList addFeatures = new ArrayList();

    					int addedErrors = 0;
    					int geometryIndex = systemLayer.getFeatureCollectionWrapper().getFeatureSchema().getGeometryIndex();
    					int geometryType = ((GeopistaSchema)systemLayer.getFeatureCollectionWrapper().getUltimateWrappee().getFeatureSchema()).getColumnByAttribute(
    							geometryIndex).getTable().getGeometryType();
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

    						currentFeature.setLayer(systemLayer);

    						((GeopistaSchema) currentFeature.getSchema()).getColumnByAttribute(geometryIndex).getTable().setGeometryType(geometryType);

    						currentFeature.setGeometry(currentFeature.getGeometry());                   
    						addFeatures.add(currentFeature);

    					}

    					if (addedErrors > 0)
    					{
    						String tempStringBuffer = MessageFormat
    						.format(
    								I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.NumberWrongFeaturesInsert"),
    								new Object[] { String.valueOf(addedErrors),
    									new Integer(addedErrors),
    									systemLayer.getName() });

    						int selectedValue = JOptionPane.showOptionDialog(aplicacion
    								.getMainFrame(), tempStringBuffer, 
    								I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.Problems"), 0,
    								JOptionPane.QUESTION_MESSAGE, null, possibleValues,
    								possibleValues[0]);
    						if (selectedValue == CANCELAR)
    							return;
    					}
    					featuresByLayer.put(SynchronizeAutoCADPlugIn.ADDED_FEATURES, addFeatures);

    					tempCollection = currentLog.getDeletedFeatures();
    					Iterator deleteFeaturesIterator = tempCollection.iterator();
    					ArrayList deleteFeatures = new ArrayList();

    					int deleteErrors = 0;
    					while (deleteFeaturesIterator.hasNext())
    					{
    						LogEvent currentEvent = (LogEvent) deleteFeaturesIterator.next();
    						String currentFeatureID = currentEvent.getFeatureId();
    						GeopistaFeature currentFeature = (GeopistaFeature) searchableTargetFeatureCollection
    						.query(currentFeatureID);
    						if (currentFeature == null)
    						{
    							deleteErrors++;
    							continue;
    						}
    						currentFeature.setLayer(systemLayer);
    						deleteFeatures.add(currentFeature);

    					}

    					if (deleteErrors > 0)
    					{
    						String tempStringBuffer = MessageFormat
    						.format(
    								I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.NumberWrongFeaturesDelete"),
    								new Object[] { String.valueOf(deleteErrors),
    									new Integer(deleteErrors),
    									systemLayer.getName() });

    						int selectedValue = JOptionPane.showOptionDialog(aplicacion
    								.getMainFrame(), tempStringBuffer, 
    								I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.Problems"), 0,
    								JOptionPane.QUESTION_MESSAGE, null, possibleValues,
    								possibleValues[0]);
    						if (selectedValue == CANCELAR)
    							return;
    					}
    					// las features borradas en la capa con ella misma como
    					// clave
    					featuresByLayer
    					.put(SynchronizeAutoCADPlugIn.DELETE_FEATURES, deleteFeatures);

    					tempCollection = currentLog.getModifiedFeatures();
    					Hashtable modifedFeaturesHashtable = new Hashtable();
    					Iterator modifiedFeaturesIterator = tempCollection.iterator();

    					int modifiedErrors = 0;
    					while (modifiedFeaturesIterator.hasNext())
    					{
    						LogEvent currentEvent = (LogEvent) modifiedFeaturesIterator
    						.next();
    						String currentFeatureID = currentEvent.getFeatureId();
    						Feature currentSourceFeature = searchableSourceFeatureCollection
    						.query(currentFeatureID);
    						Feature currentTargetFeature = searchableTargetFeatureCollection
    						.query(currentFeatureID);
    						if (currentSourceFeature == null || currentTargetFeature == null)
    						{
    							modifiedErrors++;
    							continue;
    						}
    						modifedFeaturesHashtable.put(currentSourceFeature,
    								currentTargetFeature);
    					}

    					if (modifiedErrors > 0)
    					{
    						String tempStringBuffer = MessageFormat
    						.format(I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.NumberWrongFeaturesUpdate"),
    								new Object[] { String.valueOf(modifiedErrors),
    							new Integer(modifiedErrors),
    							systemLayer.getName() });

    						int selectedValue = JOptionPane.showOptionDialog(aplicacion
    								.getMainFrame(), tempStringBuffer, 
    								I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.Problems"), 0,
    								JOptionPane.QUESTION_MESSAGE, null, possibleValues,
    								possibleValues[0]);
    						if (selectedValue == CANCELAR)
    							return;
    					}

    					featuresByLayer.put(SynchronizeAutoCADPlugIn.MODIFIED_FEATURES,
    							modifedFeaturesHashtable);

    					layerToSynchronize.put(systemLayer, featuresByLayer);

    				}catch(Exception e)
    				{
    					JOptionPane.showMessageDialog((Component) context.getWorkbenchGuiComponent(),
    							I18N.get("SynchronizeAutoCADPlugIn", "SynchronizeAutoCAD.ErroneousLayer"));
    					continue;
    				}

    			}
    		}

    		context.getLayerViewPanel().getLayerManager().getUndoableEditReceiver().startReceiving();

    		execute(new UndoableCommand(getName())
    		{

    			TaskMonitor localMonitor = monitor;

    			Hashtable localLayerToSynchronizeHashTable = new Hashtable(
    					layerToSynchronize);

    			public void execute()
    			{
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
    					.get(SynchronizeAutoCADPlugIn.DELETE_FEATURES);
    					
    					ArrayList localAddedFeatures = (ArrayList) currentLayerFeaturesToSynchronize
    					.get(SynchronizeAutoCADPlugIn.ADDED_FEATURES);
    					Hashtable localModifedFeaturesHashtable = (Hashtable) currentLayerFeaturesToSynchronize
    					.get(SynchronizeAutoCADPlugIn.MODIFIED_FEATURES);
    					Hashtable clonesModifiedFeatures = new Hashtable();
    					exec(localMonitor, lockManager,
    							localTargetLayer, localDeleteFeatures,
    							localAddedFeatures, localModifedFeaturesHashtable,
    							clonesModifiedFeatures);
    					currentLayerFeaturesToSynchronize.put(
    							SynchronizeAutoCADPlugIn.MODIFIED_CLONES_FEATURES,
    							clonesModifiedFeatures);
    				}

    			}

    			public void unexecute()
    			{
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
    					.get(SynchronizeAutoCADPlugIn.DELETE_FEATURES);
    					ArrayList localAddedFeatures = (ArrayList) currentLayerFeaturesToSynchronize
    					.get(SynchronizeAutoCADPlugIn.ADDED_FEATURES);
    					Hashtable localModifedFeaturesHashtable = (Hashtable) currentLayerFeaturesToSynchronize
    					.get(SynchronizeAutoCADPlugIn.MODIFIED_FEATURES);
    					Hashtable clonesModifiedFeatures = (Hashtable) currentLayerFeaturesToSynchronize
    					.get(SynchronizeAutoCADPlugIn.MODIFIED_CLONES_FEATURES);
    					unexec(localMonitor, lockManager,
    							localTargetLayer, localDeleteFeatures,
    							localAddedFeatures, localModifedFeaturesHashtable,
    							clonesModifiedFeatures);
    				}
    			}
    		}, (LayerManagerProxy) context.getLayerViewPanel());

    		context.getLayerViewPanel().getLayerManager().getUndoableEditReceiver().stopReceiving();

    		reallocateMap(geopistaMapSource);    		
    		
            try
            {
                context.getActiveInternalFrame().setClosed(true);
            } catch (PropertyVetoException e)
            {
                e.printStackTrace();
            }
    	}    	
    	
    }

    /**
     * @param monitor
     * @param geopistaMapTarget
     * @param lockManager
     * @param targetLayer
     */
    private void unexec(final TaskMonitor monitor, final LockManager lockManager, final GeopistaLayer targetLayer,
            ArrayList localDeleteFeatures, ArrayList localAddedFeatures,
            Hashtable localModifedFeaturesHashtable, Hashtable clonesModifiedFeatures)
    {
        try
        {
            lockManager.lockSelectedFeatures(localAddedFeatures, monitor);
            lockManager.lockSelectedFeatures(new ArrayList(clonesModifiedFeatures
                    .values()), monitor);

            monitor.report(I18N.get("SynchronizeAutoCADPlugIn",
            		"SynchronizeAutoCAD.UndoSynchronizeLayer") + " "
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
                lockManager.unlockAllLockedFeatures((LayerManager)targetLayer.getLayerManager(),
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
    private void exec(final TaskMonitor monitor, final LockManager lockManager, 
    		final GeopistaLayer targetLayer, ArrayList localDeleteFeatures, 
    		ArrayList localAddedFeatures, Hashtable localModifedFeaturesHashtable, 
    		Hashtable clonesModifiedFeatures)
    {
        try
        {

            lockManager.lockSelectedFeatures(localDeleteFeatures, monitor);
            lockManager.lockSelectedFeatures(new ArrayList(localModifedFeaturesHashtable
                    .values()), monitor);

            monitor.report(I18N.get("SynchronizeAutoCADPlugIn", 
            		"SynchronizeAutoCAD.SynchronizingLayer") + " "
                    + targetLayer.getName());
            targetLayer.getFeatureCollectionWrapper().addAll(localAddedFeatures);

            targetLayer.getFeatureCollectionWrapper().removeAll(localDeleteFeatures);

            Set modifiedSet = localModifedFeaturesHashtable.entrySet();
            Iterator modifiedSetIterator = modifiedSet.iterator();
            while (modifiedSetIterator.hasNext())
            {
                Entry currentModifiedEntry = (Entry) modifiedSetIterator.next();
                Feature sourceFeature = (Feature) currentModifiedEntry.getKey();
                Feature targetFeature = (Feature) currentModifiedEntry.getValue();

                clonesModifiedFeatures.put(targetFeature.clone(), targetFeature);
                
                
                int geometryIndex = targetFeature.getSchema().getGeometryIndex();
                int targetGeometryType = ((GeopistaSchema) targetFeature.getSchema()).getColumnByAttribute(
                        geometryIndex).getTable().getGeometryType();
                
                
                geometryIndex = sourceFeature.getSchema().getGeometryIndex();
                ((GeopistaSchema) sourceFeature.getSchema()).getColumnByAttribute(
                        geometryIndex).getTable().setGeometryType(targetGeometryType);
                sourceFeature.setGeometry(sourceFeature.getGeometry());
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
                lockManager.unlockAllLockedFeatures((LayerManager)targetLayer.getLayerManager(),
                        monitor);
            } catch (LockException e)
            {
                e.printStackTrace();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void reallocateMap(GeopistaMap selectMap)
    {   

    	final String dirBase = aplicacion.getUserPreference(
    			AppContext.PREFERENCES_DATA_PATH_KEY, AppContext.DEFAULT_DATA_PATH,
    			true) + "autocad"
    			+ ((String)System.getProperties().getProperty("file.separator"));

    	String nameMap = selectMap.getName().split(".dxf")[0]; 
    	try
    	{    	
    		File baseDirFile = new File(dirBase, "map.backups");
    		File dirBaseMake = new File(baseDirFile, nameMap);
    		if (dirBaseMake.exists()){
    			deleteDir(dirBaseMake);
    			dirBaseMake.mkdirs();
    		}    		
    		GeopistaUtil.makeMapBackup(dirBase, nameMap);
    	} 
    	catch (IOException e)
    	{                
    		e.printStackTrace();
    	}

    }  
    
//  Borra un directorio completo
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            
            //Borra todos los ficheros del directorio
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        //Ahora que el directorio está vacío, se puede borrar
        return dir.delete();
    }

}
