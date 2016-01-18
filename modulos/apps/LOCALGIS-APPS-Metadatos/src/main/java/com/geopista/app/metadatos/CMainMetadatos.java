/**
 * CMainMetadatos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos;

import java.awt.BorderLayout;
import java.awt.Container;
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
import java.util.Locale;
import java.util.ResourceBundle;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.metadatos.contactos.CSearchContactos;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.IConnection;
import com.geopista.security.ISecurityPolicy;
import com.geopista.security.SecurityManager;
import com.geopista.security.connect.ConnectionStatus;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.util.ApplicationContext;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
* Created by IntelliJ IDEA.
* User: angeles
* Date: 26-ago-2004
* Time: 12:17:22
*/

public class CMainMetadatos extends CMain implements IConnection,ISecurityPolicy {
    //public static String ficheroConfiguracion = System.getProperty("user.dir", ".")+ java.io.File.separator +"config" + java.io.File.separator + "configMetadatos.ini";
	//Logger logger = Logger.getLogger(CMainMetadatos.class);
	 private static Logger logger;
	    static {
	       createDir();
	       logger  = Logger.getLogger(CMainMetadatos.class);
	    }	
	
    ResourceBundle messages;
    public static final String idApp="Metadatos";
    CMetadatos metadatosFrame;
    public static GeopistaAcl aclMetadatos;
	private boolean fromInicio;
	private ApplicationContext aplicacion = (AppContext)AppContext.getApplicationContext();
	private Blackboard blackboard = aplicacion.getBlackboard();
	
	 ConnectionStatus status;
	 
	
	public CMainMetadatos() {
		this(false);
	}   
    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }	
	public CMainMetadatos(boolean fromInicio) {
		aplicacion.setUrl(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.METADATOS_WEBAPP_NAME);

		this.fromInicio=fromInicio;
        AppContext.getApplicationContext().setMainFrame(this);
        try{initLookAndFeel();}catch (Exception e){}
        try
        {
            SplashWindow splashWindow=showSplash();
            initComponents();
            setSize(900,700);
            show();
            configureApp();
            if (splashWindow!=null)splashWindow.setVisible(false);
            com.geopista.security.SecurityManager.getTestConnection().setIconnection(this);
            //System.out.println("Mostrando pantalla de autenticación");
            //******************
            //Mostramos la pantalla de autenticación del usuario.
            //******************
			//Antes de autenticar al usuario hacemos un logout
			if (SecurityManager.getIdSesion()!=null)
				SecurityManager.logout();
			
			//--------NUEVO-------------->	
//			if(SSOAuthManager.isSSOActive()){
//				Constantes.url = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
//				com.geopista.app.metadatos.init.Constantes.url = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
//			}
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
            
            
            
            //Solo mostramos el panel de municipios cuando el usuario esta logueado.
            if (com.geopista.security.SecurityManager.isOnlyLogged()){
            	 if (!AppContext.seleccionarMunicipio((Frame)this)){
                 	stopApp();
                 	return;
                 }
            	
            	//AppContext.seleccionarMunicipio((Frame)this);
            	com.geopista.app.metadatos.init.Constantes.idMunicipio = AppContext.getIdMunicipio();
            	connect();
            }
            
            GeopistaPermission permiso = new GeopistaPermission("Geopista.Metadatos.Login");
            boolean tienePermiso = aplicacion.checkPermission(permiso,"Metadatos");
            
            if(!tienePermiso){
            	noPermApp();
            	return;
            }  
            
            /**Una vez autenticado el usuario, ya podemos mostrarlo en el título de la aplicación*/
            com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
            if(principal!=null){
            	//System.out.println("El usuario es nulo");
            	setTitle(messages.getString("CMainMetadatos.title")+" - "+
            		messages.getString("CAuthDialog.jLabelNombre")+principal);
            }
            
            if (com.geopista.security.SecurityManager.isLogged())
            {
                aclMetadatos= com.geopista.security.SecurityManager.getPerfil("Metadatos");
                enabled();
            }
            
            //Estructuras.cargarEstructuras();
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(this, null);                                    
            progressDialog.setTitle(aplicacion.getI18nString("inventario.app.tag1"));
            progressDialog.addComponentListener(new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                     new Thread(new Runnable(){
                              public void run(){
                                  try{
                                      progressDialog.report(aplicacion.getI18nString("inventario.app.tag1"));
                                                      
                                      while (!Estructuras.isCargada()) {

                                            if (!Estructuras.isIniciada()) 
                                            	Estructuras.cargarEstructuras();
                                            try {Thread.sleep(500);} 
                                            catch (Exception e) {}
                                      }                                     
                                      logger.info("Estructuras cargadas");
                                  }
                                  //catch (CancelException e1){
                                	  
                                  //}
                                  catch(Exception e){                                	  
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
   

         }catch(Exception ex)
        {
            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
            System.out.println("ERROR:"+sw.toString());
			logger.error("Exception: " + sw.toString());
        }
	}
	
	 private void  stopApp(){
	    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Inicio de aplicación cancelado. Se cerrará el aplicativo");
	    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    	 System.exit(1);
	    }
	 
	    private void noPermApp(){
	    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"No tiene permisos para entrar. Se cerrará el aplicativo");
	    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    	 System.exit(1);
	    }
 
	 
    private void conectar()
    {

        if (!com.geopista.security.SecurityManager.isLogged())
        {
        	//--------NUEVO-------------->	
//			if(SSOAuthManager.isSSOActive()){
//				Constantes.url = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
//				com.geopista.app.metadatos.init.Constantes.url = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
//			}
			SSOAuthManager.ssoAuthManager(idApp);								
            if (!AppContext.getApplicationContext().isOnlyLogged()){  
            //-------FIN-NUEVO----------->
            showAuth();
            //--------NUEVO-------------->	
            }
            //-------FIN-NUEVO----------->
            if (com.geopista.security.SecurityManager.isOnlyLogged())
            {
                try
                {
                    aclMetadatos= com.geopista.security.SecurityManager.getPerfil("Metadatos");
                    consultarMetadatoJMenuItem.setEnabled(aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_METADATOS)));
                    crearMetadatoJMenuItem.setEnabled(aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS)));
                    if (metadatosFrame!=null)
                        metadatosFrame.enabled(aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS)));
                    enabled();
                }catch(Exception e)
                {    logger.error(e.toString());}
            }
        }
        else
        {
           JOptionPane optionPane= new JOptionPane(messages.getString("CMainMetadatos.mensaje.usuarioconectado"),JOptionPane.INFORMATION_MESSAGE);
           JDialog dialog =optionPane.createDialog(this,"");
           dialog.show();
        }
        if (com.geopista.security.SecurityManager.isLogged()){
        	 /**Una vez autenticado el usuario, ya podemos mostrarlo en el título de la aplicación*/
        	 com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
            if(principal!=null){
            	System.out.println("El usuario es nulo");
            setTitle(messages.getString("CMainMetadatos.title")+" - "+
            		messages.getString("CAuthDialog.jLabelNombre")+principal);
            }
            openSession();
        }
        else{
            closeSession();
            setTitle(messages.getString("CMainMetadatos.title"));
            }
        

    }
    private void desconectar()
    {
        try
        {
            if (com.geopista.security.SecurityManager.isLogged())
            {
            	//-----NUEVO----->	
                if(!SSOAuthManager.isSSOActive()) 
             	   com.geopista.security.SecurityManager.logout();
                //---FIN NUEVO--->
                JOptionPane optionPane= new JOptionPane(messages.getString("CMainMetadatos.mensaje.desconexioncorrecta"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                if (metadatosFrame!=null) metadatosFrame.enabled(true);
                enabled();
            }
            else
            {
               JOptionPane optionPane= new JOptionPane(messages.getString("CMainMetadatos.mensaje.usuariodesconectado"),JOptionPane.INFORMATION_MESSAGE);
               JDialog dialog =optionPane.createDialog(this,"");
               dialog.show();
            }
            if (com.geopista.security.SecurityManager.isLogged()){
            	 com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
            	 setTitle(messages.getString("CMainMetadatos.title")+" - "+
                 		messages.getString("CAuthDialog.jLabelNombre")+principal);
                openSession();
            }
            else{
            	setTitle(messages.getString("CMainMetadatos.title"));
                closeSession();
            }

        }catch(Exception e)
        {
             JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.ERROR_MESSAGE);
             JDialog dialog =optionPane.createDialog(this,"");
             dialog.show();
        }
    }
    public void enabled()
    {
        if (com.geopista.security.SecurityManager.isLogged() && aclMetadatos!=null)
        {
            //System.out.println("VIEW: "+com.geopista.security.GeopistaPermission.VER_METADATOS);
            //System.out.println("EDIT: "+com.geopista.security.GeopistaPermission.EDITAR_METADATOS);
            if (aclMetadatos.getPermissions()!=null)
            {
                for (Enumeration e=aclMetadatos.getPermissions();e.hasMoreElements();)
                {
                    GeopistaPermission per=(GeopistaPermission)e.nextElement();
                    //System.out.println("PERMISO: "+per.getName());
                }
            }
            else
            {
                System.out.println("Permisos nulos");
            }
            consultarMetadatoJMenuItem.setEnabled(aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.VER_METADATOS)));
            crearMetadatoJMenuItem.setEnabled(aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS)));
        }
        else
        {
             consultarMetadatoJMenuItem.setEnabled(true);
            crearMetadatoJMenuItem.setEnabled(true);
        }
    }

	private boolean showAuth() {

		boolean resultado=false;
		CAuthDialog auth = new CAuthDialog(this, true,com.geopista.app.metadatos.init.Constantes.url,
                                        idApp,com.geopista.app.metadatos.init.Constantes.idMunicipio,
                                        messages,true);
		//----NUEVO---->
		//if(CertificateUtils.isDNIeActive()){
		//	auth.setBounds(30,60,270,215);
		//}else auth.setBounds(30,60,270,155);
		
		//Dialogo Login Centrado
		//GUIUtil.centreOnWindow(auth);
		//--FIN NUEVO-->
		auth.show();
        if (com.geopista.security.SecurityManager.isLogged())
            openSession();
        else
            closeSession();
		return true;
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
            //logger.warn("Fichero configuracion:"+ficheroConfiguracion);
			//fichConfig = new puzzled.IniFile(ficheroConfiguracion);
    		//Constantes.url= fichConfig.getValue("GENERAL", "SERVER_URL");
			//Constantes.timeout= new Integer(fichConfig.getValue("GENERAL", "SERVER_TIMEOUT")).intValue();
            //Constantes.country = fichConfig.getValue("IDIOMA", "COUNTRY");
            //Constantes.language = fichConfig.getValue("IDIOMA", "LANGUAGE");
            //Constantes.Locale = Constantes.language+"_"+Constantes.country;
            //Constantes.Municipio= fichConfig.getValue("AYUNTAMIENTO", "MUNICIPIO");
            //try
            //{
            //     Constantes.idMunicipio= new Integer(fichConfig.getValue("AYUNTAMIENTO", "IDMUNICIPIO")).intValue();
            //}catch (Exception e){
            //    logger.error("Valor de id municipio no valido:"+e.toString()+fichConfig.getValue("AYUNTAMIENTO", "IDMUNICIPIO"));
            //}
            try{
                   ApplicationContext app= AppContext.getApplicationContext();
                   com.geopista.app.metadatos.init.Constantes.url= app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+ WebAppConstants.METADATOS_WEBAPP_NAME;
                   com.geopista.app.metadatos.init.Constantes.Locale = app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES");
                   try{com.geopista.app.metadatos.init.Constantes.idMunicipio=new Integer(app.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)).intValue();
                   }catch (Exception e){
                              JOptionPane.showMessageDialog(this, "Valor de id municipio no valido:"+e.toString()+app.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
                              System.out.println("Valor de id municipio no valido:"+e.toString()+app.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
                              logger.error("Valor de id municipio no valido:"+e.toString()+app.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
                              if (fromInicio)
                              	  dispose();
                                else
                              	  System.exit(-1);
                   }
                   com.geopista.app.metadatos.init.Constantes.CARGAR_DE_DB=1;
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
           	logger.debug("metadatos.Constantes.url: " + com.geopista.app.metadatos.init.Constantes.url);
			logger.debug("metadatos.Constantes.timeout: " + com.geopista.app.metadatos.init.Constantes.timeout);
			//****************************************************************
			//** Establecemos el idioma especificado en la configuracion
			//*******************************************************
			setLang(com.geopista.app.metadatos.init.Constantes.Locale);
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
        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        gestionMetadatosMenu = new javax.swing.JMenu();
        consultarMetadatoJMenuItem = new javax.swing.JMenuItem();
        crearMetadatoJMenuItem = new javax.swing.JMenuItem();
        salirJMenuItem = new javax.swing.JMenuItem();
        idiomaMenu = new javax.swing.JMenu();
        castellanoJMenuItem = new javax.swing.JMenuItem();
        catalanJMenuItem = new javax.swing.JMenuItem();
        euskeraJMenuItem = new javax.swing.JMenuItem();
        gallegoJMenuItem = new javax.swing.JMenuItem();
        valencianoJMenuItem = new javax.swing.JMenuItem();
        helpJMenu = new javax.swing.JMenu();
        jMenuItemHelp = new javax.swing.JMenuItem();
        jMenuItemContactos= new  javax.swing.JMenuItem();
        aboutJMenuItem = new javax.swing.JMenuItem();
        salirJMenuItem = new javax.swing.JMenuItem();
        jMenuArchivo= new javax.swing.JMenu();
        jMenuVer= new javax.swing.JMenu();
        jChecBoxShowArbol= new javax.swing.JCheckBoxMenuItem();
        jChecBoxShowArbol.setSelected(true);
        jMenuItemConectar= new javax.swing.JMenuItem();
        jMenuItemDesconectar= new javax.swing.JMenuItem();
        jp_status = new JPanel();
        jLabelConectado= new JLabel("Desconectado");
        jLabelSesion= new JLabel("Sesión cerrada");


        setBackground(new java.awt.Color(0, 78, 152));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        desktopPane.setMinimumSize(null);
        desktopPane.setPreferredSize(null);
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        jp_status.setLayout(new BorderLayout());
        jp_status.add(jLabelConectado, BorderLayout.EAST);
        jp_status.add(jLabelSesion,BorderLayout.WEST);
        jp_status.setBorder(new BevelBorder(BevelBorder.LOWERED));
        getContentPane().add(jp_status,java.awt.BorderLayout.SOUTH);
        jMenuItemConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conectar();
            }
        });
        jMenuItemDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desconectar();
            }
        });

        jMenuArchivo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            compruebaConexion();
        }
        });


        jMenuArchivo.add(jMenuItemConectar);        
        //-----NUEVO----->	
        if(!SSOAuthManager.isSSOActive()) 
            jMenuArchivo.add(jMenuItemDesconectar);            
        //---FIN NUEVO--->        
        jMenuArchivo.add(salirJMenuItem);

        menuBar.add(jMenuArchivo);



        consultarMetadatoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarMetadato();
            }
        });
        gestionMetadatosMenu.add(consultarMetadatoJMenuItem);

        crearMetadatoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarMetadato();
            }
        });
        gestionMetadatosMenu.add(crearMetadatoJMenuItem);

        jMenuItemContactos.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        mostrarContactos();
                    }
        });
        gestionMetadatosMenu.add(jMenuItemContactos);


        salirJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitForm(null);
            }
        });



        menuBar.add(gestionMetadatosMenu);

        jChecBoxShowArbol.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            showArbol();
        }
        });
        jMenuVer.add(jChecBoxShowArbol);
        menuBar.add(jMenuVer);

        idiomaMenu.setMargin(null);

        castellanoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    setLang(com.geopista.app.metadatos.init.Constantes.LOCALE_CASTELLANO);
            }
        });
        catalanJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    setLang(com.geopista.app.metadatos.init.Constantes.LOCALE_CATALAN);
            }
        });

        euskeraJMenuItem.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                            setLang(com.geopista.app.metadatos.init.Constantes.LOCALE_EUSKEDA);
              }
        });
        gallegoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    setLang(com.geopista.app.metadatos.init.Constantes.LOCALE_GALLEGO);
              }
        });
        gallegoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  setLang(com.geopista.app.metadatos.init.Constantes.LOCALE_VALENCIANO);
              }
        });

        idiomaMenu.add(castellanoJMenuItem);

        idiomaMenu.add(catalanJMenuItem);

        idiomaMenu.add(euskeraJMenuItem);

        idiomaMenu.add(gallegoJMenuItem);

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

        
      //Gestion de conexiones y desconexiones contra el administrador
  		//de cartografia.
  		status=new ConnectionStatus(this,fromInicio);
  		status.init();	
  		aplicacion.getBlackboard().put(UserPreferenceConstants.CONNECT_STATUS,status);
      		
        getContentPane().add(status.getJPanelStatus(), BorderLayout.SOUTH);
        
        pack();

        java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage("img"+File.separator+"geopista.gif");
        setIconImage(img);
    }//GEN-END:initComponents


    private void mostrarMetadato() {
       if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
       logger.debug("Antes de Crear");
       Container c = this.getContentPane();
       c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       metadatosFrame=new CMetadatos(messages, this);
       if (com.geopista.security.SecurityManager.isLogged() && aclMetadatos!=null)
       {
            metadatosFrame.enabled(aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS)));
       }
       logger.debug("Despues de crear");
       mostrarJInternalFrame(metadatosFrame);
       showArbol();
       c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
       c.setCursor(Cursor.getDefaultCursor());
    }
    public void mostrarMetadato(CMetadatos metadatosFrame) {
       Container c = this.getContentPane();
       c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       if (this.metadatosFrame!=null) this.metadatosFrame.dispose();
       this.metadatosFrame=metadatosFrame;
       if (com.geopista.security.SecurityManager.isLogged() && aclMetadatos!=null)
       {
            this.metadatosFrame.enabled(aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS)));
       }
       else this.metadatosFrame.enabled(true);
       mostrarJInternalFrame(metadatosFrame);
       showArbol();
       c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
       c.setCursor(Cursor.getDefaultCursor());
    }



	private boolean mostrarJInternalFrame(JInternalFrame internalFrame) {

		try {

			int numInternalFrames=desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(internalFrame)).length;
			//logger.info("numInternalFrames: "+numInternalFrames);

			//if (numInternalFrames==0){
				internalFrame.setFrameIcon(new javax.swing.ImageIcon("img"+File.separator+"geopista.gif"));


				desktopPane.add(internalFrame);
                logger.debug("antes de mostrar pantalla");
				internalFrame.setMaximum(true);
				internalFrame.show();
                logger.debug("despues de mostrar pantalla");

			//}else{
			//	logger.info("cannot open another JInternalFrame");
			//}



		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}

		return true;
	}
       private boolean showSearchDialog() {

		com.geopista.app.metadatos.busqueda.CSearchMetadatos searchDialog = new com.geopista.app.metadatos.busqueda.CSearchMetadatos(this, true, messages);
        if (com.geopista.security.SecurityManager.isLogged())
        {
            searchDialog.enabled(aclMetadatos.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_METADATOS)));
        }
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        searchDialog.setLocation(d.width/2 - searchDialog.getSize().width/2, d.height/2 -  searchDialog.getSize().height/2);
 		searchDialog.setResizable(false);
		searchDialog.show();

		return true;
	}

        private void consultarMetadato()
        {
	        showSearchDialog();
        }

     public void showArbol()
     {
         if (metadatosFrame!=null)
             metadatosFrame.showArbol(jChecBoxShowArbol.isSelected());
     }

		/**
	 * Exit the Application
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {
        try
        {
        	//-----NUEVO----->	
            if(!SSOAuthManager.isSSOActive()) 
         	   com.geopista.security.SecurityManager.logout();
            //---FIN NUEVO--->
            dispose();
        }catch(Exception e){};
        if (fromInicio)
        	  dispose();
          else
        	  System.exit(0);
	}

	private boolean setLang(String locale) {

		try {

            //System.out.println("CAMBIO idioma a "+locale);
            //logger.debug("Cambio idioma a: " + locale);

            try
            {
                //ApplicationContext app = AppContext.getApplicationContext();
                UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY,locale);
            }catch(Exception e)
            {
                logger.error("Exception: " + e.toString());
            }

			Locale currentLocale = new Locale(locale);
            try
            {
			    messages = ResourceBundle.getBundle("config.metadatos", currentLocale);
                messages.getString("CAuthDialog.title");
                logger.debug("Current Locale existe: " + locale);
            }catch (Exception e)
            {
                messages = ResourceBundle.getBundle("config.metadatos", new Locale(Constantes.LOCALE_CASTELLANO));
                logger.debug("Current Locale No existe: " + locale);
            }
            if (messages==null)
            {
                messages = ResourceBundle.getBundle("config.metadatos", new Locale(Constantes.LOCALE_CASTELLANO));
            }
            changeScreenLang(messages);

            //Aqui tengo que cambiar las pantalla que tenga de la aplicacion
            if (metadatosFrame!=null) metadatosFrame.changeScreenLang(messages);
            com.geopista.app.metadatos.init.Constantes.Locale = locale;
			return true;

		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString());
			return false;
		}

	}





	private boolean changeScreenLang(ResourceBundle messages) {
		  if (com.geopista.security.SecurityManager.isLogged()){
			   com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
			   setTitle(messages.getString("CMainMetadatos.title")+" - "+
	            		messages.getString("CAuthDialog.jLabelNombre")+principal);
		  }
		  else
			  setTitle(messages.getString("CMainMetadatos.title"));
        this.messages=messages;
        gestionMetadatosMenu.setText(messages.getString("CMainMetadatos.gestionMetadatosMenu"));
        consultarMetadatoJMenuItem.setText(messages.getString("CMainMetadatos.consultarMetadatoJMenuItem"));
        crearMetadatoJMenuItem.setText(messages.getString("CMainMetadatos.crearMetadatoJMenuItem"));
        salirJMenuItem.setText(messages.getString("CMainMetadatos.salirJMenuItem"));
        idiomaMenu.setText(messages.getString("CMainMetadatos.idiomaMenu"));

        castellanoJMenuItem.setText(messages.getString("CMainMetadatos.castellanoJMenuItem"));
        catalanJMenuItem.setText(messages.getString("CMainMetadatos.catalanJMenuItem"));
        euskeraJMenuItem.setText(messages.getString("CMainMetadatos.euskeraJMenuItem"));
        gallegoJMenuItem.setText(messages.getString("CMainMetadatos.gallegoJMenuItem"));
        valencianoJMenuItem.setText(messages.getString("CMainMetadatos.valencianoJMenuItem"));
        helpJMenu.setText(messages.getString("CMainMetadatos.helpJMenu"));
        jMenuItemHelp.setText(messages.getString("CMainMetadatos.jMenuItemHelp"));
        aboutJMenuItem.setText(messages.getString("CMainMetadatos.aboutJMenuItem"));
        jMenuArchivo.setText(messages.getString("CMainMetadatos.jMenuArchivo"));
        jMenuItemConectar.setText(messages.getString("CMainMetadatos.jMenuItemConectar"));
        jMenuItemDesconectar.setText(messages.getString("CMainMetadatos.jMenuItemDesconectar"));
        jMenuVer.setText(messages.getString("CMainMetadatos.jMenuVer"));
        jChecBoxShowArbol.setText(messages.getString("CMainMetadatos.jChecBoxShowArbol"));
        jMenuItemContactos.setText(messages.getString("CSearchContactos.jPanelListaContactos"));
        if (metadatosFrame!=null) metadatosFrame.changeScreenLang(messages);
        return true;

	}
    private void mostrarContactos() {
       CSearchContactos dialogContactos = new CSearchContactos(this, true, messages);
       Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
       dialogContactos.setLocation(d.width/2 - dialogContactos.getSize().width/2, d.height/2 - dialogContactos.getSize().height/2);
       dialogContactos.setResizable(false);
       dialogContactos.show();
       dialogContactos=null;
    }

    private void compruebaConexion()
    {
        if (com.geopista.security.SecurityManager.isLogged())
        {
            jMenuItemConectar.setEnabled(false);
            jMenuItemDesconectar.setEnabled(true);
        }
        else
        {
            jMenuItemConectar.setEnabled(true);
            jMenuItemDesconectar.setEnabled(false);
        }

    }

    public void connect()
    {
         jLabelConectado.setText("Conectado");
    }
    public void disconnect()
    {
         jLabelConectado.setText("Desconectado");
         closeSession();
    }
    public void openSession()
    {
    	
        try
        {
        Municipio municipio = (new OperacionesAdministrador(com.geopista.app.metadatos.init.Constantes.url)).getMunicipio(new Integer(com.geopista.app.metadatos.init.Constantes.idMunicipio).toString());
        if (municipio!=null)
        {
                Constantes.Municipio = municipio.getNombre();
                Constantes.Provincia= municipio.getProvincia();
        }}catch(Exception e){}
        jLabelSesion.setText("Sesión abierta");
    }
    public void closeSession()
    {
        jLabelSesion.setText("Sesión cerrada");
    }
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

		new CMainMetadatos();


	}
    
     public boolean mostrarAyuda()
    {
        HelpSet hs = null;
        ClassLoader loader = this.getClass().getClassLoader();
        try
        {
                String helpSetFile = "help/metadatos/GeopistaHelpMetadatos_" + com.geopista.app.metadatos.init.Constantes.Locale + ".hs";
                logger.info("Mostrando el fichero de ayuda: "+helpSetFile);
                URL url = HelpSet.findHelpSet(loader, helpSetFile);
                if (url == null)//tomamos el idioma castellano por defecto
                {
                    logger.error("Imposible cargar el fichero de ayuda: "+helpSetFile);
                    helpSetFile = "help/metadatos/GeopistaHelpMetadatos_" + com.geopista.app.metadatos.init.Constantes.LOCALE_CASTELLANO + ".hs";
                    url = HelpSet.findHelpSet(loader, helpSetFile);
                }
                if (url== null)
                {
                    logger.error("Imposible cargar el fichero de ayuda: "+helpSetFile);
                    return false;
                }
                logger.info("Fichero de ayuda: "+helpSetFile);
                hs = new HelpSet(loader, url);
                hs.setHomeID(com.geopista.app.metadatos.init.Constantes.helpSetHomeID);
         } catch (Exception ex) {
                logger.error("Exception: " + ex.toString());
                return false;
         }
        HelpBroker hb = hs.createHelpBroker();
        hb.setDisplayed(true);
        new CSH.DisplayHelpFromSource(hb);
        return true;
    }


 	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutJMenuItem;
    private javax.swing.JMenuItem castellanoJMenuItem;
    private javax.swing.JMenuItem catalanJMenuItem;
    private javax.swing.JMenuItem consultarMetadatoJMenuItem;
    private javax.swing.JMenuItem crearMetadatoJMenuItem;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem euskeraJMenuItem;
    private javax.swing.JMenuItem gallegoJMenuItem;
    private javax.swing.JMenu gestionMetadatosMenu;
    private javax.swing.JMenu helpJMenu;
    private javax.swing.JMenu idiomaMenu;
    private javax.swing.JMenu jMenuVer;
    private javax.swing.JCheckBoxMenuItem jChecBoxShowArbol;
    private javax.swing.JMenu jMenuArchivo;
    private javax.swing.JMenuItem jMenuItemConectar;
    private javax.swing.JMenuItem jMenuItemDesconectar;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem valencianoJMenuItem;
    private javax.swing.JMenuItem salirJMenuItem;
    private javax.swing.JPanel jp_status;
    private javax.swing.JLabel jLabelConectado;
    private javax.swing.JLabel jLabelSesion;
    private javax.swing.JMenuItem jMenuItemHelp;
    private javax.swing.JMenuItem jMenuItemContactos;

    // End of variables declaration//GEN-END:variables


    // Metodos que implementan la interfaz ISecurityPolicy

	@Override
	public void setPolicy() throws AclNotFoundException, Exception {		
	}
	@Override
	public void resetSecurityPolicy() {	
	}
	
	@Override
	public ApplicationContext getAplicacion() {
		return aplicacion;
	}
	@Override
	public String getIdApp() {
		return idApp;
	}
	@Override
	public String getIdMunicipio() {
		return String.valueOf(Constantes.idEntidad);
	}
	@Override
	public String getLogin() {
		return Constantes.url;
	}
	@Override
	public JFrame getFrame() {
		return this;
	}
}


