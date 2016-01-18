/**
 * CellScreenComposite.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

import com.geopista.feature.GeopistaSchema;
import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.core.Application;
import es.satec.localgismobile.core.Applications;
import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.net.communications.HttpManager;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.utils.PropertiesReader;
import es.satec.localgismobile.fw.validation.exceptions.LoginException;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.localgismobile.ui.widgets.ToolBarMain;
import es.satec.localgismobile.utils.LocalGISUtils;
import es.satec.svgviewer.event.SVGViewerDrawListener;
import es.satec.svgviewer.event.SVGViewerPointInsertedListener;
import es.satec.svgviewer.localgis.MetaInfo;
import es.satec.svgviewer.localgis.SVGLocalGISController;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;
import es.satec.svgviewer.localgis.SynchChangesUtils;
import es.satec.svgviewer.localgis.WMSData;
import es.satec.svgviewer.localgis.sld.StyledLayerDescriptor;

public class CellScreenComposite extends Composite {

	private static Logger logger = Global.getLoggerFor(CellScreenComposite.class);  //  @jve:decl-index=0:
	
	private SVGLocalGISViewer viewer;

	private Vector metaInfos;  //  @jve:decl-index=0:

	//Clase que se encarga de la toolbar
	ToolBarMain toolBarMain=null;

	public CellScreenComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		SessionInfo session = SessionInfo.getInstance();
		
		metaInfos = new Vector();
		session.getCellInfo().setMetaInfos(metaInfos);

		setLayout(new GridLayout());

		createViewer();

		createToolbar();
		

		// Controller para seleccion de elementos del svg
		SVGLocalGISController controller= new SVGLocalGISController(viewer);
		controller.setBrightness(0.8f);
        controller.setContrast(0.8f);

		// Establecer el gps provider
		viewer.setGPSProvider(session.getGPSProvider());
		
		//Añadir imagen puntero GPS
		org.eclipse.swt.graphics.Image gpsPointer = new org.eclipse.swt.graphics.Image(getDisplay(), ClassLoader.getSystemClassLoader().getResourceAsStream(Config.prResources.getProperty("GPS_pointer")));
		viewer.setGPSPointerImage(gpsPointer);
		
		// Cargar el svg de la celda seleccionada
		try {
			// Cargar los estilos y esquemas
			Hashtable hashSLD = session.getProjectInfo().getHashSLD();
			Enumeration e = hashSLD.keys();
			while (e.hasMoreElements()) {
				String layer = (String) e.nextElement();
				StyledLayerDescriptor sld = (StyledLayerDescriptor) hashSLD.get(layer);
				viewer.putSLD(sld, layer, null, true, false);
			}
			
			Hashtable hashSch = session.getProjectInfo().getHashSCH();
			e = hashSch.keys();
			while (e.hasMoreElements()) {
				String layer = (String) e.nextElement();
				GeopistaSchema schema = (GeopistaSchema) hashSch.get(layer);
				viewer.putGeopistaSchema(schema, layer);
			}
			
			// Cargar las capas WMS
			PropertiesReader prWMS = session.getProjectInfo().getWMSProperties();
			if (prWMS.fileExists()) {
				int wmsLayers = prWMS.getPropertyAsInt("NumberOfLayers", 0);
				for (int i=0; i<wmsLayers; i++) {
					String baseUrl = prWMS.getProperty("WMS.baseURL." + i);
					String version = prWMS.getProperty("WMS.GetMapVersion." + i);
					String layers = prWMS.getProperty("WMS.GetMapLayers." + i);
					String srs = prWMS.getProperty("WMS.GetMapSRS." + i);
					String format = prWMS.getProperty("WMS.GetMapFormat." + i);
					if (baseUrl!=null && version!=null && layers!=null && srs!=null && format!=null) {
						WMSData wmsData = new WMSData(baseUrl, version, layers, "", srs, format);
						viewer.addMapServer(wmsData);
					}
					else {
						logger.warn("Parametros incompletos para la capa WMS " + i);
					}
				}
			}
			else {
				logger.info("No existe fichero wms.properties");
			}

			// Cargar el SVG
			String projectPath = session.getProjectInfo().getPath();
			String selectedCell = session.getCellInfo().getSelectedCell();
			URL svgUrl = new URL("file", "", LocalGISUtils.slashify(projectPath + File.separator + selectedCell + ".svg", false));
			viewer.loadSVGUrl(svgUrl);
			
			// Instanciar la metainformacion
			Vector enabledApplications = session.getProjectInfo().getEnabledApplications();
			if (enabledApplications != null) {
				e = enabledApplications.elements();
				while (e.hasMoreElements()) {
					Application a = Applications.getInstance().getApplicationByName((String) e.nextElement());
					if (a != null) {
						metaInfos.addElement(new MetaInfo(projectPath, selectedCell, a.getName(), a.getKeyAttribute(),
								Config.prLocalgis.getPropertyAsInt(SessionInfo.getInstance().getProjectInfo().getNumFicherosLicencias(), 1)));
					}
				}
			}

			
						
			
		} catch (Exception e) {
			logger.error("Error al cargar el svg de la celda seleccionada", e);
			MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
			mb.setText(Messages.getMessage("errores.error"));
			mb.setMessage(Messages.getMessage("CellScreen.cargarCeldaError"));
			mb.open();
		}
	}

	private void createViewer() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;

		viewer = new SVGLocalGISViewer(this, SWT.NO_BACKGROUND,true);


		viewer.setLayoutData(gridData);
		
		viewer.addSVGViewerDrawListener(new SVGViewerDrawListener() {
			public void beginDraw() {
				ScreenUtils.startHourGlass(LocalGISMobile.getMainWindow().getShell());
			}

			public void endDraw() {
				ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
			}
		});
		
		viewer.addSVGViewerPointInsertedListener(new SVGViewerPointInsertedListener(){

            public void pointInserted(SVGNode arg0) {
                if(arg0!=null){
                    if(viewer.getActiveLayerNode().isEditable()){
                        //System.out.println(arg0.toString());
                        new CreateElementInfoScreen(LocalGISMobile.getMainWindow().getShell(),arg0,viewer);
                    }
                    else{
                        System.out.println("No es editable");
                    }
                    //test.viewer.moveNode(node.parent, node, node.parent.children.count-1);
                }
                else{
                    System.out.println("No existe nodo");
                }
            }

        });

		// Seguimiento GPS desactivado
		viewer.setGPSTrackingActive(false);
	}

	private void createToolbar() {
		//toolBarMain= new ToolBarMain(sShell,viewer,properties,messages,busqu, mapSvgLinkListener);
		toolBarMain= new ToolBarMain(LocalGISMobile.getMainWindow().getShell(), viewer);
		toolBarMain.createToolBarMain();
	}

	/**
	 * Guarda el proyecto en el servidor remoto.
	 */
	public void saveProjectRemote() {
		logger.debug("Guardando celda en remoto...");
		/*Inicio el reloj*/
		ScreenUtils.startHourGlass(LocalGISMobile.getMainWindow().getShell());

		SVGDocument doc = viewer.getSVGDocument();
		if (doc == null || doc.root == null) {
			logger.warn("El documento svg esta vacío");
			return;
		}

		ByteArrayOutputStream baos = null;
		ByteArrayInputStream bais = null;
		FileOutputStream fos = null;
		try {
			if (!isModified()) {
				logger.debug("La celda no ha sido modificada");
				MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_INFORMATION | SWT.OK);
				mb.setMessage(Messages.getMessage("CellScreen.celdaNoModificada"));
				mb.open();
			}
			else {
				boolean errorsLayer = false;
				String layers[] = viewer.getAllIDLayers();
				for (int i=layers.length-1; i>=0; i--) {
					if (SynchChangesUtils.ERRORS_LAYER_NAME.equals(layers[i])) {
						errorsLayer = true;
						break;
					}
				}
				
				SessionInfo session = SessionInfo.getInstance();
				String projectPath = session.getProjectInfo().getPath();
				String selectedCell = session.getCellInfo().getSelectedCell();
				int srid = session.getProjectInfo().getSrid();
				
				// URL de conexion con el servidor
				String url = Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_PROTOCOL) + "://" +
					Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_HOST) + ":" +
					Config.prLocalgis.getPropertyAsInt(Config.PROPERTY_SERVER_PORT, 80) +
					Config.prLocalgis.getProperty(Config.PROPERTY_SERVER_CONTEXT) +
					Config.prLocalgis.getProperty(Config.PROPERTY_SAVE_MAP_QUERY);
				logger.debug("URL: " + url);
			
				// Serializacion y envio de las modificaciones del svg
				logger.debug("Enviando cambios serializados para la celda actual");
				viewer.resetSLDChanges();
				baos = new ByteArrayOutputStream();
				SynchChangesUtils.serializeToUpload(baos, doc, metaInfos, srid);
					
				HttpManager httpManager = new HttpManager(url, "POST", "text/xml", false, null);
				httpManager.enviar(baos.toByteArray());

				byte[] respuesta = httpManager.getRespuestaBytes();
				
				// descomentar si se quiere depurar la respuesta del server de Nachete..!!
			   //imprimeRespuesta (respuesta);
				// Lectura e interpretacion de la respuesta del servidor
				bais = new ByteArrayInputStream(respuesta);
				Parser p = new Parser();
				p.setNodeFactory(new DomNodeFactory());
				p.setInputStream(bais);
				p.parse();
				Document responseDoc = p.getDocument();
				
				int code = SynchChangesUtils.readResponseToResetSVGChanges(responseDoc, doc, metaInfos);
				if (code == 0 || code == 1 || code ==3) {
					// Guardar el documento svg en local para que no haya inconsistencias
					logger.debug("Guardando en local");
					fos = new FileOutputStream(projectPath + File.separator + selectedCell + ".svg");
					doc.serializeSVG2XML(fos);
					doc.setSavePending(false); // Desmarcamos como pendiente de guardar
					// Guardar la metainformacion
					Enumeration e = metaInfos.elements();
					while (e.hasMoreElements()) {
						MetaInfo metaInfo = (MetaInfo) e.nextElement();
						metaInfo.unload();
					}

					// Repintar para quitar la capa de errores
					if (errorsLayer) {
						viewer.drawSVG();
					}
				}
				if (code == 0) {
					logger.debug("Celda guardada con exito");
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_INFORMATION | SWT.OK);
					mb.setMessage(Messages.getMessage("CellScreen.finGuardado"));
					mb.open();
					//NUEVO
					viewer.drawSVG();
					//FIN NUEVO
				}
				else if (code == 1 || code ==3) {
					// Ha habido errores al almacenar las modificaciones en remoto
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
					if (code ==1)
						mb.setMessage(Messages.getMessage("SaveRemoteScreen.errores.entidades"));
					else
						mb.setMessage(Messages.getMessage("SaveRemoteScreen.errores.entidades.permisos"));
					mb.open();
				
					// Redibujar para que se muestre la capa de errores
					viewer.drawSVG();
					
					logger.error("Ha habido errores al almacenar las modificaciones en remoto");
				
				}
				else if (code == 2) {
					// Error interno en el servidor
					logger.error("Se ha producido un error en el servidor");
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
					mb.setMessage(Messages.getMessage("SaveRemoteScreen.error.servidor"));
					mb.open();
					
				}
			}
		} catch (LoginException e) {
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
			logger.error("No hay sesion de usuario", e);
			MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
			mb.setMessage(Messages.getMessage("SaveRemoteScreen.error.sesionUsuario"));
			if (mb.open() == SWT.OK) {
				new LoginScreen(LocalGISMobile.getMainWindow().getShell(), SWT.NONE);
			}
		} catch (NoConnectionException e) {
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
			logger.error("No hay conexion", e);
			showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.noConexion"));
		} catch (MalformedURLException e) {
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
			logger.error("URL incorrecta", e);
			showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.urlServidor"));
		} catch (FileNotFoundException e) {
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
			logger.error("Fichero de celda no encontrado", e);
			showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.noFicheroCelda") + " "/* + selectedCells[i]*/);
		} catch (Exception e) {
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
			logger.error("Error al enviar los cambios serializados", e);
			showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.enviar"));
		} finally {
			try {
				if (baos != null) baos.close();
				if (bais != null) bais.close();
				if (fos != null) fos.close();
			} catch (IOException e) {}
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
		}
		 
	}

private void imprimeRespuesta(byte[] respuesta ) {
	String path = SessionInfo.getInstance().getProjectInfo().getPath();
	try {
		String file = path + File.separator  + "server_response.bytes";
		logger.debug(" a imprimir en file " + file);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(respuesta);
		fos.flush();
		fos.close();
		
	} catch (Exception e) {
		 
		e.printStackTrace();
	}
		
	}

	/**
	 * Guarda el proyecto en local.
	 */
	public void saveProjectLocal() {
		logger.debug("Guardando celda en local...");
		
		/*Inicio del reloj*/
		ScreenUtils.startHourGlass(LocalGISMobile.getMainWindow().getShell());
		
		SVGDocument doc = viewer.getSVGDocument();
		if (doc == null || doc.root == null) {
			logger.warn("El documento svg esta vacío");
			return;
		}

		SessionInfo session = SessionInfo.getInstance();
		String projectPath = session.getProjectInfo().getPath();
		String selectedCell = session.getCellInfo().getSelectedCell();
			
		FileOutputStream fos = null;
		try {
			// Guardar el mapa
			if (doc.isModified()) {
				viewer.resetSLDChanges();
				fos = new FileOutputStream(projectPath + File.separator + selectedCell + ".svg");
				viewer.serializeSVG2XML(fos);
				doc.setSavePending(false); // Desmarcamos como pendiente de guardar
			}
			// Guardar la metainformacion
			Enumeration e = metaInfos.elements();
			while (e.hasMoreElements()) {
				MetaInfo metaInfo = (MetaInfo) e.nextElement();
				metaInfo.unload();
			}
			
			logger.debug("Celda guardada con exito");
			MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_INFORMATION | SWT.OK);
			mb.setMessage(Messages.getMessage("CellScreen.finGuardado"));
			mb.open();
			//NUEVO
			viewer.drawSVG();
			//FIN NUEVO
		} catch (FileNotFoundException e) {
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
			logger.error("Fichero de celda no encontrado", e);
			showErrorMessageBox(Messages.getMessage("SaveRemoteScreen.error.noFicheroCelda") + " " + selectedCell);
		} catch (IOException e) {
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
			logger.error("Error al guardar celda en local", e);
			showErrorMessageBox(Messages.getMessage("CellScreen.guardarCeldaError"));
			return;
		} finally {
			try {
				if (fos != null) fos.close();
			} catch (IOException e) {}
			ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
		}

	}

	private void showErrorMessageBox(String message) {
		MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		mb.setText(Messages.getMessage("errores.error"));
		mb.setMessage(message);
		mb.open();
	}
	
	public boolean isModified() throws IOException {
		if (viewer.getSVGDocument() != null && viewer.getSVGDocument().isModified()) {
			return true;
		}
		Enumeration e = metaInfos.elements();
		while (e.hasMoreElements()) {
			MetaInfo metaInfo = (MetaInfo) e.nextElement();
			if (metaInfo.isModified())
				return true;
		}
		
		/*String layers[] = viewer.getAllIDLayers();
		for (int i=layers.length-1; i>=0; i--) {
			if (SynchChangesUtils.ERRORS_LAYER_NAME.equals(layers[i])) {
				return true;
			}
		}*/
		
		return false;
	}
	
	public boolean isSavePending() {
		if (viewer.getSVGDocument() != null && viewer.getSVGDocument().isSavePending()) {
			return true;
		}
		Enumeration e = metaInfos.elements();
		while (e.hasMoreElements()) {
			MetaInfo metaInfo = (MetaInfo) e.nextElement();
			if (metaInfo.isSavePending())
				return true;
		}
		return false;
	}
	
	public void centerGPS() {		
		if (!viewer.goGPS()) {
			MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
			mb.setMessage(Messages.getMessage("CellScreenComposite.noPosicionGPS"));
			mb.open();
		}
	}
	
	public void setGPSTrackingActive(boolean gpsTrackingActive) {
		viewer.setGPSTrackingActive(gpsTrackingActive);
	}
	
	public SVGLocalGISViewer getViewer(){
		return viewer;
	}
	
	public void dispose(){

		if (viewer!=null){
			Vector listener1=viewer.getSVGViewerDrawListeners();
			if (listener1!=null){
				for (int i=0;i<listener1.size();i++){
					SVGViewerDrawListener listener=(SVGViewerDrawListener)listener1.elementAt(i);
					viewer.removeSVGViewerDrawListener(listener);	
				}
			}
			if ((viewer!=null) && (!viewer.isDisposed()))
				viewer.dispose();
		}

		if (toolBarMain!=null)
			toolBarMain.dispose();
		super.dispose();
	}
}
