package com.geopista.app.eiel.plugin.eielsavemap;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.acl.AclNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaSchema;
import com.geopista.io.datasource.GeopistaStandarReaderWriteFileDataSource.GeoGML;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.ui.dialogs.GeopistaMapPropertiesDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaAbstractSaveMapPlugIn;
import com.geopista.ui.plugin.LogFeatutesEvents;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class EIELSaveMapPlugIn extends GeopistaAbstractSaveMapPlugIn
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(EIELSaveMapPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();  
    
    private String toolBarCategory = "EIELSaveMapPlugIn.category";

    public static final ImageIcon ICON = IconLoader.icon("Folder.gif");
    
    public int contador=0;
    
    private boolean localSave = false;
    private static final int GRABARENBASEDATOS = 0; 
    private static final int GRABARENLOCAL = 1;
    private static final int CANCELARGRABACION = 2;
    
    private String oldName = new String();
    private String oldName1 = new String();
    private String newName = new String();
    private String newName1 = new String();
    
    private AdministradorCartografiaClient administradorCartografiaClient = null;

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck());

    }

    public String getName()
    {
        return "EIEL Save Map";
    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {

        reportNothingToUndoYet(context);

        int thumbSizeX = Integer.parseInt(aplicacion.getString("thumbSizeX"));
        int thumbSizeY = Integer.parseInt(aplicacion.getString("thumbSizeY"));

        if (!localSave)
        {

            dataBaseSave(context, administradorCartografiaClient, thumbSizeX, thumbSizeY, monitor);
			return;
        } else
        {

            localSave(context, thumbSizeX, thumbSizeY, monitor);
        }
        

    }

    /**
	 * @param context
	 * @param thumbSizeX
	 * @param thumbSizeY
	 * @param monitor
	 * @throws IOException
	 */
	private void localSave(PlugInContext context, int thumbSizeX, int thumbSizeY, TaskMonitor monitor) throws IOException
	{
		String dirBase = aplicacion.getUserPreference(
		        AppContext.PREFERENCES_DATA_PATH_KEY, AppContext.DEFAULT_DATA_PATH,
		        true);

		String mapName = context.getTask().getName();

		GeopistaFunctionUtils.makeMapBackup(dirBase, mapName);

		try
		{
		    boolean currentLayerIsLocalLayer = false;		    
		    File dirBaseMake = null;
		    File dirBaseMake_ = null;
		    File dirBaseMake1 = null;
		    File dirBaseMake_1 = null;
		    
		    if (context.getTask() instanceof GeopistaMap)
		    {		    	
		        dirBaseMake = new File(dirBase, mapName+"."+AppContext.getIdMunicipio());
		        dirBaseMake_ = new File(dirBase, mapName+"."+0);
		        dirBaseMake1 = new File(dirBase, mapName+"."+0);
		        dirBaseMake_1 = new File(dirBase, mapName+"."+AppContext.getIdMunicipio());
		    } 
		    
		  //Se comprueba si el directorio local ya existe. En ese caso pregunta al usuario si 
            //desea conservarlo o sobreescribirlo
            while (dirBaseMake.exists() && !((GeopistaMap)context.getTask()).isExtracted())
            {
            	String string1 = aplicacion.getI18nString("SaveMapPlugin.otronombre"); 
                String string2 = aplicacion.getI18nString("SaveMapPlugin.sobreescribir"); 
                String string3 = aplicacion.getI18nString("EIELSaveMapPlugIn.CancelarGrabacion"); 
                Object[] options = {string1, string2, string3};
                
                int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                        aplicacion.getI18nString("SaveMapPlugin.mensaje.yaexiste"),
                        "",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
                
                //otro nombre
                if (n==JOptionPane.YES_OPTION) 
                {
                    GeopistaMapPropertiesDialog propertiesMapDialog = new GeopistaMapPropertiesDialog(context);
                    String nombreMapa = ((GeopistaMap) context.getTask()).getName();                    
                    dirBaseMake = new File(dirBase, nombreMapa+"."+AppContext.getIdMunicipio());
                    dirBaseMake_ = new File(dirBase, nombreMapa+"."+0);                   
                    newName=nombreMapa;
                    contador=1;
                }
                //sobreescribir
                else if (n==JOptionPane.NO_OPTION)
                {
                	deleteDir(dirBaseMake);                	
                }
                else if (n==JOptionPane.CANCEL_OPTION)
                {
                    if (oldName!=null && context.getWorkbenchContext().getTask() instanceof GeopistaMap)
                        ((GeopistaMap)context.getWorkbenchContext().getTask()).setName(oldName);
                    return;
                }
            }       
            
            while (dirBaseMake1.exists() && !((GeopistaMap)context.getTask()).isExtracted())
            {
            	String string1 = aplicacion.getI18nString("SaveMapPlugin.otronombre"); 
                String string2 = aplicacion.getI18nString("SaveMapPlugin.sobreescribir"); 
                String string3 = aplicacion.getI18nString("EIELSaveMapPlugIn.CancelarGrabacion"); 
                Object[] options = {string1, string2, string3};
                
                int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                        aplicacion.getI18nString("SaveMapPlugin.mensaje.yaexiste"),
                        "",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
                
                //otro nombre
                if (n==JOptionPane.YES_OPTION) 
                {
                    GeopistaMapPropertiesDialog propertiesMapDialog = new GeopistaMapPropertiesDialog(context);
                    String nombreMapa = ((GeopistaMap) context.getTask()).getName();                    
                    dirBaseMake1 = new File(dirBase, nombreMapa+"."+0);
                    dirBaseMake_1 = new File(dirBase, nombreMapa+"."+AppContext.getIdMunicipio());
                    newName1=nombreMapa;
                    contador=2;
                }
                //sobreescribir
                else if (n==JOptionPane.NO_OPTION)
                {
                	contador=0;
                	deleteDir(dirBaseMake1);                	
                    }
                else if (n==JOptionPane.CANCEL_OPTION)
                {
                    if (oldName1!=null && context.getWorkbenchContext().getTask() instanceof GeopistaMap)
                        ((GeopistaMap)context.getWorkbenchContext().getTask()).setName(oldName1);
                    return;
                }
            }   
            
            while (dirBaseMake.exists() && ((GeopistaMap)context.getTask()).isExtracted())
            {
            	String string1 = aplicacion.getI18nString("SaveMapPlugin.otronombre"); 
                String string2 = aplicacion.getI18nString("SaveMapPlugin.sobreescribir"); 
                String string3 = aplicacion.getI18nString("EIELSaveMapPlugIn.CancelarGrabacion"); 
                Object[] options = {string1, string2, string3};
                
                int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                        aplicacion.getI18nString("SaveMapPlugin.mensaje.yaexiste"),
                        "",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
                
                //otro nombre
                if (n==JOptionPane.YES_OPTION) 
                {
                    GeopistaMapPropertiesDialog propertiesMapDialog = new GeopistaMapPropertiesDialog(context);
                    String nombreMapa = ((GeopistaMap) context.getTask()).getName();                    
                    dirBaseMake = new File(dirBase, nombreMapa+"."+AppContext.getIdMunicipio());
                    dirBaseMake_ = new File(dirBase, nombreMapa+"."+0);                   
                    newName=nombreMapa;
                    contador=1;
                }
                //sobreescribir
                else if (n==JOptionPane.NO_OPTION)
                {
                	deleteDir(dirBaseMake);                	
                }
                else if (n==JOptionPane.CANCEL_OPTION)
                {
                    if (oldName!=null && context.getWorkbenchContext().getTask() instanceof GeopistaMap)
                        ((GeopistaMap)context.getWorkbenchContext().getTask()).setName(oldName);
                    return;
                }
            }       
            
            while (dirBaseMake1.exists() && ((GeopistaMap)context.getTask()).isExtracted())
            {
            	String string1 = aplicacion.getI18nString("SaveMapPlugin.otronombre"); 
                String string2 = aplicacion.getI18nString("SaveMapPlugin.sobreescribir"); 
                String string3 = aplicacion.getI18nString("EIELSaveMapPlugIn.CancelarGrabacion"); 
                Object[] options = {string1, string2, string3};
                
                int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                        aplicacion.getI18nString("SaveMapPlugin.mensaje.yaexiste"),
                        "",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
                
                //otro nombre
                if (n==JOptionPane.YES_OPTION) 
                {
                    GeopistaMapPropertiesDialog propertiesMapDialog = new GeopistaMapPropertiesDialog(context);
                    String nombreMapa = ((GeopistaMap) context.getTask()).getName();                    
                    dirBaseMake1 = new File(dirBase, nombreMapa+"."+0);
                    dirBaseMake_1 = new File(dirBase, nombreMapa+"."+AppContext.getIdMunicipio());
                    newName1=nombreMapa;
                    contador=2;
                }
                //sobreescribir
                else if (n==JOptionPane.NO_OPTION)
                {
                	contador=0;
                	deleteDir(dirBaseMake1);                	
                    }
                else if (n==JOptionPane.CANCEL_OPTION)
                {
                    if (oldName1!=null && context.getWorkbenchContext().getTask() instanceof GeopistaMap)
                        ((GeopistaMap)context.getWorkbenchContext().getTask()).setName(oldName1);
                    return;
                }
            }   
            
            //Se pregunta si se quiere guardar como mapa de sistema o mapa normal.
            while ((!dirBaseMake.exists() && !dirBaseMake1.exists() && (!dirBaseMake_.exists() || !dirBaseMake_1.exists())) && !((GeopistaMap)context.getTask()).isExtracted())
            {
                String string1 = aplicacion.getI18nString("SaveMapPlugin.mapasistema"); 
                String string2 = aplicacion.getI18nString("SaveMapPlugin.mapanormal"); 
                String string3 = aplicacion.getI18nString("EIELSaveMapPlugIn.CancelarGrabacion"); 
                Object[] options = {string1, string2, string3};
                
                int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                		aplicacion.getI18nString("SaveMapPlugin.mensaje.tipo"),
                        "",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
                
                //mapa de sistema          
                
                if (n==JOptionPane.YES_OPTION) 
                {
                	//Pasar de normal a sistema
                	if (contador==1){
                		dirBaseMake_.mkdirs();

            		    context.getTask()
            		            .setProjectFile(new File(dirBaseMake_, "geopistamap.gpc"));
                        		    
            		    if(context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setSystemMap(false);       
            		        ((GeopistaMap) context.getTask()).setIdMunicipio(0);
            		        String systemId = ((GeopistaMap) context.getTask()).getSystemId();
            		        if(systemId!=null && !systemId.startsWith("l") && !systemId.startsWith("e")) 
                                systemId= "l" + systemId;
            		        ((GeopistaMap) context.getTask()).setSystemId(systemId);                    
            		    }            
                       
                        GeometryFactory factory = new GeometryFactory();
                        com.vividsolutions.jts.geom.Geometry geometryEnvelope = null;
                        geometryEnvelope = factory.toGeometry(context.getLayerManager().getEnvelopeOfAllLayers());
                        WKTWriter wktWriter = new WKTWriter();
                        ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
                                    
                        
            		    List layers = context.getLayerManager().getLayers();
            		    Iterator layersIter = layers.iterator();
            		    while (layersIter.hasNext())
            		    {
            		        Layer currentLayer = (Layer) layersIter.next();
            		        monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
            		                + currentLayer.getName());
                            
                            
                            if(currentLayer instanceof GeopistaLayer)
            		        {
            		            currentLayerIsLocalLayer = new Boolean(((GeopistaLayer)currentLayer).isLocal()).booleanValue();
            		            ((GeopistaLayer)currentLayer).setLocal(true);  
           		            
            		            //guardamos el log almacenado para cada una de las capas
            		            LogFeatutesEvents logFeatutesEvents = ((GeopistaLayer)currentLayer).getLogger();
            		            if(logFeatutesEvents!=null)
            		            {   
                                    logFeatutesEvents.setLogFileName(null);
            		                logFeatutesEvents.save();
            		            }
            		            currentLayer.setFeatureCollectionModified(false);
            			        if(currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
            			        {
            			            SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer.getStyle(SLDStyle.class);
            			            sldStyle.setPermanentChanged(false);
            			        }
            		        }
            		       /** 
            		        * Graba el esquema serializado
            		        */
            		        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();

            		        File saveSchemaLayer = new File(dirBaseMake_, currentLayer.getName()
            		                + ".sch");
            		        if (featureSchema instanceof GeopistaSchema)
            		        {
            		            StringWriter stringWriterSch = new StringWriter();
            		            try
            		            {
            		                Java2XML converter = new Java2XML();
            		                converter.write(featureSchema, "GeopistaSchema",
            		                        stringWriterSch);

            		                saveSchema(stringWriterSch, saveSchemaLayer);

            		            } finally
            		            {
            		                stringWriterSch.flush();
            		                stringWriterSch.close();
            		            }
            		        }
                            /**
                             * Graba la información de la capa en su DataSource
                             */
            		        DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

            		        if (dataSourceQuery == null || !currentLayerIsLocalLayer )
            		        {
                                GeoGML newDatasource = new GeoGML();
            		            dataSourceQuery = new DataSourceQuery();

            		            HashMap properties = new HashMap();
            		            
            		            File saveFileLayer = new File(dirBaseMake_, currentLayer.getName()
            		                    + ".gml");
            		            properties.put(DataSource.FILE_KEY, saveFileLayer
            		                    .getAbsolutePath());
                               
            		            properties.put(DataSource.COORDINATE_SYSTEM_KEY, context
            		                    .getTask().getLayerManager().getCoordinateSystem()
            		                    .getName());

            		            newDatasource.setProperties(properties);
            		            dataSourceQuery.setDataSource(newDatasource);

            		        }
                            
            		        Connection connection = dataSourceQuery.getDataSource()
            		                .getConnection();
                            

            		        try
            		        {
            		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
            		                    .getFeatureCollectionWrapper(), monitor);
            		            currentLayer.setDataSourceQuery(dataSourceQuery)
            		                    .setFeatureCollectionModified(false);
            		        } finally
            		        {
            		            connection.close();
            		        }

            		    }

            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            		    if (context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setTimeStamp(new Date());
            		        if (((GeopistaMap) context.getTask()).getDescription() == null)
            		        {
            		            ((GeopistaMap) context.getTask()).setDescription("");
            		        }
            		        saveISO88591((GeopistaMap) context.getTask(), context.getTask()
            		                .getProjectFile(), context.getWorkbenchGuiComponent());
            		    } else
            		    {
            		        saveTask(context.getTask(), context.getTask().getProjectFile(),
            		                context.getWorkbenchGuiComponent());
            		    }

            		    try
            		    {
            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.CreatingThumbnail"));

            		        ILayerViewPanel layerViewPanel = context.getLayerViewPanel();
            		        Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
            		                layerViewPanel);
            		        File thumbnailFile = new File(dirBaseMake_, "thumb.png");
            		        ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            		    } catch (Exception e)
            		    {
            				logger.error("localSave(PlugInContext, int, int, TaskMonitor)",
            						e);
            		    }
            		    
            		    //Una vez grabado ponemos el flag isDirty a false para saber que ya no hay cambios sin grabar
            		    if(context.getTask().getLayerManager() instanceof GeopistaLayerManager)
            		    {
            		        ((GeopistaLayerManager) context.getTask().getLayerManager()).setDirty(false);
            		    }		    
            		    
                        updateTitleToNoModified(context.getTask().getLayerManager(),context.getTask().getTaskComponent());
                	}
                	else
                	{
                		
                		dirBaseMake1.mkdirs();        		    

            		    context.getTask()
            		            .setProjectFile(new File(dirBaseMake1, "geopistamap.gpc"));
                        		    
            		    if(context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setSystemMap(false);
            		        ((GeopistaMap) context.getTask()).setIdMunicipio(0);
            		        String systemId = ((GeopistaMap) context.getTask()).getSystemId();
            		        if(systemId!=null && !systemId.startsWith("l") && !systemId.startsWith("e")) 
                                systemId= "l" + systemId;
            		        ((GeopistaMap) context.getTask()).setSystemId(systemId);                    
            		    }            
                       
                        GeometryFactory factory = new GeometryFactory();
                        com.vividsolutions.jts.geom.Geometry geometryEnvelope = null;
                        geometryEnvelope = factory.toGeometry(context.getLayerManager().getEnvelopeOfAllLayers());
                        WKTWriter wktWriter = new WKTWriter();
                        ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
                                    
                        
            		    List layers = context.getLayerManager().getLayers();
            		    Iterator layersIter = layers.iterator();
            		    while (layersIter.hasNext())
            		    {
            		        Layer currentLayer = (Layer) layersIter.next();
            		        monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
            		                + currentLayer.getName());
                            
                            
                            if(currentLayer instanceof GeopistaLayer)
            		        {
            		            currentLayerIsLocalLayer = new Boolean(((GeopistaLayer)currentLayer).isLocal()).booleanValue();
            		            ((GeopistaLayer)currentLayer).setLocal(true);  
            		            
            		            //guardamos el log almacenado para cada una de las capas
            		            LogFeatutesEvents logFeatutesEvents = ((GeopistaLayer)currentLayer).getLogger();
            		            if(logFeatutesEvents!=null)
            		            {   
                                    logFeatutesEvents.setLogFileName(null);
            		                logFeatutesEvents.save();
            		            }
            		            currentLayer.setFeatureCollectionModified(false);
            			        if(currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
            			        {
            			            SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer.getStyle(SLDStyle.class);
            			            sldStyle.setPermanentChanged(false);
            			        }
            		        }
            		       /** 
            		        * Graba el esquema serializado
            		        */
            		        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();

            		        File saveSchemaLayer = new File(dirBaseMake1, currentLayer.getName()
            		                + ".sch");
            		        if (featureSchema instanceof GeopistaSchema)
            		        {
            		            StringWriter stringWriterSch = new StringWriter();
            		            try
            		            {
            		                Java2XML converter = new Java2XML();
            		                converter.write(featureSchema, "GeopistaSchema",
            		                        stringWriterSch);

            		                saveSchema(stringWriterSch, saveSchemaLayer);

            		            } finally
            		            {
            		                stringWriterSch.flush();
            		                stringWriterSch.close();
            		            }
            		        }
                            /**
                             * Graba la información de la capa en su DataSource
                             */
            		        DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

            		        if (dataSourceQuery == null || !currentLayerIsLocalLayer )
            		        {
                                GeoGML newDatasource = new GeoGML();
            		            dataSourceQuery = new DataSourceQuery();

            		            HashMap properties = new HashMap();
            		            
            		            File saveFileLayer = new File(dirBaseMake1, currentLayer.getName()
            		                    + ".gml");
            		            properties.put(DataSource.FILE_KEY, saveFileLayer
            		                    .getAbsolutePath());
                               
            		            properties.put(DataSource.COORDINATE_SYSTEM_KEY, context
            		                    .getTask().getLayerManager().getCoordinateSystem()
            		                    .getName());

            		            newDatasource.setProperties(properties);
            		            dataSourceQuery.setDataSource(newDatasource);

            		        }
                            
            		        Connection connection = dataSourceQuery.getDataSource()
            		                .getConnection();
                            

            		        try
            		        {
            		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
            		                    .getFeatureCollectionWrapper(), monitor);
            		            currentLayer.setDataSourceQuery(dataSourceQuery)
            		                    .setFeatureCollectionModified(false);
            		        } finally
            		        {
            		            connection.close();
            		        }

            		    }

            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            		    if (context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setTimeStamp(new Date());
            		        if (((GeopistaMap) context.getTask()).getDescription() == null)
            		        {
            		            ((GeopistaMap) context.getTask()).setDescription("");
            		        }
            		        saveISO88591((GeopistaMap) context.getTask(), context.getTask()
            		                .getProjectFile(), context.getWorkbenchGuiComponent());
            		    } else
            		    {
            		        saveTask(context.getTask(), context.getTask().getProjectFile(),
            		                context.getWorkbenchGuiComponent());
            		    }

            		    try
            		    {
            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.CreatingThumbnail"));

            		        ILayerViewPanel layerViewPanel = context.getLayerViewPanel();
            		        Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
            		                layerViewPanel);
            		        File thumbnailFile = new File(dirBaseMake1, "thumb.png");
            		        ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            		    } catch (Exception e)
            		    {
            				logger.error("localSave(PlugInContext, int, int, TaskMonitor)",
            						e);
            		    }
            		    
            		    //Una vez grabado ponemos el flag isDirty a false para saber que ya no hay cambios sin grabar
            		    if(context.getTask().getLayerManager() instanceof GeopistaLayerManager)
            		    {
            		        ((GeopistaLayerManager) context.getTask().getLayerManager()).setDirty(false);
            		    }		    
            		    
                        updateTitleToNoModified(context.getTask().getLayerManager(),context.getTask().getTaskComponent());
                	}                	
                }	
                //mapa normal                 
                else if (n==JOptionPane.NO_OPTION)
                {
                	if (contador==2){
                		dirBaseMake_1.mkdirs();        		    

            		    context.getTask()
            		            .setProjectFile(new File(dirBaseMake_1, "geopistamap.gpc"));
                        		    
            		    if(context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setSystemMap(false);
            		        String systemId = ((GeopistaMap) context.getTask()).getSystemId();
            		        ((GeopistaMap) context.getTask()).setIdMunicipio(aplicacion.getIdMunicipio());
            		        if(systemId!=null && !systemId.startsWith("l") && !systemId.startsWith("e")) 
                                systemId= "l" + systemId;
            		        ((GeopistaMap) context.getTask()).setSystemId(systemId);                    
            		    }            
                       
                        GeometryFactory factory = new GeometryFactory();
                        com.vividsolutions.jts.geom.Geometry geometryEnvelope = null;
                        geometryEnvelope = factory.toGeometry(context.getLayerManager().getEnvelopeOfAllLayers());
                        WKTWriter wktWriter = new WKTWriter();
                        ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
                                    
                        
            		    List layers = context.getLayerManager().getLayers();
            		    Iterator layersIter = layers.iterator();
            		    while (layersIter.hasNext())
            		    {
            		        Layer currentLayer = (Layer) layersIter.next();
            		        monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
            		                + currentLayer.getName());
                            
                            
                            if(currentLayer instanceof GeopistaLayer)
            		        {
            		            currentLayerIsLocalLayer = new Boolean(((GeopistaLayer)currentLayer).isLocal()).booleanValue();
            		            ((GeopistaLayer)currentLayer).setLocal(true);  
            		            
            		            //guardamos el log almacenado para cada una de las capas
            		            LogFeatutesEvents logFeatutesEvents = ((GeopistaLayer)currentLayer).getLogger();
            		            if(logFeatutesEvents!=null)
            		            {   
                                    logFeatutesEvents.setLogFileName(null);
            		                logFeatutesEvents.save();
            		            }
            		            currentLayer.setFeatureCollectionModified(false);
            			        if(currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
            			        {
            			            SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer.getStyle(SLDStyle.class);
            			            sldStyle.setPermanentChanged(false);
            			        }
            		        }
            		       /** 
            		        * Graba el esquema serializado
            		        */
            		        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();

            		        File saveSchemaLayer = new File(dirBaseMake_1, currentLayer.getName()
            		                + ".sch");
            		        if (featureSchema instanceof GeopistaSchema)
            		        {
            		            StringWriter stringWriterSch = new StringWriter();
            		            try
            		            {
            		                Java2XML converter = new Java2XML();
            		                converter.write(featureSchema, "GeopistaSchema",
            		                        stringWriterSch);

            		                saveSchema(stringWriterSch, saveSchemaLayer);

            		            } finally
            		            {
            		                stringWriterSch.flush();
            		                stringWriterSch.close();
            		            }
            		        }
                            /**
                             * Graba la información de la capa en su DataSource
                             */
            		        DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

            		        if (dataSourceQuery == null || !currentLayerIsLocalLayer )
            		        {
                                GeoGML newDatasource = new GeoGML();
            		            dataSourceQuery = new DataSourceQuery();

            		            HashMap properties = new HashMap();
            		            
            		            File saveFileLayer = new File(dirBaseMake_1, currentLayer.getName()
            		                    + ".gml");
            		            properties.put(DataSource.FILE_KEY, saveFileLayer
            		                    .getAbsolutePath());
                               
            		            properties.put(DataSource.COORDINATE_SYSTEM_KEY, context
            		                    .getTask().getLayerManager().getCoordinateSystem()
            		                    .getName());

            		            newDatasource.setProperties(properties);
            		            dataSourceQuery.setDataSource(newDatasource);

            		        }
                            
            		        Connection connection = dataSourceQuery.getDataSource()
            		                .getConnection();
                            

            		        try
            		        {
            		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
            		                    .getFeatureCollectionWrapper(), monitor);
            		            currentLayer.setDataSourceQuery(dataSourceQuery)
            		                    .setFeatureCollectionModified(false);
            		        } finally
            		        {
            		            connection.close();
            		        }

            		    }

            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            		    if (context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setTimeStamp(new Date());
            		        if (((GeopistaMap) context.getTask()).getDescription() == null)
            		        {
            		            ((GeopistaMap) context.getTask()).setDescription("");
            		        }
            		        saveISO88591((GeopistaMap) context.getTask(), context.getTask()
            		                .getProjectFile(), context.getWorkbenchGuiComponent());
            		    } else
            		    {
            		        saveTask(context.getTask(), context.getTask().getProjectFile(),
            		                context.getWorkbenchGuiComponent());
            		    }

            		    try
            		    {
            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.CreatingThumbnail"));

            		        ILayerViewPanel layerViewPanel = context.getLayerViewPanel();
            		        Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
            		                layerViewPanel);
            		        File thumbnailFile = new File(dirBaseMake_1, "thumb.png");
            		        ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            		    } catch (Exception e)
            		    {
            				logger.error("localSave(PlugInContext, int, int, TaskMonitor)",
            						e);
            		    }
            		    
            		    //Una vez grabado ponemos el flag isDirty a false para saber que ya no hay cambios sin grabar
            		    if(context.getTask().getLayerManager() instanceof GeopistaLayerManager)
            		    {
            		        ((GeopistaLayerManager) context.getTask().getLayerManager()).setDirty(false);
            		    }		    
            		    
                        updateTitleToNoModified(context.getTask().getLayerManager(),context.getTask().getTaskComponent());
                	}else{
                		
                		dirBaseMake.mkdirs();

            		    context.getTask()
            		            .setProjectFile(new File(dirBaseMake, "geopistamap.gpc"));
                        		    
            		    if(context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setSystemMap(false);        		        
            		        String systemId = ((GeopistaMap) context.getTask()).getSystemId();
            		        if(systemId!=null && !systemId.startsWith("l") && !systemId.startsWith("e")) 
                                systemId= "l" + systemId;
            		        ((GeopistaMap) context.getTask()).setSystemId(systemId);                    
            		    }            
                       
                        GeometryFactory factory = new GeometryFactory();
                        com.vividsolutions.jts.geom.Geometry geometryEnvelope = null;
                        geometryEnvelope = factory.toGeometry(context.getLayerManager().getEnvelopeOfAllLayers());
                        WKTWriter wktWriter = new WKTWriter();
                        ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
                                    
                        
            		    List layers = context.getLayerManager().getLayers();
            		    Iterator layersIter = layers.iterator();
            		    while (layersIter.hasNext())
            		    {
            		        Layer currentLayer = (Layer) layersIter.next();
            		        monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
            		                + currentLayer.getName());
                            
                            
                            if(currentLayer instanceof GeopistaLayer)
            		        {
            		            currentLayerIsLocalLayer = new Boolean(((GeopistaLayer)currentLayer).isLocal()).booleanValue();
            		            ((GeopistaLayer)currentLayer).setLocal(true);  
            		            
            		            //guardamos el log almacenado para cada una de las capas
            		            LogFeatutesEvents logFeatutesEvents = ((GeopistaLayer)currentLayer).getLogger();
            		            if(logFeatutesEvents!=null)
            		            {   
                                    logFeatutesEvents.setLogFileName(null);
            		                logFeatutesEvents.save();
            		            }
            		            currentLayer.setFeatureCollectionModified(false);
            			        if(currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
            			        {
            			            SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer.getStyle(SLDStyle.class);
            			            sldStyle.setPermanentChanged(false);
            			        }
            		        }
            		       /** 
            		        * Graba el esquema serializado
            		        */
            		        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();

            		        File saveSchemaLayer = new File(dirBaseMake, currentLayer.getName()
            		                + ".sch");
            		        if (featureSchema instanceof GeopistaSchema)
            		        {
            		            StringWriter stringWriterSch = new StringWriter();
            		            try
            		            {
            		                Java2XML converter = new Java2XML();
            		                converter.write(featureSchema, "GeopistaSchema",
            		                        stringWriterSch);

            		                saveSchema(stringWriterSch, saveSchemaLayer);

            		            } finally
            		            {
            		                stringWriterSch.flush();
            		                stringWriterSch.close();
            		            }
            		        }
                            /**
                             * Graba la información de la capa en su DataSource
                             */
            		        DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

            		        if (dataSourceQuery == null || !currentLayerIsLocalLayer )
            		        {
                                GeoGML newDatasource = new GeoGML();
            		            dataSourceQuery = new DataSourceQuery();

            		            HashMap properties = new HashMap();
            		            
            		            File saveFileLayer = new File(dirBaseMake, currentLayer.getName()
            		                    + ".gml");
            		            properties.put(DataSource.FILE_KEY, saveFileLayer
            		                    .getAbsolutePath());
                               
            		            properties.put(DataSource.COORDINATE_SYSTEM_KEY, context
            		                    .getTask().getLayerManager().getCoordinateSystem()
            		                    .getName());

            		            newDatasource.setProperties(properties);
            		            dataSourceQuery.setDataSource(newDatasource);

            		        }
                            
            		        Connection connection = dataSourceQuery.getDataSource()
            		                .getConnection();
                            

            		        try
            		        {
            		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
            		                    .getFeatureCollectionWrapper(), monitor);
            		            currentLayer.setDataSourceQuery(dataSourceQuery)
            		                    .setFeatureCollectionModified(false);
            		        } finally
            		        {
            		            connection.close();
            		        }

            		    }

            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            		    if (context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setTimeStamp(new Date());
            		        if (((GeopistaMap) context.getTask()).getDescription() == null)
            		        {
            		            ((GeopistaMap) context.getTask()).setDescription("");
            		        }
            		        saveISO88591((GeopistaMap) context.getTask(), context.getTask()
            		                .getProjectFile(), context.getWorkbenchGuiComponent());
            		    } else
            		    {
            		        saveTask(context.getTask(), context.getTask().getProjectFile(),
            		                context.getWorkbenchGuiComponent());
            		    }

            		    try
            		    {
            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.CreatingThumbnail"));

            		        ILayerViewPanel layerViewPanel = context.getLayerViewPanel();
            		        Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
            		                layerViewPanel);
            		        File thumbnailFile = new File(dirBaseMake, "thumb.png");
            		        ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            		    } catch (Exception e)
            		    {
            				logger.error("localSave(PlugInContext, int, int, TaskMonitor)",
            						e);
            		    }
            		    
            		    //Una vez grabado ponemos el flag isDirty a false para saber que ya no hay cambios sin grabar
            		    if(context.getTask().getLayerManager() instanceof GeopistaLayerManager)
            		    {
            		        ((GeopistaLayerManager) context.getTask().getLayerManager()).setDirty(false);
            		    }		    
            		    
                        updateTitleToNoModified(context.getTask().getLayerManager(),context.getTask().getTaskComponent());
                	}                	
                }
                else if (n==JOptionPane.CANCEL_OPTION)
                {
                	return;
                }            
            }
            
            while ((!dirBaseMake.exists() && !dirBaseMake1.exists() && (!dirBaseMake_.exists() || !dirBaseMake_1.exists())) && ((GeopistaMap)context.getTask()).isExtracted())
            {
                String string1 = aplicacion.getI18nString("SaveMapPlugin.mapasistema"); 
                String string2 = aplicacion.getI18nString("SaveMapPlugin.mapanormal"); 
                String string3 = aplicacion.getI18nString("EIELSaveMapPlugIn.CancelarGrabacion"); 
                Object[] options = {string1, string2, string3};
                
                int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                		aplicacion.getI18nString("SaveMapPlugin.mensaje.tipo"),
                        "",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
                
                //mapa de sistema          
                
                if (n==JOptionPane.YES_OPTION) 
                {
                	//Pasar de normal a sistema
                	if (contador==1){
                		dirBaseMake_.mkdirs();

            		    context.getTask()
            		            .setProjectFile(new File(dirBaseMake_, "geopistamap.gpc"));
                        		    
            		    if(context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setSystemMap(false);       
            		        ((GeopistaMap) context.getTask()).setIdMunicipio(0);
            		        String systemId = ((GeopistaMap) context.getTask()).getSystemId();
            		        if(systemId!=null && !systemId.startsWith("l") && !systemId.startsWith("e")) 
                                systemId= "l" + systemId;
            		        ((GeopistaMap) context.getTask()).setSystemId(systemId);                    
            		    }            
                       
                        GeometryFactory factory = new GeometryFactory();
                        com.vividsolutions.jts.geom.Geometry geometryEnvelope = null;
                        geometryEnvelope = factory.toGeometry(context.getLayerManager().getEnvelopeOfAllLayers());
                        WKTWriter wktWriter = new WKTWriter();
                        ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
                                    
                        
            		    List layers = context.getLayerManager().getLayers();
            		    Iterator layersIter = layers.iterator();
            		    while (layersIter.hasNext())
            		    {
            		        Layer currentLayer = (Layer) layersIter.next();
            		        monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
            		                + currentLayer.getName());
                            
                            
                            if(currentLayer instanceof GeopistaLayer)
            		        {
            		            currentLayerIsLocalLayer = new Boolean(((GeopistaLayer)currentLayer).isLocal()).booleanValue();
            		            ((GeopistaLayer)currentLayer).setLocal(true);  
            		            
            		            //guardamos el log almacenado para cada una de las capas
            		            LogFeatutesEvents logFeatutesEvents = ((GeopistaLayer)currentLayer).getLogger();
            		            if(logFeatutesEvents!=null)
            		            {   
                                    logFeatutesEvents.setLogFileName(null);
            		                logFeatutesEvents.save();
            		            }
            		            currentLayer.setFeatureCollectionModified(false);
            			        if(currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
            			        {
            			            SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer.getStyle(SLDStyle.class);
            			            sldStyle.setPermanentChanged(false);
            			        }
            		        }
            		       /** 
            		        * Graba el esquema serializado
            		        */
            		        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();

            		        File saveSchemaLayer = new File(dirBaseMake_, currentLayer.getName()
            		                + ".sch");
            		        if (featureSchema instanceof GeopistaSchema)
            		        {
            		            StringWriter stringWriterSch = new StringWriter();
            		            try
            		            {
            		                Java2XML converter = new Java2XML();
            		                converter.write(featureSchema, "GeopistaSchema",
            		                        stringWriterSch);

            		                saveSchema(stringWriterSch, saveSchemaLayer);

            		            } finally
            		            {
            		                stringWriterSch.flush();
            		                stringWriterSch.close();
            		            }
            		        }
                            /**
                             * Graba la información de la capa en su DataSource
                             */
            		        DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

            		        if (dataSourceQuery == null || !currentLayerIsLocalLayer )
            		        {
                                GeoGML newDatasource = new GeoGML();
            		            dataSourceQuery = new DataSourceQuery();

            		            HashMap properties = new HashMap();
            		            
            		            File saveFileLayer = new File(dirBaseMake_, currentLayer.getName()
            		                    + ".gml");
            		            properties.put(DataSource.FILE_KEY, saveFileLayer
            		                    .getAbsolutePath());
                               
            		            properties.put(DataSource.COORDINATE_SYSTEM_KEY, context
            		                    .getTask().getLayerManager().getCoordinateSystem()
            		                    .getName());

            		            newDatasource.setProperties(properties);
            		            dataSourceQuery.setDataSource(newDatasource);

            		        }
                            
            		        Connection connection = dataSourceQuery.getDataSource()
            		                .getConnection();
                            

            		        try
            		        {
            		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
            		                    .getFeatureCollectionWrapper(), monitor);
            		            currentLayer.setDataSourceQuery(dataSourceQuery)
            		                    .setFeatureCollectionModified(false);
            		        } finally
            		        {
            		            connection.close();
            		        }

            		    }

            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            		    if (context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setTimeStamp(new Date());
            		        if (((GeopistaMap) context.getTask()).getDescription() == null)
            		        {
            		            ((GeopistaMap) context.getTask()).setDescription("");
            		        }
            		        saveISO88591((GeopistaMap) context.getTask(), context.getTask()
            		                .getProjectFile(), context.getWorkbenchGuiComponent());
            		    } else
            		    {
            		        saveTask(context.getTask(), context.getTask().getProjectFile(),
            		                context.getWorkbenchGuiComponent());
            		    }

            		    try
            		    {
            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.CreatingThumbnail"));

            		        ILayerViewPanel layerViewPanel = context.getLayerViewPanel();
            		        Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
            		                layerViewPanel);
            		        File thumbnailFile = new File(dirBaseMake_, "thumb.png");
            		        ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            		    } catch (Exception e)
            		    {
            				logger.error("localSave(PlugInContext, int, int, TaskMonitor)",
            						e);
            		    }
            		    
            		    //Una vez grabado ponemos el flag isDirty a false para saber que ya no hay cambios sin grabar
            		    if(context.getTask().getLayerManager() instanceof GeopistaLayerManager)
            		    {
            		        ((GeopistaLayerManager) context.getTask().getLayerManager()).setDirty(false);
            		    }		    
            		    
                        updateTitleToNoModified(context.getTask().getLayerManager(),context.getTask().getTaskComponent());
                	}
                	else
                	{
                		
                		dirBaseMake1.mkdirs();        		    

            		    context.getTask()
            		            .setProjectFile(new File(dirBaseMake1, "geopistamap.gpc"));
                        		    
            		    if(context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setSystemMap(false);
            		        ((GeopistaMap) context.getTask()).setIdMunicipio(0);
            		        String systemId = ((GeopistaMap) context.getTask()).getSystemId();
            		        if(systemId!=null && !systemId.startsWith("l") && !systemId.startsWith("e")) 
                                systemId= "l" + systemId;
            		        ((GeopistaMap) context.getTask()).setSystemId(systemId);                    
            		    }            
                       
                        GeometryFactory factory = new GeometryFactory();
                        com.vividsolutions.jts.geom.Geometry geometryEnvelope = null;
                        geometryEnvelope = factory.toGeometry(context.getLayerManager().getEnvelopeOfAllLayers());
                        WKTWriter wktWriter = new WKTWriter();
                        ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
                                    
                        
            		    List layers = context.getLayerManager().getLayers();
            		    Iterator layersIter = layers.iterator();
            		    while (layersIter.hasNext())
            		    {
            		        Layer currentLayer = (Layer) layersIter.next();
            		        monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
            		                + currentLayer.getName());
                            
                            
                            if(currentLayer instanceof GeopistaLayer)
            		        {
            		            currentLayerIsLocalLayer = new Boolean(((GeopistaLayer)currentLayer).isLocal()).booleanValue();
            		            ((GeopistaLayer)currentLayer).setLocal(true);  
            		            
            		            //guardamos el log almacenado para cada una de las capas
            		            LogFeatutesEvents logFeatutesEvents = ((GeopistaLayer)currentLayer).getLogger();
            		            if(logFeatutesEvents!=null)
            		            {   
                                    logFeatutesEvents.setLogFileName(null);
            		                logFeatutesEvents.save();
            		            }
            		            currentLayer.setFeatureCollectionModified(false);
            			        if(currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
            			        {
            			            SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer.getStyle(SLDStyle.class);
            			            sldStyle.setPermanentChanged(false);
            			        }
            		        }
            		       /** 
            		        * Graba el esquema serializado
            		        */
            		        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();

            		        File saveSchemaLayer = new File(dirBaseMake1, currentLayer.getName()
            		                + ".sch");
            		        if (featureSchema instanceof GeopistaSchema)
            		        {
            		            StringWriter stringWriterSch = new StringWriter();
            		            try
            		            {
            		                Java2XML converter = new Java2XML();
            		                converter.write(featureSchema, "GeopistaSchema",
            		                        stringWriterSch);

            		                saveSchema(stringWriterSch, saveSchemaLayer);

            		            } finally
            		            {
            		                stringWriterSch.flush();
            		                stringWriterSch.close();
            		            }
            		        }
                            /**
                             * Graba la información de la capa en su DataSource
                             */
            		        DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

            		        if (dataSourceQuery == null || !currentLayerIsLocalLayer )
            		        {
                                GeoGML newDatasource = new GeoGML();
            		            dataSourceQuery = new DataSourceQuery();

            		            HashMap properties = new HashMap();
            		            
            		            File saveFileLayer = new File(dirBaseMake1, currentLayer.getName()
            		                    + ".gml");
            		            properties.put(DataSource.FILE_KEY, saveFileLayer
            		                    .getAbsolutePath());
                               
            		            properties.put(DataSource.COORDINATE_SYSTEM_KEY, context
            		                    .getTask().getLayerManager().getCoordinateSystem()
            		                    .getName());

            		            newDatasource.setProperties(properties);
            		            dataSourceQuery.setDataSource(newDatasource);

            		        }
                            
            		        Connection connection = dataSourceQuery.getDataSource()
            		                .getConnection();
                            

            		        try
            		        {
            		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
            		                    .getFeatureCollectionWrapper(), monitor);
            		            currentLayer.setDataSourceQuery(dataSourceQuery)
            		                    .setFeatureCollectionModified(false);
            		        } finally
            		        {
            		            connection.close();
            		        }

            		    }

            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            		    if (context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setTimeStamp(new Date());
            		        if (((GeopistaMap) context.getTask()).getDescription() == null)
            		        {
            		            ((GeopistaMap) context.getTask()).setDescription("");
            		        }
            		        saveISO88591((GeopistaMap) context.getTask(), context.getTask()
            		                .getProjectFile(), context.getWorkbenchGuiComponent());
            		    } else
            		    {
            		        saveTask(context.getTask(), context.getTask().getProjectFile(),
            		                context.getWorkbenchGuiComponent());
            		    }

            		    try
            		    {
            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.CreatingThumbnail"));

            		        ILayerViewPanel layerViewPanel = context.getLayerViewPanel();
            		        Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
            		                layerViewPanel);
            		        File thumbnailFile = new File(dirBaseMake1, "thumb.png");
            		        ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            		    } catch (Exception e)
            		    {
            				logger.error("localSave(PlugInContext, int, int, TaskMonitor)",
            						e);
            		    }
            		    
            		    //Una vez grabado ponemos el flag isDirty a false para saber que ya no hay cambios sin grabar
            		    if(context.getTask().getLayerManager() instanceof GeopistaLayerManager)
            		    {
            		        ((GeopistaLayerManager) context.getTask().getLayerManager()).setDirty(false);
            		    }		    
            		    
                        updateTitleToNoModified(context.getTask().getLayerManager(),context.getTask().getTaskComponent());
                	}                	
                }	
                //mapa normal                 
                else if (n==JOptionPane.NO_OPTION)
                {
                	if (contador==2){
                		dirBaseMake_1.mkdirs();        		    

            		    context.getTask()
            		            .setProjectFile(new File(dirBaseMake_1, "geopistamap.gpc"));
                        		    
            		    if(context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setSystemMap(false);
            		        String systemId = ((GeopistaMap) context.getTask()).getSystemId();
            		        ((GeopistaMap) context.getTask()).setIdMunicipio(aplicacion.getIdMunicipio());
            		        if(systemId!=null && !systemId.startsWith("l") && !systemId.startsWith("e")) 
                                systemId= "l" + systemId;
            		        ((GeopistaMap) context.getTask()).setSystemId(systemId);                    
            		    }            
                       
                        GeometryFactory factory = new GeometryFactory();
                        com.vividsolutions.jts.geom.Geometry geometryEnvelope = null;
                        geometryEnvelope = factory.toGeometry(context.getLayerManager().getEnvelopeOfAllLayers());
                        WKTWriter wktWriter = new WKTWriter();
                        ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
                                    
                        
            		    List layers = context.getLayerManager().getLayers();
            		    Iterator layersIter = layers.iterator();
            		    while (layersIter.hasNext())
            		    {
            		        Layer currentLayer = (Layer) layersIter.next();
            		        monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
            		                + currentLayer.getName());
                            
                            
                            if(currentLayer instanceof GeopistaLayer)
            		        {
            		            currentLayerIsLocalLayer = new Boolean(((GeopistaLayer)currentLayer).isLocal()).booleanValue();
            		            ((GeopistaLayer)currentLayer).setLocal(true);  
                                
            		            //guardamos el log almacenado para cada una de las capas
            		            LogFeatutesEvents logFeatutesEvents = ((GeopistaLayer)currentLayer).getLogger();
            		            if(logFeatutesEvents!=null)
            		            {   
                                    logFeatutesEvents.setLogFileName(null);
            		                logFeatutesEvents.save();
            		            }
            		            currentLayer.setFeatureCollectionModified(false);
            			        if(currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
            			        {
            			            SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer.getStyle(SLDStyle.class);
            			            sldStyle.setPermanentChanged(false);
            			        }
            		        }
            		       /** 
            		        * Graba el esquema serializado
            		        */
            		        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();

            		        File saveSchemaLayer = new File(dirBaseMake_1, currentLayer.getName()
            		                + ".sch");
            		        if (featureSchema instanceof GeopistaSchema)
            		        {
            		            StringWriter stringWriterSch = new StringWriter();
            		            try
            		            {
            		                Java2XML converter = new Java2XML();
            		                converter.write(featureSchema, "GeopistaSchema",
            		                        stringWriterSch);

            		                saveSchema(stringWriterSch, saveSchemaLayer);

            		            } finally
            		            {
            		                stringWriterSch.flush();
            		                stringWriterSch.close();
            		            }
            		        }
                            /**
                             * Graba la información de la capa en su DataSource
                             */
            		        DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

            		        if (dataSourceQuery == null || !currentLayerIsLocalLayer )
            		        {
                                GeoGML newDatasource = new GeoGML();
            		            dataSourceQuery = new DataSourceQuery();

            		            HashMap properties = new HashMap();
            		            
            		            File saveFileLayer = new File(dirBaseMake_1, currentLayer.getName()
            		                    + ".gml");
            		            properties.put(DataSource.FILE_KEY, saveFileLayer
            		                    .getAbsolutePath());
                               
            		            properties.put(DataSource.COORDINATE_SYSTEM_KEY, context
            		                    .getTask().getLayerManager().getCoordinateSystem()
            		                    .getName());

            		            newDatasource.setProperties(properties);
            		            dataSourceQuery.setDataSource(newDatasource);

            		        }
                            
            		        Connection connection = dataSourceQuery.getDataSource()
            		                .getConnection();
                            

            		        try
            		        {
            		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
            		                    .getFeatureCollectionWrapper(), monitor);
            		            currentLayer.setDataSourceQuery(dataSourceQuery)
            		                    .setFeatureCollectionModified(false);
            		        } finally
            		        {
            		            connection.close();
            		        }

            		    }

            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            		    if (context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setTimeStamp(new Date());
            		        if (((GeopistaMap) context.getTask()).getDescription() == null)
            		        {
            		            ((GeopistaMap) context.getTask()).setDescription("");
            		        }
            		        saveISO88591((GeopistaMap) context.getTask(), context.getTask()
            		                .getProjectFile(), context.getWorkbenchGuiComponent());
            		    } else
            		    {
            		        saveTask(context.getTask(), context.getTask().getProjectFile(),
            		                context.getWorkbenchGuiComponent());
            		    }

            		    try
            		    {
            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.CreatingThumbnail"));

            		        ILayerViewPanel layerViewPanel = context.getLayerViewPanel();
            		        Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
            		                layerViewPanel);
            		        File thumbnailFile = new File(dirBaseMake_1, "thumb.png");
            		        ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            		    } catch (Exception e)
            		    {
            				logger.error("localSave(PlugInContext, int, int, TaskMonitor)",
            						e);
            		    }
            		    
            		    //Una vez grabado ponemos el flag isDirty a false para saber que ya no hay cambios sin grabar
            		    if(context.getTask().getLayerManager() instanceof GeopistaLayerManager)
            		    {
            		        ((GeopistaLayerManager) context.getTask().getLayerManager()).setDirty(false);
            		    }		    
            		    
                        updateTitleToNoModified(context.getTask().getLayerManager(),context.getTask().getTaskComponent());
                	}else{
                		
                		dirBaseMake.mkdirs();

            		    context.getTask()
            		            .setProjectFile(new File(dirBaseMake, "geopistamap.gpc"));
                        		    
            		    if(context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setSystemMap(false);        		        
            		        String systemId = ((GeopistaMap) context.getTask()).getSystemId();
            		        if(systemId!=null && !systemId.startsWith("l") && !systemId.startsWith("e")) 
                                systemId= "l" + systemId;
            		        ((GeopistaMap) context.getTask()).setSystemId(systemId);                    
            		    }            
                       
                        GeometryFactory factory = new GeometryFactory();
                        com.vividsolutions.jts.geom.Geometry geometryEnvelope = null;
                        geometryEnvelope = factory.toGeometry(context.getLayerManager().getEnvelopeOfAllLayers());
                        WKTWriter wktWriter = new WKTWriter();
                        ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
                                    
                        
            		    List layers = context.getLayerManager().getLayers();
            		    Iterator layersIter = layers.iterator();
            		    while (layersIter.hasNext())
            		    {
            		        Layer currentLayer = (Layer) layersIter.next();
            		        monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
            		                + currentLayer.getName());
                            
                            
                            if(currentLayer instanceof GeopistaLayer)
            		        {
            		            currentLayerIsLocalLayer = new Boolean(((GeopistaLayer)currentLayer).isLocal()).booleanValue();
            		            ((GeopistaLayer)currentLayer).setLocal(true);  
            		            
            		            //guardamos el log almacenado para cada una de las capas
            		            LogFeatutesEvents logFeatutesEvents = ((GeopistaLayer)currentLayer).getLogger();
            		            if(logFeatutesEvents!=null)
            		            {   
                                    logFeatutesEvents.setLogFileName(null);
            		                logFeatutesEvents.save();
            		            }
            		            currentLayer.setFeatureCollectionModified(false);
            			        if(currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
            			        {
            			            SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer.getStyle(SLDStyle.class);
            			            sldStyle.setPermanentChanged(false);
            			        }
            		        }
            		       /** 
            		        * Graba el esquema serializado
            		        */
            		        FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper().getFeatureSchema();

            		        File saveSchemaLayer = new File(dirBaseMake, currentLayer.getName()
            		                + ".sch");
            		        if (featureSchema instanceof GeopistaSchema)
            		        {
            		            StringWriter stringWriterSch = new StringWriter();
            		            try
            		            {
            		                Java2XML converter = new Java2XML();
            		                converter.write(featureSchema, "GeopistaSchema",
            		                        stringWriterSch);

            		                saveSchema(stringWriterSch, saveSchemaLayer);

            		            } finally
            		            {
            		                stringWriterSch.flush();
            		                stringWriterSch.close();
            		            }
            		        }
                            /**
                             * Graba la información de la capa en su DataSource
                             */
            		        DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

            		        if (dataSourceQuery == null || !currentLayerIsLocalLayer )
            		        {
                                GeoGML newDatasource = new GeoGML();
            		            dataSourceQuery = new DataSourceQuery();

            		            HashMap properties = new HashMap();
            		            
            		            File saveFileLayer = new File(dirBaseMake, currentLayer.getName()
            		                    + ".gml");
            		            properties.put(DataSource.FILE_KEY, saveFileLayer
            		                    .getAbsolutePath());
                               
            		            properties.put(DataSource.COORDINATE_SYSTEM_KEY, context
            		                    .getTask().getLayerManager().getCoordinateSystem()
            		                    .getName());

            		            newDatasource.setProperties(properties);
            		            dataSourceQuery.setDataSource(newDatasource);

            		        }
                            
            		        Connection connection = dataSourceQuery.getDataSource()
            		                .getConnection();
                            

            		        try
            		        {
            		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
            		                    .getFeatureCollectionWrapper(), monitor);
            		            currentLayer.setDataSourceQuery(dataSourceQuery)
            		                    .setFeatureCollectionModified(false);
            		        } finally
            		        {
            		            connection.close();
            		        }

            		    }// bucle de capas

            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            		    if (context.getTask() instanceof GeopistaMap)
            		    {
            		        ((GeopistaMap) context.getTask()).setTimeStamp(new Date());
            		        if (((GeopistaMap) context.getTask()).getDescription() == null)
            		        {
            		            ((GeopistaMap) context.getTask()).setDescription("");
            		        }
            		        saveISO88591((GeopistaMap) context.getTask(), context.getTask()
            		                .getProjectFile(), context.getWorkbenchGuiComponent());
            		    } else
            		    {
            		        saveTask(context.getTask(), context.getTask().getProjectFile(),
            		                context.getWorkbenchGuiComponent());
            		    }

            		    try
            		    {
            		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.CreatingThumbnail"));

            		        ILayerViewPanel layerViewPanel = context.getLayerViewPanel();
            		        Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
            		                layerViewPanel);
            		        File thumbnailFile = new File(dirBaseMake, "thumb.png");
            		        ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            		    } catch (Exception e)
            		    {
            				logger.error("localSave(PlugInContext, int, int, TaskMonitor)",
            						e);
            		    }
            		    
            		    //Una vez grabado ponemos el flag isDirty a false para saber que ya no hay cambios sin grabar
            		    if(context.getTask().getLayerManager() instanceof GeopistaLayerManager)
            		    {
            		        ((GeopistaLayerManager) context.getTask().getLayerManager()).setDirty(false);
            		    }		    
            		    
                        updateTitleToNoModified(context.getTask().getLayerManager(),context.getTask().getTaskComponent());
                	}                	
                }
                else if (n==JOptionPane.CANCEL_OPTION)
                {
                	return;
                }            
            }
		} catch (Exception e)
		{
		    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
		            .getI18nString("ErrorAlGrabarMapa"), aplicacion
		            .getI18nString("ElMapaNoHaPodidoGrabarse"), StringUtil
		            .stackTrace(e));
		    monitor.report(aplicacion.getI18nString("RestaurandoCopiaDeSeguridad"));
		    GeopistaFunctionUtils.restoreBackup(dirBase, mapName);
		    

		}
		}

	/**
	 * @param context
	 * @param administradorCartografiaClient
	 * @param thumbSizeX
	 * @param thumbSizeY
	 * @param monitor
	 * @throws AclNotFoundException
	 * @throws Exception
	 * @throws ACException
	 */
	private void dataBaseSave(PlugInContext context, AdministradorCartografiaClient administradorCartografiaClient, int thumbSizeX, int thumbSizeY, TaskMonitor monitor) throws AclNotFoundException, Exception, ACException
	{
		
		if (!aplicacion.isLogged())
		{

		    aplicacion.setProfile("Geopista");
		    aplicacion.login();
		}
		if (aplicacion.isLogged())
		{
		    ILayerViewPanel layerViewPanel = (ILayerViewPanel) context.getWorkbenchContext()
		            .getLayerViewPanel();
		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));
		    Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
		            layerViewPanel);
		    GeopistaMap saveActualMap = (GeopistaMap) context.getTask();
		                	
		    saveActualMap.setSystemMap(true);
                  
		    if(!saveActualMap.isSystemMap())
		    {
		        saveActualMap.setSystemId("");
		        saveActualMap.setSystemMap(true);
		    }
		    
		    saveActualMap.setIdMunicipio(0); 
		    
		    saveActualMap.setThumbnail(thumbnail);
		    administradorCartografiaClient.saveMap(saveActualMap, aplicacion
		            .getUserPreference(AppContext.PREFERENCES_LOCALE_KEY, "es_ES",
		                    false));

		    List allLayers = saveActualMap.getLayerManager().getLayers();
		    Iterator allLayersIter = allLayers.iterator();
		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingStyles"));

		    if(saveActualMap.getLayerManager() instanceof GeopistaLayerManager)
		    {
		        ((GeopistaLayerManager) saveActualMap.getLayerManager()).setDirty(false);
		    }
		    while (allLayersIter.hasNext())
		    {
		        GeopistaLayer actualLayer = (GeopistaLayer) allLayersIter.next();
		        String systemId = actualLayer.getSystemId();
		        if (systemId != null)
		        {
		            administradorCartografiaClient.uploadStyle(actualLayer);
		        }
		        actualLayer.setFeatureCollectionModified(false);
		        if(actualLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl)
		        {
		            SLDStyleImpl sldStyle = (SLDStyleImpl) actualLayer.getStyle(SLDStyle.class);
		            sldStyle.setPermanentChanged(false);
		        }
		    }
		    
		    updateTitleToNoModified(saveActualMap.getLayerManager(),saveActualMap.getTaskComponent());
		        
		    return;
		} else
		{			
		    return;
		}		
	}

	/**
     * @throws IOException
     * 
     */
    
	private void updateTitleToNoModified(ILayerManager layerManager,
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
    
    

    public boolean execute(PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);
        String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
        administradorCartografiaClient = new AdministradorCartografiaClient (sUrlPrefix+"AdministradorCartografiaServlet");

        List totalLayers = context.getWorkbenchContext().getTask().getLayerManager().getLayers();
        Iterator totalLayersIter = totalLayers.iterator();
        
        boolean localLayers = false;
        boolean systemLayers = false;
        
        while (totalLayersIter.hasNext() && (localLayers==false || systemLayers==false))
        {
            Layer actualLayer = (Layer) totalLayersIter.next();
            if(actualLayer instanceof GeopistaLayer)
            {
                boolean isLocal = ((GeopistaLayer) actualLayer).isLocal();
                if(!isLocal)
                {
                    systemLayers = true;
                    
                }
                else
                {
                    localLayers = true;
                }
                
            }
            else
            {
                localLayers = true;
            }

        }
        
        if(systemLayers)
        {
            
                Object[] options = { aplicacion.getI18nString("EIELSaveMapPlugIn.CrearActualizarBaseDatos"),
                        aplicacion.getI18nString("EIELSaveMapPlugIn.GrabarLocal"),aplicacion.getI18nString("EIELSaveMapPlugIn.CancelarGrabacion") };
                int confirmResult = JOptionPane.showOptionDialog(
                        (Component) aplicacion.getMainFrame(), aplicacion
                                .getI18nString("EIELSaveMapPlugIn.SoloCapasSistema"), null,
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        options, options[0]);
                if(confirmResult==CANCELARGRABACION) return false;
                if(confirmResult==GRABARENBASEDATOS)
                {
                    if(localLayers)
                    {
                        Object[] optionsBaseDatosCapasLocales = { aplicacion.getI18nString("EIELSaveMapPlugIn.IgnorarPerderCapasLocales"),
                                aplicacion.getI18nString("EIELSaveMapPlugIn.GrabarLocal"),aplicacion.getI18nString("EIELSaveMapPlugIn.CancelarGrabacion") };
                        confirmResult = JOptionPane.showOptionDialog(
                                (Component) aplicacion.getMainFrame(), aplicacion
                                        .getI18nString("EIELSaveMapPlugIn.ExistenCapasLocales"), null,
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                optionsBaseDatosCapasLocales, optionsBaseDatosCapasLocales[0]);
                        if(confirmResult==CANCELARGRABACION) return false;
                        if(confirmResult==GRABARENBASEDATOS)
                        {
                            localSave = false;
                        }
                        else
                        {
                            localSave = true;
                        }
                    }
                    else
                    {
                        localSave = false;
                    }
                }
                	else localSave = true;
          
        }
        else
        {
            localSave = localLayers;
        }
        
        if (!((GeopistaMap)context.getWorkbenchContext().getTask()).isExtracted())
        {
            oldName = ((GeopistaMap)context.getWorkbenchContext().getTask()).getName();
            GeopistaMapPropertiesDialog propertiesMapDialog = new GeopistaMapPropertiesDialog(context);
            if(!propertiesMapDialog.wasOKPressed()) return false;
        }
        
        if (systemLayers)
        {
            if (!aplicacion.isLogged())
            {

                aplicacion.setProfile("Geopista");
                aplicacion.login();
            }
            if (aplicacion.isLogged())
            {
                return true;
            } else
                return false;
        }
        return true;
    }

    public void initialize(PlugInContext context) throws Exception
    {
    	String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent())
                .getToolBar(pluginCategory).addPlugIn(getIcon(), this,
                        createEnableCheck(context.getWorkbenchContext()),
                        context.getWorkbenchContext());
        
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addMainMenuItem(this, GeopistaFunctionUtils.i18n_getname("File"),
        		GeopistaFunctionUtils.i18n_getname(this.getName()), null,
                EIELSaveMapPlugIn.createEnableCheck(context.getWorkbenchContext()));

    }
    
    
    //  Borra un directorio completo
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            
            //Borra todos los ficheros del directorio
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        //Ahora que el directorio está vacío, se puede borrar
        return dir.delete();
    }   

    public ImageIcon getIcon()
    {
        return ICON;
    }
}