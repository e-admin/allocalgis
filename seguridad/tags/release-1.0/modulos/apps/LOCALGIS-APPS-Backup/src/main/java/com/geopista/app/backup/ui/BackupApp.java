/**
 * Módulo para la creación de Backup en modo gráfico
 *
*/
package com.geopista.app.backup.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.acl.AclNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.log4j.PropertyConfigurator;
import org.exolab.castor.xml.Marshaller;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.backup.BackupAdapter;
import com.geopista.app.backup.BackupConfig;
import com.geopista.app.backup.BackupOracle;
import com.geopista.app.backup.BackupPostgres;
import com.geopista.app.backup.BackupPreferences;
import com.geopista.app.backup.InformacionTabla;
import com.geopista.app.backup.SecurityManager;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.protocol.CConstantesComando;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.server.DBPropertiesStore;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.administradorCartografia.ACQuery;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.ui.dialogs.LoginDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.app.backup.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.geopista.app.utilidades.CMain;
import com.geopista.consts.config.DBConstants;

public class BackupApp extends CMain{
	
    private BackupAdapter backupAdapter;
    private AppContext aplicacion;
    public static final String idApp = "BackupApp";
    private JDesktopPane desktopPane;
    private JPanel jPanelStatus = null;
	public static final int DIM_X = 800;
	public static final int DIM_Y = 600;
    public static final String LOCALGIS_LOGO = "geopista.gif";
    public static int IdMunicipio= 34083;
    public static ResourceBundle literales = null;
	private boolean fromInicio;
    private final BackupPreferences backupPreferences = BackupPreferences.getInstance();
    
    public static final int CREAR_USUARIO = 0;
	public static final int BORRAR_USUARIO = 1;
	public static final String USUARIO = "usuario";
	public static final String PASSWORD = "password";
	public static final String	mensajeXML	= "mensajeXML";
    
	private JLabel connectionLabel = new JLabel();
	private JLabel helpLabel = new JLabel();
	private JMenuBar jMenuBarEIEL = null;
	private JMenu jMenuMapasEIEL = null;
	private JMenu jMenuDominios = null;
	private JMenu jMenuEstadisticas = null;
	private JMenu jMenuAyuda = null;
	private JMenuItem jMenuItemCargarMapaTematicoViviendas = null;
	private JMenuItem contenidoAyudaMenuItem = null;
	private JMenuItem jMenuItemAcercaDe = null;;
	private JMenuItem jMenuItemEditorDominiosEIEL = null;
	private JMenuItem jMenuItemIndicadores = null;
	
	private LoginDialog dial = null;

    public static void main(String[] args) {

    	BackupApp backupApp = new BackupApp();
    	backupApp.execute();
    	backupApp.dispose();
    	System.exit(0);

    }
    
    public BackupApp() {
    	
    	this.fromInicio=false;
    
    }
    
    public void execute() {
        System.out.println("Inicio Backup...");
        String url = backupPreferences.getUrl();
        
        /*
         *  Determinamos a traves de la información de preferencia 
         *  el tipo de base de datos de geopista y en funcion de 
         *  ello instanciamos un tipo de backup u otro
         *  
         */
        
        if (url.indexOf("jdbc:postgresql")>=0) {
            backupAdapter = new BackupPostgres();
        }else { 
        	if (url.indexOf("jdbc:oracle")>=0) {
        		backupAdapter = new BackupOracle();
                    
            }
        }
        /*
         * Cargamos la clase del driver que vamos a usar para
         * la conexión jdbc
         * 
         */
        try {
            Class.forName(backupAdapter.obtenerClaseDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        
        /*
         * Inicializamos el interfaz de usuario
         * 
         */
        
        
        
        //******************
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		aplicacion= (AppContext)AppContext.getApplicationContext();
		aplicacion.setMainFrame(this);
		System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.showGrowBox", "true");
        if (UIManager.getLookAndFeel() != null
                && UIManager.getLookAndFeel().getClass().getName().equals(UIManager.getSystemLookAndFeelClassName())) {
            return;
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        //XXXXXXXXXXXXXXXXXXXX Parte autenticación nueva XXXXXXXXXXXXXXXXXXXX
        
		SplashWindow splashWindow = showSplash();

		Locale loc= I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.backup.ui.language.BackupSupraMunicipali18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("BackupSupraMunicipal", bundle);

		inicializaElementos();                
		configureApp();

		this.setIconImage(IconLoader.icon(LOCALGIS_LOGO).getImage());
		this.setTitle(I18N.get("BackupSupraMunicipal","geopista.app.backup.titulo"));
		try
		{
					
			show();

			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (splashWindow != null)
			{
				splashWindow.setVisible(false);
			}
			
	        //******************
	        //Mostramos la pantalla de autenticación del usuario.
	        //******************
			//--------NUEVO-------------->	
			if(SSOAuthManager.isSSOActive())
				CConstantesComando.loginLicenciasUrl = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
			SSOAuthManager.ssoAuthManager(BackupApp.idApp);								
            if (!AppContext.getApplicationContext().isOnlyLogged()){  
            //-------FIN-NUEVO----------->
            showAuth();
            //--------NUEVO-------------->
            }
            //-------FIN-NUEVO----------->  
            
            //TODO 
            //setPolicy();

		}
		catch (Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("ERROR:" + sw.toString());
			//logger.error("Exception: " + sw.toString());
		}
        
     //XXXXXXXXXXXXXXXXXXXX Fin Parte autenticación nueva XXXXXXXXXXXXXXXXXXXX
     
     
        //*******************************
        //Cargamos los recursos
        //*******************************
        //Locale loc = I18N.getLocaleAsObject();         
        //ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.backup.ui.language.BackupSupraMunicipali18n", loc, this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("BackupSupraMunicipal", bundle);
        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
    
        String urlServlet = aplicacion.getString("geopista.conexion.servidorurl")+"Backup/BackupServlet";
        
        //Creamos rol de usuario
        try {
			crearUsuario(urlServlet,dial.getLogin(),dial.getPassword());
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			System.out.println("No se puede crear el rol especificado: "+ e2.getMessage());
			e2.printStackTrace();
		}
             
        //Comentamos esta parte ya que es el login de la BBDD
        
//        LoginDialog dialog = new LoginDialog(this);
//        centreOnScreen(dialog);
//        dialog.setVisible(true);
//        int i = 0;
//        boolean isAuthorized = false;
//        while (!dialog.isCanceled()&&(i<3)&&(!isAuthorized)) {
//            isAuthorized = SecurityManager.isAuthorized(dialog.getLogin(), dialog.getPassword());
//            if (!isAuthorized) {
//                dialog.setErrorLabel(I18N.get("BackupSupraMunicipal", "geopista.app.backup.noAutorizado"));
//                dialog.setVisible(true);
//            }
//            i++;
//        }
//        if (!isAuthorized) {
//            return;
//        }
//        backupPreferences.setUser(dialog.getLogin());
//        backupPreferences.setPassword(dialog.getPassword());
        
        //Conectamos a la BBDD con el rol creado
        Connection connection = CPoolDatabase.getConnection(dial.getLogin(),dial.getPassword());
        //Connection connection = getConnection();
        if (connection==null) {
            return;
        }   
        //dialog = null;             
        
	        String[] entidades = new String[0];
	        Hashtable hashtable = null;
	        try {
	            hashtable = backupAdapter.obtenerEntidades(connection);
	            hashtable.put(I18N.get("BackupSupraMunicipal", "geopista.app.backup.todasEntidades"), new Integer(-1));
	            Collection collection = hashtable.keySet();
	            entidades = (String[])collection.toArray(entidades);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        EntidadDialog entidadDialog = new EntidadDialog(this, entidades);
	        centreOnScreen(entidadDialog);
	        entidadDialog.setVisible(true);
	        if (entidadDialog.isCancelled()) {
	            return;
	        }
	        String entidadSelected = entidadDialog.getEntidadSelected();
	        Integer id = (Integer) hashtable.get(entidadSelected);
	        try {
	
	            File directorySelected = entidadDialog.getDirectorySelected();
	            System.out.println(directorySelected.getAbsolutePath());
	
	            File error = new File(directorySelected, BackupConfig.FILEERROR);
	
				error.createNewFile();
	
	            File backupMunicipio = new File(directorySelected, BackupConfig.FILEENTIDAD);
	            backupMunicipio.createNewFile();
	            
	            File backupComun = new File(directorySelected, BackupConfig.FILECOMUN);
	            backupComun.createNewFile();
	
	            FileOutputStream backupErrorFile = new FileOutputStream(error);
	            FileOutputStream backupMunicipioFile = new FileOutputStream(backupMunicipio);
	            FileOutputStream backupComunFile = new FileOutputStream(backupComun);
	
	            PrintStream printStreamError = new PrintStream(backupErrorFile);
	            PrintStream printStreamSalidaMunicipio = new PrintStream(backupMunicipioFile);
	            PrintStream printStreamSalidaComun = new PrintStream(backupComunFile);
	
	            doBackup(connection, printStreamSalidaMunicipio, printStreamSalidaComun, printStreamError, id.intValue());
	
	            entidadDialog = null;
	
	            backupErrorFile.close();
	            printStreamError.close();
	
	            generarFicherosBackupUTF8(directorySelected);
	            
	            connection.close();
	            JOptionPane.showMessageDialog(this, I18N.get("BackupSupraMunicipal", "geopista.app.backup.backupFinalizado"), I18N.get("BackupSupraMunicipal", "geopista.app.backup.informacion"), JOptionPane.INFORMATION_MESSAGE);           
			}   catch (Exception e)  {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, I18N.get("BackupSupraMunicipal", "geopista.app.backup.backupAbortado"), I18N.get("BackupSupraMunicipal", "geopista.app.backup.error"), JOptionPane.ERROR_MESSAGE);
			} 
	        System.out.println("...Fin Backup."); 
	        
	        //Una vez creado el backup, borramos el rol
	        try {
				borrarUsuario(urlServlet,dial.getLogin(),dial.getPassword());
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				System.out.println("No se puede borrar el rol especificado: "+ e2.getMessage());
				e2.printStackTrace();
			}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			        
    }
    
    /**
     * Lo convertimos a UTF-8
     * @param backupFile
     * @param backupFileUTF8
     */
    private void convertFile(File backupFile,File backupFileUTF8){
    	
    	try {
			BufferedReader reader= new BufferedReader(new FileReader(backupFile));
			
			Writer out = new BufferedWriter(new OutputStreamWriter(
		            new FileOutputStream(backupFileUTF8), "UTF8"));

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
    private  void centreOnScreen(Component componentToMove) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        componentToMove.setLocation((screenSize.width -
            componentToMove.getWidth()) / 2,
            (screenSize.height - componentToMove.getHeight()) / 2);
    }
    
    private Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(backupPreferences.getUrl(),backupPreferences.getUser(),backupPreferences.getPassword());
            return connection;
        } catch (SQLException e) {          
            e.printStackTrace();
        }
        return null;
    }
    
      
    private void doBackup(Connection connection, PrintStream printStreamSalidaMunicipio, PrintStream printStreamSalidaComun, PrintStream printStreamError, int idEntidad) throws SQLException {
    	
    	// Ponemos el título a la ventana: 	
    	this.setTitle(I18N.get("BackupSupraMunicipal", "geopista.app.backup.titulo"));
    	
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
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.realizandoBackup") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
        texto.append(BackupConfig.MSGLINEA1 + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength()); 
        /*
         * 1. Obtenemos las tablas comunes de las que hay que hacer backup
         */
        InformacionTabla[] tablasComunes = backupAdapter.obtenerTablasComunes(connection);
        
        // Escribimos texto y hacemos un salto de linea:
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasComunes") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
    	
        /*
         * 2. Obtenemos las tablas dependientes del municipio de las que hay que
         * hacer backup
         */
        InformacionTabla[] tablasMunicipio = backupAdapter.obtenerTablasMunicipio(connection);

        // Escribimos texto y hacemos un salto de linea:
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasMunicipio") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
 
  	 	printStreamSalidaComun.println(BackupConfig.SQLBEGIN);
  	 	printStreamSalidaMunicipio.println(BackupConfig.SQLBEGIN);
  	 	
  	 	/*
  	 	 * 3. Creamos el script de cosas comunes que puedan hacer falta para realizar el resto de las operaciones 
  	 	 */
        backupAdapter.crearScriptInicial(connection, printStreamSalidaComun, printStreamError);
        
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.creando") + BackupConfig.NUEVALINEA);
        // Nos posicionamos en la última línea
    	texto.setCaretPosition(texto.getDocument().getLength());
    	    	
        /*
         * 4. Creamos el script correspondiente a las tablas comunes
         */
        
        if ((tablasComunes == null )||(tablasMunicipio == null)) {
            return;
        }
        
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.scriptTablasComunes") + BackupConfig.NUEVALINEA);
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
            texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.creando")+" "+i+" "+I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasComunes") + BackupConfig.NUEVALINEA);
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
        
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.actualizandoTablasComunes") + BackupConfig.NUEVALINEA);
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

        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.scriptTablasMunicipio") + BackupConfig.NUEVALINEA);
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
            texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.creando")+" "+i+" "+I18N.get("BackupSupraMunicipal", "geopista.app.backup.tablasMunicipio")+ BackupConfig.NUEVALINEA);
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

        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.actualizandoTablasMunicipio") + BackupConfig.NUEVALINEA);
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
        texto.append(I18N.get("BackupSupraMunicipal", "geopista.app.backup.exito"));
        
        printStreamSalidaMunicipio.println(BackupConfig.SQLCOMMIT);
        printStreamSalidaComun.println(BackupConfig.SQLCOMMIT); 
    }    
    
//	private boolean showAuth() {
//	       try
//	       {
//	    	   boolean resultado=false;
//	    	   com.geopista.app.utilidades.CAuthDialog auth =
//	                   new com.geopista.app.utilidades.CAuthDialog(this, true,
//	                           CConstantesComando.loginLicenciasUrl,BackupApp.idApp,
//	                           34083, literales);
//			    auth.setBounds(30, 60, 315, 155);
//	            if (fromInicio){
//	            	resultado=auth.showD(true);
//	            	if (!resultado)
//	            		return false;
//	            }
//	            else{
//	            	auth.show();
//	            }
//	        } catch(Exception e)
//	        {
//	             //logger.error("ERROR al autenticar al usuario ",e);
//	             JOptionPane optionPane = new JOptionPane("Error al inicializar: \n"
//	                    +((e.getMessage()!=null && e.getMessage().length()>=0)?e.getMessage():e.toString()), JOptionPane.ERROR_MESSAGE);
//	             JDialog dialog = optionPane.createDialog(this, "ERROR");
//		         dialog.show();
//	            return false;
//	        }
//			return true;
//		}
    
	/**
	 * Comprueba que el usuario esta autorizado para acceder a la aplicacion y sus permisos para las diferentes
	 *  acciones.
	 */
	@SuppressWarnings("deprecation")
	private boolean showAuth()
	{
		try
		{
		
			if (!aplicacion.isOnlyLogged()){
		    	   com.geopista.app.utilidades.CAuthDialog auth =
                new com.geopista.app.utilidades.CAuthDialog(this, true,
                        CConstantesComando.loginLicenciasUrl,BackupApp.idApp,
                        34083, aplicacion.getI18NResource());
				auth.setBounds(30, 60, 315, 155);
				dial = auth.showLogPass();

			}

//			int cont = 0;
//			while(!aplicacion.isOnlyLogged() && cont<=3)
//			{                     
//				cont++;
//				if (cont>3)
//				{              
//					System.exit(0);  
//				}            
//				aplicacion.login(); 
//			}
			com.geopista.security.SecurityManager.setHeartBeatTime(50000);
//			GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("EIEL");
//			if (!applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal())){
//				showAuth();
//			}
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			//logger.error("ERROR al autenticar al usuario "+sw.toString());
			JOptionPane optionPane = new JOptionPane("Error al inicializar: \n"
					+((e.getMessage()!=null && e.getMessage().length()>=0)?e.getMessage():e.toString()), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.show();
			return false;
		}
		return true;
	}
	/**
	 * Funcion que inicializa la gui de la pantalla principal, asociandole eventos para que tenga un tamaño minimo y
	 * la accion de cierre.
	 */
	private void inicializaElementos()
	{	

		desktopPane= new JDesktopPane();
		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);	        

		//Creacion de objetos.
		getContentPane().add(getJPanelStatus(), BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		//Evento para que la ventana al minimizar tenga un tamaño minimo.
		addComponentListener(new java.awt.event.ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				ajustaVentana(aplicacion.getMainFrame(), DIM_X, DIM_Y+50);
			}
		});

		//Evento al cerrar la ventana.
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				exitForm(evt);
			}
		});

		setConnectionInitialStatusMessage(aplicacion.isOnline());
		aplicacion.addAppContextListener(new AppContextListener()
		{

			public void connectionStateChanged(GeopistaEvent e)
			{
				switch (e.getType())
				{
				case GeopistaEvent.DESCONNECTED:
					setConnectionStatusMessage(false);
					break;
				case GeopistaEvent.RECONNECTED:
					setConnectionStatusMessage(true);
					break;                    
				}
			}
		});
	}
	/**
	 * Metodo que inicializa los parametros principales de la aplicacion para conectarse con el servidor, inicializa
	 * el locale, y actualiza el idMunicipio segun las variables de entorno.
	 */
	private boolean configureApp()
	{
		try
		{
			try
			{
				PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);

			}catch(Exception e){};

			try
			{

//				ConstantessLocalGISObraCivil.localgisObraCivil = aplicacion.getString("geopista.conexion.servidorurl")+"eiel";
//				ConstantesLocalGISEIEL.clienteLocalGISEIEL = new LocalGISEIELClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
//				"/EIELServlet");
				//-------NUEVO----------------->
				if(Boolean.valueOf(AppContext.getApplicationContext().getString(SSOConstants.SSO_AUTH_ACTIVE)))
					Constantes.url = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
				else
					Constantes.url= aplicacion.getString("geopista.conexion.servidorurl")+"administracion";
		    	//-------FIN-NUEVO------------->
//				ConstantesLocalGISEIEL.Locale = aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");

				try
				{
					//Constantes.idMunicipio = 34083;
					IdMunicipio = new Integer(aplicacion.getString("geopista.DefaultCityId")).intValue();
				}
				catch (Exception e)
				{
					//mostrarMensajeDialogo( "Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
					System.out.println("Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
					//logger.error("Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
					System.exit(-1);
				}
			}
			catch(Exception e)
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				//mostrarMensajeDialogo( "Excepcion al cargar el fichero de configuración:\n"+sw.toString());
				//logger.error("Exception: " + sw.toString());
				System.exit(-1);
				return false;
			}
			return true;
		}
		catch (Exception ex)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			//logger.error("Exception: " + sw.toString());
			return false;
		}
	}
	
	private JPanel getJPanelStatus ()
	{
		if (jPanelStatus == null)
		{
			jPanelStatus = new JPanel(new GridBagLayout());

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

			connectionLabel.setBorder(new javax.swing.border.SoftBevelBorder(
					javax.swing.border.SoftBevelBorder.LOWERED));
			jPanelStatus.add(connectionLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0,
							0, 0), 0, 0));

			jPanelStatus.add(new JPanel(),
					new GridBagConstraints(1, 0, 4, 1, 1.0, 1.0,
							GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
							new Insets(5, 0, 5, 0), 0, 0));     

			jPanelStatus.add(helpLabel, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0,
							0, 0), 0, 0));
		}
		return jPanelStatus;
	}
	
	/**
	 * Metodo que no permite que la pantalla se minimize mas de 600 en largo y del ancho pasado por parametro.
	 *
	 * @param desktop La ventana a analizar.
	 * @param wid El ancho pasado.
	 * @param hig El largo pasado.
	 */
	public static void ajustaVentana(JFrame desktop, int wid, int hig)
	{
		if (desktop.getWidth()<wid || desktop.getHeight()<600)
		{
			desktop.setSize(wid, hig);
		}
	}

	/**
	 * Acciones realizadas cuando se cierra la aplicacion.
	 *
	 * @param evt El evento capturado.
	 */
	private void exitForm(java.awt.event.WindowEvent evt)
	{
		try
		{
			//-----NUEVO----->
        	if(!Boolean.valueOf(AppContext.getApplicationContext().getString(SSOConstants.SSO_AUTH_ACTIVE)))
        		com.geopista.security.SecurityManager.logout();
        	//---FIN NUEVO--->			
		}
		catch (Exception ex)
		{
			//logger.warn("Exception: " + ex.toString());
		}
		System.exit(0);
	}
	
	public void setConnectionInitialStatusMessage (boolean connected){
		if (!connected)
		{
			connectionLabel.setIcon(IconLoader.icon("no_network.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OffLineStatusMessage"));


		} else
		{
			connectionLabel.setIcon(IconLoader.icon("online.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OnLineStatusMessage"));

		}
	}
	
	public void setConnectionStatusMessage(boolean connected)
	{
		if (!connected)
		{

			connectionLabel.setIcon(IconLoader.icon("no_network.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OffLineStatusMessage"));

			if (aplicacion.isLogged())
				SecurityManager.unLogged();
			else
				aplicacion.login();

		} else
		{
			connectionLabel.setIcon(IconLoader.icon("online.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OnLineStatusMessage"));

			if (!aplicacion.isLogged())
				showAuth();            

		}
	}
    
	public static void menuBarSetEnabled(boolean b, JFrame desktop)
	{
		try
		{           
			Component[] c = desktop.getJMenuBar().getComponents();
			for (int i=0; i<c.length; i++)
			{
				if (c[i] instanceof JMenu)
				{
					c[i].setEnabled(b);
				}
			}

			if (b)
			{
				GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("RegistroExpedientes");
				//((LocalGISGestionCiudad)desktop).applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
			}

		} catch (Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
		}
	}
	
    /**
     * Creamos un rol en la BBDD
     * @throws Exception
     */
    public void crearUsuario(String url, String usuario, String password) throws Exception
    {
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(CREAR_USUARIO);
        params.put(USUARIO,usuario);
        params.put(PASSWORD,password);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviar(url, swQuery.toString(),usuario, password);
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
            	System.out.println("crearUsuario()" + ode.getMessage());
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }
    
    /**
     * Borramos un rol en la BBDD
     * @throws Exception
     */
    public void borrarUsuario(String url, String usuario, String password) throws Exception
    {
        ACQuery query= new ACQuery();
        Hashtable params= new Hashtable();
        query.setAction(BORRAR_USUARIO);
        params.put(USUARIO,usuario);
        params.put(PASSWORD,password);
        query.setParams(params);
        StringWriter swQuery= new StringWriter();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(query);
        Marshaller.marshal(new ACMessage(baos.toByteArray()), swQuery);
        InputStream in= enviar(url, swQuery.toString(),usuario, password);
        ObjectInputStream ois= new ObjectInputStream(in);
        try
        {
            readObject(ois);
        }
        catch(OptionalDataException ode)
        {
            if (ode.eof!=true)
            {
            	System.out.println("borrarUsuario()" + ode.getMessage());
            }
        }
        catch (EOFException ee)
        {
        }
        finally
        {
            try{ois.close();}catch(Exception e){};
        }
    }
    
    /**
     * Envía la consulta al servidor.
     * @param sUrl url a la que enviar la consulta.
     * @param sMensaje mensaje a enviar.
     * @param sUserName nombre del usuario.
     * @param sPassword password del usuario.
     * @return InputStream
     * @throws Exception
     */
    private static InputStream enviar(String sUrl, String sMensaje, String sUserName, String sPassword) throws Exception
    {
		Credentials creds = null;
		if (sUserName != null && sPassword != null)
			creds = new UsernamePasswordCredentials(sUserName, sPassword);
        else
        {
			if (com.geopista.security.SecurityManager.getIdSesion() == null){
				System.out.println("enviar(String, String, String, String) - Usuario no autienticado");
                creds = new UsernamePasswordCredentials("GUEST", "");
            }else
               creds = new UsernamePasswordCredentials(com.geopista.security.SecurityManager.getIdSesion(), com.geopista.security.SecurityManager.getIdSesion());
		}
		//create a singular HttpClient object		
		org.apache.commons.httpclient.HttpClient client= AppContext.getHttpClient();

		//establish a connection within 5 seconds
		client.setConnectionTimeout(5000);

		//set the default credentials
		if (creds != null)
        {
			client.getState().setCredentials(null, null, creds);
		}
       
        /* -- MultipartPostMethod -- */
        org.apache.commons.httpclient.methods.MultipartPostMethod method= new org.apache.commons.httpclient.methods.MultipartPostMethod(sUrl);

//        if (com.geopista.security.SecurityManager.getIdMunicipio()!=null)
//        {
//             method.addParameter(idMunicipio, SecurityManager.getIdMunicipio());
//        }
//        if (SecurityManager.getIdApp()!=null)
//        {
//			 method.addParameter(IdApp, SecurityManager.getIdApp());
//        }
        if (sMensaje!=null)
        {
            method.addPart(new org.apache.commons.httpclient.methods.multipart.StringPart(mensajeXML, sMensaje, "ISO-8859-1"));
        }

        /** Necesario para la aplicacion de Inventario. En la aplicacion se pasa un vector files a insertar o modificar */
        //TODO esto mirarlo y quitarlo si no es necesario.
       

        method.setFollowRedirects(false);

		//execute the method
		byte[] responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBody();
		} catch (HttpException he) {
			throw new Exception("Http error connecting to '" + sUrl + "'" + he.getMessage());
		} catch (IOException ioe) {
			System.out.println("enviar(String, String, String, String) - Unable to connect to '"
							+ sUrl + "'");
			throw new Exception("Unable to connect to '" + sUrl + "'" + ioe.getMessage());
		}
		System.out.println("enviar(String, String, String, String) - Request Path: "
							+ method.getPath());
		System.out.println("enviar(String, String, String, String) - Request Query: "
							+ method.getQueryString());

		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
			System.out.println("enviar(String, String, String, String)"
							+ requestHeaders[i]);
		}

		//write out the response headers
		System.out.println("enviar(String, String, String, String) - *** Response ***");
		System.out.println("enviar(String, String, String, String) - Status Line: "
							+ method.getStatusLine());

        int iStatusCode = method.getStatusCode();
        String sStatusLine=method.getStatusLine().toString();
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++)
        {
			System.out.println("enviar(String, String, String, String)"
						+ responseHeaders[i]);
		}
		//clean up the connection resources
		method.releaseConnection();
		method.recycle();
        if (iStatusCode==200)
        {
	        return new GZIPInputStream(new ByteArrayInputStream(responseBody));
        }
        else
            throw new Exception(sStatusLine);
	}


    /**
     * Lee la respuesta del servidor.
     * @param ois ObjetInputStream
     * @return Object devuelto por el servidor.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ACException
     */
    private Object readObject(ObjectInputStream ois) throws IOException,ClassNotFoundException, ACException
    {
       Object oRet=null;
       oRet=ois.readObject();
       if (oRet instanceof ACException)
       {
           throw (ACException)oRet;
       }
       return oRet;
   }
    
    //TODO 
   /* private void setPolicy() throws AclNotFoundException, Exception{
   	 com.geopista.security.SecurityManager.setHeartBeatTime(5000);
        GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("RegistroExpedientes");
        applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
   }*/
    
    
}
