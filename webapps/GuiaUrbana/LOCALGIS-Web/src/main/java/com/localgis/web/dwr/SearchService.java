/**
 * SearchService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.dwr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.directwebremoting.WebContextFactory;



import com.localgis.web.config.LocalgisWebConfiguration;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.utils.ChangeCoordinateSystem;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;

import com.localgis.web.wfsg.domain.ElementEntity;
import com.localgis.web.wfsg.exceptions.ConnectionException;
import com.localgis.web.wfsg.exceptions.IncorrectArgumentException;
import com.localgis.web.wfsg.exceptions.InternalException;
import com.localgis.web.wfsg.exceptions.NoDataFoundException;

/**
 * Clase para acceder a los servicios DWR relacionados con las búsquedas
 * @author hugo
 *
 */
public class SearchService {
    private static Log logger = LogFactory.getLog(SearchService.class);
    /** Directorio del índice lucene*/
    private static String INDEX_DIR="c:/tmp/localgis/index";
    //private static String INDEX_DIR="/usr/local/LocalGIS/localgislayerindexer/index";

    public List getSearchInformation(String layerId, String termino, String idEntidad, String sourceCRSCode, String targetCRSCode ) throws IncorrectArgumentException, NoDataFoundException, ConnectionException, InternalException, LocalgisDBException, LocalgisInitiationException, LocalgisConfigurationException {
    	//Primero hay que traducir el layerID (guía urbana) a un idLayer de geopista
    	HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        logger.debug("SearchService "+request.getRemoteUser());
        LocalgisMapManager localgisMapManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
        
    	String idLayerGeopista="";
    	try {
    	   idLayerGeopista=localgisMapManager.getLayerIdFromIdGuiaUrbana(layerId);
    	   
    	}catch (Exception ex){
    		ex.printStackTrace();
    	}
    	//System.out.println("Buscando en índice "+INDEX_DIR+"/"+idEntidad+"/"+idLayerGeopista);
    	
    	

    	
    	//TODO el código de las búsquedas se coloca aquí y la respuesta tiene que ser un List de ElementEntity
    	List result = new ArrayList();    	
    

    	
    	//Analyzer an = new SimpleAnalyzer(Version.LUCENE_35);    	

    	try {
			//Query query = new QueryParser(Version.LUCENE_35, "searcheable", an).parse('*'+termino.toUpperCase()+'*');
    		
			
			
			String directorio=INDEX_DIR;
			try {
				directorio=LocalgisWebConfiguration.getPropertyString("INDEX_DIR");
			}catch(Exception ex){

			}
			
			result=search(directorio,idEntidad,idLayerGeopista,termino,sourceCRSCode, targetCRSCode);
			
		} catch (Exception e) {
			logger.error("Error CorruptIndexException: " + e.getMessage(), e);
		}    	
    	
//    	ElementEntity item = new ElementEntity();
//   	item.setPosX("22,99");
//    	item.setPosY("33,10");
//    	item.setName("Elemento de prueba");
//    	result.add(item);
    	return result;
    }
    
    
    public List search(String directorio,String idEntidad,String idLayerGeopista, String termino,String sourceCRSCode,String targetCRSCode){
    	
    	List result = new ArrayList();  
    	IndexSearcher searcher=null;
    	IndexReader reader=null;
    	
    	try {
			Analyzer an = new StandardAnalyzer(Version.LUCENE_35);    
			
			Query query = new QueryParser(Version.LUCENE_35, "searcheable", an).parse(termino.toUpperCase());
			logger.debug("Query: " + query.toString());
			System.out.println("Query:"+query.toString());
			
			logger.debug("Usando directorio índice: "+directorio+"/"+idEntidad+"/"+idLayerGeopista);
			System.out.println("Usando directorio índice: "+directorio+"/"+idEntidad+"/"+idLayerGeopista);
			Directory indexDir = new NIOFSDirectory(new File(directorio+"/"+idEntidad+"/"+idLayerGeopista));
			/*
			LocalgisMap localgisMap;
			LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();
			LocalgisMapsConfigurationManager localgisMapsConfigurationManager = localgisManagerBuilder.getLocalgisMapsConfigurationManager();
			String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
			//localgisMap = localgisMapsConfigurationManager.getDefaultMap(new Integer(idEntidad), new Boolean(configurationLocalgisWeb.equals("public")));
			localgisMap = localgisMapManager.getPublishedMap(new Integer(idMap));
			*/
			if ((IndexReader.indexExists(indexDir))) {
				reader=IndexReader.open(indexDir);
				searcher = new IndexSearcher(reader);
								
				ScoreDoc[] hits;
				
				hits = searcher.search(query, 100).scoreDocs;
									
				logger.info("Found " + hits.length + " hits.");
				System.out.println("Found " + hits.length + " hits.");

				for(ScoreDoc currentHit : hits) {
					int docId = currentHit.doc;
					Document d = searcher.doc(docId);
					//log.debug((i + 1) + ". " + d.get("idMensaje") + " - "
					//		+ d.get("asunto"));
					
					//String srid=localgisMap.getOriginalSrid();
					
					//Strin targetCRSCode=Configuration.getPropertyString(Configuration.PROPERTY_DISPLAYPROJECTION);
					
					//Debido a que la Base de Datos ya realiza la transformación de coordenadas no es necesario realizar una adaptación
					//de las coordenadas como cuando se realizaba anteriormente. Ahora simplemente devolvemos los datos tal cual
					
					//double[] transformedCoordinates=ChangeCoordinateSystem.transformSpecial(sourceCRSCode,targetCRSCode,new double[] {Double.parseDouble(d.get("pos_x")),Double.parseDouble(d.get("pos_y"))});
					//double x=transformedCoordinates[0];
					//double y=transformedCoordinates[1];
					double x=Double.parseDouble(d.get("pos_x"));
					double y=Double.parseDouble(d.get("pos_y"));

					
			    	ElementEntity item = new ElementEntity();
			    	item.setPosX(String.valueOf(x));
			    	item.setPosY(String.valueOf(y));
			    	//item.setName(d.get("name"));
			    	item.setName(d.get("searcheable"));
			    	result.add(item);				
				}
			}
			else{
				System.out.println("Directorio de indice no encontrado");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (Throwable e1){
    		logger.error("Error al instanciar la clase",e1);
    	}
    	finally{
    		try {
				if (searcher!=null)
					searcher.close();
				if (reader!=null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		return result;
    }
    
    public static void main(String args[]){
    	String directorio="c:\\Program Files\\LocalGIS\\index";
    	//String layerid="12325";
    	String layerid="104";
    	String termino="002300*";
    	String idEntidad="77";
    	String  sourceCRSCode="23030";
    	String targetCRSCode="23030";    
    	try {    			  
			new SearchService().search(directorio,idEntidad,layerid,termino,sourceCRSCode,targetCRSCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    		       	
    }
}
