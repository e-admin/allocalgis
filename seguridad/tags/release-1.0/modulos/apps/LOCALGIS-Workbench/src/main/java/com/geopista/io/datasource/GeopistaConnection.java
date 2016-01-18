/*
 * Created on 18-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.io.datasource;

import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.protocol.Version;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FeatureLockResult;
import com.geopista.server.administradorCartografia.FilterNode;
import com.geopista.server.administradorCartografia.LockException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.task.TaskMonitor;


/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GeopistaConnection implements Connection, IGeopistaConnection
{

    private IGeopistaLayer layer = null;
    
    private DriverProperties driverProperties = null;

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private AdministradorCartografiaClient administradorCartografiaClient = null;
    
    private boolean isDinamica = false;
    
    /**
     * 
     */

    public GeopistaConnection(DriverProperties driverProperties)
        {

        	this.driverProperties = driverProperties;
            String sUrlPrefix = aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA);
            administradorCartografiaClient = new AdministradorCartografiaClient(sUrlPrefix);
        }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQuery(java.lang.String, java.util.Collection, com.vividsolutions.jump.task.TaskMonitor)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQuery(java.lang.String, java.util.Collection, com.vividsolutions.jump.task.TaskMonitor)
	 */
    
	
	public FeatureCollection executeQuery(String query, Collection exceptions, TaskMonitor monitor)
    {

        try
        {
            if(driverProperties==null) throw new NullPointerException("DriverProperties no puede ser nulo");
            
            
            GeopistaMap map = (GeopistaMap) driverProperties.get("mapadestino");
            
            Geometry g = (Geometry) driverProperties.get("filtrogeometrico");
            FilterNode filter = (FilterNode) driverProperties.get("nodofiltro");            
            Integer srid_destino; 
            if (driverProperties.get("srid_destino") instanceof Integer)
            	srid_destino = (Integer)driverProperties.get("srid_destino");
            else{
            	if (driverProperties.get("srid_destino")!=null)
            		srid_destino = new Integer((String)driverProperties.get("srid_destino"));
            	else
            		srid_destino = new Integer(this.getSRIDDefecto(true,-1));
            	
            
            }
            if (driverProperties.get(AppContext.VERSION) != null){
            	Version version = (Version)driverProperties.get(AppContext.VERSION);
            	aplicacion.getBlackboard().put(AppContext.VERSION, version);
            }else{
            	aplicacion.getBlackboard().remove(AppContext.VERSION);
            }
            //Sacamos de la url del layer el identificador
            URL urlLayer = new URL(query);
            String idLayer = urlLayer.getFile();
            if(idLayer.indexOf('/')==0)
            {
                idLayer = urlLayer.getFile().substring(1);
            }
            

            GeopistaLayer localLayer = administradorCartografiaClient.loadLayer(map, idLayer, aplicacion.getUserPreference(AppContext.PREFERENCES_LOCALE_KEY, "es_ES", true), g, filter, srid_destino,monitor);
            this.layer = localLayer;
            
            if (isDinamica)
                return ((DynamicLayer)layer).getFeatureCollectionWrapper().getUltimateWrappee();
            else
            	return layer.getFeatureCollectionWrapper().getUltimateWrappee();
        } catch (Exception e)
        {
            exceptions.add(e);
            return null;
            
        }

    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQueryLayer(java.lang.String, java.util.Collection, com.vividsolutions.jump.task.TaskMonitor, com.geopista.model.GeopistaLayer)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQueryLayer(java.lang.String, java.util.Collection, com.vividsolutions.jump.task.TaskMonitor, com.geopista.model.GeopistaLayer)
	 */
    
	
	public FeatureCollection executeQueryLayer(String query, Collection exceptions, TaskMonitor monitor, IGeopistaLayer localLayer)
    {

        try
        {
            if(driverProperties==null) throw new NullPointerException("DriverProperties no puede ser nulo");
            
            
            GeopistaMap map = (GeopistaMap) driverProperties.get("mapadestino");
            Geometry g = (Geometry) driverProperties.get("filtrogeometrico");
            FilterNode filter = (FilterNode) driverProperties.get("nodofiltro");            
            Integer srid_destino; 
            if (driverProperties.get("srid_destino") instanceof Integer)
            	srid_destino = (Integer)driverProperties.get("srid_destino");
            else
            	srid_destino = new Integer((String)driverProperties.get("srid_destino"));

            if (driverProperties.get(AppContext.VERSION) != null){
            	Version version = (Version)driverProperties.get(AppContext.VERSION);
            	aplicacion.getBlackboard().put(AppContext.VERSION, version);
            }
            
            
            //Sacamos de la url del layer el identificador
            URL urlLayer = new URL(query);
            String idLayer = urlLayer.getFile();
            if(idLayer.indexOf('/')==0)
            {
                idLayer = urlLayer.getFile().substring(1);
            }
            
            localLayer = administradorCartografiaClient.loadLayer(map, localLayer, aplicacion.getUserPreference(AppContext.PREFERENCES_LOCALE_KEY, "es_ES", true), g, filter, true,srid_destino);
            this.layer = localLayer;
            
            if (isDinamica)
                return ((DynamicLayer)layer).getFeatureCollectionWrapper().getUltimateWrappee();
            else
            	return layer.getFeatureCollectionWrapper().getUltimateWrappee();
        } catch (Exception e)
        {
            exceptions.add(e);
            return null;
            
        }

    }
    

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#loadFeatures(com.geopista.model.GeopistaLayer, java.lang.Object[])
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#loadFeatures(com.geopista.model.GeopistaLayer, java.lang.Object[])
	 */
    
	
	public List loadFeatures(IGeopistaLayer localLayer, Object[] featureIds)
    {
        try
        {
        	Integer srid_destino = null;
            if(driverProperties==null) throw new NullPointerException("DriverProperties no puede ser nulo");
            if (driverProperties.get("srid_destino") instanceof Integer)
            	srid_destino = (Integer)driverProperties.get("srid_destino");
            else
            	srid_destino = new Integer((String)driverProperties.get("srid_destino"));
            return administradorCartografiaClient.loadFeatures(localLayer,Arrays.asList(featureIds),aplicacion.getUserPreference(AppContext.PREFERENCES_LOCALE_KEY, "es_ES", true),srid_destino);
        } catch (Exception e)
        {
            return null;
            
        }

    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeUpdate(java.lang.String, com.vividsolutions.jump.feature.FeatureCollection, com.vividsolutions.jump.task.TaskMonitor)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeUpdate(java.lang.String, com.vividsolutions.jump.feature.FeatureCollection, com.vividsolutions.jump.task.TaskMonitor)
	 */
    
	
	public ArrayList executeUpdate(String update, FeatureCollection featureCollection, TaskMonitor monitor) throws Exception
    {
        boolean refreshFeatures = true;
        boolean validateFeatures = true;
        
        if (update == null) return null;
        
        Object refreshFeaturesObject = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
        Integer srid_destino = -1; 
        if (driverProperties.get("srid_destino") != null){
		    if (driverProperties.get("srid_destino") instanceof Integer)
		        srid_destino = (Integer)driverProperties.get("srid_destino");
		       else
		        srid_destino = new Integer((String)driverProperties.get("srid_destino"));
        }
        if (driverProperties.get("srid_inicial") != null){
		    AppContext.getApplicationContext().getBlackboard().put(AppContext.SRID_INICIAL, driverProperties.get("srid_inicial"));
        }else
		    AppContext.getApplicationContext().getBlackboard().put(AppContext.SRID_INICIAL, null);
        if(refreshFeaturesObject!=null)
        {
            try
            {
                Boolean refreshFeaturesBoolean = (Boolean) refreshFeaturesObject;
                refreshFeatures = refreshFeaturesBoolean.booleanValue();
            }
            catch(Exception e)
            {
                //si falla algo se hace refresco de features
            }
        }
        
        Object validateFeaturesObject = driverProperties.get(Constantes.VALIDATE_ABSTRACT_FEATURE);
        
        if(validateFeaturesObject!=null)
        {
            try
            {
                Boolean validateFeaturesBoolean = (Boolean) validateFeaturesObject;
                validateFeatures = validateFeaturesBoolean.booleanValue();
            }
            catch(Exception e)
            {
                //si falla algo se hace refresco de features
            }
        }
        
        String language = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", true);

        ArrayList features = (ArrayList) administradorCartografiaClient.uploadFeatures(language, featureCollection.getFeatures().toArray(),refreshFeatures, validateFeatures, monitor, srid_destino);
        return features;

    }
    
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeUpdateFeatures(java.lang.Object[], com.vividsolutions.jump.task.TaskMonitor)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeUpdateFeatures(java.lang.Object[], com.vividsolutions.jump.task.TaskMonitor)
	 */
    
	
	public ArrayList executeUpdateFeatures(Object[] features, TaskMonitor monitor) throws Exception
    {
        boolean refreshFeatures = true;
        boolean validateFeatures = true;
        
        Object refreshFeaturesObject = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
        Integer srid_destino; 
        if (driverProperties.get("srid_destino") instanceof Integer)
        	srid_destino = (Integer)driverProperties.get("srid_destino");
        else
        	srid_destino = new Integer((String)driverProperties.get("srid_destino"));
        
        if(refreshFeaturesObject!=null)
        {
            try
            {
                Boolean refreshFeaturesBoolean = (Boolean) refreshFeaturesObject;
                refreshFeatures = refreshFeaturesBoolean.booleanValue();
            }
            catch(Exception e)
            {
                //si falla algo se hace refresco de features
            }
        }
        
        Object validateFeaturesObject = driverProperties.get(Constantes.VALIDATE_ABSTRACT_FEATURE);
        
        if(validateFeaturesObject!=null)
        {
            try
            {
                Boolean validateFeaturesBoolean = (Boolean) validateFeaturesObject;
                validateFeatures = validateFeaturesBoolean.booleanValue();
            }
            catch(Exception e)
            {
                //si falla algo se hace refresco de features
            }
        }
        
        if (driverProperties.get(AppContext.VERSION) != null){
        	Version version = (Version)driverProperties.get(AppContext.VERSION);
        	version.setbRecuperarFeatures(true);
        	aplicacion.getBlackboard().put(AppContext.VERSION, version);
        }else{
        	aplicacion.getBlackboard().remove(AppContext.VERSION);
        }

        String language = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", true);

        return (ArrayList) administradorCartografiaClient.uploadFeatures(language, features,refreshFeatures, validateFeatures, monitor, srid_destino);

    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#close()
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#close()
	 */
    
	
	public void close()
    {
    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQuery(java.lang.String, com.vividsolutions.jump.task.TaskMonitor)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQuery(java.lang.String, com.vividsolutions.jump.task.TaskMonitor)
	 */
    
	
	public FeatureCollection executeQuery(String query, TaskMonitor monitor) throws Exception
    {
        ArrayList exceptions = new ArrayList();
        FeatureCollection featureCollection = executeQuery(query, exceptions, monitor);
        if (!exceptions.isEmpty())
        {
            throw (Exception) exceptions.iterator().next();
        }
        return featureCollection;
    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getSRIDDefecto(boolean, int)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getSRIDDefecto(boolean, int)
	 */
    
	
	public String getSRIDDefecto(boolean defecto, int idEntidad) throws Exception
    {
    	return administradorCartografiaClient.getSRIDDefecto(defecto, idEntidad);
    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getLayer()
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getLayer()
	 */
    
	
	public IGeopistaLayer getLayer()
    {
        return this.layer;
    }
    
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#lockLayer(java.lang.String, com.vividsolutions.jts.geom.Geometry)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#lockLayer(java.lang.String, com.vividsolutions.jts.geom.Geometry)
	 */
    
	
	public int lockLayer(String sLayer, Geometry g)
    {
        //administradorCartografiaClient.loadLayer();
        return 0;
    }
    
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#unlockLayer(java.lang.String)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#unlockLayer(java.lang.String)
	 */
    
	
	public int unlockLayer(String sLayer)
    {
        //administradorCartografiaClient.unlockLayer();
    	return 0;
    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#lockFeatures(java.util.List, com.vividsolutions.jump.task.TaskMonitor)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#lockFeatures(java.util.List, com.vividsolutions.jump.task.TaskMonitor)
	 */
    
	
	public FeatureLockResult lockFeatures(List lockedFeatures, TaskMonitor monitor)
    throws ACException, LockException, Exception {
    	
    	if(monitor!=null) {
    		monitor.report(aplicacion.getI18nString("bloqueandoFeatures"));
    	}
    	    	
    	ArrayList layers = new ArrayList();
    	ArrayList featureIds = new ArrayList();
    	    
    	int count = 0;
    	int packetSize = 250;
		FeatureLockResult featureLockResult = null;
    	for (int i = 0; i < lockedFeatures.size(); i++) {
    		GeopistaFeature currentFeature = (GeopistaFeature) lockedFeatures.get(i);
    		
    		if(currentFeature.getSystemId()!=null&&!currentFeature.getSystemId().equals("")&&!currentFeature.isTempID()) {
    			layers.add(currentFeature.getLayer().getSystemId());    				
    			featureIds.add(new Integer(currentFeature.getSystemId()));
    			count++;
    		}
    		    		
    		if ((count != 0 && count%packetSize == 0) || i == lockedFeatures.size() - 1){
    			if (monitor != null) {
                    monitor.report(count, lockedFeatures.size(), aplicacion.getI18nString("features"));
                }
    			
    			int numItems = (count%packetSize == 0) ? packetSize : count%packetSize;
    			    			
    			ArrayList layersToLock = new ArrayList(layers.subList(layers.size() - numItems, layers.size()));
    			ArrayList featureIdsToLock = new ArrayList(featureIds.subList(featureIds.size() - numItems, layers.size()));
    			
    			featureLockResult =	administradorCartografiaClient.lockFeature(layersToLock, featureIdsToLock);
    	    	
    			if (featureLockResult == null ||
    				featureLockResult.getLockResultCode() != AdministradorCartografiaClient.LOCK_FEATURE_LOCKED){
    				try {
    					unlockFeatures(lockedFeatures.subList(0, i), null);
    				} catch(Exception e){}
    				break;
    			}    	    	
    		}
    	}
		    	
    	if (featureLockResult == null){
    		throw new LockException("LockException");
    	}
    	else if (featureLockResult.getLockResultCode() != AdministradorCartografiaClient.LOCK_FEATURE_LOCKED){
    		throw new LockException("LockException", featureLockResult);
    	}
    	
    	return featureLockResult;
    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#unlockFeatures(java.util.Collection, com.vividsolutions.jump.task.TaskMonitor)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#unlockFeatures(java.util.Collection, com.vividsolutions.jump.task.TaskMonitor)
	 */
    
	
	public int unlockFeatures(Collection unlockedFeatures, TaskMonitor monitor) throws Exception
    {
        if (monitor!=null) {
            monitor.report(aplicacion.getI18nString("desbloqueandoFeatures"));
        }
        
        int result = 0;
                
        ListIterator listIterator = ((List) unlockedFeatures).listIterator();
        ArrayList layers = new ArrayList();
        ArrayList featureIds = new ArrayList();

        while(listIterator.hasNext()) {
        	GeopistaFeature currentFeature = (GeopistaFeature) listIterator.next();

        	if(currentFeature.getSystemId()!=null&&!currentFeature.getSystemId().equals("")&&!currentFeature.isTempID()) {
        		layers.add(currentFeature.getLayer().getSystemId());    				
        		featureIds.add(new Integer(currentFeature.getSystemId()));
        	}
        }

        boolean unlockOK = false;
        while(!unlockOK) {    
        	Exception catchedException = null;
        	try {
        		List unlockResult = administradorCartografiaClient.unlockFeature(layers, featureIds);
        		if (unlockResult != null){
        			unlockOK = true;
        		}        		
        	}catch(Exception e) {
        		unlockOK = false;
        		catchedException = e;
        	}
        	if (!unlockOK){
        		Object[] optionsError = { aplicacion.getI18nString("Reintentar"),
        				aplicacion.getI18nString("OKCancelPanel.Cancel") };
        		int confirmResultError = JOptionPane.showOptionDialog((Component) aplicacion
        				.getMainFrame(), aplicacion.getI18nString("ErrorAlDesbloquearFeature"), null,
        				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
        				optionsError, optionsError[0]);
        		if (confirmResultError==1){
        			throw catchedException;
        		}
        	}
        }

        return result;        
    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#isDinamica()
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#isDinamica()
	 */
    
	
	public boolean isDinamica(){
    	return isDinamica;
    }

    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#setDinamica(boolean)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#setDinamica(boolean)
	 */
    
	
	public void setDinamica(boolean isDinamica){
    	this.isDinamica = isDinamica;
    }
    
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getDriverProperties()
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getDriverProperties()
	 */
    
	
	public DriverProperties getDriverProperties(){
        return driverProperties;
    }
    
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#obtenerGeometriaMunicipio(int)
	 */
    /* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#obtenerGeometriaMunicipio(int)
	 */
    
	
	public Geometry obtenerGeometriaMunicipio(int codMunicipio){
    	return administradorCartografiaClient.obtenerGeometriaMunicipio(codMunicipio);
    }
	
	public void setDriverProperties(DriverProperties driverProperties){
		this.driverProperties = driverProperties;
	}
}
