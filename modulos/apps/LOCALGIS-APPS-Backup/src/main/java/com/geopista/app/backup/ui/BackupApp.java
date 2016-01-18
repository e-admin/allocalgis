/**
 * BackupApp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Módulo para la creación de Backup en modo gráfico
 *
 */
package com.geopista.app.backup.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.backup.BackupAdapter;
import com.geopista.app.backup.BackupConfig;
import com.geopista.app.backup.BackupOracle;
import com.geopista.app.backup.BackupPostgres;
import com.geopista.app.backup.BackupPreferences;
import com.geopista.app.backup.InformacionTabla;
import com.geopista.app.backup.SecurityManager;
import com.geopista.app.backup.images.IconLoader;
import com.geopista.app.backup.protocol.BackupClient;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.global.WebAppConstants;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.ui.dialogs.LoginDialog;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.SplashWindow;

public class BackupApp extends CMain {

	private static final long serialVersionUID = 1L;

	private static Logger logger= Logger.getLogger(BackupApp.class);

	private static final String USR_BACKUP_BdD = "localgisbackup";
	private static final String NAME_RESOURCE = "BackupSupraMunicipal";
	private static final String LOCALGIS_LOGO = "geopista.gif";
	private static final int DIM_X = 800;
	private static final int DIM_Y = 600;
	private static int IdMunicipio= 34083;
	
	private static boolean isVersion8=false;

	private AppContext aplicacion = null;
	private BackupAdapter backupAdapter = null;

	private String _idApp = "Backup";
	private String _urlServerBackup = "";
	private Hashtable _mapaEntidades = null;

	private JDesktopPane desktopPane = null;
	JLabel helpLabel =  null;
	JLabel connectionLabel = null;

	private final BackupPreferences backupPreferences = BackupPreferences.getInstance();

	private LoginDialog loginDialog = null;

	public static void main(String[] args) {
		BackupApp backupApp = new BackupApp();
		backupApp.execute();
		backupApp.dispose();
		System.exit(0);
	}

	public BackupApp() {
		initBasicApp();
	}
	
	private void initBasicApp () {
		try {
			//Init logs
			PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);
		} catch(Exception e) { };
		//Carga literales
		loadResourceBundle ();
	}
	
	private void loadResourceBundle  () {
		Locale locale = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.backup.language.BackupSupraMunicipali18n", locale, this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put(NAME_RESOURCE, bundle);
	}

	private String getMessageI18N (String keyMenssage) {
		return I18N.get(NAME_RESOURCE, keyMenssage);
	}

	public void execute() 
	{
		logger.debug("Inicio Backup...");
		
		//Inicializar interfaz de usuario
		try {initLookAndFeel();} catch (Exception e) {}
				
		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			//Configuracion inicial
			configureApp();
			initComponents();
			//Carga pantalla ppal
			SplashWindow splashWindow = showSplash();
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setIconImage(IconLoader.icon(LOCALGIS_LOGO).getImage());
			this.setTitle(getMessageI18N ("geopista.app.backup.titulo"));
			show();
			if (splashWindow != null) 
				splashWindow.setVisible(false);

			//Mostrar formulario login
			SSOAuthManager.ssoAuthManager(_idApp);				
			if (!aplicacion.isOnlyLogged()){             
				showAuth();
			}

			//Realizar backup
			iniciarProcesoBackup ();

			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}catch (Exception e) {
			logger.error("Error al inicializar: " + StringUtil.stackTrace(e));
			ErrorDialog.show(this, "ERROR", "ERROR: " + e.getMessage(), StringUtil.stackTrace(e));
		}
		logger.debug("Fin Backup...");
	}


	private boolean configureApp()
	{
		try {
			aplicacion = (AppContext)AppContext.getApplicationContext();
			_urlServerBackup = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.BACKUP_WEBAPP_NAME;
			aplicacion.setUrl(_urlServerBackup);
			aplicacion.setMainFrame(this);

			IdMunicipio = new Integer(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)).intValue();
		}
		catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			//mostrarMensajeDialogo( "Excepcion al cargar el fichero de configuración:\n"+sw.toString());
			logger.error("Exception: " + sw.toString());
			System.exit(-1);
			return false;
		}

		return true;
	}

	private boolean showAuth() {
		try  {
			CAuthDialog auth = new CAuthDialog(this, true, _urlServerBackup, _idApp, BackupApp.IdMunicipio, aplicacion.getI18NResource());
			auth.setBounds(30, 60, 315, 155);
			//Realizar login recuperando panel con datos establecidos
			loginDialog = auth.showLogPass();

			return true;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR al autenticar al usuario " + sw.toString());
			JOptionPane optionPane = new JOptionPane("Error al inicializar: \n" +((e.getMessage()!= null && e.getMessage().length()>=0)?e.getMessage():e.toString()), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.show();
			return false;
		}
	}

	private void initComponents() {
		desktopPane= new JDesktopPane();
		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		getContentPane().add(desktopPane, BorderLayout.CENTER);	        

		//Creacion de objetos.
		getContentPane().add(getPanelStatus(), BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		//Evento para que la ventana al minimizar tenga un tamaño minimo.
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				ajustaVentana(aplicacion.getMainFrame(), DIM_X, DIM_Y+50);
			}
		});

		//Evento al cerrar la ventana.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exitForm();
			}
		});

		setConnectionMessage(aplicacion.isOnline());

		aplicacion.addAppContextListener(new AppContextListener() {
			public void connectionStateChanged(GeopistaEvent e) {
				switch (e.getType()) {
				case GeopistaEvent.DESCONNECTED:
					setConnectionStatus(false);
					break;
				case GeopistaEvent.RECONNECTED:
					setConnectionStatus(true);
					break;                    
				}
			}
		});
	}

	public void setConnectionMessage (boolean connected)
	{	
		String icon = ((!connected)? "no_network.png" : "online.png");
		String mensaje = aplicacion.getI18nString(((!connected)? "geopista.OffLineStatusMessage" : "geopista.OnLineStatusMessage"));
		connectionLabel.setIcon(IconLoader.icon(icon));
		connectionLabel.setToolTipText(mensaje);
	}

	public void setConnectionStatus(boolean connected)
	{
		setConnectionMessage(connected);

		if (!connected) {
			if (aplicacion.isLogged())
				SecurityManager.unLogged();
			else
				aplicacion.login();
		} 
		else if (!aplicacion.isLogged())
			showAuth();   

	}

	private JPanel getPanelStatus ()
	{
		helpLabel = new JLabel();
		connectionLabel = new JLabel();
		JPanel jPanelStatus = new JPanel(new GridBagLayout());
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());

		helpLabel.setIcon(IconLoader.icon("help.gif"));
		helpLabel.setToolTipText(aplicacion.getI18nString("geopista.Help"));
		helpLabel.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				//mostrarAyudaActionPerformed();
			}
			public void mouseEntered(MouseEvent e) {					
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {				
			}
			public void mouseReleased(MouseEvent e) {
			}
		});

		connectionLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
		jPanelStatus.add(connectionLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelStatus.add(new JPanel(), new GridBagConstraints(1, 0, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));     
		jPanelStatus.add(helpLabel, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		return jPanelStatus;
	}

	/**
	 * Metodo que no permite que la pantalla se minimize mas de 600 en largo y del ancho pasado por parametro.
	 * @param desktop La ventana a analizar.
	 * @param wid El ancho pasado.
	 * @param hig El largo pasado.
	 */
	public static void ajustaVentana(JFrame desktop, int wid, int hig) {
		if (desktop != null) {
			if (desktop.getWidth()<wid || desktop.getHeight()<600)
				desktop.setSize(wid, hig);
		}
	}

	/**
	 * Acciones realizadas cuando se cierra la aplicacion.
	 */
	private void exitForm() 
	{
		try {
			if(!SSOAuthManager.isSSOActive()) 
				com.geopista.security.SecurityManager.logout();
		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}
		System.exit(0);
	}

	private void iniciarProcesoBackup () throws Exception {
		Connection connection = null;
		try {
			//Carga driver para conexion 
			loadBackupAdapter();
			
			//Obtenemos conexion para proceso backup
			connection = getConecctionProcesoBackup();
			
			//Finalizar si no obtenemos conexion
			if (connection == null)
				throw new RuntimeException("Imposible obtener conexion para proceso backup");
			
			//Obtener configuracion backpup e iniciar proceso backup
			Object[] configBackup = getConfigBackup(connection);
			if (configBackup != null) 
				initBackup(configBackup, connection);
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private void loadBackupAdapter () {
		String url = backupPreferences.getUrl();
		//Determinar tipo BdD de geopista y obtener un tipo de backup adecuado
		if (url.indexOf("jdbc:postgresql")>=0) 
			backupAdapter = new BackupPostgres();
		else if (url.indexOf("jdbc:oracle")>=0) 
			backupAdapter = new BackupOracle();
		try {
			//Cargamos la clase del driver que vamos a usar para la conexión jdbc
			Class.forName(backupAdapter.obtenerClaseDriver());
			System.out.println("LoadClassDriver: " + backupAdapter.obtenerClaseDriver());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Obtenemos conexion para realizar proceso de backup.
	 * Para ello actualizamos pwd de forma aleatoria del usuario backup y obtenemos conexion
	 * @return
	 * @throws Exception
	 */
	private Connection getConecctionProcesoBackup () throws Exception {
		Connection connection = null;
		try {
			String usrBackup = USR_BACKUP_BdD;
			//TODO: pendiente generacion aleatoria
			String pwdBackup = USR_BACKUP_BdD;
			//Actualizacion aleatoria pwd
			BackupClient backupClient = new BackupClient(_urlServerBackup + "/BackupServlet");
			backupClient.actualizarPwdUsuarioBdD(usrBackup, pwdBackup);
			//Obtenemos conexion para usuario backup
			connection = DriverManager.getConnection(backupPreferences.getUrl(), usrBackup, pwdBackup);
			System.out.println("connection URL: " + backupPreferences.getUrl());
		} catch (Exception ex) {
			throw ex;
		}
		return connection;
	}

	private String[] loadListaEntidades (Connection connection) throws Exception {
		String[] entidades = new String[0];
		try {
			//Obtener lista de entidades y annadir opcion defecto
			_mapaEntidades = backupAdapter.obtenerEntidades(connection);
			
			//Establecer resultados para carga combo
			List<String> lstEntidades = new ArrayList<String>(_mapaEntidades.keySet());
		    Collections.sort(lstEntidades);
		    //Incluir opcion "Todas las entidades" en opciones del combo
		    lstEntidades.add(0, getMessageI18N("geopista.app.backup.todasEntidades"));
		    
		    //Incluir opcion "Todas las entidades" en mapa de entidades total
		    _mapaEntidades.put(getMessageI18N("geopista.app.backup.todasEntidades"), new Integer(-1));
		    
			entidades = (String[]) lstEntidades.toArray(new String[lstEntidades.size()]);
		} catch (SQLException e) {
			throw e;
		}
		return entidades;
	}

	private Object[] getConfigBackup (Connection connection) throws Exception {
		boolean haySeleccion = true;
		Object[] config = null;
		try {
			EntidadDialog entidadDialog = new EntidadDialog(this, loadListaEntidades(connection));
			centreOnScreen(entidadDialog);
			entidadDialog.setVisible(true);
			if (entidadDialog.isCancelled()) 
				haySeleccion = false;
			
			//Establecer resultados segun seleccion
			if (haySeleccion) {
				String entidadSelected = entidadDialog.getEntidadSelected();
				File directorySelected = entidadDialog.getDirectorySelected();
				int idEntidadSelected = (int) _mapaEntidades.get(entidadSelected);
				config = new Object[] {idEntidadSelected, directorySelected};
			}
		} catch (Exception e) {
			throw e;
		}
		return config;
	}

	/**
	 * Inicia proceso de backup: generacion elementos resultados segun configuracion
	 * @param configBackup
	 * @throws Exception
	 */
	private void initBackup (Object[] configBackup, Connection connection) throws Exception {

		FileOutputStream backupErrorFile = null;
		FileOutputStream backupMunicipioFile = null;
		FileOutputStream backupComunFile = null;

		PrintStream printStreamError = null;
		PrintStream printStreamSalidaMunicipio = null;
		PrintStream printStreamSalidaComun = null;

		try {
			//Obtener configuracion
			Integer idEntidadSelected = (Integer) configBackup[0];
			File directorySelected = (File) configBackup[1];

			File error = new File(directorySelected, BackupConfig.FILEERROR);
			error.createNewFile();

			File backupMunicipio = new File(directorySelected, BackupConfig.FILEENTIDAD);
			backupMunicipio.createNewFile();

			File backupComun = new File(directorySelected, BackupConfig.FILECOMUN);
			backupComun.createNewFile();

			backupErrorFile = new FileOutputStream(error);
			backupMunicipioFile = new FileOutputStream(backupMunicipio);
			backupComunFile = new FileOutputStream(backupComun);

			printStreamError = new PrintStream(backupErrorFile);
			printStreamSalidaMunicipio = new PrintStream(backupMunicipioFile);
			printStreamSalidaComun = new PrintStream(backupComunFile);

			doBackup(connection, printStreamSalidaMunicipio, printStreamSalidaComun, printStreamError, idEntidadSelected.intValue());

			generarFicherosBackupUTF8(directorySelected);
			
			JOptionPane.showMessageDialog(this, getMessageI18N("geopista.app.backup.backupFinalizado"), getMessageI18N("geopista.app.backup.informacion"), JOptionPane.INFORMATION_MESSAGE);           
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, getMessageI18N("geopista.app.backup.backupAbortado"), getMessageI18N("geopista.app.backup.error"), JOptionPane.ERROR_MESSAGE);
			throw e;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, getMessageI18N("geopista.app.backup.backupAbortado"), getMessageI18N("geopista.app.backup.error"), JOptionPane.ERROR_MESSAGE);
			throw e;
		} finally {
			if (backupErrorFile != null) backupErrorFile.close();
			if (backupMunicipioFile != null) backupMunicipioFile.close();
			if (backupComunFile != null) backupComunFile.close();
			if (printStreamError != null) printStreamError.close();
			if (printStreamSalidaMunicipio != null) printStreamSalidaMunicipio.close();
			if (printStreamSalidaComun != null) printStreamSalidaComun.close();
		}
	}

	private  void centreOnScreen(Component componentToMove) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		componentToMove.setLocation((screenSize.width - componentToMove.getWidth()) / 2, (screenSize.height - componentToMove.getHeight()) / 2);
	}


	/**
	 * Proceso backup
	 * @param connection
	 * @param printStreamSalidaMunicipio
	 * @param printStreamSalidaComun
	 * @param printStreamError
	 * @param idEntidad
	 * @throws SQLException
	 */
	private void doBackup(Connection connection, PrintStream printStreamSalidaMunicipio, PrintStream printStreamSalidaComun, PrintStream printStreamError, int idEntidad) throws SQLException {

		// Ponemos el título a la ventana: 	
		this.setTitle(getMessageI18N("geopista.app.backup.titulo"));

		// Creamos un area de texto para mostrar la información:
		JTextArea texto = new JTextArea(10,100);

		// Lo ponemos para que no se pueda escribir en él:
		texto.setEditable(false);

		// Creamos las barras de Scroll en el texto anterior:
		JScrollPane pScroll = new JScrollPane(texto, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Colocamos el texto en el panel: 
		getContentPane().add(pScroll, BorderLayout.CENTER);

		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		// Escribimos texto y hacemos un salto de linea:
		texto.append(getMessageI18N("geopista.app.backup.realizandoBackup") + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());
		texto.append(BackupConfig.MSGLINEA1 + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength()); 
		
		/**
		 * Verificamos la version de Postgres
		 */
		
		String version= backupAdapter.getVersion(connection);
		
		if (version.contains("PostgreSQL 8")){
			isVersion8=true;
		}
		else{
			isVersion8=false;
		}
		
		
		/*
		 * 1. Obtenemos las tablas comunes de las que hay que hacer backup
		 */
		InformacionTabla[] tablasComunes = backupAdapter.obtenerTablasComunes(connection,isVersion8);

		// Escribimos texto y hacemos un salto de linea:
		texto.append(getMessageI18N("geopista.app.backup.tablasComunes") + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());

		/*
		 * 2. Obtenemos las tablas dependientes del municipio de las que hay que
		 * hacer backup
		 */
		InformacionTabla[] tablasMunicipio = backupAdapter.obtenerTablasMunicipio(connection,isVersion8);

		// Escribimos texto y hacemos un salto de linea:
		texto.append(getMessageI18N("geopista.app.backup.tablasMunicipio") + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());

		printStreamSalidaComun.println(BackupConfig.SQLBEGIN);
		printStreamSalidaMunicipio.println(BackupConfig.SQLBEGIN);

		/*
		 * 3. Creamos el script de cosas comunes que puedan hacer falta para realizar el resto de las operaciones 
		 */
		backupAdapter.crearScriptInicial(connection, printStreamSalidaComun, printStreamError);

		texto.append(getMessageI18N("geopista.app.backup.creando") + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());

		/*
		 * 4. Creamos el script correspondiente a las tablas comunes
		 */

		if ((tablasComunes == null )||(tablasMunicipio == null)) {
			return;
		}

		texto.append(getMessageI18N("geopista.app.backup.scriptTablasComunes") + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());


		String municipios = backupAdapter.obtenerStringMunicipiosEntidad(connection, idEntidad);


		for (int i = 0; i < tablasComunes.length; i++) {
			InformacionTabla tabla = tablasComunes[i];
			String deshabilitarConstraints = backupAdapter.obtenerSentenciaDeshabilitarConstraints(tabla,connection);
			printStreamSalidaComun.println(deshabilitarConstraints);
			texto.append(deshabilitarConstraints + BackupConfig.NUEVALINEA);
			// Nos posicionamos en la última línea
			texto.setCaretPosition(texto.getDocument().getLength());        	
		}

		for (int i = 0; i < tablasComunes.length; i++) {
			InformacionTabla tabla = tablasComunes[i];
			backupAdapter.crearScript(connection, printStreamSalidaComun, printStreamError, tabla, false, municipios);
			texto.append(getMessageI18N("geopista.app.backup.creando")+" "+i+" "+getMessageI18N("geopista.app.backup.tablasComunes") + BackupConfig.NUEVALINEA);
			// Nos posicionamos en la última línea
			texto.setCaretPosition(texto.getDocument().getLength());  
		}

		for (int i = 0; i < tablasComunes.length; i++) {
			InformacionTabla tabla = tablasComunes[i];
			String habilitarConstraints = backupAdapter.obtenerSentenciaHabilitarConstraints(tabla,connection);
			printStreamSalidaComun.println(habilitarConstraints);
			texto.append(habilitarConstraints + BackupConfig.NUEVALINEA);
			// Nos posicionamos en la última línea
			texto.setCaretPosition(texto.getDocument().getLength());                       
		}

		/*
		 * Actualizamos las secuencias de las tablas comunes a los ultimos valores
		 */

		texto.append(getMessageI18N("geopista.app.backup.actualizandoTablasComunes") + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());

		for (int i = 0; i < tablasComunes.length; i++) {
			String sentencia = backupAdapter.obtenerSentenciaActualizarSecuencia(tablasComunes[i]);
			if (sentencia != null) {
				printStreamSalidaComun.println(sentencia);
				texto.append(sentencia + BackupConfig.NUEVALINEA);
				// Nos posicionamos en la última línea
				texto.setCaretPosition(texto.getDocument().getLength());  
			}
		}

		texto.append(getMessageI18N("geopista.app.backup.scriptTablasMunicipio") + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());

		for (int i = 0; i < tablasMunicipio.length; i++) {
			InformacionTabla tabla = tablasMunicipio[i];
			String deshabilitarConstraints = backupAdapter.obtenerSentenciaDeshabilitarConstraints(tabla,connection);
			printStreamSalidaMunicipio.println(deshabilitarConstraints);
			texto.append(deshabilitarConstraints + BackupConfig.NUEVALINEA);
			// Nos posicionamos en la última línea
			texto.setCaretPosition(texto.getDocument().getLength());              
		}       

		for (int i = 0; i < tablasMunicipio.length; i++) {
			InformacionTabla tabla = tablasMunicipio[i];
			backupAdapter.crearScript(connection, printStreamSalidaMunicipio, printStreamError, tabla, true, municipios);
			texto.append(getMessageI18N("geopista.app.backup.creando") + " " + i + " "+getMessageI18N("geopista.app.backup.tablasMunicipio")+ BackupConfig.NUEVALINEA);
			// Nos posicionamos en la última línea
			texto.setCaretPosition(texto.getDocument().getLength());              
		}      

		for (int i = 0; i < tablasMunicipio.length; i++) {
			InformacionTabla tabla = tablasMunicipio[i];
			String habilitarConstraints = backupAdapter.obtenerSentenciaHabilitarConstraints(tabla,connection);
			printStreamSalidaMunicipio.println(habilitarConstraints);
			texto.append(habilitarConstraints + BackupConfig.NUEVALINEA);
			// Nos posicionamos en la última línea
			texto.setCaretPosition(texto.getDocument().getLength());     

		}

		texto.append(getMessageI18N("geopista.app.backup.actualizandoTablasMunicipio") + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());

		for (int i = 0; i < tablasMunicipio.length; i++) {
			String sentencia = backupAdapter.obtenerSentenciaActualizarSecuencia(tablasMunicipio[i]);
			if (sentencia != null) {
				printStreamSalidaMunicipio.println(sentencia);
				texto.append(sentencia + BackupConfig.NUEVALINEA);
				// Nos posicionamos en la última línea
				texto.setCaretPosition(texto.getDocument().getLength());                  
			}
		}    
		texto.append(BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());     
		texto.append(BackupConfig.MSGLINEA2 + BackupConfig.NUEVALINEA);
		// Nos posicionamos en la última línea
		texto.setCaretPosition(texto.getDocument().getLength());     
		texto.append(getMessageI18N("geopista.app.backup.exito"));

		printStreamSalidaMunicipio.println(BackupConfig.SQLCOMMIT);
		printStreamSalidaComun.println(BackupConfig.SQLCOMMIT); 
	}   

	/**
	 * Generamos los ficheros en formato UTF-8 por si se quieren 
	 * @param directorySelected
	 */
	private void generarFicherosBackupUTF8(File directorySelected){
		try {
			File backupMunicipio = new File(directorySelected, BackupConfig.FILEENTIDAD);			
			File backupMunicipio_UTF8 = new File(directorySelected, BackupConfig.FILEENTIDAD_UTF8);
			backupMunicipio_UTF8.createNewFile();
			File backupComun = new File(directorySelected, BackupConfig.FILECOMUN);
			File backupComun_UTF8 = new File(directorySelected, BackupConfig.FILECOMUN_UTF8);
			backupComun.createNewFile();
			convertFile(backupMunicipio,backupMunicipio_UTF8);			        
			convertFile(backupComun,backupComun_UTF8);
		} catch (IOException e) {
			e.printStackTrace();
		}			        
	}

	/**
	 * Lo convertimos a UTF-8
	 * @param backupFile
	 * @param backupFileUTF8
	 */
	private void convertFile(File backupFile,File backupFileUTF8) {
		try {
			BufferedReader reader= new BufferedReader(new FileReader(backupFile));
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(backupFileUTF8), "UTF8"));
			String linea= reader.readLine();
			while(linea!=null) { 
				out.write(linea+"\n");
				linea= reader.readLine(); 
			}
			out.close();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
	}

}
