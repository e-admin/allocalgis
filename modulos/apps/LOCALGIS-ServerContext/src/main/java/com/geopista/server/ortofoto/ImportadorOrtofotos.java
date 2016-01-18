/**
 * ImportadorOrtofotos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.ortofoto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import com.geopista.consts.config.ConfigConstants;
import com.geopista.server.ServerContext;
import com.geopista.server.administradorCartografia.GeopistaConnection;
import com.geopista.server.administradorCartografia.GeopistaConnectionFactory;
import com.geopista.server.administradorCartografia.NewSRID;
import com.geopista.server.database.CPoolDatabase;

public class ImportadorOrtofotos {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ImportadorOrtofotos.class);
	
	// properties with all the configuration parameters
	//private static final String configurationFile = "config/importadorConfiguration.properties";
	
	//temp folder
	private static File tempFolder = null;
	
	private static File ortoDestFolder = null;
	
	/**
	 * 
	 * @param origin
	 * @param destination
	 * @throws IOException 
	 */
	private static void copyFile(String origin, String destination) 
		throws IOException {

	    FileInputStream from = null;
	    FileOutputStream to = null;
	    try {
	    	from = new FileInputStream(origin);
	    	to = new FileOutputStream(destination);
	    	byte[] buffer = new byte[4096];
	    	int bytesRead;

	    	while ((bytesRead = from.read(buffer)) != -1)
	    		to.write(buffer, 0, bytesRead);
	    } catch (Exception e) {
	    	logger.error("copyFile - Error copiando el archivo " + origin+" to "+ destination);
	    	throw new IOException("Error copiando el archivo " + origin+ " to "+destination);
	    } finally {
			if (from != null) {
				try {
					from.close();
				} catch (IOException e) {
					logger.error("copyFile - Error cerrando el archivo origen");
					throw new IOException("Error cerrando el archivo origen");
				}
			}
	        if (to != null) {
	        	try {
	        		to.close();
	        	} catch (IOException e) {
	        		logger.error("copyFile - Error cerrando el archivo destino");
	        		throw new IOException("Error cerrando el archivo destino");
	        	}
	        }
	    }
	}
	
	
	/**
	 * 
	 * @param folderPath
	 * @throws IOException
	 */
	private static void emptyFolder(String folderPath) 
		throws IOException {
		
		logger.debug("Inicio ImportadorOrtofotos.emptyFolder()...");
		File folder = new File(folderPath);
		File[] fileList = folder.listFiles();
		
		if (fileList != null) {
			for (int i=0;i<fileList.length;i++) {
				File file = fileList[i];
				if (file.isDirectory()) {
					emptyFolder(file.getAbsolutePath());
				} else {
					boolean success = file.delete();
					if (!success) {
						logger.error("emptyFolder - Error limpiando el directorio " + folderPath);
						throw new IOException("Error limpiando el directorio " + folderPath);
					}
				}
			}
		}
	}	
	
	/**
	 * 
	 * @param idMunicipio
     * @param srs
     * @param extension
	 * @throws Exception
	 */
	private static void publishToDB (String idMunicipio, String srs, String extension) 
		throws Exception {
		logger.debug("Inicio ImportadorOrtofotos.publishToDB()...");
		try {
			
			 Connection conn=CPoolDatabase.getConnection();
			 boolean dbOracle=false;
	            /*if (conn instanceof org.postgresql.PGConnection)
	                dbOracle=false;
	            else
	                dbOracle=true;*/
	            conn.close();
	            CPoolDatabase.releaseConexion();
			
//	        GeopistaConnection geoConn= GeopistaConnectionFactory.getInstance(new SRID(Const.SRID_PROPERTIES), dbOracle);
	        GeopistaConnection geoConn= GeopistaConnectionFactory.getInstance(new NewSRID(), dbOracle);
			String url = ortoDestFolder.getPath()+File.separator+"gvDesc.xml";
			int idName = geoConn.getNextDictionaryId();
			geoConn.insertCoverageLayer(new Integer(idMunicipio).intValue(), idName, url, srs, extension);
			geoConn.insertVocablo(idName, "es_ES", "Ortofoto."+idMunicipio);
		} catch (Exception e) {
			logger.error("publishToDB - Error al publicar en la base de datos",e);
			throw new Exception("Error al publicar en la base de datos");
		}
	}
	

	public static  void importar(String imageName, boolean hasWorldfile, String worldfileName, String epsg, String idMunicipio, String extension) 
		throws Exception {
		
		logger.debug("Inicio ImportadorOrtofotos.importar()...");
		
		//leemos los valores necesarios del archivo properties
		logger.info("Fichero de configuracion:"+ConfigConstants.PROPERTIES_NAME);
//        ClassLoader classLoader = ImportadorOrtofotos.class.getClassLoader();
//        Properties properties = new Properties();
//        properties.load(classLoader.getResourceAsStream(ConfigConstants.PROPERTIES_NAME));

//		String destination = properties.getProperty("ortofoto_destination_folder");
		String destination = ServerContext.getConfig().get(ConfigConstants.IMPORT_ORTOFOTO_DESTINATION_FOLDER);
		logger.info("Destination Folder:"+destination);

		tempFolder = new File(destination+File.separator+"temp");
		ortoDestFolder = new File(destination+File.separator+idMunicipio);
		tempFolder.mkdirs();
		ortoDestFolder.mkdirs();
		
		//vaciamos el directorio de destino 
		emptyFolder(ortoDestFolder.getPath());
		
		//Copiamos la imagen y el fichero de wordfile, si lo hay al directorio final donde se almacenan las ortofotos
        String outputImagefileName = idMunicipio+"."+extension;
        copyFile(tempFolder.getPath()+File.separator+imageName, ortoDestFolder.getPath()+File.separator+outputImagefileName);
		if (hasWorldfile) {
			//copiamos el worldfile al directorio final donde se almacenan las ortofotos. La extension wld es la apropiada para MapServer
	        String outputWorldfileName = idMunicipio+".wld";
			copyFile(tempFolder.getPath()+File.separator+worldfileName, ortoDestFolder.getPath()+File.separator+outputWorldfileName);
		}
			
		//System.out.println("Eliminando archivos temporales generados...");
		logger.warn("Eliminando archivos temporales generados...");
		emptyFolder(tempFolder.getPath());
		
		logger.warn("Archivos temporales eliminados.");
		//System.out.println("Archivos temporales eliminados.");
		
		//publicamos en la bd la nueva ortofoto del municipio
		logger.warn("Publicando en la BD...");
		publishToDB(idMunicipio, epsg, extension);
		
		System.gc();
	}

	
//	public static void main(String[] args)
//	{
//        
//		try {
//			importar(args[0], new Boolean(args[1]).booleanValue(), args[2], args[3], args[4]);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
