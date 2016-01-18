package com.geopista.ui.plugin.loadautocadmap;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;
import org.deegree_impl.graphics.sld.StyleFactory;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.model.LayerFamily;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.ui.GeopistaWorkbenchFrame;
import com.geopista.ui.plugin.io.dxf.DxfPlugIn.Dxf;
import com.geopista.ui.plugin.io.dxf.GeopistaLoadDxfQueryChooser;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jts.util.AssertionFailedException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class LoadAutoCADMapPlugIn extends ThreadedBasePlugIn
{
	/**
	 * Logger for this class
	 */
	private static final Log logger	= LogFactory.getLog(LoadAutoCADMapPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private GeopistaMap mapGeopista = null;

    private ListaMapasAutoCADDialog ListaMapasDialog = null;
    public static final String DIRTOLOAD = "dirToLoad";
  	private Blackboard blackboard  = aplicacion.getBlackboard();
  	private JFileChooser fileChooser; 
  	 public static final String DIRAUTOCAD = "autocad";
    Frame owner = null;

    public static final FileFilter GEOPISTA_MAP_FILE_FILTER = GUIUtil.createFileFilter(
            I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.AutoCADMapFiles"),
            new String[] { "gpc" });

    public LoadAutoCADMapPlugIn()
    {
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.loadautocadmap.languages.LoadAutoCADMapPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("LoadAutoCADMapPlugIn",bundle2);
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        return null;
    }

    public String getName()
    {
        return I18N.get("LoadAutoCADMapPlugIn","LoadAutoCADMap");
    }

    public void initialize(PlugInContext context) throws Exception
    {
       
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addMainMenuItem(this, GeopistaUtil.i18n_getname("File"),
                this.getName() + "...", null,
                LoadAutoCADMapPlugIn.createEnableCheck(context.getWorkbenchContext()));

        if (context.getWorkbenchGuiComponent() instanceof Frame)
        {
            if (owner == null)
                owner = (Frame) context.getWorkbenchGuiComponent();
        } else
        {
            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
                    (Component) context.getWorkbenchContext().getIWorkbench().getGuiComponent());
        }
        ListaMapasDialog = new ListaMapasAutoCADDialog(owner);
    }
    
    public boolean execute(PlugInContext context) throws Exception
    {
    	boolean ejecutar = false;
        reportNothingToUndoYet(context);

        int resp=getFileChooser().showOpenDialog(aplicacion.getMainFrame());
		File selFil= getFileChooser().getSelectedFile();
		if (resp==JFileChooser.APPROVE_OPTION && selFil!=null)
		{
			 blackboard.put(DIRTOLOAD,selFil.getAbsolutePath());
		
        
        ListaMapasDialog = new ListaMapasAutoCADDialog(aplicacion.getMainFrame());        
        ListaMapasDialog.getMap(context);        
        mapGeopista = ListaMapasDialog.getMapAutoCAD();

        ListaMapasDialog.dispose();
        if (mapGeopista == null)
            return false;
        
        	ejecutar = true ;
		}
        return ejecutar;
    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {
    	File file = null;
    	ITask generalTask = null;

    	try
    	{
    		monitor.report(I18N.get("LoadAutoCADMapPlugIn","LoadAutoCADMap.LoadingMap")
    				+ " " + mapGeopista.getName());

    		file = new File(mapGeopista.getBasePath(),mapGeopista.getName());
    		GeopistaMap sourceTask = GeopistaMap.getMapDXF(file.getAbsolutePath());
    		
    		
    		//ASD
    		File fileMap = new File(file.getParent(), "geopistamap.gpc");
			GeopistaMap geopistaMap = null;
			try {
				geopistaMap = GeopistaMap.getMapUTF8(fileMap.getAbsolutePath());
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			sourceTask.setMapCoordinateSystem( 
					CoordinateSystemRegistry.instance(AppContext.getApplicationContext().getBlackboard()).
					get(geopistaMap.getMapProjection())
			);
    		
    		
    		
    		GeopistaLoadDxfQueryChooser dxfLoad = new GeopistaLoadDxfQueryChooser(Dxf.class,
    				"GEOPISTA dxf",
    				GeopistaFunctionUtils.extensions(Dxf.class),
    				context.getWorkbenchContext());   
    		
    		InputStream fileDXF = new FileInputStream(file);
    		    
    		Collection dataSourceQueries = null;
    		try
    		{
    			dataSourceQueries = dxfLoad.getDataSourceQueries(fileDXF, file.getParent(), sourceTask.getMapCoordinateSystem());    
    			//dataSourceQueries = dxfLoad.getDataSourceQueries(fileDXF, file.getParent());    	
    			Assert.isTrue(!dataSourceQueries.isEmpty());
    		}
    		catch (AssertionFailedException e)
    		{
    			throw new AssertionFailedException(I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.FileEmpty"));

    		}
    		//ASD
//    		File fileMap = new File(file.getParent(), "geopistamap.gpc");
//			GeopistaMap geopistaMap = null;
//			try {
//				geopistaMap = GeopistaMap.getMapUTF8(fileMap.getAbsolutePath());
//			} catch (Exception e1) {
//				
//				e1.printStackTrace();
//			}

			//ASD
//			sourceTask.setMapCoordinateSystem( 
//					CoordinateSystemRegistry.instance(AppContext.getApplicationContext().getBlackboard()).
//					get(geopistaMap.getMapProjection())
//			);
			
			
			
			ArrayList layers = new ArrayList();
			for (Iterator iterCategories = geopistaMap.getCategories().iterator();iterCategories.hasNext();){
				
				LayerFamily layerFamily = (LayerFamily)iterCategories.next();
				for(Iterator iterLayerables = layerFamily.getLayerables().iterator();iterLayerables.hasNext();){
					Layer layer = (Layer)iterLayerables.next();
					layers.add(layer);					
				}
			}
    		
    		boolean exceptionsEncountered = false;
    		for (Iterator i = dataSourceQueries.iterator(); i.hasNext();) {
    			DataSourceQuery dataSourceQuery = (DataSourceQuery) i.next();
    			boolean layerRepeated = false;
    			sourceTask.getLayerManager().getLayers();
    			List allLayerList = sourceTask.getLayerManager().getLayers();
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

    			Connection connection = dataSourceQuery.getDataSource().getConnection();
    			try {
    				FeatureCollection dataset = dataSourceQuery.getDataSource().installCoordinateSystem(connection.executeQuery(dataSourceQuery.getQuery(),
    						exceptions, monitor), CoordinateSystemRegistry.instance(context.getWorkbenchContext().getBlackboard()));
    				if (dataset != null) {
    					Layer currentLayer = sourceTask.getLayerManager()
    					.addLayer(GeopistaUtil.chooseCategory(context),
    							dataSourceQuery.toString(), dataset)
    							.setDataSourceQuery(dataSourceQuery)
    							.setFeatureCollectionModified(false);
    					
    					if (currentLayer instanceof GeopistaLayer){
    						((GeopistaLayer)currentLayer).setLocal(false);
    					}
    					Symbolizer[] symbolizerArray = new Symbolizer[1];
    					SLDStyleImpl style = (SLDStyleImpl)currentLayer.getBasicStyle();  				
    					GeopistaUtil.createTextPoint(style, symbolizerArray);
    					Rule newRule = StyleFactory.createRule(symbolizerArray);    				
    					style.getUserStyle("default").getFeatureTypeStyles()[0].addRule(newRule);
    					currentLayer.getLabelStyle().setEnabled(false);
    					currentLayer.getVertexStyle().setEnabled(false);

    	                for(Iterator features = dataset.getFeatures().iterator();features.hasNext();){
    	                	GeopistaFeature feature = (GeopistaFeature)features.next();
    	                	feature.setLayer((GeopistaLayer)currentLayer);
    	                }
    	                
    					if(dxfLoad instanceof GeopistaLoadDxfQueryChooser)
    					{
    						if(currentLayer instanceof GeopistaLayer)
    						{    							
    							GeopistaLayer currentGeopistaLayer = (GeopistaLayer) currentLayer;
    							for (Iterator lstlayers = layers.iterator();lstlayers.hasNext();){
    								Layer layer = (Layer)lstlayers.next();
    								if(layer instanceof GeopistaLayer){
    									if (currentGeopistaLayer.getSystemId().equalsIgnoreCase(((GeopistaLayer)layer).getSystemId())){
    										currentGeopistaLayer.setSystemId(((GeopistaLayer)layer).getSystemId());
    										currentGeopistaLayer.setName(layer.getName());
    									}
    								}
    								else{
    									if (currentGeopistaLayer.getName().equalsIgnoreCase(layer.getName())){
    										currentGeopistaLayer.setName(layer.getName());
    									}
    								}
    							}
    							String logFilePath = (String)dataSourceQuery.getDataSource().getProperties().get(Constantes.ORIGINAL_FILE_KEY);
    							currentGeopistaLayer.activateLogger(logFilePath);
    						}
    						else{
    							for (Iterator lstlayers = layers.iterator();lstlayers.hasNext();){
    								Layer layer = (Layer)lstlayers.next();
    								
    									if (currentLayer.getName().equalsIgnoreCase(layer.getName())){
    										currentLayer.setName(layer.getName());
    									}
    								
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
    				//reportExceptions(exceptions, dataSourceQuery, context);
    			}
    		}
    				
    		if (exceptionsEncountered) {
    			context.getWorkbenchGuiComponent().warnUser("Problems were encountered. See Output Window for details.");
    		}
    		
    		monitor.report(I18N.get("LoadAutoCADMapPlugIn","LoadAutoCADMap.LoadingMap")
    				+ " " + mapGeopista.getName());

    		HashMap orderLayers = (HashMap) sourceTask.getOrdersLayer();
    		String mapSystemId = sourceTask.getSystemId();
    		JInternalFrame[] loaderMaps = context.getWorkbenchGuiComponent()
    		.getInternalFrames();

    		if (loaderMaps != null)
    		{
    			for (int i = 0; i < loaderMaps.length; i++)
    			{
    				if (!(loaderMaps[i] instanceof GeopistaTaskFrame)) continue;
    				String actualSystemId = ((GeopistaMap) (((GeopistaTaskFrame) loaderMaps[i])
    						.getTaskFrame()).getTask()).getSystemId();
    				if (mapSystemId.equals(actualSystemId))
    				{
    					(((GeopistaTaskFrame) loaderMaps[i]).getTaskFrame())
    					.moveToFront();
    					return;
    				}
    			}
    		}

    		GeopistaMap newTask = new GeopistaMap(context.getWorkbenchContext());

    		WorkbenchGuiComponent workbenchFrame = context.getWorkbenchGuiComponent();

    		GeopistaLayerManager newLayerManager = null;

    		if (workbenchFrame instanceof GeopistaWorkbenchFrame)
    		{
    			newTask.setName(sourceTask.getName());
    			newTask.setExtracted(sourceTask.isExtracted());
    			newTask.setDescription(sourceTask.getDescription());
    			newTask.setMapUnits(sourceTask.getMapUnits());
    			newTask.setMapScale(sourceTask.getMapScale());	
    			newTask.setMapCoordinateSystem( 
    					sourceTask.getMapCoordinateSystem()
    			);
    			newTask.setProjectFile(file);
    			if(sourceTask.getSystemId()!=null&&!sourceTask.getSystemId().trim().equals(""))
    			{
    				newTask.setSystemId(sourceTask.getSystemId());
    			}
    			else
    			{    				
    				newTask.setSystemId("l" + String.valueOf(System.currentTimeMillis()));
    			}

    			workbenchFrame.addTaskFrame(newTask);
    			newLayerManager = (GeopistaLayerManager) newTask.getLayerManager();
    			generalTask = newTask;
    		} else
    		{
    			((GeopistaEditor) context.getWorkbenchGuiComponent()).reset();
    			context.getTask().setName(sourceTask.getName());
    			context.getTask().setProjectFile(file);
    			newLayerManager = (GeopistaLayerManager) context.getLayerManager();
    			generalTask = context.getTask();
    		}

    		ILayerManager sourceLayerManager = sourceTask.getLayerManager();
    		for (Iterator i = sourceLayerManager.getCategories().iterator(); i
    		.hasNext();)
    		{
    			Category sourceLayerCategory = (Category) i.next();

    			CoordinateSystemRegistry registry = CoordinateSystemRegistry
    			.instance(context.getWorkbenchGuiComponent().getContext()
    					.getBlackboard());

    			newLayerManager.addCategory(sourceLayerCategory.getName());
    			((LayerFamily) newLayerManager.getCategory(sourceLayerCategory
    					.getName())).setSystemId(((LayerFamily) sourceLayerCategory)
    							.getSystemId());

    			ArrayList layerables = new ArrayList(sourceLayerCategory
    					.getLayerables());
    			Collections.reverse(layerables);

    			for (Iterator j = layerables.iterator(); j.hasNext();)
    			{
    				Layerable layerable = (Layerable) j.next();
    				layerable.setLayerManager(newLayerManager);
    				monitor.report(I18N.get("LoadAutoCADMapPlugIn", "LoadAutoCADMap.Loading")
    						+ " " + layerable.getName());
    				if (layerable instanceof Layer)
    				{
    					Layer layer = (Layer) layerable;

    					layer.setFeatureCollection(executeQuery(layer
    							.getDataSourceQuery().getQuery(), layer
    							.getDataSourceQuery().getDataSource(),
    							registry, null));

    					if (layer instanceof GeopistaLayer)
    					{    						
    						FeatureSchema schema = layer.getFeatureCollectionWrapper().getFeatureSchema();
    						if (schema instanceof GeopistaSchema)
    						{
    							((GeopistaSchema)schema).setGeopistalayer((GeopistaLayer)layer);

    						}
    					}

    					layer.setFeatureCollectionModified(false);
    					if (layer instanceof GeopistaLayer)
    					{                               
    						//((GeopistaLayer) layer).setLocal(true);
    						{
    							try
    							{
    								((GeopistaLayer) layer).activateLogger((GeopistaMap)generalTask);
    							}catch(Exception e)
    							{
    								JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("GeopistaLoadMapPlugIn.errorCargandoLog") + " " + layer.getName() + " "+ aplicacion.getI18nString("GeopistaLoadMapPlugIn.perdidosDatos"));
    							}
    						}
    					}

    				}
    				Integer layerPosition = null;
    				if (layerable instanceof GeopistaLayer)
    					layerPosition = (Integer) orderLayers.get(((GeopistaLayer) layerable).getSystemId());
    				else
    					layerPosition = (Integer) orderLayers.get(layerable.getName());
    				if(layerPosition!=null)
    				{
    					newLayerManager.addLayerable(sourceLayerCategory.getName(),
    							layerable,layerPosition.intValue());
    				}
    				else
    				{
    					newLayerManager.addLayerable(sourceLayerCategory.getName(),
    							layerable);
    				}
    			}
    		}
    		updateTitleToNoModified(newLayerManager, generalTask.getTaskComponent());
    		
    		if (context.getActiveInternalFrame() instanceof GeopistaTaskFrame)
            {
//    			int j=0;
//    			for (Iterator i = ((GeopistaTaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getLayerManager().getLayer("parcelas").getFeatureCollectionWrapper().iterator(); i.hasNext();){
//    				GeopistaFeature f = (GeopistaFeature)i.next();
//    				System.out.println(f.getSystemId());
//    				try{
//    					Integer w = new Integer(f.getSystemId());
//    					System.out.println(f.getAttribute("Referencia catastral"));
//    				}catch(Exception e){
//    					((GeopistaTaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getViewport().zoomToFullExtent();
//    				}
//    			}
                ((GeopistaTaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getViewport().zoomToFullExtent();    
            }

    	} catch (Exception e)
    	{
    		logger.error("run(TaskMonitor, PlugInContext)", e);
    		throw e;
    	}

    }

    private void updateTitleToNoModified(LayerManager layerManager,
            TaskComponent taskFrame)
    {

        if (layerManager instanceof GeopistaLayerManager)
        {
            ((GeopistaLayerManager) layerManager).setDirty(false);
            if (taskFrame instanceof JInternalFrame)
            {
                String newTitle = taskFrame.getTitle();
                if (newTitle.charAt(0) == '*')
                {
                    newTitle = newTitle.substring(1);
                }
                ((JInternalFrame) taskFrame).setTitle(newTitle);
            }
        }
    }

    private FeatureCollection executeQuery(String query, DataSource dataSource,
            CoordinateSystemRegistry registry, TaskMonitor monitor) throws Exception
    {
        Connection connection = dataSource.getConnection();
        try
        {
            return dataSource.installCoordinateSystem(connection.executeQuery(query,
                    monitor), registry);
        } finally
        {
            connection.close();
        }
    }
    
    private JFileChooser getFileChooser() {
	 	 
        if (fileChooser == null) {
        	fileChooser=new JFileChooser();
        	fileChooser.setMultiSelectionEnabled(false);
        	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	 
        }
        File currentDirectory = new File(AppContext.DEFAULT_DATA_PATH + File.separator + DIRAUTOCAD);
        fileChooser.setCurrentDirectory(currentDirectory);
   	

        return (JFileChooser) fileChooser;
    }
        
}
