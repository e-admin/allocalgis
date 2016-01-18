/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */



package com.geopista.app.inicio;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextMap;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.catastro.GeopistaEditarCatastro;
import com.geopista.app.catastro.GeopistaEditarCatastroPanel;
import com.geopista.app.catastro.GeopistaFinImportacionPanel;
import com.geopista.app.catastro.GeopistaImportarFINCARUPanel;
import com.geopista.app.catastro.GeopistaImportarFINURBPanel;
import com.geopista.app.catastro.GeopistaImportarPadronFin;
import com.geopista.app.catastro.GeopistaImportarPadronFin2006;
import com.geopista.app.catastro.GeopistaImportarPadronRusticaFin;
import com.geopista.app.catastro.GeopistaImportarParcelas;
import com.geopista.app.catastro.GeopistaImportarParcelas02;
import com.geopista.app.catastro.GeopistaImportarParcelasMapa;
import com.geopista.app.catastro.GeopistaImportarRustica2005;
import com.geopista.app.catastro.GeopistaImportarUrbana2005;
import com.geopista.app.catastro.GeopistaImportarUrbana2006;
import com.geopista.app.catastro.GeopistaInformesCatastralesPanel;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.editor.GeopistaEditor;
import com.geopista.protocol.CConstantesComando;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;

public class CatastroInicioExtended extends JFrame implements HyperlinkListener, ActionListener {
	/**
	 * Logger for this class
	 */
	 private static final Log logger;
	    static {
	       createDir();
	       logger  = LogFactory.getLog(CatastroInicioExtended.class);
	    } 	
	//private static final Log logger = LogFactory.getLog(GeopistaInicio.class);

	private JEditorPane browser = new JEditorPane ( ) ; // The main HTML pane
//	private GeopistaEditor geopistaEditorBean;

	
//	Contexto de la aplicación
	private ApplicationContext appContext;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	public final static String LAST_IMPORT_DIRECTORY = "lastImportDirectory";
	public static final String LOCALGIS_LOGO = "geopista.gif";
	private Blackboard blackboardInformes = aplicacion.getBlackboard();
	private GeopistaEditor geo2 = null;
	private JScrollPane browserScrolled;
	
	private static boolean fromInicio=true;


    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }	
	public CatastroInicioExtended ( ) {
//		setTitle ("GEOPISTA") ;
		aplicacion.setMainFrame(this);  
		aplicacion.initHeartBeat(); // Forces to start HeartBeat
		// inicializa el contexto de aplicación para informar a todos
		// los componentes.

		appContext= new AppContext();
		//appContext.setEditor(geopistaEditorBean);
		appContext.setMainFrame(this);

		setBounds ( 0, 0 , 820, 620 ) ;
		browser.setEditable ( false ) ;
		browser.addHyperlinkListener ( this ) ;
		browser.setContentType("text/html");
		Container cp = this.getContentPane() ;
		cp.setLayout(new CardLayout());
		cp.add ( browserScrolled= new JScrollPane ( browser ) ,"primerplano") ;

		//ASO pongo el icono de geopista.
		ClassLoader cl = this.getClass().getClassLoader();
		java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(cl.getResource("img/geopista.gif"));
		setIconImage(img);
		//
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			jbInit();
		}
		catch(Exception e)
		{
			logger.error("GeopistaInicio()", e);
		}

		
		

	}
	
	public void login(){
		// ******************
		// Mostramos la pantalla de autenticación del usuario.
		// ******************
		// --------NUEVO-------------->
		if (SSOAuthManager.isSSOActive()) {
			com.geopista.app.administrador.init.Constantes.url = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
			CConstantesComando.loginGeopistaInicio = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
		}
		SSOAuthManager.ssoAuthManager("GeopistaInicio");
		if (!AppContext.getApplicationContext().isOnlyLogged()) {
			// -------FIN-NUEVO----------->
			if (fromInicio) {
				if (!showAuth()) {
					dispose();
					return;
				}
			} else {
				showAuth();
			}
			// --------NUEVO-------------->
		}
		// -------FIN-NUEVO----------->
		
		//setPolicy();
		
		if (!AppContext.seleccionarMunicipio((Frame) this)) {
			stopApp();
			return;
		}
	}
	
	private void stopApp() {
		JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
				"Inicio de aplicación cancelado. Se reiniciará el aplicativo");
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		System.exit(1);
		
	}
	
	public static void main (String[] args) {		

		String initURL="inicio.htm";
		String otherURL="";
		        
		if (args.length>0)
		{
			for (int i=0;i< args.length;i++)
			{
				if (args[i].equalsIgnoreCase("-URL"))
				{
					otherURL=args[i+1];
					break;
				}
			}
		
			if (!otherURL.equals(""))
			{
				URL direccion=HTMLLoader.getPath(otherURL);
				if (direccion!=null)
					initURL=otherURL;
			}
		}

		URL initPage= HTMLLoader.getPath(initURL);		
		CatastroInicioExtended appFrame = new CatastroInicioExtended();
		appFrame.show() ;

		logger.info("main() - mostrando pagina " + initURL + "= "
				+ initPage);

		appFrame.displayPage(initPage);
		appFrame.setTitle(":: LocalGIS ::");
		appFrame.setVisible(true);
		appFrame.setSize(850,660);

		appFrame.setVisible(true);

		appFrame.login();
	}
	
	public void displayPage ( URL page ) {
		try {
			browser.setPage(page);
		}
		catch ( Exception e1 ) {
			browser.setText ( "Could not load page:" + page + "\n" +
					"Error:" + e1.getMessage ( ) ) ;
			logger.debug("displayPage(page = " + page
					+ ") - No se puede cargar recurso imagen:");
		}
	}

	public void hyperlinkUpdate (HyperlinkEvent e) {
		if ( e.getEventType ( ) == HyperlinkEvent.EventType.ACTIVATED ) {
			try {
				String description = e.getDescription().substring(e.getDescription().lastIndexOf('/')+1);
				if (logger.isDebugEnabled())
				{
					logger.debug("hyperlinkUpdate(HyperlinkEvent)" + description);
				}
				URL url = e.getURL ( ) ;
				String url1 = url.toString();
				if (url1.endsWith("htm")) {
					browser.setPage ( url ) ;
				}else if (description.equals("noDisponible")){
					ResourceBundle rb = ResourceBundle.getBundle("Geopista");
					String mensaje = rb.getString("opcionNoDisponible");
					JOptionPane dialog = new JOptionPane();
					JOptionPane.showConfirmDialog( this, mensaje,":: LocalGIS ::", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null );
					this.getContentPane().add(dialog);
				}else if (description.equals("com.geopista.app.browser.GeopistaBrowser")){
					GeopistaBrowser.openURL(aplicacion.getString("geopista.conexion.guiaurbana","http://localhost:8080/guiaurbana/index.jsp"));

				//CATASTRO
				}else if (description.equals("com.geopista.app.catastro.GeopistaEditarCatastroPanel"))
				{
					if (aplicacion.isOnline())
					{
						GeopistaEditarCatastroPanel panel = new  GeopistaEditarCatastroPanel();
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}


				else if (description.equals("com.geopista.app.catastro.GeopistaImportarUrbana2005"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar Padrón Urbana 2005", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarUrbana2005(), new GeopistaImportarPadronFin()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}	
				}

				else if (description.equals("com.geopista.app.catastro.GeopistaImportarRustica2005"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar Padrón Rústica 2005", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarRustica2005(), new GeopistaImportarPadronRusticaFin()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (description.equals("com.geopista.app.catastro.GeopistaImportarUrbana2006"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar Padrón Urbana 2006", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarUrbana2006(), new GeopistaImportarPadronFin2006()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (description.equals("com.geopista.app.catastro.GeopistaInformesCatastralesPanel"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Catastro: Informes Catastrales", null);
						d.init(new WizardPanel[] {
								new GeopistaInformesCatastralesPanel()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}else if (description.equals("com.geopista.app.catastro.GeopistaConsultarCatastroPanel"))
				{

					if (aplicacion.isOnline()){
						/*WizardComponent d = new WizardComponent(appContext, "Catastro: Consulta Catastro", null);
           d.init(new WizardPanel[] {
                new GeopistaConsultarCatastroPanel()});
           showAppPanel(d);*/
						GeopistaEditarCatastro panel = new GeopistaEditarCatastro();
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}else  if (description.equals("ValidarCatastro"))
				{
				}
				else if (description.equals("com.geopista.app.catastro.GeopistaImportarFINURBPanel"))
				{
					if (aplicacion.isOnline())
					{   
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar FINURB", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarFINURBPanel(), new GeopistaFinImportacionPanel()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}
				else if (description.equals("com.geopista.app.catastro.GeopistaImportarFINCARUPanel"))
				{
					if(aplicacion.isOnline())
					{
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar FINCARU", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarFINCARUPanel(), new GeopistaFinImportacionPanel()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}
				else if (e.getDescription().equals("com.geopista.app.catastro.GeopistaImportarParcelas"))
				{
					if(aplicacion.isOnline())
					{
						WizardComponent d = new WizardComponent(appContext,
								"Catastro: Importar Parcelas", null);
						d.init(new WizardPanel[] {
								//new GeopistaImportarParcelas(), new GeopistaImportarParcelas02(), new GeopistaImportarParcelas03()});
								new GeopistaImportarParcelas(), new GeopistaImportarParcelasMapa(), new GeopistaImportarParcelas02()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}
				else if (e.getDescription().equals("com.geopista.app.catastro.gestorcatastral.MainCatastro")){
					
					MainCatastro catastro = new MainCatastro();
				}


				//GENERALES
				else if (url1.endsWith("Dialog")) {
					if (logger.isDebugEnabled())
					{
						logger.debug("hyperlinkUpdate(HyperlinkEvent) - Dialog");
					}
					JDialog dialog = (JDialog)Class.forName(e.getDescription()).newInstance();
					this.setVisible(false);
					dialog.setVisible(true);
					dialog.setLocation(150, 90);
				} else  if (url1.endsWith("Panel")) {
					JPanel otroD = (JPanel)Class.forName(e.getDescription()).newInstance();
					otroD.setVisible(true);
					otroD.setLocation(150, 90);
					this.setVisible(false);
				}else  if (url1.endsWith("Frame")) {
					if (logger.isDebugEnabled())
					{
						logger.debug("hyperlinkUpdate(HyperlinkEvent) - Frame");
					}
					JFrame otroD = (JFrame)Class.forName(e.getDescription()).newInstance();
					otroD.setVisible(true);
					otroD.setLocation(150, 90);
					this.setVisible(false);
				}
			}
			catch ( Exception exc ) {
				logger.error("hyperlinkUpdate(HyperlinkEvent)", exc);
			}
		}
	}
	
	
	/**
	 *
	 */
	private WizardComponent old;
	private void showAppPanel(WizardComponent d)
	{
		if(!d.isCanceled())
		{
			if (old!=null)
				getContentPane().remove(old);
			getContentPane().add(d,"appPanel");
			((CardLayout)getContentPane().getLayout()).show(getContentPane(),"appPanel");
			old=d;
			
			//AppContext.seleccionarMunicipio((Frame)this);
		}
	}
	
	/*
	 * No está documentado que hace esto.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed ( ActionEvent e ) {
		URL page ;
		try
		{
			page = new URL("");
			displayPage ( page ) ;
		} catch (MalformedURLException e1)
		{
			browser.setText ( "Page could not be loaded:\n" +
					"Error:" + e1.getMessage ( ) ) ;
		}

	}

	private void jbInit() throws Exception
	{
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt){System.exit(0);}
		});

	}

	private void launchIReport(){
		// Obtengo la ruta donde estan las librerias .jar necesarias para
		// lanzar la aplicacion a partir del classpath actual
		
		System.out.println("Iniciando la invocacion de iReport");
		
		char cpSeparator = System.getProperty( "path.separator" ).charAt(0);
		String classPath = System.getProperty( "java.class.path" );
		String[] classPathEntries = classPath.split(Character.toString(cpSeparator));
		
		JOptionPane.showMessageDialog(this, "Classpath actual: " + classPath);
		JOptionPane.showMessageDialog(this, "Libarypath actual: " + System.getProperty( "java.library.path" ));
		
		String baseLibPath = null;
		
		for (int i  = 0 ; i < classPathEntries.length; i++){
			String classPathEntry = classPathEntries[i];
			int index = classPathEntry.indexOf("productos");
			if (index != -1){
				int endIndex = index - 1;
				if (endIndex > 0){
					baseLibPath = classPathEntry.substring(0, endIndex);
					//break;
				}
			}
		}
		
		String libPath = baseLibPath + File.separator + "productos" +
			File.separator + "ireport";
		
		System.out.println("Ruta base con las librerias de ireport: " + libPath);
		JOptionPane.showMessageDialog(this,"Ruta base con las librerias de ireport: " + libPath);
		
		StringBuffer sbClassPath = new StringBuffer();
		
		File productClassPathDir = new File(libPath);
		String[] fileList = productClassPathDir.list();

		if (fileList != null){
			for (int i = 0; i < fileList.length; i++){
				if (fileList[i].endsWith(".jar") || fileList[i].endsWith(".zip")){
					if (i != 0){
						sbClassPath.append(cpSeparator + libPath + File.separator 
								+ fileList[i]);
					}
					else {
						sbClassPath.append(libPath + File.separator + fileList[i]);
					}
				}
			}
		}
		
		String jre15Home = aplicacion.getUserPreference(
				AppContext.PREFERENCES_JRE15_HOME_KEY,
				AppContext.JRE15_DEFAULT_HOME, true);
		
		String iReportHome = aplicacion.getUserPreference(
				AppContext.PREFERENCES_DATA_PATH_KEY,
				AppContext.DEFAULT_DATA_PATH, true);
		
		String[] cmdarray = new String[5];
		cmdarray[0] = jre15Home + File.separator + "bin" + File.separator + "java.exe";
		cmdarray[1] = "-cp";
		cmdarray[2] = "\"" + sbClassPath.toString() + "\"";
		cmdarray[3] = "-Direport.home=\"" + iReportHome + "\"";
		cmdarray[3] = cmdarray[3].replaceAll("\\\\", "\\\\\\\\");
		cmdarray[4] = "com.geopista.reports.launcher.Launcher";
		
		System.out.println("Ejecutable de java: " + cmdarray[0]);
		System.out.println("Classpath: " + cmdarray[2]);
		System.out.println("iReport Home: " + cmdarray[3]);
		System.out.println("Clase principal: " + cmdarray[4]);		
		
		File workDirFile = new File(iReportHome);
		try {
			Process proc = Runtime.getRuntime().exec(cmdarray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Comprueba que el usuario esta autorizado para acceder a la aplicacion y
	 * sus permisos para las diferentes acciones.
	 */
	private  boolean showAuth() {
		try {
			boolean resultado = false;
			
			//resetSecurityPolicy();
			CAuthDialog auth = new com.geopista.app.utilidades.CAuthDialog(
					this, true, CConstantesComando.loginGeopistaInicio,
					"GeopistaInicio",
					0,
					aplicacion.getI18NResource());
			/*
			 * FUNCIONA com.geopista.app.utilidades.CAuthDialog auth = new
			 * com.geopista.app.utilidades.CAuthDialog(this, true,
			 * CConstantesComando.loginLicenciasUrl,Constantes.idApp,
			 * ConstantesLocalGISEIEL.IdEntidad, aplicacion.getI18NResource());
			 */
			// // CAuthDialog auth = new CAuthDialog(this, true,
			// ConstantesLocalGISEIEL.localgisEIEL,ConstantesLocalGISEIEL.idApp,
			// ConstantesLocalGISEIEL.IdMunicipio,
			// aplicacion.getI18NResource());
			auth.setBounds(30, 60, 315, 155);
			if (fromInicio) {
				resultado = auth.showD(true);
				if (!resultado)
					return false;
			} else {
				auth.show();
			}
			// com.geopista.security.SecurityManager.setHeartBeatTime(1000000);
			// CAMBIADO
			/*
			 * com.geopista.security.SecurityManager.setHeartBeatTime(10000);
			 * GeopistaAcl acl =
			 * com.geopista.security.SecurityManager.getPerfil(
			 * "EIEL");//TODO:CAMBIAR cuando exista el ACL EIEL
			 * applySecurityPolicy(acl,
			 * com.geopista.security.SecurityManager.getPrincipal());
			 */
			//TOTOD setPolicy();

			return true;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR al autenticar al usuario " + sw.toString());
			JOptionPane optionPane = new JOptionPane(
					"Error al inicializar: \n"
							+ ((e.getMessage() != null && e.getMessage()
									.length() >= 0) ? e.getMessage()
									: e.toString()), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.setVisible(true);
			return false;
		}
	}


}
