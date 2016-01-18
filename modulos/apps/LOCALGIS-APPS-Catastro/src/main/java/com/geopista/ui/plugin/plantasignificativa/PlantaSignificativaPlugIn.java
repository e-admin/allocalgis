/**
 * 
 */
package com.geopista.ui.plugin.plantasignificativa;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.deegree.graphics.sld.Font;
import org.deegree.graphics.sld.LabelPlacement;
import org.deegree.graphics.sld.PointPlacement;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree.graphics.sld.TextSymbolizer;
import org.deegree_impl.graphics.sld.FeatureTypeStyle_Impl;
import org.deegree_impl.graphics.sld.LineSymbolizer_Impl;
import org.deegree_impl.graphics.sld.Rule_Impl;
import org.deegree_impl.graphics.sld.StyleFactory;
import org.deegree_impl.graphics.sld.StyleFactory2;
import org.deegree_impl.graphics.sld.UserStyle_Impl;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.ASCPanel;
import com.geopista.app.catastro.intercambio.edicion.dialogs.FileLoader;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.LayerFamily;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.ui.plugin.io.dxf.GeopistaLoadDxfQueryChooser;
import com.geopista.ui.plugin.io.dxf.DxfPlugIn.Dxf;
import com.geopista.ui.plugin.plantageneral.PlantaGeneralPlugIn;
import com.geopista.ui.plugin.plantasignificativa.images.IconLoader;
import com.geopista.ui.plugin.plantasignificativa.info.PlantaSignificativaFormDialog;
import com.geopista.ui.plugin.plantasignificativa.info.PlantaSignificativaInfo;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jts.util.AssertionFailedException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrameImpl;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.geopista.app.administrador.init.Constantes;
/**
 * 
 * 
 * @author javieraragon
 */
public class PlantaSignificativaPlugIn extends AbstractPlugIn{

	private boolean plantaSignificativaButtonAdded = false;

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	private PlantaSignificativaInfo plantaInfo = null;

	private int numPlantasAdded = 0;


	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public PlantaSignificativaPlugIn() {
		// TODO Auto-generated constructor stub
		Locale loc=Locale.getDefault();      	 
		ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.plantasignificativa.languages.PlantaSignificativaPlugIni18n",loc,this.getClass().getClassLoader());    	
		I18N.plugInsResourceBundle.put("PlantaSignificativaPlugIn",bundle2);	  

	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.plugin.AbstractPlugIn#initialize(com.vividsolutions.jump.workbench.plugin.PlugInContext)
	 */
	public void initialize(PlugInContext context) throws Exception {

		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar().addPlugIn(this.getIcon(),
				this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());

		//Líneas necesarias para añadir el PlugIn a la caja de Edición
		//        GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(EditingPlugIn.KEY));
		//      	geopistaEditingPlugIn.addAditionalPlugIn(this);
	}

	/**
	 * @return
	 */
	public ImageIcon getIcon() {
		return IconLoader.icon("PlantaS.png");
	}

	/**
	 * @param workbenchContext
	 * @return
	 */
	public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1) 
		);
	}


	public boolean execute(PlugInContext context) throws Exception {
		// info para la planta significativa.
		this.plantaInfo = new PlantaSignificativaInfo();
		
		// Solicita confirmacion para añadir una nueva planta significativa.
		if ( confirmarAnniadirDatosPlantaSignificativa() ){
			
			// Solicita la informacion de la nueva planta significativa.
			if( insertadoPlantaSignificativaInfo() ){

				ASCPanel.get_instance().infoplantas.put(this.plantaInfo.getNombrePlantas(), this.plantaInfo );
				
				// obtiene el número de plantas significatvias de las capas Editor.
				numPlantasAdded = getNumPlantasSignificativasFromEditor(context.getLayerManager().getCategories()) + 1;

				final PlugInContext context1 = context;
				final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
				//progressDialog.setTitle(I18N.get("Expedientes","fxcc.panel.CargandoFicheroDXF"));
				progressDialog.setTitle("TaskMonitorDialog.Wait");
				progressDialog.report(I18N.get("Expedientes","fxcc.panel.CargandoPlantillaDXF"));
				progressDialog.addComponentListener(new ComponentAdapter()
				{
					public void componentShown(ComponentEvent e)
					{   
						new Thread(new Runnable()
						{
							public void run()
							{
								try
								{   
									//InputStream resultado = new FileInputStream("classes" + File.separator + "DXFCATASTRO.txt");
									//    							InputStream resultado = FileLoader.getFile("DXFCATASTRO.txt");
									InputStream resultado = FileLoader.getFile("PS_DXF.txt");
									if(resultado!=null){
										ImportarUtils operations = new ImportarUtils();
										final String file = operations.parseISToString(resultado); 

										try {
											//    											this.dispose();
											final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
											//progressDialog.setTitle(I18N.get("Expedientes","fxcc.panel.CargandoFicheroDXF"));
											progressDialog.setTitle("TaskMonitorDialog.Wait");
											progressDialog.report(I18N.get("Expedientes","fxcc.panel.CargandoPlantillaDXF"));
											progressDialog.addComponentListener(new ComponentAdapter()
											{
												public void componentShown(ComponentEvent e)
												{   
													new Thread(new Runnable()
													{
														public void run()
														{
															try
															{   
																//InputStream resultado = new FileInputStream("classes" + File.separator + "DXFCATASTRO.txt");
																//    														InputStream resultado = FileLoader.getFile("DXFCATASTRO.txt");
																InputStream resultado = FileLoader.getFile("PS_DXF.txt");
																if(resultado!=null){
																	ImportarUtils operations = new ImportarUtils();
																	final String file = operations.parseISToString(resultado); 
																	try {
																		//ArrayList namesLayerFamilies = new ArrayList();
																		// namesLayerFamilies.add("parcelario");
																		//namesLayerFamilies.add("construcciones");
																		//namesLayerFamilies.add("cultivos");
																		//ImportarUtils.cargarCapas(namesLayerFamilies, geopistaEditor);
																		loadLayers(context1);
																		loadDXF(progressDialog,context1,file);

																	} catch (Exception e) {

																		e.printStackTrace();
																	}        
																}

															} 
															catch (Exception e)
															{
																e.printStackTrace();
															} 
															finally
															{
																progressDialog.setVisible(false);
															}
														}
													}).start();
												}
											});
											GUIUtil.centreOnWindow(progressDialog);
											progressDialog.setVisible(true);

										} catch (Exception e) {

											e.printStackTrace();
										}        
									}

								} 
								catch (Exception e)
								{
									e.printStackTrace();
								} 
								finally
								{
									progressDialog.setVisible(false);
								}
							}
						}).start();
					}
				});
				GUIUtil.centreOnWindow(progressDialog);
				progressDialog.setVisible(true);
				return true;

			}

		}
		return true;
	}

	private int getNumPlantasSignificativasFromEditor(List layerList) {
		// TODO Auto-generated method stub
		Iterator<LayerFamily> it = layerList.iterator();
		int result = 0;
		while (it.hasNext()){
			LayerFamily actualLayer = it.next();
			if (actualLayer.getName().startsWith("PS")){
				result ++;
			}
		}

		return result;
	}

	private void loadLayers(PlugInContext context){

		if (ConstantesRegistroExp.geopistaEditor !=null ){
			ArrayList baseLayers = (ArrayList) ConstantesRegistroExp.geopistaEditor.getLayerManager().getLayers();

			for (Iterator iterBaseLayers = baseLayers.iterator(); iterBaseLayers.hasNext();){
				Object object = (Object)iterBaseLayers.next();    										
				if (object instanceof GeopistaLayer){
					GeopistaLayer layer = (GeopistaLayer)object;
					if (context.getLayerManager().uniqueLayerName(layer.getName()).equals(layer.getName()) ){
						context.getLayerManager().addLayer(ConstantesRegistroExp.geopistaEditor.getLayerManager().getCategory(layer).getName(),layer);
					}
				}
			}
		}
	}

	public void loadDXF(TaskMonitor monitor, PlugInContext context, String file)
	throws Exception {

		GeopistaLoadDxfQueryChooser dxfLoad = new GeopistaLoadDxfQueryChooser(Dxf.class,
				"GEOPISTA dxf",
				extensions(Dxf.class),
				context.getWorkbenchContext());    			

		InputStream fileDXF = ImportarUtils.parseStringToIS(file);

		try
		{
			Assert.isTrue(!dxfLoad.getDataSourceQueries(fileDXF).isEmpty());
		}
		catch (AssertionFailedException e)
		{
			throw new AssertionFailedException(I18N.get("FileEmpty"));
		}

		fileDXF = ImportarUtils.parseStringToIS(file);

		boolean exceptionsEncountered = false;
		for (Iterator i = dxfLoad.getDataSourceQueries(fileDXF).iterator(); i.hasNext();) {
			DataSourceQuery dataSourceQuery = (DataSourceQuery) i.next();
			boolean layerRepeated = false;
			List allLayerList = context.getLayerManager().getLayers();

			//Ordenar Capas
			Iterator allLayerListIterator = allLayerList.iterator();
			while(allLayerListIterator.hasNext())
			{
				Layer currentLayer = (Layer) allLayerListIterator.next();
				if(currentLayer.getDataSourceQuery() == null) continue;
				if(currentLayer.getDataSourceQuery().getDataSource() == null) continue;
				Map currentLayerProperties = currentLayer.getDataSourceQuery().getDataSource().getProperties();
				String currentFileKey = (String) currentLayerProperties.get(DataSource.FILE_KEY);
				Map insertLayerProperties = dataSourceQuery.getDataSource().getProperties();
				String insertFileKey = (String) insertLayerProperties.get(DataSource.FILE_KEY);
				if(insertFileKey!=null && currentFileKey!=null && insertFileKey.trim().equals(currentFileKey.trim()))
				{
					layerRepeated=true;
					break;
				}

			}

			if(layerRepeated){
				JOptionPane.showMessageDialog(context.getActiveInternalFrame(), "layer repeated.. skip????");
			} else {

				ArrayList exceptions = new ArrayList();
				Assert.isTrue(dataSourceQuery.getDataSource().isReadable());

				Connection connection = dataSourceQuery.getDataSource()
				.getConnection();
				try {
					FeatureCollection dataset = dataSourceQuery.getDataSource().installCoordinateSystem(connection.executeQuery(dataSourceQuery.getQuery(),
							exceptions, monitor), CoordinateSystemRegistry.instance(context.getWorkbenchContext().getBlackboard()));

					String category = chooseCategory(dataSourceQuery, context);
					String layerName = dataSourceQuery.toString().replace("NUM_LAYER", fillnumplanta(Integer.toString(this.numPlantasAdded), 2));
					
					if (dataset != null && 
							context.getLayerManager().uniqueLayerName(layerName).equals(layerName)
					) {
						//						dataSourceQuery.setQuery(query)

						//ASCPanel.get_instance().infoplantas.put(category, this.plantaInfo);

						Layer currentLayer = context.getLayerManager().addLayer(category,
								layerName, dataset)
								.setDataSourceQuery(dataSourceQuery)
								.setFeatureCollectionModified(false);

						setStylesFXCC(currentLayer, dataset);

						for(Iterator features = dataset.getFeatures().iterator();features.hasNext();){
							GeopistaFeature feature = (GeopistaFeature)features.next();
							feature.setLayer((GeopistaLayer)currentLayer);
						}

						if(dxfLoad instanceof GeopistaLoadDxfQueryChooser)
						{
							if(currentLayer instanceof GeopistaLayer)
							{
								GeopistaLayer currentGeopistaLayer = (GeopistaLayer) currentLayer;
								String logFilePath = (String)dataSourceQuery.getDataSource().getProperties().get(Constantes.ORIGINAL_FILE_KEY);
								currentGeopistaLayer.activateLogger(logFilePath);
								if(!currentGeopistaLayer.getName().toUpperCase().startsWith("PS") || 
										currentGeopistaLayer.getName().toUpperCase().endsWith("CO")){
									currentGeopistaLayer.setVisible(false);
								}

							}
						}
					}
				} finally {
					connection.close();
				}
				if (!exceptions.isEmpty()) {
					if (!exceptionsEncountered) {
						context.getOutputFrame().createNewDocument();
						exceptionsEncountered = true;
					}
					reportExceptions(exceptions, dataSourceQuery, context);
				}
			}
		}

		if (exceptionsEncountered) {
			context.getWorkbenchGuiComponent().warnUser("Problems were encountered. See Output Window for details.");
		}

	}


	private void reportExceptions(ArrayList exceptions,
			DataSourceQuery dataSourceQuery, PlugInContext context) {
		context.getOutputFrame().addHeader(1,
				exceptions.size() + " problem" + StringUtil.s(exceptions.size()) +
				" loading " + dataSourceQuery.toString() + "." +
				((exceptions.size() > 10) ? " First and last five:" : ""));
		context.getOutputFrame().addText("See View / Log for stack traces");
		context.getOutputFrame().append("<ul>");

		Collection exceptionsToReport = exceptions.size() <= 10 ? exceptions
				: CollectionUtil.concatenate(Arrays.asList(
						new Collection[] {
								exceptions.subList(0, 5),
								exceptions.subList(exceptions.size() - 5,
										exceptions.size())
						}));
		for (Iterator j = exceptionsToReport.iterator(); j.hasNext();) {
			Exception exception = (Exception) j.next();
			context.getWorkbenchGuiComponent().log(StringUtil.stackTrace(exception));
			context.getOutputFrame().append("<li>");
			context.getOutputFrame().append(GUIUtil.escapeHTML(
					WorkbenchFrameImpl.toMessage(exception), true, true));
			context.getOutputFrame().append("</li>");
		}
		context.getOutputFrame().append("</ul>");
	}

	private void setStylesFXCC(Layer currentLayer, FeatureCollection dataset){

		Symbolizer[] symbolizerArray = new Symbolizer[1];
		SLDStyleImpl style = (SLDStyleImpl)currentLayer.getBasicStyle();  				
		createTextPoint(style, symbolizerArray, currentLayer.getName());
		Rule newRule = StyleFactory.createRule(symbolizerArray);    				
		style.getUserStyle("default").getFeatureTypeStyles()[0].addRule(newRule);
		currentLayer.getLabelStyle().setEnabled(false);
		currentLayer.getVertexStyle().setEnabled(false);

		if (style!=null && style.getStyles().size()>0){
			for (Iterator iteratorStyles = style.getStyles().iterator();iteratorStyles.hasNext();){
				UserStyle_Impl userStyle = (UserStyle_Impl)iteratorStyles.next();
				if(userStyle!=null && userStyle.getFeatureTypeStyles()!=null){
					for (int indice=0;indice<userStyle.getFeatureTypeStyles().length;indice++){
						FeatureTypeStyle_Impl featureStyle = (FeatureTypeStyle_Impl) userStyle.getFeatureTypeStyles()[indice];
						if(featureStyle.getRules()!=null){
							for (int indiceRules = 0; indiceRules < featureStyle.getRules().length; indiceRules++){
								Rule_Impl rule = (Rule_Impl) featureStyle.getRules()[indiceRules];
								if(rule.getSymbolizers()!=null){
									for(int indiceSymbolicers = 0; indiceSymbolicers < rule.getSymbolizers().length; indiceSymbolicers++){
										if (rule.getSymbolizers()[indiceSymbolicers] instanceof LineSymbolizer_Impl){
											if((dataset.getFeatureSchema().hasAttribute("TEXTO"))&&(currentLayer.getName().indexOf("CO")==-1)){
												featureStyle.removeRule(rule);    											
											}
											else if (currentLayer.getName().indexOf("LS")!=-1){
												float[] dash = {(float) 2.0,(float) 2.0};
												((LineSymbolizer_Impl)rule.getSymbolizers()[indiceSymbolicers]).getStroke().setDashArray(dash);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static String[] extensions(Class readerWriterDataSourceClass) {
		try {
			return ((StandardReaderWriterFileDataSource) readerWriterDataSourceClass
					.newInstance()).getExtensions();
		} catch (Exception e) {
			Assert.shouldNeverReachHere(e.toString());
			return null;
		}
	}

	private String chooseCategory(DataSourceQuery dataSourceQuery, PlugInContext context) {

		if (dataSourceQuery != null && dataSourceQuery.toString()!=null){
			if (dataSourceQuery.toString().toUpperCase().startsWith("PG")){
				return I18N.get("Expedientes","fxcc.panel.CategoriaPG");
			}
			else if(dataSourceQuery.toString().toUpperCase().startsWith("PS")){
				//	    			return I18N.get("Expedientes","fxcc.panel.CategoriaPS");
				LayerFamily psLayerFamily = psLayerFamiliesWithNomPlanta(this.plantaInfo.getNombrePlantas(), context);
				if (psLayerFamily == null){
					if (dataSourceQuery.toString().toUpperCase().length()>=4){
						return dataSourceQuery.toString().toUpperCase().substring(0,2) + fillnumplanta(Integer.toString(this.numPlantasAdded), 2) + "-" + this.plantaInfo.getNombrePlantas();
					}
					else{
						return I18N.get("Expedientes","fxcc.panel.CategoriaPS");
					}
				}else{
					this.numPlantasAdded = Integer.parseInt(psLayerFamily.getName().substring(2,4));
					return psLayerFamily.getName();
				}
			}
			else if (!context.getLayerNamePanel().getSelectedCategories().isEmpty()){
				return context.getLayerNamePanel().getSelectedCategories().iterator().next()
				.toString();
			}
			else{
				return StandardCategoryNames.WORKING;
			}
		}
		else{
			return StandardCategoryNames.WORKING;
		}

	}

	private LayerFamily psLayerFamiliesWithNomPlanta(String nombrePlantas, PlugInContext context) {
		// TODO Auto-generated method stub
		Iterator<LayerFamily> it = context.getLayerManager().getCategories().iterator();

		while (it.hasNext()){
			LayerFamily actualLayer = it.next();
			if (actualLayer.getName().contains(nombrePlantas)){
				return actualLayer;
			}
		}

		return null;
	}

	private void createTextPoint(SLDStyleImpl style,Symbolizer[] symbolizer, String layerName) {

		boolean italic = false;
		boolean bold = false;
		double fontSize = 10;
		Font font = StyleFactory.createFont("Arial",italic,bold,
				fontSize);
		Color colorFont = style.getLineColor();
		font.setColor(colorFont);
		String atributeName = "TEXTO";

		double anchorX = 0.5;
		double anchorY = 0.5;
		double displacementX = 0;
		double displacementY = 0;
		double rotation = 0.0;

		if(layerName!=null){

			if (layerName.indexOf("AS")!=-1){				
				anchorX = 0.5;
				anchorY = 0.0;				
			}
			else if (layerName.indexOf("TL")!=-1){
				anchorX = 0.5;
				anchorY = 0.5;		
			}
			else{
				anchorX = 0.5;
				anchorY = 1.0;	
			}
		}

		PointPlacement pointPlacement = StyleFactory.createPointPlacement(anchorX,
				anchorY,displacementX,displacementY,rotation);
		LabelPlacement labelPlacement = StyleFactory.createLabelPlacement(pointPlacement);
		TextSymbolizer textSymbolizer = StyleFactory.createTextSymbolizer( null, StyleFactory2.createLabel(atributeName), font, labelPlacement, null, null, 0, Double.MAX_VALUE);		
		symbolizer[0] = textSymbolizer;	
	}


	/**
	 * @return
	 */
	public boolean confirmarAnniadirDatosPlantaSignificativa(){
		// TODO Auto-generated method stub

		// mensaje del dialogo.
		String mensaje = "¿Desea realmente añadir los datos una planta significativa?";
		String titulo =  "Planta Significativa PlugIn" ;

		int seleccion = JOptionPane.showOptionDialog(
				AppContext.getApplicationContext().getMainFrame(),
				mensaje, 
				titulo,
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,    // null para icono por defecto.
				new Object[] { "Aceptar", "cancelar"},   // null para YES, NO y CANCEL
		"cancelar");

		if (seleccion == 0)
			return true;

		return false;
	}


	/**
	 * @return true if accept action dialog performed correctly and false in any other case
	 */ 
	public boolean insertadoPlantaSignificativaInfo (){

		int seleccion = PlantaSignificativaFormDialog.showPlantaSignificativaFormDialog(aplicacion.getMainFrame(),plantaInfo);

		if (seleccion == 0){
			return true;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.plugin.AbstractPlugIn#addButton(com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog)
	 */
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!this.plantaSignificativaButtonAdded)
		{
			toolbox.addToolBar();
			PlantaGeneralPlugIn explode = new PlantaGeneralPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			this.plantaSignificativaButtonAdded = true;
		}
	}


	/**
	 * Método que devuelve la cadena rellena con el caracter "0", hasta ocupar "lon" posiciones, desde el lado indicado.
	 *	<ul>
	 * 		<li>Utiliza el caracter como símbolo de relleno</li>
	 * 		<li>Valores posibles de lado
	 *			<ul>
	 *				<li>0: izquierda</li>
	 *				<li>1: derecha</li>
	 *			</ul>
	 * 		</li>
	 *	</ul>
	 *
	 * @param java.lang.String cadena: Cadena a ser formateada
	 * @param int longitud: Indica la longitud de la cadena resultante
	 * @return java.lang.String Cadena rellena
	 */
	public String fillnumplanta(String cadena, int longitud)
	{
		String salida = cadena;
		char caracterRelleno = '0';

		//Si tiene la misma longitud la devuelve
		if(salida.length() == longitud)	return salida;

		//Si es más larga la trunca
		if(salida.length() > longitud)	return salida;

		//Si es menor, entonces modificamos
		if(salida.length() < longitud)
		{

			//				//	Rellenar por la derecha
			//				for(int k=cadena.length();k<longitud;k++)
			//					salida = salida + caracterRelleno;

			//	Rellenar por la izquierda
			for(int k=cadena.length();k<longitud;k++)
				salida = caracterRelleno + salida;


			return salida;
		}

		return cadena;
	}
}
