/**
 * ToolBarMain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.widgets;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGGroupElem;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.fw.validation.bean.ValidationBean;
import es.satec.localgismobile.global.Constants;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.listeners.DeleteSelectedNodeListener;
import es.satec.localgismobile.ui.listeners.MapLinkListener;
import es.satec.localgismobile.ui.screens.CreateElementInfoScreen;
import es.satec.localgismobile.ui.screens.ECWLayersScreen;
import es.satec.localgismobile.ui.screens.LayerOrderVisibilityScreen;
import es.satec.localgismobile.ui.screens.SHPLayersScreen;
import es.satec.localgismobile.ui.screens.SearchScreen;
import es.satec.localgismobile.ui.screens.StyleScreen;
import es.satec.localgismobile.ui.screens.WMSLayersScreen;
import es.satec.localgismobile.ui.utils.SearchDataBean;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

public class ToolBarMain {
	
	
	ToolBar padre;
	ToolBar hijo1;
	ToolBar hijo2;
	ToolBar hijo3;
	ToolBar hijo4;
	
//	Item que aparecen en la ToolBar
//	Toolbar padre
	FactoryToolBar factorytoolbar= new FactoryToolBar();
	FactoryToolBar factorytoolbar1;
	FactoryToolBar factorytoolbar2;
	FactoryToolBar factorytoolbar3;	
	FactoryToolBar factorytoolbar4;	
	
	ToolItemSatec toolItemPunto=null;
	ToolItemSatec toolZoomIn= null;
	ToolItemSatec toolItemZoomOut=null;
	ToolItemSatec toolItemMano=null;
	ToolItemSatec toolItemInfor=null;
	//ToolItemSatec toolItemInforBorrar=null;
	ToolItemSatec toolItemLinea=null;
	ToolItemSatec toolItemPoligono=null;
	ToolItemSatec toolItemBorrar=null;
	ToolItemSatec toolItemSeleccionBorrar=null;
	ToolItemSatec toolItemVerCapa=null;
	ToolItemSatec toolItemOcultarBusqueda=null;
	ToolItemSatec toolItemWMS=null;
	ToolItemSatec toolItemSHP=null;
	ToolItemSatec toolItemECW=null;
	ToolItemSatec toolItemEstilos=null;
	
	ToolItemSatec toolItemMapa;
	ToolItemSatec toolItemBuscar;
	ToolItemSatec toolItemCapa;
	ToolItemSatec toolItemBusquedas;
	ToolItemSatec toolItemVerCapa2;
	
//	Modo de la seleccion
	private int mode;
	public static final int MODE_NONE = 0;
	public static final int MODE_ZOOMIN = 1;
	public static final int MODE_PAN = 2;
	public static final int MODE_LINK = 3;
	public static final int MODE_POLYLINE = 4;
	public static final int MODE_POLYGON = 5;
	public static final int MODE_POINT = 6;
	public static final int MODE_SELECT = 7;
		
	//Para el numero de veces q se ha seleccionado un boton
	int cont=0;
	int contPan=0;
	int contLink=0;
	int contPoint=0;
	int contPolyline=0;
	int contPolygon=0;
	int conSele=0;
	//int contInfPintar=0;

	Shell sShell=null;
	SVGLocalGISViewer viewer=null;
	 
 
	private static Logger logger = Global.getLoggerFor(ToolBarMain.class);
	
	String borrar="No";
	DeleteSelectedNodeListener listBor=null;
	MapLinkListener mapSvgLinkListener=null;
	
	public ToolBarMain(Shell shell, SVGLocalGISViewer view) {

		sShell=shell;
		viewer=view;
		 
		mapSvgLinkListener=new MapLinkListener(viewer);
		listBor= new DeleteSelectedNodeListener(viewer);	
		
	}
	

	
	public FactoryToolBar getFactoryToolBar(){
		return factorytoolbar;
	}

	public void createToolBarMain(){
		//final ToolBar padre=factorytoolbar.createToolBar(sShell, viewer,Config.prResources.getProperty("ToolBar_volver"), Config.prResources.getProperty("ToolBar_BuscarLetra"), false, null, Config.prResources.getProperty("ToolBar_volverDerecha"));
		//final ToolBar padre=factorytoolbar.createToolBar(sShell, viewer,Config.prResources.getProperty("ToolBarMain.desplegarMenu_on"), Config.prResources.getProperty("ToolBarMain.gestion"), false, null, Config.prResources.getProperty("ToolBarMain.desplegarMenu_off"));

		
		padre=factorytoolbar.createToolBar(sShell, viewer,Config.prResources.getProperty("ToolBarMain.desplegarMenu_on"), Config.prResources.getProperty("ToolBarMain.gestion"), false, null, Config.prResources.getProperty("ToolBarMain.desplegarMenu_off"));
	
		//primer hijo creado de Zoom
		factorytoolbar1= new FactoryToolBar();
		hijo1=factorytoolbar1.createToolBar(sShell, viewer,Config.prResources.getProperty("ToolBarMain.desplegarMenu_on"), Config.prResources.getProperty("ToolBarMain.volver_on"), true, padre, Config.prResources.getProperty("ToolBarMain.desplegarMenu_off"));

		//toolitem imagen para subir al padre
		ToolItem toolItemVolverarriba = factorytoolbar1.getToolItemVolverarriba();
		
		toolItemVolverarriba.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event event) {
				viewer.removeSVGViewerLinkListener(mapSvgLinkListener);
			}
		});		

		//Añade a la toolbar padre una imagen
		//Primer nodo ZOOM
		toolItemMapa= new ToolItemSatec(padre, hijo1, null);
		toolItemMapa.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.gestion"), new Listener(){

			public void handleEvent(Event event) {
				viewer.setModeLink();
				desActivarBoton();
				Point localmenu=padre.getParent().getLocation();
				padre.getParent().setLocation(-1000, -1000);
				//hay que poner el numero de hijo porque no se conoce dinamicamente cual es su hijo
				hijo1.getParent().setLocation(localmenu);
				//hijo1.getParent().setVisible(true);
				viewer.addSVGViewerLinkListener(mapSvgLinkListener);
			}

			private void desActivarBoton() {

				toolZoomIn.itemHijo.setSelection(false);
				toolItemZoomOut.itemHijo.setSelection(false);
				toolItemMano.itemHijo.setSelection(false);
				toolItemInfor.itemHijo.setSelection(false);
			}
		}, Messages.getMessage("ToolBarMain.gestion"));

		//Añado imagenes a los hijos
//		Este hijo se encargar del Zoom +
		toolZoomIn = new ToolItemSatec(hijo1, null, padre);
		
		toolZoomIn.makeItem(sShell,SWT.CHECK, Config.prResources.getProperty("ToolBarMain.acercar_off"),new Listener(){

			public void handleEvent(Event event) {
				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					changeSelectionMode(MODE_ZOOMIN);
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.acercar_off"));
		
		//Zoom -
		toolItemZoomOut= new ToolItemSatec(hijo1, null, padre);
		
		toolItemZoomOut.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.alejar_off"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					viewer.zoomOut();
				}
				else{
					mensajeError( Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.alejar_off"));
		
		/*Mover mapa*/
		toolItemMano= new ToolItemSatec(hijo1, null, padre);
		
		toolItemMano.makeItem(sShell,SWT.CHECK, Config.prResources.getProperty("ToolBarMain.mover_off"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					changeSelectionMode(MODE_PAN);
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.mover_off"));
		
		

		
		/*Informacion de un nodo*/
		toolItemInfor= new ToolItemSatec(hijo1, null, padre);
		
		toolItemInfor.makeItem(sShell,SWT.CHECK, Config.prResources.getProperty("ToolBarMain.info_off"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){			
					//NUEVO
					if(viewer.getActiveLayerNode()==null){
						//Selecciona una por defecto
						String [] allIDLayers = viewer.getAllIDLayers();
						for(int i=0;i<allIDLayers.length;i++)
							if(!allIDLayers[i].equals(Constants.GRATICULE_LAYER_NAME))
								viewer.setActiveLayerNode(allIDLayers[i]);

					}
					//FIN NUEVO
					if(viewer.getActiveLayerNode()==null){						
							mensajeError(Messages.getMessage("ToolBar_ErrorCapa"));
							LayerOrderVisibilityScreen pantInfor= new LayerOrderVisibilityScreen(sShell, viewer.getAllIDLayers(),viewer);
							if (pantInfor.getSelectedLayer()!=null) {
								changeSelectionMode(MODE_LINK);								
							}
							else {
								toolItemInfor.itemHijo.setSelection(false);
							}						
					}
					else{
						changeSelectionMode(MODE_LINK);
					}
					//NUEVO
					if(viewer.getActiveLayer()!=null)
						sShell.setText("LocalGIS3 - Capa: " + viewer.getActiveLayer());
					//FIN NUEVO
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
				
			}},Messages.getMessage("ToolBarMain.info_off"));
		
		
		
		//segundo toolitem del padre que va a ser dibujar poligonos
		factorytoolbar2= new FactoryToolBar();
		//final ToolBar hijo2=factorytoolbar2.createToolBar(sShell, viewer,Config.prResources.getProperty("ToolBar_volver2"), Config.prResources.getProperty("ToolBar_icono_paint_Volver"), true, padre, Config.prResources.getProperty("ToolBar_volverDerecha"));
		hijo2=factorytoolbar2.createToolBar(sShell, viewer,Config.prResources.getProperty("ToolBarMain.desplegarMenu_on"), Config.prResources.getProperty("ToolBarMain.volver_on"), true, padre, Config.prResources.getProperty("ToolBarMain.desplegarMenu_off"));
		
//		toolitem imagen para subir al padre
		ToolItem toolItemVolverarribaDibujaPoligonos = factorytoolbar2.getToolItemVolverarriba();
		
		toolItemVolverarribaDibujaPoligonos.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event event) {
				viewer.removeSVGViewerLinkListener(listBor);
			}
		});
		
		toolItemBuscar= new ToolItemSatec(padre, hijo2, null);
		toolItemBuscar.makeItem(sShell, SWT.PUSH, Config.prResources.getProperty("ToolBarMain.editar_on"), new Listener(){

			public void handleEvent(Event event) {
				viewer.setModeLink();
				desActivarBoton();
				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					//NUEVO
					if(viewer.getActiveLayerNode()==null){
						//Selecciona una por defecto
						String [] allIDLayers = viewer.getAllIDLayers();
						for(int i=0;i<allIDLayers.length;i++)
							if(!allIDLayers[i].equals(Constants.GRATICULE_LAYER_NAME))
								viewer.setActiveLayerNode(allIDLayers[i]);

					}
					//FIN NUEVO
					if(viewer.getActiveLayerNode()==null){
						mensajeError(Messages.getMessage("ToolBar_ErrorCapa"));
						LayerOrderVisibilityScreen pantInfor= new LayerOrderVisibilityScreen(sShell, viewer.getAllIDLayers(),viewer);
						pantInfor.getSelectedLayer();						
					}

					if (viewer.getActiveLayerNode()!=null) {
						if(viewer.getActiveLayerNode().isEditable()&& isUuarioPermisos() && validationBean((SVGGroupElem) viewer.getActiveLayerNode())){
							Point localmenu=padre.getParent().getLocation();
							padre.getParent().setLocation(-1000, -1000);
							//hay que poner el numero de hijo porque no se conoce dinamicamente cual es su hijo
							hijo2.getParent().setLocation(localmenu);
							//hijo2.setVisible(true);
							
							// Permitir crear elementos solo de la geometria permitida en la capa
							try {
								String gt = ((SVGGroupElem) viewer.getActiveLayerNode()).getGeometryType();
								int gti = Integer.parseInt(gt);
								switch (gti) {
								case 1: /* Point */
									toolItemPunto.itemHijo.setEnabled(true);
									toolItemLinea.itemHijo.setEnabled(false);
									toolItemPoligono.itemHijo.setEnabled(false);
									break;
								case 3: /* LineString */
								case 9: /* MultiLineString */
									toolItemPunto.itemHijo.setEnabled(false);
									toolItemLinea.itemHijo.setEnabled(true);
									toolItemPoligono.itemHijo.setEnabled(false);
									break;
								case 5: /* Polygon */
								case 11: /* MultiPolygon */
									toolItemPunto.itemHijo.setEnabled(false);
									toolItemLinea.itemHijo.setEnabled(false);
									toolItemPoligono.itemHijo.setEnabled(true);
									break;
								case 0: /* geometry - permitir todo */ 
								default:
									toolItemPunto.itemHijo.setEnabled(true);
									toolItemLinea.itemHijo.setEnabled(true);
									toolItemPoligono.itemHijo.setEnabled(true);
									break;
								}
							} catch (Exception e) {
								logger.error(e, e);
								toolItemPunto.itemHijo.setEnabled(true);
								toolItemLinea.itemHijo.setEnabled(true);
								toolItemPoligono.itemHijo.setEnabled(true);
							}
						}
						else{
							mensajeError(Messages.getMessage("ToolBar_ErrorDibujado"));
						}
					}
					//NUEVO
					if(viewer.getActiveLayer()!=null)
						sShell.setText("LocalGIS3 - Capa: " + viewer.getActiveLayer());
					//FIN NUEVO					
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}
			private boolean validationBean(SVGGroupElem activeLayerNode) {
				
				boolean validation = false;
				ValidationBean validationBean = SessionInfo.getInstance().getValidationBean();
				
				validationBean.getPermisos();
				int [] validationBeanArray = validationBean.getPermisosBySystemId(activeLayerNode.getSystemId()); 

				for(int i = 0;i < validationBeanArray.length && !validation; i++){
					//if(validationBeanArray[i] == ValidationBean.PERM_LAYER_ADD){ //873 "geopista.layer.lectura" a fuego
						validation = true;
					//}
				}
				
				return validation;
			}
			private void desActivarBoton() {

				toolItemPunto.itemHijo.setSelection(false);
				toolItemLinea.itemHijo.setSelection(false);
				toolItemPoligono.itemHijo.setSelection(false);
				toolItemSeleccionBorrar.itemHijo.setSelection(false);
				//toolItemInforBorrar.itemHijo.setSelection(false);
			}
			/*private boolean capaEspecial() {

				boolean isCapaEspecial=false;
				String vew=viewer.getActiveLayer();
				//System.out.println(viewer.getActiveLayer());
				for(int i=0;i<ConfigLocalgis.capasEspeciales.length;i++){
					//System.out.println(ConfigLocalgis.capasEspeciales[i]);
					if(ConfigLocalgis.capasEspeciales[i].equals(viewer.getActiveLayer())){
						isCapaEspecial=true;
					}
				}
				return isCapaEspecial;
			}*/

			//metodo que se encarga de ver los permisos de los usuarios
			private boolean isUuarioPermisos() {

				boolean permisos=false;
				SessionInfo session = SessionInfo.getInstance();
				if(session.getProjectInfo().getPermisosCeldas().permisoUsuCelda(session.getCellInfo().getSelectedCell(), SessionInfo.getInstance().getValidationBean().getIdUsuario())){
					permisos=true;
				}
				return permisos;
			}
			
		},Messages.getMessage("ToolBarMain.editar_on"));
		
//		Añado imagenes a los hijos
		toolItemPunto= new ToolItemSatec(hijo2, null, padre);
		
		toolItemPunto.makeItem(sShell,SWT.CHECK, Config.prResources.getProperty("ToolBarMain.punto_off"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					if(viewer.getActiveLayerNode()!=null){
						if(viewer.getActiveLayerNode().isEditable()){
							changeSelectionMode(MODE_POINT);
						}
					}
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.punto_off"));
		
		toolItemLinea= new ToolItemSatec(hijo2, null, padre);
		
		toolItemLinea.makeItem(sShell,SWT.CHECK, Config.prResources.getProperty("ToolBarMain.linea_off"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					if(viewer.getActiveLayerNode()!=null){
						if(viewer.getActiveLayerNode().isEditable()){
							changeSelectionMode(MODE_POLYLINE);
						}
					}
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.linea_off"));
		
		toolItemPoligono= new ToolItemSatec(hijo2, null, padre);
		
		toolItemPoligono.makeItem(sShell,SWT.CHECK, Config.prResources.getProperty("ToolBarMain.poligono_off"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					if(viewer.getActiveLayerNode()!=null){
						if(viewer.getActiveLayerNode().isEditable()){
							changeSelectionMode(MODE_POLYGON);
						}
					}
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.poligono_off"));
		
				
		toolItemBorrar= new ToolItemSatec(hijo2, null, padre);
		toolItemBorrar.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.borrar"),listBor,Messages.getMessage("ToolBarMain.borrar"));
		toolItemBorrar.itemHijo.setEnabled(false);

		//Seleccion para borrar un nodo
		
		toolItemSeleccionBorrar= new ToolItemSatec(hijo2, null, padre);
		
		toolItemSeleccionBorrar.makeItem(sShell,SWT.CHECK, Config.prResources.getProperty("ToolBarMain.seleccion"),new Listener(){

			public void handleEvent(Event event) {

				if(toolItemSeleccionBorrar.itemHijo.getSelection()){
					toolItemBorrar.itemHijo.setEnabled(true);
				}
				else{
					toolItemBorrar.itemHijo.setEnabled(false);
				}
				viewer.setModeLink();
				changeSelectionMode(MODE_SELECT);
				viewer.addSVGViewerLinkListener(listBor);				
			}},Messages.getMessage("ToolBarMain.seleccion"));
		
		/*Informacion de un nodo*/
		/*toolItemInforBorrar= new ToolItemSatec(hijo2, null, padre);
		
		toolItemInforBorrar.makeItem(sShell,SWT.CHECK, Config.prResources.getProperty("ToolBarMain.info_off"),new Listener(){

			public void handleEvent(Event event) {

				viewer.removeSVGViewerLinkListener(listBor);
				viewer.addSVGViewerLinkListener(mapSvgLinkListener);
				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					if(viewer.getActiveLayerNode()==null){
						toolItemInfor.itemHijo.setSelection(false);
						LayerOrderVisibilityScreen pantInfor= new LayerOrderVisibilityScreen(sShell, viewer.getAllIDLayers(),viewer);
						mensajeError( Messages.getMessage("ToolBar_ErrorCapa"));
					}
					else{
						changeSelectionMode(MODE_LINK);
					}
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
				
			}});*/
		
		//3 hijo es el de las capas
		factorytoolbar3= new FactoryToolBar();
				
		hijo3=factorytoolbar3.createToolBar(sShell, viewer,Config.prResources.getProperty("ToolBarMain.desplegarMenu_on"), Config.prResources.getProperty("ToolBarMain.volver_on"), true, padre, Config.prResources.getProperty("ToolBarMain.desplegarMenu_off"));
		
		toolItemCapa= new ToolItemSatec(padre, hijo3, null);
		toolItemCapa.makeItem(sShell, SWT.PUSH, Config.prResources.getProperty("ToolBarMain.capas"), new Listener(){

			public void handleEvent(Event event) {
				viewer.setModeLink();
				Point localmenu=padre.getParent().getLocation();
				padre.getParent().setLocation(-1000, -1000);
				//hay que poner el numero de hijo porque no se conoce dinamicamente cual es su hijo
				hijo3.getParent().setLocation(localmenu);
				//hijo2.setVisible(true);
			}
		},Messages.getMessage("ToolBarMain.capas"));
		
//		Añado imagenes a los hijos
		toolItemVerCapa= new ToolItemSatec(hijo3, null, padre);
		
		toolItemVerCapa.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.capas"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					LayerOrderVisibilityScreen pantInfor= new LayerOrderVisibilityScreen(sShell, viewer.getAllIDLayers(),viewer);
					pantInfor.getSelectedLayer();
					//NUEVO
					if(viewer.getActiveLayer()!=null)
						sShell.setText("LocalGIS3 - Capa: " + viewer.getActiveLayer());
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.capas"));
		
		toolItemWMS= new ToolItemSatec(hijo3, null, padre);
		
		toolItemWMS.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.capas_wms"),new Listener(){

			public void handleEvent(Event event) {
				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					new WMSLayersScreen(sShell, viewer);
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.capas_wms"));
		
		
		
		toolItemSHP = new ToolItemSatec(hijo3, null, padre);
		
		toolItemSHP.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.capas_shp"),new Listener(){

			public void handleEvent(Event event) {
				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					new SHPLayersScreen(sShell, viewer);
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.capas_shp"));
		
		toolItemECW = new ToolItemSatec(hijo3, null, padre);
		
		toolItemECW.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.capas_ecw"),new Listener(){

			public void handleEvent(Event event) {
				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					new ECWLayersScreen(sShell, viewer);
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.capas_ecw"));
		
		toolItemEstilos= new ToolItemSatec(hijo3, null, padre);
		
		toolItemEstilos.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.capadisenio_off"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					StyleScreen estilo = new StyleScreen(sShell, viewer);
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.capadisenio_off"));
		
		//cuarto hijo es buscar
		
		factorytoolbar4= new FactoryToolBar();
				
		hijo4=factorytoolbar4.createToolBar(sShell, viewer,Config.prResources.getProperty("ToolBarMain.desplegarMenu_on"), Config.prResources.getProperty("ToolBarMain.volver_on"), true, padre, Config.prResources.getProperty("ToolBarMain.desplegarMenu_off"));
		toolItemBusquedas= new ToolItemSatec(padre, hijo4, null);
		toolItemBusquedas.makeItem(sShell, SWT.PUSH, Config.prResources.getProperty("ToolBarMain.buscar_on"), new Listener(){

			public void handleEvent(Event event) {
				viewer.setModeLink();
				Point localmenu=padre.getParent().getLocation();
				padre.getParent().setLocation(-1000, -1000);
				//hay que poner el numero de hijo porque no se conoce dinamicamente cual es su hijo
				hijo4.getParent().setLocation(localmenu);
				//hijo2.setVisible(true);
			}
		},Messages.getMessage("ToolBarMain.buscar_on"));
		
//		Añado imagenes a los hijos
		toolItemVerCapa2= new ToolItemSatec(hijo4, null, padre);
		
		toolItemVerCapa2.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.configbusqueda"),new Listener(){

			public void handleEvent(Event event) {

				SVGDocument doc=viewer.getSVGDocument();
				if(doc.root.children.count!=0){
					SearchScreen busqueda = new SearchScreen(sShell, viewer);
				}
				else{
					mensajeError(Messages.getMessage("ToolBar_ErrorDoc"));
				}
			}},Messages.getMessage("ToolBarMain.configbusqueda"));
		
		toolItemOcultarBusqueda = new ToolItemSatec(hijo4, null, padre);
		
		toolItemOcultarBusqueda.makeItem(sShell,SWT.PUSH, Config.prResources.getProperty("ToolBarMain.ocultarbusqueda"),new Listener(){

			public void handleEvent(Event event) {
				// Desactivar la busqueda actual
				Vector defSearchs = SessionInfo.getInstance().getCurrentDefinedSearchs();
				if (defSearchs != null && !defSearchs.isEmpty()) {
					Enumeration en = defSearchs.elements();
					while (en.hasMoreElements()) {
						SearchDataBean search = (SearchDataBean) en.nextElement();
						search.setEjecutado(false);
					}
				}
				// Ocultar los resultados del mapa
				viewer.setSearchSLDActive(false);
				viewer.drawSVG();
			}},Messages.getMessage("ToolBarMain.ocultarbusqueda"));
		
	}

	private void changeSelectionMode(int newMode) {
		switch (mode) {
			case MODE_ZOOMIN:
				toolZoomIn.itemHijo.setSelection(false);
				if(cont>1){
					if(newMode==mode){
						toolZoomIn.itemHijo.setSelection(true);
						cont=0;
					}
					
				}
				break;
			case MODE_PAN:
				toolItemMano.itemHijo.setSelection(false);
				if(contPan>1){
					if(newMode==mode){
						toolItemMano.itemHijo.setSelection(true);
						contPan=0;
					}
				}
				break;
			case MODE_LINK:
				toolItemInfor.itemHijo.setSelection(false);
				if(contLink>1){
					if(newMode==mode){
						toolItemInfor.itemHijo.setSelection(true);
						contLink=0;
					}
				}
				break;
			case MODE_POINT:
				if(conSele!=0){
					toolItemBorrar.itemHijo.setEnabled(false);
				}
				
				//toolItemInforBorrar.itemHijo.setSelection(false);
				toolItemPunto.itemHijo.setSelection(false);
				if(contPoint>1){
					if(newMode==mode){
						toolItemPunto.itemHijo.setSelection(true);
						contPoint=0;
					}
				}
				break;
			case MODE_POLYLINE:
				if (newMode == MODE_POLYLINE) {
					SVGNode nodoSel=viewer.endDraw();
					viewer.setModeLink();
					if (nodoSel != null) {
						CreateElementInfoScreen pantI= new CreateElementInfoScreen(sShell, nodoSel, viewer);
					}
					newMode = MODE_NONE;
				}
				else {
					if(conSele!=0){
						toolItemBorrar.itemHijo.setEnabled(false);
					}
					toolItemLinea.itemHijo.setSelection(false);
					viewer.cancelDraw();
					if(contPolyline>1){
						if(newMode==mode){
							toolItemLinea.itemHijo.setSelection(true);
							contPolyline=0;
						}
					}
				}
				break;
			case MODE_POLYGON:
				if (newMode == MODE_POLYGON) {
					SVGNode nodoSel=viewer.endDraw();
					viewer.setModeLink();
					
					if (nodoSel != null) {
							CreateElementInfoScreen pantIn= new CreateElementInfoScreen(sShell, nodoSel, viewer);
					}
					 
					newMode = MODE_NONE;
				}
				else {
					if(conSele!=0){
						toolItemBorrar.itemHijo.setEnabled(false);
					}
					toolItemPoligono.itemHijo.setSelection(false);
					viewer.cancelDraw();
					if(contPolygon>1){
						if(newMode==mode){
							toolItemPoligono.itemHijo.setSelection(true);
							contPolygon=0;
						}
					}
				}
				break;
			case MODE_SELECT:
				toolItemBorrar.itemHijo.setEnabled(false);
				toolItemSeleccionBorrar.itemHijo.setSelection(false);
				if(conSele>1){
					if(newMode==mode){
						toolItemSeleccionBorrar.itemHijo.setSelection(true);
						conSele=0;
					}
				}
				break;
				
		}
		switch (newMode) {
		case MODE_LINK:
			viewer.setModeLink();
			contLink+=1;
			cont=0;
			contPan=0;
			break;
		case MODE_ZOOMIN:
			viewer.setModeZoomIn();
			cont+=1;
			contPan=0;
			contLink=0;
			break;
		case MODE_PAN:
			viewer.setModePan();
			contPan+=1;
			cont=0;
			contLink=0;
			break;
		case MODE_POINT:
			//toolItemInforBorrar.itemHijo.setSelection(false);
			viewer.setModeDrawPoint(viewer.getActiveLayer());
			contPoint+=1;
			contPolyline=0;
			contPolygon=0;
			conSele=0;
			break;
		case MODE_POLYLINE:
			//toolItemInforBorrar.itemHijo.setSelection(false);
			if (toolItemLinea.itemHijo.getSelection()) viewer.setModeDrawLine(viewer.getActiveLayer());
			contPolyline+=1;
			contPoint=0;
			contPolygon=0;
			conSele=0;
			break;
		case MODE_POLYGON:
			//toolItemInforBorrar.itemHijo.setSelection(false);
			if (toolItemPoligono.itemHijo.getSelection()) viewer.setModeDrawPolygon(viewer.getActiveLayer());
			contPolygon+=1;
			contPolyline=0;
			contPoint=0;
			conSele=0;
			break;
		case MODE_SELECT:
			//toolItemInforBorrar.itemHijo.setSelection(false);
			conSele+=1;
			contPolygon=0;
			contPolyline=0;
			contPoint=0;
			break;
		}
		mode = newMode;
	}

	protected void mensajeError(String mensaje){
		MessageBox mb = new MessageBox(sShell, SWT.ICON_ERROR | SWT.OK);
		mb.setMessage(mensaje);
		mb.open();
	}

	
	//Elimamos los elementos que componen la toolbar para que no tengamos
	//problemas de memoria ni de handles.
	public void dispose() {
				
		if (toolZoomIn!=null) {
			toolZoomIn.dispose();
		}	
		if (hijo1!=null && !hijo1.isDisposed()) {
			hijo1.dispose();
		}
		
		if (hijo2!=null && !hijo2.isDisposed()) {
			hijo2.dispose();
		}	
		if (hijo3!=null && !hijo3.isDisposed()) {
			hijo3.dispose();
		}	
		if (hijo4!=null && !hijo4.isDisposed()) {
			hijo4.dispose();
		}
		if (padre!=null && !padre.isDisposed()) {
			padre.dispose();
		}	
		if (toolItemMapa!=null) {
			toolItemMapa.dispose();
		}		
		if (toolZoomIn!=null) {
			toolZoomIn.dispose();
		}	
		if (toolItemZoomOut!=null) {
			toolItemZoomOut.dispose();
		}	
		if (toolItemMano!=null) {
			toolItemMano.dispose();
		}	
		if (toolItemInfor!=null) {
			toolItemInfor.dispose();
		}
	
		if (toolItemBuscar!=null) {
			toolItemBuscar.dispose();
		}
		if (toolItemPunto!=null) {
			toolItemPunto.dispose();
		}
		if (toolItemLinea!=null) {
			toolItemLinea.dispose();
		}
		if (toolItemPoligono!=null) {
			toolItemPoligono.dispose();
		}
		if (toolItemBorrar!=null) {
			toolItemBorrar.dispose();
		}
		if (toolItemSeleccionBorrar!=null) {
			toolItemSeleccionBorrar.dispose();
		}
		if (toolItemCapa!=null) {
			toolItemCapa.dispose();
		}
		if (toolItemVerCapa!=null) {
			toolItemVerCapa.dispose();
		}
		if (toolItemWMS!=null) {
			toolItemWMS.dispose();
		}
		if (toolItemSHP!=null) {
			toolItemSHP.dispose();
		}
		if (toolItemECW!=null) {
			toolItemECW.dispose();
		}
		if (toolItemEstilos!=null) {
			toolItemEstilos.dispose();
		}
		if (toolItemBusquedas!=null) {
			toolItemBusquedas.dispose();
		}
		if (toolItemVerCapa2!=null) {
			toolItemVerCapa2.dispose();
		}	
		if (toolItemOcultarBusqueda!=null) {
			toolItemOcultarBusqueda.dispose();
		}
		
		if (factorytoolbar1!=null)
			factorytoolbar1.dispose();		
		
		if (factorytoolbar!=null)
			factorytoolbar.dispose();

		if (factorytoolbar2!=null)
			factorytoolbar2.dispose();

		if (factorytoolbar3!=null)
			factorytoolbar3.dispose();
		
		if (factorytoolbar4!=null)
			factorytoolbar4.dispose();
		
	}
}
