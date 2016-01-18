/*
 * Creado el 18-abr-2004
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */
package org.agil.core.jump.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.agil.core.jump.io.FeatureCollectionDxfConverter;
import org.agil.dxf.reader.DxfException;
import org.agil.dxf.reader.DxfFile;

import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.LoadFileDataSourceQueryChooser;

/**
 * Esta clase se encarga de cargar varias FeatureCollection (en funcion de las capas
 * y del tipo de geometria) para un fichero Dxf
 * 
 * 
 * @author Alvaro Zabala (AGIL)
 *
 */
public class DxfQueryChooser extends LoadFileDataSourceQueryChooser {
	
	
		/**
		 * @param dataSourceClass ScalableShapefileReader que nos permite
		 * construir FeatureCollection que accede a los ficheros shape seleccionados
		 * 
		 * @param description descripcion del origen 
		 * 
		 * @param extensions file extensions associated to shapefile (shp)
		 */
		public DxfQueryChooser(Class dataSourceClass, String description, 
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
		 * @todo ESTO SE ESTÁ LLAMANDO 2 VECES. CACHEAR
		 */
		public Collection getDataSourceQueries() {
			//aquí se guardaran todas las DataSourceQuery que se vayan creando
			ArrayList solucion = new ArrayList();
			FeatureCollectionDxfConverter converter = new FeatureCollectionDxfConverter();
			//Primero obtenemos los ficheros Dxf seleccionados
			Collection dataSourceQuerysFile = super.getDataSourceQueries();
			Iterator iterator = dataSourceQuerysFile.iterator();
			
			while(iterator.hasNext()){
				DataSourceQuery dataSourceQuery = (DataSourceQuery) iterator.next();
				DataSource dataSource = dataSourceQuery.getDataSource();
				final String selectedFile = (String)dataSource.
						getProperties().get(DataSource.FILE_KEY);
				DxfFile dxfFile = null;
				try {
					dxfFile = new DxfFile(selectedFile);
				} catch (DxfException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				converter.convert(dxfFile);
				
				Iterator pointsIterator = converter.
					getPointFeatureCollection().iterator();
				
				createDataSourceQuery(solucion, pointsIterator, converter);
				
				Iterator linesIterator = converter.
					getLineFeatureCollection().iterator();
					
				createDataSourceQuery(solucion, linesIterator, converter);
					
				Iterator polygonsIterator = converter.
					getPolygonFeatureCollection().iterator();
				
				createDataSourceQuery(solucion, polygonsIterator, converter);
					
				Iterator textIterator = converter.
					getTextFeatureCollection().iterator();
				
				createDataSourceQuery(solucion, textIterator, converter);
					
			} //while
			return solucion;
		}
		
		/**
		 * Vuelca en una coleccion los DataSourceQueryChooser construidos a partir de las
		 * FeatureCollection devueltas por el iterador.
		 * 
		 * @param dataSourceQueryCollection
		 * @param featureCollectionIterator
		 */
		private void createDataSourceQuery(ArrayList dataSourceQueryCollection, 
										Iterator featureCollectionIterator, FeatureCollectionDxfConverter converter){
		
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
				properties.put(DataSource.COORDINATE_SYSTEM_KEY,null);
				dataSourceWrapper.setProperties(properties);
			}//while points
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
