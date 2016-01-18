package es.satec.localgismobile.ui.screens;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.japisoft.fastparser.sax.Sax2Parser;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGNode;
import com.tinyline.tiny2d.TinyColor;

import es.satec.localgismobile.core.Application;
import es.satec.localgismobile.core.Applications;
import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.listeners.GridToCellLinkListener;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.svgviewer.event.SVGViewerDrawListener;
import es.satec.svgviewer.event.SVGViewerLoadListener;
import es.satec.svgviewer.localgis.MetaInfo;
import es.satec.svgviewer.localgis.SVGLocalGISController;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;
import org.eclipse.swt.layout.GridData;

public class GridScreenComposite extends Composite {
	
	private static Logger logger = Global.getLoggerFor(GridScreenComposite.class);  //  @jve:decl-index=0:
	
	private SVGLocalGISViewer viewer;

	public GridScreenComposite(Composite parent, int style) {
		super(parent, style);
		init_destroy();
		initialize();
	}
	
	private void init_destroy(){

		this.addDisposeListener(new DisposeListener() {
		      public void widgetDisposed(DisposeEvent e) {
		        dispose();
		      }
		});
	}

	private void initialize() {
		setLayout(new GridLayout());
		createViewer();
		createDateProject();
		
		// Controller para seleccion de elementos del svg
		SVGLocalGISController controller = new SVGLocalGISController(viewer);
		controller.setBrightness(0.8f);
		controller.setContrast(0.8f);
		
		viewer.setBackground(Config.COLOR_APLICACION);
		viewer.setGPSProvider(SessionInfo.getInstance().getGPSProvider());
		viewer.addSVGViewerLoadListener(new SVGViewerLoadListener(){

			public void documentLoaded(SVGDocument doc) {
				aplicarEstilosPermisos(doc);
			}
			
		});
		viewer.loadSVGUrl(SessionInfo.getInstance().getProjectInfo().getUrlGridFile());
		
		viewer.addSVGViewerLinkListener(new GridToCellLinkListener());
	}

	protected void aplicarEstilosPermisos(SVGDocument arg0) {
		String usuarioRegistrado=SessionInfo.getInstance().getValidationBean().getIdUsuario();
		//System.out.println("usuarioRegistrado. "+usuarioRegistrado);
		SVGNode padre=(SVGNode) arg0.root.children.data[0];
		for(int i=0;i<padre.children.count;i++){
			SVGNode cuadriculai=(SVGNode)padre.children.data[i];
			if(cuadriculai.nameAtts.size()!=0){
				String numCuadricula=(String) cuadriculai.nameAtts.elementAt(0);
				if(SessionInfo.getInstance().getProjectInfo().getPermisosCeldas().permisoUsuCelda(numCuadricula, usuarioRegistrado)){
					cuadriculai.fill=new TinyColor(Config.COLOR_CUADRICULA);
					cuadriculai.opacity=Config.OPACITY_CUADRICULA;
				}
			}
		}
	}

	private void createDateProject() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(gridLayout);

		Label labelNombreMapa = new Label(composite, SWT.NONE);
		labelNombreMapa.setText(SessionInfo.getInstance().getProjectInfo().getNombreMapa());
		Label labelNombreMunicipio = new Label(composite, SWT.NONE);
		labelNombreMunicipio.setText(SessionInfo.getInstance().getProjectInfo().getNombreMunicipio());
		Label labelCeldasPermisos = new Label(composite, SWT.NONE);
		labelCeldasPermisos.setText(Messages.getMessage("GridScreenComposite_PermisosCeldas"));
		
		
		int r=(Config.COLOR_CUADRICULA & 0xFF0000) >> 16;
		int g= (Config.COLOR_CUADRICULA & 0x00FF00) >> 8;
		int b=Config.COLOR_CUADRICULA & 0x0000FF;
		
		Color colorFill = new Color(this.getDisplay(), new RGB(r,g,b));
		
		Label labelColorCeldasPermisos = new Label(composite, SWT.NONE);
		labelColorCeldasPermisos.setText("            ");
		labelColorCeldasPermisos.setBackground(colorFill);
	}

	private void createViewer() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		viewer = new SVGLocalGISViewer(this, SWT.NO_BACKGROUND, true);
		viewer.setLayoutData(gridData);
	}


	/**
	 * Guarda el proyecto en el servidor remoto.
	 */
	public void saveProjectRemote() {
		logger.debug("Guardando el proyecto completo en remoto...");
		/*Inicio el reloj*/
		ScreenUtils.startHourGlass(LocalGISMobile.getMainWindow().getShell());
		
		SVGDocument doc = viewer.getSVGDocument();
		if (doc == null || doc.root == null) {
			logger.warn("El documento svg esta vacío");
			return;
		}
		if (doc.root.children.count != 1) {
			logger.warn("El formato del documento svg de rejilla no es el esperado");
			return;
		}
		SVGNode g = (SVGNode) doc.root.children.data[0];
		if (g.children == null || g.children.count <= 0) {
			logger.warn("El formato del documento svg de rejilla no es el esperado");
			return;
		}

		// Vector para almacenar los nombres de las celdas modificadas
		Vector modifiedCells = new Vector();
		// Recorrido de los path de cada celda
		for (int i=0; i<g.children.count; i++) {
			SVGNode cellNode = (SVGNode) g.children.data[i];

			if (cellNode.nameAtts == null || cellNode.nameAtts.isEmpty()) continue;

			String fileName = (String) cellNode.nameAtts.elementAt(0);

			String projectPath = SessionInfo.getInstance().getProjectInfo().getPath(); 
			String filePath = projectPath + File.separator + fileName + ".svg";
			File file = new File(filePath);
			if (file.exists()) {
				// Parsear el svg para ver si ha sido modificado
				FileInputStream fis = null;
				try {
					int n = modifiedCells.size();
					Sax2Parser p = new Sax2Parser();
					p.setContentHandler(new SVGContentHandler(modifiedCells, fileName, p));
					fis = new FileInputStream(file);
					p.setInputStream(fis);
					p.parse();
					
					if (n == modifiedCells.size()) {
						// El mapa no ha sido modificado. Comprobamos la metainformacion
						Vector enabledApplications = SessionInfo.getInstance().getProjectInfo().getEnabledApplications();
						if (enabledApplications != null) {
							Enumeration e = enabledApplications.elements();
							while (e.hasMoreElements()) {
								Application a = Applications.getInstance().getApplicationByName((String) e.nextElement());
								MetaInfo metaInfo = new MetaInfo(projectPath, fileName, a.getName(), a.getKeyAttribute(),
									Config.prLocalgis.getPropertyAsInt(SessionInfo.getInstance().getProjectInfo().getNumFicherosLicencias(), 1));
								if (metaInfo.isModified()) {
									modifiedCells.addElement(fileName);
									break;
								}
							}
						}
					}
				} catch (Exception e) {
					// No deberia ocurrir nunca
					ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
					logger.error("Error al parsear el fichero " + filePath);
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
					mb.setText(Messages.getMessage("errores.error"));
					mb.setMessage(filePath + " " + Messages.getMessage("errores.ficheroNoValido"));
					mb.open();
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {}
					}
				}
			}
		}

		if (modifiedCells.isEmpty()) {
			logger.debug("No hay celdas modificadas");
			MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
			mb.setMessage(Messages.getMessage("GridScreen.guardarRemoto.noCeldasModificadas"));
			mb.open();
		}
		else {
			// Ir a nueva pantalla
			new SaveRemoteScreen(LocalGISMobile.getMainWindow().getShell(), modifiedCells);
		}
		ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
	}
	
	/**
	 * Destruimos los objetos
	 */
	public void dispose(){

		
		if (viewer!=null){
			/*Vector listener1=viewer.getSVGViewerDrawListeners();
			if (listener1!=null){
				for (int i=0;i<listener1.size();i++){
					SVGViewerDrawListener listener=(SVGViewerDrawListener)listener1.elementAt(i);
					viewer.removeSVGViewerDrawListener(listener);	
				}
			}
			Vector listener2=viewer.getSVGViewerLinkListeners();
			if (listener2!=null){
				for (int i=0;i<listener2.size();i++){
					GridToCellLinkListener listener=(GridToCellLinkListener)listener2.elementAt(i);
					viewer.removeSVGViewerLinkListener(listener);	
				}
			}*/
			viewer.dispose();
		}

		super.dispose();
	}

	/**
	 * Listener que recibe los eventos al parsear el svg e interrumpe el proceso tras interpretar
	 * el atributo modified del nodo svg.
	 */
	private class SVGContentHandler implements ContentHandler {

		private Vector modifiedCells;
		private String cell;
		private Sax2Parser parser;

		public SVGContentHandler(Vector modifiedCells, String cell, Sax2Parser parser) {
			this.modifiedCells = modifiedCells;
			this.cell = cell;
			this.parser = parser;
		}

		/**
		 * Detecta el nodo svg, busca el atributo modified y si vale true lo incluye en una lista
		 * de celdas modificadas. Tras tratar el nodo svg, detiene el proceso.
		 */
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			if (localName.equalsIgnoreCase("svg")) {
				for (int j=0; j<atts.getLength(); j++) {
					if (atts.getLocalName(j).equals("modified")) {
						boolean modified = Boolean.valueOf(atts.getValue(j)).booleanValue();
						if (modified) {
							logger.debug("La celda " + cell + " ha sido modificada");
							modifiedCells.addElement(cell);
						}
						break;
					}
				}
				parser.interruptParsing();
			}
		}

		public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		}

		public void endDocument() throws SAXException {
		}

		public void endElement(String arg0, String arg1, String arg2) throws SAXException {
		}

		public void endPrefixMapping(String arg0) throws SAXException {
		}

		public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
		}

		public void processingInstruction(String arg0, String arg1) throws SAXException {
		}

		public void setDocumentLocator(Locator arg0) {
		}

		public void skippedEntity(String arg0) throws SAXException {
		}

		public void startDocument() throws SAXException {
		}

		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
		}
	}

}
