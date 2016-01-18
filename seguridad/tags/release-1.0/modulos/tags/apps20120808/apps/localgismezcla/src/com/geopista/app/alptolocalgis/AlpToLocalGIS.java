package com.geopista.app.alptolocalgis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.acl.AclNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.app.alptolocalgis.images.IconLoader;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.importacion.ScreenComponent;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.app.alptolocalgis.beans.ConstantesAlp;
import com.geopista.app.alptolocalgis.panels.AlpToLocalGISPanel;
import com.geopista.app.alptolocalgis.panels.GraphicEditorPanel;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Clase principal del programa. Inicia la aplicacion, cargando los datos necesarios al inicio y controlando el flujo
 * entre las pantallas ayudandose de la clase MaquinaEstadosFlujo. Es la capa de nivel mas alto de la aplicacion.
 * Tambien en esta capa se crea la gui de la pantalla principal. En esta capa se controlan todos los permisos del usuario
 * que esta intentando acceder.
 *
 */

public class AlpToLocalGIS extends CMain implements IMainAlpToLocalGIS{
	
    static Logger logger = Logger.getLogger(AlpToLocalGIS.class);
    private Hashtable permisos= new Hashtable();

    //Atributos del GUI
    private ApplicationContext aplicacion;
    private JDesktopPane desktopPane;
    private JPanel jPanelStatus = null;    
    public static final int DIM_X = 800;
    public static final int DIM_Y = 600;
    public static final Rectangle PICTURE_BORDER = new Rectangle(15, 5, 100, 440);
    public static final String LOCALGIS_LOGO = "geopista.gif";
    
    private HelpBroker hb = null;
	private Blackboard blackboard = null;

    
    public AlpToLocalGIS() {
    	
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        aplicacion= (AppContext)AppContext.getApplicationContext();
        aplicacion.setMainFrame(this);
        blackboard = aplicacion.getBlackboard();
        try
        {
            initLookAndFeel();
        }
        catch (Exception e) {}
        SplashWindow splashWindow = showSplash();
        //Cotesa
        Locale loc= I18N.getLocaleAsObject();
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.alptolocalgis.languages.AlpToLocalGISi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("AlpToLocalGIS",bundle);

        inicializaElementos();                
        configureApp();
        
        this.setIconImage(IconLoader.icon(LOCALGIS_LOGO).getImage());
        this.setTitle(I18N.get("AlpToLocalGIS","alptolocalgis.frame.title"));
        try
        {
			show();
            /** deshabilitamos la barra de tareas */

            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (splashWindow != null)
            {
                splashWindow.setVisible(false);
            }
			
			//--------NUEVO-------------->	
			if(SSOAuthManager.isSSOActive())
				CConstantesComando.loginAlpUrl = AppContext.getApplicationContext().getString(SSOAuthManager.SSO_SERVER_URL);
			SSOAuthManager.ssoAuthManager(ConstantesAlp.idApp);								
            if (!AppContext.getApplicationContext().isOnlyLogged()){  
            //-------FIN-NUEVO----------->
            	showAuth();
            //--------NUEVO-------------->
            }
            //-------FIN-NUEVO----------->  
            
            setPolity();
            
            AppContext.seleccionarMunicipio((Frame)this);
            ConstantesAlp.IdMunicipio = AppContext.getIdMunicipio();

            /** cargamos la provincia y el municipio */
            Municipio municipio = (new OperacionesAdministrador(com.geopista.protocol.CConstantesComando.loginAlpUrl)).getMunicipio(new Integer(ConstantesAlp.IdMunicipio).toString());
            if (municipio!=null)
            {
            	ConstantesAlp.Municipio = municipio.getNombre();
                ConstantesAlp.Provincia= municipio.getProvincia();
             
                com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
                if(principal!=null)
                	setTitle(getTitle()+ " - " + ConstantesAlp.Municipio + " (" + ConstantesAlp.Provincia+") - " + I18N.get("AlpToLocalGIS","alptolocalgis.cauthdialog.jlabelnombre") + principal);
               	com.geopista.security.SecurityManager.setIdMunicipio(new Integer(ConstantesAlp.IdMunicipio).toString());
            }

            /** dialogo de espera de carga */
            final JFrame desktop= (JFrame)this;
            final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
            progressDialog.setTitle("TaskMonitorDialog.Wait");
            progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {        
                            	String idUser = ConstantesAlp.clienteAlp.inicializaBlackBoard();
                                aplicacion.getBlackboard().put("idUserAlp", idUser);
                            	getContentPane().add(getJPanelAlpToLocalGIS(), java.awt.BorderLayout.CENTER);                  
                            }
                            catch(Exception e)
                            {
                                logger.error("Error ", e);
                                ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                return;
                            }
                            finally
                            {
                                progressDialog.setVisible(false);                                
                                progressDialog.dispose();
                            }
                        }
                  }).start();
              }
           });
           GUIUtil.centreOnWindow(progressDialog);
           progressDialog.setVisible(true);
                    
           show();
                    
           ((GraphicEditorPanel)((AlpToLocalGISPanel)getJPanelAlpToLocalGIS()).getJPanelGraphicEditor()).initEditor();          
           this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                  
        }
        catch (Exception e)
        {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("ERROR:" + sw.toString());
			logger.error("Exception: " + sw.toString());
		}
        
        
        
    }

    /**
     * Funcion que inicializa la gui de la pantalla principal, asociandole eventos para que tenga un tamaño minimo y
     * la accion de cierre.
     */
    private void inicializaElementos()
    {
        //Creacion de objetos.
        getContentPane().add(getJPanelStatus(), BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

        //Evento para que la ventana al minimizar tenga un tamaño minimo.
        addComponentListener(new java.awt.event.ComponentAdapter()
        {
          public void componentResized(ComponentEvent e)
          {
                UtilRegistroExp.ajustaVentana(aplicacion.getMainFrame(), DIM_X, DIM_Y+50);
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
    
    private JMenuItem logMenuItem = null;
	private JPanel jPanelAlpToLocalGIS = null;
    
    /**
     * This method initializes logMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getLogMenuItem()
    {
        if (logMenuItem == null)
        {
            logMenuItem = new JMenuItem();
            logMenuItem.setEnabled(aplicacion.isOnline());
            logMenuItem.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        if (aplicacion.isLogged())
                            aplicacion.logout();
                        else
                            aplicacion.login();
                    }
                });
        }
        return logMenuItem;
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
					mostrarAyudaActionPerformed();
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
    
    private JPanel getJPanelAlpToLocalGIS(){
    	
    	if (jPanelAlpToLocalGIS  == null){
    		
    		jPanelAlpToLocalGIS = new AlpToLocalGISPanel();
    	}
    	return jPanelAlpToLocalGIS;
    }
    
    private JLabel connectionLabel = new JLabel();
    
    private JLabel helpLabel = new JLabel();

   
	public boolean mostrarAyudaActionPerformed()
    {
		if (hb == null){
			HelpSet hs = null;
			ClassLoader loader = this.getClass().getClassLoader();
			URL url;
			try
			{
				String helpSetFile = "help/alptolocalgis/AlpToLocalGISHelp_" + ConstantesAlp.Locale.substring(0,2) + ".hs";
				url = HelpSet.findHelpSet(loader, helpSetFile);
				if (url == null)
				{
					url=new URL("help/alptolocalgis/AlpToLocalGISHelp_" + ConstantesAlp.LocalCastellano.substring(0,2) + ".hs");
				}
				hs = new HelpSet(loader, url);

				// ayuda sensible al contexto
				hs.setHomeID(ConstantesAlp.helpSetHomeID);


			} catch (Exception ex)
			{
				logger.error("Exception: " + ex.toString());

				return false;
			}
			hb = hs.createHelpBroker();

			hb.setDisplayed(true);
			new CSH.DisplayHelpFromSource(hb);
		}
		else{
			hb.setDisplayed(true);
		}
		return true;
	}

    /**
     * Muestra un dialogo con el mensaje de advertencia msg pasado por parametro.
     */
    private void mostrarMensajeDialogo(String msg)
    {
        JOptionPane.showMessageDialog(this, msg);
    }

    protected void openComponent (JComponent jComp, boolean resize, boolean unica)
    {
        openComponent(jComp, null, resize, unica);
    }

    protected void openComponent(JComponent jComp, JPanel p, boolean resize, boolean unica)
    {
        if (aplicacion.isOnline())
        {
            ScreenComponent sc = new ScreenComponent();
            sc.addComponent(jComp);
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(sc,null);
            sc.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {

                    String command = e.getActionCommand();
                    if ("finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                    }
                }
            });
            if (p!=null)
                sc.setFillerPanel(p);

            llamadaAInternalFrame(JIFrame, resize, unica);
        }
        else
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
    }

    protected void openComponent2(JComponent jComp, JPanel p, boolean resize, boolean unica)
    {
        if (aplicacion.isOnline())
        {
            final ScreenComponent sc = new ScreenComponent();
            sc.addComponent(jComp);
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(sc,null);
            sc.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {

                    String command = e.getActionCommand();
                    if ("finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                        //Eliminacion de la informacion
                        JPanel panelInterno=(JPanel)sc.getComponentFromPanel();
                        if (panelInterno instanceof GestionExpedientePanel){
                        	GestionExpedientePanel gep=(GestionExpedientePanel)panelInterno;
                        	gep.cleanup();
                        }                                                                   
                    }
                }
            });
            if (p!=null)
                sc.setFillerPanel(p);

            llamadaAInternalFrame(JIFrame, resize, unica);
        }
        else
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
    }    
    
    
    protected void openModalComponent(String title, JPanel panel)
    {
        if (aplicacion.isOnline())
        {
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(panel,title);
            ((ScreenComponent)panel).addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                    }
                }
            });
            llamadaAInternalFrame(JIFrame, false, false);
            
            //Trata de poner por delante la pantalla que se acaba de lanzar
            try{
                if (desktopPane.getAllFrames().length!=0 &&
                        desktopPane.getAllFrames()[desktopPane.getAllFrames().length-1] 
                                                   instanceof JInternalFrame)
                {
                    ((JInternalFrame)desktopPane.getAllFrames()
                            [desktopPane.getAllFrames().length-1]).setSelected(true);                       
                    repaint();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
        else{
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
                    aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }

    protected void openWizard(WizardPanel[] wp, boolean resize)
    {
        if (aplicacion.isOnline()){
            WizardComponent d = new WizardComponent(aplicacion, "", null);
            d.init(wp);

            //Elimina el panel blanco con título que aparece en la zona superior de la pantalla
            d.setWhiteBorder(false);
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(d,null);
            d.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {

                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                    }
                }
            });

            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            llamadaAInternalFrame(JIFrame, false, true);
        }
        else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }
       

    protected void openModalWizard(String title, WizardPanel[] wp)
    {
        if (aplicacion.isOnline())
        {
            WizardComponent d = new WizardComponent(aplicacion, "", null);
            d.init(wp);

            //Elimina el panel blanco con título que aparece en la zona superior de la pantalla
            d.setWhiteBorder(false);
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(d,title);
            ((WizardComponent)d).addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e){

                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                    }
                }
            });
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            llamadaAInternalFrame(JIFrame, false, true);
        }
        else{
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }

    private JInternalFrame encapsulaEnJInternalFrame(Component comp, String title)
    {
        final JInternalFrame JIFrame = new JInternalFrame();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        UtilRegistroExp.menuBarSetEnabled(false, this);
        if(title!=null)
        {
            JIFrame.setTitle(title);
        }
        //JIFrame.getContentPane().add(comp);
        JScrollPane internalFrameScrollPane = new JScrollPane();
        internalFrameScrollPane.setViewportView(comp);
        JIFrame.getContentPane().add(internalFrameScrollPane);
        JIFrame.addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
                cierraInternalFrame(JIFrame);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });
        JIFrame.setClosable(true);
        JIFrame.pack();
        JIFrame.setVisible(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return JIFrame;
    }

    private void cierraInternalFrame(JInternalFrame JIFrame)
    {
        if(desktopPane.getAllFrames().length>0)
        {
            desktopPane.getDesktopManager().closeFrame(JIFrame);
            desktopPane.repaint();
        }
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);        
        UtilRegistroExp.menuBarSetEnabled(true, this);
    }

    /**
     * Funcion que muestra la JInternalFrame pasada como parametro. En caso de que el valor de isMaxTam sea true la
     * internalFrame sera maximizada, sino no. En caso de que el valor unica sea true, se comprueba si hay otra ventana
     * abierta, si es asi no hace nada, sino muestra la internalFrame pasada por parametro. Si el valor de unica es false
     * se muestra aunque haya otra ventana.
     */
    private void  llamadaAInternalFrame(JInternalFrame iFrame, boolean isMaxTam, boolean unica)
    {
		try
        {
            desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			int numInternalFrames = desktopPane.getAllFrames().length;
            if (numInternalFrames == 0 || !unica)
            {
                iFrame.setFrameIcon(new javax.swing.ImageIcon(IconLoader.icon(LOCALGIS_LOGO).getImage()));
                desktopPane.add(iFrame);
                if(isMaxTam)
                {
                    desktopPane.getDesktopManager().maximizeFrame(iFrame);
                }
                iFrame.show();                   
			}
			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
        catch (Exception ex)
        {
		}
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
            
               com.geopista.protocol.CConstantesComando.loginAlpUrl = aplicacion.getString("geopista.conexion.servidorurl")+"alptolocalgis";
               ConstantesAlp.clienteAlp = new AlpClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                "/AlpServlet");

//            	Constantes.url= aplicacion.getString("geopista.conexion.servidorurl")+"administracion";
            	ConstantesAlp.Locale = aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");

//               try
//               {
//                   ConstantesAlp.IdMunicipio=new Integer(aplicacion.getString("geopista.DefaultCityId")).intValue();
//                   ConstantesAlp.IdEntidad=new Integer(aplicacion.getString("geopista.DefaultEntityId")).intValue();
//               }
//               catch (Exception e)
//               {
//                   mostrarMensajeDialogo( "Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
//                   mostrarMensajeDialogo( "Valor de id entidad no valido:"+e.toString()+aplicacion.getString("geopista.DefaultEntityId"));
//                   logger.error("Valor de id entidad no valido:"+e.toString()+aplicacion.getString("geopista.DefaultEntityId"));
//                   System.out.println("Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
//                   logger.error("Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
//                   System.exit(-1);
//               }
            }
            catch(Exception e)
            {
                	StringWriter sw = new StringWriter();
			        PrintWriter pw = new PrintWriter(sw);
			        e.printStackTrace(pw);
                    mostrarMensajeDialogo( "Excepcion al cargar el fichero de configuración:\n"+sw.toString());
			        logger.error("Exception: " + sw.toString());
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
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}
   
    /**
     * Comprueba que el usuario esta autorizado para acceder a la aplicacion y sus permisos para las diferentes
     *  acciones.
     */
    private boolean showAuth()
    {
        try
        {
        	if (!aplicacion.isLogged()){
        		CAuthDialog auth = new CAuthDialog(this, true,
        				CConstantesComando.loginAlpUrl,ConstantesAlp.idApp,
        				ConstantesAlp.IdEntidad, aplicacion.getI18NResource());
        	auth.setBounds(30, 60, 315, 155);
        	//auth.add(new JLabel("Se ha restablecido la conexión. \nIntroducir login y password de usuario"));
        	auth.show();
        	}

        	int cont = 0;
            while(!aplicacion.isLogged() && cont<=3)
            {                     
                cont++;
                if (cont>3)
                {              
                    System.exit(0);  
                }            
                aplicacion.login(); 
            }
            //com.geopista.security.SecurityManager.setHeartBeatTime(1000000);
            //CAMBIADO
            /*
            com.geopista.security.SecurityManager.setHeartBeatTime(5000);
            GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("ALP");
            if (!applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal())){
            	showAuth();
            }*/
            setPolity();
        }
        catch(Exception e)
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
    private void setPolity() throws AclNotFoundException, Exception{
    	com.geopista.security.SecurityManager.setHeartBeatTime(5000);
        GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("ALP");
        if (!applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal())){
        	showAuth();
        }
    }

    /**
     * Comprueba los permisos del usuario.
     */
    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal)
    {
		try
        {
            if ((acl == null) || (principal == null))
            {
            	return false;
			}
			ConstantesAlp.principal = principal;
            setPermisos(acl.getPermissions(principal));
            if (!tienePermisos("LocalGis.ALP.Login"))
            {
               return false;
            }
            
            return true;
		}
        catch (Exception ex)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
			return false;
		}

	}

    /**
     * Crea hash de permisos del usuario.
     */
    private void setPermisos(Enumeration e)
    {
        permisos= new Hashtable();
        while (e.hasMoreElements())
        {
            GeopistaPermission geopistaPermission = (GeopistaPermission) e.nextElement();
            String permissionName = geopistaPermission.getName();
            if (!permisos.containsKey(permissionName))
            {
                permisos.put(permissionName, "");
            }
        }
        ConstantesAlp.permisos = permisos;
    }

    /**
     * Funcion que comprueba si el usuario tiene permisos para una tarea en especial.
     */
    private boolean tienePermisos(String permiso)
    {
        if (permisos.containsKey(permiso))
        {
            return true;
        }
        return false;
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
            if(!SSOAuthManager.isSSOActive()) 
         	   com.geopista.security.SecurityManager.logout();
            //---FIN NUEVO--->
        }
        catch (Exception ex)
        {
            logger.warn("Exception: " + ex.toString());
        }
        System.exit(0);
    }



    /*Main de la aplicacion*/

    public static void main(String args[])
    {
         AlpToLocalGIS aux = new AlpToLocalGIS();
    }
}
