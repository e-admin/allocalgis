package com.geopista.app.licencias.actividad;

import com.geopista.app.utilidades.CMain;
import com.geopista.app.utilidades.JDialogConfiguracion;
import com.geopista.app.licencias.informes.CGeneracionInformes;
import com.geopista.app.licencias.pendientes.TareasPendientesJInternalFrame;
import com.geopista.app.licencias.historico.CMantenimientoHistorico;
import com.geopista.app.licencias.notificaciones.CNotificaciones;
import com.geopista.app.licencias.planos.CGeneracionPlanos;
import com.geopista.app.licencias.eventos.CUltimosEventos;
import com.geopista.app.licencias.eventos.CConsultaEventos;
import com.geopista.app.licencias.*;
import com.geopista.app.AppContext;
import com.geopista.protocol.licencias.CConstantesPaths;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.CConstantesComando;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.ui.dialogs.MunicipioDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.editor.GeopistaEditor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.help.HelpSet;
import javax.help.HelpBroker;
import javax.help.CSH;

import java.security.acl.AclNotFoundException;
import java.util.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.URL;

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



public class MainActividadLicencias extends MainActividad implements IMainLicencias
 {

	//static Logger logger = Logger.getLogger(MainActividad.class);
	 private static Logger logger;
	    static {
	       createDir();
	       logger  = Logger.getLogger(MainActividadLicencias.class);
	    }		
 	public static ResourceBundle literales = null;
    private IMultilingue pantallaMultilingue;
    private Hashtable permisos= new Hashtable();
    public static GeopistaEditor geopistaEditor;
	private boolean fromInicio;
	private ApplicationContext aplicacion = AppContext.getApplicationContext();
	private Blackboard blackboard = aplicacion.getBlackboard();
	
	public MainActividadLicencias() {
		this(false);
	}    
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }	
	public MainActividadLicencias(boolean fromInicio) {
		this.fromInicio=fromInicio;
        AppContext.getApplicationContext().setMainFrame(this);
        try {initLookAndFeel();} catch (Exception e) {}
		SplashWindow splashWindow = showSplash();
		initComponents();
		configureApp();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
        try
        {
            ClassLoader cl = this.getClass().getClassLoader();
            java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(cl.getResource("img/geopista.gif"));
            setIconImage(img);
            System.out.println("Icono encontrado");
        }catch(Exception e)
        {
            System.out.println("Icono no encontrado");
        }
		try {
			show();
            com.geopista.app.licencias.CUtilidadesComponentes.inicializar();
            com.geopista.app.licencias.CUtilidadesComponentes.menuLicenciasSetEnabled(false, this);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         	if (splashWindow != null) splashWindow.setVisible(false);
            //******************
            //Mostramos la pantalla de autenticación del usuario.
            //******************
         	//--------NUEVO-------------->	
			if(SSOAuthManager.isSSOActive())
				CConstantesComando.loginLicenciasUrl = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
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
            if(!tienePermisos("Geopista.Licencias.Login")){
            	noPermApp();
            	return;
            }           
            if (!AppContext.seleccionarMunicipio((Frame)this)){
            	stopApp();
            	return;
            }
            CConstantesLicencias.IdMunicipio = AppContext.getIdMunicipio();
            
            Municipio municipio = (new OperacionesAdministrador(com.geopista.protocol.CConstantesComando.loginLicenciasUrl)).getMunicipio(new Integer(CConstantesLicencias.IdMunicipio).toString());
            if (municipio!=null)
            {
                   CConstantesLicencias.Municipio = municipio.getNombre();
                   CConstantesLicencias.Provincia= municipio.getProvincia();
                   setTitle(getTitle()+" - "+CConstantesLicencias.Municipio + " ("+CConstantesLicencias.Provincia+")");
            }

            /** dialogo de espera de carga */
            final JFrame desktop= (JFrame)this;
            final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
            progressDialog.setTitle(MainActividadLicencias.literales.getString("Licencias.Tag3"));
            progressDialog.addComponentListener(new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    new Thread(new Runnable(){
                        public void run(){
                            /* añadimos el documento a la lista */
                            try{
                                progressDialog.report(MainActividadLicencias.literales.getString("Licencias.Tag2"));
                                /** cargamos las estructuras */
                                while (!com.geopista.app.licencias.estructuras.Estructuras.isCargada())
                                {
                                    if (!com.geopista.app.licencias.estructuras.Estructuras.isIniciada()) com.geopista.app.licencias.estructuras.Estructuras.cargarEstructuras();
                                    try {Thread.sleep(500);}catch(Exception e){}
                                }

                                progressDialog.report(MainActividadLicencias.literales.getString("Licencias.Tag3"));
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

		} catch (Exception e) {
			logger.error("Exception: " ,e);
		}
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
             logger.error("ERROR al autenticar al usuario ",e);
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
		
	    private void  stopApp(){
	    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Inicio de aplicación cancelado. Reinicie el aplicativo");
	    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
			try{org.apache.log4j.PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);}catch(Exception e){};


			//****************************************************************
			//** Cargamos la configuracion de config.ini
			//*******************************************************
            try{
                   ApplicationContext app= AppContext.getApplicationContext();

                   com.geopista.protocol.CConstantesComando.servletLicenciasUrl = app.getString("geopista.conexion.servidorurl")+"licencias/CServletLicencias";
                   com.geopista.protocol.CConstantesComando.anexosLicenciasUrl = app.getString("geopista.conexion.servidorurl")+"anexos/licencias/";
                   com.geopista.protocol.CConstantesComando.loginLicenciasUrl = app.getString("geopista.conexion.servidorurl")+"licencias";
                   com.geopista.protocol.CConstantesComando.adminCartografiaUrl = app.getString("geopista.conexion.servidorurl")+"geopista/";
                   com.geopista.protocol.CConstantesComando.plantillasActividadUrl = app.getString("geopista.conexion.servidorurl")+"plantillas/actividad/";
                   com.geopista.app.licencias.CConstantesLicencias.Locale = app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");



                   try{CConstantesLicencias.IdMunicipio=new Integer(app.getString("geopista.DefaultCityId")).intValue();
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
                   CConstantesLicencias.DBPassword = app.getUserPreference("conexion.pass","",false);;

                   try{
                       CConstantesLicencias.totalMaxSizeFilesUploaded= new Long(app.getString("geopista.anexos.totalMaxSizeFilesUploaded")).longValue();
                   }catch(Exception ex){
                      System.out.println("[CMainLicencias.configureApp]:geopista.anexos.totalMaxSizeFilesUploaded="+app.getString("geopista.anexos.totalMaxSizeFilesUploaded"));
                      System.out.println("[CMainLicencias.configureApp]"+ex.toString());
                   }

            }catch(Exception e){
                    JOptionPane.showMessageDialog(this, "Excepcion al cargar el fichero de configuración:\n"+e.toString());
			        logger.error("Exception: " , e);
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
			setLang(com.geopista.app.licencias.CConstantesLicencias.Locale);
			return true;
		} catch (Exception ex) {
			logger.error("Exception: " ,ex);
			return false;
		}
	}

	public void setLang(String locale) {
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
                Locale currentLocale = new Locale(locale);
                try
                {
			        literales = ResourceBundle.getBundle("config.licenciasActividad", currentLocale);
                }catch (Exception e)
                {
                    literales = ResourceBundle.getBundle("config.licenciasActividad", new Locale(com.geopista.app.licencias.CConstantesLicencias.LocalCastellano));
                }
                com.geopista.app.licencias.CConstantesLicencias.Locale = locale;
                renombrarComponentes();
                if (pantallaMultilingue!=null) pantallaMultilingue.renombrarComponentes(literales);
         } catch (Exception e) {
             logger.error("Excepcion al cambiar el idicoma a: " + locale,e);
         }
	}

     /**
	 * Inicializa los componentes de la pantalla
	 */
	private void initComponents() {
		desktopPane = new javax.swing.JDesktopPane();
		menuBar = new javax.swing.JMenuBar();
		gestionLicenciasMenu = new javax.swing.JMenu();
		jMenuSalir = new javax.swing.JMenuItem();
		jMenuItemConsultar = new javax.swing.JMenuItem();
		jMenuItemCrear = new javax.swing.JMenuItem();
		jMenuItemModificar = new javax.swing.JMenuItem();

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

		setBackground(new java.awt.Color(255, 255, 255));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(null);
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

		jMenuItemConsultar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				consultar();
			}
		});
		gestionLicenciasMenu.add(jMenuItemConsultar);

		jMenuItemCrear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				crear();
			}
		});
		gestionLicenciasMenu.add(jMenuItemCrear);

		jMenuItemModificar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modificar();
			}
		});
		gestionLicenciasMenu.add(jMenuItemModificar);

		generarInformesJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				generarInformes();
			}
		});
		gestionLicenciasMenu.add(generarInformesJMenuItem);
		menuBar.add(gestionLicenciasMenu);
		
		jMenuSalir.setMnemonic('S');
		jMenuSalir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitForm();
			}
		});

		gestionLicenciasMenu.add(jMenuSalir);
		
		consultaEventosJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				consultaEventos();
			}
		});
		gestionEventosMenu.add(consultaEventosJMenuItem);

		ultimosEventosJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ultimosEventos();
			}
		});
		gestionEventosMenu.add(ultimosEventosJMenuItem);
		menuBar.add(gestionEventosMenu);

		generacionPlanosJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				generacionPlanos();
			}
		});
		planosMenu.add(generacionPlanosJMenuItem);
		menuBar.add(planosMenu);

		consultaNotificacionesJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				consultaNotificaciones();
			}
		});
		notificacionesJMenu.add(consultaNotificacionesJMenuItem);
		menuBar.add(notificacionesJMenu);

		mantenimientoHistoricoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mantenimientoHistorico();
			}
		});
		gestionHistoricoMenu.add(mantenimientoHistoricoJMenuItem);
		menuBar.add(gestionHistoricoMenu);

        tareasPendientesJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tareasPendientes();
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

		idiomaMenu.setMargin(null);
		castellanoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setLang(com.geopista.app.licencias.CConstantesLicencias.LocalCastellano);
			}
		});
		idiomaMenu.add(castellanoJMenuItem);

		idiomaMenu.add(catalanJMenuItem);
		catalanJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                setLang(com.geopista.app.licencias.CConstantesLicencias.LocalCatalan);
			}
		});


		idiomaMenu.add(euskeraJMenuItem);
		euskeraJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setLang(com.geopista.app.licencias.CConstantesLicencias.LocalEuskera);
			}
		});

		idiomaMenu.add(gallegoJMenuItem);
		gallegoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                setLang(com.geopista.app.licencias.CConstantesLicencias.LocalGallego);
			}
		});


		idiomaMenu.add(valencianoJMenuItem);
		valencianoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                setLang(com.geopista.app.licencias.CConstantesLicencias.LocalValenciano);
			}
		});
		menuBar.add(idiomaMenu);

		ayudaJMenu.setMargin(null);
		contenidoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mostrarAyuda();
			}
		});

		acercaJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				acerca();
			}
		});

		ayudaJMenu.add(contenidoJMenuItem);
		ayudaJMenu.add(acercaJMenuItem);
		menuBar.add(ayudaJMenu);
		setJMenuBar(menuBar);
		pack();
	}//GEN-END:initComponents

	private void ultimosEventos() {//GEN-FIRST:event_ultimosEventosJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.helpSetHomeID = "licenciasUltimosEventos";
        CUltimosEventos ultimosEventos= new CUltimosEventos(this,literales);
        ultimosEventos.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(ultimosEventos);
	}

	private void consultaEventos() {//GEN-FIRST:event_consultaEventosJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		CConstantesLicencias.helpSetHomeID = "licenciasConsultarEventos";
        CConsultaEventos consultaEventos= new CConsultaEventos(this,literales);
        consultaEventos.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(consultaEventos);
	}//GEN-LAST:event_consultaEventosJMenuItemActionPerformed

	private void generacionPlanos() {//GEN-FIRST:event_generacionPlanosJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		mostrarJInternalFrame(new CGeneracionPlanos(this,literales));
		zoomMapa();
	}//GEN-LAST:event_generacionPlanosJMenuItemActionPerformed

	private void consultaNotificaciones() {//GEN-FIRST:event_consultaNotificacionesJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		CConstantesLicencias.helpSetHomeID = "licenciasNotificaciones";
        CNotificaciones notificaciones= new CNotificaciones(this,literales);
        notificaciones.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(notificaciones);
	}//GEN-LAST:event_consultaNotificacionesJMenuItemActionPerformed

	private void mantenimientoHistorico() {//GEN-FIRST:event_mantenimientoHistoricoJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		CConstantesLicencias.helpSetHomeID = "licenciasHistorico";
        CMantenimientoHistorico mantenimientoHistorico= new CMantenimientoHistorico(this,literales);
        mantenimientoHistorico.setEnabledIrExpedienteJButton(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(mantenimientoHistorico);
	}//GEN-LAST:event_mantenimientoHistoricoJMenuItemActionPerformed

    private void tareasPendientes() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		CConstantesLicencias.helpSetHomeID = "licenciasTareasPendientes";
        TareasPendientesJInternalFrame tareasPendientes= new TareasPendientesJInternalFrame(this,literales);
        tareasPendientes._tareasPendientesJPanel.rellenarEventosYNotificaciones();
        tareasPendientes._tareasPendientesJPanel.aceptarJButtonSetEnabled(tienePermisos("Geopista.Licencias.Modificacion"));
		mostrarJInternalFrame(tareasPendientes);
    }

	private void generarInformes() {//GEN-FIRST:event_generarInformesJMenuItemActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.helpSetHomeID = "licenciasInformes";
        mostrarJInternalFrame(new CGeneracionInformes(this,literales));
	}//GEN-LAST:event_generarInformesJMenuItemActionPerformed


	private void renombrarComponentes() {
		try {
			//setTitle(literales.getString("CMainLicenciasForm.JFrame.title"));
			String title=literales.getString("CMainLicenciasForm.JFrame.title");
			String release=aplicacion.getString("localgis.release");
			if (release==null)
				release="LocalGIS";
			title=title.replaceAll("\\$\\{localgis\\.release\\}",release);				
	        setTitle(title);
            if (CConstantesLicencias.Municipio!=null)
                setTitle(getTitle()+" - "+CConstantesLicencias.Municipio + " ("+CConstantesLicencias.Provincia+")");
			gestionLicenciasMenu.setText(literales.getString("CMainLicenciasForm.gestionLicenciasMenu.text"));

			jMenuItemConsultar.setText(literales.getString("CMainLicenciasForm.consultarLicenciaJMenu.text"));
			jMenuItemCrear.setText(literales.getString("CMainLicenciasForm.crearLicenciaJMenu.text"));
			jMenuItemModificar.setText(literales.getString("CMainLicenciasForm.modificarLicenciaJMenu.text"));

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
			logger.error("Exception al renombrar las etiquetas: " ,ex);
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
			logger.warn("Exception al mostrar la pantalla: " , ex);
		}

		return true;
	}

	public boolean mostrarAyuda()
    {
		HelpSet hs = null;
        ClassLoader loader = this.getClass().getClassLoader();
        try
        {      String helpSetFile = "help/licenciasActividad/LicenciasHelp_" + com.geopista.app.licencias.CConstantesLicencias.Locale.substring(0,2) + ".hs";
                URL url = HelpSet.findHelpSet(loader, helpSetFile);
                if (url == null)//tomamos el idioma castellano por defecto
                {
                    logger.error("Imposible cargar el fichero de ayuda: "+helpSetFile);
                    helpSetFile = "help/licenciasActividad/LicenciasHelp_" + com.geopista.app.licencias.CConstantesLicencias.LocalCastellano.substring(0,2) + ".hs";
                    url = HelpSet.findHelpSet(loader, helpSetFile);
                }
                if (url== null)
                {
                    logger.error("Imposible cargar el fichero de ayuda: "+helpSetFile);
                    return false;
                }
                hs = new HelpSet(loader, url);
                hs.setHomeID(com.geopista.app.licencias.CConstantesLicencias.helpSetHomeID);
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
		jMenuItemConsultar.setEnabled(false);
		jMenuItemCrear.setEnabled(false);
		jMenuItemModificar.setEnabled(false);
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


	public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal)
    {

		try {
			if ((acl == null) || (principal == null)) {
				return false;
			}
			CConstantesLicenciasActividad.principal = principal;
            setPermisos(acl.getPermissions(principal));
            
            if(tienePermisos("Geopista.Licencias.Login")){
	            if (tienePermisos("Geopista.Licencias.Consulta")) {
	                jMenuItemConsultar.setEnabled(true);
	            }
	            if (tienePermisos("Geopista.Licencias.Creacion")) {
	                jMenuItemCrear.setEnabled(true);
	            }
	            if (tienePermisos("Geopista.Licencias.Modificacion")) {
	                jMenuItemModificar.setEnabled(true);
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
            logger.error("Exception: " , ex);
			return false;
		}
	}

	private void consultar() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       	CConstantesLicencias.helpSetHomeID = "licenciasConsultaActividad";
        com.geopista.app.licencias.actividad.CConsultaLicencias consulta= new com.geopista.app.licencias.actividad.CConsultaLicencias(this,null,literales);
        consulta.setEnabledModificarJButton(tienePermisos("Geopista.Licencias.Modificacion"));
        consulta.setEnabledPublicarJButton(tienePermisos("Geopista.Licencias.Publicacion"));
		mostrarJInternalFrame(consulta);
		zoomMapa();
	}
	private void crear() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       	CConstantesLicencias.helpSetHomeID = "licenciasCreacionActividad";
		mostrarJInternalFrame(new com.geopista.app.licencias.actividad.CCreacionLicencias(this,literales));
		zoomMapa();
	}


	private void modificar() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		CConstantesLicencias.helpSetHomeID = "licenciasModificacionActividad";
		mostrarJInternalFrame(new com.geopista.app.licencias.actividad.CModificacionLicencias(this, null,literales, true));
		zoomMapa();
	}

     private void mostrarConfiguracion()
    {
        JDialogConfiguracion dialogConfiguracion = new JDialogConfiguracion(this, true, literales, "LICENCIAS_ACTIVIDAD");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		dialogConfiguracion.setSize(400, 250);
		dialogConfiguracion.setLocation(d.width / 2 - dialogConfiguracion.getSize().width / 2, d.height / 2 - dialogConfiguracion.getSize().height / 2);
		dialogConfiguracion.setResizable(false);
		dialogConfiguracion.show();
		dialogConfiguracion = null;
    }

	private void acerca() {//GEN-FIRST:event_acercaJMenuItemActionPerformed
        CConstantesLicencias.helpSetHomeID = "licenciasIntro";
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


    private void mostrarEventosYNotificacionesPendientesJInternalFrame(){
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        TareasPendientesJInternalFrame tareasPendientes= new TareasPendientesJInternalFrame(this,literales);
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CConstantesLicencias.selectedSubApp=CConstantesLicencias.LicenciasObraMayor;
        CConstantesLicencias.helpSetHomeID = "licenciasTareasPendientes";
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


	/**
	 * Exit the Application
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		try {
            if (pantallaMultilingue!=null)
            {
                if (pantallaMultilingue instanceof com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias)
                    ((com.geopista.app.licencias.actividad.modificacion.CModificacionLicencias)pantallaMultilingue).desbloquearExpediente();
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
	public static void main(String args[])
    {
    	new MainActividadLicencias();
	}

    public Vector getTiposLicencia()
    {
         Vector vectorTipos= new Vector();
         vectorTipos.add(new CTipoLicencia(CConstantesLicencias.Actividades,"",""));
         vectorTipos.add(new CTipoLicencia(CConstantesLicencias.ActividadesNoCalificadas,"",""));
         return vectorTipos;
    }

    public Vector getTiposLicenciaObra(){
         Vector vectorTipos= new Vector();
         vectorTipos.add(new CTipoLicencia(CConstantesLicencias.ObraMayor,"",""));
         vectorTipos.add(new CTipoLicencia(CConstantesLicencias.ObraMenor,"",""));
         return vectorTipos;
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

	private javax.swing.JMenuItem jMenuItemConsultar;
	private javax.swing.JMenuItem jMenuItemCrear;
	private javax.swing.JMenuItem jMenuItemModificar;

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



