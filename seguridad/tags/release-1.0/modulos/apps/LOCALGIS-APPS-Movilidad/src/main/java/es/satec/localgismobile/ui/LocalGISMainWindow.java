package es.satec.localgismobile.ui;


import java.io.File;


import org.apache.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

import com.japisoft.fastparser.document.Document;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGNode;
import com.tinyline.tiny2d.TinyRect;
import com.tinyline.tiny2d.TinyVector;

import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.validation.exceptions.LoginException;
import es.satec.localgismobile.global.Constants;
import es.satec.localgismobile.proxyserver.LocalGISProxyServer;
import es.satec.localgismobile.session.ProjectInfo;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.screens.CellScreenComposite;
import es.satec.localgismobile.ui.screens.DownloadProjectScreen;
import es.satec.localgismobile.ui.screens.GPSDataScreen;
import es.satec.localgismobile.ui.screens.GridScreenComposite;
import es.satec.localgismobile.ui.screens.LoadProjectScreen;
import es.satec.localgismobile.ui.screens.LoginScreen;
import es.satec.localgismobile.ui.screens.LoginScreenComposite;
import es.satec.localgismobile.ui.screens.MenuScreenComposite;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.svgviewer.localgis.GPSProvider;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

public class LocalGISMainWindow extends LocalGISWindow {
	
	public static final int LOGIN_SCREEN_COMPOSITE = 1;
	public static final int MENU_SCREEN_COMPOSITE = 2;
	public static final int GRID_SCREEN_COMPOSITE = 3;
	public static final int CELL_SCREEN_COMPOSITE = 4;
	

	
	private Composite composite;
	private boolean closed;
	private int idCurrentComposite;
	
	// Componentes del menu
	private Menu mainMenu;
	private MenuItem openItem;
	private MenuItem downloadItem;
	private MenuItem saveItem;
	private MenuItem saveLocalItem;
	private MenuItem saveRemoteItem;
	private MenuItem backItem;
	private MenuItem exitItem;
	
	private Menu gpsMenu;
	private MenuItem activateGPSItem;
	private MenuItem activateGPSTrackingItem;
	private MenuItem centerGPSItem;
	private MenuItem gpsStateItem;
	
	private static Logger logger = Global.getLoggerFor(LocalGISMainWindow.class);
	
	public LocalGISMainWindow(Display display, int idComposite) {
		super(display);
		closed = false;
		init();
		go(idComposite);
		show();
	}

	private void init() {
		shell.setText("LocalGIS");
		shell.setLayout(new FillLayout());
		
		shell.addShellListener(new ShellListener() {
			public void shellActivated(ShellEvent e) {
			}

			public void shellClosed(ShellEvent e) {
				if (confirmIgnoreChanges()) {
					closed = true;
				}
				else {
					e.doit = false; // Cancela el cierre
				}
			}

			public void shellDeactivated(ShellEvent e) {
			}

			public void shellDeiconified(ShellEvent e) {
			}

			public void shellIconified(ShellEvent e) {
			}
		});
	}
	
	public void go(int idComposite) {
		ScreenUtils.startHourGlass(shell);
		try{
			if (composite!=null && !composite.isDisposed()) {
				composite.dispose();
			}
			
			switch (idComposite) {
			case LOGIN_SCREEN_COMPOSITE:
				// Vaciar el menu
				if (menuBar.getItemCount() > 0) {
					menuBar = new Menu(shell, SWT.BAR);
					shell.setMenuBar(menuBar);
				}
				
				composite = new LoginScreenComposite(shell, SWT.NONE);
				break;
			case MENU_SCREEN_COMPOSITE:
				if (menuBar.getItemCount() == 0) {
					fillMenu();
				}
				saveItem.setEnabled(false);
				backItem.setEnabled(false);
				activateGPSTrackingItem.setSelection(false);
				activateGPSTrackingItem.setEnabled(false);
				centerGPSItem.setEnabled(false);
				
				composite = new MenuScreenComposite(shell, SWT.NONE);
				break;
			case GRID_SCREEN_COMPOSITE:
				saveItem.setEnabled(true);
				saveLocalItem.setEnabled(false);
				backItem.setEnabled(true);
				activateGPSTrackingItem.setSelection(false);
				activateGPSTrackingItem.setEnabled(false);
				centerGPSItem.setEnabled(false);
				
				composite = new GridScreenComposite(shell, SWT.NONE);

				break;
			case CELL_SCREEN_COMPOSITE:
				saveLocalItem.setEnabled(true);
				activateGPSTrackingItem.setEnabled(activateGPSItem.getSelection());
				centerGPSItem.setEnabled(activateGPSItem.getSelection());
				
				composite = new CellScreenComposite(shell, SWT.NONE);
			
				break;
			}
			
			
			if (composite!=null && !composite.isDisposed()) {
				composite.setSize(shell.getClientArea().width, shell.getClientArea().height);
				
				//**********************************
				//Realizamos un zoom a la cuadricula desconectada por si no coincide exactamente
				//con lo que vemos en pantalla. Esto puede ser debido a que caigan
				//elementos muy grandes dentro de la zona desconectada.
				//**********************************
				if (idComposite==CELL_SCREEN_COMPOSITE){
					autoZoom();					
				}
			}
		
			
			idCurrentComposite = idComposite;
		}
		catch(Exception e){
			logger.error("Error al cambiar de pantalla", e);
		}
		finally{
			ScreenUtils.stopHourGlass(shell);
		}
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	/**
	 * Autozoom a la zona indicada por la cuadricula.
	 */
	private void autoZoom(){

		SVGLocalGISViewer viewer=((CellScreenComposite)composite).getViewer();		
		if (viewer!=null){
			SVGDocument root = viewer.getSVGDocument();
			TinyVector svgLayers = root.root.children;
			SVGNode svgLayer = null;
			for (int i=0; i<svgLayers.count; i++) {
				SVGNode layer = (SVGNode) svgLayers.data[i];
				//CAMBIAR
				if (layer.id != null && layer.id.data != null) {
					String id = new String(layer.id.data);					
						if (layer.isActive())
							viewer.setActiveLayer(id);
						if (id.equals(Constants.GRATICULE_LAYER_NAME)) 
							svgLayer = layer;						
				}
			}
			logger.info("Auto Zoom");
			//El elemento 0 de la cuadricula contiene la zona a hacer zoom.
			if (svgLayer!=null){
				TinyVector svgNodes = svgLayer.children; 
				if (svgNodes.count>0){
					SVGNode node = (SVGNode) svgNodes.data[0];
					TinyRect devBounds = viewer.getRasterBounds(node);
					((CellScreenComposite)composite).getViewer().zoomIn(devBounds.xmin, devBounds.ymin, devBounds.xmax, devBounds.ymax,true);			
				}
			}
			logger.info("Auto Zoom realizado");
			
		}
	}
 
	private void fillMenu() {
		MenuItem mainItem = new MenuItem(menuBar, SWT.CASCADE);
		mainItem.setText(Messages.getMessage("menu.principal"));
		mainMenu = new Menu(shell, SWT.DROP_DOWN);
		mainItem.setMenu(mainMenu);

		openItem = new MenuItem(mainMenu, SWT.PUSH);
		openItem.setText(Messages.getMessage("menu.principal.abrir"));
		openItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (confirmIgnoreChanges()) {
					
					//Previo a cargar el proyecto liberamos el anterior que se hubiera generado
					ProjectInfo currentProjectInfo=SessionInfo.getInstance().getProjectInfo();
					if (currentProjectInfo!=null)
						currentProjectInfo.dispose();
					
					LoadProjectScreen loadProjectScreen = new LoadProjectScreen(shell,Global.APP_PATH + File.separator + Config.prLocalgis.getProperty("proyectos.ruta"));
					ProjectInfo projectInfo = loadProjectScreen.getSelectedProjectInfo();
					if (projectInfo != null) {
						SessionInfo.getInstance().setProjectInfo(projectInfo);
						LocalGISMobile.getMainWindow().go(LocalGISMainWindow.GRID_SCREEN_COMPOSITE);
						//NUEVO
						shell.setText("LocalGIS");
					}
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		downloadItem = new MenuItem(mainMenu, SWT.PUSH);
		downloadItem.setText(Messages.getMessage("menu.principal.descargar"));
		downloadItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				checkCallServer();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
			private void checkCallServer() {
				try {
					
					Document documentListMaps=LocalGISProxyServer.listRemoteLocalGISMaps(SessionInfo.getInstance().getValidationBean().getIdUsuario());
					if(documentListMaps!=null){
						new DownloadProjectScreen(shell,documentListMaps);
					}
					else{
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
						mb.setMessage(Messages.getMessage("errores.Doc"));
						mb.open();
					}
				} catch (LoginException e) {
					logger.error("No hay sesion de usuario", e);
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
					mb.setMessage(Messages.getMessage("errores.sesionUsuario"));
					if (mb.open() == SWT.OK) {
						new LoginScreen(LocalGISMobile.getMainWindow().getShell(), SWT.NONE);
					}
				} catch (NoConnectionException e) {
					logger.error("No hay conexion con el servidor", e);
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR);
					mb.setMessage(Messages.getMessage("errores.noConexion"));
					mb.open();
				} catch (Exception e) {
					logger.error("Error en la operacion", e);
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR);
					mb.setMessage(Messages.getMessage("errores.conexion"));
					mb.open();
				}
				
			}
		});
		
		//Submenus del item guardar
		saveItem = new MenuItem(mainMenu, SWT.CASCADE);
		saveItem.setText(Messages.getMessage("menu.principal.guardar"));
		Menu saveSubmenu = new Menu(saveItem);
		saveItem.setMenu(saveSubmenu);
		saveLocalItem = new MenuItem(saveSubmenu, SWT.PUSH);
		saveLocalItem.setText(Messages.getMessage("menu.principal.guardar.local"));
		saveLocalItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				
				if (idCurrentComposite == CELL_SCREEN_COMPOSITE) {
					((CellScreenComposite) composite).saveProjectLocal();
				}
			}
		});
		saveRemoteItem = new MenuItem(saveSubmenu, SWT.PUSH);
		saveRemoteItem.setText(Messages.getMessage("menu.principal.guardar.remoto"));
		saveRemoteItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {

				if (idCurrentComposite == GRID_SCREEN_COMPOSITE) {
					((GridScreenComposite) composite).saveProjectRemote();
				}
				else if (idCurrentComposite == CELL_SCREEN_COMPOSITE) {
					((CellScreenComposite) composite).saveProjectRemote();
				}

			}
		});
		
		backItem = new MenuItem(mainMenu, SWT.PUSH);
		backItem.setText(Messages.getMessage("menu.principal.volver"));
		backItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (confirmIgnoreChanges()) {
					if (idCurrentComposite == GRID_SCREEN_COMPOSITE) {
						LocalGISMobile.getMainWindow().go(LocalGISMainWindow.MENU_SCREEN_COMPOSITE);
						//NUEVO
						shell.setText("LocalGIS");
					}
					//*************************************
					//Volvemos a la ventana donde se muestra la cuadricula para que volvamos
					//a seleccionar otra
					//*************************************
					else if (idCurrentComposite == CELL_SCREEN_COMPOSITE) {
						SessionInfo.getInstance().setCellInfo(null);
						LocalGISMobile.getMainWindow().go(LocalGISMainWindow.GRID_SCREEN_COMPOSITE);
						//NUEVO
						shell.setText("LocalGIS");
					}
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		exitItem = new MenuItem(mainMenu, SWT.PUSH);
		exitItem.setText(Messages.getMessage("menu.principal.salir"));
		exitItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				shell.close();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		MenuItem gpsItem = new MenuItem(menuBar, SWT.CASCADE);
		gpsItem.setText(Messages.getMessage("menu.gps"));
		gpsMenu = new Menu(shell, SWT.DROP_DOWN);
		gpsItem.setMenu(gpsMenu);

		activateGPSItem = new MenuItem(gpsMenu, SWT.CHECK);
		activateGPSItem.setText(Messages.getMessage("menu.gps.activar"));
		activateGPSItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				ScreenUtils.startHourGlass(shell);
				try {
					GPSProvider gpsProvider = SessionInfo.getInstance().getGPSProvider();
					if (activateGPSItem.getSelection()) {
						try {
							gpsProvider.startGPS();
							// Activar otras opciones del menu relacionadas
							gpsStateItem.setEnabled(true);
							if (idCurrentComposite == CELL_SCREEN_COMPOSITE) {
								activateGPSTrackingItem.setEnabled(true);
								centerGPSItem.setEnabled(true);
							}
						} catch (Exception ex) {
							logger.error("Error al arrancar el GPS", ex);
							activateGPSItem.setSelection(false); // Cancelar la accion
							MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
							mb.setText(Messages.getMessage("errores.error"));
							mb.setMessage(Messages.getMessage("menu.gps.activar.error"));
							mb.open();
						}
					}
					else {
						gpsProvider.stopGPS();
						// Desactivar otras opciones del menu relacionadas
						gpsStateItem.setEnabled(false);
						if (idCurrentComposite == CELL_SCREEN_COMPOSITE) {
							activateGPSTrackingItem.setEnabled(false);
							centerGPSItem.setEnabled(false);
						}
					}
				}catch(Exception e1){
					logger.error(e1);
				}
				finally{
					ScreenUtils.stopHourGlass(shell);
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		activateGPSTrackingItem = new MenuItem(gpsMenu, SWT.CHECK);
		activateGPSTrackingItem.setText(Messages.getMessage("menu.gps.activarSeguimiento"));
		activateGPSTrackingItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (idCurrentComposite == CELL_SCREEN_COMPOSITE) {
					((CellScreenComposite) composite).setGPSTrackingActive(activateGPSTrackingItem.getSelection());
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		activateGPSTrackingItem.setEnabled(false); // Deshabilitado mientras no se active el GPS
		
		centerGPSItem = new MenuItem(gpsMenu, SWT.PUSH);
		centerGPSItem.setText(Messages.getMessage("menu.gps.centrar"));
		centerGPSItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (idCurrentComposite == CELL_SCREEN_COMPOSITE) {
					((CellScreenComposite) composite).centerGPS();
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		centerGPSItem.setEnabled(false); // Deshabilitado mientras no se active el GPS

		gpsStateItem = new MenuItem(gpsMenu, SWT.PUSH);
		gpsStateItem.setText(Messages.getMessage("menu.gps.estado"));
		gpsStateItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				// Abrir la ventana de estado de gps
				if(SessionInfo.getInstance().getGPSProvider()!=null && SessionInfo.getInstance().getGPSProvider().getGPSLocation()!=null){
					new GPSDataScreen(shell);
				}
				else{
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR);
					mb.setMessage(Messages.getMessage("errores.GPS"));
					mb.open();
				}
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		gpsStateItem.setEnabled(false); // Deshabilitado mientras no se active el GPS
	}
 
	/**
	 * Si se esta en la pantalla de celda, comprueba si hay cambios no guardados en el documento
	 * actual. Si es así, informa al usuario y pide confirmación de si desea ignorar estos
	 * cambios.
	 * @return true si no hay cambios o se quieren ignorar; false si hay cambios y no se quieren ignorar.
	 */
	private boolean confirmIgnoreChanges() {
		boolean confirmed = true;
		if (idCurrentComposite == CELL_SCREEN_COMPOSITE) {
			if (((CellScreenComposite) composite).isSavePending()) {
				MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				mb.setMessage(Messages.getMessage("aplicacion.cerrar.mapaModificado"));
				if (mb.open() == SWT.NO) confirmed = false;
			}
		}
		return confirmed;
	}
	
}
