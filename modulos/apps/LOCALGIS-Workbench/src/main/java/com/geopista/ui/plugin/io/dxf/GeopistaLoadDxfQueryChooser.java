/**
 * GeopistaLoadDxfQueryChooser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.io.dxf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.plugin.io.dxf.core.jump.io.DxfReader;
import com.geopista.ui.plugin.io.dxf.core.jump.io.FeatureCollectionDxfConverter;
import com.geopista.ui.plugin.io.dxf.core.jump.io.GeopistaDxfConverter;
import com.geopista.ui.plugin.io.dxf.reader.DxfException;
import com.geopista.ui.plugin.io.dxf.reader.DxfFile;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.LoadFileDataSourceQueryChooser;


public class GeopistaLoadDxfQueryChooser extends LoadFileDataSourceQueryChooser {

		/**
		 * @param dataSourceClass ScalableShapefileReader que nos permite
		 * construir FeatureCollection que accede a los ficheros dxf seleccionados
		 *
		 * @param description descripcion del origen
		 *
		 * @param extensions file extensions associated to dxf 
		 */
		public GeopistaLoadDxfQueryChooser(Class dataSourceClass, String description,
		String[] extensions, WorkbenchContext context) {
			super(dataSourceClass, description, extensions, context);

		}

		/**
		 * En principio, este metodo debería devolver una coleccion de DataSourceQuery.
		 * Cada uno de estos envolvería a un DataSource,  y este a una Connection, cuya
		 * llamada a executeQuery devolveria un FeatureCollection.<br>
		 *
		 * Pero resulta que dentro de cada Dxf podemos tener varias FeatureCollection.
		 *
		 * Por eso vamos a hacer un pequeño "truco". Obtenemos el fichero Dxf, lo interpretamos
		 * (con DxfReader), y obtenemos todas las FeatureCollection de todos los tipos (punto,
		 * linea, poligono, texto). Para cada una de ellas creamos un DataSourceQuery que
		 * envuelve a la FeatureCollection.
		 *
		 * @return colección de DataSourceQuery que nos
		 *
		 * todo ESTO SE ESTÁ LLAMANDO 2 VECES. CACHEAR
		 */
		public Collection getDataSourceQueries() {

			//aquí se guardaran todas las DataSourceQuery que se vayan creando
			ArrayList solucion = new ArrayList();
			GeopistaDxfConverter converter = new GeopistaDxfConverter();
			//Primero obtenemos los ficheros Dxf seleccionados
			Collection dataSourceQuerysFile = super.getDataSourceQueries();
			Iterator iterator = dataSourceQuerysFile.iterator();

			while(iterator.hasNext()){
				DataSourceQuery dataSourceQuery = (DataSourceQuery) iterator.next();
				DataSource dataSource = dataSourceQuery.getDataSource();
				final String selectedFile = (String)dataSource.getProperties().get(DataSource.FILE_KEY);

				DxfFile dxfFile = null;
				try {
					dxfFile = new DxfFile(selectedFile);
				} catch (DxfException e) {
					e.printStackTrace();
				}
                converter.convert(dxfFile);

                // TODO: Obtener FeatureCollections para cada Layer
                // Creamos las geometrias que pertenecen a capas (layers) definidas
                // en los esquemas de geopista (llamamos al conversor de geopista - GeopistaDxfConverter)
                for (Enumeration eLayers=converter.getLayers();eLayers.hasMoreElements();){
                    String sLayer=(String)eLayers.nextElement();
                    FeatureCollection fc=converter.getFeatureCollection(sLayer);
                    createDataSourceQuery(solucion,fc,sLayer,selectedFile);
                }

                // Creamos las geometrias que no pertenecen a ninguna capa (layer) definida
                // en los esquemas de geopista (llamamos al conversor de agil - FeatureCollectionDxfConverter)
                Iterator pointsIterator = ((FeatureCollectionDxfConverter)converter).getPointFeatureCollection().iterator();
                createDataSourceQuery(solucion, pointsIterator, (FeatureCollectionDxfConverter)converter,selectedFile);

                Iterator linesIterator = converter.getLineFeatureCollection().iterator();
                createDataSourceQuery(solucion, linesIterator, (FeatureCollectionDxfConverter)converter,selectedFile);

                Iterator polygonsIterator = converter.getPolygonFeatureCollection().iterator();
                createDataSourceQuery(solucion, polygonsIterator, (FeatureCollectionDxfConverter)converter,selectedFile);

                Iterator textIterator = converter.getTextFeatureCollection().iterator();
                createDataSourceQuery(solucion, textIterator, (FeatureCollectionDxfConverter)converter,selectedFile);

			} //while
			return solucion;
		}
		
		public Collection getDataSourceQueries(InputStream fileDXF) {

			//aquí se guardaran todas las DataSourceQuery que se vayan creando
			ArrayList solucion = new ArrayList();
			GeopistaDxfConverter converter = new GeopistaDxfConverter();
			//Primero obtenemos los ficheros Dxf seleccionados
			//Collection dataSourceQuerysFile = super.getDataSourceQueries();
			//Iterator iterator = dataSourceQuerysFile.iterator();

			//while(iterator.hasNext()){
				//DataSourceQuery dataSourceQuery = (DataSourceQuery) iterator.next();
				//DataSource dataSource = dataSourceQuery.getDataSource();
				//final String selectedFile = (String)dataSource.getProperties().get(DataSource.FILE_KEY);

				DxfFile dxfFile = null;
				try {
					dxfFile = new DxfFile(fileDXF);
				} catch (DxfException e) {
					e.printStackTrace();
				}
                converter.convert(dxfFile);

                // TODO: Obtener FeatureCollections para cada Layer
                // Creamos las geometrias que pertenecen a capas (layers) definidas
                // en los esquemas de geopista (llamamos al conversor de geopista - GeopistaDxfConverter)
                for (Enumeration eLayers=converter.getLayers();eLayers.hasMoreElements();){
                    String sLayer=(String)eLayers.nextElement();
                    FeatureCollection fc=converter.getFeatureCollection(sLayer);
                    createDataSourceQuery(solucion,fc,sLayer,fileDXF);
                }

                // Creamos las geometrias que no pertenecen a ninguna capa (layer) definida
                // en los esquemas de geopista (llamamos al conversor de agil - FeatureCollectionDxfConverter)
                Iterator pointsIterator = ((FeatureCollectionDxfConverter)converter).getPointFeatureCollection().iterator();
                createDataSourceQuery(solucion, pointsIterator, (FeatureCollectionDxfConverter)converter,fileDXF);

                Iterator linesIterator = converter.getLineFeatureCollection().iterator();
                createDataSourceQuery(solucion, linesIterator, (FeatureCollectionDxfConverter)converter,fileDXF);

                Iterator polygonsIterator = converter.getPolygonFeatureCollection().iterator();
                createDataSourceQuery(solucion, polygonsIterator, (FeatureCollectionDxfConverter)converter,fileDXF);

                Iterator textIterator = converter.getTextFeatureCollection().iterator();
                createDataSourceQuery(solucion, textIterator, (FeatureCollectionDxfConverter)converter,fileDXF);

			//} //while
                orderLayersByName(solucion);
			return solucion;
		}
		
		public Collection getDataSourceQueries(InputStream fileDXF, String pathSch, CoordinateSystem coord) {

			//aquí se guardaran todas las DataSourceQuery que se vayan creando
			ArrayList solucion = new ArrayList();			
			
			GeopistaDxfConverter converter = new GeopistaDxfConverter();
			
			final String ext = "." + "sch";                
			File dir = new File(pathSch);
			FilenameFilter filter = new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					return name.endsWith(ext);
				}
			};

			File[] files = dir.listFiles(filter);
			
			HashMap table = new HashMap();
			
			for (int i = 0; i < files.length; i++)
			{
				String nameLayer = files[i].getName().split(".sch")[0];
								
				StringBuffer sbXml = new StringBuffer();

				BufferedReader bufferReader;
				try {
					
					InputStream is = new FileInputStream(files[i]);
		        	
		        	String stringReader = FileUtil.parseISToStringUTF8(is);
		        						
					GeopistaSchema schema=(GeopistaSchema)new XML2Java().read(stringReader,GeopistaSchema.class);
					schema.setCoordinateSystem(coord);
					converter.setLayer(nameLayer, schema);
										
				} catch (FileNotFoundException e) {
					e.printStackTrace();

				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			DxfFile dxfFile = null;
			try {
				dxfFile = new DxfFile(fileDXF);
			} catch (DxfException e) {
				e.printStackTrace();
			}
			converter.convert(dxfFile);

			for (Enumeration eLayers=converter.getLayers();eLayers.hasMoreElements();){
				String sLayer=(String)eLayers.nextElement();
				FeatureCollection fc=converter.getFeatureCollection(sLayer);
				createDataSourceQuery(solucion,fc,sLayer,fileDXF);
			}

			// Creamos las geometrias que no pertenecen a ninguna capa (layer) definida
			// en los esquemas de geopista (llamamos al conversor de agil - FeatureCollectionDxfConverter)
			Iterator pointsIterator = ((FeatureCollectionDxfConverter)converter).getPointFeatureCollection().iterator();
			createDataSourceQuery(solucion, pointsIterator, (FeatureCollectionDxfConverter)converter,fileDXF);

			Iterator linesIterator = converter.getLineFeatureCollection().iterator();
			createDataSourceQuery(solucion, linesIterator, (FeatureCollectionDxfConverter)converter,fileDXF);

			Iterator polygonsIterator = converter.getPolygonFeatureCollection().iterator();
			createDataSourceQuery(solucion, polygonsIterator, (FeatureCollectionDxfConverter)converter,fileDXF);

			Iterator textIterator = converter.getTextFeatureCollection().iterator();
			createDataSourceQuery(solucion, textIterator, (FeatureCollectionDxfConverter)converter,fileDXF);

			//} //while
			return solucion;
		}


		/**
		 * Vuelca en una coleccion los DataSourceQueryChooser construidos a partir de las
		 * FeatureCollection devueltas por el iterador.
		 *
		 * @param dataSourceQueryCollection
		 */
		private void createDataSourceQuery(ArrayList dataSourceQueryCollection,
										FeatureCollection featureCollection,String sLayer, String filePath){
				String layerName = sLayer;
				GeopistaLoadDxfQueryChooser.DataSourceWrapper dataSourceWrapper =
					 new GeopistaLoadDxfQueryChooser.DataSourceWrapper(featureCollection);
				DataSourceQuery dataSourceQueryWrapper =
					new DataSourceQuery(dataSourceWrapper, null, layerName);
				dataSourceQueryCollection.add(dataSourceQueryWrapper);
				HashMap properties= new HashMap();
				properties.put(DataSource.COORDINATE_SYSTEM_KEY,null);
				properties.put(Constantes.ORIGINAL_FILE_KEY,filePath);
				dataSourceWrapper.setProperties(properties);
		}
		
		private void createDataSourceQuery(ArrayList dataSourceQueryCollection,
				FeatureCollection featureCollection,String sLayer, InputStream filePath){
			String layerName = sLayer;
			GeopistaLoadDxfQueryChooser.DataSourceWrapper dataSourceWrapper =
				new GeopistaLoadDxfQueryChooser.DataSourceWrapper(featureCollection);
			DataSourceQuery dataSourceQueryWrapper =
				new DataSourceQuery(dataSourceWrapper, null, layerName);
			dataSourceQueryCollection.add(dataSourceQueryWrapper);
			HashMap properties= new HashMap();
			
			properties.put(DataSource.COORDINATE_SYSTEM_KEY,featureCollection.getFeatureSchema().getCoordinateSystem().getName());
			//properties.put(DataSource.COORDINATE_SYSTEM_KEY,null);
			//properties.put(SynchronizePlugIn.ORIGINAL_FILE_KEY,filePath);
			properties.put(Constantes.ORIGINAL_FILE_KEY,null);
			dataSourceWrapper.setProperties(properties);
		}
		
		


    /**
     * Vuelca en una coleccion los DataSourceQueryChooser construidos a partir de las
     * FeatureCollection devueltas por el iterador.
     *
     * @param dataSourceQueryCollection
     * @param featureCollectionIterator
     */
    /** Para cargar capas que no son de Geopista */
    private void createDataSourceQuery(ArrayList dataSourceQueryCollection,
                                    Iterator featureCollectionIterator, FeatureCollectionDxfConverter converter, String filePath){

        while(featureCollectionIterator.hasNext()){
            FeatureCollection featureCollection =
                (FeatureCollection) featureCollectionIterator.next();
            String layerName = converter.getFeatureCollectionName(featureCollection);
            DataSourceWrapper dataSourceWrapper =
                 new DataSourceWrapper(featureCollection);
            DataSourceQuery dataSourceQueryWrapper =
                new DataSourceQuery(dataSourceWrapper, null, layerName);
            dataSourceQueryCollection.add(dataSourceQueryWrapper);
            //TODO: Pasar un properties...
            HashMap properties= new HashMap();
            //properties.put(DataSource.COORDINATE_SYSTEM_KEY,null);
        	if (context!=null)
        		 properties.put(DataSource.COORDINATE_SYSTEM_KEY,
	            		context.getLayerManager().getCoordinateSystem().getName());
        	else
        		properties.put(DataSource.COORDINATE_SYSTEM_KEY,CoordinateSystem.UNSPECIFIED.getName());            
            properties.put(Constantes.ORIGINAL_FILE_KEY,filePath);
            dataSourceWrapper.setProperties(properties);
        }
    }
    /**/

    private void createDataSourceQuery(ArrayList dataSourceQueryCollection,
    		Iterator featureCollectionIterator, FeatureCollectionDxfConverter converter, InputStream filePath){

    	while(featureCollectionIterator.hasNext()){
    		FeatureCollection featureCollection =
    			(FeatureCollection) featureCollectionIterator.next();
    		String layerName = converter.getFeatureCollectionName(featureCollection);
    		DataSourceWrapper dataSourceWrapper =
    			new DataSourceWrapper(featureCollection);
    		DataSourceQuery dataSourceQueryWrapper =
    			new DataSourceQuery(dataSourceWrapper, null, layerName);
    		dataSourceQueryCollection.add(dataSourceQueryWrapper);
//  		TODO: Pasar un properties...
    		HashMap properties= new HashMap();
            //properties.put(DataSource.COORDINATE_SYSTEM_KEY,null);
        	if (context!=null)
       		 properties.put(DataSource.COORDINATE_SYSTEM_KEY,
	            		context.getLayerManager().getCoordinateSystem().getName());
        	else
        		properties.put(DataSource.COORDINATE_SYSTEM_KEY,CoordinateSystem.UNSPECIFIED.getName());            

    		//properties.put(SynchronizePlugIn.ORIGINAL_FILE_KEY,filePath);
    		properties.put(Constantes.ORIGINAL_FILE_KEY,null);
    		dataSourceWrapper.setProperties(properties);
    	}
    }
        
		public static class DataSourceWrapper extends DataSource{
			private Connection connection;
			private FeatureCollection featureCollection;

			public DataSourceWrapper(){
				this.connection = new Connection(){
					public FeatureCollection executeQuery(String query, Collection exceptions, TaskMonitor monitor) {
						try {
							 DxfReader reader=new DxfReader();
							 DriverProperties properties = new DriverProperties();
						     properties.putAll(getProperties());						     
							return reader.readSpecial(properties);
		                } catch (Exception e) {
		                    exceptions.add(e);

		                    FeatureSchema schema = new FeatureSchema();
		                    schema.addAttribute("Geometry", AttributeType.GEOMETRY);

		                    return new FeatureDataset(schema);
		                }
					}

					public FeatureCollection executeQuery(String query, TaskMonitor monitor) throws Exception {
						ArrayList exceptions = new ArrayList();
		                FeatureCollection featureCollection = executeQuery(query, exceptions, monitor);
		                if (!exceptions.isEmpty()) {
		                    throw (Exception) exceptions.iterator().next();
		                }
		                return featureCollection;
					}

					public ArrayList executeUpdate(String query, FeatureCollection featureCollection, TaskMonitor monitor) throws Exception {
						return null;
					}

					public void close() {
					}

				};
			}

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

		private void orderLayersByName(List lstLayers){
			
			Comparator comparator = new ComparatorLayers(); 
		    Collections.sort(lstLayers, comparator);
			
		}
		
		public static class ComparatorLayers 
		implements Comparator {
			public int compare(Object element1, 
					Object element2) {
				String lower1 = 
					((DataSourceQuery)element1).toString().toLowerCase();
				String lower2 = 
					((DataSourceQuery)element2).toString().toLowerCase();
				return lower1.compareTo(lower2);
			}
		}


}
