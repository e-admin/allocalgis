/**
 * LocalGISEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.acl.AclNotFoundException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
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
import javax.swing.Timer;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.dominios.CDominiosFrame;
import com.geopista.app.administrador.estructuras.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistro;
import com.geopista.app.eiel.dialogs.EIELListaTablaVersionDialog;
import com.geopista.app.eiel.dialogs.ExportCuadrosMPTDialog;
import com.geopista.app.eiel.dialogs.ExportMPTDialog;
import com.geopista.app.eiel.dialogs.MapasTematicosDialog;
import com.geopista.app.eiel.dialogs.ValidateMPTDialog;
import com.geopista.app.eiel.images.IconLoader;
import com.geopista.app.eiel.panels.EIELEditorPanel;
import com.geopista.app.eiel.panels.EditorPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.GeopistaMapPanel;
import com.geopista.app.eiel.panels.ShowMapPanel;
import com.geopista.app.eiel.update.UpdatePadronDialog;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.app.utilidades.ProcessCancel;
import com.geopista.editor.GeopistaEditor;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.protocol.control.ISesion;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.ISecurityPolicy;
import com.geopista.security.SecurityManager;
import com.geopista.security.TransparentLogin;
import com.geopista.security.connect.ConnectionStatus;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.ui.plugin.MunicipalitiesListener;
import com.geopista.ui.plugin.reload.ReloadMapPlugIn;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.exportshp.dialogs.ExportarSHPDialog;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.plugin.InstallStandardFeatureTextWritersPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class LocalGISEIEL extends CMain implements IMainLocalGISEIEL, ILocalGISMap,ISecurityPolicy {

	static Logger logger = Logger.getLogger(LocalGISEIEL.class);
	
	private static final String NAME_RESOURCE = "LocalGISEIEL";
	
	private Hashtable permisos = new Hashtable();
	private boolean fromInicio;

	private String _idApp = "Eiel";
	private String _urlEiel = "";
	private String _urlEielServlet = "";
	
	private ApplicationContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private JDesktopPane desktopPane;
	//rivate JPanel jPanelStatus = null;
	public static final int DIM_X = 800;
	public static final int DIM_Y = 600;
	public static final Rectangle PICTURE_BORDER = new Rectangle(15, 5, 100,440);
	public static final String LOCALGIS_LOGO = "geopista.gif";
	private HelpBroker hb = null;
	private Blackboard blackboard = null;
	private JLabel connectionLabel = new JLabel();
	private JLabel helpLabel = new JLabel();
	private JMenuBar jMenuBarEIEL = null;
	private JMenu jMenuMapasEIEL = null;
	private JMenu jMenuGenerationMPT = null;
	private JMenu jMenuDominios = null;
	private JMenu jMenuMapasTematicos = null;
	private JMenu jMenuAyuda = null;
	private JMenu jMenuPermisos = null;
	private JMenu jMenuLogout = null;
	private JMenuItem contenidoAyudaMenuItem = null;
	private JMenuItem sugerenciasMenuItem = null;
	private JMenuItem recargarPermisosMenuItem = null;
	private JMenuItem contenidoLogoutMenuItem = null;
	private JMenuItem jMenuItemAcercaDe = null;
	private JMenuItem jMenuItemCargarMapaEIEL = null;
	private JMenuItem jMenuItemExportarMPTEIEL = null;
	private JMenuItem jMenuItemValidarMPTEIEL = null;
	private JMenuItem jMenuItemExportarSHPMPTEIEL = null;
	private JMenuItem jMenuItemActualizarPadron = null;
	private JMenuItem jMenuItemExportarCuadrosMPTEIEL = null;
	private JMenuItem jMenuItemEditorDominiosEIEL = null;
	private JMenuItem jMenuItemVersionesEIEL = null;
	//rivate Municipio municipio;

	private static LocalGISEIEL localLocalGISEIEL;

	private static boolean cancelada = false;
	JInternalFrame jif;
	
	boolean edicionCargada=false;
	boolean globalCargado=false;
	
	ConnectionStatus status;

    private InstallStandardFeatureTextWritersPlugIn installStandardFeatureTextWritersPlugIn = new InstallStandardFeatureTextWritersPlugIn();
	public LocalGISEIEL() {

		this(false);
		localLocalGISEIEL = this;
	}

	public LocalGISEIEL(boolean fromInicio) {
		logger.info("Arrancando aplicacion de eiel");
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		_urlEiel = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.EIEL_WEBAPP_NAME;
		aplicacion.setUrl(_urlEiel);
		
		loadResourceBundle ();
		InitEIEL.init();
		this.fromInicio = fromInicio;
		localLocalGISEIEL = this;
		aplicacion.setMainFrame(this);
		blackboard = aplicacion.getBlackboard();
		
		try {initLookAndFeel(); }catch (Exception e){}
		try {

			SplashWindow splashWindow = showSplash();
			configureApp();
			inicializaElementos();
			desactivarMenu();

			setExtendedState(JFrame.MAXIMIZED_BOTH);

			this.setIconImage(IconLoader.icon(LOCALGIS_LOGO).getImage());
			setVisible(true);

			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			this.setTitle(I18N.get("LocalGISEIEL", "localgiseiel.frame.title"));

			if (splashWindow != null)
				splashWindow.setVisible(false);

			// ******************
			// Mostramos la pantalla de autenticación del usuario.
			// ******************
			//Antes de autenticar al usuario hacemos un logout
			//if (SecurityManager.getIdSesion()!=null)
			//	SecurityManager.logout();
			
			//Thread.sleep(2000);
			
			SSOAuthManager.ssoAuthManager(_idApp);
			if (!AppContext.getApplicationContext().isOnlyLogged()){  
				/*if (fromInicio){
					if (!showAuth()){
						dispose();                	
						return;
					}
				}
				else{
					showAuth();
				}*/
				if (fromInicio) {
					if (!new TransparentLogin().showAuth(this,fromInicio,false,false,false)) {
						dispose();
						return;
					}
				} else {
					new TransparentLogin().showAuth(this,fromInicio,false,false,false);
				}
			}
			setPolicy();

			/*if (!AppContext.seleccionarMunicipio((Frame) this)) {
				stopApp();
				return;
			}*/
			
			// Buscamos el mapa de la EIEL
			int idMapa;
			String mapaEIEL = System.getProperty("mapaEIELReducido");

			
			
			if ((mapaEIEL != null) && (mapaEIEL.equals("true"))) {
				
				idMapa = InitEIEL.clienteLocalGISEIEL.getIdMapa(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL_REDUCIDO);
				ConstantesLocalGISEIEL.MAPA_EIEL = idMapa;
			} else {

				
				try {
						idMapa = InitEIEL.clienteLocalGISEIEL.getIdMapa(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL);
						if (idMapa != -1) {
							ConstantesLocalGISEIEL.MAPA_EIEL = idMapa;
						}
						
												
						idMapa = InitEIEL.clienteLocalGISEIEL.getIdMapa(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL_GLOBAL);
						if (idMapa != -1) {
							ConstantesLocalGISEIEL.MAPA_EIEL_GLOBAL = idMapa;
						}
						else{
							ConstantesLocalGISEIEL.MAPA_EIEL_GLOBAL = ConstantesLocalGISEIEL.MAPA_EIEL;	
						}
						
						
						idMapa = InitEIEL.clienteLocalGISEIEL.getIdMapa(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL_COMPLETO);
						if (idMapa != -1) {
							ConstantesLocalGISEIEL.MAPA_EIEL_COMPLETO = idMapa;
						}
						else{
							ConstantesLocalGISEIEL.MAPA_EIEL_COMPLETO = ConstantesLocalGISEIEL.MAPA_EIEL;	
						}

					} catch (Exception e) {
					e.printStackTrace();
				}
			}

			LocalGISEIELUtils.inicializarConstantesMunicipio();
			
			mostrarCabeceraAplicacion();

			try {
				com.geopista.app.eiel.UtilidadesComponentes.inicializar();
				com.geopista.app.eiel.UtilidadesComponentes.setEnabled(false,this);
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			}

			AppContext.showMunicipiosEntidad();
			// UtilidadesComponentes.setEnabled(true, (JFrame)this);
			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			activarMenu();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("ERROR:" + sw.toString());
			logger.error("Exception: " + sw.toString());
			ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
		}
	}
	
	private void loadResourceBundle  () {
		Locale locale = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n", locale, this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put(NAME_RESOURCE, bundle);
	}
	
	private boolean showAuth() {
		try  {
			boolean resultado=false;
			CAuthDialog auth = new CAuthDialog(this, true, _urlEiel, _idApp, ConstantesLocalGISEIEL.IdEntidad, aplicacion.getI18NResource());
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
			JOptionPane optionPane = new JOptionPane("Error al inicializar: \n" +((e.getMessage()!= null && e.getMessage().length()>=0)?e.getMessage():e.toString()), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.show();
			return false;
		}
	}
	
	/**
	 * Solicita la entidad y el Municipio
	 * solicitarMunicipio (Para la EIEL en el caso de global no se pide el municipio)
	 */
	private boolean solicitarEntidadyMunicipio(boolean solicitarMunicipio){
		
		boolean comprobarUsuarioYaAutenticado=false;
		
		status.setSolicitarEntidad(true);
		
		if (!AppContext.seleccionarMunicipio((Frame) this,solicitarMunicipio,comprobarUsuarioYaAutenticado)) {
			stopApp();
			return false;		
		}
		LocalGISEIELUtils.inicializarConstantesMunicipio();
		try {
			getJFrame().setTitle(I18N.get("LocalGISEIEL", "localgiseiel.frame.title"));
			mostrarCabeceraAplicacion();
		} catch (Exception e) {

		}
		return true;
	}
	
	/**
	 * Mostramos la cabecera de la aplicacion
	 * @throws Exception
	 */
	private void mostrarCabeceraAplicacion() throws Exception{
		// Cargamos la provincia y el municipio
		/** cargamos el municipio y la provincia */
		Municipio municipio = (new OperacionesAdministrador(_urlEiel).getMunicipio(new Integer(ConstantesRegistro.IdMunicipio).toString()));
		if (municipio != null) {
			ConstantesLocalGISEIEL.Municipio = municipio.getNombre();
			ConstantesLocalGISEIEL.Provincia = municipio.getProvincia();
			setTitle(getTitle() + " - " + ConstantesLocalGISEIEL.Municipio
					+ "-" + ConstantesRegistro.IdMunicipio + " ("
					+ ConstantesLocalGISEIEL.Provincia + ")" + "  Usuario: " + 	SecurityManager.getPrincipal().getName());
			

			System.setProperty("CodigoIne", municipio.getId());
						
			if(!ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL)){
				setTitle(getTitle() + " - " + "Usuario validador");
    		}  
			else{
				setTitle(getTitle() + " - " + "Usuario publicador (Ayto)");
			}
		}
	}

	private void stopApp() {
		// En el caso de la EIEL dejamos seguir
		cancelada=false;
		//InitEIEL.clienteLocalGISEIEL = new LocalGISEIELClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)	+ "/EIELServlet");
		
		
		/*JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
				"Inicio de aplicación cancelado. Reinicie el aplicativo");
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		jMenuEdicionEIEL.setEnabled(false);
		jMenuBarEIEL.setEnabled(false);
		// jMenuMapasEIEL.setEnabled(false);
		jMenuGenerationMPT.setEnabled(false);
		jMenuDominios.setEnabled(false);
		jMenuMapasTematicos.setEnabled(false);
		jMenuAyuda.setEnabled(false);*/
	}
	
	private void restartApp(){
		try {
			// ******************
			// Mostramos la pantalla de autenticación del usuario.
			// ******************
			SSOAuthManager.ssoAuthManager(_idApp);
			if (!AppContext.getApplicationContext().isOnlyLogged()){  
				if (fromInicio){
					if (!showAuth()){
						dispose();                	
						return;
					}
				}
				else{
					showAuth();
				}	
			}
			setPolicy();

			// AppContext.seleccionarMunicipio((Frame)this);
			if (!AppContext.seleccionarMunicipio((Frame) this,false,false)) {
				stopApp();
				return;
			}
		} catch (AclNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Funcion que inicializa la gui de la pantalla principal, asociandole
	 * eventos para que tenga un tamaño minimo y la accion de cierre.
	 */
	private void inicializaElementos() {

		desktopPane = new JDesktopPane();
		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(Toolkit.getDefaultToolkit()
				.getScreenSize());
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

		

		//Gestion de conexiones y desconexiones contra el administrador
		//de cartografia.
		status=new ConnectionStatus(this,fromInicio);
		status.init();		
		aplicacion.getBlackboard().put(UserPreferenceConstants.CONNECT_STATUS,status);
		
		
		// Creacion de objetos.
		getContentPane().add(getJPanelStatus(status.getJPanelStatus()), BorderLayout.SOUTH);
		
		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

		setJMenuBar(getJMenuBarLocalGISEIEL());
		pack();

		// Evento para que la ventana al minimizar tenga un tamaño minimo.
		addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				LocalGISEIELUtils.ajustaVentana(aplicacion.getMainFrame(),
						DIM_X, DIM_Y + 50);
			}
		});

		// Evento al cerrar la ventana.
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});


	}

	
	public JLabel getConnectionLabel(){
		return connectionLabel;
	}	
	public ApplicationContext getAplicacion(){
		return aplicacion;
	}	
	public String getIdApp(){
		return _idApp;
	}
	public String getIdMunicipio(){
		return ConstantesLocalGISEIEL.idMunicipio;
	}
	public String getLogin(){
		return _urlEiel;
	}
	public JFrame getFrame(){
		return this;
	}


	

	private JMenuItem logMenuItem = null;
	private JMenu jMenuEdicionEIEL = null;
	private JMenuItem jMenuItemEdicionEIEL = null;
	private JMenuItem jMenuItemEdicionEIELGlobal = null;
	private EditorPanel jPanelEditorEIEL = null;
	private EIELEditorPanel jPanelEIEL = null;
	private ShowMapPanel jPanelMap = null;
	/*
	 * private JMenuItem jMenuItemCargarMapaProvincialEIEL = null; private
	 * JMenuItem jMenuItemCargarMapaComarcalEIEL = null; private JMenuItem
	 * jMenuItemCargarMapaMunicipalEIEL = null; private JMenuItem
	 * jMenuItemCargarMapaInfraestructurasEIEL = null;
	 */

	/*
	 * private JMenuItem jMenuItemCargarMapaTematicoViviendas = null;
	 * 
	 * private JMenuItem jMenuItemCargarMapaTematicoPlaneamiento = null; private
	 * JMenuItem jMenuItemCargarMapaTematicoDepositos1 = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoDepositos2 = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoPavimentacion1 = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoPavimentacion2 = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoAlumbrado = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoCaptaciones = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoPotabilizacion = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoDepositosSint = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoDistribucion = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoSaneamiento = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoDepuracion = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoPavimentacionSint = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoAlumbradoSint = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoBasuras = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoResiduos = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoCultura = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoDeportes = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoZonasVerdes = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoAdministrativo = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoGlobal = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoCentrosSanitarios1 = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoCentrosSanitarios2 = null; private JMenuItem
	 * jMenuItemCargarMapaTematicoCentrosSanitarios3 = null;
	 */
	private JMenuItem jMenuItemCalcularIndices;
	/*
	 * private JMenu jMenuMapasMunicipales; private JMenu jMenuMapasComarcales;
	 */

	// Comentado SATEC
	/*
	 * private JMenuItem jMenuItemCargarMapaComarcalViviendas; private JMenuItem
	 * jMenuItemCargarMapaComarcalPlaneamiento; private JMenuItem
	 * jMenuItemCargarMapaComarcalDepositos1; private JMenuItem
	 * jMenuItemCargarMapaComarcalDepositos2; private JMenuItem
	 * jMenuItemCargarMapaComarcalPavimentacion1; private JMenuItem
	 * jMenuItemCargarMapaComarcalPavimentacion2; private JMenuItem
	 * jMenuItemCargarMapaComarcalAlumbrado; private JMenuItem
	 * jMenuItemCargarMapaComarcalCentrosSanitarios1; private JMenuItem
	 * jMenuItemCargarMapaComarcalCentrosSanitarios2; private JMenuItem
	 * jMenuItemCargarMapaComarcalPotabilizacion; private JMenuItem
	 * jMenuItemCargarMapaComarcalCaptaciones; private JMenuItem
	 * jMenuItemCargarMapaComarcalDepositosSint; private JMenuItem
	 * jMenuItemCargarMapaComarcalDistribucion; private JMenuItem
	 * jMenuItemCargarMapaComarcalSaneamiento; private JMenuItem
	 * jMenuItemCargarMapaComarcalDepuracion; private JMenuItem
	 * jMenuItemCargarMapaComarcalPavimentacionSint; private JMenuItem
	 * jMenuItemCargarMapaComarcalAlumbradoSint; private JMenuItem
	 * jMenuItemCargarMapaComarcalBasuras; private JMenuItem
	 * jMenuItemCargarMapaComarcalResiduos; private JMenuItem
	 * jMenuItemCargarMapaComarcalCultura; private JMenuItem
	 * jMenuItemCargarMapaComarcalDeportes; private JMenuItem
	 * jMenuItemCargarMapaComarcalZonasVerdes; private JMenuItem
	 * jMenuItemCargarMapaComarcalAdministrativo; private JMenuItem
	 * jMenuItemCargarMapaComarcalGlobal;
	 */
	/*
	 * private JMenu jMenuMapasObras; private JMenuItem
	 * jMenuItemCargarMapaObrasInversion; private JMenuItem
	 * jMenuItemCargarMapaObrasHabitantes; private JMenuItem
	 * jMenuItemCargarMapaObrasTotal; private JMenuItem jMenuItemCalcularObras;
	 * private JMenu jMenuMapasObrasMunicipales; private JMenu
	 * jMenuMapasObrasComarcales; private JMenuItem
	 * jMenuItemCargarMapaComarcalObrasInversion; private JMenuItem
	 * jMenuItemCargarMapaComarcalObrasHabitantes; private JMenuItem
	 * jMenuItemCargarMapaComarcalObrasTotal;
	 */

	private JMenuItem jMenuItemCargarMapaTematicoPorNucleo = null;

	/**
	 * This method initializes logMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getLogMenuItem() {
		if (logMenuItem == null) {
			logMenuItem = new JMenuItem();
			logMenuItem.setEnabled(aplicacion.isOnline());
			logMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (aplicacion.isLogged())
						aplicacion.logout();
					else
						aplicacion.login();
				}
			});
		}
		return logMenuItem;
	}

	/**
	 * Añadimos adicionalmente mas informacion al panel de estado
	 * @param jPanelStatus
	 * @return
	 */
	private JPanel getJPanelStatus(JPanel jPanelStatus) {
		if (jPanelStatus != null) {			

			helpLabel.setIcon(IconLoader.icon("help.gif"));
			helpLabel.setToolTipText(aplicacion.getI18nString("geopista.Help"));
			helpLabel.addMouseListener(new MouseListener() {

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

			jPanelStatus.add(helpLabel, new GridBagConstraints(5, 0, 1, 1, 0.0,
					0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelStatus;
	}

	public boolean mostrarAyudaActionPerformed() {

		if (hb == null) {
			HelpSet hs = null;
			ClassLoader loader = this.getClass().getClassLoader();
			URL url;
			try {
				String helpSetFile = "help/eiel/LocalGISEIELHelp_"
						+ ConstantesLocalGISEIEL.Locale.substring(0, 2) + ".hs";
				url = HelpSet.findHelpSet(loader, helpSetFile);
				if (url == null) {
					url = new URL("help/eiel/LocalGISEIELHelp_"
							+ ConstantesLocalGISEIEL.LocalCastellano.substring(
									0, 2) + ".hs");
				}
				hs = new HelpSet(loader, url);

				// ayuda sensible al contexto
				hs.setHomeID(ConstantesLocalGISEIEL.helpSetHomeID);

			} catch (Exception ex) {
				logger.error("Exception: " + ex.toString());

				return false;
			}
			hb = hs.createHelpBroker();

			hb.setDisplayed(true);
			new CSH.DisplayHelpFromSource(hb);
		} else {
			hb.setDisplayed(true);
		}

		return true;
	}
	
	public boolean sugerenciasActionPerformed() {
		com.geopista.app.sugerencias.SugerenciasForm sform = new com.geopista.app.sugerencias.SugerenciasForm();
		sform.setVisible(true);
		return true;
	}

	public void recargarPermisosActionPerformed() {		
		try {
			GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("EIEL");
			applySecurityPolicy(acl,com.geopista.security.SecurityManager.getPrincipal());
			getJFrame().setTitle(I18N.get("LocalGISEIEL", "localgiseiel.frame.title"));
			mostrarCabeceraAplicacion();
		} catch (AclNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Muestra un dialogo con el mensaje de advertencia msg pasado por
	 * parametro.
	 */
	private void mostrarMensajeDialogo(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	protected void openWizard(WizardPanel[] wp, boolean resize,
			boolean isAdjustable) {
		if (aplicacion.isOnline()) {
			WizardComponent d = new WizardComponent(aplicacion, "", null);
			d.init(wp);

			// Elimina el panel blanco con título que aparece en la zona
			// superior de la pantalla
			d.setWhiteBorder(false);
			final JInternalFrame JIFrame = encapsulaEnJInternalFrame(d, null);

			d.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					String command = e.getActionCommand();
					if ("canceled".equals(command)
							|| "finished".equals(command)) {
						cierraInternalFrame(JIFrame);
					}
				}
			});

			Dimension dim = null;
			if (wp[0] instanceof JComponent) {
				dim = ((JComponent) wp[0]).getSize();
				if (dim.getHeight() == 0 && dim.getWidth() == 0)
					dim = null;
			}

			llamadaAInternalFrame(JIFrame, resize, true, isAdjustable, dim);
			if (dim != null)
				JIFrame.setSize(dim);
		} else {
			JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
					.getI18nString("mensaje.no.conectado.a.la.base.datos"));
		}
	}

	private JInternalFrame encapsulaEnJInternalFrame(Component comp,
			String title) {
		final JInternalFrame JIFrame = new JInternalFrame();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		LocalGISEIELUtils.menuBarSetEnabled(false, this);
		
		//Activamos algunos menus que puede ser interesante disponer de ellos.
		//jMenuDominios.setEnabled(true);
		jMenuAyuda.setEnabled(true);
		if (jMenuPermisos!=null)
			jMenuPermisos.setEnabled(true);
		if (title != null) {
			JIFrame.setTitle(title);
		}
		JScrollPane internalFrameScrollPane = new JScrollPane();
		internalFrameScrollPane.setViewportView(comp);
		JIFrame.getContentPane().add(internalFrameScrollPane);
		JIFrame.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
			public void internalFrameOpened(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameClosing(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameClosed(
					javax.swing.event.InternalFrameEvent evt) {
				cierraInternalFrame(JIFrame);
			}

			public void internalFrameIconified(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameDeiconified(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameActivated(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameDeactivated(
					javax.swing.event.InternalFrameEvent evt) {
			}
		});
		JIFrame.setResizable(true);
		JIFrame.pack();
		JIFrame.setVisible(true);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		return JIFrame;
	}

	private void cierraInternalFrame(JInternalFrame JIFrame) {
		if (desktopPane.getAllFrames().length > 0) {
			desktopPane.getDesktopManager().closeFrame(JIFrame);
			desktopPane.repaint();
		}

		LocalGISEIELUtils.menuBarSetEnabled(true, this);
	}

	/**
	 * Funcion que muestra la JInternalFrame pasada como parametro. En caso de
	 * que el valor de isMaxTam sea true la internalFrame sera maximizada, y si
	 * no, no. En caso de que el valor unica sea true, se comprueba si hay otra
	 * ventana abierta, si es asi no hace nada, sino muestra la internalFrame
	 * pasada por parametro. Si el valor de unica es false se muestra aunque
	 * haya otra ventana.
	 */
	private void llamadaAInternalFrame(JInternalFrame internalFrame,
			boolean isMaxTam, boolean unica, boolean isAdjustable) {
		llamadaAInternalFrame(internalFrame, isMaxTam, unica, isAdjustable,
				null);
	}

	private void llamadaAInternalFrame(JInternalFrame internalFrame,
			boolean isMaxTam, boolean unica, boolean isAdjustable, Dimension dim) {
		try {
			desktopPane.setCursor(Cursor
					.getPredefinedCursor(Cursor.WAIT_CURSOR));

			int numInternalFrames = desktopPane.getAllFrames().length;
			if (numInternalFrames == 0 || !unica) {
				internalFrame.setFrameIcon(new javax.swing.ImageIcon(IconLoader
						.icon(LOCALGIS_LOGO).getImage()));
				desktopPane.add(internalFrame);
				if (isMaxTam) {
					desktopPane.getDesktopManager()
							.maximizeFrame(internalFrame);
				}

				internalFrame.setClosable(true);
				internalFrame.setMaximum(isAdjustable);

				if (dim != null && isMaxTam) {
					internalFrame.setSize(dim);
				}

				internalFrame.show();

			}
			desktopPane.setCursor(Cursor
					.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Metodo que inicializa los parametros principales de la aplicacion para
	 * conectarse con el servidor, inicializa el locale, y actualiza el
	 * idMunicipio segun las variables de entorno.
	 */
	private boolean configureApp() {
		try {
			try {
				// ConstantesLocalGISEIEL.servletIntentosUrl =
				// aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"licencias/CServletLicencias";
				// ConstantesLocalGISEIEL.loginEIELUrl =
				// aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"eiel";
				// com.geopista.protocol.CConstantesComando.adminCartografiaUrl
				// =
				// aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"geopista/";

				String urlServidorBase = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL);
				_urlEielServlet = _urlEiel + ServletConstants.EIEL_SERVLET_NAME;
				com.geopista.protocol.CConstantesComando.adminCartografiaUrl = urlServidorBase + WebAppConstants.GEOPISTA_WEBAPP_NAME;
				
				InitEIEL.clienteLocalGISEIEL = new LocalGISEIELClient(_urlEielServlet);
				//System.out.println("ruta EIELServlet: " + _urlEielServlet);

				ConstantesLocalGISEIEL.Locale = aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES");
				// try{
				// Constantes.IdEntidad=new
				// Integer(aplicacion.getString("geopista.DefaultEntityId")).intValue();
				// }catch (Exception e){
				// JOptionPane.showMessageDialog(this,
				// "Valor de id municipio no valido:"+e.toString()+aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
				// System.out.println("Valor de id municipio no valido:"+e.toString()+aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
				// logger.error("Valor de id municipio no valido:"+e.toString()+aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
				// if (fromInicio)
				// dispose();
				// else
				// System.exit(-1);
				// }

			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				JOptionPane.showMessageDialog(this,
						"Excepcion al cargar el fichero de configuración:\n"
								+ sw.toString());
				logger.error("Exception: " + sw.toString());
				if (fromInicio)
					dispose();
				else
					System.exit(-1);
				return false;
			}

			/** Establecemos el idioma especificado en la configuracion */
			setTitle("");
			return true;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}
	
	private void addFixedPlugins(){
    	
    	try {
    		
    		GeopistaEditor editor=((GeopistaEditorPanel)getJPanelEditorEIEL().getGeopistaEditorPanel()).getEditor();
    		    		    			
    		installStandardFeatureTextWritersPlugIn.initialize(new PlugInContext(editor.getContext(), null, null,
                    null, null));
			WorkbenchToolBar newToolBar=editor.addToolbar(aplicacion.getString("MunicipalitiesPlugIn.category"));
						
		
			PlugIn cargandoPlugin = (PlugIn) (Class.forName("com.geopista.ui.plugin.MunicipalitiesPlugIn")).newInstance();		       
			editor.addPlugIn(cargandoPlugin,null);
			
			editor.addPlugIn("com.geopista.ui.plugin.reload.ReloadMapPlugIn");
			editor.addPlugIn("com.geopista.ui.plugin.reload.ReloadLayerPlugIn");		
			//newToolBar.addCursorTool(aplicacion.getI18nString("VisualizarFeatures"), (CursorTool)(Class.forName("com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicFenceTool")).newInstance());
			//newToolBar.addCursorTool(aplicacion.getI18nString("VisualizarUnaFeature"), (CursorTool)(Class.forName("com.vividsolutions.jump.workbench.ui.cursortool.UpdateDynamicClipTool")).newInstance());
			
			
			
			((JPanel)editor.getToolBar().getParent()).setLayout(new GridLayout(2,3));						
			//TODO (Borders)
			((JPanel)editor. getToolBar().getParent()).setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
	
			
			//********************************
			//Listener de cambios de municipio
			//********************************
			((com.geopista.ui.plugin.MunicipalitiesPlugIn)cargandoPlugin).addMunicipalitiesListener(new MunicipalitiesListener()
	        {

	            public void municipalitiesChanged(String idMunicipio)
	            {
	            	try {
						LocalGISEIELUtils.actualizarInformacionMunicipioSeleccionado(idMunicipio,getJPanelEditorEIEL());
						getJFrame().setTitle(I18N.get("LocalGISEIEL", "localgiseiel.frame.title"));
						mostrarCabeceraAplicacion();
						
						int[] rows=((com.geopista.app.eiel.panels.EditingInfoPanel)jPanelEIEL.getJPanelEditingInfo()).getJPanelTree().getTree().getSelectionRows();
						((com.geopista.app.eiel.panels.EditingInfoPanel)jPanelEIEL.getJPanelEditingInfo()).getJPanelTree().getTree().setSelectionRow(0);
						((com.geopista.app.eiel.panels.EditingInfoPanel)jPanelEIEL.getJPanelEditingInfo()).getJPanelTree().getTree().setSelectionRow(rows[0]);
					} catch (Exception e) {
						logger.error("Error al cambiar de municipio",e);
					}
	               
	            }
	        });
    		
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
		private void addFixedPluginsLocal() {
			try {
				GeopistaEditor editor = GeopistaEditorPanel.getEditor();
				installStandardFeatureTextWritersPlugIn
						.initialize(new PlugInContext(editor.getContext(), null,
								null, null, null));
				editor.addPlugIn("com.geopista.ui.plugin.reload.ReloadLayerPlugIn");
			} catch (Exception e) {
				e.printStackTrace();
			}

    }

	public void setPolicy() throws AclNotFoundException, Exception {
		com.geopista.security.SecurityManager.setHeartBeatTime(10000);
		GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("EIEL");
		applySecurityPolicy(acl,com.geopista.security.SecurityManager.getPrincipal());
	}

	
	public void resetSecurityPolicy() {
	}

	/**
	 * Comprueba los permisos del usuario.
	 */
	public boolean applySecurityPolicy(GeopistaAcl acl,
			GeopistaPrincipal principal) {
		try {
			if ((acl == null) || (principal == null)) {
				return false;
			}
			InitEIEL.principal = principal;
			setPermisos(acl.getPermissions(principal));
			if (!tienePermisos(ConstantesLocalGISEIEL.PERM_LOGIN_EIEL)) {
				noPermApp();
				return false;				
			}

			// Si tiene permisos de visualizacion, lo dejamos visible al
			// usuario
			if (tienePermisos(ConstantesLocalGISEIEL.PERM_VERSION_READ))
				getJMenuItemVersion().setVisible(true);
			getJMenuItemVersion().setEnabled(true);

			return true;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}

	}

	 private void noPermApp(){
	    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"No tiene permisos para entrar. Se cerrará el aplicativo");
	    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    	 System.exit(1);
	    }
	/**
	 * Crea hash de permisos del usuario.
	 */
	private void setPermisos(Enumeration e) {
		permisos = new Hashtable();
		if (e!=null){
			while (e.hasMoreElements()) {
				GeopistaPermission geopistaPermission = (GeopistaPermission) e
						.nextElement();
				String permissionName = geopistaPermission.getName();
				if (!permisos.containsKey(permissionName)) {
					permisos.put(permissionName, "");
				}
			}
		}
		ConstantesLocalGISEIEL.permisos = permisos;
		aplicacion.getBlackboard().put(UserPreferenceConstants.PERMISOS,permisos);
	}

	/**
	 * Funcion que comprueba si el usuario tiene permisos para una tarea en
	 * especial.
	 */
	private boolean tienePermisos(String permiso) {
		if (permisos.containsKey(permiso)) {
			return true;
		}
		return false;
	}

	/**
	 * Acciones realizadas cuando se cierra la aplicacion.
	 * 
	 * @param evt
	 *            El evento capturado.
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {
		try {
			if (!SSOAuthManager.isSSOActive())
				com.geopista.security.SecurityManager.logout();
		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString());
		}
		if (fromInicio)
			dispose();
		else
			exit(0,1000);
	}
    public static void exit(final int status, long maxDelayMillis) {
      try {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new java.util.TimerTask() {
          @Override
          public void run() {
            Runtime.getRuntime().halt(status);
          }
        }, maxDelayMillis);
        System.exit(status);
      } catch (Throwable ex) {
        Runtime.getRuntime().halt(status);
      } finally {
        Runtime.getRuntime().halt(status);
	}
    }

	/* Main de la aplicacion */

	public static void main(String args[]) {
		
		//Inicializamos una instancia AppContextMap
		//AppContextMap.initAppContextMap();			
		//LocalGISEIEL aux = new LocalGISEIEL();
		new LocalGISEIEL();
	}

	/**
	 * Funcion que inicializa el menu principal de la aplicacion.
	 */
	private JMenuBar getJMenuBarLocalGISEIEL() {
		if (jMenuBarEIEL == null) {
			jMenuBarEIEL = new JMenuBar();

			jMenuBarEIEL.add(getJMenuEdicionEIEL());
			// jMenuBarEIEL.add(getJMenuMapasEIEL());
			jMenuBarEIEL.add(getJMenuMapasTematicos());
			jMenuBarEIEL.add(getJMenuDominios());
			jMenuBarEIEL.add(getJMenuGenerationMPT());
			jMenuBarEIEL.add(getJMenuAyuda());
			//jMenuBarEIEL.add(getJMenuLogout());
			String mostrarMenuPermisos = System.getProperty("mostrarMenuPermisos");
			if ((mostrarMenuPermisos != null) && (mostrarMenuPermisos.equals("true"))) {
				jMenuBarEIEL.add(getJMenuPermisos());
			}
			jMenuBarEIEL.setBorderPainted(false);
		}
		return jMenuBarEIEL;
	}



	private JMenu getJMenuAyuda() {
		if (jMenuAyuda == null) {
			jMenuAyuda = new JMenu();
			jMenuAyuda.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.menu.ayuda"));
			jMenuAyuda.add(getJMenuItemAyuda());
			jMenuAyuda.add(getJMenuItemSugerencias());
			jMenuAyuda.add(getJMenuItemAcercaDe());
		}
		return jMenuAyuda;
	}
	private JMenu getJMenuPermisos() {
		if (jMenuPermisos== null) {
			jMenuPermisos = new JMenu();
			jMenuPermisos.setText("Permisos");
			jMenuPermisos.add(getJMenuItemRecargarPermisos());
		}
		return jMenuPermisos;
	}

	private JMenuItem getJMenuItemAyuda() {
		if (contenidoAyudaMenuItem == null) {
			contenidoAyudaMenuItem = new JMenuItem(I18N.get("LocalGISEIEL",
					"localgiseiel.submenu.contenidoayuda"));
			contenidoAyudaMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							mostrarAyudaActionPerformed();
						}
					});
		}
		return contenidoAyudaMenuItem;
	}
	
	private JMenuItem getJMenuItemSugerencias() {
		if (sugerenciasMenuItem == null) {
			sugerenciasMenuItem = new JMenuItem("Sugerencias/Incidencias");
			sugerenciasMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							sugerenciasActionPerformed();
						}
					});
		}
		return sugerenciasMenuItem;
	}
	private JMenuItem getJMenuItemRecargarPermisos() {
		if (recargarPermisosMenuItem == null) {
			recargarPermisosMenuItem = new JMenuItem("Recargar Permisos");
			recargarPermisosMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							recargarPermisosActionPerformed();
						}
					});
		}
		return recargarPermisosMenuItem;
		
	}
	
	private JMenu getJMenuLogout() {
		if (jMenuLogout == null) {
			jMenuLogout = new JMenu();
			jMenuLogout.setText("Administracion");
			jMenuLogout.add(getJMenuItemLogut());
		}
		return jMenuLogout;
	}
	

	private JMenuItem getJMenuItemLogut() {
		if (contenidoLogoutMenuItem == null) {
			contenidoLogoutMenuItem = new JMenuItem("Logout");
			
			contenidoLogoutMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							try {
								SecurityManager.logout();
								SSOAuthManager.clearRegistrySesion();
								getJPanelEIEL().restartEditingPanel();
								restartApp();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
		return contenidoLogoutMenuItem;
	}
	

	private JMenuItem getJMenuItemAcercaDe() {
		if (jMenuItemAcercaDe == null) {
			jMenuItemAcercaDe = new JMenuItem(I18N.get("LocalGISEIEL",
					"localgiseiel.submenu.acercade"));
			jMenuItemAcercaDe
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							aboutJMenuItemActionPerformed();
						}
					});
		}
		return jMenuItemAcercaDe;
	}

	private void aboutJMenuItemActionPerformed() {

		if (desktopPane.getAllFramesInLayer(JDesktopPane
				.getLayer(new JInternalFrame())).length > 0)
			return;
		desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		CAcercaDeJDialog acercaDe = new CAcercaDeJDialog(this, true);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		acercaDe.setSize(310, 240);
		acercaDe.setLocation(d.width / 2 - acercaDe.getSize().width / 2,
				d.height / 2 - acercaDe.getSize().height / 2);
		acercaDe.setResizable(false);
		acercaDe.show();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		desktopPane
				.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * This method initializes jMenuImportacion
	 * 
	 * @return javax.swing.JMenu
	 */
	/*
	 * private JMenu getJMenuMapasEIEL() { if (jMenuMapasEIEL == null) {
	 * jMenuMapasEIEL = new JMenu();
	 * jMenuMapasEIEL.setText(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.menu.mapseiel"));
	 * jMenuMapasEIEL.add(getJMenuItemCargarMapaProvincial());
	 * jMenuMapasEIEL.add(getJMenuItemCargarMapaComarcal());
	 * jMenuMapasEIEL.add(getJMenuItemCargarMapaMunicipal());
	 * 
	 * } return jMenuMapasEIEL; }
	 */
	private JMenu getJMenuGenerationMPT() {
		if (jMenuGenerationMPT == null) {
			jMenuGenerationMPT = new JMenu();
			jMenuGenerationMPT.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.menu.exportmpteiel"));
			jMenuGenerationMPT.add(getJMenuItemValidarMPT());
			jMenuGenerationMPT.add(getJMenuItemExportarMPT());
			jMenuGenerationMPT.add(getJMenuItemExportarCuadrosMPT());
			jMenuGenerationMPT.add(getJMenuItemExportarSHPMPT());
			jMenuGenerationMPT.add(getJMenuItemActualizacionPadron());
		}
		return jMenuGenerationMPT;
	}

	private JMenu getJMenuEdicionEIEL() {
		if (jMenuEdicionEIEL == null) {
			jMenuEdicionEIEL = new JMenu();
			jMenuEdicionEIEL.setText(I18N.get("LocalGISEIEL","localgiseiel.menu.editioneiel"));
			jMenuEdicionEIEL.add(getJMenuItemEdicionEIEL());
			jMenuEdicionEIEL.add(getJMenuItemEdicionEIELGlobal());
			jMenuEdicionEIEL.add(getJMenuItemVersion());

		}
		return jMenuEdicionEIEL;
	}

	private JMenuItem getJMenuItemVersion() {
		if (jMenuItemVersionesEIEL == null) {
			jMenuItemVersionesEIEL = new JMenuItem(I18N.get("LocalGISEIEL",
					"localgiseiel.submenu.contenidoversion"));
			jMenuItemVersionesEIEL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					
					if (!solicitarEntidadyMunicipio(true)) return;
					EIELListaTablaVersionDialog dialog = new EIELListaTablaVersionDialog();
					dialog.setVisible(true);

				}
			});
			jMenuItemVersionesEIEL.setEnabled(false);
			jMenuItemVersionesEIEL.setVisible(false);

		}
		return jMenuItemVersionesEIEL;
	}

	/**
	 * This method initializes jMenuDominios
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenuDominios() {
		if (jMenuDominios == null) {
			jMenuDominios = new JMenu();
			jMenuDominios.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.menu.dominios.editor"));
			jMenuDominios.add(getJMenuItemEditorDominiosEIEL());

		}
		return jMenuDominios;
	}

	/*
	 * private JMenu getJMenuMapasMunicipales(){
	 * 
	 * if (jMenuMapasMunicipales == null){
	 * 
	 * jMenuMapasMunicipales = new JMenu();
	 * jMenuMapasMunicipales.setText(I18N.get
	 * ("LocalGISEIEL","localgiseiel.menu.municipalmap"));
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoViviendas());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoPlaneamiento());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDepositos1());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDepositos2());
	 * jMenuMapasMunicipales
	 * .add(getJMenuItemCargarMapaTematicoPavimentacion1());
	 * jMenuMapasMunicipales
	 * .add(getJMenuItemCargarMapaTematicoPavimentacion2());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoAlumbrado());
	 * jMenuMapasMunicipales
	 * .add(getJMenuItemCargarMapaTematicoCentrosSanitarios1());
	 * jMenuMapasMunicipales
	 * .add(getJMenuItemCargarMapaTematicoCentrosSanitarios2());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoCaptaciones());
	 * jMenuMapasMunicipales
	 * .add(getJMenuItemCargarMapaTematicoPotabilizacion());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDepositosSint());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDistribucion());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoSaneamiento());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDepuracion());
	 * jMenuMapasMunicipales
	 * .add(getJMenuItemCargarMapaTematicoPavimentacionSint());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoAlumbradoSint());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoBasuras());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoResiduos());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoCultura());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoDeportes());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoZonasVerdes());
	 * jMenuMapasMunicipales
	 * .add(getJMenuItemCargarMapaTematicoAdministrativo());
	 * jMenuMapasMunicipales.add(getJMenuItemCargarMapaTematicoGlobal());
	 * 
	 * } return jMenuMapasMunicipales; }
	 */

	/*
	 * private JMenu getJMenuMapasComarcales(){
	 * 
	 * if (jMenuMapasComarcales == null){
	 * 
	 * jMenuMapasComarcales = new JMenu();
	 * jMenuMapasComarcales.setText(I18N.get(
	 * "LocalGISEIEL","localgiseiel.menu.comarcalmap"));
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalViviendas());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPlaneamiento());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDepositos1());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDepositos2());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPavimentacion1());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPavimentacion2());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalAlumbrado());
	 * jMenuMapasComarcales
	 * .add(getJMenuItemCargarMapaComarcalCentrosSanitarios1());
	 * jMenuMapasComarcales
	 * .add(getJMenuItemCargarMapaComarcalCentrosSanitarios2());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalCaptaciones());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalPotabilizacion());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDepositosSint());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDistribucion());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalSaneamiento());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDepuracion());
	 * jMenuMapasComarcales
	 * .add(getJMenuItemCargarMapaComarcalPavimentacionSint());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalAlumbradoSint());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalBasuras());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalResiduos());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalCultura());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalDeportes());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalZonasVerdes());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalAdministrativo());
	 * jMenuMapasComarcales.add(getJMenuItemCargarMapaComarcalGlobal());
	 * 
	 * } return jMenuMapasComarcales; }
	 */

	/*
	 * private JMenu getJMenuMapasObras(){
	 * 
	 * if (jMenuMapasObras == null){
	 * 
	 * jMenuMapasObras = new JMenu();
	 * jMenuMapasObras.setText(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.menu.obrasmap"));
	 * jMenuMapasObras.add(getJMenuItemCargarMapaObrasInversion());
	 * jMenuMapasObras.add(getJMenuItemCargarMapaObrasHabitantes());
	 * jMenuMapasObras.add(getJMenuItemCargarMapaObrasTotal());
	 * 
	 * } return jMenuMapasObras; }
	 */

	private JMenu getJMenuMapasTematicos() {

		if (jMenuMapasTematicos == null) {
			jMenuMapasTematicos = new JMenu();
			jMenuMapasTematicos.setText(I18N.get("LocalGISEIEL",
					"localgiseiel.menu.tematicmap"));
			jMenuMapasTematicos.add(getJMenuItemCalcularIndices());
			// jMenuMapasTematicos.add(getJMenuItemCalcularObras());
			//jMenuMapasTematicos.addSeparator();
			jMenuMapasTematicos.addSeparator();
			jMenuMapasTematicos
					.add(getJMenuItemCargarMapasTematicosPorNucleo());
			// jMenuMapasTematicos.add(getJMenuMapasMunicipales());
			// jMenuMapasTematicos.add(getJMenuMapasComarcales());
			// jMenuMapasTematicos.add(getJMenuMapasObrasMunicipales());
			// jMenuMapasTematicos.add(getJMenuMapasObrasComarcales());

		}
		return jMenuMapasTematicos;
	}

	private JMenuItem getJMenuItemCargarMapasTematicosPorNucleo() {
		if (jMenuItemCargarMapaTematicoPorNucleo == null) {
			
			
			
			jMenuItemCargarMapaTematicoPorNucleo = new JMenuItem(I18N.get(
					"LocalGISEIEL",
					"localgiseiel.submenu.loadtematicmaps.pornucleo"));
			jMenuItemCargarMapaTematicoPorNucleo
					.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							if (!solicitarEntidadyMunicipio(true)) return;
							
							MapasTematicosDialog dialog;
							try {
								dialog = new MapasTematicosDialog(aplicacion
										.getMainFrame(), localLocalGISEIEL);
								dialog.addActionListener(new java.awt.event.ActionListener() {
									public void actionPerformed(ActionEvent e) {
										dialog_actionPerformed((MapasTematicosDialog) e
												.getSource());
									}
								});
								dialog.setVisible(true);
							} catch (Exception e1) {
								e1.printStackTrace();
							}

							/*
							 * int idMapa =
							 * ConstantesLocalGISEIEL.MAPA_TEMATICO_VIVIENDAS;
							 * 
							 * showMap("workbench-properties-eiel-system.xml",
							 * idMapa);
							 * 
							 * LocalGISEIELUtils.setMapToReload(idMapa, true);
							 */

						}
					});
		}
		return jMenuItemCargarMapaTematicoPorNucleo;

	}

	public void dialog_actionPerformed(MapasTematicosDialog dialog) {
		dialog.setVisible(false);
	}

	/*
	 * private JMenu getJMenuMapasObrasMunicipales(){
	 * 
	 * if (jMenuMapasObrasMunicipales == null){
	 * 
	 * jMenuMapasObrasMunicipales = new JMenu();
	 * jMenuMapasObrasMunicipales.setText
	 * (I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmap.obras.munic"));
	 * jMenuMapasObrasMunicipales.add(getJMenuItemCargarMapaObrasInversion());
	 * jMenuMapasObrasMunicipales.add(getJMenuItemCargarMapaObrasHabitantes());
	 * jMenuMapasObrasMunicipales.add(getJMenuItemCargarMapaObrasTotal()); }
	 * return jMenuMapasObrasMunicipales; }
	 * 
	 * private JMenu getJMenuMapasObrasComarcales(){
	 * 
	 * if (jMenuMapasObrasComarcales == null){
	 * 
	 * jMenuMapasObrasComarcales = new JMenu();
	 * jMenuMapasObrasComarcales.setText
	 * (I18N.get("LocalGISEIEL","localgiseiel.menu.tematicmap.obras.comarcal"));
	 * jMenuMapasObrasComarcales
	 * .add(getJMenuItemCargarMapaComarcalObrasInversion());
	 * jMenuMapasObrasComarcales
	 * .add(getJMenuItemCargarMapaComarcalObrasHabitantes());
	 * jMenuMapasObrasComarcales
	 * .add(getJMenuItemCargarMapaComarcalObrasTotal()); } return
	 * jMenuMapasObrasComarcales; }
	 */

	private JMenuItem getJMenuItemCalcularIndices() {
		if (jMenuItemCalcularIndices == null) {
			
			
			
			
			jMenuItemCalcularIndices = new JMenuItem(I18N.get("LocalGISEIEL",
					"localgiseiel.submenu.calcularindic"));
			jMenuItemCalcularIndices.setEnabled(true);
			jMenuItemCalcularIndices.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if (!solicitarEntidadyMunicipio(true)) return;
					final JFrame desktop = (JFrame) LocalGISEIEL.this;
					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
							desktop, null);
					progressDialog.setSize(550, 100);
					progressDialog.setTitle("TaskMonitorDialog.Wait");
					progressDialog.addComponentListener(new ComponentAdapter() {
						public void componentShown(ComponentEvent e) {
							new Thread(new Runnable() {
								public void run() {
									try {
										
										ProcessCancel processCancel=null;			
				        				if (progressDialog!=null){				
				        					if (progressDialog!=null){
				        						processCancel=new ProcessCancel(progressDialog);
				        						processCancel.start();
				        					}
				        				}
				        				
				        				ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY);
				        				List listMunicipios=iSesion.getAlMunicipios();
				        				
				        				long startMils=Calendar.getInstance().getTimeInMillis();
				        				java.util.Iterator it=listMunicipios.iterator();
				        				int contador=1;
				        				while (it.hasNext()){
				        					
				        					com.geopista.app.catastro.model.beans.Municipio municipio=(com.geopista.app.catastro.model.beans.Municipio)it.next();
				        					String idMunicipio=String.valueOf(municipio.getId());
				        					
				        					String avance=contador+"/"+listMunicipios.size()+"->"+municipio.getNombreOficial()+".";
				        					
											progressDialog.report(avance+"EIEL Indicadores. Borrando datos previos...");
											InitEIEL.clienteLocalGISEIEL.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_CLEANDATA,idMunicipio);
											progressDialog.report(avance+"EIEL Indicadores. Cargando bloque de datos I: Población, vivienda y planeamiento");
											InitEIEL.clienteLocalGISEIEL.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_POBLACION,idMunicipio);
											progressDialog.report(avance+"EIEL Indicadores. Cargando bloque de datos II: Ciclo del agua");
											InitEIEL.clienteLocalGISEIEL.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_CICLOAGUA,idMunicipio);
											progressDialog.report(avance+"EIEL Indicadores. Cargando bloque de datos III: Infraestructuras básicas");
											InitEIEL.clienteLocalGISEIEL.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_INFRAESTRUCTURAS,idMunicipio);
											progressDialog.report(avance+"EIEL Indicadores. Cargando bloque de datos IV: Recogida y eliminación de residuos urbanos");
											InitEIEL.clienteLocalGISEIEL.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_RESIDUOSURBANOS,idMunicipio);
											progressDialog.report(avance+"EIEL Indicadores. Cargando bloque de datos V: Equipamientos educativos, culturales y recreativos");
											InitEIEL.clienteLocalGISEIEL.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_EDUCACIONCULTURA,idMunicipio);
											progressDialog.report(avance+"EIEL Indicadores. Cargando bloque de datos VI: Equipamientos sanitarios y asistenciales");
											InitEIEL.clienteLocalGISEIEL.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_SANITARIOASISTENCIAL,idMunicipio);
											progressDialog.report(avance+"EIEL Indicadores. Cargando bloque de datos VII: Otros servicios");
											InitEIEL.clienteLocalGISEIEL.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_OTROS,idMunicipio);
											contador++;
					        				long endMils=Calendar.getInstance().getTimeInMillis();
						        			logger.info("Tiempo Total carga de inidicadores para el municipio:"+idMunicipio+"->"+(endMils-startMils)/1000+" segundos");
				        				}
				        				long endMils=Calendar.getInstance().getTimeInMillis();
										JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de indicadores realizada correctamente en:"+(endMils-startMils)/1000/60+" minutos");
										
					        			logger.info("Tiempo Total carga de inidicadores:"+(endMils-startMils)/1000/60+" minutos");
						

										
										/*progressDialog
												.report("EIEL Indicadores. Borrando datos previos...");
										InitEIEL.clienteLocalGISEIEL
												.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_CLEANDATA);
										progressDialog
												.report("EIEL Indicadores. Cargando bloque de datos I: Población, vivienda y planeamiento");
										InitEIEL.clienteLocalGISEIEL
												.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_POBLACION);
										progressDialog
												.report("EIEL Indicadores. Cargando bloque de datos II: Ciclo del agua");
										InitEIEL.clienteLocalGISEIEL
												.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_CICLOAGUA);
										progressDialog
												.report("EIEL Indicadores. Cargando bloque de datos III: Infraestructuras básicas");
										InitEIEL.clienteLocalGISEIEL
												.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_INFRAESTRUCTURAS);
										progressDialog
												.report("EIEL Indicadores. Cargando bloque de datos IV: Recogida y eliminación de residuos urbanos");
										InitEIEL.clienteLocalGISEIEL
												.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_RESIDUOSURBANOS);
										progressDialog
												.report("EIEL Indicadores. Cargando bloque de datos V: Equipamientos educativos, culturales y recreativos");
										InitEIEL.clienteLocalGISEIEL
												.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_EDUCACIONCULTURA);
										progressDialog
												.report("EIEL Indicadores. Cargando bloque de datos VI: Equipamientos sanitarios y asistenciales");
										InitEIEL.clienteLocalGISEIEL
												.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_SANITARIOASISTENCIAL);
										progressDialog
												.report("EIEL Indicadores. Cargando bloque de datos VII: Otros servicios");
										InitEIEL.clienteLocalGISEIEL
												.calcularIndices(ConstantesLocalGISEIEL.ACTION_EIEL_INDICADORES_LOAD_OTROS);

										JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de indicadores realizada correctamente");
										
										*/
									} catch (Exception e) {
										logger.error("Error ", e);
										ErrorDialog.show(desktop, "ERROR",
												"ERROR",
												StringUtil.stackTrace(e));
										return;
									} finally {
										progressDialog.setVisible(false);
										progressDialog.dispose();
									}
								}
							}).start();
						}
					});
					GUIUtil.centreOnWindow(progressDialog);
					progressDialog.setResizable(false);
					progressDialog.setVisible(true);

					show();

				}
			});
		}
		return jMenuItemCalcularIndices;
	}

	/*
	 * private JMenuItem getJMenuItemCalcularObras() { if
	 * (jMenuItemCalcularObras == null) { jMenuItemCalcularObras = new
	 * JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.calcularobras"));
	 * jMenuItemCalcularObras.setEnabled(true);
	 * jMenuItemCalcularObras.addActionListener(new ActionListener() { public
	 * void actionPerformed(ActionEvent e) { final JFrame desktop=
	 * (JFrame)LocalGISEIEL.this; final TaskMonitorDialog progressDialog= new
	 * TaskMonitorDialog(desktop, null);
	 * progressDialog.setTitle("TaskMonitorDialog.Wait");
	 * progressDialog.addComponentListener(new ComponentAdapter() { public void
	 * componentShown(ComponentEvent e) { new Thread(new Runnable() { public
	 * void run() { try {
	 * InitEIEL.clienteLocalGISEIEL.calcularObras();
	 * 
	 * } catch(Exception e) { logger.error("Error ", e);
	 * ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
	 * return; } finally { progressDialog.setVisible(false);
	 * progressDialog.dispose(); } } }).start(); } });
	 * GUIUtil.centreOnWindow(progressDialog); progressDialog.setVisible(true);
	 * 
	 * show();
	 * 
	 * } }); } return jMenuItemCalcularObras; }
	 */

	/*
	 * private JMenuItem getJMenuItemCargarMapaProvincial() { if
	 * (jMenuItemCargarMapaProvincialEIEL == null) {
	 * jMenuItemCargarMapaProvincialEIEL = new
	 * JMenuItem(I18N.get("LocalGISEIEL",
	 * "localgiseiel.submenu.loadmapseiel.provincial"));
	 * jMenuItemCargarMapaProvincialEIEL.setEnabled(true);
	 * jMenuItemCargarMapaProvincialEIEL.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_PROVINCIAL;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaProvincialEIEL; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcal() { if
	 * (jMenuItemCargarMapaComarcalEIEL == null) {
	 * jMenuItemCargarMapaComarca1uItem
	 * (I18N.get("LocalGISEIEL","localgiseiel.submenu.loadmapseiel.comarcal"));
	 * jMenuItemCargarMapaComarcalEIEL.setEnabled(true);
	 * jMenuItemCargarMapaComarcalEIEL.addActionListener(new ActionListener() {
	 * public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_COMARCAL;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalEIEL; }
	 */

	private JMenuItem getJMenuItemValidarMPT() {
		if (jMenuItemValidarMPTEIEL == null) {
			jMenuItemValidarMPTEIEL = new JMenuItem(I18N.get("LocalGISEIEL",
					"localgiseiel.submenu.validatempt"));
			jMenuItemValidarMPTEIEL.setEnabled(false);
			jMenuItemValidarMPTEIEL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					ValidateMPTDialog mptDialog = new ValidateMPTDialog();
					mptDialog.setVisible(true);
				}
			});
			

		}
		return jMenuItemValidarMPTEIEL;
	}

	private JMenuItem getJMenuItemExportarCuadrosMPT() {
		if (jMenuItemExportarCuadrosMPTEIEL == null) {
			jMenuItemExportarCuadrosMPTEIEL = new JMenuItem(I18N.get("LocalGISEIEL", "localgiseiel.submenu.exportcuadrosmpt"));
			jMenuItemExportarCuadrosMPTEIEL.setEnabled(false);
			jMenuItemExportarCuadrosMPTEIEL
					.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								ExportCuadrosMPTDialog mptDialog = new ExportCuadrosMPTDialog();
								mptDialog.setVisible(true);
							} catch (Exception ex) {
								ErrorDialog.show(aplicacion.getMainFrame(), I18N.get("LocalGISEIEL", "localgiseiel.sqlerror.title"),
												 I18N.get("LocalGISEIEL", "localgiseiel.sqlerror.warning"), StringUtil.stackTrace(ex));
								ex.printStackTrace();
							}
						}
					});
			

		}
		return jMenuItemExportarCuadrosMPTEIEL;
	}

	private JMenuItem getJMenuItemExportarMPT() {
		if (jMenuItemExportarMPTEIEL == null) {
			jMenuItemExportarMPTEIEL = new JMenuItem(I18N.get("LocalGISEIEL",
					"localgiseiel.submenu.exporttompt"));
			jMenuItemExportarMPTEIEL.setEnabled(false);
			jMenuItemExportarMPTEIEL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ExportMPTDialog mptDialog = new ExportMPTDialog();
					mptDialog.setVisible(true);
				}
			});
			
		}
		return jMenuItemExportarMPTEIEL;
	}

	private JMenuItem getJMenuItemExportarSHPMPT() {
		if (jMenuItemExportarSHPMPTEIEL == null) {
			jMenuItemExportarSHPMPTEIEL = new JMenuItem(I18N.get("LocalGISEIEL",
					"localgiseiel.submenu.exportshpmpt"));
			jMenuItemExportarSHPMPTEIEL.setEnabled(false);
			jMenuItemExportarSHPMPTEIEL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					ExportarSHPDialog mptDialog = new ExportarSHPDialog();
					mptDialog.setVisible(true);
				}
			});
			

		}
		return jMenuItemExportarSHPMPTEIEL;
	}
	

	private JMenuItem getJMenuItemActualizacionPadron() {
		if (jMenuItemActualizarPadron == null) {
			jMenuItemActualizarPadron = new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.actualizarpadron"));
			jMenuItemActualizarPadron.setEnabled(false);
			jMenuItemActualizarPadron.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					UpdatePadronDialog mptDialog = new UpdatePadronDialog();
					mptDialog.setVisible(true);
					//String path="C:\\LocalGIS\\Datos\\";
					//ActualizarPadron padron=new ActualizarPadron("2011", false, path,null);
					//padron.actualizar();
				}
			});
			

		}
		return jMenuItemActualizarPadron;
	}
	
	
	/*
	 * private JMenuItem getJMenuItemCargarMapaMunicipal() { if
	 * (jMenuItemCargarMapaMunicipalEIEL == null) {
	 * jMenuItemCargarMapaMunicipalEIEL = new JMenuItem(I18N.get("LocalGISEIEL",
	 * "localgiseiel.submenu.loadmapseiel.municipal"));
	 * jMenuItemCargarMapaMunicipalEIEL.setEnabled(true);
	 * jMenuItemCargarMapaMunicipalEIEL.addActionListener(new ActionListener() {
	 * public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_MUNICIPAL;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaMunicipalEIEL; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaInfraestructuras() { if
	 * (jMenuItemCargarMapaInfraestructurasEIEL == null) {
	 * jMenuItemCargarMapaInfraestructurasEIEL = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadmapseiel.infraestructuras"));
	 * jMenuItemCargarMapaInfraestructurasEIEL.setEnabled(true);
	 * jMenuItemCargarMapaInfraestructurasEIEL.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_INFRAESTRUCTURAS;
	 * 
	 * showMap("workbench-properties-eiel.xml", idMapa);
	 * LocalGISEIELUtils.setMapToReload(idMapa, true); } }); } return
	 * jMenuItemCargarMapaInfraestructurasEIEL; }
	 */

	private JMenuItem getJMenuItemEdicionEIEL() {
		String mostrarMapa = System.getProperty("mostrarMapa");

		if ((mostrarMapa != null) && (mostrarMapa.equals("false"))) {
			Constantes.MOSTRAR_MAPA = false;
		}

		/*String mapaEIEL = System.getProperty("mapaEIEL");

		if ((mapaEIEL != null) && (!mapaEIEL.equals("0"))) {

			ConstantesLocalGISEIEL.MAPA_EIEL = Integer.parseInt(mapaEIEL);
		}*/

		if (jMenuItemEdicionEIEL == null) {
			jMenuItemEdicionEIEL = new JMenuItem(I18N.get("LocalGISEIEL",
					"localgiseiel.submenu.editioneiel"));
			jMenuItemEdicionEIEL.setEnabled(true);
			jMenuItemEdicionEIEL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					boolean global=false;
					
					status.setGlobal(false);
					showEdicionEIEL(global);

					
					// La siguiente carga del mapa de planeamiento exige recarga
					// por si se ha modificado alguna
					// de sus capas
					int idMapa = new Integer(aplicacion.getString("id.mapa.planeamiento.informe.urbanistico")).intValue();
					LocalGISEIELUtils.setMapToReload(idMapa, true);
					ConstantesLocalGISEIEL.GLOBAL_MAP=false;
					addFixedPluginsLocal();

				}
			});
		}
		return jMenuItemEdicionEIEL;
	}
	
	private JMenuItem getJMenuItemEdicionEIELGlobal() {
		String mostrarMapa = System.getProperty("mostrarMapa");

		if ((mostrarMapa != null) && (mostrarMapa.equals("false"))) {
			Constantes.MOSTRAR_MAPA = false;
		}

		/*String mapaEIEL = System.getProperty("mapaEIEL");

		if ((mapaEIEL != null) && (!mapaEIEL.equals("0"))) {

			ConstantesLocalGISEIEL.MAPA_EIEL = Integer.parseInt(mapaEIEL);
		}*/

		if (jMenuItemEdicionEIELGlobal == null) {
			jMenuItemEdicionEIELGlobal = new JMenuItem(I18N.get("LocalGISEIEL","localgiseiel.submenu.editioneiel.global"));
			jMenuItemEdicionEIELGlobal.setEnabled(true);
			jMenuItemEdicionEIELGlobal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					boolean global=true;
					status.setGlobal(false);
					showEdicionEIEL(global);

					// La siguiente carga del mapa de planeamiento exige recarga
					// por si se ha modificado alguna de sus capas
					int idMapa = new Integer(aplicacion.getString("id.mapa.planeamiento.informe.urbanistico")).intValue();
					LocalGISEIELUtils.setMapToReload(idMapa, true);
					ConstantesLocalGISEIEL.GLOBAL_MAP=true;
					

				}
			});
		}
		return jMenuItemEdicionEIELGlobal;
	}

	
	
	private void showEdicionEIEL(boolean global) {
		
		boolean recargarCapas=true;

		if (global){
			if (!solicitarEntidadyMunicipio(false)) return;
		}
		else{
			if (!solicitarEntidadyMunicipio(true)) return;
		}
		
		if (global){
			if (ConstantesLocalGISEIEL.MAPA_EIEL_CARGA==ConstantesLocalGISEIEL.MAPA_EIEL_GLOBAL)
				recargarCapas=false;
			else
				recargarCapas=true;
			ConstantesLocalGISEIEL.MAPA_EIEL_CARGA=ConstantesLocalGISEIEL.MAPA_EIEL_GLOBAL;			
		}
		else{

			if (ConstantesLocalGISEIEL.MAPA_EIEL_CARGA==ConstantesLocalGISEIEL.MAPA_EIEL)
				LocalGISEIELUtils.setMapToReload(ConstantesLocalGISEIEL.MAPA_EIEL, false);
			else
				LocalGISEIELUtils.setMapToReload(ConstantesLocalGISEIEL.MAPA_EIEL, true);
			
			ConstantesLocalGISEIEL.MAPA_EIEL_CARGA=ConstantesLocalGISEIEL.MAPA_EIEL;

		}
		
		final JFrame desktop = (JFrame) this;
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(desktop,
				null);
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.setResizable(false);
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {

							if (desktopPane.getAllFramesInLayer(JDesktopPane
									.getLayer(new JInternalFrame())).length > 0)
								return;

							progressDialog.report(aplicacion.getI18nString("inventario.app.tag1"));

							Container c = LocalGISEIEL.this.getContentPane();
							c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

							//Cargamos la lista de Entidades Singulares y Nucleos que tiene
							//el Municipio
							if (!edicionCargada){
								LocalGISEIELUtils.storeNucleosMunicipio();
								LocalGISEIELUtils.storeNucleosAllMunicipios();
							}
							
							
							/*ProcessCancel processCancel=null;			
	        				if (progressDialog!=null){				
	        					if (progressDialog!=null){
	        						processCancel=new ProcessCancel(progressDialog);
	        						processCancel.start();
	        					}
	        				}*/
							
							// La siguiente sentencia tarda bastante en
							// ejecutarse
							// Porque se cargan los dominios
							// Clase: EIELDominioPanelTree (Constructor)

							aplicacion.getBlackboard().put("TASK_MONITOR",progressDialog);
							jif = encapsulaEnJInternalFrame(getJPanelEIEL(), "");

							llamadaAInternalFrame(jif, false, true, true);
							c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							c.setCursor(Cursor.getDefaultCursor());
							
							
							
							

						} catch (Exception e) {
							if ((progressDialog != null)
									&& (progressDialog.isCancelRequested())) {
								logger.warn("Carga de estructuras cancelada");
								cancelada = true;
								
								// JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de estructuras cancelada");
							} else {

								logger.error("Error ", e);
								ErrorDialog.show(desktop, "ERROR", "ERROR",
										StringUtil.stackTrace(e));
							}
							return;
						} finally {
							
							if ((progressDialog != null)
									&& (progressDialog.isCancelRequested())) {
								logger.warn("Carga de estructuras cancelada");
								cancelada = true;
								AppContext.releaseResources();
								aplicacion.getBlackboard().remove("TASK_MONITOR");
								// JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de estructuras cancelada");
							} 
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

		//System.out.println("Cancelada:"+cancelada);
		if (cancelada) {
			stopApp();
			return;
		}

		// Precarga el mapa de la eiel
		if (Constantes.MOSTRAR_MAPA){
			((com.geopista.app.eiel.panels.GeopistaEditorPanel) ((EditorPanel) getJPanelEditorEIEL())
					.getGeopistaEditorPanel()).initEditor(jif);
			
			if (global){
				
				if (recargarCapas){
					logger.info("Cargando capas de mapa global");
						new Thread(new Runnable()
						{
							public void run()
							{
								Timer timer=null;
								//Cargamos las capas del mapa de la EIEL sin datos pero en background
								try {
									
									timer = new Timer (2000, new ActionListener () 
									{ 
									    public void actionPerformed(ActionEvent e) 
									    { 
									    	
									    	GeopistaEditor editor=((GeopistaEditorPanel)getJPanelEditorEIEL().getGeopistaEditorPanel()).getEditor();
									    	if (editor!=null)
									    		editor.getLayerViewPanel().getContext().setStatusMessage("Cargando las capas de la EIEL en background...");
									    } 
									}); 		
									timer.start();
									
									
									ReloadMapPlugIn.forceLoadMap(ConstantesLocalGISEIEL.MAPA_EIEL,null);
									LocalGISEIELUtils.setMapToReload(ConstantesLocalGISEIEL.MAPA_EIEL, true);
									
									
								} catch (Exception e1) {
								}
								finally{
									if (timer!=null){
										timer.stop();
								    	GeopistaEditor editor=((GeopistaEditorPanel)getJPanelEditorEIEL().getGeopistaEditorPanel()).getEditor();
								    	if (editor!=null)
								    		editor.getLayerViewPanel().getContext().setStatusMessage("");
		
									}
								}
							}
						}
						).start();
				}
				else{
					logger.info("Las capas de la EIEL no se cargan");
				}
				
				
			}else{
				LocalGISEIELUtils.setMapToReload(ConstantesLocalGISEIEL.MAPA_EIEL_COMPLETO, true);
				//globalCargado=false;
			}
		}
		
		
		//Solo añadimos los plugins al entorno global
		if (global){
			if (!globalCargado){
				addFixedPlugins();
				AppContext.showMunicipiosEntidad();    
				addEielMaps();
				globalCargado=true;
			}
		}
		
		edicionCargada=true;
	}

	private void addEielMaps() {

    	JComboBox jc = (JComboBox)aplicacion.getBlackboard().get(UserPreferenceConstants
.MAPAS_COMBO);
    	if (jc != null){
        	jc.addItem("");
	        //jc.addItem("Mapa EIEL Global\t-"+ConstantesLocalGISEIEL.MAPA_EIEL_GLOBAL);
	        jc.addItem("Mapa EIEL\t-"+ConstantesLocalGISEIEL.MAPA_EIEL);
	        jc.addItem("Mapa EIEL Completo\t-"+ConstantesLocalGISEIEL.MAPA_EIEL_COMPLETO);	    	
    	} 		
	}

	
	
	
	public void showMap(final String fileProperties, final int idMap) {

		final JFrame desktop = (JFrame) this;
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(desktop,
				null);
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.setResizable(false);
		progressDialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0)
								return;
							Container c = LocalGISEIEL.this.getContentPane();
							c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							if (aplicacion.getBlackboard().get("internalframe") != null) {
								cierraInternalFrame((JInternalFrame) aplicacion.getBlackboard().get("internalframe"));
							}
							String title = InitEIEL.clienteLocalGISEIEL.getNombreMapa(idMap);
							JInternalFrame jif = encapsulaEnJInternalFrame(getJPanelMap(fileProperties, new Integer(idMap)), title);
							aplicacion.getBlackboard().put("internalframe", jif);
							llamadaAInternalFrame(jif, false, true, true);
							c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							c.setCursor(Cursor.getDefaultCursor());
						} catch (Exception e) {
							logger.error("Error ", e);
							ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
							return;
						} finally {
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

		GeopistaMapPanel panel = (GeopistaMapPanel) aplicacion.getBlackboard().get("GeopistaEditor");
		panel.initEditor();
	}

	private EditorPanel getJPanelEditorEIEL() {

		if (jPanelEditorEIEL == null) {
			jPanelEditorEIEL = new EditorPanel();
		}
		return jPanelEditorEIEL;
	}
	
	private JFrame getJFrame() {

		return this;
	}
	

	private EIELEditorPanel getJPanelEIEL() {

		if (jPanelEIEL == null) {
			jPanelEIEL = new EIELEditorPanel();
		}
		if (!jPanelEIEL.isRestarted())
			return jPanelEIEL;
		else{
			//jPanelEIEL.initialize();
			//return jPanelEIEL;
			jPanelEIEL = new EIELEditorPanel();
			return jPanelEIEL;
		}
	}

	private ShowMapPanel getJPanelMap(String fileProperties, int idMap) {

		jPanelMap = new ShowMapPanel(fileProperties, idMap);

		return jPanelMap;
	}

	private void activarMenu() {
		jMenuEdicionEIEL.setEnabled(true);
		jMenuBarEIEL.setEnabled(true);
		jMenuGenerationMPT.setEnabled(true);
		if (tienePermisos(ConstantesLocalGISEIEL.PERM_VALIDA_MPT_EIEL))
			jMenuItemValidarMPTEIEL.setEnabled(true);
		if (tienePermisos(ConstantesLocalGISEIEL.PERM_CUADRO_MPT_EIEL))
			jMenuItemExportarCuadrosMPTEIEL.setEnabled(true);
		if (tienePermisos(ConstantesLocalGISEIEL.PERM_GENERA_MPT_EIEL))
			jMenuItemExportarMPTEIEL.setEnabled(true);
		if (tienePermisos(ConstantesLocalGISEIEL.PERM_EXPORTAR_SHP_MPT_EIEL))
			jMenuItemExportarSHPMPTEIEL.setEnabled(true);
		if (tienePermisos(ConstantesLocalGISEIEL.PERM_ACTUALIZAR_PADRON_EIEL)){
        	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_VALIDADOR_EIEL)){
			jMenuItemActualizarPadron.setEnabled(true);
        	}
		}
		jMenuDominios.setEnabled(true);
		jMenuMapasTematicos.setEnabled(true);
		jMenuAyuda.setEnabled(true);

	}

	private void desactivarMenu() {
		jMenuEdicionEIEL.setEnabled(false);
		jMenuBarEIEL.setEnabled(false);
		jMenuGenerationMPT.setEnabled(false);
		jMenuDominios.setEnabled(false);
		jMenuMapasTematicos.setEnabled(false);
		jMenuAyuda.setEnabled(false);

	}

	/*
	 * private JMenuItem getJMenuItemMapasTematicos() { if
	 * (jMenuItemCargarMapaTematicoViviendas == null) {
	 * jMenuItemCargarMapaTematicoViviendas = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps"));
	 * jMenuItemCargarMapaTematicoViviendas.setEnabled(true);
	 * jMenuItemCargarMapaTematicoViviendas.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { } }); }
	 * return jMenuItemCargarMapaTematicoViviendas; }
	 */

	private JMenuItem getJMenuItemEditorDominiosEIEL() {
		if (jMenuItemEditorDominiosEIEL == null) {
			jMenuItemEditorDominiosEIEL = new JMenuItem(I18N.get(
					"LocalGISEIEL", "localgiseiel.submenu.dominios.editor"));
			jMenuItemEditorDominiosEIEL.setEnabled(true);
			jMenuItemEditorDominiosEIEL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					
					if (!solicitarEntidadyMunicipio(true)) return;
					
					
					
					final JFrame desktop = LocalGISEIEL.this;
					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
							desktop, null);
					progressDialog.setTitle("TaskMonitorDialog.Wait");
					progressDialog.setResizable(false);
					
					
					progressDialog.addComponentListener(new ComponentAdapter() {
						public void componentShown(ComponentEvent e) {
							new Thread(new Runnable() {
								public void run() {
									try {

										ProcessCancel processCancel=null;			
					    				if (progressDialog!=null){				
					    					if (progressDialog!=null){
					    						processCancel=new ProcessCancel(progressDialog);
					    						processCancel.start();
					    					}
					    				}
					    				
										if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length > 0)
											return;
										Container c = LocalGISEIEL.this.getContentPane();
										c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
										Locale currentLocale = new Locale(ConstantesLocalGISEIEL.Locale);
										ResourceBundle messages;
										try {
											messages = ResourceBundle.getBundle("locale.administrador",currentLocale);
										} catch (Exception ex) {
											messages = ResourceBundle.getBundle("locale.administrador",new Locale(Constantes.Locale));
										}
										GeopistaAcl aclAdministracion;
										boolean editable = false;
										try {
											aclAdministracion = com.geopista.security.SecurityManager.getPerfil("Administracion");
											editable = aclAdministracion
													.checkPermission(new com.geopista.security.GeopistaPermission(
															com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
										} catch (Exception e1) {
											editable = false;
										}
										OperacionesAdministrador.registerGeopistaDrivers();
										CDominiosFrame dominiosFrame = new CDominiosFrame(messages, LocalGISEIEL.this);
										
										dominiosFrame.addInternalFrameListener(new InternalFrameListener() {
											public void internalFrameClosed(
													InternalFrameEvent arg0) {	
												//Recargamos los dominios

												Estructuras.setCargada(false);
												Estructuras.setIniciada(false);
												Estructuras.cargarEstructuras();
											}
											public void internalFrameActivated(InternalFrameEvent e) {}
											public void internalFrameClosing(InternalFrameEvent e) {}
											public void internalFrameDeactivated(InternalFrameEvent e) {}
											public void internalFrameDeiconified(InternalFrameEvent e){}
											public void internalFrameIconified(InternalFrameEvent e) {}
											public void internalFrameOpened(InternalFrameEvent e) {}
										});
									
										dominiosFrame.editable(editable);
										if (!progressDialog.isCancelRequested())
											llamadaAInternalFrame(dominiosFrame,false, true, true);
										c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
										c.setCursor(Cursor.getDefaultCursor());
										
										processCancel.terminateProcess();

									} catch (Exception e) {
										logger.error("Error ", e);
										ErrorDialog.show(desktop, "ERROR",
												"ERROR",
												StringUtil.stackTrace(e));
										return;
									} finally {
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
					if (progressDialog.isCancelRequested())
						return;
					if (desktopPane.getAllFramesInLayer(JDesktopPane
							.getLayer(new JInternalFrame())).length > 0)
						return;
					Container c = LocalGISEIEL.this.getContentPane();
					c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					Locale currentLocale = new Locale(Constantes.Locale);
					ResourceBundle messages;
					try {
						messages = ResourceBundle.getBundle(
								"config.administrador", currentLocale);
					} catch (Exception ex) {
						messages = ResourceBundle.getBundle(
								"config.administrador", new Locale(
										Constantes.Locale));
					}
					GeopistaAcl aclAdministracion;
					boolean editable = false;
					try {
						aclAdministracion = com.geopista.security.SecurityManager
								.getPerfil("Administracion");
						editable = aclAdministracion
								.checkPermission(new com.geopista.security.GeopistaPermission(
										com.geopista.security.GeopistaPermission.EDITAR_ADMINITRACION));
					} catch (Exception e1) {
						editable = false;
					}
					CDominiosFrame dominiosFrame = new CDominiosFrame(messages,
							LocalGISEIEL.this);
					dominiosFrame.editable(editable);
					if (!progressDialog.isCancelRequested())
						llamadaAInternalFrame(dominiosFrame, false, true, true);
					c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					c.setCursor(Cursor.getDefaultCursor());
					
				}
			});
		}
		return jMenuItemEditorDominiosEIEL;
	}

	/*
	 * private JMenuItem getJMenuItemCargarMapaObrasTotal() { if
	 * (jMenuItemCargarMapaObrasTotal == null) { jMenuItemCargarMapaObrasTotal =
	 * new JMenuItem(I18N.get("LocalGISEIEL",
	 * "localgiseiel.submenu.loadtematicmaps.numobras"));
	 * jMenuItemCargarMapaObrasTotal.addActionListener(new ActionListener() {
	 * public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_OBRAS_TOTAL;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaObrasTotal; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalObrasTotal() { if
	 * (jMenuItemCargarMapaComarcalObrasTotal == null) {
	 * jMenuItemCargarMapaComarcalObrasTotal = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.numobras"));
	 * jMenuItemCargarMapaComarcalObrasTotal.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_TOTAL;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalObrasTotal; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaObrasHabitantes() { if
	 * (jMenuItemCargarMapaObrasHabitantes == null) {
	 * jMenuItemCargarMapaObrasHabitantes = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.obrashabitante"));
	 * jMenuItemCargarMapaObrasHabitantes.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_OBRAS_HABITANTE;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaObrasHabitantes; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalObrasHabitantes() { if
	 * (jMenuItemCargarMapaComarcalObrasHabitantes == null) {
	 * jMenuItemCargarMapaComarcalObrasHabitantes = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.obrashabitante"));
	 * jMenuItemCargarMapaComarcalObrasHabitantes.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_HABITANTE;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalObrasHabitantes; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaObrasInversion() { if
	 * (jMenuItemCargarMapaObrasInversion == null) {
	 * jMenuItemCargarMapaObrasInversion = new
	 * JMenuItem(I18N.get("LocalGISEIEL",
	 * "localgiseiel.submenu.loadtematicmaps.obrasinversion"));
	 * jMenuItemCargarMapaObrasInversion.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_OBRAS_INVERSION;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaObrasInversion; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalObrasInversion() { if
	 * (jMenuItemCargarMapaComarcalObrasInversion == null) {
	 * jMenuItemCargarMapaComarcalObrasInversion = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.obrasinversion"));
	 * jMenuItemCargarMapaComarcalObrasInversion.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_INVERSION;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalObrasInversion; }
	 */

	/*
	 * private JMenuItem getJMenuItemCargarMapaTematicoViviendas() { if
	 * (jMenuItemCargarMapaTematicoViviendas == null) {
	 * jMenuItemCargarMapaTematicoViviendas = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.viviendas"));
	 * jMenuItemCargarMapaTematicoViviendas.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_VIVIENDAS;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoViviendas; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalViviendas() { if
	 * (jMenuItemCargarMapaComarcalViviendas == null) {
	 * jMenuItemCargarMapaComarcalViviendas = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.viviendas"));
	 * jMenuItemCargarMapaComarcalViviendas.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_VIVIENDAS;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalViviendas; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoPlaneamiento() { if
	 * (jMenuItemCargarMapaTematicoPlaneamiento == null) {
	 * jMenuItemCargarMapaTematicoPlaneamiento = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.planeamiento"));
	 * jMenuItemCargarMapaTematicoPlaneamiento.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_PLANEAMIENTO;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoPlaneamiento; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalPlaneamiento() { if
	 * (jMenuItemCargarMapaComarcalPlaneamiento == null) {
	 * jMenuItemCargarMapaComarcalPlaneamiento = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.planeamiento"));
	 * jMenuItemCargarMapaComarcalPlaneamiento.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_PLANEAMIENTO;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalPlaneamiento; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoDepositos1() { if
	 * (jMenuItemCargarMapaTematicoDepositos1 == null) {
	 * jMenuItemCargarMapaTematicoDepositos1 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.depositos1"));
	 * jMenuItemCargarMapaTematicoDepositos1.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPOSITOS1;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoDepositos1; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalDepositos1() { if
	 * (jMenuItemCargarMapaComarcalDepositos1 == null) {
	 * jMenuItemCargarMapaComarcalDepositos1 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.depositos1"));
	 * jMenuItemCargarMapaComarcalDepositos1.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPOSITOS1;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalDepositos1; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoDepositos2() { if
	 * (jMenuItemCargarMapaTematicoDepositos2 == null) {
	 * jMenuItemCargarMapaTematicoDepositos2 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.depositos2"));
	 * jMenuItemCargarMapaTematicoDepositos2.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPOSITOS2;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoDepositos2; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalDepositos2() { if
	 * (jMenuItemCargarMapaComarcalDepositos2 == null) {
	 * jMenuItemCargarMapaComarcalDepositos2 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.depositos2"));
	 * jMenuItemCargarMapaComarcalDepositos2.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPOSITOS2;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalDepositos2; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoPavimentacion1() { if
	 * (jMenuItemCargarMapaTematicoPavimentacion1 == null) {
	 * jMenuItemCargarMapaTematicoPavimentacion1 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.pavimentacion1"));
	 * jMenuItemCargarMapaTematicoPavimentacion1.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_PAVIMENTACION1;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoPavimentacion1; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalPavimentacion1() { if
	 * (jMenuItemCargarMapaComarcalPavimentacion1 == null) {
	 * jMenuItemCargarMapaComarcalPavimentacion1 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.pavimentacion1"));
	 * jMenuItemCargarMapaComarcalPavimentacion1.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_PAVIMENTACION1;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalPavimentacion1; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoPavimentacion2() { if
	 * (jMenuItemCargarMapaTematicoPavimentacion2 == null) {
	 * jMenuItemCargarMapaTematicoPavimentacion2 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.pavimentacion2"));
	 * jMenuItemCargarMapaTematicoPavimentacion2.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_PAVIMENTACION2;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoPavimentacion2; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalPavimentacion2() { if
	 * (jMenuItemCargarMapaComarcalPavimentacion2 == null) {
	 * jMenuItemCargarMapaComarcalPavimentacion2 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.pavimentacion2"));
	 * jMenuItemCargarMapaComarcalPavimentacion2.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_PAVIMENTACION2;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalPavimentacion2; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoAlumbrado() { if
	 * (jMenuItemCargarMapaTematicoAlumbrado == null) {
	 * jMenuItemCargarMapaTematicoAlumbrado = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.alumbrado"));
	 * jMenuItemCargarMapaTematicoAlumbrado.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_ALUMBRADO;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoAlumbrado; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalAlumbrado() { if
	 * (jMenuItemCargarMapaComarcalAlumbrado == null) {
	 * jMenuItemCargarMapaComarcalAlumbrado = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.alumbrado"));
	 * jMenuItemCargarMapaComarcalAlumbrado.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_ALUMBRADO;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalAlumbrado; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoCentrosSanitarios1() { if
	 * (jMenuItemCargarMapaTematicoCentrosSanitarios1 == null) {
	 * jMenuItemCargarMapaTematicoCentrosSanitarios1 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.centrossanitarios1"));
	 * jMenuItemCargarMapaTematicoCentrosSanitarios1.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_CENTROS_SANITARIOS1;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoCentrosSanitarios1; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalCentrosSanitarios1() { if
	 * (jMenuItemCargarMapaComarcalCentrosSanitarios1 == null) {
	 * jMenuItemCargarMapaComarcalCentrosSanitarios1 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.centrossanitarios1"));
	 * jMenuItemCargarMapaComarcalCentrosSanitarios1.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_CENTROS_SANITARIOS1;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalCentrosSanitarios1; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoCentrosSanitarios2() { if
	 * (jMenuItemCargarMapaTematicoCentrosSanitarios2 == null) {
	 * jMenuItemCargarMapaTematicoCentrosSanitarios2 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.centrossanitarios2"));
	 * jMenuItemCargarMapaTematicoCentrosSanitarios2.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_CENTROS_SANITARIOS2;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoCentrosSanitarios2; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalCentrosSanitarios2() { if
	 * (jMenuItemCargarMapaComarcalCentrosSanitarios2 == null) {
	 * jMenuItemCargarMapaComarcalCentrosSanitarios2 = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.centrossanitarios2"));
	 * jMenuItemCargarMapaComarcalCentrosSanitarios2.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_CENTROS_SANITARIOS2;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalCentrosSanitarios2; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoPotabilizacion() { if
	 * (jMenuItemCargarMapaTematicoPotabilizacion == null) {
	 * jMenuItemCargarMapaTematicoPotabilizacion = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.potabilizacion"));
	 * jMenuItemCargarMapaTematicoPotabilizacion.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_POTABILIZACION;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoPotabilizacion; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalPotabilizacion() { if
	 * (jMenuItemCargarMapaComarcalPotabilizacion == null) {
	 * jMenuItemCargarMapaComarcalPotabilizacion = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.potabilizacion"));
	 * jMenuItemCargarMapaComarcalPotabilizacion.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_POTABILIZACION;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalPotabilizacion; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoCaptaciones() { if
	 * (jMenuItemCargarMapaTematicoCaptaciones == null) {
	 * jMenuItemCargarMapaTematicoCaptaciones = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.captaciones"));
	 * jMenuItemCargarMapaTematicoCaptaciones.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_CAPTACIONES;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoCaptaciones; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalCaptaciones() { if
	 * (jMenuItemCargarMapaComarcalCaptaciones == null) {
	 * jMenuItemCargarMapaComarcalCaptaciones = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.captaciones"));
	 * jMenuItemCargarMapaComarcalCaptaciones.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_CAPTACIONES;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalCaptaciones; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoDepositosSint() { if
	 * (jMenuItemCargarMapaTematicoDepositosSint == null) {
	 * jMenuItemCargarMapaTematicoDepositosSint = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.depositossint"));
	 * jMenuItemCargarMapaTematicoDepositosSint.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPOSITOS_SINT;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoDepositosSint; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalDepositosSint() { if
	 * (jMenuItemCargarMapaComarcalDepositosSint == null) {
	 * jMenuItemCargarMapaComarcalDepositosSint = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.depositossint"));
	 * jMenuItemCargarMapaComarcalDepositosSint.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPOSITOS_SINT;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalDepositosSint; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoDistribucion() { if
	 * (jMenuItemCargarMapaTematicoDistribucion == null) {
	 * jMenuItemCargarMapaTematicoDistribucion = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.distribucion"));
	 * jMenuItemCargarMapaTematicoDistribucion.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DISTRIBUCION;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoDistribucion; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalDistribucion() { if
	 * (jMenuItemCargarMapaComarcalDistribucion == null) {
	 * jMenuItemCargarMapaComarcalDistribucion = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.distribucion"));
	 * jMenuItemCargarMapaComarcalDistribucion.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DISTRIBUCION;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalDistribucion; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoSaneamiento() { if
	 * (jMenuItemCargarMapaTematicoSaneamiento == null) {
	 * jMenuItemCargarMapaTematicoSaneamiento = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.saneamiento"));
	 * jMenuItemCargarMapaTematicoSaneamiento.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_SANEAMIENTO;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoSaneamiento; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalSaneamiento() { if
	 * (jMenuItemCargarMapaComarcalSaneamiento == null) {
	 * jMenuItemCargarMapaComarcalSaneamiento = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.saneamiento"));
	 * jMenuItemCargarMapaComarcalSaneamiento.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_SANEAMIENTO;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalSaneamiento; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoDepuracion() { if
	 * (jMenuItemCargarMapaTematicoDepuracion == null) {
	 * jMenuItemCargarMapaTematicoDepuracion = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.depuracion"));
	 * jMenuItemCargarMapaTematicoDepuracion.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPURACION;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoDepuracion; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalDepuracion() { if
	 * (jMenuItemCargarMapaComarcalDepuracion == null) {
	 * jMenuItemCargarMapaComarcalDepuracion = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.depuracion"));
	 * jMenuItemCargarMapaComarcalDepuracion.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPURACION;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalDepuracion; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoPavimentacionSint() { if
	 * (jMenuItemCargarMapaTematicoPavimentacionSint == null) {
	 * jMenuItemCargarMapaTematicoPavimentacionSint = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.pavimentacionsint"));
	 * jMenuItemCargarMapaTematicoPavimentacionSint.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_PAVIMENTACION_SINT;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoPavimentacionSint; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalPavimentacionSint() { if
	 * (jMenuItemCargarMapaComarcalPavimentacionSint == null) {
	 * jMenuItemCargarMapaComarcalPavimentacionSint = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.pavimentacionsint"));
	 * jMenuItemCargarMapaComarcalPavimentacionSint.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_PAVIMENTACION_SINT;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalPavimentacionSint; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoAlumbradoSint() { if
	 * (jMenuItemCargarMapaTematicoAlumbradoSint == null) {
	 * jMenuItemCargarMapaTematicoAlumbradoSint = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.alumbradosint"));
	 * jMenuItemCargarMapaTematicoAlumbradoSint.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_ALUMBRADO_SINT;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoAlumbradoSint; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalAlumbradoSint() { if
	 * (jMenuItemCargarMapaComarcalAlumbradoSint == null) {
	 * jMenuItemCargarMapaComarcalAlumbradoSint = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.alumbradosint"));
	 * jMenuItemCargarMapaComarcalAlumbradoSint.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_ALUMBRADO_SINT;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalAlumbradoSint; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoBasuras() { if
	 * (jMenuItemCargarMapaTematicoBasuras == null) {
	 * jMenuItemCargarMapaTematicoBasuras = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.basuras"));
	 * jMenuItemCargarMapaTematicoBasuras.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_TEMATICO_BASURAS;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoBasuras; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalBasuras() { if
	 * (jMenuItemCargarMapaComarcalBasuras == null) {
	 * jMenuItemCargarMapaComarcalBasuras = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.basuras"));
	 * jMenuItemCargarMapaComarcalBasuras.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_COMARCAL_BASURAS;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalBasuras; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoResiduos() { if
	 * (jMenuItemCargarMapaTematicoResiduos == null) {
	 * jMenuItemCargarMapaTematicoResiduos = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.residuos"));
	 * jMenuItemCargarMapaTematicoResiduos.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_RESIDUOS;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoResiduos; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalResiduos() { if
	 * (jMenuItemCargarMapaComarcalResiduos == null) {
	 * jMenuItemCargarMapaComarcalResiduos = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.residuos"));
	 * jMenuItemCargarMapaComarcalResiduos.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_RESIDUOS;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalResiduos; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoCultura() { if
	 * (jMenuItemCargarMapaTematicoCultura == null) {
	 * jMenuItemCargarMapaTematicoCultura = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.cultura"));
	 * jMenuItemCargarMapaTematicoCultura.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_TEMATICO_CULTURA;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoCultura; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalCultura() { if
	 * (jMenuItemCargarMapaComarcalCultura == null) {
	 * jMenuItemCargarMapaComarcalCultura = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.cultura"));
	 * jMenuItemCargarMapaComarcalCultura.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_COMARCAL_CULTURA;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalCultura; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoDeportes() { if
	 * (jMenuItemCargarMapaTematicoDeportes == null) {
	 * jMenuItemCargarMapaTematicoDeportes = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.deportes"));
	 * jMenuItemCargarMapaTematicoDeportes.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_DEPORTES;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoDeportes; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalDeportes() { if
	 * (jMenuItemCargarMapaComarcalDeportes == null) {
	 * jMenuItemCargarMapaComarcalDeportes = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.deportes"));
	 * jMenuItemCargarMapaComarcalDeportes.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_DEPORTES;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalDeportes; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoZonasVerdes() { if
	 * (jMenuItemCargarMapaTematicoZonasVerdes == null) {
	 * jMenuItemCargarMapaTematicoZonasVerdes = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.zonasverdes"));
	 * jMenuItemCargarMapaTematicoZonasVerdes.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_ZONASVERDES;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoZonasVerdes; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalZonasVerdes() { if
	 * (jMenuItemCargarMapaComarcalZonasVerdes == null) {
	 * jMenuItemCargarMapaComarcalZonasVerdes = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.zonasverdes"));
	 * jMenuItemCargarMapaComarcalZonasVerdes.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_ZONASVERDES;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalZonasVerdes; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoAdministrativo() { if
	 * (jMenuItemCargarMapaTematicoAdministrativo == null) {
	 * jMenuItemCargarMapaTematicoAdministrativo = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.administrativo"));
	 * jMenuItemCargarMapaTematicoAdministrativo.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_TEMATICO_ADMINISTRATIVO;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoAdministrativo; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalAdministrativo() { if
	 * (jMenuItemCargarMapaComarcalAdministrativo == null) {
	 * jMenuItemCargarMapaComarcalAdministrativo = new
	 * JMenuItem(I18N.get("LocalGISEIEL"
	 * ,"localgiseiel.submenu.loadtematicmaps.administrativo"));
	 * jMenuItemCargarMapaComarcalAdministrativo.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) { int
	 * idMapa = ConstantesLocalGISEIEL.MAPA_COMARCAL_ADMINISTRATIVO;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalAdministrativo; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaTematicoGlobal() { if
	 * (jMenuItemCargarMapaTematicoGlobal == null) {
	 * jMenuItemCargarMapaTematicoGlobal = new
	 * JMenuItem(I18N.get("LocalGISEIEL",
	 * "localgiseiel.submenu.loadtematicmaps.global"));
	 * jMenuItemCargarMapaTematicoGlobal.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_TEMATICO_GLOBAL;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaTematicoGlobal; }
	 * 
	 * private JMenuItem getJMenuItemCargarMapaComarcalGlobal() { if
	 * (jMenuItemCargarMapaComarcalGlobal == null) {
	 * jMenuItemCargarMapaComarcalGlobal = new
	 * JMenuItem(I18N.get("LocalGISEIEL",
	 * "localgiseiel.submenu.loadtematicmaps.global"));
	 * jMenuItemCargarMapaComarcalGlobal.addActionListener(new ActionListener()
	 * { public void actionPerformed(ActionEvent e) { int idMapa =
	 * ConstantesLocalGISEIEL.MAPA_COMARCAL_GLOBAL;
	 * 
	 * showMap("workbench-properties-eiel-system.xml", idMapa);
	 * 
	 * LocalGISEIELUtils.setMapToReload(idMapa, true);
	 * 
	 * } }); } return jMenuItemCargarMapaComarcalGlobal; }
	 */
}
