package com.geopista.io.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.geopista.model.GeopistaLayer;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.FeatureLockResult;
import com.geopista.server.administradorCartografia.LockException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.task.TaskMonitor;

public interface IGeopistaConnection {

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQuery(java.lang.String, java.util.Collection, com.vividsolutions.jump.task.TaskMonitor)
	 */
	public abstract FeatureCollection executeQuery(String query,
			Collection exceptions, TaskMonitor monitor);

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQueryLayer(java.lang.String, java.util.Collection, com.vividsolutions.jump.task.TaskMonitor, com.geopista.model.GeopistaLayer)
	 */
	public abstract FeatureCollection executeQueryLayer(String query,
			Collection exceptions, TaskMonitor monitor, GeopistaLayer localLayer);

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#loadFeatures(com.geopista.model.GeopistaLayer, java.lang.Object[])
	 */
	public abstract List loadFeatures(GeopistaLayer localLayer,
			Object[] featureIds);

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeUpdate(java.lang.String, com.vividsolutions.jump.feature.FeatureCollection, com.vividsolutions.jump.task.TaskMonitor)
	 */
	public abstract ArrayList executeUpdate(String update,
			FeatureCollection featureCollection, TaskMonitor monitor)
			throws Exception;

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeUpdateFeatures(java.lang.Object[], com.vividsolutions.jump.task.TaskMonitor)
	 */
	public abstract ArrayList executeUpdateFeatures(Object[] features,
			TaskMonitor monitor) throws Exception;

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#close()
	 */
	public abstract void close();

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#executeQuery(java.lang.String, com.vividsolutions.jump.task.TaskMonitor)
	 */
	public abstract FeatureCollection executeQuery(String query,
			TaskMonitor monitor) throws Exception;

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getSRIDDefecto(boolean, int)
	 */
	public abstract String getSRIDDefecto(boolean defecto, int idEntidad)
			throws Exception;

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getLayer()
	 */
	public abstract GeopistaLayer getLayer();

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#lockLayer(java.lang.String, com.vividsolutions.jts.geom.Geometry)
	 */
	public abstract int lockLayer(String sLayer, Geometry g);

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#unlockLayer(java.lang.String)
	 */
	public abstract int unlockLayer(String sLayer);

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#lockFeatures(java.util.List, com.vividsolutions.jump.task.TaskMonitor)
	 */
	public abstract FeatureLockResult lockFeatures(List lockedFeatures,
			TaskMonitor monitor) throws ACException, LockException, Exception;

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#unlockFeatures(java.util.Collection, com.vividsolutions.jump.task.TaskMonitor)
	 */
	public abstract int unlockFeatures(Collection unlockedFeatures,
			TaskMonitor monitor) throws Exception;

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#isDinamica()
	 */
	public abstract boolean isDinamica();

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#setDinamica(boolean)
	 */
	public abstract void setDinamica(boolean isDinamica);

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#getDriverProperties()
	 */
	public abstract DriverProperties getDriverProperties();

	/* (non-Javadoc)
	 * @see com.geopista.io.datasource.IGeopistaConnection#obtenerGeometriaMunicipio(int)
	 */
	public abstract Geometry obtenerGeometriaMunicipio(int codMunicipio);
	public abstract void setDriverProperties(DriverProperties driverProperties);

}