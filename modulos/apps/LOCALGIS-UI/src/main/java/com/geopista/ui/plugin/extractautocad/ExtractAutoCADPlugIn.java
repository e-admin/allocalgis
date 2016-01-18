/**
 * ExtractAutoCADPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.extractautocad;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.ui.GeopistaTaskFrame;
import com.geopista.ui.plugin.GeopistaAbstractSaveMapPlugIn;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.extractautocad.images.IconLoader;
import com.geopista.ui.plugin.io.dxf.core.jump.io.DxfWriter;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.DataSource;
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

public class ExtractAutoCADPlugIn extends GeopistaAbstractSaveMapPlugIn
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ExtractAutoCADPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private Blackboard blackboard = aplicacion.getBlackboard();
    
    public static final String ALLMAP = "All Map";

    public static final String SHOWNINSCREEN = "Shown in Screen";

    public static final String BOOKMARKS = "Bookmark";

    public static final String EXTRACTZONE = "Capture Zone";

    public static final String EXTRACTLAYERS = "Extract Layers";
    
    public static final String BOOKMARKEXTRACTENVELOPE = "Bookmark Extract Envelope";
    
    public static final String DIRAUTOCAD = "Autocad";
    
    public static final String DIRTOEXTRACT = "dirToExtract";
     
    
    private JFileChooser fileChooser;  
    
    public ExtractAutoCADPlugIn(){
    	
    	Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.extractautocad.languages.ExtractAutoCADPlugIni18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("ExtractAutoCADPlugIn",bundle2);
    }

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
    	String name = I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD");
        return name;
    }

    public boolean execute(PlugInContext context) throws Exception
    { 
    	boolean devolver = false;
    	int resp=getFileChooser().showOpenDialog(aplicacion.getMainFrame());
		File selFil= getFileChooser().getSelectedFile();
		if (resp==JFileChooser.APPROVE_OPTION && selFil!=null)
		{
		 blackboard.put(DIRTOEXTRACT,selFil.getAbsolutePath());
		 
			reportNothingToUndoYet(context);

	        WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context
	                .getWorkbenchGuiComponent()), I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD"),
	                context.getErrorHandler());
	        
	        d.init(new WizardPanel[] {
	                new ExtractAutoCADPanel1("ExtractAutoCADPanel1", "ExtractAutoCADPanel2", context),
	                new ExtractAutoCADPanel2("ExtractAutoCADPanel2", null, context) });
	                
	        // Set size after #init, because #init calls #pack. [Jon Aquino]
	        d.setSize(700, 650);
	        d.getContentPane().remove(d.getInstructionTextArea());
	        GUIUtil.centreOnWindow(d);
	        d.setVisible(true);
	        if (!d.wasFinishPressed())
	        {
	            return false;
	        }
	        devolver = true;
		}
		
    	
       

        return devolver;

    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
    {

        // Capas a extraer
        ArrayList layersToExtract = (ArrayList) blackboard.get(EXTRACTLAYERS);
        
        //Zona a extraer
        String zoneToExtract = (String) blackboard.get(EXTRACTZONE);        
        String dirToExtract = (String) blackboard.get(DIRTOEXTRACT);        
        
        Iterator layersToExtractIterator = layersToExtract.iterator();
        GeometryFactory factory = new GeometryFactory();
        
        Geometry geometryEnvelope = null;
        Envelope envelopeTotal = new Envelope();
        
        ArrayList extractLayers = new ArrayList();

        try
        {
            while (layersToExtractIterator.hasNext())
            {
                GeopistaLayer currentExtractLayer = (GeopistaLayer) layersToExtractIterator
                        .next();
                if (zoneToExtract.equals(ALLMAP))
                {                     
                     Envelope e = currentExtractLayer.getEnvelope();
                     
                     if (e!=null)
                         envelopeTotal.expandToInclude(e);
                     
                }

                if (zoneToExtract.equals(SHOWNINSCREEN))
                {
                    Envelope e = context.getLayerViewPanel().getViewport().getEnvelopeInModelCoordinates();
                    
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
                            .get(ExtractAutoCADPlugIn.BOOKMARKEXTRACTENVELOPE);
                    
                    if (bookMarkEnvelope!=null)
                    {
                        envelopeTotal.expandToInclude(bookMarkEnvelope);
                        
                        List fc = currentExtractLayer.getFeatureCollectionWrapper().query(bookMarkEnvelope);
                        currentExtractLayer.getFeatureCollectionWrapper().getWrappee().clear();
                        currentExtractLayer.getFeatureCollectionWrapper().getWrappee().addAll(fc);
                    }
                }

                extractLayers.add(currentExtractLayer);

            }
            int thumbSizeX = Integer.parseInt(aplicacion.getString("thumbSizeX"));
            int thumbSizeY = Integer.parseInt(aplicacion.getString("thumbSizeY"));
            WKTWriter wktWriter = new WKTWriter();
            
            //HGH
            geometryEnvelope = factory.toGeometry(envelopeTotal);
            ((GeopistaMap) context.getTask()).setGeometryEnvelope(wktWriter.write(geometryEnvelope));
           
            extractMap(extractLayers, context, thumbSizeX, thumbSizeY, monitor, dirToExtract);
               
        } catch (Exception e)
        {      
            ErrorDialog.show(aplicacion.getMainFrame(), 
            		I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.ProblemsExtractMap"), 
            		I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.ProblemsExtractMap"), StringUtil
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

    private boolean extractMap(ArrayList extractLayers, PlugInContext context, int thumbSizeX, int thumbSizeY,
            TaskMonitor monitor, String dirToExtract) throws IOException
    {
    	
        //String dirBase = UserPreferenceStore.getUserPreference(
          //      AppContext.PREFERENCES_DATA_PATH_KEY, UserPreferenceConstants.DEFAULT_DATA_PATH, true);
       
        //dirBase = dirBase + DIRAUTOCAD + System.getProperties().getProperty("file.separator");
    	String dirBase = dirToExtract;
        String mapName = context.getTask().getName() +"."+AppContext.getIdMunicipio();

        GeopistaMap currentMap = (GeopistaMap) context.getTask();
        GeopistaMap newMap = new GeopistaMap();

        try
        {
            boolean currentLayerIsLocalLayer = false;
            File dirBaseMake = new File(dirBase, mapName);
           
            //Se comprueba si el directorio local ya existe. En ese caso pregunta al usuario si 
            //desea cancelar la operación o sobreescribir el directorio
            if (dirBaseMake.exists())// && !((GeopistaMap)context.getTask()).isExtracted())
            {
                String string1 = I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.OverWrite"); 
                String string2 = I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Cancel"); 
                Object[] options = {string1, string2};
                
                int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                		I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Message.Exist"),
                        "",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
                
                //sobreescribir
                if (n==JOptionPane.YES_OPTION) 
                {
                    deleteDir(dirBaseMake);                    
                }
                //cancelar 
                else
                {
                    return false;
                    
                }
            }            
            
            dirBaseMake.mkdirs();
            
            currentMap.setProjectFile(new File(dirBaseMake, "geopistamap.gpc"));
            currentMap.setSystemMap(false);
            currentMap.setExtractionTimeStamp(new Date());
            currentMap.setSystemId("e"+currentMap.getSystemId());
            
            Iterator layersIter = extractLayers.iterator();
            
            while (layersIter.hasNext())
            {
                Layer currentLayer = (Layer) layersIter.next();
                monitor.report(I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.SaveMapPlugin.SavingLayer")
                        + currentLayer.getName());

                if (currentLayer instanceof GeopistaLayer)
                {
                    currentLayerIsLocalLayer = ((GeopistaLayer) currentLayer).isLocal();
                  
                }

                if (currentLayerIsLocalLayer){
                	
                	String string1 = I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Continue"); 
                    String string2 = I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Cancel"); 
                    Object[] options = {string1, string2};
                    
                    int n = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),
                    		I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.Message.LocalLayer"),
                            "",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
                   
                    if (n==JOptionPane.NO_OPTION) 
                    {
                    	return false;               
                    }
                    
                }
                FeatureSchema featureSchema = currentLayer.getFeatureCollectionWrapper()
                        .getFeatureSchema();

                File saveSchemaLayer = null;
                if(currentLayer instanceof GeopistaLayer){
                	saveSchemaLayer = new File(dirBaseMake, ((GeopistaLayer)currentLayer).getSystemId().toUpperCase() + ".sch");
                }
                else{
                	saveSchemaLayer = new File(dirBaseMake, currentLayer.getName().toUpperCase() + ".sch");
                }
                        
                if (featureSchema instanceof GeopistaSchema)
                {
                    
                    StringWriter stringWriterSch = new StringWriter();
                    featureSchema = currentLayer.getFeatureCollectionWrapper()
                            .getFeatureSchema();
                    try
                    {
                        Java2XML converter = new Java2XML();
                        converter.write(featureSchema, "GeopistaSchema", stringWriterSch);

                        saveSchemaUTF8(stringWriterSch, saveSchemaLayer);

                    } finally
                    {
                        stringWriterSch.flush();
                        stringWriterSch.close();
                    }                   
                    
                }

            }
                       
            Iterator mapLayers = currentMap.getLayerManager().getLayers().iterator();
            
            while (mapLayers.hasNext())
            {
            	Layer currentLayer = (Layer) mapLayers.next();  
            	currentLayer.getDataSourceQuery().setQuery(null);
            	HashMap properties = new HashMap();
            	properties.put(DataSource.FILE_KEY, null);
               
	            properties.put(DataSource.COORDINATE_SYSTEM_KEY, currentMap.getMapProjection());
            	currentLayer.getDataSourceQuery().getDataSource().setProperties(properties);            
            }
            
            currentMap.setTimeStamp(new Date());
            if (currentMap.getDescription() == null)
            {
                currentMap.setDescription("");
            }
              
            if(AppContext.getApplicationContext().getBlackboard().get(Const.KEY_SRID_MAPA) != null){
            	currentMap.setMapSrid((String)AppContext.getApplicationContext().getBlackboard().get(Const.KEY_SRID_MAPA));
            }

            save(currentMap, currentMap.getProjectFile(), context
                    .getWorkbenchGuiComponent());
            
            if (extractLayers.size()>0){

            	for (Iterator lstExtractlayers = extractLayers.iterator(); lstExtractlayers.hasNext();){
            		Layer layer = (Layer)lstExtractlayers.next();
            		if (layer instanceof GeopistaLayer){
            			GeopistaLayer geopistaLayer = (GeopistaLayer)layer;
            			geopistaLayer.setSystemId(geopistaLayer.getSystemId().toUpperCase());
            		}
            		else{
            			layer.setName(layer.getName().toUpperCase());
            		}
            		
            	}
            	monitor.report(I18N.get("ExtractAutoCADPlugIn","ExtractAutoCAD.SaveDxf"));
            	new DxfWriter().writeAutoCAD(extractLayers, dirBaseMake + "\\" + mapName + ".dxf");
            }
            
            currentMap.setSystemMap(true);
            
            try
            {
                monitor.report(I18N.get("ExtractAutoCADPlugIn", 
                		"ExtractAutoCAD.CreatingThumbnail"));

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
            
            try
            {
                context.getActiveInternalFrame().setClosed(true);
            } catch (PropertyVetoException e)
            {
                e.printStackTrace();
            }
            
            return true;
            
        } catch (Exception e)
        {
            ErrorDialog.show(aplicacion.getMainFrame(), 
                    I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.ErrorExtract"), 
                    I18N.get("ExtractAutoCADPlugIn", "ExtractAutoCAD.LayersNotExtract"), StringUtil.stackTrace(e));
                        
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
    
    private JFileChooser getFileChooser() {
	 	 
        if (fileChooser == null) {
        	
        	fileChooser=new JFileChooser();
        	fileChooser.setMultiSelectionEnabled(false);
        	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	 

//        	fileChooser=new GUIUtil.FileChooserWithOverwritePrompting();
//        	fileChooser.setMultiSelectionEnabled(false);
//        	 File currentDirectory = (File) aplicacion.getBlackboard().get( UserPreferenceConstants.DEFAULT_DATA_PATH);
//             fileChooser.setCurrentDirectory(currentDirectory);
//        	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }

        File currentDirectory = new File(UserPreferenceConstants.DEFAULT_DATA_PATH + File.separator + DIRAUTOCAD);
        fileChooser.setCurrentDirectory(currentDirectory);
   	
        return (JFileChooser) fileChooser;
    }

}
