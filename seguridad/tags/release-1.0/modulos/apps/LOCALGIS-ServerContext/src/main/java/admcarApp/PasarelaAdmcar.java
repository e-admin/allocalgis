package admcarApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServlet;
import javax.swing.Timer;

import org.apache.ftpserver.config.PropertiesConfiguration;
import org.apache.ftpserver.config.XmlConfigurationHandler;
import org.apache.ftpserver.ftplet.Configuration;
import org.apache.ftpserver.ftplet.EmptyConfiguration;
import org.apache.ftpserver.util.IoUtils;
import org.apache.log4j.PropertyConfigurator;
//import org.eclipse.jetty.deploy.providers.WebAppProvider;
//import org.eclipse.jetty.util.resource.Resource;
//import org.eclipse.jetty.xml.XmlConfiguration;
//import org.enhydra.jdbc.standard.StandardXADataSource;
import org.objectweb.rmijdbc.RJJdbcServer;

import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.server.administrador.init.Constantes;
import com.geopista.server.control.notificadores.ControlSesiones;
import com.geopista.server.database.COperacionesDatabase_LCGIII;
import com.geopista.server.licencias.teletramitacion.CConstantesTeletramitacion;
import com.geopista.server.licencias.workflow.CConstantesWorkflow;

import com.geopista.server.civilwork.CivilWorkConfiguration;
import com.localgis.server.SessionsContextShared;

public class PasarelaAdmcar extends HttpServlet {

	private static org.apache.log4j.Logger logger;
	static {
		createDir();
		logger = org.apache.log4j.Logger.getLogger(PasarelaAdmcar.class);
	}
	// private static org.apache.log4j.Logger logger =
	// org.apache.log4j.Logger.getLogger(PasarelaAdmcar.class);

	public static ListaSesiones listaSesiones = new ListaSesiones();

	public static void createDir() {
		File file = new File("logs");

		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public PasarelaAdmcar() {
		try {

			// ****************************************
			// ** Inicializamos log4j
			// ****************************************************
			PropertyConfigurator.configureAndWatch("config" + File.separator
					+ "log4j.ini", 3000);
			logger.info("Log4j inicializado.");
			// System.out.println(System.getProperty("java.security.auth.login.config"));
			// ****************************************
			// ** Hacemos una carga de la configuracion
			// ****************************************************
			// obtenerParametrosConfiguracion();
			// System.out.println("Configuracion Administracion Cartografia cargada");
			// logger.info("Configuracion Administracion Cartografia cargada.");

			// ****************************************
			// ** Inicio del servidor web
			// ****************************************************

			// NUEVO
			// ** SERVER **//
			// String jettyConfig = "config" + File.separator + "jetty.xml";
			// Resource jettyConfig_xml =
			// Resource.newSystemResource(jettyConfig);
			// XmlConfiguration configuration = new
			// XmlConfiguration(jettyConfig_xml.getInputStream());
			// Server server = (Server)configuration.configure();
			// Server server = Server
			//
			//
			// //** DATASOURCES **//
			// String dbDriver = "org.postgresql.Driver";
			// String dbUrl =
			// "jdbc:postgresql://pamod-pre.c.ovd.interhost.com:5432/geopista";
			// String dbUsername = "geopista";
			// String dbPassword = "01g7PT3a";
			// StandardXADataSource dataSource = new StandardXADataSource();
			// dataSource.setDriverName(dbDriver);
			// dataSource.setUrl(dbUrl);
			// dataSource.setUser(dbUsername);
			// dataSource.setPassword(dbPassword);
			// server.addBean(new
			// org.eclipse.jetty.plus.jndi.Resource("geopista",dataSource));
			//
			//
			// // boolean jettyTest = false;
			// boolean jettyTest = true;
			// if(jettyTest){
			// WebAppProvider webAppProvider = (WebAppProvider)
			// server.getAttribute("webappprovider");
			// webAppProvider.setMonitoredDirName("./docroot");

			// server.getBeans();
			// Server server = new Server();
			//
			// //** SERVER CONFIGURATION **//
			// server.setStopAtShutdown(true);
			// server.setSendServerVersion(true);
			// server.setSendDateHeader(true);
			// server.setGracefulShutdown(1000);
			// server.setDumpAfterStart(false);
			// server.setDumpBeforeStop(false);
			// server.setAttribute("org.mortbay.http.HttpRequest.maxFormContentSize",
			// 1000000);

			// ** THREATPOOLS **//
			// QueuedThreadPool threadPool = new QueuedThreadPool();
			// threadPool.setMinThreads(10);
			// threadPool.setMaxThreads(200);
			// threadPool.setDetailedDump(false);
			// server.setThreadPool(threadPool);

			// ** CONECTORS **//
			// int defaultPort = 8081;
			// int sslPort = 8085;
			// String localhost = "localhost";
			// String sslKeystore = "C:/certificados/jettystore";
			// String sslKeystorePass = "geopista";
			// String sslKeyManagerPass = "geopista";

			// Puerto por defecto (sin seguridad)
			// SelectChannelConnector defaultConnector = new
			// SelectChannelConnector();
			// defaultConnector.setName("UserPassConnector");
			// defaultConnector.setHost(localhost);
			// defaultConnector.setPort(defaultPort);
			// defaultConnector.setConfidentialPort(sslPort);
			// defaultConnector.setLowResourcesConnections(20000);
			// defaultConnector.setMaxIdleTime(300000);
			// server.addConnector(defaultConnector);

			// Puerto seguro SSL
			// SslSelectChannelConnector sslConnector = new
			// SslSelectChannelConnector();
			// sslConnector.setName("UserPassSSLConnector");
			// sslConnector.setHost(localhost);
			// sslConnector.setPort(sslPort);
			// sslConnector.setLowResourcesConnections(20000);
			// sslConnector.setMaxIdleTime(300000);
			// SslContextFactory sslContextFactory =
			// sslConnector.getSslContextFactory();
			// sslContextFactory.setKeyStorePath(sslKeystore);
			// sslContextFactory.setKeyStorePassword(sslKeystorePass);
			// sslContextFactory.setKeyManagerPassword(sslKeyManagerPass);

			// server.addConnector(sslConnector);

			// Puerto seguro FNTM/DNIe
			// llamada a funcion si dnie/fntm esta habilitado hace lo siguiente
			// if(DNIE=true){
			// int certificatePort = 9443;
			// String certificateKeystore = "C:/certificados/keystore";
			// String certificateTruststore = "C:/certificados/truststore";
			// String certificateKeystorePass = "password";
			// String certificateKeyManagerPass = "password";
			// String certificateTruststorePass = "password";
			//
			// SslSelectChannelConnector certificateConnector = new
			// SslSelectChannelConnector();
			// certificateConnector.setName("CertificateSSLConnector");
			// certificateConnector.setHost(localhost);
			// certificateConnector.setPort(certificatePort);
			// certificateConnector.setLowResourcesConnections(20000);
			// certificateConnector.setMaxIdleTime(300000);
			// SslContextFactory certificateContextFactory =
			// certificateConnector.getSslContextFactory();
			// certificateContextFactory.setKeyStorePath(certificateKeystore);
			// certificateContextFactory.setTrustStore(certificateTruststore);
			// certificateContextFactory.setKeyStorePassword(certificateKeystorePass);
			// certificateContextFactory.setKeyManagerPassword(certificateKeyManagerPass);
			// certificateContextFactory.setTrustStorePassword(certificateTruststorePass);
			// certificateContextFactory.setNeedClientAuth(true);
			// certificateContextFactory.setWantClientAuth(true);
			// server.addConnector(certificateConnector);
			// }

			// SECURITY HANDLERS
			// DefaultIdentityService identityService = new
			// DefaultIdentityService();
			// GeopistaJAASLoginService basicLoginService = new
			// GeopistaJAASLoginService();
			// basicLoginService.setName("BasicLogin");
			// basicLoginService.setLoginModuleName("jdbc");
			// basicLoginService.setCallbackHandlerClass("com.geopista.security.DefaultGeopistaCallbackHandler");
			// basicLoginService.setIdentityService(new
			// DefaultIdentityService());
			// // basicLoginService.setRoleClassNames(new
			// String[]{"org.eclipse.jetty.plus.jaas.JAASRole","org.eclipse.jetty.plus.jaas.JAASGroup"});
			// basicLoginService.setRoleClassNames(new
			// String[]{"org.eclipse.jetty.plus.jaas.JAASGroup"});
			// server.addBean(basicLoginService);
			//
			// ConstraintSecurityHandler basicSecurityHandler = new
			// ConstraintSecurityHandler();
			// basicSecurityHandler.setAuthenticator(new BasicAuthenticator());
			// basicSecurityHandler.setLoginService(basicLoginService);
			// basicSecurityHandler.setAuthMethod("BASIC");
			// basicSecurityHandler.setRealmName("BASIC-AUTH");
			//
			// GeopistaJAASLoginService certificateLoginService = new
			// GeopistaJAASLoginService();
			// certificateLoginService.setName("CertificateLogin");
			// certificateLoginService.setLoginModuleName("dnielogin");
			// certificateLoginService.setCallbackHandlerClass("com.geopista.security.DefaultGeopistaCallbackHandler");
			// certificateLoginService.setRoleClassNames(new
			// String[]{"org.eclipse.jetty.plus.jaas.JAASGroup"});
			// server.addBean(certificateLoginService);
			//
			// ConstraintSecurityHandler certificateSecurityHandler = new
			// ConstraintSecurityHandler();
			// certificateSecurityHandler.setIdentityService(new
			// DefaultIdentityService());
			// certificateSecurityHandler.setAuthenticator(new
			// ClientCertDNIeFNMTAuthenticator());
			// certificateSecurityHandler.setLoginService(certificateLoginService);
			// certificateSecurityHandler.setAuthMethod("CLIENT-CERT");
			// certificateSecurityHandler.setRealmName("CLIENT-CERT-AUTH");

			// ** CONTEXTS **//
			// DeploymentManager deploymentManager = new DeploymentManager();
			// // ContextProvider contextProvider = new ContextProvider();
			// // contextProvider.setMonitoredDirName("./contexts");
			// // contextProvider.setScanInterval(1);
			// // deploymentManager.addAppProvider(contextProvider);
			// WebAppProvider webAppProvider = new WebAppProvider();
			// webAppProvider.setMonitoredDirName("./webapps");
			// //webAppProvider.setMonitoredDirName("/webapp");
			// webAppProvider.setExtractWars(true);
			// webAppProvider.addLifeCycleListener(new
			// WebAppProviderLyfeCycleListener());
			// //webAppProvider.setTempDir(new File("./work"));
			// //webAppProvider.setContextXmlDir("./contexts");
			// webAppProvider.setScanInterval(1);
			// // ScanningAppProvider scanningAppProvider = new
			// ScanningAppProvider();
			// // webAppProvider.addScannerListener(new
			// LocalGISScannerListener());
			//
			// deploymentManager.addAppProvider(webAppProvider);
			// server.addBean(deploymentManager);
			// server.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
			// ".*/jsp-api-[^/]*\\.jar$|.*/jsp-[^/]*\\.jar$");

			// server.addLifeCycle(deploymentManager);
			// server.addLifeCycleListener(listener);

			// borrar arraylist: recuperar contextos de la bd
			// ArrayList<LocalGISWebApplicationContext> webapps = new
			// ArrayList<LocalGISWebApplicationContext>();
			// LocalGISWebApplicationContext webApp;

			// webApp = new LocalGISWebApplicationContext("Administración",
			// "administracion", "Geopista.Administracion.Login");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Alp To LocalGIS",
			// "alptolocalgis", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Catastro",
			// "catastro", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Contaminantes",
			// "contaminantes", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("EIEL", "eiel", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Geopista",
			// "geopista", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Inventario",
			// "inventario", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Licencias",
			// "licencias", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Metadatos",
			// "metadatos", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Ocupaciones",
			// "ocupaciones", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Ortofoto",
			// "ortofoto", "");
			// webapps.add(webApp);
			// webApp = new LocalGISWebApplicationContext("Single Sign On",
			// "sso", "Geopista.Administracion.Login");
			// webapps.add(webApp);

			// webapps.add("geopista");
			// webapps.add("inventario");
			// webapps.add("catastro");
			// webapps.add("contaminantes");
			// webapps.add("licencias");
			// webapps.add("sso");

			// ArrayList<Handler> contextsList = new ArrayList<Handler>();
			//
			// //String jetty_war =
			// "c:/DESARROLLO/PRUEBA%20JETTY/EIELMODELO%20Jetty/eielmodelo/classes/";
			// WebAppContext context = null;
			// Iterator<LocalGISWebApplicationContext> webappsIt =
			// webapps.iterator();
			// while(webappsIt.hasNext()){
			// LocalGISWebApplicationContext webapp = webappsIt.next();
			//
			// context = new WebAppContext();
			// context.setDisplayName(webapp.getName());
			// // context.setDisplayName("JAAS Role");
			// context.setContextPath("/" + webapp);
			//
			// //context.setClassLoader(Thread.currentThread().getContextClassLoader());
			// //context.setDescriptor(webapp + "/WEB-INF/web.xml");
			// context.setDescriptor("contexts/" + webapp + "/WEB-INF/web.xml");
			// //context.setResourceBase("docroot" + "/" + webapp);
			// //context.setWar("docroot" + "/" + webapp);
			// //context.setParentLoaderPriority(true);
			// //context.setTempDirectory(new File("work" + "/" +webapp));
			// //context.setTempDirectory(new File("work"));
			// context.setCopyWebDir(false);
			// context.setWar("webapps/" + webapp + ".war");
			// context.setExtractWAR(true);
			//
			// context.addEventListener(new WebAppContextListener());
			// // context.setWar("docroot" + "/" + webapp + "/" + webapp +
			// ".war");
			// // context.setExtractWAR(false);
			// //context.setVirtualHosts(new String[]{"localhost","127.0.0.1"});
			// //context.setSecurityHandler(returnBasicSecurityConstraint(basicLoginService,
			// webapp.getConstraintRole(), identityService));
			// context.setSecurityHandler(basicSecurityHandler);
			// contextsList.add(context);
			// }

			// Para DNIe
			// String name = "DNIe y FNMT";
			// String webapp = "dnie";
			// WebAppContext context2 = new WebAppContext();
			// context2.setDisplayName(name);
			// context2.setContextPath("/" + webapp);
			// context2.setDescriptor("contexts/" + webapp +
			// "/WEB-INF/web.xml");
			// context2.setWar("webapps" + "/" + webapp + ".war");
			// context2.setCopyWebDir(false);
			// //context2.setTempDirectory(new File("work" + "/" +webapp));
			// //context2.setTempDirectory(new File("webapps"));
			// context2.setExtractWAR(true);
			// context2.setSecurityHandler(certificateSecurityHandler);
			// contextsList.add(context2);

			// ContextHandlerCollection contexts = new
			// ContextHandlerCollection();
			// Handler[] handlers = new Handler[contextsList.size()];
			// contextsList.toArray(handlers);
			// contexts.setHandlers(handlers);
			// deploymentManager.setContexts(contexts);
			// server.setHandler(contexts);

			// RequestLogHandler requestLogHandler = new RequestLogHandler();
			// contextsList.add(requestLogHandler);
			//
			// NCSARequestLog requestLog = new
			// NCSARequestLog("./logs/jettyAdmCar-yyyy_mm_dd.log");
			// requestLog.setRetainDays(30);
			// requestLog.setAppend(true);
			// requestLog.setExtended(false);
			// requestLog.setLogTimeZone("GMT");
			// requestLog.setLogServer(true);
			// requestLog.setLogDispatch(true);
			// requestLogHandler.setRequestLog(requestLog);

			// ContextHandlerCollection contexts = new
			// ContextHandlerCollection();
			// Handler[] handlers = new Handler[contextsList.size()];
			// contextsList.toArray(handlers);
			// contexts.setHandlers(handlers);
			//
			// server.setHandler(contexts);

			// String jetty_home =
			// System.getProperty("jetty.home","../jetty-distribution/target/distribution");
			// System.setProperty("jetty.home",jetty_home);
			// WebAppContext webapp = new WebAppContext();
			// webapp.setContextPath("/");
			// webapp.setWar(jetty_home+"/webapps/geopista.war");
			// server.setHandler(webapp);

			// WebAppContext context = new WebAppContext();
			// context.setDescriptor(webapp+"/WEB-INF/web.xml");
			// context.setResourceBase("../test-jetty-webapp/src/main/webapp");
			// context.setContextPath("/");
			// context.setParentLoaderPriority(true);
			// server.setHandler(context);

			// }
			// ** START **//
			// try{
			// server.start();
			// server.join();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// FIN NUEVO

			logger.info("Servidor web LocalGIS arrancado.");
			System.out.println("Servidor web LocalGIS arrancado.");

			// ****************************************
			// ** Desbloqueo de expedientes
			// ****************************************************
			CResultadoOperacion ro = COperacionesDatabase_LCGIII
					.desbloquearExpedientes();
			if (ro.getResultado())
				logger.info("Expedientes desbloqueados.");

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
			/*RJJdbcServer
					.main(new String[] { "-port",
							Integer.toString(CConstantesComando.REPORTS_PORT),
							"org.postgresql.Driver",
							"oracle.jdbc.driver.OracleDriver" });*/

			com.geopista.security.SecurityManager.setInitHeartBeat(false);

			System.out
					.println("Arranque del administrador de cartografia finalizado.");

			Runtime.getRuntime().addShutdownHook(new ShutdownThread());
			logger.info("Shutdown hook added");
			
			//obtenerParametrosConfiguracion();

			// ****************************************
			// ** Inicio del servidor ftp para ortofotos
			// ****************************************************
			/*
			 * String hostIp = InetAddress.getLocalHost().getHostAddress();
			 * System.out.println("hostIP: " + hostIp);
			 * 
			 * FileInputStream fis = new FileInputStream("config" +
			 * File.separator + "ftp.properties"); Properties properties = new
			 * Properties(); properties.load(fis);
			 * properties.put("config.socket-factory.address", hostIp);
			 * FileOutputStream fos = new FileOutputStream("config" +
			 * File.separator + "ftp.properties"); properties.store(fos,
			 * "Parametros de configuracion del servidor ftp");
			 */

			// get configuration

			// DESCOMENTAR - FALTA FICHERO ftpproperties
			// String[] args = {"-prop", "config" + File.separator +
			// "ftp.properties"};
			// Configuration ftpConf = getFtpConfiguration(args);
			//
			// if(ftpConf != null) {
			// // create root configuration object
			// IFtpConfig ftpConfig = new FtpConfigImpl(ftpConf);
			// logger.info("ftpConfig.getserverport " +
			// ftpConfig.getServerPort());
			//
			// // start the server
			// FtpServer ftpServer = new FtpServer(ftpConfig);
			// ftpServer.start();
			//
			// String ortofotoDestinationFolder =
			// ftpServer.getFtpConfig().getUserManager().getUserByName("ortofotos").getHomeDirectory();
			// File ortofotoDestFolderFile = new
			// File(ortofotoDestinationFolder);
			//
			// logger.info("destino de las ortofotos: " +
			// ortofotoDestinationFolder);
			//
			// if (!ortofotoDestFolderFile.exists()){
			// if(ortofotoDestFolderFile.mkdirs())
			// {
			// logger.info("Path destino de Ortofotos creado.");
			// }
			// else
			// {
			// logger.info("no creado path ortofotos");
			// logger.error("No se pudo crear el path destino de las Ortofotos");
			// }
			// }
			//
			// logger.info("Arranque del servidor FTP para ortofotos finalizado.");
			// }

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

	public static boolean obtenerParametrosConfiguracion() {
		String ficheroConfiguracion = "config" + java.io.File.separator
				+ "config.ini";

		try {
			puzzled.IniFile fichConfig = new puzzled.IniFile(
					ficheroConfiguracion);
			if (fichConfig.getValue("ADMINISTRACION", "SUPERUSER") == null) {
				System.out
						.println("WARNING!!! Párametro de configuración SUPERUSER no en contrado en el fichero: "
								+ ficheroConfiguracion);
				logger.warn("WARNING!!! Párametro de configuración SUPERUSER no encontrado en el fichero: "
						+ ficheroConfiguracion);
			} else
				Constantes.SUPERUSER = fichConfig.getValue("ADMINISTRACION",
						"SUPERUSER");

			if ((fichConfig.getValue("VU", "VU_NOTIF_CRUTAACC") == null)
					|| (fichConfig.getValue("VU", "VU_NOTIF_CRUTAACC")
							.equals(""))) {
				System.out
						.println("WARNING!!! Párametro de configuración VU_NOTIF_CRUTAACC no encontrado en el fichero: "
								+ ficheroConfiguracion);
				logger.warn("WARNING!!! Párametro de configuración VU_NOTIF_CRUTAACC no encontrado en el fichero: "
						+ ficheroConfiguracion);
			} else
				CConstantesTeletramitacion.VU_NOTIF_CRUTAACC = fichConfig
						.getValue("VU", "VU_NOTIF_CRUTAACC");

			if ((fichConfig.getValue("FILE_UPLOAD", "MAX_MEMORY_SIZE") == null)
					|| (fichConfig.getValue("FILE_UPLOAD", "MAX_MEMORY_SIZE")
							.equals(""))) {
				System.out
						.println("WARNING!!! Párametro de configuración MAX_MEMORY_SIZE no encontrado en el fichero: "
								+ ficheroConfiguracion);
				logger.warn("WARNING!!! Párametro de configuración MAX_MEMORY_SIZE no encontrado en el fichero: "
						+ ficheroConfiguracion);
			} else
				CConstantesComando.MaxMemorySize = new Integer(
						fichConfig.getValue("FILE_UPLOAD", "MAX_MEMORY_SIZE"))
						.intValue();

			if ((fichConfig.getValue("FILE_UPLOAD", "MAX_REQUEST_SIZE") == null)
					|| (fichConfig.getValue("FILE_UPLOAD", "MAX_REQUEST_SIZE")
							.equals(""))) {
				System.out
						.println("WARNING!!! Párametro de configuración MAX_REQUEST_SIZE no encontrado en el fichero: "
								+ ficheroConfiguracion);
				logger.warn("WARNING!!! Párametro de configuración MAX_REQUEST_SIZE no encontrado en el fichero: "
						+ ficheroConfiguracion);
			} else
				CConstantesComando.MaxRequestSize = new Integer(
						fichConfig.getValue("FILE_UPLOAD", "MAX_REQUEST_SIZE"))
						.intValue();

			if ((fichConfig
					.getValue("WORKFLOW", "DIAS_SILENCIO_ADMINISTRATIVO") == null)
					|| (fichConfig.getValue("WORKFLOW",
							"DIAS_SILENCIO_ADMINISTRATIVO").equals(""))) {
				System.out
						.println("WARNING!!! Párametro de configuración DIAS_SILENCIO_ADMINISTRATIVO no encontrado en el fichero: "
								+ ficheroConfiguracion);
				logger.warn("WARNING!!! Párametro de configuración DIAS_SILENCIO_ADMINISTRATIVO no encontrado en el fichero: "
						+ ficheroConfiguracion);
			} else
				CConstantesWorkflow.diasSilencioAdministrativo = new Long(
						fichConfig.getValue("WORKFLOW",
								"DIAS_SILENCIO_ADMINISTRATIVO")).longValue();
			if ((fichConfig.getValue("CIVIL_WORK", "TIME_THREAD") == null)
					|| (fichConfig.getValue("CIVIL_WORK", "TIME_THREAD")
							.equals(""))) {
				System.out
						.println("WARNING!!! Time thread not found in file : "
								+ ficheroConfiguracion + " Using default "
								+ CivilWorkConfiguration.timeCivilWorkThread);
				logger.warn("WARNING!!! Time thread not found in file : "
						+ ficheroConfiguracion + " Using default "
						+ CivilWorkConfiguration.timeCivilWorkThread);
			} else
				CivilWorkConfiguration.timeCivilWorkThread = new Long(
						fichConfig.getValue("CIVIL_WORK", "TIME_THREAD"))
						.longValue();
			if ((fichConfig.getValue("CIVIL_WORK", "TIME_PRIORITY_UPGRADE") == null)
					|| (fichConfig.getValue("CIVIL_WORK",
							"TIME_PRIORITY_UPGRADE").equals(""))) {
				System.out
						.println("WARNING!!! time Priority Upgrade not found in file : "
								+ ficheroConfiguracion
								+ " Using default "
								+ CivilWorkConfiguration.timePriorityUpgrade);
				logger.warn("WARNING!!! time Priority Upgrade not found in file : "
						+ ficheroConfiguracion
						+ " Using default "
						+ CivilWorkConfiguration.timePriorityUpgrade);
			} else
				CivilWorkConfiguration.timePriorityUpgrade = new Integer(
						fichConfig.getValue("CIVIL_WORK",
								"TIME_PRIORITY_UPGRADE")).intValue();

			if ((fichConfig.getValue("WORKFLOW", "DIAS_ACTIVACION_EVENTO") == null)
					|| (fichConfig.getValue("WORKFLOW",
							"DIAS_ACTIVACION_EVENTO").equals(""))) {
				System.out
						.println("WARNING!!! Párametro de configuración DIAS_ACTIVACION_EVENTO no encontrado en el fichero: "
								+ ficheroConfiguracion);
				logger.warn("WARNING!!! Párametro de configuración DIAS_ACTIVACION_EVENTO no encontrado en el fichero: "
						+ ficheroConfiguracion);
			} else
				CConstantesWorkflow.diasActivacionEvento = new Long(
						fichConfig.getValue("WORKFLOW",
								"DIAS_ACTIVACION_EVENTO")).longValue();

			if ((fichConfig.getValue("DOCUMENTOS", "PATH") == null)
					|| (fichConfig.getValue("DOCUMENTOS", "PATH").equals(""))) {
				System.out
						.println("WARNING!!! Párametro de configuración DOCUMENTOS.PATH no encontrado en el fichero: "
								+ ficheroConfiguracion);
				logger.warn("WARNING!!! Párametro de configuración DOCUMENTOS.PATH no encontrado en el fichero: "
						+ ficheroConfiguracion);
			} else {
				CConstantesComando.documentosPath = fichConfig.getValue(
						"DOCUMENTOS", "PATH");
				/** Eliminamos la barra final si existe */
				if ((CConstantesComando.documentosPath
						.lastIndexOf(File.separator) != -1)
						&& (CConstantesComando.documentosPath
								.lastIndexOf(File.separator) == CConstantesComando.documentosPath
								.length() - 1)) {
					CConstantesComando.documentosPath = CConstantesComando.documentosPath
							.substring(
									0,
									CConstantesComando.documentosPath.length() - 1);
				}
			}

			if ((fichConfig.getValue("REPORTS", "PORT") == null)
					|| (fichConfig.getValue("REPORTS", "PORT").equals(""))) {
				System.out
						.println("WARNING!!! Párametro de configuración REPORTS.PORT no encontrado en el fichero: "
								+ ficheroConfiguracion
								+ ". Se mantiene el valor por defecto ["
								+ CConstantesComando.REPORTS_PORT + "]");
				logger.warn("WARNING!!! Párametro de configuración REPORTS.PORT no encontrado en el fichero: "
						+ ficheroConfiguracion
						+ ". Se mantiene el valor por defecto ["
						+ CConstantesComando.REPORTS_PORT + "]");
			} else {
				String reportsPortStr = fichConfig.getValue("REPORTS", "PORT");
				try {
					CConstantesComando.REPORTS_PORT = Integer
							.parseInt(reportsPortStr);
				} catch (NumberFormatException e) {
					System.out
							.println("WARNING!!! Párametro de configuración REPORTS.PORT con valor inválido ["
									+ reportsPortStr
									+ "]en el fichero: "
									+ ficheroConfiguracion
									+ ". Se mantiene el valor por defecto ["
									+ CConstantesComando.REPORTS_PORT + "]");
					logger.warn("WARNING!!! Párametro de configuración REPORTS.PORT con valor inválido ["
							+ reportsPortStr
							+ "] en el fichero: "
							+ ficheroConfiguracion
							+ ". Se mantiene el valor por defecto ["
							+ CConstantesComando.REPORTS_PORT + "]");

				}
				// Para pruebas de funcionamiento de la aplicacion y que permita
				// disponer de un entorno
				// mas rapida de funcionamiento sin cargar toda la informacion.
				if (fichConfig.getValue("ADMINISTRACION", "TEST_MODE") == null) {
					Constantes.TEST_MODE = false;
				} else
					Constantes.TEST_MODE = Boolean.parseBoolean(fichConfig
							.getValue("ADMINISTRACION", "TEST_MODE"));

				// TTL de Usuarios
				if ((fichConfig.getValue("USUARIOS", "TTL") == null)
						|| fichConfig.getValue("USUARIOS", "TTL").equals("")) {
					// public static final int MINUTOS_SESION=60*48;//48 HORAS
					Constantes.TTL_USUARIOS = 60 * 48;
				} else
					Constantes.TTL_USUARIOS = Integer.parseInt(fichConfig
							.getValue("USUARIOS", "TTL"));
				logger.info("TTL Usuarios:" + Constantes.TTL_USUARIOS);

				// Directorio de plantillas por entidad
				if (fichConfig.getValue("PLANTILLAS", "DIR") == null) {
					// public static final int MINUTOS_SESION=60*48;//48 HORAS
					Constantes.DIR_PLANTILLAS = "plantillas.entidad";
				} else
					Constantes.DIR_PLANTILLAS = fichConfig.getValue(
							"PLANTILLAS", "DIR");

				logger.info("Directorio de Plantillas:"
						+ Constantes.DIR_PLANTILLAS);

			}

		} catch (Exception e) {
			logger.error("Error al obtener los parámentros de configuración revise el fichero: "
					+ ficheroConfiguracion + "Excepción: " + e.toString());
			System.out
					.println("Error al obtener los parámentros de configuración revise el fichero: "
							+ ficheroConfiguracion);
			e.printStackTrace();
			System.exit(1);
		}

		return true;
	}

	/**
	 * Get the configuration object.
	 */
	private static Configuration getFtpConfiguration(String[] args)
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

	public void init(){
		SessionsContextShared.getContextShared().setSharedAttribute(this.getServletContext(), "UserSessions", PasarelaAdmcar.listaSesiones);	
	}
	
//	public void startSessionTimer() {	
//		try {		
//			JGroupsChannel.getJChannel().connect("Cluster");
//			JGroupsChannel.getJChannel().setReceiver(
//					new JGroupsReceiverAdapter());
//			Timer timer = new Timer(10000, new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					try {		
//						Address sourceAddress = JGroupsChannel.getJChannel()
//						.getAddress();			
//						View view = JGroupsChannel.getJChannel().getView();
//						Iterator<Address> it = view.getMembers().iterator();
//						while (it.hasNext()) {
//							Address destinyAddress = it.next();
//							if(!destinyAddress.equals(view.getMembers().get(0))){
//								JGroupsChannel.getJChannel().send(
//										new Message(destinyAddress, sourceAddress,
//												PasarelaAdmcar.listaSesiones));
//							}
//						}
//					} catch (Exception ex) {
//						System.out.println(ex);
//					}
//				}
//			});
//			timer.setRepeats(true);
//			timer.start();	
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//	}

}
