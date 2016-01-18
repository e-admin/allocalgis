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
package com.geopista.app.administrador;
import java.awt.Container;
import java.awt.Cursor;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.dominios.CDominiosFrame;
import com.geopista.app.administrador.entidades.CEntidadesFrame;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.administrador.sesiones.CSesionesFrame;
import com.geopista.app.administrador.sesiones.SessionManagerFrame;
import com.geopista.app.administrador.usuarios.CUsuariosFrame;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.control.ISesion;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.SplashWindow;


/*
 * GeoPistaMDI.java
 *
 * Created on 13 de febrero de 2004, 11:38
 */

/**
 * @author avivar
 */
public class CMainAdministrador extends CMain{
	
	static Logger logger;
	    static {
	       createDir();
	       logger  = Logger.getLogger(CMainAdministrador.class);
	   
	    } 
		
	private static AppContext app = (AppContext) AppContext.getApplicationContext();
    //Logger logger = Logger.getLogger(CMainAdministrador.class);
	public static final String idApp="Administracion";
    CUsuariosFrame usuariosFrame;
    CDominiosFrame dominiosFrame;
    private CEntidadesFrame entidadesFrame;
    //jalopez CSesionesFrame sesionesFrame;
    ResourceBundle messages;
    GeopistaAcl aclAdministracion;
    private boolean fromInicio;
    
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }    
    public CMainAdministrador() {
    	this(false);
    }
  	public CMainAdministrador(boolean fromInicio) {	
  		this.fromInicio=fromInicio;
         AppContext.getApplicationContext().setMainFrame(this);
         try {initLookAndFeel(); }catch (Exception e){}
        try
        {
            //System.out.println(System.getProperty("user.dir"));
            SplashWindow splashWindow=showSplash();
    	    initComponents();
		    setSize(900,770);
		    show();
	    	configureApp();
            if (splashWindow!=null)splashWindow.setVisible(false);
            //******************
            //Mostramos la pantalla de autenticación del usuario.
            //******************
			//Antes de autenticar al usuario hacemos un logout
			if (SecurityManager.getIdSesion()!=null)
				SecurityManager.logout();
			
			//--------NUEVO-------------->	
//			if(SSOAuthManager.isSSOActive())
//				Constantes.url = AppContext.getApplicationContext().getString(SSOAuthManager.SSO_SERVER_URL);
			SecurityManager.setUrl(Constantes.url);
			SSOAuthManager.ssoAuthManager(idApp);								
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
            
            /*if (!AppContext.seleccionarMunicipio((Frame)this)){
          
            	return;
            }*/
            
	            Constantes.idEntidad = Integer.parseInt(((ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY)).getIdEntidad());
	            
	            //Obtenemos los acls que tiene el usuario logueado relacionado con la aplicación de Administración de usuarios
	            aclAdministracion = com.geopista.security.SecurityManager.getPerfil(idApp);
	            //com.geopista.security.SecurityManager.setHeartBeatTime(1000000);
	            com.geopista.security.SecurityManager.setHeartBeatTime(10000);
	            //En esta línea se comprueba si este usuario que está iniciando sesión tiene el permiso de entrar en esta aplicación(creo):
	            //si los tiene le pone todo los menús con enabled a true, en caso contrario lo pone a false.
	            editable(aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_ADMINITRACION)));
	/*            Municipio municipio = (new OperacionesAdministrador(Constantes.url)).getMunicipio(new Integer(Constantes.idMunicipio).toString());
	            if (municipio!=null)
	            {
	                Constantes.Municipio = municipio.getNombre();
	                Constantes.Provincia= municipio.getProvincia();
	                //setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+")");
	                com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
	                if (principal != null){
	                    setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+")" + " - " + messages.getString("CAuthDialog.jLabelNombre") + " " + principal.getName());
	                }else{
	                    setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+")");
	                }
	            }
	*/            
	            Entidad entidad = (new OperacionesAdministrador(Constantes.url)).getEntidad(new Integer(Constantes.idEntidad).toString());
	            if (entidad!=null)
	            {
	                //setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+")");
	                Constantes.Entidad = entidad.getNombre();
	                com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
	                if (principal != null){
	                    setTitle(getTitle()+" - "+Constantes.Entidad + " - " + messages.getString("CAuthDialog.jLabelNombre") + " " + principal.getName());
	                }else{
	                    setTitle(getTitle()+" - "+Constantes.Entidad);
	                }
	            }
        }catch(Exception ex)
        {
            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
            System.out.println("ERROR:"+sw.toString());
			logger.error("Exception: " + sw.toString());
        }
	}


	private boolean showAuth() {
 		boolean resultado=false;
		CAuthDialog auth = new CAuthDialog(this, true,Constantes.url,
                            CMainAdministrador.idApp, Constantes.idEntidad,
                            messages);     
		auth.setBounds(30,60,315,155);
        if (fromInicio){
        	resultado=auth.showD(true);
        	if (!resultado)
        		return false;
        }
        else{
        	auth.show();
        }
		return true;
	}

	private boolean configureApp() {

		try {

			//****************************************************************
			//** Inicializamos el log4j
			//*******************************************************
			try{PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);}catch(Exception e){};

	           try{
                   Constantes.url = app.getString("geopista.conexion.servidorurl") + WebAppConstants.ADMINISTRACION_WEBAPP_NAME;                   
                   Constantes.Locale = app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");
                 /*  try{
                	   Constantes.idEntidad=new Integer(app.getString("geopista.DefaultEntityId")).intValue();
                   }catch (Exception e){
                              JOptionPane.showMessageDialog(this, "Valor de id entidad no valido:"+e.toString()+app.getString("geopista.DefaultEntityId"));
                              System.out.println("Valor de id entidad no valido:"+e.toString()+app.getString("geopista.DefaultEntityId"));
                              logger.error("Valor de id entidad no valido:"+e.toString()+app.getString("geopista.DefaultEntityId"),e);
                              if (fromInicio)
                            	  dispose();
                              else
                            	  System.exit(-1);
                   }*/


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

            //System.out.println("administrador.Constantes.url: " + Constantes.url);
            //System.out.println("administrador.Constantes.timeout: " + Constantes.timeout);

			logger.debug("administrador.Constantes.url: " + Constantes.url);
			logger.debug("administrador.Constantes.timeout: " + Constantes.timeout);
			//****************************************************************
			//** Establecemos el idioma especificado en la configuracion
			//*******************************************************
			setLang(Constantes.Locale);
            return true;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
            JOptionPane.showMessageDialog(this, "Excepcion al cargar el fichero de configuración:\n"+sw.toString());
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
        gestionUsuariosMenu = new javax.swing.JMenu();
        jMenuGestionUsuarios = new javax.swing.JMenuItem();
        jMenuGestionDepartamentos = new javax.swing.JMenuItem();
        jMenuGestionOrgainismos = new javax.swing.JMenuItem();
        jMenuSalir = new javax.swing.JMenuItem();
        gestionDominiosMenu = new javax.swing.JMenu();
        jMenuItemGestionDominios = new javax.swing.JMenuItem();
        jMenuSesiones = new javax.swing.JMenu();
        jMenuItemSesiones = new javax.swing.JMenuItem();
        jMenuItemSesionesActivas = new javax.swing.JMenuItem();
        
        jMenuGestionEntidades = new JMenu();
        jMenuItemGestionEntidades = new JMenuItem();
        
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
        setBounds(0,0,800,600);


        setBackground(new java.awt.Color(0, 78, 152));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        desktopPane.setMinimumSize(null);
        desktopPane.setPreferredSize(null);
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        gestionUsuariosMenu.setMnemonic('U');
        gestionUsuariosMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuGestionUsuariosActionPerformed();
            }
        });

        jMenuGestionUsuarios.setMnemonic('u');
        jMenuGestionUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuGestionUsuariosActionPerformed();
            }
        });

        gestionUsuariosMenu.add(jMenuGestionUsuarios);

        jMenuGestionDepartamentos.setMnemonic('d');
        jMenuGestionDepartamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //jMenuGestionDepartamentosActionPerformed(evt);
            }
        });

       // gestionUsuariosMenu.add(jMenuGestionDepartamentos);

        jMenuGestionOrgainismos.setMnemonic('o');
        jMenuGestionOrgainismos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //jMenuGestionOrgainismosActionPerformed(evt);
            }
        });
        jMenuSalir.setMnemonic('i');
        jMenuSalir.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                          exitForm(null);
                          }
         });


        //gestionUsuariosMenu.add(jMenuGestionOrgainismos);
        gestionUsuariosMenu.add(jMenuSalir);

        menuBar.add(gestionUsuariosMenu);

        gestionDominiosMenu.setMnemonic('D');
         gestionDominiosMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGestionDominiosActionPerformed();
            }
        });

        jMenuSesiones.setMnemonic('S');
        jMenuItemSesiones.setMnemonic('s');
       jMenuItemSesiones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               jMenuItemGestionSesionesActionPerformed();
            }
        });
       jMenuItemSesionesActivas.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              jMenuItemGestionSesionesActivasActionPerformed();
           }
       });
       
        jMenuItemGestionDominios.setMnemonic('d');
       jMenuItemGestionDominios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGestionDominiosActionPerformed();
            }
        });

        gestionDominiosMenu.add(jMenuItemGestionDominios);
        jMenuSesiones.add(jMenuItemSesiones);
        jMenuSesiones.add(jMenuItemSesionesActivas);
        
        menuBar.add(gestionDominiosMenu);
        menuBar.add(jMenuSesiones);

        
        jMenuGestionEntidades.setMnemonic('E');
        jMenuItemGestionEntidades.setMnemonic('e');
        jMenuItemGestionEntidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
              jMenuItemGestionEntidadesActionPerformed();
            }
        });
        jMenuGestionEntidades.add(jMenuItemGestionEntidades);
        menuBar.add(jMenuGestionEntidades);        
           
        idiomaMenu.setMargin(null);
        castellanoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setLang(Constantes.LOCALE_CASTELLANO);
            }
        });

       idiomaMenu.add(castellanoJMenuItem);
       catalanJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setLang(Constantes.LOCALE_CATALAN);
            }
        });
       idiomaMenu.add(catalanJMenuItem);
        euskeraJMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        setLang(Constantes.LOCALE_EUSKEDA);
                    }
                });

        idiomaMenu.add(euskeraJMenuItem);

        gallegoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        setLang(Constantes.LOCALE_GALLEGO);
                    }
                });

        idiomaMenu.add(gallegoJMenuItem);

        valencianoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                setLang(Constantes.LOCALE_VALENCIANO);
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
        menuBar.add(helpJMenu);
        setJMenuBar(menuBar);
        pack();
        //java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage("img"+File.separator+"geopista.gif");

//        try
//        {
//            ClassLoader cl = this.getClass().getClassLoader();
//            java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(cl.getResource("img/geopista.gif"));
//            setIconImage(img);
//            //System.out.println("Icono encontrado");
//        }catch(Exception e)
//        {
//            System.out.println("Icono no encontrado");
//        }
    }//GEN-END:initComponents

    private void jMenuItemGestionDominiosActionPerformed() {//GEN-FIRST:event_jMenuItemGestionDominiosActionPerformed
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        Container c = this.getContentPane();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Locale currentLocale = new Locale(Constantes.Locale);
        try
        {
            messages = ResourceBundle.getBundle("config.administrador", currentLocale);
        }catch (Exception e)
        {
            messages = ResourceBundle.getBundle("config.administrador", new Locale(Constantes.LOCALE_CASTELLANO));
        }
        dominiosFrame=new CDominiosFrame(messages,this);
        dominiosFrame.editable(aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION)));
        mostrarJInternalFrame(dominiosFrame);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        c.setCursor(Cursor.getDefaultCursor());

    }//GEN-LAST:event_jMenuItemGestionDominiosActionPerformed

	private void jMenuItemGestionSesionesActionPerformed() {
		if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0)
			return;
		
		Container c = this.getContentPane();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Locale currentLocale = new Locale(Constantes.Locale);
		
		try {
			messages = ResourceBundle.getBundle("config.administrador", currentLocale);
		} catch (Exception e) {
			messages = ResourceBundle.getBundle("config.administrador", new Locale(Constantes.LOCALE_CASTELLANO));
		}
		
		SessionManagerFrame sesionesFrame = new SessionManagerFrame(messages,aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.ADMINISTRACION_VER_SESIONES)));
		mostrarJInternalFrame(sesionesFrame);
		c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		c.setCursor(Cursor.getDefaultCursor());
	}

    private void jMenuItemGestionSesionesActivasActionPerformed()
    {
         if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
         Container c = this.getContentPane();
         c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         Locale currentLocale = new Locale(Constantes.Locale);
         try
        {
            messages = ResourceBundle.getBundle("config.administrador", currentLocale);
        }catch (Exception e)
        {
            messages = ResourceBundle.getBundle("config.administrador", new Locale(Constantes.LOCALE_CASTELLANO));
        }
         //jalopez sesionesFrame=new com.geopista.app.administrador.sesiones.CSesionesFrame(messages);
         CSesionesFrame sesionesFrame=new com.geopista.app.administrador.sesiones.CSesionesFrame(messages,aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.ADMINISTRACION_VER_SESIONES)));
         mostrarJInternalFrame(sesionesFrame);
         c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
         c.setCursor(Cursor.getDefaultCursor());
    }

    private void jMenuGestionUsuariosActionPerformed() {//GEN-FIRST:event_jMenuGestionUsuariosActionPerformed
       if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
       Container c = this.getContentPane();
       c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       Locale currentLocale = new Locale(Constantes.Locale);
       try
       {
           messages = ResourceBundle.getBundle("config.administrador", currentLocale);
       }catch (Exception e)
       {
           messages = ResourceBundle.getBundle("config.administrador", new Locale(Constantes.LOCALE_CASTELLANO));
       }
       usuariosFrame=new CUsuariosFrame(messages,this);
       usuariosFrame.editable(aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION)));
       
       mostrarJInternalFrame(usuariosFrame);
       c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
       c.setCursor(Cursor.getDefaultCursor());


    }//GEN-LAST:event_jMenuGestionUsuariosActionPerformed

    private void jMenuItemGestionEntidadesActionPerformed() {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0)
            return;
        Container c = this.getContentPane();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Locale currentLocale = new Locale(Constantes.Locale);
        try {
            messages = ResourceBundle.getBundle("config.administrador", currentLocale);
        } catch (Exception e) {
            messages = ResourceBundle.getBundle("config.administrador", new Locale(
                    Constantes.LOCALE_CASTELLANO));
        }
        entidadesFrame = new CEntidadesFrame(messages, this);
        entidadesFrame.editable(aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION)));
        
        mostrarJInternalFrame(entidadesFrame);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        c.setCursor(Cursor.getDefaultCursor());
    }
    
	private boolean mostrarJInternalFrame(JInternalFrame internalFrame) {

		try {

			int numInternalFrames=desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(internalFrame)).length;
			//logger.info("numInternalFrames: "+numInternalFrames);

			if (numInternalFrames==0){
				internalFrame.setFrameIcon(new javax.swing.ImageIcon("img"+File.separator+"geopista.gif"));


				desktopPane.add(internalFrame);
				internalFrame.setMaximum(true);
				internalFrame.show();
			}else{
				logger.info("cannot open another JInternalFrame");
			}



		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}

		return true;
	}
    public boolean mostrarAyuda()
    {
        HelpSet hs = null;
        ClassLoader loader = this.getClass().getClassLoader();
        try
        {
                String helpSetFile = "help/administracion/GeopistaHelpAdministracion_" + com.geopista.app.administrador.init.Constantes.Locale + ".hs";
                URL url = HelpSet.findHelpSet(loader, helpSetFile);
                if (url == null)//tomamos el idioma castellano por defecto
                {
                    logger.error("Imposible cargar el fichero de ayuda: "+helpSetFile);
                    helpSetFile = "help/administracion/GeopistaHelpAdministracion_" + com.geopista.app.administrador.init.Constantes.LOCALE_CASTELLANO + ".hs";
                    url = HelpSet.findHelpSet(loader, helpSetFile);
                }
                if (url== null)
                {
                    logger.error("Imposible cargar el fichero de ayuda: "+helpSetFile);
                    return false;
                }
                hs = new HelpSet(loader, url);
                hs.setHomeID(com.geopista.app.administrador.init.Constantes.helpSetHomeID);
         } catch (Exception ex) {
                logger.error("Exception: " + ex.toString());
                return false;
         }
        HelpBroker hb = hs.createHelpBroker();
        hb.setDisplayed(true);
        new CSH.DisplayHelpFromSource(hb);
        return true;
    }

    /**
	* Exit the Application
	*/
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        try
        {
           //-----NUEVO----->	
           if(!SSOAuthManager.isSSOActive()) 
        	   com.geopista.security.SecurityManager.logout();
           //---FIN NUEVO--->
        }catch(Exception e){};
        if (fromInicio)
      	  dispose();
        else
      	  System.exit(0);
	}

	private boolean setLang(String locale) {

		try {
            //System.out.println("Cambiamos el idioma a "+locale);
			logger.debug("Cambiamos idioma a: " + locale);
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
                messages = ResourceBundle.getBundle("config.administrador", currentLocale);
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.administrador", new Locale(Constantes.LOCALE_CASTELLANO));
            }
            changeScreenLang(messages);
            if (usuariosFrame!=null) usuariosFrame.changeScreenLang(messages);
            if (dominiosFrame!=null) dominiosFrame.changeScreenLang(messages);
            //jalopez if (sesionesFrame!=null) sesionesFrame.changeScreenLang(messages);
            if (entidadesFrame != null)
                entidadesFrame.changeScreenLang(messages);
            Constantes.Locale = locale;
			return true;

		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString());
			return false;
		}

	}

    private void editable(boolean b)
    {
   
        jMenuGestionDepartamentos.setEnabled(b);
        jMenuGestionOrgainismos.setEnabled(b);
        jMenuItemGestionDominios.setEnabled(b);
        jMenuItemSesiones.setEnabled(b);
        jMenuItemSesionesActivas.setEnabled(b);
        jMenuItemGestionEntidades.setEnabled(b);
    }
	private boolean changeScreenLang(ResourceBundle messages) {
		
		String title=messages.getString("CMainAdministrador.title");
		String release=app.getString("localgis.release");
		if (release==null)
			release="LocalGIS";
		title=title.replaceAll("\\$\\{localgis\\.release\\}",release);				
        setTitle(title);
        if (Constantes.Municipio!=null)
            setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+")");
        com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
        if (principal != null){
            setTitle(getTitle()+ " - " + messages.getString("CAuthDialog.jLabelNombre") + " " + principal.getName());
        }
        gestionUsuariosMenu.setText(messages.getString("CMainAdministrador.gestionUsuariosMenu"));
        jMenuGestionUsuarios.setText(messages.getString("CMainAdministrador.jMenuGestionUsuarios"));
        jMenuGestionDepartamentos.setText(messages.getString("CMainAdministrador.jMenuGestionDepartamentos"));
        jMenuGestionOrgainismos.setText(messages.getString("CMainAdministrador.jMenuGestionOrgainismos"));
        jMenuSalir.setText(messages.getString("CMainAdministrador.jMenuSalir"));
        gestionDominiosMenu.setText(messages.getString("CMainAdministrador.gestionDominiosMenu"));
        jMenuItemSesiones.setText(messages.getString("CMainAdministrador.jMenuItemSesiones"));
        jMenuItemSesionesActivas.setText(messages.getString("CMainAdministrador.jMenuItemSesionesActivas"));
        jMenuSesiones.setText(messages.getString("CMainAdministrador.jMenuSesiones"));
        jMenuItemGestionDominios.setText(messages.getString("CMainAdministrador.jMenuItemGestionDominios"));
        jMenuGestionEntidades.setText(messages.getString("CMainAdministrador.jMenuGestionEntidades"));
        jMenuItemGestionEntidades.setText(messages.getString("CMainAdministrador.jMenuItemGestionEntidades"));
        idiomaMenu.setText(messages.getString("CMainAdministrador.idiomaMenu"));
        castellanoJMenuItem.setText(messages.getString("CMainAdministrador.castellanoJMenuItem"));
        catalanJMenuItem.setText(messages.getString("CMainAdministrador.catalanJMenuItem"));
        euskeraJMenuItem.setText(messages.getString("CMainAdministrador.euskeraJMenuItem"));
        gallegoJMenuItem.setText(messages.getString("CMainAdministrador.gallegoJMenuItem"));
        valencianoJMenuItem.setText(messages.getString("CMainAdministrador.valencianoJMenuItem"));
        helpJMenu.setText(messages.getString("CMainAdministrador.helpJMenu"));
        aboutJMenuItem.setText(messages.getString("CMainAdministrador.aboutJMenuItem"));
        jMenuItemHelp.setText(messages.getString("CMainAdministrador.jMenuItemHelp"));
       	return true;

	}
    
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

      new CMainAdministrador();

	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutJMenuItem;
    private javax.swing.JMenuItem castellanoJMenuItem;
    private javax.swing.JMenuItem catalanJMenuItem;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem euskeraJMenuItem;
    private javax.swing.JMenuItem gallegoJMenuItem;
    private javax.swing.JMenu gestionDominiosMenu;
    private javax.swing.JMenu jMenuSesiones;
    private javax.swing.JMenuItem jMenuItemSesiones;
    private javax.swing.JMenuItem jMenuItemSesionesActivas;
    private javax.swing.JMenu gestionUsuariosMenu;
    private javax.swing.JMenu helpJMenu;
    private javax.swing.JMenu idiomaMenu;
    private javax.swing.JMenuItem jMenuGestionDepartamentos;
    private javax.swing.JMenuItem jMenuGestionOrgainismos;
    private javax.swing.JMenuItem jMenuSalir;
    private javax.swing.JMenuItem jMenuGestionUsuarios;
    private javax.swing.JMenuItem jMenuItemGestionDominios;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem valencianoJMenuItem;
    private javax.swing.JMenuItem jMenuItemHelp;

    private JMenu jMenuGestionEntidades;
    private JMenuItem jMenuItemGestionEntidades;
    
}

