/**
 * ExtractPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.feature.GeopistaSchema;
import com.geopista.io.datasource.GeopistaStandarReaderWriteFileDataSource.GeoGML;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.ui.dialogs.ExtractPanel01;
import com.geopista.ui.dialogs.ExtractPanel02;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class ExtractPlugIn extends GeopistaAbstractSaveMapPlugIn
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ExtractPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private Blackboard blackboard = aplicacion.getBlackboard();
    
    private Geometry geometryToBlock = null;

    public static final String ALLMAP = "All Map";

    public static final String SHOWNINSCREEN = "Shown in Screen";

    public static final String BOOKMARKS = "Bookmark";

    public static final String EXTRACTZONE = "Capture Zone";

    public static final String EXTRACTLAYERS = "Extract Layers";
    
    public static final String BOOKMARKEXTRACTENVELOPE = "Bookmark Extract Envelope";

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
                checkFactory.createWindowWithSystemMapMustBeActiveCheck());
    }

    public String getName()
    {
        return "Extract";
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);

        WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context
                .getWorkbenchGuiComponent()), aplicacion.getI18nString("ExtractDialog"),
                context.getErrorHandler());
        d.init(new WizardPanel[] {
                new ExtractPanel01("ExtractPanel01", "ExtractPanel02", context),
                new ExtractPanel02("ExtractPanel02", null, context) });

        // Set size after #init, because #init calls #pack. [Jon Aquino]
        d.setSize(800, 650);
        GUIUtil.centreOnWindow(d);
        d.setVisible(true);
        if (!d.wasFinishPressed())
        {
            return false;
        }

        return true;

    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {

        // Capas a extraer
        ArrayList layersToExtract = (ArrayList) blackboard.get(EXTRACTLAYERS);
        
        //Zona a extraer
        String zoneToExtract = (String) blackboard.get(EXTRACTZONE);
        
        
        Iterator layersToExtractIterator = layersToExtract.iterator();
        GeometryFactory factory = new GeometryFactory();
        String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
        AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                sUrlPrefix + "/AdministradorCartografiaServlet");
        
        Geometry geometryEnvelope = null;
        Envelope envelopeTotal = new Envelope();
        
        ArrayList successLockLayers = new ArrayList();

        try
        {
            while (layersToExtractIterator.hasNext())
            {
                GeopistaLayer currentExtractLayer = (GeopistaLayer) layersToExtractIterator
                        .next();
                if (zoneToExtract.equals(ALLMAP))
                {                     
                     Envelope e = currentExtractLayer.getEnvelope();
                     geometryToBlock = factory.toGeometry(e);
                     if (e!=null)
                         envelopeTotal.expandToInclude(e);
                     
                }

                if (zoneToExtract.equals(SHOWNINSCREEN))
                {
                    Envelope e = context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
                    geometryToBlock = factory.toGeometry(e);
                    if (e!=null)
                    {
                        envelopeTotal.expandToInclude(e);
                        
                        List fc = currentExtractLayer.getFeatureCollectionWrapper().query(e);
                        currentExtractLayer.getFeatureCollectionWrapper().getWrappee().clear();
                        currentExtractLayer.getFeatureCollectionWrapper().getWrappee().addAll(fc);
                    }
                    
                }

                if (zoneToExtract.equals(BOOKMARKS))
                {
                    Envelope bookMarkEnvelope = (Envelope) blackboard
                            .get(ExtractPlugIn.BOOKMARKEXTRACTENVELOPE);
                    geometryToBlock = factory.toGeometry(bookMarkEnvelope);
                    if (bookMarkEnvelope!=null)
                    {
                        envelopeTotal.expandToInclude(bookMarkEnvelope);
                        
                        List fc = currentExtractLayer.getFeatureCollectionWrapper().query(bookMarkEnvelope);
                        currentExtractLayer.getFeatureCollectionWrapper().getWrappee().clear();
                        currentExtractLayer.getFeatureCollectionWrapper().getWrappee().addAll(fc);
                    }
                }

                administradorCartografiaClient.lockLayer(currentExtractLayer
                        .getSystemId(), geometryToBlock);
                successLockLayers.add(currentExtractLayer);
                currentExtractLayer.setExtracted(true);

            }
            int thumbSizeX = Integer.parseInt(aplicacion.getString("thumbSizeX"));
            int thumbSizeY = Integer.parseInt(aplicacion.getString("thumbSizeY"));
            WKTWriter wktWriter = new WKTWriter();
            
            //HGH
            geometryEnvelope = factory.toGeometry(envelopeTotal);
            ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
            
            if(!extractMap(context, thumbSizeX, thumbSizeY, monitor))
            {
                Iterator unlocklayersIterator = successLockLayers.iterator();
                while (unlocklayersIterator.hasNext())
                {
                    try
                    {
                        GeopistaLayer currentUnlockLayer = (GeopistaLayer) unlocklayersIterator
                                .next();
                        administradorCartografiaClient.unlockLayer(currentUnlockLayer
                                .getSystemId());
                    } catch (Exception e1)
                    {
                        // Si falla algo en el desbloqueo seguimos con el resto de
                        // las capas bloqueadas
                    }
                }
            }
            

        } catch (Exception e)
        {
            // Si se produce algun problema al realizar la extraccion intentamos
            // quitar los bloqueos
            Iterator unlocklayersIterator = successLockLayers.iterator();
            while (unlocklayersIterator.hasNext())
            {
                try
                {
                    GeopistaLayer currentUnlockLayer = (GeopistaLayer) unlocklayersIterator
                            .next();
                    administradorCartografiaClient.unlockLayer(currentUnlockLayer
                            .getSystemId());
                } catch (Exception e1)
                {
                    // Si falla algo en el desbloqueo seguimos con el resto de
                    // las capas bloqueadas
                }

            }
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("ExtractPlugIn.ProblemasExtraerMapa"), aplicacion
                    .getI18nString("ExtractPlugIn.ProblemasExtraerMapa"), StringUtil
                    .stackTrace(e));
        }

        if (context.getActiveInternalFrame() instanceof GeopistaTaskFrame)
        {
            ((GeopistaTaskFrame)context.getActiveInternalFrame()).getLayerViewPanel().getViewport().zoomToFullExtent();    
        }
    }

    public void initialize(PlugInContext context) throws Exception
    {
        FeatureInstaller featureInstaller = new FeatureInstaller(context
                .getWorkbenchContext());

        featureInstaller.addMainMenuItem(this, GeopistaUtil.i18n_getname("File"),
                GeopistaUtil.i18n_getname(this.getName()), null, this.createEnableCheck(context.getWorkbenchContext()));

    }

    public ImageIcon getIcon()
    {
        return IconLoader.icon("ToFront.gif");
    }

    private boolean extractMap(PlugInContext context, int thumbSizeX, int thumbSizeY,
            TaskMonitor monitor) throws IOException
    {
        String dirBase = UserPreferenceStore.getUserPreference(
                UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY, UserPreferenceConstants.DEFAULT_DATA_PATH, true);

        String mapName = context.getTask().getName() +"."+aplicacion.getIdMunicipio();

        GeopistaUtil.makeMapBackup(dirBase, mapName);

        GeopistaMap currentMap = (GeopistaMap) context.getTask();

        try
        {
            boolean currentLayerIsLocalLayer = false;
            File dirBaseMake = new File(dirBase, mapName);
           

            //Se comprueba si el directorio local ya existe. En ese caso pregunta al usuario si 
            //desea cancelar la operación o sobreescribir el directorio
            if (dirBaseMake.exists())// && !((GeopistaMap)context.getTask()).isExtracted())
            {
                String string1 = aplicacion.getI18nString("ExtractMapPlugin.sobreescribir"); 
                String string2 = aplicacion.getI18nString("ExtractMapPlugin.cancelar"); 
                Object[] options = {string1, string2};
                
                int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                        aplicacion.getI18nString("ExtractMapPlugin.mensaje.yaexiste"),
                        "",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
                
                //sobreescribir
                if (n==JOptionPane.YES_OPTION) 
                {
                    deleteDir(dirBaseMake);                    
                }
                //cancelar (lanza excepcion para deshacer el bloqueo de layers)
                else
                {
                    return false;
                    
                }
            }
            
            
            dirBaseMake.mkdirs();

            currentMap.setProjectFile(new File(dirBaseMake, "geopistamap.gpc"));

            currentMap.setSystemMap(false);
            currentMap.setExtracted(true);
            currentMap.setExtractionTimeStamp(new Date());
            currentMap.setSystemId("e"+currentMap.getSystemId());

            List layers = context.getWorkbenchContext().getLayerManager().getLayers();
            Iterator layersIter = layers.iterator();
            while (layersIter.hasNext())
            {
                Layer currentLayer = (Layer) layersIter.next();
                monitor.report(aplicacion.getI18nString("SaveMapPlugin.SavingLayer")
                        + currentLayer.getName());

                if (currentLayer instanceof GeopistaLayer)
                {
                    currentLayerIsLocalLayer = ((GeopistaLayer) currentLayer).isLocal();
                    ((GeopistaLayer) currentLayer)
                            .activateLogger((GeopistaMap) currentMap);
                }

                FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper()
                        .getFeatureSchema();

                File saveSchemaLayer = new File(dirBaseMake, currentLayer.getName()
                        + ".sch");
                if (featureSchema instanceof GeopistaSchema)
                {
                    if (currentLayer instanceof GeopistaLayer
                            && ((GeopistaLayer) currentLayer).isExtracted())
                    {
                        
                        //((GeopistaSchema)featureSchema).setGeopistalayer((GeopistaLayer)currentLayer);
                        
                        //GeopistaSchema newSchema = new GeopistaSchema();

                        /*List allAttributes = ((GeopistaSchema) featureSchema)
                                .getAttributes();
                        Iterator allAttributesIterator = allAttributes.iterator();
                        while (allAttributesIterator.hasNext())
                        {
                            Attribute currentAttribute = (Attribute) allAttributesIterator
                                    .next();
                            newSchema.addAttribute(currentAttribute);

                        }*/

                        /*Attribute extractedIdAttribute = new Attribute();
                        Domain extractedIdDomain = new AutoFieldDomain("AUTOID",
                                "dominio attributo identificador features extraidas");
                        Table extractedIdTable = new Table("id Extracted Table",
                                "attributo identificador features extraidas");
                        Column extractedIdColumn = new Column("idExtracted",
                                "attributo identificador features extraidas",
                                extractedIdTable, extractedIdDomain);
                        extractedIdAttribute.setColumn(extractedIdColumn);
                        extractedIdAttribute.setName("idExtracted");
                        extractedIdAttribute.setType("LONG");
                        extractedIdAttribute.setAccessType("R");
                        ((GeopistaSchema) newSchema).addAttribute(extractedIdAttribute);*/

                        //((GeopistaLayer) currentLayer).changeGeopistaSchema(newSchema);
                    }
                    StringWriter stringWriterSch = new StringWriter();
                    featureSchema = currentLayer.getFeatureCollectionWrapper()
                            .getFeatureSchema();
                    try
                    {
                        Java2XML converter = new Java2XML();
                        converter.write(featureSchema, "GeopistaSchema", stringWriterSch);

                        saveSchema(stringWriterSch, saveSchemaLayer);

                    } finally
                    {
                        stringWriterSch.flush();
                        stringWriterSch.close();
                    }
                }

                
                DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();
                
                if (dataSourceQuery == null || currentLayerIsLocalLayer == false)
                {
                    GeoGML newDatasource = new GeoGML();
                    dataSourceQuery = new DataSourceQuery();
                    
                    File saveFileLayer = new File(dirBaseMake, currentLayer.getName()
                            + ".gml");

                    HashMap properties = new HashMap();
                    properties.put(DataSource.FILE_KEY, saveFileLayer.getAbsolutePath());
                    properties.put(DataSource.COORDINATE_SYSTEM_KEY, context.getTask()
                            .getLayerManager().getCoordinateSystem().getName());
                    properties.put("srid_destino", Integer.valueOf(context.getTask().getLayerManager().getCoordinateSystem().getEPSGCode()));
                                  
                    newDatasource.setProperties(properties);
                    dataSourceQuery.setDataSource(newDatasource);
                    ((GeopistaLayer) currentLayer).setLocal(true);

                }
                Connection connection = dataSourceQuery.getDataSource().getConnection();
                try
                {    
                    connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer.getFeatureCollectionWrapper(), monitor);
                    currentLayer.setDataSourceQuery(dataSourceQuery).setFeatureCollectionModified(false);
                    
                } finally
                {
                    connection.close();
                }

            }// bucle de capas

            monitor.report(aplicacion.getI18nString("SaveMapPlugin.UpdatingMap"));

            currentMap.setTimeStamp(new Date());
            if (currentMap.getDescription() == null)
            {
                currentMap.setDescription("");
            }

            save(currentMap, currentMap.getProjectFile(), context
                    .getWorkbenchGuiComponent());

            try
            {
                monitor.report(aplicacion
                        .getI18nString("SaveMapPlugin.CreatingThumbnail"));

                ILayerViewPanel layerViewPanel = context.getWorkbenchContext()
                        .getLayerViewPanel();
                Image thumbnail = GeopistaFunctionUtils.printMap(thumbSizeX, thumbSizeY,
                        (LayerViewPanel) layerViewPanel);
                File thumbnailFile = new File(dirBaseMake, "thumb.png");
                ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);

            } catch (Exception e)
            {
                logger.error("localSave(PlugInContext, int, int, TaskMonitor)", e);
            }

            // Una vez grabado ponemos el flag isDirty a false para saber que ya
            // no hay cambios sin grabar
            if (context.getWorkbenchContext().getLayerManager() instanceof GeopistaLayerManager)
            {
                ((GeopistaLayerManager) context.getWorkbenchContext().getLayerManager())
                        .setDirty(false);
            }
            
            return true;
            
        } catch (Exception e)
        {
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("ErrorAlGrabarMapa"), aplicacion
                    .getI18nString("ElMapaNoHaPodidoGrabarse"), StringUtil.stackTrace(e));
            monitor.report(aplicacion.getI18nString("RestaurandoCopiaDeSeguridad"));
            GeopistaUtil.restoreBackup(dirBase, mapName);
            
            return false;

        }
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

    
    
    /*public void changeSchema(GeopistaLayer currentLayer)
    {
        for (Iterator i = currentLayer.getFeatureCollectionWrapper().getFeatures()
                .iterator(); i.hasNext();)
        {
            Feature feature = (Feature) i.next();
            tempFeatures.add(convert(feature, newSchema));
        }

        List originalFeatures = getFeatureCollectionWrapper().getFeatures();
        // Phase 2: commit. [Jon Aquino]
        for (int i = 0; i < originalFeatures.size(); i++)
        {
            Feature originalFeature = (Feature) originalFeatures.get(i);
            Feature tempFeature = (Feature) tempFeatures.get(i);

            // Modify existing features rather than creating new features,
            // because
            // there may be references to the existing features (e.g. Attribute
            // Viewers).
            // [Jon Aquino]
            originalFeature.setSchema(tempFeature.getSchema());
            originalFeature.setAttributes(tempFeature.getAttributes());
        }
    }*/

}
