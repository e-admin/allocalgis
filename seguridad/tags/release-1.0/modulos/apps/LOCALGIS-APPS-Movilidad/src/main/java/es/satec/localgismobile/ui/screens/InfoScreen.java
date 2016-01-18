package es.satec.localgismobile.ui.screens;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import com.geopista.feature.GeopistaSchema;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.global.Utils;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.global.Constants;
import es.satec.localgismobile.ui.utils.ControlsDomainFactory;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.localgismobile.ui.utils.impl.SVGNodeItemImpl;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;
import es.satec.localgismobile.utils.LocalGISUtils;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class InfoScreen extends LocalGISWindow{

	private Label labelTitulo = null;
	private SVGNode node= null;
	private Button button = null;
	private ScrolledComposite scrolledComposite = null;
	private Composite composite = null;
	//Para saber si los campos son editables o no
	private boolean editable=false;
	private SVGLocalGISViewer viewer= null;
	private Label informacion=null;
	private Table tableImagenes = null;
	private Button buttonAnadirImag = null;
	private Button buttonDeleteImag = null;
	private TableColumn column0=null;
	//private TableColumn column1=null;
	private Canvas canvasThumb;
	private Image thumb;
	
	Font font=null;
	
	private static Logger logger = Global.getLoggerFor(InfoScreen.class);
	
	private HashMap controls = new HashMap();

	public InfoScreen(Shell parent,SVGNode n, SVGLocalGISViewer view, boolean edit) {

		super(parent);
	
		shell.setBackground(Config.COLOR_APLICACION);
		node=n;
		viewer=view;
		editable=edit;
		init();
		ScreenUtils.startHourGlass(shell);
		try{
			leerNodo();
		}catch(Exception e){
			logger.error(e, e);
		}
		finally{
			ScreenUtils.stopHourGlass(shell);
		}
		show();
		
		if(node.getImageURLs()!=null){
			for(int i=0; i<node.getImageURLs().size();i++){
				try {
					//URL url = new URL((String)node.getImageURLs().elementAt(i));
					//File f = new File(url.getFile());
					//String[] text = new String[]{f.getName(), url.getPath()};
					String text = (String) node.getImageURLs().elementAt(i);
					TableItem item = new TableItem(tableImagenes, SWT.CENTER);
					item.setText(text);	
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
		else{
			try{
				buttonDeleteImag.setEnabled(false);
			}catch(Exception e){
				logger.error(e);
			}
			
		}

	}

	
	public void leerNodo(){
		if(node.nameAtts.size()==0){
			node=node.parent;
		}
		
		font = new Font(shell.getDisplay(), "Arial", 7, SWT.BOLD);
		for(int i=0; i<node.nameAtts.size();i++){
			//if(node.parent.getValueLayertAtt(i)!=null && !node.parent.getValueLayertAtt(i).equals(Constants.DOMAINFIELD_CODPROVINCIA) && !node.parent.getValueLayertAtt(i).equals(Constants.DOMAINFIELD_CODMUNICIPIO)){
				String val=viewer.getActiveLayer();
				GeopistaSchema geoSche=viewer.getGeopistaSchema(val);
				
				Label label1 = new Label(composite, SWT.NONE);
				label1.setText(node.parent.nameAtts.elementAt(i).toString());
				label1.setBackground(Config.COLOR_APLICACION);
				//Domain domain=geoSche.getAttributeDomain(n);
				label1.setFont(font);
				ControlsDomainFactory controlDomainFactory = new ControlsDomainFactory();
				String attName = node.parent.getValueLayertAtt(i);
				SVGNodeItemImpl itemNode = new SVGNodeItemImpl(node, attName);
				String val1 = node.getValueLayertAtt(i);
				if (val1 == null) val1="";
				logger.debug("Campo: " + label1.getText() + ", Valor: " + val1);
				controls.put(attName, controlDomainFactory.createControl(composite, geoSche, attName, val1 , editable, informacion, itemNode));
			//}			
		}
		//Boton de las imagenes
		//Creacion de la tabla con imagenes		
		creacionTabla();
	
		Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(pSize);
		scrolledComposite.setMinSize(pSize);
//		boton almacenar
		
		crearBotonAlmacenar();
	}
	
	private void crearBotonAlmacenar() {

		GridData gridData1 = new GridData();
		gridData1.horizontalSpan = 3;
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.grabExcessHorizontalSpace = true;
		button = new Button(shell, SWT.NONE);
		button.setText(Messages.getMessage("InfoScreen_Almacenar"));
		button.setLayoutData(gridData1);
			button.addListener(SWT.Selection, new Listener(){
	
				public void handleEvent(Event arg0) {
					//propagamos la información del nodo para el resto de nodos del mismo grupo
					int featGroup = node.getGroup();
					if(featGroup!=-1){
						Object[] nodeList = node.parent.children.data;
						SVGNode nodeChild = null;
						Vector nameAtts = node.nameAtts;
						for (int i = 0; i < nodeList.length; i++) {
							nodeChild = (SVGNode) nodeList[i];
							if(nodeChild!=null && !nodeChild.equals(node) && nodeChild.getGroup()==featGroup){
								//igualamos cada nodo a los del nodo actual
								for (int j = 0; j < nameAtts.size(); j++) {
									try {
										nodeChild.setExtendedAttributeAndRecordEvent(j, (String) nameAtts.get(j));
									} catch (Exception e) {
										logger.error("Error al igualar el nodo " + nodeChild + " :" +e, e);
									}
								}
								nodeChild.changeEvent = null;
							}
						}
					}
					viewer.drawSVG();
					shell.dispose();
				}
				
			});
	}

	private void creacionTabla() {

		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		tableImagenes = new Table(composite,SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tableImagenes.setHeaderVisible(true);
		tableImagenes.setLayoutData(gridData);
		if(!editable){
			tableImagenes.setEnabled(false);
		}
		
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.BEGINNING;
		gridData2.grabExcessHorizontalSpace = false;
		gridData2.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.BEGINNING;
		gridData1.grabExcessHorizontalSpace = false;
		gridData1.verticalAlignment = GridData.CENTER;
		
		
		
		column0 = new TableColumn(tableImagenes, SWT.NONE);
		column0.setText(Messages.getMessage("InfoScreen_NombreImagen")+"       ");
		
		
		/* Zona para dibujar la vista previa de la imagen */
		canvasThumb = new Canvas(composite, SWT.BORDER);
		canvasThumb.setSize(100, 100);
		canvasThumb.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent ev) {
				if (thumb != null && !thumb.isDisposed()) {
					ScreenUtils.startHourGlass(LocalGISMobile.getMainWindow().getShell());
					int imWidth = thumb.getBounds().width;
					int imHeight = thumb.getBounds().height;
					int canvasWidth = canvasThumb.getBounds().width;
					int canvasHeight = canvasThumb.getBounds().height;
					logger.debug("Tamaño de la imagen: (" + imWidth + "x" + imHeight + ")");
					logger.debug("Tamaño del canvas: (" + canvasWidth + "x" + canvasHeight + ")");
					/* Dibujar la imagen proporcional al area de dibujo */
					int destX = 0;
					int destY = 0;
					int destW = canvasWidth;
					int destH = canvasHeight;
					if (imWidth > imHeight) {
						if (imWidth < canvasWidth) {
							destW = imWidth;
							destX = canvasWidth/2-destW/2;
						}
						destH = (int) ((destW*1.0/imWidth)*imHeight);
						destY = canvasHeight/2-destH/2;
					}
					else {
						if (imHeight < canvasHeight) {
							destH = imHeight;
							destY = canvasHeight/2-destH/2;
						}
						destW = (int) ((destH*1.0/imHeight)*imWidth);
						destX = canvasWidth/2-destW/2;
					}
					logger.debug("Dibujando vista previa en (" + destX + ", " + destY + " " + destW + ", " + destH + ")");
					ev.gc.drawImage(thumb, 0, 0, imWidth, imHeight, destX, destY, destW, destH);
					ScreenUtils.stopHourGlass(LocalGISMobile.getMainWindow().getShell());
				}
			}
		});
		
		/* Listener que crea la imagen de vista previa */
		tableImagenes.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent event) {
				TableItem item = (TableItem) event.item;
//				String path = item.getText(1);
//				if (thumb != null) thumb.dispose();
//				try {
//					thumb = new Image(shell.getDisplay(), path);
//				} catch (Throwable t) {
//					logger.error("Error al cargar la imagen de vista previa", t);
//				}
//				logger.debug("Dibujando vista previa de imagen " + path);
				String urlString = item.getText(0);
				
				//if (urlString.startsWith("filelocal"))
				//	urlString = urlString.replace("filelocal", "file");
				if (thumb != null) thumb.dispose();
				InputStream is = null;
				try {
					URL url = new URL(urlString);
					is = url.openStream();
					thumb = new Image(shell.getDisplay(), is);
				} catch (Throwable t) {
					logger.error("Error al cargar la imagen de vista previa", t);
				} finally {
					if (is!=null) {
						try { is.close(); } catch (IOException e) {}
					}
				}
				logger.debug("Dibujando vista previa de imagen " + urlString);
				
				canvasThumb.redraw();
			}
		});
		
		/* Botones para las imagenes */
		Composite buttonsComposite = new Composite(composite, SWT.NONE);
		GridLayout buttonsLayout = new GridLayout();
		buttonsLayout.numColumns = 1;
		buttonsComposite.setLayout(buttonsLayout);
		buttonsComposite.setBackground(Config.COLOR_APLICACION);
		
		buttonAnadirImag = new Button(buttonsComposite, SWT.NONE);
		buttonAnadirImag.setLayoutData(gridData1);
		buttonAnadirImag.setText(Messages.getMessage("InfoScreen_Anadir"));
		if(!editable){
			buttonAnadirImag.setEnabled(false);
		}
		buttonAnadirImag.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				if(fd == null ) return;
				String[] filterExt = {"*.jpg","*.jpeg","*.gif","*.png","*.bmp","*.tiff","*.psd","*.pcx"};
				fd.setFilterExtensions(filterExt);
				String ruta = fd.open();
				if (ruta != null) {
					File file = new File(ruta);
					try {
						URL imgurl = new URL("file", "", LocalGISUtils.slashify(file.getAbsolutePath(), file.isDirectory()));
						String imgUrlAsString = imgurl.toExternalForm();
						if (node.getImageURLs() != null && node.getImageURLs().contains(imgUrlAsString)) {
							logger.debug("La imagen ya ha sido añadida " + imgUrlAsString);
							MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_INFORMATION | SWT.OK);
							mb.setMessage(Messages.getMessage("InfoScreen_ImagenDuplicada"));
							mb.open();
						}
						else {
							TableItem item = new TableItem(tableImagenes, SWT.CENTER);
							//String[] text = new String[]{file.getName(),file.getPath()};
							//item.setText(text);
							item.setText(imgUrlAsString);
							logger.debug("Añadiendo imagen con url: " + imgUrlAsString);
							node.addImageURL(imgUrlAsString);
						}
					}
					catch (Exception e) {
						logger.error(e);
					}
				}
				buttonDeleteImag.setEnabled(true);
				/*Point p=tableImagenes.getSize();
				int height=tableImagenes.getItemHeight();
				tableImagenes.setSize(p.x, p.y+height);
				Point p1=buttonAnadirImag.getLocation();
				buttonAnadirImag.setLocation(p1.x,p1.y+height);
				Point p2=buttonDeleteImag.getLocation();
				buttonDeleteImag.setLocation(p2.x,p2.y+height);
				Point p3=button.getLocation();
				button.setLocation(p3.x,p3.y+height);*/
				Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				composite.setSize(pSize);
				scrolledComposite.setMinSize(pSize);
			}});
		buttonDeleteImag = new Button(buttonsComposite, SWT.NONE);
		buttonDeleteImag.setLayoutData(gridData2);
		buttonDeleteImag.setText(Messages.getMessage("InfoScreen_Borrar"));
		if(!editable){
			buttonDeleteImag.setEnabled(false);
		}
		buttonDeleteImag.addListener(SWT.Selection, new Listener(){

			public void handleEvent(Event event) {

				if(tableImagenes.getSelectionIndex()!=-1){
					int val=tableImagenes.getSelectionIndex();
					tableImagenes.remove(val);
					//System.out.println(val);
					node.removeImageURL(val);
				}
				
			}
		});
				
		column0.pack();
//		column1.pack();
		Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		
		composite.setSize(pSize);
		scrolledComposite.setMinSize(pSize);
	}

	/**
	 * This method initializes shell
	 */
	private void init() {
		
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = GridData.CENTER;
		
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 8;
		shell.setText(Messages.getMessage("InfoScreen_Informacion"));
		
		//shell.setSize(new Point(226, 264));
		shell.setLayout(gridLayout);
		//shell.setSize(Display.getDefault().getClientArea().width,Display.getDefault().getClientArea().height);
		labelTitulo = new Label(shell, SWT.NONE);
		labelTitulo.setText(Messages.getMessage("InfoScreen_InfNod"));
		labelTitulo.setBackground(Config.COLOR_APLICACION);
		labelTitulo.setLayoutData(gridData);
		
		
		createScrolledComposite();
		
		informacion = new Label(shell, SWT.NONE);
		informacion.setLayoutData(gridData11);
		informacion.setBackground(Config.COLOR_APLICACION);
		/*Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(pSize);
		scrolledComposite.setMinSize(pSize);*/
	}



	/**
	 * This method initializes scrolledComposite	
	 *
	 */
	private void createScrolledComposite() {

		scrolledComposite=new ScrolledComposite(shell,SWT.H_SCROLL|SWT.V_SCROLL);
		scrolledComposite.setBackground(Config.COLOR_APLICACION);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setLayout(new GridLayout());
		scrolledComposite.setExpandHorizontal(true);
        GridData gridData1=new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.verticalAlignment = GridData.FILL;
        gridData1.grabExcessVerticalSpace = true;
        scrolledComposite.setLayoutData(gridData1);
        createComposite();
        scrolledComposite.setContent(composite);

	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridData gridData1=new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.verticalAlignment = GridData.FILL;
        gridData1.grabExcessVerticalSpace = true;
		composite = new Composite(scrolledComposite, SWT.BORDER);
		composite.setBackground(Config.COLOR_APLICACION);
		composite.setLayoutData(gridData1);
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		composite.setLayout(gridLayout2);
		
		composite.addDisposeListener(new DisposeListener() {
		      public void widgetDisposed(DisposeEvent e) {
		        dispose();
		      }
		});

	}
	
	public void dispose(){
		if ((font!=null) && (!font.isDisposed()))
			font.dispose();
		
		if ((tableImagenes!=null) && (!tableImagenes.isDisposed()))
			tableImagenes.dispose();
		if ((thumb!=null) && (!thumb.isDisposed()))
			thumb.dispose();
		
	}

}
