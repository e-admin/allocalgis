package com.geopista.app.licencias;
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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.acl.AclNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.licencias.eventos.CConsultaEventos;
import com.geopista.app.licencias.eventos.CUltimosEventos;
import com.geopista.app.licencias.historico.CMantenimientoHistorico;
import com.geopista.app.licencias.informes.CGeneracionInformes;
import com.geopista.app.licencias.notificaciones.CNotificaciones;
import com.geopista.app.licencias.pendientes.TareasPendientesJInternalFrame;
import com.geopista.app.licencias.planos.CGeneracionPlanos;
import com.geopista.app.utilidades.CMain;
import com.geopista.app.utilidades.JDialogConfiguracion;
import com.geopista.editor.GeopistaEditor;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.licencias.CConstantesPaths;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author avivar
 */
public class CMainLicencias extends CMain   implements IMainLicencias 
 {
	//static Logger logger = Logger.getLogger(CMainLicencias.class);
	 private static Logger logger;
	    static {
	       createDir();
	       logger  = Logger.getLogger(CMainLicencias.class);
	    }	
    public static String currentLocale = null;
	public static ResourceBundle literales = null;
    private IMultilingue pantallaMultilingue;
    private Hashtable permisos= new Hashtable();
    public static GeopistaEditor geopistaEditor;    
	private boolean fromInicio;
	private ApplicationContext aplicacion = AppContext.getApplicationContext();
	private Blackboard blackboard = aplicacion.getBlackboard();
	
	
    public CMainLicencias() {    	
    	this(false);
    }
    
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }    
	public CMainLicencias(boolean fromInicio) {
		COperacionesLicencias.webappContext = "/" + WebAppConstants.LICENCIAS_OBRA_WEBAPP_NAME +
	            ServletConstants.CSERVLETLICENCIAS_SERVLET_NAME;
		this.fromInicio=fromInicio;
        AppContext.getApplicationContext().setMainFrame(this);
        try {initLookAndFeel();} catch (Exception e) {}
		SplashWindow splashWindow = showSplash();

		initComponents();

		configureApp();

		setExtendedState(JFrame.MAXIMIZED_BOTH);
        ClassLoader cl = this.getClass().getClassLoader();
        java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(cl.getResource("img/geopista.gif"));
		setIconImage(img);

		try {
			show();
            /*
            // no mostramos la imagen de fondo
			JLabel label = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(CConstantesPaths.IMAGE_PATH + "mapa.jpg")));
			label.setBounds(0, 0, (this.getWidth()), this.getHeight());
			desktopPane.add(label);
            */

            /** deshabilitamos la barra de tareas */
            try {
                com.geopista.app.licencias.CUtilidadesComponentes.inicializar();
                com.geopista.app.licencias.CUtilidadesComponentes.menuLicenciasSetEnabled(false, this);

            }catch(Exception e) {};
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
			SSOAuthManager.ssoAuthManager(CConstantesLicencias.idApp);								
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
            
            //AppContext.seleccionarMunicipio((Frame)this);
            if(!tienePermisos("Geopista.Licencias.Login")){
            	noPermApp();
            	return;
            }            
            if (!AppContext.seleccionarMunicipio((Frame)this)){
            	stopApp();
            	return;
            }
            
            CConstantesLicencias.IdMunicipio = AppContext.getIdMunicipio();

            /** cargamos la provincia y el municipio */
            Municipio municipio = (new OperacionesAdministrador(com.geopista.protocol.CConstantesComando.loginLicenciasUrl)).getMunicipio(new Integer(CConstantesLicencias.IdMunicipio).toString());
            if (municipio!=null)
            {
                   CConstantesLicencias.Municipio = municipio.getNombre();
                   CConstantesLicencias.Provincia= municipio.getProvincia();
             
                   com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
                   if(principal!=null)
                   setTitle(getTitle()+" - "+CConstantesLicencias.Municipio + " ("+CConstantesLicencias.Provincia+") -"+ literales.getString("CAuthDialog.jLabelNombre")+principal);
            }

            /** dialogo de espera de carga */
            final JFrame desktop= (JFrame)this;
            final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
            progressDialog.setTitle(CMainLicencias.literales.getString("Licencias.Tag3"));
            progressDialog.addComponentListener(new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    new Thread(new Runnable(){
                        public void run(){
                            /* añadimos el documento a la lista */
                            try{
                                progressDialog.report(CMainLicencias.literales.getString("Licencias.Tag2"));
                                /** cargamos las estructuras */
                                while (!com.geopista.app.licencias.estructuras.Estructuras.isCargada())
                                {
                                    if (!com.geopista.app.licencias.estructuras.Estructuras.isIniciada()) com.geopista.app.licencias.estructuras.Estructuras.cargarEstructuras();
                                    try {Thread.sleep(500);}catch(Exception e){}
                                }

                                progressDialog.report(CMainLicencias.literales.getString("Licencias.Tag3"));
                                /** mostramos el dialogo con los eventos y notificaciones pendientes */
                                if (tienePermisos("Geopista.Licencias.Eventos") && tienePermisos("Geopista.Licencias.Notificaciones")){
                                    mostrarEventosYNotificacionesPendientesJInternalFrame();
                                }else{
                                    CUtilidadesComponentes.menuLicenciasSetEnabled(true, desktop);
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
           
           if (com.geopista.app.licencias.estructuras.Estructuras.isCancelada()){
           	stopApp();
           	return;            	
           }
           zoomMapa();
           CConstantesLicencias.helpSetHomeID = "licenciasIntro";
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("ERROR:" + sw.toString());
			logger.error("Exception: " + sw.toString());
		}
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

		private boolean showAuth() {
        try
        {
        	boolean resultado=false;
            resetSecurityPolicy();
		    com.geopista.app.utilidades.CAuthDialog auth =
                new com.geopista.app.utilidades.CAuthDialog(this, true,
                        CConstantesComando.loginLicenciasUrl,CConstantesLicencias.idApp,
                        CConstantesLicencias.IdMunicipio, literales);
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
            GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("Licencias de Obra");
            applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());*/
            setPolicy();
            
        }catch(Exception e)
        {
             StringWriter sw = new StringWriter();
			 PrintWriter pw = new PrintWriter(sw);
			 e.printStackTrace(pw);
             logger.error("ERROR al autenticar al usuario "+sw.toString());
             JOptionPane optionPane = new JOptionPane("Error al inicializar: \n"
                    +((e.getMessage()!=null && e.getMessage().length()>=0)?e.getMessage():e.toString()), JOptionPane.ERROR_MESSAGE);
             JDialog dialog = optionPane.createDialog(this, "ERROR");
	         dialog.show();
            return false;
        }
		return true;
	}
		
	private void setPolicy() throws AclNotFoundException, Exception{
		 com.geopista.security.SecurityManager.setHeartBeatTime(10000);
         GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("Licencias de Obra");
         applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());	
	}

	private boolean configureApp() {

		try {


			//****************************************************************
			//** Inicializamos el log4j
			//*******************************************************
			try{PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);}catch(Exception e){};


			//****************************************************************
			//** Cargamos la configuracion de config.ini
			//*******************************************************
            try{
                   ApplicationContext app= AppContext.getApplicationContext();
                   com.geopista.protocol.CConstantesComando.servletLicenciasUrl = app.getString("geopista.conexion.servidorurl") + WebAppConstants.LICENCIAS_OBRA_WEBAPP_NAME + ServletConstants.CSERVLETLICENCIAS_SERVLET_NAME;
                   com.geopista.protocol.CConstantesComando.anexosLicenciasUrl = app.getString("geopista.conexion.servidorurl")+"anexos/licencias/";
                   com.geopista.protocol.CConstantesComando.loginLicenciasUrl = app.getString("geopista.conexion.servidorurl") + WebAppConstants.LICENCIAS_OBRA_WEBAPP_NAME;
                   com.geopista.protocol.CConstantesComando.adminCartografiaUrl = app.getString("geopista.conexion.servidorurl") + WebAppConstants.GEOPISTA_WEBAPP_NAME;
                   com.geopista.protocol.CConstantesComando.plantillasLicenciasUrl = app.getString("geopista.conexion.servidorurl")+"plantillas/licencias/";

                   CConstantesLicencias.Locale = app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");

                   try{CConstantesLicencias.IdMunicipio=new Integer(app.getString("geopista.DefaultCityId")).intValue();
                       //CConstantesLicencias.IdMunicipio=new Integer(app.getUserPreference("geopista.DefaultCityId","0",false)).intValue();
                   }catch (Exception e){
                              JOptionPane.showMessageDialog(this, "Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              System.out.println("Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              logger.error("Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              if (fromInicio)
                            	  dispose();
                              else
                            	  System.exit(-1);
                   }
                   try{CConstantesLicencias.N_UltimosEventos = new Integer(app.getString("geopista.ultimos.eventos")).intValue();}catch(Exception e){}
                   CConstantesLicencias.DriverClassName = app.getString("conexion.driver");
                   CConstantesLicencias.ConnectionInfo = app.getString("conexion.url");
                   CConstantesLicencias.DBUser = app.getString("conexion.user");
                   //CConstantesLicencias.DBPassword = app.getString("conexion.pass");
                   CConstantesLicencias.DBPassword = app.getUserPreference("conexion.pass","",false);


                   try{
                       CConstantesLicencias.totalMaxSizeFilesUploaded= new Long(app.getString("geopista.anexos.totalMaxSizeFilesUploaded")).longValue();
                   }catch(Exception ex){
                      System.out.println("[CMainLicencias.configureApp]:geopista.anexos.totalMaxSizeFilesUploaded="+app.getString("geopista.anexos.totalMaxSizeFilesUploaded"));
                      System.out.println("[CMainLicencias.configureApp]"+ex.toString());
                   }

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


			logger.debug("CConstantesLicencias.numMaxCharObservaciones: " + CConstantesLicencias.MaxLengthObservaciones);
			logger.debug("CConstantesLicencias.Locale: " + CConstantesLicencias.Locale);


			//****************************************************************
			//** Establecemos el idioma especificado en la configuracion
			//*******************************************************
			setLang(CConstantesLicencias.Locale);


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
		setLang("licenciasObraMayor", locale);
	}


	public void setLang(String resource, String locale) {
         try
         {
                try
                {
                    ApplicationContext app = AppContext.getApplicationContext();
                    app.setUserPreference(AppContext.GEOPISTA_LOCALE_KEY,locale);
                }catch(Exception e)
                {
                    logger.error("Exception: " + e.toString());
                }
                currentLocale = locale;
                if (CConstantesLicencias.selectedSubApp.equals(CConstantesLicencias.LicenciasObraMayor))
                    try
                    {
                         literales = ResourceBundle.getBundle("config.licenciasObraMayor", new Locale(currentLocale));
                     
                    }catch (Exception e)
                    {
                        currentLocale=CConstantesLicencias.LocalCastellano;
                        literales = ResourceBundle.getBundle("config.licenciasObraMayor", new Locale(currentLocale));
                    }
                else if (CConstantesLicencias.selectedSubApp.equals(CConstantesLicencias.LicenciasObraMenor))
                    try {
                        literales = ResourceBundle.getBundle("config.licenciasObraMenor", new Locale(currentLocale));
                    }catch (Exception e){
                        currentLocale=CConstantesLicencias.LocalCastellano;
                        literales = ResourceBundle.getBundle("config.licenciasObraMenor", new Locale(currentLocale));
                    }
                else if (CConstantesLicencias.selectedSubApp.equals(CConstantesLicencias.LicenciasActividad))
                    try {
                        literales = ResourceBundle.getBundle("config.licenciasActividad", new Locale(currentLocale));
                    }catch (Exception e){
                        currentLocale=CConstantesLicencias.LocalCastellano;
                        literales = ResourceBundle.getBundle("config.licenciasActividad", new Locale(currentLocale));
                    }
                renombrarComponentes();
                if (pantallaMultilingue!=null) pantallaMultilingue.renombrarComponentes(literales);
         } catch (Exception e) {
             logger.error("Can't find resource file for locale: " + locale);
         }

	}


	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {//GEN-BEGIN:initComponents
		//System.out.println("ESTAMOS EJECUTANDO: INITCOMPONENTS");
		desktopPane = new javax.swing.JDesktopPane();
		menuBar = new javax.swing.JMenuBar();
		gestionLicenciasMenu = new javax.swing.JMenu();
		jMenuSalir = new javax.swing.JMenuItem();
		consultarLicenciaJMenu = new javax.swing.JMenu();
		consultarLicenciaObraMayorJMenuItem = new javax.swing.JMenuItem();
		consultarLicenciaObraMenorJMenuItem = new javax.swing.JMenuItem();

		crearLicenciaJMenu = new javax.swing.JMenu();
		crearLicenciaObraMayorJMenuItem = new javax.swing.JMenuItem();
		crearLicenciaObraMenorJMenuItem = new javax.swing.JMenuItem();

		modificarLicenciaJMenu = new javax.swing.JMenu();
		modificarLicenciaObraMayorJMenuItem = new javax.swing.JMenuItem();
		modificarLicenciaObraMenorJMenuItem = new javax.swing.JMenuItem();

        jMenuConfiguracion= new javax.swing.JMenu();
        jMenuItemConfiguracion= new javax.swing.JMenuItem();


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

		setTitle(aplicacion.getString("localgis.release")+ " LocalGIS - Licencias de Obra Mayor y Menor");
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


		consultarLicenciaJMenu.setText("Consulta de licencias");

		consultarLicenciaObraMayorJMenuItem.setText("Consultar licencia obra mayor");
		consultarLicenciaObraMayorJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				consultarLicenciaObraMayorJMenuItemActionPerformed();
			}
		});

		consultarLicenciaJMenu.add(consultarLicenciaObraMayorJMenuItem);

		consultarLicenciaObraMenorJMenuItem.setText("Consultar licencia obra menor");
		consultarLicenciaObraMenorJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				consultarLicenciaObraMenorJMenuItemActionPerformed();
			}
		});

		consultarLicenciaJMenu.add(consultarLicenciaObraMenorJMenuItem);

		gestionLicenciasMenu.add(consultarLicenciaJMenu);












		crearLicenciaJMenu.setText("Creaci\u00f3n de licencias");

		crearLicenciaObraMayorJMenuItem.setText("Creaci\u00f3n licencia obra mayor");
		crearLicenciaObraMayorJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				crearLicenciaObraMayorJMenuItemActionPerformed();
			}
		});

		crearLicenciaJMenu.add(crearLicenciaObraMayorJMenuItem);

		crearLicenciaObraMenorJMenuItem.setText("Creaci\u00f3n licencia obra menor");
		crearLicenciaObraMenorJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				crearLicenciaObraMenorJMenuItemActionPerformed();
			}
		});

		crearLicenciaJMenu.add(crearLicenciaObraMenorJMenuItem);


		gestionLicenciasMenu.add(crearLicenciaJMenu);









				modificarLicenciaJMenu.setText("Modificar licencias");

		modificarLicenciaObraMayorJMenuItem.setText("Modificar licencia obra mayor");
		modificarLicenciaObraMayorJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modificarLicenciaObraMayorJMenuItemActionPerformed();
			}
		});

		modificarLicenciaJMenu.add(modificarLicenciaObraMayorJMenuItem);

		modificarLicenciaObraMenorJMenuItem.setText("Modificar licencia obra menor");
		modificarLicenciaObraMenorJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modificarLicenciaObraMenorJMenuItemActionPerformed();
			}
		});

		modificarLicenciaJMenu.add(modificarLicenciaObraMenorJMenuItem);

		gestionLicenciasMenu.add(modificarLicenciaJMenu);

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


		contenidoJMenuItem.setText("Contenido de la ayuda");
		contenidoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mostrarAyuda();
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
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
		CConstantesLicencias.helpSetHomeID = "licenciasUltimosEventos";
		CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
        CUltimosEventos consultaUltimosEventos= new CUltimosEventos(this, literales);
        consultaUltimosEventos.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(consultaUltimosEventos);
	}//GEN-LAST:event_ultimosEventosJMenuItemActionPerformed

	private void consultaEventosJMenuItemActionPerformed() {//GEN-FIRST:event_consultaEventosJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
		CConstantesLicencias.helpSetHomeID = "licenciasConsultarEventos";
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
        CConsultaEventos consultaEventos= new CConsultaEventos(this,literales);
        consultaEventos.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(consultaEventos);
	}//GEN-LAST:event_consultaEventosJMenuItemActionPerformed

	private void generacionPlanosJMenuItemActionPerformed() {//GEN-FIRST:event_generacionPlanosJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
		CConstantesLicencias.helpSetHomeID = "licenciasPlanos";
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
		mostrarJInternalFrame(new CGeneracionPlanos(this, literales));
		zoomMapa();
	}//GEN-LAST:event_generacionPlanosJMenuItemActionPerformed

	private void consultaNotificacionesJMenuItemActionPerformed() {//GEN-FIRST:event_consultaNotificacionesJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
		CConstantesLicencias.helpSetHomeID = "licenciasNotificaciones";
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
        CNotificaciones notificaciones= new CNotificaciones(this,literales);
        notificaciones.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(notificaciones);
	}//GEN-LAST:event_consultaNotificacionesJMenuItemActionPerformed

	private void mantenimientoHistoricoJMenuItemActionPerformed() {//GEN-FIRST:event_mantenimientoHistoricoJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
		CConstantesLicencias.helpSetHomeID = "licenciasHistorico";
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
        CMantenimientoHistorico mantenimientoHistorico= new CMantenimientoHistorico(this,literales);
        mantenimientoHistorico.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(mantenimientoHistorico);
	}//GEN-LAST:event_mantenimientoHistoricoJMenuItemActionPerformed

    private void tareasPendientesJMenuItemActionPerformed () {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
		CConstantesLicencias.helpSetHomeID = "licenciasTareasPendientes";
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
        TareasPendientesJInternalFrame tareasPendientes= new TareasPendientesJInternalFrame(this, literales);
        tareasPendientes._tareasPendientesJPanel.rellenarEventosYNotificaciones();
        tareasPendientes._tareasPendientesJPanel.aceptarJButtonSetEnabled(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(tareasPendientes);
    }

	private void generarInformesJMenuItemActionPerformed() {//GEN-FIRST:event_generarInformesJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
		CConstantesLicencias.helpSetHomeID = "licenciasInformes";
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
		mostrarJInternalFrame(new CGeneracionInformes(this, literales));
	}//GEN-LAST:event_generarInformesJMenuItemActionPerformed




	private void renombrarComponentes() {
		//System.out.println("ESTAMOS EJECUTANDO: RENOMBRARCOMPONENTES");
		try {
			//setTitle(literales.getString("CMainLicenciasForm.JFrame.title"));
			
			String title=literales.getString("CMainLicenciasForm.JFrame.title");
			String release=aplicacion.getString("localgis.release");
			if (release==null)
				release="LocalGIS";
			title=title.replaceAll("\\$\\{localgis\\.release\\}",release);				
	        setTitle(title);
			//System.out.println("Se establece ahora el título: "+literales.getString("CMainLicenciasForm.JFrame.title"));
            if (CConstantesLicencias.Municipio!=null){
            	
            	com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
            	
            	if(principal!=null)
            		  setTitle(getTitle()+" - "+CConstantesLicencias.Municipio + " ("+CConstantesLicencias.Provincia+") -"+literales.getString("CAuthDialog.jLabelNombre")+" " +principal);
            	else
            		setTitle(getTitle()+" - "+CConstantesLicencias.Municipio + " ("+CConstantesLicencias.Provincia+")");
            }//fin if
			gestionLicenciasMenu.setText(literales.getString("CMainLicenciasForm.gestionLicenciasMenu.text"));

			consultarLicenciaJMenu.setText(literales.getString("CMainLicenciasForm.consultarLicenciaJMenu.text"));
			consultarLicenciaObraMayorJMenuItem.setText(literales.getString("CMainLicenciasForm.consultarLicenciaObraMayorJMenuItem.text"));
			consultarLicenciaObraMenorJMenuItem.setText(literales.getString("CMainLicenciasForm.consultarLicenciaObraMenorJMenuItem.text"));

			crearLicenciaJMenu.setText(literales.getString("CMainLicenciasForm.crearLicenciaJMenu.text"));
			crearLicenciaObraMayorJMenuItem.setText(literales.getString("CMainLicenciasForm.crearLicenciaObraMayorJMenuItem.text"));
			crearLicenciaObraMenorJMenuItem.setText(literales.getString("CMainLicenciasForm.crearLicenciaObraMenorJMenuItem.text"));

			modificarLicenciaJMenu.setText(literales.getString("CMainLicenciasForm.modificarLicenciaJMenu.text"));
			modificarLicenciaObraMayorJMenuItem.setText(literales.getString("CMainLicenciasForm.modificarLicenciaObraMayorJMenuItem.text"));
			modificarLicenciaObraMenorJMenuItem.setText(literales.getString("CMainLicenciasForm.modificarLicenciaObraMenorJMenuItem.text"));


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
            tareasPendientesMenu.setText(literales.getString("CMainLicenciasForm.tareasPendientesMenu.text"));
            tareasPendientesJMenuItem.setText(literales.getString("CMainLicenciasForm.tareasPendientesJMenuItem.text"));

			idiomaMenu.setText(literales.getString("CMainLicenciasForm.idiomaMenu.text"));
			castellanoJMenuItem.setText(literales.getString("CMainLicenciasForm.castellanoJMenuItem.text"));
			catalanJMenuItem.setText(literales.getString("CMainLicenciasForm.catalanJMenuItem.text"));
			euskeraJMenuItem.setText(literales.getString("CMainLicenciasForm.euskeraJMenuItem.text"));
			gallegoJMenuItem.setText(literales.getString("CMainLicenciasForm.gallegoJMenuItem.text"));
			valencianoJMenuItem.setText(literales.getString("CMainLicenciasForm.valencianoJMenuItem.text"));
			ayudaJMenu.setText(literales.getString("CMainLicenciasForm.ayudaJMenu.text"));
			contenidoJMenuItem.setText(literales.getString("CMainLicenciasForm.contenidoJMenuItem.text"));
			acercaJMenuItem.setText(literales.getString("CMainLicenciasForm.acercaJMenuItem.text"));

            jMenuConfiguracion.setText(literales.getString("CMainLicenciasForm.jMenuConfiguracion"));//"Configuración");
            jMenuItemConfiguracion.setText(literales.getString("CMainLicenciasForm.jMenuItemConfiguracion"));//"Ver");

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
				internalFrame.setFrameIcon(new javax.swing.ImageIcon(CConstantesPaths.IMAGE_PATH + "geopista.gif"));


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

	public boolean mostrarAyuda()
    {
		HelpSet hs = null;
		ClassLoader loader = this.getClass().getClassLoader();
		URL url;
		try {
            String helpSetFile = "help/licenciasObra/LicenciasHelp_" + CConstantesLicencias.Locale.substring(0,2) + ".hs";
			url = HelpSet.findHelpSet(loader, helpSetFile);
			if (url == null) {                
                url=new URL("help/licenciasObra/LicenciasHelp_" + CConstantesLicencias.LocalCastellano.substring(0,2) + ".hs");
   			}
            hs = new HelpSet(loader, url);

			// ayuda sensible al contexto
			hs.setHomeID(CConstantesLicencias.helpSetHomeID);


		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString());

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

				internalFrame.setFrameIcon(new javax.swing.ImageIcon(CConstantesPaths.IMAGE_PATH + "geopista.gif"));

				desktopPane.add(internalFrame);
				internalFrame.setMaximum(true);
				internalFrame.show();
                try{pantallaMultilingue=(IMultilingue)internalFrame;}catch(Exception e){pantallaMultilingue=null;}
			} else {
				logger.info("cannot open another JInternalFrame");

			}

			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}

		return true;
	}

    public JDesktopPane getDesktopPane(){
        return this.desktopPane;
    }


	public void resetSecurityPolicy() {
		consultarLicenciaObraMayorJMenuItem.setEnabled(false);
		consultarLicenciaObraMenorJMenuItem.setEnabled(false);
		crearLicenciaObraMayorJMenuItem.setEnabled(false);
		crearLicenciaObraMenorJMenuItem.setEnabled(false);
		modificarLicenciaObraMayorJMenuItem.setEnabled(false);
		modificarLicenciaObraMenorJMenuItem.setEnabled(false);
		generarInformesJMenuItem.setEnabled(false);
		generacionPlanosJMenuItem.setEnabled(false);
		consultaEventosJMenuItem.setEnabled(false);
		ultimosEventosJMenuItem.setEnabled(false);
		consultaNotificacionesJMenuItem.setEnabled(false);
		mantenimientoHistoricoJMenuItem.setEnabled(false);
		idiomaMenu.setEnabled(false);
        jMenuConfiguracion.setEnabled(false);
        tareasPendientesJMenuItem.setEnabled(false);
	}


	public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal) {

		try {

			if ((acl == null) || (principal == null)) {
				return false;
			}

			CConstantesLicencias_LCGIII.principal = principal;
            setPermisos(acl.getPermissions(principal));

            if(tienePermisos("Geopista.Licencias.Login")){
	            if (tienePermisos("Geopista.Licencias.Consulta")) {
	                consultarLicenciaObraMayorJMenuItem.setEnabled(true);
	                consultarLicenciaObraMenorJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Licencias.Creacion")) {
	                crearLicenciaObraMayorJMenuItem.setEnabled(true);
	                crearLicenciaObraMenorJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Licencias.Modificacion")) {
	                modificarLicenciaObraMayorJMenuItem.setEnabled(true);
	                modificarLicenciaObraMenorJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Licencias.Informes")) {
	                generarInformesJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Licencias.Eventos")) {
	                consultaEventosJMenuItem.setEnabled(true);
	                ultimosEventosJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Licencias.Planos")) {
	                generacionPlanosJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Licencias.Notificaciones")) {
	                consultaNotificacionesJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Licencias.Historico")) {
	                mantenimientoHistoricoJMenuItem.setEnabled(true);
	            }
	
	            if (tienePermisos("Geopista.Licencias.Idiomas")) {
	                idiomaMenu.setEnabled(true);
	            }
	            if (tienePermisos("Geopista.Licencias.Eventos") && tienePermisos("Geopista.Licencias.Notificaciones")){
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


	private void consultarLicenciaObraMayorJMenuItemActionPerformed() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       	CConstantesLicencias.helpSetHomeID = "licenciasConsultaObraMayor";
		CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
		CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
        com.geopista.app.licencias.consulta.CConsultaLicencias consulta= new com.geopista.app.licencias.consulta.CConsultaLicencias(this, null);
        consulta.setEnabledModificarJButton(tienePermisos("Geopista.Licencias.Modificacion"));
        consulta.setEnabledPublicarJButton(tienePermisos("Geopista.Licencias.Publicacion"));
		mostrarJInternalFrame(consulta);
		zoomMapa();
	}
	private void consultarLicenciaObraMenorJMenuItemActionPerformed() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       	CConstantesLicencias.helpSetHomeID = "licenciasConsultaObraMenor";
		CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMenor;
        setLang("licenciasObraMenor",CConstantesLicencias.Locale);
		CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
        com.geopista.app.licencias.obraMenor.consulta.CConsultaLicencias consulta= new com.geopista.app.licencias.obraMenor.consulta.CConsultaLicencias(this, null);
        consulta.setEnabledModificarJButton(tienePermisos("Geopista.Licencias.Modificacion"));
        consulta.setEnabledPublicarJButton(tienePermisos("Geopista.Licencias.Publicacion"));
		mostrarJInternalFrame(consulta);
		zoomMapa();
	}

	private void crearLicenciaObraMayorJMenuItemActionPerformed() {//GEN-FIRST:event_crearLicenciaObraMayorJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.helpSetHomeID = "licenciasCreacionObraMayor";
		CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
    	CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
		mostrarJInternalFrame(new com.geopista.app.licencias.creacion.CCreacionLicencias(this));
		zoomMapa();
	}//GEN-LAST:event_crearLicenciaObraMayorJMenuItemActionPerformed


	private void crearLicenciaObraMenorJMenuItemActionPerformed() {//GEN-FIRST:event_crearLicenciaObraMenorJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.helpSetHomeID = "licenciasCreacionObraMenor";
		CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMenor;
        setLang("licenciasObraMenor",CConstantesLicencias.Locale);
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
		mostrarJInternalFrame(new com.geopista.app.licencias.obraMenor.creacion.CCreacionLicencias(this));
		zoomMapa();
	}//GEN-LAST:event_crearLicenciaObraMenorJMenuItemActionPerformed




	private void modificarLicenciaObraMayorJMenuItemActionPerformed() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	CConstantesLicencias.helpSetHomeID = "licenciasModificacionObraMayor";
		CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
	    CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
		mostrarJInternalFrame(new com.geopista.app.licencias.modificacion.CModificacionLicencias(this, null, true));
		zoomMapa();
	}
	private void modificarLicenciaObraMenorJMenuItemActionPerformed() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       	CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMenor;
		CConstantesLicencias.helpSetHomeID = "licenciasModificacionObraMenor";
        setLang("licenciasObraMenor",CConstantesLicencias.Locale);
		CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;
    	mostrarJInternalFrame(new com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias(this, null, true));
    	zoomMapa();
	}

     private void mostrarConfiguracion()
    {
        JDialogConfiguracion dialogConfiguracion = new JDialogConfiguracion(this, true, literales, "LICENCIAS_OBRA");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		dialogConfiguracion.setSize(400, 250);
		dialogConfiguracion.setLocation(d.width / 2 - dialogConfiguracion.getSize().width / 2, d.height / 2 - dialogConfiguracion.getSize().height / 2);
		dialogConfiguracion.setResizable(false);
		dialogConfiguracion.show();
		dialogConfiguracion = null;
    }

	private void acercaJMenuItemActionPerformed() {//GEN-FIRST:event_acercaJMenuItemActionPerformed
       CConstantesLicencias.helpSetHomeID = "licenciasIntro";
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;

        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
        CAcercaDeJDialog acercaDe= new CAcercaDeJDialog(this, true);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        acercaDe.setSize(255, 240);
        acercaDe.setLocation(d.width / 2 - acercaDe.getSize().width / 2, d.height / 2 - acercaDe.getSize().height / 2);
        acercaDe.setResizable(false);
        acercaDe.show();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


	}//GEN-LAST:event_acercaJMenuItemActionPerformed


	private void castellanoJMenuItemActionPerformed() {
		CConstantesLicencias.Locale = CConstantesLicencias.LocalCastellano;
		setLang(CConstantesLicencias.Locale);
	}

	private void gallegoJMenuItemActionPerformed() {
        CConstantesLicencias.Locale = CConstantesLicencias.LocalGallego;
        setLang(CConstantesLicencias.Locale);
	}
	private void catalanJMenuItemActionPerformed() {
        CConstantesLicencias.Locale = CConstantesLicencias.LocalCatalan;
        setLang(CConstantesLicencias.Locale);
	}
	private void euskeraJMenuItemActionPerformed() {
        CConstantesLicencias.Locale = CConstantesLicencias.LocalEuskera;
        setLang(CConstantesLicencias.Locale);
    }

	private void valencianoJMenuItemActionPerformed() {
        CConstantesLicencias.Locale = CConstantesLicencias.LocalValenciano;
        setLang(CConstantesLicencias.Locale);
    }


   /* private void mostrarEventosYNotificacionesPendientesJDialog() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
        TareasPendientesJDialog tareasPendientes= new TareasPendientesJDialog(this, true);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        tareasPendientes.setSize(900, 700);
        tareasPendientes.setLocation(d.width / 2 - tareasPendientes.getSize().width / 2, d.height / 2 - tareasPendientes.getSize().height / 2);
        tareasPendientes.setResizable(false);

        boolean hayPendientes= tareasPendientes._tareasPendientesJPanel.rellenarEventosYNotificaciones();
        if (hayPendientes){
            tareasPendientes.show();
        }else CUtilidadesComponentes.menuLicenciasSetEnabled(true, (JFrame)this);
        tareasPendientes = null;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }  */

    private void mostrarEventosYNotificacionesPendientesJInternalFrame(){

        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final TareasPendientesJInternalFrame tareasPendientes= new TareasPendientesJInternalFrame(this,literales);
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        setLang("licenciasObraMayor",CConstantesLicencias.Locale);
        CConstantesLicencias.helpSetHomeID = "licenciasTareasPendientes";
        CConstantesLicencias_LCGIII.persona= null;
        CConstantesLicencias_LCGIII.representante= null;
        CConstantesLicencias_LCGIII.tecnico= null;
        CConstantesLicencias_LCGIII.promotor= null;


        boolean hayPendientes= tareasPendientes._tareasPendientesJPanel.rellenarEventosYNotificaciones();
        if (hayPendientes){
            tareasPendientes._tareasPendientesJPanel.aceptarJButtonSetEnabled(tienePermisos("Geopista.Licencias.Modificacion"));
            mostrarJInternalFrame(tareasPendientes);
        }else CUtilidadesComponentes.menuLicenciasSetEnabled(true, (JFrame)this);

        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }



	/**
	 * Exit the Application
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		try {
            if (pantallaMultilingue!=null)
            {
                if (pantallaMultilingue instanceof com.geopista.app.licencias.modificacion.CModificacionLicencias)
                    ((com.geopista.app.licencias.modificacion.CModificacionLicencias)pantallaMultilingue).desbloquearExpediente();
                if (pantallaMultilingue instanceof com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias)
                    ((com.geopista.app.licencias.obraMenor.modificacion.CModificacionLicencias)pantallaMultilingue).desbloquearExpediente();
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


	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

		//logger.info("main");
		new CMainLicencias();
	}

    public Vector getTiposLicencia()
    {
         Vector vectorTipos= new Vector();
         vectorTipos.add(new CTipoLicencia(CConstantesLicencias.ObraMayor,"",""));
         vectorTipos.add(new CTipoLicencia(CConstantesLicencias.ObraMenor,"",""));
         return vectorTipos;
    }

    public Vector getTiposLicenciaObra(){
         return getTiposLicencia();
    }

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


	// Variables declaration - do not modify
	private javax.swing.JMenuItem contenidoJMenuItem;
	private javax.swing.JMenuItem acercaJMenuItem;

	private javax.swing.JMenuItem castellanoJMenuItem;
	private javax.swing.JMenuItem catalanJMenuItem;
	private javax.swing.JMenuItem consultaEventosJMenuItem;
	private javax.swing.JMenuItem consultaNotificacionesJMenuItem;


	private javax.swing.JMenu consultarLicenciaJMenu;
	private javax.swing.JMenuItem consultarLicenciaObraMayorJMenuItem;
	private javax.swing.JMenuItem consultarLicenciaObraMenorJMenuItem;


	private javax.swing.JMenu crearLicenciaJMenu;
	private javax.swing.JMenuItem crearLicenciaObraMayorJMenuItem;
	private javax.swing.JMenuItem crearLicenciaObraMenorJMenuItem;

	private javax.swing.JMenu modificarLicenciaJMenu;
	private javax.swing.JMenuItem modificarLicenciaObraMayorJMenuItem;
	private javax.swing.JMenuItem modificarLicenciaObraMenorJMenuItem;



	private javax.swing.JDesktopPane desktopPane;
	private javax.swing.JMenuItem euskeraJMenuItem;
	private javax.swing.JMenuItem gallegoJMenuItem;
	private javax.swing.JMenuItem generacionPlanosJMenuItem;
	private javax.swing.JMenuItem generarInformesJMenuItem;
	private javax.swing.JMenu gestionEventosMenu;
	private javax.swing.JMenu gestionHistoricoMenu;
    private javax.swing.JMenu tareasPendientesMenu;
	private javax.swing.JMenu gestionLicenciasMenu;
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
    private javax.swing.JMenu jMenuConfiguracion;
    private javax.swing.JMenuItem jMenuItemConfiguracion;
	// End of variables declaration

}


