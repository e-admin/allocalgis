/**
 * Global.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw;


import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import es.satec.localgismobile.fw.utils.PropertiesReader;
import es.satec.localgismobile.fw.validation.bean.ValidationBean;

/**
 * Clase donde se definen las constantes.
 *  
 * @author marta
 * @version 1.0
 */

public class Global {
	
	/**
	 * Número de reintentos
	 */
	public static final int VECES = 3;

	public static String DB_PATH = "db\\";

	public static String DB_NAME = "csv.db";

	/**
	 * Directorio de trabajo de la aplicacion
	 */
	public static final String APP_PATH;

	public static final String JAVA_SERIALIZATION = "java.io.ObjectOutputStream";

	public static final String JAVA_DESERIALIZATION = "java.io.ObjectInputStream";

	/**
	 * Nombre del directorio donde se guardan los archivos de configuración,
	 * a partir del directorio de trabajo de la aplicación.
	 */
	public static final String CONFIG = "config";
	
	/**
	 * Nombre del directorio donde se almacenan recursos que necesita la aplicación, como 
	 * por ejemplo imágenes
	 */
	public static final String RESOURCES = "resources";
	
	
	/**
	 * Directorio donde se almacenan los objetos serializados
	 */
	public static String APP_BASE_DIR;
	
	public static final String NEGOTIATION_URI = "/peticionRemota.do?method=negociate";
	
	public static final String REMOTECALL_URI = "/peticionRemota.do?method=remoteCallManagement";
	
	/**
	 * Dirección del servidor central de autentificación donde se lleva a cabo
	 * el registro y el bloqueo de dispositivos
	 */
	public static String CENTRAL_AUTH_SERVER_ADDRESS = "";
	
	/**
	 * Puerto del servidor central de autentificación donde se lleva a cabo
	 * el registro y el bloqueo de dispositivos
	 */
	public static String CENTRAL_AUTH_SERVER_PORT ="";
	
	/**
	 * Uri del servidor central de autentificación donde se lleva a cabo
	 * el registro y el bloqueo de dispositivos
	 */
	public static String CENTRAL_AUTH_BASE_URI = "/platmovilidad";	
	
	public static final String SYNCH_ACTION = "/entityOperation.do?method=doSynchronization";

	public static final String FIND_ACTION = "/entityOperation.do?method=doFind";

	public static final String FIND_BY_PK_ACTION = "/entityOperation.do?method=doFindByPrimaryKey";

	public static final String CONTEXT_QUERY_ACTION = "/contextExists.do";

	public static final String CONTEXT_RETRIEVE_ACTION = "/contextRetrieve.do";
	
	public static final String LOGIN_ACTION_URI = "/login.do";

	/**
	 * Identificador de dispositivo
	 */
	public static String DEVICE_ID;
	
	/**
	 * Tiempo entre dos comprobaciones consecutivas de conexion
	 */
	public static int CONNECTION_CHECK_INTERVAL;
	
	/**
	 * Variable que indicara si se han inicializado los logs o no
	 */
	public static boolean LOG_INIT = false;
	
	/**
	 * Indica si se esta usando la pda o el pc
	 */
	public static boolean USING_PDA = true;

	/**
	 * Autologin para hacer pruebas mas sencillas
	 */
	public static boolean AUTOLOGIN = false;

	static {
		
		// Cargamos los parametros de configuracion basicos
		String appPath = System.getProperty("app_path");
		if (appPath != null && !appPath.trim().equals("")) {
			int l = appPath.length();
			if (appPath.charAt(l-1) == File.separatorChar) {
				appPath = appPath.substring(0, l-1);
			}
		}
		APP_PATH = appPath;
		DEVICE_ID = System.getProperty("device_id");
		if (System.getProperty("desktop")!=null) {
			USING_PDA = false;
		}
		if (System.getProperty("autologin")!=null) {
			AUTOLOGIN = true;
		}
	}
	
	public static Logger getLoggerFor(Class cClass) {
		/// Configuramos Log4JME para log en el cliente (si no estaba configurado)
		setLog4jConfig(true);
		return (Logger) Logger.getInstance(cClass);
	}

	private static synchronized void setLog4jConfig(boolean b24) {
		if (!LOG_INIT) {
			if (APP_PATH == null || APP_PATH.trim().equals("")) {
				System.err.println("Error, el PATH de aplicacion definido en configuracion (APP_PATH) es nulo o vacio: "+APP_PATH);
				return;
			}

			/// Comprobar si existe APP_PATH
			File fDirApp = new File(APP_PATH);
			if ( !fDirApp.exists() ) {
				System.err.println("Error, no existe el PATH de aplicacion definido en configuracion (APP_PATH): "+APP_PATH);
				return;
			}

			fDirApp = null;
			/// Comprobar si existe APP_PATH+logs. En este caso, si no existe, lo creamos
			String sFile = APP_PATH + File.separator + "logs";
			fDirApp = new File(sFile);
			if ( !fDirApp.exists() && !fDirApp.mkdir() ) {
				System.err.println("Error, no se pudo crear el PATH de logs: "+sFile);
				sFile = null;
				return;
			}

			PropertiesReader propLog4j = new PropertiesReader(File.separator + CONFIG + File.separator + "log4j.properties");
			Properties propsFile = propLog4j.getProperties();
			if (propsFile!=null) {
				String sLogFileName = propsFile.getProperty("log4j.appender.A2.File");
				if (b24) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date(System.currentTimeMillis()));
					sLogFileName = sLogFileName + "_" + cal.get(Calendar.HOUR_OF_DAY);
				}

				/// Si sFile no es nulo ni vacio, es que hemos podido crear
				/// nuestro PATH de log. En ese caso, sobreescribimos la propiedad
				/// File del log4jME por la nuestra. Sino lo creamos en otro sitio
				if (sFile!=null && !sFile.trim().equals("")) {
					sFile = sFile + File.separator + sLogFileName;
					propsFile.put("log4j.appender.A2.File", sFile);
				}

				PropertyConfigurator.configure(propsFile);
				System.out.println("Fichero de log creado en: "+sLogFileName);
				System.out.println("log4j.properties cargado adecuadamente.");

				// Logs inicializados
				LOG_INIT = true;
			} else {
				System.out.println("log4j.properties no pudo ser cargado.");
			}
		}
	}

	public static String getDeviceId() {
		return DEVICE_ID;
	}


	public static void setDeviceId(String deviceId) {
		Global.DEVICE_ID = deviceId;
	}
	

//	public static String getApplicationId() {
//		return APPLICATION_ID;
//	}
	
	/**
	 * Se almacena una copia del bean de validación para poder tener acceso a él
	 */
	private static ValidationBean validationBean;
	
	public static ValidationBean getValidationBean() {
		return validationBean;
	}

	public static void setValidationBean(ValidationBean b) {
		validationBean = b;
	}

    /**
     * Se almacena si la autenticación fue online (true) u offline (false)
     */
    private static boolean onlineValidation;

    public static boolean isOnlineValidation() {
        return onlineValidation;
    }

    public static void setOnlineValidation(boolean onlineValidation) {
        Global.onlineValidation = onlineValidation;
    }
}
