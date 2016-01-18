package com.geopista.ui.plugin.infcattitularidad.dialogs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.DxfWriterCatastro;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.model.beans.ASCCatastro;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.PlantaCatastro;
import com.geopista.app.catastro.model.beans.UsoCatastro;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.geopista.editor.GeopistaContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.LayerFamily;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.ui.cursortool.GeopistaMeasureTool;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.plugin.SynchronizePlugIn;
import com.geopista.ui.plugin.io.dxf.GeopistaLoadDxfQueryChooser;
import com.geopista.ui.plugin.io.dxf.DxfPlugIn.Dxf;
import com.geopista.ui.plugin.plantasignificativa.info.PlantaSignificativaInfo;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jts.util.AssertionFailedException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.util.java2xml.Java2XMLCatastro;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrameImpl;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.zoom.PanTool;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomTool;
import com.geopista.app.administrador.init.Constantes;

public class FxccPanel extends JPanel implements FeatureExtendedPanel{
    
	public GeopistaEditor geopistaEditor=null;
	//private GeopistaEditor geopistaEditor=new GeopistaEditor();
    
    public GeopistaEditor getGeopistaEditor() {
		return geopistaEditor;
	}

	public void setGeopistaEditor(GeopistaEditor geopistaEditor) {
		this.geopistaEditor = geopistaEditor;
	}

	private String refCatastral;
	private static FincaCatastro finca = null;

	private JButton jButtonNuevoDXF = null;
	

	private JPanel jPanelGeopistaEditor = null;
	private String lastRefCatastral = "";
	
	private static Logger logger = Logger.getLogger(UtilRegistroExp.class);
    
	public boolean insertarCapasGeopista=true;
	
	public String textoAscTemp=null;
	
	private GestionExpedientePanel gestionExpedientePanel = null;
	
    public FxccPanel()
    {
        super(new GridLayout(1,0));
        initialize();
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.infcattitularidad.languages.InfCatastraTitularidadPlugIni18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("InfCatastralTitularidadPlugIn",bundle);        
    }
    
    public FxccPanel(GestionExpedientePanel gestionExpedientePanel)
    {    	
        super(new GridLayout(1,0));
        this.gestionExpedientePanel = gestionExpedientePanel;
        initialize();
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.infcattitularidad.languages.InfCatastraTitularidadPlugIni18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("InfCatastralTitularidadPlugIn",bundle);        
    }
    
    private void initialize() {

    	this.setLayout(new GridBagLayout());

    	geopistaEditor = new GeopistaEditor("workbench-properties-simple.xml");
    	geopistaEditor.getToolBar().addCursorTool("Zoom In/Out", new ZoomTool());
    	geopistaEditor.getToolBar().addCursorTool("Measure", new GeopistaMeasureTool());
    	geopistaEditor.getToolBar().addCursorTool("Pan", new PanTool());		
    	geopistaEditor.setVisible(true);
    	geopistaEditor.showLayerName(true);

    	this.add(geopistaEditor,
    			new GridBagConstraints(0, 0, 1, 1, 1, 1, 
    					GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets (0,0,0,0), 0,0));
    	this.add(getJButtonNuevoDXF(), 
    			new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.EAST,
    					GridBagConstraints.NONE, new Insets (0,0,0,0), 0,0));

	}
    

	public void setDxfData(FincaCatastro finca){
		this.refCatastral = finca.getRefFinca().getRefCatastral();
		this.finca = finca;
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
    		Assert.isTrue(!dxfLoad
    				.getDataSourceQueries(fileDXF).isEmpty());
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
    		List allLayerList = geopistaEditor.getLayerManager().getLayers();
    		
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

    		if(layerRepeated) continue;

    		ArrayList exceptions = new ArrayList();
    		Assert.isTrue(dataSourceQuery.getDataSource().isReadable());
 
    		Connection connection = dataSourceQuery.getDataSource()
    		.getConnection();
    		try {
    			FeatureCollection dataset = dataSourceQuery.getDataSource().installCoordinateSystem(connection.executeQuery(dataSourceQuery.getQuery(),
    					exceptions, monitor), CoordinateSystemRegistry.instance(context.getWorkbenchContext().getBlackboard()));
    			if (dataset != null) {
    				Layer currentLayer = geopistaEditor.getLayerManager()
    				.addLayer(chooseCategory(dataSourceQuery),
    						dataSourceQuery.toString(), dataset)
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
    						if(!currentGeopistaLayer.getName().toUpperCase().startsWith("PG") || 
    								currentGeopistaLayer.getName().toUpperCase().startsWith("PG-CO")){
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
    			
    	if (exceptionsEncountered) {
    		context.getWorkbenchGuiComponent().warnUser("Problems were encountered. See Output Window for details.");
    	}
    }

    public static String[] extensions(Class readerWriterDataSourceClass) {
    	try {
    		return ((StandardReaderWriterFileDataSource) readerWriterDataSourceClass
    				.newInstance()).getExtensions();
    	} catch (Exception e) {
    		Assert.shouldNeverReachHere(e.toString());
    		return null;
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

    private String chooseCategory(DataSourceQuery dataSourceQuery) {
    	
    	if (dataSourceQuery != null && dataSourceQuery.toString()!=null){
    		if (dataSourceQuery.toString().toUpperCase().startsWith("PG")){
    			return I18N.get("InfCatastralTitularidadPlugIn","infCatastralTitularidad.fxcc.panel.CategoriaPG");
    		}
    		else if(dataSourceQuery.toString().toUpperCase().startsWith("PS")){
//    			return I18N.get("Expedientes","fxcc.panel.CategoriaPS");
    			if (dataSourceQuery.toString().toUpperCase().length()>=4){
    				String nombreASC = getNombrePlantaASC(dataSourceQuery.toString().toUpperCase().substring(0,4));
    				if (nombreASC!=null && !nombreASC.equals("")){
    					return dataSourceQuery.toString().toUpperCase().substring(0,4)+ "-" + nombreASC;
    				}
    				else{
    					return dataSourceQuery.toString().toUpperCase().substring(0,4);
    				}
    			}
    			else{
    				return I18N.get("InfCatastralTitularidadPlugIn","infCatastralTitularidad.fxcc.panel.CategoriaPS");
    			}
    		}
    		else if (!geopistaEditor.getLayerNamePanel().getSelectedCategories().isEmpty()){
    			return geopistaEditor.getLayerNamePanel().getSelectedCategories().iterator().next()
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
    
    private String getNombrePlantaASC(String nomPlantaDXF){
    	
    	String nombreASC = "";
    	
    	String nombreDXF = nomPlantaDXF.substring(2,4);
    	int numPlanta = Integer.parseInt(nombreDXF);
    	
    	if (AppContext.getApplicationContext().getBlackboard().get("ascObject")!=null){
    		
    		ASCCatastro asc = (ASCCatastro) AppContext.getApplicationContext().getBlackboard().get("ascObject");
    		ArrayList lstPlantas = asc.getLstPlantas();
    		if (lstPlantas != null && lstPlantas.size()>0){
    			PlantaCatastro planta = (PlantaCatastro) lstPlantas.get(numPlanta-1);
    			nombreASC = planta.getNombre();
    		}
    	}
    	
    	return nombreASC;
    }
    
    public void initDXF() {

    	final FincaCatastro fincaCatastro = this.finca;
    	
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
    	//progressDialog.setTitle(I18N.get("Expedientes","fxcc.panel.CargandoFicheroDXF"));
    	progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.report(I18N.get("InfCatastralTitularidadPlugIn","infCatastralTitularidad.fxcc.panel.ComprobandoFicheroDXF"));
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
    						//EdicionOperations oper = new EdicionOperations();
    						//String textFxcc = oper.obtenerDxf(id_Expediente,ref_Catastral);
    						Java2XMLCatastro aux2 = new Java2XMLCatastro();
							String textFxcc = aux2.write(fincaCatastro.getFxcc(), "fxcc");
							
    						if(textFxcc!=null){

    							SAXBuilder builder = new SAXBuilder();
    							Document docFxcc = builder.build(ImportarUtils.parseStringToIS(textFxcc));
    							Element raizFxcc = docFxcc.getRootElement();

    							Element dxf = raizFxcc.getChild("dxf");
    							Element asc = raizFxcc.getChild("asc");
    							
    							
    							if(dxf!=null){
    								//String base64 = dxf.getText();
    								if (asc!=null){
    									textoAscTemp=asc.getText();
    									AppContext.getApplicationContext().getBlackboard().put("ascTemp",textoAscTemp);
    								}
    								progressDialog.report(I18N.get("InfCatastralTitularidadPlugIn","infCatastralTitularidad.fxcc.panel.CargandoFicheroDXF"));

    								//final String file = ImportarUtils.base64ToAscii(base64.toString());
    								final String file = dxf.getText();
    								final PlugInContext context = new PlugInContext(new GeopistaContext(geopistaEditor),null,null,null,null);

    								try {
    									
    									if (textoAscTemp!=null && !textoAscTemp.equals("")){
    										ASCPanel.get_instance().loadASC(progressDialog, textoAscTemp);
    									}

    									loadLayers();
    									loadDXF(progressDialog,context,file);
    									getJButtonNuevoDXF().setEnabled(false);
    									
    									ASCPanel.get_instance().enter();

    								} catch (Exception e) {
    									
    									e.printStackTrace();
    								}
    							}
    						}
    						else{
    							geopistaEditor.reset();
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
    }
    
    public void loadLayers(){
    	
    	if (ConstantesRegistroExp.geopistaEditor !=null ){
			ArrayList baseLayers = (ArrayList) ConstantesRegistroExp.geopistaEditor.getLayerManager().getLayers();

			for (Iterator iterBaseLayers = baseLayers.iterator(); iterBaseLayers.hasNext();){
				Object object = (Object)iterBaseLayers.next();    										
				if (object instanceof GeopistaLayer){
					GeopistaLayer layer = (GeopistaLayer)object;
					geopistaEditor.getLayerManager().addLayer(ConstantesRegistroExp.geopistaEditor.getLayerManager().getCategory(layer).getName(),layer);
				}
			}
		}
    }
    

    public void enter() {

    	if (this.gestionExpedientePanel != null){
    		this.gestionExpedientePanel.getJButtonValidar().setEnabled(false);
    		
    		if(this.gestionExpedientePanel.getExpediente().getIdEstado() >= ConstantesCatastro.ESTADO_FINALIZADO)
            {
                EdicionUtils.enablePanel(this.gestionExpedientePanel.getJPanelBotones(), false);   
                this.gestionExpedientePanel.getJButtonEstadoModificado().setEnabled(true);
            }
    		else{
    			EdicionUtils.enablePanel(this.gestionExpedientePanel.getJPanelBotones(), true);
    			this.gestionExpedientePanel.getJButtonEstadoModificado().setEnabled(false);
    		}
    	}
    	
    	if(refCatastral!=null){

    		if(!refCatastral.equals(lastRefCatastral)){

    			if(geopistaEditor==null){

    				geopistaEditor = new GeopistaEditor("workbench-properties-catastro-simple.xml");
    				geopistaEditor.getToolBar().addCursorTool("Zoom In/Out", new ZoomTool());
    				geopistaEditor.getToolBar().addCursorTool("Measure", new GeopistaMeasureTool());
    				geopistaEditor.getToolBar().addCursorTool("Pan", new PanTool());	

    				geopistaEditor.setVisible(true);
    				geopistaEditor.showLayerName(true);

    				this.add(geopistaEditor,
    						new GridBagConstraints(0, 0, 1, 1, 1, 1, 
    								GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets (0,0,0,0), 0,0));
    			}
    			else{

    				this.remove(geopistaEditor);			
    				geopistaEditor.removeAll();
    				geopistaEditor=null;

    				geopistaEditor = new GeopistaEditor("workbench-properties-catastro-simple.xml");
    				geopistaEditor.getToolBar().addCursorTool("Zoom In/Out", new ZoomTool());            
    				geopistaEditor.getToolBar().addCursorTool("Pan", new PanTool());
    				geopistaEditor.getToolBar().addCursorTool("Measure", new GeopistaMeasureTool());

    				geopistaEditor.setVisible(true);
    				geopistaEditor.showLayerName(true);


    				this.add(geopistaEditor,
    						new GridBagConstraints(0, 0, 1, 1, 1, 1, 
    								GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets (0,0,0,0), 0,0));


    			}
    			getJButtonNuevoDXF().setEnabled(true);	
    			geopistaEditor.updateUI();
    			initDXF();
    			try {
    				GeopistaLayer geopistaLayer = (GeopistaLayer) geopistaEditor.getLayerViewPanel().getLayerManager().getLayer("PG-LP");
					
					geopistaEditor.getLayerViewPanel().getViewport().zoom(EnvelopeUtil.bufferByFraction(
				                envelopeOfSelectedLayers(geopistaLayer), 0.03));
					geopistaEditor.updateUI();
					
				} catch (NoninvertibleTransformException e) {
					e.printStackTrace();
				}

    		lastRefCatastral = refCatastral;

    		try
    		{
    			// Iniciamos la ayuda
    			String helpHS = "help/catastro/herramientasEdicion/HerrEdicHelp_es.hs";
    			ClassLoader c1 = this.getClass().getClassLoader();
    			URL hsURL = HelpSet.findHelpSet(c1, helpHS);
    			HelpSet hs = new HelpSet(null, hsURL);
    			HelpBroker hb = hs.createHelpBroker();
    			// fin de la ayuda
    			hb.enableHelpKey(this,"introduccion", hs);
    		} 
    		catch (Exception excp)
    		{
    			excp.printStackTrace();
    		}
    		}
    		else{
    			getJButtonNuevoDXF().setEnabled(false);
    		}
    	}
    	else{
    		getJButtonNuevoDXF().setEnabled(false);
    	}
    	
    }
    
	public void exit() {
		try{
				
			GeopistaEditingPlugIn geopistaEditingPlugIn = (GeopistaEditingPlugIn) (geopistaEditor.getContext().getBlackboard().get(EditingPlugIn.KEY));
			ToolboxDialog toolbox = geopistaEditingPlugIn.getToolbox(geopistaEditor.getContext());
			toolbox.setVisible(false);	
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	public ASCCatastro setLstPlantasUsos(ASCCatastro ascCatastro){

		if (getGeopistaEditor() != null){

			if (!getGeopistaEditor().getLayerManager().getLayers().isEmpty()){
				
				Iterator<LayerFamily> it =  geopistaEditor.getLayerManager().getCategories().iterator(); 
				
				ArrayList lstPlantasUsos = new ArrayList();
				
				while(it.hasNext()){
					
					LayerFamily actualCategory = it.next(); 
					
					if (actualCategory.getName().startsWith("PS")){
						
						Layer actualLayerAU = null;
						Layer actualLayerAS = null;
						
						PlantaCatastro planta = new PlantaCatastro();

						Iterator<Layer> layerIteraor = actualCategory.getLayerables().iterator();
						
						while(layerIteraor.hasNext()){
							
							Layer auxLayer = layerIteraor.next();
							if (auxLayer.getName().startsWith("PS") && auxLayer.getName().endsWith("AU")){
								actualLayerAU = auxLayer;
							} else if (auxLayer.getName().startsWith("PS") && auxLayer.getName().endsWith("AS")) {
								actualLayerAS = auxLayer;
							}		
						}

							String plantaCategory = this.getPlantaCategory(actualLayerAU);
							
							String namesCategory[] = plantaCategory.split("-");
							String nameCategory = null;
							if (namesCategory != null && namesCategory.length>0){
								
								nameCategory= namesCategory[namesCategory.length-1];
							
								if (nameCategory != null && !nameCategory.equals("")){

									PlantaSignificativaInfo pInfo = (PlantaSignificativaInfo) ASCPanel.get_instance().infoplantas.get(nameCategory);
									if (pInfo != null){

										ArrayList usos = this.getUsosFromLayer(actualLayerAU, actualLayerAS);

										planta.setNombre(pInfo.getNombrePlantas());
										planta.setNumPlantasReales(pInfo.getNumPlantas());

										if (usos != null){
											planta.setLstUsos(usos);
										}
									}
								}
							}

							lstPlantasUsos.add(planta);
					}
				}
				ascCatastro.setLstPlantas(lstPlantasUsos);
			}			
		}
		return ascCatastro;
	}

	private ArrayList getUsosFromLayer(Layer layerAU, Layer layerAS) {
	// TODO Auto-generated method stub

	ArrayList result = new ArrayList();


	for (Iterator iterAUFeatures = layerAU.getFeatureCollectionWrapper().getFeatures().iterator(); iterAUFeatures.hasNext();){

		Object objAU = iterAUFeatures.next();
		Object  sup = null;
		Object  nom = null;

		if (objAU instanceof GeopistaFeature){

			GeopistaFeature featureAU = (GeopistaFeature)objAU;
			nom = featureAU.getAttribute("TEXTO");

			for (Iterator iterASFeatures = layerAS.getFeatureCollectionWrapper().getFeatures().iterator(); iterASFeatures.hasNext();){

				Object objAS = iterASFeatures.next();
				if (objAS instanceof GeopistaFeature){

					GeopistaFeature featureAS = (GeopistaFeature)objAS;
					if (featureAU.getGeometry().equals(featureAS.getGeometry())){
						sup = featureAS.getAttribute("TEXTO");
						break;
					}
				}
			}
		}
		if ( (nom!=null) && (sup!=null) ){
			result.add( new UsoCatastro((String) nom, Long.parseLong((String) sup) ) );
		}

	}



	//		Iterator<GeopistaFeature> it = layerAU.getFeatureCollectionWrapper().getFeatures().iterator();
	//		Iterator<GeopistaFeature> it2 = layerAS.getFeatureCollectionWrapper().getFeatures().iterator();
	//		
	//		
	//		while(it.hasNext()){
	//			GeopistaFeature gfNombre = it.next();
	//			Object nom = gfNombre.getAttribute("TEXTO");
	//			
	//			GeopistaFeature gfSuperficie = null;
	//			Object  sup = null;
	//			Object obj = it2.next();
	//			
	//			if (obj != null){
	//				gfSuperficie = (GeopistaFeature)obj;
	//				sup = gfSuperficie.getAttribute("TEXTO");
	//			}
	//			else{
	//				sup = 0;
	//			}
	//			
	//			
	//			if ( (nom!=null) && (sup!=null) ){
	//				result.add( new UsoCatastro((String) nom, Long.parseLong((String) sup) ) );
	//			}
	//			
	//		}
	//		


	return result;
}
	
	private boolean listaPlantasContiene(ArrayList<PlantaCatastro> lst, String patron){

		Iterator<PlantaCatastro> it = lst.iterator();

		while (it.hasNext()){
			if(it.next().getNombre().endsWith(patron)){
				return true;
			}
		}

		return false;
	}
	
	private String getPlantaCategory(Layer layer) {
		Iterator<LayerFamily> it = getGeopistaEditor().getLayerManager().getCategories().iterator();
		while (it.hasNext()){
			LayerFamily lf =  it.next();
			if (lf.getLayerables().contains(layer)){
				return lf.getName();
			}

		}
		return null;
	}


    
    public void guardarFXCC(FX_CC fxcc)
    {
        DxfWriterCatastro dxfWrite = new DxfWriterCatastro();
        try
        {
            if(geopistaEditor.getLayerManager().getLayers().size()>0)
            {

                ArrayList lstCapas = (ArrayList) geopistaEditor.getLayerManager().getLayers();
                ArrayList capas = new ArrayList();
                for(Iterator iterCapas = lstCapas.iterator(); iterCapas.hasNext();){
                	Layer capa = (Layer)iterCapas.next();
                	if(capa instanceof GeopistaLayer && ((GeopistaLayer)capa).isLocal() && (capa.getName().startsWith("PG-") || capa.getName().startsWith("PS"))) {
                  		capas.add(capa);
                  }
                }
                String resul= dxfWrite.write(capas);              
                fxcc.setDXF(resul);                
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();   
        }
    }
    
    public JButton getJButtonNuevoDXF()
    {
    	if (jButtonNuevoDXF  == null)
    	{
    		jButtonNuevoDXF = new JButton();
    		jButtonNuevoDXF.setText(I18N.get("InfCatastralTitularidadPlugIn", "infCatastralTitularidad.fxcc.panel.boton.nuevodxf"));

    		jButtonNuevoDXF.addActionListener(new java.awt.event.ActionListener()
    		{
    			public void actionPerformed(java.awt.event.ActionEvent e)
    			{
    				onNuevoDocumento();
    			}
    		});

    		jButtonNuevoDXF.setName("_nuevodxf");
    	}
    	return jButtonNuevoDXF;
    }
    
	private void onNuevoDocumento() {
		// TODO Auto-generated method stub
	//	ASCEditDialog ascDialog = new ASCEditDialog(ASCPanel.get_instance().getAsc(), this);
	}
    
    private JPanel getJPanelGeopistaEditor(){
    	
    	if(jPanelGeopistaEditor ==null){
    		jPanelGeopistaEditor = new JPanel();
    	}
    	return jPanelGeopistaEditor;
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
    
   /* public void guardarFXCCNoSeleccionado(FX_CC fxcc)
    {
        EdicionOperations oper = new EdicionOperations();
        try
        {
            String textFxcc = oper.obtenerDxf(this.idExpediente,this.refCatastral);
            if(textFxcc!=null)
            {
                SAXBuilder builder = new SAXBuilder();
                Document docFxcc = builder.build(ImportarUtils.parseStringToIS(textFxcc));
                Element raizFxcc = docFxcc.getRootElement();

                Element dxf = raizFxcc.getChild("dxf");
                if(dxf!=null)
                {
                    String base64 = dxf.getText();
                    fxcc.setDXF(base64);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }*/
    
    private Envelope envelopeOfSelectedLayers(GeopistaLayer layer) {
        Envelope envelope = new Envelope();		
		Envelope envelope2 = null;
		if (layer != null){
			envelope2 = layer.getEnvelope();
			if (envelope2!=null)
				envelope.expandToInclude(envelope2);
		}
        return envelope;
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

	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	
}
