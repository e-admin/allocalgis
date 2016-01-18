/**
 * LoadProjectScreen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.deegree.framework.xml.XMLParsingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.GeopistaSchemaFactory;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.utils.PropertiesReader;
import es.satec.localgismobile.session.CellPermissionsBean;
import es.satec.localgismobile.session.ProjectInfo;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.utils.PrjParser;
import es.satec.localgismobile.ui.utils.ProjectFolderNameFilter;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;
import es.satec.localgismobile.utils.LocalGISUtils;
import es.satec.svgviewer.localgis.sld.SLDFactory;
import es.satec.svgviewer.localgis.sld.StyledLayerDescriptor;

public class LoadProjectScreen extends LocalGISWindow {
	
	/*Informacion del proyectp*/
	private ProjectInfo selectedProjectInfo;
	/*Ruta de donde se encuentran los proyectos*/
	private String projectsPath=null;
	private ScrolledComposite scrolledComposite = null;
	/*Composite donde va ir la informacion del proyecto seleccionado*/
	private Composite compInformacionProyecto = null;
	/*Clase que se encarga del parseo del proyecto .prj*/
	private PrjParser parserPrj=null;
	/*Vector donde se van a almacenar las rutas de los proyectos
	 * que apareceran por pantalla*/
	private Vector rutaProyecto=new Vector();
	/*Vector donde aparece los nombre de las carpetas
	 * de los mapas que aparcen en la ventana*/
	private Vector nombreProyecto= new Vector();
	/*Tabla donde se insertaran los proyectos*/
	private Table tableArch=null;
	/*Hashtable donde almacenara el parseo de 
	 * los .sld y .sch*/
	private Hashtable sch=new Hashtable();
	private Hashtable sld=new Hashtable();
	/*Toolbar para los botones de aceptar y cancelar*/
	private ToolBar toolBarAceptarCancelar = null;
	private ToolItem toolItemOk=null;
	
	
	Font font;
	Font font2;
	
	private static Logger logger = Global.getLoggerFor(LoadProjectScreen.class);
	
	/*Columnas de la tabla*/
	TableColumn column0=null;
	TableColumn column1=null;
	TableColumn column2=null;
	
	private final int COLUMN_FECHA_SIZE = 150;
	
	Image imageOk;
	Image imageCancel;
	Image imgBorrar;

	public LoadProjectScreen(Shell parent, String projectsPath) {
		super(parent);
		shell.setBackground(Config.COLOR_APLICACION);
		selectedProjectInfo = null;
		this.projectsPath=projectsPath;
		init_destroy();
		init();
		show();
	}
	private void init_destroy(){

		shell.addDisposeListener(new DisposeListener() {
		      public void widgetDisposed(DisposeEvent e) {
		        dispose();
		      }
		});
	}

	public ProjectInfo getSelectedProjectInfo() {
		// Ventana bloqueante
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return selectedProjectInfo;
	}
	
	public void init() {
		
		
		//creacion de la tabla
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;		
		//gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = GridData.FILL;
		int sizeROW=13;
		gridData2.heightHint=sizeROW*8;
		
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.grabExcessHorizontalSpace = false;

		shell.setLayout (new GridLayout (1, true));
		/*Creacion de la tabla donde van a ir el nombre del proyecto y el del municipio*/
		tableArch = new Table(shell,SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tableArch.setLayoutData(gridData2);
		//tableArch.setSize(200,200);
		
		
		font = new Font(shell.getDisplay(), "Arial", 7, SWT.BOLD);
		font2 = new Font(shell.getDisplay(), "Arial", 7, SWT.NORMAL);
		
		tableArch.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			//Cuando se selecciona el proyecto se cargará los datos existentes en el .prj
			public void widgetSelected(SelectionEvent arg0) {
				ScreenUtils.startHourGlass(shell);
				try{
					//Se eliminan los datos que han sido insertados en el composite
					if(compInformacionProyecto.getChildren().length!=0){
						Control [] datosComposite=compInformacionProyecto.getChildren();
						for(int i=0;i<datosComposite.length;i++){
							datosComposite[i].dispose();
						}
					}
					
					//Redimension del scrolled
					Point pSize1=compInformacionProyecto.computeSize(SWT.DEFAULT, SWT.DEFAULT);
					compInformacionProyecto.setSize(pSize1);
					scrolledComposite.setMinSize(pSize1);
					compInformacionProyecto.setVisible(true);
					
	
					//Se inicializa esta clase para compenzar con el parseo del .prj
					//y para sacar la información del ese archivo
					parserPrj= new PrjParser((TableItem) arg0.item,(String)rutaProyecto.elementAt(tableArch.getSelectionIndex()));

					if(parserPrj!=null && parserPrj.getDocument()!=null){
						
						//Se obtiene la lista de item de detail del prj
						NodeList listaNodosItemPrj=parserPrj.getElementsByTagName("item");
						for(int i=0;i<listaNodosItemPrj.getLength();i++){
							//Se obtiene la informacion del .prj
							Node itemPrj=listaNodosItemPrj.item(i);
							Vector vectorItemPrj=parserPrj.getDetailItem(itemPrj);
							//Font font3 = new Font(shell.getDisplay(), "Arial", 7, SWT.BOLD);
							
							Label etiqueta = new Label(compInformacionProyecto, SWT.NONE);
							etiqueta.setBackground(Config.COLOR_APLICACION);
							etiqueta.setText((String) vectorItemPrj.elementAt(0));
							etiqueta.setFont(font);
							//Font font2 = new Font(shell.getDisplay(), "Arial", 7, SWT.NORMAL);
							Text valor = new Text(compInformacionProyecto, SWT.BORDER);
							valor.setText((String) vectorItemPrj.elementAt(1));
							valor.setText(valor.getText().length()<3?(" "+valor.getText()+" "):valor.getText());
							valor.setFont(font2);
							valor.setEnabled(false);
						}
					}
					else{
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR);
						mb.setMessage(Messages.getMessage("LoadProjectScreen.Parser"));
						mb.open();
						toolItemOk.setEnabled(false);
					}
					//Redimension del scrolled
					Point pSize=compInformacionProyecto.computeSize(SWT.DEFAULT, SWT.DEFAULT);
					compInformacionProyecto.setSize(pSize);
					scrolledComposite.setMinSize(pSize);
					compInformacionProyecto.setVisible(true);
				}
			catch(Exception e){
				logger.error("Error al cambiar de pantalla", e);
			}
			finally{
				ScreenUtils.stopHourGlass(shell);
			}
			}
			
		});
		
		createScrolledComposite();
		compInformacionProyecto.setVisible(false);
		Point pSize=compInformacionProyecto.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		compInformacionProyecto.setSize(pSize);
		scrolledComposite.setMinSize(pSize);
		
		/*Creacion del composite aceptar y cancelar*/
		createToolBarMenuVolver();
		
		tableArch.setHeaderVisible(true);
		
		/*Carga de datos en la tabla*/
		if(this.projectsPath!="" && this.projectsPath!=null){
			File dir= new File(this.projectsPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			if(dir.isDirectory() && dir.exists()){
				File[] files=dir.listFiles(new ProjectFolderNameFilter());
				
				// Ordenarlos por fecha
				Arrays.sort(files, new Comparator() {
					public int compare(Object o1, Object o2) {
						File f1 = (File) o1;
						File f2 = (File) o2;
						Date d1 = new Date(f1.lastModified());
						Date d2 = new Date(f2.lastModified());
						return d2.compareTo(d1);
					}
				});

				column0 = new TableColumn(tableArch, SWT.NONE);
				column0.setText(Messages.getMessage("LoadProjectScreen.nombreMapa")+"           ");

				
				column1 = new TableColumn(tableArch, SWT.NONE);
				column1.setText(Messages.getMessage("LoadProjectScreen.idEntidad"));

				column2 = new TableColumn(tableArch, SWT.NONE);
				column2.setText(Messages.getMessage("LoadProjectScreen.fechaExtraccion"));

				column0.pack();
				column1.pack();
				column2.setWidth(COLUMN_FECHA_SIZE);

				for(int x=0;x<files.length;x++){
					File file= files[x];
					String nombreArch=file.getName();
					if(file.isDirectory() && tienePrj(file)){
						try {
							int pos = nombreArch.indexOf(".");
							String nombreMapa=nombreArch.substring(0, pos);
							int pos2 = nombreArch.indexOf(".", pos+1);
							String municipio=nombreArch.substring(pos+1, pos2);
							String id = nombreArch.substring(pos2+1, nombreArch.length());
							Date date = new Date(Long.parseLong(id));
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
							TableItem item = new TableItem(tableArch, SWT.CENTER);
							item.setText(0, nombreMapa);
							item.setText(1, municipio);
							item.setText(2, sdf.format(date));
							rutaProyecto.add(file.getPath());
							nombreProyecto.add(file.getName());
						} catch (Exception e) {
							logger.error(e);
						}
					}
				}
			}
			else{
				MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				mb.setMessage(Messages.getMessage("LoadProjectScreen.errorDirectorioProyectos"));
				mb.open();
				//shell.dispose();
			}
			
		}
		else{
			MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			mb.setMessage(Messages.getMessage("LoadProjectScreen.errorDirectorioProyectos"));
			mb.open();
			//shell.dispose();
		}
		
	}
	
	private void createToolBarMenuVolver() {
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.CENTER;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = GridData.CENTER;
		toolBarAceptarCancelar = new ToolBar(shell, SWT.NONE);
		toolBarAceptarCancelar.setLayoutData(gridData11);
		toolItemOk = new ToolItem(toolBarAceptarCancelar, SWT.PUSH);
		String imagenOk=Config.prResources.getProperty("LayerOrderVisibilityScreen_ok");
		InputStream isOk = this.getClass().getClassLoader().getResourceAsStream(imagenOk);
		imageOk = new Image(Display.getCurrent(), isOk);
		
		toolItemOk.setImage(imageOk);
		toolItemOk.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				if(tableArch.getSelectionIndex()!=-1){
					ScreenUtils.startHourGlass(shell);
					
					recorrerDirectorio();
					//Reyena los datos en la clase ProjectInfo
					selectedProjectInfo = new ProjectInfo();
					selectedProjectInfo.setDocument(parserPrj.getDocument());
					selectedProjectInfo.setGridName(parserPrj.getGridName());
					selectedProjectInfo.setPath(parserPrj.getPath());
					// introduzco 
					 
					selectedProjectInfo.setHashSLD(sld);
					selectedProjectInfo.setHashSCH(sch);
					selectedProjectInfo.setUrlGridFile(parserPrj.getUrlGridFile());
					selectedProjectInfo.setEnabledApplications(parserPrj.getEnabledApplications());
					//Datos del nombre del mapa y del municipio
					TableItem mapaSeleccionado=tableArch.getItem(tableArch.getSelectionIndex());
					selectedProjectInfo.setNombreMapa(mapaSeleccionado.getText(0));
					selectedProjectInfo.setNombreMunicipio(mapaSeleccionado.getText(1));
					selectedProjectInfo.setSrid(Integer.parseInt(parserPrj.getSrid()));
					
					//Se obtiene el nombre del archivo que va a tener los permisos 
					//de usuario y lo almaceno en la clase Permisos Celda
					String nombreArchivoPermisosCeldas=parserPrj.getDatosPrj("users2grid");
					//System.out.println(parserPrj.getPath());
					
					//Numero de Ficheros de Licencias
					String numFicherosLicencias=parserPrj.getDatosPrj("numFichLic");
					selectedProjectInfo.setNumFicherosLicencias(numFicherosLicencias);
					
					CellPermissionsBean permisosCeldas= new CellPermissionsBean((String) nombreProyecto.elementAt(tableArch.getSelectionIndex()),nombreArchivoPermisosCeldas);
					
					//se crea el properties de WMS
					PropertiesReader propertiesWMS=new PropertiesReader(parserPrj.getPath()+File.separator+Config.PROPERTY_WMS, false);
					
					
					selectedProjectInfo.setPermisosCeldas(permisosCeldas);
					selectedProjectInfo.setWMSProperties(propertiesWMS);
					
					if ((compInformacionProyecto!=null) && (!compInformacionProyecto.isDisposed()))
						compInformacionProyecto.dispose();
					
					shell.dispose();
					
				}
			}});
		ToolItem toolItemCancel = new ToolItem(toolBarAceptarCancelar, SWT.PUSH);
		String imagenCancel=Config.prResources.getProperty("LayerOrderVisibilityScreen_cancel");
		InputStream isCancel = this.getClass().getClassLoader().getResourceAsStream(imagenCancel);
		imageCancel = new Image(Display.getCurrent(), isCancel);
		try {
			isOk.close();
			isCancel.close();
		} catch (IOException e) {

			logger.error(e);
		}
		toolItemCancel.setImage(imageCancel);
		toolItemCancel.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				selectedProjectInfo=null;
				shell.dispose();
			}});
		
		// Opcion para borrar proyecto
		ToolItem toolItemBorrar = new ToolItem(toolBarAceptarCancelar, SWT.PUSH);
		String imBorrarFile = Config.prResources.getProperty("SearchScreen_eliminar");
		InputStream isBorrar = null;
		imgBorrar = null;
		try {
			isBorrar = getClass().getClassLoader().getResourceAsStream(imBorrarFile);
			imgBorrar = new Image(Display.getCurrent(), isBorrar);
			toolItemBorrar.setImage(imgBorrar);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (isBorrar != null) {
				try { isBorrar.close(); } catch (Exception e) {}
			}
			if (imgBorrar != null) imgBorrar.dispose();
		}
		
		toolItemBorrar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(tableArch.getSelectionIndex() != -1) {
					MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
					mb.setMessage(Messages.getMessage("LoadProjectScreen.confirmarBorrarProyecto"));
					if (mb.open() == SWT.OK) {
						if (rutaProyecto != null) {
							ScreenUtils.startHourGlass(shell);
							try {
								File projectDir = new File((String)rutaProyecto.elementAt(tableArch.getSelectionIndex()));
								if (deleteDirectory(projectDir)) {
									tableArch.remove(tableArch.getSelectionIndex());
								}
								else {
									mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
									mb.setMessage(Messages.getMessage("LoadProjectScreen.errorBorrarProyecto"));
									mb.open();
								}
							} catch (Exception e) {
								logger.error("Error al borrar proyecto", e);
							} finally {
								ScreenUtils.stopHourGlass(shell);
							}
						}
					}
				}
			}
		});
	}
	
	 

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
        scrolledComposite.setLayout(new FillLayout());
        createComposite();
        scrolledComposite.setContent(compInformacionProyecto);
	}
	private void createComposite() {

		compInformacionProyecto = new Composite(scrolledComposite, SWT.BORDER);
		compInformacionProyecto.setBackground(Config.COLOR_APLICACION);
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		compInformacionProyecto.setLayout(gridLayout2);
		compInformacionProyecto.addDisposeListener(new DisposeListener() {
		      public void widgetDisposed(DisposeEvent e) {
		        destroyComposer();
		      }
		});
	}
	private boolean tienePrj(File dirProyecto) {

		boolean tiene=false;
		String [] archProyectos=dirProyecto.list();
		for(int i=0; i<archProyectos.length && !tiene;i++){
			String nombreArchivo=archProyectos[i];
			int posArchivo=nombreArchivo.indexOf(".");
			if(posArchivo!=-1){
				String extension=nombreArchivo.substring(posArchivo, nombreArchivo.length());
				if(extension.equals(".prj")){
					tiene=true;
				}
			}
		}
		return tiene;
	}
	
	private void recorrerDirectorio() {

		if(tableArch.getSelectionIndex()!=-1 && rutaProyecto!= null){
			File dir = new File((String)rutaProyecto.elementAt(tableArch.getSelectionIndex()));
			File[] files=dir.listFiles();
			if (files == null)
				logger.error("No hay ficheros en el directorio del proyecto");
			else {
				for(int x=0;x<files.length;x++){
					File file= files[x];
					if(file.isFile()){
						String nombreArch=file.getName();
						int posExtension=nombreArch.indexOf(".");
						String exten=nombreArch.substring(posExtension, nombreArch.length());
						if(exten.equals(".sch")){
							loadSch(file, nombreArch, posExtension);
						}
						if(exten.equals(".sld")){
							if (parserPrj!=null)
								loadSld(file, nombreArch, posExtension, parserPrj.getPath());
						}
					}
				}
			}
		}
	}
	
	public void loadSld(File file, String nombreArch, int posExtension, String rutaProyecto){
		if(file.isFile()){
			InputStream issld=null;
			//Cargo los sch
			try {
				String nombArch=nombreArch.substring(0, posExtension);
				String nombreArchivoDecodificado = java.net.URLDecoder.decode(nombArch,"UTF-8");
				issld = new FileInputStream(file);
				
				URL urlBase = new URL("file", "", LocalGISUtils.slashify(rutaProyecto, true));
			    if (urlBase != null) logger.debug("** url base proyecto " + urlBase.toExternalForm());
				
				StyledLayerDescriptor styleld = SLDFactory.createSLD(issld, urlBase);
				sld.put(nombreArchivoDecodificado, styleld);

			} catch (FileNotFoundException e) {

				logger.error(Messages.getMessage(e.getMessage()));
			}
			catch (XMLParsingException e) {

				logger.error(Messages.getMessage(e.getMessage()));
			} catch (IOException e) {

				logger.error(Messages.getMessage(e.getMessage()));
			}
			finally{
				try {
					if (issld!=null)
						issld.close();
				} catch (IOException e) {}
			}
		}
		else{
			logger.error("Error al cargar el SLD");
		}
		
	}
	
	public void loadSch(File file, String nombreArch, int posExtension){
		if(file.isFile()){
			InputStream iSsch;
			//Cargo los sch
			try {
				String nombArch=nombreArch.substring(0, posExtension);
				logger.debug("Loading Schema:"+nombArch);
				String nombreArchivoDecodificado = java.net.URLDecoder.decode(nombArch,"UTF-8");
				//System.out.println("nombreArchivoDecodificado. "+nombreArchivoDecodificado);
				iSsch = new FileInputStream(file);
				GeopistaSchema geoSch;
				geoSch = GeopistaSchemaFactory.loadGeopistaSchema(iSsch);
				sch.put(nombreArchivoDecodificado, geoSch);
				iSsch.close();
			} catch (FileNotFoundException e) {

				logger.error(Messages.getMessage(e.getMessage()));
			}
			 catch (XMLParsingException e) {

				 logger.error(Messages.getMessage(e.getMessage()));
			} catch (IOException e) {

				logger.error(Messages.getMessage(e.getMessage()));
			}
		}
		else{
			logger.error("Error al cargar el SCH");
		}
	}
	
	private boolean deleteDirectory(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			for(int i=0; i<files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return(dir.delete());
	}
	
	private void destroyComposer(){
		/*Se eliminan los datos que han sido insertados en el composite*/
		if(compInformacionProyecto.getChildren().length!=0){
			Control [] datosComposite=compInformacionProyecto.getChildren();
			for(int i=0;i<datosComposite.length;i++){
				datosComposite[i].dispose();
				datosComposite[i]=null;
			}
		}
		compInformacionProyecto.dispose();

	}
	
	public void dispose(){
		if ((tableArch!=null) && (!tableArch.isDisposed()))
			tableArch.dispose();
		if ((imageOk!=null) && (!imageOk.isDisposed()))
			imageOk.dispose();
		if ((imageCancel!=null) && (!imageCancel.isDisposed()))
			imageCancel.dispose();
		if ((imgBorrar!=null) && (!imgBorrar.isDisposed()))
			imgBorrar.dispose();
		
		
		if ((font!=null) && (!font.isDisposed()))
			font.dispose();
		if ((font2!=null) && (!font2.isDisposed()))
			font2.dispose();
		
		//super.dispose();
	}
}
