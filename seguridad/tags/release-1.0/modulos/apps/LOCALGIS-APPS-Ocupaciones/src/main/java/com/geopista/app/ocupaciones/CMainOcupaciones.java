package com.geopista.app.ocupaciones;

import com.geopista.app.ocupaciones.images.IconLoader;
import com.geopista.app.ocupaciones.Estructuras;
import com.geopista.app.ocupaciones.CConsultaEventos;
import com.geopista.app.ocupaciones.CUltimosEventos;
import com.geopista.app.ocupaciones.CMantenimientoHistorico;
import com.geopista.app.ocupaciones.CGeneracionInformes;
import com.geopista.app.ocupaciones.CNotificaciones;
import com.geopista.app.ocupaciones.CGeneracionPlanos;
import com.geopista.app.ocupaciones.TareasPendientesJInternalFrame;
import com.geopista.app.utilidades.CMain;
import com.geopista.app.ocupaciones.panel.CAcercaDeJDialog;
import com.geopista.app.ocupaciones.panel.JDialogConfiguracion;
import com.geopista.app.AppContext;
import com.geopista.app.AppContextMap;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.licencias.CConstantesPaths;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.ui.dialogs.MunicipioDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;

import org.agil.core.catalogo.Aplicacion;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.acl.AclNotFoundException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Hashtable;

/*
 * GeoPistaMDI.java
 *
 * Created on 13 de febrero de 2004, 11:38
 */

/**
 * @author avivar
 */
//public class CMainOcupaciones extends javax.swing.JFrame {
public class CMainOcupaciones extends CMain {

	//static Logger logger = Logger.getLogger(CMainOcupaciones.class);
	 private static Logger logger;
	    static {
	       createDir();
	       logger  = Logger.getLogger(CMainOcupaciones.class);
	    }		
    private static final String resourceName="config.ocupaciones";
    public static final String LOCALGIS_LOGO = "geopista.gif";
	public static String currentLocale = null;
	public static ResourceBundle literales = null;
    public static GeopistaEditor geopistaEditor;
    private JInternalFrame iFrame;
    private Hashtable permisos= new Hashtable();
	private boolean fromInicio=false;
	private ApplicationContext aplicacion = AppContext.getApplicationContext();;
	private Blackboard blackboard = aplicacion.getBlackboard();
	
	
	
   public CMainOcupaciones(String tipoOcupacion) {
       if ((tipoOcupacion != null) && (tipoOcupacion.trim().equalsIgnoreCase("")))
        CConstantesOcupaciones.patronTipoOcupacion= tipoOcupacion;
        
   }

	public CMainOcupaciones() {
		this(false);
	}	
	public CMainOcupaciones(boolean fromInicio) {
		COperacionesLicencias.webappContext = "/" + WebAppConstants.OCUPACIONES_WEBAPP_NAME +
	            ServletConstants.CSERVLETOCUPACIONES_SERVLET_NAME;
    	this.fromInicio=fromInicio;
    	cargarMainOcupaciones();
	}
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }
    public void start()
    {
    	cargarMainOcupaciones();
    }
    private void cargarMainOcupaciones(){
        AppContext.getApplicationContext().setMainFrame(this);
        try {initLookAndFeel();} catch (Exception e) {}
		try
        {
            SplashWindow splashWindow = showSplash();
            initComponents();
            configureApp();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            try{
            	//Modificamos la carga de las imágenes
            	this.setIconImage(IconLoader.icon(LOCALGIS_LOGO).getImage());
//                ClassLoader cl = this.getClass().getClassLoader();
//                java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(cl.getResource(CConstantesPaths.IMAGE_PATH + "/geopista.gif"));
//                setIconImage(img);
            }catch(Exception e){}
  			show();

			/** deshabilitamos la barra de tareas */
            try{com.geopista.app.ocupaciones.CUtilidadesComponentes.inicializar();
                com.geopista.app.ocupaciones.CUtilidadesComponentes.menuLicenciasSetEnabled(false, this);}catch(Exception e){logger.error(e.toString());e.printStackTrace();}
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			if (splashWindow != null) splashWindow.setVisible(false);
	           //******************
            //Mostramos la pantalla de autenticación del usuario.
            //******************
			//Antes de autenticar al usuario hacemos un logout
			if (SecurityManager.getIdSesion()!=null)
				SecurityManager.logout();
						
			//--------NUEVO-------------->	
//			if(SSOAuthManager.isSSOActive())
//				CConstantesComando.loginLicenciasUrl = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
			SSOAuthManager.ssoAuthManager(CConstantesOcupaciones.idApp);								
            if (!AppContext.getApplicationContext().isOnlyLogged()){  
            //-------FIN-NUEVO----------->
            if (fromInicio){
                if (!showAuth()){
                	dispose();
                	return;
                }
            }
            else{
            	showAuth();
            }
            //--------NUEVO-------------->
            }
            //-------FIN-NUEVO-----------> 
            setPolicy();
            
            if(!tienePermisos("Geopista.Ocupaciones.Login")){
            	noPermApp();
            	return;
            }            
            //AppContext.seleccionarMunicipio((Frame)this);
            if (!AppContext.seleccionarMunicipio((Frame)this)){
            	stopApp();
            	return;
            }
            
            CConstantesOcupaciones.IdMunicipio = AppContext.getIdMunicipio();

            /** cargamos el municipio y la provincia */
            Municipio municipio = (new OperacionesAdministrador(com.geopista.protocol.CConstantesComando.loginLicenciasUrl)).getMunicipio(new Integer(CConstantesOcupaciones.IdMunicipio).toString());
            if (municipio!=null){
                CConstantesOcupaciones.Municipio= municipio.getNombre();
                CConstantesOcupaciones.Provincia= municipio.getProvincia();
                com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
                setTitle(getTitle()+" - "+CConstantesOcupaciones.Municipio + " ("+CConstantesOcupaciones.Provincia+") - "+literales.getString("CAuthDialog.jLabelNombre")+" "+principal);
            }

            /** dialogo de espera de carga */
            final JFrame desktop= (JFrame)this;
            final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
            progressDialog.setTitle(CMainOcupaciones.literales.getString("Licencias.Tag3"));
            progressDialog.addComponentListener(new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    new Thread(new Runnable(){
                        public void run(){
                            /* añadimos el documento a la lista */
                            try{
                                progressDialog.report(CMainOcupaciones.literales.getString("Licencias.Tag2"));
                                /** cargamos las estructuras */
                                while (!Estructuras.isCargada()) {
                                    if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {}
                                }
                    
                                progressDialog.report(CMainOcupaciones.literales.getString("Licencias.Tag3"));
                                /** mostramos el dialogo con los eventos y notificaciones pendientes */
                                if (tienePermisos("Geopista.Ocupaciones.Eventos") && tienePermisos("Geopista.Ocupaciones.Notificaciones")){
                                	mostrarEventosYNotificacionesPendientesJInternalFrame();
                                }else{
                                    com.geopista.app.ocupaciones.CUtilidadesComponentes.menuLicenciasSetEnabled(true, desktop);
                                    desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                    desktop.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                }
                            }catch(Exception e){
                                logger.error("Error ", e);
                                ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                return;
                            }finally{
                                progressDialog.setVisible(false);
                            }
                        }
                  }).start();
              }
           });
           GUIUtil.centreOnWindow(progressDialog);
           progressDialog.setVisible(true);
           
           CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("ERROR:" + sw.toString());
			logger.error("Exception: " + sw.toString());
		}
    }



	private boolean showAuth() {


		try {
			boolean resultado=false;
			resetSecurityPolicy();
            com.geopista.app.utilidades.CAuthDialog auth =
                new com.geopista.app.utilidades.CAuthDialog(this, true,
                        CConstantesComando.loginLicenciasUrl,CConstantesOcupaciones.idApp,
                        CConstantesOcupaciones.IdMunicipio, literales);
		    auth.setBounds(30, 60, 315, 155);
           if (fromInicio){
            	resultado=auth.showD(true);
            	if (!resultado)
            		return false;
            }
            else{
            	auth.show();
            }
            //com.geopista.security.SecurityManager.setHeartBeatTime(1000000);
           //CAMBIADO
            /*com.geopista.security.SecurityManager.setHeartBeatTime(10000);
            GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("Ocupaciones");
			applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());*/
			setPolicy();
			return true;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR al autenticar al usuario " + sw.toString());
			JOptionPane optionPane = new JOptionPane("Error al inicializar: \n"
            +((e.getMessage()!=null && e.getMessage().length()>=0)?e.getMessage():e.toString()), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.show();
			return false;
		}


	}
	private void setPolicy() throws AclNotFoundException, Exception{
		com.geopista.security.SecurityManager.setHeartBeatTime(10000);
        GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("Ocupaciones");
		applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
	}
	 private void  stopApp(){
	    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Inicio de aplicación cancelado. Se reiniciará el aplicativo");
	    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    	 System.exit(1);
	    }
	    private void noPermApp(){
	    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"No tiene permisos para entrar. Se reiniciará el aplicativo");
	    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    	 System.exit(1);
	    }

	private boolean configureApp() {

		try {


			//****************************************************************
			//** Inicializamos el log4j
			//*******************************************************
			try{PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);}catch(Exception e){};


		    try{
                   ApplicationContext app= AppContext.getApplicationContext();
                   com.geopista.protocol.CConstantesComando.servletLicenciasUrl = app.getString("geopista.conexion.servidorurl")+ WebAppConstants.OCUPACIONES_WEBAPP_NAME + ServletConstants.CSERVLETOCUPACIONES_SERVLET_NAME;
                   com.geopista.protocol.CConstantesComando.anexosLicenciasUrl = app.getString("geopista.conexion.servidorurl")+"anexos/ocupacion/";
                   com.geopista.protocol.CConstantesComando.loginLicenciasUrl = app.getString("geopista.conexion.servidorurl")+ WebAppConstants.OCUPACIONES_WEBAPP_NAME;
                   com.geopista.protocol.CConstantesComando.adminCartografiaUrl = app.getString("geopista.conexion.servidorurl")+WebAppConstants.GEOPISTA_WEBAPP_NAME + "/";
                   com.geopista.protocol.CConstantesComando.plantillasOcupacionesUrl = app.getString("geopista.conexion.servidorurl")+"plantillas/ocupacion/";

                   CConstantesOcupaciones.Locale = app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");
                   try{CConstantesOcupaciones.N_UltimosEventos = new Integer(app.getString("geopista.ultimos.eventos")).intValue();}catch(Exception e){}
                   try{
                       //CConstantesOcupaciones.IdMunicipio=new Integer(app.getUserPreference("geopista.DefaultCityId","0",false)).intValue();
                       CConstantesOcupaciones.IdMunicipio=new Integer(app.getString("geopista.DefaultCityId")).intValue();
                   }catch (Exception e){
                              JOptionPane.showMessageDialog(this, "Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              System.out.println("Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              logger.error("Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              if (fromInicio)
                            	  dispose();
                              else
                            	  System.exit(-1);
                   }
                   try{
                        CConstantesOcupaciones.totalMaxSizeFilesUploaded= new Long(app.getString("geopista.anexos.totalMaxSizeFilesUploaded")).longValue();
                   }catch(Exception ex){
                      System.out.println("[CMainLicencias.configureApp]:geopista.anexos.totalMaxSizeFilesUploaded="+app.getString("geopista.anexos.totalMaxSizeFilesUploaded"));
                      System.out.println("[CMainLicencias.configureApp]"+ex.toString());
                   }
                   /** Parametros de conexion de BD para los informes */
                   CConstantesOcupaciones.DriverClassName = app.getString("conexion.driver");
                   CConstantesOcupaciones.ConnectionInfo = app.getString("conexion.url");
                   CConstantesOcupaciones.DBUser = app.getString("conexion.user");
                   //CConstantesOcupaciones.DBPassword = app.getString("conexion.pass");
                   CConstantesOcupaciones.DBPassword = app.getUserPreference("conexion.pass","",false);

            }catch(Exception e){
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    JOptionPane.showMessageDialog(this, "Excepcion al cargar el fichero de configuración:\n"+sw.toString());
                    logger.error("Exception: " + sw.toString());
                    if (fromInicio)
                  	  dispose();
                    else
                  	  System.exit(-1);
                    return false;
             }

			logger.debug("CConstantesComando.timeout: " + CConstantesComando.timeout);

			logger.info("CConstantesComando.servletLicenciasUrl: " + CConstantesComando.servletLicenciasUrl);
			logger.info("CConstantesComando.anexosLicenciasUrl: " + CConstantesComando.anexosLicenciasUrl);
			logger.info("CConstantesComando.loginLicenciasUrl: " + CConstantesComando.loginLicenciasUrl);
            logger.debug("CConstantesOcupaciones.numMaxCharObservaciones: " + CConstantesOcupaciones.MaxLengthObservaciones);
			logger.debug("CConstantesOcupaciones.Locale: " + CConstantesOcupaciones.Locale);


			//****************************************************************
			//** Establecemos el idioma especificado en la configuracion
			//*******************************************************
			setLang(CConstantesOcupaciones.Locale);


			return true;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}

	}

	/**
	 * Exit the Application
	 */
	private void exitForm() {
		try {
			//-----NUEVO----->	
	           if(!SSOAuthManager.isSSOActive()) 
	        	   com.geopista.security.SecurityManager.logout();
	           //---FIN NUEVO--->
		} catch (Exception e) {
		}
		;
        if (fromInicio)
      	  dispose();
        else
      	  System.exit(0);
	}
	

	private void setLang(String locale) {
		setLang(resourceName, locale);
	}


	private void setLang(String resource, String locale) {
		try {

            try
            {
                ApplicationContext app = AppContext.getApplicationContext();
                app.setUserPreference(AppContext.GEOPISTA_LOCALE_KEY,locale);
            }catch(Exception e)
            {
                logger.error("Exception: " + e.toString());
            }

			try
            {
                literales = ResourceBundle.getBundle(resource, new Locale(locale));
                currentLocale = locale;
            }catch (Exception e)
            {
                literales = ResourceBundle.getBundle(resource, new Locale(CConstantesOcupaciones.LocalCastellano));
                currentLocale= CConstantesOcupaciones.LocalCastellano;
            }

		} catch (Exception e) {
			logger.error("Can't find resource file for locale: " + locale, e);
		}
		renombrarComponentes();
        try {
        ((IMultilingue)desktopPane.getComponents()[0]).renombrarComponentes();
        }
        catch (Exception e) {}
	}


	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {//GEN-BEGIN:initComponents
		desktopPane = new javax.swing.JDesktopPane();
		menuBar = new javax.swing.JMenuBar();
		gestionLicenciasMenu = new javax.swing.JMenu();
        jMenuConfiguracion= new javax.swing.JMenu();
        jMenuItemConfiguracion= new javax.swing.JMenuItem();
		consultarOcupacionJMenuItem = new javax.swing.JMenuItem();
		crearOcupacionJMenuItem = new javax.swing.JMenuItem();
		modificarOcupacionJMenuItem = new javax.swing.JMenuItem();
		jMenuSalir = new javax.swing.JMenuItem();

		generarInformesJMenuItem = new javax.swing.JMenuItem();
		gestionEventosMenu = new javax.swing.JMenu();
		consultaEventosJMenuItem = new javax.swing.JMenuItem();
		ultimosEventosJMenuItem = new javax.swing.JMenuItem();
		planosMenu = new javax.swing.JMenu();
		generacionPlanosJMenuItem = new javax.swing.JMenuItem();
		notificacionesJMenu = new javax.swing.JMenu();
		consultaNotificacionesJMenuItem = new javax.swing.JMenuItem();
		gestionHistoricoMenu = new javax.swing.JMenu();
		mantenimientoHistoricoJMenuItem = new javax.swing.JMenuItem();
		tareasPendientesMenu = new javax.swing.JMenu();
		tareasPendientesJMenuItem = new javax.swing.JMenuItem();
		idiomaMenu = new javax.swing.JMenu();
		castellanoJMenuItem = new javax.swing.JMenuItem();
		catalanJMenuItem = new javax.swing.JMenuItem();
		euskeraJMenuItem = new javax.swing.JMenuItem();
		gallegoJMenuItem = new javax.swing.JMenuItem();
		valencianoJMenuItem = new javax.swing.JMenuItem();
		ayudaJMenu = new javax.swing.JMenu();
		contenidoJMenuItem = new javax.swing.JMenuItem();
		acercaJMenuItem = new javax.swing.JMenuItem();

		setTitle("LocalGIS - Licencias de Obra Mayor, menor y de Actividad");
		//setBackground(new java.awt.Color(0, 78, 152));
		setBackground(new java.awt.Color(255, 255, 255));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(null);
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

		gestionLicenciasMenu.setText("Gesti\u00f3n de Licencias");
		consultarOcupacionJMenuItem.setText("Consulta de Ocupación");
		crearOcupacionJMenuItem.setText("Creación de Ocupación");
		modificarOcupacionJMenuItem.setText("Modificación de Ocupación");

		consultarOcupacionJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				consultarOcupacionJMenuItemActionPerformed();
			}
		});
		crearOcupacionJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				crearOcupacionJMenuItemActionPerformed();
			}
		});
		modificarOcupacionJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modificarOcupacionJMenuItemActionPerformed();
			}
		});


		gestionLicenciasMenu.add(consultarOcupacionJMenuItem);
		gestionLicenciasMenu.add(crearOcupacionJMenuItem);
		gestionLicenciasMenu.add(modificarOcupacionJMenuItem);


		generarInformesJMenuItem.setText("Generar informes");
		generarInformesJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				generarInformesJMenuItemActionPerformed();
			}
		});

		gestionLicenciasMenu.add(generarInformesJMenuItem);
		
		jMenuSalir.setMnemonic('S');
		jMenuSalir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitForm();
			}
		});
		gestionLicenciasMenu.add(jMenuSalir);

		menuBar.add(gestionLicenciasMenu);

		gestionEventosMenu.setText("Gesti\u00f3n de eventos");
		consultaEventosJMenuItem.setText("Consulta de eventos");
		consultaEventosJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				consultaEventosJMenuItemActionPerformed();
			}
		});

		gestionEventosMenu.add(consultaEventosJMenuItem);

		ultimosEventosJMenuItem.setText("\u00daltimos eventos");
		ultimosEventosJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ultimosEventosJMenuItemActionPerformed();
			}
		});

		gestionEventosMenu.add(ultimosEventosJMenuItem);

		menuBar.add(gestionEventosMenu);

		planosMenu.setText("Planos");
		generacionPlanosJMenuItem.setText("Generaci\u00f3n de planos");
		generacionPlanosJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				generacionPlanosJMenuItemActionPerformed();
			}
		});

		planosMenu.add(generacionPlanosJMenuItem);

		menuBar.add(planosMenu);

		notificacionesJMenu.setText("Notificaciones");
		consultaNotificacionesJMenuItem.setText("Consulta de notificaciones");
		consultaNotificacionesJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				consultaNotificacionesJMenuItemActionPerformed();
			}
		});

		notificacionesJMenu.add(consultaNotificacionesJMenuItem);

		menuBar.add(notificacionesJMenu);

		gestionHistoricoMenu.setText("Gesti\u00f3n de hist\u00f3rico");
		mantenimientoHistoricoJMenuItem.setText("Consultar Hist\u00f3rico");
		mantenimientoHistoricoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mantenimientoHistoricoJMenuItemActionPerformed();
			}
		});

		gestionHistoricoMenu.add(mantenimientoHistoricoJMenuItem);

		menuBar.add(gestionHistoricoMenu);

		tareasPendientesMenu.setText("Pendientes");
		tareasPendientesJMenuItem.setText("Tareas Pendientes");
		tareasPendientesJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tareasPendientesJMenuItemActionPerformed();
			}
		});

		tareasPendientesMenu.add(tareasPendientesJMenuItem);

		menuBar.add(tareasPendientesMenu);

        jMenuItemConfiguracion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mostrarConfiguracion();
			}
		});
        jMenuConfiguracion.add(jMenuItemConfiguracion);
        menuBar.add(jMenuConfiguracion);

		idiomaMenu.setText("Idioma");
		idiomaMenu.setMargin(null);
		castellanoJMenuItem.setText("Castellano");
		castellanoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				castellanoJMenuItemActionPerformed();
			}
		});

		idiomaMenu.add(castellanoJMenuItem);

		catalanJMenuItem.setText("Catal\u00e1n");
		idiomaMenu.add(catalanJMenuItem);
		catalanJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				catalanJMenuItemActionPerformed();
			}
		});


		euskeraJMenuItem.setText("Euskera");
		idiomaMenu.add(euskeraJMenuItem);
		euskeraJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				euskeraJMenuItemActionPerformed();
			}
		});


		gallegoJMenuItem.setText("Gallego");
		idiomaMenu.add(gallegoJMenuItem);
		gallegoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				gallegoJMenuItemActionPerformed();
			}
		});


		valencianoJMenuItem.setText("Valenciano");
		idiomaMenu.add(valencianoJMenuItem);
		valencianoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				valencianoJMenuItemActionPerformed();
			}
		});


		menuBar.add(idiomaMenu);

		ayudaJMenu.setText("Ayuda");
		ayudaJMenu.setMargin(null);


		contenidoJMenuItem.setText("Mostrar ayuda");
		contenidoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				contenidoJMenuItemActionPerformed();
			}
		});

		acercaJMenuItem.setText("Acerca");
		acercaJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				acercaJMenuItemActionPerformed();
			}
		});


		ayudaJMenu.add(contenidoJMenuItem);
		ayudaJMenu.add(acercaJMenuItem);
		menuBar.add(ayudaJMenu);

		setJMenuBar(menuBar);

		pack();
	}//GEN-END:initComponents

	private void ultimosEventosJMenuItemActionPerformed() {//GEN-FIRST:event_ultimosEventosJMenuItemActionPerformed
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesUltimosEventos";
		CUltimosEventos ultimosEventos= new CUltimosEventos(this);
        ultimosEventos.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Ocupaciones.Modificacion"));
		mostrarJInternalFrame(ultimosEventos);
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_ultimosEventosJMenuItemActionPerformed

	private void consultaEventosJMenuItemActionPerformed() {//GEN-FIRST:event_consultaEventosJMenuItemActionPerformed
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesConsultaEventos";
        CConsultaEventos consultaEventos= new CConsultaEventos(this);
        consultaEventos.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Ocupaciones.Modificacion"));
		mostrarJInternalFrame(consultaEventos);
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_consultaEventosJMenuItemActionPerformed

	private void generacionPlanosJMenuItemActionPerformed() {//GEN-FIRST:event_generacionPlanosJMenuItemActionPerformed
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesPlanos";
		mostrarJInternalFrame(new CGeneracionPlanos(this));
		zoomMapa();
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_generacionPlanosJMenuItemActionPerformed


	private void consultaNotificacionesJMenuItemActionPerformed() {//GEN-FIRST:event_consultaNotificacionesJMenuItemActionPerformed
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesConsultaNotificaciones";
	    CNotificaciones notificaciones= new CNotificaciones(this);
        notificaciones.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Ocupaciones.Modificacion"));
		mostrarJInternalFrame(notificaciones);
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_consultaNotificacionesJMenuItemActionPerformed

	private void mantenimientoHistoricoJMenuItemActionPerformed() {//GEN-FIRST:event_mantenimientoHistoricoJMenuItemActionPerformed
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesConsultaHistorico";
        CMantenimientoHistorico mantenimientoHistorico= new CMantenimientoHistorico(this);
        mantenimientoHistorico.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Ocupaciones.Modificacion"));
		mostrarJInternalFrame(mantenimientoHistorico);
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_mantenimientoHistoricoJMenuItemActionPerformed

	private void tareasPendientesJMenuItemActionPerformed() {
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesTareasPendientes";
		TareasPendientesJInternalFrame tareasPendientes = new TareasPendientesJInternalFrame(this);
		tareasPendientes._tareasPendientesJPanel.rellenarEventosYNotificaciones();
        tareasPendientes._tareasPendientesJPanel.aceptarJButtonSetEnabled(tienePermisos("Geopista.Ocupaciones.Modificacion"));
		mostrarJInternalFrame(tareasPendientes);
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}


	private void generarInformesJMenuItemActionPerformed() {//GEN-FIRST:event_generarInformesJMenuItemActionPerformed
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesInformes";
		mostrarJInternalFrame(new CGeneracionInformes(this));
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}//GEN-LAST:event_generarInformesJMenuItemActionPerformed


	private void renombrarComponentes() {
		try {
			String title=literales.getString("CMainLicenciasForm.JFrame.title");
			String release=aplicacion.getString("localgis.release");
			if (release==null)
				release="LocalGIS";
			title=title.replaceAll("\\$\\{localgis\\.release\\}",release);				
	        setTitle(title);
            if (CConstantesOcupaciones.Municipio!=null){
            	  com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
            		
            		if(principal!=null)
            		setTitle(getTitle()+" - "+CConstantesOcupaciones.Municipio + " ("+CConstantesOcupaciones.Provincia+") - "+literales.getString("CAuthDialog.jLabelNombre")+" "+principal);
            	else
                        setTitle(getTitle()+" - "+CConstantesOcupaciones.Municipio + " ("+CConstantesOcupaciones.Provincia+")");
            }
			gestionLicenciasMenu.setText(literales.getString("CMainLicenciasForm.gestionLicenciasMenu.text"));

			consultarOcupacionJMenuItem.setText(literales.getString("CMainLicenciasForm.consultarOcupacionJMenuItem.text"));
			crearOcupacionJMenuItem.setText(literales.getString("CMainLicenciasForm.crearOcupacionJMenuItem.text"));
			modificarOcupacionJMenuItem.setText(literales.getString("CMainLicenciasForm.modificarOcupacionJMenuItem.text"));


			generarInformesJMenuItem.setText(literales.getString("CMainLicenciasForm.generarInformesJMenuItem.text"));
			jMenuSalir.setText(literales.getString("CMainLicenciasForm.salirJMenuItem.text"));
			gestionEventosMenu.setText(literales.getString("CMainLicenciasForm.gestionEventosMenu.text"));
			consultaEventosJMenuItem.setText(literales.getString("CMainLicenciasForm.consultaEventosJMenuItem.text"));
			ultimosEventosJMenuItem.setText(literales.getString("CMainLicenciasForm.ultimosEventosJMenuItem.text"));
			planosMenu.setText(literales.getString("CMainLicenciasForm.planosMenu.text"));
			generacionPlanosJMenuItem.setText(literales.getString("CMainLicenciasForm.generacionPlanosJMenuItem.text"));
			notificacionesJMenu.setText(literales.getString("CMainLicenciasForm.notificacionesJMenu.text"));
			consultaNotificacionesJMenuItem.setText(literales.getString("CMainLicenciasForm.consultaNotificacionesJMenuItem.text"));
			gestionHistoricoMenu.setText(literales.getString("CMainLicenciasForm.gestionHistoricoMenu.text"));
			mantenimientoHistoricoJMenuItem.setText(literales.getString("CMainLicenciasForm.mantenimientoHistoricoJMenuItem.text"));
			idiomaMenu.setText(literales.getString("CMainLicenciasForm.idiomaMenu.text"));
			castellanoJMenuItem.setText(literales.getString("CMainLicenciasForm.castellanoJMenuItem.text"));
			catalanJMenuItem.setText(literales.getString("CMainLicenciasForm.catalanJMenuItem.text"));
			euskeraJMenuItem.setText(literales.getString("CMainLicenciasForm.euskeraJMenuItem.text"));
			gallegoJMenuItem.setText(literales.getString("CMainLicenciasForm.gallegoJMenuItem.text"));
			valencianoJMenuItem.setText(literales.getString("CMainLicenciasForm.valencianoJMenuItem.text"));
			ayudaJMenu.setText(literales.getString("CMainLicenciasForm.ayudaJMenu.text"));
			contenidoJMenuItem.setText(literales.getString("CMainLicenciasForm.contenidoJMenuItem.text"));
			acercaJMenuItem.setText(literales.getString("CMainLicenciasForm.acercaJMenuItem.text"));
            jMenuConfiguracion.setText(literales.getString("CMainOcupaciones.jMenuConfiguracion"));//"Configuración");
            jMenuItemConfiguracion.setText(literales.getString("CMainOcupaciones.jMenuItemConfiguracion"));//"Ver");
            tareasPendientesMenu.setText(literales.getString("CMainLicenciasForm.tareasPendientesMenu.text"));
            tareasPendientesJMenuItem.setText(literales.getString("CMainLicenciasForm.tareasPendientesJMenuItem.text"));

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}


	public boolean mostrarJInternalFrame(Class clase) {

		try {


			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			int numInternalFrames = desktopPane.getAllFrames().length;
			if (numInternalFrames == 0) {

				JInternalFrame internalFrame = (JInternalFrame) clase.newInstance();
                ClassLoader cl =this.getClass().getClassLoader();
                internalFrame.setFrameIcon(new javax.swing.ImageIcon(cl.getResource("img/geopista.gif")));
				desktopPane.add(internalFrame);
				internalFrame.setMaximum(true);
				internalFrame.show();
			} else {
				logger.info("cannot open another JInternalFrame");

			}

			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}

		return true;
	}

	public boolean mostrarAyuda() {
		logger.info("Inicio.");

		HelpSet hs = null;
		ClassLoader loader = this.getClass().getClassLoader();
		URL url;
		try {
			String helpSetFile = "help/ocupaciones/OcupacionesHelp_" + CConstantesOcupaciones.Locale.substring(0, 2) + ".hs";
			logger.info("helpSetFile: " + helpSetFile);
			url = HelpSet.findHelpSet(loader, helpSetFile);

			logger.info("url: "+url);
			logger.info("loader: "+loader);
			if (url == null) {
                url=new URL("help/ocupaciones/OcupacionesHelp_" + CConstantesOcupaciones.LocalCastellano.substring(0,2) + ".hs");
			}
			hs = new HelpSet(loader, url);

			// ayuda sensible al contexto
			hs.setHomeID(CConstantesOcupaciones.helpSetHomeID);
		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString());


			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());

			return false;
		}
		HelpBroker hb = hs.createHelpBroker();

		hb.setDisplayed(true);
		new CSH.DisplayHelpFromSource(hb);
		return true;
	}


	public boolean mostrarJInternalFrame(JInternalFrame internalFrame) {

		try {

			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			int numInternalFrames = desktopPane.getAllFrames().length;
			if (numInternalFrames == 0) {
                ClassLoader cl =this.getClass().getClassLoader();
                internalFrame.setFrameIcon(new javax.swing.ImageIcon(cl.getResource("img/geopista.gif")));
		        desktopPane.add(internalFrame);
				internalFrame.setMaximum(true);
				internalFrame.show();
                try{iFrame=(JInternalFrame)internalFrame;}catch(Exception e){iFrame=null;}

			} else {
				logger.info("cannot open another JInternalFrame");

			}

			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}

		return true;
	}


	public void resetSecurityPolicy() {
		consultarOcupacionJMenuItem.setEnabled(false);
		crearOcupacionJMenuItem.setEnabled(false);
		modificarOcupacionJMenuItem.setEnabled(false);
		generarInformesJMenuItem.setEnabled(false);
		generacionPlanosJMenuItem.setEnabled(false);
		consultaEventosJMenuItem.setEnabled(false);
		ultimosEventosJMenuItem.setEnabled(false);
		consultaNotificacionesJMenuItem.setEnabled(false);
		mantenimientoHistoricoJMenuItem.setEnabled(false);
        tareasPendientesJMenuItem.setEnabled(false);
		idiomaMenu.setEnabled(false);
        jMenuConfiguracion.setEnabled(false);
	}


	public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal) {

		try {

			if ((acl == null) || (principal == null) || (acl.getPermissions(principal)==null)) {
				return false;
			}

			CConstantesOcupaciones.principal = principal;
            setPermisos(acl.getPermissions(principal));

            if(tienePermisos("Geopista.Ocupaciones.Login")){
            
	            if (tienePermisos("Geopista.Ocupaciones.Consulta")) {
	                consultarOcupacionJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Ocupaciones.Creacion")) {
	                crearOcupacionJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Ocupaciones.Modificacion")) {
	                modificarOcupacionJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Ocupaciones.Informes")) {
	                generarInformesJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Ocupaciones.Eventos")) {
	                consultaEventosJMenuItem.setEnabled(true);
	                ultimosEventosJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Ocupaciones.Planos")) {
	                generacionPlanosJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Ocupaciones.Notificaciones")) {
	                consultaNotificacionesJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Ocupaciones.Historico")) {
	                mantenimientoHistoricoJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Ocupaciones.Idiomas")) {
	                idiomaMenu.setEnabled(true);
	            }
	            if (tienePermisos("Geopista.Ocupaciones.Eventos") && tienePermisos("Geopista.Ocupaciones.Notificaciones")){
	                tareasPendientesJMenuItem.setEnabled(true);
	            }
            
            }

            jMenuConfiguracion.setEnabled(true);
			return true;

		} catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());			
			return false;
		}

	}


	private void consultarOcupacionJMenuItemActionPerformed() {
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesConsulta";
		CConstantesOcupaciones.selectedSubApp = CConstantesOcupaciones.Ocupacion;
        com.geopista.app.ocupaciones.CConsultaLicencias consultarLicencia= new com.geopista.app.ocupaciones.CConsultaLicencias(this);
        consultarLicencia.setEnabledModificarJButton(tienePermisos("Geopista.Ocupaciones.Modificacion"));
        consultarLicencia.setEnabledPublicarJButton(tienePermisos("Geopista.Ocupaciones.Publicacion"));
		mostrarJInternalFrame(consultarLicencia);
		zoomMapa();
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}


	private void crearOcupacionJMenuItemActionPerformed() {//GEN-FIRST:event_crearLicenciaObraMayorJMenuItemActionPerformed
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesCreacion";
		CConstantesOcupaciones.selectedSubApp = CConstantesOcupaciones.Ocupacion;
		mostrarJInternalFrame(new com.geopista.app.ocupaciones.CCreacionLicencias(this));
		zoomMapa();
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


	}//GEN-LAST:event_crearLicenciaObraMayorJMenuItemActionPerformed


	private void modificarOcupacionJMenuItemActionPerformed() {

		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesModificacion";
		CConstantesOcupaciones.selectedSubApp = CConstantesOcupaciones.Ocupacion;
	    mostrarJInternalFrame(new com.geopista.app.ocupaciones.CModificacionLicencias(this, null, true));
	    zoomMapa();
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}


	private void contenidoJMenuItemActionPerformed() {//GEN-FIRST:event_contenidoJMenuItemActionPerformed
		mostrarAyuda();
	}//GEN-LAST:event_contenidoJMenuItemActionPerformed

	private void acercaJMenuItemActionPerformed() {//GEN-FIRST:event_acercaJMenuItemActionPerformed
		setLang(resourceName, CConstantesOcupaciones.Locale);
        CConstantesOcupaciones.helpSetHomeID = "ocupacionesIntro";
        CConstantesOcupaciones.selectedSubApp = CConstantesOcupaciones.Ocupacion;

        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CAcercaDeJDialog acercaDe= new CAcercaDeJDialog(this, true);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        acercaDe.setSize(255, 240);
        acercaDe.setLocation(d.width / 2 - acercaDe.getSize().width / 2, d.height / 2 - acercaDe.getSize().height / 2);
        acercaDe.setResizable(false);
        acercaDe.show();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}//GEN-LAST:event_acercaJMenuItemActionPerformed


	private void castellanoJMenuItemActionPerformed() {//GEN-FIRST:event_castellanoJMenuItemActionPerformed

		logger.info("Inicio.");
		CConstantesOcupaciones.Locale = CConstantesOcupaciones.LocalCastellano;
		setLang(CConstantesOcupaciones.Locale);

	}//GEN-LAST:event_castellanoJMenuItemActionPerformed

	private void gallegoJMenuItemActionPerformed() {

		logger.info("Inicio.");
		CConstantesOcupaciones.Locale = CConstantesOcupaciones.LocalGallego;
		setLang(CConstantesOcupaciones.Locale);
	}


	private void catalanJMenuItemActionPerformed() {

		logger.info("Inicio.");
        CConstantesOcupaciones.Locale = CConstantesOcupaciones.LocalCatalan;
		setLang(CConstantesOcupaciones.Locale);
	}


	private void euskeraJMenuItemActionPerformed() {

		logger.info("Inicio.");
        CConstantesOcupaciones.Locale = CConstantesOcupaciones.LocalEuskera;
		setLang(CConstantesOcupaciones.Locale);
	}

	private void valencianoJMenuItemActionPerformed() {

		logger.info("Inicio.");
        CConstantesOcupaciones.Locale = CConstantesOcupaciones.LocalValenciano;
		setLang(CConstantesOcupaciones.Locale);
	}


	private void mostrarEventosYNotificacionesPendientesJInternalFrame() {
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setLang(resourceName, CConstantesOcupaciones.Locale);
		TareasPendientesJInternalFrame tareasPendientes = new TareasPendientesJInternalFrame(this);
		CConstantesOcupaciones.helpSetHomeID = "ocupacionesTareasPendientes";

		boolean hayPendientes = tareasPendientes._tareasPendientesJPanel.rellenarEventosYNotificaciones();
		if (hayPendientes) {
            tareasPendientes._tareasPendientesJPanel.aceptarJButtonSetEnabled(tienePermisos("Geopista.Ocupaciones.Modificacion"));
			mostrarJInternalFrame(tareasPendientes);
		} else
			com.geopista.app.ocupaciones.CUtilidadesComponentes.menuLicenciasSetEnabled(true, (JFrame) this);

		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
    private void mostrarConfiguracion()
    {
        JDialogConfiguracion dialogConfiguracion = new JDialogConfiguracion(this, true, literales, "LICENCIAS_OCUPACION");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		dialogConfiguracion.setSize(400, 250);
		dialogConfiguracion.setLocation(d.width / 2 - dialogConfiguracion.getSize().width / 2, d.height / 2 - dialogConfiguracion.getSize().height / 2);
		dialogConfiguracion.setResizable(false);
		dialogConfiguracion.show();
		dialogConfiguracion = null;
    }

	/**
	 * Exit the Application
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		try {

            if (iFrame!=null){
                if (iFrame instanceof com.geopista.app.ocupaciones.CModificacionLicencias)
                {
                    ((com.geopista.app.ocupaciones.CModificacionLicencias)iFrame).desbloquearExpediente();
                    ((com.geopista.app.ocupaciones.CModificacionLicencias)iFrame).borrarFeaturesSinGrabar();
                }
                else if (iFrame instanceof com.geopista.app.ocupaciones.CCreacionLicencias)
                    ((com.geopista.app.ocupaciones.CCreacionLicencias)iFrame).borrarFeaturesSinGrabar();
            }

			com.geopista.security.SecurityManager.logout();

		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}
        if (fromInicio)
      	  dispose();
        else
      	  System.exit(0);
	}//GEN-LAST:event_exitForm

    public void setPermisos(Enumeration e){
        permisos= new Hashtable();

        while (e.hasMoreElements()){
            GeopistaPermission geopistaPermission = (GeopistaPermission) e.nextElement();
            String permissionName = geopistaPermission.getName();
            if (!permisos.containsKey(permissionName)){
                permisos.put(permissionName, "");
            }
        }
    }


    public boolean tienePermisos(String permiso){
        if (permisos.containsKey(permiso)) return true;

        return false;
    }

    private void zoomMapa()
    {
        try
        {
            if(geopistaEditor!=null)
            {
                WorkbenchContext wb=geopistaEditor.getContext();
                PlugInContext plugInContext = wb.createPlugInContext();
                plugInContext.getLayerViewPanel().getViewport().zoomToFullExtent();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


 	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

        String param = null;
        if(args.length > 0) param = args[0];
      
        //Inicializamos una instancia AppContextMap
    	//AppContextMap.initAppContextMap();
		//CMainOcupaciones app = new CMainOcupaciones(param);
		//app.start();
        new CMainOcupaciones();
	}

	// Variables declaration - do not modify
	private javax.swing.JMenuItem contenidoJMenuItem;
	private javax.swing.JMenuItem acercaJMenuItem;

	private javax.swing.JMenuItem castellanoJMenuItem;
	private javax.swing.JMenuItem catalanJMenuItem;
	private javax.swing.JMenuItem consultaEventosJMenuItem;
	private javax.swing.JMenuItem consultaNotificacionesJMenuItem;


	private javax.swing.JMenuItem consultarOcupacionJMenuItem;
	private javax.swing.JMenuItem crearOcupacionJMenuItem;
	private javax.swing.JMenuItem modificarOcupacionJMenuItem;


	private javax.swing.JDesktopPane desktopPane;
	private javax.swing.JMenuItem euskeraJMenuItem;
	private javax.swing.JMenuItem gallegoJMenuItem;
	private javax.swing.JMenuItem generacionPlanosJMenuItem;
	private javax.swing.JMenuItem generarInformesJMenuItem;
	private javax.swing.JMenu gestionEventosMenu;
	private javax.swing.JMenu gestionHistoricoMenu;
	private javax.swing.JMenu tareasPendientesMenu;
	private javax.swing.JMenu gestionLicenciasMenu;
    private javax.swing.JMenu jMenuConfiguracion;
    private javax.swing.JMenuItem jMenuItemConfiguracion;
	private javax.swing.JMenu ayudaJMenu;
	private javax.swing.JMenu idiomaMenu;
	private javax.swing.JMenuItem mantenimientoHistoricoJMenuItem;
	private javax.swing.JMenuItem tareasPendientesJMenuItem;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem jMenuSalir;
	private javax.swing.JMenu notificacionesJMenu;
	private javax.swing.JMenu planosMenu;
	private javax.swing.JMenuItem ultimosEventosJMenuItem;
	private javax.swing.JMenuItem valencianoJMenuItem;
	// End of variables declaration

}


