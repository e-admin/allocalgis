/**
 * GeopistaSaveMapPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;

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
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
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
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.I18NUtils;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.feature.Feature;
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

public class GeopistaSaveMapPlugIn extends GeopistaAbstractSaveMapPlugIn
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(GeopistaSaveMapPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private String toolBarCategory = "GeopistaSaveMapPlugIn.category";

    public static final ImageIcon ICON = IconLoader.icon("Folder.gif");
    
    private boolean localSave = false;
    private static final int GRABARENBASEDATOS = 0; 
    private static final int GRABARENLOCAL = 1;
    private static final int CANCELARGRABACION = 2;
    
    private String oldName = new String();
    
    
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
        return "Save Map";
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
		String dirBase = UserPreferenceStore.getUserPreference(
		        UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY, UserPreferenceConstants.DEFAULT_DATA_PATH,
		        true);
		logger.info("Directorio donde se almacenara el mapa:"+dirBase);
		String mapName = context.getTask().getName();

	   String mapNameForBackup="";
	   if (context.getTask() instanceof GeopistaMap)
	    {
		   mapNameForBackup = mapName+"."+AppContext.getIdEntidad();	        
	        
	    } 
	   logger.info("Mapa Backup:"+mapNameForBackup);
	   boolean existeMapa=GeopistaFunctionUtils.existeMapa(dirBase,mapNameForBackup);
	   logger.info("Existe backup de mapa:"+existeMapa);   
	   
	   GeopistaFunctionUtils.makeMapBackup(dirBase, mapNameForBackup);

		try
		{
		    boolean currentLayerIsLocalLayer = false;
		    File dirBaseMake = null;
            
		    if (context.getTask() instanceof GeopistaMap)
		    {
		        dirBaseMake = new File(dirBase, mapName+"."+AppContext.getIdEntidad());	        
		        
		    }           
            
            //Se comprueba si el directorio local ya existe. En ese caso pregunta al usuario si 
            //desea conservarlo o sobreescribirlo
            while (dirBaseMake.exists() && !((GeopistaMap)context.getTask()).isExtracted())
            {
                String string1 = aplicacion.getI18nString("SaveMapPlugin.otronombre"); 
                String string2 = aplicacion.getI18nString("SaveMapPlugin.sobreescribir"); 
                String string3 = aplicacion.getI18nString("GeopistaSaveMapPlugIn.CancelarGrabacion"); 
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
                    dirBaseMake = new File(dirBase, nombreMapa+"."+AppContext.getIdEntidad());
                    
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
                    
		            //Quitar el comentario si se quiere guardar copia de los ficheros incluidos en el mapa
                    //currentLayer.setDataSourceQuery(null);
		            
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
		            properties.put("srid_destino", Integer.valueOf(context.getTask().getLayerManager().getCoordinateSystem().getEPSGCode()));
		            newDatasource.setProperties(properties);
		            dataSourceQuery.setDataSource(newDatasource);

		        }
		        //Si se cambia el nombre del mapa tambien hay que cambiar la localizacion del fichero gml
		        else if (currentLayerIsLocalLayer){
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
			            properties.put("srid_destino", Integer.valueOf(context.getTask().getLayerManager().getCoordinateSystem().getEPSGCode()));
			            newDatasource.setProperties(properties);
			            dataSourceQuery.setDataSource(newDatasource);
		        }
                
		        Connection connection = dataSourceQuery.getDataSource()
		                .getConnection();
                

		        try
		        {
		        	//if (true)
		        	//	throw new Exception();
		            connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer
		                    .getFeatureCollectionWrapper(), monitor);
		            currentLayer.setDataSourceQuery(dataSourceQuery)
		                    .setFeatureCollectionModified(false);
		        } finally
		        {
		            connection.close();
		        }
		        
		        ((GeopistaLayer)currentLayer).setLocal(currentLayerIsLocalLayer);  

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
		                (LayerViewPanel) layerViewPanel);
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
            
		} catch (Exception e)
		{
		    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
		            .getI18nString("ErrorAlGrabarMapa"), aplicacion
		            .getI18nString("ElMapaNoHaPodidoGrabarse"), StringUtil
		            .stackTrace(e));
		    monitor.report(aplicacion.getI18nString("RestaurandoCopiaDeSeguridad"));
		    if (existeMapa)
		    	GeopistaFunctionUtils.restoreBackup(dirBase, mapNameForBackup);
		    

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
		    ILayerViewPanel layerViewPanel = context.getWorkbenchContext()
		            .getLayerViewPanel();
		    monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));
		    Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
		            (LayerViewPanel) layerViewPanel);
		    GeopistaMap saveActualMap = (GeopistaMap) context.getTask();

		    //comprobamos si el mapa es local y se va a grabar en la base de datos y en ese caso
		    //borramos el systemId para que el administrador de cartografia sepa que es nuevo
		    if(!saveActualMap.isSystemMap())
		    {
		        saveActualMap.setSystemId("");
		        saveActualMap.setSystemMap(true);
		    }
		    
		    //JARC Para poder guardar los mapas desde los modulos es necesario indicar la entidad seleccionada
		    if(saveActualMap.getIdEntidadSeleccionada() == null)
		    	saveActualMap.setIdEntidadSeleccionada(String.valueOf(saveActualMap.getIdEntidad()));
		    
		    saveActualMap.setThumbnail(thumbnail);
		    saveActualMap.getLayerManager().setCoordinateSystem(saveActualMap.getLayerManager().getCoordinateSystem());
		    saveActualMap.setMapProjection(saveActualMap.getLayerManager().getCoordinateSystem().getName());
		    administradorCartografiaClient.saveMap(saveActualMap, UserPreferenceStore
		            .getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES",
		                    true));

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
		        //reproject(actualLayer, PredefinedCoordinateSystems.GEOGRAPHICS_ED50, context);
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
	
	
    private void reproject(Layer layer, CoordinateSystem coordinateSystem, PlugInContext context) {
        try {
            Assert.isTrue( ((GeopistaLayerManager)((GeopistaMap) context.getTask()).getLayerManager()).getLayers().indexOf(layer) == -1,
                "If the LayerManager contained this layer, we'd need to be concerned about rolling back on an error [Jon Aquino]");

            if (!Reprojector.instance().wouldChangeValues(layer.getFeatureCollectionWrapper()
                                                                         .getFeatureSchema()
                                                                         .getCoordinateSystem(),
                        coordinateSystem)) {
                return;
            }

            for (Iterator i = layer.getFeatureCollectionWrapper().iterator();
                    i.hasNext();) {
                Feature feature = (Feature) i.next();
                Reprojector.instance().reproject(feature.getGeometry(),
                    layer.getFeatureCollectionWrapper().getFeatureSchema()
                         .getCoordinateSystem(), coordinateSystem);
            }
        } finally {
            //Even if #isReprojectionNecessary returned false, we still need to set
            //the CoordinateSystem to the new value [Jon Aquino]
            layer.getFeatureCollectionWrapper().getFeatureSchema()
                 .setCoordinateSystem(coordinateSystem);
        }
    }

    
    

	/**
     * @throws IOException
     * 
     */
    
	private void updateTitleToNoModified(ILayerManager iLayerManager,
            TaskComponent taskFrame)
    {

        if (iLayerManager instanceof GeopistaLayerManager)
        {
            ((GeopistaLayerManager) iLayerManager).setDirty(false);
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

        String sUrlPrefix = aplicacion.getString(Constantes.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA);
        administradorCartografiaClient = new AdministradorCartografiaClient(sUrlPrefix);


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
            
                Object[] options = { aplicacion.getI18nString("GeopistaSaveMapPlugIn.CrearActualizarBaseDatos"),
                        aplicacion.getI18nString("GeopistaSaveMapPlugIn.GrabarLocal"),aplicacion.getI18nString("GeopistaSaveMapPlugIn.CancelarGrabacion") };
                int confirmResult = JOptionPane.showOptionDialog(
                        (Component) aplicacion.getMainFrame(), aplicacion
                                .getI18nString("GeopistaSaveMapPlugIn.SoloCapasSistema"), null,
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        options, options[0]);
                if(confirmResult==CANCELARGRABACION) return false;
                if(confirmResult==GRABARENBASEDATOS)
                {
                    if(localLayers)
                    {
                        Object[] optionsBaseDatosCapasLocales = { aplicacion.getI18nString("GeopistaSaveMapPlugIn.IgnorarPerderCapasLocales"),
                                aplicacion.getI18nString("GeopistaSaveMapPlugIn.GrabarLocal"),aplicacion.getI18nString("GeopistaSaveMapPlugIn.CancelarGrabacion") };
                        confirmResult = JOptionPane.showOptionDialog(
                                (Component) aplicacion.getMainFrame(), aplicacion
                                        .getI18nString("GeopistaSaveMapPlugIn.ExistenCapasLocales"), null,
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

        featureInstaller.addMainMenuItem(this, I18NUtils.i18n_getname("File"),
                I18NUtils.i18n_getname(this.getName()), null,
                GeopistaSaveMapPlugIn.createEnableCheck(context.getWorkbenchContext()));

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
