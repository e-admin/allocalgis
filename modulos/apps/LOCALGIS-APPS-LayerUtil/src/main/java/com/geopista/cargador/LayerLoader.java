/**
 * LayerLoader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.cargador;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.util.GeopistaCommonUtils;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class LayerLoader {
	private static Logger logger = Logger.getLogger("com.geopista.cargador");
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
	public LayerLoader(){
	   
	}
	
	
	/**
	 * Metodo que carga datos desde un fichero SHP dentro de una tabla en la BBDD. 
	 * 
	 * 
	 * @param iMunicipioCode:  el código municipio 
	 * @param sLayerName: el nombre de la capa
	 * @param sSHPFilePath: el path del fichero SHP. 
	 * @param urlAdministradorCartografia: la URL del administrador de cartografia que queremos usar. 
	 * 				(ej: http://localhost:8081/geopista/AdministradorCartografiaServlet)
	 * @param user usuario BBDD
	 * @param pass contraseña BBDD
	 * @return
	 */
	public boolean load(Integer iMunicipioCode, String sLayerName, String sSHPFilePath, URL urlAdministradorCartografia, String user, String pass) {

		// We need to check información entrada por el usuario (tiene que ser un numero de 5 números) .
		if (iMunicipioCode.intValue()<100000){
			// OK we have 5 characters
			aplicacion = (AppContext) AppContext.getApplicationContext();
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID, iMunicipioCode.intValue()+"");
		}else{
			System.err.println("El código pasado por el usuario no es válido. El código Municipio esta compuesto de 5 digitos. ");
			System.err.println("Se detiene la ejecución");
			return false;
		}
		
		
		//check file is existing
		if (!GeopistaCommonUtils.revisarDirectorio(sSHPFilePath)) {
			System.err.println("Error al leer el fichero SHP. No se ha encontrado el fichero especificado"); 
			return false;
		}
		
		
		//comprobar login
		if (!login(aplicacion,user,pass)){
			System.err.println("Error en la conexión con la BBDD.");
			return false;
		}
		
		logger.info("Usuario logeado");
		
		return this.loadCommon(iMunicipioCode, sLayerName, sSHPFilePath, urlAdministradorCartografia);
		
		/*GeopistaLayer myOriginalLayer=null;
		GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
		
		try{
			myOriginalLayer = (GeopistaLayer) geopistaEditor1.loadData(sSHPFilePath,sLayerName);
		
		} catch (Exception exp){
			exp.printStackTrace();
			return false;
        }
		
		
		FeatureSchema fileSchema = myOriginalLayer.getFeatureCollectionWrapper().getFeatureSchema();
		
		List listaLayer = myOriginalLayer.getFeatureCollectionWrapper().getFeatures();
		
		AdministradorCartografiaClient acClient =new AdministradorCartografiaClient (urlAdministradorCartografia.toString());

		GeopistaLayer localLayer = null;
		try{
			localLayer = acClient.loadLayer(sLayerName,"es_ES",null,FilterLeaf.equal("1",new Integer(1)));
		}catch(ACException e){
			System.err.println("La Capa " + sLayerName + " no existe en la base de datos.");
			System.err.println("Por favor, comprobar el nombre de la capa");
			return false;
		}catch (Exception e){
			System.err.println("Exception (1): " + e.getMessage());
			return false;
		}
		GeopistaSchema featureSchema = (GeopistaSchema) localLayer.getFeatureCollectionWrapper().getFeatureSchema();
		
		GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
		Map <String, Object> properties = new HashMap<String, Object>();
		serverDataSource.setProperties(properties);
		
		try {
			URL urlLayer = new URL("geopistalayer://default/" + ((GeopistaLayer) localLayer).getSystemId());
			DataSourceQuery dataSourceQuery = new DataSourceQuery();
			dataSourceQuery.setQuery(urlLayer.toString());
			dataSourceQuery.setDataSource(serverDataSource);
			localLayer.setDataSourceQuery(dataSourceQuery);
			LayerManager myLayerManager = new LayerManager();
			myLayerManager.addCategory(sLayerName);
			localLayer.setLayerManager(myLayerManager);
		}catch (MalformedURLException murle){
			System.err.println("La URL generada esta malformada.");
			return false;
		}catch (Exception e){
			System.err.println("Exception: " + e.getMessage());
			return false;
		}
		
		GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localLayer.getDataSourceQuery().getDataSource();
		Map driverProperties = geopistaServerDataSource.getProperties();
		
		TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion
                .getI18nString("GeopistaMostrarTramosVias.ImportandoDatos"));
        progressDialog.report(aplicacion
                .getI18nString("GeopistaMostrarTramosVias.ImportandoDatos"));
               
        
        Iterator itLayer = listaLayer.iterator();
        
        //Now we need to compare both schema (file and feature) to see if they are completely the same
        // if not, we just show an information to the user
        // first we read the feature Schema and compare it to the file Schema
        // and then we do exactly the same thing but the other way
        boolean bSameScheme = true;
        for (int i=0; i<featureSchema.getAttributeCount(); i++){
        	String sFeatureColumn = featureSchema.getAttributeName(i);
        	if (!sFeatureColumn.equalsIgnoreCase("id") && !sFeatureColumn.equalsIgnoreCase("id_municipio") && !sFeatureColumn.equalsIgnoreCase("GEOMETRY")){
	        	boolean bExistSameColumn = false;
	        	for (int j=0; j<fileSchema.getAttributeCount(); j++){
	        		String sFileColumn = fileSchema.getAttributeName(j);
	        		if (sFileColumn.equalsIgnoreCase(sFeatureColumn)){
	        			bExistSameColumn = true;
	        		}
	        	}
	        	if (!bExistSameColumn){
	        		bSameScheme = false;
	        	}
        	}
        }
        // if the first comparation has been realized and said that both schemes were equals, then
        // we do the second comparation
        if (bSameScheme){
        	for (int i=0; i<fileSchema.getAttributeCount(); i++){
        		String sFileColumn = fileSchema.getAttributeName(i);
        		if (!sFileColumn.equalsIgnoreCase("id") && !sFileColumn.equalsIgnoreCase("id_municipio") && !sFileColumn.equalsIgnoreCase("GEOMETRY")){
	        		boolean bExistSameColumn = false;
	        		for (int j=0; j<featureSchema.getAttributeCount(); j++){
	        			String sFeatureColumn = featureSchema.getAttributeName(j);
	        			if (sFeatureColumn.equalsIgnoreCase(sFileColumn)){
	        				bExistSameColumn = true;
	        			}
	        		}
	        		if (!bExistSameColumn){
	        			bSameScheme = false;
	        		}
        		}
        	}
        }
        
        // if bSameScheme is true this means both schemes are equals
        if (!bSameScheme){
        	logger.info("WARNING: La estructura de la capa " + sLayerName + " es diferente a la estructura del fichero \"" + sSHPFilePath + "\"");
        }
        
        
        
        while (itLayer.hasNext()){
        	boolean bExistSimilarities = false;
        	Feature fileFeature = (Feature) itLayer.next();
        	GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);
        	currentFeature.setGeometry(fileFeature.getGeometry());

        	for (int i=0; i<featureSchema.getAttributeCount(); i++){
        		String sColumnName = featureSchema.getAttributeName(i);
        		
        		// esquema y featureSchema might have a different orden so 
        		// we need to be sure that we match the same fields
        		// Now we need to get the index of the sColumnName column in the File Esquema
        		if (!sColumnName.equals("id_municipio")){
        			
        			int iFileAttributeIndex = -1;
        			for (int j=0; j<fileSchema.getAttributeCount(); j++){
        				if (fileSchema.getAttributeName(j).equals(sColumnName)){
        					iFileAttributeIndex = j;
        					break;
        				}
        			}
        			
	        		AttributeType myAttributeType = featureSchema.getAttributeType(i);
	        		String sClass = myAttributeType.toString();	        		
	        		if (!sClass.equals("GEOMETRY") && iFileAttributeIndex!=-1){
	        			//Comprobamos que al menos un campo (otro que id_municipio y geometria) sea común entre el shapefile y la estructura de la capa. 
	        			currentFeature.setAttribute(sColumnName,fileFeature.getAttribute(iFileAttributeIndex));
	        			bExistSimilarities = true;
	        		}
        		}else{
        			currentFeature.setAttribute(sColumnName,iMunicipioCode);
        		}
        	}
        	if (bExistSimilarities){
				((GeopistaFeature) currentFeature).setLayer(localLayer);
				localLayer.getLayerManager().setFiringEvents(false);
				localLayer.getFeatureCollectionWrapper().add(currentFeature);
        	}else{
        		System.err.println("No existe Coincidencias entre la capa referencia y los datos recogidos. ");
        		return false;
        	}
        }
        
		try{
			driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,new Boolean(true));
			geopistaServerDataSource.getConnection().executeUpdate(localLayer.getDataSourceQuery().getQuery(),localLayer.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
			
		}catch (Exception e){
			System.err.println("No se ha podido grabar los datos en la bbdd. ");
			System.err.println("Exception: " + e.getMessage());
			return false;
		}
		
		
		return true;*/
	}
	
	
	/**
	 * Metodo que carga datos desde un fichero SHP dentro de una tabla en la BBDD. 
	 * Only difference with the other load method is that application comes from outside
	 * 
	 * 
	 * @param iMunicipioCode:  el código municipio 
	 * @param sLayerName: el nombre de la capa
	 * @param sSHPFilePath: el path del fichero SHP. 
	 * @param urlAdministradorCartografia: la URL del administrador de cartografia que queremos usar. 
	 * 				(ej: http://localhost:8081/geopista/AdministradorCartografiaServlet)
	 * @param user usuario BBDD
	 * @param pass contraseña BBDD
	 * @return
	 */
	
	public boolean load(AppContext application, Integer iMunicipioCode, String sLayerName, String sSHPFilePath, URL urlAdministradorCartografia, String user, String pass) {
		boolean bNeedRelogin = false;
		
		// We need to check información entrada por el usuario (tiene que ser un numero de 5 números) .
		if (iMunicipioCode.intValue()<100000){
			// OK we have 5 characters
			aplicacion = application;
			String sCodigoMunicipio = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID, "0", false);
			if (!sCodigoMunicipio.trim().equalsIgnoreCase(iMunicipioCode.intValue()+"")){
				logger.info("We need to log again");
				bNeedRelogin = true;
				UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID, iMunicipioCode.intValue()+"");
			}
			
		}else{
			System.err.println("El código pasado por el usuario no es válido. El código Municipio esta compuesto de 5 digitos. ");
			System.err.println("Se detiene la ejecución");
			return false;
		}
		
		
		//check file is existing
		if (!GeopistaCommonUtils.revisarDirectorio(sSHPFilePath)) {
			System.err.println("Error al leer el fichero SHP. No se ha encontrado el fichero especificado"); 
			return false;
		}
		
		
		//comprobar login
		if (bNeedRelogin){
			logger.info("Relogin");
			if (!login(aplicacion,user,pass)){
				System.err.println("Error en la conexión con la BBDD.");
				return false;
			}
		}
		
		logger.info("Usuario logeado");
		
		return this.loadCommon(iMunicipioCode, sLayerName, sSHPFilePath, urlAdministradorCartografia);
		
		/*GeopistaLayer myOriginalLayer=null;
		GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
		
		try{
			myOriginalLayer = (GeopistaLayer) geopistaEditor1.loadData(sSHPFilePath,sLayerName);
		
		} catch (Exception exp){
			exp.printStackTrace();
			return false;
        }
		
		
		FeatureSchema fileSchema = myOriginalLayer.getFeatureCollectionWrapper().getFeatureSchema();
		
		List listaLayer = myOriginalLayer.getFeatureCollectionWrapper().getFeatures();
		
		AdministradorCartografiaClient acClient =new AdministradorCartografiaClient (urlAdministradorCartografia.toString());

		GeopistaLayer localLayer = null;
		try{
			localLayer = acClient.loadLayer(sLayerName,"es_ES",null,FilterLeaf.equal("1",new Integer(1)));
		}catch(ACException e){
			System.err.println("La Capa " + sLayerName + " no existe en la base de datos.");
			System.err.println("Por favor, comprobar el nombre de la capa");
			return false;
		}catch (Exception e){
			System.err.println("Exception (1): " + e.getMessage());
			return false;
		}
		GeopistaSchema featureSchema = (GeopistaSchema) localLayer.getFeatureCollectionWrapper().getFeatureSchema();
		
		GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
		Map <String, Object> properties = new HashMap<String, Object>();
		serverDataSource.setProperties(properties);
		
		try {
			URL urlLayer = new URL("geopistalayer://default/" + ((GeopistaLayer) localLayer).getSystemId());
			DataSourceQuery dataSourceQuery = new DataSourceQuery();
			dataSourceQuery.setQuery(urlLayer.toString());
			dataSourceQuery.setDataSource(serverDataSource);
			localLayer.setDataSourceQuery(dataSourceQuery);
			LayerManager myLayerManager = new LayerManager();
			myLayerManager.addCategory(sLayerName);
			localLayer.setLayerManager(myLayerManager);
		}catch (MalformedURLException murle){
			System.err.println("La URL generada esta malformada.");
			return false;
		}catch (Exception e){
			System.err.println("Exception: " + e.getMessage());
			return false;
		}
		
		GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localLayer.getDataSourceQuery().getDataSource();
		Map driverProperties = geopistaServerDataSource.getProperties();
		
		TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion
                .getI18nString("GeopistaMostrarTramosVias.ImportandoDatos"));
        progressDialog.report(aplicacion
                .getI18nString("GeopistaMostrarTramosVias.ImportandoDatos"));
               
        
        Iterator itLayer = listaLayer.iterator();
        
        //Now we need to compare both schema (file and feature) to see if they are completely the same
        // if not, we just show an information to the user
        // first we read the feature Schema and compare it to the file Schema
        // and then we do exactly the same thing but the other way
        boolean bSameScheme = true;
        for (int i=0; i<featureSchema.getAttributeCount(); i++){
        	String sFeatureColumn = featureSchema.getAttributeName(i);
        	if (!sFeatureColumn.equalsIgnoreCase("id") && !sFeatureColumn.equalsIgnoreCase("id_municipio") && !sFeatureColumn.equalsIgnoreCase("GEOMETRY")){
	        	boolean bExistSameColumn = false;
	        	for (int j=0; j<fileSchema.getAttributeCount(); j++){
	        		String sFileColumn = fileSchema.getAttributeName(j);
	        		if (sFileColumn.equalsIgnoreCase(sFeatureColumn)){
	        			bExistSameColumn = true;
	        		}
	        	}
	        	if (!bExistSameColumn){
	        		bSameScheme = false;
	        	}
        	}
        }
        // if the first comparation has been realized and said that both schemes were equals, then
        // we do the second comparation
        if (bSameScheme){
        	for (int i=0; i<fileSchema.getAttributeCount(); i++){
        		String sFileColumn = fileSchema.getAttributeName(i);
        		if (!sFileColumn.equalsIgnoreCase("id") && !sFileColumn.equalsIgnoreCase("id_municipio") && !sFileColumn.equalsIgnoreCase("GEOMETRY")){
	        		boolean bExistSameColumn = false;
	        		for (int j=0; j<featureSchema.getAttributeCount(); j++){
	        			String sFeatureColumn = featureSchema.getAttributeName(j);
	        			if (sFeatureColumn.equalsIgnoreCase(sFileColumn)){
	        				bExistSameColumn = true;
	        			}
	        		}
	        		if (!bExistSameColumn){
	        			bSameScheme = false;
	        		}
        		}
        	}
        }
        
        // if bSameScheme is true this means both schemes are equals
        if (!bSameScheme){
        	logger.info("WARNING: La estructura de la capa " + sLayerName + " es diferente a la estructura del fichero \"" + sSHPFilePath + "\"");
        }
        
        
        
        while (itLayer.hasNext()){
        	boolean bExistSimilarities = false;
        	Feature fileFeature = (Feature) itLayer.next();
        	GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);
        	currentFeature.setGeometry(fileFeature.getGeometry());

        	for (int i=0; i<featureSchema.getAttributeCount(); i++){
        		String sColumnName = featureSchema.getAttributeName(i);
        		
        		// esquema y featureSchema might have a different orden so 
        		// we need to be sure that we match the same fields
        		// Now we need to get the index of the sColumnName column in the File Esquema
        		if (!sColumnName.equals("id_municipio")){
        			
        			int iFileAttributeIndex = -1;
        			for (int j=0; j<fileSchema.getAttributeCount(); j++){
        				if (fileSchema.getAttributeName(j).equals(sColumnName)){
        					iFileAttributeIndex = j;
        					break;
        				}
        			}
        			
	        		AttributeType myAttributeType = featureSchema.getAttributeType(i);
	        		String sClass = myAttributeType.toString();	        		
	        		if (!sClass.equals("GEOMETRY") && iFileAttributeIndex!=-1){
	        			//Comprobamos que al menos un campo (otro que id_municipio y geometria) sea común entre el shapefile y la estructura de la capa. 
	        			currentFeature.setAttribute(sColumnName,fileFeature.getAttribute(iFileAttributeIndex));
	        			bExistSimilarities = true;
	        		}
        		}else{
        			currentFeature.setAttribute(sColumnName,iMunicipioCode);
        		}
        	}
        	if (bExistSimilarities){
				((GeopistaFeature) currentFeature).setLayer(localLayer);
				localLayer.getLayerManager().setFiringEvents(false);
				localLayer.getFeatureCollectionWrapper().add(currentFeature);
        	}else{
        		System.err.println("No existe Coincidencias entre la capa referencia y los datos recogidos. ");
        		return false;
        	}
        }
        
		try{
			driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,new Boolean(true));
			geopistaServerDataSource.getConnection().executeUpdate(localLayer.getDataSourceQuery().getQuery(),localLayer.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
			
		}catch (Exception e){
			System.err.println("No se ha podido grabar los datos en la bbdd. ");
			System.err.println("Exception: " + e.getMessage());
			return false;
		}
		
		
		return true;*/
	}
	
	
	private boolean loadCommon(Integer iMunicipioCode, String sLayerName, String sSHPFilePath, URL urlAdministradorCartografia){
		
		GeopistaLayer myOriginalLayer=null;
		GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
		
		try{
			myOriginalLayer = (GeopistaLayer) geopistaEditor1.loadData(sSHPFilePath,sLayerName);
		
		} catch (Exception exp){
			exp.printStackTrace();
			return false;
        }
		
		
		FeatureSchema fileSchema = myOriginalLayer.getFeatureCollectionWrapper().getFeatureSchema();
		
		List listaLayer = myOriginalLayer.getFeatureCollectionWrapper().getFeatures();
		
		AdministradorCartografiaClient acClient =new AdministradorCartografiaClient (urlAdministradorCartografia.toString());

		GeopistaLayer localLayer = null;
		try{
			localLayer = acClient.loadLayer(sLayerName,"es_ES",null,FilterLeaf.equal("1",new Integer(1)));
		}catch(ACException e){
			System.err.println("La Capa " + sLayerName + " no existe en la base de datos.");
			System.err.println("Por favor, comprobar el nombre de la capa");
			return false;
		}catch (Exception e){
			System.err.println("Exception (1): " + e.getMessage());
			return false;
		}
		GeopistaSchema featureSchema = (GeopistaSchema) localLayer.getFeatureCollectionWrapper().getFeatureSchema();
		
		GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
		Map <String, Object> properties = new HashMap<String, Object>();
		serverDataSource.setProperties(properties);
		
		try {
			URL urlLayer = new URL("geopistalayer://default/" + ((GeopistaLayer) localLayer).getSystemId());
			DataSourceQuery dataSourceQuery = new DataSourceQuery();
			dataSourceQuery.setQuery(urlLayer.toString());
			dataSourceQuery.setDataSource(serverDataSource);
			localLayer.setDataSourceQuery(dataSourceQuery);
			LayerManager myLayerManager = new LayerManager();
			myLayerManager.addCategory(sLayerName);
			localLayer.setLayerManager(myLayerManager);
		}catch (MalformedURLException murle){
			System.err.println("La URL generada esta malformada.");
			return false;
		}catch (Exception e){
			System.err.println("Exception: " + e.getMessage());
			return false;
		}
		
		GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localLayer.getDataSourceQuery().getDataSource();
		Map driverProperties = geopistaServerDataSource.getProperties();
		
		TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion
                .getI18nString("GeopistaMostrarTramosVias.ImportandoDatos"));
        progressDialog.report(aplicacion
                .getI18nString("GeopistaMostrarTramosVias.ImportandoDatos"));
               
        
        Iterator itLayer = listaLayer.iterator();
        
        //Now we need to compare both schema (file and feature) to see if they are completely the same
        // if not, we just show an information to the user
        // first we read the feature Schema and compare it to the file Schema
        // and then we do exactly the same thing but the other way
        boolean bSameScheme = true;
        for (int i=0; i<featureSchema.getAttributeCount(); i++){
        	String sFeatureColumn = featureSchema.getAttributeName(i);
        	if (!sFeatureColumn.equalsIgnoreCase("id") && !sFeatureColumn.equalsIgnoreCase("id_municipio") && !sFeatureColumn.equalsIgnoreCase("GEOMETRY")){
	        	boolean bExistSameColumn = false;
	        	for (int j=0; j<fileSchema.getAttributeCount(); j++){
	        		String sFileColumn = fileSchema.getAttributeName(j);
	        		if (sFileColumn.equalsIgnoreCase(sFeatureColumn)){
	        			bExistSameColumn = true;
	        		}
	        	}
	        	if (!bExistSameColumn){
	        		bSameScheme = false;
	        	}
        	}
        }
        // if the first comparation has been realized and said that both schemes were equals, then
        // we do the second comparation
        if (bSameScheme){
        	for (int i=0; i<fileSchema.getAttributeCount(); i++){
        		String sFileColumn = fileSchema.getAttributeName(i);
        		if (!sFileColumn.equalsIgnoreCase("id") && !sFileColumn.equalsIgnoreCase("id_municipio") && !sFileColumn.equalsIgnoreCase("GEOMETRY")){
	        		boolean bExistSameColumn = false;
	        		for (int j=0; j<featureSchema.getAttributeCount(); j++){
	        			String sFeatureColumn = featureSchema.getAttributeName(j);
	        			if (sFeatureColumn.equalsIgnoreCase(sFileColumn)){
	        				bExistSameColumn = true;
	        			}
	        		}
	        		if (!bExistSameColumn){
	        			bSameScheme = false;
	        		}
        		}
        	}
        }
        
        // if bSameScheme is true this means both schemes are equals
        if (!bSameScheme){
        	logger.info("WARNING: La estructura de la capa " + sLayerName + " es diferente a la estructura del fichero \"" + sSHPFilePath + "\"");
        }
        
        
        
        while (itLayer.hasNext()){
        	boolean bExistSimilarities = false;
        	Feature fileFeature = (Feature) itLayer.next();
        	GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);
        	currentFeature.setGeometry(fileFeature.getGeometry());

        	for (int i=0; i<featureSchema.getAttributeCount(); i++){
        		String sColumnName = featureSchema.getAttributeName(i);
        		
        		// esquema y featureSchema might have a different orden so 
        		// we need to be sure that we match the same fields
        		// Now we need to get the index of the sColumnName column in the File Esquema
        		if (!sColumnName.equals("id_municipio")){
        			
        			int iFileAttributeIndex = -1;
        			for (int j=0; j<fileSchema.getAttributeCount(); j++){
        				if (fileSchema.getAttributeName(j).equals(sColumnName)){
        					iFileAttributeIndex = j;
        					break;
        				}
        			}
        			
	        		AttributeType myAttributeType = featureSchema.getAttributeType(i);
	        		String sClass = myAttributeType.toString();	        		
	        		if (!sClass.equals("GEOMETRY") && iFileAttributeIndex!=-1){
	        			//Comprobamos que al menos un campo (otro que id_municipio y geometria) sea común entre el shapefile y la estructura de la capa. 
	        			currentFeature.setAttribute(sColumnName,fileFeature.getAttribute(iFileAttributeIndex));
	        			bExistSimilarities = true;
	        		}
        		}else{
        			currentFeature.setAttribute(sColumnName,iMunicipioCode);
        		}
        	}
        	
        	//puede ocurrir el caso en el que el fileSchema sólo tenga geometría, en ese casi igualamos el bExistSimilarities a true
        	
        	if(fileSchema.getAttributeCount()==1)
        		if(fileSchema.getAttributeType(0).toString().endsWith("GEOMETRY"))
        			bExistSimilarities=true;
        	
        	
        	if (bExistSimilarities){
				((GeopistaFeature) currentFeature).setLayer(localLayer);
				localLayer.getLayerManager().setFiringEvents(false);
				localLayer.getFeatureCollectionWrapper().add(currentFeature);
        	}else{
        		System.err.println("No existe Coincidencias entre la capa referencia y los datos recogidos. ");
        		return false;
        	}
        }
        
		try{
			driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,new Boolean(true));
			geopistaServerDataSource.getConnection().executeUpdate(localLayer.getDataSourceQuery().getQuery(),localLayer.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
			
		}catch (Exception e){
			System.err.println("No se ha podido grabar los datos en la bbdd. ");
			System.err.println("Exception: " + e.getMessage());
			return false;
		}
		
		
		return true;
	}
	
	
	/**
	 * Login into the system.
	 * @param aplicacion 
	 * @param user
	 * @param pass
	 */
	public static boolean login(AppContext aplicacion, String user, String pass) {
		// TODO Auto-generated method stub
		
		//comprobar login
		if (aplicacion.isLogged()){
			logger.info("Application is logged, we log out");
			aplicacion.logout();
		}
		logger.info("Logged out");
		
		if (aplicacion.isLogged()){	
			aplicacion.relogin(user, pass);
			
		}		
		//comprobar login
		if (!aplicacion.isLogged()) {
			if (!aplicacion.loginNoGrafico(user, pass)){
				logger.info("Error al autenticar al usuario");
				return false;
			}
		}
		if (!aplicacion.isLogged()) {
			logger.info("Error en la conexión con la BBDD.");
			System.exit(1);
		}	
		return true;
	}
	
}