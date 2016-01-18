package es.satec.localgismobile.fw;

import java.io.File;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import es.satec.localgismobile.fw.utils.PropertiesReader;

public class Config {
	/**
	 * Nombre del fichero de properties específico con las propiedades de la aplicación
	 */
	public final static String LOCALGIS_PROPERTIES = "localgis.properties";
	
	/**
	 * Color de las parcelas cuando un usuario regustrado tiene los permisos
	 */
	public final static int COLOR_CUADRICULA = 0xFF00FFFF;
	public final static int OPACITY_CUADRICULA=80;
	
	/* Nombres de las propiedades */
	public final static String PROPERTY_LOCALE_LANGUAGE = "locale.language";
	public final static String PROPERTY_LOCALE_COUNTRY = "locale.country";
	public final static String PROPERTY_MESSAGES_PATH = "messagesbundle.path";
	
	public final static String PROPERTY_SERVER_PROTOCOL = "server.protocol";
	public final static String PROPERTY_SERVER_HOST = "server.host";
	public final static String PROPERTY_SERVER_PORT = "server.port";
	public final static String PROPERTY_SERVER_CONTEXT = "server.context";

	public final static String PROPERTY_GPS_PROPERTIES = "gps.properties";

	public final static String PROPERTY_LIST_MAPS_QUERY = "server.listMapsQuery";
	public final static String PROPERTY_DOWNLOAD_MAP_QUERY = "server.downloadMapQuery";
	public final static String PROPERTY_MAP_FILE = "proyectos.downloadedMapFileName";
	public final static String PROPERTY_SAVE_MAP_QUERY = "server.saveMapQuery";
	//public final static String PROPERTY_METAINFO_NUMBER_OF_FILES = "metainfo.numberOfFiles";
	
	public final static String PROPERTY_WMS = "wms.properties";
	public final static String PROJECT_PRJ= "project.prj";
	
	/**
	 * Color de la aplicación.
	 */
	public final static Color COLOR_APLICACION = new Color(Display.getCurrent(), new RGB(193,226,237));
	
	/**
	 * Nombre del fichero de properties con los recursos graficos
	 */
	public final static String RESOURCES_PROPERTIES = "resources.properties";

	public static final String DEFAULT_NUM_FICHEROS_LICENCIAS = "1";

	/**
	 * Fichero de properties con los parametros de configuracion
	 */
	public static PropertiesReader prLocalgis = new PropertiesReader(Global.CONFIG + File.separator + Config.LOCALGIS_PROPERTIES);

	/**
	 * Fichero de properties para los recursos graficos
	 */
	public static PropertiesReader prResources = new PropertiesReader(Global.CONFIG + File.separator + Config.RESOURCES_PROPERTIES);
}
