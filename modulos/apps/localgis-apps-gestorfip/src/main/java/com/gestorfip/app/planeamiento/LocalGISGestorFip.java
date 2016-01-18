/**
 * LocalGISGestorFip.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.acl.AclNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.help.HelpBroker;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mozilla.universalchardet.UniversalDetector;
import org.xml.sax.InputSource;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.global.WebAppConstants;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.ISecurityPolicy;
import com.geopista.security.SecurityManager;
import com.geopista.security.connect.ConnectionStatus;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.config.UserPreferenceStore;
import com.gestorfip.app.planeamiento.beans.fip.FicheroFipConsoleBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.gestorfip.app.planeamiento.config.ConfiguracionGestorFip;
import com.gestorfip.app.planeamiento.dialogs.accesocomun.dialogs.AccesoComunDialog;
import com.gestorfip.app.planeamiento.dialogs.configuracion.dialogs.ConfiguracionGestorDialog;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.GestorFipGestorPlaneamientoPanel;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.GestorFipModuloPlaneamientoMapPanel;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels.ModuloPlaneamientoGestorFip;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.utils.GestorPlaneamientoUtils;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.utils.OperacionesDeterminaciones;
import com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.utils.OperacionesEntidades;
import com.gestorfip.app.planeamiento.dialogs.migracionasistida.panel.MigracionAsistidaDeterminacionesPanel;
import com.gestorfip.app.planeamiento.dialogs.migracionasistida.panel.MigracionAsistidaRegulacionesPanel;
import com.gestorfip.app.planeamiento.dialogs.migracionasistida.panel.MigracionAsistidaUsosPanel;
import com.gestorfip.app.planeamiento.dialogs.migracionasistida.panel.MigracionAsistidaValoresPanel;
import com.gestorfip.app.planeamiento.images.IconLoader;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.gestorfip.app.planeamiento.ws.cliente.ClientConsole_v1_86;
import com.gestorfip.app.planeamiento.ws.cliente.ClientConsole_v2_00;
import com.gestorfip.app.planeamiento.ws.cliente.ClientGestorFip;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import es.gestorfip.serviciosweb.ServicesStub.CRSGestor;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfiguracionGestor;
import es.gestorfip.serviciosweb.ServicesStub.VersionesUER;


/**
 * @author javieraragon
 *
 */
public class LocalGISGestorFip extends CMain implements ISecurityPolicy{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8767857741197660844L;
	
	static Logger logger = Logger.getLogger(LocalGISGestorFip.class);
	private Hashtable<String, String> permisos= new Hashtable<String, String>();

	//Atributos del GUI
	private ApplicationContext aplicacion;
	static private JDesktopPane desktopPane;
	private JPanel jPanelStatus = null;    
	private JPanel jPanelStatusTramite = null;    
	public static final int DIM_X = 800;
	public static final int DIM_Y = 600;
	public static final Rectangle PICTURE_BORDER = new Rectangle(15, 5, 100, 440);
	public static final String LOCALGIS_LOGO = "geopista.gif";
	private HelpBroker hb = null;

	private JLabel connectionLabel = new JLabel();
	private JLabel helpLabel = new JLabel();
	private JMenuBar jMenuBarGestorFip = null;
	private JMenu jMenuAbrir = null;
	private JMenu jMenuConfiguracion= null;
	private JMenu jMenuImportarFip = null;
	private JMenuItem logMenuItem = null;
	
	private JMenuItem jMenuItemImportarFicheroFip1 = null;
	private JMenuItem jMenuItemImportarFicheroFip1Disco = null;
	private ModuloPlaneamientoGestorFip jPanelModuloPlaneamientoGestroFip = null;
	private GestorFipGestorPlaneamientoPanel jPanelEditorAsociacioneDeterminacionesEntidades = null;
	
	private AccesoComunDialog accesoComunDialog = null;

	JFileChooser fc = new JFileChooser();
	
	 String resultImportacionFip1 = "";
	 String resultExportacionFip2 = "";
	 
	 ArrayList lstFicherosFip1 = new ArrayList();
	
	 ConnectionStatus status;
	 
	 @SuppressWarnings({ "unchecked", "deprecation" })
	 public LocalGISGestorFip() {

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
		 ResourceBundle bundle = ResourceBundle.getBundle("com.gestorfip.app.planeamiento.language.Fipi18n",loc,this.getClass().getClassLoader());
		 I18N.plugInsResourceBundle.put("LocalGISGestorFip", bundle);

		 ConfiguracionGestorFip.executeCargaPropertiesGestorFip();
		 GestorFipUtils.inicializarIconos();

		 inicializaElementos();   
		 GestorFipUtils.menuBarSetEnabled(false, this);
		 configureApp();

		 this.setIconImage(IconLoader.icon(LOCALGIS_LOGO).getImage());
		 this.setTitle(I18N.get("LocalGISGestorFip","gestorFip.frame.title"));
		 try
		 {

			 show();

			 this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			 GestorFipUtils.menuBarSetEnabled(false, this);

			 if (splashWindow != null)
				 splashWindow.setVisible(false);


			 SSOAuthManager.ssoAuthManager(ConstantesGestorFIP.idApp);								
			 if (!AppContext.getApplicationContext().isOnlyLogged()){  
				 if (!showAuth()){
					 dispose();
					 return;
				 }
			 }

			 setPolicy();

			 if(!tienePermisos("LocalGIS.GestorFip.Login")){
				 noPermApp();
				 return;
			 }  

			 if (!AppContext.seleccionarMunicipio((Frame)this)){
				 stopApp();
				 return;
			 }

			 Constantes.idMunicipio = ConstantesGestorFIP.IdMunicipio = AppContext.getIdMunicipio();
			 UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID, Integer.toString(Constantes.idMunicipio));
			
			 cargarDatosApliacionGestorFip();

			 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			 showAccesoComun();
			 GestorFipUtils.menuBarSetEnabled(false, this);
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

	 private void noPermApp(){
		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"No tiene permisos para entrar. Se cerrará el aplicativo");
		 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		 System.exit(1);
	 }
	 
	 private void  stopApp(){
		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Inicio de aplicación cancelado. Se cerrará el aplicativo.");
		 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		 System.exit(1);
	 }

	/**
	 * Funcion que inicializa la gui de la pantalla principal, asociandole eventos para que tenga un tama�o minimo y
	 * la accion de cierre.
	 */
	private void inicializaElementos()
	{	

		desktopPane= new JDesktopPane();
		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);	        

		

		//Gestion de conexiones y desconexiones contra el administrador
		//de cartografia.
		status=new ConnectionStatus(this,false);
		status.init();		
		aplicacion.getBlackboard().put(UserPreferenceConstants.CONNECT_STATUS,status);
		
		
		
		//Creacion de objetos.
		getContentPane().add(getJPanelStatus(status.getJPanelStatus()), BorderLayout.SOUTH);
		//getContentPane().add(getJPanelStatus(), BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		setJMenuBar(getJMenuBarLocalGISGestorFip());

		//Evento para que la ventana al minimizar tenga un tama�o minimo.
		addComponentListener(new java.awt.event.ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				GestorFipUtils.ajustaVentana(aplicacion.getMainFrame(), DIM_X, DIM_Y+50);
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
			connectionLabel.setToolTipText(aplicacion.getI18nString("geopista.OffLineStatusMessage"));
			if (aplicacion.isLogged())
				SecurityManager.unLogged();
			else
				aplicacion.login();
		} 
		else {
			connectionLabel.setIcon(IconLoader.icon("online.png"));
			connectionLabel.setToolTipText(aplicacion.getI18nString("geopista.OnLineStatusMessage"));

			if (!aplicacion.isLogged())
				showAuth();   
		}
	}

	public void setConnectionInitialStatusMessage (boolean connected){
		if (!connected) {
			connectionLabel.setIcon(IconLoader.icon("no_network.png"));
			connectionLabel.setToolTipText(aplicacion.getI18nString("geopista.OffLineStatusMessage"));
		} 
		else {
			connectionLabel.setIcon(IconLoader.icon("online.png"));
			connectionLabel.setToolTipText(aplicacion.getI18nString("geopista.OnLineStatusMessage"));
		}
	}

	
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
	
	public JPanel getjPanelStatusTramite() {
		if (jPanelStatusTramite == null)
		{
			jPanelStatusTramite = new JPanel(new GridBagLayout());
		
		}
		return jPanelStatusTramite;
	}



	public void setjPanelStatusTramite(JPanel jPanelStatusTramite) {
		this.jPanelStatusTramite = jPanelStatusTramite;
	}


	private JPanel getJPanelStatus (JPanel jPanelStatus)
	{
		if (jPanelStatus != null)
		{
			

			helpLabel.setIcon(IconLoader.icon("help.gif"));
			helpLabel.setToolTipText(aplicacion.getI18nString("geopista.Help"));
			helpLabel.addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent e) {
	
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

			jPanelStatus.add(helpLabel, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0,
							0, 0), 0, 0));
			
		}
		return jPanelStatus;
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

			//Elimina el panel blanco con t�tulo que aparece en la zona superior de la pantalla
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
		GestorFipUtils.menuBarSetEnabled(false, this);
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

		GestorFipUtils.menuBarSetEnabled(true, this);
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


	static public void  llamadaAInternalFrame(JInternalFrame internalFrame, boolean isMaxTam, boolean unica, boolean isAdjustable, Dimension dim)
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

				Constantes.url = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.PRINCIPAL_WEBAPP_NAME;
				ConstantesGestorFIP.loginFIPUrl = Constantes.url;
				
				ClientGestorFip.setTargetEndPoint(aplicacion.getString(UserPreferenceConstants.TOMCAT_URL));
		
				VersionesUER[] versiones = ClientGestorFip.obtenerVersionesConsolaUER(aplicacion);
				aplicacion.getBlackboard().put(ConstantesGestorFIP.VERSIONES_CONSOLE_UER, versiones);
				
				ConfiguracionGestor config = ClientGestorFip.obtenerConfigVersionConsolaUER(aplicacion);
				aplicacion.getBlackboard().put(ConstantesGestorFIP.CONFIG_VERSION_CONSOLE_UER, config);
				
				CRSGestor[] lstCrs = ClientGestorFip.obtenerCrsGestor(aplicacion);
				aplicacion.getBlackboard().put(ConstantesGestorFIP.LST_CRS_GESTOR, lstCrs);
				
				ClientConsole_v1_86.setTargetEndPoint(aplicacion.getString(UserPreferenceConstants.URL_CONSOLEUER_WS));
				ClientConsole_v2_00.setTargetEndPoint(aplicacion.getString(UserPreferenceConstants.URL_CONSOLEUER_WS));
				
				try
				{
					Constantes.idMunicipio = ConstantesGestorFIP.IdMunicipio = 
						new Integer(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)).intValue();
				}
				catch (Exception e)
				{
					String mensaje = "Valor de id municipio no valido:" + e.toString() + aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID);
					mostrarMensajeDialogo(mensaje);
					System.out.println(mensaje);
					logger.error(mensaje);
					System.exit(-1);
				}
			}
			catch(RemoteException excepcionDeInvocacion){
			System.exit(-1);
			return false;
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
			CAuthDialog auth = new CAuthDialog(this, true,
					ConstantesGestorFIP.loginFIPUrl,ConstantesGestorFIP.idApp,
					ConstantesGestorFIP.IdMunicipio, aplicacion.getI18NResource());
			auth.setBounds(30, 60, 315, 155);            
			auth.show();

			setPolicy();
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

	public void setPolicy() throws AclNotFoundException, Exception{
		com.geopista.security.SecurityManager.setHeartBeatTime(5000);
		GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("GestorFip");
		applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
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
				return false;
			
			ConstantesGestorFIP.principal = principal;
			setPermisos(acl.getPermissions(principal));
			if (!tienePermisos("LocalGis.EIEL.Login"))
				return false;

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
				permisos.put(permissionName, "");
		}
		ConstantesGestorFIP.permisos = permisos;
	}

	/**
	 * Funcion que comprueba si el usuario tiene permisos para una tarea en especial.
	 */
	private boolean tienePermisos(String permiso) {
		return (permisos.containsKey(permiso));
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
	           if(!SSOAuthManager.isSSOActive()) 
	        	   com.geopista.security.SecurityManager.logout();
	        }catch(Exception e){
	        	logger.warn("Exception: " + e.toString());
	        };
	       System.exit(0);
	}



	/*Main de la aplicacion*/
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		LocalGISGestorFip aux = new LocalGISGestorFip();
	}


	/**
	 * Funcion que inicializa el menu principal de la aplicacion.
	 */
	private JMenuBar getJMenuBarLocalGISGestorFip()
	{
		if(jMenuBarGestorFip==null)
		{
			jMenuBarGestorFip = new JMenuBar();
			jMenuBarGestorFip.setEnabled(false);

			jMenuBarGestorFip.add(getJMenuImportarFip());
			jMenuBarGestorFip.add(getjMenuAbrir());
			jMenuBarGestorFip.add(getjMenuConfiguracion());
			jMenuBarGestorFip.setBorderPainted(false);

		}
		return jMenuBarGestorFip;
	}


	public JMenu getjMenuAbrir() {
		if (jMenuAbrir == null)
		{
			jMenuAbrir = new JMenu();
			jMenuAbrir.setText(I18N.get("LocalGISGestorFip","gestorFip.menu.gestorfip"));
			jMenuAbrir.add(getJMenuAbrirPlaneamiento());
			jMenuAbrir.setEnabled(false);
		}
		return jMenuAbrir;
	}

	public JMenu getjMenuConfiguracion() {
		
		if (jMenuConfiguracion == null)
		{
			jMenuConfiguracion = new JMenu();
			jMenuConfiguracion.setText(I18N.get("LocalGISGestorFip","gestorFip.menu.configuracion"));
			jMenuConfiguracion.add(getJMenuConfiguracion());
			jMenuConfiguracion.setEnabled(false);
		}
		return jMenuConfiguracion;
	}


	public void setjMenuAbrir(JMenu jMenuAbrir) {
		this.jMenuAbrir = jMenuAbrir;
	}
	
	
	private void configuracionGestor(){
		
		final JFrame desktop= (JFrame)this;
		final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.report(I18N.get("LocalGISGestorFip", "gestorFip.cargandodatos"));
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
							
							final ConfiguracionGestorDialog configuracionGestor = new ConfiguracionGestorDialog(desktop, aplicacion);
							
							configuracionGestor.getAceptarJButton().addActionListener(new ActionListener()
							{
								public void actionPerformed(java.awt.event.ActionEvent evt)
								{
									
									try {
										//guardar en BBDD la version seleccionada
										ConfiguracionGestor config = configuracionGestor.getConfigSelected();
										ClientGestorFip.guardarConfiguracionGestorFip(config, aplicacion);
										configuracionGestor.dispose();
										
										aplicacion.getBlackboard().put(ConstantesGestorFIP.CONFIG_VERSION_CONSOLE_UER, config);
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
								
									}
								}
								
							});

							llamadaAInternalFrame(configuracionGestor, true, true, false,new Dimension(300,120));
							 int posx = (aplicacion.getMainFrame().size().width - configuracionGestor.getWidth())/2;
							int posy = (aplicacion.getMainFrame().size().height -configuracionGestor.getHeight())/2;
							configuracionGestor.setLocation(posx, posy);
							
							
							configuracionGestor.addInternalFrameListener(new javax.swing.event.InternalFrameListener()
					        {
					            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
					            {
					            }
					            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
					            {
					            }
					            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
					            {
					            	showAccesoComun();
					                
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
		
		
	}
	
	private void abrirPlaneamiento(){
	
		final JFrame desktop= (JFrame)this;
		final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.report(I18N.get("LocalGISGestorFip", "gestorFip.abrirfip.buscandoFip"));
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
							
							accesoComunDialog.dispose();

							String codAmbito = (String) aplicacion.getBlackboard().get(ConstantesGestorFIP.AMBITO_TRABAJO);

							final ArrayList<FipPanelBean> lstFips = obtenerListaFipsAmbito(codAmbito, progressDialog);

							if(lstFips != null && !lstFips.isEmpty()){
								FipPanelBean fipSelected = (FipPanelBean)lstFips.get(0);
								aplicacion.getBlackboard().put(ConstantesGestorFIP.FIP_TRABAJO, fipSelected);
								establecerDatosBarraStatus();
								GestorFipUtils.menuBarSetEnabled(true, aplicacion.getMainFrame());
								//accionAbrirGestorPlaneamiento();
								GestorPlaneamientoUtils.inicializarIdiomaGestorPlaneamientoPanels();
								FipPanelBean fipSeleccionado = (FipPanelBean)aplicacion.getBlackboard().get(ConstantesGestorFIP.FIP_TRABAJO);
								showGestorPlaneamientoMainPanel(fipSeleccionado);
							}						
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
		
		
	
	}

	private void showAccesoComun(){
		
		JFrame desktop= (JFrame)this;
		this.accesoComunDialog = new AccesoComunDialog(desktop, aplicacion);
		
		llamadaAInternalFrame(accesoComunDialog, true, true, false,new Dimension(600,180));
		int posx = (aplicacion.getMainFrame().size().width - this.accesoComunDialog.getWidth())/2;
		int posy = (aplicacion.getMainFrame().size().height - this.accesoComunDialog.getHeight())/2;
		this.accesoComunDialog.setLocation(posx, posy);
	
		this.accesoComunDialog.getImportFipConsoleButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				accionImportarFipConsola();
				
			}
		});
		
		this.accesoComunDialog.getImportFipArchivoButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				accionInsertarFicheroFIPDisco();
				
			}
		});
		
		this.accesoComunDialog.getGestorFipButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{			
				abrirPlaneamiento();

			}
		});

	}

	private JMenuItem getJMenuAbrirPlaneamiento(){
		
		
		JMenuItem abrirFipMenuItem = new JMenuItem(I18N.get("LocalGISGestorFip","gestorFip.submenu.sincronizarabrirfip"));
		abrirFipMenuItem.setEnabled(true);
		
		abrirFipMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				abrirPlaneamiento();	
			}
		});
		
		return abrirFipMenuItem;
		
	}
	
	
	private void accionAbrirGestorPlaneamiento(){
		GestorPlaneamientoUtils.inicializarIdiomaGestorPlaneamientoPanels();
		FipPanelBean fipSeleccionado = (FipPanelBean)aplicacion.getBlackboard().get(ConstantesGestorFIP.FIP_TRABAJO);
		showGestorPlaneamientoMainPanel(fipSeleccionado);
	}

	private JMenuItem getJMenuConfiguracion(){
		
		
		JMenuItem configuracionMenuItem = new JMenuItem(I18N.get("LocalGISGestorFip","gestorFip.submenu.configuracion"));
		configuracionMenuItem.setEnabled(true);
	
		configuracionMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				configuracionGestor();	
			}
		});
	
		return configuracionMenuItem;

	}

	private JMenu getJMenuImportarFip()
	{
		if (jMenuImportarFip == null)
		{
			jMenuImportarFip  = new JMenu();
			jMenuImportarFip.setEnabled(false);
			jMenuImportarFip.setText(I18N.get("LocalGISGestorFip","gestorFip.menu.importarFip"));
		
			jMenuImportarFip.setEnabled(false);	
			jMenuImportarFip.add(getJMenuItemImportarFicheroFipConsola());
			jMenuImportarFip.add(getJMenuItemImportarFicheroFipArchivo());

		}
		return jMenuImportarFip;
	}
		
	private JMenuItem getJMenuItemImportarFicheroFipArchivo()
	{
		if (jMenuItemImportarFicheroFip1Disco  == null)
		{
			jMenuItemImportarFicheroFip1Disco  =
				new JMenuItem(I18N.get("LocalGISGestorFip","gestorFip.submenu.importarFicheroFipArchivo"));
			jMenuItemImportarFicheroFip1Disco.setEnabled(true);
			jMenuItemImportarFicheroFip1Disco.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					accionInsertarFicheroFIPDisco();
				}
			});
		}
		return jMenuItemImportarFicheroFip1Disco;
	}
	

	private void accionImportarFipConsola(){
		accesoComunDialog.dispose();
		Integer  idAmbitoTrabajo = Integer.valueOf((String)aplicacion.getBlackboard().get(ConstantesGestorFIP.AMBITO_TRABAJO));
		// se obtiene de la consola el ultimo fip
		FicheroFipConsoleBean fichFipConsoleBean = buscarImportarFicherosFip(idAmbitoTrabajo.intValue());
		
		if(fichFipConsoleBean != null){
			try{
				// se comprueba si en la BBDD existe el ultimo fip.
				String fechaConsolidacionFIPBBDD = ClientGestorFip.obtenerFechaConsolidacionFip(idAmbitoTrabajo.intValue(), aplicacion);
								
				String date_BBDD = null;
				if(fechaConsolidacionFIPBBDD != null){
					java.sql.Date dateBBDD = new java.sql.Date(0);
					dateBBDD = dateBBDD.valueOf(fechaConsolidacionFIPBBDD);
					
					java.sql.Date dateConsole = new java.sql.Date(0);
					dateConsole = dateBBDD.valueOf(fichFipConsoleBean.getFecha());
					date_BBDD = dateBBDD.toString();
				}
				
				insertarFicheroFIP(idAmbitoTrabajo.intValue(), date_BBDD, fichFipConsoleBean);
				

			}
			catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex.getStackTrace());
				ErrorDialog.show(desktopPane, "ERROR", "ERROR", StringUtil.stackTrace(ex));
			}
		}
		else{
			StringBuffer txt = new StringBuffer(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.noencontrado"));
			txt.append("\n").append(I18N.get("LocalGISGestorFip","gestorFip.acceso.municpio")).append(aplicacion.getBlackboard().get(ConstantesGestorFIP.MUNICIPIO_TRABAJO))
				.append("\n").append(I18N.get("LocalGISGestorFip","gestorFip.acceso.ambito")).append(String.valueOf(idAmbitoTrabajo));
			
			mostrarMensajeDialogo(txt.toString());
			GestorFipUtils.menuBarSetEnabled(true, aplicacion.getMainFrame());
		}
		showAccesoComun();
	}
	

	private JMenuItem getJMenuItemImportarFicheroFipConsola()
	{
		if (jMenuItemImportarFicheroFip1  == null)
		{
			jMenuItemImportarFicheroFip1  =
				new JMenuItem(I18N.get("LocalGISGestorFip","gestorFip.submenu.importarFicheroFipConsola"));
			jMenuItemImportarFicheroFip1.setEnabled(true);
			jMenuItemImportarFicheroFip1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					accionImportarFipConsola();
				}
			});
		}
		return jMenuItemImportarFicheroFip1;
	}
	
	

	private void cargarDatosApliacionGestorFip(){
		
		final JFrame desktop= (JFrame)this;
		final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.report(I18N.get("LocalGISGestorFip", "gestorFip.cargandodatosgestor"));
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
		
							boolean configCorrecta = false;
							try {
								int IdMunicipio = 
										new Integer(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)).intValue();
							
								List lstMunicipios = AppContext.getAlMunicipios();
								for (Iterator iterator = lstMunicipios.iterator(); iterator
										.hasNext();) {
									Municipio muni = (Municipio) iterator.next();
									if(muni.getId() == IdMunicipio){
										aplicacion.getBlackboard().put(ConstantesGestorFIP.MUNICIPIO_TRABAJO,	muni.getNombreOficial());
									}
								}
								
								String codAmbitoTrabajo = ClientGestorFip.obtenerAmbitoTrabajo(aplicacion, IdMunicipio);
								if(codAmbitoTrabajo != null && !codAmbitoTrabajo.equals("")){
									aplicacion.getBlackboard().put(ConstantesGestorFIP.AMBITO_TRABAJO,	codAmbitoTrabajo);
									configCorrecta = true;
								}
								else{
									configCorrecta = false;
									GestorFipUtils.menuBarSetEnabled(false, aplicacion.getMainFrame());
									JOptionPane.showMessageDialog(desktopPane, I18N.get("LocalGISGestorFip", "gestorFip.acceso.noexistemuni"));
								}
							} catch (Exception e) {
								ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
								e.printStackTrace();
							}
							finally{
								if(configCorrecta){
									GestorFipUtils.menuBarSetEnabled(true, aplicacion.getMainFrame());
								}
								else{
									GestorFipUtils.menuBarSetEnabled(false, aplicacion.getMainFrame());
								}
							}
		
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
	}
	
	public static String detectEncode(InputStream is) throws java.io.IOException {
	    byte[] buf = new byte[4096];

	    UniversalDetector detector = new UniversalDetector(null);

	    int nread;
	    while ((nread = is.read(buf)) > 0 && !detector.isDone()) {
	      detector.handleData(buf, 0, nread);
	    }
	    detector.dataEnd();

	    String encoding = detector.getDetectedCharset();

	    detector.reset();
	    return encoding;
	  }
	
	private void accionInsertarFicheroFIPDisco( ){
		
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
		StringBuffer sbMsg = null;

		progressDialog.setTitle(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.obteniendofipconsola.title"));
	    progressDialog.report(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.obteniendofipconsola"));
		
		progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){

                    	URL urlfip;
						try {
							accesoComunDialog.dispose();
							FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
							JFileChooser fileChooser = new JFileChooser();
							fileChooser.setFileFilter(filter);
							int result = fileChooser.showOpenDialog(aplicacion.getMainFrame());
							if (result == JFileChooser.APPROVE_OPTION) {

								File selectedFile = fileChooser.getSelectedFile();

								InputStream iS =new FileInputStream(selectedFile.getAbsoluteFile());
								InputStream inputStream =new FileInputStream(selectedFile.getAbsoluteFile());
								
								String encoding = detectEncode(inputStream);
								//String encoding = detectEncoding(inputStream);
								//String encoding = "ISO-8859-1";
								BufferedReader bReader = new BufferedReader(new InputStreamReader(iS, encoding));
				                StringBuffer sbfFileContents = new StringBuffer();
				                String line = null;
					               
				                //read file line by line
				                while( (line = bReader.readLine()) != null){
				                        sbfFileContents.append(line);
				                }     

				                String fechaXML = ClientGestorFip.ObtenerFechaFipXML(sbfFileContents.toString(), encoding, aplicacion);
				                Integer  idAmbitoTrabajo = Integer.valueOf((String)aplicacion.getBlackboard().get(ConstantesGestorFIP.AMBITO_TRABAJO));
				             // se comprueba si en la BBDD existe el ultimo fip.
								String fechaConsolidacionFIPBBDD = ClientGestorFip.obtenerFechaConsolidacionFip(idAmbitoTrabajo.intValue(), aplicacion);
								boolean insertar = false;
								if(fechaConsolidacionFIPBBDD != null){
									java.sql.Date dateFIPBBDD = new java.sql.Date(0);
									dateFIPBBDD = dateFIPBBDD.valueOf(fechaConsolidacionFIPBBDD);
									
									java.sql.Date dateFipXML = new java.sql.Date(0);
									dateFipXML = dateFipXML.valueOf(fechaXML);
									
									StringBuffer sbMsg = new StringBuffer(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.fechaconsolidacion")+": "+dateFipXML+"\n");
									sbMsg.append(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.encontrado"));
									sbMsg.append(" ").append(dateFIPBBDD).append(" ");
									sbMsg.append(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.ambito")).append(" ").append(idAmbitoTrabajo);
									sbMsg.append("\n").append(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.realizarimportacion"));

									Object[] options = { "OK", "CANCEL" };
									
									int n =JOptionPane.showOptionDialog(null,
											sbMsg.toString(),
								            "",
								            JOptionPane.YES_NO_OPTION,
								            JOptionPane.QUESTION_MESSAGE,
								            null,
								            options,
								            options[1]);
									
									if(n == JOptionPane.YES_OPTION){
								    	   insertar = true;
									}
							       	else{
								    	   insertar = false;
							       	}
								}
								else{
									insertar = true;
								}
								
								// se inserta los datos del fip, unicamente si la fecha del fichero xml es posterior a la fecha
								// de refundido del ultimo fip del municipio.
								if(insertar){
									migracionInsercion(sbfFileContents.toString(), encoding, progressDialog);
					                borrarDatos();
								}
							}
							GestorFipUtils.menuBarSetEnabled(true, aplicacion.getMainFrame());
							showAccesoComun();
				
						} 
						catch (Exception e) {
								e.printStackTrace();
								logger.error(e.getStackTrace());
//								ErrorDialog.show(desktopPane, "ERROR", "ERROR", StringUtil.stackTrace(e));
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
	
	}
	
	private void migracionInsercion(String xml, String encoding, TaskMonitorDialog progressDialog) throws Exception{
		
		boolean wasFinishPressed = true;
		final Integer  idAmbito = Integer.valueOf((String)aplicacion.getBlackboard().get(ConstantesGestorFIP.AMBITO_TRABAJO));
		
		ConfLayerBean[] lstConfLayerBean  = ClientGestorFip.obtenerDatosMigracionAsistida(xml, encoding, aplicacion);
        aplicacion.getBlackboard().put(ConstantesGestorFIP.GESTORFIP_DATA_MIGRACION_ASISTIDA, lstConfLayerBean);
        wasFinishPressed = openWizard ( new WizardPanel[] {
                new MigracionAsistidaDeterminacionesPanel("MigracionAsistidaDeterminacionesPanel", "MigracionAsistidaValoresPanel"),
                new MigracionAsistidaValoresPanel("MigracionAsistidaValoresPanel", "MigracionAsistidaUsosPanel"),
                new MigracionAsistidaUsosPanel("MigracionAsistidaUsosPanel", "MigracionAsistidaRegulacionesPanel"),
                new MigracionAsistidaRegulacionesPanel("MigracionAsistidaRegulacionesPanel", null)}, true, 950, 700);

        if(wasFinishPressed){
        		// se realiza la importacion
        		progressDialog.report(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.insertandofip"));
        		String resultImport = ClientGestorFip.insertarFIP1(idAmbito, xml,  "", encoding,  aplicacion, AppContext.getIdEntidad());
        		
        		if(resultImport.equals(ConstantesGestorFIP.SUCCESS_CODE)){
					progressDialog.dispose();

					aplicacion.getBlackboard().put(ConstantesGestorFIP.FIP_TRABAJO, null);
					establecerDatosBarraStatus();
					
					JOptionPane.showMessageDialog(desktopPane,I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.exitoimportacion"));
				}
				else{
					progressDialog.dispose();
					
					aplicacion.getBlackboard().put(ConstantesGestorFIP.FIP_TRABAJO, null);
					establecerDatosBarraStatus();
					
					JOptionPane.showMessageDialog(desktopPane,I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.noexitoimportacion"));
				}
        }
	}
	
	private void insertarFicheroFIP(final int idAmbito, final String dateConsolidacionFipBBDD, final FicheroFipConsoleBean fichFipConsoleBean){

			final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
			
			StringBuffer sbMsg = null;
			if(fichFipConsoleBean != null){
				
				if(dateConsolidacionFipBBDD != null){
					sbMsg = new StringBuffer(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.fechaconsolidacion")+": "+fichFipConsoleBean.getFecha()+"\n");
					sbMsg.append(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.encontrado"));
					sbMsg.append(" ").append(dateConsolidacionFipBBDD).append(" ");
					sbMsg.append(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.ambito")).append(" ").append(idAmbito);
					sbMsg.append("\n").append(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.realizarimportacion"));
				}
				else{
					sbMsg = new StringBuffer(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.fechaconsolidacion")+": "+fichFipConsoleBean.getFecha()+"\n");
					sbMsg.append("\n").append(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.realizarimportacion"));
				}
				
				Object[] options = { "OK", "CANCEL" };
				int n =JOptionPane.showOptionDialog(null, sbMsg.toString(),
			            "",
			            JOptionPane.YES_NO_OPTION,
			            JOptionPane.QUESTION_MESSAGE,
			            null,
			            options,
			            options[1]);
				
				if(n == JOptionPane.YES_OPTION){	  

					 StringBuffer sb = new StringBuffer(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.consultadofipconsola")).append(" ")
			        	.append(I18N.get("LocalGISGestorFip","gestorFip.acceso.municpio")).append(":").append(aplicacion.getBlackboard().get(ConstantesGestorFIP.MUNICIPIO_TRABAJO))
			        	.append(I18N.get("LocalGISGestorFip","gestorFip.acceso.ambito")).append(":").append(idAmbito);
			      
					progressDialog.setTitle(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.obteniendofipconsola.title"));
				    progressDialog.report(sb.toString());
					
					progressDialog.addComponentListener(new ComponentAdapter(){
		                public void componentShown(ComponentEvent e){
		                    new Thread(new Runnable(){
		                        public void run(){

		                        	URL urlfip;
									try {
										StringBuffer sbfFileContents = null;
										String encoding = "ISO-8859-1";
//										String encoding = "UTF-8";
										String xmlDoc = null;
										 
										HttpClient httpclient = new HttpClient();
										GetMethod method = new GetMethod(fichFipConsoleBean.getUrlFip());
										httpclient.executeMethod(method);
										ConfiguracionGestor config = (ConfiguracionGestor)aplicacion.getBlackboard().get(ConstantesGestorFIP.CONFIG_VERSION_CONSOLE_UER);
		                            	VersionesUER[] versiones= (VersionesUER[])aplicacion.getBlackboard().get(ConstantesGestorFIP.VERSIONES_CONSOLE_UER);
		                            	for(int i=0; i<versiones.length; i++){
		                            		 if(versiones[i].getId() == config.getIdVersion()){
		                            			 if(versiones[i].getVersion() == ConstantesGestorFIP.VERSIONCONSOLA_UER_1_86){
		                            				//Consola 1.86v
		                            				 xmlDoc = method.getResponseBodyAsString();
		                            				
		                            				 InputSource is = new InputSource( new ByteArrayInputStream(xmlDoc.getBytes(encoding)) );
		                            				 InputStream inputStream = is.getByteStream();
		                            				 BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
		                            				  sbfFileContents = new StringBuffer();
		                            				 String line = null;
		                            				 //read file line by line
		                            				 while( (line = bReader.readLine()) != null){
		     						                        sbfFileContents.append(line);
		                            				 }
		                            				 
		                            				 migracionInsercion(sbfFileContents.toString(), encoding, progressDialog);
		                            			 }
		                            			 else  if(versiones[i].getVersion() == ConstantesGestorFIP.VERSIONCONSOLA_UER_2_00){
		                            				 //Consola 2.00v
		                            				 final int BUFFER = 1;
		                            				 
		                            				 InputStream in = new BufferedInputStream(method.getResponseBodyAsStream());  
		                            				 ZipInputStream zis = new ZipInputStream(in);
		                            				 ZipEntry entry;
		                            				 while((entry = zis.getNextEntry()) != null) {
		                            					 
	                            					    int count;
	                            					    byte data[] = new byte[BUFFER];
	                            					    sbfFileContents = new StringBuffer();
	                            					    while ((count = zis.read(data, 0, BUFFER)) != -1) {
	     
	                            					    	 InputSource is = new InputSource( new ByteArrayInputStream(data) );
				                            				 InputStream inputStream = is.getByteStream();
				                            				 BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
				                            				 String line = null;
				                            				 //read file line by line
				                            				 while( (line = bReader.readLine()) != null){
				     						                        sbfFileContents.append(line);
				                            				 }
	                            					    }
	                            					
	                            					    migracionInsercion(sbfFileContents.toString(), encoding, progressDialog);
		                            				 }
		                            			 }
		                            		 }
		                            	}
										GestorFipUtils.menuBarSetEnabled(true, aplicacion.getMainFrame());
										borrarDatos();
									} catch (MalformedURLException e) {
										e.printStackTrace();
										logger.error(e.getStackTrace());
										ErrorDialog.show(desktopPane, "ERROR", "ERROR", StringUtil.stackTrace(e));
									}
									 catch (IOException e) {
											e.printStackTrace();
											logger.error(e.getStackTrace());
											ErrorDialog.show(desktopPane, "ERROR", "ERROR", StringUtil.stackTrace(e));
									}
									
									catch (Exception e) {
											e.printStackTrace();
											logger.error(e.getStackTrace());
											ErrorDialog.show(desktopPane, "ERROR", "ERROR", StringUtil.stackTrace(e));
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
		     
			    } 	
				else {
					if(aplicacion.getBlackboard().get(ConstantesGestorFIP.FIP_TRABAJO) == null){
						GestorFipUtils.menuBarSetEnabled(true, aplicacion.getMainFrame());
					}
					else{
						GestorFipUtils.menuBarSetEnabled(true, aplicacion.getMainFrame());
					}
				}
			}
			else{
				sbMsg = new StringBuffer(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.noencontrado"));
				sbMsg.append("\n").append(I18N.get("LocalGISGestorFip","gestorFip.acceso.municpio")).append(aplicacion.getBlackboard().get(ConstantesGestorFIP.MUNICIPIO_TRABAJO));
					
				mostrarMensajeDialogo(sbMsg.toString());
			}
	}
	
	private void borrarDatos(){
		aplicacion.getBlackboard().put(ConstantesGestorFIP.FIP_TRABAJO, null);
		establecerDatosBarraStatus();
	}
	
	private FicheroFipConsoleBean buscarImportarFicherosFip(final int idAmbito) 
    {
		GestorFipUtils.menuBarSetEnabled(false, aplicacion.getMainFrame());

		final ArrayList<FicheroFipConsoleBean> lstFichFip = new ArrayList<FicheroFipConsoleBean>();

		try {
			final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
		                .getMainFrame(), null);
			
	        progressDialog.setTitle(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.consultadofipconsola.title"));
	        StringBuffer sb = new StringBuffer(I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.consultadofipconsola")).append(" ")
	        	.append(I18N.get("LocalGISGestorFip","gestorFip.acceso.municpio")).append(aplicacion.getBlackboard().get(ConstantesGestorFIP.MUNICIPIO_TRABAJO)).append(" ")
	        	.append(I18N.get("LocalGISGestorFip","gestorFip.acceso.ambito")).append(idAmbito);
	      
	        progressDialog.report(sb.toString());

	        progressDialog.addComponentListener(new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    new Thread(new Runnable(){
                        public void run(){

                        	try {
                        		FicheroFipConsoleBean fichFipConsoleBean = null;
                        		ConfiguracionGestor config = (ConfiguracionGestor)aplicacion.getBlackboard().get(ConstantesGestorFIP.CONFIG_VERSION_CONSOLE_UER);
                            	 VersionesUER[] versiones= (VersionesUER[])aplicacion.getBlackboard().get(ConstantesGestorFIP.VERSIONES_CONSOLE_UER);
                            	 for(int i=0; i<versiones.length; i++){
                            		 if(versiones[i].getId() == config.getIdVersion()){
                            			 if(versiones[i].getVersion() == ConstantesGestorFIP.VERSIONCONSOLA_UER_1_86){
                            				//Consola 1.86v
                            				 ClientConsole_v1_86 clientConsole  = new ClientConsole_v1_86();
                            				 fichFipConsoleBean = clientConsole.obtenerFechaRefundido(aplicacion, idAmbito);
                            			 }
                            			 else  if(versiones[i].getVersion() == ConstantesGestorFIP.VERSIONCONSOLA_UER_2_00){
                            				 //Consola 2.00v
                            				 ClientConsole_v2_00 clientConsole  = new ClientConsole_v2_00();
                            				 fichFipConsoleBean = clientConsole.obtenerFechaRefundido(aplicacion, idAmbito);
                            			 }
                            		 }
                            	 }
                        		
                        		lstFichFip.add(fichFipConsoleBean);
							} catch (Exception e) {
								e.printStackTrace();
								logger.error(e.getStackTrace());
								ErrorDialog.show(desktopPane, "ERROR", "ERROR", StringUtil.stackTrace(e));
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getStackTrace());
			ErrorDialog.show(desktopPane, "ERROR", "ERROR", StringUtil.stackTrace(e));
		}
		return lstFichFip.get(0);
    }


	
	
	
	private void showGestorPlaneamientoMainPanel( final FipPanelBean fip)
	{	
		final ArrayList lst = new ArrayList();
		final JFrame desktop= (JFrame)this;
		final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.report(I18N.get("LocalGISGestorFip","gestorFip.planeamiento.consultadodatos"));
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

							Container c = LocalGISGestorFip.this.getContentPane();
							c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							GestorFipGestorPlaneamientoPanel gestorFipGestorPlaneamiento = getEditorGestorPlaneamiento(fip, progressDialog);
							if(gestorFipGestorPlaneamiento != null){
								lst.add(gestorFipGestorPlaneamiento);
								JInternalFrame jif = encapsulaEnJInternalFrame(	gestorFipGestorPlaneamiento, 
										I18N.get("LocalGISGestorFip","gestorFip.planeamiento.planeamiento.title"));
								llamadaAInternalFrame(jif, false, true, true);
								c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
								c.setCursor(Cursor.getDefaultCursor());
								jif.setMaximizable(false);
								jif.addInternalFrameListener(new javax.swing.event.InternalFrameListener()
						        {
						            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
						            {
						            }
						            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
						            {
						            }
						            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
						            {
						            	showAccesoComun();
						                
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
						
							}
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
		
		ModuloPlaneamientoGestorFip panelPlaneamiento = (ModuloPlaneamientoGestorFip)getJPanelModuloPlaneamientoGestorFip();
		GestorFipModuloPlaneamientoMapPanel panelPlaneamientoMap =(GestorFipModuloPlaneamientoMapPanel) panelPlaneamiento.getGeopistaEditorMapPanel();
		panelPlaneamientoMap.setGestorFipGestorPlaneamiento(jPanelEditorAsociacioneDeterminacionesEntidades);
		panelPlaneamientoMap.initEditor();   
	}
	
	
	
	private ModuloPlaneamientoGestorFip getJPanelModuloPlaneamientoGestorFip(){
		if (jPanelModuloPlaneamientoGestroFip  == null) {						
			jPanelModuloPlaneamientoGestroFip = new ModuloPlaneamientoGestorFip();    
		}		
		return jPanelModuloPlaneamientoGestroFip;
	}
	

	private ArrayList obtenerListaFipsAmbito(String codAmbito, TaskMonitorDialog progressDialog){

		try {
			// primero se comprueba que no existe ninguna actualizacion en la consola de UER.
			Integer  idAmbitoTrabajo = Integer.valueOf((String)aplicacion.getBlackboard().get(ConstantesGestorFIP.AMBITO_TRABAJO));
			// se obtiene de la consola el ultimo fip
			FicheroFipConsoleBean fichFipConsoleBean = buscarImportarFicherosFip(idAmbitoTrabajo.intValue());
			// se comprueba si en la BBDD existe el ultimo fip.
			String fechaConsolidacionFIPBBDD = ClientGestorFip.obtenerFechaConsolidacionFip(idAmbitoTrabajo.intValue(), aplicacion);

			if(fechaConsolidacionFIPBBDD != null){
				java.sql.Date dateBBDD = new java.sql.Date(0);
				dateBBDD = dateBBDD.valueOf(fechaConsolidacionFIPBBDD);

				if(fichFipConsoleBean != null){
					java.sql.Date dateConsole = new java.sql.Date(0);
					dateConsole = dateBBDD.valueOf(fichFipConsoleBean.getFecha());
					if(dateConsole.compareTo(dateBBDD) > 0){

						if(tienePermisos("LocalGIS.GestorFip.Importador")){
							insertarFicheroFIP(idAmbitoTrabajo.intValue(), dateBBDD.toString(), fichFipConsoleBean);
						}
						else{
							StringBuffer sbMsg = null;
							sbMsg = new StringBuffer(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.consutauserimportador"));
							progressDialog.dispose();
							JOptionPane.showMessageDialog(this, sbMsg.toString());
						}
					}
				}

			}
			else{
				
				// no se ha encontrado ningun fip para el ambito, es la primera insercion
				if(tienePermisos("LocalGIS.GestorFip.Importador")){
					insertarFicheroFIP(idAmbitoTrabajo.intValue(), null, fichFipConsoleBean);
				}
				else{
					StringBuffer sbMsg = null;
					sbMsg = new StringBuffer(I18N.get("LocalGISGestorFip", "gestorFip.importar.ficherofip.consutauserimportador"));
					JOptionPane.showMessageDialog(this, sbMsg.toString());
				}
			}
			return ClientGestorFip.obtenerLstFips(codAmbito, aplicacion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorDialog.show(desktopPane, "ERROR", "ERROR", StringUtil.stackTrace(e));
			return null;
		}
		
	}

	
	

	/**
	 * 
	 */
	private void establecerDatosBarraStatus(){
		
		JLabel titleMunicipioLabel = new JLabel();
		JLabel municipioLabel = new JLabel();

		JLabel titleFechaLabel = new JLabel();
		JLabel fechaLabel = new JLabel();
		
		JLabel titleCodigoLabel = new JLabel();
		JLabel codigoLabel = new JLabel();
		
		
		
		titleMunicipioLabel.setText(
				I18N.get("LocalGISGestorFip", "gestorFip.statusmenu.municipio"));
		titleMunicipioLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
		
		titleFechaLabel.setText(
				I18N.get("LocalGISGestorFip", "gestorFip.statusmenu.fecha"));
		titleFechaLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
	
		titleCodigoLabel.setText(
				I18N.get("LocalGISGestorFip", "gestorFip.statusmenu.codigo"));
		titleCodigoLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
		
		FipPanelBean fip = (FipPanelBean)aplicacion.getBlackboard().get(ConstantesGestorFIP.FIP_TRABAJO);
		
		if(fip != null){
			municipioLabel.setText(fip.getMunicipio());
			municipioLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
			
			fechaLabel.setText(fip.getFechaConsolidacion());
			fechaLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
		
			codigoLabel.setText(fip.getTramitePlaneamientoVigente().getCodigo());
			codigoLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
		
		
			getjPanelStatusTramite().removeAll();
			getjPanelStatusTramite().add(titleMunicipioLabel,
					new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 10, 0, 0), 0, 0));
			getjPanelStatusTramite().add(municipioLabel,
					new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 3, 0, 0), 0, 0));
			
			getjPanelStatusTramite().add(titleFechaLabel,
					new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 10, 0, 0), 0, 0));
			getjPanelStatusTramite().add(fechaLabel,
					new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 3, 0, 0), 0, 0));
			
			getjPanelStatusTramite().add(titleCodigoLabel,
					new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 10, 0, 0), 0, 0));
			getjPanelStatusTramite().add(codigoLabel,
					new GridBagConstraints(5, 0, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 3, 0, 0), 0, 0));
		}
		else{
			getjPanelStatusTramite().removeAll();
			getjPanelStatusTramite().repaint();
			
		}
	}

	
	/**
	 * @param fip
	 * @param progressDialog
	 * @return
	 */
	private GestorFipGestorPlaneamientoPanel getEditorGestorPlaneamiento( FipPanelBean fip, TaskMonitorDialog progressDialog){
		fip = OperacionesDeterminaciones.consultaDeterminacionesAsociadasFip_GestorPlaneamiento(fip, aplicacion, progressDialog);
		if (fip != null){
			if(progressDialog.isCancelRequested())
				return null;
			fip = OperacionesEntidades.consultaEntidadesAsociadasFip_GestorPlaneamiento(fip, aplicacion,progressDialog, true);
			if (fip != null){
				if(progressDialog.isCancelRequested())
					return null;
			}

		jPanelEditorAsociacioneDeterminacionesEntidades  = new GestorFipGestorPlaneamientoPanel(fip);    
		}
		return jPanelEditorAsociacioneDeterminacionesEntidades;
		
	}
	

	/**
	 * @param wp
	 * @param resize
	 * @return
	 */
	protected boolean openWizard( WizardPanel[] wp, boolean resize, int width , int height)
 	{
		boolean wasFinishPressed = false;
		WizardDialog d = new WizardDialog(this, I18N.get("LocalGISGestorFip","gestorFip.importar.ficherofip.migracionasistida"),
	            null);
		
	    d.init(wp);
           
	    // Set size after #init, because #init calls #pack. [Jon Aquino]
	    d.setSize(width, height);
	    d.getContentPane().remove(d.getInstructionTextArea());
	    GUIUtil.centreOnWindow(d);
	    d.setVisible(true);
	    
	    if (d.wasFinishPressed()){
	    	wasFinishPressed = true;
	    	
	    }
	    return wasFinishPressed;
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
		return ConstantesGestorFIP.idApp;
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
