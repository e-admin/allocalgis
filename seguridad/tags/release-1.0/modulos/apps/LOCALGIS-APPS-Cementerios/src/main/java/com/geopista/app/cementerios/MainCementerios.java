package com.geopista.app.cementerios;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.acl.AclNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
//import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.cementerios.UtilidadesComponentes;
import com.geopista.app.utilidades.CMain;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.licencias.CConstantesPaths;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.geopista.app.cementerios.CementeriosInternalFrame;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;

public class MainCementerios extends CMain {
	
    static Logger logger= Logger.getLogger(MainCementerios.class);
    private JInternalFrame iFrame;
    private Hashtable permisos= new Hashtable();
    private ApplicationContext aplicacion;
	private boolean fromInicio;
	private Blackboard blackboard = null;
	
	public MainCementerios() {
		this(false);
	}	
	
    public MainCementerios(boolean fromInicio) {
    	this.fromInicio=fromInicio;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        aplicacion= (AppContext)AppContext.getApplicationContext();
        aplicacion.setMainFrame(this);
        blackboard = aplicacion.getBlackboard();
        try {initLookAndFeel();} catch (Exception e) {}
        try{
            SplashWindow splashWindow = showSplash();
            initComponents();
            configureApp();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            ClassLoader cl = this.getClass().getClassLoader();
            java.awt.Image img= java.awt.Toolkit.getDefaultToolkit().getImage(cl.getResource(CConstantesPaths.IMAGE_PATH + "geopista.gif"));
            setIconImage(img);
            show();
            desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            /** deshabilitamos la barra de tareas */
            try{
                UtilidadesComponentes.inicializar();
                UtilidadesComponentes.setEnabled(false, this);
            }catch(Exception e){logger.error(e.toString());e.printStackTrace();}

            if (splashWindow != null) splashWindow.setVisible(false);
            //******************
            //Mostramos la pantalla de autenticación del usuario.
            //******************
			//--------NUEVO-------------->
//            if(SSOAuthManager.isSSOActive()){ 				
// 				com.geopista.protocol.CConstantesComando.servletLicenciasUrl = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL)+"CServletLicencias";
//                com.geopista.protocol.CConstantesComando.loginLicenciasUrl = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
//                com.geopista.protocol.CConstantesComando.adminCartografiaUrl = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
//       	  	}	           
            SSOAuthManager.ssoAuthManager(aplicacion.getDefaultProfile());					
            if (!aplicacion.isOnlyLogged()){  
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
			
            AppContext.seleccionarMunicipio((Frame)this);
            //Descomentar
            //ConstantesRegistroExp.IdMunicipio = AppContext.getIdMunicipio();

            /** cargamos las estructuras */
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(this, null);
            progressDialog.setTitle(aplicacion.getI18nString("cementerio.app.tag1"));
            progressDialog.addComponentListener(new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                     new Thread(new Runnable(){
                              public void run(){
                                  try{
                                      progressDialog.report(aplicacion.getI18nString("cementerio.app.tag1"));
                                      while (!Estructuras.isCargada()) {
                                            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
                                            try {Thread.sleep(500);} catch (Exception e) {}
                                      }
                                  }catch(Exception e){
                                    logger.error("Error", e);
                                    ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
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

            CementeriosInternalFrame cementeriosInternalFrame = new CementeriosInternalFrame(this);
            mostrarJInternalFrame(cementeriosInternalFrame);
            if (cementeriosInternalFrame.getJPanelMap() != null){
            	cementeriosInternalFrame.getJPanelMap().initEditor();

            	cementeriosInternalFrame.getCemenenerioJPanel().tipoElemCementeriosJPanel.setEnabled(cementeriosInternalFrame.getJPanelMap().isHayCementerio());
            }
            
            
            UtilidadesComponentes.setEnabled(true, (JFrame)this);
            desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        }catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("ERROR:" + sw.toString());
			logger.error("Exception: " + sw.toString());
		}
    }

    private void initComponents() {
        desktopPane= new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();

        idiomaMenu = new javax.swing.JMenu();
        castellanoJMenuItem= new javax.swing.JMenuItem();
        catalanJMenuItem= new javax.swing.JMenuItem();
        euskeraJMenuItem= new javax.swing.JMenuItem();
        gallegoJMenuItem= new javax.swing.JMenuItem();
        valencianoJMenuItem= new javax.swing.JMenuItem();

		idiomaMenu.setMargin(null);

        idiomaMenu.add(castellanoJMenuItem);
		castellanoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				castellanoJMenuItemActionPerformed();
			}
		});

		idiomaMenu.add(catalanJMenuItem);
		catalanJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				catalanJMenuItemActionPerformed();
			}
		});


		idiomaMenu.add(euskeraJMenuItem);
		euskeraJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				euskeraJMenuItemActionPerformed();
			}
		});


		idiomaMenu.add(gallegoJMenuItem);
		gallegoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				gallegoJMenuItemActionPerformed();
			}
		});

		idiomaMenu.add(valencianoJMenuItem);
		valencianoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				valencianoJMenuItemActionPerformed();
			}
		});


		menuBar.add(idiomaMenu);

        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });


        desktopPane.setMinimumSize(null);
        desktopPane.setPreferredSize(null);
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        setJMenuBar(menuBar);
        pack();


    }

    private void castellanoJMenuItemActionPerformed() {
        Constantes.Locale= "es_ES";
        setLang(Constantes.Locale);
    }

    private void gallegoJMenuItemActionPerformed() {
        Constantes.Locale= "gl_ES";
        setLang(Constantes.Locale);
    }
    private void catalanJMenuItemActionPerformed() {
        Constantes.Locale= "ca_ES";
        setLang(Constantes.Locale);
    }
    private void euskeraJMenuItemActionPerformed() {
        Constantes.Locale= "eu_ES";
        setLang(Constantes.Locale);
    }

    private void valencianoJMenuItemActionPerformed() {
        Constantes.Locale= "va_ES";
        setLang(Constantes.Locale);
    }


    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {
        try {
            //-----NUEVO----->	
            if(!SSOAuthManager.isSSOActive()) 
            	com.geopista.security.SecurityManager.logout();
            //---FIN NUEVO--->
        } catch (Exception ex) {
            logger.warn("Exception: " + ex.toString());
        }
        if (fromInicio)
      	  dispose();
        else
      	  System.exit(0);
    }

    public boolean mostrarJInternalFrame(JInternalFrame internalFrame) {
        try {
            desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            int numInternalFrames = desktopPane.getAllFrames().length;
            if (numInternalFrames == 0) {
                ClassLoader cl= this.getClass().getClassLoader();
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


    private void setLang(String locale) {
        try{
            aplicacion.setUserPreference(AppContext.GEOPISTA_LOCALE_KEY, locale);

            String lengua= locale.substring(0,2);
            String pais= locale.substring(3,5);
            Locale deflocale= new Locale(lengua, pais);
            aplicacion.loadI18NResource("GeoPistai18n", deflocale);
        }catch(Exception e){
            logger.error("Exception: " + e.toString());
        }

        renombrarComponentes();
        try {((IMultilingue)desktopPane.getComponents()[0]).renombrarComponentes();}catch (Exception e) {}
    }

    private void renombrarComponentes() {
        try {
        	String title=aplicacion.getI18nString("cementerio.app.tag0");
			String release=aplicacion.getString("localgis.release");
			if (release==null)
				release="LocalGIS";
			title=title.replaceAll("\\$\\{localgis\\.release\\}",release);				
	        setTitle(title);
            
            if (Constantes.Municipio!=null)
                        setTitle(getTitle()+" - "+Constantes.Municipio + " ("+Constantes.Provincia+")");

            idiomaMenu.setText(aplicacion.getI18nString("cementerio.idioma.tag0"));
            castellanoJMenuItem.setText(aplicacion.getI18nString("cementerio.idioma.tag1"));
            euskeraJMenuItem.setText(aplicacion.getI18nString("cementerio.idioma.tag2"));
            catalanJMenuItem.setText(aplicacion.getI18nString("cementerio.idioma.tag3"));
            gallegoJMenuItem.setText(aplicacion.getI18nString("cementerio.idioma.tag4"));
            valencianoJMenuItem.setText(aplicacion.getI18nString("cementerio.idioma.tag5"));

        }catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
    }

    private boolean configureApp() {
        try {
            try{PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);}catch(Exception e){};
            try{
            		 com.geopista.protocol.CConstantesComando.servletLicenciasUrl = aplicacion.getString("geopista.conexion.servidorurl") + WebAppConstants.LICENCIAS_OBRA_WEBAPP_NAME + ServletConstants.CSERVLETLICENCIAS_SERVLET_NAME;
                    com.geopista.protocol.CConstantesComando.loginLicenciasUrl = aplicacion.getString("geopista.conexion.servidorurl")+ WebAppConstants.CEMENTERIOS_WEBAPP_NAME;
                    com.geopista.protocol.CConstantesComando.adminCartografiaUrl = aplicacion.getString("geopista.conexion.servidorurl")+ WebAppConstants.GEOPISTA_WEBAPP_NAME;

                   Constantes.Locale= aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");

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

            /** Establecemos el idioma especificado en la configuracion */
            setLang(Constantes.Locale);
            return true;

        } catch (Exception ex) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;
        }
    }

    private boolean showAuth() {
        try {
        	boolean resultado=false;
            resetSecurityPolicy();
            com.geopista.app.utilidades.CAuthDialog auth =
                new com.geopista.app.utilidades.CAuthDialog(this, true,
                        CConstantesComando.loginLicenciasUrl,Constantes.idApp,
                        Constantes.IdEntidad, aplicacion.getI18NResource());
            auth.setBounds(30, 60, 315, 155);
            if (fromInicio){
            	resultado=auth.showD(true);
            	if (!resultado)
            		return false;
            }
            else{
            	auth.show();
            }
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
            GeopistaAcl acl= com.geopista.security.SecurityManager.getPerfil("Cementerios");
            applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
    }
    

    public void resetSecurityPolicy() {
    }

    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal) {
        try {
            if ((acl == null) || (principal == null) || (acl.getPermissions(principal)==null)) {
                return false;
            }           
            setPermisos(acl.getPermissions(principal));

            if (tienePermisos("Geopista.Cementerios.Login")) {
            }

            return true;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;
        }
    }

    public boolean tienePermisos(String permiso){
        if (permisos.containsKey(permiso)) return true;
        return false;
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


    public JInternalFrame getIFrame(){
    	return iFrame;
    }
    
	/** @param args the command line arguments */
    public static void main(String args[]) {
        new MainCementerios();
    }

    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu idiomaMenu;
    private javax.swing.JMenuItem castellanoJMenuItem;
    private javax.swing.JMenuItem catalanJMenuItem;
    private javax.swing.JMenuItem valencianoJMenuItem;
    private javax.swing.JMenuItem euskeraJMenuItem;
    private javax.swing.JMenuItem gallegoJMenuItem;

}
