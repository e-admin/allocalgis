package es.satec.localgismobile.ui.screens;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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

import com.geopista.feature.Attribute;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.tinyline.svg.SVGGroupElem;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.global.Constants;
import es.satec.localgismobile.global.Utils;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.utils.ControlsDomainFactory;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.localgismobile.ui.utils.impl.SVGNodeItemImpl;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;
import es.satec.localgismobile.utils.LocalGISUtils;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CreateElementInfoScreen extends LocalGISWindow {

	private Label labelTitulo = null;
	private SVGNode node= null;
	private Button button = null;
	private ScrolledComposite scrolledComposite = null;
	private Composite composite = null;
	SVGLocalGISViewer viewer= null;
	Label informacion=null;
	private Table tableImagenes = null;
	private Button buttonAnadirImag = null;
	private Button buttonDeleteImag = null;
	TableColumn column0=null;
	TableColumn column1=null;
	Font font;
	
	private static Logger logger = Global.getLoggerFor(CreateElementInfoScreen.class);
	
	private HashMap controls = new HashMap();

	public CreateElementInfoScreen(Shell parent, SVGNode n, SVGLocalGISViewer view) {
		
		super(parent);
		ScreenUtils.startHourGlass(shell);
		try{
			node=n;
			viewer=view;
			init();
		}
		catch(Exception e){
			logger.error("Error al cambiar de pantalla", e);
		}
		finally{
			ScreenUtils.stopHourGlass(shell);
		}
		
		show();
		shell.addShellListener(new ShellListener(){

			public void shellActivated(ShellEvent arg0) {
				
			}

			public void shellClosed(ShellEvent arg0) {
				viewer.cancelDraw();
			}

			public void shellDeactivated(ShellEvent arg0) {
				
			}

			public void shellDeiconified(ShellEvent arg0) {
				
			}

			public void shellIconified(ShellEvent arg0) {
				
			}
			
		});
		Control [] cont=composite.getChildren();
		int anchura=0;
		/*for(int i=0;i<cont.length;i++){
			if(cont[i].getClass().getName().equals("org.eclipse.swt.widgets.Text")){
				Text t1=(Text)cont[i];
				Point tamamo=t1.getSize();
				if(anchura<tamamo.x){
					anchura=tamamo.x;
				}
			}
			
		}
		for(int i=0;i<cont.length;i++){
			if(cont[i].getClass().getName().equals("org.eclipse.swt.widgets.Text")){
				Text t1=(Text)cont[i];
				t1.setSize(anchura,t1.getSize().y);
			}
		}*/
		if(node.getImageURLs()!=null){
			for(int i=0; i<node.getImageURLs().size();i++){
				TableItem item = new TableItem(tableImagenes, SWT.CENTER);
				File f= new File((String)node.getImageURLs().elementAt(i));
				String[] text = new String[]{f.getName(),f.getPath()};
				item.setText(text);	
			}
		}
		else{
			buttonDeleteImag.setEnabled(false);
			
		}
	}
	
	public void leerNodo(){		
		
		ControlsDomainFactory controlDomainFactory = new ControlsDomainFactory(controls);
		String val=viewer.getActiveLayer();
		GeopistaSchema geoSche=viewer.getGeopistaSchema(val);		
		font = new Font(shell.getDisplay(), "Arial", 7, SWT.BOLD);

		for(int i=0; i<node.parent.nameAtts.size();i++){
		//	if(!node.parent.getValueLayertAtt(i).equals(Constants.DOMAINFIELD_ID) && !node.parent.getValueLayertAtt(i).equals(Constants.DOMAINFIELD_CODPROVINCIA) && !node.parent.getValueLayertAtt(i).equals(Constants.DOMAINFIELD_CODMUNICIPIO)){
				Label label1 = new Label(composite, SWT.NONE);
				label1.setBackground(Config.COLOR_APLICACION);
				label1.setText(node.parent.nameAtts.elementAt(i).toString());
				label1.setFont(font);
				
				String attName = node.parent.getValueLayertAtt(i);
				node.addNewExtendedAttribute(attName);
				SVGNodeItemImpl itemNode = new SVGNodeItemImpl(node, attName);
				String val1 = null;
				
				boolean editable = true;
				//CAMBIAR
//				if(node.parent.getValueLayertAtt(i).equals(Constants.DOMAINFIELD_ID_MUNICIPIO)){
				
//				}
//				else if(node.parent.getValueLayertAtt(i).equals(Constants.DOMAINFIELD_CLAVE)){
//					SVGGroupElem svgGroupElem = (SVGGroupElem) node.parent;
//					val1 = svgGroupElem.getSystemId();
//					editable = false;
//				}
				
				controls.put(attName,controlDomainFactory.createControl(composite, geoSche, attName, val1, editable, informacion, itemNode));
			//}
		}
		
		/**Añadir los atributos que estan en el esquema y no estan en el svg**/
		Set listaAtributosSch = geoSche.getAttributeKeys();
		Iterator iter = listaAtributosSch.iterator();
	    while (iter.hasNext()) {
	    	String nombreAtributo = (String) iter.next();
	    	Domain domain=geoSche.getAttributeDomain(nombreAtributo);
	    	if(domain != null && !domain.isNullable() && !isGeometry(nombreAtributo,domain)){
	    		int indiceAtributo = node.parent.getPosByNameLayertAtt(nombreAtributo);
	    		if(indiceAtributo == -1){
	    			/**Añado el nombre del atributo**/
	    			Label label1 = new Label(composite, SWT.NONE);
	    			label1.setBackground(Config.COLOR_APLICACION);
	    			label1.setText(nombreAtributo);
	    			label1.setFont(font);
	    			node.addNewExtendedAttribute(nombreAtributo);
	    			SVGNodeItemImpl itemNode = new SVGNodeItemImpl(node, nombreAtributo);
	    			controlDomainFactory.createControl(composite, geoSche, nombreAtributo, null, true, informacion, itemNode);
	    		}
	    	}
	    }
	    //System.out.println("aqui");

	}

	/**
	 * Los campos de tipo geometria normalmente vienen sin dominio pero por ejemplo
	 * si por algun tipo de error como por ejemplo en la capa de verterdero viene
	 * con un dominio de tipo String intentamos que no se muestre.
	 * @param nombreAtributo
	 * @param domain
	 * @return
	 */
	private boolean isGeometry(String nombreAtributo, Domain domain){
		if ((nombreAtributo.equalsIgnoreCase("GEOMETRIA")) ||
				(nombreAtributo.equalsIgnoreCase("GEOMETRY")))
			return true;
		else
			return false;
	}
	
	private boolean campoText(Attribute atrib) {

		if(atrib.getType().equals("BYTE") || atrib.getType().equals("CHAR") || atrib.getType().equals("VARCHAR") || atrib.getType().equals("NUMERIC") || atrib.getType().equals("INTEGER") || atrib.getType().equals("STRING") || atrib.getType().equals("DOUBLE")){
			return true;
		}
		else{
			return false;
		}
		
	}

	/**
	 * This method initializes shell
	 */
	private void init() {
		
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalSpan = 3;
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.grabExcessHorizontalSpace = false;
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 8;
		gridLayout.marginWidth = 14;
		gridLayout.horizontalSpacing = 6;
		shell.setText(Messages.getMessage("CreateElementInfoScreen_Informacion"));
		shell.setBackground(Config.COLOR_APLICACION);
		//shell.setSize(new Point(226, 264));
		shell.setLayout(gridLayout);
		//shell.setSize(Display.getDefault().getClientArea().width,Display.getDefault().getClientArea().height);
		labelTitulo = new Label(shell, SWT.NONE);
		labelTitulo.setText(Messages.getMessage("InfoScreen_InfNod"));
		labelTitulo.setBackground(Config.COLOR_APLICACION);
		labelTitulo.setLayoutData(gridData);
		informacion = new Label(shell, SWT.NONE);
		informacion.setBackground(Config.COLOR_APLICACION);
		informacion.setLayoutData(gridData11);
		informacion.setData(new Vector());
		
		createScrolledComposite();
		leerNodo();
		Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(pSize);
		scrolledComposite.setMinSize(pSize);
		
		creacionTabla();
		
		//boton almacenar
		button = new Button(shell, SWT.NONE);
		button.setText(Messages.getMessage("CreateElementInfoScreen_Almacenar"));
		button.setLayoutData(gridData1);
		button.addListener(SWT.Selection, new Listener(){

			public void handleEvent(Event arg0) {
				//NUEVO
				//CAMBIAR : SI TIPO_EIEL && (TAMBIEN!!!!)  MIRAR SI PONERLO EN EL CAMBIODEL COMBO idMunicip
				//Utils.fillEielControlsINE(informacion, controls, viewer.getGeopistaSchema(viewer.getActiveLayer()), "CA", true);
				//Utils.fillEielControlsClave(informacion, controls, viewer.getGeopistaSchema(viewer.getActiveLayer()), "CA", true);
				//FIN NUEVO
				
				
				Vector erroresProducidos = (Vector) informacion.getData();
				if(erroresProducidos.size() != 0){
				//if(informacion.getForeground().equals(Display.getDefault().getSystemColor(SWT.COLOR_RED))){
				//if(mensajeError.equals("true")){
					MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
					mb.setMessage(Messages.getMessage("CreateElementInfoScreen_Error"));
					if (mb.open() == SWT.OK){
						viewer.cancelDraw();
						shell.dispose();
					}
				}	
				else{
					shell.dispose();
				}
				viewer.drawSVG();
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
		
		
		column1 = new TableColumn(tableImagenes, SWT.NONE);
		column1.setText(Messages.getMessage("InfoScreen_RutaImagen")+"           ");
		
		buttonAnadirImag = new Button(composite, SWT.NONE);
		buttonAnadirImag.setLayoutData(gridData1);
		buttonAnadirImag.setText(Messages.getMessage("InfoScreen_Anadir"));
		buttonAnadirImag.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {
                /***ASO añade porque no funcionaba bien 25-03-2010***/
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
				/******************************************/
				/**** ASO comenta porque no funciona bien**
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				if(fd == null ) return;
				String[] filterExt = {"*.jpg","*.jpeg","*.gif","*.png","*.bmp","*.tiff","*.psd","*.pcx"};
				fd.setFilterExtensions(filterExt);
				String ruta = fd.open();
				if (ruta != null) {
					File file = new File(ruta);
					try {
						TableItem item = new TableItem(tableImagenes, SWT.CENTER);
						//URL urlImagen = new URL("file", "", slashify(file.getAbsolutePath(), file.isDirectory()));
						String[] text = new String[]{file.getName(),file.getPath()};
						item.setText(text);	
						node.addImageURL(file.getPath());
					}
					catch (Exception e) {
						logger.error(e);
					}
				}*/
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
		buttonDeleteImag = new Button(composite, SWT.NONE);
		buttonDeleteImag.setLayoutData(gridData2);
		buttonDeleteImag.setText(Messages.getMessage("InfoScreen_Borrar"));
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
		column1.pack();
		
		Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(pSize);
		scrolledComposite.setMinSize(pSize);
		
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
		if ((tableImagenes!=null) && (!tableImagenes.isDisposed()))
			tableImagenes.dispose();	
		if ((font!=null) && (!font.isDisposed()))
			font.dispose();	
	}

}
