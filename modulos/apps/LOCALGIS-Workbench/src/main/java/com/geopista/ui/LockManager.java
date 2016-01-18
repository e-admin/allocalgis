/**
 * LockManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 20-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * 
 * www.geopista.com
 *
 */

package com.geopista.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JToggleButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.IGeopistaConnection;
import com.geopista.model.GeopistaLayer;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.LockException;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.LayerManager;

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class LockManager
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(LockManager.class);

    // String[] lockedTools = {"Move Selected Items"};
    private Hashtable idFeaturesLock = new Hashtable();

    private Hashtable featureIdsLock = new Hashtable();

    private Hashtable idTaskLock = new Hashtable();

    private int lockId = 0;

    public static final String LOCK_MANAGER_KEY = "lockManager";

    public static final String SELECTION_TOOL_LOCK_GROUP_ID_KEY = "selectionToolLockGroupID";

    // Almacenamos el boton del plugin que controla en bloqueo
    JToggleButton jToggleButton = null;

    LockListener lockListener = null;

    /**
     * Bloquea las features que se le proporcionan en el hashtable de entrada,
     * si alguna de ellas falla al bloquearse se desbloquean las que ya habian
     * sido bloqueadas y se informa al usuario del error, es decir se bloquean
     * todas o ninguna.
     * 
     * @param selectionManager
     * @param currentCursorTool
     * @return
     * @throws ACException
     * @throws LockException
     */

    public LockManager(LockListener lockListener)
        {
            this.lockListener = lockListener;
        }
    
    public Integer lockFeature(Feature lockFeature,
            TaskMonitor monitor) throws ACException, LockException, Exception
            {
        		List tempCollection = new ArrayList();
        		tempCollection.add(lockFeature);
        		return lockSelectedFeatures(tempCollection, monitor);
            }

    public Integer lockSelectedFeatures(List featuresToLock,
            TaskMonitor monitor) throws ACException, LockException, Exception
    {

        try
        {
            List featuresToBeLocked=new ArrayList(featuresToLock);// Hacemos una copia para almacenar localmente en la historia del lockmanager
            Integer lockIdInteger = null;
            if (featuresToBeLocked.size() > 0)
            {
                Object currentFeatureObject = featuresToBeLocked.iterator().next();
                if (currentFeatureObject instanceof GeopistaFeature)
                {
                    GeopistaFeature currentFeature = (GeopistaFeature) currentFeatureObject;
                    if (currentFeature.getLayer() instanceof GeopistaLayer)
                    {
                        GeopistaLayer currentLayer = (GeopistaLayer)currentFeature.getLayer();
                        if (currentLayer.getDataSourceQuery() != null)
                        {
                            DataSourceQuery dataSourceQuery = currentLayer
                                    .getDataSourceQuery();

                            DataSource dataSource = dataSourceQuery.getDataSource();
                            if (dataSource.getConnection() instanceof IGeopistaConnection)
                            {
                            	IGeopistaConnection geopistaConnection = (IGeopistaConnection) dataSource
                                        .getConnection();
                                geopistaConnection.lockFeatures(
                                        featuresToBeLocked, monitor);
                                lockIdInteger = new Integer(lockId++);
                                registerFeatures(featuresToBeLocked, lockIdInteger);
                            }
                        }
                    }
                }
            }

            return lockIdInteger;
        } finally
        {
            lockListener.updateButton();
            lockListener.updateLayerViewPanel();
        }
    }

    public void unlockFeaturesByLockId(Integer lockId, TaskMonitor monitor)
            throws Exception
    {
        try
        {
            // Metemos aqui la lista de features que queremos desbloquear
            Collection featuresToUnlock = new ArrayList();
            // a partir del identificador de bloqueo proporcionado sacamos la
            // lista de features
            // bloquedas asociadas a este identificador
            Collection lockedFeatures = (Collection) idFeaturesLock.get(lockId);
            // si no hay features bloquedas no hacemos nada
            if (lockedFeatures == null)
                return;

            // Creamos una nueva lista para recorrerla ya que las modificaciones
            // en la lista
            // inicial provocan problemas en el bucle.
            Collection lockedFeaturesTemp = new ArrayList();
            lockedFeaturesTemp.addAll(lockedFeatures);

            Iterator lockedFeaturesIter = lockedFeaturesTemp.iterator();

            while (lockedFeaturesIter.hasNext())
            {
                GeopistaFeature currentFeature = (GeopistaFeature) lockedFeaturesIter
                        .next();
                if (currentFeature.getSystemId() != null
                        && !currentFeature.getSystemId().equals("")
                        && !currentFeature.isTempID())
                {
                    Collection currentLockIds = (Collection) featureIdsLock
                            .get(currentFeature);
                    if (currentLockIds.size() == 1)
                    {
                        featuresToUnlock.add(currentFeature);
                    }
                }
            }

            if (featuresToUnlock.size() > 0)
            {
                GeopistaFeature tempFeature = (GeopistaFeature) featuresToUnlock
                        .iterator().next();
                IGeopistaConnection geopistaConnection = (IGeopistaConnection) tempFeature
                        .getLayer().getDataSourceQuery().getDataSource().getConnection();
                geopistaConnection.unlockFeatures(featuresToUnlock, monitor);
            }

            lockedFeaturesIter = lockedFeaturesTemp.iterator();
            while (lockedFeaturesIter.hasNext())
            {
                GeopistaFeature currentFeature = (GeopistaFeature) lockedFeaturesIter
                        .next();
                if (currentFeature.getSystemId() != null
                        && !currentFeature.getSystemId().equals("")
                        && !currentFeature.isTempID())
                {
                    Collection currentLockIdsFeature = (Collection) featureIdsLock
                            .get(currentFeature);
                    if (this.containElement(lockId, currentLockIdsFeature))
                    {
                        currentLockIdsFeature.remove(lockId);
                    }
                    if (currentLockIdsFeature.size() == 0)
                    {
                        featureIdsLock.remove(currentFeature);
                        lockedFeatures.remove(currentFeature);

                    }
                }

            }
            if (lockedFeatures.size() == 0)
                idFeaturesLock.remove(lockId);

            idTaskLock.remove(lockId);

        } finally
        {
            lockListener.updateButton();
            lockListener.updateLayerViewPanel();
        }

    }

    /*
     * private boolean isLockedTool(String currentCursorTool) { for(int n=0;n
     * <lockedTools.length;n++) { if(currentCursorTool.equals(lockedTools[n]))
     * return true; } return false; }
     */

    private boolean containElement(Object currentId, Collection currentCollection)
    {

        Iterator currentCollectionIter = currentCollection.iterator();
        while (currentCollectionIter.hasNext())
        {
            Object currentElement = currentCollectionIter.next();
            if (currentId.equals(currentElement))
                return true;

        }

        return false;
    }

    private void registerFeatures(Collection featuresWithSelectedItems,
            Integer lockIdInteger)
    {
        idFeaturesLock.put(lockIdInteger, featuresWithSelectedItems);

        Iterator featuresWithSelectedItemsIter = featuresWithSelectedItems.iterator();
        while (featuresWithSelectedItemsIter.hasNext())
        {
            GeopistaFeature currentFeature = (GeopistaFeature) featuresWithSelectedItemsIter
                    .next();
            if (currentFeature.getSystemId() != null
                    && !currentFeature.getSystemId().equals("")
                    && !currentFeature.isTempID())
            {
                if (featureIdsLock.contains(currentFeature))
                {
                    Collection currentFeatureIds = (Collection) featureIdsLock
                            .get(currentFeature);
                    currentFeatureIds.add(lockIdInteger);

                } else
                {
                    Collection currentFeatureIds = new ArrayList();
                    currentFeatureIds.add(lockIdInteger);
                    featureIdsLock.put(currentFeature, currentFeatureIds);

                }

            }

        }
        GeopistaFeature tempFeature = (GeopistaFeature) featuresWithSelectedItems
                .iterator().next();
        idTaskLock.put(lockIdInteger, tempFeature.getLayer().getLayerManager());

    }

    public Collection getLockedFeatures()
    {
        return featureIdsLock.keySet();
    }

    public void unlockAllLockedFeatures(LayerManager layerManager, TaskMonitor monitor)
            throws Exception
    {
        ArrayList finalUnlockIds = new ArrayList();

        Collection lockIdsLayerManager = idTaskLock.keySet();
        Iterator lockIdsLayerManagerIter = lockIdsLayerManager.iterator();

        while (lockIdsLayerManagerIter.hasNext())
        {
            Integer currentIdLock = (Integer) lockIdsLayerManagerIter.next();
            if (idTaskLock.get(currentIdLock) == layerManager)
            {
                finalUnlockIds.add(currentIdLock);
            }

        }

        Iterator finalUnlockIdsIter = finalUnlockIds.iterator();

        while (finalUnlockIdsIter.hasNext())
        {
            Integer currentIdLock = (Integer) finalUnlockIdsIter.next();
            this.unlockFeaturesByLockId(currentIdLock, monitor);

        }

    }

    public boolean existLockedFeaturesInTask(LayerManager layerManager)
    {
        Collection lockIdsLayerManager = idTaskLock.keySet();
        Iterator lockIdsLayerManagerIter = lockIdsLayerManager.iterator();

        while (lockIdsLayerManagerIter.hasNext())
        {
            Integer currentIdLock = (Integer) lockIdsLayerManagerIter.next();
            if (idTaskLock.get(currentIdLock) == layerManager)
            {
                return true;
            }

        }

        return false;
    }
    
    public boolean existLockedFeatures()
    {
        if(featureIdsLock.size()>0) return true;

        return false;
    }
    
    public void unlockAllLockedFeatures(TaskMonitor monitor) throws Exception
    {
        ArrayList finalUnlockIds = new ArrayList();

        Collection lockIdsLayerManager = idTaskLock.keySet();
        //creamos un arrayList para que no nos afecten los borrados de elementos
        ArrayList allLockIds = new ArrayList(lockIdsLayerManager);
        Iterator lockIdsLayerManagerIter = allLockIds.iterator();

        while (lockIdsLayerManagerIter.hasNext())
        {
            Integer currentIdLock = (Integer) lockIdsLayerManagerIter.next();
            this.unlockFeaturesByLockId(currentIdLock, monitor);

        }
    }

}
