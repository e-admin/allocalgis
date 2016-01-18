package es.satec.localgismobile.ui.screens;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.model.filterencoding.Filter;
import org.deegree.model.filterencoding.FilterConstructionException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.session.SessionInfo;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.localgismobile.ui.utils.SearchDataBean;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;
import es.satec.svgviewer.localgis.sld.AbstractStyle;
import es.satec.svgviewer.localgis.sld.FeatureTypeStyle;
import es.satec.svgviewer.localgis.sld.NamedLayer;
import es.satec.svgviewer.localgis.sld.Rule;
import es.satec.svgviewer.localgis.sld.StyledLayerDescriptor;
import es.satec.svgviewer.localgis.sld.UserStyle;
import es.satec.svgviewer.localgis.util.SearchSLDGenerator;

import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class SearchScreen extends LocalGISWindow {

	private SVGLocalGISViewer viewer=null;
	private ToolBar toolBarMenu = null;
	private Table tableBusquedas = null;
	private Composite composite = null;
	private Button buttonOk = null;
	//Se van almacenar las busquedas realizadas
	private Vector inforBusq= null;  //  @jve:decl-index=0:
	TableColumn column0=null;
	TableColumn column1=null;
	TableColumn column2=null;
	TableColumn column3=null;
	TableColumn column4=null;
	TableColumn column5=null;
	TableColumn column6=null;
	TableColumn column7=null;
	Vector checkedItem= new Vector();  //  @jve:decl-index=0:
	TableItem selectItem= null;  //  @jve:decl-index=0:
	TableItem checkItem= null;
	private Image imageOjo;
	private Image imagCrear;
	private Image imagBorrar;
	private Image imagModificar;
	private Image imagEjecutar;

	private static Logger logger = Global.getLoggerFor(SearchScreen.class);
	
	
	public SearchScreen(Shell parent,SVGLocalGISViewer viewer ){
		super(parent);
		this.inforBusq= SessionInfo.getInstance().getCurrentDefinedSearchs();
		shell.setBackground(Config.COLOR_APLICACION);
		this.viewer=viewer;
		loadImages();
		init();
		show();	
	}
	
	private void loadImages() {
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(Config.prResources.getProperty("SearchScreen_ojo"));
			imageOjo = new Image(Display.getCurrent(), is);
			is.close();
			
			is = this.getClass().getClassLoader().getResourceAsStream(Config.prResources.getProperty("SearchScreen_crear"));
			imagCrear = new Image(Display.getCurrent(), is);
			is.close();
			
			is = this.getClass().getClassLoader().getResourceAsStream(Config.prResources.getProperty("SearchScreen_eliminar"));
			imagBorrar = new Image(Display.getCurrent(), is);
			is.close();

			is = this.getClass().getClassLoader().getResourceAsStream(Config.prResources.getProperty("SearchScreen_modificar"));
			imagModificar = new Image(Display.getCurrent(), is);
			is.close();
			
			is = this.getClass().getClassLoader().getResourceAsStream(Config.prResources.getProperty("SearchScreen_ejecutar"));
			imagEjecutar = new Image(Display.getCurrent(), is);
			is.close();
		} catch (Exception e) {
			logger.error("Error al cargar imagenes", e);
		} finally {
			if (is != null) {
				try { is.close(); } catch (IOException e) {}
			}
		}
		
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent ev) {
				if (imageOjo != null) imageOjo.dispose();
				if (imagCrear != null) imagCrear.dispose();;
				if (imagBorrar != null) imagBorrar.dispose();;
				if (imagModificar != null) imagModificar.dispose();;
				if (imagEjecutar != null) imagEjecutar.dispose();;
			}
		});
	}
	
	/**
	 * This method initializes sShell
	 */
	private void init() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 10;
		gridLayout.marginWidth = 10;
		gridLayout.marginHeight = 10;
		gridLayout.horizontalSpacing = 10;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = GridData.FILL;
		shell.setText(Messages.getMessage("SearchScreen_Titulo"));
		createToolBarMenu();
		shell.setLayout(gridLayout);
		//shell.setSize(Display.getDefault().getClientArea().width,Display.getDefault().getClientArea().height);
		tableBusquedas = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tableBusquedas.setHeaderVisible(true);
		tableBusquedas.setLayoutData(gridData1);
		cargaDatos();
		createComposite();
	}

	private void cargaDatos() {

		column0 = new TableColumn(tableBusquedas, SWT.NONE);
		column0.setText(Messages.getMessage("SearchScreen_Ejecucion"));
		column1 = new TableColumn(tableBusquedas, SWT.NONE);
		column1.setText(Messages.getMessage("SearchScreen_Capa"));
		column2 = new TableColumn(tableBusquedas, SWT.NONE);
		column2.setText(Messages.getMessage("SearchScreen_Atributo"));
		column3 = new TableColumn(tableBusquedas, SWT.NONE);
		column3.setText(Messages.getMessage("SearchScreen_Operador"));
		column4 = new TableColumn(tableBusquedas, SWT.NONE);
		column4.setText(Messages.getMessage("SearchScreen_Valor"));
		column5 = new TableColumn(tableBusquedas, SWT.NONE);
		column5.setText(Messages.getMessage("SearchScreen_Border"));
		column6 = new TableColumn(tableBusquedas, SWT.NONE);
		column6.setText(Messages.getMessage("SearchScreen_Fill"));
		column7 = new TableColumn(tableBusquedas, SWT.NONE);
		column7.setText(Messages.getMessage("SearchScreen_Stroke"));

		for(int i=0;i<this.inforBusq.size();i++){
			SearchDataBean datos = (SearchDataBean)this.inforBusq.elementAt(i);
			TableItem item = new TableItem(tableBusquedas, SWT.CENTER);
			String[] text = new String[]{"",datos.getCapa(), datos.getAtributo(), datos.getOperador(), datos.getValor()};
			item.setText(text);
			item.setBackground(6, datos.getFill());
			item.setBackground(7, datos.getStroke());
			if(datos.getEjecutado()){
				item.setImage(0, imageOjo);
			}
			else{
				item.setImage(0, null);
			}
		}

		column0.pack();
		column1.pack();
		column2.pack();
		column3.pack();
		column4.pack();
		column5.pack();
		column6.pack();
		column7.pack();
	}

	/**
	 * This method initializes toolBarMenu	
	 *
	 */
	private void createToolBarMenu() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.verticalAlignment = GridData.CENTER;
		toolBarMenu = new ToolBar(shell, SWT.NONE);
		toolBarMenu.setBackground(Config.COLOR_APLICACION);
		toolBarMenu.setLayoutData(gridData);
		
		ToolItem toolItemCrear = new ToolItem(toolBarMenu, SWT.PUSH);
		toolItemCrear.setImage(imagCrear);
		
		toolItemCrear.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				CreateNewSearchScreen busqueda = new CreateNewSearchScreen(shell,viewer,inforBusq);
				
				busqueda.getSShell().addDisposeListener(new DisposeListener (){
					public void widgetDisposed(DisposeEvent arg0) {
						if (inforBusq.size() > tableBusquedas.getItemCount()) {
							SearchDataBean datos = (SearchDataBean)inforBusq.elementAt(tableBusquedas.getItemCount());
							if(datos!=null){
								TableItem item = new TableItem(tableBusquedas, SWT.CENTER);
								item.setText(new String[]{"",datos.getCapa(), datos.getAtributo(), datos.getOperador(), datos.getValor(), datos.getstrokeWidth()});
								item.setBackground(6, datos.getFill());
								item.setBackground(7, datos.getStroke());
								if(datos.getEjecutado()){
									item.setImage(0, imageOjo);
								}
								else{
									item.setImage(0, null);
								}
							}
						}
					}
					
				});
				
			}});

		ToolItem toolItemBorrar = new ToolItem(toolBarMenu, SWT.PUSH);
		toolItemBorrar.setImage(imagBorrar);
		
		//toolItemBorrar.setText(Messages.getMessage("SearchScreen_Borrar"));
		toolItemBorrar.addListener(SWT.Selection, new Listener(){

			public void handleEvent(Event event) {
				if(tableBusquedas.getSelectionIndex()!=-1){
					
					SearchDataBean datos = (SearchDataBean)inforBusq.elementAt(tableBusquedas.getSelectionIndex());
                    if (datos.getEjecutado()) {
                    	viewer.setSearchSLDActive(false);
                    	viewer.drawSVG();
                    }
                    inforBusq.removeElementAt(tableBusquedas.getSelectionIndex());
					tableBusquedas.remove(tableBusquedas.getSelectionIndex());
				}
			}
		});
				
		ToolItem toolItemModificar = new ToolItem(toolBarMenu, SWT.PUSH);
		toolItemModificar.setImage(imagModificar);
		
		//toolItemModificar.setText(Messages.getMessage("SearchScreen_Modificar"));
		toolItemModificar.addListener(SWT.Selection, new Listener(){

			public void handleEvent(Event event) {
				if(tableBusquedas.getSelectionIndex()!=-1){

				CreateNewSearchScreen busqueda = new CreateNewSearchScreen(shell,inforBusq,tableBusquedas.getSelectionIndex(), viewer);
				
					busqueda.getSShell().addDisposeListener(new DisposeListener (){
						public void widgetDisposed(DisposeEvent arg0) {
							if(tableBusquedas.getSelectionIndex()!=-1) {
								SearchDataBean datos = (SearchDataBean)inforBusq.elementAt(tableBusquedas.getSelectionIndex());
								TableItem item = new TableItem(tableBusquedas, SWT.CENTER, tableBusquedas.getSelectionIndex());
								tableBusquedas.remove(tableBusquedas.getSelectionIndex());
								item.setText(new String[]{"",datos.getCapa(), datos.getAtributo(), datos.getOperador(), datos.getValor(), datos.getstrokeWidth()});
								item.setBackground(6, datos.getFill());
								item.setBackground(7, datos.getStroke());
								if(datos.getEjecutado()){
									item.setImage(0,imageOjo);
									
									/* Crear el nuevo estilo de busqueda y redibujar */
									String filterOperator = transfor(datos.getOperador());
									String filterLiteral="";
									String filterLiteral2="";
									if(datos.getOperador().equals("between")){
										int val=datos.getValor().indexOf(" - ");
										String ini=datos.getValor().substring(0, val);
										String fin=datos.getValor().substring(val+3, datos.getValor().length());
										filterLiteral=ini;
										filterLiteral2=fin;
									}
									else{
										filterLiteral=datos.getValor();
									}
									String xmlEstiloBusq = SearchSLDGenerator.generateSearchSLD("Busqueda",filterOperator, datos.getAtributo(),
											filterLiteral,filterLiteral2,datos.getStroke(),
											Integer.parseInt(datos.getstrokeWidth()),datos.getFill());
									
									ByteArrayInputStream fileIn = new ByteArrayInputStream(xmlEstiloBusq.getBytes());
									try {
										viewer.loadSLD(fileIn,  datos.getCapa(), "Busqueda", false, true);
									} catch (XMLParsingException e) {
										logger.error(e, e);
									} finally {
										if (fileIn != null) {
											try { fileIn.close(); } catch (IOException e) {}
										}
									}
									viewer.setSearchSLDActive(true);
									viewer.drawSVG();
								}
								else{
									item.setImage(0, null);
								}
							}
						}
					});
				}
				
			}});
		ToolItem toolItemEjecutar = new ToolItem(toolBarMenu, SWT.PUSH);
		toolItemEjecutar.setImage(imagEjecutar);
		
		//toolItemEjecutar.setText(Messages.getMessage("SearchScreen_Ejecutar"));
		toolItemEjecutar.addListener(SWT.Selection, new Listener(){

			public void handleEvent(Event event) {
				/*Inicio del reloj*/
				ScreenUtils.startHourGlass(shell);
				
				if(tableBusquedas.getSelectionIndex()!=-1){
					SearchSLDGenerator sSLDgen= new SearchSLDGenerator();
					SearchDataBean datosSeleccion = (SearchDataBean)inforBusq.elementAt(tableBusquedas.getSelectionIndex());
					String filterOperator = transfor(datosSeleccion.getOperador());
					String filterLiteral="";
					String filterLiteral2="";
					if(datosSeleccion.getOperador().equals("between")){
						int val=datosSeleccion.getValor().indexOf(" - ");
						String ini=datosSeleccion.getValor().substring(0, val);
						String fin=datosSeleccion.getValor().substring(val+3, datosSeleccion.getValor().length());
						filterLiteral=ini;
						filterLiteral2=fin;
					}
					else{
						filterLiteral=datosSeleccion.getValor();
					}
	
					String xmlEstiloBusq = sSLDgen.generateSearchSLD("Busqueda",filterOperator, datosSeleccion.getAtributo(),
							filterLiteral,filterLiteral2,datosSeleccion.getStroke(),
							Integer.parseInt(datosSeleccion.getstrokeWidth()),datosSeleccion.getFill());
					
					ByteArrayInputStream fileIn = new ByteArrayInputStream(xmlEstiloBusq.getBytes());
					StyledLayerDescriptor sLayDesc=null;
					
					try {
						sLayDesc=viewer.loadSLD(fileIn,  datosSeleccion.getCapa(), "Busqueda", false, true);
					} catch (XMLParsingException e) {
						logger.error(e.toString());
					} finally {
						if (fileIn != null) {
							try { fileIn.close(); } catch (IOException e) {}
						}
					}
					
					
					if(sLayDesc!=null){
						NamedLayer [] aL=sLayDesc.getNamedLayers();
						AbstractStyle [] absStyl= aL[0].getStyles();
						UserStyle userStyle= (UserStyle)absStyl[0];
						FeatureTypeStyle [] feaTypStyle=userStyle.getFeatureTypeStyles();
						Rule [] rule = feaTypStyle[0].getRules();
						Filter filter=rule[0].getFilter();
						Vector nodo = new Vector();
						ScreenUtils.startHourGlass(shell);
						try {
							nodo=viewer.applyFilter(filter, datosSeleccion.getCapa());
						} catch (FilterConstructionException e) {
							logger.error(e.toString());
						}
						catch (Exception e) {
								logger.error(e.toString());			
						}
						finally{
							ScreenUtils.stopHourGlass(shell);
						}
						final SearchResultsScreen pBET = new SearchResultsScreen(shell,nodo, datosSeleccion.getCapa());
						
						 pBET.getButtonVisualizar().addListener(SWT.Selection, new Listener(){
							public void handleEvent(Event event) {
								viewer.setSearchSLDActive(true);
								viewer.drawSVG();
								activaFilaVisibleEnMapa();
								// Cerrar las dos ventanas para volver a mostrar el mapa
								pBET.getShell().close();
								shell.close();
							}
						});	 
					}
					else{
						mensajeError(Messages.getMessage("SearchScreen_ErrorBusq"));
					}
				}
				ScreenUtils.stopHourGlass(shell);
			}
			
		});
	}
	
	public void activaFilaVisibleEnMapa() {
		TableItem itemCheck=tableBusquedas.getItem(tableBusquedas.getSelectionIndex());
		TableItem [] checkItemTba=tableBusquedas.getItems();
		for(int i=0; i<tableBusquedas.getItemCount();i++){
			SearchDataBean datos = (SearchDataBean)inforBusq.elementAt(i);
			TableItem checkItem2=checkItemTba[i];
			if(!itemCheck.equals(checkItem2)){
				checkItem2.setImage(0, null);
				datos.setEjecutado(false);
			}
			else{
				datos.setEjecutado(true);
				checkItem2.setImage(0, imageOjo);
			}
		}
	}
	
	private String transfor(String operador) {

		String res= "";
		if(operador.equals("=")){
			res="PropertyIsEqualTo";
		}
		if(operador.equals("!=")){
			res="PropertyIsNotEqualTo";
		}
		if(operador.equals("<")){
			res="PropertyIsLessThan";
		}
		if(operador.equals(">")){
			res="PropertyIsGreaterThan";
		}
		if(operador.equals("<=")){
			res="PropertyIsLessThanOrEqualTo";
		}
		if(operador.equals(">=")){
			res="PropertyIsGreaterThanOrEqualTo";
		}
		if(operador.equals("between")){
			res="PropertyIsBetween";
		}
		if(operador.equals("like")){
			res="PropertyIsLike";
		}
		return res;
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.CENTER;
		gridData2.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		composite = new Composite(shell, SWT.NONE);
		composite.setBackground(Config.COLOR_APLICACION);
		composite.setLayout(gridLayout1);
		composite.setLayoutData(gridData2);
		buttonOk = new Button(composite, SWT.NONE);
		buttonOk.setText(Messages.getMessage("SearchScreen_Ok"));
		buttonOk.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				shell.dispose();
			}});		
	}

}
