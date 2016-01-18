/**
 * ScalableShapefileQueryChooser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 26-mar-2004
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas derivadas
 * del uso de este software.
 */
package org.agil.core.jump.plugin;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;

import org.agil.core.dao.ShapeFileOnDemandDataAccesor;
import org.agil.core.feature.FeatureCollectionOnDemand;

import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.LoadFileDataSourceQueryChooser;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

/**
 * Panel que se mostrará cuando se selecciona el origen de datos
 * ShapefileScalable al abrir el dialogo Load DataSet...
 * 
 * @author alvaro zabala (AGIL)
 */
public class ScalableShapefileQueryChooser extends LoadFileDataSourceQueryChooser {

	/**
	 * @param dataSourceClass ScalableShapefileReader que nos permite
	 * construir FeatureCollection que accede a los ficheros shape seleccionados
	 * 
	 * @param description descripcion del origen 
	 * 
	 * @param extensions file extensions associated to shapefile (shp)
	 */
	public ScalableShapefileQueryChooser(Class dataSourceClass, String description, String[] extensions, WorkbenchContext context) {
		super(dataSourceClass, description, extensions, context);
	}
	
	public Connection getConnection() {
		return ((DataSourceQuery)getDataSourceQueries().iterator().next()).getDataSource().getConnection();
	}

	
    /**
     * A partir de todos los ficheros seleccionados devuelve una coleccion
     * de DataSourceQuery (un DataSourceQuery es una consulta sobre un DataSource)
     */
	public Collection getDataSourceQueries() {
		Collection dataSourceQuerysFile = super.getDataSourceQueries();
		ArrayList solucion = new ArrayList();
	    Iterator iterator = dataSourceQuerysFile.iterator();
	    while(iterator.hasNext()){
			DataSourceQuery dataSourceQuery = (DataSourceQuery) iterator.next();
			DataSource dataSource = dataSourceQuery.getDataSource();
	        final String selectedFile = (String)dataSource.getProperties().get(DataSource.FILE_KEY);
			
	        DataSource newSource = new DataSource(){
				public Connection getConnection() {
					return new Connection(){
						public FeatureCollection executeQuery(String query, Collection exceptions, TaskMonitor monitor) {
							return createFeatureCollectionFromSelection(selectedFile);
						}
						public FeatureCollection executeQuery(String query, TaskMonitor monitor) throws Exception {
							return createFeatureCollectionFromSelection(selectedFile);
						}
						/**
						 * TODO buscar algun ejemplo de uso de executeUpdate de una conexion
						 */
						public ArrayList executeUpdate(String query, FeatureCollection featureCollection, TaskMonitor monitor) throws Exception {
							return null;
						}
						public void close() {	
						}};
				}//getConection
			};
	        
	    	DataSourceQuery query = new DataSourceQuery(dataSource,null, selectedFile);
	    	solucion.add(query);
	    } //while
	    return solucion;
    }
    
    private FeatureCollectionOnDemand createFeatureCollectionFromSelection(String selectedFile){
			String shapeDirectory = null;
			//los indices espaciales deben residir en el mismo directorio que los shapes
			int lastSlash = selectedFile.lastIndexOf(File.separator);
			if(lastSlash == -1){
			  shapeDirectory = "./";
			}else{
			  shapeDirectory = selectedFile.substring(0,lastSlash);
			}  
			String shapePathWithoutExt = selectedFile.substring(0, selectedFile.length() - 4);
			ShapeFileOnDemandDataAccesor dataAccesor =
						new ShapeFileOnDemandDataAccesor(shapeDirectory,shapePathWithoutExt);
			FeatureSchema schema = dataAccesor.getShapefileSchema();
			dataAccesor.setSchema(schema);
			if(!dataAccesor.isSpatialIndexed()){
				boolean generar = askForSpatialIndex();
			  	if(generar){
					try {
						dataAccesor.createIndex();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}//try	
				}//if generar
			}//if isSpatialIndexed
			final FeatureCollectionOnDemand fC = new FeatureCollectionOnDemand(schema);
			fC.setDataAccesor(dataAccesor);
			return fC;
    }
    private MultiInputDialog askForSpatialIndexDialog = null;
    /**
     * Muestra un dialogo preguntando por la generacion de índices espaciales para el fichero
     * seleccionado.
     * @return Devuelve si el usuario pulsó aceptar o no
     */
    private boolean askForSpatialIndex(){
		  if(askForSpatialIndexDialog == null){
			askForSpatialIndexDialog = new MultiInputDialog(null, "Generación de Indices Espaciales", true);
			askForSpatialIndexDialog.setSideBarImage(new ImageIcon(IconLoader.icon("World2.gif").getImage()));
			askForSpatialIndexDialog.setSideBarDescription("Genera un fichero de índice espacial para el shapefile seleccionado.");
			askForSpatialIndexDialog.addLabel("El fichero seleccionado  \n no se encuentra indexado espacialmente.");
			askForSpatialIndexDialog.addLabel("¿Desea crear su fichero de indice? \n Esta operacion puede tomar varios minutos.");
			askForSpatialIndexDialog.setSize(600,500);
			Dimension screenSize = Toolkit.getDefaultToolkit().
			  getScreenSize();
			int xLocation = (int) (screenSize.width - askForSpatialIndexDialog.getWidth() / 2d);
			int yLocation = (int) (screenSize.height - askForSpatialIndexDialog.getHeight() / 2d);
			askForSpatialIndexDialog.setLocation(xLocation, yLocation);
		  }
		  askForSpatialIndexDialog.setVisible(true);
		  return askForSpatialIndexDialog.wasOKPressed();
    }
    
	

}
