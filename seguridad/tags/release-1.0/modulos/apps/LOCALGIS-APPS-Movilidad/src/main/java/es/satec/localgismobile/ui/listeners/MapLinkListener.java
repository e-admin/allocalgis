package es.satec.localgismobile.ui.listeners;


import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.geopista.feature.GeopistaSchema;
import com.tinyline.svg.SVGGroupElem;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.core.Application;
import es.satec.localgismobile.core.ApplicationOption;
import es.satec.localgismobile.core.Applications;
import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.validation.bean.ValidationBean;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.screens.InfoScreen;
import es.satec.svgviewer.event.SVGViewerLinkListener;
import es.satec.svgviewer.localgis.MetaInfo;
import es.satec.svgviewer.localgis.SVGLocalGISController;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

//Clase para mostrar la informacion de cada parcela viendo los permisos necesarios

public class MapLinkListener implements SVGViewerLinkListener {
	
	SVGLocalGISViewer viewer=null; 
	//boolean verAtributos=true;
	
	private static Logger logger = Global.getLoggerFor(MapLinkListener.class);

	public MapLinkListener(SVGLocalGISViewer view) {
		viewer=view;
	}

	private boolean isCapaErrores (SVGNode n) {
		// compruebo si la capa que me pasan es la de errores.
		if (n.id != null){
	      String idCapa = new String(n.id.data);
	      if (idCapa.equals(es.satec.svgviewer.localgis.SynchChangesUtils.ERRORS_LAYER_NAME)){
	    	  return true;
	      }
	    }
		return false;
	}
	
	public void nodeSelected(final SVGNode n, Point p) {

			if(n!=null && n.parent!=null){
				if(viewer.getActiveLayerNode()!=null){
					//Elimino el listerner creado en la cuadricula
					//Para el caso de la capa especial de errores lo dejo ver siempre
					if (isCapaErrores (n)){
						logger.debug(" Glupssss capa de errores ala,  ventanita maja, sal ya, pero no me toques ná eh..");
						new InfoScreen(LocalGISMobile.getMainWindow().getShell(), n, viewer, false);
					}
				 
					else if(isUuarioPermisos() && validationBean(n)){
						
						// Comprobar las opciones disponibles de las aplicaciones habilitadas
						Vector layerApps = Applications.getInstance().getApplicationsByLayerId(viewer.getActiveLayerSystemId());
						Vector enabledApps = SessionInfo.getInstance().getProjectInfo().getEnabledApplications();
						Vector options = new Vector();
						if (enabledApps != null) {
							// Recorrido de las aplicaciones para la capa
							Enumeration e = layerApps.elements();
							while (e.hasMoreElements()) {
								Application a = (Application) e.nextElement();
								// Recorrido de las aplicaciones habilitadas
								Enumeration e2 = enabledApps.elements();
								while (e2.hasMoreElements()) {
									String enabledApp = (String) e2.nextElement();
									if (a.getName().equals(enabledApp)) {
										Vector appOptions = a.getOptions();
										if (appOptions != null) {
											// Recorrido de las opciones de la aplicacion habilitada
											Enumeration e3 = appOptions.elements();
											while (e3.hasMoreElements()) {
												options.addElement(e3.nextElement());
											}
										}
										break;
									}
								}
							}
						}
						
						if (options.isEmpty()) {
							// Ir directamente a la pantalla de informacion
							new InfoScreen(LocalGISMobile.getMainWindow().getShell(), n, viewer, viewer.getActiveLayerNode().editable);
						}
						else {
							// Mostrar el menu emergente con las opciones
							Menu menu = new Menu(LocalGISMobile.getMainWindow().getShell(), SWT.POP_UP);
							MenuItem item1 = new MenuItem(menu, SWT.PUSH);
							item1.setText(Messages.getMessage("CellScreen.popup.verAtributos"));
							item1.addSelectionListener(new SelectionListener() {
								public void widgetSelected(SelectionEvent e) {
									SVGLocalGISController controller= new SVGLocalGISController(viewer);
									controller.setBrightness(0.8f);
									controller.setContrast(0.8f);
									new InfoScreen(LocalGISMobile.getMainWindow().getShell(),n, viewer,viewer.getActiveLayerNode().editable);
								}
								public void widgetDefaultSelected(SelectionEvent e) {
								}
							});

							Enumeration e = options.elements();
							while (e.hasMoreElements()) {
								final ApplicationOption option = (ApplicationOption) e.nextElement();
								MenuItem item = new MenuItem(menu, SWT.PUSH);
								item.setText(Messages.getMessage(option.getMessageKey()));
								item.addSelectionListener(new SelectionListener() {
									public void widgetSelected(SelectionEvent e) {
										try {
											String screenClass = option.getScreenClass();
											// Instanciar la ventana por reflection
											Class c = Class.forName(screenClass);
											Class[] paramTypes = { Shell.class, SVGNode.class, MetaInfo.class, GeopistaSchema.class, Hashtable.class };
											Constructor constructor = c.getConstructor(paramTypes);
											MetaInfo metaInfo = SessionInfo.getInstance().getCellInfo().getMetaInfo(option.getApplication().getName());
											GeopistaSchema metaInfoSchema = viewer.getGeopistaSchema(metaInfo.getInfoType());
											Object[] params = { LocalGISMobile.getMainWindow().getShell(), n, metaInfo, metaInfoSchema, option.getParams() };
											constructor.newInstance(params);
										} catch (Exception ex) {
											logger.error("Error al instanciar la pantalla de metainformacion", ex);
											mensajeError(Messages.getMessage("MapLinkListener_ErrorPantallaAplicacion"));
										}
									}
									public void widgetDefaultSelected(SelectionEvent e) {
									}
								});
							}

							menu.setLocation(viewer.toDisplay(p.x, p.y));
							menu.setVisible(true);
						}
						
					}
					else{
						if (!isUuarioPermisos())
							logger.error("El usuario no tiene permisos para mostrar la información");
						new InfoScreen(LocalGISMobile.getMainWindow().getShell(),n, viewer,false);
					}
				}
				else{
					
					logger.debug("No hay capa activa");
				}
			}
			//CAMBIAR
//			else{
//				MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
//				mb.setText(Messages.getMessage("errores.error"));
//				mb.setMessage("En esta capa no puede verse los atributos, seleccionela como capa activa");
//				mb.open();
				//logger.debug("En esta capa no puede verse los atributos, seleccionela como capa activa");
//			}
		}
	private boolean validationBean(SVGNode node) {

		boolean validation = false;
		ValidationBean validationBean = SessionInfo.getInstance().getValidationBean();
		
		validationBean.getPermisos();
		SVGGroupElem grupoCapa = (SVGGroupElem) viewer.getActiveLayerNode();

		int [] validationBeanArray = validationBean.getPermisosBySystemId(grupoCapa.getSystemId()); 

		for(int i = 0;i < validationBeanArray.length && !validation; i++){
			//if(validationBeanArray[i] == ValidationBean.PERM_LAYER_WRITE){ //geopista.layer.escribir 872 a fuego
				validation = true;
			//}
		}
		
		return validation;
	}

	//metodo que se encarga de ver los permisos de los usuarios
	private boolean isUuarioPermisos() {

		boolean permisos=false;
		SessionInfo session = SessionInfo.getInstance(); 
		if(session.getProjectInfo().getPermisosCeldas().permisoUsuCelda(session.getCellInfo().getSelectedCell(), SessionInfo.getInstance().getValidationBean().getIdUsuario())){
			permisos=true;
		}
		else{
			mensajeError(Messages.getMessage("MapLinkListener_ErrorPermisos"));
		}
		return permisos;
	}
	protected void mensajeError(String mensaje){
		MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		mb.setMessage(mensaje);
		mb.open();
	}

}
