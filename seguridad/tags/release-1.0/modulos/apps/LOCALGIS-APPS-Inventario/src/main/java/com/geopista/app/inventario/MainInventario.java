package com.geopista.app.inventario;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.acl.AclNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
//import com.geopista.app.catastro.gestorcatastral.images.IconLoader;
import com.geopista.ui.images.IconLoader;
//import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistro;
import com.geopista.app.inventario.panel.BienesPreAltaJPanel;
import com.geopista.app.utilidades.CMain;
import com.geopista.editor.GeopistaEditor;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.licencias.CConstantesPaths;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.exception.CancelException;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class MainInventario extends CMain implements IMainInventario {
    static Logger logger= Logger.getLogger(MainInventario.class);
    private JInternalFrame iFrame;
    private Hashtable permisos= new Hashtable();
    private ApplicationContext aplicacion;
	private boolean fromInicio;
	private Blackboard blackboard = null;
	private InventarioInternalFrame inventarioInternalFrame; 
	private Municipio municipio;
	
	
    private JPanel jPanelStatus = null;
    private JLabel connectionLabel = new JLabel();
	
	public MainInventario() {
		this(false);
	}	
	
    public MainInventario(boolean fromInicio) {
    	
    	startApp(fromInicio);
    }
    public void startApp(boolean fromInicio){
    
    	logger.info("Arrancando aplicacion de inventario");
    	String mostrarPaginacion=System.getProperty("mostrarPaginacion");
    	
    	if ((mostrarPaginacion!=null) && (mostrarPaginacion.equals("true"))){
    		Constantes.MOSTRAR_PAGINACION=true;
    	}
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
            setVisible(true);
            desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            /** deshabilitamos la barra de tareas */
            try{
                com.geopista.app.inventario.UtilidadesComponentes.inicializar();
                com.geopista.app.inventario.UtilidadesComponentes.setEnabled(false, this);
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
                   
            if(!tienePermisos("Geopista.Inventario.Login")){
            	noPermApp();
            	return;
            }      
            if (!AppContext.seleccionarMunicipio((Frame)this)){
            	stopApp();
            	return;
            }
            
            ConstantesRegistro.IdMunicipio = AppContext.getIdMunicipio();

            /** cargamos el municipio y la provincia */
            municipio= (new OperacionesAdministrador(com.geopista.protocol.CConstantesComando.loginLicenciasUrl)).getMunicipio(new Integer(ConstantesRegistro.IdMunicipio).toString());
            if (municipio!=null){
                Constantes.Municipio= municipio.getNombre();
                Constantes.Provincia= municipio.getProvincia();
                setTitle(getTitle()+" - "+Constantes.Municipio + "-"+ConstantesRegistro.IdMunicipio+" ("+Constantes.Provincia+")");
            	com.geopista.security.SecurityManager.setIdMunicipio(new Integer(ConstantesRegistro.IdMunicipio).toString());
            	System.setProperty("CodigoIne",municipio.getId());
            	
            	//No se si realment hay que utilizar esta llamada para inicializar el log4j
            	/*
            	try{
            		URL url = getClass().getResource( "/config/log4j.ini");  
            		PropertyConfigurator.configure (url); 
     		   		//PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);
     		   	}catch(Exception e){};
     		   	*/
            }
            

            /** cargamos las estructuras */
  
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
                                  }catch (CancelException e1){
                                	  
                                  }
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
            
             
            if (Estructuras.isCancelada()){
            	stopApp();
            	return;            	
            }
            
            inventarioInternalFrame = new InventarioInternalFrame(this);
            mostrarJInternalFrame(inventarioInternalFrame);
                        
            
        	String mostrarMapa=System.getProperty("mostrarMapa");
        	
        	if ((mostrarMapa!=null) && (mostrarMapa.equals("false"))){
        		Constantes.MOSTRAR_MAPA=false;
        	}

            //AQUI SE INICIALIZA EL EDITOR
        	if (Constantes.MOSTRAR_MAPA)
	            if (inventarioInternalFrame.getJPanelMap() != null){
	            	inventarioInternalFrame.getJPanelMap().initEditor();
	            }
        	
        	addFixedPlugins();
        	AppContext.showMunicipiosEntidad();        	
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
    
    private void addFixedPlugins(){
    	
    	try {
    		GeopistaEditor editor=inventarioInternalFrame.getJPanelMap().getGeopistaEditor();
			WorkbenchToolBar newToolBar=editor.addToolbar(aplicacion.getString("MunicipalitiesPlugIn.category"));
						
			/*String pluginName="com.geopista.ui.plugin.MunicipalitiesPlugIn";
			PlugIn cargandoPlugin = (PlugIn) (Class.forName(pluginName)).newInstance();
			
			((com.geopista.ui.plugin.MunicipalitiesPlugIn)cargandoPlugin).initialize(
					new PlugInContext(editor.getContext(), 
										editor.getTask(),
										editor , 
										editor.getLayerNamePanel(), 
										editor.getLayerViewPanel()),newToolBar);
			*/
			/*newToolBar.addCursorTool("Zoom In/Out", (CursorTool)(Class.forName("com.vividsolutions.jump.workbench.ui.zoom.ZoomTool")).newInstance() );       
	        for (int i=1;i<10;i++){
	        	newToolBar.addCursorTool("Zoom In/Out"+i, (CursorTool)(Class.forName("com.vividsolutions.jump.workbench.ui.zoom.ZoomTool")).newInstance() );
	        }*/
	      			
			editor.addPlugIn("com.geopista.ui.plugin.MunicipalitiesPlugIn");
			((JPanel)editor. getToolBar().getParent()).setLayout(new GridLayout(2,1));
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }
    
    private void  stopApp(){
    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Inicio de aplicación cancelado. Se reiniciará el aplicativo");
    	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	toolMenu.setEnabled(false);
    	configMenu.setEnabled(false);
    	idiomaMenu.setEnabled(false);
    	amortizacionMenu.setEnabled(false);
    	eielMenu.setEnabled(false);
    	ayudaMenu.setEnabled(false);
    	System.exit(1);
    }

    private void noPermApp(){
    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"No tiene permisos para entrar. Se reiniciará el aplicativo");
    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	 System.exit(1);
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        desktopPane= new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();

        idiomaMenu = new javax.swing.JMenu();
        amortizacionMenu = new javax.swing.JMenu();
        toolMenu = new javax.swing.JMenu();
        configMenu = new javax.swing.JMenu();
        eielMenu = new javax.swing.JMenu();
        ayudaMenu = new javax.swing.JMenu();
        historicoAmortizacionJMenuItem=new javax.swing.JMenuItem();
        castellanoJMenuItem= new javax.swing.JMenuItem();
        catalanJMenuItem= new javax.swing.JMenuItem();
        euskeraJMenuItem= new javax.swing.JMenuItem();
        gallegoJMenuItem= new javax.swing.JMenuItem();
        valencianoJMenuItem= new javax.swing.JMenuItem();
        loadInventarioJMenuItem= new javax.swing.JMenuItem();
        saveInventarioJMenuItem= new javax.swing.JMenuItem();
        saveCatalogoJMenuItem= new javax.swing.JMenuItem();
        loadCatalogoJMenuItem= new javax.swing.JMenuItem();
        bienePreAltaJMenuItem= new javax.swing.JMenuItem();
        
        initInventarioJMenuItem= new javax.swing.JMenuItem();
        
        //Filtro de Elementos
        filtrarEliminadosInventarioJMenuItem=new javax.swing.JCheckBoxMenuItem();
        filtrarBajasInventarioJMenuItem=new javax.swing.JCheckBoxMenuItem();
        
        confDatosValoracionJMenuItem=new javax.swing.JMenuItem();
        elementoseielJMenuItem=new javax.swing.JMenuItem();
        
        menuIncidenciasJMenuItem=new javax.swing.JMenuItem();
        
		idiomaMenu.setMargin(null);
		amortizacionMenu.setMargin(null);
		historicoAmortizacionJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				historicoAmortizacionJMenuItemActionPerformed();
			}
		});
		amortizacionMenu.add(historicoAmortizacionJMenuItem);
		
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

        toolMenu.add(loadCatalogoJMenuItem);
        loadCatalogoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadCatalogoJMenuItemActionPerformed();
			}
		});       
        
        toolMenu.add(saveCatalogoJMenuItem);
        saveCatalogoJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveCatalogoJMenuItemActionPerformed();
			}
		});
        toolMenu.add(loadInventarioJMenuItem);
        loadInventarioJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			    loadInventarioJMenuItemActionPerformed();
			}
		});
        toolMenu.add(saveInventarioJMenuItem);
        saveInventarioJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			    saveInventarioJMenuItemActionPerformed();
			}
		});
        
        toolMenu.add(bienePreAltaJMenuItem);
        bienePreAltaJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bienePreAltaJMenuItemActionPerformed();
			}
		}); 
       
        
        //TODO QUITAR
        /*toolMenu.add(initInventarioJMenuItem);
        initInventarioJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			    initInventarioJMenuItemActionPerformed();
			}
		});*/
        

        configMenu.add(filtrarEliminadosInventarioJMenuItem);
        filtrarEliminadosInventarioJMenuItem.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	filtrarEliminadosJMenuItemActionPerformed();            	
            }
          });
        configMenu.add(filtrarBajasInventarioJMenuItem);
        filtrarBajasInventarioJMenuItem.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	filtrarBajasJMenuItemActionPerformed();            	
            }
          });
        
        configMenu.add(confDatosValoracionJMenuItem);
        //TODO: dependiendo del permiso set enabled
//        confDatosValoracionJMenuItem.setEnabled(false);
        confDatosValoracionJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	confDatosValoracionJMenuItemActionPerformed();            	
            }
          }); 
        
        eielMenu.add(elementoseielJMenuItem);
        elementoseielJMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	elementoseielJMenuItemActionPerformed();            	
            }
          }); 
        
        
        ayudaMenu.add(menuIncidenciasJMenuItem);
        menuIncidenciasJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	sugerenciasActionPerformed();           	
            }
          });
        
               
        menuBar.add(toolMenu);
		menuBar.add(idiomaMenu);
		menuBar.add(eielMenu);
		menuBar.add(amortizacionMenu);
		menuBar.add(configMenu);
		menuBar.add(ayudaMenu);

        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });


        desktopPane.setMinimumSize(null);
        desktopPane.setPreferredSize(null);
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        //Panel de Estado (Conexion)
        getContentPane().add(getJPanelStatus(), BorderLayout.SOUTH);
        setJMenuBar(menuBar);
        pack();
        
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
    
    private JPanel getJPanelStatus ()
    {
        if (jPanelStatus == null)
        {
            jPanelStatus = new JPanel(new GridBagLayout());
            //jPanelStatus.setLayout(new BorderLayout());
            /*JLabel lblMessage = new JLabel("Mensaje");
            jPanelStatus.add(lblMessage, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                    0, 0), 0, 0));*/
            jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());
            
            connectionLabel.setBorder(new javax.swing.border.SoftBevelBorder(
                    javax.swing.border.SoftBevelBorder.LOWERED));
            jPanelStatus.add(connectionLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0,
                            0, 0), 0, 0));
            jPanelStatus.add(new JPanel(),
                    new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));               
        }
        return jPanelStatus;
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
            else{
            	if (!aplicacion.isPartialLogged())
            		aplicacion.login();
            }

        } else
        {
            connectionLabel.setIcon(IconLoader.icon("online.png"));
            connectionLabel.setToolTipText(aplicacion
                    .getI18nString("geopista.OnLineStatusMessage"));
                        
            if (!aplicacion.isLogged()){
            	if (!aplicacion.isPartialLogged())
            		showAuth();            
            }
            
        }
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
    
    public boolean sugerenciasActionPerformed() {
		com.geopista.app.sugerencias.SugerenciasForm sform = new com.geopista.app.sugerencias.SugerenciasForm();
		sform.setVisible(true);
		return true;
	}
    
    /**
     * Muestra una pantalla con los bienes en prealta
     */
	private void bienePreAltaJMenuItemActionPerformed() {
		BienesPreAltaJPanel bienesPAJPanel = new BienesPreAltaJPanel(this,
				aplicacion.getI18NResource());
		boolean bienes = bienesPAJPanel.rellenaBienes();
		if (bienes) {
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			bienesPAJPanel.setLocation(d.width / 7, d.height / 7);
			bienesPAJPanel.setVisible(true);
		}
    }
    /**
     * Muestra la pantalla para cargar el catalogo
     */
    private void loadCatalogoJMenuItemActionPerformed(){
    	LoadCatalogo loadCatalogo = new LoadCatalogo(this, true, aplicacion.getI18NResource(), municipio);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        loadCatalogo.setLocation(d.width/2 - 400/2, d.height/2 - 400/2);
        loadCatalogo.setSize(500,500);
        loadCatalogo.setVisible(true);
    }
    /**
     * Muestra la pantalla para cargar el inventario
     */
    private void loadInventarioJMenuItemActionPerformed(){
    	if (inventarioInternalFrame==null || inventarioInternalFrame.getJPanelMap()==null || 
    			inventarioInternalFrame.getJPanelMap().getGeopistaEditor()==null) return;
    	LoadInventario loadInventario = new LoadInventario(this, true, aplicacion.getI18NResource(), 
    			inventarioInternalFrame.getJPanelMap().getGeopistaEditor(), municipio);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        loadInventario.setLocation(d.width/2 - 500/2, d.height/2 - 500/2);
        loadInventario.setSize(500,500);
        loadInventario.setVisible(true);
    }
    
    /**
     * Muestra la pantalla para exportar el catalogo
     */
    private void saveCatalogoJMenuItemActionPerformed(){
    	SaveCatalogo saveCatalogo = new SaveCatalogo(this, true, aplicacion.getI18NResource(), municipio);
    	saveCatalogo.guardarFichero();
//        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//        saveCatalogo.setLocation(d.width/2 - 400/2, d.height/2 - 400/2);
//        saveCatalogo.setSize(500,500);
//        saveCatalogo.setVisible(true);
    }
    
    /**
     * Muestra la pantalla para guardar los bienes del inventario
     */
    private void saveInventarioJMenuItemActionPerformed(){
    	if (inventarioInternalFrame==null || inventarioInternalFrame.getJPanelMap()==null || 
    			inventarioInternalFrame.getJPanelMap().getGeopistaEditor()==null) return;
    	SaveInventario saveInventario = new SaveInventario(this, true, aplicacion.getI18NResource(), 
    			inventarioInternalFrame.getJPanelMap().getGeopistaEditor(), municipio);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        saveInventario.setLocation(d.width/2 - 500/2, d.height/2 - 500/2);
        saveInventario.setSize(350,250);
        saveInventario.setVisible(true);
    }
    
    private void initInventarioJMenuItemActionPerformed(){
    	

    	if (inventarioInternalFrame!=null){
    		inventarioInternalFrame.dispose();
    		inventarioInternalFrame=null;
    	}
    	else{
    		  inventarioInternalFrame = new InventarioInternalFrame(this);
              mostrarJInternalFrame(inventarioInternalFrame);
              
             ((JPanel)inventarioInternalFrame.getJPanelMap().getGeopistaEditor().
            		 getToolBar().getParent()).setLayout(new GridLayout(2,10));
             	 
    	}
    	/*
		aplicacion.logout();
		Constantes.Municipio= null;
        Constantes.Provincia= null;
        Estructuras.setCargada(false);
        Estructuras.setIniciada(false);
		startApp(false);*/   	
    }
    
    private void filtrarEliminadosJMenuItemActionPerformed(){
    	
    	Vector filtrosToRemove=new Vector();
    	filtrosToRemove.add("borrado");
    	filtrosToRemove.add("bajas");
    	inventarioInternalFrame.getInventarioJPanel().setFilter("eliminados",filtrarEliminadosInventarioJMenuItem.isSelected(),filtrosToRemove);
    	
    	//Recargamos
    	inventarioInternalFrame.getInventarioJPanel().tipoBienesJPanel_actionPerformed();
    	
    }
    private void filtrarBajasJMenuItemActionPerformed(){
    	
    	Vector filtrosToRemove=new Vector();
    	filtrosToRemove.add("borrado");
    	filtrosToRemove.add("eliminados");
    	inventarioInternalFrame.getInventarioJPanel().
    		setFilter("bajas",filtrarBajasInventarioJMenuItem.isSelected(),filtrosToRemove);
    	
    	//Recargamos
    	inventarioInternalFrame.getInventarioJPanel().tipoBienesJPanel_actionPerformed();
    	
    	
    }
    
    private void elementoseielJMenuItemActionPerformed(){
    	
		 if (inventarioInternalFrame==null ) return;
		 IntegracionEIELJDialog integracionEIEL = new IntegracionEIELJDialog(this, true, aplicacion.getI18NResource(), municipio);
	     Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	     integracionEIEL.setLocation(d.width/2 - 500/2, d.height/2 - 500/2);
	     integracionEIEL.setSize(600,400);
	     integracionEIEL.setVisible(true);
   	
    }
    
    private void historicoAmortizacionJMenuItemActionPerformed(){
    	 if (inventarioInternalFrame==null) return; 
 		SolicitarAnioHistoricoAmortDialog solicitarAnioAmortDialog = new SolicitarAnioHistoricoAmortDialog();
 		solicitarAnioAmortDialog.setVisible(true);
 		if(solicitarAnioAmortDialog.wasPressedOk()){
	    	 HistoricoAmortizacionJDialog haJDialog = new HistoricoAmortizacionJDialog(this, true, aplicacion.getI18NResource(), municipio);
	    	 haJDialog.setTitle(GeopistaUtil.i18n_getname("inventario.historicoamortizacion.menu.title", aplicacion.getI18NResource()));
		     Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		     haJDialog.setLocation(d.width/2 - 850/2, d.height/2 -590/2);
		     haJDialog.setSize(900,590);
		     haJDialog.setVisible(true);
 		}
    }

    
 private void confDatosValoracionJMenuItemActionPerformed(){
    	
	 if (inventarioInternalFrame==null ) return;
	 ConfiguraDatosValoracion configuraDV= new ConfiguraDatosValoracion(this, true, aplicacion.getI18NResource(), municipio);
     Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
     configuraDV.setLocation(d.width/2 - 500/2, d.height/2 - 500/2);
     configuraDV.setSize(330,200);
     configuraDV.setVisible(true);
    	
    	
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
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        try {
            //com.geopista.security.SecurityManager.logout();
            /** No se realiza desbloqueo porque los dialogos son modales. Si se cierra la ventana com la x,
             * en ese punto el usuario no tiene bloqueado ningun bien de inventario */
           // com.geopista.security.SecurityManager.logout();
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
    }//GEN-LAST:event_exitForm

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
            //setTitle(aplicacion.getI18nString("inventario.app.tag0"));
        	String title=aplicacion.getI18nString("inventario.app.tag0");
			String release=aplicacion.getString("localgis.release");
			if (release==null)
				release="LocalGIS";
			title=title.replaceAll("\\$\\{localgis\\.release\\}",release);				
	        setTitle(title);
            
            if (Constantes.Municipio!=null)
                  setTitle(getTitle()+" - "+Constantes.Municipio + "-"+ConstantesRegistro.IdMunicipio+" ("+Constantes.Provincia+")");

            
            amortizacionMenu.setText(aplicacion.getI18nString("inventario.historicoamortizacion.menu"));
            historicoAmortizacionJMenuItem.setText(aplicacion.getI18nString("inventario.historicoamortizacion.menuitem"));
            
            idiomaMenu.setText(aplicacion.getI18nString("inventario.idioma.tag0"));
            castellanoJMenuItem.setText(aplicacion.getI18nString("inventario.idioma.tag1"));
            euskeraJMenuItem.setText(aplicacion.getI18nString("inventario.idioma.tag2"));
            catalanJMenuItem.setText(aplicacion.getI18nString("inventario.idioma.tag3"));
            gallegoJMenuItem.setText(aplicacion.getI18nString("inventario.idioma.tag4"));
            valencianoJMenuItem.setText(aplicacion.getI18nString("inventario.idioma.tag5"));
            
            toolMenu.setText(aplicacion.getI18nString("inventario.tools.tag0"));
            loadCatalogoJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag1"));
            loadInventarioJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag2"));
            saveInventarioJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag7"));
            saveCatalogoJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag8"));
            initInventarioJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag3"));
            bienePreAltaJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag9"));

            configMenu.setText(aplicacion.getI18nString("inventario.tools.tag4"));

            ayudaMenu.setText("Ayuda");

            
            filtrarEliminadosInventarioJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag5"));
            filtrarBajasInventarioJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag6"));
            confDatosValoracionJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag10"));
            
            eielMenu.setText(aplicacion.getI18nString("inventario.tools.tag11"));
            elementoseielJMenuItem.setText(aplicacion.getI18nString("inventario.tools.tag12"));
            
            menuIncidenciasJMenuItem.setText("Sugerencias/Incidencias");

            
        }catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
    }

    private boolean configureApp() {
        try {
             try{
                    com.geopista.protocol.CConstantesComando.servletLicenciasUrl = aplicacion.getString("geopista.conexion.servidorurl")+ WebAppConstants.LICENCIAS_OBRA_WEBAPP_NAME + ServletConstants.CSERVLETLICENCIAS_SERVLET_NAME;
                    com.geopista.protocol.CConstantesComando.loginLicenciasUrl = aplicacion.getString("geopista.conexion.servidorurl")+ WebAppConstants.INVENTARIO_WEBAPP_NAME;
                    com.geopista.protocol.CConstantesComando.adminCartografiaUrl = aplicacion.getString("geopista.conexion.servidorurl")+ WebAppConstants.GEOPISTA_WEBAPP_NAME;

                   Constantes.Locale= aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");
//                   try{
//                       Constantes.IdEntidad=new Integer(aplicacion.getString("geopista.DefaultEntityId")).intValue();
//                   }catch (Exception e){
//                      JOptionPane.showMessageDialog(this, "Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
//                      System.out.println("Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
//                      logger.error("Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
//                      if (fromInicio)
//                    	  dispose();
//                      else
//                    	  System.exit(-1);
//                   }

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
            setTitle("");
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
            //com.geopista.security.SecurityManager.setHeartBeatTime(1000000);
            //CAMBIADO
           /* com.geopista.security.SecurityManager.setHeartBeatTime(10000);
            GeopistaAcl acl= com.geopista.security.SecurityManager.getPerfil("Inventario");
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
            dialog.setVisible(true);
            return false;
        }
    }

    private void setPolicy() throws AclNotFoundException, Exception{
    	 com.geopista.security.SecurityManager.setHeartBeatTime(10000);
         GeopistaAcl acl= com.geopista.security.SecurityManager.getPerfil("Inventario");
         applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
    }
    
    public void resetSecurityPolicy() {
    }

    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal) {
        try {
            if ((acl == null) || (principal == null) || (acl.getPermissions(principal)==null)) {
                return false;
            }
            //Constantes.principal = principal;
            setPermisos(acl.getPermissions(principal));

            if (tienePermisos("Geopista.Inventario.Login")) {
            }

            //Si tiene permisos, lo dejamos visible al usurario
            if (tienePermisos("Geopista.Inventario.BienesPreAlta.Conversion")) {
            	bienePreAltaJMenuItem.setVisible(true);
            	bienePreAltaJMenuItem.setEnabled(true);
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
        new MainInventario();
    }

    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu idiomaMenu;
  private javax.swing.JMenu amortizacionMenu;
    private javax.swing.JMenu toolMenu;
    private javax.swing.JMenu configMenu;
    private javax.swing.JMenu eielMenu;
    private javax.swing.JMenu ayudaMenu;
    private javax.swing.JMenuItem historicoAmortizacionJMenuItem;
    private javax.swing.JMenuItem loadInventarioJMenuItem;
    private javax.swing.JMenuItem saveInventarioJMenuItem;
    private javax.swing.JMenuItem saveCatalogoJMenuItem;
    private javax.swing.JMenuItem loadCatalogoJMenuItem;
    private javax.swing.JMenuItem initInventarioJMenuItem;
    private javax.swing.JMenuItem bienePreAltaJMenuItem;
    private javax.swing.JMenuItem castellanoJMenuItem;
    private javax.swing.JMenuItem catalanJMenuItem;
    private javax.swing.JMenuItem valencianoJMenuItem;
    private javax.swing.JMenuItem euskeraJMenuItem;
    private javax.swing.JMenuItem gallegoJMenuItem;

    private javax.swing.JCheckBoxMenuItem filtrarEliminadosInventarioJMenuItem;
    private javax.swing.JCheckBoxMenuItem filtrarBajasInventarioJMenuItem;
    private javax.swing.JMenuItem confDatosValoracionJMenuItem;
    private javax.swing.JMenuItem menuIncidenciasJMenuItem;    
    private javax.swing.JMenuItem elementoseielJMenuItem;
}
