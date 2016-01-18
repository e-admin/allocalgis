package es.satec.localgismobile.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Global {
	
	private static Logger log;
	
	public final static String PROP_GEOPISTA_CON_SERVER = "localgis.server.admcar";
	public final static String PROP_GEOPISTA_EIEL_ADMCAR_SERVER = "geopista.conexion.eiel.administradorcartografia";
		
	//ruta de subida de los ficheros
	public static String UPLOAD_PATH = System.getProperty("user.home") + File.separator + ".LOCALGIS" + File.separator + "uploadFiles" + File.separator;
	
	public static final String JAVA_SERIALIZATION = "java.io.ObjectOutputStream";
	public static final String JAVA_DESERIALIZATION = "java.io.ObjectInputStream";
	public static final String XML_SERIALIZATION = "es.satec.platmovilidad.serializator.XmlSerializator";
	public static final String XML_DESERIALIZATION = "es.satec.platmovilidad.serializator.XmlDeserializator";
	public static final String SYNC_CONFIG_PATH = "/WEB-INF/Sync-Config.xml";
	
	public static final String CONFIG = "config";
	
//	Campos necesarios para la configuracion del generador de historicos
	public static final boolean historico=false;
    public static final String PATH_CDR="/home/busgosu/test2/historico";
    public static final String PATH_CDR_PROCESADOS="/home/busgosu/test2/procesados";
    public static final String FORMATOFECHACDR = "EEE, d MMM yyyy HH:mm:ss";
    public static final int TIEMPOROTADO=1000;//sg
    public static final int TAMROTACIONCDR=5000;//kb
    public static final String SEPARATOR="|";
    public static final long DEFAULT_TIME =100; //sg
    public static final long DEFAULT_SIZE = 1000; //KB
    public static final String prefix = "TCS_CDR_";
    public static final String extension = ".cdr";
    public static String pathCompleto = "/home/busgosu/test2/historico";
    	    
    public static final String LOCALGIS_MOBILE_DS = "LocalGISMobileDS";
    
    static {
    	setLog4jConfig();
         log = Logger.getLogger(Global.class);
        //vamos a leer del fichero de configuracion el valor de todas sus propiedades
    	

         File uploadDir = new File(UPLOAD_PATH);
         if(!uploadDir.exists()){
        	 uploadDir.mkdirs();
         }
    	
    	log.debug("Configuracion del servidor cargada correctamente");
    }


    public static void setLog4jConfig() {
        System.out.println("Cargando log4j.properties.....");
        Properties propsFile = new Properties();
        try {
            InputStream is = Global.class.getClassLoader().getResourceAsStream("log4j.properties");
            if (is != null) {
                propsFile.load(is);
                PropertyConfigurator.configure(propsFile);
                System.out.println("log4j.properties cargado adecuadamente.");
            } else {
                System.out.println("[setLog4jConfig()] is es null");
            }

        } catch (Exception e) {
            System.out.println("[Global.setLog4jConfig()]: Error leyendo log4j.properties " + e.toString());
        }
    }
    

    
    /**
     * Convierte un stream de entrada a una byt array
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] convertStreamToByte(InputStream is) throws IOException{
    	byte[] buffer = new byte[1024];
    	ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    	byte[] result = new byte[0];
    	try{
	        int numBytes;
		    while ((numBytes = is.read(buffer)) > 0){
		    	bOut.write(buffer, 0, numBytes);
		    }
	    
	    	result = bOut.toByteArray();
    	}finally{		
	    	bOut.close();
	    	is.close();
    	}
    	return result;
    }
    
    /**
     * Convierte un stream de entrada a una byt array
     * @param is
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(InputStream is) throws IOException{
    	byte[] buffer = new byte[1024];
    	ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    	String result = "";
    	try{
	        int numBytes;
		    while ((numBytes = is.read(buffer)) > 0){
		    	bOut.write(buffer, 0, numBytes);
		    }
	    
	    	result = bOut.toString("UTF-8");
    	}finally{		
	    	bOut.close();
	    	is.close();
    	}
    	return result;
    }

}
