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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */
package com.geopista.app.contaminantes;

import com.geopista.app.contaminantes.CAcercaDeJDialog;
import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.app.contaminantes.arbolado.JFrameArbolado;
import com.geopista.app.contaminantes.arbolado.JFrameInformes;
import com.geopista.app.contaminantes.vertederos.JFrameInformesVertedero;
import com.geopista.app.contaminantes.actividades.JFrameContaminantes;
import com.geopista.app.contaminantes.actividades.JDialogInspectores;
import com.geopista.app.contaminantes.actividades.JDialogBuscarContaminantes;
import com.geopista.app.contaminantes.actividades.JFrameInformesActividades;
import com.geopista.app.contaminantes.vertederos.JFrameVertedero;
import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.contaminantes.planos.JFrameGeneracionPlanos;
import com.geopista.app.contaminantes.util.CUtilidades;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.ui.dialogs.MunicipioDialog;
import com.geopista.ui.dialogs.SystemConfigPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
import com.geopista.editor.GeopistaEditor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import javax.help.HelpSet;
import javax.help.HelpBroker;
import javax.help.CSH;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.awt.*;
import java.net.URL;


/*
 * GeoPistaMDI.java
 *
 * Created on 13 de febrero de 2004, 11:38
 */

/**
 * @author avivar
 */
public class CMainContaminantes extends CMain {
	//public static String ficheroConfiguracion = "config" + java.io.File.separator + "configContaminantes.ini";
  	//Logger logger = Logger.getLogger(CMainContaminantes.class);
  	
	 private static Logger logger;
	    static {
	       createDir();
	       logger  = Logger.getLogger(CMainContaminantes.class);
	    }  	
	public static final String idApp = "Contaminantes";
	JFrameContaminantes jFrameContaminantes;
	JFrameArbolado jFrameArbolado;
	JFrameVertedero jFrameVertedero;
	JFrameInformes jFrameInformesArbolado;
	JFrameInformesVertedero jFrameInformesVertedero;
    JFrameInformesActividades jFrameInformesActividades;
    JFrameHistoricoContaminantes jFrameHistorico;
    JFrameGeneracionPlanos jFramePlanos;
	public static ResourceBundle messages;
    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(this,null);
    public static GeopistaEditor geopistaEditor;
	private boolean fromInicio;
	private ApplicationContext aplicacion = AppContext.getApplicationContext();
	private Blackboard blackboard = aplicacion.getBlackboard();
    
	public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    } 
	public static GeopistaAcl acl;

	public CMainContaminantes() {
		this(false);
	}	

	public CMainContaminantes(boolean fromInicio) {
		
		this.fromInicio=fromInicio;
        AppContext.getApplicationContext().setMainFrame(this);
		try {initLookAndFeel();} catch (Exception e) {}
		try {
			//System.out.println(System.getProperty("user.dir"));
			SplashWindow splashWindow = showSplash();
			initComponents();
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			show();
			configureApp();
            
            CUtilidades.inicializar();
			if (splashWindow != null) splashWindow.setVisible(false);
            enabled();
            //******************
            //Mostramos la pantalla de autenticación del usuario.
            //******************
			//Antes de autenticar al usuario hacemos un logout
			if (SecurityManager.getIdSesion()!=null)
				SecurityManager.logout();
			
			//--------NUEVO-------------->	
			if(SSOAuthManager.isSSOActive())
				com.geopista.app.contaminantes.init.Constantes.url = AppContext.getApplicationContext().getString(SSOAuthManager.SSO_SERVER_URL);
			SSOAuthManager.ssoAuthManager(CMainContaminantes.idApp);								
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
            
            //AppContext.seleccionarMunicipio((Frame)this);
            if (!AppContext.seleccionarMunicipio((Frame)this)){
            	stopApp();
            	return;
            }
            com.geopista.app.contaminantes.init.Constantes.idMunicipio = AppContext.getIdMunicipio();
            
            if (com.geopista.security.SecurityManager.isLogged())
            {
                acl= com.geopista.security.SecurityManager.getPerfil("Contaminantes");
                enabled();
            }
            //com.geopista.security.SecurityManager.setHeartBeatTime(1000000);
            com.geopista.security.SecurityManager.setHeartBeatTime(10000);
			/** cargamos las estructuras */
			while (!com.geopista.app.contaminantes.estructuras.Estructuras.isCargada()) {
				if (!com.geopista.app.contaminantes.estructuras.Estructuras.isIniciada()) com.geopista.app.contaminantes.estructuras.Estructuras.cargarEstructuras();
				try {Thread.sleep(500);} catch (Exception e) {}
			}
            Municipio municipio = (new OperacionesAdministrador(com.geopista.app.contaminantes.init.Constantes.url)).getMunicipio(new Integer(com.geopista.app.contaminantes.init.Constantes.idMunicipio).toString());
            if (municipio!=null)
            {
                Constantes.Municipio = municipio.getNombre();
                Constantes.Provincia= municipio.getProvincia();
              
                com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
                if(principal!=null)
                setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+") -"+ messages.getString("CAuthDialog.jLabelNombre")+" "+principal);
            }

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println("ERROR:" + sw.toString());
			logger.error("Exception: " + sw.toString());
		}
	}
    public void enabled()
       {
           if (com.geopista.security.SecurityManager.isLogged() && acl!=null)
           {
               if (acl.getPermissions()!=null)
               {
                   for (Enumeration e=acl.getPermissions();e.hasMoreElements();)
                   {
                       GeopistaPermission per=(GeopistaPermission)e.nextElement();
                       //System.out.println("PERMISO: "+per.getName());
                   }
               }
               else
               {
                   System.out.println("Permisos nulos");
               }
               jMenuItemActividadesInformes.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuItemInspectores.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuBuscarActividad.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuCrearActividad.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuMostrarMapa.setEnabled(false);
               jMenuItemVerVertederos.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuItemVerHistorico.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuPlanos.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuItemVerPlanos.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuItemVertederoInformes.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuItemVerArbolado.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));
               jMenuItemArboladoInformes.setEnabled(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_CONTAMINANTES)));

           }else
           {
               jMenuItemActividadesInformes.setEnabled(false);
               jMenuItemInspectores.setEnabled(false);
               jMenuBuscarActividad.setEnabled(false);
               jMenuCrearActividad.setEnabled(false);
               jMenuMostrarMapa.setEnabled(false);
               jMenuItemVerVertederos.setEnabled(false);
               jMenuItemVerHistorico.setEnabled(false);
               jMenuPlanos.setEnabled(false);
               jMenuItemVerPlanos.setEnabled(false);
               jMenuItemVertederoInformes.setEnabled(false);
               jMenuItemVerArbolado.setEnabled(false);
               jMenuItemArboladoInformes.setEnabled(false);

           }

       }


	private boolean showAuth() {
       try
       {
    	   boolean resultado=false;
		    CAuthDialog auth = new CAuthDialog(this, true, com.geopista.app.contaminantes.init.Constantes.url,
				CMainContaminantes.idApp, com.geopista.app.contaminantes.init.Constantes.idMunicipio,
				messages);
		    auth.setBounds(30, 60, 315, 155);
            if (fromInicio){
            	resultado=auth.showD(true);
            	if (!resultado)
            		return false;
            }
            else{
            	auth.show();
            }
        } catch(Exception e)
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

	private boolean configureApp() {

		try {

			//****************************************************************
			//** Inicializamos el log4j
			//*******************************************************
			try{PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);}catch(Exception e){}

			//****************************************************************
			//** Cargamos la configuracion de config.ini
			//*******************************************************
	         try{
                   ApplicationContext app= AppContext.getApplicationContext();
                   com.geopista.app.contaminantes.init.Constantes.url= app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"contaminantes/";
                   com.geopista.protocol.CConstantesComando.servletLicenciasUrl = app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"licencias/CServletLicencias";
                   com.geopista.protocol.CConstantesComando.anexosLicenciasUrl = app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"anexos/licencias/";
                   com.geopista.protocol.CConstantesComando.loginLicenciasUrl = app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"licencias";
                   com.geopista.protocol.CConstantesComando.adminCartografiaUrl = app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"geopista/";
                   com.geopista.protocol.CConstantesComando.servidorUrl= app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL);


                   com.geopista.protocol.contaminantes.CConstantes.anexosActividadesContaminantesUrl=app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"anexos/contaminantes/";
                   com.geopista.app.contaminantes.init.Constantes.Locale = app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");
                   try{com.geopista.app.contaminantes.init.Constantes.idMunicipio=new Integer(app.getString("geopista.DefaultCityId")).intValue();
                   }catch (Exception e){
                              JOptionPane.showMessageDialog(this, "Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              System.out.println("Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              logger.error("Valor de id municipio no valido:"+e.toString()+app.getString("geopista.DefaultCityId"));
                              if (fromInicio)
                            	  dispose();
                              else
                            	  System.exit(-1);
                   }
                   app.setMainFrame(this);
                 try{
                     com.geopista.app.contaminantes.init.Constantes.totalMaxSizeFilesUploaded= new Long(app.getString("geopista.anexos.totalMaxSizeFilesUploaded")).longValue();
                 }catch(Exception ex){
                    System.out.println("[CMainContaminantes.configureApp]:geopista.anexos.totalMaxSizeFilesUploaded="+app.getString("geopista.anexos.totalMaxSizeFilesUploaded"));
                    System.out.println("[CMainContaminantes.configureApp]"+ex.toString());
                 }
                 /** Parametros de conexion de BD para los informes */
                 com.geopista.app.contaminantes.init.Constantes.DriverClassName = app.getString("conexion.driver");
                 com.geopista.app.contaminantes.init.Constantes.ConnectionInfo = app.getString("conexion.url");
                 com.geopista.app.contaminantes.init.Constantes.DBUser = app.getString("conexion.user");
                 //com.geopista.app.contaminantes.init.Constantes.DBPassword = app.getString("conexion.pass");
                 com.geopista.app.contaminantes.init.Constantes.DBPassword=app.getUserPreference("conexion.pass","",false);

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
			logger.debug("contaminantes.Constantes.url: " + com.geopista.app.contaminantes.init.Constantes.url);
			logger.debug("contaminantes.Constantes.timeout: " + com.geopista.app.contaminantes.init.Constantes.timeout);
        	logger.debug("contaminantes.Constantes.locale: " + com.geopista.app.contaminantes.init.Constantes.Locale);


			//****************************************************************
			//** Establecemos el idioma especificado en la configuracion
			//*******************************************************
			setLang(com.geopista.app.contaminantes.init.Constantes.Locale);
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
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {//GEN-BEGIN:initComponents
		CMain.setDefaultLookAndFeelDecorated(true);
		desktopPane = new javax.swing.JDesktopPane();
		menuBar = new javax.swing.JMenuBar();
		jMenuActividades = new javax.swing.JMenu();
		jMenuSalir = new javax.swing.JMenuItem();
		jMenuVertederos = new javax.swing.JMenu();
        jMenuHistorico = new javax.swing.JMenu();
        jMenuPlanos = new javax.swing.JMenu();
		jMenuItemVerVertederos = new javax.swing.JMenuItem();
        jMenuItemVerHistorico= new javax.swing.JMenuItem();
        jMenuItemVerPlanos = new javax.swing.JMenuItem();
		jMenuItemVertederoInformes = new javax.swing.JMenuItem();
		jMenuArbolado = new javax.swing.JMenu();
		jMenuItemVerArbolado = new javax.swing.JMenuItem();
		jMenuItemArboladoInformes = new javax.swing.JMenuItem();
		jMenuItemInspectores = new javax.swing.JMenuItem();
		jMenuCrearActividad = new javax.swing.JMenuItem();
		jMenuBuscarActividad = new javax.swing.JMenuItem();
        jMenuItemActividadesInformes = new javax.swing.JMenuItem();
		jMenuMostrarMapa = new javax.swing.JMenuItem();

		idiomaMenu = new javax.swing.JMenu();
		castellanoJMenuItem = new javax.swing.JMenuItem();
		catalanJMenuItem = new javax.swing.JMenuItem();
		euskeraJMenuItem = new javax.swing.JMenuItem();
		gallegoJMenuItem = new javax.swing.JMenuItem();
		valencianoJMenuItem = new javax.swing.JMenuItem();
		helpJMenu = new javax.swing.JMenu();
        jMenuItemHelp = new javax.swing.JMenuItem();
		aboutJMenuItem = new javax.swing.JMenuItem();
		pack();
		setBounds(0, 0, 800, 600);


		setBackground(new java.awt.Color(0, 78, 152));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm();
			}
		});

		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(null);
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

		jMenuCrearActividad.setMnemonic('C');
		jMenuCrearActividad.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				verActividad(null);
			}
		});
		jMenuActividades.add(jMenuCrearActividad);

		jMenuBuscarActividad.setMnemonic('B');
		jMenuBuscarActividad.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buscarActividad();
			}
		});
		jMenuActividades.add(jMenuBuscarActividad);


		jMenuMostrarMapa.setMnemonic('M');
		jMenuMostrarMapa.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//mostrarMapa();
			}
		});
		//jMenuActividades.add(jMenuMostrarMapa);

		jMenuItemInspectores.setMnemonic('I');
		jMenuItemInspectores.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mostrarInspectores();
			}
		});
		jMenuActividades.add(jMenuItemInspectores);

        jMenuItemActividadesInformes.setMnemonic('F');
        jMenuItemActividadesInformes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInformesActividades();
            }
        });
        jMenuActividades.add(jMenuItemActividadesInformes);

		jMenuSalir.setMnemonic('S');
		jMenuSalir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitForm();
			}
		});


		jMenuActividades.add(jMenuSalir);

		menuBar.add(jMenuActividades);

		jMenuVertederos.setMnemonic('D');
		jMenuItemVerVertederos.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showVertederos();
			}
		});
        jMenuItemVerHistorico.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showHistorico();
			}
		});

        jMenuItemVerPlanos.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        showPlanos();
                    }
                });

        jMenuHistorico.add(jMenuItemVerHistorico);
        jMenuPlanos.add(jMenuItemVerPlanos);
        jMenuVertederos.add(jMenuItemVerVertederos);

		jMenuVertederos.add(jMenuItemVertederoInformes);
		jMenuItemVertederoInformes.setMnemonic('F');
		jMenuItemVertederoInformes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showInformesVertedero();
			}
		});
		jMenuVertederos.add(jMenuItemVertederoInformes);

		menuBar.add(jMenuVertederos);

		jMenuArbolado.add(jMenuItemVerArbolado);
		jMenuArbolado.setMnemonic('R');
		jMenuItemVerArbolado.setMnemonic('V');
		jMenuItemVerArbolado.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showArbolado();
			}
		});

		jMenuArbolado.add(jMenuItemArboladoInformes);
		jMenuItemArboladoInformes.setMnemonic('F');
		jMenuItemArboladoInformes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showInformes();
			}
		});

		menuBar.add(jMenuArbolado);
        menuBar.add(jMenuPlanos);
        menuBar.add(jMenuHistorico);
		idiomaMenu.setMargin(null);
		castellanoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setLang(com.geopista.app.contaminantes.init.Constantes.LOCALE_CASTELLANO);
			}
		});

		idiomaMenu.add(castellanoJMenuItem);
		catalanJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setLang(com.geopista.app.contaminantes.init.Constantes.LOCALE_CATALAN);
			}
		});
		idiomaMenu.add(catalanJMenuItem);
		euskeraJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setLang(com.geopista.app.contaminantes.init.Constantes.LOCALE_EUSKEDA);
			}
		});

		idiomaMenu.add(euskeraJMenuItem);

		gallegoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setLang(com.geopista.app.contaminantes.init.Constantes.LOCALE_GALLEGO);
			}
		});

		idiomaMenu.add(gallegoJMenuItem);

		valencianoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setLang(com.geopista.app.contaminantes.init.Constantes.LOCALE_VALENCIANO);
			}
		});

		idiomaMenu.add(valencianoJMenuItem);

		menuBar.add(idiomaMenu);

		helpJMenu.setMargin(null);
        helpJMenu.add(jMenuItemHelp);
        jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
                                   public void actionPerformed(java.awt.event.ActionEvent evt) {
                                       mostrarAyuda();
                                   }
                               });
		helpJMenu.add(aboutJMenuItem);
		
		aboutJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutJMenuItemActionPerformed();
			}
		});

		menuBar.add(helpJMenu);

		setJMenuBar(menuBar);

		pack();
        ClassLoader cl = this.getClass().getClassLoader();
        java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(cl.getResource("img/geopista.gif"));
        setIconImage(img);

	}//GEN-END:initComponents


	private void showArbolado() {
        progressDialog.setTitle("Abrir Mapa");
        GUIUtil.centreOnWindow(progressDialog);
	   	if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		Container c = this.getContentPane();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Locale currentLocale = new Locale(com.geopista.app.contaminantes.init.Constantes.Locale);
		try
            {
			    messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
            }
        // ShowArbolado showArbolado = new ShowArbolado(progressDialog, jFrameArbolado, messages, this );
        //progressDialog.show();
        jFrameArbolado = new com.geopista.app.contaminantes.arbolado.JFrameArbolado(messages, this);
       
        //jFrameArbolado=showArbolado.getFrameArbolado();

        mostrarJInternalFrame(jFrameArbolado);
        zoomMapa();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		c.setCursor(Cursor.getDefaultCursor());


	}

	private void showVertederos() {
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		Container c = this.getContentPane();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Locale currentLocale = new Locale(com.geopista.app.contaminantes.init.Constantes.Locale);
		try
        {
            messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
        }catch (Exception e)
        {
            messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
        }
        jFrameVertedero = new com.geopista.app.contaminantes.vertederos.JFrameVertedero(messages, this);
		mostrarJInternalFrame(jFrameVertedero);
		zoomMapa();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		c.setCursor(Cursor.getDefaultCursor());
	}

    private void showHistorico() {
            if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
            Container c = this.getContentPane();
            c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Locale currentLocale = new Locale(com.geopista.app.contaminantes.init.Constantes.Locale);
            try
            {
                messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
            }
            jFrameHistorico = new com.geopista.app.contaminantes.JFrameHistoricoContaminantes(messages, this);
            mostrarJInternalFrame(jFrameHistorico);
            c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            c.setCursor(Cursor.getDefaultCursor());
        }
    private void showPlanos() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
        Container c = this.getContentPane();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Locale currentLocale = new Locale(com.geopista.app.contaminantes.init.Constantes.Locale);
        try
        {
            messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
        }catch (Exception e)
        {
            messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
        }
        jFramePlanos = new com.geopista.app.contaminantes.planos.JFrameGeneracionPlanos(messages, this);
        mostrarJInternalFrame(jFramePlanos);
        zoomMapa();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        c.setCursor(Cursor.getDefaultCursor());
    }

    private void showInformesActividades() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
        Container c = this.getContentPane();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Locale currentLocale = new Locale(com.geopista.app.contaminantes.init.Constantes.Locale);
        try
        {
            messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
        }catch (Exception e)
        {
            messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
        }
        jFrameInformesActividades= new com.geopista.app.contaminantes.actividades.JFrameInformesActividades(messages, this, null);
        mostrarJInternalFrame(jFrameInformesActividades);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        c.setCursor(Cursor.getDefaultCursor());
    }


	private void showInformesVertedero() {
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		Container c = this.getContentPane();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Locale currentLocale = new Locale(com.geopista.app.contaminantes.init.Constantes.Locale);
		try
            {
			    messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
            }jFrameInformesVertedero = new com.geopista.app.contaminantes.vertederos.JFrameInformesVertedero(messages, this);
		mostrarJInternalFrame(jFrameInformesVertedero);
		c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		c.setCursor(Cursor.getDefaultCursor());
	}

	private void showInformes() {
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0) return;
		Container c = this.getContentPane();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Locale currentLocale = new Locale(com.geopista.app.contaminantes.init.Constantes.Locale);
		try
            {
			    messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
            }jFrameInformesArbolado = new com.geopista.app.contaminantes.arbolado.JFrameInformes(messages, this);
		mostrarJInternalFrame(jFrameInformesArbolado);
		c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		c.setCursor(Cursor.getDefaultCursor());
	}


	public boolean verActividad(String sIdActividad) {
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0)
        {
            if (jFrameContaminantes!=null&&jFrameContaminantes.isVisible())
            {
               jFrameContaminantes.initData(sIdActividad);
               return true;
            }
            if (jFramePlanos!=null&&jFramePlanos.isVisible())
            {
               jFramePlanos.initDataActividad(sIdActividad);
               return true;
            }
            return false;
        }
		Container c = this.getContentPane();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Locale currentLocale = new Locale(com.geopista.app.contaminantes.init.Constantes.Locale);
		try
            {
			    messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
            }
        jFrameContaminantes = new JFrameContaminantes(messages, this, sIdActividad);
		//jFrameContaminantes.editable(acl.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION)));
		mostrarJInternalFrame(jFrameContaminantes);
		zoomMapa();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		c.setCursor(Cursor.getDefaultCursor());
        jFrameContaminantes.zoom();
        return true;
	}//GEN-LAST:event_jMenuGestionUsuariosActionPerformed


	private void mostrarInspectores() {
		// Aqui añado lo de buscar contactos
		JDialogInspectores dialogInspectores = new JDialogInspectores(this, true, messages);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		dialogInspectores.setSize(750, 475);
		dialogInspectores.setLocation(d.width / 2 - dialogInspectores.getSize().width / 2, d.height / 2 - dialogInspectores.getSize().height / 2);
		dialogInspectores.setResizable(false);

		dialogInspectores.show();
		/*if (dialogInspectores.isSeleccionado()&&dialogInspectores.getContactoSelected()!=null)
		 {
			 contacto=dialogContactos.getContactoSelected();
			 jTextFieldResponsable.setText(contacto.getIndividualName()+
								(contacto.getOrganisationName()!=null&&contacto.getOrganisationName().length()>0?" ["+contacto.getOrganisationName()+"] ":"")+
								(contacto.getPositionName()!=null&&contacto.getPositionName().length()>0?" ["+contacto.getPositionName()+"] ":""));
		 }*/

		dialogInspectores = null;
	}

	private void buscarActividad() {
		// Aqui añado lo de buscar actividades contaminantes
		JDialogBuscarContaminantes dialogBuscar = new JDialogBuscarContaminantes(this, true, messages,this);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		dialogBuscar.setSize(525, 500);
		dialogBuscar.setLocation(d.width / 2 - dialogBuscar.getSize().width / 2, d.height / 2 - dialogBuscar.getSize().height / 2);
		dialogBuscar.setResizable(false);
		dialogBuscar.show();
		dialogBuscar = null;
	}


	public boolean mostrarJInternalFrame(JInternalFrame internalFrame) {

		try {

			int numInternalFrames = desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(internalFrame)).length;
			//logger.info("numInternalFrames: " + numInternalFrames);

			if (numInternalFrames == 0) {
				internalFrame.setFrameIcon(new javax.swing.ImageIcon("img" + File.separator + "geopista.gif"));


				desktopPane.add(internalFrame);
				internalFrame.setMaximum(true);
				internalFrame.show();
			} else {
				logger.info("cannot open another JInternalFrame");
			}


		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}

		return true;
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

	private boolean setLang(String locale) {

		try {
			logger.debug("Cambiando el idioma: " + locale);

            //fichConfig.setValue("IDIOMA", "COUNTRY", country);
			//fichConfig.setValue("IDIOMA", "LANGUAGE", language);
			//fichConfig.saveFile();
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
			    messages = ResourceBundle.getBundle("config.contaminantes", currentLocale);
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.contaminantes", new Locale(Constantes.LOCALE_CASTELLANO));
            }
			changeScreenLang(messages);
			if (jFrameContaminantes != null) jFrameContaminantes.changeScreenLang(messages);
			if (jFrameArbolado != null) jFrameArbolado.changeScreenLang(messages);
			if (jFrameVertedero != null) jFrameVertedero.changeScreenLang(messages);
			if (jFrameInformesArbolado != null) jFrameInformesArbolado.changeScreenLang(messages);
            if (jFrameInformesActividades != null) jFrameInformesActividades.changeScreenLang(messages);
            if (jFrameInformesVertedero != null) jFrameInformesVertedero.changeScreenLang(messages);
            if (jFrameHistorico != null) jFrameHistorico.changeScreenLang(messages);
            if (jFramePlanos != null) jFramePlanos.changeScreenLang(messages);
			com.geopista.app.contaminantes.init.Constantes.Locale = locale;
			return true;

		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString());
			return false;
		}

	}

	private boolean changeScreenLang(ResourceBundle messages) {
        try
        {
    		String title=messages.getString("CMainContaminantes.title");
    		String release=aplicacion.getString("localgis.release");
    		if (release==null)
    			release="LocalGIS";
    		title=title.replaceAll("\\$\\{localgis\\.release\\}",release);				
            setTitle(title);

        	
            //setTitle(messages.getString("CMainContaminantes.title"));
            if (Constantes.Municipio!=null){
            	 com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
            	 if(principal!=null)
            		setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+") -"+ messages.getString("CAuthDialog.jLabelNombre")+" "+principal);
            else
               setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+")");
            }

            jMenuActividades.setText(messages.getString("CMainContaminantes.jMenuActividades"));
            jMenuCrearActividad.setText(messages.getString("CMainContaminantes.jMenuCrearActividad"));
            jMenuBuscarActividad.setText(messages.getString("CMainContaminantes.jMenuBuscarActividades"));
            jMenuMostrarMapa.setText(messages.getString("CMainContaminantes.jMenuMostrarMapa"));
            jMenuItemInspectores.setText(messages.getString("CMainContaminantes.jMenuItemInspectores"));
            jMenuItemActividadesInformes.setText(messages.getString("CMainContaminantes.jMenuItemActividadesInformes"));
            jMenuVertederos.setText(messages.getString("CMainContaminantes.jMenuVertederos"));
            jMenuItemVerVertederos.setText(messages.getString("CMainContaminantes.jMenuItemVerVertederos"));
            jMenuItemVertederoInformes.setText(messages.getString("CMainContaminantes.jMenuItemVertederoInformes"));
            jMenuArbolado.setText(messages.getString("CMainContaminantes.jMenuArbolado"));
            jMenuItemVerArbolado.setText(messages.getString("CMainContaminantes.jMenuItemVerArbolado"));
            jMenuItemArboladoInformes.setText(messages.getString("CMainContaminantes.jMenuItemArboladoInformes"));
            jMenuSalir.setText(messages.getString("CMainContaminantes.jMenuSalir"));
            idiomaMenu.setText(messages.getString("CMainContaminantes.idiomaMenu"));
            castellanoJMenuItem.setText(messages.getString("CMainContaminantes.castellanoJMenuItem"));
            catalanJMenuItem.setText(messages.getString("CMainContaminantes.catalanJMenuItem"));
            euskeraJMenuItem.setText(messages.getString("CMainContaminantes.euskeraJMenuItem"));
            gallegoJMenuItem.setText(messages.getString("CMainContaminantes.gallegoJMenuItem"));
            valencianoJMenuItem.setText(messages.getString("CMainContaminantes.valencianoJMenuItem"));
            helpJMenu.setText(messages.getString("CMainContaminantes.helpJMenu"));
            jMenuItemHelp.setText(messages.getString("CMainContaminantes.jMenuItemHelp"));
            aboutJMenuItem.setText(messages.getString("CMainContaminantes.aboutJMenuItem"));
            jMenuHistorico.setText(messages.getString("CMainContaminantes.jMenuHistorico"));
            jMenuItemVerHistorico.setText(messages.getString("CMainContaminantes.jMenuItemVer"));
            jMenuPlanos.setText(messages.getString("CMainContaminantes.jMenuPlanos"));
            jMenuItemVerPlanos.setText(messages.getString("CMainContaminantes.jMenuItemVerPlanos"));
            return true;
        }catch(Exception e)
        {
            logger.error("Error al cargar las etiquetas:",e);
            return false;
        }
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
	 private void  stopApp(){
		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Inicio de aplicación cancelado. Se reiniciará el aplicativo");
		 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		 System.exit(1);
	 }


	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

		new CMainContaminantes();
	}
     public boolean mostrarAyuda()
    {
        HelpSet hs = null;
        ClassLoader loader = this.getClass().getClassLoader();
        try
        {
                String helpSetFile = "help/contaminantes/GeopistaHelpContaminantes_" + com.geopista.app.contaminantes.init.Constantes.Locale + ".hs";
        		URL url = HelpSet.findHelpSet(loader, helpSetFile);
        		if (url == null)//tomamos el idioma castellano por defecto
                {
                    logger.error("3. Imposible cargar el fichero de ayuda: "+helpSetFile);
                    helpSetFile = "help/contaminantes/GeopistaHelpContaminantes_" + com.geopista.app.contaminantes.init.Constantes.LOCALE_CASTELLANO + ".hs";
                    url = HelpSet.findHelpSet(loader, helpSetFile);
                }
                if (url== null)
                {
                    logger.error("4. Imposible cargar el fichero de ayuda: "+helpSetFile);
                    return false;
                }
                hs = new HelpSet(loader, url);
                hs.setHomeID(com.geopista.app.contaminantes.init.Constantes.helpSetHomeID);
         } catch (Exception ex) {
                logger.error("Exception: " + ex.toString());
                return false;
         }
        HelpBroker hb = hs.createHelpBroker();
        hb.setDisplayed(true);
        new CSH.DisplayHelpFromSource(hb);
        return true;
    }
     
     
     private void aboutJMenuItemActionPerformed() {//GEN-FIRST:event_acercaJMenuItemActionPerformed
    	 setLang(com.geopista.app.contaminantes.init.Constantes.Locale);

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
     
     
	// Variables declaration - do not modify
	private javax.swing.JMenuItem aboutJMenuItem;
	private javax.swing.JMenuItem castellanoJMenuItem;
	private javax.swing.JMenuItem catalanJMenuItem;
	private javax.swing.JDesktopPane desktopPane;
	private javax.swing.JMenuItem euskeraJMenuItem;
	private javax.swing.JMenuItem gallegoJMenuItem;
	private javax.swing.JMenuItem valencianoJMenuItem;
	private javax.swing.JMenu jMenuActividades;
	private javax.swing.JMenuItem jMenuCrearActividad;
	private javax.swing.JMenuItem jMenuBuscarActividad;
	private javax.swing.JMenuItem jMenuMostrarMapa;
	private javax.swing.JMenuItem jMenuItemInspectores;
    private javax.swing.JMenuItem jMenuItemActividadesInformes;
	private javax.swing.JMenu jMenuVertederos;
    private javax.swing.JMenu jMenuHistorico;
    private javax.swing.JMenuItem jMenuItemVerHistorico;
    private javax.swing.JMenu jMenuPlanos;
    private javax.swing.JMenuItem jMenuItemVerPlanos;
	private javax.swing.JMenuItem jMenuItemVerVertederos;
	private javax.swing.JMenuItem jMenuItemVertederoInformes;
	private javax.swing.JMenu jMenuArbolado;
	private javax.swing.JMenu helpJMenu;
	private javax.swing.JMenu idiomaMenu;
	private javax.swing.JMenuItem jMenuItemVerArbolado;
	private javax.swing.JMenuItem jMenuItemArboladoInformes;
	private javax.swing.JMenuItem jMenuSalir;
	private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem jMenuItemHelp;

}


class ShowArbolado extends Thread
{
    TaskMonitorDialog progressDialog;
    JFrameArbolado jFrameArbolado;
    ResourceBundle messages;
    CMain principal;
    public ShowArbolado(TaskMonitorDialog progressDialog, JFrameArbolado jFrameArbolado,ResourceBundle messages,
                        CMain principal)
    {
       this.progressDialog=progressDialog;
       this.jFrameArbolado = jFrameArbolado;
       this.messages=messages;
       this.principal= principal;
       start();
    }
    public void run()
    {
        jFrameArbolado = new com.geopista.app.contaminantes.arbolado.JFrameArbolado(messages, principal);
        progressDialog.setVisible(false);
        progressDialog.dispose();
    }
    public JFrameArbolado getFrameArbolado()
    {
        return jFrameArbolado;
    }
}

