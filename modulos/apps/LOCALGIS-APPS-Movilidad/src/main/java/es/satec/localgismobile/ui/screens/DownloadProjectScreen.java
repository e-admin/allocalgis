/**
 * DownloadProjectScreen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.InputStream;
import java.util.Collections;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.japisoft.fastparser.document.Document;

import es.satec.localgismobile.core.LocalGISMobile;
import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.net.communications.exceptions.NoConnectionException;
import es.satec.localgismobile.fw.validation.exceptions.LoginException;
import es.satec.localgismobile.proxyserver.LocalGISProxyServer;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.utils.DownloadProjectEntry;
import es.satec.localgismobile.ui.utils.ScreenUtils;

public class DownloadProjectScreen extends LocalGISWindow {
	
	//private Button buttonOk = null;
	private Table tableDownloadProject=null;
	private Document documentListadoMapas=null;
	/*Toolbar para los botones de aceptar*/
	private ToolBar toolBarAceptar = null;
	private ToolItem toolItemOk=null;
	
	private final int COLUMN_FECHA_SIZE = 150;
	
	private static Logger logger = Global.getLoggerFor(DownloadProjectScreen.class);
	
	//Vector que almacena los datos de cada uno de los item del Proyecto
	Vector dateProyect= new Vector();

	Image imageOk;
	
	public DownloadProjectScreen(Shell parent, Document documentListadoMapas) {
		super(parent);

		this.documentListadoMapas=documentListadoMapas;
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

	private void init() {

		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		shell.setLayout(new GridLayout());
		shell.setBackground(Config.COLOR_APLICACION);
		
		createTable();

		toolBarAceptar = new ToolBar(shell, SWT.NONE);
		toolBarAceptar.setLayoutData(gridData1);
		//toolBarAceptar.setBackground(new Color(shell.getDisplay(), new RGB(255, 255, 255)));
		toolItemOk = new ToolItem(toolBarAceptar, SWT.PUSH);
		String imagenOk=Config.prResources.getProperty("LayerOrderVisibilityScreen_ok");
		InputStream isOk = this.getClass().getClassLoader().getResourceAsStream(imagenOk);
		imageOk = new Image(Display.getCurrent(), isOk);

		toolItemOk.setImage(imageOk);
		toolItemOk.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {
				if(dateProyect.size()>0 && tableDownloadProject.getSelectionIndex()!=-1){
					ScreenUtils.startHourGlass(shell);
					try {
						//Le paso el shell, para mostrar el mensaje de warning si el directorio ya esta creado
						LocalGISProxyServer.downloadProject((DownloadProjectEntry)dateProyect.elementAt(tableDownloadProject.getSelectionIndex()), shell);
						
					} catch (LoginException e) {
						logger.error("No hay sesion de usuario", e);
						MessageBox mb = new MessageBox(LocalGISMobile.getMainWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
						mb.setMessage(Messages.getMessage("errores.sesionUsuario"));
						if (mb.open() == SWT.OK) {
							new LoginScreen(shell, SWT.NONE);
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
					finally{
						ScreenUtils.stopHourGlass(shell);
					}
				}
				shell.dispose();
			}
		});
	}

	private void createTable() {

		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = GridData.FILL;
		
		/*Creacion de la tabla donde van a ir el nombre del proyecto, el del municipio y la hora de extracción*/
		tableDownloadProject = new Table(shell,SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tableDownloadProject.setLayoutData(gridData2);
		tableDownloadProject.setHeaderVisible(true);
		
		loadDataTable();
		
	}

	private void loadDataTable() {

		
		/*Se parsea el documento para almacenar la infomación en la tabla*/
		parserDocument();
		
		/*Cargando los datos del vector en la tabla*/
		TableColumn column0 = new TableColumn(tableDownloadProject, SWT.NONE);
		column0.setText(Messages.getMessage("DownloadProjectScreen_NombreMapa"));

		
		TableColumn column1 = new TableColumn(tableDownloadProject, SWT.NONE);
		column1.setText(Messages.getMessage("DownloadProjectScreen_Entidad"));
		
		TableColumn column2 = new TableColumn(tableDownloadProject, SWT.NONE);
		column2.setText(Messages.getMessage("DownloadProjectScreen_FechaExtracion"));
		
		column0.pack();
		column1.pack();
		column2.setWidth(COLUMN_FECHA_SIZE);
		
		
		for(int x=0;x<dateProyect.size();x++){
			try{ Collections.sort(dateProyect); }catch(Exception e){}
			DownloadProjectEntry project= (DownloadProjectEntry)dateProyect.elementAt(x);
			TableItem item = new TableItem(tableDownloadProject, SWT.CENTER);
			item.setText(new String[]{project.getNombreProyecto(), project.getIdMunicipio(), project.getFechaExtraccion()});
		}

	}


	/*Reyenando el vector donde va a ir la informacion de los proyectos*/
	private void parserDocument() {

		Element e = (Element) this.documentListadoMapas.getRoot();
		if(e!=null){
			NodeList ListProject=e.getElementsByTagName("project");
			for(int i=0;i<ListProject.getLength();i++){
				Node itemProject=ListProject.item(i);
				printNode(itemProject,"");
			}
		}
		else{
			logger.error(Messages.getMessage("errores.DatosDoc"));
			MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			mb.setMessage(Messages.getMessage("errores.DatosDoc"));
			mb.open();
		}
	}

	private void printNode(Node node, String indent) {

		DownloadProjectEntry downloadProjectEntry=new DownloadProjectEntry();
		 switch (node.getNodeType()) {
         case Node.ELEMENT_NODE:
             NamedNodeMap attributes = node.getAttributes();
             for (int i=0; i<attributes.getLength(); i++) {
            	 Node current = attributes.item(i);
            	 if(current.getNodeName().equals("idProyecto")){
            		 downloadProjectEntry.setIdProyecto(current.getNodeValue());
            	 }
            	 if(current.getNodeName().equals("nombreProyecto")){
            		 downloadProjectEntry.setNombreProyecto(current.getNodeValue());
            	 }
            	 if(current.getNodeName().equals("fechaExtraccion")){
            		 downloadProjectEntry.setFechaExtraccion(current.getNodeValue());
            	 }
            	 if(current.getNodeName().equals("idMunicipio")){
            		 downloadProjectEntry.setIdMunicipio(current.getNodeValue());
            	 }
            	 if(current.getNodeName().equals("idMap")){
            		 downloadProjectEntry.setIdMap(current.getNodeValue());
            	 }
             }
             break;
		 }
		 dateProyect.add(downloadProjectEntry);
	}

	public void dispose(){
		if ((tableDownloadProject!=null) && (!tableDownloadProject.isDisposed()))
			tableDownloadProject.dispose();
		if ((imageOk!=null) && (!imageOk.isDisposed()))
				imageOk.dispose();
		super.dispose();
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
