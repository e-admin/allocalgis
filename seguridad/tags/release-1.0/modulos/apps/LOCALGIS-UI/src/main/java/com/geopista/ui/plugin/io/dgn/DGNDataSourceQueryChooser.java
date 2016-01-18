/*
 * Created on 14-jun-2004
 */
package com.geopista.ui.plugin.io.dgn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.LoadFileDataSourceQueryChooser;

/**
 * @author Enxenio, SL
 */
public class DGNDataSourceQueryChooser extends LoadFileDataSourceQueryChooser {

	public DGNDataSourceQueryChooser(Class dataSourceClass, String description, String[] extensions, WorkbenchContext context) {
		super(dataSourceClass, description, extensions, context);
	}
			
	public Collection getDataSourceQueries() {
		ArrayList resultado = new ArrayList();
		DGNReader reader = new DGNReader();
		Collection dataSourceQueries = super.getDataSourceQueries();
		Iterator iterator = dataSourceQueries.iterator();
		while(iterator.hasNext()){
			DataSourceQuery dataSourceQuery = (DataSourceQuery) iterator.next();
			DataSource dataSource = dataSourceQuery.getDataSource();
			final String selectedFile = (String)dataSource.getProperties().get(DataSource.FILE_KEY);
			DriverProperties dp = new DriverProperties();
			dp.put("File", selectedFile);
			try {
				reader.read(dp);
				Iterator featureTypesIterator = reader.getFeatureTypesIterator();
				while(featureTypesIterator.hasNext()) {
					FeatureCollection featureCollection = (FeatureCollection) featureTypesIterator.next();
					String layerName = reader.getFeatureCollectionName(featureCollection);
					DataSourceWrapper dataSourceWrapper = new DataSourceWrapper(featureCollection);
					DataSourceQuery dataSourceQueryWrapper = new DataSourceQuery(dataSourceWrapper, null, layerName);
					resultado.add(dataSourceQueryWrapper);
					//TODO: Pasar un properties...
					HashMap properties= new HashMap();
					properties.put(DataSource.COORDINATE_SYSTEM_KEY,null);
					dataSourceWrapper.setProperties(properties);
				}
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		} //while
		return resultado;
	}

	private class DataSourceWrapper extends DataSource{
		private Connection connection;
		private FeatureCollection featureCollection;
		
		// TODO: Implementar las propiedades que necesita JUMP para el sistema de coordenadas
		public DataSourceWrapper(final FeatureCollection featureCollection){
			this.featureCollection = featureCollection;
			
			this.connection = new Connection(){	
				public FeatureCollection executeQuery(String query, Collection exceptions, TaskMonitor monitor) {
					return featureCollection;
				}

				public FeatureCollection executeQuery(String query, TaskMonitor monitor) throws Exception {
					return featureCollection;
				}

				public ArrayList executeUpdate(String query, FeatureCollection featureCollection, TaskMonitor monitor) throws Exception {
					return null;
				}

				public void close() {
				}
			
			};
		}
		public Connection getConnection() {
			return connection;
		}
	}//DataSourceWrapper
}
