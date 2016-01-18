package com.geopista.app.centralizadorsso;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.AppContextListener;
import com.geopista.app.GeopistaEvent;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.centralizadorsso.localgisapp.CLocalGISAppFrame;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

import javax.jnlp.ServiceManager;
import javax.jnlp.SingleInstanceListener;
import javax.jnlp.SingleInstanceService;
import javax.jnlp.UnavailableServiceException;

/**
 * @author dcaaveiro
 */
public class CMainCentralizadorSSO extends CMain implements
		SingleInstanceListener {

	static Logger logger;
	static {
		createDir();
		logger = Logger.getLogger(CMainCentralizadorSSO.class);
	}
	private static AppContext app = (AppContext) AppContext
			.getApplicationContext();
	public static final String idApp = "CentralizadorSSO";
	ResourceBundle messages;

	private static final String IMAGE_ICON_LOCALGIS_MODELO = "/img/icon_modelo.gif";
	private static final String IMAGE_GEOPISTA = "img/geopista.gif";
	private CLocalGISAppFrame localGISAppFrame;
	private SingleInstanceService sis;
	private SingleInstanceListener sisL;

	private JLabel connectionLabel = new JLabel();

	private ApplicationContext aplicacion;

	public static void createDir() {
		File file = new File("logs");

		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public CMainCentralizadorSSO() {
		if (SSOAuthManager.isSSOActive()) {
			// NUEVO
			addJnlpSingleInstanceListener();
			// FIN NUEVO

			aplicacion = (AppContext) AppContext.getApplicationContext();
			AppContext.getApplicationContext().setMainFrame(this);

			try {
				initLookAndFeel();
			} catch (Exception e) {
			}
			System.out.println(System.getProperty("user.dir"));
			SplashWindow splashWindow = showSplash();
			initComponents();
			setSize(900, 700);
			this.setResizable(false);
			setVisible(true);
			configureApp();
			if (splashWindow != null)
				splashWindow.setVisible(false);
			
			inicio();
			changeScreenLang(messages);
		} else {
			JOptionPane.showMessageDialog(this,
					"Esta aplicación solo funciona en modo Single Sign On");
			System.exit(0);
		}
	}

	public void inicio() {
		try {
			// Minimizado de la barra de iconos de tareas activas del sistema
			// (sustituye al minimizado convencional)
			// traySystemConfig();

			SSOAuthManager.ssoAuthManager(idApp);
			if (!AppContext.getApplicationContext().isOnlyLogged()) {
				// ******************
				// Mostramos la pantalla de autenticación del usuario.
				// ******************
				while (!AppContext.getApplicationContext().isOnlyLogged()) {
					showAuth();
				}
			}
			SecurityManager.setHeartBeatTime(10000);

			gestionLocalGISAppActionPerformed();

			changeCabecera();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString() + " - Error en la carga");
			JOptionPane.showMessageDialog(this, "ERROR: " + sw.toString());
		}
	}

	private boolean showAuth() {
		CAuthDialog auth = new CAuthDialog(this, true, Constantes.url,
				CMainCentralizadorSSO.idApp, Constantes.idEntidad, messages);
		auth.setBounds(30, 60, 315, 155);
		auth.show();
		return true;
	}

	private boolean configureApp() {

		try {

			// ****************************************************************
			// ** Inicializamos el log4j
			// *******************************************************
			try {
				PropertyConfigurator.configureAndWatch("config"
						+ File.separator + "log4j.ini", 3000);
			} catch (Exception e) {
			}
			;
			try {
				Constantes.Locale = app.getString(
						AppContext.GEOPISTA_LOCALE_KEY, "es_ES");
				Constantes.url = AppContext.getApplicationContext().getString(
						SSOConstants.SSO_SERVER_URL);
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				JOptionPane.showMessageDialog(this,
						"Excepcion al cargar el fichero de configuración:\n"
								+ sw.toString());
				logger.error("Exception: " + sw.toString()
						+ " - Error en la configuracion");
				sis.removeSingleInstanceListener(sisL);
				System.exit(-1);
				return false;
			}

			System.out.println("administrador.Constantes.url: "
					+ Constantes.url);
			System.out.println("administrador.Constantes.timeout: "
					+ Constantes.timeout);

			logger.debug("administrador.Constantes.url: " + Constantes.url);
			logger.debug("administrador.Constantes.timeout: "
					+ Constantes.timeout);
			// ****************************************************************
			// ** Establecemos el idioma especificado en la configuracion
			// *******************************************************
			setLang(Constantes.Locale);
			return true;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			JOptionPane.showMessageDialog(
					this,
					"Excepcion al cargar el fichero de configuración:\n"
							+ sw.toString());
			logger.error("Exception: " + sw.toString()
					+ " - Error al cargar el idioma");
			return false;
		}

	}

	private JPanel getJPanelStatus() {
		if (jPanelStatus == null) {
			jPanelStatus = new JPanel(new GridBagLayout());

			jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());

			/*
			 * helpLabel.setIcon(IconLoader.icon("help.gif"));
			 * helpLabel.setToolTipText
			 * (aplicacion.getI18nString("geopista.Help"));
			 * helpLabel.addMouseListener(new MouseListener() {
			 * 
			 * public void mouseClicked(MouseEvent e) {
			 * mostrarAyudaActionPerformed(); }
			 * 
			 * public void mouseEntered(MouseEvent e) { }
			 * 
			 * public void mouseExited(MouseEvent e) { }
			 * 
			 * public void mousePressed(MouseEvent e) { }
			 * 
			 * public void mouseReleased(MouseEvent e) { }
			 * 
			 * });
			 */

			connectionLabel.setBorder(new javax.swing.border.SoftBevelBorder(
					javax.swing.border.SoftBevelBorder.LOWERED));
			jPanelStatus.add(connectionLabel, new GridBagConstraints(0, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			jPanelStatus.add(new JPanel(),
					new GridBagConstraints(1, 0, 4, 1, 1.0, 1.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5,
									0), 0, 0));

			/*
			 * jPanelStatus.add(helpLabel, new GridBagConstraints(5, 0, 1, 1,
			 * 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new
			 * Insets(0, 0, 0, 0), 0, 0));
			 */
		}
		return jPanelStatus;
	}

	private void gestionLocalGISAppActionPerformed() {
		if (desktopPane.getAllFramesInLayer(JDesktopPane
				.getLayer(new JInternalFrame())).length > 0)
			return;
		Container c = this.getContentPane();
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// setLang(Constantes.Locale);
		localGISAppFrame = new com.geopista.app.centralizadorsso.localgisapp.CLocalGISAppFrame(
				messages);
		// localGISAppFrame.changeScreenLang(messages);
		mostrarJInternalFrame(localGISAppFrame);
		c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		c.setCursor(Cursor.getDefaultCursor());
	}

	/*
	 * Minimizado de la barra de iconos de tareas activas del sistema
	 */
	private void traySystemConfig() {
		popupTray = new javax.swing.JPopupMenu();
		menuItemRestore = new javax.swing.JMenuItem();
		menuItemCloseApp = new javax.swing.JMenuItem();
		menuItemCloseSessionAndApp = new javax.swing.JMenuItem();

		menuItemRestore.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuItemRestoreActionPerformed();
			}
		});

		popupTray.add(menuItemRestore);
		popupTray.addSeparator();

		menuItemCloseSessionAndApp
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						closeSessionAndApp();
					}
				});

		popupTray.add(menuItemCloseSessionAndApp);
		popupTray.addSeparator();

		menuItemCloseApp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeSessionAndApp();
			}
		});

		popupTray.add(menuItemCloseApp);

		popupTray.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!popupTray.contains(e.getX(), e.getY()))
					popupTray.setVisible(false);
			}
		});

		addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				stateChanged(e);
			}
		});

		trayIcon = new TrayIcon(new ImageIcon(
				CMainCentralizadorSSO.class
						.getResource(IMAGE_ICON_LOCALGIS_MODELO)).getImage());

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		CMain.setDefaultLookAndFeelDecorated(true);
		desktopPane = new javax.swing.JDesktopPane();
		menuBar = new javax.swing.JMenuBar();

		idiomaMenu = new javax.swing.JMenu();
		castellanoJMenuItem = new javax.swing.JMenuItem();
		catalanJMenuItem = new javax.swing.JMenuItem();
		euskeraJMenuItem = new javax.swing.JMenuItem();
		gallegoJMenuItem = new javax.swing.JMenuItem();
		valencianoJMenuItem = new javax.swing.JMenuItem();
		// helpJMenu = new javax.swing.JMenu();
		// jMenuItemHelp = new javax.swing.JMenuItem();
		// aboutJMenuItem = new javax.swing.JMenuItem();
		pack();
		setBounds(0, 0, 800, 600);

		setBackground(new java.awt.Color(0, 78, 152));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeSessionAndApp();
			}
		});

		desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(null);
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

		// Creacion de objetos.
		getContentPane().add(getJPanelStatus(), BorderLayout.SOUTH);

		idiomaMenu.setMargin(null);
		castellanoJMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
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

		valencianoJMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						setLang(Constantes.LOCALE_VALENCIANO);
					}
				});
		idiomaMenu.add(valencianoJMenuItem);
		menuBar.add(idiomaMenu);
		// AYUDA
		// helpJMenu.setMargin(null);
		// helpJMenu.add(jMenuItemHelp);
		// jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// mostrarAyuda();
		// }
		// });
		// helpJMenu.add(aboutJMenuItem);
		// menuBar.add(helpJMenu);
		setJMenuBar(menuBar);
		pack();

		try {
			ClassLoader cl = this.getClass().getClassLoader();
			java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(
					cl.getResource(IMAGE_GEOPISTA));
			setIconImage(img);
		} catch (Exception e) {
			System.out.println("Icono no encontrado");
		}

		// Gestion de reconexiones
		setConnectionInitialStatusMessage(aplicacion.isOnline());
		aplicacion.addAppContextListener(new AppContextListener() {

			public void connectionStateChanged(GeopistaEvent e) {
				switch (e.getType()) {
				case GeopistaEvent.DESCONNECTED:
					setConnectionStatusMessage(false);
					break;
				case GeopistaEvent.RECONNECTED:
					setConnectionStatusMessage(true);
					break;
				}
			}
		});
	}// GEN-END:initComponents

	public void setConnectionStatusMessage(boolean connected) {
		if (!connected) {

			connectionLabel.setIcon(IconLoader.icon("no_network.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OffLineStatusMessage"));

			// System.out.println("PASO1"+aplicacion.isLogged());
			if (aplicacion.isLogged()){
				SecurityManager.unLogged();
				try {
					SecurityManager.logout();
					SSOAuthManager.clearRegistrySesion();
					localGISAppFrame.setClosed(true);
				}  catch (Exception e) {
					//e.printStackTrace();
					SSOAuthManager.clearRegistrySesion();
					try {
						localGISAppFrame.setClosed(true);
					} catch (PropertyVetoException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			else {
				if (!aplicacion.isPartialLogged()) {
					aplicacion.login();
					// showAuth();
				}
			}
		} else {
			connectionLabel.setIcon(IconLoader.icon("online.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OnLineStatusMessage"));

			// System.out.println("PASO2"+aplicacion.isLogged());
			// System.out.println("PASO2.1"+aplicacion.isPartialLogged());
			if (!aplicacion.isLogged()) {
				if (!aplicacion.isPartialLogged()) {
					logger.error("Mostrando pantalla de autenticacion por desconexion");
					showAuth();
				}
			}

		}
	}

	public void setConnectionInitialStatusMessage(boolean connected) {
		if (!connected) {
			connectionLabel.setIcon(IconLoader.icon("no_network.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OffLineStatusMessage"));

		} else {
			connectionLabel.setIcon(IconLoader.icon("online.png"));
			connectionLabel.setToolTipText(aplicacion
					.getI18nString("geopista.OnLineStatusMessage"));

		}
	}

	private boolean mostrarJInternalFrame(JInternalFrame internalFrame) {

		try {

			int numInternalFrames = desktopPane
					.getAllFramesInLayer(JDesktopPane.getLayer(internalFrame)).length;

			final CMainCentralizadorSSO jFrame = this;
			if (numInternalFrames == 0) {
				internalFrame.setFrameIcon(new javax.swing.ImageIcon("img"
						+ File.separator + "geopista.gif"));

				desktopPane.add(internalFrame);
				internalFrame
						.addInternalFrameListener(new InternalFrameAdapter() {
							public void internalFrameClosed(InternalFrameEvent e) {
								inicio();
							}
						});

				internalFrame.setMaximum(true);
				internalFrame.show();
			} else {
				logger.info("cannot open another JInternalFrame");
			}

		} catch (Exception ex) {
			logger.warn("Exception: " + ex.toString()
					+ " - Error al cargar el panel de aplicaciones");
		}

		return true;
	}

	// public boolean mostrarAyuda()
	// {
	// HelpSet hs = null;
	// ClassLoader loader = this.getClass().getClassLoader();
	// try
	// {
	// String helpSetFile =
	// "help/centralizadorsso/GeopistaHelpCentralizadorSSO_" +
	// com.geopista.app.administrador.init.Constantes.Locale + ".hs";
	// URL url = HelpSet.findHelpSet(loader, helpSetFile);
	// if (url == null)//tomamos el idioma castellano por defecto
	// {
	// logger.error("Imposible cargar el fichero de ayuda: "+helpSetFile);
	// helpSetFile = "help/centralizadorsso/GeopistaHelpCentralizadorSSO_" +
	// com.geopista.app.administrador.init.Constantes.LOCALE_CASTELLANO + ".hs";
	// url = HelpSet.findHelpSet(loader, helpSetFile);
	// }
	// if (url== null)
	// {
	// logger.error("Imposible cargar el fichero de ayuda: "+helpSetFile);
	// return false;
	// }
	// hs = new HelpSet(loader, url);
	// hs.setHomeID(com.geopista.app.administrador.init.Constantes.helpSetHomeID);
	// } catch (Exception ex) {
	// logger.error("Exception: " + ex.toString());
	// return false;
	// }
	// HelpBroker hb = hs.createHelpBroker();
	// hb.setDisplayed(true);
	// new CSH.DisplayHelpFromSource(hb);
	// return true;
	// }

	private boolean setLang(String locale) {

		try {
			System.out.println("Cambiamos el idioma a " + locale);
			logger.debug("Cambiamos idioma a: " + locale);
			try {
				ApplicationContext app = AppContext.getApplicationContext();
				app.setUserPreference(AppContext.GEOPISTA_LOCALE_KEY, locale);
			} catch (Exception e) {
				logger.error("Exception: " + e.toString()
						+ " - Error al cambiar idioma");
			}
			Locale currentLocale = new Locale(locale);
			try {
				messages = ResourceBundle.getBundle("config.centralizadorsso",
						currentLocale);
			} catch (Exception e) {
				messages = ResourceBundle.getBundle("config.centralizadorsso",
						new Locale(Constantes.LOCALE_CASTELLANO));
			}
			logger.info("Changing Screen Lang");
			changeScreenLang(messages);
			Constantes.Locale = locale;
			return true;

		} catch (Exception ex) {
			logger.error("Exception: " + ex.toString()
					+ " - Error al cambiar idioma", ex);
			return false;
		}

	}

	private boolean changeScreenLang(ResourceBundle messages) {

		String title = messages.getString("CMainCentralizadorSSO.title");
		String release = app.getString("localgis.release");
		if (release == null)
			release = "LocalGIS";
		title = title.replaceAll("\\$\\{localgis\\.release\\}", release);
		setTitle(title);
		if (Constantes.Municipio != null)
			setTitle(getTitle() + " - " + Constantes.Municipio + " ("
					+ Constantes.Provincia + ")");
		GeopistaPrincipal principal = SecurityManager.getPrincipal();
		if (principal != null) {
			setTitle(getTitle() + " - "
					+ messages.getString("CAuthDialog.jLabelNombre") + " "
					+ principal.getName());
		}
		idiomaMenu.setText(messages
				.getString("CMainCentralizadorSSO.idiomaMenu"));
		castellanoJMenuItem.setText(messages
				.getString("CMainCentralizadorSSO.castellanoJMenuItem"));
		catalanJMenuItem.setText(messages
				.getString("CMainCentralizadorSSO.catalanJMenuItem"));
		euskeraJMenuItem.setText(messages
				.getString("CMainCentralizadorSSO.euskeraJMenuItem"));
		gallegoJMenuItem.setText(messages
				.getString("CMainCentralizadorSSO.gallegoJMenuItem"));
		valencianoJMenuItem.setText(messages
				.getString("CMainCentralizadorSSO.valencianoJMenuItem"));
		// helpJMenu.setText(messages.getString("CMainCentralizadorSSO.helpJMenu"));
		// aboutJMenuItem.setText(messages.getString("CMainCentralizadorSSO.aboutJMenuItem"));
		// jMenuItemHelp.setText(messages.getString("CMainCentralizadorSSO.jMenuItemHelp"));

		// System.out.println("Centralizador: menuItemRestore!=null: " +
		// menuItemRestore!=null);
		if (menuItemRestore != null)
			menuItemRestore.setText(messages
					.getString("CMainCentralizadorSSO.traySystem.restoreApp"));
		if (menuItemCloseApp != null)
			menuItemCloseApp.setText(messages
					.getString("CMainCentralizadorSSO.traySystem.closeApp"));
		if (menuItemCloseSessionAndApp != null)
			menuItemCloseSessionAndApp
					.setText(messages
							.getString("CMainCentralizadorSSO.traySystem.closeSessionAndApp"));
		if (trayIcon != null)
			trayIcon.setToolTip(messages
					.getString("CMainCentralizadorSSO.traySystem.appTitle"));

		if (localGISAppFrame != null)
			localGISAppFrame.changeScreenLang(messages);
		return true;

	}

	private void changeCabecera() {

		String title = messages.getString("CMainCentralizadorSSO.title");
		String release = app.getString("localgis.release");
		if (release == null)
			release = "LocalGIS";
		title = title.replaceAll("\\$\\{localgis\\.release\\}", release);
		setTitle(title);
		if (Constantes.Municipio != null)
			setTitle(getTitle() + " - " + Constantes.Municipio + " ("
					+ Constantes.Provincia + ")");
		GeopistaPrincipal principal = SecurityManager.getPrincipal();
		if (principal != null) {
			setTitle(getTitle() + " - "
					+ messages.getString("CAuthDialog.jLabelNombre") + " "
					+ principal.getName());
		}

	}

	/**
	 * Exit the Application
	 */
	private void exitForm() {
		dispose();
		try {
			sis.removeSingleInstanceListener(sisL);
		} catch (NullPointerException e) {
		} finally {
			System.exit(0);
		}
	}

	/**
	 * Close SSO Session and Exit the Application
	 */
	private void closeSessionAndApp() {
		try {
			SecurityManager.logout();
			SSOAuthManager.clearRegistrySesion();
			//tray.remove(trayIcon);
			exitForm();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {

		new CMainCentralizadorSSO();

	}

	private void menuItemRestoreActionPerformed() {
		this.setVisible(true);
		this.toFront();
		this.requestFocus();
		this.setState(NORMAL);
		tray.remove(trayIcon);

	}

	private void stateChanged(java.awt.event.WindowEvent evt) {

		if (evt.getNewState() == ICONIFIED) {

			if (SystemTray.isSupported()) {
				this.setVisible(false);

				tray = SystemTray.getSystemTray();

				// final CMainCentralizadorSSO CentralizadorToTime=this;
				MouseListener mouseListener = new MouseListener() {

					public void mouseClicked(MouseEvent e) {
						// if (popupTray.isVisible() &&
						// !popupTray.contains(e.getX(), e.getY())) {
						// popupTray.setVisible(false);
						// } else {
						if (e.getButton() == e.BUTTON1) {
							menuItemRestoreActionPerformed();
						} else {
							popupTray.setInvoker(popupTray);
							popupTray.setVisible(true);
							popupTray.setLocation((e.getX() - (int) popupTray
									.getSize().getWidth()),
									(e.getY() - (int) popupTray.getSize()
											.getHeight()));

							Timer timer = new Timer(3000, new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									popupTray.setVisible(false);
								}
							});
							timer.setRepeats(false);
							timer.start();
						}
						// }
					}

					public void mouseEntered(MouseEvent e) {
					}

					public void mouseExited(MouseEvent e) {
					}

					public void mousePressed(MouseEvent e) {
					}

					public void mouseReleased(MouseEvent e) {
					}
				};

				trayIcon.setImageAutoSize(true);
				trayIcon.addMouseListener(mouseListener);
				trayIcon.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						menuItemRestoreActionPerformed();
					}
				});

				try {
					if (!(tray.getTrayIcons().length > 0)
							|| !(tray.getTrayIcons()[0].equals(trayIcon)))
						tray.add(trayIcon);
				} catch (AWTException e) {
					System.err
							.println("No se pudo agregar el ícono a la barra tray");
				}
				// } else {
				// // System Tray is not supported
			}
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// private javax.swing.JMenuItem aboutJMenuItem;
	private javax.swing.JMenuItem castellanoJMenuItem;
	private javax.swing.JMenuItem catalanJMenuItem;
	private javax.swing.JDesktopPane desktopPane;
	private JPanel jPanelStatus = null;
	private javax.swing.JMenuItem euskeraJMenuItem;
	private javax.swing.JMenuItem gallegoJMenuItem;
	// private javax.swing.JMenu helpJMenu;
	private javax.swing.JMenu idiomaMenu;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem valencianoJMenuItem;
	// private javax.swing.JMenuItem jMenuItemHelp;

	private javax.swing.JMenuItem menuItemRestore;
	private javax.swing.JMenuItem menuItemCloseApp;
	private javax.swing.JMenuItem menuItemCloseSessionAndApp;

	private javax.swing.JPopupMenu popupTray;
	private SystemTray tray;
	private TrayIcon trayIcon;

	@Override
	public void newActivation(String[] arg0) {
		setAlwaysOnTop(true);
		JOptionPane.showMessageDialog(this, "La aplicación ya está abierta");
		setAlwaysOnTop(false);
	}

	public void addJnlpSingleInstanceListener() {
		try {
			SingleInstanceService singleInstanceService = (SingleInstanceService) ServiceManager
					.lookup("javax.jnlp.SingleInstanceService");
			singleInstanceService
					.addSingleInstanceListener((SingleInstanceListener) this);
		} catch (UnavailableServiceException e) {
			logger.info("No encontrado JNLP Single Instance Service: " + e);
		} catch (Exception e) {
			logger.info("Error: " + e);
		}
	}

}
