/**
 * SynchronizePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 06-sep-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.plugin;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SearchableFeatureCollection;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.ui.LockManager;
import com.geopista.ui.dialogs.SynchronizePanel01;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SynchronizePlugIn extends ThreadedBasePlugIn
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


    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
                workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
                checkFactory.createWindowWithNoSystemMapMustBeActiveCheck());
    }

    public String getName()
    {
        return "Synchronize Map";
    }

    public void initialize(PlugInContext context) throws Exception
    {

        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addMainMenuItem(this, GeopistaFunctionUtils.i18n_getname("File"),
        		GeopistaFunctionUtils.i18n_getname(this.getName()) + "...", null,
                SynchronizePlugIn.createEnableCheck(context.getWorkbenchContext()));

    }

    public ImageIcon getIcon()
    {
        return IconLoader.icon("Open.gif");
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context
                .getWorkbenchGuiComponent()), aplicacion
                .getI18nString("SynchronizeDialog"), context.getErrorHandler());
        d.init(new WizardPanel[] { new SynchronizePanel01("SynchronizePanel01", null,
                context) });

        // Set size after #init, because #init calls #pack. [Jon Aquino]
        d.setSize(550, 450);
        GUIUtil.centreOnWindow(d);
        d.setVisible(true);
        if (!d.wasFinishPressed())
        {
            return false;
        }
        return true;

    }

    public void run(final TaskMonitor monitor, PlugInContext context) throws Exception
    {
        Object[] possibleValues = {
                aplicacion.getI18nString("SynchronizePlugIn.Continuar"),
                aplicacion.getI18nString("SynchronizePlugIn.Cancelar") };
        final GeopistaMap geopistaMapTarget = (GeopistaMap) blackboard
                .get("SelectedSynchronizeGeopistaMap");
        final LockManager lockManager = (LockManager) context.getActiveTaskComponent()
                .getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);

        if (((GeopistaTaskFrame) context.getActiveInternalFrame()).getTaskFrame()
                .getTask() instanceof GeopistaMap)
        {
            GeopistaMap geopistaMapSource = ((GeopistaMap) ((GeopistaTaskFrame) context
                    .getActiveInternalFrame()).getTaskFrame().getTask());

            List sourceLayers = geopistaMapSource.getLayerManager().getLayers();

            final Hashtable layerToSynchronize = new Hashtable();

            Iterator sourceLayersIterator = sourceLayers.iterator();
            while (sourceLayersIterator.hasNext())
            {
                Object currenLayer = sourceLayersIterator.next();

                Hashtable featuresByLayer = new Hashtable();
                if (currenLayer instanceof GeopistaLayer)
                {
                    GeopistaLayer currentGeopistaLayer = (GeopistaLayer) currenLayer;

                    GeopistaLayer targetLayer = (GeopistaLayer) geopistaMapTarget
                            .getLayerManager().getLayer(
                                    currentGeopistaLayer.getSystemId().toLowerCase());
                    if (targetLayer == null)
                        continue;

                    LogFeatutesEvents currentLog = currentGeopistaLayer.getLogger();

                    SearchableFeatureCollection searchableSourceFeatureCollection = new SearchableFeatureCollection(
                            currentGeopistaLayer.getFeatureCollectionWrapper()
                                    .getWrappee());
                    SearchableFeatureCollection searchableTargetFeatureCollection = new SearchableFeatureCollection(
                            targetLayer.getFeatureCollectionWrapper().getWrappee());

                    Collection tempCollection = currentLog.getNewFeatures();
                    Iterator addFeaturesIterator = tempCollection.iterator();
                    ArrayList addFeatures = new ArrayList();

                    int addedErrors = 0;
                    int geometryIndex = targetLayer.getFeatureCollectionWrapper().getFeatureSchema().getGeometryIndex();
                    int geometryType = ((GeopistaSchema)targetLayer.getFeatureCollectionWrapper().getUltimateWrappee().getFeatureSchema()).getColumnByAttribute(
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
                        
                        
                        
                        currentFeature.setLayer(targetLayer);
                        
                        ((GeopistaSchema) currentFeature.getSchema()).getColumnByAttribute(geometryIndex).getTable().setGeometryType(geometryType);
                        
                        currentFeature.setGeometry(currentFeature.getGeometry());                   
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
                                                targetLayer.getName() });

                        int selectedValue = JOptionPane.showOptionDialog(aplicacion
                                .getMainFrame(), tempStringBuffer, aplicacion
                                .getI18nString("SynchronizePlugIn.Problemas"), 0,
                                JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                                possibleValues[0]);
                        if (selectedValue == CANCELAR)
                            return;
                    }
                    featuresByLayer.put(SynchronizePlugIn.ADDED_FEATURES, addFeatures);

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
                        currentFeature.setLayer(targetLayer);
                        deleteFeatures.add(currentFeature);

                    }

                    if (deleteErrors > 0)
                    {
                        String tempStringBuffer = MessageFormat
                                .format(
                                        aplicacion
                                                .getI18nString("SynchronizePlugIn.NumeroFeaturesErroneasBorrar"),
                                        new Object[] { String.valueOf(deleteErrors),
                                                new Integer(deleteErrors),
                                                targetLayer.getName() });

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
                                .format(
                                        aplicacion
                                                .getI18nString("SynchronizePlugIn.NumeroFeaturesErroneasModificar"),
                                        new Object[] { String.valueOf(modifiedErrors),
                                                new Integer(modifiedErrors),
                                                targetLayer.getName() });

                        int selectedValue = JOptionPane.showOptionDialog(aplicacion
                                .getMainFrame(), tempStringBuffer, aplicacion
                                .getI18nString("SynchronizePlugIn.Problemas"), 0,
                                JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                                possibleValues[0]);
                        if (selectedValue == CANCELAR)
                            return;
                    }

                    featuresByLayer.put(SynchronizePlugIn.MODIFIED_FEATURES,
                            modifedFeaturesHashtable);

                    layerToSynchronize.put(targetLayer, featuresByLayer);
                }
            }
            geopistaMapTarget.getTaskComponent().getLayerViewPanel().getLayerManager()
                    .getUndoableEditReceiver().startReceiving();
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
                                    .get(SynchronizePlugIn.DELETE_FEATURES);
                            ArrayList localAddedFeatures = (ArrayList) currentLayerFeaturesToSynchronize
                                    .get(SynchronizePlugIn.ADDED_FEATURES);
                            Hashtable localModifedFeaturesHashtable = (Hashtable) currentLayerFeaturesToSynchronize
                                    .get(SynchronizePlugIn.MODIFIED_FEATURES);
                            Hashtable clonesModifiedFeatures = new Hashtable();
                            exec(localMonitor, geopistaMapTarget, lockManager,
                                    localTargetLayer, localDeleteFeatures,
                                    localAddedFeatures, localModifedFeaturesHashtable,
                                    clonesModifiedFeatures);
                            currentLayerFeaturesToSynchronize.put(
                                    SynchronizePlugIn.MODIFIED_CLONES_FEATURES,
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
                                    .get(SynchronizePlugIn.DELETE_FEATURES);
                            ArrayList localAddedFeatures = (ArrayList) currentLayerFeaturesToSynchronize
                                    .get(SynchronizePlugIn.ADDED_FEATURES);
                            Hashtable localModifedFeaturesHashtable = (Hashtable) currentLayerFeaturesToSynchronize
                                    .get(SynchronizePlugIn.MODIFIED_FEATURES);
                            Hashtable clonesModifiedFeatures = (Hashtable) currentLayerFeaturesToSynchronize
                                    .get(SynchronizePlugIn.MODIFIED_CLONES_FEATURES);
                            unexec(localMonitor, geopistaMapTarget, lockManager,
                                    localTargetLayer, localDeleteFeatures,
                                    localAddedFeatures, localModifedFeaturesHashtable,
                                    clonesModifiedFeatures);
                        }
                    }
                }, (LayerViewPanel)geopistaMapTarget.getTaskComponent().getLayerViewPanel());

            geopistaMapTarget.getTaskComponent().getLayerViewPanel().getLayerManager()
                    .getUndoableEditReceiver().stopReceiving();

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
    private void exec(final TaskMonitor monitor, final GeopistaMap geopistaMapTarget,
            final LockManager lockManager, final GeopistaLayer targetLayer,
            ArrayList localDeleteFeatures, ArrayList localAddedFeatures,
            Hashtable localModifedFeaturesHashtable, Hashtable clonesModifiedFeatures)
    {
        try
        {

            lockManager.lockSelectedFeatures(localDeleteFeatures, monitor);
            lockManager.lockSelectedFeatures(new ArrayList(localModifedFeaturesHashtable
                    .values()), monitor);

            monitor.report(aplicacion.getI18nString("sincronizandoCapa") + " "
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
                lockManager.unlockAllLockedFeatures((LayerManager)geopistaMapTarget.getLayerManager(),
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

}
