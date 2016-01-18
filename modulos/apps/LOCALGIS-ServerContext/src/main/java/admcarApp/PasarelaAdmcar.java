/**
 * PasarelaAdmcar.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package admcarApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.ftpserver.FtpConfigImpl;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.config.PropertiesConfiguration;
import org.apache.ftpserver.config.XmlConfigurationHandler;
import org.apache.ftpserver.ftplet.Configuration;
import org.apache.ftpserver.ftplet.EmptyConfiguration;
import org.apache.ftpserver.interfaces.IFtpConfig;
import org.apache.ftpserver.usermanager.BaseUser;
import org.apache.ftpserver.util.IoUtils;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.consts.config.ConfigConstants;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.server.control.notificadores.ControlSesiones;
import com.geopista.server.database.COperacionesDatabase_LCGIII;
import com.geopista.util.config.ConfigPropertiesStore;
import com.localgis.server.SessionsContextShared;

public class PasarelaAdmcar extends HttpServlet {

	private static org.apache.log4j.Logger logger;
	static {
		createDir();
		logger = org.apache.log4j.Logger.getLogger(PasarelaAdmcar.class);
	}

	public static ListaSesiones listaSesiones = new ListaSesiones();
	public static HashMap<String, String> config = new HashMap<String, String>();

	private static final String warningMessage = "WARNING!!! No encontrado en el fichero "
			+ ConfigConstants.PROPERTIES_NAME
			+ " el parametro de configuración ";

	public static void createDir() {
		File file = new File("logs");

		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public PasarelaAdmcar() {
		try {

			logger.info("Arrancado server-principal");
			// ****************************************
			// ** Inicializamos log4j
			// ****************************************************
			// PropertyConfigurator.configureAndWatch("log4j.ini", 3000);
			// logger.info("Log4j inicializado.");

			// ****************************************
			// ** Hacemos una carga de la configuracion
			// ****************************************************
			// loadConfigProperties();
			// System.out.println("Configuracion Principal cargada.");
			// logger.info("Configuracion Principal cargada.");

			// ****************************************
			// ** Desbloqueo de expedientes
			// ****************************************************
			CResultadoOperacion ro = COperacionesDatabase_LCGIII
					.desbloquearExpedientes();
			if (ro.getResultado())
				logger.info("Expedientes desbloqueados.");

			
			 //****************************************
            //** Eliminacion de elementos de la tabla lock_layers
            //****************************************************
            CResultadoOperacion re= COperacionesDatabase_LCGIII.eliminarLockLayers();
            if (re.getResultado()) logger.info("Lock Layers eliminados.");
        
            
			// ****************************************
			// ** Desbloqueo de Bienes de Inventario
			// ****************************************************
			/*
			 * COperacionesDatabase.desbloquearBienesInventario();
			 * logger.info("Bienes Inventario desbloqueados.");
			 */

			// **********************************************
			// ** Arranque del controlador de sesiones
			// ********************************************************
			new ControlSesiones();
			logger.info("Control session started.");

			// METER EN OTRO INICIO PARA QUE NO HAYA INTERDEPENDENCIA

			// **********************************************
			// ** Arranque del demonio workflow
			// ********************************************************
			// No arrancamos esta funcionalidad
			// new CWorkflowThread();
			// logger.info("Workflow thread started.");
			// **********************************************
			// ** Arranque del demonio CivilWork
			// ********************************************************
			// new CivilWorkThread();
			// logger.info("Workflow thread started.");

			// METER EN OTRO INICIO PARA QUE NO HAYA INTERDEPENDENCIA

			// Arrancamos el servidor RMI-JDBC
			/*
			 * RJJdbcServer .main(new String[] { "-port",
			 * Integer.toString(CConstantesComando.REPORTS_PORT),
			 * "org.postgresql.Driver", "oracle.jdbc.driver.OracleDriver" });
			 */

			com.geopista.security.SecurityManager.setInitHeartBeat(false);

			System.out.println("Arranque del administrador de cartografia finalizado.");

			Runtime.getRuntime().addShutdownHook(new ShutdownThread());
			logger.info("Shutdown hook added");

			arrancarServidorOrtofotos();

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

	private void arrancarServidorOrtofotos() {
		// ****************************************
		// ** Inicio del servidor ftp para ortofotos
		// ****************************************************
		/*
		 * String hostIp = InetAddress.getLocalHost().getHostAddress();
		 * System.out.println("hostIP: " + hostIp);
		 * 
		 * FileInputStream fis = new FileInputStream("config" + File.separator +
		 * "ftp.properties"); Properties properties = new Properties();
		 * properties.load(fis); properties.put("config.socket-factory.address",
		 * hostIp); FileOutputStream fos = new FileOutputStream("config" +
		 * File.separator + "ftp.properties"); properties.store(fos,
		 * "Parametros de configuracion del servidor ftp");
		 */

		// get configuration
		
		try{
	
			String[] args = { "-instance", "ftp.properties" };
			//String[] args = { "-prop", "ConfigConstants.PROPERTIES_NAME };

			//Antes leiamos el fichero de configuracion directamente. Ahora todo está integrado
			//en el fichero ftp.properties
			Configuration ftpConf = getFtpConfiguration(args);

			//String sDirectorioTrabajo = System.getProperty("user.dir");
			//System.out.println("El directorio de trabajo es " + sDirectorioTrabajo);			
	
			if (ftpConf != null) {
				// create root configuration object
				IFtpConfig ftpConfig = new FtpConfigImpl(ftpConf);
				
				
				String destinationFolder=ConfigPropertiesStore.getConfigPropertiesStore().getString("ortofoto_destination_folder");
				
				//Creamos a fuego el usario de ortofotos ya que en el modelo de LocalGIS III
				//el fichero user.gen no se carga correctamente.
				BaseUser ortofotosUser = new BaseUser();
				ortofotosUser.setName("ortofotos");
				ortofotosUser.setPassword("ortofotos");
				ortofotosUser.setEnabled(true);
				ortofotosUser.setWritePermission(true);
				ortofotosUser.setMaxUploadRate(0);
				ortofotosUser.setMaxDownloadRate(0);
				ortofotosUser.setHomeDirectory("C:/Archivos de Programa/LocalGIS/Ortofotos/Temp");
				ortofotosUser.setHomeDirectory(destinationFolder+"/Temp");
				ortofotosUser.setMaxIdleTime(0);				
				ftpConfig.getUserManager().save(ortofotosUser);
				
				logger.info("ftpConfig.getserverport " + ftpConfig.getServerPort());
	
				// start the server
				FtpServer ftpServer = new FtpServer(ftpConfig);
				ftpServer.start();
	
				String ortofotoDestinationFolder = ftpServer.getFtpConfig()
						.getUserManager().getUserByName("ortofotos")
						.getHomeDirectory();
				File ortofotoDestFolderFile = new File(ortofotoDestinationFolder);
	
				logger.info("destino de las ortofotos: "
						+ ortofotoDestinationFolder);
	
				if (!ortofotoDestFolderFile.exists()) {
					if (ortofotoDestFolderFile.mkdirs()) {
						logger.info("Path destino de Ortofotos creado.");
					} else {
						logger.info("no creado path ortofotos");
						logger.error("No se pudo crear el path destino de las Ortofotos");
					}
				}
	
				logger.info("Arranque del servidor FTP para ortofotos finalizado.");
			}
		}
		catch (Exception ex) {				
			logger.error("Excepcion al arrancar el servidor de ortofotos: ",ex);
		}

	}

	// private void loadConfigProperty(String configPropertyName){
	// String configPropertyValue =
	// ConfigPropertiesStore.getConfigPropertiesStore().getString(configPropertyName);
	// if (configPropertyValue != null) {
	// config.put(configPropertyName, configPropertyValue);
	// } else{
	// System.out.println(warningMessage + configPropertyName);
	// logger.warn(warningMessage + configPropertyName);
	// }
	// }

	// public void loadConfigProperties() {
	// try {
	// loadConfigProperty(ConfigConstants.ADMINISTRACION_SUPERUSER);
	// loadConfigProperty(ConfigConstants.ADMINISTRACION_TESTMODE);
	//
	// loadConfigProperty(ConfigConstants.USUARIOS_TTL);
	//
	// loadConfigProperty(ConfigConstants.PLANTILLAS_DIR);
	//
	// loadConfigProperty(ConfigConstants.VU_NOTIFCRUTAACC);
	//
	// loadConfigProperty(ConfigConstants.FILEUPLOAD_MAXMEMORYSIZE);
	// loadConfigProperty(ConfigConstants.FILEUPLOAD_MAX_REQUEST_SIZE);
	//
	// loadConfigProperty(ConfigConstants.WORKFLOW_DIASSILENCIOADMINISTRATIVO);
	// loadConfigProperty(ConfigConstants.WORKFLOW_DIASACTIVACIONEVENTO);
	//
	// loadConfigProperty(ConfigConstants.DOCUMENTOS_PATH);
	//
	// loadConfigProperty(ConfigConstants.REPORTS_PORT);
	//
	// loadConfigProperty(ConfigConstants.CIVILWORK_TIMETHREAD);
	// loadConfigProperty(ConfigConstants.CIVILWORK_TIMEPRIORITYUPGRADE);
	//
	// loadConfigProperty(ConfigConstants.SIGM_URL);
	// } catch (Exception e) {
	// logger.error("Error al obtener los parámentros de configuración revise el fichero: "
	// + ConfigConstants.PROPERTIES_NAME + "Excepción: " + e.toString());
	// System.out
	// .println("Error al obtener los parámentros de configuración revise el fichero: "
	// + ConfigConstants.PROPERTIES_NAME);
	// e.printStackTrace();
	// System.exit(1);
	// }
	// }

	/**
	 * Get the configuration object.
	 */
	private Configuration getFtpConfiguration(String[] args)
			throws Exception {

		Configuration config = null;
		FileInputStream in = null;
		try {
			if (args.length == 0) {
				System.out.println("Using default configuration....");
				config = EmptyConfiguration.INSTANCE;
			} else if ((args.length == 1) && args[0].equals("-default")) {
				System.out.println("Using default configuration....");
				config = EmptyConfiguration.INSTANCE;
			} else if ((args.length == 2) && args[0].equals("-xml")) {
				System.out.println("Using xml configuration file " + args[1]
						+ "...");
				in = new FileInputStream(args[1]);
				XmlConfigurationHandler xmlHandler = new XmlConfigurationHandler(
						in);
				config = xmlHandler.parse();
			} else if ((args.length == 2) && args[0].equals("-prop")) {
				System.out.println("Using properties configuration file "
						+ args[1] + "...");
				in = new FileInputStream(args[1]);
				config = new PropertiesConfiguration(in);
			}
			else if ((args.length == 2) && args[0].equals("-instance")) {
				System.out.println("Using properties configuration file "	+ args[1] + "...");
				config = new PropertiesConfiguration(getClass().getClassLoader().getResourceAsStream(args[1]));
			}
		} finally {
			IoUtils.close(in);
		}

		return config;
	}

	class ShutdownThread extends Thread {

		public ShutdownThread() {
			super();
		}

		public void run() {
			logger.info("Parando el Administrador de Cartografia.....");
			System.out.println("Parando el Administrador de Cartografia....");
		}
	}

	public void init() {
		setContextObject("UserSessions", PasarelaAdmcar.listaSesiones);
		// setContextObject("Config", config);

		// El objeto que se pasa serialiado es un Map.
		setContextObject("Config", ConfigPropertiesStore.getConfigPropertiesStore().getMap());
		setContextObject("ResourcesPath", getServletContext().getRealPath("/")
				+ File.separator + "WEB-INF" + File.separator + "classes"
				+ File.separator + "resources");
	}

	private void setContextObject(String name, Object object) {
		SessionsContextShared.getContextShared().setSharedAttribute(
				this.getServletContext(), name, object);
	}

	// public void startSessionTimer() {
	// try {
	// JGroupsChannel.getJChannel().connect("Cluster");
	// JGroupsChannel.getJChannel().setReceiver(
	// new JGroupsReceiverAdapter());
	// Timer timer = new Timer(10000, new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// try {
	// Address sourceAddress = JGroupsChannel.getJChannel()
	// .getAddress();
	// View view = JGroupsChannel.getJChannel().getView();
	// Iterator<Address> it = view.getMembers().iterator();
	// while (it.hasNext()) {
	// Address destinyAddress = it.next();
	// if(!destinyAddress.equals(view.getMembers().get(0))){
	// JGroupsChannel.getJChannel().send(
	// new Message(destinyAddress, sourceAddress,
	// PasarelaAdmcar.listaSesiones));
	// }
	// }
	// } catch (Exception ex) {
	// System.out.println(ex);
	// }
	// }
	// });
	// timer.setRepeats(true);
	// timer.start();
	// } catch (Exception ex) {
	// System.out.println(ex);
	// }
	// }

}
