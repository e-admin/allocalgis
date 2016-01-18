/**
 * ReportService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.dwr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;

import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.reports.UtilsReport;
import com.geopista.app.eiel.reports.WebEIELReport;
import com.geopista.app.reports.JReportDatabase;
import com.geopista.app.reports.maps.MapImageFactory;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaMap;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.wms.WMSConfigurator;
import com.localgis.web.util.LayerUtils;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;

public class ReportService {

	private static Log logger = LogFactory.getLog(ReportService.class);
	
    public String showReport(String idLayers,String idFeatures,String idEntidad, String idMunicipio,String idPlantilla,String format,String locale) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
    	HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
       
        
        logger.info("Identificador de municipio para informes:"+idMunicipio);
        
        byte [] output=null;
        
        //Para multiples capas la información se enviaría de esta forma, separadas por comas.
        //idLayers="CC_TC,CC_TC";
        //idFeatures="6802,6810";
        		
        output=getReport(idMunicipio,idEntidad,idLayers,idFeatures,idPlantilla,format,locale);
                       
        String reportName=saveInforme(output,idMunicipio,format);

        if (reportName==null)
        	throw new LocalgisDBException();
		                
		//Escribir
        //response.setContentLength(gpxOut.size());
        /*response.setContentType("application/x-file-download");
        String filename = idLayers + ".pdf";
        filename = filename.replace(" ", "_");
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        
        ServletOutputStream outStream = response.getOutputStream();
        outStream.write(output);
        outStream.flush();
        */
        return reportName;
    }
    
    
    /**
     * Almacenamiento del informe en disco.
     * @param output
     * @param idMunicipio
     * @param format 
     * @return
     */
    private String saveInforme(byte[] output,String idMunicipio, String format){
        String reportName=null;
        try {
			String directorio=Configuration.getPropertyString("reports.directory.temp");
			File f=new File(directorio);
			
			if (!f.exists()) {
				boolean resultado=f.mkdirs();
				if (!resultado)
					logger.error("No se ha podido crear el directorio temporal para almacenar los informes");
			}
				
			deleteFilesOlderThanNdays(3,directorio,logger);
			
			if (format.equals("PDF"))
				reportName=directorio+File.separator+idMunicipio+".pdf";
			else if (format.equals("XML"))
				reportName=directorio+File.separator+idMunicipio+".xml";
			else
				reportName=directorio+File.separator+idMunicipio+".pdf";
			if (output!=null){
				FileOutputStream fileOuputStream =  new FileOutputStream(reportName); 							
				fileOuputStream.write(output);
				fileOuputStream.close();
			}
			else
				reportName=null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        catch (Throwable e) {
			e.printStackTrace();
		}
        return reportName;
    }
   

    
   
    
    private int getIdMap(String nombreMapa,String idMunicipio){
    	int idMap=-1;
		try {
			
			LocalgisMapsConfigurationManager localgisMapsConfigurationManager=LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
			GeopistaMap map=localgisMapsConfigurationManager.selectMapByName(nombreMapa, Integer.parseInt(idMunicipio));
			
			//DaoManager daoManager=LocalgisManagerBuilderFactory.getDaoManager();
			//GeopistaMapDAO geopistaMapDAO=(GeopistaMapDAO) daoManager.getDao(GeopistaMapDAO.class);
			//Recuperar idMunicpio
			//GeopistaMap map = (GeopistaMap) geopistaMapDAO.selectMapByName(nombreMapa, Integer.parseInt(idMunicipio));
			if (map!=null) {
				idMap=map.getIdMap();
			}else{
				logger.error("No se ha podido obtener el codigo de mapa:"+idMap);
			}
		} catch (LocalgisConfigurationException e) {
		} catch (LocalgisInitiationException e) {
		}
		catch (Exception e){	
			e.printStackTrace();
		}
		
        return idMap;
    }
    
    
    private byte[] getReport(String idMunicipio,String idEntidad,String idLayers,
    			String idFeatures,String idPlantilla,String format,String locale){
    	Connection conn=null;
    	byte[] output=null;
    	
    	Enumeration enDrivers = DriverManager.getDrivers();
		try {			
			
			
			String jrxmlfile=getPathPlantilla(idPlantilla,idEntidad);
			logger.info("Path Plantilla para idEntidad:"+idEntidad+" Entidad:"+idEntidad+"->"+jrxmlfile);
			
			File f=new File(jrxmlfile);
			String pathPadre=f.getParent();
			
			ConstantesLocalGISEIEL.SUREPORT_DIR=pathPadre+File.separator+"subreports"+File.separator;
			ConstantesLocalGISEIEL.IMAGE_DIR=pathPadre+File.separator+"img"+File.separator;
			ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL=pathPadre;
			/*ConstantesLocalGISEIEL.SUREPORT_DIR=directorioPlantillas+File.separator+"eiel"+File.separator+"subreports"+File.separator;
			ConstantesLocalGISEIEL.IMAGE_DIR=directorioPlantillas+File.separator+"eiel"+File.separator+"img"+File.separator;
			ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL=directorioPlantillas+File.separator+"eiel";
			String jrxmlfile=ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL+File.separator+"EIEL_equipamientos_EQ.jrxml";*/
			//listaFiltros.put("filtro_cc","and (eiel_t_cc.codprov='33' and eiel_t_cc.codmunic='001' and eiel_t_cc.clave='CC' and eiel_t_cc.codentidad='0401' and eiel_t_cc.codpoblamiento='01' and eiel_t_cc.orden_cc='017')");
			//listaFiltros.put("filtro_"+layerNameShorted.toLowerCase(),"and (eiel_c_cc.id=6802)");
			//listaFiltros.put("filtro_"+layerNameShorted.toLowerCase(),"and (eiel_c_ce.id=5384)");
		
			
			HashMap listaFiltros=new HashMap();
			String tipoElemento=null;
			
			//Si vienen varios elementos de la misma capa se genera un informe en el que los filtros se van añadiendo			
			String[] layersDisponibles=idLayers.split(",");
			String[] featuresDisponibles=idFeatures.split(",");
			
		
			//En el caso de que la capa tenga la cadena "TC" la eliminamos para buscar la categoria
			//asignada. Por ejemplo CC_TC se quedaria como CC y su categoria asignada es la EQ.
			String layerNameShorted=layersDisponibles[0];
			if (idLayers.contains("TC")){
				layerNameShorted=layersDisponibles[0].substring(0,layersDisponibles[0].indexOf("_TC"));
			}
			
			if (layerNameShorted.equals("TU"))
				layerNameShorted="IV";
			else if (layerNameShorted.equals("TEM"))
				layerNameShorted="EM";
			else if (layerNameShorted.equals("TCL"))
				layerNameShorted="CL";
			else if (layerNameShorted.equals("TCN"))
				layerNameShorted="CN";
			else if (layerNameShorted.equals("carreteras"))
				layerNameShorted="TC";
			else if (layerNameShorted.equals("TN"))
				layerNameShorted="TA";
			else if (layerNameShorted.equals("PL"))
				layerNameShorted="ALUM";

			
			LocalgisMapManager localgisMapManager=LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
			
			LocalgisLayer localgisLayer=localgisMapManager.getLayerByName(layersDisponibles[0]);
			
			//DaoManager daoManager=LocalgisManagerBuilderFactory.getDaoManager();
			//LocalgisLayerDAO localgisLayerDAO=(LocalgisLayerDAO) daoManager.getDao(LocalgisLayerDAO.class);
			//LocalgisLayer localgisLayer=localgisLayerDAO.selectLayerByName(layersDisponibles[0]);
		

			String[] datosCapa; datosCapa = LayerUtils.getTableNameFromLayer(localgisLayer);
			logger.debug("idCapa=" + localgisLayer + ", tabla asociada=" + datosCapa[0] + ", municipio=" + datosCapa[1]);
			
			String resultadoFiltro="";
			if (layerNameShorted.equals("AR")){
				resultadoFiltro="";
			}
			else{
				String tablaGeografica=datosCapa[0];
				
				StringBuffer filtro=new StringBuffer();
				filtro.append("and (");
				for (int i=0;i<featuresDisponibles.length;i++){
					filtro.append("("+tablaGeografica+".id="+featuresDisponibles[i]+") or ");
				}
				resultadoFiltro=filtro.toString().substring(0,filtro.toString().length()-3)+")";
			}
			listaFiltros.put("filtro_"+layerNameShorted,resultadoFiltro);
			listaFiltros.put("filtro_"+layerNameShorted.toLowerCase(),resultadoFiltro);

			tipoElemento=layerNameShorted;
			
				
			//Variables por defecto
			//String locale="es_ES";
			//No nos interesa filtrar por codigo de entidad
			String codEntidad="000";
			//No nos interesa filtrar por codigo de nucleos
			String codNucleo="000";
			//Seleccionamos todos los nucleos
			String nucleoSeleccionado="000_000_Todos";
			//No se imprimen imagenes
			String imprimirImagenes="Si";
			//No se obtiene el PNOA
			String usarWMSExternos="Si";
						
			
			int idMapaInformes1=getIdMap(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL_INFORMES,idMunicipio);
			int idMapaInformes2=getIdMap(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL,idMunicipio);
			
			
			MapImageFactory.setExecuteFromWeb(idMapaInformes1,idMapaInformes2,usarWMSExternos,
												LocalgisManagerBuilderSingleton.getInstance());
			
			conn=getDatabaseConnection();
			
			output=WebEIELReport.preGenerateReport(jrxmlfile,String.valueOf(idMunicipio),idEntidad,listaFiltros,tipoElemento,
					locale,codEntidad,codNucleo,nucleoSeleccionado,imprimirImagenes,usarWMSExternos,conn,format);
			System.out.println("Size====>"+output.length);
			
			//FileOutputStream fileOuputStream =  new FileOutputStream("C:\\tmp\\testing2.pdf"); 
		    //fileOuputStream.write(output);
		    //fileOuputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	 
		try{if (conn!=null)conn.close();}catch(Exception ex){}
		finally{
			registerDrivers(enDrivers);
		}
		return output;
    }
    
    private String getPathPlantilla(String idPlantilla,String idEntidad){
    	
    	String pahtPlantilla=null;
		try {
			
						
			ArrayList plantillas= new ArrayList();
			String directorioPlantillasGenericas=Configuration.getPropertyString("reports.plantillas.genericas");
			String directorioPlantillasEntidad=Configuration.getPropertyString("reports.plantillas.entidad")+File.separator+idEntidad;
			   
			logger.info("Directorio Plantillas Genericas:"+directorioPlantillasGenericas);
			logger.info("Directorio Plantillas Entidad:"+directorioPlantillasEntidad);
			
			if (idPlantilla.startsWith("("))
				UtilsReport.getNombresPlantillas(directorioPlantillasEntidad,null,null,idPlantilla,plantillas,idEntidad);
			else
				UtilsReport.getNombresPlantillas(directorioPlantillasGenericas,null,null,idPlantilla,plantillas);
		
			if (plantillas.size()>1){
				logger.error("Existen mas de una plantilla para el nombre indicado:"+idPlantilla);			
			}
			else if (plantillas.size()==1){
				Object[] obj=(Object[])plantillas.get(0);				
				pahtPlantilla=(String)obj[1]; 
			}
			else{
				logger.error("No se ha encontrado ninguna plantilla:"+idPlantilla);			
			}
					
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return pahtPlantilla;
	}
    
    /**
     * Se obtiene la conexion contra la Base de Datos
     * @return
     * @throws LocalgisConfigurationException
     */
    private Connection getDatabaseConnection() throws LocalgisConfigurationException{
    	
    	Connection conn=null;
    	String connectionType = Configuration.getPropertyString(Configuration.PROPERTY_DB_CONNECTIONTYPE);
        String dbHost = Configuration.getPropertyString(Configuration.PROPERTY_DB_HOST);
        String dbPort = Configuration.getPropertyString(Configuration.PROPERTY_DB_PORT);
        String dbName = Configuration.getPropertyString(Configuration.PROPERTY_DB_NAME);
        String dbUser =Configuration.getPropertyString(Configuration.PROPERTY_DB_USERNAME);
        String dbPassword =Configuration.getPropertyString(Configuration.PROPERTY_DB_PASSWORD);
        String driver="";
        
        String jdbcUrlConnection;
        int connectionTypeInt;
        if (connectionType.equals("postgis")) {
            jdbcUrlConnection = "jdbc:postgresql://"+dbHost+":"+dbPort+"/"+dbName;
            connectionTypeInt = WMSConfigurator.POSTGIS_CONNECTION;
            driver="org.postgresql.Driver";
        } else if (connectionType.equals("oraclespatial")) {
            jdbcUrlConnection = "jdbc:oracle:thin:@"+dbHost+":"+dbPort+":"+dbName;
            connectionTypeInt = WMSConfigurator.ORACLESPATIAL_CONNECTION;
            driver="oracle.jdbc.driver.OracleDriver";
        } else {
            logger.error("El parametro \""+ Configuration.PROPERTY_DB_CONNECTIONTYPE+"\" no tiene un valor válido. Valores validos \"postgis\" y \"oraclespatial\".");
            throw new LocalgisConfigurationException("El parametro \""+ Configuration.PROPERTY_DB_CONNECTIONTYPE+"\" no tiene un valor válido. Valores validos \"postgis\" y \"oraclespatial\".");
        }   		
		
		conn = JReportDatabase.getConnection(jdbcUrlConnection,dbUser,dbPassword,driver,false);
		
		return conn;
    }
		
	private void registerDrivers(Enumeration enDrivers){
		//Registramos los drivers que hubiera al principio ya que al generar
		//el reporte se eliminan en la clase MapImageFactory
		try {
			while (enDrivers.hasMoreElements()){
				Driver driver=(Driver)enDrivers.nextElement();
				DriverManager.registerDriver(driver);
			}
		} catch (Exception e) {
			e.printStackTrace();
	}
	}
    
    public static void deleteFilesOlderThanNdays(int daysBack, String dirWay, org.apache.commons.logging.Log log) {  
    	  
        File directory = new File(dirWay);  
        if(directory.exists()){  
  
            File[] listFiles = directory.listFiles();              
            long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);  
            for(File listFile : listFiles) {  
                if(listFile.lastModified() < purgeTime) {  
                    if(!listFile.delete()) {  
                        System.err.println("Unable to delete file: " + listFile);  
                    }  
                }  
            }  
        } else {  
            log.warn("Files were not deleted, directory " + dirWay + " does'nt exist!");  
        }  
    } 
    
}
