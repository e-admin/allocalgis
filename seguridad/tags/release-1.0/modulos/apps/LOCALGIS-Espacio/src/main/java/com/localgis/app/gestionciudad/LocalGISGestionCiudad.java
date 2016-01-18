package com.localgis.app.gestionciudad;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.app.administrador.dominios.CDominiosFrame;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.utilidades.CMain;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.localgis.app.gestionciudad.dialogs.main.CAcercaDeJDialog;
import com.localgis.app.gestionciudad.dialogs.main.EditorPanel;
import com.localgis.app.gestionciudad.dialogs.main.GeopistaMapPanel;
import com.localgis.app.gestionciudad.dialogs.main.ObraCivilEditorPanel;
import com.localgis.app.gestionciudad.dialogs.main.ObraCivilPrintPanel;
import com.localgis.app.gestionciudad.dialogs.main.PrintEditor;
import com.localgis.app.gestionciudad.dialogs.main.ShowMapPanel;
import com.localgis.app.gestionciudad.dialogs.statistics.signs.SignsDialog;
import com.localgis.app.gestionciudad.expiredactuations.ExpiredActuationsThread;
import com.localgis.app.gestionciudad.images.IconLoader;
import com.localgis.app.gestionciudad.utils.LocalGISObraCivilUtils;
import com.localgis.app.gestionciudad.webservicesclient.wrapper.WSInterventionsWrapper;
import com.localgis.webservices.civilwork.client.CivilWorkWSStub.StatisticalDataOT;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * @author javieraragon
 *
 */
public class LocalGISGestionCiudad extends CMain{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -8767857741197660844L;
	
	static Logger logger = Logger.getLogger(LocalGISGestionCiudad.class);
	private Hashtable<String, String> permisos= new Hashtable<String, String>();

	//Atributos del GUI
	private ApplicationContext aplicacion;
	private JDesktopPane desktopPane;
	private JPanel jPanelStatus = null;    
	public static final int DIM_X = 800;
	public static final int DIM_Y = 600;
	public static final Rectangle PICTURE_BORDER = new Rectangle(15, 5, 100, 440);
	public static final String LOCALGIS_LOGO = "geopista.gif";
	private HelpBroker hb = null;

	private JLabel connectionLabel = new JLabel();
	private JLabel helpLabel = new JLabel();
	private JMenuBar jMenuBarEIEL = null;
	private JMenu jMenuMapasEIEL = null;
	private JMenu jMenuDominios = null;
	private JMenu jMenuEstadisticas = null;
	private JMenu jMenuAyuda = null;
	private JMenuItem jMenuItemCargarMapaTematicoViviendas = null;
	private JMenuItem contenidoAyudaMenuItem = null;
	private JMenuItem jMenuItemAcercaDe = null;;
	private JMenuItem jMenuItemEditorDominiosEIEL = null;
	private JMenuItem jMenuItemIndicadores = null;


	@SuppressWarnings({ "unchecked", "deprecation" })
	public LocalGISGestionCiudad() {

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		aplicacion= (AppContext)AppContext.getApplicationContext();
		aplicacion.setMainFrame(this);
		try
		{
			initLookAndFeel();
		}
		catch (Exception e) {}

		SplashWindow splashWindow = showSplash();

		Locale loc= I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.localgis.app.gestionciudad.language.LocalGISOBRACIVILi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("LocalGISObraCivil", bundle);

		inicializaElementos();                
		configureApp();

		this.setIconImage(IconLoader.icon(LOCALGIS_LOGO).getImage());
		this.setTitle(I18N.get("LocalGISObraCivil","localgisobracivil.frame.title"));
		try
		{
					
			show();

			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (splashWindow != null)
			{
				splashWindow.setVisible(false);
			}
			
			LocalGISObraCivilUtils.menuBarSetEnabled(false, this);
			showAuth();
			
			AppContext.seleccionarMunicipio((Frame)this);

			LocalGISObraCivilUtils.menuBarSetEnabled(true, this);
			
			ExpiredActuationsThread thread = new ExpiredActuationsThread("Actuaciones Caducadas");
			thread.start();


//			WSInterventionsWrapper.getNumInterventions(AppContext.getApplicationContext().getUserPreference(AppContext.USER_LOGIN, "", true), AppContext.getIdMunicipio());
			// Cargamos la provincia y el municipio 
			//TODO Error al buscar municipio
//			Municipio municipio = (new OperacionesAdministrador(ConstantessLocalGISObraCivil.localgisObraCivil)).getMunicipio(new Integer(ConstantessLocalGISObraCivil.IDMunicipioCompleto).toString());
//			if (municipio!=null)
//			{
//				ConstantessLocalGISObraCivil.Municipio = municipio.getNombre();
//				ConstantessLocalGISObraCivil.Provincia= municipio.getProvincia();
//				ConstantessLocalGISObraCivil.idMunicipio = municipio.getId();
//				ConstantessLocalGISObraCivil.idProvincia= municipio.getIdprovincia();
//
//				com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
//				if(principal!=null)
//					setTitle(getTitle()+ " - " + ConstantessLocalGISObraCivil.Municipio + " (" + ConstantessLocalGISObraCivil.Provincia+") - " + I18N.get("LocalGISEIEL","localgiseiel.cauthdialog.jlabelnombre") + principal);
//			}

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

		desktopPane= new JDesktopPane();
		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);	        

		//Creacion de objetos.
		getContentPane().add(getJPanelStatus(), BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		setJMenuBar(getJMenuBarLocalGISEspacioPublico());

		//Evento para que la ventana al minimizar tenga un tamaño minimo.
		addComponentListener(new java.awt.event.ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				LocalGISObraCivilUtils.ajustaVentana(aplicacion.getMainFrame(), DIM_X, DIM_Y+50);
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
	private JMenu jMenuEdicionEIEL = null;
	private JMenuItem jMenuItemEdicionEIEL = null;
	private EditorPanel jPanelEditorEIEL = null;
	private PrintEditor jPanelPrintEditorEIEL = null;
	private ObraCivilEditorPanel jPanelEIEL = null;
	private ObraCivilPrintPanel jPanelPrintGestionCiudad = null;
	private ShowMapPanel jPanelMap = null;

	
//	private JMenuItem jMenuItemCargarMapaProvincialEIEL = null;
//	private JMenuItem jMenuItemCargarMapaComarcalEIEL = null;
//	private JMenuItem jMenuItemCargarMapaMunicipalEIEL = null;
//	private JMenuItem jMenuItemCargarMapaInfraestructurasEIEL = null;
//	private JMenuItem jMenuItemCargarMapaTematicoPlaneamiento = null;
//	private JMenuItem jMenuItemCargarMapaTematicoDepositos1 = null;
//	private JMenuItem jMenuItemCargarMapaTematicoDepositos2 = null;
//	private JMenuItem jMenuItemCargarMapaTematicoPavimentacion1 = null;
//	private JMenuItem jMenuItemCargarMapaTematicoPavimentacion2 = null;
//	private JMenuItem jMenuItemCargarMapaTematicoAlumbrado = null;
//	private JMenuItem jMenuItemCargarMapaTematicoCaptaciones = null;
//	private JMenuItem jMenuItemCargarMapaTematicoPotabilizacion = null;
//	private JMenuItem jMenuItemCargarMapaTematicoDepositosSint = null;
//	private JMenuItem jMenuItemCargarMapaTematicoDistribucion = null;
//	private JMenuItem jMenuItemCargarMapaTematicoSaneamiento = null;
//	private JMenuItem jMenuItemCargarMapaTematicoDepuracion = null;
//	private JMenuItem jMenuItemCargarMapaTematicoPavimentacionSint = null;
//	private JMenuItem jMenuItemCargarMapaTematicoAlumbradoSint = null;
//	private JMenuItem jMenuItemCargarMapaTematicoBasuras = null;
//	private JMenuItem jMenuItemCargarMapaTematicoResiduos = null;
//	private JMenuItem jMenuItemCargarMapaTematicoCultura = null;
//	private JMenuItem jMenuItemCargarMapaTematicoDeportes = null;
//	private JMenuItem jMenuItemCargarMapaTematicoZonasVerdes = null;
//	private JMenuItem jMenuItemCargarMapaTematicoAdministrativo = null;
//	private JMenuItem jMenuItemCargarMapaTematicoGlobal = null;
//	private JMenuItem jMenuItemCargarMapaTematicoCentrosSanitarios1 = null;
//	private JMenuItem jMenuItemCargarMapaTematicoCentrosSanitarios2 = null;
//	private JMenuItem jMenuItemCargarMapaTematicoCentrosSanitarios3 = null;
//	private JMenuItem jMenuItemCalcularIndices;
//	private JMenu jMenuMapasMunicipales;
//	private JMenu jMenuMapasComarcales;
//	private JMenuItem jMenuItemCargarMapaComarcalViviendas;
//	private JMenuItem jMenuItemCargarMapaComarcalPlaneamiento;
//	private JMenuItem jMenuItemCargarMapaComarcalDepositos1;
//	private JMenuItem jMenuItemCargarMapaComarcalDepositos2;
//	private JMenuItem jMenuItemCargarMapaComarcalPavimentacion1;
//	private JMenuItem jMenuItemCargarMapaComarcalPavimentacion2;
//	private JMenuItem jMenuItemCargarMapaComarcalAlumbrado;
//	private JMenuItem jMenuItemCargarMapaComarcalCentrosSanitarios1;
//	private JMenuItem jMenuItemCargarMapaComarcalCentrosSanitarios2;
//	private JMenuItem jMenuItemCargarMapaComarcalPotabilizacion;
//	private JMenuItem jMenuItemCargarMapaComarcalCaptaciones;
//	private JMenuItem jMenuItemCargarMapaComarcalDepositosSint;
//	private JMenuItem jMenuItemCargarMapaComarcalDistribucion;
//	private JMenuItem jMenuItemCargarMapaComarcalSaneamiento;
//	private JMenuItem jMenuItemCargarMapaComarcalDepuracion;
//	private JMenuItem jMenuItemCargarMapaComarcalPavimentacionSint;
//	private JMenuItem jMenuItemCargarMapaComarcalAlumbradoSint;
//	private JMenuItem jMenuItemCargarMapaComarcalBasuras;
//	private JMenuItem jMenuItemCargarMapaComarcalResiduos;
//	private JMenuItem jMenuItemCargarMapaComarcalCultura;
//	private JMenuItem jMenuItemCargarMapaComarcalDeportes;
//	private JMenuItem jMenuItemCargarMapaComarcalZonasVerdes;
//	private JMenuItem jMenuItemCargarMapaComarcalAdministrativo;
//	private JMenuItem jMenuItemCargarMapaComarcalGlobal;
//	private JMenu jMenuMapasObras;
//	private JMenuItem jMenuItemCargarMapaObrasInversion;
//	private JMenuItem jMenuItemCargarMapaObrasHabitantes;
//	private JMenuItem jMenuItemCargarMapaObrasTotal;
//	private JMenuItem jMenuItemCalcularObras;
//	private JMenu jMenuMapasObrasMunicipales;
//	private JMenu jMenuMapasObrasComarcales;
//	private JMenuItem jMenuItemCargarMapaComarcalObrasInversion;
//	private JMenuItem jMenuItemCargarMapaComarcalObrasHabitantes;
//	private JMenuItem jMenuItemCargarMapaComarcalObrasTotal;

	/**
	 * This method initializes logMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	@SuppressWarnings("unused")
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

	@SuppressWarnings({"null","unused"})
	public boolean mostrarAyudaActionPerformed()
	{

		if (hb == null){
			try
			{
				HelpSet hs = null;
				ClassLoader loader = this.getClass().getClassLoader();
				URL url;

				//				String helpSetFile = "help/eiel/LocalGISEIELHelp_" + ConstantesLocalGISEIEL.Locale.substring(0,2) + ".hs";
				//				url = HelpSet.findHelpSet(loader, helpSetFile);
				//				if (url == null)
				//				{
				//				url=new URL("help/eiel/LocalGISEIELHelp_" + ConstantesLocalGISEIEL.LocalCastellano.substring(0,2) + ".hs");
				//				}
				//				hs = new HelpSet(loader, url);

				// ayuda sensible al contexto
				//				hs.setHomeID(ConstantesLocalGISEIEL.helpSetHomeID);

				hb = hs.createHelpBroker();

				hb.setDisplayed(true);
				new CSH.DisplayHelpFromSource(hb);

			} catch (Exception ex)
			{
				logger.error("Exception: " + ex.toString());

				return false;
			}

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


	protected void openWizard(WizardPanel[] wp, boolean resize, boolean isAdjustable)
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

			Dimension dim = null;
			if (wp[0] instanceof JComponent)
			{
				dim = ((JComponent)wp[0]).getSize();
				if (dim.getHeight()==0 && dim.getWidth()==0)
					dim = null;
			}

			llamadaAInternalFrame(JIFrame, resize, true, isAdjustable, dim);
			if (dim!=null)
				JIFrame.setSize(dim);
		}
		else
		{
			JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
		}
	}

	private JInternalFrame encapsulaEnJInternalFrame(Component comp, String title)
	{
		final JInternalFrame JIFrame = new JInternalFrame();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		LocalGISObraCivilUtils.menuBarSetEnabled(false, this);
		this.getJMenuEstadisticas().setEnabled(true);
		if(title!=null)
		{
			JIFrame.setTitle(title);
		}
		JScrollPane internalFrameScrollPane = new JScrollPane();
		internalFrameScrollPane.setViewportView(comp);
		JIFrame.getContentPane().add(comp);
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
		JIFrame.setResizable(true);
		JIFrame.pack();
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

		LocalGISObraCivilUtils.menuBarSetEnabled(true, this);
	}

	/**
	 * Funcion que muestra la JInternalFrame pasada como parametro. En caso de que el valor de isMaxTam sea true la
	 * internalFrame sera maximizada, y si no, no. En caso de que el valor unica sea true, se comprueba si hay otra ventana
	 * abierta, si es asi no hace nada, sino muestra la internalFrame pasada por parametro. Si el valor de unica es false
	 * se muestra aunque haya otra ventana.
	 */
	private void  llamadaAInternalFrame(JInternalFrame internalFrame, boolean isMaxTam, boolean unica, boolean isAdjustable)
	{
		llamadaAInternalFrame(internalFrame, isMaxTam, unica, isAdjustable, null);
	}


	private void  llamadaAInternalFrame(JInternalFrame internalFrame, boolean isMaxTam, boolean unica, boolean isAdjustable, Dimension dim)
	{
		try
		{
			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			int numInternalFrames = desktopPane.getAllFrames().length;
			if (numInternalFrames == 0 || !unica)
			{
				internalFrame.setFrameIcon(new javax.swing.ImageIcon(IconLoader.icon(LOCALGIS_LOGO).getImage()));
				desktopPane.add(internalFrame);
				if(isMaxTam)
				{
					desktopPane.getDesktopManager().maximizeFrame(internalFrame);
				}

				internalFrame.setClosable(true);
				internalFrame.setMaximum(isAdjustable);

				if (dim!=null && isMaxTam)
				{
					internalFrame.setSize(dim);
				}

				internalFrame.show();

			}
			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
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

//				ConstantessLocalGISObraCivil.localgisObraCivil = aplicacion.getString("geopista.conexion.servidorurl")+"eiel";
//				ConstantesLocalGISEIEL.clienteLocalGISEIEL = new LocalGISEIELClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
//				"/EIELServlet");
				//-------NUEVO----------------->
				if(Boolean.valueOf(AppContext.getApplicationContext().getString(SSOAuthManager.SSO_AUTH_ACTIVE)))
					Constantes.url = AppContext.getApplicationContext().getString(SSOAuthManager.SSO_SERVER_URL);
				else
					Constantes.url= aplicacion.getString("geopista.conexion.servidorurl")+"administracion";
		    	//-------FIN-NUEVO------------->
//				ConstantesLocalGISEIEL.Locale = aplicacion.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES");

				try
				{
					Constantes.idMunicipio = ConstantessLocalGISObraCivil.IDMunicipioCompleto = 
						new Integer(aplicacion.getString("geopista.DefaultCityId")).intValue();
				}
				catch (Exception e)
				{
					mostrarMensajeDialogo( "Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
					System.out.println("Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
					logger.error("Valor de id municipio no valido:"+e.toString()+aplicacion.getString("geopista.DefaultCityId"));
					System.exit(-1);
				}
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
	@SuppressWarnings("deprecation")
	private boolean showAuth()
	{
		try
		{
		
			if (!aplicacion.isOnlyLogged()){
//				CAuthDialog auth = new CAuthDialog(this, true,
//						ConstantesLocalGISEIEL.localgisEIEL,ConstantesLocalGISEIEL.idApp,
//						ConstantesLocalGISEIEL.IdMunicipio, aplicacion.getI18NResource());
//				auth.setBounds(30, 60, 315, 155);
//				auth.show();
				aplicacion.login();
			}

			int cont = 0;
			while(!aplicacion.isOnlyLogged() && cont<=3)
			{                     
				cont++;
				if (cont>3)
				{              
					System.exit(0);  
				}            
				aplicacion.login(); 
			}
			com.geopista.security.SecurityManager.setHeartBeatTime(5000);
//			GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("EIEL");
//			if (!applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal())){
//				showAuth();
//			}
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

	/**
	 * Comprueba los permisos del usuario.
	 */
	@SuppressWarnings("unchecked")
	public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal)
	{
		try
		{
			if ((acl == null) || (principal == null))
			{
				return false;
			}
			ConstantessLocalGISObraCivil.principal = principal;
			setPermisos(acl.getPermissions(principal));
			if (!tienePermisos("LocalGis.EIEL.Login"))
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
	private void setPermisos(Enumeration<GeopistaPermission> e)
	{
		permisos= new Hashtable<String, String>();
		while (e.hasMoreElements())
		{
			GeopistaPermission geopistaPermission = (GeopistaPermission) e.nextElement();
			String permissionName = geopistaPermission.getName();
			if (!permisos.containsKey(permissionName))
			{
				permisos.put(permissionName, "");
			}
		}
		ConstantessLocalGISObraCivil.permisos = permisos;
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
        	if(!Boolean.valueOf(AppContext.getApplicationContext().getString(SSOAuthManager.SSO_AUTH_ACTIVE)))
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
	@SuppressWarnings("unused")
	public static void main(String args[])
	{
		LocalGISGestionCiudad aux = new LocalGISGestionCiudad();
	}


	/**
	 * Funcion que inicializa el menu principal de la aplicacion.
	 */
	private JMenuBar getJMenuBarLocalGISEspacioPublico()
	{
		if(jMenuBarEIEL==null)
		{
			jMenuBarEIEL = new JMenuBar();

			jMenuBarEIEL.add(getJMenuEdicionEIEL());
			jMenuBarEIEL.add(getJMenuMapasEIEL());
//			jMenuBarEIEL.add(getJMenuMapasTematicos());
//			jMenuBarEIEL.add(getJMenuDominios());	
			jMenuBarEIEL.add(getJMenuEstadisticas());
			jMenuBarEIEL.add(getJMenuAyuda());
			jMenuBarEIEL.setBorderPainted(false);
			
		}
		return jMenuBarEIEL;
	}

	private JMenu getJMenuAyuda()
	{
		if (jMenuAyuda == null)
		{
			jMenuAyuda = new JMenu();
			jMenuAyuda.setText(I18N.get("LocalGISObraCivil","localgisgestionciudad.menu.ayuda"));
			jMenuAyuda.add(getJMenuItemAyuda());
			jMenuAyuda.add(getJMenuItemAcercaDe());
		}
		return jMenuAyuda;
	}
	
	private JMenu getJMenuEstadisticas()
	{
		if (jMenuEstadisticas == null)
		{
			jMenuEstadisticas = new JMenu();
			jMenuEstadisticas.setText(I18N.get("LocalGISObraCivil","localgisgestionciudad.menu.estaditicas"));
			jMenuEstadisticas.add(getJMenuItemIndicadores());
		}
		return jMenuEstadisticas;
	}

	private JMenuItem getJMenuItemAyuda()
	{
		if (contenidoAyudaMenuItem == null)
		{
			contenidoAyudaMenuItem = new JMenuItem(I18N.get("LocalGISObraCivil", "localgisgestionciudad.submenu.contenidoayuda"));
			contenidoAyudaMenuItem.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent evt)
				{
					mostrarAyudaActionPerformed();
				}
			});
		}
		return contenidoAyudaMenuItem;
	}

	private JMenuItem getJMenuItemAcercaDe()
	{
		if (jMenuItemAcercaDe == null)
		{
			jMenuItemAcercaDe = new JMenuItem(I18N.get("LocalGISObraCivil","localgisgestionciudad.submenu.acercade"));
			jMenuItemAcercaDe.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent evt){
					aboutJMenuItemActionPerformed();
				}
			});
		}
		return jMenuItemAcercaDe;
	}

	@SuppressWarnings("deprecation")
	private void aboutJMenuItemActionPerformed() {

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
	}

	/**
	 * This method initializes jMenuImportacion
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuMapasEIEL()
	{
		if (jMenuMapasEIEL == null)
		{
			jMenuMapasEIEL = new JMenu();
			jMenuMapasEIEL.setText(I18N.get("LocalGISObraCivil","localgisgestionciudad.menu.maps"));
			jMenuMapasEIEL.add(getJMenuImprimirMapa());
			

		}
		return jMenuMapasEIEL;
	}
	
	private JMenuItem getJMenuImprimirMapa(){
		
		JMenuItem imprMenuItem = new JMenuItem(I18N.get("LocalGISObraCivil","localgisgestionciudad.submenu.imprimirmapa"));
		imprMenuItem.setEnabled(true);
		
		imprMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showImprimirMainPanel();
				
				int idMapa = ConstantessLocalGISObraCivil.MAPA_GESTIONESPACIOPUBLICO;
				LocalGISObraCivilUtils.setMapToReload(idMapa, true);

			}
		});
		
		return imprMenuItem;
	}

	private JMenu getJMenuEdicionEIEL()
	{
		if (jMenuEdicionEIEL == null)
		{
			jMenuEdicionEIEL  = new JMenu();
			jMenuEdicionEIEL.setText(I18N.get("LocalGISObraCivil","localgisobracivil.menu.adminadviseslabel"));
			jMenuEdicionEIEL.add(getJMenuItemEdicionEIEL());

		}
		return jMenuEdicionEIEL;
	}


	/**
	 * This method initializes jMenuDominios
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuDominios()
	{
		if (jMenuDominios == null)
		{
			jMenuDominios = new JMenu();
			jMenuDominios.setText(I18N.get("LocalGISObraCivil","localgisgestionciudad.menu.dominios.editor"));
//			jMenuDominios.add(getJMenuItemEditorDominiosEIEL());

		}
		return jMenuDominios;
	}

//	private JMenu getJMenuMapasMunicipales(){
//
//		if (jMenuMapasMunicipales == null){
//
//			jMenuMapasMunicipales = new JMenu();
//			jMenuMapasMunicipales.setText(I18N.get("LocalGISEIEL","localgiseiel.menu.municipalmap"));
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoViviendas());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoPlaneamiento());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDepositos1());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDepositos2());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoPavimentacion1());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoPavimentacion2());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoAlumbrado());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoCentrosSanitarios1());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoCentrosSanitarios2());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoCaptaciones());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoPotabilizacion());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDepositosSint());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDistribucion());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoSaneamiento());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDepuracion());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoPavimentacionSint());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoAlumbradoSint());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoBasuras());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoResiduos());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoCultura());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDeportes());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoZonasVerdes());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoAdministrativo());
//			jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoGlobal());			
//
//		}
//		return jMenuMapasMunicipales;
//	}

//	private JMenu getJMenuMapasComarcales(){
//
//		if (jMenuMapasComarcales == null){
//
//			jMenuMapasComarcales = new JMenu();
//			jMenuMapasComarcales.setText(I18N.get("LocalGISEIEL","localgiseiel.menu.comarcalmap"));
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalViviendas());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPlaneamiento());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDepositos1());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDepositos2());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPavimentacion1());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPavimentacion2());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalAlumbrado());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalCentrosSanitarios1());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalCentrosSanitarios2());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalCaptaciones());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPotabilizacion());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDepositosSint());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDistribucion());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalSaneamiento());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDepuracion());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPavimentacionSint());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalAlumbradoSint());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalBasuras());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalResiduos());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalCultura());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDeportes());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalZonasVerdes());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalAdministrativo());
//			jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalGlobal());			
//
//		}
//		return jMenuMapasComarcales;
//	}
//
//	private JMenu getJMenuMapasObras(){
//
//		if (jMenuMapasObras == null){
//
//			jMenuMapasObras = new JMenu();
//			jMenuMapasObras.setText(I18N.get("LocalGISEIEL","localgiseiel.menu.obrasmap"));
//			jMenuMapasObras.add(getJMenuItemCargarMapaObrasInversion());
//			jMenuMapasObras.add(getJMenuItemCargarMapaObrasHabitantes());
//			jMenuMapasObras.add(getJMenuItemCargarMapaObrasTotal());			
//
//		}
//		return jMenuMapasObras;
//	}

	/**
	 * This method initializes jMenuGenerarFicheros
	 *
	 * @return javax.swing.JMenu
	 */
//	private JMenu getJMenuMapasTematicos()
//	{
//		if (jMenuMapasTematicos == null)
//		{
//			jMenuMapasTematicos = new JMenu();
//			jMenuMapasTematicos.setText(I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmap"));
//			jMenuMapasTematicos.add(getJMenuItemCalcularIndices());
//			jMenuMapasTematicos.add(getJMenuItemCalcularObras());
//			jMenuMapasTematicos.addSeparator();
//			jMenuMapasTematicos.addSeparator();
//			jMenuMapasTematicos.add(getJMenuMapasMunicipales());
//			jMenuMapasTematicos.add(getJMenuMapasComarcales());
//			//			jMenuMapasTematicos.add(getJMenuMapasObras());
//			jMenuMapasTematicos.add(getJMenuMapasObrasMunicipales());
//			jMenuMapasTematicos.add(getJMenuMapasObrasComarcales());
//
//		}
//		return jMenuMapasTematicos;
//	}

//	private JMenu getJMenuMapasObrasMunicipales(){
//
//		if (jMenuMapasObrasMunicipales == null){
//
//			jMenuMapasObrasMunicipales = new JMenu();
//			jMenuMapasObrasMunicipales.setText(I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmap.obras.munic"));
//			jMenuMapasObrasMunicipales.add(getJMenuItemCargarMapaObrasInversion());
//			jMenuMapasObrasMunicipales.add(getJMenuItemCargarMapaObrasHabitantes());
//			jMenuMapasObrasMunicipales.add(getJMenuItemCargarMapaObrasTotal());			
//		}
//		return jMenuMapasObrasMunicipales;
//	}

//	private JMenu getJMenuMapasObrasComarcales(){
//
//		if (jMenuMapasObrasComarcales == null){
//
//			jMenuMapasObrasComarcales = new JMenu();
//			jMenuMapasObrasComarcales.setText(I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmap.obras.comarcal"));
//			jMenuMapasObrasComarcales.add(getJMenuItemCargarMapaComarcalObrasInversion());
//			jMenuMapasObrasComarcales.add(getJMenuItemCargarMapaComarcalObrasHabitantes());
//			jMenuMapasObrasComarcales.add(getJMenuItemCargarMapaComarcalObrasTotal());			
//		}
//		return jMenuMapasObrasComarcales;
//	}

//	private JMenuItem getJMenuItemCalcularIndices()
//	{
//		if (jMenuItemCalcularIndices  == null)
//		{
//			jMenuItemCalcularIndices  =
//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.calcularindic"));
//			jMenuItemCalcularIndices.setEnabled(true);
//			jMenuItemCalcularIndices.addActionListener(new ActionListener()
//			{
//				public void actionPerformed(ActionEvent e)
//				{				
//					final JFrame desktop= (JFrame)LocalGISObraCivil.this;
//					final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
//					progressDialog.setTitle("TaskMonitorDialog.Wait");
//					progressDialog.addComponentListener(new ComponentAdapter()
//					{
//						public void componentShown(ComponentEvent e)
//						{
//							new Thread(new Runnable()
//							{
//								public void run()
//								{
//									try
//									{        
//										ConstantesLocalGISEIEL.clienteLocalGISEIEL.calcularIndices();
//
//									}
//									catch(Exception e)
//									{
//										logger.error("Error ", e);
//										ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
//										return;
//									}
//									finally
//									{
//										progressDialog.setVisible(false);                                
//										progressDialog.dispose();
//									}
//								}
//							}).start();
//						}
//					});
//					GUIUtil.centreOnWindow(progressDialog);
//					progressDialog.setVisible(true);
//
//					show();
//
//				}
//			});
//		}
//		return jMenuItemCalcularIndices;
//	}


//	private JMenuItem getJMenuItemCalcularObras()
//	{
//		if (jMenuItemCalcularObras  == null)
//		{
//			jMenuItemCalcularObras  =
//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.calcularobras"));
//			jMenuItemCalcularObras.setEnabled(true);
//			jMenuItemCalcularObras.addActionListener(new ActionListener()
//			{
//				public void actionPerformed(ActionEvent e)
//				{				
//					final JFrame desktop= (JFrame)LocalGISObraCivil.this;
//					final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
//					progressDialog.setTitle("TaskMonitorDialog.Wait");
//					progressDialog.addComponentListener(new ComponentAdapter()
//					{
//						public void componentShown(ComponentEvent e)
//						{
//							new Thread(new Runnable()
//							{
//								public void run()
//								{
//									try
//									{        
//										ConstantesLocalGISEIEL.clienteLocalGISEIEL.calcularObras();
//
//									}
//									catch(Exception e)
//									{
//										logger.error("Error ", e);
//										ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
//										return;
//									}
//									finally
//									{
//										progressDialog.setVisible(false);                                
//										progressDialog.dispose();
//									}
//								}
//							}).start();
//						}
//					});
//					GUIUtil.centreOnWindow(progressDialog);
//					progressDialog.setVisible(true);
//
//					show();
//
//				}
//			});
//		}
//		return jMenuItemCalcularObras;
//	}


	/**
	 * This method initializes jMenuItemImportarSubparcelas
	 *
	 * @return javax.swing.JMenuItem
	 */
//	private JMenuItem getJMenuItemCargarMapaProvincial()
//	{
//		if (jMenuItemCargarMapaProvincialEIEL  == null)
//		{
//			jMenuItemCargarMapaProvincialEIEL  =
//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadmapseiel.provincial"));
//			jMenuItemCargarMapaProvincialEIEL.setEnabled(true);
//			jMenuItemCargarMapaProvincialEIEL.addActionListener(new ActionListener()
//			{
//				public void actionPerformed(ActionEvent e)
//				{
//					int idMapa = ConstantesLocalGISEIEL.MAPA_PROVINCIAL;
//
//					showMap("workbench-properties-eiel-system.xml", idMapa);
//
//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
//
//				}
//			});
//		}
//		return jMenuItemCargarMapaProvincialEIEL;
//	}

//	private JMenuItem getJMenuItemCargarMapaComarcal()
//	{
//		if (jMenuItemCargarMapaComarcalEIEL  == null)
//		{
//			jMenuItemCargarMapaComarcalEIEL  =
//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadmapseiel.comarcal"));
//			jMenuItemCargarMapaComarcalEIEL.setEnabled(true);
//			jMenuItemCargarMapaComarcalEIEL.addActionListener(new ActionListener()
//			{
//				public void actionPerformed(ActionEvent e)
//				{
//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL;
//
//					showMap("workbench-properties-eiel-system.xml", idMapa);
//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
//
//				}
//			});
//		}
//		return jMenuItemCargarMapaComarcalEIEL;
//	}

//	private JMenuItem getJMenuItemCargarMapaMunicipal()
//	{
//		if (jMenuItemCargarMapaMunicipalEIEL  == null)
//		{
//			jMenuItemCargarMapaMunicipalEIEL  =
//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadmapseiel.municipal"));
//			jMenuItemCargarMapaMunicipalEIEL.setEnabled(true);
//			jMenuItemCargarMapaMunicipalEIEL.addActionListener(new ActionListener()
//			{
//				public void actionPerformed(ActionEvent e)
//				{
//					int idMapa = ConstantesLocalGISEIEL.MAPA_MUNICIPAL;
//
//					showMap("workbench-properties-eiel-system.xml", idMapa);
//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
//
//				}
//			});
//		}
//		return jMenuItemCargarMapaMunicipalEIEL;
//	}

//	private JMenuItem getJMenuItemCargarMapaInfraestructuras()
//	{
//		if (jMenuItemCargarMapaInfraestructurasEIEL  == null)
//		{
//			jMenuItemCargarMapaInfraestructurasEIEL  =
//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadmapseiel.infraestructuras"));
//			jMenuItemCargarMapaInfraestructurasEIEL.setEnabled(true);
//			jMenuItemCargarMapaInfraestructurasEIEL.addActionListener(new ActionListener()
//			{
//				public void actionPerformed(ActionEvent e)
//				{
//					int idMapa = ConstantesLocalGISEIEL.MAPA_INFRAESTRUCTURAS;
//
//					showMap("workbench-properties-eiel.xml", idMapa);
//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
//				}
//			});
//		}
//		return jMenuItemCargarMapaInfraestructurasEIEL;
//	}

	private JMenuItem getJMenuItemEdicionEIEL()
	{
		if (jMenuItemEdicionEIEL  == null)
		{
			jMenuItemEdicionEIEL  =
				new JMenuItem(I18N.get("LocalGISObraCivil","localgisobracivil.submenu.adviseslabel"));
			jMenuItemEdicionEIEL.setEnabled(true);
			jMenuItemEdicionEIEL.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showWarningsMainPanel();

					//La siguiente carga del mapa de planeamiento exige recarga por si se ha modificado alguna 
					//de sus capas
//					int idMapa = new Integer(AppContext.getApplicationContext().getString("id.mapa.planeamiento.informe.urbanistico")).intValue();
					int idMapa = ConstantessLocalGISObraCivil.MAPA_GESTIONESPACIOPUBLICO;
					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
				}
			});
		}
		return jMenuItemEdicionEIEL;
	}

	@SuppressWarnings("deprecation")
	private void showWarningsMainPanel()
	{	

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

							if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) 
								return;

							Container c = LocalGISGestionCiudad.this.getContentPane();
							c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							JInternalFrame jif = encapsulaEnJInternalFrame(getJPanelGestionCiudad(), "");
							llamadaAInternalFrame(jif, false, true, true);
							c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							c.setCursor(Cursor.getDefaultCursor());
							jif.setMaximizable(false);
							

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
		//Precarga el mapa de la eiel 
		((com.localgis.app.gestionciudad.dialogs.main.GeopistaEditorPanel)((EditorPanel)getJPanelEditorEIEL())
				.getGeopistaEditorPanel()).initEditor();          
	}
	
	
	private void showImprimirMainPanel()
	{	

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

							if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) 
								return;

							Container c = LocalGISGestionCiudad.this.getContentPane();
							c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							JInternalFrame jif = encapsulaEnJInternalFrame(getPrintJPanelGestionCiudad(), "Imprimir Mapas De Gestión Ciudad");
							llamadaAInternalFrame(jif, false, true, true);
							c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							c.setCursor(Cursor.getDefaultCursor());
							jif.setMaximizable(false);
						

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
		//Precarga el mapa de la eiel 
		((com.localgis.app.gestionciudad.dialogs.main.PrintEditorPanel)((PrintEditor)getJPanelEditorPrintGestionCiudad())
				.getGeopistaEditorPanel()).initEditor();          
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private void showMap(final String fileProperties, final int idMap)
	{	

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

							if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) 
								return;

							Container c = LocalGISGestionCiudad.this.getContentPane();
							c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							if (AppContext.getApplicationContext().getBlackboard().get("internalframe") != null){
								cierraInternalFrame((JInternalFrame)AppContext.getApplicationContext().getBlackboard().get("internalframe"));
							}
//							String title = ConstantessLocalGISObraCivil.clienteLocalGISEIEL.getNombredMapa(idMap);
							String title ="";
							JInternalFrame jif = encapsulaEnJInternalFrame(getJPanelMap(fileProperties, new Integer(idMap)), title);
							AppContext.getApplicationContext().getBlackboard().put("internalframe", jif);
							llamadaAInternalFrame(jif, false, true, true);
							c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							c.setCursor(Cursor.getDefaultCursor());

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

		GeopistaMapPanel panel = (GeopistaMapPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor");
		panel.initEditor(); 
	}

	private EditorPanel getJPanelEditorEIEL(){

		if (jPanelEditorEIEL  == null)
		{						
			jPanelEditorEIEL = new EditorPanel();    
		}		
		return jPanelEditorEIEL;
	}
	
	private PrintEditor getJPanelEditorPrintGestionCiudad(){

		if (jPanelPrintEditorEIEL  == null)
		{						
			jPanelPrintEditorEIEL = new PrintEditor();    
		}		
		return jPanelPrintEditorEIEL;
	}


	private ObraCivilEditorPanel getJPanelGestionCiudad(){

		if (jPanelEIEL  == null)
		{						
			jPanelEIEL  = new ObraCivilEditorPanel();    
		}		
		return jPanelEIEL;
	}
	
	private ObraCivilPrintPanel getPrintJPanelGestionCiudad(){

		if (jPanelPrintGestionCiudad  == null)
		{						
			jPanelPrintGestionCiudad  = new ObraCivilPrintPanel();    
		}		
		return jPanelPrintGestionCiudad;
	}

	private ShowMapPanel getJPanelMap(String fileProperties, int idMap){

		jPanelMap  = new ShowMapPanel(fileProperties, idMap);   

		return jPanelMap;
	}


	/**
	 * This method initializes jMenuItemInformes
	 *
	 * @return javax.swing.JMenuItem
	 */
	@SuppressWarnings("unused")
	private JMenuItem getJMenuItemMapasTematicos()
	{
		if (jMenuItemCargarMapaTematicoViviendas  == null)
		{
			jMenuItemCargarMapaTematicoViviendas =
				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps"));
			jMenuItemCargarMapaTematicoViviendas.setEnabled(true);
			jMenuItemCargarMapaTematicoViviendas.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
//										showInformes();
				}
			});
		}
		return jMenuItemCargarMapaTematicoViviendas;
	}

	private JMenuItem getJMenuItemIndicadores()
	{
		if (jMenuItemIndicadores  == null)
		{
			jMenuItemIndicadores =
				new JMenuItem(I18N.get("LocalGISObraCivil","localgisgestionciudad.submenu.statistics"));
			jMenuItemIndicadores.setEnabled(true);
			jMenuItemIndicadores.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					onIndicadoresButtonDo();
				}
			});
			
		}
		return jMenuItemIndicadores;
	}
	
	
	private void onIndicadoresButtonDo() {
		
		final TaskMonitorDialog progressDialog2 = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		progressDialog2.setTitle(I18N.get("geomarketing","localgis.geomarketing.plugins.draw.tittle"));
		progressDialog2.setTitle("TaskMonitorDialog.Wait");
		progressDialog2.addComponentListener(new ComponentAdapter()
		{public void componentShown(ComponentEvent e)
		{new Thread(new Runnable()
		{public void run()
		{try {
			progressDialog2.report(
					I18N.get("LocalGISObraCivil","localgisgestionciudad.submenu.statistics.progressdialog"));
			
			StatisticalDataOT[] statistics;	
			String entidad = Integer.toString(AppContext.getIdEntidad());
			String userLogin = AppContext.getApplicationContext().getString(AppContext.USER_LOGIN);
			
			
			statistics = WSInterventionsWrapper.getStatistics(userLogin,Integer.parseInt(entidad));
			
			if (statistics!=null && statistics.length>0){
				progressDialog2.setVisible(false);
				SignsDialog dialog = new SignsDialog(
						AppContext.getApplicationContext().getMainFrame(),
						I18N.get("LocalGISObraCivil","localgisgestionciudad.submenu.statistics.dialog.title"), 
						true, 
						statistics);
			} else{
				JOptionPane.showMessageDialog(progressDialog2,I18N.get("LocalGISObraCivil","localgisgestionciudad.submenu.statistics.nodata.error"));
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			progressDialog2.setVisible(false);
		}
		}
		}).start();
		}
		});
		GUIUtil.centreOnWindow(progressDialog2);
		progressDialog2.setVisible(true);
		
		
		
	}

	/**
	 * This method initializes jMenuItemEditorDominiosPlaneamiento
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemEditorDominiosEIEL()
	{
		if (jMenuItemEditorDominiosEIEL  == null)
		{
			jMenuItemEditorDominiosEIEL =
				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.dominios.editor"));
			jMenuItemEditorDominiosEIEL.setEnabled(true);
			jMenuItemEditorDominiosEIEL.addActionListener(new ActionListener()
			{
				@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent e)
				{					

					final JFrame desktop= LocalGISGestionCiudad.this;
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

										if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
										Container c = LocalGISGestionCiudad.this.getContentPane();
										c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
										Locale currentLocale = new Locale(Constantes.Locale);
										ResourceBundle messages;
										try
										{
											messages = ResourceBundle.getBundle("config.administrador", currentLocale);
										}catch (Exception ex)
										{
											messages = ResourceBundle.getBundle("config.administrador", new Locale(Constantes.LOCALE_CASTELLANO));
										}
										GeopistaAcl aclAdministracion;
										boolean editable = false;
										try {
											aclAdministracion = com.geopista.security.SecurityManager.getPerfil("Administracion");
											editable = aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
										} catch (Exception e1) {
											editable = false;
										}
										CDominiosFrame dominiosFrame=new CDominiosFrame(messages,LocalGISGestionCiudad.this);
										dominiosFrame.editable(editable);
										llamadaAInternalFrame(dominiosFrame, false, true, true);
										c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
										c.setCursor(Cursor.getDefaultCursor());						



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

					if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
					Container c = LocalGISGestionCiudad.this.getContentPane();
					c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					Locale currentLocale = new Locale(Constantes.Locale);
					ResourceBundle messages;
					try
					{
						messages = ResourceBundle.getBundle("config.administrador", currentLocale);
					}catch (Exception ex)
					{
						messages = ResourceBundle.getBundle("config.administrador", new Locale(Constantes.LOCALE_CASTELLANO));
					}
					GeopistaAcl aclAdministracion;
					boolean editable = false;
					try {
						aclAdministracion = com.geopista.security.SecurityManager.getPerfil("Administracion");
						editable = aclAdministracion.checkPermission(new com.geopista.security.GeopistaPermission(com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
					} catch (Exception e1) {
						editable = false;
					}
					CDominiosFrame dominiosFrame=new CDominiosFrame(messages,LocalGISGestionCiudad.this);
					dominiosFrame.editable(editable);
					llamadaAInternalFrame(dominiosFrame, false, true, true);
					c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					c.setCursor(Cursor.getDefaultCursor());						
				}
			});
		}
		return jMenuItemEditorDominiosEIEL;
	}

	//	private JMenuItem getJMenuItemCargarMapaObrasTotal()
	//	{
	//		if (jMenuItemCargarMapaObrasTotal == null)
	//		{
	//			jMenuItemCargarMapaObrasTotal  = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.numobras"));
	//			jMenuItemCargarMapaObrasTotal.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_OBRAS_TOTAL;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaObrasTotal;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalObrasTotal()
	//	{
	//		if (jMenuItemCargarMapaComarcalObrasTotal == null)
	//		{
	//			jMenuItemCargarMapaComarcalObrasTotal  = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.numobras"));
	//			jMenuItemCargarMapaComarcalObrasTotal.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_TOTAL;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalObrasTotal;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaObrasHabitantes()
	//	{
	//		if (jMenuItemCargarMapaObrasHabitantes == null)
	//		{
	//			jMenuItemCargarMapaObrasHabitantes  = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.obrashabitante"));
	//			jMenuItemCargarMapaObrasHabitantes.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_OBRAS_HABITANTE;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaObrasHabitantes;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalObrasHabitantes()
	//	{
	//		if (jMenuItemCargarMapaComarcalObrasHabitantes == null)
	//		{
	//			jMenuItemCargarMapaComarcalObrasHabitantes  = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.obrashabitante"));
	//			jMenuItemCargarMapaComarcalObrasHabitantes.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_HABITANTE;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalObrasHabitantes;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaObrasInversion()
	//	{
	//		if (jMenuItemCargarMapaObrasInversion == null)
	//		{
	//			jMenuItemCargarMapaObrasInversion  = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.obrasinversion"));
	//			jMenuItemCargarMapaObrasInversion.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_OBRAS_INVERSION;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaObrasInversion;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalObrasInversion()
	//	{
	//		if (jMenuItemCargarMapaComarcalObrasInversion == null)
	//		{
	//			jMenuItemCargarMapaComarcalObrasInversion  = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.obrasinversion"));
	//			jMenuItemCargarMapaComarcalObrasInversion.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_INVERSION;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalObrasInversion;
	//	}
	//		
	//	/**
	//	 * This method initializes jMenuItemFinEntradaRelativo
	//	 *
	//	 * @return javax.swing.JMenuItem
	//	 */
	//	private JMenuItem getJMenuItemCargarMapaTematicoViviendas()
	//	{
	//		if (jMenuItemCargarMapaTematicoViviendas == null)
	//		{
	//			jMenuItemCargarMapaTematicoViviendas = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.viviendas"));
	//			jMenuItemCargarMapaTematicoViviendas.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_VIVIENDAS;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoViviendas;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalViviendas()
	//	{
	//		if (jMenuItemCargarMapaComarcalViviendas == null)
	//		{
	//			jMenuItemCargarMapaComarcalViviendas = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.viviendas"));
	//			jMenuItemCargarMapaComarcalViviendas.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_VIVIENDAS;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalViviendas;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoPlaneamiento()
	//	{
	//		if (jMenuItemCargarMapaTematicoPlaneamiento == null)
	//		{
	//			jMenuItemCargarMapaTematicoPlaneamiento  = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.planeamiento"));
	//			jMenuItemCargarMapaTematicoPlaneamiento.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_PLANEAMIENTO;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoPlaneamiento;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalPlaneamiento()
	//	{
	//		if (jMenuItemCargarMapaComarcalPlaneamiento == null)
	//		{
	//			jMenuItemCargarMapaComarcalPlaneamiento  = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.planeamiento"));
	//			jMenuItemCargarMapaComarcalPlaneamiento.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_PLANEAMIENTO;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalPlaneamiento;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoDepositos1()
	//	{
	//		if (jMenuItemCargarMapaTematicoDepositos1 == null)
	//		{
	//			jMenuItemCargarMapaTematicoDepositos1   = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.depositos1"));
	//			jMenuItemCargarMapaTematicoDepositos1.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPOSITOS1;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoDepositos1;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalDepositos1()
	//	{
	//		if (jMenuItemCargarMapaComarcalDepositos1 == null)
	//		{
	//			jMenuItemCargarMapaComarcalDepositos1   = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.depositos1"));
	//			jMenuItemCargarMapaComarcalDepositos1.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPOSITOS1;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalDepositos1;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoDepositos2()
	//	{
	//		if (jMenuItemCargarMapaTematicoDepositos2 == null)
	//		{
	//			jMenuItemCargarMapaTematicoDepositos2   = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.depositos2"));
	//			jMenuItemCargarMapaTematicoDepositos2.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPOSITOS2;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoDepositos2;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalDepositos2()
	//	{
	//		if (jMenuItemCargarMapaComarcalDepositos2 == null)
	//		{
	//			jMenuItemCargarMapaComarcalDepositos2   = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.depositos2"));
	//			jMenuItemCargarMapaComarcalDepositos2.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPOSITOS2;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalDepositos2;
	//	}
	//
	//	private JMenuItem getJMenuItemCargarMapaTematicoPavimentacion1()
	//	{
	//		if (jMenuItemCargarMapaTematicoPavimentacion1 == null)
	//		{
	//			jMenuItemCargarMapaTematicoPavimentacion1    = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.pavimentacion1"));
	//			jMenuItemCargarMapaTematicoPavimentacion1.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_PAVIMENTACION1;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoPavimentacion1;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalPavimentacion1()
	//	{
	//		if (jMenuItemCargarMapaComarcalPavimentacion1 == null)
	//		{
	//			jMenuItemCargarMapaComarcalPavimentacion1    = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.pavimentacion1"));
	//			jMenuItemCargarMapaComarcalPavimentacion1.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_PAVIMENTACION1;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalPavimentacion1;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoPavimentacion2()
	//	{
	//		if (jMenuItemCargarMapaTematicoPavimentacion2 == null)
	//		{
	//			jMenuItemCargarMapaTematicoPavimentacion2     = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.pavimentacion2"));
	//			jMenuItemCargarMapaTematicoPavimentacion2.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_PAVIMENTACION2;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoPavimentacion2;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalPavimentacion2()
	//	{
	//		if (jMenuItemCargarMapaComarcalPavimentacion2 == null)
	//		{
	//			jMenuItemCargarMapaComarcalPavimentacion2     = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.pavimentacion2"));
	//			jMenuItemCargarMapaComarcalPavimentacion2.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_PAVIMENTACION2;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalPavimentacion2;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoAlumbrado()
	//	{
	//		if (jMenuItemCargarMapaTematicoAlumbrado == null)
	//		{
	//			jMenuItemCargarMapaTematicoAlumbrado      = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.alumbrado"));
	//			jMenuItemCargarMapaTematicoAlumbrado.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_ALUMBRADO;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoAlumbrado;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalAlumbrado()
	//	{
	//		if (jMenuItemCargarMapaComarcalAlumbrado == null)
	//		{
	//			jMenuItemCargarMapaComarcalAlumbrado      = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.alumbrado"));
	//			jMenuItemCargarMapaComarcalAlumbrado.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_ALUMBRADO;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalAlumbrado;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoCentrosSanitarios1()
	//	{
	//		if (jMenuItemCargarMapaTematicoCentrosSanitarios1 == null)
	//		{
	//			jMenuItemCargarMapaTematicoCentrosSanitarios1       = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.centrossanitarios1"));
	//			jMenuItemCargarMapaTematicoCentrosSanitarios1.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_CENTROS_SANITARIOS1;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoCentrosSanitarios1;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalCentrosSanitarios1()
	//	{
	//		if (jMenuItemCargarMapaComarcalCentrosSanitarios1 == null)
	//		{
	//			jMenuItemCargarMapaComarcalCentrosSanitarios1       = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.centrossanitarios1"));
	//			jMenuItemCargarMapaComarcalCentrosSanitarios1.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_CENTROS_SANITARIOS1;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalCentrosSanitarios1;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoCentrosSanitarios2()
	//	{
	//		if (jMenuItemCargarMapaTematicoCentrosSanitarios2 == null)
	//		{
	//			jMenuItemCargarMapaTematicoCentrosSanitarios2       = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.centrossanitarios2"));
	//			jMenuItemCargarMapaTematicoCentrosSanitarios2.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_CENTROS_SANITARIOS2;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoCentrosSanitarios2;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalCentrosSanitarios2()
	//	{
	//		if (jMenuItemCargarMapaComarcalCentrosSanitarios2 == null)
	//		{
	//			jMenuItemCargarMapaComarcalCentrosSanitarios2       = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.centrossanitarios2"));
	//			jMenuItemCargarMapaComarcalCentrosSanitarios2.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_CENTROS_SANITARIOS2;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalCentrosSanitarios2;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoPotabilizacion()
	//	{
	//		if (jMenuItemCargarMapaTematicoPotabilizacion == null)
	//		{
	//			jMenuItemCargarMapaTematicoPotabilizacion        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.potabilizacion"));
	//			jMenuItemCargarMapaTematicoPotabilizacion.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_POTABILIZACION;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoPotabilizacion;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalPotabilizacion()
	//	{
	//		if (jMenuItemCargarMapaComarcalPotabilizacion == null)
	//		{
	//			jMenuItemCargarMapaComarcalPotabilizacion        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.potabilizacion"));
	//			jMenuItemCargarMapaComarcalPotabilizacion.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_POTABILIZACION;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalPotabilizacion;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoCaptaciones()
	//	{
	//		if (jMenuItemCargarMapaTematicoCaptaciones == null)
	//		{
	//			jMenuItemCargarMapaTematicoCaptaciones        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.captaciones"));
	//			jMenuItemCargarMapaTematicoCaptaciones.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_CAPTACIONES;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoCaptaciones;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalCaptaciones()
	//	{
	//		if (jMenuItemCargarMapaComarcalCaptaciones == null)
	//		{
	//			jMenuItemCargarMapaComarcalCaptaciones        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.captaciones"));
	//			jMenuItemCargarMapaComarcalCaptaciones.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_CAPTACIONES;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalCaptaciones;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoDepositosSint()
	//	{
	//		if (jMenuItemCargarMapaTematicoDepositosSint == null)
	//		{
	//			jMenuItemCargarMapaTematicoDepositosSint        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.depositossint"));
	//			jMenuItemCargarMapaTematicoDepositosSint.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPOSITOS_SINT;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoDepositosSint;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalDepositosSint()
	//	{
	//		if (jMenuItemCargarMapaComarcalDepositosSint == null)
	//		{
	//			jMenuItemCargarMapaComarcalDepositosSint        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.depositossint"));
	//			jMenuItemCargarMapaComarcalDepositosSint.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPOSITOS_SINT;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalDepositosSint;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoDistribucion()
	//	{
	//		if (jMenuItemCargarMapaTematicoDistribucion == null)
	//		{
	//			jMenuItemCargarMapaTematicoDistribucion        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.distribucion"));
	//			jMenuItemCargarMapaTematicoDistribucion.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DISTRIBUCION;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoDistribucion;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalDistribucion()
	//	{
	//		if (jMenuItemCargarMapaComarcalDistribucion == null)
	//		{
	//			jMenuItemCargarMapaComarcalDistribucion        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.distribucion"));
	//			jMenuItemCargarMapaComarcalDistribucion.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DISTRIBUCION;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalDistribucion;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoSaneamiento()
	//	{
	//		if (jMenuItemCargarMapaTematicoSaneamiento == null)
	//		{
	//			jMenuItemCargarMapaTematicoSaneamiento        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.saneamiento"));
	//			jMenuItemCargarMapaTematicoSaneamiento.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_SANEAMIENTO;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoSaneamiento;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalSaneamiento()
	//	{
	//		if (jMenuItemCargarMapaComarcalSaneamiento == null)
	//		{
	//			jMenuItemCargarMapaComarcalSaneamiento        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.saneamiento"));
	//			jMenuItemCargarMapaComarcalSaneamiento.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_SANEAMIENTO;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalSaneamiento;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoDepuracion()
	//	{
	//		if (jMenuItemCargarMapaTematicoDepuracion == null)
	//		{
	//			jMenuItemCargarMapaTematicoDepuracion        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.depuracion"));
	//			jMenuItemCargarMapaTematicoDepuracion.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPURACION;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoDepuracion;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalDepuracion()
	//	{
	//		if (jMenuItemCargarMapaComarcalDepuracion == null)
	//		{
	//			jMenuItemCargarMapaComarcalDepuracion        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.depuracion"));
	//			jMenuItemCargarMapaComarcalDepuracion.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPURACION;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalDepuracion;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoPavimentacionSint()
	//	{
	//		if (jMenuItemCargarMapaTematicoPavimentacionSint == null)
	//		{
	//			jMenuItemCargarMapaTematicoPavimentacionSint        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.pavimentacionsint"));
	//			jMenuItemCargarMapaTematicoPavimentacionSint.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_PAVIMENTACION_SINT;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoPavimentacionSint;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalPavimentacionSint()
	//	{
	//		if (jMenuItemCargarMapaComarcalPavimentacionSint == null)
	//		{
	//			jMenuItemCargarMapaComarcalPavimentacionSint        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.pavimentacionsint"));
	//			jMenuItemCargarMapaComarcalPavimentacionSint.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_PAVIMENTACION_SINT;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalPavimentacionSint;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoAlumbradoSint()
	//	{
	//		if (jMenuItemCargarMapaTematicoAlumbradoSint == null)
	//		{
	//			jMenuItemCargarMapaTematicoAlumbradoSint        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.alumbradosint"));
	//			jMenuItemCargarMapaTematicoAlumbradoSint.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_ALUMBRADO_SINT;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoAlumbradoSint;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalAlumbradoSint()
	//	{
	//		if (jMenuItemCargarMapaComarcalAlumbradoSint == null)
	//		{
	//			jMenuItemCargarMapaComarcalAlumbradoSint        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.alumbradosint"));
	//			jMenuItemCargarMapaComarcalAlumbradoSint.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_ALUMBRADO_SINT;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalAlumbradoSint;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoBasuras()
	//	{
	//		if (jMenuItemCargarMapaTematicoBasuras == null)
	//		{
	//			jMenuItemCargarMapaTematicoBasuras        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.basuras"));
	//			jMenuItemCargarMapaTematicoBasuras.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_BASURAS;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoBasuras;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalBasuras()
	//	{
	//		if (jMenuItemCargarMapaComarcalBasuras == null)
	//		{
	//			jMenuItemCargarMapaComarcalBasuras        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.basuras"));
	//			jMenuItemCargarMapaComarcalBasuras.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_BASURAS;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalBasuras;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoResiduos()
	//	{
	//		if (jMenuItemCargarMapaTematicoResiduos == null)
	//		{
	//			jMenuItemCargarMapaTematicoResiduos        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.residuos"));
	//			jMenuItemCargarMapaTematicoResiduos.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_RESIDUOS;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoResiduos;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalResiduos()
	//	{
	//		if (jMenuItemCargarMapaComarcalResiduos == null)
	//		{
	//			jMenuItemCargarMapaComarcalResiduos        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.residuos"));
	//			jMenuItemCargarMapaComarcalResiduos.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_RESIDUOS;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalResiduos;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoCultura()
	//	{
	//		if (jMenuItemCargarMapaTematicoCultura == null)
	//		{
	//			jMenuItemCargarMapaTematicoCultura        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.cultura"));
	//			jMenuItemCargarMapaTematicoCultura.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_CULTURA;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoCultura;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalCultura()
	//	{
	//		if (jMenuItemCargarMapaComarcalCultura == null)
	//		{
	//			jMenuItemCargarMapaComarcalCultura        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.cultura"));
	//			jMenuItemCargarMapaComarcalCultura.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_CULTURA;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalCultura;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoDeportes()
	//	{
	//		if (jMenuItemCargarMapaTematicoDeportes == null)
	//		{
	//			jMenuItemCargarMapaTematicoDeportes        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.deportes"));
	//			jMenuItemCargarMapaTematicoDeportes.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPORTES;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoDeportes;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalDeportes()
	//	{
	//		if (jMenuItemCargarMapaComarcalDeportes == null)
	//		{
	//			jMenuItemCargarMapaComarcalDeportes        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.deportes"));
	//			jMenuItemCargarMapaComarcalDeportes.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPORTES;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalDeportes;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoZonasVerdes()
	//	{
	//		if (jMenuItemCargarMapaTematicoZonasVerdes == null)
	//		{
	//			jMenuItemCargarMapaTematicoZonasVerdes        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.zonasverdes"));
	//			jMenuItemCargarMapaTematicoZonasVerdes.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_ZONASVERDES;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoZonasVerdes;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalZonasVerdes()
	//	{
	//		if (jMenuItemCargarMapaComarcalZonasVerdes == null)
	//		{
	//			jMenuItemCargarMapaComarcalZonasVerdes        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.zonasverdes"));
	//			jMenuItemCargarMapaComarcalZonasVerdes.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_ZONASVERDES;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalZonasVerdes;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoAdministrativo()
	//	{
	//		if (jMenuItemCargarMapaTematicoAdministrativo == null)
	//		{
	//			jMenuItemCargarMapaTematicoAdministrativo        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.administrativo"));
	//			jMenuItemCargarMapaTematicoAdministrativo.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_ADMINISTRATIVO;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoAdministrativo;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalAdministrativo()
	//	{
	//		if (jMenuItemCargarMapaComarcalAdministrativo == null)
	//		{
	//			jMenuItemCargarMapaComarcalAdministrativo        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.administrativo"));
	//			jMenuItemCargarMapaComarcalAdministrativo.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_ADMINISTRATIVO;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalAdministrativo;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaTematicoGlobal()
	//	{
	//		if (jMenuItemCargarMapaTematicoGlobal == null)
	//		{
	//			jMenuItemCargarMapaTematicoGlobal        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.global"));
	//			jMenuItemCargarMapaTematicoGlobal.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_GLOBAL;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaTematicoGlobal;
	//	}
	//	
	//	private JMenuItem getJMenuItemCargarMapaComarcalGlobal()
	//	{
	//		if (jMenuItemCargarMapaComarcalGlobal == null)
	//		{
	//			jMenuItemCargarMapaComarcalGlobal        = 
	//				new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.loadtematicmaps.global"));
	//			jMenuItemCargarMapaComarcalGlobal.addActionListener(new ActionListener()
	//			{
	//				public void actionPerformed(ActionEvent e)
	//				{	
	//					int idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_GLOBAL;
	//					
	//					showMap("workbench-properties-eiel-system.xml", idMapa);
	//										
	//					LocalGISObraCivilUtils.setMapToReload(idMapa, true);
	//				
	//				}
	//			});
	//		}
	//		return jMenuItemCargarMapaComarcalGlobal;
	//	}
}
